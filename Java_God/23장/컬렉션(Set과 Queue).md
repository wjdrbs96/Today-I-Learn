# `컬렉션(Set과 Queue)에 대해서`

![Collection](https://blog.kakaocdn.net/dn/5x8kY/btqFkI7RILS/tQFVC0vhg8CS4eIzorgeAk/img.png)

이번 글에서는 `컬렉션 인터페이스` 중에 하나인 `Set` 인터페이스와 `Queue` 인터페이스에 대해서 알아보겠습니다.

<br>

## `Set 인터페이스`

Set은 집합이랑 같습니다. 따라서 `순서에 상관 없이`, 어떤 데이터가 존재하는지를 확인하기 위한 용도로 많이 사용됩니다. 
집합의 특징과 마찬가지로 `중복되는 것을 방지하고 원하는 값이 포함되어 있는지를 확인하는 것이 주 용도입니다.`

Set 인터페이스를 구현한 주요 클래스는 `HashSet`, `TreeSet`, `LinkedHashSet`이 있습니다. 
클래스 각각의 특징은 아래와 같습니다.

- `HashSet : 순서가 전혀 필요 없는 데이터를 해시 테이블(hash table)에 저장합니다. Set 중에 가장 성능이 좋습니다.`
- `TreeSet : 저장된 데이터 값에 따라서 졍렬되는 셋입니다. red-black이라는 트리 타입으로 값이 저장되며, HashSet 보다 약간 성능이 느립니다.`
- `LinkedHashSet : 연결된 목록 타입으로 구현된 해시 테이블에 데이터를 저장한다. 저장된 순서에 따라서 값이 정렬된다. 성능이 이 셋 중에서 가장 나쁩니다.`

<br>

### `HashSet 클래스에 대해서`

```java
public class HashSet<E>
    extends AbstractSet<E>
    implements Set<E>, Cloneable, java.io.Serializable
{
    private transient HashMap<E,Object> map;
   
    private static final Object PRESENT = new Object();

    public HashSet() {
        map = new HashMap<>();
    }

    public boolean add(E e) {
        return map.put(e, PRESENT)==null;
    }
}
```

HashSet 클래스의 일부분을 보면 위와 같습니다.  

그리고 Set은 무엇보다도 데이터가 중복되는 것을 허용하지 않으므로, 데이터가 같은지를 확인하는 작업은 Set의 핵심입니다. 따라서 `equals()` 메소드와 `hashCode()` 메소드를 구현하는 것은 Set에서 매우 중요합니다. 

그래서 `Set<Integer> set = new HashSet<>()`와 같이 HashSet 객체를 만들면 내부적으로 `HashMap()`을 사용하기 때문에 데이터의 중복을 처리할 수 있습니다. 

([equals 메소드와 hashCode 메소드란?](https://github.com/wjdrbs96/Today-I-Learn/blob/master/Java/Java_Class/equals%2C%20hashCode%EB%9E%80%3F.md))

- [equals에 대해](https://github.com/delicious-tangerine/effective-java/blob/master/3%EC%9E%A5/item10.md)
- [hashCode에 대해](https://github.com/delicious-tangerine/effective-java/blob/master/3%EC%9E%A5/item11.md)

<br>

## `Queue 인터페이스`

Queue는 선입 선출의 자료구조 입니다. 대표적으로 프린트 작업이 있습니다. 먼저 프린트 예약이 들어가면 그 프린트가 먼저 나오게 되는 것이죠.

Java 에서는 Queue를 구현할 때 `LinkedList` 클래스를 이용합니다. `LinkedList` 클래스가 무엇인지 알아보겠습니다.

```java
public class LinkedList<E>
    extends AbstractSequentialList<E>
    implements List<E>, Deque<E>, Cloneable, java.io.Serializable {
}
```

이러한 상속 구조를 가지고 있고 `Deque` 인터페이스의 상위 인터페이스가 `Queue`이므로 `Queue` 인터페이스를 구현하는 클래스는 LinkedList가 됩니다. 
한마디로 LinkedList는 List 이면서 Queue, Deque도 됩니다. 

`LinkedList` 클래스는 List 인터페이스를 보면서도 봤던 클래스 입니다. 

![linkedList](https://miro.medium.com/max/1332/1*JG-58S8EMxVXrk7cKAaK8w.png)

위와 같은 형태를 갖고 있는 자료구조 입니다. [배열과 LinkedList의 차이](https://devlog-wjdrbs96.tistory.com/64?category=882228) 는 여기서 자세히 확인할 수 있습니다. 
LinkedList에 대해서 간단히 요약하자면 아래와 같은 특징을 가지고 있습니다.

- 중간에 있는 데이터가 지속적으로 삭제되고 추가될 때 효율적인 자료구조입니다.
- 탐색을 할 때는 Head 부터 찾아야 하기 때문에 시간이 배열에서 인덱스로 찾는 것 보다는 더 걸립니다.

