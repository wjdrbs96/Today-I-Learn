# `Entity 조회, 1차 캐시`

<img width="736" alt="스크린샷 2021-07-05 오후 10 24 12" src="https://user-images.githubusercontent.com/45676906/124478086-c35c8c80-dddf-11eb-985c-7646c8168390.png">

영속성 컨텍스트는 내부에 `1차 캐시` 라는 것을 가지고 있습니다. 

```java
public class JpaMain {

    public static void main(String[]  args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            Member member = new Member(50L, "HellJPA");
            
            // 엔티티를 영속
            em.persist(member);
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }
        
        emf.close();
    }
}
```

위와 같이 `persist()`를 통해서 객체를 영속 상태로 만들게 되면 1차 캐시에 저장이 됩니다. 이렇게 1차 캐시에 저장을 하면 어떤 이점이 있을까요?

<br>

```java
Member member = new Member(50L, "HellJPA");
em.persist(member);

Member findMember = em.find(Member.class, 50L);
tx.commit();
```

만약에 위와 같이 어떤 객체를 `영속 상태`로 만들고, 그 객체를 다시 조회했을 때 어떤 내부 과정을 거치게 될까요? 

<br>

<img width="995" alt="스크린샷 2021-07-05 오후 10 27 21" src="https://user-images.githubusercontent.com/45676906/124478460-2bab6e00-dde0-11eb-8d82-45faffc29a9d.png">

내부 과정을 보면 위와 같이 객체를 `영속 상태로 만들면 1차 캐시`에 저장이 되고, 그것을 다시 조회하게 되면 먼저 1차 캐시를 조회해서 바로 반환해주게 됩니다. 

<br>

<img width="1474" alt="스크린샷 2021-07-05 오후 10 31 22" src="https://user-images.githubusercontent.com/45676906/124478985-bb511c80-dde0-11eb-81fb-065a3e788c6d.png">

만약에 1차 캐시에 없는 내용을 조회한다면 위의 그림과 같은 로직으로 실행됩니다. 즉, 1차 캐시에 없다면 DB를 조회하고 조회 결과를 1차 캐시에 저장하고 클라이언트에게 반환해주는 것입니다. 

<br>

## `예제 코드`

```java
Member member = new Member(52L, "HellJPA");
em.persist(member);

Member findMember = em.find(Member.class, 52L);
System.out.println(findMember.getId());
System.out.println(findMember.getName());

tx.commit();
```

위와 같이 어떤 객체를 영속 상태로 만든 후에 바로 그 객체를 조회하게 되면 어떻게 될까요? 위에서 말했던 것처럼 1차 캐시에 등록이 된다면 INSERT 쿼리만 출력되고 SELECT 쿼리는 출력되지 않아야 합니다. 
예상대로 되는지 결과를 한번 알아보겠습니다. 

<br>

```
52
HellJPA
Hibernate: 
    /* insert gyun.Member
        */ insert 
        into
            Member
            (name, id) 
        values
            (?, ?)
```

위와 같이 출력되었는데요. 1차 캐시 때문에 실제 DB 조회를 하지 않아서 SELECT 쿼리가 실행되지 않은 것을 볼 수 있습니다. 

<br> <br>

## `Entity 등록 트랜잭션을 지원하는 쓰기 지연`

```java
EntityManager em = emf.createEntityManager();
EntityTransaction transaction = em.getTransaction();

//엔티티 매니저는 데이터 변경시 트랜잭션을 시작해야 한다.
transaction.begin(); // [트랜잭션] 시작
em.persist(memberA);
em.persist(memberB);

//여기까지 INSERT SQL을 데이터베이스에 보내지 않는다.
//커밋하는 순간 데이터베이스에 INSERT SQL을 보낸다.
transaction.commit(); // [트랜잭션] 커밋
```

<img width="1468" alt="스크린샷 2021-07-05 오후 10 51 26" src="https://user-images.githubusercontent.com/45676906/124481702-8a261b80-dde3-11eb-98d4-761deded16b1.png">

