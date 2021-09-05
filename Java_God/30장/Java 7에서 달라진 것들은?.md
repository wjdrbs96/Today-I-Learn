# `Java 7에서 달라진 것들은?`

JDK 1.7 버전에서 달라진 것에 대해서 정리해보려 합니다. 하나씩 알아보겠습니다. 

<br>

## `달라진 숫자 표현법`

숫자 앞에 `0을 넣어주면 8진수`, `0x를 넣어주면 16진수`로 인식합니다. 그런데 JDK 1.7부터는 `0b`를 숫자 앞에 넣어주면 2진수로 인식하게 됩니다. 

바로 예제를 보겠습니다. 

```java
public class JDK7Numbers {
    public static void main(String[] args) {
        JDK7Numbers jdk7Numbers = new JDK7Numbers();
        jdk7Numbers.jdk6();
    }

    public void jdk6() {
        int decVal = 1106;                  // 10진수
        int octVal = 02122;                 // 8진수 
        int hexVal = 0x452;                 // 16진수
        int binaryVal = 0b10001010010;      // 2진수
        System.out.println(decVal);
        System.out.println(octVal);
        System.out.println(hexVal);
        System.out.println(binaryVal);
    }
}
```
```
1106
1106
1106
1106
```

예제를 보면 쉽게 이해할 수 있을 것입니다. 그리고 하나 더 볼 것이 있습니다. 

보통 돈 단위나 숫자 단위를 표시할 때, 백만의 경우는 1,000,000과 같이 표시를 합니다. 이렇게 ,를 통해서 구분해주면 훨씬 가독성이 좋아집니다. 
이러한 기능을 자바에서도 `_`를 이용해서 제공합니다. 

```java
public class JDK7Numbers {
    public static void main(String[] args) {
        JDK7Numbers jdk7Numbers = new JDK7Numbers();
        jdk7Numbers.jdk7Underscore();
    }

    public void jdk7Underscore() {
        int binaryVal = 0b0100_0101_0010;
        int million = 1_000_000;
        System.out.println(binaryVal);
        System.out.println(million);
    }
}
```
```
1106
1000000
```

위와 같이 2진수와 10진수 모두 _를 사용할 수 있습니다. (숫자 사이에만 사용할 수 있다는 것을 알아두기)

<br>

## `Switch 문장의 확장`

JDK 1.6 까지는 switch-case에서 int만 사용할 수 있었습니다. 하지만 JDK 1.7 부터는 String도 사용할 수 있게 되었습니다. 
바로 예제 코드를 보겠습니다. 

```java
public class JDK7Switch {
    public static void main(String[] args) {
        JDK7Switch jdk7Switch = new JDK7Switch();
        System.out.println(jdk7Switch.salaryIncreaseAmount(3));
    }

    public double salaryIncreaseAmount(int employeeLevel) {
        switch (employeeLevel) {
            case 1:
                return 10.0;
            case 2:
                return 15.0;
            case 3:
                return 100.0;
        }
        return 0.0;
    }
}
```  

이렇게 int 형으로만 사용을 했었는데 7버전 부터는 String도 사용할 수 있습니다. 

```java
public class JDK7Switch {
    public static void main(String[] args) {
        JDK7Switch jdk7Switch = new JDK7Switch();
        System.out.println(jdk7Switch.salaryIncreaseAmount("Manager"));
    }

    public double salaryIncreaseAmount(String employeeLevel) {
        switch (employeeLevel) {
            case "CEO":
                return 10.0;
            case "Manager":
                return 15.0;
            case "Engineer":
                return 100.0;
        }
        return 0.0;
    }
}
``` 

여기서 String 문자열이 null인 경우에는 `NullPointerException`이 발생하니 null 체크를 꼭 확인해야 합니다. 

<br>

## `제네릭 다이아몬드`

```java
public class Test {
    public static void main(String[] args) {
        HashMap<String, Integer> map = new HashMap<String, Integer>();
        List<Integer> list = new ArrayList<Integer>();
    }
}
```

원래는 위와 같이 다이아몬드 양쪽에 모두 타입을 적어줘야 했지만, 이제는 왼쪽 다이아몬드에만 적어주면 됩니다. 

```java
public class Test {
    public static void main(String[] args) {
        HashMap<String, Integer> map = new HashMap<>();
        List<Integer> list = new ArrayList<>();
    }
}
```

이렇게 다이아몬드가 편하긴 하지만 제약이 일부 있습니다. 예제 코드를 보면서 이해해보겠습니다. 

<br>

### `다이어몬드 생성시 유의 사항`

