# `아이템54 : null이 아닌, 빈 컬렉션이나 배열을 반환하라.`

```java
import java.util.ArrayList;
import java.util.List;

public class Test {
    private final List<Cheese> cheesesInStock = new ArrayList<>();

    public List<Cheese> getCheeses() {
        return cheesesInStock.isEmpty() ? null
                : new ArrayList<>(cheesesInStock);
    }
}
```

컬렉션이 비었다면 위와 같이 `null`을 반환하는 코드가 있습니다. 그러면 해당 메소드를 사용하는 클라이언트가 null 처리를 따로 해줘야 하는 번거로움이 생깁니다.

```java
public class Test {
    public static void main(String[] args) {
        Test test = new Test();
        List<Cheese> cheeses = test.getCheeses();
        if (cheeses != null && cheeses.contains(Cheese.STILTON)) {
            System.out.println("Thanks");
        }
    }
}
```

위와 같이 클라이언트에서는 방어 코드를 작성해야 합니다. null을 반환하려면 반환하는 쪽에서도 이 상황을 특별히 처리해야 해서 코드가 더 복잡해집니다. 

<br>

### `때로는 빈 컨테이너를 할당하는 데도 비용이 드니 null을 반환하는 쪽이 낫다는 주장도 있습니다.`

하지만 이는 2가지 면에서 틀린 주장입니다.

1. 성능 분석 결과 할당이 성능 저하의 주범이라고 확인되지 않는 한, 이 정도의 성능 차이는 신경 쓸 수준이 아닙니다.
2. 빈 컬렉션과 배열은 굳이 새로 할당하지 않고도 반환할 수 있습니다. (`새로 할당하지 않고도 .. 왜 일까요?`)

```java
public class Test {
    private final List<Cheese> cheesesInStock = new ArrayList<>();

    public List<Cheese> getCheeses() {
        return new ArrayList<>(cheesesInStock);   // 음,, 새로 할당한 거 아닌가 ㅠ (질문..)
    }
}
```

<br>

### `가능성은 적지만 빈 컬렉션 할당이 성능을 눈에 띄게 떨어뜨릴 수 있다면?`

매번 똑같은 빈 `불변 컬렉션`을 반환하는 것입니다. 

```java
public class Test {
    private final List<Cheese> cheesesInStock = new ArrayList<>();

    public List<Cheese> getCheeses() {
        return cheesesInStock.isEmpty() ? Collections.emptyList()
                : new ArrayList<>(cheesesInStock);
    }
}
```

emptyList 메소드가 `불변 컬렉션`의 예시입니다. 지금 코드의 예시가 매번 새로 할당하지 않는 예시의 코드일까요?

<br>

### `배열을 쓸 때도 절대 null을 반환하지 말고 길이가 0인 배열을 반환하라.`

```java
public class Test {
    private final List<Cheese> cheesesInStock = new ArrayList<>();

    public Cheese[] getCheeses() {
        return cheesesInStock.toArray(new Cheese[0]);
    }
}
```

이러한 방식이 있는데 이러한 방식이 성능을 떨어뜨린다면 길이 0짜리 배열을 미리 선언해두고 매번 그 배열을 반환하면 됩니다. 


```java
public class Test {
    private final List<Cheese> cheesesInStock = new ArrayList<>();
    private static final Cheese[] EMPTY_CHEESE_ARRAY = new Cheese[0];

    public Cheese[] getCheeses() {
        return cheesesInStock.toArray(EMPTY_CHEESE_ARRAY);
    }
}
```

