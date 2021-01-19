# `HashMap이란?`

`HashMap`은 `Map` 인터페이스를 구현하고 있는 대표적인 클래스입니다. 그리고 Map의 구조인 `key`-`value`쌍으로 구성되어 있습니다. 

그리고 Map의 대표적인 특징은 하나의 key는 정확히 하나의 value만 가질 수 있다는 것입니다.

```java
public class HashMap<K,V> extends AbstractMap<K,V>
    implements Map<K,V>, Cloneable, Serializable {
}
```

HashMap 클래스의 내부는 위와 같은 상속, 구현 관계를 가지고 있습니다. 

이제 본격적으로 HashMap에 대해서 정리를 해보겠습니다. 

<br>

## `HashMap은 왜 필요한가?`

list 형태를 사용하지 않고 HashMap을 사용하는 이유는 `성능` 때문입니다. 만약에 HashMap을 사용하지 않고 list를 사용했다면 원소를 검색하는데 시간복잡도는 O(n)일 것입니다. 
(정렬되어 있는 원소라면 Binary Search로 O(logN)가 됩니다.)

반면에 HashMap은 `삽입`, `검색`에 시간복잡도 O(1)이라는 이점을 가지고 있습니다. 

<br>

## `세팅 코드`

```java
import java.util.List;

public class Product {

    private String name;
    private String description;
    private List<String> tags;

    // standard getters/setters/constructors

    public Product addTagsOfOtherProdcut(Product product) {
        this.tags.addAll(product.getTags());
        return this;
    }
}
```

위와 같이 코드를 세팅한 후에 `HashMap API`에 대해서 알아보겠습니다. 

<br>

## `put() Method`
 
```java
public class Test {

    public static void main(String[] args) {
        Map<String, Product> productsByName = new HashMap<>();
        Product eBike = new Product("E-Bike", "A bike with a battery");
        Product roadBike = new Product("Road bike", "A bike for competition");
        productsByName.put(eBike.getName(), eBike);
        productsByName.put(roadBike.getName(), roadBike);
    }
}
```

위와 같이 `put()`을 이용해서 `key`-`value`를 세팅할 수 있습니다. 

<br>

## `get() Method`

```java
public class Test {

    public static void main(String[] args) {
        Map<String, Product> productsByName = new HashMap<>();
        Product eBike = new Product("E-Bike", "A bike with a battery");
        Product roadBike = new Product("Road bike", "A bike for competition");

        productsByName.put(eBike.getName(), eBike);
        productsByName.put(roadBike.getName(), roadBike);

        Product product = productsByName.get("E-Bike");
        Product road_bike = productsByName.get("Road bike");
    }
}
```

`get()`을 통해서 `key`-`value`로 저장되어 있는 것을 꺼내올 수 있습니다. (없는 key를 찾는다면 null을 반환합니다.)

<br>

## `key로 Null이 가능`

```java
public class Test {

    public static void main(String[] args) {
        Map<String, Product> productsByName = new HashMap<>();
        Product eBike = new Product("E-Bike", "A bike with a battery");

        productsByName.put(null, eBike);
        Product product = productsByName.get(null);
        System.out.println(product.name);    // E-Bike

    }
}
```

`put()` 메소드를 통해서 저장을 할 때 `null`도 가능한 것을 볼 수 있습니다.

<br>

## `remove() Method`

```java
public class Test {

    public static void main(String[] args) {
        Map<String, Product> productsByName = new HashMap<>();
        Product roadBike = new Product("Road bike", "A bike for competition");

        productsByName.put(roadBike.getName(), roadBike);
        productsByName.remove(roadBike.getName());

        Product product = productsByName.get(roadBike.getName());
        System.out.println(product);   // null 

    }
}
```

<br>

## `Check If a Key or Value Exists in the Map`

```
productsByName.containsKey("E-Bike");  
```

containsKey()를 통해서 현재 해당 키가 존재하는지 여부를 확인 가능합니다. 

```
productsByName.containsValue(eBike);
```

또한 containsValue()를 통해서 현재 해당 값이 존재하는지 여부를 확인할 수 있습니다. 

여기서 `containsKey()`, `containsValue()` 메소드의 차이점이 존재합니다. 

