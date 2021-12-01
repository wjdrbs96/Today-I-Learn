# `Spring`

## 1. 의존성 주입이란 무엇인가? 

의존 관계 주입(Dependency Injection)이라고도 하며, 어떤 객체가 사용하는 의존 객체를 직접 만들어 사용하는게 아니라, 외부에서 주입 받아 사용하는 방법이다. (new 연산자를 이용해서 객체를 생성하는 것이라고 생각하면 된다)   

<br>

## 2. Spring Bean 이란 무엇인가요?

Spring Bean 이란 IoC 컨테이너에서 관리하는 객체를 말한다. 

- 스프링 IoC 컨테이너에 등록된 Bean들은 `의존성 관리가 수월`해진다.
- 스프링 IoC 컨테이너에 등록된 Bean들은 `싱글톤`의 형태이다

Spring Bean 으로 등록되었을 때 위와 같은 장점이 있다. 

<br>

## 3. Spring 의존성 주입 방식에 대해서 장단점 설명해주세요.

- 생성자 주입
  - 제일 권장되는 방식이다. 필드에 final 을 추가하면 생성자에서 반드시 초기화 해주어야 하기 때문에 NPE를 방지하는데 도움을 줄 수 있다. 
- Setter 주입
  - Setter 를 통해서도 의존성 주입을 할 수 있다. 하지만 Setter를 통해서 의존 관계를 변경할 수 있어 여러 명에서 개발할 때 예상치 못한 에러를 발생할 수 있다는 단점이 있다.  
- 필드 주입
  - 필드 주입은 권장하지 않는 방식이다. 필드 주입을 통해서 주입을 해버리면 다른 의존 관계로 바꾸고 싶어도 바꿀 수가 없다. 그리고 DI 프레임 워크가 없다면 의존성 주입을 할 수 없다는 큰 단점이 있다. 
  
<br>

## @Autowired 에 대해서 설명해주세요.

해당 어노테이션이 존재하면 어노테이션이 붙어있는 클래스가 `IoC 컨테이너에 Bean`으로 등록되어 있는지 확인하고 등록되어 있다면 의존성 주입을 해주고, 등록되어 있지 않다면 런타임에 에러가 발생합니다.

<br>

## 4. Bean LifeCycle 에 대해서 설명해주세요.

- IoC 컨테이너 생성 -> Bean 등록 -> Bean 객체들의 의존 관계 주입 -> 초기화 콜백 -> 소멸전 콜백 -> IoC 컨테이너 소멸 

<br>

## 4. AOP란 무엇인가요? 

AOP는 `Aspect Oriented Programming` 으로 `관점 지향 프로그래밍` 이다. `흩어진 관심사`를 `Aspect` 라는 모듈화를 시켜 중복되는 코드를 재사용하겠다는 것이 취지이다. 그리고 `Proxy Pattern`을 사용한다. 

- Aspect: 공통 코드를 모아놓는 모듈
- Advice: 실질적으로 어떤 일을 해야 하는지를 담고 있음
- PointCut: 어디에 적용해야 하는지에 대한 정보를 담고 있음
- Target: Aspect에 적용이 되는 대상
- Join Point: Advice가 적용될 위치
- Weaving : Advice를 핵심 로직코드에 적용하는 것

<br>

Spring AOP에서는 `Dynamic Proxy`를 사용해서 기존의 코드를 건드리지 않고 코드를 추가하는 방식을 사용한다. 코드를 추가할 때는 3가지 방법이 있다. 

- 컴파일
- 로드 타임
- `런타임` : 스프링에서는 `런타임`에 동작합니다. (`Dynamic Proxy`는 런타임 시점에 인터페이스를 구현하는 클래스 또는 인스턴스를 만드는 기술을 말한다)

<br>

## 5. Proxy 패턴이란?

프록시 패턴이란 실제 클래스가 구현하고 있는 인터페이스를 똑같이 구현하고, 실제 클래스를 참조하면서 프록시 클래스를 만듭니다. 그리고 프록시 클래스에서 추가하고자 하는 코드를 추가하고 중간에 실제 코드를 호출하는 방식입니다. 