<img width="1458" alt="스크린샷 2021-07-05 오후 10 52 26" src="https://user-images.githubusercontent.com/45676906/124481829-acb83480-dde3-11eb-95b3-df8315a4cbd1.png">

위와 같이 `persist()`를 통해서 객체를 영속 상태로 만들 때 객체가 1차 캐시에 저장이 되고, `쓰기 지연 SQL 저장소`에 INSERT 쿼리가 저장이 됩니다.

<br>

<img width="1336" alt="스크린샷 2021-07-05 오후 10 53 16" src="https://user-images.githubusercontent.com/45676906/124481964-ca859980-dde3-11eb-984e-e903e272233f.png">

그리고 `commit()`하면 시점에 `쓰기 지연 SQL 저장소`에 있던 것들이 실제 DB에 반영이 됩니다. 

<br>

```java
Member member1 = new Member(300L, "HellJPA");
Member member2 = new Member(400L, "HelloMyBatis");

em.persist(member1);
em.persist(member2);

System.out.println("==================");

tx.commit();
```

위에서 말했던 것처럼 `member1`, `member2`를 모두 영속 상태로 만들게 되면 둘 다 1차 캐시에 저장이 되고 INSERT 쿼리는 `쓰기 지연 SQL 저장소`에 등록이 됩니다. 
그리고 `commit`이 되면 해당 SQL이 DB에 반영되는 로직인데요. 진짜로 그런지 한번 실행해서 확인해보겠습니다. 

<br> 

```
==================
Hibernate: 
    /* insert gyun.Member
        */ insert 
        into
            Member
            (name, id) 
        values
            (?, ?)
Hibernate: 
    /* insert gyun.Member
        */ insert 
        into
            Member
            (name, id) 
        values
            (?, ?)
```

결과를 보면 `=====` 이후에 INSERT 쿼리 2번이 실행된 것을 볼 수 있습니다. 이러한 `쓰기 지연 SQL 저장소`를 잘 사용한다면 성능을 올리는데 많은 도움이 됩니다. 

<br>

## `Entity 수정`

```java
Member member = em.find(Member.class, 300L);
member.setName("Gyunny Develop");

em.persist(member);

System.out.println("==================");
```

Entity의 값을 위와 같이 `setter`를 통해서 변경한다고 했을 때, 다시 persist() 메소드를 호출해야 하나? 라는 의문점이 들 수 있습니다. 
하지만 사용하지 않고 Setter만 사용해도 앞아서 Update 쿼리가 실행이 됩니다. 

<br>

```
Hibernate: 
    select
        member0_.id as id1_0_0_,
        member0_.name as name2_0_0_ 
    from
        Member member0_ 
    where
        member0_.id=?
==================
Hibernate: 
    /* update
        gyun.Member */ update
            Member 
        set
            name=? 
        where
            id=?
```

위와 같이 `Setter`로만 이름을 변경하였는데, Update 쿼리가 실행된 것을 볼 수 있습니다. 어떤 update 쿼리를 실행해라 라는 메소드 같은 것을 사용해야 될 거 같은데 사용하지 않아도
된다는게 신기한데, 원리는 아래와 같습니다.  

<br>

<img width="1379" alt="스크린샷 2021-07-05 오후 11 13 10" src="https://user-images.githubusercontent.com/45676906/124484607-9364b780-dde6-11eb-8189-6451f40852e7.png">

1차 캐시에는 값을 읽어오는 그 시점에 `스냅샷`을 생성해서 저장해놓습니다. 그래서 위처럼 Entity가 변경이 된다면 트랜잭션 Commit 시점에서 내부 어떤 로직에 따라서 스냅샷과 엔티티를 비교해서 변경된 것이 있다면
UPDATE 쿼리를 `쓰기 지연 SQL 저장소`에 저장을 하고 Update 쿼리를 DB에 반영하게 됩니다. 

<br>


