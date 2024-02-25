## `Java 날짜, 시간에 대해 알아보자`

자바에는 Date, LocalDateTime, ZonedDateTime, Period, Duration, ZoneId 등등.. 많은 날짜 관련 클래스들이 존재하는데요. 

이번 글에서는 시간에 대한 개념과 각각 어떤 역할을 하는지 정리 해보겠습니다.

<br>

## `Calendar와 Date 클래스란?`

Date 클래스는 날짜와 시간을 다룰 목적으로 jdk 1.0 부터 존재하던 클래스입니다. 하지만 너무 옛날부터 존재하는 클래스라서 많은 단점들이 존재합니다.

그래서 jdk 1.1부터는 `Calendar` 클래스가 나왔지만, 이 클래스 또한 단점이 존재하기 때문에 현재 jdk 1.8 부터는 `java.time 패키지`로 기존의 단점들을 개선한 새로운 클래스들이 추가되었습니다.

<br>

### `Calendar 클래스란?`

```java
public abstract class Calendar implements Serializable, Cloneable, Comparable<Calendar> {}
``` 

Calendar 클래스는 `abstract` 클래스입니다. 즉, 직접 객체를 생성할 수 없고 메소드를 통해서 클래스의 인스턴스를 얻어야 합니다.

```
Calendar calendar = Calendar.getInstance();
```

Calendar를 상속받아 완전히 구현한 클래스로는 `GregorianCalendar`와 `BuddhistCalendar`가 있는데 getInstance()는 시스템의 국가와 지역설정을 확인해서 태국인 경우에는 `BuddhistCalendar` 인스턴스를 반환하고,
그 외에는 `GregorianCalendar`의 인스턴스를 반환합니다.

<br>

## `Date와 Calendar 간의 변환`

```java
// 1. Calendar를 Date로 변환

Calendar calendar = Calendar.getInstance();

Date d = new Date(calendar.getTimeInMillis());

// 2. Date를 Calendar로 변환
Date d = new Date();

Calendar cal = Calendar.getInstance();
cal.setTime(d);
```

Calendar가 새로 추가되면서 Date는 대부분의 메소드가 `deprecated` 되었으므로 잘 사용되지 않습니다.

<br>

### `예제 코드`

```java
import java.util.Calendar;

public class Test {
    public static void main(String[] args) {
        Calendar calendar = Calendar.getInstance();
        System.out.println("이 해의 년도 : " + calendar.get(Calendar.YEAR));
        System.out.println("월(0~11, 0: 1월) : " + calendar.get(Calendar.MONTH));
        System.out.println("이 달의 몇째 주 : " + calendar.get(Calendar.WEEK_OF_MONTH));

        System.out.println("이 달의 몇 일 : " + calendar.get(Calendar.DATE));
        System.out.println("이 달의 몇 일 : " + calendar.get(Calendar.DAY_OF_MONTH));
        System.out.println("이 해의 몇 일 : " + calendar.get(Calendar.DAY_OF_YEAR));

        System.out.println("시간(0~11) : " + calendar.get(Calendar.HOUR));
        System.out.println("시간(0~23) : " + calendar.get(Calendar.HOUR_OF_DAY));
    }
}
```
```
이 해의 년도 : 2021
월(0~11, 0: 1월) : 1
이 달의 몇째 주 : 1
이 달의 몇 일 : 5
이 달의 몇 일 : 5
이 해의 몇 일 : 36
시간(0~11) : 0
시간(0~23) : 12
```


위와 같이 `Calendar`를 이용해서 `날짜, 시간` 관련 정보를 얻을 수 있습니다. 하지만 `get(Calendar.MONTH)`로 얻어오는 값의 범위가 `1 ~ 12`가 아닌 `0 ~ 11` 이라는 API 사용에 큰 단점이 있습니다. (0이면 1월을 의미하고 11이면 12월을 의미)

<br>

### `Date 클래스 단점 정리`

