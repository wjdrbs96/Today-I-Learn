## `Multi Module Domain 모듈에서 테스트 코드 작성하는 법`

![스크린샷 2022-06-21 오전 10 12 59](https://user-images.githubusercontent.com/45676906/174696019-140ff536-9689-47ef-8263-4db70e4d806b.png)

위와 같이 `main 클래스`를 가지는 `api 모듈`과 `domain` 모듈로 2개가 분리되어 있습니다.

<br>

<img width="748" alt="스크린샷 2022-06-21 오전 10 23 25" src="https://user-images.githubusercontent.com/45676906/174697188-90f170f6-35c0-479e-a062-308967a4329a.png">

그리고 `api 모듈에서 Domain 모듈을 import` 해서 사용하는 것이 일반적인 `Multi-Module`의 사용법일 것입니다.

<br>

## `Domain 모듈에서 테스트 코드를 실행해보자.`

<img width="1647" alt="스크린샷 2022-06-21 오전 10 32 48" src="https://user-images.githubusercontent.com/45676906/174697930-39b07d17-33ad-4185-8c70-902a64d98f9b.png">

`Domain` 모듈에는 `SpringBootApplication`도 없기 때문에 테스트를 실행했을 때 위와 같이 에러가 발생합니다. 즉, Domain 모듈에선 `Spring Context`가 없기 때문에 에러가 발생합니다.

- `SpringBootApplication`
- `ConfigurationContext`

즉, `Domain` 모듈에서 테스트를 돌리기 위해서는 위의 두 가지 중에 하나를 추가해서 통합 테스트할 떄 `Spring Context`가 뜰 수 있게 하면 됩니다.