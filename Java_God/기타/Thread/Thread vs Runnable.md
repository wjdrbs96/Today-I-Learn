# `Thread vs Runnable 차이점`

```java
@FunctionalInterface
public interface Runnable {

    public abstract void run();
}
```

Runnable 인터페이스는 run() 메소드만을 가지고 있는 `함수형 인터페이스` 입니다. 

```java
public class Thread implements Runnable {
}
```

Thread 클래스는 Runnable 인터페이스를 구현하고 있으며 많은 메소드들을 가지고 있습니다.

<br>

## `자바에서 쓰레드를 구현하기 위해 2가지를 만든 이유가 무엇일까요?`

자바에서 Thread 클래스를 상속받기 위해서는 extends 키워드를 사용해야 합니다. 그런데 만약 Thread 클래스 외에 다른 클래스도 상속을 받아야 한다면 어떻게 될까요?

- `자바에서는 다중 상속이 불가능하기 때문에 해당 클래스를 쓰레드로 만들 수 없게 됩니다. 이러한 경우에는 Runnable 인터페이스를 구현해서 사용하면 됩니다.`
- `한마디로 쓰레드 클래스가 다른 클래스를 확장할 필요가 있을 경우에는 Runnable 인터페이스를 구현하면 되고, 그렇지 안다면 쓰레드 클래스를 사용하는 것이 편합니다.`

<br>

### `Thread 클래스를 이용하여 쓰레드 구현`

```java
public class ThreadTest extends Thread {
    @Override
    public void run() {
        System.out.println("Test");
    }

    public static void main(String[] args) {
        ThreadTest threadTest = new ThreadTest();
        threadTest.start();
    }
}
```

Thread 클래스를 이용하면 위와 같이 쉽게 쓰레드를 구현할 수 있습니다. 

<br>

### `Runnable을 이용한 쓰레드 구현`

```java
public class ThreadTest implements Runnable {

    @Override
    public void run() {
        System.out.println("Test");
    }

    public static void main(String[] args) {
        ThreadTest threadTest = new ThreadTest();
        new Thread(threadTest).start();
    }
}
```

반면에 `Runnable`은 인터페이스 이기 때문에 결국은 Thread 클래스를 이용해서 쓰레드를 만들어야 하기에, Thread 클래스를 이용하는 것보다는 좀 더 불편하다고 생각합니다. 

그래서 Thread 클래스 외에 다른 클래스를 상속 받는 상황이 아니라면 `Thread` 클래스를 이용해서 쓰레드를 구현하는 것이 더 좋다고 생각합니다. 