- `Thread-safe 하지 않은 클래스입니다.`
- `불변(immutable) 객체도 아니어서 지속적으로 값이 변경 가능합니다.`
- `API 구성도 복잡하게 되어 있어서 연도는 1900년도 부터 시작하도록 되어 있고, 달(month)은 1부터, 일(daty)은 0부터 시작합니다.`

<br>

## `SimpleDateFormat 클래스란?`

Date와 Calender 만으로 날짜 데이터를 원하는 형태로 다양하게 출력하는 것은 불편하고 복잡합니다. 날짜를 원하는 형식으로 출력할 때 사용하는 것이 `SimpleDateFormat` 입니다.

```java
public class SimpleDateFormatExample {
    public static void main(String[] args) {
        Date today = new Date();

        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");

        System.out.println("format1: " + sdf1.format(today));
        System.out.println("format2: " + sdf2.format(today));
        System.out.println("format3: " + sdf3.format(today));
    }
}
```
```
format1: 2024-02-25
format2: 2024-02-25 17:43:18.873
format3: 2024-02-25 05:43:18 오후
```

위처럼 날짜를 원하는 형식으로 편리하게 출력할 수 있습니다.

<br>

## `java.time 패키지`

위에서 정리한 것처럼 JDK 1.0 부터 제공된 Date 클래스에는 문제점이 많았습니다. 이러한 단점을 보완하기 위해 JDK 1.8에 `java.time 패키지`가 추가되었습니다.

`java.time` 패키지에 속한 클래스들의 가장 큰 특징은 `불변(immutable)` 하다는 것입니다. 날짜나 시간을 변경하는 메소드들은 기존의 객체를 변경하는 대신 항상 변경된 새로운 객체를 반환합니다.

```
LocalDate(날짜) + LocalTime(시간) = LocalDateTime(날짜 + 시간)
```

`java.time 패키지` 에서는 날짜와 시간을 별도의 클래스로 분리해 놓았습니다. `시간을 표현할 때는 LocalDate`를 사용하고, `날짜를 표현할 때는 LocalTime` 클래스를 사용합니다. 그리고 `날짜와 시간이 모두 필요할 때는 LocalDateTime 클래스`를 사용하면 됩니다.

<br>

```
LocalDateTime + 시간대(time-zone) = ZonedDateTime
```

여기에 `시간대(time-zone)` 까지 다뤄야 한다면, `ZonedDateTime` 클래스를 사용할 수 있습니다.

<br>

### `타임스탬프(timestamps)?`

날짜와 시간을 초단위로 표현한 값은 `타임스탬프란(timestamps)` 라고 부르고, 이 값은 날짜와 시간을 하나의 정수로 표현할 수 있으므로 날짜와 시간의 차이를 계산하거나 순서를 비교하는데 유리해서 데이터베이스에 많이 사용됩니다.

참고로 Calendar는 ZonedDateTime 처럼, 날짜와 시간 그리고 시간대까지 모두 가지고 있습니다. Date와 유사한 클래스로는 Instant가 있는데, 이 클래스는 날짜와 시간을 나노초 단위로 표현합니다.

<br>

### `Period와 Duration 클래스`

```java
public class PeriodExample {
    public static void main(String[] args) {
        LocalDate today = LocalDate.now();
        LocalDate yesterday = LocalDate.of(2024, 2, 24);
        Period between = Period.between(today, yesterday);
        System.out.println("between days: " + between.getDays());  // -1
    }
}
```

```java
public class DurationExample {
    public static void main(String[] args) {
        LocalTime todayTime = LocalTime.now();
        LocalTime yesterdayTime = LocalTime.of(20, 2);
        Duration between = Duration.between(todayTime, yesterdayTime);
        System.out.println("between times: " + between.getSeconds()); // 6013
    }
}
```

```
날짜 - 날짜 = Period
시간 - 시간 = Duration
```

