# `아이템31: 한정적 와일드카드를 사용해 API 유연성을 높이라`

### 얘기하면 좋을 것

```java
import java.util.ArrayList;
import java.util.List;

public class Test {
    public static void swap(List<?> list, int i, int j) {
        list.add(null);
        list.forEach(System.out::println);
    }
    public static void main(String[] args) {
        List<String> list = new ArrayList<>();
        list.add("1");
        list.add("1");
        list.add("1");
        swap(list, 1, 2);
    }
}
```

- 이렇게 쓰면 `?`의 의미가 음,, 단순히 출력하는 다양한 타입의 List를 출력하는 메소드로 사용할 때 유용한 것인가 ? ? 
- 예를들어 <? extends E> 이럴 때 사용하는 것은 알겠는데 단독으로 ?를 쓸 때에 관하여

