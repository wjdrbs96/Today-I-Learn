# `Spring MVC 프레임워크 동작 방식`

Spring Boot, Spring MVC가 엄청난 설정들을 대신 해주기에 편리하게 사용할 수 있는 것인데요. Spring MVC를 구성하는 주요 요소가 무엇이고 각 구성 요소들이 서로 어떻게 연결되는지 정도는 이해하면 좋을 것 같아서 이번 글에서 정리해보려 합니다. 

<br> <br>

## `Spring MVC 핵심 구성 요소`

![스크린샷 2021-10-22 오후 2 04 48](https://user-images.githubusercontent.com/45676906/138396170-fc427199-7e69-4f23-8673-0e87b6c771a5.png)

위의 그림에서 `<<spring bean>>` 이라고 되어 있는 것들은 `Spring Bean`으로 등록해야 하는 것을 의미합니다. 그리고 `분홍색`으로 칠해져 있는 것은 개발자가 직접 `Spring Bean`으로 등록해야 하는 것을 의미합니다. 예를들어, `@Controller`를 통해서 직접 Controller Class를 만든 후에 @Controller로 Bean으로 등록하는 것을 의미합니다. 

@Controller가 붙어 있을 때 실행되는 순서를 정리하면 아래와 같습니다. 

1. 제일 먼저 볼 것은 `DispatcherServlet` 인데요. 중앙에 위치한 `DispatcherServlet`은 모든 연결을 담당합니다. 먼저 클라이언트(브라우저)로 요청이 들어오면 그 요청을 처리할 Controller 객체를 검색합니다. 

2. 이 때 `DispatcherServlet`은 직접 검색하지 않고 `HandlerMapping`이라는 빈 객체에서 컨트롤러 검색을 요청합니다.

3. `DispatcherServlet`은 `HandlerMapping`이 찾아준 컨트롤러 객체를 처리할 수 있는 `HandlerAdapter` Bean에게 요청 처리를 위임합니다. 

4. 그리고 `HandlerAdapter`는 DispatcherServlet과 Handler 객체 사이의 변환을 알맞게 처리해 줍니다. 즉, 컨트롤러의 알맞은 메소드를 호출해서 요청합니다. 

5. 4번의 결과를 `DispatcherServlet`에게 return 합니다.

6. `HandlerAdapter`는 컨트롤러의 처리 결과를 `ModelAndView`라는 객체로 변환해서 `DispatcherServlet`에 return 합니다.

7. `HandlerAdapter`로 부터 컨트롤러의 요청 처리 결과를 `ModelAndView`로 받으면 `DispatcherServlet`은 결과를 보여줄 뷰를 찾기 위해 `ViewResolver` Bean 객체를 사용합니다.

8. 응답을 생성하기 위해 JSP를 사용하는 `ViewResolver`는 매번 새로운 View 객체를 생성해서 `DispatcherServlet`에 return 합니다.

<br> <br>

## `Spring MVC 디버깅 해보기`

```java
@RestController
public class HelloController {

    @GetMapping
    public String hello() {
        return "Gyunny Hello";
    }
}
```

위처럼 `@RestController`를 사용하는 컨트롤러 API에 접근할 때 내부적으로 어떤 일이 일어나는지 디버깅을 해보면서 알아보겠습니다.

<br> 

![스크린샷 2021-10-22 오후 11 13 42](https://user-images.githubusercontent.com/45676906/138469398-9012fbee-7c21-4b7f-8030-cea14d1162b1.png)

먼저 `DispatcherServlet`에 `doService` 메소드 첫 번째 줄에 `Break Point`를 찍어놓은 후에 디버그 모드로 실행하고, 위에서 작성한 `http://localhost:8080`에 요청을 보내보겠습니다. (참고로 디버거 모드에서 F7은 step into로 해당 메소드 내부 코드로 가는 것이고, F8은 다음 줄로 실행하는 것입니다.)

<br>

![스크린샷 2021-10-22 오후 11 24 01](https://user-images.githubusercontent.com/45676906/138471045-fc40028a-21f0-444d-a4e6-637200526433.png)

F8을 통해서 계속 다음으로 가다 보면 `doDispatch()` 메소드가 나오는데요. 여기서 `F7`을 통해서 해당 메소드 내부로 가보겠습니다. 

<br>

![스크린샷 2021-10-22 오후 11 25 41](https://user-images.githubusercontent.com/45676906/138471386-9ba7898c-169b-4b48-ab0c-002314963684.png)

메소드 내부 코드를 보면 `getHandler`가 보이는데요. 이름으로 추측해보면 위의 그림에서 보았던 `HandlerMapping`과 관련된 곳인 것 같습니다. 

<br>

![스크린샷 2021-10-22 오후 11 28 30](https://user-images.githubusercontent.com/45676906/138472144-b727eb1b-774b-4572-b4b7-bcdeedda0e96.png)

메소드 내부 코드를 보면 `HandlerMappings` 관련 코드가 보입니다.

<br>

![스크린샷 2021-10-22 오후 11 31 44](https://user-images.githubusercontent.com/45676906/138472321-aca071ad-29bc-4af8-9be6-12f447dac72d.png)

`HandlerMappings` 변수에 저희가 아무런 설정을 하지 않아도 위와 같이 6개의 `HandlerMapping`이 존재하는 것을 볼 수 있습니다. 어떤 핸들러가 사용되는지 보기 위해서 F8로 다음 코드로 계속 이동해보겠습니다. 

<br>

![스크린샷 2021-10-22 오후 11 33 10](https://user-images.githubusercontent.com/45676906/138472641-48a194df-18e3-4d56-aacf-a2db28664c44.png)

for문을 돌다보면 `RequestMappingHandlerMapping`이 사용된 후에 for문이 종료되는 것을 볼 수 있습니다. 이렇게 `HandlerMapping`을 통해서 컨트롤러 Bean 객체를 `DispatcherServlet`에게 전달했을 것입니다.

<br>

![스크린샷 2021-10-22 오후 11 36 25](https://user-images.githubusercontent.com/45676906/138473203-5cdfa883-3734-4628-9b4a-800d9643c5bf.png)

그리고 F8을 누르면서 다음으로 가보면 `getHandlerAdapter`를 통해서 위에서 찾은 `Handler`를 실행할 수 있는 `Adapter`를 찾아오는 것을 볼 수 있습니다. 

<br>

![스크린샷 2021-10-22 오후 11 40 43](https://user-images.githubusercontent.com/45676906/138473895-1fb11b5c-dd15-44ec-aa41-97af1ed1bd8d.png)

메소드 내부로 들어가게 되면 이번에도 for문을 통해서 사용할 Adapter를 찾는 과정이 진행되는데요.

<br>

![스크린샷 2021-10-22 오후 11 41 35](https://user-images.githubusercontent.com/45676906/138474005-8726efdb-ae2f-4fba-9bc8-354f5b6c69b9.png)

Adapter는 기본적으로 4개가 존재하는 것을 볼 수 있습니다.

<br>

![스크린샷 2021-10-22 오후 11 44 44](https://user-images.githubusercontent.com/45676906/138474712-da3045b7-7a93-45e6-9d7d-2ae857553771.png)

디버깅을 계속 진행해보면, `RequestMappingHandlerAdapter`가 선택되어 실행되는 것을 확인할 수 있습니다. 이제 Handler를 실행할 수 있는 `HandlerAdpater`를 찾았습니다. 

<br>

![스크린샷 2021-10-22 오후 11 50 53](https://user-images.githubusercontent.com/45676906/138475739-8e7f3602-a95b-4621-aac4-b41cb7e9e7e9.png)

그리고 위의 코드에서 `HandlerAdapter`를 이용해서 요청을 처리하게 되는데요. 좀 더 보기 위해서 F8을 통해서 다음으로 계속 간 후에, 위의 코드에서 다시 `F7`을 통해서 내부 코드로 들어가보겠습니다. 

<br>

![스크린샷 2021-10-22 오후 11 52 43](https://user-images.githubusercontent.com/45676906/138476080-cf66fee7-4ac8-4495-a44a-d8f24c8327c5.png)

여기서도 `handleInternal` 내부 코드로 가기 위해서 F7을 누르겠습니다. 

<br>

![스크린샷 2021-10-22 오후 11 53 53](https://user-images.githubusercontent.com/45676906/138476318-4cedc7b0-0014-4da8-9447-ba189d49f6ec.png)

그리고 F8을 통해서 다음으로 진행되는 코드를 보면 `invokeHandlerMethod`가 보입니다. 여기서 자바의 리플렉션을 사용해서 `Controller`의 메소드를 실행하게 되는 것입니다. 

<br>

![스크린샷 2021-10-22 오후 11 58 07](https://user-images.githubusercontent.com/45676906/138477267-28bcd1aa-a6f6-47d7-95fe-d49865e71ea5.png)

여기서 이미 `handlerMethod`에 실행하고자 하는 Controller Method가 존재하는 것을 볼 수 있습니다. 

<br>

![스크린샷 2021-10-23 오전 12 02 11](https://user-images.githubusercontent.com/45676906/138477658-86d05062-5f1c-499e-b74f-b41d56bb4612.png)

그리고 다음 F8을 통해서 이동하면 `Controller Method`로 이동하는 것을 볼 수 있습니다. 계속 다음으로 이동해보겠습니다. 

<br>

![스크린샷 2021-10-23 오전 12 04 35](https://user-images.githubusercontent.com/45676906/138478066-cd79f2e2-295b-4b31-b7c5-1bb4b08fae9b.png)

그리고 `return` 값이 예상할 수 있듯이 `String`인 것도 확인할 수 있습니다. 

<br>

![스크린샷 2021-10-23 오전 12 09 37](https://user-images.githubusercontent.com/45676906/138479032-d7c19388-f59f-44fc-a40e-27883304aa98.png)

그리고 F8을 누르면서 다음으로 이동하면 `returnValueHandlers.handleReturnValue`가 존재하는 것을 볼 수 있습니다. 현재 Controller는 @Controller를 사용하는 것이 아니라 @RestController를 사용하기 때문에 return 에는 여러가지 값이 들어갈 수 있습니다. 

<br>

![스크린샷 2021-10-23 오전 12 13 05](https://user-images.githubusercontent.com/45676906/138479556-2b050a76-0f9f-421f-a2b7-69521495ef55.png)

위의 이름이 `HandlerMethodReturnValueHandler`인 것을 보면 return 값의 타입에 따라 어떤 것을 선택할 지 정하는 것처럼 보이는데요. 이것도 디버깅을 해보면 `RequestResponseBodyMethodProcessor`이 사용됩니다.

<br>

![스크린샷 2021-10-23 오전 12 18 00](https://user-images.githubusercontent.com/45676906/138480463-81769175-f5c2-412d-acd2-8ab4d37b42cf.png)

그리고 계속 쭉 F8로 실행을 하다 보면 위의 코드까지 오게 되는데요. mv는 `ModelAndView` 타입의 변수인데, 위의 사진을 보면 `null`인 것을 알 수 있습니다. 이유는 현재는 `@Controller`가 아닌 `@RestController`를 사용하기 때문에 `view`를 찾는 과정이 필요 없기 때문입니다.  

이렇게 지금까지 `@RestController`가 존재하는 Controller에 요청이 오면 Spring MVC에 내부적으로 어떻게 동작하는지 디버거를 통해서 알아보았습니다. 간단하게? 본 것 같은데도 내부적으로 정말 복잡하게 이루어지는 것을 볼 수 있습니다. 

<br> <br>

## `@Controller 어노테이션으로 View를 찾는 경우 디버깅`

```java
@Controller
public class HelloController {

    @GetMapping
    public String hello() {
        return "login";
    }
}
```

이번에는 `@Controller` 어노테이션을 사용해서 `View`를 찾는 과정도 `Debug`를 통해서 알아보겠습니다. 위에서 정리한 내용과 비슷하기 때문에 차이점이 있는 부분만 정리해보겠습니다. 

<br>

![스크린샷 2021-10-23 오전 1 00 41](https://user-images.githubusercontent.com/45676906/138486766-188058a0-bd16-4fcb-b4f2-3f72a271b119.png)

똑같이 진행되다가 위에서 보았던 `HandlerMethodReturnValueHandler`를 보면 이번에는 `ViewNameMethodReturnValueHandler`가 사용되는 것을 볼 수 있습니다. 

<br>

![스크린샷 2021-10-23 오전 1 04 42](https://user-images.githubusercontent.com/45676906/138487435-61e68405-4205-471a-91b6-06d706aac4ab.png)

그리고 이번에는 `@Controller` 이기 때문에 `ModelAndView`에 제가 사용하는 `login`이라는 View 이름이 보이는 것을 볼 수 있습니다. 

<br>

![스크린샷 2021-10-23 오전 1 09 52](https://user-images.githubusercontent.com/45676906/138488031-4c2b6573-6898-4b83-a611-6b041c8a1120.png)

또한 `@Controller`이기 때문에 `ModelAndView`가 null이 아니라 값을 가지고 있는 것도 볼 수 있습니다. 

<br> 

![스크린샷 2021-10-23 오전 2 52 26](https://user-images.githubusercontent.com/45676906/138503862-4d0fee83-e109-442f-b100-7f9a8d9cacfe.png)

그리고 계속 디버깅을 해보면 위와 같이 `View`와 관련된 코드를 만날 수 있는데요. 여기서 뭔가 `ViewResolver`와 관련된 것이 있을 것 같아서 좀 더 자세히 보았습니다.

<br>

![스크린샷 2021-10-23 오전 3 06 56](https://user-images.githubusercontent.com/45676906/138504053-d59294da-7684-4ade-951f-e4f5650078c7.png)

그러면 위와 같이 `ViewResolver`가 사용되는 것을 볼 수 있습니다. 코드를 자세히 이해할 순 없지만.. 그래도 디버깅을 계속 돌려보았습니다.

<br>

![스크린샷 2021-10-23 오전 3 11 24](https://user-images.githubusercontent.com/45676906/138504160-9a79fc22-38b0-42d2-8462-37a485d3476f.png)

그러면 `ViewResolver` 인터페이스 구현체인 `ContentNegotiatingViewResolver`가 사용되면서 View를 찾는 과정을 볼 수 있습니다.

<br>

이렇게 까지 계속 디버깅을 통해서 따라가본 이유는 내부 코드를 완벽하게 이해할 순 없지만, 대략적으로 위에서 정리했던 내용대로 실행되는 것인지 알아보면서 전체적인 흐름을 정리하기 위해서 보았습니다. 

<br> <br>

# `Reference`

- [스프링 5 프로그래밍 입문(최범균)]()
- [백기선 Spring MVC 인프런 강의]()