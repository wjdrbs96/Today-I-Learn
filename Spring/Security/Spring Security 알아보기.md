# `Spring Security 인증 알아보기`

- [인증(Authentication), 인가(Authorization)]()
- [Spring Security 구조]()
    - [Security Config]()
    - [DelegatingFilterProxy]()
    - [FilterChainProxy]()
    - [Filter Chain]()
    - [Authentication Architecture]()
    - [AuthenticationToken]()
    - [SecurityContextHolder]()
    - [Authentication]()
    - [CustomToken 인증 방식]()
    - [Handling Security Exceptions]()
    - [@AuthenticationPrincipal]()
- [마무리]()

<br>

## `인증(Authentication), 인가(Authorization)`

- `인증(Authentication)`: 사용자가 누구인지 확인하는 과정
- `인가(Authorization)`: 사용자가 특정 자원에 접근할 수 있는 권한을 부여받는 과정

여기서는 인증에 대해서만 정리하고자 함

<br>

## `Spring Security 구조`

![image](https://github.com/user-attachments/assets/595c1493-e60c-45b7-927f-460a5d514d1d)

<br>

### `Security Config`

![image](https://github.com/user-attachments/assets/21aded14-4ec0-418b-b850-4d80f8b10cdd)

- 현재는 `WebSecurityConfigurerAdapter Deprecated` ([참고 Link](https://spring.io/blog/2022/02/21/spring-security-without-the-websecurityconfigureradapter))

```java
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests() 
				.anyRequest().authenticated()  // 리소스(URL) 접근 권한 설정 
			.and()
				.httpBasic().and()
				.formLogin().disable() // 커스텀 로그인 페이지 지원
				.logout().disable()    // 사용자 로그아웃
				.csrf().disable()      // CSRF 설정
			.and()
				.addFilterBefore(filter(), UsernamePasswordAuthenticationFilter.class)      // 필터 위치 지정
				.exceptionHandling(exceptions ->
					exceptions.authenticationEntryPoint(new TestAuthenticationEntryPoint()) // 인증 후 성공/실패 핸들링
				);
	}
```

- 리소스(URL) 접근 권한 설정
- 커스텀 로그인 페이지 지원
- 사용자 로그아웃
- CSRF 설정
- 필터 위치 지정
- 인증 후 성공/실패 핸들링

=> 여기서 기존에 미리 등록되어 있던 Filter와 직접 등록한 필터들의 목록들이 FilterChainProxy로 전달되는 것 같음

<br>

### `DelegatingFilterProxy`

DelegatingFilterProxy는 Servlet 필터이며, Spring ApplicationContext에서 관리되는 특정 필터 Bean을 서블릿 필터로 등록하고 실행함

- DelegatingFilterProxy는 Servlet 컨테이너에 직접 등록되어 Filter 역할을 수행하지만, 실제 필터링 로직은 Spring 컨텍스트의 Bean으로 구현된 필터에게 위임한다.
- 서블릿 컨테이너는 Spring에서 관리하는 필터 Bean(ex: FilterChainProxy와)을 간접적으로 실행할 수 있음

![image](https://github.com/user-attachments/assets/c778a7cc-3207-446d-8f89-de6964967d43)

<br>

### `FilterChainProxy`

![image](https://github.com/user-attachments/assets/a08a24df-d49e-4b88-b96f-6c69436d2d0e)

- FilterChainProxy는 Spring Security의 모든 필터 체인을 관리하고 실행하는 클래스
- FilterChainProxy는 Spring Context 영역이라 DI도 가능하고, Spring 기능을 다 사용할 수 있는 것 같음

<br>

### `Filter Chain`

![image](https://github.com/user-attachments/assets/d6ae7a75-1a52-4da4-8848-aed83c7fb3c3)

1. `SecurityContextPersistenceFilter`: 해당 필터에서 SecurityContextHolder를 생성하고 삭제하는 작업까지 해주기 때문에 사용자가 따로 해야 할 것이 없음 ([참고 Link](https://github.com/spring-projects/spring-security/blob/main/web/src/main/java/org/springframework/security/web/context/SecurityContextPersistenceFilter.java#L122))
2. LogoutFilter: 로그아웃에 대한 처리를 담당하는 필터 ([참고 Link](https://github.com/spring-projects/spring-security/blob/main/web/src/main/java/org/springframework/security/web/authentication/logout/LogoutFilter.java#L96))
3. `UsernamePasswordAuthenticationFilter` ([참고 Link](https://github.com/spring-projects/spring-security/blob/main/web/src/main/java/org/springframework/security/web/authentication/UsernamePasswordAuthenticationFilter.java#L74))
    - ID와 Password를 사용하는 실제 Form 기반 유저 인증을 처리
    - `실제 인증이 진행되는 필터` (다른 인증 방식의 커스텀 필터 적용 가능)
4. ConcurrentSessionFilter: 현재 사용자 계정으로 인증을 받은 사용자가 두 명 이상일 때 실행되는 필터 ([참고 Link](https://github.com/spring-projects/spring-security/blob/main/web/src/main/java/org/springframework/security/web/session/ConcurrentSessionFilter.java#L127))
5. RememberMeAuthenticationFilter: 세션이 사라지거나 만료 되더라도, 쿠키 또는 DB를 사용하여 저장된 토큰 기반으로 인증을 처리
6. AnonymousAuthenticationFilter: 사용자 정보가 인증되지 않았다면 익명 사용자 토큰을 반환 ([참고 Link](https://github.com/spring-projects/spring-security/blob/main/web/src/main/java/org/springframework/security/web/authentication/AnonymousAuthenticationFilter.java#L97))
7. SessionManagementFilter: 로그인 후 세션과 관련된 작업을 처리 ([참고 Link](https://github.com/spring-projects/spring-security/blob/main/web/src/main/java/org/springframework/security/web/session/SessionManagementFilter.java#L89))
8. `ExceptionTranslationFilter`: 필터 체인 내에서 발생되는 인증, 인가 예외를 처리 ([참고 Link](https://github.com/spring-projects/spring-security/blob/main/web/src/main/java/org/springframework/security/web/access/ExceptionTranslationFilter.java#L126))
9. FilterSecurityInterceptor: 권한 부여와 관련한 결정을 AccessDecisionManager에게 위임해 권한부여 결정 및 접근 제어 처리

<br>

### `Authentication Architecture`

![image](https://github.com/user-attachments/assets/5e4da2ba-fcd7-4809-82fb-0128228cc8ff)

1. AbstractAuthenticationProcessingFilter 인증 Authentication 생성
    - UsernamePasswordAuthenticationFilter.attemptAuthentication 호출
2. AuthenticationManager (ProviderManager)
    - ProviderManager -> AuthenticationProvider 위임
3. AuthenticationProvider
    - 인증 로직 진행 (UserDetailsService.loadUserByUsername 호출)
4. AbstractAuthenticationProcessingFilter Authentication 객체 반환
5. 인증 성공시 SecurityContextHolder 저장

<br>

### `AuthenticationToken`

```java
public abstract class AbstractAuthenticationToken implements Authentication, CredentialsContainer {}
```

Spring Security 에서 AbstractAuthenticationToken 하위 클래스로 기본적으로 제공해주는 토큰이 존재함

![image](https://github.com/user-attachments/assets/478986aa-c2fb-43f1-a639-79e4184b118f)

- 기본으로 제공하는 것 이외에 다른 인증 방식을 사용한다면 자신의 서비스에 맞게 AbstractAuthenticationToken 하위 클래스로 `AuthenticationToken` 클래스를 커스텀해서 만들어서 사용할 수 있음

<br>

### `SecurityContextHolder`

![image](https://github.com/user-attachments/assets/fec8511f-366c-4804-ad90-9b4aa1854021)

- Spring Security에서 인증된 사용자의 인증 정보를 저장하는 곳
- SecurityContextHolder에 값이 저장되어 있으면 현재 인증된 사용자로 사용됨

```kotlin
val authentication = TestingAuthenticationToken("생략");
SecurityContextHolder.getContext().authentication = authentication
```

<br>

### `Authentication`

- 현재 인증된 사용자를 나타내고, 인증이 완료되면 principal(인증 객체), 권한 정보 등등 담고 SecurityContext에 저장하여 전역적으로 사용 가능
- 사용자의 인증 정보를 저장하는 토큰의 개념

<br>

### `CustomToken 인증 방식`

> UsernamePasswordAuthenticationToken는 id, password 기반으로 동작할 때 사용하는데 현재 인증 방식에서는 id, password 방식이 아니라면?

위에서 보았던 미리 정의되어 있는 토큰이 적합하지 않아서 Custom 하게 인증을 구현해야할 때가 있음

- AbstractAuthenticationToken 하위 CustomToken 생성
- AbstractAuthenticationProcessingFilter 구현한 Filter 생성
- AuthenticationProvider 커스텀 클래스로 인증 로직 재정의

<br>

### `Handling Security Exceptions`

![image](https://github.com/user-attachments/assets/1e19e813-8f62-4067-8cbe-29ede55d109d)

- Spring Security에서 인증 예외(AuthenticationException), 인가 예외(AccessDeniedException) 두 가지가 존재함 (`ExceptionTranslationFilter 참고`)
- `Spring Security Exception은 DispatcherServlet 동작하기 전에 동작하기 떄문에 @ControllerAdvice 어노테이션이 동작하지 않음`

<br>

### `@AuthenticationPrincipal`

```kotlin
@RestController
class TestController {
    @GetMapping("/test")
    fun test(@AuthenticationPrincipal testAuthentication: TestAuthentication) {}
}
```

@AuthenticationPrincipal 어노테이션 통해서 SecurityContextHolder에 넣은 인증 객체를 꺼내올 수 있음

<br>

## `마무리`

- Spring Security 인증 동작 방식에 대해서는 한번쯤 봐도 좋은듯 함
- Servlet Filter, Spring Container에 대해서 명확하게 알고 있어야 할 듯

<br>

## `Referenece`

- [https://docs.spring.io/spring-security/reference/servlet/authentication/architecture.html](https://docs.spring.io/spring-security/reference/servlet/authentication/architecture.html)
- [https://docs.spring.io/spring-security/reference/servlet/architecture.html](https://docs.spring.io/spring-security/reference/servlet/architecture.html)
- [https://docs.spring.io/spring-security/reference/servlet/exploits/index.html#servlet-exploits](https://docs.spring.io/spring-security/reference/servlet/exploits/index.html#servlet-exploits)
- [https://spring.io/blog/2022/02/21/spring-security-without-the-websecurityconfigureradapter](https://spring.io/blog/2022/02/21/spring-security-without-the-websecurityconfigureradapter)
- https://docs.spring.io/spring-security/reference/servlet/authorization/method-security.html
- [https://gngsn.tistory.com/160](https://gngsn.tistory.com/160)