Period는 두 날짜간의 차이를 표현하기 위한 것이고, Duration은 시간의 차이를 표현하기 위한 것입니다.

<br>

### `Temporal과 TemporalAmount`

- Temporal, TemporalAccessor, TemporalAdjuster를 구현한 클래스
  - 날짜, 시간을 표현하기 위한 클래스들 LocalDate, LocalTime, LocalDateTime, ZonedDateTime, Instant 등등
- TemporalAmount를 구현한 클래스
  - Period, Duration

<br>

### `TemporalUnit과 TemporalField`

- `TemporalUnit`: 날짜와 시간의 단위를 정의해 놓은 것
  - `ChronoUnit`: `TemporalUnit` 인터페이스를 구현한 enum
- `TemporalField`: 년, 월, 일 등 날짜와 시간의 필드를 정의해 놓은 것
  - `ChronoFiled`: `TemporalField` 인터페이스를 구현한 enum

```java
public enum ChronoUnit implements TemporalUnit {
  NANOS("Nanos", Duration.ofNanos(1)),
  MICROS("Micros", Duration.ofNanos(1000)),
  MILLIS("Millis", Duration.ofNanos(1000_000)),
  SECONDS("Seconds", Duration.ofSeconds(1)),
  MINUTES("Minutes", Duration.ofSeconds(60)),
  HOURS("Hours", Duration.ofSeconds(3600)),
  HALF_DAYS("HalfDays", Duration.ofSeconds(43200)),
  DAYS("Days", Duration.ofSeconds(86400)),
  WEEKS("Weeks", Duration.ofSeconds(7 * 86400L)),
  MONTHS("Months", Duration.ofSeconds(31556952L / 12)),
  YEARS("Years", Duration.ofSeconds(31556952L)),
  DECADES("Decades", Duration.ofSeconds(31556952L * 10L)),
  CENTURIES("Centuries", Duration.ofSeconds(31556952L * 100L)),
  MILLENNIA("Millennia", Duration.ofSeconds(31556952L * 1000L)),
  ERAS("Eras", Duration.ofSeconds(31556952L * 1000_000_000L)),
  FOREVER("Forever", Duration.ofSeconds(Long.MAX_VALUE, 999_999_999)),
  ;
}
```

```java
public class ChronoUnitExample {
    public static void main(String[] args) {
        LocalDate today = LocalDate.now();
        LocalDate tomorrow = today.plus(1, ChronoUnit.DAYS);
        System.out.println("tomorrow: " + tomorrow);
    }
}
```
```
tomorrow: 2024-02-26
```

위와 같이 특정 날짜와 시간에서 지정된 단위의 값을 더하거나 뺄 때는 `ChronoUnit`을 사용할 수 있습니다.

<br>

