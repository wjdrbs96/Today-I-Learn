# `LocalDateTime & ZonedDateTime 이란?`

```
LocalDate + LocalTime = LocalDateTime
LocalDateTime + 시간대 = ZonedDateTime
```

LocalDate와 LocalTime은 [여기](https://github.com/wjdrbs96/Today-I-Learn/blob/master/Java/Date%20%26%20Time/LocalDate%20%26%20LocalTime.md) 에서 확인할 수 있습니다. 

<br>

## `API 예제`

```java
import java.time.LocalDateTime;

public class Test {
    public static void main(String[] args) {
        LocalDateTime startDateTime = LocalDateTime.now();
        // 결과 : 2021-02-05T14:46:35.709
        System.out.println(startDateTime);

        LocalDateTime endDateTime = LocalDateTime.of(2021, 2, 5,14, 50,5,3333);
        // 결과 : 2019-11-12T12:32:22.000003333

        // startDateTime이 endDateTime 보다 이전 날짜 인지 비교
        System.out.println(startDateTime.isBefore(endDateTime));
        // 결과 : true

        // startDateTime이 endDateTime 보다 이후 날짜 인지 비교
        System.out.println(startDateTime.isAfter(endDateTime));
        // 결고: false

        // 동일 날짜인지 비교
        System.out.println(startDateTime.isEqual(endDateTime));
        // 결과 : false

        // startDateTime이 endDateTime 보다 이후 날짜인지 비교
        System.out.println(startDateTime.isAfter(endDateTime));
        // 결과 : false
    }
}
```

