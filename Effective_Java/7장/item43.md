# `아이템43 : 람다보다는 메소드 참조를 사용하라`

```java

import java.util.HashMap;
import java.util.Map;
public class Test {
    public static void main(String[] args) {
        Map<Integer, Integer> key = new HashMap<>();

        key.put(1, 1);
        key.merge(1, 1,  (c ,i) -> c + i);

        key.values().forEach(System.out::println);   // 2
    }
}
```

매번 if문을 처리해서 key가 존재한다면 key에 해당하는 value를 꺼내고 없다면 key를 등록하는 로직으로 코딩을 했었는데 
`merge` 메소드가 있다는 건 처음 알았다. 

위와 같이 람다를 이용해서 작성해도 되지만 `메소드 레퍼런스`를 이용하면 더 쉽게 줄일 수 있다. 

```java
import java.util.HashMap;
import java.util.Map;

public class Test {
    public static void main(String[] args) {
        Map<Integer, Integer> key = new HashMap<>();

        key.put(1, 1);
        key.merge(1, 1,  Integer::sum);

        key.values().forEach(System.out::println);   // 2
    }
}
``` 

이렇게 메소드 레퍼런스를 사용하면 코도를 간결하게 줄일 수 있다. 

하지만 람다에서는 매게변수의 이름이 존재하기 때문에 이름 자체로 좋은 가이드를 제시해줄 수 있기 때문에 메소드 참조보다는 유지보수가 더 좋을 수도 있다. 

<br>

## 같이 보면 좋을 것

인스턴스 메소드를 참조하는 유형이 두 가지가 있다.

- 수신 객체를 특정하는 `한정적 인스턴스 메소드 참조`
- 수신 객체를 특정하지 않는 `비한정적 인스턴스 메소드 참조`

`한정적 인스턴스`와 `비한정적 인스턴스`의 차이에 대하여,, 논의?
 
<br>

## 핵심 정리

- 메소드 참조 쪽이 짧고 명확하다면 메소드 참조를 쓰고, 그렇지 않을 때만 람다를 사용하라.

