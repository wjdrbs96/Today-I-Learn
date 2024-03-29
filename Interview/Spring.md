## `Spring`

<details>
  <summary>의존성 주입이란 무엇인가?</summary> 
  <br>

의존 관계 주입(Dependency Injection)이라고도 하며, 어떤 객체가 사용하는 의존 객체를 직접 만들어 사용하는게 아니라, `외부에서 주입 받아 사용`하는 방법이다. (new 연산자를 이용해서 객체를 생성하는 것이라고 생각하면 된다)

</details>

<details>
  <summary>Spring Bean 이란 무엇인가요?</summary>  
  <br>

Spring Bean 이란 IoC 컨테이너에서 관리하는 객체를 말한다.(스프링이 제어권을 가지고 직접 만들고 관계를 부여하는 오브젝트를 Bean 이라고 부른다.)

- 스프링 IoC 컨테이너에 등록된 Bean들은 `의존성 관리가 수월`해진다.
- 스프링 IoC 컨테이너에 등록된 Bean들은 `싱글톤`의 형태이다

Spring Bean 으로 등록되었을 때 위와 같은 장점이 있다.

</details>

<details>
  <summary>IoC 컨테이너란 무엇인가요?</summary>
  <br>

스프링에서는 빈의 생성과 관계설정 같은 제어를 담당하는 IoC 오브젝트를 `빈 팩토리(Bean Factory)` 라고 부릅니다. 보통은 빈 팩토리보다는 이를 좀 더 확장한 `애플리케이션 컨텍스트`를 주로 사용합니다.

</details>  

<details>
  <summary>Spring 의존성 주입 방식 3가지에 대해서 설명해주세요.</summary>
  <br>

- 생성자 주입
  - 제일 권장되는 방식이다. 필드에 final 을 추가하면 생성자에서 반드시 초기화 해주어야 하기 때문에 NPE를 방지하는데 도움을 줄 수 있다.
- Setter 주입
  - Setter 를 통해서도 의존성 주입을 할 수 있다. 하지만 Setter를 통해서 의존 관계를 변경할 수 있어 여러 명에서 개발할 때 예상치 못한 에러를 발생할 수 있다는 단점이 있다.
- 필드 주입
  - 필드 주입은 권장하지 않는 방식이다. 필드 주입을 통해서 주입을 해버리면 다른 의존 관계로 바꾸고 싶어도 바꿀 수가 없다. 그리고 DI 프레임 워크가 없다면 의존성 주입을 할 수 없다는 큰 단점이 있다.
  
</details>

<details>
  <summary>@Autowired 에 대해서 설명해주세요.</summary>
  <br>

해당 어노테이션이 존재하면 어노테이션이 붙어있는 클래스가 `IoC 컨테이너에 Bean`으로 등록되어 있는지 확인하고 등록되어 있다면 의존성 주입을 해주고, 등록되어 있지 않다면 런타임에 에러가 발생합니다.
</details>

<details>
  <summary>Bean LifeCycle 에 대해서 설명해주세요.</summary>
  <br>

IoC 컨테이너 생성 -> Bean 등록 -> Bean 객체들의 의존 관계 주입 -> 초기화 콜백 -> 소멸전 콜백 -> IoC 컨테이너 소멸

- 인터페이스(InitializingBean, DisposableBean)
- 설정 정보에 초기화 메소드, 종료 메소드 지정 
- @PostConstruct, @PreDestroy 애노테이션 지원

스프링은 총 3가지의 빈 생명주기 콜백을 제공합니다.

</details>

<details>
  <summary>@SpringBootApplication 동작 방식을 설명해주세요.</summary>
  <br>

SpringBootApplication 내부를 보면 `ComponentSacn`, `@SpringBootConfiguration`, `@EnableAutoConfiguration` 대표적으로 3가지가 존재한다.

- `ComponentScan`: 현재 어노테이션이 존재하는 같은 곳에 위치한 Bean 으로 등록할 수 있는 어노테이션을 찾아서 자동으로 Bean 으로 등록해주는 역할을 합니다.
- `EnableAutoConfigure`: 
  - @ComponentScan에서 먼저 스캔해서 Bean으로 등록하고 tomcat등 스프링이 정의한 외부 의존성을 갖는 class들을 스캔해서 Bean으로 등록합니다. 스프링 의존성에 autoconfigure 에 보면 자동으로 값들을 설정해준다. 대표적으로 Spring Boot 에서는 MVC 설정을 따로 하지 않아도 편리하게 사용할 수 있는데 이 어노테이션이 자동으로 번거로운 설정들을 대신 해줍니다.
  - 사전에 정의한 라이브러리들을 Bean 으로 등록해주는 어노테이션입니다. (ex: Spring MVC, Spring Batch 등등)
- `SpringBootConfiguration` 은 Configration 과 거의 같은 어노테이션이지만, Spring Boot 에서 `@SpringBootTest` 어노테이션을 사용해서 테스트 코드에서 작동할 때 관련 설정들을 자동으로 읽어오도록 해주는 역할을 합니다.(`SpringBootTestContextBootstrapper` 기억하기)

</details>

<details>
  <summary>Spring Bean Scope 에 대해서 설명해주세요.</summary>
  <br>

