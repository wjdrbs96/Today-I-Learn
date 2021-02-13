# `들어가기 전에`

synchronized의 Lock의 범위가 어떻게 되냐는 질문은 [여기](https://github.com/wjdrbs96/Today-I-Learn/blob/master/Java/Thread/Synchronized%EC%9D%98%20Lock%20%EB%B2%94%EC%9C%84.md) 에서 정리한 적이 있습니다. 
간단하게 요약하자면 `Lock은 객체 단위로 가지고 있습니다.` 즉, 하나의 쓰레드가 객체의 Lock을 점유하고 있다면 다른 쓰레드는 Lock을 받을 때까지 기다려야 합니다. 

그런데 `하나의 쓰레드가 너무 오래 Lock을 쥐고 있다면 어떻게 했나요?` 그것도 [여기](https://github.com/wjdrbs96/Today-I-Learn/blob/master/Java/Thread/Thread%20%EC%83%81%ED%83%9C%2C%20%ED%95%B4%EC%A7%80%20%EC%A1%B0%EA%B1%B4.md) 에서 정리를 했었는데요. 바로 Object 클래스의 `wait()` 메소드를 사용하는 것이었습니다.
그러면 wait() 메소드를 호출한 쓰레드는 `해당 객체의 Waiting Pool`에 들어가게 됩니다. 그리고 `Waiting Pool`에 있는 쓰레드를 다시 깨우려면 `notify()`, `notifyAll()` 메소드를 사용했는데요.

이러한 방법은 하나의 단점이 존재합니다. 어떤 단점인지 `제가 만든 예제`를 보면서 알아보겠습니다. (예제가 좋지는 않지만...)

```java
public class Customer extends Thread {
    private Table table;

    public Customer(Table table) {
        this.table = table;
    }

    @Override
    public void run() {
        table.eatTableA();
    }
}

class Cook extends Thread {
    private Table table;

    public Cook(Table table) {
        this.table = table;
    }

    @Override
    public void run() {
        table.eatTableA();
    }
}
```

위와 같이 `손님 쓰레드`와 `요리사 쓰레드`가 있습니다. 

```java
public class Table {
    public synchronized void eatTableA() {
        for (int i = 0; i < 10; ++i) {
            System.out.println("요리사와 손님이 테이블을 공유하는 중");
            try {
                int random = (int)(Math.random() * 100000); 
                System.out.println(random);
                Thread.sleep(random);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
``` 

그리고 `손님 쓰레드`와 `요리사 쓰레드`는 `테이블 객체`를 공유해서 사용하고 있습니다. (sleep() 하는 시간이 랜덤입니다.)

```java
public class MainTest {
    public static void main(String[] args) {
        Table table = new Table();

        Customer customer1 = new Customer(table);
        Customer customer2 = new Customer(table);
        Cook cook = new Cook(table);
        
        cook.start();
        customer1.start();
        customer2.start();
    }
}
```

만약에 하나의 쓰레드가 테이블 객체의 Lock을 오래 가지고 있어 wait() 메소드를 사용하여 테이블 객체의 Waiting Pool로 보냈다고 가정하겠습니다. 

그래서 현재 Waiting Pool에는 `손님쓰레드`, `요리사 쓰레드`가 존재하는 상황입니다. `이 때 notify()를 하면 어떤 객체가 깨어날까요?` 정답은 알 수 없습니다. `이것이 wait(), notify()의 단점입니다.`

만약에 위와 같이 Waiting Pool에 있는 임의의 객체를 깨우는 것이 아니라 특정 손님 쓰레드를 깨우고 싶다면 어떻게 해야 할까요? 

그럴 때 사용하는 것이 바로 `Lock`, `Condition`입니다. 이것을 이용하면 wait(), notify()로는 불가능한 선별적인 통지가 가능합니다.(아래에서 자세히 살펴보겠습니다.)

<br>

# `Lock과 Condition을 이용한 동기화`

JDK 1.5 이전까지는 동기화 방법이 synchronized 뿐이었지만, 지금은 동기화 할 수 있는 방법이 3가지가 있습니다. 

- `synchronized 사용`
- `java.util.concurrent.locks 사용`
- `java.util.concurrent.atomic 사용`

지금은 `java.util.concurrent.locks` 패키지가 제공하는 Lock 인터페이스 기반의 방법들을 알아보겠습니다.

<br>

## `Lock 인터페이스`

```java
public interface Lock {

    void lock();

    void lockInterruptibly() throws InterruptedException;

    boolean tryLock();

    boolean tryLock(long time, TimeUnit unit) throws InterruptedException;

    void unlock();

    Condition newCondition();
}
```

Lock 인터페이스가 가지고 있는 메소드는 위와 같습니다. 특징에 대해서 간단히 정리해보면 아래와 같습니다.

- `lock(): 사용 가능한 경우 lock을 얻습니다. lock을 사용할 수 없는 경우 lock을 얻을 때까지 스레드를 블락(Block) 시킵니다.`
- `lockInterruptibly(): lock과 유사합니다. 차단(Block) 상태일 때 java.lang.InterruptedException를 발생시키면서 다시 실행을 할 수 있습니다.`
- `tryLock(): lock의 non-blocking 버전입니다. 다른 쓰레드에 lock이 걸려있으면 lock을 얻으려고 기다리지 않는다는 특징이 있습니다.`
- `tryLock(long timeout, TimeUnit timeUnit): lock()은 lock을 얻을 때까지 쓰레드를 블락(Block) 시키므로 쓰레드의 응답성이 나빠질 수 있습니다. 즉, 응답성이 중요한 경우 지정된 시간을 정해서 그 시간안에 lock을 얻지 못하면 다시 작업을 할 지, 포기할지를 정할 수 있습니다.`
- `unlock(): 말그대로 lock을 해지하는 것입니다.`

<br>

교착상태(deadlock)을 방지하려면 `unlock()`을 항상 실행해주어야 합니다. 

```
Lock lock = ...; 
lock.lock();
try {
    // access to the shared resource
} finally {
    lock.unlock();
}
``` 

그래서 위와 같이 `try-finally`를 사용하여 예외가 발생하더라도 항상 `unlock()`을 호출할 수 있도록 합니다. 

<br>

## `ReentrankLock 이란?`

이번에는 Lock 인터페이스 구현체인 `ReentrantLock 클래스`에 대해서 알아보겠습니다. `Reentrant(재 진입할 수 있는` 이라는 단어가 붙어 있는 이유는 wait(), notify()와 같이 `특정 조건에서 lock을 풀고 나중에 다시 lock을 얻고 임계영역으로 들어와서 이후의 작업을 수행할 수 있기 때문`입니다. 

```java
public class ReentrantLock implements Lock, java.io.Serializable {
    public ReentrantLock() {
        sync = new NonfairSync();
    }
 
    public ReentrantLock(boolean fair) {
        sync = fair ? new FairSync() : new NonfairSync();
    }
}
```

`ReentrantLock` 클래스는 위와 같이 두 개의 생성자를 가지고 있습니다. 

- `생성자의 매개변수를 true를 주면 lock이 풀렸을 때 가장 오래 기다린 쓰레드가 lock을 획득할 수 있게 공정(fair)하게 처리합니다.(하지만 공정하게 처리하려면 어떤  쓰레드가 가장 오래 기다렸는지 확인하는 과정을 거칠 수 밖에 없으므로 성능은 떨어질 수 밖에 없습니다.)`

ReetrantLock 역시 `lock()`, `unlock()`을 이용하여 동기화를 합니다. 그러면 `synchronized와 ReentrantLock은 어떤 차이가 있을까요?`

<br>

## `synchronized vs ReetrantLock 차이`

- ### `synchronized`
    - `synchronized 블럭으로 동기화를 하면 자동적으로 lock이 잠기고 풀립니다.(synchronized 블럭 내에서 예외가 발생해도 lock은 자동적으로 풀립니다.)`
    - `그러나 같은 메소드 내에서만 lock을 걸 수 있다는 제약이 존재합니다.`
    - `암묵적인 lock 방식`
    - `WAITING 상태인 스레드는 interrupt가 불가능합니다.`
      ```java
      synchronized(lock) {
          // 임계영역
      }
      ```      

   
- ### `ReentrantLock`
    - `synchronized와 달리 수동으로 lock을 잠그고 해제해야 합니다.`
    - `명시적인 lock 방식`
    - `암묵적인 락만으로는 해결할 수 없는 복잡한 상황에서 사용할 수 있습니다.`
    - `lockInterruptably() 함수를 통해 WAITING 상태의 스레드를 interrupt할 수 있습니다.`
      ```java
      lock.lock();
      // 임계영역
      lock.unlock();
      ```
      
<br>

## `ReetrantLock과 Condition`

위에서 `notify()`의 단점은 원하는 객체를 깨울 수 없는 것이라고 하였습니다. `Condition은 이러한 단점을 해결하기 위한 것입니다.`

`wait()`, `notify()`는 쓰레드의 종류를 구분하지 않고, 공유 객체의 `Waiting pool`에 같이 넣었습니다. 하지만 Condition은 손님 쓰레드 Condition, 요리사 쓰레드 Condition을 만들어서 각각의 Waiting pool에서 따로 기다리도록 하면 됩니다. 

```java
private ReetrantLock lock  = new ReetrantLock();

// lock으로 condition 생성
private Condition forCook = lock.newCondition();          // 요리사 쓰레드 Condition
private Condition forCustomer = lock.newCondition();      // 손님 쓰레드 Condition
```

위와 같이 `손님 쓰레드`, `요리사 쓰레드`의 Condition을 각각 만들어주면 됩니다. 

|Object|Condition|
|--------|-------------|
|void wait()|void await() <br> void awaitUninterruptibly()|
|void wait(long timeout)|boolean await(long time, TimeUnit unit)|
|void notify()|void signal()|
|void notifyAll()| void signalAll()|

- `wait() 대신 await()를 사용`
- `notify() 대신 signal()을 사용`

Condition을 사용하면 위와 같이 바꿔서 사용하면 됩니다. 즉, 명확하게 손님 쓰레드, 요리사 쓰레드를 구분해서 wait(), notify()를 할 수 있습니다.

<br>

## `다른 예제 코드`

```java
public class ReentrantLockWithCondition {

    Stack<String> stack = new Stack<>();
    int CAPACITY = 5;

    ReentrantLock lock = new ReentrantLock();
    Condition stackEmptyCondition = lock.newCondition();
    Condition stackFullCondition = lock.newCondition();

    public void pushToStack(String item){
        try {
            lock.lock();
            while(stack.size() == CAPACITY) {
                stackFullCondition.await();
            }
            stack.push(item);
            stackEmptyCondition.signalAll();
        } finally {
            lock.unlock();
        }
    }

    public String popFromStack() {
        try {
            lock.lock();
            while(stack.size() == 0) {
                stackEmptyCondition.await();
            }
            return stack.pop();
        } finally {
            stackFullCondition.signalAll();
            lock.unlock();
        }
    }
}
```

`Stack`을 사용할 때도 위와 같이 `ReetrantLock`을 사용해서 동기화 할 수 있습니다. 

<br>

# `Reference`

- [자바의 정석](http://www.yes24.com/Product/Goods/24259565)
- [https://www.baeldung.com/java-concurrent-locks](https://www.baeldung.com/java-concurrent-locks)