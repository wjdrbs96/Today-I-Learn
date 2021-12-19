# `Security Form 인증 살펴보기`

이번 글은 `Kotlin` 기반으로 `Spring Security Form 인증`에 대해서 간단하게 정리해보겠습니다. 

<br>

## `build.gradle`

```
implementation("org.springframework.boot:spring-boot-starter-security")
```

`Spring Security`를 사용하기 위해서 위의 의존성을 추가하겠습니다. 

<br> <br>

## `Controller 만들기`

```kotlin
@RestController
class HelloController {

    @GetMapping("/info")
    fun info(): String {
        return "info"
    }

    @GetMapping("/dashboard")
    fun dashboard(principal: Principal): String {
        return "dashboard ${principal.name}"
    }
}
```

위와 같이 `/info`, `/dashboard` API를 만들었습니다. 

<br> 

![스크린샷 2021-11-19 오후 2 48 23](https://user-images.githubusercontent.com/45676906/142571898-5a6cb20c-d316-4d3d-a296-b806b6efcb5b.png)

<br>

![스크린샷 2021-11-19 오후 2 48 42](https://user-images.githubusercontent.com/45676906/142571924-010ea9f1-c400-4c73-b041-bdd23a45a08e.png)

그리고 하나씩 접속해보면 위와 같이 `info`는 잘 들어가지고 `dashboard`는 `login form`으로 `Redirect` 된 것을 볼 수 있습니다. 

<br>

```kotlin
@Configuration
class WebSecurityConfig : WebSecurityConfigurerAdapter() {

    override fun configure(http: HttpSecurity) {
        http.authorizeRequests()
                .mvcMatchers("/", "/info").permitAll()
                .mvcMatchers("/admin").hasRole("ADMIN")
                .anyRequest().authenticated()   // 나머지 요청은 인증을 해야 접근이 가능 (ex: dashboard)
        http.formLogin()
        http.httpBasic()
    }

}
```

`info`가 로그인 폼으로 리다이렉트 하지 않았던 것은 위와 같이 `Security Config`에서 `info`는 `permitAll()`을 해주었기 때문입니다. 쉽게 예측할 수 있죠~?
