# `컬렉션(Map)에 대해서`

![Collection](https://blog.kakaocdn.net/dn/5x8kY/btqFkI7RILS/tQFVC0vhg8CS4eIzorgeAk/img.png)

이번 글에서는 `컬렉션 프레임워크`에서 `Map 인터페이스`에 대해서 알아보겠습니다. 
Map 인터페이스는 Collection 인터페이스를 상속 받지 않고 별도로 구성되어 있습니다. 

```java
public interface Map<K, V> {
}
```

그래서 위와 같은 형태를 가지고 있습니다. 그러면 Map 특징에 대해서 알아보겠습니다. 

- `Map의 Key는 중복될 수 없습니다.` 
- 키가 없이 값만 저장될 수는 없습니다.
- 값은 중복되어도 상관 없습니다. 

Map 인터페이스를 구현하고 있는 대표적인 클래스는 `HashMap`, `TreeMap`, `LinkedHashMap`이 있습니다. 
각 클래스들에 대한 설명과 어떤 메소드들을 가지고 있는지는 설명하지 않겠습니다. 

그리고 `Hashtable` 클래스에도 존재합니다.  `Hashtable`과 `HashMap`의 관계는 `Vector`와 `ArrayList`의 관계와 같습니다. 한마디로 `Hashtable`은 여러 쓰레드에 대해 안전하고, `HashMap`은 여러 쓰레드에 대해 안전하지 않다는 것입니다. 
하지만 `Hashtable` 보다 더 새로운 버전인 `HashMap`을 사용할 것을 권합니다. 

이번 글에서는 Map 인터페이스와 그 하위 클래스들의 메소드들에 대해서 알아보는 것보다는 `해싱과 해시함수`에 대해서 정리해보려 합니다. 

<br>

## `해싱과 해시함수란?`

해싱이란 해시함수(hash function)을 이용해서 데이터를 해시테이블(hash table)에 저장하고 검색하는 기법을 말합니다. 

![hash](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FcuOw7P%2FbtqAUu6aLu3%2FBSYlS3hrlPYm4Yikfvb94k%2Fimg.png)

- `해시 함수(hash table) : 키를 입력으로 받아 해시 주소(hash address)를 생성하고 이 해시 주소를 해시 테이블(hash table)의 인덱스로 사용`
- `해싱(hashing) : 해시 테이블을 이용한 탐색`

위와 같이 keys를 해시 함수에 넣으면 해시 테이블의 인덱스로 사용하고 버킷에 key들을 채워 넣습니다. 

해시함수는 데이터가 저장되어 있는 곳을 알려 주기 때문에 다량의 데이터 중에서도 원하는 데이터를 빠르게 찾을 수 있습니다. (시간 복잡도 O(1))

`하지만 항상 이렇게 이상적으로만 작동하지는 않습니다.`

![slot](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FDKRDL%2FbtqARiMCm5g%2FasMxWkWuBK1mM8WsutkR21%2Fimg.png)

해시 테이블을 보면 위와 같이 버킷 당 여러 개의 슬롯을 가지고 있습니다. 이처럼 만약에 키를 해시 함수에 넣었는데 `동일한 버킷에 들어간다면 어떻게 될까요?`

![over](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2Fn4EZ4%2FbtqASzNILPu%2FiktZQ9JZwyrJc3uSamCeJK%2Fimg.png)

그러면 위와 같이 `버킷에 슬롯들이 연결리스트 형태로 쌓일 것입니다.` 만약 슬롯이 꽉 찼다면 위와 같은 `오버플로우`의 상황도 발생합니다.

연결리스트는 검색에 좋지 않은 자료구조라는 것은 알고 있을 것입니다.(Head 부터 찾아서 가야하니까..) 따라서 위와 같이 버킷당 슬롯에 연결리스트 처럼 쌓인다면 검색 시간이 O(1)이 아니라 O(n)이 될 것입니다. 

따라서 하나의 버킷에 하나의 슬롯만 저장하는 것이 제일 좋습니다. 그러려면 `해시테이블의 크기도 적절하게 지정해주어야 하고, 해시함수가 서로 다른 키에 대해서 중복된 해시코드의 반환을 최소화해야 합니다.`

그래서 `해싱을 구현하는 과정에서 제일 중요한 것은 해시함수의 알고리즘 입니다.` 해시 함수의 알고리즘을 단순하게 한다면 서로 다른 키에 대해서 중복된 해시코드를 반환하는 경우가 많습니다. 

실제로 HashMap과 같이 해싱을 구현한 컬렉션 클래스에서는 Object 클래스에 정의된 `hashCode()`를 해시함수로 사용합니다. 
`Object 클래스에 정의된 hashCode()는 객체의 주소를 이용하는 알고리즘으로 해시코드를 만들어 내기 때문에 모든 객체에 대해 hashCode()를 호출한 결과가 서로 유일한 훌룡한 방법`입니다. 

<br>

## 정리하기

- 서로 다른 두 객체에 대해 equals()로 비교한 결과가 true인 동시에 hashCode()의 반환값이 같아야 같은 객체로 인식합니다.
- equals()를 오버라이딩해야 한다면 hashCode()도 같이 재정의해서 equals()의 결과가 true인 두 객체의 해시코드의 결과 값이 항상 같도록 해주어야 합니다.


