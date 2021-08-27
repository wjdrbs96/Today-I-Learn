# `JPA 영속성 컨텍스트`

이번 글에서는 JPA에서 정말 중요한 `영속성 컨텍스트`에 대해서 알아보겠습니다. 

<br>

## `JPA에서 가장 중요한 2가지`

- 객체와 관계형 데이터베이스 매핑하기

- `영속성 컨텍스트`

<br>

## `EntityManager란?`

<img width="1201" alt="스크린샷 2021-07-05 오후 10 00 23" src="https://user-images.githubusercontent.com/45676906/124475202-67443900-dddc-11eb-9c5a-f2b5670569d7.png">

유저가 요청을 보낼 때마다 EntityManager를 생성하고 DB Connection을 연결해서 어떤 일련의 동작들이 일어나게 됩니다.  

<br>

## `영속성 컨텍스트`

- JPA를 이해하는데 가장 중요한 용어

- `Entity를 영구 저장하는 환경`이라는 뜻

- EntityManager.persist(entity);

- 영속성 컨텍스트는 논리적인 개념

- EntityManager를 통해서 `영속성 컨텍스트`에 접근

<br>

<img width="977" alt="스크린샷 2021-07-05 오후 10 04 11" src="https://user-images.githubusercontent.com/45676906/124475667-efc2d980-dddc-11eb-893e-75bbba54279b.png">

EntityManager 안에 `영속성 컨텍스트` 라는 논리적인 공간이 생긴다고 생각하면 좋습니다. 

<br>

## `엔티티의 생명주기`

### `비영속 (new/transient)`

- 영속성 컨텍스트와 전혀 관계가 없는 `새로운` 상태

<br>

### `영속 (managed)`

- 영속성 컨텍스트에 관리되는 상태

<br>

### `준영속 (detached)`

- 영속성 컨텍스트에 저장되었다가 `분리`된 상태

<br>

### `삭제 (removed)`

- `삭제`된 상태

<img width="1129" alt="스크린샷 2021-07-05 오후 10 07 02" src="https://user-images.githubusercontent.com/45676906/124476015-55af6100-dddd-11eb-8f9d-17c11abdbc29.png">


<br>

### `비영속(new/transient)`

- 영속성 컨텍스트와 전혀 관계가 없는 새로운 상태

<img width="1099" alt="스크린샷 2021-07-05 오후 10 07 30" src="https://user-images.githubusercontent.com/45676906/124476072-652eaa00-dddd-11eb-91b2-9183da6b546f.png">

```java
//객체를 생성한 상태(비영속)
Member member = new Member();
member.setId("member1");
member.setUsername("회원1");
```

<br>

### `영속 (managed)`

<img width="739" alt="스크린샷 2021-07-05 오후 10 08 25" src="https://user-images.githubusercontent.com/45676906/124476199-868f9600-dddd-11eb-93d6-25d2faf3dbf7.png">

```java
//객체를 생성한 상태(비영속)
Member member = new Member();
member.setId("member1");
member.setUsername(“회원1”);
EntityManager em = emf.createEntityManager();
em.getTransaction().begin();

 //객체를 저장한 상태(영속)
em.persist(member);
```

위와 같이 `persist()`를 통해서 `객체를 영속성 컨텍스트`에 넣게 되면 `영속` 상태가 됩니다. 이제 영속성 컨텍스트에 의해서 객체가 관리되는 것입니다. 

여기서 한가지 더 알아두어야 할 점은 `persist()`를 호출했다고 해서 `쿼리`가 실행되는 것이 아니라는 것입니다. 예제 코드를 보겠습니다. 

```java
public class JpaMain {

    public static void main(String[]  args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            Member member = new Member(50L, "HellJPA");
            System.out.println("===Before===");
            em.persist(member);
            System.out.println("===After===");
            
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }

        emf.close();

    }
}
```

위 코드를 실행시키면 결과는 아래와 같습니다. 

<br>

```
===Before===
===After===
Hibernate: 
    /* insert gyun.Member
        */ insert 
        into
            Member
            (name, id) 
        values
            (?, ?)
```

즉, `persist()` 일 때 쿼리가 실행되는 것이 아니라 트랜잭션이 `commit` 될 때 쿼리가 실행되는 것을 볼 수 있습니다. 

<br>

<br>

### `준영속, 삭제`

```java
//회원 엔티티를 영속성 컨텍스트에서 분리, 준영속 상태
em.detach(member);

// 객체를 삭제한 상태 (삭제) => DB 삭제를 요청한 상태
em.remove(member);
```

<br>

## `영속성 컨텍스트의 이점`

- 1차 캐시

- 동일성(identity) 보장

- 트랜잭션을 지원하는 쓰기 지연

- 변경 감지(Dirty Checking)

- 지연 로딩(Lazy Loading)