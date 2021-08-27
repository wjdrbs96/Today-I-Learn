# `아이템33: 타입 안전 이종 컨테이너를 고려하라`

- 여기서 말하는 컨테이너란 무엇일까? List 처럼 원소들을 담는 것을 의미하나?

```java
public class Test {
    public <T> void putFavorite(Class<T> type, T instance) {
        
    }
}
``` 

- 제너릭 메소드를 사용한 예시 (putFavorite 메소드에서 T는 모두 같음)
- 일반적인 맵과 달리 여러 가지 타입의 원소를 매게변수에 담을 수 있기 때문에 `타입 안전 이종 컨테이너라 할 만하다`
    - ex) `Class<Integer>`, `Class<String>` 

<br>