```java
public enum ChronoField implements TemporalField {

    NANO_OF_SECOND("NanoOfSecond", NANOS, SECONDS, ValueRange.of(0, 999_999_999)),
    NANO_OF_DAY("NanoOfDay", NANOS, DAYS, ValueRange.of(0, 86400L * 1000_000_000L - 1)),
    MICRO_OF_SECOND("MicroOfSecond", MICROS, SECONDS, ValueRange.of(0, 999_999)),
    MICRO_OF_DAY("MicroOfDay", MICROS, DAYS, ValueRange.of(0, 86400L * 1000_000L - 1)),
    MILLI_OF_SECOND("MilliOfSecond", MILLIS, SECONDS, ValueRange.of(0, 999)),
    MILLI_OF_DAY("MilliOfDay", MILLIS, DAYS, ValueRange.of(0, 86400L * 1000L - 1)),

    SECOND_OF_MINUTE("SecondOfMinute", SECONDS, MINUTES, ValueRange.of(0, 59), "second"),
    SECOND_OF_DAY("SecondOfDay", SECONDS, DAYS, ValueRange.of(0, 86400L - 1)),
    MINUTE_OF_HOUR("MinuteOfHour", MINUTES, HOURS, ValueRange.of(0, 59), "minute"),
    MINUTE_OF_DAY("MinuteOfDay", MINUTES, DAYS, ValueRange.of(0, (24 * 60) - 1)),
    HOUR_OF_AMPM("HourOfAmPm", HOURS, HALF_DAYS, ValueRange.of(0, 11)),
    CLOCK_HOUR_OF_AMPM("ClockHourOfAmPm", HOURS, HALF_DAYS, ValueRange.of(1, 12)),
  
    HOUR_OF_DAY("HourOfDay", HOURS, DAYS, ValueRange.of(0, 23), "hour"),
    CLOCK_HOUR_OF_DAY("ClockHourOfDay", HOURS, DAYS, ValueRange.of(1, 24)),
    AMPM_OF_DAY("AmPmOfDay", HALF_DAYS, DAYS, ValueRange.of(0, 1), "dayperiod"),
    DAY_OF_WEEK("DayOfWeek", DAYS, WEEKS, ValueRange.of(1, 7), "weekday"),
    ALIGNED_DAY_OF_WEEK_IN_MONTH("AlignedDayOfWeekInMonth", DAYS, WEEKS, ValueRange.of(1, 7)),

    ALIGNED_DAY_OF_WEEK_IN_YEAR("AlignedDayOfWeekInYear", DAYS, WEEKS, ValueRange.of(1, 7)),
    DAY_OF_MONTH("DayOfMonth", DAYS, MONTHS, ValueRange.of(1, 28, 31), "day"),
    DAY_OF_YEAR("DayOfYear", DAYS, YEARS, ValueRange.of(1, 365, 366)),
  
    EPOCH_DAY("EpochDay", DAYS, FOREVER, ValueRange.of(-365243219162L, 365241780471L)),
    ALIGNED_WEEK_OF_MONTH("AlignedWeekOfMonth", WEEKS, MONTHS, ValueRange.of(1, 4, 5)),
    ALIGNED_WEEK_OF_YEAR("AlignedWeekOfYear", WEEKS, YEARS, ValueRange.of(1, 53)),
    MONTH_OF_YEAR("MonthOfYear", MONTHS, YEARS, ValueRange.of(1, 12), "month"),
    PROLEPTIC_MONTH("ProlepticMonth", MONTHS, FOREVER, ValueRange.of(Year.MIN_VALUE * 12L, Year.MAX_VALUE * 12L + 11)),
    YEAR_OF_ERA("YearOfEra", YEARS, FOREVER, ValueRange.of(1, Year.MAX_VALUE, Year.MAX_VALUE + 1)),
    YEAR("Year", YEARS, FOREVER, ValueRange.of(Year.MIN_VALUE, Year.MAX_VALUE), "year"),
    ERA("Era", ERAS, FOREVER, ValueRange.of(0, 1), "era"
    INSTANT_SECONDS("InstantSeconds", SECONDS, FOREVER, ValueRange.of(Long.MIN_VALUE, Long.MAX_VALUE)),
    OFFSET_SECONDS("OffsetSeconds", SECONDS, FOREVER, ValueRange.of(-18 * 3600, 18 * 3600));

}
```

```java
public class ChronoFieldExample {
    public static void main(String[] args) {
        LocalTime now = LocalTime.now();
        int minute = now.getMinute();
        int minuteByChronoFiled = now.get(ChronoField.MINUTE_OF_HOUR);
        System.out.println("minute: " + minute);
        System.out.println("minuteByChronoFiled: " + minuteByChronoFiled);
    }
}
```
```
minute: 35
minuteByChronoFiled: 35
```

요약하면, `ChronoUnit` 에서 날짜와 시간의 단위를 정의해놓은 것을 기반으로, `ChronoField` 에서 날짜와 시간의 필드를 정의해놓은 것을 알 수 있습니다.

<br>

## `Instant`