- key가 존재하는지 여부를 확인하는 메소드인 `constainsKey()`는 시간복잡도 O(1)에 확인할 수 있습니다. 
- 반면에 value가 존재하는지 여부를 확인하는 메소드인 `containsValue()`는 시간복잡도 O(n)에 확인할 수 있습니다.(값을 확인하기 위해서 모든 원소를 순회해야 하기 때문입니다.)

<br>

## `Key는 equals와 hashCode가 중요하다.`

Map에서 key를 올바르게 사용을 하기 위해서는 `equals()`, `hashCode()`를 오버라이딩 해서 사용해야 합니다. (key를 가지고 버킷을 찾아가고, 충돌이 난다면 같은지 다른지 비교를 하는 과정이 있기 때문에..)

<br>

## `HashMap 내부 동작원리`

이번 글에서 가장 중요한 부분이 아닐까 싶습니다.. ! 

아까 위에서도 list를 사용하지 않고 HashMap을 사용하는 이유는 `시간복잡도가 리스트에서 O(n) -> 해시 O(1)`로 효율적으로 사용할 수 있기 때문이라 했습니다. 

리스트를 사용했을 때의 단점을 하나 더 말해보겠습니다. 

만약 알파벳을 사용한다면 총 26개의 공간이 필요하기 때문에 큰 문제가 되지는 않습니다. 하지만 정수를 사용한다면 `2,147,483,647`의 공간이 필요합니다. 
전부 다 필요하지도 않을텐데 초기에 메모리 낭비가 너무 심할 것입니다. 

`하지만 HashMap을 사용하면 버킷이라고 불리는 곳에 put() 메소드가 호출이 되었을 때 저장하게 됩니다.(이 때 버킷은 hashCode() 메소드를 사용해서 위치를 결정합니다.)` 

그리고 버킷 내에서 equals를 통해서 같은지 다른지 여부를 판단합니다. 

<br>

## `Key의 불변성`

Map을 사용할 때 불변 Key를 사용해야 합니다. 

```java
public class MutableKey {
    private String name;

    // standard constructor, getter and setter

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MutableKey that = (MutableKey) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
```
```java
import java.util.HashMap;
import java.util.Map;

public class Test {
    public static void main(String[] args) {
        MutableKey key = new MutableKey("initial");

        Map<MutableKey, String> items = new HashMap<>();
        items.put(key, "success");

        key.setName("changed");
    }
}
```

위와 같이 Map에 MutableKey 클래스를 key로 지정하고 setter를 통해서 key를 바꾼다면 어떻게 될까요?

`get()` 메소드를 통해서 `MutableKey`를 참으려 할 때 `name`으로 해시코드를 만들어서 버킷을 찾아갈 것입니다. 하지만 name이 바뀌었기 때문에 원래 버킷이 아닌 다른 버킷으로 찾아가게 됩니다. 

그래서 결과는 get() 메소드를 통해서 값을 꺼내면 `null`을 반환하게 됩니다. (존재하지 않는 버킷에서 찾았으니까..)

<br>

## `해시 충돌(Collisions)`

올바르게 작동하려면, 동일한 키는 같은 해시(hash) 값을 가져야 합니다. 하지만 다른 키도 같은 해시(hash) 값을 가질 수 있습니다. (이것은 버킷의 용량, 해시함수 등등에 따라 영향을 받을 것입니다.)

만약에 다른 키가 같은 해시 값을 가진다면 같은 버킷에 채워지게 됩니다. 원래는 버킷에 하나만 존재해야 시간복잡도 O(1)에 찾아올 수 있는데 `버킷에 여러 개 존재하게 되면 어떻게 될까요?`

이 때는 버킷 내부에서 `리스트` 형태를 이용하여 원소들을 관리하게 됩니다. 그래서 버킷 내부에서 원소들을 검색할 때는 반복문으로 돌아야 하기 때문에 `O(n)`의 시간이 걸리게 됩니다.

> Java 8(JEP 180 참조)에서 버킷에 8개 이상의 값이 포함되어 있으면 버킷 내부의 값이 저장된 데이터 구조가 목록에서 균형 트리로 변경되고 <br> 
> 버킷에 6개 값만 남아 있으면 다시 목록으로 변경됩니다. 이렇게 하면 성능이 O(log n)로 향상됩니다.

