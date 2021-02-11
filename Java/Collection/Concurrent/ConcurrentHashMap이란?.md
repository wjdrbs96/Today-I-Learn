# `들어가기 전에`

`HashTable`, `HashMap`, `ConcurrnetHashMap`은 많이 유사한 특징들을 가지고 있습니다. 하지만 세부적으로 보면 조금씩 꽤나 차이가 있는데요. 간단하게 어떤 차이가 있는지 알아보면서 시작하겠습니다. 

<br>

## `Hashtable 클래스`

```java
public class Hashtable<K,V>
    extends Dictionary<K,V>
    implements Map<K,V>, Cloneable, java.io.Serializable {

    public synchronized int size() { }

    @SuppressWarnings("unchecked")
    public synchronized V get(Object key) { }

    public synchronized V put(K key, V value) { }
}
```

`Hashtable` 클래스의 대부분의 API를 보면 위와 같이 메소드 전체에 `synchronized` 키워드가 존재하는 것을 볼 수 있습니다.(`메소드 전체가 임계구역으로 설정 됩니다.`) 그렇기 때문에 `Multi-Thread 환경에서는 나쁘지 않을 수도? 있습니다` 

하지만 `동시에 작업을 하려해도 객체마다 Lock을 하나씩 가지고 있기 때문에 동시에 여러 작업을 해야할 때 병목현상이 발생할 수 밖에 없습니다.`(메소드에 접근하게 되면 다른 쓰레드는 Lock을 얻을 때까지 기다려야 하기 때문입니다.)

Hashtable 클래스는 `Thread-safe` 하다는 특징이 있긴 하지만, 위와 같은 특징 때문에 `멀티쓰레드 환경에서 사용하기에도 살짝 느리다는 단점`이 있습니다. 또한 Collection Framework가 나오기 이전부터 존재하는 클래스이기 때문에 최근에는 잘 사용하지 않는 클래스입니다. 

<br>

## `HashMap 클래스`

Java에는 Map의 역할을 하는 `HashMap` 클래스가 존재합니다. 매우 중요한 클래스이면서 꼭 알아두어야 할 클래스라고 생각합니다.

```java
public class HashMap<K,V> extends AbstractMap<K,V>
    implements Map<K,V>, Cloneable, Serializable {

    public V get(Object key) {}
    public V put(K key, V value) {}
}
```

HashMap 클래스를 보면 `synchronized` 키워드가 존재하지 않습니다. 그렇기 때문에 `Map` 인터페이스를 구현한 클래스 중에서 성능이 제일 좋다고 할 수 있습니다. 하지만 synchronized 키워드가 존재하지 않기 때문에 당연히 `Multi-Thread` 환경에서 사용할 수 없다는 특징을 가지고 있습니다. 

멀티 쓰레드 환경이 아니라면 `HashMap`을 사용하기에 대체적으로 적합하겠지만, 멀티쓰레드의 환경이라면 HashMap 클래스도 Hashtable 클래스의 대안이 될 수는 없습니다. 

`그러면 Hashtable 클래스보다는 더 빠르고 Multi-Thread 환경에서 쓸 수 있는 클래스는 없을까요?`

<br>

## `ConcurrentHashMap 클래스`

Hashtable 클래스의 단점을 보완하면서 Multi-Thread 환경에서 사용할 수 있도록 나온 클래스가 바로 `ConcurrentHashMap` 입니다.(`JDK 1.5에 검색과 업데이트시 동시성 성능을 높이기 위해서 나온 클래스 입니다.`)

`HashMap`, `Hashtable`, `ConcurrentHashMap` 클래스 모두 Map의 기능적으로만 보면 큰 차이는 없습니다. 그러면 어떤 `동기화 방식을 사용하고 특징이 있길래 Hashtable의 대안 클래스가 될 수 있을까요?`

