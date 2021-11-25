## `Spring Data JPA에서 MultiBagFetchException 해결하기`

이번 글에서는 `JPA`를 사용한지 얼마 안된 초보자 입장에서 겪은 어려움을 공유하고 어떻게 해결해나갔는지 공유하며 생각을 정리해보려 합니다.

<img width="311" alt="스크린샷 2021-11-24 오후 12 29 56" src="https://user-images.githubusercontent.com/45676906/143170004-47acc5cc-3ea9-4f3c-9235-d429764599af.png">

위의 화면에 대한 `API`를 만들어야 하는 상황입니다. 위의 뷰에서는 `내가 팔로우 하고 있는 사람이 작성한 게시글`이 나타나야 합니다.  

<br>

<img width="898" alt="스크린샷 2021-11-24 오후 12 24 13" src="https://user-images.githubusercontent.com/45676906/143169650-e261450c-5574-4237-b1ce-fe0e1c50664b.png">

`View`를 보면서 `DB Table`을 간단하게 같이 보겠습니다. 

- `하나의 게시글 : 여러 장의 사진 (1 : N)`
- `하나의 게시글 : 여러 개의 가게 평가 해시 태그 (1 : N)`
- `하나의 게시글 : 같이 가게를 간 사람들의 닉네임 (1 : N)`

<br>

위의 조건에 해당하는 데이터를 다 가져와야 하나의 `Post(게시글)`을 보여줄 수 있습니다. 즉, `JOIN`이 꽤 많이 들어가는 작업이 필요한데요. 그래서 일단 `Post Entity`를 먼저 살펴보겠습니다. 

<br>