- 스프링이 관리하는 오브젝트, 즉 빈이 생성되고, 존재하고, 적용되는 범위를 `스코프(Scope)`라고 합니다.
- `싱글톤은 기본 스코프로 스프링 컨테이너의 시작과 종료까지 유지되는 가장 넓은 범위의 스코프`입니다.
- `프로토타입은 빈의 생성과 의존관계 주입까지만 관여하고 그 이후에는 더는 관리하지 않는 매우 짧은 범위의 스코프`입니다.
- `request`는 웹 요청이 들어오고 나갈때까지 유지하는 스코프, `session`은 웹 세션이 생성, 종료할때까지, `application`은 웹 서블릿 컨텍스트와 같은 범위로 유지하는 스코프입니다.

</details>

<details>
  <summary>Bean scope 별 Bean의 생성과 파괴 시점 설명해주세요.</summary>
  <br>

Bean 생명주기는 `IoC 컨테이너 생성 -> Bean 등록 -> 의존관계 주입 -> 초기화 콜백 -> 사용 -> 소멸 전 콜백 -> IoC 컨테이너 종료` 순으로 진행된다.

- 싱글톤: 객체의 생명 주기를 IoC 컨테이너가 생성되고 종료될 때까지 싱글톤으로 관리하는 것이기 때문에 위의 과정에서 처럼 Bean 등록 시점에 생성되고 IoC 컨테이너 종료와 소멸전 콜백 사이에 파괴되지 않나 싶다.
- 프로토타입: 빈의 생성과 의존관계 주입까지만 관여하고 그 이후에는 관여하지 않기 때문에 파괴는 수동으로 해야 하지 않을까 싶다.
- Request: 요청당 Bean이 생명주기를 탈 거 같다.
- Session: 세션의 범위에 따라 Bean 생명주기를 타지 않을까 싶다.

</details>

<details>
  <summary>스프링은 왜 Bean을 싱글톤으로 관리할까요?</summary>
  <br>

스프링은 주로 적용되는 대상이 자바 엔터프라이즈 기술을 사용하는 서버환경이기 때문이다. 스프링이 처음 설계됐던 대규모의 엔터프라이즈 서버환경은 서버 하나당 최대로 초당 수십에서 수백 번씩 브라우저나 여타 시스템으로부터의 요청을 받아 처리할 수 있는 높은 성능이 요구되는 환경이었다.

그런데 매번 클라이언트에서 요청이 올때마다 각 로직을 담당하는 오브젝트를 새로 만들어서 사용한다고 생각해보자. 아무리 GC 성능이 좋아도 부하가 있을 수가 있다. 그렇기 때문에 IoC 컨테이너에서 객체를 싱글톤으로 관리해서 계속 재사용하겠다는 취지이다.

</details>

<br>

## `Spring MVC`

<details>
  <summary>Spring MVC 동작 방식 설명해주세요.</summary>
  <br>

클라이언트에서 특정 Controller URI 로 요청이 오면 Filter 를 거친 후에 DispatcherServlet 이 응답을 받습니다. DispatcherServlet 은 요청 온 것을 `HadlerMapping` 에게 넘겨 어떤 Controller 의 URI 인지 찾도록 요청합니다. 그리고 `HandlerAdapter` 를 통해서 해당 컨트롤러 메소드를 호출하여 실제 오는 응답 값을 얻습니다. 만약 Controller 어노테이션과 model.addAttribute 같은 것들을 사용했다면 여기에 값을 담아서 옵니다.

1. DispatcherServlet 이 HandlerMapping 에게 해당 요청이 어떤 Controller 가 담당하는지 찾도록 요청합니다.
2. DispatcherServlet 이 어떤 Controller 인지 알았다면 HandlerAdapter 를 통해서 해당 클래스의 반환 값을 얻어오고 model.addAttribute 가 있다면 거기에 값을 넣어서 가져옵니다.
3. 리턴 값을 가지고 ViewResolver 를 통해서 View 를 찾아서 DispatcherServlet 에게 반환합니다.
4. DispatcherServlet 은 View 응답 생성 요청을 합니다.

그리고 Controller 라면 View 를 찾아야 하니 `ViewResolver`가 수행되어 어떤 뷰를 return 해주어야 하는지 찾게 됩니다.(`ViewResolver 인터페이스 구현체인 ContentNegotiatingViewResolver`가 사용되면서 View를 찾는 과정을 볼 수 있습니다.)

</details>

<details>
  <summary>Filter vs Interceptor 차이 설명해주세요.</summary>
  <br>

