## `사용자 정의 예외 만들기`

기존의 정의된 예외 클래스 외에 필요에 따라 개발자가 새로운 예외 클래스를 정의하여 사용할 수 있습니다.  

![test](http://www.nextree.co.kr/content/images/2016/09/Exception-Class.png)

자바에서 에러 처리하는 클래스들의 계층 구조는 위와 같습니다. `Throwable`의 하위 클래스로 `Error`, `Exception` 클래스가 있는 것을 볼 수 있습니다. 

Error와 관련된 클래스는 개발자가 건드릴 필요도 없고, 건드려서도 안됩니다. 하지만, 예외(Exception)를 처리하는 클래스는 개발자가 임의로 추가해서 만들 수 있습니다. 
그러기 위해서는 `Throwable` 클래스나 `Exception` 클래스를 상속받으면 되지만 일반적으로 `Exception` 클래스를 상속 받아 사용합니다. 

<br>

## `예제 코드`

```java
public class MyException extends Exception {
    public MyException() {
        super();
    }

    public MyException(String message) {
        super(message);
    }
}
```
```java
public class CustomException {
    public static void main(String[] args) {
        CustomException sample = new CustomException();
        sample.throwMyException(13);
    }

    public void throwMyException(int number) {
        try {
            if (number > 12) {
                throw new MyException("Number is over than 12");
            }
        } catch (MyException e) {
            e.printStackTrace();
        }
    }
}
```

위와 같이 `MyException` 이라는 제가 직접 정의한 예외를 정의하고 예외를 발생시킬 수 있습니다.(Exception 클래스를 extends 한 후에)

<br>

## `자바 예외 처리 전략`

예외 클래스를 위와 같이 직접 커스텀해서 정의할 때 `Exception` 클래스와 `RuntimeException` 클래스 중 어떤 것을 상속 받아야할까요?

이 질문에 대한 답은 상황에 따라 다르지만 아래와 같이 정의할 수 있습니다. 

- `반드시 try/catch로 묶어줄 필요가 있을 경우에만 Exception 클래스를 확장해야 합니다.`
- `일반적으로 실행시 예외를 처리할 수 있는 경우에는 RuntimeException 클래스를 확장하는 것이 좋습니다.`
- `catch문 내에 아무런 작업 없이 공백으로 놔두면 예외 분석이 어려워지므로 꼭 로그 처리와 같은 예외 처리를 해줘야 합니다.`