<br>

## `초기 용량과 로드 팩터`

HashMap의 `초기 용량은 16`, `로드팩터는 0.75` 입니다. (`로드팩터 = (데이터의 개수)/(초기 용량)`) 버킷 하나에 여러 개의 값을 가지는 것, 즉 충돌을 피하기 위해서 설정한 것입니다. 

버킷의 75퍼가 찼다면 용량을 2배로 늘리는 과정이 일어납니다. 그 과정에서 원래 버킷의 값들을 새로운 버킷에 옮기는 과정이 일어납니다. (자주 일어난다면 성능에 좋지 않습니다.)

한마디로 `로드 팩터`는 언제 용량을 재 설정을 해주어야 효율적인지에 대한 `초기 설정 값`이라고 생각하면 됩니다. (그래서 초기용량, 로드팩터를 처음에 잘 설정하는 것이 중요합니다.)

```java
public class HashMap<K,V> extends AbstractMap<K,V>
    implements Map<K,V>, Cloneable, Serializable {

    static final int DEFAULT_INITIAL_CAPACITY = 1 << 4; // aka 16

    static final int MAXIMUM_CAPACITY = 1 << 30;

    static final float DEFAULT_LOAD_FACTOR = 0.75f;

    final float loadFactor;

    public HashMap(int initialCapacity, float loadFactor) {

    }

    public HashMap(int initialCapacity) {
    }

    public HashMap() {
        this.loadFactor = DEFAULT_LOAD_FACTOR; // all other fields defaulted
    }
}
```

HashMap 내부 코드를 보면 위와 같이 `생성자`를 통해서 `초기 용량`, `로드 팩터`를 설정할 수도 있습니다. 

<br>

## `HashMap 성능`

HashMap은 초기 용량 16, 로드 팩터는 0.75가 디폴트 값이라고 위에서 말했습니다. 즉, 이 2개의 값에 따라 성능이 좌우되는데요.

- `용량 = 버킷 수`
- `로드팩터: 버킷이 얼마나 찼을 때 resize를 하는 것이 좋은지에 대한 값입니다.`

자바 팀이 설정한 기본값은 대부분의 경우에 잘 최적화되어 있습니다. 하지만, 매우 괜찮은 자신만의 값을 설정해서 사용해야 한다면, 설정한 것이 상황에 맞게 이유가 있어야 할 것입니다. 

<br>

### `용량을 설정하는 기준`

- `초기용량을 작게 설정한다면 공간 비용은 절감되지만 재할당 빈도는 증가합니다.(재할당은 매우 비용이 많이 드는 과정입니다)`
- `초기용량을 높게 설정하면 재할당 하는 과정은 별로 일어나지 않겠지만 너무 과하게 설정하면 메모리 낭비를 할 수도 있습니다.`

<br>

## `HashMap에서 충돌(Collisions)`

해시맵에서 충돌은 위에서 말했던 것처럼 하나의 버킷에 여러 개의 값을 가지고 있는 상태를 말합니다. 서로 다른 객체임에도 같은 해시코드 값을 가질 수 있기 때문에 발생하는 상황입니다. 

따라서 `초기용량 = 버킷 수`인데 버킷의 수가 적을 수록 충돌이 많이 일어나게 됩니다. (아무래도 공간이 적어지니 당연한 것 같습니다.)

그리고 충돌이 일어나면 버킷 내부에서 연결리스트 형태로 저장을 하게 됩니다. 

한번 예제 코드를 보면서 더 자세히 알아보겠습니다. 

<br>

### `예제 코드`

```java
public class MyKey {
    private String name;
    private int id;

    public MyKey(int id, String name) {
        this.id = id;
        this.name = name;
    }
    
    // standard getters and setters
 
    @Override
    public int hashCode() {
        System.out.println("Calling hashCode()");
        return id;
    } 
 
    // toString override for pretty logging

    @Override
    public boolean equals(Object obj) {
        System.out.println("Calling equals() for key: " + obj);
        // generated implementation
    }
}
```

