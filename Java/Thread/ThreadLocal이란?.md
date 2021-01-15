# `ThreadLocal 이란?`

각 쓰레드에서 `혼자 쓸 수 있는 값`을 가지려면 `ThreadLocal`을 사용하면 됩니다. 

이전에 여러 쓰레드에서 데이터를 공유할 때 발생하는 문제를 해결하기 위해서 `synchronized` 키워드를 사용했을 것입니다. 

`그런데 만약 쓰레드 별로 서로 다른 값을 처리해야 한다면 어떻게 해야할까요?` 이럴 때 `ThreadLocal`을 사용하면 됩니다. 

예제 코드를 보면서 자세히 이해해보겠습니다.

<br>

## `예제 코드`

```java
import java.util.Random;

public class ThreadLocalSample {
    private final static ThreadLocal<Integer> local = new ThreadLocal<>();
    private static Random random;

    static  {
        random = new Random();
    }

    public static Integer generateNumber() {
        int value = random.nextInt(45);
        local.set(value);
        return value;
    }

    public static Integer get() {
        return local.get();
    }

    public static void remove() {
        local.remove();
    }
}
```

- `private static final로 ThreadLocal 객체를 생성했습니다. ThreadLocal은 제네릭으로 선언되어 있는 클래스라 각 쓰레드에서 고유하게 사용할 데이터의 타입을 지정해주면 됩니다.`
- `get(), set(), remove() 메소드를 통해서 값을 꺼내고 저장하고 삭제하는 것을 합니다.`

<br>

```java
public class LocalUserThread extends Thread {

    @Override
    public void run() {
        int value = ThreadLocalSample.generateNumber();
        System.out.println(this.getName() + " LocalUserThread value = " + value);
        OtherLogic otherLogic = new OtherLogic();
        otherLogic.printMyNumber();
        ThreadLocalSample.remove();
    }

    public static void main(String[] args) throws InterruptedException {
        LocalUserThread userThread1 = new LocalUserThread();
        userThread1.start();

        Thread.sleep(1000);

        LocalUserThread userThread2 = new LocalUserThread();
        userThread2.start();

        Thread.sleep(1000);
        
        LocalUserThread userThread3 = new LocalUserThread();
        userThread3.start();
    }
}
```
```java
public class OtherLogic {
    public void printMyNumber() {
        System.out.println(Thread.currentThread().getName() + " OtherLogic value = "+ ThreadLocalSample.get());
    }
}
```

- `LocalUserThread 클래스 run() 메소드에서 쓰레드 별로 값이 어떻게 찍히는지 테스트 하고 있습니다.`

```
Thread-0 LocalUserThread value = 1
Thread-0 OtherLogic value = 1
Thread-1 LocalUserThread value = 27
Thread-1 OtherLogic value = 27
Thread-2 LocalUserThread value = 21
Thread-2 OtherLogic value = 21
```

실행해보면 값의 결과는 매번 다르겠지만 위와 같이 각자의 쓰레드가 고유의 값을 가지는 것을 볼 수 있습니다. 
(`그리고 각 쓰레드별로 절대 값을 공유하지 않는다는 점을 알아두어야 합니다.`)

- `ThreadLocal에 저장된 값은 해당 쓰레드에서 고유하게 사용할 수 있습니다.`
- `ThreadLocal 클래스의 변수는 private static final로 선언합니다.`
- `ThreadLocal 클래스에 선언되어 있는 메소드는 set(), get(), remove(), initialValue()가 있습니다.`
- `사용이 끝난 후에는 remove() 메소드를 호출해 주는 습관을 가져야만 합니다.`

<br>

### `왜 remove() 메소드를 호출해주는 것이 좋을까요?`

위의 예제 코드에서는 쓰레드를 한번 사용하고 말았지만, 실제 쓰레드를 사용할 때는 `쓰레드 풀`을 사용해서 재사용하게 되기 때문에 쓰레드를 한번 사용했다고 끝난 것이 아니게 됩니다. 
따라서 remove()를 통해서 값을 삭제해줘야 다음에 사용할 때 쓰레기 값이 들어있지 않게 됩니다.