# `BlockingQueue란?`

이번 글에서는 `BlockingQueue`에 대해서 정리해보겠습니다. 

```java
package java.util.concurrent;

public interface BlockingQueue<E> extends Queue<E> {}
```

BlockingQueue는 Queue 인터페이스를 확장하고 있으며, `java.util.concurrent` 패키지 안에 있는 인터페이스 입니다. `java.util.concurrent` 패키지는 이름에서 유추할 수 있듯이 `동시성`과 관련된 것들을 담아놓은 패키지입니다. 

즉, BlockingQueue도 `동시성` 처리를 하는데 사용되는 인터페이스 입니다. (멀티쓰레드 환경에서 삽입, 삭제시 `Thread-safe` 하다는 특징을 가지고 있습니다.)

대표적인 특징으로는 어떤 쓰레드가 큐에 `삽입`을 할 때 큐가 꽉 차있다면 해당 쓰레드는 대기를 하게 됩니다. 그리고 어떤 쓰레드가 큐에 `삭제`를 하려고 할 때 큐가 비어 있다면 해당 쓰레드도 대기를 하게 됩니다. 

<br>

## `BlockingQueue 사용`

BlockingQueue는 한 쪽에서는 `Producer Thread`로 작동하고, 나머지 한 쪽에서는 `Consumer Thread`로 작동합니다.

![Thread](http://tutorials.jenkov.com/images/java-concurrency-utils/blocking-queue.png)

한마디로 하나의 쓰레드는 `put`을 하고, 나머지 쓰레드는 `take()`를 하는데 사용됩니다. 그리고 위에서 말했던 것처럼 큐가 꽉차있으면 `put()` 과정은 `take()`가 일어날 때까지 대기하게 됩니다. 
그리고 큐가 비어있다면 `put()`이 일어날 때까지 `take()` 과정은 대기합니다. 

<br>

## `BlockingQueue 메소드`

||Throws Exception|Special Value|Blocks|Times Out|
|--------|-------|-------|-------|---------|
|Insert|add(O)|offer(O)|put(O)|offer(O, timeout, timeunit)|
|Remove|remove(O)|poll()|take()|poll(timeout, timeunit)|
|Examine|element()|peek()|||

각각의 메소드들은 조금씩 다르게 동작합니다. 

- ### `Throw Exception`
    - 바로 실행이 되지 못한다면 `Exception 예외가 발생합니다.`
    
- ### `Special Value`
    - 바로 실행이 되지 못한다면 `true or false를 반환합니다.`
    
- ### `Blocks`
    - 바로 실행이 되지 못한다면 `반대 메소드가 실행될 때까지 대기합니다.(put -> take, take -> put)`
    
- ### `Times out`
    - 바로 실행이 되지 못한다면 `지정한 시간만큼 기다리고 그래도 안된다면 true or false를 반환합니다.`
    
<br>

> 또한 BlockingQueue는 null을 INSERT 하는 것이 불가능합니다. 만약 null을 INSERT 한다면 NullPointerException이 발생합니다.

<br>

## `BlockingQueue 구현`

- [ArrayBlockingQueue]()
- [DelayQueue]()
- [LinkedBlockingQueue]()
- [PriorityBlockingQueue]()
- [SynchronousQueue]()

<br>

## `Java BlockingQueue Example`

BlockingQueue의 구현체인 `ArrayBlockingQueue`를 이용해서 예제 코드를 보겠습니다.

```java

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class BlockingQueueExample {

    public static void main(String[] args) throws Exception {

        BlockingQueue queue = new ArrayBlockingQueue(1024);

        Producer producer = new Producer(queue);
        Consumer consumer = new Consumer(queue);

        new Thread(producer).start();
        new Thread(consumer).start();
    }
}
```

위와 같이 `Producer` 역할을 하는 클래스와 `Consumer` 역할을 하는 클래스를 만들어 각각 쓰레드로 실행시켜 테스트를 해볼 것입니다. 

```java
import java.util.concurrent.BlockingQueue;

public class Producer implements Runnable {

    protected BlockingQueue queue = null;

    public Producer(BlockingQueue queue) {
        this.queue = queue;
    }

    public void run() {
        try {
            queue.put("1");
            Thread.sleep(3000);
            queue.put("2");
            Thread.sleep(3000);
            queue.put("3");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
```
```java
import java.util.concurrent.BlockingQueue;

public class Consumer implements Runnable{

    protected BlockingQueue queue = null;

    public Consumer(BlockingQueue queue) {
        this.queue = queue;
    }

    public void run() {
        try {
            System.out.println(queue.take());
            System.out.println(queue.take());
            System.out.println(queue.take());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
```

위와 같이 `put`을 하고 나서 3초씩 대기하는 코드를 작성하였습니다. 그러면 첫 번째 원소가 INSERT 된 후에 3초를 대기하면 `Consumer` 쓰레드에서 `take()` 과정이 일어날 것입니다. 

하지만 위에서 보았던 것처럼 `take()` 메소드는 큐가 비어있다면 `put()` 과정이 일어날 때까지 대기하게 됩니다. 그래서 비어있는 큐에 take() 작업을 해서 예외가 발생하는 것이 아니라 정상적으로 결과가 출력됩니다. 

<br>

# `Reference`

- [http://tutorials.jenkov.com/java-util-concurrent/blockingqueue.html](http://tutorials.jenkov.com/java-util-concurrent/blockingqueue.html)

