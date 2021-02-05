# `SimpleDateFormat 클래스란?`

`Date`, `Calendar`를 이용해서 날짜를 계산하는 방법에 대해서 알아본 적이 있습니다. 이번 글에서는 `출력`하는 방법에 대해서 정리해보겠습니다. 

`Date`와 `Calendar`만으로 날짜 데이터를 원하는 형태로 다양하게 출력하는 것은 불편하고 복잡합니다. 하지만 `SimpleDateFormat` 클래스를 사용하면 이러한 문제들이 간단히 해결됩니다.

```java
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateFormatEx1 {
    public static void main(String[] args) {
        Date date = new Date();

        SimpleDateFormat sdf1, sdf2, sdf3;

        sdf1 = new SimpleDateFormat("yyyy-MM-dd");
        sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        sdf3 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");

        System.out.println(sdf1.format(date));
        System.out.println(sdf2.format(date));
        System.out.println(sdf3.format(date));
    }
}
```
```
2021-02-05
2021-02-05 13:37:25.268
2021-02-05 01:37:25 PM
```

위와 같이 `SimpleDateFormat`을 사용하여 쉽게 출력할 수 있습니다. 

