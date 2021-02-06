# `들어가기 전에`

Hashtable 클래스는 주요 메소드에 `synchronized` 키워드가 붙어 있어 `Thread-safe` 하다는 특징을 가지고 있습니다. 

하지만 `Hashtable`의 클래스는 멀티스레드 환경이 아니면 사용하기에 성능이 낮다는 특징도 있고,
멀티스레드 환경에서 사용하더라도 메소드 전체에 `synchronized`로 원자화 시키기 때문에 성능이 좋지 않다는 특징이 있습니다.

이러한 `Hashtable`의 대체 클래스가 바로 `ConcurrentHashMap` 클래스입니다. 이번 글에서는 `ConcurrentHashMap`이 무엇인지 알아보겠습니다. 

<br>

# `ConcurrentHashMap 이란?`

```java
public class ConcurrentHashMap<K,V> extends AbstractMap<K,V>
    implements ConcurrentMap<K,V>, Serializable {}
```

ConcurrentHashMap도 위와 같이 결국엔 `Map` 인터페이스를 구현하고 있는 클래스입니다. 

그런데 `ConcurrentHashMap`도 `Thread-safe` 하다는 특징을 가지고 있습니다. 그러면 `왜 Hashtable 클래스보다 성능이 더 좋을 것일까요?`
그 이유에 대해서 한번 알아보겠습니다. 

```java
public class Hashtable<K,V>
    extends Dictionary<K,V>
    implements Map<K,V>, Cloneable, java.io.Serializable {

    public synchronized boolean contains(Object value) {
    }

    @SuppressWarnings("unchecked")
    public synchronized V get(Object key) {
    }

    public synchronized V put(K key, V value) {
    }
}
```

먼저 `Hashtable` 클래스의 내부를 보면 위와 같이 메소드 전체에 `synchronized` 키워드 붙어 있는 것을 볼 수 있습니다. 즉, `읽기`, `쓰기`, `검색` 모두 lock이 걸려있습니다. 

<br>

```java
public class ConcurrentHashMap<K,V> extends AbstractMap<K,V>
    implements ConcurrentMap<K,V>, Serializable {

    public boolean containsKey(Object key) {
        return get(key) != null;
    }

    public boolean containsValue(Object value) {
    }

    public V put(K key, V value) {
        return putVal(key, value, false);
    }
}
```

위와 같이 `ConcurrentHashMap`은 메소드에 `synchronized` 키워드가 붙어 있지 않습니다. 그러면 어떻게 `Thread-safe` 하게 구현을 하는 것인지 알아보겠습니다. 

그러기 위해서는 `put() 메소드 내부의 putVal() 메소드를 보아야 합니다.`

![스크린샷 2021-02-04 오후 1 12 52](https://user-images.githubusercontent.com/45676906/106844124-0f00f200-66eb-11eb-9756-48a115d8586c.png)

처음에 빨간 네모의 for문의 내용은 다음과 같습니다. `빈 해시 버킷에 노드를 삽입하는 경우, lock 을 사용하지 않고 Compare and Swap 을 이용하여 새로운 노드를 해시 버킷에 삽입한다.(원자성 보장)`

정확하게 내부 코드를 분석하기는 쉽지 않지만 [Compare and Swap](http://tutorials.jenkov.com/java-concurrency/compare-and-swap.html) 의 과정을 통해서 `원자성`을 보장한다고 합니다. (이렇게 구현을 하면 synchronized 보다 더 효율적으로 사용할 수 있습니다.)

그리고 아래를 보면 `synchronized` 블럭이 있는 것을 볼 수 있습니다. 즉, 전체에 lock을 거는 것이 아니라 `노드가 존재하는 해시 버킷 객체`를 접근하려고 할 때 제어를 하는 것입니다. 
이러한 특징 때문에 `ConcurrentHashMap`이 `Hashtable` 보다 성능이 더 좋다고 할 수 있습니다. 

그리고 `Oracle Docs`에 따르면 아래와 같은 설명이 나옵니다. 

```
검색의 전체 동시성과 업데이트에 대한 높은 기대 동시성을 지원하는 해시 테이블입니다. 이 클래스는 Hashtable과 동일한 기능 사양을 준수하며, 각 Hashtable 방법에 해당하는 메소드 버전을 포함합니다. 
그러나 모든 작업이 스레드 안전함에도 불구하고 검색 작업은 잠금을 수반하지 않으며 모든 액세스를 방지하는 방식으로 전체 테이블을 잠글 수 있는 어떠한 지원도 없습니다. 

검색 작업(get 포함)은 일반적으로 차단되지 않으므로 업데이트 작업(put 및 remove 포함)과 겹칠 수 있습니다. 검색에는 시작 시 가장 최근에 완료된 업데이트 작업의 결과가 반영됩니다. 
(더 공식적으로, 지정된 키에 대한 업데이트 작업은 업데이트 값을 보고하는 키에 대한 (null이 아닌) 검색과 관련이 있습니다.) 

putAll 및 Clear와 같은 집계 작업의 경우 동시 검색 시 일부 항목만 삽입 또는 제거할 수 있습니다. 
```

즉, `ConcurrentHashMap에서는 읽기 작업이 차단되지 않는 반면 쓰기 작업은 특정 세그먼트 또는 버킷에 대한 잠금을 사용합니다.`

<br>

## `생성자`

```java
public class ConcurrentHashMap<K,V> extends AbstractMap<K,V>
    implements ConcurrentMap<K,V>, Serializable {

    public ConcurrentHashMap(int initialCapacity,
                             float loadFactor, int concurrencyLevel) {
    }
}
```

`ConcurrentHashMap`에는 위와 같은 생성자가 존재합니다. 여기서 `concurrencyLevel`는 생소한데 어떤 의미를 가지고 있을까요?

> "동시에 업데이트를 수행하는 예상 스레드 수" 라고 주석에 적혀 있지만, 구현시 이 값은 단순히 초기 테이블 크기를 정하는데 힌트로만 사용된다

<br>

## `가변 배열 리사이징`

![스크린샷 2021-02-04 오후 1 41 12](https://user-images.githubusercontent.com/45676906/106845618-a6b40f80-66ee-11eb-878c-d1e3f1940b41.png)

> HashMap 에서의 리사이징은 단순히 resize() 함수를 통해 새로운 배열(newTab)을 만들어 copy 하는 방식이다. <br>
> ConcurrentHashMap 에서는 기존 배열(table) 새로운 배열(nextTable) 로 버킷을 하나씩 전송(transfer) 하는 방식이다. <br>
> 
> 이 과정에서 다른 스레드가 버킷 전송에 참여할 수도 있다. 전송이 모두 끝나면 크기가 2배인 nextTable 이 새로운 배열이 된다. <br>
> 변수 sizeCtl 과 resizeStamp 메서드를 통해 resizing 과정이 중복으로 일어나지 않도록 방지한다. <br>

<br>

## `Reference`

- [https://pplenty.tistory.com/17](https://pplenty.tistory.com/17)
- [https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/concurrent/ConcurrentHashMap.html](https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/concurrent/ConcurrentHashMap.html)
