# `TreeSet이란?`

TreeSet은 Tree 구조를 하면서 정렬되어 있습니다. 

```java
public class TreeSet<E> extends AbstractSet<E>
    implements NavigableSet<E>, Cloneable, java.io.Serializable {}
```

TreeSet은 `AbstractSet`을 확장하고 있고, `NavigableSet` 인터페이스를 구현하고 있는 것을 볼 수 있습니다. 

그리고 TreeSet이 Tree 구조이면서 정렬된 상태를 유지하면서 원소들을 저장한다는 것도 있고, 다른 특징을 요약하면 아래와 같습니다. 

- `중복을 허용하지 않습니다.`
- `원소의 삽입 순서를 보존하지 않습니다.(즉, 삽입한대로 들어가지 않고 내부적인 구조에 따라서 정렬된 트리 구조를 만듭니다.)`
- `요소를 오름차순으로 정렬합니다.`
- `Thread-Safe 하지 않습니다.`

그리고 `TreeSet`은 내부적으로 어떤 자료구조를 사용하고 있을까요? 바로 `레드블랙트리(Red-Black-Tree)`입니다. 레드블랙트리가 무엇인지 궁금하다면 [여기](https://devlog-wjdrbs96.tistory.com/159?category=882242) 에서 확인하면 됩니다.

<br>

### `TreeSet 내부 코드 알아보기`

```java
public class TreeSet<E> extends AbstractSet<E>
    implements NavigableSet<E>, Cloneable, java.io.Serializable
{
    private transient NavigableMap<E,Object> m;

    public TreeSet() {
        this(new TreeMap<E,Object>());
    }

    public TreeSet(Comparator<? super E> comparator) {
        this(new TreeMap<>(comparator));
    }
}
```

TreeSet 내부코드 중 일부 생성자만 가져왔습니다. HashSet과는 다르게 `NavigableMap`을 사용하고 있는 것을 알 수 있습니다. 
(자세히는 모르겠지만 SortedSet 인터페이스를 확장하고 있는 것을 보아 정렬과 관련있는 Map의 구조인 것 같습니다.)

```
Set<String> treeSet = new TreeSet<>(Comparator.comparing(String::length));
```

그리고 위와 같이 정렬되는 기준을 직접 정해서 생성자 매개변수에 넘겨줄 수도 있습니다. 

`TreeSet은 Thread-Safe 하지 않지만`,  Collections를 사용하여 외부에서 동기화를 할 수 있습니다. 

```
Set<String> syncTreeSet = Collections.synchronizedSet(treeSet);
```

<br>

## `Element Null 저장 여부`

자바 7 이전에는 `TreeSet`에 null을 저장하는 것이 가능했습니다. 하지만 이로인해 버그가 발생하는 이슈가 있어 더 이상 null을 저장할 수 없습니다. 

```java
public class Test {
    public static void main(String[] args) {
        TreeSet<Integer> treeSet = new TreeSet<>();
        treeSet.add(1);
        treeSet.add(null);
    }
}
```

위와 같이 null을 저장하게 되면 내부적으로 레드블랙트리 구조를 만들 때 `1.compare(null)`과 같은 상황이 되기 때문에 `NullPointerException`이 발생합니다.

<br>

## `TreeSet 성능`

TreeSet은 정렬을 해서 저장하기 때문에 HashSet 보다는 성능이 떨어집니다. 그리고 `삽입, 삭제, 탐색은 평균 시간복잡도 O(logN)이 걸리게 됩니다.` 

하지만 원소 하나하나 출력하는 것은 O(n)의 시간복잡도를 가지게 됩니다.

<br>

> ### 지역성의 원리 <br>
> 
> 메모리 액세스 패턴에 따라 동일한 값 또는 관련 저장 위치에 자주 액세스하는 현상을 말합니다.

- 두 원소가 가까운 곳에 순서가 지정되면 TreeSet은 데이터 구조에서 두 항목을 서로 가깝게 배치하여 메모리에 저장합니다.
- 따라서 TreeSet는 지역성이 더 큰 데이터 구조로, 메모리가 부족하고 자연 순서에 따라 서로 상대적으로 가까운 요소에 접근하려는 경우 TreeSet을 사용하는 것이 좋습니다. 
- 하드 드라이브에서 데이터를 읽어야 하는 경우(캐시 또는 메모리에서 읽은 데이터보다 지연 시간이 더 긴 경우) 지역성이 높으므로 TreeSet를 선호합니다.

<br>

## `TreeSet 시간 복잡도 정리`

| Set | 삽입 | 삭제 | 검색 |
|---|------|-----|-----|
|TreeSet| O(logN) | O(logN) | O(logN) |
