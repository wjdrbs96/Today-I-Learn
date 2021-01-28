# `LinkedHashMap 이란?`

`LinkedHashMap`이란 Map과 LinedList 자료구조를 합친 형태입니다. 즉, Map 자료구조는 key-value 형태이며 key는 중복되지 않고 value는 중복될 수 있다는 특징을 가지고 있습니다. 
또한 순서를 유지하지 않는다는 특징도 가지고 있습니다. 

하지만 `LinkedHashMap`을 사용하면 key-value 형태인 Map의 구조를 이용하면서 `순서를 유지한다는` 것을 알 수 있습니다. 

연결리스트는 `양방향 연결리스트`를 사용하고 있습니다. Map은 HashMap과 똑같이 작동하기 때문에 [HashMap에 대해서](https://github.com/wjdrbs96/Today-I-Learn/blob/master/Java/Collection/Map/HashMap%EC%9D%B4%EB%9E%80%3F.md) 잘 모르겠다면 보고 오는 것을 추천드립니다. 

<br>

## `시간복잡도`

LinkedHashMap도 HashMap과 마찬가지로 버킷에 충돌이 발생하지 않았다면 `삽입`, `삭제` 시간복잡도는 O(1) 입니다. 왜냐하면 Map의 `삽입`, `삭제`도 O(1)이고, 연결리스트의 `삽입`, `삭제`도 O(1)이기 때문입니다. 

|삽입|삭제|탐색|  
|--------|-------|-------|
| O(1) | O(1) | O(1) |

위와 같이 시간복잡도를 가지긴 했지만, HashMap과는 다르게 순서를 유지해야 하기 때문에 HashMap에 비해서는 조금 더 성능이 떨어지게 됩니다. 

LinkedHashMap의 Iteration으로 반복하면 HashMap과 비슷한 시간복잡도 O(n)가 소요됩니다. 하지만 LinkedHashMap이 HashMap 보다 Iteration 성능이 좀 더 좋습니다. 

왜냐하면 LinkedHashMap의 경우 O(n)의 n은 용량에 관계없이 Map의 항목 수 만 반복하기 때문입니다. 반면에, HashMap의 경우 n은 용량이고 크기는 O(크기+용량)로 요약이기 때문에 성능이 조금 더 떨어지게 됩니다.

<br>

## `Thread-safe 하지 않다.`

HashMap 같이 LinkedHashMap도 `Thread-safe`하지 않습니다. 그래서 만약 멀티스레드 환경에서 사용해야 한다면 아래와 같이 사용하면 됩니다. 

```
Map m = Collections.synchronizedMap(new LinkedHashMap());
```


<br>

## `예제 코드`

```java
import java.util.LinkedHashMap;

public class MyKey {
    public static void main(String[] args) {
        LinkedHashMap<Integer, Integer> hm = new LinkedHashMap<>();
        hm.put(2, 1);
        hm.put(1, 3);
        hm.put(3, 3);
        hm.put(4, 3);

        hm.forEach((k, v) -> System.out.println(k + " " + v));
    }
}
```
```
2 1
1 3
3 3
4 3
```

위와 같이 순서를 유지하는 것을 볼 수 있습니다. 