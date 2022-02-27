# `@Transactional 어노테이션 동작 과정을 디버깅을 통해 알아보기`

먼저 이 글은 `공식 문서`를 기반으로 완벽한 근거로 옳은 소리만으로 작성된 글이 아니고, 여러 가지 글을 참고하며 저의 생각을 기반으로 작성된 글입니다. 그래서 틀린 말이나 부족한 말이 존재할 수 있습니다.

`Spring Boot`로 `CRUD`를 개발할 때 `@Transactional` 어노테이션은 필수라고 할 수 있는데요. 그 이유는 서버 개발할 때 `트랜잭션`은 반드시 필요하기 때문입니다. 

트랜잭션이라고 하면 트랜잭션을 시작하는 곳이 있고, 트랜잭션이 끝났다면 트랜잭션을 커밋하여 데이터베이스에 영구적으로 반영하는 기능이 있습니다. 그리고 만약에 중간에 에러가 발생한다면 롤백 처리도 해줄 수 있는 것이 바로 트랜잭션의 역할입니다.

그러면 `Spring Boot`에서는 어떻게 `@Transactional` 이라는 어노테이션만 추가했는데 `트랜잭션 시작`, `트랜잭션 커밋`, `에러가 발생한다면 트랜잭션 롤백`과 같은 기능들을 제공해주는 것일까요?

아마 저를 포함하여 많은 사람들이 `@Transactional` 어노테이션의 내부 동작 원리를 잘 모르고 `아마도 이런 것을 해줄 것이야~` 정도만 생각하고 사용해왔을 것입니다.

그런데 저는 해당 어노테이션을 언제 어떻게 사용해야 하는지 아는 것도 중요하지만, 제가 사용하고 있는 어노테이션이 `어떻게 동작하는지 알고 쓰는 것이 매우 중요하다고 생각`합니다. 그래서 이번 글에서는 `Spring Boot`에서는 `@Transactional` 어노테이션은 어떻게 동작하는가?에 대한 주제로 글을 정리해보려 합니다.

먼저 `@Transactional` 어노테이션이 동작하는 방식을 알려면 `Proxy`와 `AOP` 라는 것에 대해서 알아야 하는데요. `Proxy`와 `AOP`가 무엇인지 간단하게 알아보고 가겠습니다.(어느정도 알고 있다고 가정하고 글을 작성해보겠습니다.)

<br>

## `스프링 AOP 특징`

스프링 AOP를 공부하다 보면 항상 나오는 예제가 있습니다. 바로 `실행 시간`을 출력하는 것인데요. 실행 시간을 출력하는 예제를 보면서 AOP 특징에 대해서 알아보겠습니다.

```java
public interface EventService {

    void createEvent();
    void publishEvent();
    void deleteEvent();
}
```

```java

@Service
public class SimpleEventService implements EventService {

    @Override
    public void createEvent() {
        long begin = System.currentTimeMillis();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Created an event");
        System.out.println(System.currentTimeMillis() - begin);
    }

    @Override
    public void publishEvent() {
        long begin = System.currentTimeMillis();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Published an event");
        System.out.println(System.currentTimeMillis() - begin);
    }

    @Override
    public void deleteEvent() {

    }
}
```

위의 코드는 `EventService 인터페이스`를 구현한 `SimpleEventService 클래스`에 `createEvent()`, `publishEvent()` 메소드가 존재하고 실행 시간을 측정하려고 합니다.

만약에 위의 메소드 전부에다 실행 시간을 구하는 롲기을 넣으면, 지금의 예시는 단순히 메소드가 2개만 존재할 뿐이기에 별거 아닐 수 있지만 실제 프로젝트로 치면 엄청나게 많은 중복 코드가 필요할 것입니다. 만약에 메소드가 30개만 됐다고 해도 엄청난.. 중복이 일어날텐데요. 이러한 상황에서 사용할 수 있는 것이 `AOP` 입니다.

`AOP는 어떤 방법을 사용했기에 기존 코드는 수정하지 않고 메소드들의 성능 측정을 할 수 있는 것일까요?` 바로 `프록시 패턴`을 사용하기 때문입니다.

