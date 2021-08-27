# `아이템16 : public 클래스에서는 public 필드가 아닌 접근자 메소드를 사용하라`

```java
public class Point {
    public double x;
    public double y;

    public static void main(String[] args) {
        Point point = new Point();
        point.x = 1;
        point.y = 2;
    }
}
```

Point 클래스는 데이터 필드에 직접 접근할 수 있으니 캡슐화의 이점을 제공하지 못합니다. 객체 지향 프로그램에서는 이러한 클래스는 적절하지 않기 때문에 
필드들을 모두 private으로 바꾸고 public 접근자(getter)를 추가합니다.

```java
public class Point {
    private double x;
    private double y;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }
}
```

public 클래스라면 이러한 방식을 사용해야 합니다.

<br>

## `public 클래스의 불변 필드`

```java
public class Time {
    public final int hour;
    public final int minute;

    public Time(int hour, int minute) {
        this.hour = hour;
        this.minute = minute;
    }
}
```

final이 아니라 직접 노출할 때보다 단점이 조금 줄어들지만 여전히 좋은 생각이 아닙니다. 

- API를 변경하지 않고도 표현방식을 바꿀 수 없습니다.
- 필드를 읽을 때 부수적인 작업을 수행할 수 없습니다.
- 그래도 final 키워드로 인해 불변식을 보장할 수 있다는 장점이 있습니다. 