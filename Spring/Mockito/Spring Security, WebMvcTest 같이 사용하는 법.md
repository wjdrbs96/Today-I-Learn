## `Spring Security, @WebMvcTest 같이 사용하는 법`

이번 글에서는 진행하고 있는 프로젝트에서 `Spring AOP`에서 `Spring Security`로 바꾸면서 `@WebMvcTest` 테스트 이슈를 해결하는 과정에 대해서 아주 간단하게 정리해보려 합니다. 

![스크린샷 2021-12-16 오후 8 11 12](https://user-images.githubusercontent.com/45676906/146361277-f477e3c1-ffab-428f-947c-eb3078fbd333.png)

기존에 위의 사진처럼 `@WebMvcTest` 어노테이션을 사용해서 `Spring MVC Bean`만 등록하고 `Controller` 슬라이싱 테스트를 진행하였습니다. 

<br>

![스크린샷 2021-12-16 오후 8 12 30](https://user-images.githubusercontent.com/45676906/146361519-0093f46c-0dbd-4059-86c0-f1f6cb5ace8d.png)

그런데 `Spring Security`로 `인증/인가` 처리를 한 후에 테스트를 돌리니 위와 같은 에러가 발생했습니다. 아무래도 `Controller`를 진입하면서 `Servlet Filter`, `Interceptor` 관려해서 문제가 생기는 것이라 추측할 수 있는데요.

<br>

![스크린샷 2021-12-16 오후 8 15 46](https://user-images.githubusercontent.com/45676906/146361884-d1bc6d17-7d44-4fbc-b2bb-cadca2d58600.png)

그래서 위와 같이 `@ComponentScan` 설정에서 Security 설정 파일인 `WebSecurityConfig`를 제외시켜야 `Security` 설정을 제외하고 실행할 수 있습니다.

<br>

![스크린샷 2021-12-16 오후 8 22 03](https://user-images.githubusercontent.com/45676906/146362790-f0062db3-77fc-4865-ad5f-4e3422c1d853.png)

그리고 실행하면 이번에는 `loginUserIdArgumentResolver`가 `Bean` 주입이 안된다고 에러가 납니다. 에러가 나는 이유는 제가 `LoginUserIdArgumentResolver` 클래스를 사용하고 있어서 그런데요.

<br>

![스크린샷 2021-12-16 오후 8 24 03](https://user-images.githubusercontent.com/45676906/146363118-f4829ad9-9198-4988-819c-51d209c4b434.png)

위와 같이 `@MockBean`을 사용해서 주입해주면 에러를 해결할 수 있습니다. 

<br>

![스크린샷 2021-12-16 오후 8 25 09](https://user-images.githubusercontent.com/45676906/146363281-8ada3481-a16e-492b-b241-f1268083de32.png)

그랬더니 또 다음 에러가 기다리고 있습니다. 현재 `Spring Security`를 사용하고 있기 때문에 `Controller`를 호출하기 전에 `Security`에서 걸려서 `403 권한 에러`가 발생하는 것입니다. 

<br>

![스크린샷 2021-12-16 오후 8 28 11](https://user-images.githubusercontent.com/45676906/146363672-4d984b17-3777-43a3-b360-5c8d53f60b8a.png)

그래서 이번에 `@BeforeEach`를 사용해서 `CharacterEncodingFilter`를 설정해주면 테스트가 정상적으로 실행됩니다.