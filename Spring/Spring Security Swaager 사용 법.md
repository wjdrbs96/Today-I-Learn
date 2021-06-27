# `Spring Security Swagger 에러 해결`

이번 글에서는 Spring Boot에서 Security와 Swagger를 사용하면서 만난 문제를 해결한 과정에 대해서 정리해보려 합니다.

프로젝트를 진행하면서 특정 URI로 시작하는 것이 아니라면 시큐리티 단에서 걸러내는 작업을 해보고 싶어서 `Spring Security`를 아주 간단하게만 적용을 해보고자 하였습니다. 
하지만 적용을 하다 보니.. `Swagger` 관련해서 문제점이 생겼는데요. 그 부분에 대해서 간단하게 정리해보겠습니다. 

<br>

## `Spring Security는 어떻게 적용했을까?`

![스크린샷 2021-06-27 오후 7 53 46](https://user-images.githubusercontent.com/45676906/123541848-64649b00-d781-11eb-841b-0fc1a72cdd4c.png)

처음에는 `Spring Security`는 `비밀번호 단방향 암호화`를 하는데만 사용했기 때문에 위의 설정정도로만 아주 간단하게 했습니다. 

<br>

![스크린샷 2021-06-27 오후 8 01 23](https://user-images.githubusercontent.com/45676906/123542051-74c94580-d782-11eb-954e-4d32aed44450.png)

그래서 위와 같이 `/api/v2`로 시작하는 API, `로드밸런서 Health 체크용 API`, `Swagger 접속 주소`정도만 `permialAll()`을 하고 나머지는 `Authenticated`가 필요하도록 설계를 하였습니다. (별거 없구만..? 이라고 생각..)
하지만 위와 같이 설정을 하면 아쉽게도.. 한 가지 문제점이 존재합니다.

<br>

![스크린샷 2021-06-27 오후 8 00 48](https://user-images.githubusercontent.com/45676906/123542066-87437f00-d782-11eb-930d-e3b2582aefde.png)

`{{base_url}}/swagger-ui.html`으로 접속 했을 때 위와 같이 `로그인`을 해야 한다는 창이 뜬다는 것이었습니다. 그래서 좀 더 찾아보니까 `Swagger UI`에 접속하기 위해서는 다른 것들도 `PermitAll()`을 해주어야 한다는 것이었는데요. 

<br>

![스크린샷 2021-06-27 오후 8 03 24](https://user-images.githubusercontent.com/45676906/123542110-bce86800-d782-11eb-8bbf-294dd1df38bb.png)

그래서 위와 같이 이것저것 찾아서 추가를 해주고 다시 실행 후 접속을 했습니다. 그런데.. 무슨 이유 때문인지 이것도 계속 `로그인` 창이 뜨는 것은 똑같았습니다. 

그렇게 로그인 창이 안뜨도록 이것저것 좀 더 시도를 해보니 원인은 `httpBasic()` 때문이었습니다. 이것을 빼니까 문제 없이 실행이 되었는데요. 
`httpBasic()`은 어떤 역할을 하는 것일까요? 

<br>

## `HttpBasic() 이란?`

Basic Authentication을 사용하기 위해 추가해야 하는 것입니다. 즉, REST API를 인증할 때 `username`, `password`로 인증을 하는 방법인데요. 이것을 추가했기 때문에 계속 로그인을 하라고 떴던 것입니다.

![스크린샷 2021-06-27 오후 7 51 32](https://user-images.githubusercontent.com/45676906/123541750-151e6a80-d781-11eb-9fbb-327428a25e06.png)

그래서 위와 같이 `httpBasic()`을 제거하니까 문제 없이 Swagger-Ui 화면이 잘 출력이 되었습니다. 