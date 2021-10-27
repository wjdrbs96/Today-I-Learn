# `Kotlin, Spring Security로 JWT 구현하기`

이번 글에서는 `Kotlin`, `Spring Security`를 사용해서 `JWT`를 프로젝트에서 사용하는 법에 대해서 정리해보겠습니다.    

<br> <br>

## `Spring Security`

> Spring Security is a powerful and highly customizable authentication and access-control framework. It is the de-facto standard for securing Spring-based applications.

> Spring Security is a framework that focuses on providing both authentication and authorization to Java applications. Like all Spring projects, the real power of Spring Security is found in how easily it can be extended to meet custom requirements

<br>

[Spring Security 공식문서](https://spring.io/projects/spring-security) 를 보면 위와 같이 설명하고 있습니다. 

> Spring Security는 강력하고 사용자 정의가 가능한 인증 및 액세스 제어 프레임워크입니다. 스프링 기반 응용 프로그램 보안을 위한 실질적인 표준입니다.

> Spring Security는 Java 애플리케이션에 인증과 권한 부여를 모두 제공하는 데 초점을 맞춘 프레임워크입니다. 모든 Spring 프로젝트와 마찬가지로 Spring Security의 진정한 장점은 맞춤형 요구 사항을 충족하도록 얼마나 쉽게 확장할 수 있느냐에 있습니다.

<br>

위의 영어를 해석해보면 위와 같습니다. 즉, 이번 글에서 `JWT`를 통한 `인증`, `인가` 처리에 관련한 글이다 보니 `Spring Security`를 사용해보려 합니다.

<br>

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