# `전통적인 for 문보다는 for-each 문을 사용하라`

- for 문에서 발생할 수 있는 문제점을 for-each로 해결할 수 있다. 
    - for-each는 반복자와 인덱스를 사용하지 않기 때문에 코드가 깔끔해지고 오류가 날 일도 없습니다.
    - 하나의 관용구로 컬렉션, 배열을 모두 처리할 수 있어서 편리합니다.
 
```
for (Element e : elements) {
    ... // logic 
}
```

<br>

## `for문의 문제점`

```java
enum Suit { CLUB, DIAMOND, HEART, SPADE }
enum Rank { ACE, DEUCE, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, TEN, JACK, QUEEN, KING }

public class Test {
    static Collection<Suit> suits = Arrays.asList(Suit.values());
    static Collection<Rank> ranks = Arrays.asList(Rank.values());
    
    public static void main(String[] args) {
        List<Card> deck = new ArrayList<>();
        for (Iterator<Suit> i = suits.iterator(); i.hasNext();) {
            for (Iterator<Rank> j = ranks.iterator(); j.hasNext();) {
                deck.add(new Card(i.next(), j.next()));
            }
        }
    }
}
```

위의 코드에는 버그가 있습니다. 무엇일까요? 바로 i.next()가 너무 많이 호출된다는 문제점인데요. 이중 for 문 이기 때문에 가장 바깥 for 문이 한 번 돌 때 안에 for 문에서 계속 i.next()가 
호출 됩니다.

그래서 나중에 `NoSuchElementException`이 발생하게 되는 상황이 생깁니다.. for 문은 이러한 단점도 가지고 있습니다. 

<br>

## `이러한 문제는 for-each로 쉽게 해결할 수 있습니다.`

```java
public class Test {
    public static void main(String[] args) {
        for (Suit suit : suits) {
            for (Rank rank : ranks) {
                deck.add(new Card(suit, rank));
            }
        }
    }
}
```

위와 같이 간결하게 사용할 수 있습니다. 

<br>

## `하지만 for-each 문을 사용할 수 없는 상황이 세 가지 존재합니다.`

- 파괴적인 필터링 : 컬렉션을 순회하면서 선택된 원소를 제거해야 한다면 반복자의 remove 메소드를 호출해야 할 때
- 변형 : 리스트나 배열을 순회하면서 그 원소의 값 일부 혹은 전체를 교체해야 할 때 
- 병렬 반복 - 여러 컬렉션을 병렬로 순회해야 할 때

<br>

### `for-each 문은 컬렉션과 배열은 물론 Iterable 인터페이스를 구현한 객체라면 무엇이든 순회할 수 있다.`

```java
public interface Iterable<T> {
    /**
     * Returns an iterator over elements of type {@code T}.
     *
     * @return an Iterator.
     */
    Iterator<T> iterator();

    /**
     * Performs the given action for each element of the {@code Iterable}
     * until all elements have been processed or the action throws an
     * exception.  Unless otherwise specified by the implementing class,
     * actions are performed in the order of iteration (if an iteration order
     * is specified).  Exceptions thrown by the action are relayed to the
     * caller.
     *
     * @implSpec
     * <p>The default implementation behaves as if:
     * <pre>{@code
     *     for (T t : this)
     *         action.accept(t);
     * }</pre>
     *
     * @param action The action to be performed for each element
     * @throws NullPointerException if the specified action is null
     * @since 1.8
     */
    default void forEach(Consumer<? super T> action) {
        Objects.requireNonNull(action);
        for (T t : this) {
            action.accept(t);
        }
    }
}
``` 

