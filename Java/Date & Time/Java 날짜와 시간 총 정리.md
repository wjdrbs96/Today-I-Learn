## `Java 8 이후의 날짜, 시간 정리`

JDK 1.0 부터 제공된 Date 클래스에는 문제점이 많았습니다. [여기](https://github.com/wjdrbs96/Today-I-Learn/blob/master/Java/Date%20%26%20Time/Calendar%EC%99%80%20Date.md) 에서 정리를 하였습니다. 
이러한 단점을 보완하기 위해 JDK 1.8에 `java.time 패키지`가 추가되었습니다. 

이번 글에서는 `java.time 패키지`의 핵심 클래스들에 대해서 알아보겠습니다. 

<br>

## `LocalDate와 LocalTime, LocalDateTime 클래스`

`java.time 패키지`에서는 날짜와 시간을 별도의 클래스로 분리해 놓았습니다. `시간을 표현할 때는 LocalDate`를 사용하고, `날짜를 표현할 때는 LocalTime` 클래스를 사용합니다. 
그리고 `날짜와 시간이 모두 필요할 때는 LocalDateTime 클래스`를 사용하면 됩니다. 

```
LocalDate(날짜) + LocalTime(시간) = LocalDateTime(날짜 + 시간)
```

그리고 위의 클래스들은 `Local`이라는 단어가 들어간 것처럼 말 그대로 현재 시간 정보만 구할 수 있습니다.