<br>

## 6. 스프링이 AOP를 내부적으로 처리하기 위해서 어떤 동작을 하고 있는지 아시면 설명해주세요. 

Spring AOP는 런타임에 A 라는 클래스가 Bean 으로 등록될 때 A 라는 클래스를 참조하는 `Proxy` 객체를 만든 후에 흩어진 관심사의 코드를 `Weaving` 시킨 후에 작동하게 만듭니다. (Dynamic Proxy) 

<br>

## @Transactional AOP는 어떻게 동작하나요?

해당 어노테이션이 붙어있는 메소드를 호출하면 프록시 객체의 메소드가 먼저 호출됩니다. 그리고 트랜잭션이 시작되고 실제 클래스의 메소드가 호출되고 커밋되는 방식으로 진행됩니다. (`Proxy` 객체를 생성해서 내부에 클래스 객체를 넣어두고 앞뒤로 commit과 rollback처리를 해준다)

<br>

## Dynamic Proxy vs CGLIB 차이점이 무엇인가요?

<br>

## 7. SpringBootApplication 동작 방식을 설명해주세요. 

SpringBootApplication 내부를 보면 `ComponentSacn`, `@SpringBootConfiguration`, `@EnableAutoConfiguration` 대표적으로 3가지가 존재한다. 

- `ComponentScan`: 현재 어노테이션이 존재하는 같은 곳에 위치한 Bean 으로 등록할 수 있는 어노테이션을 찾아서 자동으로 Bean 으로 등록해주는 역할을 합니다.
- `EnableAutoConfigure`: 스프링 의존성에 autoconfigure 에 보면 자동으로 값들을 설정해준다. 대표적으로 Spring Boot 에서는 MVC 설정을 따로 하지 않아도 편리하게 사용할 수 있는데 이 어노테이션이 자동으로 번거로운 설정들을 대신 해줍니다.
- `SpringBootConfiguration` 은 Configration 과 거의 같은 어노테이션이지만, Spring Boot 에서 `@SpringBootTest` 어노테이션을 사용해서 테스트 코드에서 작동할 때 관련 설정들을 자동으로 읽어오도록 해주는 역할을 합니다. 

<br>

## 8. Spring MVC 동작 방식 설명해주세요. 

클라이언트에서 특정 Controller URI 로 요청이 오면 Filter 를 거친 후에 DispatcherServlet 이 응답을 받습니다. DispatcherServlet 은 요청 온 것을 `HadlerMapping` 에게 넘겨 어떤 Controller 의 URI 인지 찾도록 요청합니다. 그리고 `HandlerAdapter` 를 통해서 해당 컨트롤러 메소드를 호출하여 실제 오는 응답 값을 얻습니다. 만약 Controller 어노테이션과 model.addAttribute 같은 것들을 사용했다면 여기에 값을 담아서 옵니다.

1. DispatcherServlet 이 HandlerMapping 에게 해당 요청이 어떤 Controller 가 담당하는지 찾도록 요청합니다.
2. DispatcherServlet 이 어떤 Controller 인지 알았다면 HandlerAdapter 를 통해서 해당 클래스의 반환 값을 얻어오고 model.addAttribute 가 있다면 거기에 값을 넣어서 가져옵니다.
3. 리턴 값을 가지고 ViewResolver 를 통해서 View 를 찾아서 DispatcherServlet 에게 반환합니다. 
4. DispatcherServlet 은 View 응답 생성 요청을 합니다.

그리고 Controller 라면 View 를 찾아야 하니 `ViewResolver`가 수행되어 어떤 뷰를 return 해주어야 하는지 찾게 됩니다. 

<br>

## 9. Filter vs Interceptor 차이 설명해주세요.

