# `JPA에서 가장 중요한 2가지`

- 객체와 관계형 데이터베이스 매핑하기
- `영속성 컨텍스트!!(JPA를 이해하려면 영속성 컨텍스트라는 것을 이해해야함!)`

<br> <br> 

# `엔티티 매니저 팩토리와 엔티티 매니저`

<img width="1149" alt="스크린샷 2021-08-24 오후 2 57 56" src="https://user-images.githubusercontent.com/45676906/130563981-94e22733-8c94-413c-9d5c-18fa93ed0c49.png">

일단 `EntityManagerFactory`를 통해서 요청이 올 때마다 `EntityManager`를 생성합니다. EntityManager는 내부적으로 데이터베이스 커넥션 풀을 사용합니다. 

<br>

## `영속성 컨텍스트`

- JPA를 이해하는데 가장 중요한 용어
- 엔티티를 영구 저장하는 환경
- EntityManager.persist(entity);
- EntityManager를 통해서 영속성 컨텍스트에 접근

<br> <br>

## `엔티티의 생명주기`

- ### 비영속(new)
  - 영속성 컨텍스트와 전혀 관계가 없는 `새로운` 상태
  
- ### 영속(managed)
  - 영속성 컨텍스트에 `관리`되는 상태

- ### 준영속(detached)
  - 영속성 컨텍스트에 저장되었다가 `분리`된 상태

- ### 삭제 (removed)
  - `삭제`된 상태
  
<img width="1095" alt="스크린샷 2021-08-25 오전 6 56 07" src="https://user-images.githubusercontent.com/45676906/130695392-5133df99-f7b6-4812-be15-365147005b91.png">

<br> <br>

## `비영속이란?`

<img width="1102" alt="스크린샷 2021-08-25 오전 6 56 33" src="https://user-images.githubusercontent.com/45676906/130695446-64aeef1b-b4b4-4446-9aaa-2db7ee114e43.png">

```java
// 객체를 생성한 상태 (비영속)
Member member = new Member();
member.setId(1L);
member.setUsername("Gyunny");
```

객체만 생성했고 JPA와 전혀 관계가 없는 상태이기 때문이 이런 상태를 `비영속` 상태라고 합니다. 

<br> <br>

## `영속이란?`

<img width="712" alt="스크린샷 2021-08-25 오전 6 58 23" src="https://user-images.githubusercontent.com/45676906/130695626-25840661-780d-4d85-a62d-0ecc956df6c3.png">

```java
Member member = new Member();
member.setId(1L);
member.setUsername("Gyunny");

EntityManager em = emf.createEntityManager();
em.getTransacntion().begin();

// 객체를 저장한 상태(영속)
em.persist(member);
```

위와 같이 `em.persist(member)`를 통해서 엔티티 매니저 안에 있는 영속성 컨텍스트 안에 멤버가 들어가면서 `영속 상태`가 됩니다. 하지만 이 때 DB에 바로 저장되는 것은 아닙니다.
실제로 그런지 코드를 실행해보면서 확인해보겠습니다.

