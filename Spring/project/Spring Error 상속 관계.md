# `Spring Error 상속 관계는 어떻게 구성하면 좋을까?`

이번 글에서는 프로젝트를 진행하면서 `Spring Error Handling`을 어떻게 할 수 있었는지에 대해서 정리해보겠습니다. Spring 에서는 `ControllerAdvice`, `ExceptionHandler` 라는 어노테이션을 제공해주기 때문에 쉽게 에러 핸들링을 할 수 있습니다. 

이 어노테이션에만 의존해서 모든 에러가 생길 때마다 전부 해당 어노테이션으로 에러를 등록하게 되었습니다. 그러다 보니 어느정도의 프로젝트 규모가 커졌을 때 해당 어노테이션이 존재하는 파일의 규모가 상당히 커지고 비효율적이라는 것을 느꼈습니다.
그래서 [Cheese Yun Error Guide](https://cheese10yun.github.io/spring-guide-exception/) 글을 보고 프로젝트에 적용을 해보았는데요. 

어떤 과정이 있었고, 어떻게 적용할 수 있었는지에 대해서 정리해보겠습니다. 

<br>

## `너무 거대해진 에러 파일`

프로젝트를 하다 보면 `이메일이 중복됩니다.`, `닉네임이 중복됩니다`, `필요한 값이 없습니다.`, `존재하지 않는 유저입니다.` 등등의 엄청나게 많은 에러들이 생길 것인데요. 

![스크린샷 2021-06-26 오후 8 43 51](https://user-images.githubusercontent.com/45676906/123511901-39b20e00-d6bf-11eb-9652-0670f704fc11.png)

이렇게 에러가 발생할 때마다 전부 `ControllerAdvice`에 등록하게 되었습니다. 그러다 보니 어느순간에 하나의 파일에 에러 핸들링 메소드가 정말 많아졌고, 관리하기도 쉽지가 않다는 것을 느꼈습니다.
 
<br>

## `에러 상속 관계를 만들자!`

기존에는 모~든 클래스들이 전부 다 Unchecked Exception 으로 처리하기 위해서 `RuntimeException`을 상속 받아 사용하고 있었습니다. 그래서 위와 같은 문제점들이 생겼던 것인데요. 이러한 문제를 해결하기 위해서는 에러 클래스들을 상속 관계를 만들어야 합니다.

![스크린샷 2021-06-26 오후 9 14 17](https://user-images.githubusercontent.com/45676906/123512634-797af480-d6c3-11eb-9e86-e2c68015cf20.png)

현재 프로젝트의 에러 관계는 위와 같습니다. 즉, `RuntimeException`은 `BusinessException`만 `extends`하고 나머지는 새롭게 정의한 클래스들을 상속하게 되는 것인데요. 

<br>

### `Business Exception 처리`

Business Exception 이란 요구사항에 맞지 않을 경우 발생시키는 `Exception`을 말합니다. 즉, 요구사항에 맞지 않는 것들을 개발자들이 정의해서 예외를 발생시키는 것이라고 생각하면 됩니다. (위에서 말한 `닉네임 중복`, `이메일 중복` 등등)

BusinessException을 상속 하고 있는 클래스들을 보면 대표적으로 `EntityNotFoundException`, `InvalidValueException`이 존재합니다. 각각 어떤 것인지 이름에서도 어느정도 유추가 가능한데요. 그럼에도 하나씩 어떤 것인지 알아보겠습니다. 

<br>

## `EntityNotFoundException`

위의 클래스는 어떤 Entity가 존재하지 않는 예외 클래스들이 상속 받도록 만든 클래스입니다. 예를들어 `UserNotFoundException`, `BucketNotFoundExeption` 등등이 존재합니다. 

![스크린샷 2021-06-26 오후 9 32 12](https://user-images.githubusercontent.com/45676906/123513092-1fc7f980-d6c6-11eb-8403-dd696c2beea9.png)

코드로 보면 위와 같습니다. `BusinessException`을 상속 받고 있는 것을 볼 수 있습니다. 

<br>

![스크린샷 2021-06-26 오후 9 37 10](https://user-images.githubusercontent.com/45676906/123513186-ab418a80-d6c6-11eb-9bfd-e466e9d46179.png)

위와 같이 `UserNotFoundException`은 `EntityNotFoundException`을 상속 받고 있습니다. 이렇게 어떤 Entity 라는 객체가 존재하지 않는 경우에는 모두 `EntityNotFoundException`을 상속 받도록 설계하였습니다. 

<br>

## `InvalidValueException`

이 클래스도 이름에서 알 수 있듯이 어떤 값이 적절하지 않을 때 발생하는 에러입니다. 예를들면 `닉네임 중복`, `이메일 중복`들이 있습니다. 400에러에 해당하는 것들이 대표적이라 할 수 있을 거 같습니다. 

![스크린샷 2021-06-26 오후 9 42 41](https://user-images.githubusercontent.com/45676906/123513300-7124b880-d6c7-11eb-9999-5c1c13c1c54a.png)

이것 또한 `BusinessException`을 상속 받도록 설계하였습니다. 

<br>

![스크린샷 2021-06-26 오후 9 44 57](https://user-images.githubusercontent.com/45676906/123513352-c365d980-d6c7-11eb-8d4a-3c7da3d10baa.png)

위처럼 대표적으로 `닉네임 중복`의 경우도 유효하지 않은 값에 해당하기 때문에 InvalidValueException 클래스를 상속하도록 설계하였습니다. 

이런식으로 클래스 상속 관계를 잘 설계해놓으면 `ControllerAdvice`에서 아래와 같이 몇 개를 등록하지 않아도 모든 에러를 핸들링 할 수 있게 됩니다. 

![스크린샷 2021-06-26 오후 10 02 45](https://user-images.githubusercontent.com/45676906/123513812-3ec88a80-d6ca-11eb-8807-8a2c05a43f10.png)

`@ExceptionHandler`에 `BusinessException` 하나만 등록해놓으면 BusinessException을 상속 받고 있는 모든 클래스들의 에러를 핸들링 할 수 있게 됩니다. 상속 관계 없이 모든 클래스들이 다 RuntimeException을 상속 받았다면 모든 에러를 위의 클래스 파일에다 다 등록을 했어야 할텐데 
이렇게 상속 관계를 만들어주니 훨씬 더 깔끔하게 에러 핸들링을 할 수 있습니다. 

