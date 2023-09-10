# `들어가기 전에`

이번 글에서는 현재 [프로젝트](https://github.com/YAPP-18th/iOS1_Backend) 를 진행하면서 `인가` 관련 로직은 대부분의 API에서 사용되기 때문에 AOP를 사용하여 중복 로직을 제거한 과정에 대해서 어떤 것을 깨달았고 어떻게 하였는지를 정리하면 좋을 거 같아서 한번 정리를 해보려 합니다.

먼저 `인증`, `인가`의 차이는 [여기](https://www.youtube.com/watch?v=y0xMXlOAfss) 를 참고하시면 좋을 거 같습니다. 간단하게 무엇인지 알아보면 아래와 같은데요. 

<br>

## `인증(Authentication)`

어떤 A라는 건물에 출입을 할 때, 출입증이 있다면 들어갈 수 있고 없다면 들어갈 수 없다. 이렇게 `식별가능한 정보로` 서비스에 등록된 유저의 신원을 입증하는 과정을 `인증`이라 합니다.

<br>

## `인가(Authorization)`

`출입증`으로 회사 건물의 모든 곳을 다 돌아다닐 수 있는 것은 아닙니다. (ex: A라는 10층짜리 건물에 내가 다니는 회사는 5층 한 층만 사용한다면, 나머지 층에는 출입을 할 수 없습니다.)
이러한 것을 `인가`라고 한다. 한마디로 `권한에 대한 허가`를 나타내고, `인증된 사용자에 대한 자원 접근 권한 확인`입니다.

또한 유저의 `글을 수정, 삭제` 할 수 있는 `권한`을 대표적으로 예시를 들 수 있습니다. 

<br>

![스크린샷 2021-06-16 오후 10 54 09](https://user-images.githubusercontent.com/45676906/122231887-c6a5dc00-cef5-11eb-95d6-74e558684227.png)

제가 진행하고 있는 프로젝트에서는 위와 같이 `로그인, 회원가입`을 성공적으로 했을 때 `AccessToken`, `RefreshToken`을 발급해줍니다. (만료시간, 재발급 등등은 다루지 않습니다.) 

그리고 클라이언트는 발급 받은 AccessToken을 HTTP Header에 담아 서버 API를 요청하고 있습니다. 현재 프로젝트 거의 대부분 API에 `인가` 체크가 필요한 상태입니다. (예를들어, `마이페이지 조회`의 경우도 해당 유저만 볼 수 있어야 하기 때문에 `인가` 처리를 하고 있습니다.)

그런데 만약 API가 100개이고, 그 중에서 95개의 API에 `인가 체크`가 필요하다면 95개 API에 `중복된 로직`을 작성해야 한다는 문제점이 존재하는 것을 느꼈습니다. 그래서 이러한 중복 로직을 제거하기 위해서 `Spring AOP`를 적용하려 하였습니다. 
그러면 AOP는 무엇일까요? 


<br>

# `Spring AOP란?`

![스크린샷 2021-06-16 오후 10 24 30](https://user-images.githubusercontent.com/45676906/122227178-a1af6a00-cef1-11eb-8c22-23cbcb43bc03.png)

AOP는 Spring 삼각형 중에 하나입니다. 즉, 엄청나게 중요한 개념이라고 할 수 있겠죠..? 전체 구조를 보면 위와 같은 그림으로 표현할 수 있습니다. Filter, Interceptor 등등 많이 사용하고 있는데요. Controller 바로 앞단에 보면 `AOP(Aspect Oriented Programming)`가 존재하는 것을 볼 수 있습니다. AOP는 `관점 지향 프로그래밍` 이라고 불리는데요. 

관점지향..? 뭔소리인가 싶습니다. 그래서 조금 더 알아보면서 이해해보겠습니다. 

<br>

![aop](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FbJOHOE%2FbtqF0QCJlCc%2F2sTwCFrVK71VUGTCK6Hkl0%2Fimg.png)

위의 A, B, C 클래스에서 동일한 색깔의 선들의 의미는 클래스들에 나타나는 비슷한(중복되는) 메소드, 필드, 코드들이 나타난다는 것입니다. 이러한 경우 만약 클래스 A에 주황색 부분을 수정해야 한다면 B, C 클래스들에 주황색 부분에 해당하는 곳을 찾아가 전부 코드를 수정해야 합니다. 
(지금은 예시가 몇 개 안되니 그렇지 엄청나게 많다면.. 유지보수가 쉽지 않을 것입니다.) 이런식으로 `반복되는 코드를 흩어진 관심사 (Crosscutting Concerns)`라 부릅니다.

이러한 문제를 `AOP는 Aspect를 이용해서 해결`합니다. 사진 아래쪽을 보면 알 수 있는데, 흩어져 있는 부분들을 Aspect를 이용해서 모듈화 시킵니다. (모듈화란 어떤 공통된 로직이나 기능을 하나의 단위로 묶는 것을 말합니다.) 그리고 개발자가 모듈화 시킨 Aspect를 사진에서 위에 클래스에 어느 곳에 사용해야 하는지만 정의해주면 됩니다.

즉, `결론적으로 Aspect로 모듈화하고 핵심적인 비즈니스 로직에서 분리하여 재사용하겠다는 것이 AOP의 취지입니다.` 이번 글은 AOP에 대한 설명 글은 아니니.. AOP에 대해서 좀 더 자세히 알고 싶다면 [여기](https://devlog-wjdrbs96.tistory.com/178?category=882236) 를 참고하시면 될 거 같습니다. 

<br> <br> 

## `Spring AOP로 중복 로직 제거하기`

이제 프로젝트에서 어떻게 AOP로 중복로직을 제거했는지 좀 더 알아보아야 할텐데요. 먼저 공통 로직을 담은 `Aspect`를 아래와 같이 만들어보겠습니다. 

```java
@RequiredArgsConstructor 
@Aspect     // AOP Aspect
@Component 
public class AuthAspect {

    private static final String AUTHORIZATION = "accessToken";

    private final JwtService jwtService;
    private final UserFindService userFindService;
    private final HttpServletRequest httpServletRequest;

    @Around("@annotation(com.yapp.ios1.annotation.Auth)") // 어노테이션과 Aspect 연결
    public Object accessToken(final ProceedingJoinPoint pjp) throws Throwable {
        try {
            String accessToken = httpServletRequest.getHeader(AUTHORIZATION); // HTTP Header 에서 AccessToken을 꺼냄
            JwtPayload payload = jwtService.getPayload(accessToken);          // Token 검증
            User user = userFindService.getUser(payload.getId());
            UserContext.USER_CONTEXT.set(new JwtPayload(user.getId()));
            return pjp.proceed();
        } catch (SignatureException | ExpiredJwtException | MalformedJwtException | UnsupportedJwtException | IllegalArgumentException e) {
            throw new JwtException();
        }
    }
}
```

코드 하나하나 의미를 보지는 않고 이러한 중복 로직이 있어서 AOP를 적용하였다 정도만 보면 좋을 거 같습니다. 위의 코드가 현재 프로젝트에서 적용하고 있는 인가 체크를 하는 코드입니다. 먼저 HTTP Header에서 AccessToken을 꺼낸 후, decode 해서 토큰에 문제가 있는지 없는지 체크합니다.
(JWT가 만료되었거나, 변조되었거나, 없거나 등등 상황에서는 401 에러를 반환합니다.)

이러한 인가 코드가 모든 API 마다 추가된다고 생각하면 정말.. 유지보수가 쉽지 않을 것입니다. (제가 하는 규모정도만 하더라도..) 하나만 수정되어도 모든 API를 수정해야 할 것입니다. 이러한 API 마다 중복으로 사용되는 로직은 
`공통 Aspect`로 빼서 AOP로 사용하면 코드도 간단해지고 유지보수도 쉽고 여러므로 장점이 많습니다. 

<br>

### `그러면 AOP는 어떻게 API에 적용할 수 있을까요?`

```
@Around("@annotation(com.yapp.ios1.annotation.Auth)")
```

메소드 위에 보면 @Around()가 존재하는 것을 볼 수 있는데요. @Around()은 Aspect의 실행 시점을 지정할 수 있는 적용할 범위를 지정할 수도 있고, 어노테이션과 연결시킬 수 있습니다. 저는 어노테이션을 사용할 것이기에 어노테이션 경로를 적었습니다. 
즉, 연결된 어노테이션이 붙어있는 곳은 AOP 코드가 실행되도록 하는 것입니다.

<br>

```java
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Auth {
}
```

어노테이션은 위와 같이 만들었는데요. `RetentionPolicy`는 `RUNTIME`으로만 해도 될 거 같아서 런타임으로 지정하였습니다. 

- RUNTIME: A라는 클래스를 빈으로 만들 때 A라는 타입의 프록시 빈을 감싸서 만든 후에, 프록시 빈이 클래스 중간에 코드를 추가해서 넣습니다.

<br>

### `API에 어노테이션 적용하기`

```java
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v2/users")
public class UserInfoController {

    private final UserInfoService userInfoService;
    
    @Auth  // AOP 어노테이션
    @GetMapping("/me")
    public ResponseEntity<ResponseDto> getMyInfo() {
        Long userId = UserContext.getCurrentUserId();
        return ResponseEntity.ok(ResponseDto.of(HttpStatus.OK, GET_MY_INFO, userInfoService.getUserInfo(userId)));
    }
}
```

`마이 페이지`를 조회하는 API 코드입니다. 여기서 보면 `@Auth` 어노테이션을 사용하고 있는 것을 볼 수 있습니다. 이것은 바로 위에서 만든 어노테이션 입니다.
위의 어노테이션만 추가하고 API 호출을 하면 어떻게 될까요?  

<br>

![스크린샷 2021-06-17 오전 12 14 34](https://user-images.githubusercontent.com/45676906/122245898-150ca800-cf01-11eb-8441-036ca886cb4c.png)

어노테이션 하나로 `Aspect` 코드가 동작하는 것을 확인할 수 있습니다. (참고로 위와 같이 `Header`에 JWT를 넣지 않으니 JWT가 없다는 응답이 오는 것인데요.)

즉, JWT가 없거나 만료되었거나 변조되었으면 401 에러로 응답이 간다고 이해하면 좋을 거 같습니다.

<br>

## `글을 마무리 하며`

현재 글에서는 AccessToken 일부만을 적었지만, 실제 프로젝트에서는 `AccessToken`,  `RefreshToken` 모두 AOP를 사용하여 중복 로직을 제거하고 있습니다. 이 글을 읽는 분들도 꼭 인가 관련이 아니더라도 자주 사용되는 로직이 있다면 `AOP`를 고려해보시면 좋을 거 같습니다.

AOP가 아니더라도 위에 그림에서 나왔던 `Filter`, `Interceptor`도 상황에 맞게 적절하게 잘 이용하시면 좀 더 깔끔하게 프로젝트 구조를 잡을 수 있을 거 같습니다.