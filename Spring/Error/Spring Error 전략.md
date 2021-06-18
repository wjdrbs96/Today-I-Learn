# `Spring Error Exception 가이드`

이번 글에서는 [CheeseYun Spring Exeption Guide](https://cheese10yun.github.io/spring-guide-exception/) 글을 보고 저도 [프로젝트](https://github.com/YAPP-18th/iOS1_Backend) 에 적용하면서 어떠한 과정을 통해서 적용할 수 있었는지 정리해보겠습니다. 

<br>

![스크린샷 2021-06-18 오전 9 10 11](https://user-images.githubusercontent.com/45676906/122487011-2941c980-d015-11eb-8c81-83ef5583ef46.png)

![스크린샷 2021-06-18 오전 9 10 33](https://user-images.githubusercontent.com/45676906/122487012-2b0b8d00-d015-11eb-81a2-87d10d13ba2d.png)

프로젝트를 진행하다 보면 위와 같이 `어떤 값이 존재하지 않을 때`, `유효하지 않은 값이 왔을 때`, `서버 내부 에러` 등등 다양한 에러처리를 해야하는 상황이 존재합니다.
Spring에서는 `ExceptionHandler` 라는 어노테이션을 지원해주기 때문에 에러를 핸들링해서 클라이언트에게 응답을 보낼 수 있는데요. 

그런데 프로젝트 규모가 점점 커지다 보니 `ExceptionHandler`로 등록해야 하는 에러들이 점점 많아져서 관리하기가 힘들다는 것을 느꼈습니다. 

![스크린샷 2021-06-18 오전 9 26 25](https://user-images.githubusercontent.com/45676906/122487945-61e2a280-d017-11eb-8896-2a6c2d44b944.png)

위의 코드 예시는 몇 개의 예시지만 나중에 `이메일 중복`, `닉네임 중복`, `로그인 실패` 등등 여~러 가지 에러를 전부 다 `ExceptionAdvice`에 정의한다면 파일의 규모가 엄청나게 거대해질 것입니다. 

그래서 도메인 별로 Advice를 나눠서 관리를 하기도 했지만 여전히 관리하기가 쉽지 않았고 `너무 무분별하게 ExceptionHandler`를 등록하는 것이라 판단이 되었습니다. 그래서 이러한 에러 관련 로직을 어떻게 처리할까 하다가 [CheeseYun Spring Exeption Guide](https://cheese10yun.github.io/spring-guide-exception/) 이 글을 보고 프로젝트에 적용을 해보았습니다. 

그래서 현재 프로젝트 에러 관리의 문제점을 정리해보면 아래와 같습니다.

1. 모든 에러 클래스는 `RuntimeException`을 직접 상속 받고 있습니다. 그렇기 때문에 모든 에러를 ExceptionHandler로 등록할 수 밖에 없었습니다. 

2. 정상적인 Response와 Error Response가 한번에 관리되고 있습니다. (나누어 놓으면 관리하기도 편하고 좀 더 상세하게 응답을 줄 수 있기 때문입니다.)

<br>

대략적으로는 이정도로 문제점을 요약할 수 있는데요. 하나씩 풀어나가 보겠습니다. 

![스크린샷 2021-06-18 오전 9 41 53](https://user-images.githubusercontent.com/45676906/122488891-6e67fa80-d019-11eb-9f20-1788dc5a03b1.png)





<br>

