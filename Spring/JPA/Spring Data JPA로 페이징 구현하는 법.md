# `Spring Data JPA에서 페이징 구현하는 법`

이번 글에서는 `Spring Data JPA`에서 `Paging`을 구현하는 법에 대해서 알아보겠습니다. 저는 `JPA`에 대해서 이제 공부하는 단계라 페이징을 처음 구현해보는데요. 공부하기 전에도 `JPA로 페이징을 구현하는게 어렵다` 라는 말은 많이 듣기만 해봤는데 이번 기회에 한번 공부해보면서 정리를 해보겠습니다. (이번 글의 코드를 보고 싶다면 [Github](https://github.com/wjdrbs96/blog-code/tree/master/JPA_Paging) 에서 확인하실 수 있습니다.)

<img width="1053" alt="스크린샷 2021-11-07 오후 3 27 58" src="https://user-images.githubusercontent.com/45676906/140634844-9ba0a684-1fb8-40c4-8c2c-e58c5b9250f7.png">

페이징은 위의 보이는 것처럼 한 화면에 다 보여줄 수 없기 때문에, `페이지`를 나눠서 게시글을 보여주는 것을 말하는데요. 페이징을 구현하는 방법, 성능을 개선하는 방법 등등 정말 다양하고 복잡하지만 초급자의 마음으로 정리해보겠습니다.

<br> 

## `이번 글에서 사용할 기술`

- `Java 11`
- `Spring Boot 2.5.6 Gradle`
- `Spring Data JPA`
- `H2 DataBase`

<br>

위의 기술들을 사용해서 `페이징`을 구현해보겠습니다. 바로 고고싱~!

<br> <br>

## `build.gradle`

```
implementation 'org.springframework.boot:spring-boot-starter-web'

compileOnly 'org.projectlombok:lombok'
annotationProcessor 'org.projectlombok:lombok'

implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
runtimeOnly 'com.h2database:h2'
```

먼저 `build.gradle`에 `starter-web`, `lombok`, `JPA`, `H2` 의존성을 추가하겠습니다. 그리고 `페이징` 예제를 만들 때 필요한 `Entity`를 간단하게 만들어보겠습니다. 

<br> <br>

## `Entity 설계하기`

<img width="480" alt="스크린샷 2021-11-07 오후 3 14 38" src="https://user-images.githubusercontent.com/45676906/140634559-93e1baf0-be4c-4843-9d63-709ba2283064.png">

일반적으로 한명의 유저는 여러 개의 게시글을 작성할 수 있고, 게시글을 작성한 사람은 한명이기 때문에 `User - Post = 1 : N` 관계로 `Entity`를 구현해보겠습니다.

<br>

### `Post Entity`

```java
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "post")
@Entity
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Lob
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

}
```

<br>

### `User Entity`

```java
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "user")
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String email;

}
```

이번 글에서 `Post`, `User` Entity에서 `ManyToOne`, `LAZY` 등등 이런 것들이 중요한 것이 아니라서 `Entity` 코드만 작성한 후에 얼른 `페이징`을 구현하는 것으로 넘어가겠습니다. 

<br> <br>

## `application.yml 설정하기`

```yaml
spring:
  datasource:
    url: jdbc:h2:mem:paging-db
    username: sa
    password:
      
  h2:
    console:
      enabled: true
      path: /h2-console

  jpa:
    show-sql: true
    properties:
      hibernate:
        format_sql: true
      javax:
        persistence:
          sharedcache:
            mode: ENABLE_SELECTIVE

    generate-ddl: true
    hibernate:
      ddl-auto: create
```

먼저 `JPA`, `H2`에 필요한 설정을 `application.yml`에 추가하겠습니다. H2 mem DB로 사용할 수 있도록 설정하였습니다. 혹시나 잘 사용법을 잘 모르겠다면 [H2 Memory DB 사용법](https://github.com/wjdrbs96/Today-I-Learn/blob/master/Spring/H2/Spring%20H2%20memory%20DB%20%EC%82%AC%EC%9A%A9%EB%B2%95.md) 을 보고 오시면 됩니다.   

<br> <br>

## `Paging 구현하기`

![스크린샷 2021-11-07 오후 3 43 20](https://user-images.githubusercontent.com/45676906/140635219-34824906-748d-4236-b728-b00f90216280.png)

가장 먼저 `Controller` API 에서 `Pageable` 인터페이스 타입으로 파라미터를 받으면 되는데요. `Pageable`는 어떤 인터페이스인지 내부 메소드를 간단하게 보고 가겠습니다. 

<br>

![스크린샷 2021-11-07 오후 3 45 13](https://user-images.githubusercontent.com/45676906/140635301-b5c34df2-92d3-4d26-aff3-7df197bf6fec.png)

인터페이스가 가진 메소드를 보면 여러가지가 있지만 `getPageNumber()`, `getPageSize()`, `getOffset()` 처럼 페이징을 구현할 때 필요한 값들을 편하게 구할 수 있는 메소드들을 추상화 시켜놓은 것을 볼 수 있습니다. 

그래서 이제 시도해보려는 방법은 `Spring Data JPA에서 메소드 이름으로 쿼리를 만들 때 페이징을 추가하는 법` 입니다. 바로 고고싱! 

<br> <br>

## `Repository 구현하기`

```java
public interface PostRepository extends JpaRepository<Post, Long> {

    Page<Post> findByUserOrderByIdDesc(User user, Pageable pageable);

}
```

게시글을 조회하기 위해서 위와 같이 `User`가 작성한 `Post(게시글)`을 `내림차순`으로 조회하도록 메소드를 작성하였습니다. 그리고 주목해서 볼 점은 두 번째 파라미터가 `Pageable 인터페이스` 라는 것을 볼 수 있습니다. 즉, `JpaRepository<> 를 사용할 때, findAll() 메서드에 Pageable 인터페이스로 파라미터를 넘기면 페이징을 사용할 수 있습니다.` 그리고 반환 타입이 `Page` 인터페이스라는 것을 알 수 있습니다. 

<br>

![스크린샷 2021-11-07 오후 6 19 54](https://user-images.githubusercontent.com/45676906/140639424-3d885eb6-cf79-439e-9eef-f3d6f5c0d2ff.png)

`Page` 인터페이스도 내부 메소드를 보면 페이징을 구현할 때 필요한 값들을 `getTotalPages()`, `getTotalElements()`와 같은 메소드로 추상화 시켜놓은 인터페이스임을 알 수 있습니다. 그러면 이제 `Repository`에서 정의한 메소드의 결과 값을 `return 받아서 DTO로 변환하는 로직을 담당하는 Service` 계층의 코드를 한번 보겠습니다. 

<br>

![스크린샷 2021-11-07 오후 6 26 41](https://user-images.githubusercontent.com/45676906/140639595-bf68be65-6ea1-4d58-908d-e49d0963cf46.png)

정리하면 단순하게 `map`을 사용해서 `Post Entity -> PostResponseDTO`로 변환해서 `Controller`로 return 하는 로직입니다.  

<br>

![스크린샷 2021-11-07 오후 6 41 23](https://user-images.githubusercontent.com/45676906/140640005-75f49791-154d-46c4-8c1b-bcb348dc4dcb.png)

그리고 `Controller` 로직에서 `getContent()` 메소드를 사용해서 `List<PostResponseDTO>`를 반환하도록 수정한 후에 페이징 요청을 해보겠습니다. 

<br>

<img width="435" alt="스크린샷 2021-11-07 오후 6 43 24" src="https://user-images.githubusercontent.com/45676906/140640084-dc098b6c-3925-41c4-89e4-a4d2d8c9d34f.png">

```
http://localhost:8080/post?page=0&size=5
```

위와 같이 API에 `Query String` 형태로 API를 보내면 위에서 작성한 코드를 기반으로 JPA가 페이징 쿼리를 만들어줍니다. 

<br>

![스크린샷 2021-11-07 오후 6 46 44](https://user-images.githubusercontent.com/45676906/140640153-ca68f633-aec0-497b-a4df-3c0bd7c23920.png)

그래서 실행되는 쿼리를 보면 `limit`을 통해서 페이징 쿼리를 만들고, 내부적으로 `count()` 쿼리도 실행하는 것을 볼 수 있습니다.  

<br>

<img width="435" alt="스크린샷 2021-11-07 오후 6 43 45" src="https://user-images.githubusercontent.com/45676906/140640091-c542589f-bc84-4120-9d92-3423fb3a8224.png">

```
http://localhost:8080/post?page=1&size=5
```

그리고 이번에는 그 다음 페이지를 조회하는 요청을 보냈을 때 위와 같이 다음 5개가 응답으로 온 것을 확인할 수 있습니다. 

<br>

![스크린샷 2021-11-08 오후 4 37 56](https://user-images.githubusercontent.com/45676906/140702008-aa1b8153-3518-49b7-828c-a0e08834e642.png)

이번에는 두 번째 페이지라서 쿼리에 보면 `offset`이 생긴 것도 볼 수 있습니다. 이렇게 페이징 구현은 끝이 났는데요. 단순히 구현만 하는 것은 그렇게 어렵지는 않지만, 여기서 한 가지 아쉬운 점이 있습니다. `Count` 쿼리는 맨 처음에 한번만 실행하여 전체 Post Entity 개수를 알면되는데 지금은 매 페이지 API를 요청할 때 마다 Count 쿼리가 실행되고 있습니다. 

즉, 성능이 중요하다면 이런 것 또한 부담이 될 수 있을 수 있다고 생각합니다. 그래서 이번 글에서는 다루지 않지만, [조졸두님의 페이징 성능 개선하기 시리즈](https://jojoldu.tistory.com/531) 를 같이 참고해서 보시면 좋을 거 같습니다.

<br> <br>

## `Pageable 동작원리 간단하게 살펴보기`

![스크린샷 2021-11-08 오후 8 14 56](https://user-images.githubusercontent.com/45676906/140732601-64fbf321-a448-4237-bcbe-893994ac8d40.png)

Controller API 메소드 파라미터에 `Pageable`만 추가해주면 `PageRequest` 객체를 내부적으로 생성하여 위에서 보았던 페이징에 필요한 작업들을 해주는 것인데요. 그 과정을 살짝만 알아보겠습니다. 

<br>

![스크린샷 2021-11-08 오후 8 16 42](https://user-images.githubusercontent.com/45676906/140732822-538685fd-9f81-44a4-88dd-8fe7d454da86.png)

`Pageable` 인터페이스 구현체를 보았을 때, 느낌상 `PageRequest`를 사용할 것 같아 `PageRequest` 클래스 내부 코드를 보았습니다. 

<br>

![스크린샷 2021-11-08 오후 8 18 44](https://user-images.githubusercontent.com/45676906/140733097-a7b7d2c2-881d-447f-aea3-040e1bab03c1.png)

PageRequest 클래스에서 `of` 메소드로 객체를 생성하는 코드에 `Break Point`를 걸어놓고 디버그 모드로 실행을 해보겠습니다. (참고로 여기서 변수 이름이 `page`, `size`기 때문에 Query String에도 `page`, `size` 이름이 사용되는 것 같습니다.)

<br>

![스크린샷 2021-11-08 오후 8 20 53](https://user-images.githubusercontent.com/45676906/140733815-acc467df-7d47-44e7-82f8-3e96857bdbdc.png)

위와 같이 특별하게 페이징 `Size`를 지정하지 않으면 `Default`로 20이 지정되는 것을 볼 수 있습니다. 여기서 `Default Paging Size`를 변경하고 싶다면 아래와 같이 하면 됩니다. 

<br>

![스크린샷 2021-11-08 오후 8 35 07](https://user-images.githubusercontent.com/45676906/140735186-d270481a-c925-4806-a330-7b76cd01b46d.png)

위와 같이 `@PagingDefault` 애노테이션을 사용하면 따로 `size`를 Query String에 지정하지 않으면 7개씩 페이징 처리가 됩니다. 

<br>

이번 글의 코드를 보고 싶다면 [Github](https://github.com/wjdrbs96/blog-code/tree/master/JPA_Paging) 에서 확인하실 수 있습니다.