![스크린샷 2021-11-24 오후 12 36 45](https://user-images.githubusercontent.com/45676906/143170662-b960e778-242b-4e52-bb49-9158ce1187af.png)

`Post Entity`를 보면 위에서 말한 것처럼 `Post`와 `@OneToMany` 관계인 `Entity`가 3개 존재하는 것을 볼 수 있습니다. (참고로 모든 참조 관계는 `LAZY Loading` 입니다.)

<br>

![스크린샷 2021-11-24 오후 1 21 58](https://user-images.githubusercontent.com/45676906/143174589-5e6898da-55c0-4d63-bb0b-b40a840ab2f7.png)

원래는 `내가 팔로우하고 있는 유저들이 작성한 게시글`을 들고와야 하지만, 지금은 이게 중요한 건 아니기에 `findAll()`로 전체 게시글을 다 가져오는 것으로 예제 코드를 작성하였습니다. 

<br>

![스크린샷 2021-11-24 오후 1 27 55](https://user-images.githubusercontent.com/45676906/143175102-896f28bd-e70f-4484-aaad-6bf81390b79c.png)

그리고 `DTO` 내부에서 `from` 메소드의 역할을 `Entity -> DTO`로 변환하는 작업을 진행합니다. 즉, `현재 모든 FetchType은 Lazy Loading`이고, `DTO from` 메소드를 통해서 `Entity -> DTO`로 변환하고 있는 상황입니다. 이런 상태에서 실행했을 때 쿼리가 몇 번 실행되는지 알아보겠습니다. 

<br>

![스크린샷 2021-11-24 오후 1 31 29](https://user-images.githubusercontent.com/45676906/143177324-58a83942-fe53-42b7-9785-05d5473cef66.png)

<br>

![스크린샷 2021-11-24 오후 1 54 07](https://user-images.githubusercontent.com/45676906/143177330-b4d491f2-40fa-45d7-80f8-0c231b78874b.png)

쿼리가 나가는 상황을 요약하면 아래와 같습니다. 

- `post.findAll() 쿼리 실행 => (id 1, 2, 3) 반환 => 쿼리 1번 수행`
- `post에 해당하는 with_user 가져오기` => `쿼리 3번 실행`
- `post에 해당하는 post_image 가져오기` => `쿼리 3번 실행`
- `post에 해당하는 post_evaluate 가져오기` => `쿼리 3번 실행`

<br>

위와 같이 쿼리가 실행되고 있습니다. 즉, 게시글이 현재는 3개여서 3번씩 반복이 되었지만, 게시글이 훨씬 더 많다면 훨씬 더 많은 쿼리가 실행되었을 것입니다.  

<br>

![스크린샷 2021-11-24 오후 12 43 21](https://user-images.githubusercontent.com/45676906/143171190-216cc3b7-7c2d-4b55-a35c-24ff9afee814.png)

그래서 `fetch join`을 사용하여 위에서 발생하는 `N + 1` 문제를 해결하기 위해 사용하였습니다. 그런데 위와 쿼리를 보면 빨간 네모의 `fetch join`이 `@OneToMany` 관계를 가지고 있고 `@OneToMany` 관계 2번을 `fetch join`으로 한번에 사용하고 있는 것을 볼 수 있습니다.   

<br>

![스크린샷 2021-11-24 오후 12 47 01](https://user-images.githubusercontent.com/45676906/143171449-edf62adc-7b31-45aa-874e-ae5d8dd66bc2.png)

이 상태로 실행하면 `MultipleBagFetchException`이 발생하는 것을 볼 수 있습니다. 이렇게 `N + 1` 문제를 해결하기 위해 `fetch join`을 사용하면 `MultipleBagFetchException`을 만나게 됩니다. 즉, 이러한 문제는 `2개 이상의 OneToMany 자식 테이블에 Fetch Join을 사용했을 때` 발생합니다. 

JPA에서 `Fetch Join`의 특징은 아래와 같습니다.

- `OneToOne, OneToMany는 몇개든 사용 가능합니다.`
- `ManyToMany, OneToMany는 1개만 사용 가능합니다.`

<br>

그래서 이러한 문제를 해결하기 위해서는 아래와 같은 방법들이 존재합니다.

- `자식 테이블 하나만 Fetch Join을 걸고 나머지는 Lazy Loading`
- `모든 자식 테이블을 다 Lazy Loading으로(위에서 보았던 예제)`
- `Fetch Join을 나누어서 실행한 후에 조합하기`

<br>

하지만 모든 방법 다 데이터가 많아진다면 `Lazy Loading` 부분에서 쿼리가 정말 많이 나간다면 성능 이슈가 생길 방법들이라고 생각합니다.

<br>

![스크린샷 2021-11-24 오후 2 12 53](https://user-images.githubusercontent.com/45676906/143178755-baf89337-5e64-4a1d-ade9-e1f3c54c0bb0.png)

그래서 저는 `첫 번째 방법`이 그나마 낫다고 생각해서 `Post - User - PostEvaluates`만 `Fetch Join`을 사용해서 가져오고 나머지 `PostImage`, `UserWith`는 `Lazy Loading`으로 설정하고 이 부분에선 `N + 1` 문제를 안고 가려고 이것도 시도해보았습니다. (`Post - User는 N : 1이기 때문에 같이 Fetch Join을 사용할 수 있습니다.`)

<br>

![스크린샷 2021-11-24 오후 2 15 55](https://user-images.githubusercontent.com/45676906/143179196-f46556d8-ad75-4b9e-a2a6-ed620993f165.png)

여기서 `Fetch Join`을 통해서 `Post - User - Evaluate`를 한번에 가져온 것을 볼 수 있습니다. 

<br>

![스크린샷 2021-11-24 오후 2 17 32](https://user-images.githubusercontent.com/45676906/143179204-003e607d-29ef-4a2f-8d3e-8aaea72801ab.png)

그런데 `Fetch Join`으로 가져오지 못한 나머지 부분은 `Post 수 만큼 위의 쿼리를 반복하게 됩니다.` 즉, `Lazy Loading` 부분에서는 `Entity- > DTO`로 변환하면서 `N + 1` 쿼리를 안고 가야 합니다. `Fetch Join`도 `DB 서버에 부하를 주는 것`일 수 있기 때문에 이것도 하나의 방법일 수 있습니다. 하지만 그럼에도 `N + 1` 쿼리를 안고가는건 조금 부담이 될 수 있어 좀 더 좋은 방법을 정리해보려 합니다. 

<br> <br>

## `Hibernate default_batch_fetch_size 사용하기`

이건 [김영한님 JPA 강의](https://www.inflearn.com/course/ORM-JPA-Basic) 에서도 소개해주신 적이 있고, [Jojoldu님이 아주 좋은 내용을 정리 해주신 것](https://jojoldu.tistory.com/457) 이 있습니다. 

![스크린샷 2021-11-24 오후 2 31 30](https://user-images.githubusercontent.com/45676906/143180472-5997f572-f082-4cc2-a2ae-4fad4176be98.png)

다시 한번 `N + 1` 문제를 정리하고 가면 위의 그림 처럼 `하나의 게시글이 여러 장의 사진, 여러 개의 해시 태그`를 가지다 보니 Post(부모 엔티티)의 Key를 자식 엔티티들을 조회로 사용하다 보니 `N + 1` 쿼리가 발생하는 상황인데요.

<br>

![스크린샷 2021-11-24 오후 2 37 31](https://user-images.githubusercontent.com/45676906/143181187-55e25832-b4f3-4682-bd61-a489b31a03a3.png)

`default_batch_fetch_size`를 사용하면 기존에 쿼리를 나눠서 실행해서 발생했던 `N + 1` 문제를 해당 옵션에 지정된 수 만큼 `IN` 절에 부모 키를 사용해서 한번에 가져오기 때문에 성능 향상을 할 수 있습니다. 

<br>

![스크린샷 2021-11-24 오후 2 40 23](https://user-images.githubusercontent.com/45676906/143181339-67e415ed-8db9-4752-b209-c30b22329448.png)

```yaml
spring:
  jpa:
    show-sql: true
    properties:
      hibernate.default_batch_fetch_size: 1000
      hibernate:
        format_sql: true
      javax:
        persistence:
          sharedcache:
            mode: ENABLE_SELECTIVE

    generate-ddl: true
    hibernate:
      ddl-auto: update
```

JPA 관련 설정 `application.yml` 파일 입니다. 

<br>

![스크린샷 2021-11-24 오후 2 42 06](https://user-images.githubusercontent.com/45676906/143181512-2055b240-8e63-4e0d-a7f4-006464cd874f.png)

그리고 위에서 보았던 `자식 테이블 하나만 Fetch Join을 걸고 나머지는 Lazy Loading` 예시 코드 그대로 `default_batch_fetch_size` 설정만 해놓고 실행하면 위와 같이 `IN`을 사용해서 쿼리가 실행되는 것을 볼 수 있습니다. 즉, `default_batch_fetch_size` 옵션을 사용하지 않았다면 게시글이 100개만 되어도 300번, 300번 총 600번 쿼리가 실행되었을 것입니다. 

그런데 현재 `default_batch_fetch_size`를 1000으로 주었기 때문에 이번엔 `2번` 쿼리로 다 가져올 수 있습니다. 엄청난 차이가 있는 것을 볼 수 있습니다.

> Tip) <br>
> 보통 옵션값을 1,000 이상 주지는 않습니다. <br> 
> in절 파라미터로 1,000 개 이상을 주었을때 너무 많은 in절 파라미터로 인해 문제가 발생할수도 있기 때문입니다. <br> 
> 지금 옵션은 1000으로 두었기 때문에 Store가 1000개를 넘지 않으면 단일 쿼리로 수행 된다는 장점도 있습니다.

<br>

> Tip) <br>
> 같은 방법으로 Fetch 적용시 발생하는 페이징 문제도 동일하게 해결됩니다. <br> 
> 1 : N 관계에서의 페이징 문제는 Join으로 인해 1에 대한 페이징이 정상작동 하지 않기 때문입니다. <br> 

<br> <br>

## `Reference`

- [https://jojoldu.tistory.com/457](https://jojoldu.tistory.com/457)