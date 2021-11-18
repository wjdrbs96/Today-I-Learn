## `13장: 웹 애플리케이션과 영속성 관리`

스프링이나 J2EE 컨테이너 환경에서 JPA를 사용하면 컨테이너가 `트랜잭션과 영속성 컨텍스트를 관리`해주므로 애플리케이션을 손쉽게 개발할 수 있습니다. 하지만 컨테이너 환경에서 동작하는 JPA의 내부 동작 방식을 이해하지 못하면 문제가 발생했을 때 해결하기가 쉽지 않습니다. 이번 글에서 이러한 부분들에 대해서 정리해보겠습니다. 

<br> <br>

## `스프링 컨테이너의 기본 전략`

스프링 컨테이너는 `트랜잭션 범위의 영속성 컨텍스트` 전략을 기본적으로 사용합니다. 이 전략은 이름 그대로 `트랜잭션의 범위와 영속성 컨텍스트의 생존 범위가 같다는 뜻`입니다. 즉, 트랜잭션이 시작될 때 영속성 컨텍스트를 생성하고 트랜잭션이 끝날 때 영속성 컨텍스트를 종료합니다. 

![스크린샷 2021-11-17 오후 12 55 08](https://user-images.githubusercontent.com/45676906/142131756-01e53d78-02c0-4d1e-bc23-aca705a75d26.png)

스프링 프레임워크를 사용해서 개발하면 보통 트랜잭션 순서를 처리하는 `Service Layer`에 `@Transactional` 어노테이션을 선언해서 트랜잭션을 시작합니다. `Controller`와 같이 외부에서 `Service` 메소드를 호출하면 `@Transactional` 동작 원리에 따라 `Spring AOP`가 동작합니다. 

<br>

![스크린샷 2021-11-17 오후 1 11 43](https://user-images.githubusercontent.com/45676906/142133317-3a30d32e-4778-4bf7-afc7-2dc68fb40d07.png)

스프링 트랜잭션 AOP는 대상 메소드를 호출하기 직전에 트랜잭션을 시작하고, 대상 메소드가 정상 종료되면 트랜잭션을 커밋하면서 종료합니다. 트랜잭션을 커밋하면 JPA는 먼저 영속성 컨텍스트를 `flush` 해서 변경 내용을 데이터베이스에 반영한 후에 데이터베이스 트랜잭션을 커밋합니다. 

<br> <br>

## `트랜잭션 범위의 영속성 컨텍스트 예제`

```java
@RequiredArgsConstructor
@Controller
public class HelloController {
    
    private HelloService helloService;
    
    public void hello() {
        // 반환된 Member Entity는 준영속 상태
        Member member = helloService.logic();
    }
}
```

```java
@RequiredArgsConstructor
@Service
public class HelloService {
    
    @PersitenceContext // 엔티티 매니저 주입
    private EntityManage em;
    
    private Repository1 repository1;
    private Repository2 repository2;
    
    // 트랜잭션 시작
    @Transactional
    public void logic() {
        repository1.hello();
        
        // Member 영속 상태
        Member member = repository2.findMember();
        return member;
    }
    // 트랜잭션 종료
}
```

```java
@Repository
public class Repository1 {
    
    @PersistenceContext
    private EntityManager em;
    
    public void hello() {
        em.xxx();  // A. 영속성 컨텍스트 접근
    }
}

@Repository
public class Repository2 {

    @PersistenceContext
    private EntityManager em;

    public void hello() {
        em.find(Member.class, "id1");  // B. 영속성 컨텍스트 접근
    }
}
```

`@PersitenceContext` 어노테이션을 사용하면 스프링 컨테이너가 엔티티 매니저를 주입해줍니다.

1. helloService.logic() 메소드에 `@Transactional`을 선언해서 메소드를 호출할 때 트랜잭션을 먼저 시작합니다. 
2. repository2.findMember()를 통해 조회한 Member 엔티티는 트랜잭션 범위 안에 있으므로 영속성 컨텍스트의 관리를 받습니다. (현재 영속 상태)
3. `@Transactional`을 선언한 메소드가 정상 종료되면 트랜잭션을 커밋하는데, 이 때 영속성 컨텍스트를 종료합니다. 영속성 컨텍스트가 사라졌기 때문에 조회한 엔티티는 준영속 상태가 됩니다. 
4. 서비스 메소드가 끝나면서 트랜잭션과 영속성 컨텍스트가 종료되었습니다. 따라서 컨트롤러에 반환된 member 엔티티는 준영속 상태입니다.

<br> <br>

## `트랜잭션이 같으면 같은 영속성 컨텍스트를 사용한다.`

![스크린샷 2021-11-17 오후 1 33 16](https://user-images.githubusercontent.com/45676906/142135169-5a9d0ada-00b4-4b22-b059-47a5cddf5f92.png)

트랜잭션 범위의 영속성 컨텍스트 전략은 다양한 위치에서 엔티티 매니저를 주입받아 사용해도 트랜잭션이 같으면 항상 같은 영속성 컨텍스트를 사용합니다. 

<br> <br>

## `트랜잭션이 다르면 다른 영속성 컨텍스트를 사용한다.`

![스크린샷 2021-11-17 오후 1 40 10](https://user-images.githubusercontent.com/45676906/142135887-5aedb576-58eb-4968-9da1-ce9f8a31fc40.png)

위와 같이 여러 스레드에서 동시에 요청이 와서 `같은 엔티티 매니저를 사용해도 트랜잭션에 따라 접근하는 영속성 컨테스트가 다릅니다.` 즉, 스프링 컨테이너는 스레드마다 각각 다른 트랜잭션을 할당합니다. 

<br> <br>

## `준영속 상태와 지연 로딩`

위에서 말했던 것처럼 트랜잭션은 보통 서비스 계층에서 시작하므로 서비스 계층이 끝나는 시점에 트랜잭션이 종료되면서 영속성 컨텍스트도 함께 종료됩니다. 따라서 조회한 엔티티가 서비스와 레포지토리 계층에서는 영속성 컨텍스트에 관리되면서 영속 상태를 유지하지만 컨트롤러나 뷰 같은 프리젠테이션 계층에서는 준영속 상태가 됩니다. 

```java
@Entity
public class Order {
    
    @Id @GeneratedValue
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY) // 지연로딩 전략
    private Member member;
}
```

컨테이너 환경의 기본 전략인 트랜잭션 범위의 영속성 컨텍스트 전략을 사용하면 트랜잭션이 없는 프리젠테이션 계층에서 엔티티는 준영속 상태입니다. 따라서 변경 감지와 지연 로딩이 동작하지 않습니다. 

<br>

```java
public class OrderController {
    
    public String view(Long orderId) {
        Order order = orderService.findOne(orderId);
        Member member = order.getMember();
        member.getName(); // 지연 로딩 시 예외 발생
    }
}
```

- ### `준영속 상태와 변경 감지`
  - 변경 감지 기능은 영속성 컨텍스트가 살아 있는 서비스 계층까지만 동작하고 영속성 컨텍스트가 종료된 프리젠테이션 계층에서는 동작하지 않습니다. 보통 `변경 감지 기능`은 서비스 계층에서 비즈니스 로직을 수행하면서 발생합니다. 단순히 데이터를 보여주기만 하는 프리젠테이션 계층에서 데이터를 수정할 일은 거의 없습니다. 오히려 변경 감지 기능이 프리젠테이션 계층에서도 동작하면 애플리케이션 계층이 가지는 책임이 모호해지고 어디서 데이터를 변경했는지 알기가 쉽지 않습니다.

- ### `준영속 상태와 지연 로딩`
  - 준영속 상태의 가장 큰 문제는 `지연 로딩` 기능이 동작하지 않는다는 것입니다. 예를들어 뷰를 렌더링할 때 연관된 엔티티도 함께 사용해야 하는데 연관된 엔티티를 `지연 로딩`을 설정해서 프록시 객체로 조회했다고 가정하겠습니다. 아직 초기화되지 않은 프록시 객체를 사용하면 실제 데이터를 불러오려고 초기화를 시도합니다. 하지만 준영속 상태는 영속성 컨텍스트가 없으므로 지연 로딩을 할 수 없습니다. 이 때 지연 로딩을 시도하면 예와가 발생합니다. 

<br> <br>

## `준영속 상태의 지연 로딩 문제를 해결하는 방법`

- 뷰가 필요한 엔티티를 미리 로딩해두는 방법
- OSIV를 사용해서 엔티티를 항상 영속 상태로 유지하는 방법

<br>

OSIV를 사용하는 방법은 아래에서 알아서 우선 뷰가 필요한 엔티티를 미리 로딩하는 다양한 방법을 알아보겠습니다. 이 방법은 이름 그대로 영속성 컨텍스트가 살아있을 때 뷰에 필요한 엔티티들을 미리 다 로딩하거나 초기화해서 반환하는 방법입니다. 따라서 엔티티가 준영속 상태로 변해도 연관된 엔티티를 이미 다 로딩해두어서 지연 로딩이 발생하지 않습니다. 

뷰가 필요한 엔티티를 미리 로딩해두는 방법은 어디서 미리 로딩하느냐에 따라 3가지 방법이 있습니다. 

- 글로벌 페치 전략 수정
- JPQL 페치 조인(fetch join)
- 강제로 초기화

<br> <br>

## `글로벌 페치 전략 수정`

```java
@Entity
public class Order {
    
    @Id @GeneratedValue
    private Long id;
    
    @ManyToOne(fetch = FetchType.EAGER) // 즉시로딩 전략
    private Member member;
}
```

가장 간단한 방법은 글로벌 페치 전략을 `지연로딩 -> 즉시로딩`으로 수정하는 것입니다. 즉시 로딩을 사용하면 연관된 엔티티를 한번에 가져오기 때문에 준영속 상태에서 `지연 초기화` 문제가 생기지 않습니다. 

<br> <br>

## `글로벌 페치 전략에 즉시 로딩 사용 시 단점`

- 사용하지 않는 엔티티를 로딩한다.
- N+1 문제가 발생한다.

<br>

### `사용하지 않는 엔티티를 로딩한다.`

예를 들어, 화면 A는 order와 member가 필요해서 글로벌 전략을 `즉시 로딩`을 설정했습니다. 반면에 화면 B는 `order`만 있으면 되는데 즉시 로딩으로 인해서 Member 까지 조회하기 때문에 비효율적입니다.

<br>

### `N+1 문제가 발생합니다.`

JPA를 사용하면서 성능상 가장 조심해야 하는 것이 바로 `N+1 문제`입니다. 

![스크린샷 2021-11-17 오후 3 00 17](https://user-images.githubusercontent.com/45676906/142143441-4aa710cf-7f33-4c25-9655-2cba4b9a0ec8.png)

`Order`에서 `EAGER`로 Member와 연관관계를 맺고 있을 때 `Order` 하나를 조회하였습니다. 

<br>

![스크린샷 2021-11-17 오후 3 02 04](https://user-images.githubusercontent.com/45676906/142143779-83e0cb51-e740-4b81-a291-b8d43f88ed76.png)

그러면 위와 같이 `Order`, `Member`가 `JOIN`이 되어서 한번에 `Entity`들이 로딩되는 것을 볼 수 있습니다. 여기까지 보면 `글로벌 즉시 로딩 전략`이 상당히 좋아보이지만 문제는 `JPQL을 사용할 때 발생합니다.`

<br>

![스크린샷 2021-11-17 오후 3 12 25](https://user-images.githubusercontent.com/45676906/142144862-0c74ee06-bc84-4329-b9e6-3186ac12b27e.png)

위와 같이 `Member 3명을 저장한 후에 각 멤버들이 주문(Order) 하나씩을 하도록` 코드를 작성하였습니다. 그리고 `JPQL`을 통해서 `Order`를 조회했을 때 쿼리가 어떻게 실행되는지 보겠습니다. 

<br>

![스크린샷 2021-11-17 오후 3 16 49](https://user-images.githubusercontent.com/45676906/142145209-9045da56-6702-4225-81de-3e1882c8e9d4.png)

그러면 위와 같이 `Order` 엔티티 조회하는 쿼리 1번, `Member` 엔티티 조회하는 쿼리 3번 해서 4번의 쿼리가 나가는 것을 볼 수 있습니다. 현재는 예제기에 간단하지만 Member 수가 많다면 말도 안되게 쿼리가 나갈 것입니다 

이렇게 `JPA가 JPQL을 분석해서 SQL을 생성할 때는 글로벌 페치 전략을 참고하지 않고 오직 JPQL 자체만 사용합니다.` 따라서 즉시 로딩이든 지연 로딩이든 구분하지 않고, JPQL 쿼리 자체에 충실하게 SQL을 만듭니다. 

<br> <br>

## `코드 내부 동작 순서`

1. `select o from Order o` JPQL을 분석해서 `select * from Order` SQL을 생성합니다. 
2. 데이터베이스에서 결과를 받아 Order 엔티티 인스턴스를 생성합니다. 
3. Order.member의 `글로벌 페치 전략이 즉시 로딩이므로 Order를 로딩하는 직시 연관된 member도 로딩해야 합니다.`
4. 연관된 member를 영속성 컨텍스트에서 찾습니다.
5. 만약 영속성 컨텍스트에 없으면 `SELECT * FROM Member WHERE id = ?` SQL을 조회한 Order 엔티티 수 만큼 실행합니다. 

<br>

만약 조회한 `Order` 엔티티가 10개라면 `Member`를 조회하는 SQL도 10번 실행합니다. 이처럼 처음 조회한 데이터 수만큼 다시 SQL을 사용해서 조회하는 것을 `N+1` 문제라고 합니다. N+1이 발생하면 SQL이 상당히 많이 호출되므로 조회 성능에 치명적입니다. 따라서 최우선 최적화 대상이고, 이러한 `N+1 문제는 JPQL 페치 조인으로 해결할 수 있습니다.`

<br> <br>

## `JPQL 페치 조인`

글로벌 페치 전략을 즉시 로딩으로 설정하면 애플리케이션 전체에 영향을 주므로 너무 비효율적입니다. 이번에는 JPQL을 호출하는 시점에 함께 로딩할 엔티티를 선택할 수 있는 페치 조인을 알아보겠습니다. 

![스크린샷 2021-11-17 오후 3 25 48](https://user-images.githubusercontent.com/45676906/142146117-c1bf694b-325e-413e-afbe-af86e54429aa.png)

이번에는 위와 똑같은 예제에서 `JPQL`만 `fetch join`을 사용해보겠습니다. 

<br>

![스크린샷 2021-11-17 오후 3 27 01](https://user-images.githubusercontent.com/45676906/142146448-13082416-3e57-4c04-b761-9a8194076cc1.png)

`페치조인(fetch join)`을 사용하니 이번에는 `N+1` 문제가 발생하지 않고 `JOIN`을 통해서 엔티티들을 한번에 가져오는 것을 볼 수 있습니다. 페치 조인은 `N+1` 문제를 해결하면서 화면에 필요한 엔티티를 미리 로딩하는 현실적인 방법입니다. 

<br> <br>

## `JPQL 페치 조인의 단점`

페치 조인이 현실적인 대안이긴 하지만 무분별하게 사용하면 화면에 맞춘 레포지토리 메소드가 증가할 수 있습니다. 결국 프리젠테이션 계층이 알게 모르게 데이터 접근 계층을 침범하는 것입니다.

예를 들어서 화면 A는 Order 엔티티만 필요하고, 화면 B는 Order, Member 엔티티 둘 다 필요합니다. 이렇게 차이가 있어서 로딩 전략을 `지연 로딩`으로 설정하고 2가지 메소드를 만들었습니다. 

- 화면 A를 위해 Order만 조회하면 findOrder() 메소드
- 화면 B를 위해 Order, Member를 함께 `페치 조인`으로 조회하는 findOrderWithMember() 메소드

<br>

이제 각각 화면 A, B가 필요한 메소드를 호출하면 되고, 메소드를 각각 만들어 최적화할 수 있습니다. 하지만 메소드가 해당 뷰에서만 사용할 수 있도록 만들어진 것이다 보니 뷰와 레포지토리 간에 `논리적인 의존관계`가 발생하게 됩니다. 

이러한 문제점을 해결하는 대안으로는 `페치 조인을 하는 ` 메소드를 하나만 만들고 화면 A에서 Order만 필요하더라도 Member 까지 같이 사용하도록 하는 것입니다. 물론 화면 A에서 약간의 로딩 시간이 늘어나긴 하겠지만 이것도 하나의 대안이 될 수 있습니다. 

<br> <br>

## `강제로 초기화`

강제로 초기화하기는 영속성 컨텍스트가 살아있을 때 프리젠테이션 계층이 필요한 엔티티를 강제로 초기화해서 반환하는 방법입니다. (글로벌 페치 전략은 모두 지연 로딩이라고 가정하겠습니다.)

```java
public class OrderService {
    
    @Transactional
    public Order findOrder(Long id) {
        Order order = orderRepository.findOrder(id);
        order.getMember().getName();  // 프록시 객체를 강제로 초기화
        return order;
    }
}
```

글로벌 페치 전략이 지연로딩이라 연관된 엔티티들을 `프록시 객체`로 조회하게 됩니다. 프록시 객체는 실제 사용하는 시점에 초기화됩니다. 즉, 위처럼 Member 엔티티의 name 필드에 접근할 때 실제 Member 엔티티가 영속성 컨텍스트에 올라가게 됩니다. 

즉, 프록시 객체도 모두 `Service Layer`에서 로딩이 된 상태로 `Presentation Layer`로 가기 때문에 준영속 상태에서도 사용할 수 있습니다. 하지만 프록시를 초기화하는 역할을 `Service Layer`가 담당하면 뷰가 필요한 엔티티에 따라 서비스 계층의 로직을 변경해야 합니다. 조금씩 `Presentation`이 `Service` 영역을 침범하고 있는 것입니다. 

`Service Layer`는 비즈니스 로직 or 트랜잭션 순서 처리에 집중해야 하기 때문에 `프록시 초기화 역할을 분리하는 것`이 좋습니다. 그래서 이러한 역할을 `FACADE` 계층이 담당해줄 것입니다.

<br> <br>

## `FACADE 계층 추가`

![스크린샷 2021-11-17 오후 4 07 45](https://user-images.githubusercontent.com/45676906/142150729-a54c8520-92c6-4422-9f3c-c18760a0c33a.png)

위의 그림처럼 `Presentation Layer`와 `Service Layer` 사이에 `FACADE` 계층을 하나 더 두는 방법입니다. 뷰를 위한 프록시 초기화는 `FACADE` 계층에서 담당합니다. 결과적으로 `FACADE` 계층을 도입해서 서비스 계층과 프레젠테이션 계층 사이에 논리적인 의존성을 분리할 수 있습니다. 그리고 프록시를 초기화하려면 영속성 컨텍스트가 필요하므로 `FACADE`에서 트랜잭션을 시작해야 합니다. 

<br> <br>

## `FACADE 계층의 역할과 특징`

- `Presentation Layer`, `Domain Model Layer` 간의 논리적 의존성을 분리해줍니다. 
- `Presentation Layer`에서 필요한 프록시 객체를 초기화합니다.
- 서비스 계층을 호출해서 비즈니스 로직을 실행합니다.
- `Repository`를 직접 호출해서 뷰가 요구하는 엔티티를 찾습니다. 

<br> <br>

## `FACADE 계층 추가`

```java
public class OrderFacade {
    
    @Autowired
    private OrderService orderService;
    
    public class findOrder(Long id) {
        Order order = orderService.findOrder(id);
        order.getMember().getName();
        return order;
    }
}

class OrderService {
    
    public Order findOrder(Long id) {
        return orderRepository.findOrder(id);
    }
}
```

위처럼 `FACADE` 계층을 사용해서 서비스 계층과 프리젠테이션 계층 간에 논리적 의존 관계를 제거했습니다. 하지만 실용적인 관점에서 볼 때 `FACADE`의 최대 단점은 중간에 계층이 하나 더 끼어든다는 점입니다. 결국 더 많은 코드를 작성해야 합니다. 

<br> <br>

## `준영속 상태와 지연 로딩의 문제점`

지금까지 준영속 상태일 때 지연 로딩 문제를 해결하기 위해 글로벌 페치 전략도 수정하고, JPQL의 페치 조인도 사용하고, FACADE 계층까지 알아보았습니다. 뷰를 개발할 때 필요한 엔티티를 미리 초기화하는 방법은 생각보다 오류가 발생할 가능성이 높습니다. 왜냐하면 보통 뷰를 개발할 때는 엔티티 크래스를 보고 개발하지 이것이 초기화되어 있는지 아닌지 확인하기 위해 FACADE나 서비스 클래스까지 열어보는 것은 상당히 번거롭고 놓치기 쉽기 때문입니다.

결국 영속성 컨텍스트가 없는 뷰에서 초기화하지 않은 프록시 엔티티를 조회하는 실수를 하게 되고 `LazyInitializationException`을 만나게 될 것입니다. 그리고 애플리케이션 로직과 뷰가 물리적으로는 나누어져 있지만 논리적으로는 서로 의존한다는 문제가 있습니다. `FACADE`을 사용하는 것도 상당히 번거롭습니다. 

결국 `모든 문제는 엔티티가 프리젠테이션 계층에서 준영속 상태이기 때문에 발생합니다.` 이럴 때 `영속성 컨텍스트를 뷰까지 살아있게 열어둘 수 있고, 뷰에서도 지연 로딩을 사용할 수 있는 것이 OSIV` 입니다.

<br> <br>

## `OSIV란?`

`OSIV(Open Session In View)`는 영속성 컨텍스트를 뷰까지 열어둔다는 뜻입니다. 영속성 컨텍스트가 살아있으면 엔티티는 영속 상태로 유지됩니다. 따라서 뷰에서도 지연 로딩을 사용할 수 있습니다. 

<br> <br> 

## `과거 OSIV: 요청 당 트랜잭션`

![스크린샷 2021-11-17 오후 4 37 16](https://user-images.githubusercontent.com/45676906/142156346-6ce7dbc7-6153-4b5b-9219-f2b4ce02cfbb.png)

OSIV 핵심은 뷰에서도 지연 로딩이 가능하도록 하는 것입니다. 가장 단순한 구현 방법은 클라이언트의 요청이 들어오자마자 서블릿 필터나 스프링 인터셉터에서 트랜잭션을 시작하고 요청이 끝날 때 트랜잭션도 끝내는 것입니다. 이것을 `요청 당 트랜잭션 방식의 OSIV`라 합니다.

<br> <br>

## `요청 당 트랜잭션 방식의 OSIV 문제점`

요청 당 트랜잭션 방식의 OSIV가 가지는 문제점은 컨트롤러나 뷰 같은 프리젠테이션 계층이 엔티티를 변경할 수 있다는 점입니다. 

```java
public class MemberController {
    
    public String viewMember(Long id) {
        Member member = memberService.getMember(id);
        member.setName("Gyunny");
        model.addAttribute("member", member);
    }
}
```

컨트롤러에서 고객 이름을 `Gyunny`로 변경해서 렌더링할 뷰에 넘겨주었습니다. 개발자는 뷰에만 이름을 `Gyunny`로 하고 싶었지만 프리젠테이션 계층도 영속성 컨텍스트가 열려 있어서 `setter`로 이름을 변경한 것이 트랜잭션이 끝날 때 플러시 되면서 DB에 반영이 되게 되는 큰 문제가 발생합니다. 

그래서 프리젠테이션 게층에서 엔티티를 수정하지 못하게 막는 3가지 방법들이 있습니다. 

- 엔티티를 읽기 전용 인터페이스로 제공
- 엔티티 래핑
- DTO만 반환

<br> <br>

## `엔티티를 읽기 전용 인터페이스로 제공`

```java
interface MemberView {
    public String getName(); 
}

@Entity
class Member implements MemberView {
    ...
}

class MemberService {
    
    public MemberView getMember(Long id) {
        return memberRepository.findById(id);
    }
}
```

실제 회원 엔티티가 있지만 프리젠테이션 계층에서는 Member 엔티티 대신에 회원 엔티티의 읽기 전용 메소드만 있는 `MemberView` 인터페이스를 제공했습니다. 프리젠테이션 계층은 읽기 전용 메소드만 있는 인터페이스를 사용하므로 엔티티를 수정할 수 없습니다. 

<br> <br>

## `엔티티 래핑`

```java
class MemberWrapper {

  private Member member;

  // 읽기 전용 메소드만 제공
  public MemberWraaper(Member member) {
      return member.getName();
  }
}
```

Member 엔티티를 감싸고 있는 MemberWapper 객체를 만들었습니다. 이 객체는 Member 엔티티의 읽기 메소드만 제공합니다. 

<br> <br>

## `DTO만 반환`

```java
class MemberDTO {
    
    private String name;
    // Getter, Setter
}

MemberDTO memberDTO = new MemberDTO();
memberDTO.setName(member.getName());
return memberDTO;
```

위와 같이 프리젠테이션 계층에 엔티티 대신에 단순히 데이터만 전달하는 객체인 DTO를 생성해서 반환하는 것입니다. 하지만 이렇게 하면 OSIV를 사용하는 장점을 누리지 못하게 됩니다. 

지금까지 설명한 OSIV는 `요청 당 트랜잭션 방식의 OSIV` 입니다. 이것은 지금까지 설명했던 문제점들로 인해 최근에는 거의 사용하지 않습니다. 최근에는 이런 문제점을 어느정도 `보완해서 비즈니스 계층에서만 트랜잭션을 유지하는 방식의 OSIV를` 사용합니다. `스프링 프레임워크가 제공하는 OSIV가 바로 이방식을 사용하는 OSIV` 입니다.

<br> <br>

## `스프링 OSIV: 비즈니스 계층 트랜잭션`

JPA를 사용하면 서블릿 필터에 OSIV를 적용하려면 `OpenEntityManagerInViewFilter`를 서블릿 필터에 등록하면 되고 스프링 인터셉터에 OSIV를 적용하려면 `OpenEntityManagerInViewInterceptor`를 스프링 인터셉터에 등록하면 됩니다. 

<br> <br>

## `스프링 OSIV 분석`

요청 당 트랜잭션 방식의 OSIV는 프리젠테이션 계층에서 데이터를 변경할 수 있다는 문제가 있습니다. 스프링 프레임워크가 제공하는 OSIV는 이런 문제를 어느정도 해결했습니다. 스프링 프레임워크가 제공하는 `OSIV는 "비즈니스 계층에서 트랜잭션을 사용하는 OSIV" 입니다.` 이름 그대로 OSIV를 사용하기는 하지만 트랜잭션은 비즈니스 계층에서만 사용한다는 것입니다.

![스크린샷 2021-11-17 오후 5 02 19](https://user-images.githubusercontent.com/45676906/142159675-9edac7c9-5a5c-48a2-8d53-143dba6dc43c.png)

동작 원리를 정리하면 아래와 같습니다.

1. 클라이언트의 요청이 들어오면 서블릿 필터나, 스프링 인터셉터에서 영속성 컨텍스트를 생성합니다. 단, 이 때 트랜잭션은 시작하지는 않습니다.
2. 서비스 계층에서 `@Transactional`로 트랜잭션을 시작할 때 1번에서 미리 생성해둔 영속성 컨텍스트를 찾아와서 트랜잭션을 시작합니다. 
3. 서비스 계층이 끝나면 트랜잭션을 커밋하고 영속성 컨텍스트를 플러시합니다. 이 때 트랜잭션은 끝내지만 영속성 컨텍스트는 종료하지 않습니다.
4. 컨트롤러와 뷰까지 영속성 컨텍스트가 유지되므로 조회한 엔티티는 영속 상태를 유지합니다.
5. 서블릿 필터나, 스프링 인터셉터로 요청이 돌아오면 영속성 컨텍스트를 종료합니다. 이 때 플러시는 호출하지 않고 바로 종료합니다.

<br> <br>

## `트랜잭션 없이 읽기`

영속성 컨텍스트를 통한 모든 변경은 트랜잭션 안에서 이루어져야 합니다. 만약 트랜잭션 없이 엔티티를 변경하고 영속성 컨텍스트를 플러시하면 `javax.persistence.TransactionRequiredException` 예외가 발생합니다. 

엔티티를 변경하지 않고 단순히 조회만 할 때는 트랜잭션이 없어도 되는데 이것을 트랜잭션 없이 읽기(`Nontransactional reads` 라고 합니다.) 프록시를 초기화하는 지연 로딩도 조회 기능이므로 트랜잭션 없이 읽기가 가능합니다.

- 영속성 컨텍스트는 트랜잭션 범위 안에서 엔티티를 조회하고 수정할 수 있습니다.
- 영속성 컨텍스트는 트랜잭션 범위 밖에서 엔티티를 조회만 할 수 있습니다. 이것을 트랜잭션 없이 읽기라고 합니다.

<br>

스프링이 제공하는 OSIV를 사용하면 프리젠테이션 계층에서는 트랜잭션이 없으므로 엔티티를 수정할 수 없습니다. 따라서 프리젠테이션 계층에서 엔티티를 수정할 수 있는 기존 OSIV의 단점을 보완했습니다. 

- 영속성 컨텍스트를 프리젠테이션 계층까지 유지합니다.
- 프리젠테이션 계층에는 트랜잭션이 없으므로 엔티티를 수정할 수 없습니다.
- 프리젠테이션 계층에는 트랜잭션이 없지만 트랜잭션 없이 읽기를 사용해서 지연 로딩을 할 수 있습니다.

<br> <br>

```java
public class MemberController {
    
    public String viewMember(Long id) {
        Member member = memberService.getMember(id);
        member.setName("Gyunny");
        model.addAttribute("member", member);
    }
}
```

프리젠테이션 계층까지 아직 `영속성 컨텍스트`가 살아있습니다. 컨트롤러에서 `name`을 변경했지만 여기서는 플러시가 2가지의 이유로 동작하지 않습니다.

- 영속성 컨텍스트의 변경 내용을 데이터베이스에 반영하려면 영속성 컨텍스트를 플러시해야 합니다. 하지만 트랜잭션을 사용하는 서비스 계층이 끝날 때 트랜잭션이 커밋되면서 이미 플러시 해버렸습니다. 그리고 스프링 인터셉터, 필터에서 영속성 컨텍스트만 종료해버리기 때문에 플러시가 일어나지 않습니다.
- 프리젠테이션 계층에서 `em.flush()`를 호출해서 강제로 플러시해도 트랜잭션 범위 밖이므로 데이터를 수정할 수 없다는 예외를 만납니다.

<br> <br>

## `스프링 OSIV 주의 사항`

스프링 OSIV를 사용하면 프리젠테이션 계층에서 엔티티를 수정해도 수정 내용을 데이터베이스에 반영하지 않습니다. 그런데 프리젠테이션 계층에서 엔티티를 수정한 직후에 트랜잭션을 시작하는 서비스 계층을 호출하면 문제가 발생합니다.

```java
public class MemberController {
    
    public String viewMember(Long id) {
        Member member = memberService.getMember(id);
        member.setName("Gyunny");
        
        memberService.biz(); // 비즈니스 로직
        return "view";
    }
}

class MemberService {
    
    @Transactional
    public void biz() {
        // 비즈니스 로직 실행
    }
}
```

![스크린샷 2021-11-17 오후 5 25 59](https://user-images.githubusercontent.com/45676906/142162985-719456c5-94b5-498b-aefb-a9410186324c.png)

1. 컨트롤러에서 회원 엔티티를 조회하고 이름을 `member.setName("XXX")`로 수정했습니다. 
2. `biz()` 메소드를 실행해서 트랜잭션이 있는 비즈니스 로직을 실행했습니다. 
3. 트랜잭션 AOP가 동작하면서 영속성 컨텍스트에 트랜잭션을 시작합니다. 그리고 `biz()` 메소드를 실행합니다. 
4. biz() 메소드가 끝나면 트랜잭션 AOP는 트랜잭션을 커밋하고 영속성 컨텍스트를 플러시합니다. 이 때 변경 감지가 동작하면서 회원 엔티티의 수정 사항을 데이터베이스에 반영합니다. 

<br>

컨트롤러에서 엔티티를 수정하고 즉시 뷰를 호출한 것이 아니라 트랜잭션이 동작하는 비즈니스 로직을 호출해서 이러한 문제가 발생합니다. 트랜잭션이 있는 비즈니스 로직을 모두 호출하고 나서 엔티티를 변경하면 됩니다. `스프링 OSIV는 같은 영속성 컨텍스트를 여러 트랜잭션이 공유할 수 있으므로 이런 문제가 발생합니다.`

<br> <br>

## `OSIV 정리`

- ### `스프링 OSIV 특징`
  - OSIV는 클라이언트의 요청이 들어올 때 영속성 컨텍스트를 생성해서 요청이 끝날 때까지 같은 영속성 컨텍스트를 유지합니다. 따라서 한 번 조회한 엔티티는 요청이 끝날 때까지 영속 상태를 유지합니다.
  - 엔티티 수정은 트랜잭션이 있는 계층에서만 동작합니다. 트랜잭션이 없는 프리젠테이션 계층은 지연 로딩을 포함해서 조회만 할 수 있습니다.

- ### `스프링 OSIV의 단점`
  - OSIV를 적용하면 같은 영속성 컨텍스트를 여러 트랜잭션이 공유할 수 있따는 점을 주의해야 합니다.
  - 프레젠테이션 계층에서 지연 로딩에 의한 SQL이 실행됩니다. 

- ### `OSIV vs FACADE vs DTO`
  - OSIV를 사용하지 않는 대안은 FACADE 계층이나 그것을 조금 변형해서 사용하는 다양한 방법이 있는데 어떤 방법을 사용하든 결국 준영속 상태가 되기 전에 프록시를 초기화해야 합니다. 다른 방법은 `엔티티를 직접 노출하지 않고 엔티티와 거의 비슷한 DTO를 반환하는 것`입니다. 어떤 방법을 사용하든 OSIV를 사용하는 것과 비교해서 지루한 코드를 많이 작성해야 합니다. 

- ### `OSIV는 같은 JVM을 벗어난 원격 상황에서는 사용할 수 없다.`
  - 예를들어 JSON, XML을 생성할 때는 지연 로딩을 사용할 수 있지만 원격지인 클라이언트에서 연관된 엔티티를 지연 로딩하는 것은 불가능합니다. 엔티티를 JSON 변환 객체로 사용하면 엔티티를 변경할 때 API 스펙도 바뀌기 때문에 DTO로 변환해서 노출하는 것이 안전합니다.