```java
public class ConcurrentHashMap<K,V> extends AbstractMap<K,V>
    implements ConcurrentMap<K,V>, Serializable {

    public V get(Object key) {}

    public boolean containsKey(Object key) { }

    public V put(K key, V value) {
        return putVal(key, value, false);
    }

    final V putVal(K key, V value, boolean onlyIfAbsent) {
        if (key == null || value == null) throw new NullPointerException();
        int hash = spread(key.hashCode());
        int binCount = 0;
        for (Node<K,V>[] tab = table;;) {
            Node<K,V> f; int n, i, fh;
            if (tab == null || (n = tab.length) == 0)
                tab = initTable();
            else if ((f = tabAt(tab, i = (n - 1) & hash)) == null) {
                if (casTabAt(tab, i, null,
                             new Node<K,V>(hash, key, value, null)))
                    break;                   // no lock when adding to empty bin
            }
            else if ((fh = f.hash) == MOVED)
                tab = helpTransfer(tab, f);
            else {
                V oldVal = null;
                synchronized (f) {
                    if (tabAt(tab, i) == f) {
                        if (fh >= 0) {
                            binCount = 1;
                            for (Node<K,V> e = f;; ++binCount) {
                                K ek;
                                if (e.hash == hash &&
                                    ((ek = e.key) == key ||
                                     (ek != null && key.equals(ek)))) {
                                    oldVal = e.val;
                                    if (!onlyIfAbsent)
                                        e.val = value;
                                    break;
                                }
                                Node<K,V> pred = e;
                                if ((e = e.next) == null) {
                                    pred.next = new Node<K,V>(hash, key,
                                                              value, null);
                                    break;
                                }
                            }
                        }
                        else if (f instanceof TreeBin) {
                            Node<K,V> p;
                            binCount = 2;
                            if ((p = ((TreeBin<K,V>)f).putTreeVal(hash, key,
                                                           value)) != null) {
                                oldVal = p.val;
                                if (!onlyIfAbsent)
                                    p.val = value;
                            }
                        }
                    }
                }
                if (binCount != 0) {
                    if (binCount >= TREEIFY_THRESHOLD)
                        treeifyBin(tab, i);
                    if (oldVal != null)
                        return oldVal;
                    break;
                }
            }
        }
        addCount(1L, binCount);
        return null;
    }
}
```

위의 코드는 ConcurrentHashMap 클래스의 일부 API 입니다. ConcuurentHashMap에는 Hashtable 과는 다르게 synchronized 키워드가 메소드 전체에 붙어 있지 않습니다. get() 메소드에는 아예 synchronized가 존재하지 않고, put() 메소드에는 중간에 synchronized 키워드가 존재하는 것을 볼 수 있습니다. 

이것을 좀 더 정리해보면 `ConcurrentHashMap은 읽기 작업에는 여러 쓰레드가 동시에 읽을 수 있지만, 쓰기 작업에는 특정 세그먼트 or 버킷에 대한 Lock을 사용한다는 것입니다.`

```java
public class ConcurrentHashMap<K,V> extends AbstractMap<K,V>
    implements ConcurrentMap<K,V>, Serializable {

    private static final int DEFAULT_CAPACITY = 16;

    // 동시에 업데이트를 수행하는 쓰레드 수
    private static final int DEFAULT_CONCURRENCY_LEVEL = 16;
}
```

ConcurrentHashMap 클래스를 보면 위와 같이 `DEFAULT_CAPACITY`, `DEFAULT_CONCURRENCY_LEVEL`가 16으로 설정되어 있습니다. 
`DEFAULT_CAPACITY`는 HashMap에서 보았듯이 `버킷의 수` 입니다. 그리고 `DEFAULT_CONCURRENCY_LEVEL`는 `동시에 작업 가능한 쓰레드 수`라고 생각합니다.

`버킷의 수 == 동시작업 가능한 쓰레드 수`인 이유는 위에서 말했던 것처럼 `ConcurrentHashMap은 버킷 단위로 lock을 사용하기 때문에 같은 버킷만 아니라면 Lock을 기다릴 필요가 없다는 특징이 있습니다.(버킷당 하나의 Lock을 가지고 있다라고 생각하면 될 것 같습니다.)`

즉, `여러 쓰레드에서 ConcurrentHashMap 객체에 동시에 데이터를 삽입, 참조하더라도 그 데이터가 다른 세그먼트에 위치하면 서로 락을 얻기 위해 경쟁하지 않습니다.`

<br>

### `ConcurrentHashMap은 어떻게 동기화를 하는 것일까요?`

그러기 위해서 `put()` 메소드를 조금 더 상세히 알아보겠습니다. 

