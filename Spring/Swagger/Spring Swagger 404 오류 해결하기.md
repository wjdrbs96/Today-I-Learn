# `Spring Swagger 404 Not Found 해결하기`

기존에 `Java`, `Spring Boot` 기반으로 `Swagger`를 사용할 때는 아래와 같이 사용했습니다. 

<br>

```
implementation 'io.springfox:springfox-swagger2:2.9.2'
implementation 'io.springfox:springfox-swagger-ui:2.9.2'
```

먼저 `build.gradle`에 위의 의존성을 하겠습니다. 

<br>

![스크린샷 2021-10-23 오후 1 11 12](https://user-images.githubusercontent.com/45676906/138541957-31b322de-f9a8-48b4-b1cf-15314babe811.png)

그리고 간단하게 자바 코드로 `Swagger` 설정을 하였습니다. 

<br>

```
http://localhost:8080/swagger-ui.html
```

<br>

![스크린샷 2021-10-23 오후 1 12 28](https://user-images.githubusercontent.com/45676906/138541976-a6751e75-22a9-410f-bf06-c671d68e9ff1.png)

그러면 위와 같이 문제 없이 접속이 되는 것을 볼 수 있습니다. (Swagger 사용법에 대해 좀 더 자세히 궁금하다면 [여기](https://devlog-wjdrbs96.tistory.com/322?category=882974) 를 참고하시면 됩니다.)

그런데 문제는 지금부터인데요. 제가 `Kotlin`, `Spring Boot` 기반으로 `Swagger`를 사용하기 위해 아래와 같이 시도하였습니다. 

```
implementation("io.springfox:springfox-boot-starter:3.0.0")
```

`build.gradle`에 `springfox` 의존성을 추가하였습니다. 

<br>

![스크린샷 2021-10-23 오후 1 15 18](https://user-images.githubusercontent.com/45676906/138542021-77b64f91-8cf7-409d-b1cc-17f78e806218.png)

그리고 위에서 자바 코드로 설정했던 것과 비슷하게 `Kotlin`으로 `Swagger` 설정을 한 후에 위에서 접속했던 주소로 접속을 해보겠습니다. 

<br>

![스크린샷 2021-10-23 오후 1 29 00](https://user-images.githubusercontent.com/45676906/138542329-1cfcb600-ec35-4526-8b8d-dd03cb60cd21.png)

```
http://localhost:8080/swagger-ui.html
```

위에서 들어간 주소 그대로 들어갔는데, 이번에는 `404 Not Found`가 뜨는 것을 볼 수 있습니다. 처음에는 왜 `404`가 뜨는거지? `/`를 마지막에 안넣어서 그런가? 하면서 이것저것 시도를 해보며 찾아보았는데요. 

원인은 다음과 같았습니다. 

<br> <br>

## `Springfox 3.0 부터 주소가 바뀌었다..`

```
implementation("io.springfox:springfox-boot-starter:3.0.0")
```

저는 `springfox`에서 `3.0.0` 버전을 사용하고 있기 때문에 아래와 같이 접근해야 했습니다. 

```
http://localhost:8080/swagger-ui/
http://localhost:8080/swagger-ui/index.html
```

좀 더 자세히 알고 싶다면 [여기](https://github.com/springfox/springfox/issues/3360) 를 참고하시면 좋습니다. 혹여나 저처럼 왜 안들어가지나 고민하고 계신다면 사용하고 계시는 `springfox` 버전을 확인해보시는 걸 추천드립니다. 