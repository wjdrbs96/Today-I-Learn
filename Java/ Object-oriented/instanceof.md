## instanceof 연산자

참조변수가 참조하고 있는 인스턴스의 실제 타입을 알아보기 위해 `instanceof연산자`를 사용한다. 주로 조건문에 사용되며, 
`instanceof`의 왼쪽에는 `참조변수`를 오른쪽에는 `타입(클래스명)`이 피연산자로 위치한다. 그리고 연산의 결과로 boolean값인 true와
false 중의 하나를 반환한다.

```java
public class Car {
    String color;
    int door;
    
    void drive() {
        System.out.println("드라이브");
    }
    
    void stop() {
        System.out.println("멈춰");
    }
}

class FireEngine extends Car {
    void water() {
        System.out.println("물뿌리기");
    }
}

class Ambulance extends Car {
    void siren() {
        System.out.println("싸이렌");
    }
}
```  

현재 부모 클래스로 `Car`이 있고, 자식 클래스로 `FireEngine`, `Ambulance`가 있는 상태이다. 

<br>

```java
public class Test {
    void doWork(Car c) {
        if (c instanceof FireEngine) {
            FireEngine fe = (FireEngine)c;
            fe.siren();
        }
        else if (c instanceof Ambulance) {
            Ambulance a = (Ambulance)c;
            a.siren();
        }
    }    
}
```

위의 코드를 보면 doWork() 메소드의 매게변수는 `Car`타입이다. 따라서 `Car`타입 or `자식클래스`타입으로 인스턴스를 넘겨받겠지만
메소드 내부에서는 어떤 타입인지 모르기 때문에 `instanceof`를 이용해서 참조변수 c가 가리키고 있는 인스턴스의 타입을 체크하고, 적절히
형변환을 해야 한다.

<br>

```java
public class Test {
    public static void main(String[] args) {
        FireEngine fe = new FireEngine();

        if (fe instanceof FireEngine) {  // 다운캐스팅 가능
            System.out.println("This is a FireEngine instance");
        }
        if (fe instanceof Car) {         // 업캐스팅 가능
            System.out.println("This is a Car instance");
        }
        if (fe instanceof Object) {      // 업캐스팅 가능
            System.out.println("This is a Object instance");
        }
        System.out.println(fe.getClass().getName());
    }
}

class Car { }

class FireEngine extends Car { }
```

```
This is a FireEngine instance
This is a Car instance
This is a Object instance
Java.FireEngine
```

코드의 결과는 위와 같다. 생성된 인스턴스의 타입은 `FireEngine` 이지만, `Object`타입과 `Car`타입의 `instanceof연산`에서도 true를 결과로 얻었다.
왜냐하면 `FireEngine`클래스는 `Car`클래스와 `Object`클래스를 상속받았기 때문에, `FireEngine`인스턴스는 `Object`인스턴스와 
`Car`인스턴스를 포함하고 있는 셈이기 때문이다.
 
<br>

참조변수.getClass().getName()은 참조변수가 가리키고 있는 인스턴스의 클래스 이름을 문자열(String)으로 반환한다. 

```
어떤 타입에 대한 instanceof연산의 결과가 true라는 것은 검사한 타입으로 형변환이 가능하다는 것을 뜻한다. 
```