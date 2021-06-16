# `들어가기 전에`

이 글을 쓰는 이유는 현재 [프로젝트](https://github.com/YAPP-18th/iOS1_Backend) 를 진행하면서 `인가` 관련 로직은 대부분의 API에서 사용되기 때문에 AOP를 사용하여 중복 로직을 제거할 수 있었습니다. 
그 과정에서 어떤 것을 깨달았고 어떻게 하였는지를 정리하면 좋을 거 같아서 한번 정리를 해보려 합니다.

먼저 `인증`, `인가`의 차이는 [여기](https://www.youtube.com/watch?v=y0xMXlOAfss) 를 참고하면 좋을 거 같습니다. 

<br>

## `인증(Authentication)`

어떤 A라는 건물에 출입을 할 때, 출입증이 있다면 들어갈 수 있고 없다면 들어갈 수 없다. 이렇게 `식별가능한 정보로` 서비스에 등록된 유저의 신원을 입증하는 과정을 `인증`이라 합니다.


## `인가(Authorization)`

`출입증`으로 회사 건물의 모든 곳을 다 돌아다닐 수 있는 것은 아닙니다. (ex: A라는 10층짜리 건물에 내가 다니는 회사는 5층 한 층만 사용한다면, 나머지 층에는 출입을 할 수 없습니다.)
이러한 것을 `인가`라고 한다. 한마디로 `권한에 대한 허가`를 나타내고, `인증된 사용자에 대한 자원 접근 권한 확인`이다.

`글을 수정, 삭제` 할 수 있는 `권한`을 대표적으로 얘기할 수 있습니다. 

<br>

![스크린샷 2021-06-16 오후 10 54 09](https://user-images.githubusercontent.com/45676906/122231887-c6a5dc00-cef5-11eb-95d6-74e558684227.png)

제가 진행하고 있는 프로젝트에서는 위와 같이 `로그인, 회원가입`을 성공적으로 했을 때 `AccessToken`, `RefreshToken`을 발급해줍니다. (만료시간, 재발급 등등은 다루지 않습니다..) 

그리고 발급 받은 AccessToken으로 서버 API를 요청하도록 되어 있는데요. 프로젝트 거의 대부분 API에 `인가` 체크가 필요했습니다. (예를들어, `마이페이지 조회`의 경우도 해당 유저만 볼 수 있어야 하기 때문에 `인가` 처리를 하고 있습니다.)

여기서 하나 문제점이 존재한다고 생각했습니다. 만약 API가 100개이고, 그 중에서 95개의 API에 `인가 체크`가 필요하다면 모든 API에 `중복된 로직`을 작성해야 할 것입니다. 그래서 이러한 중복 로직을 제거하기 위해서 `Spring AOP`를 생각했습니다. 

<br>

# `Spring AOP란?`

![스크린샷 2021-06-16 오후 10 24 30](https://user-images.githubusercontent.com/45676906/122227178-a1af6a00-cef1-11eb-8c22-23cbcb43bc03.png)

Spring을 크게 보면 위와 같은 그림으로 표현할 수 있습니다. Controller 바로 앞단에 보면 `AOP(Aspect Oriented Programming)`이 존재하는 것을 볼 수 있습니다. AOP는 `관점 지향 프로그래밍` 이라고 불리는데요. 

관점지향..? 뭔소리인가 싶습니다. 그래서 조금 더 알아보면서 이해해보겠습니다. 

<br>

![aop](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FbJOHOE%2FbtqF0QCJlCc%2F2sTwCFrVK71VUGTCK6Hkl0%2Fimg.png)

위의 A, B, C 클래스에서 동일한 색깔의 선들의 의미는 클래스들에 나타나는 비슷한(중복되는) 메소드, 필드, 코드들이 나타난다는 것입니다. 이러한 경우 만약 클래스 A에 주황색 부분을 수정해야 한다면 B, C 클래스들에 주황색 부분에 해당하는 곳을 찾아가 전부 코드를 수정해야 합니다. 
(지금은 예시가 몇 개 안되니 그렇지 엄청나게 많다면.. 유지보수가 쉽지 않을 것입니다.) 이런식으로 `반복되는 코드를 흩어진 관심사 (Crosscutting Concerns)`라 부른다.

이러한 문제를 `AOP는 Aspect를 이용해서 해결`합니다. 사진 아래쪽을 보면 알 수 있는데, 흩어져 있는 부분들을 Aspect를 이용해서 모듈화 시킵니다. (모듈화란 어떤 공통된 로직이나 기능을 하나의 단위로 묶는 것을 말합니다.) 그리고 개발자가 모듈화 시킨 Aspect를 사진에서 위에 클래스에 어느 곳에 사용해야 하는지만 정의해주면 됩니다.

즉, `결론적으로 Aspect로 모듈화하고 핵심적인 비즈니스 로직에서 분리하여 재사용하겠다는 것이 AOP의 취지입니다.` AOP에 대해서 좀 더 자세히 알고 싶다면 [여기](https://devlog-wjdrbs96.tistory.com/178?category=882236) 를 참고하시면 될 거 같습니다. 

<br> <br> 

## `Spring AOP로 중복 로직 제거하기`

먼저 `Aspect`를 만들어보겠습니다. 

```java
@RequiredArgsConstructor 
@Aspect     // AOP Aspect
@Component  // 빈으로 등록
public class AuthAspect {

    private static final String AUTHORIZATION = "accessToken";

    private final JwtService jwtService;
    private final UserFindService userFindService;
    private final HttpServletRequest httpServletRequest;

    @Around("@annotation(com.yapp.ios1.annotation.Auth)")
    public Object accessToken(final ProceedingJoinPoint pjp) throws Throwable {
        try {
            String accessToken = httpServletRequest.getHeader(AUTHORIZATION);
            JwtPayload payload = jwtService.getPayload(accessToken);
            User user = userFindService.getUser(payload.getId());
            UserContext.USER_CONTEXT.set(new JwtPayload(user.getId()));
            return pjp.proceed();
        } catch (SignatureException | ExpiredJwtException | MalformedJwtException | UnsupportedJwtException | IllegalArgumentException e) {
            throw new JwtException();
        }
    }
}
```

코드에 대해서 자세히 설명하지는 않고 간단하게만 보겠습니다. 위의 코드가 중복되는 코드인데요. AccessToken을 decode 해서 토큰에 문제가 있는지 없는지 체크합니다. 또한 jwt에 존재하는 userId로 해당 유저가 존재하는지도 체크하고 있습니다. 

이 코드 하나로만 설명할 수는 없지만 저는 프로젝트에서 위 코드와 같이 `인가 체크`를 하고 있습니다. 이러한 코드가 모든 API 마다 추가된다고 생각하면 정말.. 끔찍할 거 같습니다. (제가 하는 규모정도만 하더라도..)

<br>

```
@Around("@annotation(com.yapp.ios1.annotation.Auth)")
```

@Around()은 Aspect의 실행 시점을 지정할 수 있는 어노테이션을 이용해서 적용할 범위를 지정해줍니다. 즉, 위의 연결된 어노테이션이 붙어있는 곳은 AOP 코드가 실행되도록 하는 것입니다.

<br>

```java
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Auth {
}
```

어노테이션은 위와 같이 만들었는데요. `RetentionPolicy`로 `RUNTIME`으로만 해도 될 거 같아서 런타임으로 지정하였습니다. 

- RUNTIME: A라는 클래스를 빈으로 만들 때 A라는 타입의 프록시 빈을 감싸서 만든 후에, 프록시 빈이 클래스 중간에 코드를 추가해서 넣습니다.

<br>

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
위의 어노테이션만 넣었음에도 `Aspect` 모듈 코드가 실행되는 것을 확인할 수 있습니다. 

<br>

![스크린샷 2021-06-17 오전 12 14 34](https://user-images.githubusercontent.com/45676906/122245898-150ca800-cf01-11eb-8441-036ca886cb4c.png)

위와 같이 `Header`에 JWT를 넣지 않으니 JWT가 없다는 응답이 오는 것인데요. 어노테이션 하나로 `Aspect` 코드가 동작하는 것을 확인할 수 있습니다. (JWT나 Error Response 내부 코드에 대해서 자세히 설명하지 않았지만..)

즉, JWT가 없거나 만료되었거나 변조되었으면 401 에러로 응답이 간다고 이해하면 좋을 거 같습니다