# `MyBatis ResultMap 사용하게 된 이유`

이번 글에서는 프로젝트에서 `MyBatis`를 사용하면서 `ResultMap`을 사용한 것에 대해서 정리해보겠습니다.(ResultMap이 어떤 것인지?를 정리한 것이라기 보다 프로젝트에서 왜 ResultMap을 썼고 어떻게 적용했는지 정도만 정리해두려 작성하는 글입니다!) `MyBatis` 사용하는 법이 궁금하다면 [여기](https://devlog-wjdrbs96.tistory.com/200) 에서 확인할 수 있습니다. 

![스크린샷 2021-11-18 오후 3 47 00](https://user-images.githubusercontent.com/45676906/142366087-9424cdf3-669c-4bfe-9d48-7637a9edfd57.png)

제가 프로젝트에서 사용하고 있는 `Layer`를 간단하게 그리면 위와 같습니다. 즉, 클라이언트에게 받아온 `DTO`를 `Service Layer`에서 `DTO -> Entity` 과정을 거친 후에 `DB Layer`에 영속화 시키는 작업을 하고 있습니다. 그리고 `DB Layer`에서 조회한 결과들도 `Service Layer`에서 `Entity -> DTO`로 변환하여 `Presentation Layer`로 반환하여 클라이언트에게 응답을 주고 있습니다. 

이 때 `MyBatis`에서 테이블 사이의 `JOIN`이 필요할 때 `ResultMap`을 사용하지 않았을 때 어떻게 하게 되었는지 먼저 알아보겠습니다. 

![스크린샷 2021-11-18 오후 3 53 37](https://user-images.githubusercontent.com/45676906/142366838-20b7f9ea-5df2-4e49-bd5e-c0b105faef8f.png)

위처럼 `DB Layer -> Service Layer`로 디비 결과를 반환할 때 `ResultMap`을 사용하지 않으면 `내부 VO or DTO` 같은 것들을 사용해서 받았어야 했습니다. 이게 어떤 말인지 예제 코드를 보겠습니다. 

<br>

<img width="516" alt="스크린샷 2021-11-18 오후 3 58 51" src="https://user-images.githubusercontent.com/45676906/142367793-02e2ce31-c4a2-40d4-94bb-8353f4e30257.png">

먼저 제가 이번 글에서 예시로 사용하려는 테이블의 관계는 위와 같습니다. 

<br>

![스크린샷 2021-11-18 오후 3 55 28](https://user-images.githubusercontent.com/45676906/142367072-f02595f6-6262-4f67-8ff2-b49ba6bd6c5b.png)

4개의 테이블을 사용해서 쿼리를 작성했고 SELECT 절에서 가져오려는 컬럼은 위와 같습니다. 그런데 가져오려는 필드는 `discussion_group`, `book`, `user` 테이블에 있는 것들입니다. 즉, `JPA` 처럼 알아서 `JOIN`된 데이터들을 넣어서 반환해주는 것이 아니기 때문에 직접 위의 SELECT 절에 적은 필드들을 받을 `VO` or `DTO`를 만들어야 합니다. 

즉, `DB Layer`인 위의 쿼리에서 테이블 컬럼이 추가된 것이 아니라 쿼리 `SELECT` 절에 추가될 때마다 `DTO`도 변경해야 한다는 단점이 있습니다. 이게 싫어서 애초에 가져올 때 `JOIN`에 사용하고 있는 테이블들의 컬럼을 `*`와 같이 전부 다 가져오고 그걸 받는 `DTO`를 만들어도 될 것입니다. 하지만 `Entity`를 사용하지 않고 추가로 또 `DTO`를 만들어서 사용한다는 것은 번거로운 작업이 될 것입니다. 

또한 클라이언트에게 응답을 줄 때는 다른 `ResponseDTO`로 변환해서 주어야 그나마 `Layer`간의 의존성이 줄어들 것이라 생각되는데, 이것도 사실상 비슷한 `DTO들 끼리` 변환하는 과정이 필요하게 됩니다. 그래서 저는 `Spring Data JPA`를 사용할 때처럼 `DB Layer`에서 `Service Layer`로 연관된 `Entity`를 조회해올 때(MyBatis에서 JOIN을 할 때) Entity로 가져오고 싶었습니다. 

이것을 `ResultMap`으로 해결할 수 있습니다.

![스크린샷 2021-11-18 오후 9 35 11](https://user-images.githubusercontent.com/45676906/142416359-b986abcc-e9ed-45b5-8db1-75441358ed18.png)

`MyBatis`에는 `JPA`처럼 `Entity` 어노테이션을 사용하는 것은 없지만, `Entity` 개념으로 `DiscussionGroup` 클래스를 만들었습니다. 그리고 따로 `@ManyToOne` 같은 연관관계 어노테이션도 없지만, `DiscussionGroup`에서 `User`, `Book` 엔티티를 참조할 수 있도록 구성하였습니다. 

<br>

![스크린샷 2021-11-18 오후 9 38 23](https://user-images.githubusercontent.com/45676906/142417032-b291260c-9af0-44ba-a29e-02bddb173cda.png)

`resultMap`은 위와 같이 사용할 수 있습니다. `DiscussionGroup`을 통해서 `Book`, `User`를 `JOIN` 할 것이기 때문에 `DiscussionGroup`에서 나머지 `Entity`를 `association` 하면 됩니다. 

- `property`: 객체 참조변수 명
- `column`: DB 컬럼 명

<br>

`result`에는 쿼리 SELECT 절에서 가져오려는 컬럼을 명시해주어야 가져올 수 있습니다.(참고로 `Book, User Entity`에도 존재하는 필드여야 합니다.) 좀 더 들어가면 `association`은 1:1 관계이고, `collection`은 1:N 관계에서 사용하는 것이라고 알고 있는데, 저는 `DiscussionGroup`에 따로 1:N을 구분해서 사용하지 않아서 `association`을 사용했습니다. 

![스크린샷 2021-11-18 오후 9 52 26](https://user-images.githubusercontent.com/45676906/142418859-f5d32059-bf52-43b3-b488-797fda7d385e.png)

반환 값은 위와 같고 실제 `DB Layer의 ResultMap` 결과가 `Service Layer`에서 어떻게 나오는지 보겠습니다. 

```
Optional[

DiscussionGroup(id=15, description=이것은 이런 책이에요! 11, createdAt=2020-05-01T00:00:04, expiredAt=2021-10-22T14:10:10, remainingDay=0, leaderScore=0, meLeader=false, classes=null, userCount=0, isFailedGroupQuiz=false, canJoinGroup=false, 

book=Book(isbn=7123, title=제목4, author=저5, image=http://image.yes24.com/momo/TopCate753/MidCate008/75277681.jpg, category=SOCIAL_SCIENCE), 

user=User(id=null, nickname=포스트맨, gender=null, birth=0, socialType=null, signupCode=null, alarm=0, leaderScore=0.0, profileImage=null, deviceToken=null))

]
```

위의 반환 값을 `ToString`을 통해서 출력해보면 위와 같이 연관된 `Entity`를 다 같이 가져오는 것을 볼 수 있습니다. JPA 보다 번거로운 작업이 필요하긴 하지만 나름 비슷하게 사용할 수 있는 방법 중에 하나라고 생각합니다. 

<br> 

![스크린샷 2021-11-18 오후 10 00 47](https://user-images.githubusercontent.com/45676906/142420076-a0441a99-310d-4978-9f29-8406fa51ba49.png)

그리고 `map`을 통해서 클라이언트에게 응답을 보낸 `DTO`로 변환하는 작업을 하면 끝입니다. 

이렇게 제가 프로젝트에서 `MyBatis`를 사용하면서 `ResultMap`을 간단하게 사용한 것에 대해서 정리해보았습니다!