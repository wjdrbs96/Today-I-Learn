# `LinedHashSet 이란?`

`LinkedHashSet`은 말 그대로 Set 자료구조와 LinkedList 자료구조를 합친 클래스입니다. Set은 중복을 허용하지 않고, 순서를 유지하지 않는다는 특징을 가지고 있습니다. 
Set에 대한 이해가 부족하다면 [HashSet에 대하여](https://github.com/wjdrbs96/Today-I-Learn/blob/master/Java/Collection/Set/HashSet%EC%9D%B4%EB%9E%80%3F.md) 를 읽고오시면 됩니다. 

HashSet은 내부적으로 Map을 사용하기 때문에 성능을 좌우하는건 `초기용량`, `로드팩터` 입니다. 하지만 LinkedHashMap은 연결리스트를 사용하기 때문에 `초기용량`에 대한 설정이 HashSet보다 덜 중요하다는 특징이 있습니다. 
(그리고 연결리스트는 `양방향 연결리스트` 형태를 유지하면서 구성되어 있습니다.) 

<br>

## `Thread-safe 하지 않다.`

HashSet과 마찬가지로 LinkedHashSet은 Thread-safe하지 않습니다. 즉, 멀티스레드 환경에서 사용하려면 아래와 같이 사용하면 됩니다. 

```
Set s = Collections.synchronizedSet(new LinkedHashSet(...));
```

<br>

## `Iterator은 Fail-Fast 하다.`

```java
import java.util.Iterator;
import java.util.LinkedHashSet;

public class MyKey {
    public static void main(String[] args) {
        LinkedHashSet<Integer> hashSet = new LinkedHashSet<>();
        hashSet.add(1);
        hashSet.add(2);
        hashSet.add(3);

        Iterator<Integer> iterator = hashSet.iterator();

        while (iterator.hasNext()) {
            iterator.remove();
            iterator.next();
        }
    }
}
```
```
Exception in thread "main" java.lang.IllegalStateException
	at java.util.LinkedHashMap$LinkedHashIterator.remove(LinkedHashMap.java:730)
	at ExampleCode.MyKey.main(MyKey.java:16)

```

`Iterator` 객체가 생긴 이후에 반복문을 돌리면서 삭제를 하게 되면 위와 같이 `IllegalStateException`이 발생하게 됩니다. 

<br>

## `시간복잡도`

|삽입|삭제|탐색|  
|--------|-------|-------|
| O(1) | O(1) | O(1) |

LinkedHashSet도 내부적으로 Map을 사용하기 때문에 시간복잡도 O(1)에 해결할 수 있습니다. 하지만 HashSet과는 다르게 순서를 유지한다는 특징이 있기 때문에 HashSet 보다는 성능이 떨어지게 됩니다. 
