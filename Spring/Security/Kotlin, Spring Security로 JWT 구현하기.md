# `Kotlin, Spring Security로 JWT 구현하기`

이번 글에서는 `Kotlin`, `Spring Security`를 사용해서 `JWT`를 프로젝트에서 사용하는 법에 대해서 정리해보겠습니다.    

<br> <br>

![filter](https://user-images.githubusercontent.com/45676906/122227178-a1af6a00-cef1-11eb-8c22-23cbcb43bc03.png)

`Spring Security`는 `DispatcherSevlet` 앞에 `Filter`를 등록시켜 요청을 가로챕니다. 클라이언트에게 리소스 접근 권한이 없을 경우 로그인 화면으로 리다이렉트 합니다. 

<br> <br>

## `Spring Security Filter`

<img width="618" alt="스크린샷 2021-10-26 오전 1 01 39" src="https://user-images.githubusercontent.com/45676906/138730344-cf47f4ae-7c35-4437-bd58-abab606abecd.png">

Spring Security는 위와 같이 매우 많은 `Filter`를 가지고 있습니다. 

> Reference: [Spring Security Filter 내용 출처](https://daddyprogrammer.org/post/636/springboot2-springsecurity-authentication-authorization/)

<br> <br>

```
implementation("io.jsonwebtoken:jjwt:0.9.1")
```

> 이 Filter를 조금 더 확장하여 스프링에서 제공하는 필터가 있는데 그것이 바로 GenericFilterBean이다. GenericFilterBean은 기존 Filter에서 얻어올 수 없는 정보였던 Spring의 설정 정보를 가져올 수 있게 확장된 추상 클래스이다.

<br>

![스크린샷 2021-11-19 오후 2 06 24](https://user-images.githubusercontent.com/45676906/142568183-61d14654-006f-4672-a1d5-d360374eb254.png)


<br> <br>

`addFilterBefore` : 지정된 필터 앞에 커스텀 필터를 추가 (UsernamePasswordAuthenticationFilter 보다 먼저 실행된다)

<br>

![image](https://user-images.githubusercontent.com/45676906/142459876-aa99e5fc-fae0-4281-bb47-9fb6c3a5f10f.png)

<br> <br>

## `UsernamePasswordAuthenticationFilter`

