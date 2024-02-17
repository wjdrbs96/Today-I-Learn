## 오버라이딩(Overriding)

부모 클래스로부터 상속받은 메소드의 내용을 변경하는 것을 `오버라이딩`이라고 한다.

```java
public class Point {
    int x;
    int y;
    
    String getLocation() {
        return "x : " + x + ", y : " + y; 
    }
}

class Point3D extends Point {
    int z;
    
    // 오버라이딩
    String getLocation() {
        return "x : " + x + ", y : " + y + ", z : " + z;
    }
}
```

위와 같이 부모클래스인 `Point`에서 `getLocation()`메소드를 자식클래스인 `Point3D`클래스에서 `재정의`해서 사용할 수 있다.
이것을 `오버라이딩`이라고 한다. 

<br>


### 오버라이딩의 조건

```
자손 클래스에서 오버라이딩하는 메소드는 조상 클래스의 메소드와 

- 이름이 같아야 한다.
- 매게변수가 같아야 한다.
- 반환타입이 같아야 한다.
```


* 접근제어자는 조상 클래스의 메소드보다 좁은 범위로 변경 할 수 없다.
    * 만약 조상 클래스에 정의된 메소드의 접근 제어자가 `protected`라면 이를 오버라이딩 하는 자식 클래스의 메소드는 
    `protected`나 `public`이어야 한다. 
    
* 조상 클래스의 메소드보다 많은 수의 예외를 선언할 수 없다.

```java
class Parent {
    void parentMethod() throws IOException, SQLException {
        // logic
    }
}

class Child extends Parent {
    
    @Override
    void parentMethod() throws IOException {
        
    }
}
```    
    
위의 코드를 보면 알 수 있듯이 부모클래스의 메소드보다 자식클래스의 `오버라이딩`된 메소드의 예외가 적거나 같아야 한다.
`하지만 주의해야할 점은 단순히 선언된 예외의 개수의 문제가 아니라는 것이다`

<br>

```java
class Parent {
    void parentMethod() throws IOException, SQLException {
        // logic
    }
}

class Child extends Parent {

    @Override
    void parentMethod() throws Exception {
        // logic
    }
}
```

위와 같이 `오버라이딩`을 한다면 부모 클래스의 메소드보다 적은 수의 예외를 선언한 것처럼 보이지만 `Exception`
은 모든 예외의 최고 조상이므로 가장 많은 개수의 예외라고 생각하면 된다.


* 인스턴스 메소드를 static 메소드로, static 메소드를 인스턴스 메소드로 변경하여 `오버라이딩` 할 수 없다.

<br>

### Q. 조상 클래스에 정의된 static 메소드를 자손 클래스에서 똑같은 이름의 static 메소드로 정의할 수 있나요?

```
가능하지만, 이것은 클래스에 별개의 static 메소드를 정의한 것일 뿐 오버라이딩은 아니다. 각 메소드는 클래스 이름으로 구별될 수 있으며,
호출할 때는 `클래스.메소드이름()`으로 호출하는 것이 바람직하다.
```

<br>

## 오버로딩(Overloading)

`오버로딩`은 기존에 없는 메소드를 새로 정의하는 것이다. 
다시 정리하면 두 메서드가 같은 이름을 갖고 있으나 인자의 수나 자료형이 다른 경우를 말한다.
                                         
                                         
                          
```java
public class Point {
    int x;
    int y;

    public Point(int x) {
        this.x = x;
    }

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
```

위와 같이 `생성자` or `메소드`의 이름이 같아도 매개변수 개수의 차이가 있는 것을 말한다. 