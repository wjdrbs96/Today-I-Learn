# `들어가기 전에`

![1](https://user-images.githubusercontent.com/45676906/116671110-86d57480-a9db-11eb-98e8-00e944812410.png)

클라이언트가 Request를 보내면 Controller로 오기 전에 만나는 여러 관문들이 존재합니다. [DispatcherServlet](https://github.com/wjdrbs96/Gyunny_Spring_Study/blob/master/spring/3%EC%A3%BC%EC%B0%A8/DispatcherServlet%20%EC%9D%B4%EB%9E%80%3F.md) 에 대해서는 저번 글에서 정리했었습니다.
그리고 `Interceptor`, `AOP`가 존재하는데요... AOP는.. 어떤 것이었죠? Spring 삼각형 중에 하나였습니다. 아주 중요한 내용 중에 하나이지만 이번 글에서는 `Filter`와 `Interceptor`에 대해서 알아볼 것입니다.

<br>

# `Spring Filter란 무엇일까?`

Filter는 말 그대로 어떤 것을 걸러내는 역할을 하는데요. 그림에서 보면 `DisPatcherServlet` 앞에 `Filter`가 존재하는 것을 볼 수 있습니다.
즉, 클라이언트의 요청에 대해서 사전에 걸러내는 역할을 합니다. (ex: CORS, XSS 방어, 인코딩 변환 등등..?) 또한 Response를 줄 때도 변경 처리를 할 수 있다는 특징을 가지고 있습니다.

즉, 이름 그대로 `클라이언트 요청 맨 앞에서 Request, Response`에 대해서 처리하는 과정이라고 생각하면 편합니다. (집에 직접 접근 하기 전에 하나의 벽으로 미리 Filter 한다는 느낌이랄까..?)


```java
package javax.servlet;

import java.io.IOException;

public interface Filter {
    public default void init(FilterConfig filterConfig) throws ServletException {}
    
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain) throws IOException, ServletException;

    public default void destroy() {}
}
```

대표적으로 `Filter`라는 인터페이스가 존재하는데요. 인터페이스 내부에는 `init()`, `doFilter()`, `destroy()` 메소드가 존재합니다. 이름에서도 바로 알 수 있듯이 `초기회`, `Filter 설정..?`, `종료` 뭐 이런 메소드들이라고 추측할 수 있습니다.

- `init()`: 필터 인스턴스 초기화
- `doFilter()`: 전/후 처리
- `destroy()`: 필터 인스턴스 종료

<br>

간단하게 말하면 Filter는 Spring 영역에 들어오기 전에 Filter 작업을 하는 역할을 합니다. 즉, `DispatcherServlet`이 실행되기 전에 앞단에서 이름 그대로 어떤 것들을 필터링 하는 역할을 할 수 있습니다.

- 인코딩 작업
- XSS, CORS와 같이 보안 방어

예를들면 위와 같은 것들을 할 수 있습니다.

<br>

```java
@Component
public class FilterTest implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("Filter init");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        System.out.println("do Filter");
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        System.out.println("Filter Destroy");
    }
}
```

실제 사용 코드는 위와 같이 사용할 수 있는데요.

<br>

![스크린샷 2021-05-04 오전 11 43 44](https://user-images.githubusercontent.com/45676906/116955652-1b86ed80-acce-11eb-8b65-84ff0997fa5b.png)

그리고 하나의 API에 접근해보면 위와 같이 `init`, `doFilter`, `destory` 메소드 로그가 찍히는 것을 볼 수 있습니다. init은 한번의 초기화가 이루어지고, destory도 WAS가 종료될 때 실행됩니다.

`doFilter`는 요청이 들어올 때마다 실행이 되는 것도 확인할 수 있습니다. 즉, Filter를 위한 로직은 `doFilter`에 작성하면 됩니다.

그리고.. 맨 위의 그림을 보면 `Filter Chain`이라고 존재합니다. 그림에서도 알 수 있듯이 Filter는 하나만 구성할 수 있는 것이 아니고 여러 개로 구성할 수 있습니다.
그럴 때는 `chain.doFilter(requeset, respsonse)`를 이용하여 다음 필터로 연결시킬 수 있습니다. (이것을 `필터체인`이라고 합니다.)

<br> <br>

## `Interceptor란?`

![1](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=http%3A%2F%2Fcfile22.uf.tistory.com%2Fimage%2F9983FB455BB4E5D30C7E10)

저번에 `Filter`에 대해서 간단하게 알아본 적이 있었는데요. 이번에는 `Interceptor`에 대해서도 간단하게 알아보려고 합니다.

인터셉터는 그림에서 볼 수 있듯이 `스프링 영역` 안에서 실행되는데요. 즉, `DispatcherServlet`이 컨트롤러를 호출하기 전, 후로 끼어들 수 있는 메소드를 제공합니다.

```java
public interface HandlerInterceptor {

	default boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		return true;
	}
	
	default void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			@Nullable ModelAndView modelAndView) throws Exception {
	}

	default void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
			@Nullable Exception ex) throws Exception {
	}
}
```

가령 `HandlerInterceptor` 내부 메소드를 보면 `preHandle`, `postHandle`, `afterCompletion`가 존재합니다.

- `preHandler()`: Controller가 실행되기 전에 실행 됨
- `postHandle()`: Controller 메소드 실행 직 후에 실행 됨
- `afterompletion()`: view 페이지가 렌더링 되고 난 후에 실행

<br>

> 요청에 대한 작업 전/후로 가로챈다고 보면 됩니다.

> 필터는 스프링 컨텍스트 외부에 존재하여 스프링과 무관한 자원에 대해 동작합니다.
> 하지만 인터셉터는 스프링의 DistpatcherServlet이 컨트롤러를 호출하기 전, 후로 끼어들기 때문에 스프링 컨텍스트(Context, 영역) 내부에서 Controller(Handler)에 관한 요청과 응답에 대해 처리합니다.

<br>

# `Reference`

- [참고하면 좋은 글 - 1](https://goddaehee.tistory.com/154)
- [참고하면 좋은 글 - 2](https://supawer0728.github.io/2018/04/04/spring-filter-interceptor/)


