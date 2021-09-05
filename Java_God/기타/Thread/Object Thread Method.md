# `Thread 관련 메소드 정리`

```java
public class Object {

    @HotSpotIntrinsicCandidate
    public final native void notify();

    @HotSpotIntrinsicCandidate
    public final native void notifyAll();

    public final void wait() throws InterruptedException {
        wait(0L);
    }

    public final native void wait(long timeoutMillis) throws InterruptedException;

    public final void wait(long timeoutMillis, int nanos) throws InterruptedException {
        if (timeoutMillis < 0) {
            throw new IllegalArgumentException("timeoutMillis value is negative");
        }

        if (nanos < 0 || nanos > 999999) {
            throw new IllegalArgumentException(
                                "nanosecond timeout value out of range");
        }

        if (nanos > 0) {
            timeoutMillis++;
        }

        wait(timeoutMillis);
    }
}
```

Object 클래스를 보면 `notify()`, `notifyAll()`, `waite()` 메소드가 존재합니다. 이러한 메소드는 쓰레드를 사용할 때 사용하는 메소드들인데 어떤 역할을 하고 있는지 알아보겠습니다. 

<br>

## `wait(), notify()란?`

자바는 `synchronized` 키워드를 이용해서 동기화를 할 수 있습니다. 하지만 하나의 쓰레드가 너무 오래동안 lock을 가진 상태로 오랜 시간을 보내지 않도록 하는 것도 중요합니다. 

즉, 하나의 쓰레드가 lock을 오래쥐고 있으면 다른 쓰레드들은 그만큼 기다려야 하기 때문에 병목현상이 생긴다는 단점이 있습니다. 그래서 나온 것이 `wait()`, `notify()` 입니다. 

![thread](https://t1.daumcdn.net/cfile/tistory/99E341435DC42E4E33)

쓰레드의 생명주기는 위와 같습니다. `Running` 일 때 `wait()`를 만나면 `waiting`으로 이동합니다. 즉, 쓰레드가 lock을 반납하고 기다리게 합니다. 
그러면 다른 쓰레드가 lock을 얻어 작업을 수행할 수 있습니다. 

그리고 `waiting`에 있는 쓰레드를 다시 작업을 하고 싶다면 `notify()`를 통해서 `Runnable` 상태로 이동시킵니다. 

다시 정리하자면 `오래 기다린 쓰레드라고 lock을 빨리 얻는다는 보장은 없습니다.` 위의 그림처럼 wait() 메소드를 호출하면 `waiting pool`이라는 곳에서 대기하게 됩니다.

`notify()`를 호출하면 `waiting pool`에서 임의의 쓰레드 하나를 `Runnable` 상태로 이동시키는 것입니다. 그리고 `notifyAll()`이라는 메소드를 사용하면 모든 쓰레드에게 `Runnable`로 가도록 하는 것이지만, lock을 가질 수 있는건 하나의 쓰레드이기 때문에 결국 나머지는 계속 대기를 해야 하는 상황이 됩니다.

이처럼 `wait(), notify(), notifyAll()`은 특정 객체에 대한 것이기 때문에 Thread 클래스가 아니라 Object 클래스에 정의되어 있습니다. 

<br>

> waiting pool은 객체마다 존재하는 것이므로 notifyAll()이 호출된다고 해서 모든 객체의 waiting pool에 있는 쓰레드가 깨워지는 것은 아니다. <br>
> notifyAll()이 호출된 객체의 waiting pool에 대기 중인 쓰레드만 해당된다는 것을 기억하자.
