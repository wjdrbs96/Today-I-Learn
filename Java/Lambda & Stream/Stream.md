## 스트림(Stream) 이란?

지금까지 데이터를 다룰 때, 컬렉션이나 배열에 데이터를 담고 원하는 결과를 얻기 위해 for문과 iterator를 이용했었다.
그러나 이러한 방식으로 코드를 작성하면 `가독성`과 `재사용성`이 떨어진다는 단점이 있다. 

그리고 데이터 소스마다 다른 방식으로 다뤄야한다는 것이다.
`Collection`이나 `Iterator`와 같은 인터페이스를 이용해서 컬렉션을 다루는 방식을 표준화하기는 했지만, 각 컬렉션 클래스에는 같은 기능의 메소드들이 중복해서 정의 되어 있다. 

<br>

이러한 문제점들을 해결하기 위해서 만든 것이 `스트림(Stream)`이다. 스트림은 데이터 소스를 추상화하고, 데이터를 다루느넫 자주 사용되는 메소드들을 정의해놓았다. 
`데이터 소스를 추상화하였다는 것은, 데이터 소스가 무엇이던 간에 같은 방식으로 다룰 수 있게 되었다는 것과 코드의 재사용성이 높아진다는 것을 의미한다.`

<br>

```java
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Test {
    public static void main(String[] args) {
        String[] strArr = {"aaa", "ddd", "ccc"};
        List<String> strList = Arrays.asList(strArr);

        Collections.sort(strList);
        Arrays.sort(strArr);

        for (String str :strArr) {
            System.out.print(str + " ");
        }

        System.out.println();

        for (String str : strList) {
            System.out.print(str + " ");
        }
    }
}
```

위의 코드를 보면 `sort`하는 기능은 똑같은데 `Collections`와 `Arrays` 클래스의 메소드를 각각 사용하고 있는 것을 볼 수 있다.
그리고 출력할 때도 for문의 코드를 하나하나 작성해야 하는 것도 알 수 있다.

<br>

```java
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class Test {
    public static void main(String[] args) {
        String[] strArr = {"aaa", "ddd", "ccc"};
        List<String> strList = Arrays.asList(strArr);

        Stream<String> stream = strList.stream();
        Stream<String> stream1 = Arrays.stream(strArr);

        stream.sorted().forEach(System.out::println);
        stream1.sorted().forEach(System.out::println);
    }
}
```

위의 코드를 `Stream`을 사용하면 위와 같이 바꿀 수 있다. 코드가 간결해지고 재사용성도 높다는 것을 알 수 있다.


<br>

### 스트림은 데이터 소스를 변경하지 않는다.

`스트림`은 데이터 소스로 부터 데이터를 읽기만할 뿐, 데이터 소스를 변경하지 않는다.

```java
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class Test {
    public static void main(String[] args) {
        List<String> strList = Arrays.asList(strArr);

        Stream<String> sorted = strList.stream().sorted();
        sorted.forEach(System.out::println);             // 정렬 된 상태
        System.out.println("===========");           
        strList.stream().forEach(System.out::println);   // 원래 대로
    }
}
```
```
aaa
ccc
ddd
===========
aaa
ddd
ccc
```


위와 같이 `strList`를 `sorted`를 이용하여 정렬했지만 원래 `strList`의 값은 변경되지 않는 것을 볼 수 있다.

<br>

### 스트림은 일회용이다.

`스트림`은 `Iterator`처럼 일회용이다. `Iterator`로 컬렉션의 요소를 모두 읽고 나면 다시 사용할 수 없는 것처럼, 스트림도 한번
사용하면 닫혀서 다시 사용할 수 없다. 필요하다면 스트림을 다시 생성해야 한다.


```java
public class Test {
    public static void main(String[] args) {
        String[] strArr = {"aaa", "ddd", "ccc"};
        List<String> strList = Arrays.asList(strArr);

        Stream<String> sorted = strList.stream().sorted();
        sorted.forEach(System.out::println);
        sorted.forEach(System.out::println);  // 에러
    }
}
```

위와 같이 `모두 읽은` 스트림은 닫히기 때문에 다시 사용할 수 없다는 것을 알 수 있다.

