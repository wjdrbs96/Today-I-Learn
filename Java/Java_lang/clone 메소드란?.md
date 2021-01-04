# `Object 클래스의 clone 메소드란?`

```java
public class Object {
    protected native Object clone() throws CloneNotSupportedException;
}
```

`clone()` 메소드는 Object 클래스 내부에 있는 메소드이며, 자신을 복제하여 새로운 인스턴스를 생성하는 일을 합니다. 

그리고 clone()은 단순히 참조 변수의 값만을 복사하기 때문에 `shallow copy`가 이루어집니다. 

<br>

### `Shallow copy란?`

간단히 말하면 객체 자체를 복사하는 것이 아니라 객체가 가리키고 있는 곳의 주소를 복사한다는 뜻입니다.
그래서 얕은 복사라고 하는 것 같습니다. 

```java
public class ArrayTest {
    public static void main(String[] args) {
        int[] a = {1, 2, 3};
        int[] b = a;    // 얕은 복사 (메모리의 같은 곳을 가르킴)

        a[0] = 10;
        System.out.println(Arrays.toString(a));
        System.out.println(Arrays.toString(b));
    }
}
```
```
[10, 2, 3]
[10, 2, 3]
```

위와 같이 원본을 수정했을 때 복사본도 같이 수정이 되는 복사를 `shallow copy`라고 합니다. a가 가리키고 있는 주소를 b에 대입을 했기 때문에 같은 곳을 가리키게 되는 것입니다.

<br>

### `Deep copy란?`

```java
public class ArrayTest {
    public static void main(String[] args) {
        int[] a = {1, 2, 3};
        int[] b = a.clone();

        a[0] = 10;
        System.out.println(Arrays.toString(a));
        System.out.println(Arrays.toString(b));
    }
}
```
```
[10, 2, 3]
[1, 2, 3]
```

위의 경우는 원본을 수정했을 때 복사본에 영향을 미치지 않는 것을 볼 수 있습니다. 이것은 a가 가리키고 있는 주소를 복사한 것이 아니라 a의 객체를 복사해 새로운 메모리에 할당한 후에 
그 메모리를 b가 가리키게 되는 것입니다. 
(`clone()` 메소드는 단순히 객체에 저장된 값을 그대로 복사할 뿐, 객체가 참조하고 있는 객체까지 복제하지는 않습니다.) 

그러면 다시 `clone()` 메소드에 대해서 알아보겠습니다. 

<br>

### `clone()을 사용하려면 Cloneable 인터페이스를 구현해야 합니다.`

```java
public interface Cloneable {
}
```

내부에 아무 것도 없는 인터페이스 입니다. 직렬화처럼 `Cloneable` 인터페이스를 구현해야 자바에서 clone()을 사용할 수 있게 해주는 것 같습니다.

> Cloneable 인터페이스를 구현한 클래스의 인스턴스만 clone()을 통한 복제가 가능한데, 그 이유는 인스턴스의 데이터를 보호하기 위해서입니다.
> Cloneable 인터페이스가 구현되어 있다는 것은 클래스 작성자가 복제를 허용한다는 의미입니다. 

<br>

## `얕은 복사와 깊은 복사의 예제 코드`

```java
public class Circle implements Cloneable {
    Point p;
    double r;

    public Circle(Point p, double r) {
        this.p = p;
        this.r = r;
    }

    // 얕은 복사
    public Circle shallowCopy() {
        Object obj = null;
        try {
            obj = super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return (Circle) obj;
    }

    // 깊은 복사
    public Circle deepCopy() {
        Object obj = null;
        try {
            obj = super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        Circle c= (Circle)obj;
        c.p = new Point(this.p.x, this.p.y);
        return c;
    }

    @Override
    public String toString() {
        return "Circle{" +
                "p=" + p +
                ", r=" + r +
                '}';
    }
}
```

위와 같이 Circle 클래스 내부의 인스턴스 변수로 Point 클래스가 존재합니다. 

```java
public class Point {
    int x, y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return "Point{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }

    public static void main(String[] args) {
        Circle c1 = new Circle(new Point(1, 1), 2.0);
        Circle c2 = c1.shallowCopy();
        Circle c3 = c1.deepCopy();

        System.out.println(c2);
        System.out.println(c3);

        c1.p.x = 9;
        c1.p.y = 9;
        System.out.println("==============변경후==============");
        System.out.println(c2);   // 얕은 복사
        System.out.println(c3);   // 깊은 복사
    }
}
```
```
Circle{p=Point{x=1, y=1}, r=2.0}
Circle{p=Point{x=1, y=1}, r=2.0}
==============변경후==============
Circle{p=Point{x=9, y=9}, r=2.0}
Circle{p=Point{x=1, y=1}, r=2.0}
```

Point 클래스는 위와 같습니다. 그리고 `얕은 복사`, `깊은 복사`의 차이를 출력해보면 얕은 복사는 원본을 바꿨을 때 복사본에도 영향을 주고 깊은 복사는 영향을 주지 않는 것을 확인할 수 있습니다.

- `얕은 복사 : 참조변수가 가리키는 주소를 복사해서 넘깁니다.(따라서 같은 곳을 가리키게 됩니다)`
- `깊은 복사 : 객체를 복사한 후에 새로운 메모리를 할당합니다.`

이러한 개념만 생각한다면 결과를 예측할 수 있을 것입니다. 아래의 글도 같이 참고해서 보면 좋을 것 같습니다. 

- [자바는 Call By Value 일까 Call By Reference 일까?](https://devlog-wjdrbs96.tistory.com/44?category=882228)