# `아이템8 : finalizer와 cleaner 사용을 피하라`

자바는 두 가지 객체 소멸자를 제공합니다. 바로 `finalizer`와 `cleaner` 입니다. 하나씩 어떤 것인지 알아보겠습니다. 

<br>

## `finalizer란?`

```java
public class Object {
   protected void finalize() throws Throwable { }
}
```

`Object의 메소드 중에 하나인 finalizer에 대해 알아보겠습니다. finalizer는 예측할 수 없고, 상황에 따라 위험할 수 있어 일반적으로 불필요합니다.` 오동작, 낮은 성능, 이식성 문제의 원인이 되기도 합니다. 
finalizer는 나름의 쓰임새가 있지만 기본적으로 `쓰지 말아야` 합니다. 

간단한 코드로 예를 들어보겠습니다. 

```java
public class FinalizerExample {

    @Override
    protected void finalize() throws Throwable {
        System.out.println("Clean Object");
    }

    public void hello() {
        System.out.println("Hello Gyunny");
    }
}
```

위와 같이 `finalizer` 클래스를 오버라이딩 하였습니다. 

```java
public class Test {
    public static void main(String[] args) throws InterruptedException {
        Test test = new Test();
        test.run();
        Thread.sleep(1000);
    }

    private void run() {
        FinalizerExample finalizerExample = new FinalizerExample();
        finalizerExample.hello();
    }
}
```

그리고 `run` 메소드 내부의 스코프를 가지는 객체를 만들었습니다. 그러면 run 메소드가 끝났을 때 finalizer가 객체를 회수해 가기를 원합니다. 
하지만 코드를 실행해보면 오버라이딩 한 finalizer 메소드는 실행되지 않습니다. `이렇게 언제 finalizer가 실행될 지 예측할 수가 없습니다.`

그래서 자바 9에서는 finalizer를 사용 자제(deprecated) API로 지정하고 `cleaner`를 그 대안으로 소개 했습니다. 

<br>

### `cleaner란?`

`cleaner는 finalizer 보다는 덜 위험하지만, 여전히 에측할 수 없고, 느리고, 일반적으로 불필요합니다.`

<br>

### `참고하기`

C++ 에서는 `파괴자(destructor)`라는 개념이 있습니다. C++에서의 파괴자는 특정 객체와 관련된 자원을 회수하는 보편적인 방법입니다. 
`하지만 자바에서는 접근할 수 없게 된 객체를 회수하는 역할을 가비지 컬렉터가 담당하기 때문에, 프로그래머는 아무런 작업을 하지 않아도 됩니다.`
자바에서 자원을 회수할 때는 `try-with-resource` or `try-finally`를 사용해 해결합니다. 

<br>

## `사용을 지양해야 하는 이유`

- ### 실행 시점을 보장할 수 없습니다. 
    - finalizer와 cleaner는 언제 실행될 지 보장할 수 없습니다. 이것은 가비지 컬렉터 알고리즘에 따라 다르기 때문입니다. 
    
- ### 실행 조차 안될 수 있습니다. 
    - 즉시 실행이 안된다는 건 그렇다 쳐도 파일 닫기를 제대로 닫아주지 않으면 프로그램이 비정상적으로 종료될 수가 있습니다. 
    
- ### 인스턴스의 자원 회사가 제멋대로 지연될 수 있습니다.
    - 만약 finalizer 스레드가 다른 애플리케이션 스레드보다 우선 순위가 낮다면 실행될 기회를 얻지 못하고, 그러다 보면 계속 객체가 쌓여서 `OutOfMemory`와 같은 오류를 발생시킬 수 있습니다.
   
- ### finalizer와 cleaner는 심각한 성능 문제도 동반합니다. 
    - 이펙티브 자바 필자의 컴퓨터에서 `AutoCloseable` 객체를 생성하고 가비지 컬렉터가 수거하기 까지 12ns가 걸렸지만, finalizer를 사용했을 때는 550ns가 걸렸다고 합니다. 
   
- ### finalizer를 사용한 클래스는 finalizer 공격에 노출되어 심각한 보안 문제를 일으킬 수도 있다.
    - 생성자에서 예외가 발생하면, 이 생성되다 만 객체에서 하위 클래스의 finalizer를 호출하는 심각한 상황이 발생할 수 있습니다. 
    객체 생성을 막으려고 생성자에서 예외를 던지는 것만으로 충분하지만, finalizer가 있다면 그렇지 않습니다. 
    - final이 붙은 클래스는 상속할 수 없기 때문에 이 공격에서 안전합니다. 
    
<br>

## `그러면 finalier와 cleaner는 어디에 사용하는 것이고 왜 사용할까요?`

- 첫 번째로 close 메소드를 호출하지 않는 것에 대비한 안전망 역할입니다. `finalizer`와 `cleaner`가 즉시 호출된다는 보장은 없지만, 클라이언트가 하지 않은 자원 회수를 늦게라도 해주는 것이 안하는 것보다는 낫습니다. 
- 두 번째로 가비지 컬렉터(Garbage Collector)가 회수하지 못하는 네이티브(native) 자원의 정리에 사용합니다. 자바 객체가 아니므로 가비지 컬렉터가 관리하는 대상이 아니기 때문입니다. finalizer를 명시적으로 호출함으로 자원을 회수할 수 있습니다.
        
<br>

## `핵심 정리`

> cleaner나 finalizer는 안전망 역할이나 중요하지 않은 네이티브 자원 회수용으로만 사용하자. 물론 이런 경우라도 불확실성과 성능 저하에 주의해야 한다. 
> 자원 반납이 필요할 때는 try-with-resources를 사용하고 close 메소드를 구현하는 것이 좋다. 





 