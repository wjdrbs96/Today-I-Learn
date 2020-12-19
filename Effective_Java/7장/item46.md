# `아이템46 : 스트림에서는 부작용 없는 함수를 사용하라`

스트림은 그저 또 하나의 API가 아닌, `함수형 프로그래밍에 기초한 패러다임`입니다. 

스트림 패러다임의 핵심은 `계산을 일련의 변환`으로 재구성 하는 부분입니다. 이 때 각 변환 단계는 가능한 한 이전 단계의 결과를 받아 처리하는 `순수 함수`여야 합니다. 

> 순수함수란? 오직 입력만이 결과에 영향을 주는 함수를 말합니다. 다른 가변 상태를 참조하지 않고, 함수 스스로도 다른 상태를 변경하지 않습니다. 

<br>

## 예제 코드

```java
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Stream;

public class Test {
    public static void main(String[] args) {
        Map<String, Long> freq = new HashMap<>();
        try (Stream<String> words = new Scanner(file).tokens()) {
            words.forEach(words -> {
                freq.merge(words.toLowerCase(), 1L, Long::sum);
            });
        }
    }
}
```

위의 코드는 문제가 있습니다. `스트림, 람다, 메소드 참조`를 사용했고, 결과도 잘 나옵니다. 그렇지만 절대 스트림 코드라 할 수 없습니다. 
`스트림 코드르 가장한 반복적 코드입니다.`

- 스트림 API의 이점을 살리지 못하여 같은 기능의 반복적 코드보다 길고, 읽기 어렵고, 유지보수에도 좋지 않습니다. 
- 종단연산인 forEach에서 작업이 수행되는데 이때 외부 상태를 수정하는 람다식을 실행하면서 문제가 발생합니다. 

```java
public class Test {
    public static void main(String[] args) {
        Map<String, Long> freq;
        try (Stream<String> words = new Scanner(file).tokens()) {
            freq = words
                    .collect(groupingBy(String::toLowerCase, counting()));
        }
    }
}
```

위의 코드는 짧고 명확하기에 올바르게 사용한 코드입니다. 

- forEach 연산은 종단 연산 중 기능이 가장 적고 가장 '덜' 스트림 답습니다. 
    - 대놓고 반복적이라서 병렬화 할 수 없고, `forEach 연산은 스트림 계산 결과를 보고할 때만 사용하고, 계산하는 데는 쓰지 않는 것이 좋습니다.`
    

위의 코드를 보면 `collector`를 사용하는데 `스트림을 사용하려면 꼭 배워야하는 새로운 개념입니다.`

```java
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;

public class Test {
    public static void main(String[] args) {
        Map<String, Long> freq = new HashMap<>();
        
        List<String> topTen = freq.keySet().stream()
                .sorted(comparing(freq::get).reversed())
                .limit(10)
                .collect(toList());
    }
}
```

이렇게 collector를 사용하면 가장 흔한 단어 10개를 뽑아내는 스트림 파이프라인을 만드는 것은 매우 쉽습니다. 

<br>

## 이와 같이 `Collectors`는 스트림에서 매우 중요합니다. 

[여기](https://sabarada.tistory.com/41) 를 참고하면 좋을 거 같습니다. 


- ### `toList, toSet 메소드`

toList collector는 모든 Stream elements를 List나 Set instance로 변경하는 메서드입니다. 

```java
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Test {
    public static void main(String[] args) {
        List<String> givenList = Arrays.asList("a", "bb", "cc", "bb");
        List<String> result1 = givenList.stream().collect(Collectors.toList()); // a, bb, ccc, dd
        Set<String> result2 = givenList.stream().collect(Collectors.toSet()); // a, bb, ccc
        
    }
}
```


- ### `toMap 메소드`

toMap collector는 Stream elements들을 Map instance로 변경하는 메서드입니다.

```
Map<String, Integer> result = givenList.stream().collect(toMap("key", String::length);
```

이렇게 예제를 구성해 보겠습니다. key라는 이름의 key에 4가지의 elements가 들어가기 때문에 key 충돌이 발생할 것입니다. 이떄 위의 경우는 IllegalStateException가 발생합니다. key 중복이라는 의미입니다. key 중복시 처리방법은 아래와 같이 설정해야합니다.

```
Map<String, Integer> result = givenList.stream()
  .collect(toMap("key", String::length, (item, identicalItem) -> item));
```

- ### `groupingBy 메소드`

database를 조작할 때나 사용하던 group by를 Collection으로 사용할 수 있습니다.

```java
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.*;

public class Test {
    public static void main(String[] args) {
        List<String> givenList = Arrays.asList("a", "bb", "cc", "bb");
        Map<Integer, List<String>> result = givenList.stream()
                .collect(groupingBy(String::length, toList()));

        result.forEach((k, v) -> System.out.println(k + " " + v));  
        
        // 1 [a]
        // 2 [bb, cc, bb]
    }
}
```

<br>

- ### `joining`

joining은 Stream을 List가 아닌 String으로 붙여주거나 할 때 사용하는 메서드입니다.

```java
import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.joining;

public class Test {
    public static void main(String[] args) {
        List<String> givenList = Arrays.asList("a", "bb", "cc", "bb");
        String result = givenList.stream().collect(joining()); 
        System.out.println(result); // abbcccdd
    }
}
```

<br>

## `핵심 정리`

- `forEach`는 스트림이 수행한 계산 결과를 보고할 때만 이용하고 계산 자체에는 이용하지 말자.
- 스트림을 올바로 사용하려면 `Collectors`를 알아둬야 한다. (toList, toSet, toMap, groupingBy, joining)