<br> <br>

## `프록시 패턴`

![proxy](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FEECrr%2FbtqFWZhqAhT%2Fl8kDltgwVpC7mAEC1uwKG1%2Fimg.png)

`프록시 패턴`이란 실제 클래스가 구현하고 있는 인터페이스를 똑같이 구현하고, 실제 클래스를 참조하면서 프록시 클래스를 만듭니다. 그리고 `프록시 클래스에서 추가하고자 하는 코드를 추가하고 실제 코드를 호출하는 방식입니다.`

즉, 위의 그림을 보면 `Client`는 Subject 인터페이스 타입으로 프록시 객체를 사용하게 되고, 프록시는 `Real Subject`를 감싸서 클라이언트의 요청을 처리하게 됩니다. 정리하면, `프록시 패턴의 목적은 기존 코드 변경 없이 접근 제어 또는 부가 기능을 추가하기 위해서`입니다.

좀 더 자세히 알아보기 위해 `EventService 인터페이스`를 구현하는 `프록시 클래스`의 예시 코드를 보겠습니다.

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

위의 `Proxy 클래스`의 코드를 보면 필드로 실제 핵심 코드를 담당하는 `SimpleEventService`를 참조하고 있는 것을 볼 수 있습니다. 즉, 이러한 구조를 가진 패턴을 `프록시 패턴`이라고 합니다. 

프록시 패턴을 사용했을 때 기존 클래스인 `SimpleEventService`에는 성능 측정하는 코드를 작성하지 않고도 성능을 측정할 수 있게 되었습니다. 

하지만 이러한 방식도 메소드가 많다면 코드의 중복이 많이 일어날 것이고 매번 `Proxy 클래스`도 직접 만들어야 한다는 큰 단점(번거로움)이 존재합니다. 그래서 이러한 단점을 해결하기 위해 나온 것이 바로 `Spring AOP` 입니다.

<br> <br>

## `Spring AOP란?`

