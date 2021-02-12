# `Thread 생명주기와 해당 상태를 만들고 해지할 수 있는 조건`

멀티쓰레드 환경에서 효율적으로 프로그래밍 하기 위해서는 `동기화(synchronization)`를 적절히 사용하고 정교한 `스케줄링(scheduling)`으로 프로세스에게 주어진 자원과 시간을 여러 쓰레드가 낭비없이 잘 사용하는 것이 중요합니다. 즉, Java를 이용해서 멀티쓰레드 환경에서 프로그래밍 하기 위해서는 `쓰레드의 생명주기`와 `Java에서 상태를 변화시키는 메소드`에 대해서 잘 알고 있어야 합니다. 

그래서 이번 글에서 Thread의 생명주기에 대해서 알아보고, `Java 에서는 쓰레드의 상태 변화를 어떤 메소드를 이용해서 하는지`를 알아보겠습니다. 

![thread](https://www.uml-diagrams.org/examples/state-machine-example-java-6-thread-states.png)

<br>

## `Thread 생명주기`

- `생성(NEW) : 쓰레드가 생성되고 start()가 호출되지 않은 상태`
- `준비(READY) : 생성된 쓰레드가 CPU를 얻을 때까지 기다리는 상태(start()가 호출된 상태)`
- `수행(RUNNING) : 준비 상태에 있는 쓰레드 중 하나가 CPU를 얻어 실제 작업을 수행하는 상태`
- `대기(WAITING) : 쓰레드의 작업이 종료되지는 않았지만 실행가능하지 않은 일시정지 상태`
- `대기(BLOCKED): 동기화블럭에 의해서 일시정지된 상태(lock이 풀릴 때까지 기다리는 상태)`
- `종료(TERMINATED) : 쓰레드의 작업이 종료된 상태`

쓰레드의 생명주기는 위와 같은 상태를 가지면서 변화합니다. 이러한 쓰레드 생명주기 개념을 가지고 `Java에서 Thread를 사용할 때 어떻게 사용하는가?`를 중점적으로 알아보겠습니다. 
Java에는 `Thread`를 구현하는 방법이 2가지가 있는데, 그 중에 하나를 이용해서 쓰레드를 만들면 `생성(NEW)` 상태가 됩니다.

그리고 `start()` 메소드를 호출하면 `생성(NEW)` 상태의 쓰레드가 `준비(READY)` 상태입니다. 그러다가 CPU 스케쥴러를 통해 선택이 되면 `실행 상태(RUNNING)`로 바뀌게 됩니다. 

`일시정지(WAITING)`는 `sleep()`, `join()`, `wait()` 메소드를 만나게 되면 해당 상태로 변화하게 됩니다.    

이렇게 간단하게 전체적인 흐름에 대해서 살펴보았는데, 이제 본격적으로 Java에서는 어떠한 메소드를 이용해서 Thread의 상태 변화를 시키는지 알아보겠습니다.

<br>

## `Thread의 실행제어`

```java
public class Thread implements Runnable {

    public static native void yield();
    public static native void sleep(long millis) throws InterruptedException;
    public static void sleep(long millis, int nanos);
    public void interrupt() {}
    public final void join() throws InterruptedException {}
}
```

이번 글에서 소개할 Thread 클래스의 대표적인 메소드는 위와 같습니다. (`@deprecated` 된 것은 제외하였습니다.) 이 외에도 Object 클래스의 메소드 일부를 아래에서 소개할 것입니다. 

일단 먼저 Thread 클래스의 메소드에 대해서 가볍게 개념을 정리하면서 예제가 필요한 경우에만 예제를 같이 보겠습니다.

<br>

### `sleep() 메소드`

맨 위의 그림에서 볼 수 있듯이, sleep() 메소드는 말 그대로 `실행 중인 쓰레드를 일시정지 상태로 잠시 재우는 것`입니다. 즉, `RUNNABLE -> WAITING 상태로 변화시키는 것`입니다. 
(매개변수에는 얼마동안 재울지 천분의 일초단위로 지정할 수 있습니다.) 

sleep() 메소드는 예제를 굳이 안봐도 되지만, 혹시나 해서 하나 예제를 만들어보았습니다. 

```java
public class TestMain {
    public static void main(String[] args) throws InterruptedException {
        FooThread fooThread = new FooThread();
        WoodyThread woodyThread = new WoodyThread();
        fooThread.start();
        woodyThread.start();
        Thread.sleep(1000);   // 어떤 쓰레드가 sleep 할까요?
        System.out.println("Main Thread Finish!");
    }
}

class FooThread extends Thread {
    @Override
    public void run() {
        for (int i = 0; i < 10; ++i) {
            System.out.println("FOO 멘토님 최고");
        }
    }
}

class WoodyThread extends Thread {
    @Override
    public void run() {
        for (int i = 0; i < 10; ++i) {
            System.out.println("WOODY 멘토님 최고");
        }
    }
}
```

`너무나도 당연하게 결과가 예측이 되고 이해가 된다면 다음으로 넘어가셔도 좋습니다.` 위의 코드는 단순한 코드입니다. 단순한 코드인데 왜? 굳이 예제를 넣어 설명을 하는가 하면 `main 메소드의 Thread.sleep(1000) 때문`입니다. 

`main 메소드의 sleep() 메소드는 어떤 쓰레드를 재울까요?` FooThread 일까요? WoodyThread 일까요? Main 쓰레드일까요? 이 질문에 대한 답도 쉽게 답할 수 있다면 다음으로.. 가셔도 좋습니다! 

정답은 `Main 쓰레드`입니다. 왜 그럴까요? 이유는 `sleep()은 항상 현재 실행 중인 쓰레드에 대해 작동하기 때문입니다.` 즉, sleep() 메소드가 실행될 때는 Main 쓰레드가 작동하고 있기 때문에 Main 쓰레드가 sleep()이 되는 것입니다.   
(만약 sleep() 메소드가 어떤 쓰레드 run() 메소드 안에 존재했다면 당연히 해당 쓰레드가 sleep() 상태가 될 것입니다.)

어쩌면.. 너무도 당연한 이유이지만 헷갈릴 수 있는 부분이라 생각해서 예제로 정리했습니다.    

<br>

### `join() 메소드`

join 메소드는 `join()을 호출한 쓰레드가 종료될 때까지 기다리게 합니다.` 위의 그림에서 보면 호출한 쓰레드를 `Running` -> `Waiting` 상태로 보내는 것입니다.
(매개변수로 어느정도 기다릴지 시간을 지정할 수도 있고, 지정하지 않는다면 해당 쓰레드가 끝날 때까지 기다립니다.)


```java
public class Test {
    public static void main(String[] args) {
        JavaThread javaThread = new JavaThread();
        javaThread.start();
//        try {
//            javaThread.join();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        System.out.println("Main thread Finish");
    }
}

class JavaThread extends Thread {

    @Override
    public void run() {
        try {
            Thread.sleep(2000);
            System.out.println("Hi Thread");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
```

만약 위의 코드에서 join()을 사용하지 않으면 결과가 어떻게 출력될까요? 결과는 아래와 같습니다. 

```
Main thread Finish
Hi Thread
```

너무도 당연하게 예측을 할 수 있습니다. `JavaThread`가 3초간 sleep() 상태로 가기 때문에 그 시간동안 `Main Thread`가 먼저 종료가 된 것입니다. 

`그러면 위 코드에서 주석을 풀고 join() 메소드를 실행하면 어떤 결과가 나올까요?`

```
Hi Thread
Main thread Finish
```

결과는 위와 같습니다. 왜냐하면 `JavaThread`에서 join()을 호출했기 때문에 Main Thread는 JavaThread가 끝날 때까지 기다리게 되는 것입니다. 

<br>

### `interrupt() 메소드`

interrupt() 메소드는 sleep(), join(), wait()에 의해 `일시정지 상태`인 쓰레드를 깨워서 `실행대기 상태`로 만듭니다.(`WAITING -> RUNNABLE`)  그냥 중단시키는 것이 아니라 `InterruptedException`을 발생시키면서 중단시킵니다. 
`즉, 멈춰있던 쓰레드를 깨워서 실행가능한 상태로 만드는 것입니다.`

<br>

### `yield() 메소드`

yield()는 쓰레드 자신에게 주언 실행시간을 다음 차례의 쓰레드에게 `양보(yield)` 합니다. 예를들어, 스케쥴러에 의해 1초의 실행시간을 할당받은 쓰레드가 0.5초의 시간동안 작업한 상태에서 `yield()가 호출되면 나머지 0.5초는 포기하고 다시 실행대기 상태`가 됩니다.

`yield()`, `interrupt()`를 적절히 사용하면 프로그램의 응답성을 높이고 보다 효율적인 실행이 가능하게 할 수 있습니다. 

<br>

## `Object 클래스의 메소드`

Object 클래스에 보면 `notify()`, `notifyAll()`, `wait()` 메소드가 존재합니다.`이러한 메소드들이 Object 클래스에 존재하는 이유가 무엇일까요?`

Thread 관련 메소드라서 Thread 클래스에 존재해도 될 거 같은데 말이죠... 그 이유와 메소드의 특징을 같이 한번 알아보겠습니다.

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

`Synchronized`와 `Lock`의 개념을 생각해본다면 위의 메소드가 왜 Object 클래스에 존재하는지 알 수 있습니다. 

Synchronized와 Lock은 `wait()`, `notify()`, `notifyAll()` 메소드가 나오게 된 배경과 연관이 되어 있습니다. 무엇이나면 `synchronized`로 동기화해서 데이터를 보호하는 것까지는 좋은데 `특정 쓰레드가 객체의 Lock을 가진 상태로 오랜 시간을 보내지 않도록 하는 것도 중요합니다.`

이러한 상황을 개선하기 위해 고안된 것이 바로 `wait()`, `notify()` 입니다. 동기화된 임계영역의 코드를 수행하다가 더 이상 진행할 상황이 아니라면 `일단 wait()을 호출하여 쓰레드가 락을 반납하고 기다리게 합니다.`

그러면 다른 쓰레드가 락을 얻어 해당 객체에 대한 작업을 수행할 수 있게 됩니다. 그리고 `나중에 작업을 진행할 수 있는 상황이 되면 notify()를 호출해서, 작업을 중단했던 쓰레드가 다시 락을 얻어 작업을 진행할 수 있게 합니다.`

정리하자면 wait()가 호출되면 `실행 중이던 쓰레드는 해당 객체의 대기실(waiting pool)에서 notify()를 기다립니다. RUNNABLE -> WAITING으로 상태가 변화하게 됩니다.` notify()가 호출되면 해당 객체의 대기실에 있던 모든 쓰레드 중에서 임의의 쓰레드만 통지를 받습니다. 

`notifyAll()`은 기다리고 있는 모든 쓰레드에게 통보를 하지만, 그래도 lock을 얻을 수 있는 것은 하나의 쓰레드일 뿐이고 나머지 쓰레드는 토옵를 받긴 했지만 lock을 얻지 못하면 다시 lock을 기다려야 합니다. 

> 객체마다 Waiting pool을 가지고 있습니다. 

<br>

위의 설명에서 저는 notify()를 하면 `해당 객체의 대기실에 있는 모든 쓰레드에게 통보를 한다.`라는 말이 살짝 긴가민가 해서 예제 코드를 통해서 좀 더 알아보았습니다.

<br>

## `예제 코드`

```java
public class MusicPlayer extends Thread {
    int type;
    MusicBox musicBox;

    public MusicPlayer(int type, MusicBox musicBox) {
        this.type = type;
        this.musicBox = musicBox;
    }

    @Override
    public void run() {
        switch (type) {
            case 1:
                musicBox.playMusicA();
                break;
            case 2:
                musicBox.playMusicB();
                break;
        }
    }
}
```
```java
public class MusicBox {
    public synchronized void playMusicA() {
        for (int i = 0; i < 10; ++i) {
            System.out.println("MusicA !!");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public synchronized void playMusicB() {
        for (int i = 0; i < 10; ++i) {
            System.out.println("MusicB !!");

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
```
```java
public class MusicExam {
    public static void main(String[] args) {
        MusicBox box = new MusicBox();

        MusicPlayer musicPlayer1 = new MusicPlayer(1, box);
        MusicPlayer musicPlayer2 = new MusicPlayer(2, box);

        musicPlayer1.start();
        musicPlayer2.start();
    }
}
```

위의 코드를 보면 `musicPlayer1`, `musicPlayer2`를 통해서 쓰레드 2개를 만들고 `MusicBox`를 쓰레드끼리 공유하고 있습니다.

여기서 만약 `musicPlayer1` 쓰레드가 Lock을 오래 가지고 있어 `musicPlayer2` 쓰레드가 기다리는 시간이 길어진다면 어떻게 해야할까요? 

이 때 `wait()` 메소드를 이용하면 됩니다. playMusicA 메소드에서 wait() 메소드를 사용하면 어떻게 될까요? `musicPlayer1` 쓰레드는 Lock을 반납하고 MusicBox 객체의 `waiting pool`로 이동합니다.
그리고 만약에 `musicPlayer2`도 wait() 메소드를 사용하면 이것 역시 MusicBox 객체의 `waiting pool`로 이동합니다. 이와 같이 여러 쓰레드가 하나의 객체를 공유하는 상황이라면 쓰레드들을 wait() 메소드를 통해 해당 객체의 waiting pool 안에서 대기하게 됩니다.   

그러면! 이번에는 `waiting pool`에 있는 쓰레드를 깨울려면 어떻게 해야할까요? 위에서 말했던 것처럼 `notify()`, `notifyAll()`을 이용하는 것입니다. 메소드의 내용을 다시 한번 정리하자면 아래와 같습니다.

- `notify(): waiting pool에 있는 쓰레드 중에 임의의 쓰레드만 통보를 받습니다.`
- `notifyAll(): waiting pool에 있는 모든 쓰레드에게 통보를 합니다.(결국 Lock을 얻는 것은 쓰레드 하나입니다.)`

즉, 이렇게 Lock은 객체 단위이고, `특정 객체를 wait 하고, notify를 하는 것이기 때문에 Object 클래스에 메소드가 존재하는 것`입니다.

<br>

### `wait(), notify(), notifyAll() 정리`

```
- Object에 정의되어 있다.
- 동기화 블럭(synchronized 블럭) 내에서만 사용할 수 있다. (호출하는 스레드가 반드시 고유 락을 갖고 있어야 한다.)
- 보다 효율적인 동기화를 가능하게 한다.
```

<br> <br>

# `Reference`

- [https://www.uml-diagrams.org/java-thread-uml-state-machine-diagram-example.html](https://www.uml-diagrams.org/java-thread-uml-state-machine-diagram-example.html)
- [자바의 정석](http://www.yes24.com/Product/Goods/24259565)
- [자바의 신](http://www.yes24.com/Product/Goods/42643850)