![스크린샷 2021-08-25 오전 7 05 22](https://user-images.githubusercontent.com/45676906/130696342-3121c2b2-aacc-42dc-bd67-97b068a5f4d4.png)

![스크린샷 2021-08-25 오전 7 06 58](https://user-images.githubusercontent.com/45676906/130696485-4529c14c-ea23-420c-88f2-7aae0ee96bf6.png)

위의 사진을 보면 `em.persist()`를 했다고 바로 쿼리가 실행되지 않는 것을 확인할 수 있습니다. 즉, 쿼리는 `트랜잭션이 commit() 되는 시점에 실행된다고 생각할 수 있습니다.`

<br> <br>

## `준영속, 삭제`

```java
// 회원 엔티티를 영속성 컨텍스트에서 분리, 준영속 상태
em.detach(member);

// 객체를 삭제한 상태(삭제)
em.remove(member);
```

<br> <br>

## `영속성 컨텍스트의 이점`

- 1차 캐시
- 동일성(identity) 보장
- 트랜잭션을 지원하는 쓰기 지원(Transactional write-behind)
- 변경 감지(Dirty Checking)
- 지연 로딩(Lazy Loading)

<br>

영속성 컨텍스트를 사용하면 위와 같은 이점들을 얻을 수 있는데요. 하나씩 어떤 의미를 가지고 있는지 좀 더 자세히 알아보겠습니다.

<br> <br>

## `엔티티 조회, 1차 캐시`

<img width="703" alt="스크린샷 2021-08-25 오전 7 13 32" src="https://user-images.githubusercontent.com/45676906/130697054-93744483-9c26-4764-8864-18a645a130ba.png">

```java
// 엔티티를 생성한 상태 (비영속)
Member member = new Member();
member.setId(1L);
member.setUsername("Gyunny");

// 엔티티를 영속
em.persist(member);   
```

영속성 컨텍스트는 내부에 캐시를 가지고 있는데 이것을 `1차 캐시`라고 합니다. 영속 상태의 엔티티는 모두 이곳에 저장됩니다. 즉, 영속성 컨텍스트 내부에 Map이 하나 있는데 키는 @Id로 매핑한 식별자이고 값은 엔티티 인스턴스입니다.

<br> <br>

## `1차 캐시에서 조회`

```java
// 엔티티를 생성한 상태 (비영속)
Member member = new Member();
member.setId(1L);
member.setUsername("Gyunny");

// 엔티티를 영속
em.persist(member);   

// 1차 캐시에서 조회
Member findMember = em.find(Member.class, "1L");
```

<img width="980" alt="스크린샷 2021-08-25 오전 7 19 18" src="https://user-images.githubusercontent.com/45676906/130697561-1b0c585c-0a4b-4c0c-ab57-82278ba751a2.png">

em.find()를 호출하면 우선 1차 캐시에서 식별자 값으로 엔티티를 찾습니다.

<br> <br>

## `만약 1차 캐시에 없는 2번 Member를 조회한다면?`

```java
Member findMember2 = em.find(Member.class, 2L);
```

<img width="1471" alt="스크린샷 2021-08-25 오전 7 21 24" src="https://user-images.githubusercontent.com/45676906/130697770-f840afea-6b3c-4f4a-a146-67a7c5227e64.png">

만약 em.find()를 호출했는데 엔티티가 1차 캐시에 없으면 엔티티 매니저는 데이터베이스를 조회해서 결과로 나온 Member2를 1차 캐시에 저장한 후에 영속 상태의 엔티티를 반환합니다.
(한 트랜잭션 안에서만 효과가 있기 때문에 막 그렇게 성능의 이점이 있지는 않음!)

<br>

![스크린샷 2021-08-25 오전 7 26 57](https://user-images.githubusercontent.com/45676906/130698357-c7adabcf-5159-4d93-9877-180d76f35865.png)

위와 같이 `엔티티를 영속 상태`로 만든 후에 `em.find()`를 통해서 조회했을 때 실제로 1차 캐시에서 조회를 하는지 알아보겠습니다. 

<br>

![스크린샷 2021-08-25 오전 7 29 17](https://user-images.githubusercontent.com/45676906/130698478-d9f3c305-5a03-49cb-b06d-1e6dd6151364.png)

결과를 보면 `em.find()`를 했을 때 `SELECT` 쿼리가 실행되지 않은 것을 볼 수 있습니다. 즉, DataBase에서 조회한 것이 아니라 1차 캐시에서 조회해서 결과를 반환한 것을 알 수 있습니다. 

이번에는 위에서 말했던 것처럼 1차 캐시에 없는 멤버를 조회했을 때는 어떻게 되는지 알아보겠습니다. 

![스크린샷 2021-08-25 오전 7 35 31](https://user-images.githubusercontent.com/45676906/130698993-f223f7d5-a8b7-4aec-bc76-d2b76e103da4.png)

1번 멤버가 DB에 존재하는 상태로 1번 멤버를 두 번 find() 하는 코드입니다. 위 코드의 결과를 예측해보면 첫 번째 find()만 SELECT 쿼리가 실행되고 두 번째 find()는 1차 캐시에서 가져오기 때문에 SELECT 쿼리가 실행되지 않을 것이라 예상할 수 있습니다. 실제로 그런지 한번 실행해보겠습니다. 

![스크린샷 2021-08-25 오전 7 38 17](https://user-images.githubusercontent.com/45676906/130699148-3306a37b-742e-4678-b2dc-bf2b80bbf7c4.png)

결과를 보면 예상했던 대로 SELECT 쿼리가 한번만 실행된 것을 확인할 수 있습니다. (하지만 현업에선 그렇게~ 도움을 주진 않는다고 하는,,)

<br> <br>

## `영속 엔티티의 동일성 보장`

![스크린샷 2021-08-25 오전 7 39 49](https://user-images.githubusercontent.com/45676906/130699282-236b47f4-0e73-494a-ad1e-60456e90b8e7.png)

이번에는 find() 두번을 했을 때 각각을 `==` 으로 비교하면 어떤 결과가 나오게 될까요? 정답은 `true` 입니다. 위에서 계속 얘기했던 대로 1차 캐시의 원리를 이해한다면 같은 엔티티일 수 밖에 없다는 것을 알 수 있을 것입니다. 

<br>

![스크린샷 2021-08-25 오전 7 41 47](https://user-images.githubusercontent.com/45676906/130699439-9d807b2c-5654-401d-81dc-e4c27df19d95.png)

> 1차 캐시로 반복 가능한 읽기(REPEATABLE READ) 등급의 트랜잭션 격리 수준을 데이터베이스가 아닌 애플리케이션 차원에서 제공

어렵게 말하면 위와 같이 정리할 수 있습니다. 

<br> <br>

## `엔티티 등록 트랜잭션을 지원하는 쓰기 지연`

엔티티 매니저는 트랜잭션을 커밋하기 직전까지 데이터베이스에 엔티티를 저장하지 않고 내부 쿼리 저장소에 INSERT SQL을 차곡차곡 모아둡니다. 그리고 트랜잭션을 커밋할 때 모아둔 쿼리를 데이터베이스에 보내는데 이것을 `트랜잭션을 지원하는 쓰기 지연` 이라 합니다.

<img width="987" alt="스크린샷 2021-08-25 오전 7 45 21" src="https://user-images.githubusercontent.com/45676906/130699692-8ded3c99-0b25-415f-abe4-14d8aa6d9799.png">

```
em.persist(memberA);
```
만약에 위와 같이 memberA를 영속화 할 때 내부적으로 어떤일이 벌어지는지 좀 더 알아보겠습니다. 일단 MemberA가 1차 캐시에 저장이 됩니다. 그리고 JPA가 엔티티를 분석해서 INSERT 쿼리를 생성하여 쓰기 지연 SQL 저장소에 쌓아둡니다. 

<br>

<img width="982" alt="스크린샷 2021-08-25 오전 7 45 34" src="https://user-images.githubusercontent.com/45676906/130699711-a5185145-98db-4c0e-a2a1-3d202fc50312.png">

이번에도 MemberB를 영속화 하면 1차 캐시에 저장이 됩니다. 그리고 JPA가 엔티티를 분석해서 INSERT 쿼리를 생성하여 쓰기 지연 SQL 저장소에 저장이 됩니다.

<br> <br>

## `그러면 언제 데이터베이스에 저장이 될까요?`

<img width="1323" alt="스크린샷 2021-08-25 오전 7 50 02" src="https://user-images.githubusercontent.com/45676906/130700053-d526e753-10de-4034-accb-07945be9213a.png">

위에서 말했던 대로 `트랜잭션이 커밋`하는 시점인데요. 커밋하는 시점에 쓰기 지연 SQL 저장소에 있던 쿼리들이 `flush` 되면서 데이터베이스에 반영이 됩니다.

<br>

![스크린샷 2021-08-25 오전 7 52 29](https://user-images.githubusercontent.com/45676906/130700314-93486a0f-193e-4116-90d3-c4d36008aac9.png)

위와 같이 커밋하는 시점에 INSERT 쿼리 2번이 같이 실행되는지 확인해보겠습니다. 

<br>

![스크린샷 2021-08-25 오전 7 54 13](https://user-images.githubusercontent.com/45676906/130700419-f1f2eddb-31c5-4cdf-9cef-71cef3e72079.png)

결과를 보면 `====` 아래에 INSERT 쿼리가 실행되는 것을 확인할 수 있습니다.  

<br> <br>

## `엔티티 수정(변경 감지)`

![스크린샷 2021-08-25 오전 8 01 24](https://user-images.githubusercontent.com/45676906/130700997-b5794b5a-aa9b-4fba-9739-92df03fb7457.png)

이번에는 Member의 값을 변경하는 것을 해볼 것인데요. 변경하기 위해서 setter를 사용해서 이름을 바꿨습니다. 그리고 이번에는 `em.persist()`와 같은 코드를 사용하지 않았는데요. 
사용하지 않아도 UPDATE 쿼리가 잘 실행이 될까요? 사용하지 않아도 UPDATE 쿼리가 실행이 되는데요. 실행이 가능한 이유가 무엇일까요?

![스크린샷 2021-08-25 오전 8 04 52](https://user-images.githubusercontent.com/45676906/130701182-e72c2f70-65da-497b-8c9b-508b425a45c8.png)

<br>

## `변경 감지`

<img width="1357" alt="스크린샷 2021-08-25 오전 8 05 40" src="https://user-images.githubusercontent.com/45676906/130701214-c609992c-9c1d-495c-b08b-f83bea1908c1.png">

JPA는 데이터베이스를 커밋하는 시점에 내부적으로 `flush()` 라는 것이 호출됩니다. 그리고 `엔티티와 스냅샷을 비교`하는데요. 정확히 말하면 1차 캐시 안에는 `스냅샷`이라는 것도 존재합니다. 
스냅샷은 `최초로 영속성 컨텍스트 1차 캐시에 들어온 상태`를 저장해두는 것입니다. 

만약 위의 예제처럼 MemberA를 변경한 상태라면 트랜잭션 커밋하는 시점에서 내부적으로 스냅샷과 변경된 것을 다 비교해서 바뀐 것이 있다면 UPDATE 쿼리를 SQL 저장소에 만들어두는 것입니다. 

<br> <br>

## `엔티티 삭제`

```java
Member memberA = em.find(Member.class, 1L);
em.remove(MemberA);
```

위와 같이 `remove()`를 사용해서 Member를 삭제할 수 있습니다. 

<br> <br>

## `플러시(flush)`

`플러시(flush)`는 영속성 컨텍스트의 변경 내용을 데이터베이스에 반영합니다. 플러시를 실행하면 구체적으로 아래와 같은 일들이 발생합니다.

- 변경 감지가 동작해서 영속성 컨텍스트에 있는 모든 엔티티를 스냅샷과 비교해서 수정된 엔티티를 찾습니다. 수정된 엔티티는 수정 쿼리를 만들어 쓰기 지연 SQL 저장소에 등록합니다.
- 쓰기 지연 SQL 저장소의 쿼리를 데이터베이스에 전송합니다.

<br> <br>

## `영속성 컨텍스트를 플러시 하는 방법`

### em.flush()를 직접 호출합니다.

flush() 메소드를 직접 호출해서 영속성 컨텍스트를 강제로 플러시합니다. 테스트나 다른 프레임워크와 JPA를 함꼐 사용할 때를 제외하고 거의 사용하지 않습니다. 

![스크린샷 2021-08-25 오후 2 17 50](https://user-images.githubusercontent.com/45676906/130730847-2f70dc11-589b-4951-a1ec-c9c9689e88b2.png)

<br>

![스크린샷 2021-08-25 오후 2 18 42](https://user-images.githubusercontent.com/45676906/130730885-79ef3365-c8a9-4e31-996d-2e8c56b43235.png)

보면 flush() 메소드가 호출된 후에 바로 INSERT 쿼리가 실행되는 것을 확인할 수 있습니다. 

<br>

### flush() 하면 1차 캐시가 다 지워지나요?

1차 캐시는 그대로 유지됩니다. 쓰기 지연 저장소에 있는 쓰기 쿼리들이 데이터베이스에 반영이 되는 것이라고 생각하면 됩니다.

<br>

### 트랜잭션 커밋 시 플러시가 자동 호출됩니다.

데이터베이스에 변경 내용을 SQL로 전달하지 않고 트랜잭션만 커밋하면 어떤 데이터도 데이터베이스에 반영되지 않습니다. 따라서 트랜잭션을 커밋하기 전에 꼭 플러시를 호출해서 영속성 컨텍스트의 변경 내용을 데이터베이스에 반영해야 합니다.
JPA는 이런 문제를 예방하기 위해 트랜잭션을 커밋할 때 플러시를 자동으로 호출합니다.

<br>

### JPQL 쿼리 실행 시 플러시가 자동 호출됩니다.

```java
em.persist(memberA);
em.persist(memberB);
em.persist(memberC);

//중간에 JPQL 실행
query = em.createQuery("select m from Member m", Member.class);
List<Member> members= query.getResultList();
```

위와 같이 `memberA, memberB, memberC`를 3개 persist 한 후에 데이터베이스에서 조회하면 조회가 안될 것인데요.(디비에 반영이 안되었기 때문에..) 그래서 이런 것은 문제가 될 수 있기 때문에 JPQL은 디폴트로 쿼리가 실행될 때 flush가 호출되도록 되어 있습니다.

<br> <br>

## `준영속 상태`

영속성 컨텍스트가 관리하는 영속 상태의 엔티티가 영속성 컨텍스트에서 분리된(detached) 것을 `준영속 상태`라고 합니다.

<br>

### `준영속 상태로 만드는 방법`

- em.detach(entity): 특정 엔티티만 준영속 상태로 전환합니다.
- em.clear(): 영속성 컨텍스트를 완전히 초기화합니다.
- em.close(): 영속성 컨텍스트를 종료합니다.

<br>

![스크린샷 2021-08-25 오후 2 30 14](https://user-images.githubusercontent.com/45676906/130731916-550610de-b173-40fa-b114-38fcf77f14e5.png)

예제 코드를 보겠습니다. 위와 같이 1차 캐시에는 존재하지 않는 데이터베이스에 존재하면 100번 유저를 조회하면 1차 캐시에도 등록이 되게 되는데요. 이런 상태를 `영속 상태`라고 합니다. 즉, JPA가 엔티티를 관리하고 있는 상태라고 할 수 있는데요.
바로 아래 `em.detach(findMember)`를 통해서 엔티티를 `준영속 상태`로 만들면 해당 엔티티를 더이상 JPA가 관리를 하지 않게 됩니다.

즉, 트랜잭션 커밋을 하게 되어도 UPDATE 쿼리는 실행되지 않는다는 것을 생각할 수 있습니다. 

![스크린샷 2021-08-25 오후 2 34 15](https://user-images.githubusercontent.com/45676906/130732194-f4444997-17d7-4154-afa3-e6197b04f6a7.png)

위와 같이 `SELECT` 쿼리만 실행되었고 `UPDATE` 쿼리는 실행되지 않는 것을 볼 수 있습니다. 

<br>

![스크린샷 2021-08-25 오후 2 38 29](https://user-images.githubusercontent.com/45676906/130732677-1e4e672d-628a-4483-986f-7d86438462a6.png)

이번에는 `em.clear()` 메소드를 사용해보겠습니다. clear() 메소드는 영속성 컨텍스트를 완전히 초기화하는 메소드인데요. 일단 위에서 보았듯이 영속성 컨텍스트가 비워지기 때문에 UPDATE 쿼리는 실행되지 않는다는 것은 알겠는데, 100번 유저를 한번 더 조회하면 어떻게 될까요?

쉽게 예상할 수 있듯이 다시 디비에서 SELECT 쿼리를 통해 조회한 후에 1차 캐시에 올릴 것입니다. 즉, SELECT 쿼리가 총 2번 실행될 것이라는 것을 예측할 수 있습니다. 

![스크린샷 2021-08-25 오후 2 42 41](https://user-images.githubusercontent.com/45676906/130733026-86c6a426-c378-4f86-b263-e6e1318770bb.png)
