# `아이템28 : 배열보다는 리스트를 사용하라`

## `제네릭 배열을 만들지 못하게 막은 이유는 무엇일까?`

타입 안전하지 않기 때문입니다. 

```java
import java.util.List;

public class Test {
    public static void main(String[] args) {
        List<String>[] stringLists = new List<String>[1];    // (1)
        List<Integer> intList = List.of(42);                 // (2)
        Object[] objects = stringLists;                      // (3)
        objects[0] = intList;                                // (4)
        String s = stringLists[0].get(0);                    // (5)
    }
}
```

여기서 (1)이 가능하다고 가정해보겠습니다. 그러면 (2)에서 원소가 하나인 List<Integer>를 만들어 반환합니다. 
그리고 (3)에서 List<String>을 Object 배열에 넣고, (4)에는 List<Integer>를 넣는 것을 보면 당연히 (5)에서 문제가 발생할 거 같습니다. 

따라서 이러한 오류 때문에 런타임이 아니라 컴파일 오류를 발생시키는 것입니다. 

`E, List<E>, List<String> 같이 타입을 실체화 불가 타입(non-reifiable type)이라 합니다.` 쉽게 말해, 실체화되지 않아서 런타임에는 컴파일타임보다 타입 정보를 적게 가지는 타입입니다. 

<br>

## 핵심 정리

> 배열과 제네릭에는 매우 다른 타입 규칙이 적용된다. 배열은 공변이고 실체화되는 반면, 제네릭은 불공변이고 타입 정보가 소거된다. 
> 그 결과 배열은 런타임에는 타입 안전하지만 컴파일타임에는 그렇지 않다. 제네릭은 반대다. 그래서 둘을 섞어 쓰기란 쉽지 않다.
> 둘을 섞어 쓰다가 컴파일 오류나 경고를 만나면, 가장 먼저 배열을 리스트로 대체하는 방법을 적용해보자.




 
