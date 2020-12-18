# `아이템45 : 스트림은 주의해서 사용하라`

스트림 API는 다량의 데이터 처리 작업(순차적이든 병렬적이든)을 돕고자 자바8에 추가되었습니다. 

API가 제공하는 추상 개념 중 핵심은 두 가지입니다. 

- 스트림은 데이터 원소의 유한 혹은 무한 시퀀스를 뜻합니다. 
- 스트림 파이프라인은 이 원소들로 수행하는 연산 단계를 표현하느 개념입니다.

<br>

스트림 `파이프라인`은 `소스 스트림`에서 시작해 `종단 연산`으로 끝나며, 그 사이에 하나 이상의 `중간 연산`이 있을 수 있습니다. 

단어들이 쉽지가 않네요 ㅠㅜ

- `중간 연산`들은 모두 한 스트림을 다른 스트림으로 변환합니다.
- `종단 연산`은 마지막 중간 연산이 내놓은 스트림에 최후의 연산을 가합니다. 


```java
import java.util.ArrayList;
import java.util.List;

public class Test {
    public static void main(String[] args) {
        List<String> names = new ArrayList<>();

        names.add("Gyunny");
        names.add("Spring");
        names.add("Study");

        names.stream().map((s) -> {
            System.out.println(s);
            return s.toUpperCase();
        });

        names.forEach(System.out::println);
    }
}
```

여기서 보면 `stream()`을 사용하면 스트림이 만들어집니다. 그리고 map 같은 메소드를 `중간 연산`이라고 합니다. 
스트림의 특성상 중간연산을 실행했을 때 map 메소드의 넘겨준 람다식이 실행되지 않고, 
`종단 연산`을 해야 코드가 실행이 됩니다. 

```java
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Test {
    public static void main(String[] args) {
        List<String> names = new ArrayList<>();

        names.add("Gyunny");
        names.add("Spring");
        names.add("Study");

        List<String> collect = names.stream().map((s) -> {
            System.out.println(s);
            return s.toUpperCase();
        }).collect(Collectors.toList());

        System.out.println("====================");

        names.forEach(System.out::println);
    }
}
```

따라서 위와 같이 `collect()` 라는 종단 연산을 사용하면 출력 코드가 실행이 됩니다. 

- 위와 같이 스트림 API는 메소드 연쇄를 지원하는 플루언트 API 입니다.
- 스트림을 제대로 사용한다면 프로그램이 짧고 깔끔해지지만, 잘못 사용하면 읽기 어렵고 유지보수도 힘들어집니다. 

<br>

책의 P270을 보면 `computeIfAbsent()` 메소드가 나옵니다. 이 메소드는 Map에서 키가 존재하지 않으면 람다식을 실행하는 메소드 인데요

```java
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Test {
    public static void main(String[] args) {
        List<String> keyList = new ArrayList<>();
        keyList.add("1");
        keyList.add("2");

        Map<String, String> map = new HashMap<>();
        map.put("1", "KOREA");
        map.put("2", "USA");

        for (String s : keyList) {
            map.computeIfAbsent(s, (key) -> map.put(s, "1"));
        }

        System.out.println("map = " + map);
    }
}
```

위의 코드를 보면 `computeIfAbsent` 메소드를 통해서 현재 해당 key가 없다면 람다식을 실행해 key - value를 등록하는 과정을 쉽게 사용할 수 있습니다. 

<br>

## 스트림 팁(Stream Tip)

- 스트림을 과용하면 프로그램이 읽거나 유지보수하기 어려워지기 때문에 잘 사용해야 합니다. 
- 람다에서는 타입 이름을 자주 생략하므로 매개변수 이름을 잘 지어야 스트림 파이프라인의 가독성이 유지된다. 
- P270의 코드보다는 P271 처럼 `정렬 하는 부분`의 세부 구현을 주 프로그램 로직 밖으로 빼내면 전체적인 가독성을 높이는데 도움이 됩니다. 

<br>

### `스트림을 처음 쓰기 시작하면 모든 반복문을 스트림으로 바꾸고 싶은 유혹이 있지만 서두르지 않는게 좋다.`

바로 제가 그런 거 같은데요,, 왜그런지 알아보겠습니다. 

- 스트림으로 바꾸는게 가능할지라도 코드 가독성과 유지보수 측면에서는 손해를 볼 수 있기 때문입니다. 
    - 중간 정도 복잡한 작업에도 스트림과 반복문을 적절히 조합하는게 최선입니다. 
    - `기존 코드는 스트림을 사용하도록 리팩토링하되, 새 코드가 더 나아 보일 때만 반영하는게 좋습니다.`
    
<br>

### `스트림 사용이 부적절한 상황`

다음은 함수 객체로는 할 수 없지만 코드 블록으로는 할 수 있는 일들 입니다. 

- 지역변수를 읽고 수정할 수 있는지 여부
- return 사용, break, continue 사용, 예외 던지기 가능 여부

<br>

### `스트림 사용이 적절한 상황`

- 원소들의 시퀀스를 일관되게 변환
- 원스들의 시퀀스를 필터링
- 원스들의 시퀀스를 하나의 연산으로 결합
- 원소들의 시퀀스를 컬렉션에 모으기
- 원스들의 시퀀스에서 특정 조건을 만족하는 원소 찾기

<br>

> P274 ~ P275 => 메르센 소수 (스트림을 사용하기 애매한 경우)

<br>

## `핵심 정리`

> 스트림과 반복 중 어느 쪽이 나은지 확신하기 어렵다면 둘 다 해보고 더 나은 쪽을 택하라.

