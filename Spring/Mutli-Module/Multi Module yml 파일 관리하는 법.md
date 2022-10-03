## `Multi module에서 yml 파일 관리하는 법`

이번 글에서는 `Multi-Module`을 사용할 때 모듈 별 `yml` 파일 관리하는 법에 대해서 정리해보려 합니다. 

- api 모듈
  - application.yml
- domain 모듈
  - application.yml

<br>

만약에 위와 같이 2개의 모듈이 있을 때, 각 모듈마다 `application.yml`을 가지고 있을 것입니다.

그런데 지금까지 `Multi-Module`로 프로젝트를 할 때는 모듈별로 `yml` 파일을 분리하지 않고 `api` 모듈 하나에 모든 설정들을 다 넣었습니다.

yml이 잘 분리가 되었다면 api 모듈의 yml에서는 api와 관련된 설정(ex: Swagger)들이 있을 것이고 domain 모듈의 yml에는 대표적으로 DB 접근 정보 및 JPA 설정들이 존재할 것입니다.

그래서 이제 모듈 별 yml 분리하는 방법에 대해서 알아보겠습니다.

<br>

## `모듈별 yml 분리하기`

```kotlin
@SpringBootApplication
class NadaServerApplication

fun main(args: Array<String>) {
    System.setProperty("spring.config.name", "application,application-domain")
    runApplication<NadaServerApplication>(*args)
}
```

```java
@SpringBootApplication
public class NadaServerApplication {
    public static void main(String[] args) {
        System.setProperty("spring.config.name", "application,application-domain");
        SpringApplication.run(NadaServerApplication.class, args);
    }
}
```

위와 같이 `main` 클래스에 `System.setProperty()` 하나만 추가하면 됩니다. `System.setProperty`는 환경변수를 등록하는 것인데요.

- `application`
- `application-domain`

환경변수 Value에 이라고 되어 있는데 위처럼 적으면 `application.**`, `application-domain.**` 파일들을 읽어와서 사용하겠다는 뜻입니다.

<img width="363" alt="스크린샷 2022-10-04 오전 12 04 43" src="https://user-images.githubusercontent.com/45676906/193611127-83695921-81f9-4ddc-8a9a-0c8470e90ddb.png">

즉, `core` 모듈에 존재하는 `application-domain.yml`을 읽어올 수 있게 됩니다. 

여기서 주의할 점은 `Value`에 적는 이름은 `yml` 파일의 이름이어야 하고, `yml` 파일의 이름이 겹치면 안됩니다. 

```
System.setProperty("spring.config.name", "application,api");
```

위처럼 적었다면 `api.yml`, `api-local.yml`로 사용할 수도 있고, `application.yml`, `application-local.yml` 같은 이름으로도 사용할 수 있는 것입니다.

그리고 모듈별로 모두 `application.yml`의 이름을 사용하게 되면 가까운 yml을 읽어서 사용하기 때문에 `domain` 모듈의 `yml`은 사용하지 못하게 됩니다.