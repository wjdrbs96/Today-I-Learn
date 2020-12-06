# `아이템9 : try-finally보다는 try-with-resources를 사용하라`

자바 라이브러리에는 `close()` 메소드를 호출해 직접 닫아줘야 하는 자원이 많습니다. `InputStream`, `OutputStream`, `java.sql.Connection`
등이 좋은 예입니다. 

전통적으로 자원이 제대로 닫힘을 보장하는 수단으로 `try~finally`가 쓰였습니다. 아래의 코드를 보겠습니다. 

```java
public class FirstError extends RuntimeException {
}
```
```java
public class SecondException extends RuntimeException {
}
```
```java
public class MyResource implements AutoCloseable {

    public void doSomething() {
        System.out.println("doing something");
        throw new FirstError();
    }

    @Override
    public void close() throws Exception {
        System.out.println("clean my resource");
        throw new SecondException();
    }
}
```

`MyResouece` 클래스에서에서 doSomething() 메소드에서는 FirstError 에러를 만들고, close() 메소드에서는 SecondException을 발생시키고 있습니다.

```java
public class Test {
    public static void main(String[] args) throws Exception {
        MyResource myResource = new MyResource();
        try {
            myResource.doSomething();
        } finally {
            myResource.close();
        }
        
    }
}
```

자원이 하나일 때 위와 같이 사용하면 그렇게 나쁘지는 않아 보입니다. 그러면 자원을 하나 더 사용해보겠습니다.

```java
public class Test {
    public static void main(String[] args) throws Exception {
        MyResource myResource = new MyResource();
        try {
            myResource.doSomething();
            MyResource myResource1 = null;
            try {
                myResource1 = new MyResource();
                myResource1.doSomething();
            } finally {
                if (myResource1 != null) {
                    myResource1.close();
                }
            }
        } finally {
            myResource.close();
        }

    }
}
```

일단 위와 같이 자원이 고작 2개인데도 코드가 복잡해지고 가독성이 떨어진다는 단점이 있습니다. 

하지만 이거보다 더 큰 단점이 존재합니다. 어떤 문제인지 알아보겠습니다.

```java
public class Test {
    public static void main(String[] args) throws Exception {
        MyResource myResource = new MyResource();
        try {
            myResource.doSomething();
        } finally {
            myResource.close();
        }
    } 
}
```

위의 코드를 실행했을 때 `doSomething()` 메소드에도 throw 에러가 존재하고, `close()` 메소드에도 throw 에러가 존재하기 때문에 에러가 두 곳에서 발생하는 것을 예측할 수 있습니다. 

<br>

### `그러면 실행 결과는 어떻게 출력이 될까요?`

```
doing something
clean my resource
Exception in thread "main" ExampleCode.Testing.SecondException
	at ExampleCode.Testing.MyResource.close(MyResource.java:13)
	at ExampleCode.Testing.Test.main(Test.java:9)
```
 
위와 같이 출력됩니다. 일단 에러를 보면 `myResource.doSomething();` 여기서 첫 번째로 에러가 발생하는데 이것은 결과 값에 출력되지 않는 것을 볼 수 있습니다. 
이렇게 두 번째로 발생한 에러가 첫 번째로 에러를 집어삼켜 버리는 문제가 발생합니다.

<br>

### `이러한 문제를 해결하기 위해 try-with-resources를 사용하면 됩니다.`

`try-with-resources`를 사용하면 `AutoCloseable` 인터페이스를 implesments 해야합니다. 

```java
public interface AutoCloseable {

    void close() throws Exception;
}
```

그리고 위에 보이는 close() 메소드를 오버라이딩 해주면 됩니다. 

```java
public class Test {
    public static void main(String[] args) throws Exception {
        try (MyResource myResource = new MyResource()) {
            myResource.doSomething();
        }
    }
}
```
 
`try-with-resources`를 적용하면 위와 같이 코드가 엄청나게 깔끔해져 가독성이 높아진 것도 바로 느낄 수 있습니다. 그리고 코드를 한번 실행하고 결과를 확인해보겠습니다.

```
doing something
clean my resource
Exception in thread "main" ExampleCode.Testing.FirstError
	at ExampleCode.Testing.MyResource.doSomething(MyResource.java:7)
	at ExampleCode.Testing.Test.main(Test.java:6)
	Suppressed: ExampleCode.Testing.SecondException
		at ExampleCode.Testing.MyResource.close(MyResource.java:13)
		at ExampleCode.Testing.Test.main(Test.java:7)
```

`try-with-resources`를 사용하면 자동으로 close 메소드를 호출해주기 때문에 close 메소드의 에러도 발생한 것을 볼 수 있습니다.
그리고 위와 다르게 에러 2개가 문제 없이 모두 출력된 것을 볼 수 있습니다.

그러면 이렇게 읽기 수월하 뿐만 아니라 나중에 콜스택을 확인하면서 디버깅할 때도 훨씬 수월하게 할 수 있습니다.

<br>

자원을 여러 개 사용해야 한다면 아래와 같이 사용할 수 있습니다.

```java
public class Test {
    public static void main(String[] args) throws Exception {
        try (MyResource myResource = new MyResource();
             MyResource myResource1 = new MyResource()) {
            myResource.doSomething();
            myResource1.doSomething();
        }
    }
}
```

위와 같이 try () 괄호 안에 자원을 나열해주면 됩니다. 

<br>

### try-with-resources 장점

- 읽기 쉽고 문제 진단에 유리합니다.
- catch를 이용해 try문을 중첩하지 않고도 다수의 예외 처리가 가능합니다.
- 숨겨진 예외도 버려지지 않고, suppressed 꼬리표를 달고 출력됩니다.

<br>

## `핵심 정리`

> 꼭 회수해야 하는 자원을 다룰 때는 try-finally 말고, try-with-resources를 사용하자. 예외는 없다. 코드는 더 짧고 분명해지고,
> 만들어지는 예외 정보도 훨씬 유용하다. try-finally로 작성하면 실용적이지 못할 만큼 코드가 지저분해지는 경우라도, try-with-resources로는 정확하고 쉽게 자원을 회수할 수 있다.





 