```java
public class GenericClass<X> {
    private X x;
    private Object o;

    public <T> GenericClass(T t) {
        this.o = t;
        System.out.println("T type = " + t.getClass().getName());
    }
    
    public void setValue(X x) {
        this.x = x;
        System.out.println("X Type = " + x.getClass().getName());
    }
}
```
```java
public class TypeInference {
    public static void main(String[] args) {
        TypeInference type = new TypeInference();
        type.makeObjects1();
    }

    public void makeObjects1() {
        GenericClass<Integer> generic1 = new GenericClass<>("String");
        generic1.setValue(999);
    }
}
```
```
T type = java.lang.String
X Type = java.lang.Integer
```

이렇게 `GenericClass`의 객체를 만든다면 T, X 타입이 무엇인지는 쉽게 예측할 수 있습니다. 

```
GenericClass<Integer> generic1 = new <String> GenericClass<>("String");
```

위에서 보았던 예제에서는 왼쪽 다이아몬드에만 타입을 적고, 오른쪽 다이아몬드에는 타입을 적지 않아도 컴파일러 유추해서 채워넣는다 했습니다. 

하지만 지금의 예시에서는 컴파일러 입장에서는 Integer를 넣어야 할 지, String을 넣어야 할 지 유추하기가 애매하기 때문에 컴파일 에러가 발생합니다. 

```
GenericClass<Integer> generic1 = new <String> GenericClass<Integer>("String");
```

따라서 위와 같이 오른쪽 다이아몬드에도 타입을 명시해주어야 컴파일 에러가 발생하지 않습니다. 

<br>

## `Non reifiable varargs 타입`

`reifiable 타입`은 런타임에 타입의 정보가 충분한 타입을 말합니다.

`Non-reifiable 타입`은 타입의 정보가 컴파일타임에서 [type erasure](https://github.com/wjdrbs96/Gyunny-Java-Lab/blob/master/Java_God/21%EC%9E%A5/Type%20erasure%EB%9E%80%3F.md) 의 호출에 의해 정보가 지워진 unbounded wildcard와 같이 정의되지 않은 generic type을 말합니다.

가변인수와 제네릭 타입은 궁합이 좋지 않아서 경고를 발생하는데 이 때 경고를 없앨 수 있는 `@SafeVarargs`라는 어노테이션이 자바 7에 추가되었습니다. 
이 부분에 대한 내용은 아래의 링크를 통해 확인하면 좋을 것 같습니다. 

- [Effective Java item32](https://github.com/wjdrbs96/Today-I-Learn/blob/master/Effective_Java/5%EC%9E%A5/item32.md)


<br>

## `예외도 이렇게 보완되었다.`

```java
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class TryWithResource {
    
    public void scanFile(String fileName, String encoding) {
        Scanner scanner = null;
        try {
            scanner = new Scanner(new File(fileName), encoding);
        } catch (IllegalArgumentException iae) {
            iae.printStackTrace();
        } catch (FileNotFoundException ffe) {
            ffe.printStackTrace();
        } catch (NullPointerException npe) {
            npe.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally{
            scanner.close();
        }
    }
}
```

이렇게 catch문을 계층적으로 작성하면 가독성에도 좋지 않고 코드가 너무 길어진다는 단점이 있습니다. 자바 7부터는 아래와 같이 작성할 수 있게 기능이 추가되었습니다. 

```java
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class TryWithResource {

    public void scanFile(String fileName, String encoding) {
        Scanner scanner = null;
        try {
            scanner = new Scanner(new File(fileName), encoding);
        } catch (IllegalArgumentException | FileNotFoundException | NullPointerException exception) {
            exception.printStackTrace();
        } finally {
            scanner.close();
        }
    }
}
```

이렇게 코드를 줄일 수 있고 가독성을 높일 수 있습니다. 그리고 finally를 통해서 매번 close 메소드로 리소스를 닫아줘야 했습니다. 

하지만 JDK 1.7에는 `AutoCloseable`이라는 인터페이스가 추가되어 이를 구현하면 자동으로 close 메소드를 호출해줍니다. 

```java
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class TryWithResource {

    public void scanFile(String fileName, String encoding) {
        try (Scanner scanner = new Scanner(new File(fileName), encoding)) {
            System.out.println(scanner.nextLine());
        } catch (IllegalArgumentException | FileNotFoundException | NullPointerException exception) {
            exception.printStackTrace();
        }
    }
}
```

사용법은 위와 같습니다. `try-with-resource`가 궁금하다면 아래에서 확인해볼 수 있습니다. 

- [try-with-resource란](https://github.com/wjdrbs96/Today-I-Learn/blob/master/Java/Exception%20handling/Try~with~resources.md)
- [이펙티브 자바: try-with-resource](https://github.com/wjdrbs96/Today-I-Learn/blob/master/Effective_Java/2%EC%9E%A5/item9.md)

