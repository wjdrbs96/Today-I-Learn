## `WebSecurityConfigurerAdapter Deprecated 해결하기`

<img width="905" alt="스크린샷 2022-06-28 오후 11 50 56" src="https://media.oss.navercorp.com/user/30855/files/a7b825a1-1a6a-4b63-8d5e-82530d5e65c2">

최근에 `Spring Security`를 설정해보려고 `WebSecurityConfigurerAdapter`를 사용하려 보니 `Deprecated`가 되어 있었는데요.

<br>

```java
@RequiredArgsConstructor
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final ObjectMapper objectMapper;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().mvcMatchers(
                "/error",
                "/favicon.ico",
                "/swagger-ui.html",
                "/swagger/**",
                "/swagger-resources/**",
                "/webjars/**",
                "/v2/api-docs"
        );

        web.ignoring().antMatchers(
                "/api/v2//login/**"
        );
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.antMatcher("/**")
                .authorizeRequests()
                .antMatchers("/api/v2/**").hasAuthority(USER.name());

        http.httpBasic().disable()
            .formLogin().disable()
            .cors().disable()
            .csrf().disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .anyRequest().permitAll()
                .and()
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        http.exceptionHandling()
                .authenticationEntryPoint(((request, response, authException) -> {
                    response.setStatus(HttpStatus.UNAUTHORIZED.value());
                    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                    objectMapper.writeValue(
                            response.getOutputStream(),
                            ExceptionResponse.of(ExceptionCode.FAIL_AUTHENTICATION)
                    );
                }))
                .accessDeniedHandler(((request, response, accessDeniedException) -> {
                    response.setStatus(HttpStatus.FORBIDDEN.value());
                    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                    objectMapper.writeValue(
                            response.getOutputStream(),
                            ExceptionResponse.of(ExceptionCode.FAIL_AUTHORIZATION)
                    );
                }));
    }
}
```

위와 같은 많은 설정들을 `WebSecurityConfigurerAdapter`를 `extends` 해서 `configure` 메소드를 오버라이딩 해서 구현했었습니다.

하지만 이제 `WebSecurityConfigurerAdapter`가 `Deprecated`가 되어서 사용할 수 없다보니 다른 것을 사용해서 구현해야 합니다.

<br>

<img width="642" alt="스크린샷 2022-06-29 오전 12 02 10" src="https://user-images.githubusercontent.com/45676906/176213158-1b9d1ed1-978a-4813-822f-4d3794cc4151.png">

`WebSecurityConfigurerAdapter` [공식문서](https://docs.spring.io/spring-security/site/docs/current/api/org/springframework/security/config/annotation/web/configuration/WebSecurityConfigurerAdapter.html)를 보면 위와 같이 나와 있습니다.

```
Deprecated.
Use a SecurityFilterChain Bean to configure HttpSecurity or a WebSecurityCustomizer Bean to configure WebSecurity
```

요약하면 `WebSecurityConfigurerAdapter`가 `Deprecated` 되었으니 `SecurityFilterChain`를 `Bean`으로 등록해서 사용해라 입니다.

<br>

<img width="595" alt="스크린샷 2022-06-29 오전 12 02 34" src="https://user-images.githubusercontent.com/45676906/176213264-402b10e6-4312-4b37-8a58-53f766157aeb.png">

<br>

<img width="606" alt="스크린샷 2022-06-29 오전 12 02 42" src="https://user-images.githubusercontent.com/45676906/176213292-da04c6d5-4637-4dee-b70a-a63b14c90730.png">

위의 내용만 보면 감이 잘 오지 않아서 [Spring Security 공식문서](https://spring.io/blog/2022/02/21/spring-security-without-the-websecurityconfigureradapter)를 보면 어떻게 설정해야 하는지 나와있습니다. 

<br>

### `기존 코드의 설정을 바꿔보면?`

```java
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {

    private final ObjectMapper objectMapper;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public WebSecurityCustomizer configure() {
        return (web) -> web.ignoring().mvcMatchers(
                "/v3/api-docs/**",
                "/swagger-ui/**",
                "/api/v1/login" // 임시
        );
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http.antMatcher("/**")
                .authorizeRequests()
                .antMatchers("/api/v1/**").hasAuthority(USER.name())
                        .and()
                .httpBasic().disable()
                .formLogin().disable()
                .cors().disable()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .anyRequest().permitAll()
                .and()
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)

                .exceptionHandling()
                    .authenticationEntryPoint(((request, response, authException) -> {
                        response.setStatus(HttpStatus.UNAUTHORIZED.value());
                        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                        objectMapper.writeValue(
                                response.getOutputStream(),
                                ExceptionResponse.of(ExceptionCode.FAIL_AUTHENTICATION)
                        );
                }))
                    .accessDeniedHandler(((request, response, accessDeniedException) -> {
                        response.setStatus(HttpStatus.FORBIDDEN.value());
                        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                        objectMapper.writeValue(
                                response.getOutputStream(),
                                ExceptionResponse.of(ExceptionCode.FAIL_AUTHORIZATION)
                        );
                    })).and().build();
    }
}
```

기존에 위에 있던 설정들을 최신 Security에 맞게 바꿔보면 위와 같이 바꿀 수 있습니다. 

<br>

## `Reference`

- [https://docs.spring.io/spring-security/site/docs/current/api/org/springframework/security/config/annotation/web/configuration/WebSecurityConfigurerAdapter.html](https://docs.spring.io/spring-security/site/docs/current/api/org/springframework/security/config/annotation/web/configuration/WebSecurityConfigurerAdapter.html)
- [https://spring.io/blog/2022/02/21/spring-security-without-the-websecurityconfigureradapter](https://spring.io/blog/2022/02/21/spring-security-without-the-websecurityconfigureradapter)