Instant는 에포크 타임(EPOCH TIME, 1970-01-01 00:00:00 UTC) 부터 경과된 시간을 나노초 단위로 표현합니다.

사람에게는 불편하지만, 단일 진법으로만 다루기 때문에 계산하기에 편리합니다. (날짜와 시간은 여러 진법이 섞여있어서 계산하기 어렵습니다.)

```java
public class InstantExample {
    public static void main(String[] args) {
        Instant now = Instant.now();
        long epochSecond = now.getEpochSecond();
        int nano = now.getNano();
        long epochMilli = now.toEpochMilli();

        System.out.println("now: " + now);
        System.out.println("epochSecond: " + epochSecond);
        System.out.println("nano: " + nano);
        System.out.println("epochMilli: " + epochMilli);
    }
}
```
```
now: 2024-02-25T09:39:11.316574Z
epochSecond: 1708853951
nano: 316574000
epochMilli: 1708853951316
```

Intent는 시간을 초 단위와 나누초 단위로 나누어 저장합니다. 데이터베이스의 타임스탬프(timestamp) 처럼 밀리초 단위의 EPOCH TIME을 필요로 하는 경우를 위해 `toEpochMilli()` 메소드가 존재합니다.

Instant는 항상 UTC(+00:00)를 기준으로 하기 때문에, LocalTime과 차이가 있을 수 있습니다. 예를들어, 한국은 시간대가 `+09:00` 이므로 Instant와 LocalDateTime 간에는 9시간의 차이가 존재합니다.

시간대를 고려해야하는 경우 `OffsetDateTime`을 사용하는 것이 더 나은 선택일 수 있습니다.

UTC는 `Coordinated Universal Time`의 약어로 `세계 협정시` 라고 하며, 1972년 1월 1일부터 시행된 국제 표준시입니다. 

이전에 사용되던 `GMT(Greenwich Mean Time)와 UTC는 거의 같지만, UTC가 좀 더 정확합니다.`

> GMT(Greenwich Mean Time): 그리니치 천문대 기준시각입니다.

> UTC(Coordinated Universal Time): 협정 시계시, 세슘 원자의 운동량을 기반으로 계산된 정확한 시간 UTC와 GMT는 엄연히 다른 의미지만 소숫점 단위에서만 차이가 나기 때문에 일상 혼용합니다.

<br>

## `LocalDateTime과 ZonedDateTime`

```
LocalDate(날짜) + LocalTime(시간) = LocalDateTime(날짜 + 시간)
LocalDateTime + 시간대(time-zone) = ZonedDateTime
```

```java
public class ZonedDateTimeExample {
    public static void main(String[] args) {
        LocalDateTime localDateTime = LocalDateTime.now();
        ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneId.of("America/New_York"));
        System.out.println("localDateTime: " + localDateTime);
        System.out.println("zonedDateTime: " + zonedDateTime);
    }
}
```
```
localDateTime: 2024-02-25T18:53:17.077308
zonedDateTime: 2024-02-25T18:53:17.077308-05:00[America/New_York]
```

LocalDateTime에 `시간대(time-zone)`을 추가하면, `ZonedDateTime`이 됩니다. 시간대를 추가할 때 ZoneId를 사용할 수 있는데, 일광 절약 시간(=썸머 타임)을 자동적으로 계산해주기에 편리합니다.

<br>

### `ZoneOffset`

UTC로부터 얼마만큼 떨어져 있는지를 ZoneOffset으로 표현됩니다. 

```java
public class ZoneOffsetExample {
    public static void main(String[] args) {
        ZoneOffset offset = ZonedDateTime.now().getOffset();
        System.out.println("offset: " + offset);
    }
}
```
```
offset: +09:00
```

위처럼 필자는 서울 시간으로 계산 되기 때문에 +9 이고, UTC보다 9시간(32400초=60x60x9초) 더 빠릅니다.

<br>

### `OffsetDateTime`

ZonedDateTime은 ZoneId로 구역을 표현하는데, ZoneId가 아닌 ZoneOffset을 사용하는 것이 `OffsetDateTime` 입니다.

