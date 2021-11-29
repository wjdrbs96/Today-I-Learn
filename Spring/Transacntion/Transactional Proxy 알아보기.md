## `@Transactional Proxy 알아보기`

스프링은 `@Transactional` 어노테이션을 이용해서 트랜잭션을 처리하기 위해 내부적으로 `AOP`를 사용합니다. ([AOP란?](https://devlog-wjdrbs96.tistory.com/398?category=882236)) `AOP`를 얘기하려면 `Proxy` 얘기를 안할 수가 없는데요.  `Proxy`에 대해서 잠깐 얘기하고 가겠습니다. 

<br> <br>

## `Proxy 패턴이란?`

![1](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FEECrr%2FbtqFWZhqAhT%2Fl8kDltgwVpC7mAEC1uwKG1%2Fimg.png)

Client는 Subject 인터페이스 타입으로 프록시 객체를 사용하게 되고, 프록시는 Real Subject를 감싸서 클라이언트의 요청을 처리하게 됩니다. 프록시 패턴의 목적은 기존 코드 변경 없이 접근 제어 또는 부가 기능을 추가하기 위해서입니다. 그리고 위의 EventService 인터페이스를 구현하는 프록시 클래스를 만들어보겠습니다.

```java
@Primary
@Service
public class ProxySimpleEventService implements EventService {

    @Autowired
    private SimpleEventService simpleEventService;

    @Override
    public void createEvent() {
        long begin = System.currentTimeMillis();
        simpleEventService.createEvent();
        System.out.println(System.currentTimeMillis() - begin);
    }

    @Override
    public void publishEvent() {
        long begin = System.currentTimeMillis();
        simpleEventService.publishEvent();
        System.out.println(System.currentTimeMillis() - begin);
    }

    @Override
    public void deleteEvent() {

    }
}
```

`EventService` 인터페이스가 존재하고 `SimpleEventService` 실제 클래스가 존재하는 상황인데요. 이 때 실제 클래스의 코드를 수정하지 않고 위와 같이 `ProxySimpleEventService` 클래스를 만들고 `Proxy Class -> Real Class`를 참조하도록 구현합니다. 

그러면 실제 클래스의 코드 수정 없이 `Proxy Class`를 통해서 기능을 추가할 수 있습니다. 그런데 이렇게 `인터페이스`를 구현하는 `Proxy Class`를 직접 만들어야 한다는 번거로움이 있는데요. 이러한 번거로움을 `Spring AOP에서 Dynamic Proxy` 라는 것을 통해서 해결해줍니다. 

`Spring AOP`는 `Real Class를 빈으로 만들 때 Real Class 타입의 프록시 빈을 감싸서 만든 후에, 프록시 빈이 클래스 중간에 코드를 추가해서 넣는 과정`을 `런타임`시에 해줍니다.  

<br> <br>

## `Spring AOP 동작 방식`

![image](https://user-images.githubusercontent.com/45676906/143893354-7c16fb39-d745-494c-ac3d-5aad4231d5d4.png)

`@Transactional` 어노테이션이 존재하는 메소드를 호출하면 위의 그림과 같은 방식으로 호출됩니다. 즉, 실제 메소드를 호출하면 프록시 객체가 호출되고 `트랜잭션 시작 -> 실제 메소드 호출 -> 트랜잭션 커밋` 과정을 통해서 진행됩니다.

```java
@Service
public class MemberService {
    
    private final MemberRepository memberRepository;

    @Transactional
    public void register(Member member) {
        memberRepository.save(member);
    }
}

```

만약에 위처럼 `@Transactional`이 붙어 있는 `save` 메소드를 호출한다고 가정하겠습니다. 

<br>

```java
@Service
public class MemberServiceProxy {
    
    private final MemberService memberService;
    private final TransactonManager manager = TransactionManager.getInstance();

    public void register(Member member) {
        try {
            manager.begin();
            memberService.register(member);
            manager.commit();
        } catch (Exception e) {
            manager.rollback();
        }
    }
}
```

그러면 코드는 정확하지 않지만 대략적으로 위와 같이 `Spring Dynamic Proxy`를 통해서 `Proxy` 객체가 생성 만들어지고 `Proxy Class -> Real Class`를 참조하게 됩니다. 그리고 위처럼 `트랜잭션 시작, 커밋, 롤백` 코드가 동작하게 됩니다.