![spring-aop](https://user-images.githubusercontent.com/45676906/122227178-a1af6a00-cef1-11eb-8c22-23cbcb43bc03.png)

전체 구성는 위와 같습니다. `Filter -> DispatcherServelt -> Interceptor -> AOP -> Controller` 순으로 실행됩니다. 

- `Filter`: 
  - Filter 는 Spring 영역 밖에 위치합니다. 즉 ExceptionHandler 같은 것을 통해서 에러 처리를 할 수 없습니다. 즉, 앞단에서 XSS 방어 어떤 처리를 해야 할 때 사용하면 좋습니다.
  - 또한, Controller 이후 자원 처리가 끝난 후 응답 처리에 대해서도 변경, 조작을 수행할 수 있다. 즉, Servlet 영역이다 보니 Request, Resposne 조작할 수 있음
  - `Filter`는 Web Application(Tomcat을 사용할 경우 web.xml)에 등록한다.
- `Interceptor`
  - Spring 영역 안에 있어 Spring 모든 Bean에 접근 가능합니다. 그리고 Controller 앞 뒤 로 끼어들 수 있는 메소드를 제공합니다. 
  - `Interceptor`는 Spring의 Application Context에 등록합니다.

<img width="765" alt="스크린샷 2021-11-30 오전 1 26 25" src="https://user-images.githubusercontent.com/45676906/143905208-b0137a50-15b3-4358-a07c-f3c321cd83b8.png">

<br>

## Spring Transactional Propagation 에 대해서 설명해주세요. 

- REQUIRED: 아무 설정도 하지 않는 DEFAULT 설정입니다. `REQUIRED`는 부모 트랜잭션이 존재하는 상황에서 자식 트랜잭션을 호출하면 자식 트랜잭션도 부모 트랜잭션에 합류합니다. 그리고 자식에서 에러가 발생해도 부모 까지 모두 Rollback 되고, 부모에서 에러가 발생해도 자식도 모두 Rollback 된다는 특징이 있습니다.
- REQUIRED_NEW: 무조건 새로운 트랜잭션을 만듭니다. 부모에서 에러가 발생하더라도 자식에서 문제가 없다면 문제 없이 트랜잭션 Commit 됩니다.
- NESTED : 부모 트랜잭션이 존재하면 부모 트랜잭션에 중첩시키고, 부모 트랜잭션이 존재하지 않는다면 새로운 트랜잭션을 생성한다. 부모 트랜잭션에 예외가 발생하면 자식 트랜잭션도 rollback한다. 자식 트랜잭션에 예외가 발생하더라도 부모 트랜잭션은 rollback하지 않는다. 

<br>

## Spring Transactional Isolation 에 대해서 설명해주세요.

특별히 설정하지 않으면 `Isolation.Default`가 설정됩니다. `Isolation.Default`는 현재 사용하고 있는 `RDBS` 기본 격리 레벨을 따라 갑니다. 저는 `MySQL InnoDB`를 사용하고 있기 때문에 `REPEATABLE READ`가 사용됩니다. 

<br>

## REPEATABLE READ 를 사용하게 되면 방어되는 db의 오류 동작이 어떤 게 방어가 될까요?

`READ COMMITTED`에서 `UNREPETABLE READ`가 발생한다는 문제가 있습니다. `UNREPEATABLE READ`는 A 트랜잭션에 C 라는 쿼리로 테이블을 조회했을 때 결과가 1건이 나왔습니다. 그런데 이 때 B 트랜잭션에서 제가 조회했던 곳의 필드 값을 Update 했습니다. 그리고 제가 한번 더 C 라는 쿼리로 조회했는데 이번에는 결과가 2건이 나오게 되는 이런 상황, 같은 트랜잭션에서 같은 쿼리를 날렸는데 결과가 다른 문제를 `UNREPEATABLE READ` 라고 하고 `REPEATABLE READ`는 이러한 문제를 해결하였습니다. 

<br>

## @Transactional 어노테이션을 아무런 속성도 주지 않고 사용을 하며 어떻게 처리가 되나요?

- Propagtaion은 REQUIRED 가 적용되고, Isolation Level 은 사용하는 RDBMS 의 고립 레벨을 따라 갑니다. 그리고 RuntimeException, Error 일 때 Rollback 되도록 설정 되어 있습니다. 

<br>

## Spring Bean Scope 에 대해서 설명해주세요. 

- `싱글톤은 기본 스코프로 스프링 컨테이너의 시작과 종료까지 유지되는 가장 넓은 범위의 스코프`입니다.
- `프로토타입은 빈의 생성과 의존관계 주입까지만 관여`하고 더는 관리하지 않는 매우 짧은 범위의 스코프입니다.
- `request`는 웹 요청이 들어오고 나갈때까지 유지하는 스코프, `session은` 웹 세션이 생성, 종료할때까지, `application`은 웹 서블릿 컨텍스트와 같은 범위로 유지하는 스코프입니다.
 
<br>

## JPA 영속성 컨텍스트 장점 아는대로 설명해주세요.

- 1차 캐시 : 영속성 컨텍스트는 내부에 캐시를 가지고 있는데 이것을 `1차 캐시`라고 합니다. 1차 캐시에 없으면 DB 에서 조회해옵니다. 1차 캐시에 있다면 DB 쿼리를 날리지 않습니다.
- 변경 감지(Dirty Checking) : 스냅샷을 1차 캐시에 들어온 데이터를 스냅샷을 찍어놓습니다. commit 되는 시점에 Entity와 스냅샷과 비교하여 다르다면 update SQL을 생성합니다.  
- 지연 로딩(Lazy Loading) : FetchType을 LAZY로 설정해놓으면 연관관계의 객체를 프록시로 가져온 후에 실제 그 객체가 사용될 때 DB에 쿼리를 날려서 가져옵니다.
- 동일성 보장: 동일성 비교가 가능합니다.(== 사용 가능)
- 쓰기 지연 : 엔티티 매니저는 트랜잭션을 커밋하기 직전까지 데이터베이스에 엔티티를 저장하지 않고 내부 쿼리 저장소에 INSERT SQL을 차곡차곡 모아둡니다. 그리고 트랜잭션을 커밋할 때 모아둔 쿼리를 데이터베이스에 보내는데 이것을 `트랜잭션을 지원하는 쓰기 지연` 이라 합니다.

<img width="987" alt="스크린샷 2021-08-25 오전 7 45 21" src="https://user-images.githubusercontent.com/45676906/130699692-8ded3c99-0b25-415f-abe4-14d8aa6d9799.png">

<br>

## N + 1 문제는 어떻게 해결하시나요?

N + 1 쿼리는 `@OneToMany` 관계에서 즉시로딩을 사용할 때 혹은 지연 로딩시에는 반복문을 돌면서 하위 객체를 조회할때  발생합니다. 정확한 의미는 1개의 쿼리를 실행했을 때, 내부에 존재하는 컬렉션들을 조회해오면서 생기는 문제입니다. 기본적으로 되도록이면 @OneToMany의 매핑을 하지 않을 수 있다면 하지 않는 것이 최고의 예방책입니다.

만약 그런 객체를 가져와야 하는 경우 `Fetch Join`이라고 하는 JPQL의 join fetch를 사용합니다. 쿼리 한 번으로 해결할 수 있고, 또 다른 방법으로는 `EntityGraph`를 사용하는 방법이 있습니다.

<br>

## JPA 써보셨으면 JPA 영속성 컨텍스트는 언제 열리나요?

트랜잭션이 시작될 때 영속성 컨텍스트가 열립니다.

<br>

## Spring 요청이 올 때 임베디드 톰캣이 있는데 요청마다 쓰레드가 생성될까여? 프로세스가 생성될까요?

쓰레드가 생성됩니다.

<br>

## 쓰레드가 생성된다면 10개 요청이 오면 10개 쓰레드가 생성될까요?

10개의 쓰레드가 생성됩니다. 톰캣 기본 설정으로 내부에 200개의 쓰레드가 존재합니다. 

<br>

## Connection Pool 관련 설정을 해보신 적이 있나요? 
