## `Multi Module JPA Cannot Access Error 해결하기`

이번 글은 `Multi-Module`을 사용했을 때 반드시 발생하는 에러는 아니고 필자처럼 사용했을 때 에러가 발생할 수 있는 상황이다.

![스크린샷 2022-11-27 오후 5 40 05](https://user-images.githubusercontent.com/45676906/204126357-0128511c-39c2-4c18-a936-6fa90d1d0029.png)

```
Cannot access 'org.springframework.data.repository.Repository' which is a supertype of 'org.springframework.data.repository.CrudRepository'. Check your module classpath for missing or conflicting dependencies
```

Multi Module로 진행할 때 JPA에서 제공해주는 `findById` 메소드를 사용하려는데 위와 같은 에러가 발생했다. 또 에러가 왜 발생하는 것일까.. 하고 관련 내용을 찾아봤는데 나의 상황에 맞는거는 딱히 찾을 수 없었다.

그래서 좀 더 고민해보니 `Multi Module Gradle` 설정 문제라는 것을 깨달았고, 내가 생각한 이유가 맞았다.

<br>

<img width="595" alt="스크린샷 2022-11-27 오후 5 52 06" src="https://user-images.githubusercontent.com/45676906/204126756-37debd5f-2b23-43af-9adc-5b287d5d03e2.png">

현재 상황은 각 모듈이 다른 모듈 or 의존성을 참조할 때 `implementation`을 사용해서 참조하고 있었다.(`spring data jpa` 의존성은 전체 `build.gradle`이 아닌 `Domain build.gradle`에 넣어놓은 상황이다.)

여기서 왜 이런 상황이 발생하는지 알기 위해서는 gradle의 `api`, `implementation` 차이가 무엇인지 알아야 한다.

<br>

## `gradle implementation vs api 차이`

![image](https://user-images.githubusercontent.com/45676906/204126960-b77b2c1e-1197-440f-9347-f0acf76079e3.png)

api를 사용하게 되면 직접, 간접 의존하고 있는 모듈까지 rebuild(recompile)이 되어야 한다. 즉, C 모듈을 수정했는데 B 모듈 뿐만 아니라 A 모듈까지 rebuild(recomplie) 되어야 하는 문제가 있다.

반면에 implementation은 직접 참조하고 있는 모듈만 rebuild(recomplie) 하면 된다는 특징이 있다.

<br>

## `위의 에러는 왜 발생한 것일까?`

api, implementation 또 하나의 차이는 `Module API 노출`이다.

![image](https://user-images.githubusercontent.com/45676906/204127087-69bab9dd-f8e7-44c7-8260-330b831aec51.png)

<br>

![image](https://user-images.githubusercontent.com/45676906/204127100-758b9e92-4791-4990-bcfa-31cb96a83879.png)

위의 그림을 보면 알 수 있지만 implementation을 사용하면 간접 참조하고 있는 모듈은 참조할 수 없다.

예를들면, C 모듈을 직접 참조하고 있는 모듈은 B이고, 간접 참조하고 있는 것은 A인데 A에서 C 모듈을 코드를 사용할 수 없다는 뜻이다. 이러한 이유 때문에 위와 같은 에러가 발생한 것이다.

- A : API 모듈
- B : Domain 모듈
- C : Spring Data JPA 의존성

<br>

위와 같은 상황에서 `A -> B -> C` 참조할 때 모두 `implementation`을 사용했기 때문에 API 모듈에서 JPA를 참조할 수 없다는 에러가 발생하는 것이다. 

이것을 해결하려면 Domain -> JPA 의존성을 참조할 때 `implementation`이 아닌 `api`를 사용해서 참조하면 해결할 수 있다.

<br>

## `Reference`

- [https://docs.gradle.org/7.0/userguide/java_library_plugin.html#sec:java_library_configurations_graph](https://docs.gradle.org/7.0/userguide/java_library_plugin.html#sec:java_library_configurations_graph)
- [https://stackoverflow.com/questions/44493378/whats-the-difference-between-implementation-api-and-compile-in-gradle](https://stackoverflow.com/questions/44493378/whats-the-difference-between-implementation-api-and-compile-in-gradle)