위의 해시 코드를 보면 `id` 값을 반환하게 되어있습니다. (원래는 저렇게 구현하면 안되지만 예제를 위해서,,,)

```java
import java.util.HashMap;

public class Test {
    public static void main(String[] args) {
        HashMap<MyKey, String> map = new HashMap<>();
        MyKey k1 = new MyKey(1, "firstKey");
        MyKey k2 = new MyKey(2, "secondKey");
        MyKey k3 = new MyKey(2, "thirdKey");

        System.out.println("storing value for k1");
        map.put(k1, "firstValue");
        System.out.println("storing value for k2");
        map.put(k2, "secondValue");
        System.out.println("storing value for k3");
        map.put(k3, "thirdValue");

        System.out.println("retrieving value for k1");
        String v1 = map.get(k1);
        System.out.println("retrieving value for k2");
        String v2 = map.get(k2);
        System.out.println("retrieving value for k3");
        String v3 = map.get(k3);
    }
}
```

코드를 하나씩 분석해보면 k1, k2 까지는 문제 없이 버킷에 등록이 될 것입니다. 하지만 k3를 등록하는 과정에서 k2와 충돌이 일어나게 됩니다. 

<br>

### `충돌이 일어나는 과정에서 어떤 일이 벌어질까요?`

```
storing value for k1
Calling hashCode()
storing value for k2
Calling hashCode()
storing value for k3
Calling hashCode()
Calling hashCode()
Calling equals() for key: ExampleCode.MyKey@2
retrieving value for k1
Calling hashCode()
retrieving value for k2
Calling hashCode()
retrieving value for k3
Calling hashCode()
Calling hashCode()
Calling equals() for key: ExampleCode.MyKey@2
```

코드의 결과를 보면 위와 같습니다. 

```
storing value for k1
Calling hashCode()
storing value for k2
Calling hashCode()
storing value for k3
Calling hashCode()
```

처음에 여기까지는 해당 객체의 해시코드 값으로 버킷의 위치를 찾아야 하기 때문에 해시코드 메소드를 호출하는 것까지는 이해할 수 있습니다. 

```
Calling hashCode()
Calling equals() for key: ExampleCode.MyKey@2
```

그리고 여기서 해시코드 메소드를 한번 더 호출하고 equals() 메소드를 호출합니다. (왜 hashCode() 메소드를 한번 더 호출하는 건지는 모르겠지만,,) equals 메소드를 호출하는 무엇일까요?

`해당 버킷에 동일한 객체가 있는지 없는지를 확인하기 위해서 입니다. 동일한 객체가 있다면 현재 객체로 바꿔주고 없다면 충돌이 일어나 뒤에 리스트 형태로 연결시켜 주면 되기 떄문입니다.`

```
retrieving value for k1
Calling hashCode()
retrieving value for k2
Calling hashCode()
retrieving value for k3
Calling hashCode()
```

탐색을 할 때도 마찬가지로 해당 버킷을 찾아가는 과정이 필요하기 때문에 위와 같은 결과가 출력되는 것을 이해할 수 있습니다.

```
Calling hashCode()
Calling equals() for key: ExampleCode.MyKey@2
```

그리고 위에서 말했던 것처럼 충돌이 난 버킷에서 값을 검색하기 위해서는 리스트를 탐색해야 합니다. 따라서 리스트를 탐색하면서 `equals()` 메소드로 객체가 같은지 다른지를 확인해야 하기 때문에 equals() 메소드를 호출하는 것입니다. 

마지막으로, Java 8에서 링크된 목록은 지정된 버킷 위치의 충돌 횟수가 특정 임계값을 초과하게 되면 `균형 잡힌 이진 검색 트리`로 동적으로 대체됩니다.

이러한 변경은 충돌의 경우 저장 및 검색이 `O(log n)`에서 발생하므로 성능을 향상시킬 수 있습니다.

<br>

# `Reference`

- [https://www.baeldung.com/java-hashmap](https://www.baeldung.com/java-hashmap)
- [https://www.baeldung.com/java-hashmap-advanced#collisions-in-the-hashmap](https://www.baeldung.com/java-hashmap-advanced#collisions-in-the-hashmap)