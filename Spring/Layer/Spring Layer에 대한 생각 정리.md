# `Spring Layer에 대해서 정리하기`

이번 글에서는 `Spring`으로 개발할 때 많이 사용하는 `Controller`, `Service`, `DTO`, `Repository` 같은 계층 구조를 많이 사용하게 되는데요. 이 때 각 계층이 어떤 역할을 하는지 대략적으로는 알고 쓰지만, 기계적으로 사용하는 느낌도 있어서 이번 글에서 한번 정리해보려 합니다.
(이번 글의 내용은 정답이 아니라 제가 개발하면서, 공부하면서 느꼈던 부분에 대해서 정리해보려 합니다! 틀리거나 부족한 부분이 많을 것입니다,,)

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
  - `@Entity`가 사용된 영역을 도메인 영역으로 이해하면 좋습니다.
  - 무조건 데이터베이스 테이블과 관련이 있어야 하는 것은 아닙니다. 

<br>

위의 `Web`, `Service`, `Repository`, `DTO`, `Domain` 중에서 `비즈니스 로직`을 처리해야 하는 곳은 바로 `Domain` 영역입니다. 

