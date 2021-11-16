## `LocalDateTime에서 Milliseconds 제거하는 법`

```java
public class Test {
    public static void main(String[] args) {
        System.out.println(LocalDateTime.now());
    }
}
```
```
2021-11-04T22:07:57.226
```

위와 같이 `Java 8에 존재하는 LocalDateTime`으로 시간을 출력하면 뒤에 `Milliseconds`가 붙는데요. 이것을 없애는 간단한 방법에 대해서 가볍게 정리해보려 합니다. 

<br>

```java
LocalDateTime now = LocalDateTime.now();
System.out.println("Pre-Truncate:  " + now);
DateTimeFormatter dtf = DateTimeFormatter.ISO_DATE_TIME;
System.out.println("Post-Truncate: " + now.truncatedTo(ChronoUnit.SECONDS).format(dtf));
```
```
Pre-Truncate:  2015-10-07T16:40:58.349
Post-Truncate: 2015-10-07T16:40:58
```

위와 같이 사용하면, 뒤에 붙는 원하지 않는 부분을 제거할 수 있습니다.  