![스크린샷 2021-02-11 오후 5 12 19](https://user-images.githubusercontent.com/45676906/107613945-af778900-6c8c-11eb-8f1b-5b0f8bce4bcb.png)

위의 코드를 정확하게는 아직 이해하기가 쉽지 않습니다.ㅠㅠ 하지만 위의 코드는 `빈 버킷에 노드를 삽입하는 과정`이라는 것은 알 수 있습니다. 이 과정에서는 [Compare and Swap](http://tutorials.jenkov.com/java-concurrency/compare-and-swap.html) 방식을 이용하여 새로운 노드를 해서 버킷에 삽입합니다. 
(Java에는 synchronized 말고도 다른 동기화 방식이 있는데 그 중에 하나를 사용한 것입니다. 이것도 정리해야지..)

![스크린샷 2021-02-11 오후 5 31 57](https://user-images.githubusercontent.com/45676906/107615378-670d9a80-6c8f-11eb-8a76-766e7595c0fc.png)

그리고 synchronized 키워드가 붙어 있습니다. 이 부분이 `버킷에 이미 노드가 존재하는 경우`에 실행되는 코드입니다. 
`synchronized(노드가 존재하는 해시 버킷 객체)`를 이용해서 하나의 쓰레드만 접근할 수 있도록 제어합니다.

<br>

## `ConcurrentHashMap 생성자`

![스크린샷 2021-02-11 오후 5 44 32](https://user-images.githubusercontent.com/45676906/107616159-d932af00-6c90-11eb-81b6-fb32864bdb81.png)

생성자 중의 위와 같은 생성자가 있습니다. 위에서 보았듯이 `conrrencyLevel`이 존재합니다. DEFAULT는 16이지만 이렇게 직접 지정할 수도 있습니다.(동시에 몇 개의 쓰레드를 작동하게 할 것인가에 대한 것을 정하는 것 같습니다.) 

ConcurrentHashMap은 `다른 버킷이라면 동시에 쓸 수 있다는 특징을 가지고 있다고 했습니다.` 그래서 데이터의 각 영역이 서로 영향을 주지 않는 작업에 대해서는 경쟁이 발생하지 않기 때문에 여러 쓰레드에서 빈번하게 접근하더라도 락 획득을 위한 대기 시간을 많이 줄일 수 있습니다. 

물론 효과를 극대화하기 위해서는 상황에 따라 적절히 세그먼트룰 나누는 것이 필요합니다. 데이터를 너무 적은 수의 조각으로 나누면 경쟁을 줄이는 효과가 적을 것이고 너무 많은 수의 조각으로 나누면 이 세그먼트를 관리하는 비용이 커지기 때문입니다.

<br>

## `ConcurrentHashMap 언제 사용해야 할까요?`

읽기 작업보다는 쓰기 작업에 성능이 중요한 상황에서 쓰면 적합한 것 같습니다.(같은 버킷만 아니라면 여러 쓰레드가 동시에 쓰는 작업을 할 수 있기 때문에)

<br>

### `읽기 작업에는 동기화가 적용이 안되는데 어떻게 진행될까요?`

`검색(get())`에는 동기화가 적용되지 않으므로 업데이트 `작업(put() or remove()`)과 겹칠 수 있습니다. 그래서 검색은 가장 최근에 완료된 업데이트 작업의 결과가 반영됩니다.

<br>

## `가변 배열 리사이징`

HashMap에서 버킷안에 노드가 `로드팩터` 값에 도달하게 되면 단순히 resize() 메소드를 통해 새로운 배열을 만들어 copy 하는 방식을 사용합니다. 
하지만 ConcurrentHashMap 에서는 기존 테이블을 새로운 테이블로 `버킷을 하나씩 전송(transfer) 하는 방식을 사용합니다.` 이 과정에서 다른 쓰레드가 버킷 전송을 같이 할 수도 있습니다. Transfer가 모두 끝나면 크기가 2배인 새로운 테이블이 됩니다.
 
![스크린샷 2021-02-11 오후 6 03 55](https://user-images.githubusercontent.com/45676906/107617623-958d7480-6c93-11eb-91b7-500dbe4b9d54.png)


<br> <br>

# `Reference`

- [https://www.baeldung.com/java-synchronizedmap-vs-concurrenthashmap](https://www.baeldung.com/java-synchronizedmap-vs-concurrenthashmap)
- [https://pplenty.tistory.com/17](https://pplenty.tistory.com/17)
- [http://agbird.egloos.com/4849046](http://agbird.egloos.com/4849046)