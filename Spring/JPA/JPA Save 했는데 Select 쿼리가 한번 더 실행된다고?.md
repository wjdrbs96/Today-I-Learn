## `JPA Save 메소드 호출했는데 Select 쿼리가 한번 더 실행된다고?`

이미 많은 곳에서 정리되어 있는 내용이지만 JPA save 메소드는 어떻게 동작하는지 간단하게 정리해보겠습니다.

```java
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
public class Member {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String part;

    public Member(String name, String part) {
        this.name = name;
        this.part = part;
    }
}
```

Member 엔티티가 PK는 `Auto Increment`가 적용되어 존재하는 상황입니다.

<br>

```java
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    public void save() {
        Member member = new Member("이름", "파트");
        memberRepository.save(member);
    }
}
```

그러면 위와 같이 `save()` 메소드 호출 한번 하면 끝인데, 쿼리는 어떻게 실행될까요?

```sql
Hibernate: 
    insert 
    into
        member
        (id, name, part) 
    values
        (default, ?, ?)
```

이거는 직관적으로 알 수 있게 `INSERT` 쿼리 한번만 실행되는 것을 알 수 있습니다.

<br>

```java
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    public void save() {
        Member member = new Member(1L, "이름", "파트");
        memberRepository.save(member);
    }
}
```

하지만 이번에는 Member가 Auto Increment가 적용되어 있지 않아서 save 할 때 PK 값을 직접 넣어주어야 한다면 쿼리는 어떻게 실행이 될까요? 

```sql
Hibernate: 
    select
        member0_.id as id1_0_0_,
        member0_.name as name2_0_0_,
        member0_.part as part3_0_0_ 
    from
        member member0_ 
    where
        member0_.id=?
Hibernate: 
    insert 
    into
        member
        (id, name, part) 
    values
        (default, ?, ?)

```

INSERT 하기 전에 SELECT 쿼리가 한번 더 호출되는 것을 볼 수 있는데요. 코드를 보면 조회한 적이 없는데 SELECT 쿼리가 나가는 것을 보면 JPA 라는 것은 참 신기하다는 생각이 드는데요. 이제 조회 쿼리가 실행되는 이유에 대해서 알아보겠습니다.

<br>

## `SELECT 쿼리는 왜 실행이 될까?`

<img width="499" alt="스크린샷 2022-07-03 오후 10 55 49" src="https://user-images.githubusercontent.com/45676906/177043009-53a10f27-a855-4934-aaaf-490e01c855b3.png">

save 메소드 내부 코드를 보면 entity id가 null이면 `persist()` 메소드를 호출하고, id가 null이 아니면 `merge()` 메소드를 호출하는 로직으로 되어 있습니다.

<br>

<img width="578" alt="스크린샷 2022-07-03 오후 11 04 41" src="https://user-images.githubusercontent.com/45676906/177043387-1a11ea80-ea89-417a-9850-1bc538993267.png">

`isNew()` 메소드를 보면 PK 존재 여부로 true/false를 반환하고 있는 것을 볼 수 있습니다.

`merge() 메소드는 영속성 컨텍스트에 해당 엔티티가 있나 확인하고 없으면 select로 엔티티를 가져오고, select로 반환되는 값이 없다면 해당 객체를 insert 합니다.` 

즉, merge() 메소드가 호출되었기 때문에 조회 쿼리가 한번 더 실행된 후에 저장이 된 것입니다.

<br>

### `이미 존재하는 PK로 save 한다면?`

```java
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    public void save() {
        Member member = new Member(1L, "이름변경", "파트변경");
        memberRepository.save(member);
    }
}
```

DB에 이미 PK가 1L인 엔티티가 존재한 상태에서 한번 더 save 메소드를 호출하면 어떤 쿼리가 실행될까요?

```sql
Hibernate: 
    select
        member0_.id as id1_0_0_,
        member0_.name as name2_0_0_,
        member0_.part as part3_0_0_ 
    from
        member member0_ 
    where
        member0_.id=?
Hibernate: 
    update
        member 
    set
        name=?,
        part=? 
    where
        id=?
```

이번에는 `UPDATE` 쿼리가 실행되는 것을 볼 수 있습니다. 즉, PK를 포함하여 Save 메소드를 호출하면 `UPSERT` 쿼리가 호출이 됩니다.(단, 엔티티 값이 변경되지 않았다면 Update 쿼리는 실행되지 않고 Select 쿼리가 실행됩니다.)

<br>

## `결론`

Auto Increment 설정된 테이블만 사용한다면 위와 같은 일이 일어나지 않겠지만.. 복합키를 사용하거나 Auto Increment를 사용하지 않는 경우면 위와 같은 상황이 생길 수 있기 때문에 유의하고 JPA를 사용하면 좋을 거 같습니다. 

<br>

## `Reference`

- [https://stackoverflow.com/questions/1069992/jpa-entitymanager-why-use-persist-over-merge](https://stackoverflow.com/questions/1069992/jpa-entitymanager-why-use-persist-over-merge)