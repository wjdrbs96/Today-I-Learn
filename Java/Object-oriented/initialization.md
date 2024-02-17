## `변수의 초기화`
    
변수를 선언하고 처음으로 값을 저장하는 것을 `변수의 초기화`라고 한다. 가능하면 선언과 동시에 적절한 값으로 변수를 초기화 하는 것이 좋다. <br>

멤버변수는 초기화를 하지 않아도 자동적으로 변수의 자료형에 맞는 기본값으로 초기화가 이루어지므로 초기화하지 않고 사용해도 되지만,
`지역변수는 사용하기 전에 반드시 초기화해야 한다`. 

```java
public class Test {
    int x;               // 인스턴스변수 
    int y = x;           // 인스턴스변수
    
    void method1() {
        int i;          // 지역변수
        int y = i;      // 에러. 지역변수를 초기화하지 않고 사용
    }
}
```

<br>

### `명시적 초기화(explicit initalization)`

변수를 선언과 동시에 초기화하는 것을 명시적 초기화라고 한다. 

```java
public class Test {
    int x = 4;                              // 기본형(primitive type) 변수의 초기화
    JavaTest javaTest = new JavaTest();     // 참조형(reference type) 변수의 초기화
}
```

명시적 초기화가 간단하고 명료하긴 하지만, 보다 복잡한 초기화 작업이 필요할 때는 `초기화 블럭(initialization block)` 또는 생성자를
사용해야 한다.

<br>

### `초기화 블럭(initialization block)`

초기화 블럭에는 `클래스 초기화 블럭`과 `인스턴스 초기화 블럭` 두 가지 종료가 있다. `클래스 초기화블럭`은 클래스변수의 초기화에 사용되고, `인스턴스 초기화블럭`은 인스턴스 변수의 초기화에 사용된다. 

```
클래스 초기화 블럭 : 클래스변수의 복잡한 초기화에 사용된다.
인스턴스 초기화 블럭 : 인스턴스 변수의 복잡한 초기화에 사용된다.
```

```java
public class Test {
    static { /*클래스 초기화 블럭*/ }

    { /*인스턴스 초기화 블럭*/ }
}
```

`클래스 초기화 블럭`은 클래스가 메모리에 처음 로딩될 때 한번만 수행되며, `인스턴스 초기화 블럭`은 생성자와 같이 인스턴스를 생성할 때 마다 수행된다. <br>
그리고 `생성자 보다 인스턴스 초기화 블럭이 먼저 수행`된다는 사실도 기억해두자. 

<br>

```java
public class Test {
    int count = 0;
    int serialNo = 0;
    String color;
    String gearType;
    
    public Test() {
        count++;                                    // 중복코드
        serialNo = count;                           // 중복코드
        color = "White";
        gearType = "Auto";
    }

    public Test(String color, String gearType) {
        count++;                                    // 중복코드
        serialNo = count;                           // 중복코드
        this.color = color;
        this.gearType = gearType;
    }
}
```

위와같이 기본생성자와 인자가 2개인 생성자에서 코드가 중복되는 것을 볼 수 있다. 이렇게 모든 생성자에서 공통으로 수행되어야 하는 곳에 사용한다.

<br>

```java
public class Test {
    int count = 0;
    int serialNo = 0;
    String color;
    String gearType;
    
    // 인스턴스 초기화 블럭
    {
        count++;
        serialNo = count;
    } 
    
    public Test() {
        color = "White";
        gearType = "Auto";
    }

    public Test(String color, String gearType) {
        this.color = color;
        this.gearType = gearType;
    }
}
```

위와 같이 공통의 코드를 `인스턴스 초기화 블럭`을 이용해서 초기화 할 수 있다.

<br>

```java
public class Test {
    static {
        System.out.println("static { }");       // 클래스 초기화 블럭
    }

    {
        System.out.println("{ }");             // 인스턴스 초기화 블럭
    }

    public Test() {
        System.out.println("생성자");
    }

    public static void main(String[] args) {
        System.out.println("Test test = new Test();");
        Test test = new Test();

        System.out.println("Test test2 = new Test();");
        Test test2 = new Test();
    }
}
```

```
statc { }
Test test = new Test();
{ }
생성자
Test test2 = new Test();
{ }
생성자
```

위의 코드가 실행되면서 Test가 메모리에 로딩될 때, `클래스 초기화 블록`이 가장 먼저 수행된다. 그리고 Test 객체를 만들면 `인스턴스 초기화 블럭`이 실행된다. 그리고 `클래스 초기화 블록`은 클래스가
처음 메모리에 로딩될 때 한번만 수행되었지만, `인스턴스 초기화 블럭`은 인스턴스가 생성될 때 마다 수행된다.

<br>

### `멤버변수의 초기화 시기와 순서`

```
클래스변수의 초기화시점 : 클래스가 처음 로딩될 때 단 한번 초기화 된다.
인스턴스변수의 초기화시점 : 인스턴스가 생성될 때 마다 각 인스턴스별로 초기화가 이루어진다.

클래스변수의 초기화순서 : 기본값 -> 명시적초기화 -> 클래스 초기화 블럭
인스턴스변수의 초기화순서 : 기본값 -> 명시적초기화 -> 인스턴스 초기화 블럭 -> 생성자
```

