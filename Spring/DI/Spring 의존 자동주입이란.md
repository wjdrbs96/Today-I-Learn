# `4장: 의존 자동 주입`

[Spring 의존성 주입이란?](https://github.com/wjdrbs96/Gyunny_Spring_Study/blob/master/spring/2%EC%A3%BC%EC%B0%A8/2.%20%EC%8A%A4%ED%94%84%EB%A7%81%EC%97%90%EC%84%9C%20%EC%9D%98%EC%A1%B4%EC%84%B1%20%EC%A3%BC%EC%9E%85.md) 글에서 Spring 에서의 의존성 주입 개념에 대해서 정리했습니다.
`Spring IoC 컨테이너`에 Bean으로 등록된 것들을 의존관계를 주입하기 위해서는 대표적으로 `@Autowired` 어노테이션을 사용합니다. 의존성 주입을 하는 방법에는 크게 3가지로 나눌 수 있습니다.

- `생성자 주입`
- `setter 주입`
- `필드 주입`

<br>

하나씩 어떤 특징들이 있는지 알아보겠습니다.

<br>

## `생성자 주입이란?`

[의존성 주입이란?](https://github.com/wjdrbs96/Gyunny_Spring_Study/blob/master/spring/2%EC%A3%BC%EC%B0%A8/1.%20%EC%9D%98%EC%A1%B4%EC%84%B1%20%EC%A3%BC%EC%9E%85%EC%9D%B4%EB%9E%80%3F.md) 글에서 `생성자 주입을 사용해야 하는 이유`에 대해서 알아보았는데요.(읽어보지 않았다면 먼저 읽어보고 오셔야 합니다.) 여기서도 한번 더 정리를 해보겠습니다.

```java
@RestController
public class HelloController {
    
    private final HelloService helloService;

    @Autowired
    public HelloController(HelloService helloService) {
        this.helloService = helloService;
    }
}
```

위와 같이 `생성자`에 `@Autowired` 어노테이션을 달아주면서 의존성 주입을 한다는 뜻입니다. 간단히 그림으로 표현하면 아래와 같습니다.(아마두..?)

<br>

<img width="286" alt="스크린샷 2021-04-24 오후 3 31 56" src="https://user-images.githubusercontent.com/45676906/115949761-3610d800-a512-11eb-9acc-f33f194eb5ed.png">

`HelloController`, `HelloService`는 모두 애플리케이션이 시작되면 `@ComponentScan`에 의해서 `@RestController`, `@Service` 어노테이션이 붙어 있는 클래스는 모두 IoC 컨테이너에 Bean으로 등록이 될 것입니다.

그리고 `@Autowired`를 사용해서 생성자 주입을 하고 있기 때문에 `IoC 컨테이너`가 `HelloService`의 객체를 만들어서 `HelloController` 객체를 만들 때 주입을 시켜주는 것입니다.

간단하게 말하면 위와 같은 과정으로 `Spring은 의존성 주입`을 해줍니다. 그러면 `IoC 컨테이너에 존재하지 않는 Bean을 가지고 의존성 주입을 시도하면 어떻게 될까요?`

<br>

<img width="272" alt="스크린샷 2021-04-24 오후 3 36 15" src="https://user-images.githubusercontent.com/45676906/115949867-cf3fee80-a512-11eb-9432-fc31f0ef6a71.png">

`HelloService`에 `@Service` 어노테이션을 넣지 않은 상태입니다. 그래서 IoC 컨테이너 내부에도 Bean으로 등록되지 않은 것을 볼 수 있습니다.

<br>

![스크린샷 2021-04-24 오후 3 37 31](https://user-images.githubusercontent.com/45676906/115949906-0910f500-a513-11eb-8fa7-3756992c45b0.png)

그러면 `IntelliJ`에서도 `Bean으로 등록되지 않았음을 감지`하고 위와 같이 `Could not autowire. No beans of 'HelloService' type found`라고 뜨는 것을 볼 수 있습니다.

<br>

![스크린샷 2021-04-24 오후 3 39 32](https://user-images.githubusercontent.com/45676906/115949980-73299a00-a513-11eb-98fb-7548ef777e39.png)

실제로 실행을 해보아도 `HelloService가 Bean으로 등록되지 않았다는 것`을 볼 수 있습니다. 즉 스프링이 의존성 주입을 하기 위해서는 `IoC 컨테이너에 등록된 Bean 이어야` 합니다.

그리고 `생성자 주입`은 외부에서 주입해주는(HelloService)가 존재하지 않는다면 `HelloController`도 만들어지지 않는다는 특징이 있습니다. (당연히.. 생성자 만들 때 에러가 발생하기 때문입니다.)

또한 `생성자 주입에서 중요하게 보아야 할 것은 또 있습니다.`

- `생성자 호출시점에 딱 1번만 호출되는 것이 보장된다.`
- `불변, 필수 의존관계에 사용한다.`

<br>

```java
@RestController
public class HelloController {

    private final HelloService helloService;

    public HelloController(HelloService helloService) {
        this.helloService = helloService;
    }
}
```

애플리케이션이 생성되어 `HelloController`가 만들어지는 시점에 딱 한번만 주입할 수 있고 더 이상 변경할 수 없습니다.(참고로 Spring 4.3 이상부터는 `생성자가 하나인 경우`에는 `@Autowired`를 사용하지 않아도 의존성 주입이 가능합니다.)

<br> <br>

## `해당 타입의 Bean이 여러 개인 경우라면?`

자동 주입 가능한 빈이 두 개 이상이면 자동 주입할 빈을 지정할 수 있는 방법이 필요합니다. 바로 코드로 예시를 들어보겠습니다.

```java
@Repository
public class MyBookRepository implements BookRepository {
    // test
}
```

```java
@Repository
public class YourBookRepository implements BookRepository {
    // test
}
```

만약에 위와 같이 `BookRepository` 인터페이스를 구현하는 클래스 2개가 존재한다고 가정하겠습니다. 그리고 각각 `@Repository` 어노테이션을 사용해서 `Bean`으로 등록되어 있는 상태인데요.
이 때 `BookRepository`에 `@Autowired`를 사용해서 `의존성 주입`을 하면 어떤 일이 벌어질까요?

<br>

```java
@RequiredArgsConstructor
@Component
public class Test {

    private final BookRepository bookRepository;

}
```

![스크린샷 2021-08-23 오전 1 14 28](https://user-images.githubusercontent.com/45676906/130362302-f45a60d0-6e1f-4989-826f-8cc2b8f7bf5e.png)


그리고 실행하면 위와 같이 `Bean이 2개`라서 주입할 수 없다는 에러가 발생한 것을 볼 수 있습니다. 이 때 `@Qualifier` 어노테이션과 `@Primary` 어노테이션을 사용할 수 있습니다.

먼저 `@Primary` 어노테이션을 먼저 보면 아래와 같은데요.

```java
@Repository @Primary
public class MyBookRepository implements BookRepository {
}
```

위와 같이 주입의 가장 우선순위를 높게 줘서 `MyBookRepository`가 주입이 되도록 해주는 어노테이션입니다. `@Primary는 Bean이 여러 개일 때 사용하기에 가장 좋은 어노테이션입니다.`
그리고 `@Qualifier`를 보겠습니다.

<br>

```java
@Component
public class Test {

    @Autowired @Qualifier("myBookRepository")
    private BookRepository bookRepository;

}
```

`@Qualifier`는 위와 같이 `myBookRepository`라는 빈의 이름을 주입을 먼저 주입을 해줍니다.(일반적으로 빈의 이름은 클래스 이름의 스몰케이스!)
하지만 이 방법은 혹시나 스펠링을 잘못적어서 에러가 발생할 수도 있다는 단점이 존재하기 때문에 `@Primary` 어노테이션을 사용하는 것이 좋습니다.

<br> <br>

## `@Autowired에서 Bean을 자동 주입하고 싶지 않을 때`

```java
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookService {

    private BookRepository bookRepository;
    
    @Autowired(required = false)
    public void setBookRepository(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }
}
```

위와 같이 `@Autowired(required = false)`를 주면 `bookRepository`는 Bean으로 등록되지 않습니다. (기본 값은 true!!)


<br> <br>

## `Setter 주입이란?`

말 그대로 setter에 `@Autowired`를 어노테이션을 추가해서 의존성 주입을 하는 것입니다.

```java
@RestController
public class HelloController {

    private HelloService helloService;

    @Autowired
    public void setHelloService(HelloService helloService) {
        this.helloService = helloService;
    }
}
```

setter 주입은 생성자 주입과는 다르게 의존성을 계속 변경할 수 있습니다. 이것 또한 [Spring 의존성 주입이란?](https://github.com/wjdrbs96/Gyunny_Spring_Study/blob/master/spring/2%EC%A3%BC%EC%B0%A8/2.%20%EC%8A%A4%ED%94%84%EB%A7%81%EC%97%90%EC%84%9C%20%EC%9D%98%EC%A1%B4%EC%84%B1%20%EC%A3%BC%EC%9E%85.md) 글에서 다루었던 내용입니다. 즉, 의존성을 바꿀 수 있기 때문에 `final` 키워드를 사용할 수 없습니다.

그리고 `setter`로 누구나 바꿀 수 있도록 public 으로 열어놓으면 나중에 찾을 수 없는 버그를 발생시키고 합니다.(다른 사람들이 막 바꿀 수도 있기 때문에..)
하지만 위에서 말했듯이.. 최근에는 `생성자 의존성 주입`을 권장하고 있습니다.

<br>

## `참고하기`

- `@Autowired의 기본 동작은 주입할 대상이 없으면 오류가 발생합니다.`
- `주입할 대상이 없어도 동작하게 하려면 @Autowired(required=false)로 지정하면 됩니다.`

```java
@RestController
public class HelloController {

    private HelloService helloService;

    @Autowired(required = false)
    public void setHelloService(HelloService helloService) {
        this.helloService = helloService;
    }
}
```

<br> <br>

## `Field 주입이란?`

이것 또한 이름 그대로 필드에다 주입을 하는 것입니다.

```java
@RestController
public class HelloController {

    @Autowired
    private HelloService helloService;
}
```

<br>

필드 주입은 `스프링에서 권장하고 있지 않는 방법`입니다.

![스크린샷 2021-04-24 오후 5 47 17](https://user-images.githubusercontent.com/45676906/115953227-2bac0980-a525-11eb-8178-8b843d1ec849.png)

<br>

`필드 주입을 하고 마우스를 가져다 대면` 위와 같이 `Field Injection is Not Recommand`라고 뜨는 것을 볼 수 있습니다. 즉, 정리하면 아래와 같습니다.

- `코드가 간결해서 많은 개발자들을 유혹하지만 외부에서 변경이 불가능해서 테스트 하기 힘들다면 치명적인 단점이 있습니다.`
- `DI 프레임워크가 없으면 아무 것도 할 수 없습니다.`
- `필드 주입은 사용하지 말자!`
- `애플리케이션의 실제 코드와 관계 없는 테스트 코드 or 스프링 설정을 목적으로 하는 @Configuration 같은 곳에서만 특별한 용도로 사용합니다.`

<br>

음.. 지금 보면 `변경이 불가능해서 테스트 하기가 힘들다..` 라는 말이 있는데.. 이거는 생성자 주입에서도 똑같이 말했는데 뭐가 다른거지? 라고 할 수 있습니다. 그래서 다시 `Car`, `Tire` 코드를 보겠습니다.

```java
public class Car {
    private Tire tire;
    
    public Car(Tire tire) {
        this.tire = tire;
    }   
}
```

그리고 위와 같이 생성자 주입을 할 때 테스트 코드를 작성해보겠습니다.

<br>

```java
class CarTest {

    @Test
    void test() {
        Tire koreaTire = new KoreaTire();
        Car car = new Car(koreaTire);
        System.out.println(car);    
    }
}
```

위와 같이 순수 자바 코드로 테스트 할 수가 있습니다. (생성자 주입은 외부에서 주입을 할 수 있기에 우리가 원하는 의존관계를 주입할 수 있습니다.)

<br>

## `반면에 필드 주입이라면 어떻게 될까요?`

```java
public class Car {

    @Autowired
    private Tire tire;
}
```

그리고 똑같이 테스트 코드를 작성해보겠습니다.

```java
class CarTest {

    @Test
    void test() {
        Tire koreaTire = new KoreaTire();
        Car car = new Car();   // 음 ?? 내가 직접 의존성을 넣을 수 없으니 테스트를 할 수가 없네.. ?
        System.out.println(car);    
    }
}
```

위에서 보는 것처럼 `DI 프레임워크`의 도움을 받지 않으면 직접 의존성을 넣어줄 수 가 없습니다. 순수 자바 코드로만 테스트 작성을 할 수 가 없다는.. 치명적인 단점이 있는 것입니다. ㅠ,ㅠ
이러한 이유들로 인해서 `필드 인잭션`은 사용하지 말라고 합니다.