ZoneId는 일광절약시간(=썸머타임)처럼 시간대와 관련된 규칙들을 포함하고 있는데, ZoneOffset은 단지 시간대를 시간의 차이로만 구분합니다. 

```java
public class OffsetDateTimeExample {
    public static void main(String[] args) {
        ZonedDateTime zonedDateTime = ZonedDateTime.now();
        OffsetDateTime offsetDateTime = zonedDateTime.toOffsetDateTime();

        System.out.println("zonedDateTime: " + zonedDateTime);
        System.out.println("offsetDateTime: " + offsetDateTime);
    }
}
```
```
zonedDateTime: 2024-02-25T19:03:27.047537+09:00[Asia/Seoul]
offsetDateTime: 2024-02-25T19:03:27.047537+09:00
```

<br>

## `파싱과 포맷`

날짜와 시간을 원하는 형식으로 파싱하는 방법에 대해서도 알아보겠습니다.

`형식화(formatting)와 관련된 클래스들은 java.time.format 패키지에 들어있는데, 이 중에서 DateTimeFormatter가 핵심입니다.`

```java
public class DateTimeFormatterExample {
    public static void main(String[] args) {
        LocalDate date = LocalDate.of(2016, 1, 2);
        String yyyymmdd = DateTimeFormatter.ISO_LOCAL_DATE.format(date);
        String yyyymmdd2 = date.format(DateTimeFormatter.ISO_LOCAL_DATE);

        System.out.println("yyyymmdd: " + yyyymmdd);
        System.out.println("yyyymmdd2: " + yyyymmdd2);
    }
}
```
```
yyyymmdd: 2016-01-02
yyyymmdd2: 2016-01-02
```

<br>

### `문자열을 날짜와 시간으로 파싱하기`

```java
public class DateTimeFormatterExample {
    public static void main(String[] args) {
        LocalDate parseLocalDate = LocalDate.parse("2024-02-25");
        LocalTime parseLocalTime = LocalTime.parse("23:59:59");
        LocalDateTime parseLocalDateTime = LocalDateTime.parse("2024-02-25T19:54:54");

        DateTimeFormatter pattern = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime parseLocalDateTimeByPattern = LocalDateTime.parse("2022-02-25 20:03:21", pattern);

        System.out.println("parseLocalDate: " + parseLocalDate);
        System.out.println("parseLocalTime: " + parseLocalTime);
        System.out.println("parseLocalDateTime: " + parseLocalDateTime);
        System.out.println("parseLocalDateTimeByPattern: " + parseLocalDateTimeByPattern);
    }
}
```
```
parseLocalDate: 2024-02-25
parseLocalTime: 23:59:59
parseLocalDateTime: 2024-02-25T19:54:54
parseLocalDateTimeByPattern: 2022-02-25T20:03:21
```

위처럼 `parse()` 메소드 통해서도 문자열을 파싱할 수 있고, `DateTimeFormatter`를 사용해서 파싱할 수도 있습니다.

<br>

## `Referenece`

- [자바의 정석 - 2편]()
- [https://medium.com/29cm/java-%ED%83%80%EC%9E%84%EC%A1%B4-%EB%82%A0%EC%A7%9C-%EA%B7%B8%EB%A6%AC%EA%B3%A0-%EC%8B%9C%EA%B0%84%EA%B0%9D%EC%B2%B4-%EB%BD%80%EA%B0%9C%EA%B8%B0-c8c896eb3094](https://medium.com/29cm/java-%ED%83%80%EC%9E%84%EC%A1%B4-%EB%82%A0%EC%A7%9C-%EA%B7%B8%EB%A6%AC%EA%B3%A0-%EC%8B%9C%EA%B0%84%EA%B0%9D%EC%B2%B4-%EB%BD%80%EA%B0%9C%EA%B8%B0-c8c896eb3094)