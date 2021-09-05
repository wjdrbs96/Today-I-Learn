# `Thread Method 정리`

`Thread` 클래스에는 `sleep()`, `join()`, `interrupt()` 메소드들이 존재합니다. 각각 어떤 역할을 하는 것인지 알아보겠습니다. 

![thread](https://t1.daumcdn.net/cfile/tistory/99E341435DC42E4E33)

먼저 `Thread 생명주기`를 다시 한번 보자면 위와 같습니다. 그림을 참고하면서 메소드들의 역할을 정리해보겠습니다. 

<br>

## `sleep() 이란?`

```java
public class ThreadEx extends Thread {
    
    @Override
    public void run() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
```

사용법은 위와 같습니다. static 메소드라서 `Thread.sleep()`으로 사용할 수 있고, `InterruptedException` 에러를 발생시킬 수 있어 에러처리를 반드시 해주어야 합니다. 

즉, 말 그대로 Thread가 lock을 쥐고 실행하고 있는 것을 일정시간 동안 재우는 역할을 합니다. 그림에서 볼 수 있듯이 sleep() 메소드를 만나면 실행중이던 쓰레드는 WAITING 상태로 가게 됩니다. 

sleep()에 의해 일시정지가 된 쓰레드는 지정된 시간이 다 되거나 `interrupt()` 메소드가 호출되면 잠에서 깨어나 `실행대기 상태`가 됩니다. 

<br>

## `interrupt()와 interrupted()` 

진행 중인 쓰레드의 작업이 끝나기 전에 취소시켜야할 때가 있습니다. 예를들어 큰 파일을 다운받다가 너무 오래걸린다 싶으면 중간에 종료시키는 상황이 있습니다. 

이 때 `interrupt()`는 쓰레드에게 작업을 멈추라고 요청합니다. 단지 멈추라고 요청하는 것이지 쓰레드를 강제 종료시키지는 못합니다.  

interrupted()는 쓰레드에 대해 interrupt()가 호출되었는지 알려줍니다. interrupt()가 호출되지 않았다면 false를, interrupt()가 호출되었다면 true를 반환합니다. 

<br>

## `suspend(), resume(), stop()`

- `suspend()는 sleep()처럼 쓰레드를 멈추게 합니다. suspend()에 의해 정지된 쓰레드는 resume()을 호출해야 다시 실행 대기 상태가 됩니다.`
- `stop()은 호출되는 즉시 쓰레드가 종료됩니다.`
- `하지만 susped(), stop()이 교착상태(deadlock)를 일으키기 쉽게 작성되어있으므로 사용이 권장되지 않습니다. 그래서 이 메소드들은 모두 deprecated 되었습니다.`
