# `throw와 throws의 차이점`

- `throw : 에러를 고의로 발생시킬 때 사용합니다.`
- `throws : 자신을 호출한 상위 메소드로 에러를 던지는 역할을 합니다.`

<br>

## `throw란?`

예제 코드를 먼저 보겠습니다. 

```java
public class File {
    public File(String pathname) {
        if (pathname == null) {
            throw new NullPointerException();
        }
        this.path = fs.normalize(pathname);
        this.prefixLength = fs.prefixLength(this.path);
    }
}
```

File 클래스의 생성자를 보면 위와 같이 경로가 null 일 때 `throw`를 통해서 강제로 에러를 발생하는 것을 볼 수 있습니다.

위와 같이 throw를 통해서 에러를 강제로 만들었을 때 내부적으로 try/catch로 해결하기 않으면 throws를 사용하여 호출한 곳으로 에러를 던지게 됩니다. 

<br>

## `throws란?`

```java
public class FileOutputStream {
    public FileOutputStream(String name) throws FileNotFoundException {
        this(name != null ? new File(name) : null, false);
    }
}
```

FileOutputStream 클래스의 생성자를 보면 위와 같이 `throws FileNotFoundException`을 볼 수 있습니다. 

이렇게 메소드나 생성자 선언부에 예외를 선언함으로써 메소드를 사용하려는 사람이 메소드의 선언부를 보았을 때, `이 메소드를 사용하기 위해서는 어떠한 예외들이 처리되어져야 하는지 쉽게 알 수 있습니다.`

자바에서는 메소드를 작성할 때 메소드 내에서 발생한 가능성이 있는 예외를 메소드의 선언부에 명시하여 이 메소드를 사용하는 쪽에서는 이에 대한 처리를 하도록 강요합니다. 

```java
public class Test {
    public static void main(String[] args) throws Exception {
        Test test = new Test();
        test.throwException(13);
    }

    public void throwException(int number) throws Exception {
        if (number > 12) {
            throw new Exception("Number is over than 12");
        }
        System.out.println("Number is + " + number);
    }
}
```

number가 13 이상이면 throw를 통해서 에러를 발생시키는 것을 볼 수 있습니다. throwException 메소드 내부에서 try/catch로 에러처리를 하지 않으면 컴파일에서 throws를 통해서 에러를 던지도록 요구합니다. 

따라서 위의 코드를 실행하면 main 메소드에서도 try/catch로 에러를 처리하지 않을거면 throws를 통해서 에러를 던져야 합니다. 

```java
public class Test {
    public static void main(String[] args) throws Exception {
        Test test = new Test();
        test.throwException(13);
    }

    public void throwException(int number) {
        try {
            if (number > 12) {
                throw new Exception("Number is over than 12");
            }
            System.out.println("Number is + " + number);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

에러를 던지지 않고 내부적으로 해결하고 싶다면 위와 같이 `try/catch`를 통해서 해결하면 됩니다. 

> 예외를 메소드의 throws에 명시하는 것은 예외를 처리하는 것이 아니라, 
> 자신을 호출한 메소드에게 예외를 전달하여 예외처리를 떠맡기는 것이다.
>
> 제일 마지막에 있는 main 메소드에서도 예외가 처리되지 않으면, main 메소드마저 종료되어 프로그램이 전체가 종료된다. 

- main 메소드가 최종 문인거 같은데.. 그래서 반드시 에러 처리를 해야 하는 곳일 거 같은데 여기서 throws를 사용 가능하게 만든 이유가 무엇일까??