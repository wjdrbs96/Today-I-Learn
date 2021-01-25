# `Hashtable 이란?`

Hashtable 클래스는 컬렉션 프레임웍이 만들어지기 이전부터 존재하던 것이기 때문에 컬렉션 프레임워의 명명법을 따르지 않습니다.

`Vector`나 `Hashtable`과 같은 기존의 컬렉션 클래스들은 호환을 위해, 설계를 변경해서 남겨두었지만 가능하면 사용하지 않는 것이 좋습니다. 
(`대신 ArrayList와 HashMap을 사용하는 것이 좋습니다.`)

`Hashtable`는 자바에서 해시 테이블을 구현한 클래스 중 가장 오래되었습니다. 그리고 두 번째로 구현한 클래스는 `HashMap` 클래스입니다. 

즉, 일반적으로 hashMap과 사용법이 거의 동일합니다. (예를들면 key - value 형태이고 key는 중복될 수 없고, value는 중복될 수 있다는 특징들 입니다.)

```java
public class Hashtable<K,V>
    extends Dictionary<K,V>
    implements Map<K,V>, Cloneable, java.io.Serializable {

    private float loadFactor;

    public Hashtable(int initialCapacity, float loadFactor) {

    }

    public Hashtable(int initialCapacity) {
        this(initialCapacity, 0.75f);
    }

    public Hashtable() {
        this(11, 0.75f);
    }
}
```

위와 같이 `기본 생성자`로 객체를 생성하게 되면 `초기용량(버킷의 수) = 11, 로드팩터 = 0.75`로 설정됩니다. 
버킷의 수와 로드팩터, HashMap에 대해서 좀 더 자세히 알고 싶다면 [여기](https://github.com/wjdrbs96/Today-I-Learn/blob/master/Java/Collection/Map/HashMap%EC%9D%B4%EB%9E%80%3F.md#%ED%95%B4%EC%8B%9C-%EC%B6%A9%EB%8F%8Ccollisions) 에서 확인하면 됩니다. 

위의 글을 읽고 왔다면 해시테이블 자료구조에 대해서는 어느정도 안다고 가정하겠습니다. 

이번 글에서는 HashMap과 Hashtable의 차이점에 대해서 정리를 해보겠습니다. 

<br>

## `HashMap과 Hashtable 클래스의 차이점`

- ### `Thread-safe 여부` 
    - Hashtable은 `Thread-safe`하고, HashMap은 `Thread-safe`하지 않다는 특징을 가지고 있습니다. 그렇기에 멀티스레드 환경이 아니라면 Hashtable은 HashMap 보다 성능이 떨어진다는 단점을 가지고 있습니다.
    
- ### `Null 값 허용 여부`
    - Hashtable은 key에 null을 허용하지 않지만, HashMap은 key에 null을 허용합니다.

- ### `Enumeration 여부`
    - Hashtable은 not fail-fast Enumeration을 제공하지만, HashMap은 Enumeration을 제공하지 않습니다. 
    
- ### `HashMap은 보조해시를 사용하기 때문에 보조 해시 함수를 사용하지 않는 Hashtable에 비하여 해시 충돌(hash collision)이 덜 발생할 수 있어 상대적으로 성능상 이점이 있습니다.`

- ### `최근까지 Hashtable은 구현에 거의 변화가 없지만, HashMap은 현재까지도 지속적으로 개선되고 있습니다.`

조금 더 자세히 알고 싶다면 [Java HashMap은 어떻게 동작하는가?](https://github.com/wjdrbs96/Today-I-Learn/blob/master/Java/Collection/Map/Java%20HashMap%20%EB%8F%99%EC%9E%91%EC%9B%90%EB%A6%AC.md) 를 참고하면 좋습니다.    
    
<br>    
    
`Fail Fast: Iteration 이란 아래와 같습니다.` 말 그대로 빠른 에러를 발생시켜 버그를 예방할 수 있습니다. 

```java
public class Test1 {
    public static void main(String[] args) {
        Hashtable<Integer, Integer> hm = new Hashtable<>();
        hm.put(1, 1);
        hm.put(2, 1);

        Iterator<Integer> iterator = hm.keySet().iterator();
        hm.remove(1);

        while (iterator.hasNext()) {
            System.out.println(iterator.next());
        }
    }
}
```
```
Exception in thread "main" java.util.ConcurrentModificationException
	at java.util.Hashtable$Enumerator.next(Hashtable.java:1387)
	at ExampleCode.Test1.main(Test1.java:18)
```

Fail-fast iteration은 반복자를 생성한 후 해시 테이블을 수정한 경우 위와 같은 예외가 발생합니다. 

위에서 말하는 `not fail-fast Enumeration`이란 아래와 같습니다. 

```java
public class Test1 {
    public static void main(String[] args) {
        Hashtable<Integer, Integer> hm = new Hashtable<>();
        hm.put(1, 1);
        hm.put(2, 1);

        Enumeration<Integer> keys = hm.keys();
        hm.remove(1);

        while (keys.hasMoreElements()) {
            System.out.println(keys.nextElement());
        }
    }
}
``` 

반면에 Enumeration를 사용한 코드는 중간에 remove를 해도 예외가 발생하지 않습니다. 그렇기 때문에 나중에 버그를 발견하지 못할 확률도 존재합니다.
