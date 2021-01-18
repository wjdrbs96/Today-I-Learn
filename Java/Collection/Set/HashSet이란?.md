# `HashSet이란?`

```java
public class HashSet<E>
    extends AbstractSet<E>
    implements Set<E>, Cloneable, java.io.Serializable {}
```

HashSet은 Set 인터페이스를 구현하고 있는 가장 대표적인 클래스입니다. 이번 글에서는 HashSet 클래스가 내부적으로 어떻게 동작하고 어떤 API들이 있는지에 대해서 알아보겠습니다. 

먼저 Set의 특징을 정리해보겠습니다. 

- `중복되지 않은 원소들을 저장하고 null을 허용합니다.`
- `내부적으로 Map을 사용합니다.`
- `순서를 유지하지 않습니다.`
- `Thread-Safe 하지 않습니다.`

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
}
```

위와 같이 `HashSet` 객체를 만들면 내부적으로 `HashMap` 객체를 만들어서 사용합니다. 

> Constructs a new, empty set; the backing <tt>HashMap</tt> instance has default initial capacity (16) and load factor (0.75).

그리고 문서에도 위와 같이 적혀있는데 해석해보면 `기본 초기 용량 = 16, 로드팩터 = 0.75`라고 하는 것을 볼 수 있습니다. 

<br>

### `로드팩터란 무엇일까요?`

- `로드팩터는 (데이터의 개수)/(저장공간)을 의미합니다.`

데이터의 개수가 증가해서 로드팩터의 값이 원래 로드팩터의 값보다 커지게 되면 `저장 공간의 크기는 증가되고 해시 재정리 작업(refresh)`을 해야만 합니다.

로드팩터라는 값이 클수록 공간은 넉넉해지지만, 데이터를 찾는 시간은 증가합니다.   

따라서 `초기 공간 개수와 로드 팩터는 데이터의 크기를 고려하여 산정하는 것이 좋습니다.` (즉, 로드팩터는 데이터를 효율적으로 관리하기 위해서 처음에 설정하는 값이라고 생각하면 됩니다.)

그리고 `데이터가 어느정도 쌓이면 버킷 사이즈를 resized 해야할 지에 대한 기준이라고 생각할 수 있습니다`

왜냐하면 `초기크기가 (데이터의 개수)/(로드 팩터) 보다 클 경우에는 데이터를 쉽게 찾기 위한 해시 재정리 작업이 발생하지 않기 때문`입니다. 

따라서 대량의 데이터를 여기에 담아 처리할 때에는 초기 크기와 로드 팩터의 값을 조절해 가면서 가장 적당한 크기를 찾아야만 합니다. 

`(자세한 내용은 아래를 참고)`

<br>

그리고 이제 HashSet API들을 몇 개 살펴보겠습니다. 

<br>

## `add() 메소드`

```java
public class HashSet<E>
    extends AbstractSet<E>
    implements Set<E>, Cloneable, java.io.Serializable
{
    private transient HashMap<E,Object> map;

    private static final Object PRESENT = new Object();

    public boolean add(E e) {
        return map.put(e, PRESENT)==null;
    }
}
```

add() 메소드 내부를 보면 Map을 이용해서 데이터를 넣는데 데이터가 없을 경우에 `true`, 데이터가 존재한다면 `false`를 반환하는 것을 볼 수 있습니다.

<br>

### `요약하자면?`

- `해시 맵은 기본 용량이 16개 요소인 버킷의 배열로, 각 버킷은 다른 해시 코드 값에 해당합니다.`
- `여러 개체가 동일한 해시 코드 값을 갖는 경우 단일 버킷에 저장됩니다.`
- `로드 팩터에 도달하면 새 배열이 이전 배열의 두 배 크기로 생성되고 모든 요소가 새 해당 버킷으로 재할당됩니다.`
- `값을 검색하려면 키를 해시 하고, 해당 버킷으로 찾아갑니다. 만약 해당 버킷에 둘 이상의 개체들이 있다면 LinkedList 탐색하듯이 탐색하게 됩니다.`

<br>

## `그러면 어떻게 Set은 중복되지 않은 원소들을 유지할 수 있을까?`

```java
public class HashSet<E>
    extends AbstractSet<E>
    implements Set<E>, Cloneable, java.io.Serializable
{
    private transient HashMap<E,Object> map;

    private static final Object PRESENT = new Object();

    public boolean add(E e) {
        return map.put(e, PRESENT)==null;
    }
}
```

개체를 해시 집합에 넣으면 개체의 해시 코드 값을 사용하여 요소가 집합에 있는지 없는지 확인합니다. 위에 실제 HashSet 클래스를 보면 내부적으로 Map을 사용하고
put을 할 때 value 자리에 Object 값을 넣어주는 것을 볼 수 있습니다. 

해시 코드로 계산된 해시 값은 특정 버킷 위치에 해당합니다. 하지만 동일한 해시 코드를 가진 두 개체가 같지 않을 수도 있습니다.

그래서 동일한 버킷에서는 `equals()` 메소드를 이용해서 체크합니다. (따라서 `equals(), hashCode() 메소드가 매우 중요합니다.`)

<br>

## `HashSet의 성능`

HashSet의 성능은 `초기 용량`, `로드 팩터`에 의해 결정됩니다. 

Set에 원소를 add 하는 과정은 일반적으로 시간복잡도 O(1)에 의해서 가능하지만, 최악의 경우는 O(n) 까지 나빠질 수 있습니다. `따라서 HashSet의 초기 용량을 설정하는 것이 중요합니다.`

따라서 `초기 용량` 설정을 처음에 잘 하는 것이 중요합니다. 한마디로 요약하면, `로드 팩터는 저장용량 대비 데이터를 이정도까지 채워야 탐색시간, 버킷 resize 등등을 효율적으로 할 수 있다`라고 미리 설정해놓는 것입니다. 

```java
public class HashSet<E>
    extends AbstractSet<E>
    implements Set<E>, Cloneable, java.io.Serializable
{
    private transient HashMap<E,Object> map;

    public HashSet() {
        map = new HashMap<>();
    }

    public HashSet(int initialCapacity, float loadFactor) {
        map = new HashMap<>(initialCapacity, loadFactor);
    }

    public HashSet(int initialCapacity) {
        map = new HashMap<>(initialCapacity);
    }
}
``` 

`HashSet` 클래스의 생성자를 보면 `초기용량`을 받는 것, `초기용량, 로드팩터`, `매개변수가 없는` 생성자들이 존재하는 것을 볼 수 있습니다. 

```
Set<String> hashset = new HashSet<>();         // 기본용량 16, 로드팩터 0.75
Set<String> hashset = new HashSet<>(20);
Set<String> hashset = new HashSet<>(20, 0.5f);
```

위와 같이 지정을 할 수 있습니다. 

<br>

## `초기 용량을 설정하는 기준`

- `초기 용량을 작게 설정하면 메모리면에서는 좋지만, 버킷사이즈를 더 크게 만드는 과정이 있기 때문에 효율적이지 못합니다.`
- `초기 용량을 크게 설정하면 초기 메모리를 소모한다는데 단점이 있습니다.`

<br>

### `원칙적으로`

- `초기 용량이 크면 반복이 거의 또는 전혀 없는 다수의 항목에 적합합니다.`
- `초기 용량이 낮으면 반복 횟수가 많은 소수의 항목에 적합합니다.`

<br>

## `마무리 결론`

> 초기용량, 로드팩터 사이의 정확한 균형을 맞추는 것이 매우 중요합니다. 일반적으로 기본 구현은 최적화되어 있으며 잘 작동하며, 요구 사항에 맞게 이러한 매개 변수를 조정해야 할 필요성을 느낄 경우 신중하게 수행해야 합니다.

<br>

# Reference

- [https://www.baeldung.com/java-hashset](https://www.baeldung.com/java-hashset)