![spring](https://user-images.githubusercontent.com/45676906/122227178-a1af6a00-cef1-11eb-8c22-23cbcb43bc03.png)

전체 구성는 위와 같습니다. `Filter -> DispatcherServelt -> Interceptor -> AOP -> Controller` 순으로 실행됩니다.

- `Filter`:
  - Filter 는 Spring 영역 밖에 위치합니다. 즉 ExceptionHandler 같은 것을 통해서 에러 처리를 할 수 없습니다. 즉, 앞단에서 XSS 방어 어떤 처리를 해야 할 때 사용하면 좋습니다.
  - 또한, Controller 이후 자원 처리가 끝난 후 응답 처리에 대해서도 변경, 조작을 수행할 수 있다. 즉, Servlet 영역이다 보니 Request, Resposne 조작할 수 있음
  - `Filter`는 Web Application(Tomcat을 사용할 경우 web.xml)에 등록한다.
- `Interceptor`
  - Spring 영역 안에 있어 Spring 모든 Bean에 접근 가능합니다. 그리고 Controller 앞 뒤 로 끼어들 수 있는 메소드를 제공합니다.
  - `Interceptor`는 Spring의 Application Context에 등록합니다.

<img width="765" alt="스크린샷 2021-11-30 오전 1 26 25" src="https://user-images.githubusercontent.com/45676906/143905208-b0137a50-15b3-4358-a07c-f3c321cd83b8.png">

</details>


<details>
  <summary>HttpSession에서 전부 같은 키로 저장하고 있는데 getAttribute 했을 때 구분할 수 있는 이유를 알고 계신가요?</summary>
  <br>

1. 클라이언트(웹 브라우저의 사용자)가 처음으로 웹 어플리케이션을 방문하거나 request.getSession()을 통해 HttpSession을 처음으로 가져 오면 `서블릿 컨테이너`는 새로운 HttpSession 객체를 생성하고 길고 unique한 ID를 생성 후, 서버의 메모리에 저장합니다.
2. 서블릿 컨테이너는 `JSESSIONID`란 이름을 key로, 생성한 session ID를 value로 하여 HTTP 응답의 Set-Cookie header에 cookie로 설정합니다
3. 브라우저는 다음 요청부터 `Request Headers`에 `JSESSIONID`를 담아서 서버로 요청을 보냅니다.
4. `서블릿 컨테이너`는 들어오는 모든 HTTP request의 cookie header에서 `JSESSIONID`라는 이름의 cookie가 있는지 확인하고 해당 값 (session ID)을 사용하여 서버의 메모리에 저장된 HttpSession을 가져옵니다.

</details>

<details>
  <summary>Spring 요청이 올 때 임베디드 톰캣이 있는데 요청마다 쓰레드가 생성될까여? 프로세스가 생성될까요?</summary>
  <br>

쓰레드가 생성됩니다.

</details>

<details>
  <summary>쓰레드가 생성된다면 10개 요청이 오면 10개 쓰레드가 생성될까요?</summary>
  <br>

10개의 쓰레드가 생성됩니다. 톰캣 기본 설정으로 내부에 200개의 쓰레드가 존재합니다.

</details>

<details>
  <summary>요청이 올 때마다 쓰레드가 생성된다고 하셨는데요. 이것을 Connection Pool 과 연관지어 설명해주실 수 있을까요?</summary>
  <br>
</details>

<br>

## `Spring AOP`

<details>
  <summary>AOP란 무엇인가요?</summary>
  <br>

AOP는 `Aspect Oriented Programming` 으로 `관점 지향 프로그래밍` 이다. `흩어진 관심사`를 `Aspect` 라는 모듈화를 시켜 중복되는 코드를 재사용하겠다는 것이 취지이다. 그리고 `Proxy Pattern`을 사용한다.

- Aspect: 공통 코드를 모아놓는 모듈
- Advice: 실질적으로 어떤 일을 해야 하는지를 담고 있음
- PointCut: 어디에 적용해야 하는지에 대한 정보를 담고 있음
- Target: Aspect 에 적용이 되는 대상
- Join Point: Advice 가 적용될 위치
- Weaving : Advice 를 핵심 로직코드에 적용하는 것

<br>

Spring AOP 에서는 `Dynamic Proxy` or `CGLib`을 사용해서 기존의 코드를 건드리지 않고 코드를 추가하는 방식을 사용한다. 코드를 추가할 때는 3가지 방법이 있다.

- 컴파일
- 로드 타임
- `런타임` : 스프링에서는 `런타임`에 동작합니다. (`Dynamic Proxy`는 런타임 시점에 인터페이스를 구현하는 클래스 또는 인스턴스를 만드는 기술을 말한다)
  - `Dynamic Proxy`는 인터페이스가 있어야 사용할 수 있다는 특징이 있습니다.
  - 인터페이스가 없다면 `CGLib`을 통해서 바이트 코드를 조작하여 동적으로 프록시 객체를 만들게 됩니다.
  
</details>

<details>
  <summary>AOP의 PointCut이 적용될 수 있는 대상에는 무엇이 있나요?</summary>
  <br>

Parameter, Method, Class, Package, Annotation 등등 인거 같은?!
</details>

<details>
  <summary>Proxy 패턴이란 무엇인가요?</summary>
  <br>

`프록시 패턴`이란 실제 클래스가 구현하고 있는 인터페이스를 똑같이 구현하고, 실제 클래스를 참조하면서 프록시 클래스를 만듭니다. 그리고 `프록시 클래스에서 추가하고자 하는 코드를 추가하고 중간에 실제 코드를 호출하는 방식입니다.`

</details>

<details>
  <summary>스프링이 AOP를 내부적으로 처리하기 위해서 어떤 동작을 하고 있는지 아시면 설명해주세요.</summary>
  <br>
  
- Spring AOP는 런타임에 A 라는 클래스가 Bean 으로 등록될 때 A 라는 클래스를 참조하는 `Proxy` 객체를 만든 후에 흩어진 관심사의 코드를 `Weaving` 시킨 후에 작동하게 만듭니다. 
- 타겟 클래스가 인터페이스를 구현하고 있다면 `Dynamic Proxy`를 사용하여 프록스 객체를 만들게 됩니다.
- 타켓 클래스가 인터페이스를 구현하고 있지 않다면 `CGLib`을 사용하여 프록시 객체를 만들게 됩니다.

`Reference`: [https://docs.spring.io/spring-framework/docs/current/reference/html/core.html#aop-introduction-proxies](https://docs.spring.io/spring-framework/docs/current/reference/html/core.html#aop-introduction-proxies) 

</details>

<details>
  <summary>Dynamic Proxy vs CGLIB 차이점이 무엇인가요?</summary>
  <br>

1. 스프링에서는 `CGLib`, `Dynamic Proxy` 를 사용해서 프록시 패턴을 구현하고 있습니다.
2. `Dynamic Proxy`는 인터페이스 타입만 생성할 수 있는 반면에 `CGLib`은 실제 구현하고자 하는 타입을 생성할 수 있어서 인터페이스를 구현하지 않는데 프록시 객체를 만들어야 하는 경우에는 `CGLib`을 사용합니다.
3. 타켓 클래스가 인터페이스를 구현하고 있지 않다면 `프록시 객체를 생성할 때 CGLib`를 사용한다.
4. `CGLib`는 `JDK Proxy`와는 달리 리플렉션을 사용하지 않고 `바이트코드를 조작하여 프록시 객체를 생성하고 있다.`
5. `Dynamic Proxy`는 `리플랙션의 Proxy 클래스가 동적으로 Proxy를 생성해줍니다.`

</details>

<details>
  <summary>@Cacheable 어노테이션이 어떻게 동작하는지 알고 계신가요?</summary> 
  <br>
</details>

<details>
  <summary>리플랙션이 무엇인지 알고 계신가요?</summary>
  <br>

리플렉션은 자바 코드 자체를 추상화하여 구체적인 객체정보를 알지 못하더라도 클래스정보들을 접근 할 수 있도록 하는 자바 API 입니다. 이를 통해 동적 객체 선언, 동적 메서드 호출 기능을 사용 할 수 있는데, Spring에서는 DI, Proxy등에서 리플렉션이 사용됩니다.

리플렉션을 사용하면 코드가 지져분해지고, 성능이 떨어진다는 단점이 존재하기 때문에 꼭 필요할 때 사용하는 것이 좋습니다.

</details>

<details>
  <summary>DynamicProxy의 한계가 무엇일까요? </summary>  
  <br>

- DynamicProxy는 원본 클래스가 인터페이스를 구현하고 있어야 사용할 수 있다는 단점이 있습니다.
- 리플렉션을 사용하기 때문에 성능이 떨어지고, 코드가 지져분해진다는 단점이 있습니다.

</details>

<br>

## `Spring Security`

<details>
  <summary>Spring Security 인증 과정에 대해 설명해주세요.</summary>
  <br>
</details>

<br>

## `JPA`

<details>
  <summary>JPA의 EntityManager는 언제 생성되나요?</summary>
  <br>

`EntityManagerFactory`를 통해서 요청이 올 때마다 `EntityManager`를 생성합니다. `EntityManager는 내부적으로 데이터베이스 커넥션 풀을 사용합니다.`

![image](https://user-images.githubusercontent.com/45676906/130561404-a884dac8-c921-4b6d-959e-7d4fb1813982.png)

<br>

![image](https://user-images.githubusercontent.com/45676906/130563981-94e22733-8c94-413c-9d5c-18fa93ed0c49.png)

- `EntityManagerFactory`는 하나만 생성해서 애플리케이션 전체에서 공유해서 사용합니다.
  - `JPA를 통작시키기 위해서는 기반 객체를 만들고 JPA 구현체에 따라서는 데이터베이스 커넥션 풀도 생성하므로 엔티티 매니저 팩토리를 생성하는 비용은 아주 큽니다.`
- `EntityManager`는 쓰레드간에 공유해서 사용하면 안됩니다.

</details>

<details>
  <summary>JPA 영속성 컨텍스트 장점 아는대로 설명해주세요.</summary>
  <br>

- `1차 캐시` : 영속성 컨텍스트는 내부에 캐시를 가지고 있는데 이것을 `1차 캐시`라고 합니다. 1차 캐시에 없으면 DB 에서 조회해옵니다. 1차 캐시에 있다면 DB 쿼리를 날리지 않습니다.(JPA는 1차 캐시를 통해서 반복 가능한 읽기(REPEATABLE RAD) 등급의 트랜잭션 격리 수준을 데이터베이스가 아닌 애플리케이션 차원에서 제공한다는 장점이 있습니다.)
- `변경 감지(Dirty Checking)` : 스냅샷을 1차 캐시에 들어온 데이터를 스냅샷을 찍어놓습니다. commit 되는 시점에 Entity와 스냅샷과 비교하여 다르다면 update SQL을 생성합니다.
- `지연 로딩(Lazy Loading)` : FetchType을 LAZY로 설정해놓으면 연관관계의 객체를 프록시로 가져온 후에 실제 그 객체가 사용될 때 DB에 쿼리를 날려서 가져옵니다.
- `동일성 보장`: 동일성 비교가 가능합니다.(== 사용 가능)
- `쓰기 지연` : `엔티티 매니저는 트랜잭션을 커밋하기 직전까지 데이터베이스에 엔티티를 저장하지 않고 내부 쿼리 저장소에 INSERT SQL을 차곡차곡 모아둡니다.` 그리고 트랜잭션을 커밋할 때 모아둔 쿼리를 데이터베이스에 보내는데 이것을 `트랜잭션을 지원하는 쓰기 지연` 이라 합니다.

<img width="987" alt="스크린샷 2021-08-25 오전 7 45 21" src="https://user-images.githubusercontent.com/45676906/130699692-8ded3c99-0b25-415f-abe4-14d8aa6d9799.png">

</details>

<details>
  <summary>JPA에서 쿼리가 DB에 반영되는 시점은 언제인가요?</summary>
  <br>

커밋하는 시점에 쓰기 지연 SQL 저장소에 있던 쿼리들이 flush 되면서 데이터베이스에 반영이 됩니다. (트랜잭션 `commit()`이 호출되거나, 강제로 `flush()` 메소드가 호출될 때 쿼리가 실행되어 DB에 반영됩니다.)

</details>

<details>
  <summary>flush() 가 호출되면 내부적으로 어떤 동작을 하고 있나요?</summary>
  <br>

1. `em.flush()` 직접 호출
2. 트랜잭션 커밋 시 플러시가 자동 호출됩니다.
3. JPQL 쿼리 실행 시 플러시가 자동 호출됩니다.

플러시를 호출하는 방법은 위와 같이 3가지 입니다. `플러시(flush())`는 영속성 컨텍스트의 변경 내용을 데이터베이스에 반영합니다. 플러시를 실행하면 구체적으로 다음과 같은 일이 일어납니다.

1. 변경 감지가 동작해서 영속성 컨텍스트에 있는 모든 엔티티를 스냅샷과 비교해서 수정된 엔티티를 찾는다. 수정된 엔티티는 수정 쿼리를 만들어 쓰기 지연 SQL 저장소에 등록한다.
2. 쓰기 지연 SQL 저장소의 쿼리를 데이터베이스에 전송합니다.(등록, 수정, 삭제 쿼리)

</details>

<details>
  <summary>N + 1 문제는 어떻게 해결하시나요?</summary>
  <br>

N + 1 쿼리는 대표적으로 `@OneToMany` 관계에서 즉시로딩을 사용할 때 혹은 지연 로딩시에는 반복문을 돌면서 하위 객체를 조회할때  발생합니다. 정확한 의미는 1개의 쿼리를 실행했을 때, 내부에 존재하는 컬렉션들을 조회해오면서 생기는 문제입니다. 기본적으로 되도록이면 @OneToMany의 매핑을 하지 않을 수 있다면 하지 않는 것이 최고의 예방책입니다.

만약 그런 객체를 가져와야 하는 경우 `Fetch Join`이라고 하는 JPQL의 join fetch를 사용합니다. 쿼리 한 번으로 해결할 수 있고, 또 다른 방법으로는 `EntityGraph`를 사용하는 방법이 있습니다. 또한 `Batch Size` 옵션을 사용하여 N + 1 문제를 해결할수도 있습니다.

`@OneToOne` 관계에서 연관관계 주인이 아닌 쪽에서 조회하면 `Proxy`의 한계로 `null`인지 아닌지 직접 조회해보아야 한다는 특징 때문에 `N + 1` 문제가 발생한다는 특징이 있습니다. 이 때도 `fetct join`을 활용해서 `N + 1` 문제를 해결할 수 있습니다.

</details>

<details>
  <summary>N + 1 문제를 해결하는 방법들에 대해서 아는대로 말해주세요.</summary>
  <br>

- `fetch join`을 사용해서 객체를 한번에 가져오는 방법이 있습니다.
- `entity graph`를 사용하여 N + 1 문제를 해결할 수 있습니다.
- `Batch Size` 옵션을 사용하여 `Size` 만큼 `IN` 쿼리로 가져와서 N + 1 문제를 해결할 수 있습니다.

</details>

<details>
  <summary>fetch join 으로 N + 1 문제를 해결하였으면 이 때 페이징은 어떻게 처리하셨나요?</summary>
  <br>
</details>

<details>
  <summary>JPA 영속성 컨텍스트(PersistenceContext)는 언제 열리나요?</summary>
  <br>

`트랜잭션이 시작될 때 영속성 컨텍스트(PersistenceContext)가 열립니다.`(엔티티 매니저를 만들면 그 내부에 영속성 컨텍스트가 생성됩니다.)
</details>

<details>
  <summary>엔티티의 생명주기가 어떻게 되시는지 아시나요?</summary>
  <br>

![image](https://user-images.githubusercontent.com/45676906/130695392-5133df99-f7b6-4812-be15-365147005b91.png)

- 비영속(new): 영속성 컨텍스트와 전혀 관계가 없는 새로운 상태
- 영속(managed): 영속성 컨텍스트에 관리되는 상태
- 준영속(detached): 영속성 컨텍스트에 저장되었다가 분리된 상태
- 삭제 (removed): 삭제된 상태

</details>

<details>
  <summary>영속성 전이에 대해서 아는대로 설명해주세요.</summary>
  <br>

## `영속성 전이: CASCADE 주의`

- 영속성 전이는 연관관계를 매핑하는 것과 아무 관련이 없습니다.
- 엔티티를 영속화할 뿐 연관된 엔티티도 함께 영속화하는 편리함을 제공할 뿐입니다.

<br> <br>

## `CASCADE의 종류`

- `ALL: 모두 적용`
- `PERSIST: 영속`
- `REMOVE: 삭제`
- `MERGE: 병합`
- `REFRESH: 갱신`
- `DETACH: 분리`

예를들어 데이터베이스에서 유저가 쓴 게시글처럼 연관되어 있을 때 유저를 삭제할 때 CASCADE 속성을 지정해서 유저가 작성한 글 까지 한번에 다 삭제하려고 할 때 사용할 수 있습니다.

<br>

## `고아 객체`

JPA는 부모 엔티티와 연관관계가 끊어진 자식 엔티티를 자동으로 삭제하는 기능을 제공하는데 이것을 `고아 객체` 제거 라고 합니다.

![스크린샷 2021-10-04 오후 2 51 03](https://user-images.githubusercontent.com/45676906/135800547-af26ad0a-0a2e-4e2f-8ec3-6132c1009f32.png)

위와 같이 `orphanRemoval = true` 옵션을 준 후에 Parent 중에 Child 하나의 연관관계를 끊어보겠습니다.

</details>

<details>
  <summary>AutoIncrement PK 지닌 테이블에 한 트랜잭션 내에서 insert를 10번 했을 때 네트워크 상으로 쿼리가 몇번 실행될까요?</summary>
  <br>

`Auto_Increment` 속성을 가진 `Entity`를 `INSERT` 10번 한다면 10번의 네트워크를 타게 됩니다. 왜냐하면 영속성 컨텍스트는 엔티티의 `PK` 값을 가지고 구별을 하고 있는데, `Auto Increment`는 엔티티가 저장된 후에 하나씩 증가하는 값이기 때문에 이 때는 `INSERT`가 한번씩 실행되게 됩니다.

</details>

<details>
  <summary>영속성 컨텍스트를 사용하면 쓰기 지연이 가능한 이유가 무엇인가요?</summary>
  <br>

데이터를 저장하면 등록 쿼리를 데이터베이스에 바로 보내지 않고 메모리(쿼리 저장소)에 모아 둡니다. 그리고 트랜잭션을 커밋할 때 등록 쿼리를 데이터베이스에 한번에 보낸 후에 커밋하기 때문에 가능합니다.

</details>

<details>
  <summary>영속성 컨텍스트의 변경 감지가 동작하는 과정에 대해서 설명해주세요.</summary>

엔티티의 변경사항을 데이터베이스에 자동으로 반영하는 기능을 `변경 감지`라고 합니다. JPA는 엔티티를 영속성 컨텍스트에 보관할 때, `최초 상태를 복사해서 저장해두는데 이것을 스냅샷이라고 합니다.` 그리고 플러시 시점에 스냅샷과 엔티티를 비교해서 변경된 엔티티를 찾습니다. `변경 감지는 영속성 컨텍스트가 관리하는 영속 상태의 엔티티에만 적용됩니다.` 

1. 트랜잭션을 커밋하면 엔티티 매니저 내부에서 먼저 `flush()`가 호출됩니다.
2. 엔티티와 스냅샷을 비교해서 변경된 엔티티를 찾습니다.
3. 변경된 엔티티가 있으면 수정 쿼리를 생성해서 쓰기 지연 SQL 저장소에 보냅니다.
4. 쓰기 지연 저장소의 SQL을 데이터베이스에 보닙니다.
5. 데이터베이스 트랜잭션을 커밋합니다.

</details>

<details>
  <summary>영속성 컨텍스트의 1차 캐시 특징에 대해서 설명해주세요.</summary>

## `엔티티 조회, 1차 캐시`

<img width="703" alt="스크린샷 2021-08-25 오전 7 13 32" src="https://user-images.githubusercontent.com/45676906/130697054-93744483-9c26-4764-8864-18a645a130ba.png">

```java
// 엔티티를 생성한 상태 (비영속)
Member member = new Member();
member.setId(1L);
member.setUsername("Gyunny");

// 엔티티를 영속
em.persist(member);   
```

영속성 컨텍스트는 내부에 캐시를 가지고 있는데 이것을 `1차 캐시`라고 합니다. 영속 상태의 엔티티는 모두 이곳에 저장됩니다. 즉, 영속성 컨텍스트 내부에 Map이 하나 있는데 키는 @Id로 매핑한 식별자이고 값은 엔티티 인스턴스입니다.

<br> <br>

## `1차 캐시에서 조회`

```java
// 엔티티를 생성한 상태 (비영속)
Member member = new Member();
member.setId(1L);
member.setUsername("Gyunny");

// 엔티티를 영속
em.persist(member);   

// 1차 캐시에서 조회
Member findMember = em.find(Member.class, "1L");
```

<img width="980" alt="스크린샷 2021-08-25 오전 7 19 18" src="https://user-images.githubusercontent.com/45676906/130697561-1b0c585c-0a4b-4c0c-ab57-82278ba751a2.png">

em.find()를 호출하면 우선 1차 캐시에서 식별자 값으로 엔티티를 찾습니다.

<br> <br>

## `만약 1차 캐시에 없는 2번 Member를 조회한다면?`

```java
Member findMember2 = em.find(Member.class, 2L);
```

<img width="1471" alt="스크린샷 2021-08-25 오전 7 21 24" src="https://user-images.githubusercontent.com/45676906/130697770-f840afea-6b3c-4f4a-a146-67a7c5227e64.png">

만약 em.find()를 호출했는데 엔티티가 1차 캐시에 없으면 엔티티 매니저는 데이터베이스를 조회해서 결과로 나온 Member2를 1차 캐시에 저장한 후에 영속 상태의 엔티티를 반환합니다.
(한 트랜잭션 안에서만 효과가 있기 때문에 막 그렇게 성능의 이점이 있지는 않음!)

<br>

![스크린샷 2021-08-25 오전 7 26 57](https://user-images.githubusercontent.com/45676906/130698357-c7adabcf-5159-4d93-9877-180d76f35865.png)

위와 같이 `엔티티를 영속 상태`로 만든 후에 `em.find()`를 통해서 조회했을 때 실제로 1차 캐시에서 조회를 하는지 알아보겠습니다.

<br>

![스크린샷 2021-08-25 오전 7 29 17](https://user-images.githubusercontent.com/45676906/130698478-d9f3c305-5a03-49cb-b06d-1e6dd6151364.png)

결과를 보면 `em.find()`를 했을 때 `SELECT` 쿼리가 실행되지 않은 것을 볼 수 있습니다. 즉, DataBase에서 조회한 것이 아니라 1차 캐시에서 조회해서 결과를 반환한 것을 알 수 있습니다.

이번에는 위에서 말했던 것처럼 1차 캐시에 없는 멤버를 조회했을 때는 어떻게 되는지 알아보겠습니다.

![스크린샷 2021-08-25 오전 7 35 31](https://user-images.githubusercontent.com/45676906/130698993-f223f7d5-a8b7-4aec-bc76-d2b76e103da4.png)

1번 멤버가 DB에 존재하는 상태로 1번 멤버를 두 번 find() 하는 코드입니다. 위 코드의 결과를 예측해보면 첫 번째 find()만 SELECT 쿼리가 실행되고 두 번째 find()는 1차 캐시에서 가져오기 때문에 SELECT 쿼리가 실행되지 않을 것이라 예상할 수 있습니다. 실제로 그런지 한번 실행해보겠습니다.

![스크린샷 2021-08-25 오전 7 38 17](https://user-images.githubusercontent.com/45676906/130699148-3306a37b-742e-4678-b2dc-bf2b80bbf7c4.png)

결과를 보면 예상했던 대로 SELECT 쿼리가 한번만 실행된 것을 확인할 수 있습니다. (하지만 현업에선 그렇게~ 도움을 주진 않는다고 하는,,)

</details>

<br>

## `Transactional`

<details>
  <summary>@Transactional 어노테이션이 동작하는 방식에 대해서 설명해주세요</summary>
  <br>

- `Spring AOP`를 사용해서 동작하게 됩니다. `@Transactional` 어노테이션이 붙어있는 메소드를 호출하면 `프록시 객체의 메소드가 먼저 호출`됩니다. (프록시 내부에 트랜잭션 코드가 존재하고 프록시 코드를 통해서 실제 요청했던 메소드를 여기서 호출함) 그리고 트랜잭션이 시작되고 실제 클래스의 메소드가 호출되고 커밋되는 방식으로 진행됩니다. (`Proxy` 객체를 생성해서 내부에 클래스 객체를 넣어두고 앞뒤로 `트랜잭션 begin, commit과 rollback처리`를 해줍니다.)
- 실제 호출했던 메소드의 클래스가 인터페이스를 구현하고 있다면 `Dynamic Proxy`를 사용하고, 인터페이스를 구현하고 있지 않다면 `CGLib`을 사용해서 `Proxy` 객체를 생성합니다.
- Dynamic Proxy는 리플렉션을 사용하고, CGLib은 바이트코드를 조작하는 방식으로 Proxy 객체를 생성하여 작업한다.

</details>

<details>
  <summary>@Transactional(readOnly=true) 는 어떻게 동작하나요?</summary>
  <br>

엔티티가 영속성 컨텍스트에 관리되면 1차 캐시, 변경 감지, 지연 로딩, 동일성 보장, 쓰기 지연 5가지 이점을 얻을 수 있습니다.

그런데 트랜잭션에 `readOnly=ture` 설정하면 `스프링 프레임워크가 하이버네이트 세션 플러시 모드를 MANUAL`로 설정한다.

그리고 `readOnly`를 사용하면 `해당 트랜잭션 내에서 쓰기 작업을 할 수 없습니다.` 즉, `읽기 전용이기 때문에 영속성 컨텍스트는 스냅샷을 보관하지 않습니다.` 따라서 메모리 사용량을 최적화할 수 있습니다.

즉, `강제로 플러시를 호출하지 않는 한 플러시가 일어나지 않습니다.` 따라서 트랜잭션을 커밋하더라도 영속성 컨텍스트가 플러시 되지 않아서 엔티티의 등록, 수정, 삭제가 동작하지 않고 또한 읽기 전용으로 영속성 컨텍스트는 변경 감지를 위한 스냅샷을 보관하지 않으므로 성능이 향상됩니다.

</details>

<details>
  <summary>@Transactional 옵션에 대해서 아는대로 말해주세요.</summary>
  <br>

- `Propagation`
- `Isolation`
- `timeout`
- `readOnly`
- `rollbackForClassName`
- `rollbackFor`

</details>


<details>
  <summary>Spring Transactional Propagation 에 대해서 설명해주세요.</summary>
  <br>

- REQUIRED: 아무 설정도 하지 않는 DEFAULT 설정입니다. `REQUIRED`는 부모 트랜잭션이 존재하는 상황에서 자식 트랜잭션을 호출하면 자식 트랜잭션도 부모 트랜잭션에 합류합니다. 그리고 자식에서 에러가 발생해도 부모 까지 모두 Rollback 되고, 부모에서 에러가 발생해도 자식도 모두 Rollback 된다는 특징이 있습니다.
- REQUIRED_NEW: 무조건 새로운 트랜잭션을 만듭니다. 부모에서 에러가 발생하더라도 자식에서 문제가 없다면 문제 없이 트랜잭션 Commit 됩니다.
- NESTED : 부모 트랜잭션이 존재하면 부모 트랜잭션에 중첩시키고, 부모 트랜잭션이 존재하지 않는다면 새로운 트랜잭션을 생성한다. 부모 트랜잭션에 예외가 발생하면 자식 트랜잭션도 Rollback한다. 자식 트랜잭션에 예외가 발생하더라도 부모 트랜잭션은 Rollback하지 않는다.
- MANDATORY : 무조건 부모 트랜잭션에 합류시킵니다. 부모 트랜잭션이 존재하지 않는다면 예외를 발생시킵니다.

</details>

<details>
  <summary>Spring Transactional Isolation 에 대해서 설명해주세요.</summary>
  <br>

특별히 설정하지 않으면 `Isolation.Default`가 설정됩니다. `Isolation.Default`는 현재 사용하고 있는 `RDBS` 기본 격리 레벨을 따라 갑니다.

</details>

<details>
  <summary>REPEATABLE READ 를 사용하게 되면 방어되는 DB의 오류 동작이 어떤 게 방어가 될까요?</summary>
  <br>

`READ COMMITTED`에서 `UNREPETABLE READ`가 발생한다는 문제가 있습니다. `UNREPEATABLE READ`는 A 트랜잭션에 C 라는 쿼리로 테이블을 조회했을 때 결과가 1건이 나왔습니다. 그런데 이 때 B 트랜잭션에서 제가 조회했던 곳의 필드 값을 Update 했습니다. 그리고 제가 한번 더 C 라는 쿼리로 조회했는데 이번에는 결과가 2건이 나오게 되는 이런 상황, 같은 트랜잭션에서 같은 쿼리를 날렸는데 결과가 다른 문제를 `UNREPEATABLE READ` 라고 하고 `REPEATABLE READ`는 이러한 문제를 해결하였습니다.

</details>

<details>
  <summary>@Transactional 어노테이션에 아무런 속성도 주지 않고 사용을 하며 어떻게 처리가 되나요?</summary>
  <br>

Propagtaion은 REQUIRED 가 적용되고, Isolation Level 은 사용하는 RDBMS 의 고립 레벨을 따라 갑니다. 그리고 RuntimeException, Error 일 때 Rollback 되도록 설정 되어 있습니다.

</details>

<details>
  <summary>OSIV 가 무엇인지 설명해주세요.</summary>
  <br>

`OSIV(Open Session In View)`는 영속성 컨텍스트를 뷰까지 열어둔다는 뜻입니다. 영속성 컨텍스트가 살아있으면 엔티티는 영속 상태로 유지됩니다. 따라서 뷰에서도 지연 로딩을 사용할 수 있습니다. 초기 설정이 `OSIV=true` 인데, `OSIV=false`로 바꿔서 `View Layer`에서는 영속성 컨텍스트를 열어놓지 않도록 할 수 있습니다.

[참고](https://github.com/wjdrbs96/Today-I-Learn/blob/master/JPA/13%EC%9E%A5.md#osiv%EB%9E%80)

</details>

<details>
  <summary>JPA의 한계에는 어떤 것이 있을까요?</summary>
  <br>

통계 쿼리나 복잡한 쿼리는 JPA, QueryDSL로 작성하는데 한계가 있다. 이런 복잡한 쿼리들은 직접 쿼리를 작성해서 하는 것이 더 좋다고 생각한다.

</details>

<br>

## `Etc`

<details>
  <summary>sync, asnyc, blocking, non-blocking 특징에 대해서 말해주세요.</summary>
</details>

<details>
  <summary>DTO는 왜 사용하는거 같나요?</summary>
  <br>

- 계층간의 데이터 이동할 때도 필요함 
- 클라이언트 <--> 서버로 데이터 받아올 때도 필요함 
- API를 개발할 때 클라이언트한테 반환하는 것을 Entity로 하지 않고 DTO로 변환시켜서 반환하게 되는데 이럴 때도 사용 가능

</details>