# `@SpringBootApplication 동작원리 알아보기`

![스크린샷 2021-11-23 오후 5 16 16](https://user-images.githubusercontent.com/45676906/142990118-6a30fa41-4c7c-43e3-8fbf-aa9d74028144.png)

`Spring Boot`로 프로젝트를 만들면 `Main` 클래스에 `@SpringBootApplication` 어노테이션이 존재하는데요. `@SpringBootApplication`이 어떻게 동작하는지 이번 글에서 간단하게 알아보겠습니다. 

<br>

![스크린샷 2021-11-23 오후 5 19 19](https://user-images.githubusercontent.com/45676906/142991095-3a3f3e3d-4ad8-4e9c-9cee-375db1885892.png)

어노테이션 내부를 보면 크게 `@SpringBootConfiguration`, `@EnableAutoConfiguration`, `@ComponentScan` 3가지로 볼 수 있는데요. 하나씩 알아보겠습니다. 

<br> <br>

## `SpringBootConfiguration`

![스크린샷 2021-11-23 오후 5 02 00](https://user-images.githubusercontent.com/45676906/142993255-796c9c86-a83c-4c4f-a4dd-21359897c80b.png)

<br>

![스크린샷 2021-11-23 오후 5 02 44](https://user-images.githubusercontent.com/45676906/142993283-045a78ee-282b-4f7b-99c9-19bff95247d3.png)

`SpringBootConfiguration`은 이름에서 알 수 있듯이 `@Configuration` 처럼 `설정`과 관련되어 있는 어노테이션이라는 것을 알 수 있는데요. 어노테이션 내부의 설명과 [baeldung](https://www.baeldung.com/springbootconfiguration-annotation) 글에서의 설명을 보면 위와 같습니다. 

`Configuration`은 `Spring`에서 `Bean 등록 같은 설정 클래스 파일`을 만들 때 사용하는데요. `SpringBootConfiguration`은 `Spring Boot`에서 사용하고 있는 `Configuration`과 비슷한 어노테이션입니다. 

> @SpringBootConfiguration is an alternative to the @Configuration annotation. The main difference is that @SpringBootConfiguration allows configuration to be automatically located. This can be especially useful for unit or integration tests.

<br>

차이를 위와 같이 설명하고 있는데요. 여기서 `@SpringBootConfiguration allows configuration to be automatically located. This can be especially useful for unit or integration tests.` 이 말이 무슨 말인지 이해가 되지 않았습니다. 그래서 좀 더 찾아보니 `SpringBootTestContextBootstrapper` 클래스를 찾았습니다. 

![스크린샷 2021-11-23 오후 6 24 45](https://user-images.githubusercontent.com/45676906/142999337-0f86d9c6-6713-4f72-9757-c38782d1e579.png)

즉, 위의 말은 `Test를 작성할 때 Configuration 클래스를 찾는데` 도움을 주는 것으로 추측이 되는데요. 조금 더 내부 코드를 보겠습니다. 

<br>

![스크린샷 2021-11-23 오후 6 26 47](https://user-images.githubusercontent.com/45676906/142999629-33c24ea5-6164-40b1-a6dc-dc3efd429da8.png)

`SpringBootTestContextBootstrapper` 내부에 `getOrFindConfigurationClasses` 메소드가 존재하는데 코드를 살펴보면 되게 복잡한 과정을 거치는 것 같은데 이 부분은 [여기](https://perfectacle.github.io/2020/12/27/detecting-test-configuration-in-spring-boot-test/) 에서 정리가 잘 되어 있어서 참고하면 좋을 거 같습니다.

즉, `SpringBootConfiguration`, `@Configuration`은 큰 차이는 없고 `SpringBootApplication`에 존재하는 `SpringBootConfiguration`을 통해서 `SpringBootTest`를 실행할 때 설정 관련 파일들을 읽어오는데 자동화 한다? 정도로 이해하면 좋을 거 같습니다. 

<br> <br>

## `@EnableAutoConfiguration`

![스크린샷 2021-11-23 오후 6 33 12](https://user-images.githubusercontent.com/45676906/143000464-67c7cd01-4312-4ada-a471-d5b99d794d0b.png)

프로젝트의 의존성을 보면 `org.springframework.book:spring-boot-autoconfigure`가 존재하는데요. 내부를 보면 `META-INF` 아래에 `spring-autoconfigure-metadata-properties` 파일을 보겠습니다. 

<br>

![스크린샷 2021-11-23 오후 6 34 53](https://user-images.githubusercontent.com/45676906/143000808-672780ff-f48c-4c32-b39a-da657556ddff.png)

정~말 많은 설정들을 자동으로 해주는 것을 볼 수 있습니다. 

<br>

![스크린샷 2021-11-23 오후 6 36 55](https://user-images.githubusercontent.com/45676906/143001106-613f4c6e-d177-4a44-b4fa-88737d33118b.png)

어노테이션의 설명을 보아도 `Spring MVC`, `Spring Data`, `Spring Cache`, `Spring Batch` 등등 정말 수 많은 설정들을 자동으로 해주기에 우리는 손 쉽게 `Spring MVC`를 사용할 수 있고 직접 Bean 을 등록하지 않고 편리하게 사용할 수 있는 것입니다. 

<br> <br>

## `ComponentScan`

`ComponentScan`은 `SpringBootApplication`이 존재하는 클래스와 `같은 패키지 or 하위패키지`들 중에서 빈으로 등록할 수 있는 것들을 찾아서 빈으로 등록해줍니다. 즉, 다른 패키지라면 빈으로 등록되지 않는다는 것을 알아야 합니다.

![1](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FbhBRwR%2FbtqFQKw4jhV%2Fc1GJu25gdTqimnWd0PhLyK%2Fimg.jpg)

`ComponentScan`은 위의 어노테이션이 붙어있다면 자동으로 `Bean`으로 등록해줍니다. 그리고 실제 `ComponentScan`의 실제 스캐닝은 `ConfigurationClassPostProcessor`라는 `BeanFactoryPostProcessor`에 의해 처리됩니다.

<br> <br>

## `Reference`

- [https://www.baeldung.com/springbootconfiguration-annotation](https://www.baeldung.com/springbootconfiguration-annotation)
- [https://docs.spring.io/spring-boot/docs/2.0.x/reference/html/using-boot-using-springbootapplication-annotation.html](https://docs.spring.io/spring-boot/docs/2.0.x/reference/html/using-boot-using-springbootapplication-annotation.html)