![spring-aop](https://user-images.githubusercontent.com/45676906/122227178-a1af6a00-cef1-11eb-8c22-23cbcb43bc03.png)

스프링도 위에서 본 `프록시를 이용해서 AOP를 구현`하고 있습니다. AOP의 핵심 기능은 `코드를 수정하지 않으면서 공통 기능의 구현을 추가하는 것`이라고 강조하고 있습니다. 핵심 기능에 공통 기능을 추가하는 방법에는 아래와 같이 3가지 방법이 존재합니다.

- 컴파일 : 자바 파일을 클래스 파일로 만들 때 바이트코드를 조작하여 적용된 바이트코드를 생성
- 로드 타임 : 컴파일은 원래 클래스 그대로 하고, 클래스를 로딩하는 시점에 끼워서 넣는다.
- `런타임` : A라는 클래스를 빈으로 만들 때 A라는 타입의 프록시 빈을 감싸서 만든 후에, 프록시 빈이 클래스 중간에 코드를 추가해서 넣는다.

<br>

스프링에서 많이 사용하는 방식은 `프록시를 이용한 세 번째 방법`입니다. `Spring AOP`는 프록시 객체를 자동으로 만들어줍니다. 따라서 위에서 본 `ProxySimpleEventService` 클래스 처럼 상위 타입의 인터페이스를 상속 받은 클래스를 직접 구현할 필요가 없습니다. 단지 공통 기능을 구현한 클래스만 잘 구현하면 됩니다.

여기서 좀 더 자세히 들어가면 `Proxy` 패턴을 구현하는 기술에는 `Dynamic Proxy`와 `CGLib` 2가지가 존재합니다. 이 2가지에 대해서도 짧게 정리하고 넘어가겠습니다. 

<br>

## `Dynamic Proxy란 무엇일까?`

`JDK Dynamic Proxy`란 Java의 리플렉션 패키지에 존재하는 Proxy라는 클래스를 통해 생성된 Proxy 객체를 의미합니다.(`Dynamic Proxy`는 인터페이스 없으면 `Proxy` 생성이 안됩니다.)

<br>

## `CGLib이란 무엇일까?`

`Code Generator Library`의 약자로, 클래스의 바이트코드를 조작하여 Proxy 객체를 생성해주는 라이브러리입니다. 동적으로 타겟 클래스의 바이트 코드를 조작하고 이후 호출시엔 조작된 바이트 코드를 재사용함으로써 성능도 `Dynamic Proxy`보다 앞선다고 밝혀졌습니다.

`CGLib`과 `Dynamic Proxy`에 대한 자세한 설명은 [여기](https://gmoon92.github.io/spring/aop/2019/04/20/jdk-dynamic-proxy-and-cglib.html) 를 참고하시는 것을 추천드립니다. 링크에 자세히 나와 있어서 여기서는 이정도만 설명하고 넘어가겠습니다.

<br> <br>

## `AOP Proxies`

[Spring Document](https://docs.spring.io/spring-framework/docs/current/reference/html/core.html#spring-core) 를 보면 아래와 같이 설명하고 있습니다.

![스크린샷 2022-02-27 오전 3 35 46](https://user-images.githubusercontent.com/45676906/155855145-2bf35f32-f6e4-4b64-a93c-a3229c500b93.png)

위의 설명을 요악하자면 타켓 클래스의 인터페이스를 존재하지 않으면 `CGLib`이 사용되고, 인터페이스가 존재하면 `Dynamic Proxy`를 사용한다고 나와있습니다.

<br> <br>

## `그러면 이제는 CGLib을 Default 방식으로 채택하는 것이 더 좋지 않나?`

![스크린샷 2022-02-27 오전 3 19 34](https://user-images.githubusercontent.com/45676906/155854613-2fb2ccd0-0414-46a5-a93f-ef484b6be91d.png)

`Spring Boot 1.4` 버전 이후부터 `CGLib`이 기본으로 선택되었습니다.([Reference](https://github.com/spring-projects/spring-boot/issues/8434))

<br> <br>

## `@Transactional 어떤 속성을 가지고 있을까?`

![스크린샷 2022-02-27 오전 3 41 26](https://user-images.githubusercontent.com/45676906/155855246-09c046ed-ea96-47ff-a644-82bbe9ef8476.png)

`@Transactional` 어노테이션을 보면 대표적으로 `propagation`, `isolation`, `readOnly`에 대한 설정을 할 수 있습니다. 하지만 이번 글에서 이런 부분까지 다루면 너무 글이 길어지기 때문이 이러한 설정을 할 수 있다 정도로만 정리하고 넘어가겠습니다.

<br> <br> 

## `디버깅을 통해 @Transactional 코드의 동작 과정을 알아보자.`

<img width="683" alt="스크린샷 2021-10-20 오후 5 10 27" src="https://user-images.githubusercontent.com/45676906/155855752-5d229f56-ebfd-4093-8422-aee7fd983948.png">

디버깅을 알아보기 전에 위의 그림을 보면 `@Transactional` 어노테이션이 있는 메소드를 호출했을 때 어떤 과정들이 일어나는지 나와 있습니다. 이제 실제로 디버깅을 통해서 `@Transactional` 어노테이션이 어떻게 `Proxy`를 생성하는지와 내부적으로 어떤 일들이 일어나는지 간단하게 알아보겠습니다. 

![스크린샷 2022-02-27 오전 3 55 30](https://user-images.githubusercontent.com/45676906/155855651-0cbc471c-2f9a-4b98-8b01-b7821256c7f2.png)

먼저 `@Transactional 어노테이션이 존재하는 saveThumbnail` 메소드를 호출하고 있는 것을 볼 수 있습니다. 그런데 `saveThumbnail`을 호출하고 있지만, 밑에 클래스 이름을 보면 `CGLib` 이라고 써져 있는 것을 볼 수 있습니다. 즉, `saveThumbnail` 메소드가 `@Transactional`이 붙어 있기 때문에 `saveThumbnail` 메소드를 호출해도 실제 메소드가 호출되기 전에 `프록시` 객체가 생성되어 `Proxy` 객체가 먼저 생성되는 것을 볼 수 있습니다.

저는 `Proxy` 객체가 만들어 진 후에 비즈니스 로직이 끝난 다음에 트랜잭션이 커밋을 할 것인지 롤백을 하던지 할텐데, 그 때의 로직이 어떻게 흘러가는지 디버깅을 통해서 알아보겠습니다. 

<br>

![스크린샷 2022-02-27 오전 4 02 18](https://user-images.githubusercontent.com/45676906/155855853-099a483c-ef2f-4bf0-b484-cb0ca86a78e8.png)

디버깅을 따라가다 보니 `invoke` 라는 메소드를 통해서 `Proxy Method`를 호출하는 것처럼 보이는데요. 계속 다음으로 넘어가보겠습니다. 

<br>

![스크린샷 2022-02-27 오전 4 07 06](https://user-images.githubusercontent.com/45676906/155855992-283eed4b-c7b4-4dac-ad35-bc2d69ba3595.png)

계속 디버깅을 따라가다보면 많은 `invoke` 호출을 하다 보면 (정확히 모든 것을 이해할 수 없지만,,) 위와 같이 `트랜잭션 commit`과 관련 있어보이는 메소드가 나옵니다.

<br>

![스크린샷 2022-02-27 오전 4 09 44](https://user-images.githubusercontent.com/45676906/155856073-8b26b5fc-8288-4694-8c08-2561802b0015.png)

모든 코드를 다 이해할 순 없지만, 트랜잭션과 관련된 무수히 많은 작업들이 일어나는 것을 볼 수 있습니다. 그리고 저는 `isolation`과 `propagation`을 모두 `deafult`로 놓고 사용하고 있지만, 이러한 속성들을 바꿔서 사용한다면 `분리 처리`를 할 때도 다양한 로직이 생길 수 있겠다라는 생각이 듭니다.

위와 같이 디버깅을 통해서 여러 번 내부 코드를 보았지만, 자세히 이해하기는 쉽지 않았습니다. 다만 이름에서 유추한다면 비즈니스 로직이 예외 없이 동작한다면 데이터베이스에 커밋 작업을 하는 코드가 존재하고, 예외가 발생하면 롤백하는 코드가 존재하는 것을 볼 수 있었습니다.

즉, 코드를 완벽하게는 이해할 순 없어도 `@Transactional` 어노테이션만 추가하면 `Spring Boot`는 `CGLib`을 통해서 트랜잭션 관련 코드를 추가해주어 마법과도 같은 일들을 해주고 있다는 것을 다시 한번 느낄 수 있었습니다. 

이러한 `Spring Boot` 덕분에 사용자 입장에서는 너무나 편리하게 사용할 수 있습니다.(너무 편리하기에 내부 구현을 모르고 사용법만 익힐 수 있기에 너무 편리한게 때로는 단점이 될 수도 있는 거 같습니다.)

<br> <br>

## `마무리 하며`

이번 글의 정리로 `Dynamic Proxy`가 무엇이며, `CGLib`은 무엇이며 두 개는 어떠한 차이점이 있는지에 대해서 알아볼 수 있었습니다. 뿐만 아니라 디버깅을 통해서 내부 코드를 완벽하게 이해하여 정리하지는 못했지만, `@Transactional` 어노테이션은 `CGLib` 기반으로 `Proxy` 객체가 생성되어 트랜잭션 관련 작업의 코드들을 생성해주어 대신 작업해준다 라는 것을 좀 더 자세히 알아볼 수 있는 시간이었습니다.

<br> <br>

## `Reference`

- [인프런 스프링 프레임워크 핵심기술](https://www.inflearn.com/course/spring-framework_core)
- [https://gmoon92.github.io/spring/aop/2019/04/20/jdk-dynamic-proxy-and-cglib.html](https://gmoon92.github.io/spring/aop/2019/04/20/jdk-dynamic-proxy-and-cglib.html)
- [Spring Core Document](https://docs.spring.io/spring-framework/docs/current/reference/html/core.html#aop-introduction-proxies)