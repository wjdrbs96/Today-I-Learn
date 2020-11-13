# try-finally보다는 try-with-resources를 사용하라

자바 라이브러리에는 close 메소드를 호출해 직접 닫아줘야 하는 자원이 많다. InputStream, OutputStream, java.sql.Connection 등이 좋은 예다. 
자원 닫기는 클라이언트가 놓치기 쉬워서 예측할 수 없는 성능 문제로 이어지기도 한다. 자원 닫기는 클라이언트가 놓치기 쉬워서 예측할 수 없는 성능 문제로 이어지기도 한다. 

<br>

이런 자원 중 상당수가 안전망으로 finalizer를 활용하고는 있지만 finalizer는 그리 믿을만하지 못하다. 
전통적으로 자원이 제대로 닫힘을 보장하는 수단으로 try-finally가 쓰였다. 

```java
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Test {
    static String firstLineOfFile(String path) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(path));
        try {
            return br.readLine();
        } finally {
            br.close();
        }
    }
}
```

위의 코드는 나쁘지 않지만, 자원을 하나 더 사용한다면 어떨까?

<br>

```java
import java.io.*;

public class Test {
    private static final int BUFFER_SIZE = 10;
    static void copy(String src, String dst) throws IOException {
        InputStream in = new FileInputStream(src);
        try {
            OutputStream out = new FileOutputStream(dst);
            try {
                byte[] buf = new byte[BUFFER_SIZE];
                int n;
                while ((n = in.read(buf)) >= 0) {
                    out.write(buf, 0, n);
                }
            } finally {
                out.close();
            }
        } finally {
            in.close();
        }
    }
}
```
자원이 둘 이상이면 try-finally 방식은 너무 지저분하다. 그리고 try-finally 블록 모두에서 발생할 수 있는데, 예컨대 기기에 물리적인 문제가 생긴다면 firstLineOfFile 메소드 안의
readLine 메소드가 예외를 던지고, 같은 이유로 close 메소드도 실패할 것이다.

<br>

이러한 문제들은 자바 7에서 나온 `try-with-resources` 덕에 모두 해결되었다. 이 구조를 사용하려면 해당 자원이 AutoCloseable 인터페이스를 구현해야 한다.
단순히 void를 반환하는 close 메소드 하나만 덩그러니 정의한 인터페이스이다. 

<br>

자바 라이브러리와 서드파티 라이브러리들의 수많은 클래스와 인터페이스가 이미 AutoCloseable을 구현하거나 확정해뒀다. 닫아야하는 자원을 뜻하는 클래스를 작성한다면 AutoCloseable을 반드시 구현하기 바란다. 

<br>

```java
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Test {
    static String firstLineOfFile(String path) throws IOException {
        try (BufferedReader br = new BufferedReader(
                new FileReader(path))) {
            return br.readLine();
        }
    }
}
``` 

위의 코드는 `try-with-resources`를 적용한 모습이다. 훨씬 깔끔해진 것을 알 수 있다. 

<br>

```java
import java.io.*;

public class Test2 {
    private static final int BUFFER_SIZE = 10;
    static void copy(String src, String dst) throws IOException {
        try (InputStream in = new FileInputStream(src);
             OutputStream out = new FileOutputStream(dst)) {
            byte[] buf = new byte[BUFFER_SIZE];
            int n;
            while ((n = in.read(buf)) >= 0) {
                out.write(buf, 0, n);
            }
        }
    }
}
```

`try-with-resources` 버전이 짧고 읽기 수월할 뿐 아니라 문제를 파악하기도 훨씬 좋다. firstLineOfFile 메소드를 생각해보자. 
readLine과 clase 호출 양쪽에서 예외가 발생하면, close에서 발생한 예외는 숨겨지고 readLine에서 발생한 예외가 기록된다. 이처럼 실전에서는 프로그래머에게 보여줄 예외 하나만
보존되고 여러 개의 다른 예외가 숨겨질 수도 있다. 

<br>

이렇게 숨겨진 예외들도 그냥 버려지지는 않고, 스택 추적 내역에 '숨겨졌다'는 꼬리표를 달고 출력된다. 자바 7에서 Throwable에 추가된 getSuppressed 메소드를 이용하면 프로그램 코드에서 가져올 수도 있다.
보통의 try-finally에서처럼 `try-with-resources`에서도 catch 절을 쓸 수 있다. 
catch절 덕분에 try 문을 더 중첩하지 않고도 다수의 예외를 처리할 수 있다. 

<br>

다음 코드에서는 firstLineOfFile 메소드를 살짝 수정하여 파일을 열거나 데이터를 읽지 못했을 때 예외를 던지는 대신 기본값을 반환하도록 해봤다. 

```java
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Test {
    static String copy(String path, String defaultVal) throws IOException {
        try (BufferedReader br = new BufferedReader(
                new FileReader(path))) {
            return br.readLine();
        } catch (IOException e) {
            return defaultVal;
        }
    }
}
```

<br>

## 핵심 정리

> 꼭 회수해야 하는 자원을 다룰 때는 try-finally 말고, try-with-resources를 사용하자. 예외는 없다. 코드는 더 짧고 분명해지고, 만들어지는 예외 정보도 훨씬 유용하다. 
> try-finally로 작성하면 실용적이지 못할 만큼 코드가 지저분해지는 경우라도, try-with-resources로는 정확하고 쉽게 자원을 회수할 수 있다. 



