# `Spring H2 Memory DB 사용하는 법`

이번 글에서는 `Spring Boot`에서 `H2 메모리 기반` 데이터베이스를 사용하는 법에 대해서 알아보겠습니다.

```
runtimeOnly("com.h2database:h2")
```

먼저 위의 H2 의존성을 추가해주겠습니다.

<br> <br>

## `application.yml`

```yaml
spring:
  datasource:
    url: jdbc:h2:mem:gyunny
    username: sa
    password:

  h2:
    console:
      enabled: true
      path: /h2-console
```

여기서 url에 `jdbc:h2:mem:ggyuni-db`로 적었는데요. 보면 `mem`이 적혀 있는데 이것은 메모리 기반으로 작동하도록 하는 것입니다. 그래서 이제 스프링 부트를 실행해서 `http://localhost:8080/h2-console` 로 들어가면 아래와 같이 뜰 것인데요.

<img width="388" alt="스크린샷 2021-10-02 오후 3 40 52" src="https://user-images.githubusercontent.com/45676906/135706485-49c5737b-95e9-46b4-a14a-dbee906dae5a.png">

여기에 보면 `JDBC URL`에 yml에 적힌 url을 그대로 복사해서 넣어주면 됩니다. 그리고 Name, Password 넣고 Connect 하면 접속이 되는 것을 확인할 수 있습니다.