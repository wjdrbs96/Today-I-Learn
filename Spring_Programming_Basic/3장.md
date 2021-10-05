# `3장: 스프링 DI`

`DI는 Dependency Injention`의 약자로 `의존 주입`이라고 합니다.

```java
public class Car {
    private Tire tire;

    public Car() {
        this.tire = new KoreaTire();
    }
}
```

`의존성을 단순하게 표현하면 new`라고 할 수 있습니다. 즉, 위의 코드는 Car가 Tire에 의존한다라고 할 수 있습니다. 그런데 다들 아시겠지만 이렇게 내부에서 new를 통해서 객체를 생성하면 `유지보수 관점`에서 문제점이 존재한다고 하는데요.

<br>

## `그 이유가 무엇일까요?`

![tire](https://user-images.githubusercontent.com/45676906/115903918-2015ff80-a49f-11eb-89fa-93550804ec25.png)

만약에 `Tire` 인터페이스를 구현하는 `KoreaTire`, `AmericaTire` 2개의 클래스가 존재한다고 생각해보겠습니다. 이런 경우에 위에처럼 `Car` 클래스에 `new KoreaTire`로 결합을 시켜버리면 어떻게 될까요? 클라이언트 코드는 아래와 같을 것입니다.

```java
public class Test {
  public static void main() {
    Car korea = new Car();
    korea.move(); 
  }
}
```

만약에 위와 같이 클라이언트 쪽에서 코드를 수정하고 있는데 `KoreaTire` 대신 `AmericaTire` 라던지 다른 나라의 Tire를 사용하고 싶다면 어떻게 해야할까요? `Car` 클래스 내부의 코드도 수정이 필요하고 클라이언트 코드의 수정도 필요할 것입니다. 이거부터 `SOLID` 원칙인 `Single Responsibility priciple(딘일 책임 원칙)`에 위배 된다는 것을 알 수 있습니다.

`두 번째`로 만약에 테스트 코드를 작성한다고 가정해보겠습니다. `Car` 인터페이스를 구현하는 클래스가 한 10개 정도로 늘어나서 10개의 타이어에 대해서 테스트를 해보아야 하는 상황인데요.

그런데 위처럼 `Car` 클래스는 `KoreaTire` 만을 사용할 수 있도록 서로 `강한 결합도`가 존재한다면 다른 타이어 클래스를 쉽게 바꾸면서 테스트 하기가 쉽지 않을 것입니다.

<br> <br>

## `외부에서 의존성 주입하기`

```java
public class Car {
    private Tire tire;

    public Car(Tire tire) {
        this.tire = tire;
    }
}
```

위에서 말했던 단점들로 인해서 한 클래스의 의존성은 외부에서 주입해주는 것이 더 좋다는 것을 느낄 수 있었을 것입니다.

![tire](https://user-images.githubusercontent.com/45676906/115905688-68362180-a4a1-11eb-9e5f-3d098cb4baaf.png)

위와 같이 구조를 바꾸게 되면 자연스럽게 `전략 패턴`을 사용하게 되는 구조가 나옵니다. 클라이언트 코드를 같이 보겠습니다.

```java
public class Test {
    public static void main(String[] args) {
        Tire tire = new KoreaTire();
        Car car = new Car(tire);
    }
}
```

위와 같이 클라이언트에서 어떤 타이어를 사용할 것인지를 정하기 때문에 나중에 타이어의 변경이 있어도 클라이언트 코드가 변경하면 됩니다. 즉, `Tire 클래스는 모듈화를 시켜서 관리할 수 있다는 장점`이 존재합니다.

<br> <br>

## `객체 조립기`

위의 예시를 들 때는 `main 메소드(클라이언트)`에서 객체를 생성해서 주입하는 것을 했었는데요. 좀 더 좋은 방법을 생각한다면 `객체를 생성하고 주입하는 클래스를 따로 만드는 것`도 나쁘지 않습니다. 즉, 이것을 `조립기 클래스`라고 할 수 있습니다.

```java
public class Assembler {
  private Tire tire;

  public Assembler() {
    tire = new KoreaTire();
  }
  // getter 존재
}
```

```java
public class Test {
  public static void main() {
    Assembler assembler = new Assembler();
  }
}
```

그리고 의존 객체의 변경 사항이 있을 때는 `Assembler` 클래스의 코드만 수정해주면 됩니다.
이렇게 Spring Framework를 사용하지 않고도 `Dependency Injection`을 사용한 것을 볼 수 있는데요. 이번에는 스프링을 사용해서 한번 해보겠습니다.

<br> <br>

## `스프링의 DI 설정`

![di](https://camo.githubusercontent.com/6f0ffbb5cdae051edc52da8427afc8d219a85a9962f774daf0c7c3c6ae0f3d94/68747470733a2f2f696d67312e6461756d63646e2e6e65742f7468756d622f523132383078302f3f73636f64653d6d746973746f72793226666e616d653d6874747073253341253246253246626c6f672e6b616b616f63646e2e6e6574253246646e2532464951487a3725324662747147786d68447456502532464e474f7865616164416e72344c7076537234496b5831253246696d672e706e67)

스프링의 삼각형 중에 하나일 정도로 중요한 개념인 `DI`인데요. 스프링이 `DI`를 지원하는 `조립기`라고 할 수 있습니다.

- `생성자 주입`

- `setter 주입`

- `필드 주입`

<br>

스프링에서 의존성 주입을 지원하는 방식은 총 3가지 인데요. 하나씩 어떤 특징을 가지고 있는지 정리해보겠습니다.

<br> <br>

## `생성자 주입`

```java
public class Car {
    private Tire tire;

    public Car(Tire tire) {
        this.tire = tire;
    }
}
```

생성자 주입은 위에서 보았던 예시처럼 생성자를 사용해서 외부에서 주입을 해주는 것입니다.

<br>

## `Setter 주입`

```java
public class Car {
    private Tire tire;

    public Tire getTire() {
        return tire;
    }

    public void setTire(Tire tire) {
        this.tire = tire;
    }
}
```

```java
public class Client {
    public static void main(String[] args) {
        Tire tire = new KoreaTire();
        Car car = new Car();
        car.setTire(tire);   // setter 의존성 주입
    }
}
```

생성자를 통해 의존성을 주입하면 한번 장착한 타이어는 더 이상 타이어를 교체할 수 없다는 문제점이 존재합니다. 운전자가 원할 때 타이어를 교체하고 싶다면 setter 의존성 주입을 사용해야 합니다.

<br> <br>

## `생성자 의존성 주입 vs setter 의존성 주입`

하지만 프로그래밍 세계에서는 생성자를 통해 의존성을 주입하는 방법을 권장하고 있습니다. 실세계라면 자동차의 타이어를 바꾸는 일이 빈번할 수도 있지만, `프로그래밍에서는 한번 주입된 의존성을 게속 사용하는 경우가 더 일반적이기 때문`입니다.

<br>

### `생성자 주입을 선택하라`

- 대부분의 의존관계 주입은 한번 일어나면 애플리케이션 종료시점까지 의존관계를 변경할 일이 없습니다.(오히려 대부분의 의존관계는 애플리케이션 종료 전까지 변하면 안됩니다. 불변해야 합니다.)
- 수정자 주입을 사용하면 setter 메소드를 public으로 열어두어야 합니다.

- 누군가 실수로 변경할 수도 있고, 변경하면 안되는 메소드를 public 으로 열어두는 것은 좋은 설계가 아닙니다.

- 생성자 주입은 객체를 생성할 때 딱 한번만 호출되므로 이후에 호출되는 일이 없습니다. 따라서 불변하게 설계할 수 있습니다.

<br>

`setter` 주입의 단점을 코드로 알아보겠습니다.

```java
public class OrderServiceImpl {
    private MemberRepository memberRepository;
    private DiscountPolicy discountPolicy;

    @Autowired
    public void setMemberRepository(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Autowired
    public void setDiscountPolicy(DiscountPolicy discountPolicy) {
        this.discountPolicy = discountPolicy;
    }

    public Order createOrder(Long memberId, String item, int itemPrice) {
        Member member = memberRepository.findById(memberId);
        int discountPrice = discountPolicy.discount(member, itemPrice);
        return new Order(memberId, item, itemPrice, discountPrice);
    }
}
```

코드 자체를 해석하려는 것보다 위의 코드를 보고 왜 setter 주입을 지양해야 하는지 정도의 느낌만 이해하면 될 것 같습니다. 현재 OrderServiceImpl 클래스에 필드로 `MemberRepository`, `DiscountPolicy` 두개가 존재합니다.

그리고 둘 다 setter 주입을 사용하고 있습니다. 그런데 이 때 OrderServiceImpl 클래스 자체만 테스트 하고 싶다면 어떻게 될까요?

```java
class OrderServiceImplTest {

    @Test
    void createOrder() {
        OrderServiceImpl orderService = new OrderServiceImpl();
        orderService.createOrder(1L, "item", 1000);
    }
}
```

그래서 위와 같이 `OrderServiceImpl` 클래스의 createOrder 메소드를 테스트 하기 위한 간단한 테스트 코드를 작성하였습니다. 위의 테스트 코드는 성공할 수 있을까요?

<br>

![스크린샷 2021-04-24 오전 2 48 02](https://user-images.githubusercontent.com/45676906/115910216-8737b200-a4a7-11eb-8343-d8eea2905042.png)

코드의 결과는 `NullPointerException`이 발생합니다. 에러가 발생하는 이유는 무엇일까요? createOrder() 메소드를 보면 `MemberRepository`, `DiscountPolicy`를 사용하고 있습니다. 하지만 테스트 코드를 작성할 때는 두 클래스를 new 하지 않았기 때문에 에러가 발생하는 것입니다.

그래서 `setter 주입을 생성자 주입`으로 바꿔서 위와 같이 테스트를 진행해보겠습니다.

```java
public class OrderServiceImpl {
    private MemberRepository memberRepository;
    private DiscountPolicy discountPolicy;

    public OrderServiceImpl(MemberRepository memberRepository, DiscountPolicy discountPolicy) {
        this.memberRepository = memberRepository;
        this.discountPolicy = discountPolicy;
    }

    public Order createOrder(Long memberId, String item, int itemPrice) {
        Member member = memberRepository.findById(memberId);
        int discountPrice = discountPolicy.discount(member, itemPrice);
        return new Order(memberId, item, itemPrice, discountPrice);
    }
}
```

코드는 위와 같이 바뀝니다. 그리고 테스트 코드를 확인해보겠습니다.

<br>

![스크린샷 2021-04-24 오전 2 52 12](https://user-images.githubusercontent.com/45676906/115910714-20ff5f00-a4a8-11eb-94a6-931c03c36c7c.png)

이번에는 `생성자 주입`이기 때문에 `OrderServiceImpl` 객체를 만들 때 내부 클래스 필드의 객체 생성을 강제하고 있습니다. 즉, 컴파일에러가 발생함으로써 개발자에게 `NullPointerException`이 날 수 있는 상황을 사전에 막게 해줍니다.

<br>

![스크린샷 2021-04-24 오전 2 53 55](https://user-images.githubusercontent.com/45676906/115910920-6459cd80-a4a8-11eb-9bc2-7ad5d184fc6a.png)

테스트를 위해서 임시로 위와 같이 객체를 new 해서 테스트 할 수 있습니다. 반면에 setter는 컴파일 에러가 발생하지 않기 때문에 실수로 객체 생성을 하지 않을 수도 있기 때문에 에러가 발생할 위험이 높습니다. (사람은 반드시 실수를 하기 때문에..)

<br>

## `필드에 final을 써야 하는 이유`

위에서 말했듯이.. 사람은 항상 실수를 할 확률이 높습니다. 까먹고 뭐를 안적었다던지.. 등등

<br>

![스크린샷 2021-04-24 오전 2 57 59](https://user-images.githubusercontent.com/45676906/115911414-05e11f00-a4a9-11eb-9ac6-e61e9baa3eba.png)

위와 같이 `생성자 의존성 주입`을 할 때도 생성자에 실수로 필드를 적지 않을 수 있습니다. 이러면 컴파일 에러가 발생하지 않기 때문에.. 테스트 코드를 실행해보기 전까지는 알 수 없습니다.

<br>

![스크린샷 2021-04-24 오전 3 00 32](https://user-images.githubusercontent.com/45676906/115911656-50fb3200-a4a9-11eb-9ea9-26639d0ab3b2.png)

그래서 이러한 경우를 사전에 방지하고자 필드에 `final` 키워드를 사용하는 것을 권장합니다. final 키워드가 붙어있는데 생성자에 초기화 되지 않는다면 컴파일 에러가 발생하기 때문에 개발자의 실수를 사전에 막을 수 있다는 장점이 있기 때문입니다.

이렇게 이번 글에서는 `의존성 주입이란 무엇일까?`에 대해서 간단하게 알아보았습니다. 지금은 마지막에 조금..? 스프링으로 의존성 주입하는 것을 해보았지만.. 처음에 보았던 예시는 스프링을 사용하지 않고 의존성 주입을 했던 예제입니다.

<br> <br>

