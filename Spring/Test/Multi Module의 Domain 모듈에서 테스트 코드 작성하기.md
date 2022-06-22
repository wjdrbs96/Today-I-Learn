## `Multi Module Domain 모듈에서 테스트 코드 실행하는 법`

<img width="455" alt="image" src="https://user-images.githubusercontent.com/45676906/174696019-140ff536-9689-47ef-8263-4db70e4d806b.png">

위와 같이 `main 클래스`를 가지는 `api 모듈`과 `domain` 모듈로 2개가 분리되어 있습니다.

<br>

<img width="748" alt="스크린샷 2022-06-21 오전 10 23 25" src="https://user-images.githubusercontent.com/45676906/174697188-90f170f6-35c0-479e-a062-308967a4329a.png">

`api` 모듈에서 `domain` 모듈을 사용할 때 위와 같이 참조해서 사용하는데요. 즉, `api 모듈에서 Domain 모듈을 import`해서 사용한다고 생각하면 됩니다.

<br>

## `Domain 모듈에서 테스트 코드를 실행해보자.`

<img width="1647" alt="스크린샷 2022-06-21 오전 10 32 48" src="https://user-images.githubusercontent.com/45676906/174697930-39b07d17-33ad-4185-8c70-902a64d98f9b.png">

그러면 이제 `Domain` 모듈에서 `통합 테스트` 코드를 간단하게 작성하여 실행해보면 위와 같은 아리송한 에러 메세지를 볼 수 있습니다. 

에러가 발생하는 이유를 생각해보면 `Domain` 모듈에서 `통합 테스트` 코드를 작성한다면 `@SpringBootTest` 어노테이션을 사용해서 진행할 것인데요.

`@SpringBootTest` 어노테이션은 `Spring Context`와 모든 `Spring Bean` 들을 띄워서 테스트 환경을 만들게 되는데요.

<img width="589" alt="스크린샷 2022-06-23 오전 12 09 43" src="https://user-images.githubusercontent.com/45676906/175065441-a779d70c-309b-4051-bbf7-d783f9fae312.png">

도메인 모듈에는 `SpringBootApplication` 어노테이션이 존재하지 않기 떄문에, 도메인 모듈은 `Spring Context`를 띄울 수 없어서 에러가 발생하는 것입니다.

- `SpringBootApplication`
- `ConfigurationContext`

즉, `Domain` 모듈에서 통합 테스트를 돌리기 위해서는 위의 두 가지 중에 하나를 추가해서 통합 테스트할 떄 `Spring Context`가 뜰 수 있게 하면 통합 테스트를 실행할 수 있습니다.

<br>

### `SpringBootApplication`

<img width="355" alt="image" src="https://user-images.githubusercontent.com/45676906/175066756-984c02d2-e6d9-4e54-858f-b8078af5d8ea.png">

```java
@SpringBootApplication
public class TestConfiguration {
}
```

위와 같이 `TestConfiguration`과 같이 `@SpringBootApplication` 어노테이션을 가지고 있는 클래스를 하나 생성해놓으면 도메인 모듈 통합 테스트를 실행할 때 스프링 컨텍스트를 띄울 수 있게 됩니다.

<br>

### `ContextConfiguration`

위처럼 `@SpringBootApplication`을 사용하지 않는다면 `ContextConfiguration` 어노테이션을 사용해서 통합 테스트를 위한 스프링 컨텍스트를 띄워서 사용하는 방법입니다.

```
@ContextConfiguration defines class-level metadata that is used to determine how to load and configure an ApplicationContext for integration tests.
```

```java
@ContextConfiguration(classes = {
    JasyptConfig.class
})
@SpringBootTest
public class JasyptConfigTest {}
```

예를들면 위와 같이 사용할 수 있습니다.

<br>

## `Reference`

- [https://stackoverflow.com/questions/47487609/unable-to-find-a-springbootconfiguration-you-need-to-use-contextconfiguration](https://stackoverflow.com/questions/47487609/unable-to-find-a-springbootconfiguration-you-need-to-use-contextconfiguration)