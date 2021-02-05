# `Calendar와 Date 클래스란?`

Date 클래스는 날짜와 시간을 다룰 목적으로 jdk 1.0 부터 존재하던 클래스입니다. 하지만 너무 옛날부터 존재하는 클래스라서 많은 단점들이 존재합니다. 

그래서 jdk 1.1부터는 `Calendar` 클래스가 나왔지만, 이 클래스 또한 단점이 존재하기 때문에 현재 jdk 1.8 부터는 `java.time 패키지`로 기존의 단점들을 개선ㄴ한 새로운 클래스들이 추가되었습니다. 

이번 글에서는 `Calendar` 클래스와 `Date` 클래스의 API와 어떤 단점들이 있는지를 알아보겠습니다. 

<br>

## `Calendar 클래스란?`

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


위와 같이 `Calendar`를 이용해서 `날짜, 시간` 관련 정보를 얻을 수 있습니다. 하지만 `get(Calendar.MONTH)`로 얻어오는 값의 범위가 `1~12`가 아닌 `0~11` 이라는 큰 단점이 있습니다. 즉, 0이면 1월을 의미하고 11이면 12월을 의미합니다. 

<br>

## `Date 클래스 단점 정리`

- `Thread-safe 하지 않은 클래스입니다.`
- `불변(immutable) 객체도 아니어서 지속적으로 값이 변경 가능합니다.`
- `API 구성도 복잡하게 되어 있어서 연도는 1900년도 부터 시작하도록 되어 있고, 달(month)은 1부터, 일(daty)은 0부터 시작합니다.`