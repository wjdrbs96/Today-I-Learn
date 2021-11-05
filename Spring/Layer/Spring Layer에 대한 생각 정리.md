# `Spring Layer에 대해서 정리하기`

이번 글에서는 `Spring`으로 개발할 때 많이 사용하는 `Controller`, `Service`, `DTO`, `Repository` 같은 계층 구조를 많이 사용하게 되는데요. 이 때 각 계층이 어떤 역할을 하는지 대략적으로는 알고 쓰지만, 기계적으로 사용하는 느낌도 있어서 이번 글에서 한번 정리해보려 합니다.
(어디까지나 정답이 아니라 제가 개발하면서, 공부하면서 느꼈던 부분에 대해서 정리해보려 합니다! 즉, 틀리거나 부족한 부분이 존재할 수 있습니다!)

<br> <br>

## `Spring Web Layer`

![image](https://user-images.githubusercontent.com/45676906/140464604-aa034b26-4222-4eea-a009-f23c464ee63f.png)

Spring에서 자주 사용하는 계층을 크게 그림으로 나타내보면 위와 같습니다. 각 영역의 역할을 정리하면 아래와 같은데요. 

- ### `Web Layer`
  - `컨트롤러(@Controller)`와 `JSP` 등의 뷰 템플릿 영역입니다. 
  - `Filter`, `Interceptor`, `Controller Advice` 등 외부 요청과 응답에 대한 전반적인 영역을 의미합니다. 

- ### `Service Layer`
  - `@Service`에서 사용되는 영역입니다. 
  - 다들 많이 사용하듯이 `Controller`, `DAO` 중간에 존재하는 계층입니다.
  - `@Transactional`이 사용되어야 하는 영역입니다. 

- ### `Repository Layer`
  - `DataBase`와 같이 데이터 저장소에 접근하는 영역입니다.(MyBatis 라면 `Mapper`)

- ### `DTOs`
  - `DTO(Data Transfer Object)`는 `계층 간의 데이터 교환을 위한 객체`를 말합니다. 

- ### `Domain Model`
  - `Domain`이 저에게는 아직도 이해하기 힘든 부분이라고 생각합니다..
  -  `개발 대상을 모든 사람이 동일한 관점에서 이해할 수 있고 공유할 수 있도록 단순화시킨 것을 도메인 모델`이라고 합니다.(이해하기가 쉽지 않습니다,,)
  - 