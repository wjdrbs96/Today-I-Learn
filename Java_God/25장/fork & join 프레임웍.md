# `fork & join 프레임 웍`

CPU의 속도는 계속 향상되어 왔지만 이제는 속도 향상에 한계에 도달해서, 속도보다는 코어의 개수를 늘려서 CPU의 성능을 향상시키는 방향으로 발전해 가고 있습니다. 

그래서 JDK 1.7부터 `fork & join 프레임웍`이 추가되었고, 이 프레임웍은 하나의 작업을 작은 단위로 나눠서 여러 쓰레드가 동시에 처리하는 것을 쉽게 만들어 줍니다. 

먼저 수행할 작업에 따라 `RecursiveAction`과 `RecursiveTask` 두 클래스 중에서 하나를 상속받아 구현해야 합니다. 

```java
public abstract class RecursiveAction extends ForkJoinTask<Void> {
    private static final long serialVersionUID = 5232453952276485070L;

    protected abstract void compute();   // 상속을 통해 이 메소드를 구현해야 합니다.
}
```

반환 타입이 없다면 `RucursiveAction`을 사용하면 됩니다. 

<br>

```java
public abstract class RecursiveTask<V> extends ForkJoinTask<V> {
    private static final long serialVersionUID = 5232453952276485270L;

    protected abstract V compute();      // 상속을 통해 이 메소드를 구현해야 합니다.
}
```

반환 타입이 있다면 `RecursiveTask`를 사용하면 됩니다. 두 클래스 모두 `compute()`라는 추상 메소드를 가지고 있습니다. 

<br>

```java
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class SumTask extends RecursiveTask<Long> {
    long from, to;

    public SumTask(long from, long to) {
        this.from = from;
        this.to = to;
    }
    
    @Override
    protected Long compute() {
        // 처리할 작업을 수행하기 위한 문장을 넣습니다. 
    }

    public static void main(String[] args) {
        ForkJoinPool pool = new ForkJoinPool();

    }
}
```

그 다음에는 [쓰레드풀](https://limkydev.tistory.com/55) 과 수행할 작업을 생성하고, `invoke()`로 작업을 시작합니다. 
쓰레드를 시작할 때 run()이 아니라 start()를 호출한 것처럼 `fork & join 프레임웍`으로 수행할 작업도 `compute()`가 아닌 `invoke()`로 시작합니다. 

`ForkJoinPool`은 fork & join 프레임웍에서 제공하는 쓰레드 풀(thread pool)로, 지정된 수의 쓰레드를 생성해서 미리 만들어 놓고 반복해서 재사용할 수 있게 합니다. 

그리고 쓰레드르르 반복해서 생성하지 않아도 된다는 장점과 너무 많은 쓰레드가 생성되어 성능이 저하되는 것을 막아준다는 장점이 있습니다. 

쓰레드 풀은 쓰레드가 수행해야하는 작업이 담긴 큐를 제공하며, 각 쓰레드는 자신의 작업 `큐`에 담긴 작업을 순서대로 처리합니다. 

```java
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class SumTask extends RecursiveTask<Long> {
    long from, to;

    public SumTask(long from, long to) {
        this.from = from;
        this.to = to;
    }

    @Override
    public Long compute() {
        long size = to - from + 1;

        if (size <= 5) {
            return sum();
        }

        long half = (from + to) / 2;

        SumTask leftSum = new SumTask(from, half);
        SumTask rightSum = new SumTask(half + 1, to);

        leftSum.fork();
        return rightSum.compute() + leftSum.join();
    }

    public Long sum() {
        long sum = 0;
        for (long i = from; i < to + 1; ++i) {
            sum += i;
        }
        return sum;
    }
}
```

위와 같이 `compute()`를 수행할 작업 외에도 작업을 어떻게 나눌 것인가에 대해서 작성해주면 됩니다. 

![forkjoin](https://t1.daumcdn.net/cfile/tistory/2628974A57BD30A21D)

정렬 알고리즘에서 많이 보았던 `분할 정복`과 상당히 유사합니다. 

- `Fork() : 해당 작업을 쓰레드 풀의 작업 큐에 넣습니다. (비동기 메소드)`
- `join() : 해당 작업의 수행이 끝날 때까지 기다렸다가, 수행이 끝나면 그 결과를 반환합니다. (동기 메소드)`  

![queue](https://t1.daumcdn.net/cfile/tistory/2138EA4B57BD31AE1D)

위의 그림을 보면 submit을 통해 inbound queue에 작업이 쌓이고, 이것은 A, B 쓰레드가 각자의 쓰레드의 작업 큐에 넣고 작업을 합니다. 그러다가 자신의 작업 큐가 비어있는 쓰레드는 다른 쓰레드의 작업 큐에서 작업을 가져와서 수행합니다. 
(`이것은 작업 훔쳐오기(work stealing) 이라고 합니다.`)

<br>

## `정리하기`

fork & join 프레임웍을 사용하면 compute() 메소드에 구현한 것처럼 작업을 나누고 다시 합치는 과정에서 시간이 소요됩니다. 이처럼 항상 멀티쓰레드로 작업하는 것이 빠르다고 생각하면 안됩니다. (for문이 더 빠를 수 있으니 테스트 해보고 사용을 해야 합니다.)

