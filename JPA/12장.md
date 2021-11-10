# `스프링 데이터 JPA`

```java
public class MemberRepository {

    @PersistenceContext
    public EntityManager em;

    public void save() {...}

    public Member findOne(Long id) {...}

    public List<Member> findAll() {...}

    public Member findByUsername(String username) {...}
}
```

JPA를 사용해도 대부분의 데이터 접근 계층은 CRUD라고 하는 코드를 반복해서 개발해야 하는 문제점이 존재합니다. 이러한 단점을 해결하기 위해 나온 것이 `Spring Data JPA` 입니다. 

<br> <br>

## `Spring Data JPA 소개`

`Spring Data JPA`는 스프링 프레임워크에서 JPA를 편리하게 사용할 수 있도록 지원하는 프로젝트입니다. 데이터 접근 계층을 개발할 때 위에서 보았던 단순하고 반복적인 CRUD 작업들을 멋지게 해결합니다. 레포지토리를 개발할 때 인터페이스만 작성하면 실행 시점에 `Spring Data JPA`가 구현 객체를 동적으로 생성해서 주입해줍니다.

따라서 위의 코드처럼 하나하나 작성하지 않고, 구현 클래스 없이 인터페이스만 작성해도 개발을 완료할 수 있습니다. 

```java
public interface MemberRepository extends JpaRepository<Member, Long> {
    Member findByUsername(String username);
}
```

즉, CRUD를 처리하기 위한 공통 메소든느 `Spring Data JPA`가 제공하는 `org.springframework.data.jpa.repository.JpaRepository 인터페이스`에 있습니다. 인터페이스의 구현체는 애플리케이션 실행 시점에 `Spring Data JPA`가 생성해서 주입해줍니다. 

일반적인 `CRUD` 메소드는 `JpaRepository`에서 제공해주지만, 위의 보이는 `findByUsername`와 같이 공통으로 처리할 수 없는 메소드는 개발자가 직접 작성해야 하는데요. `Spring Data JPA`는 메소드 이름을 분석해서 아래와 같은 JPQL을 만들어줍니다. 

```jpaql
SELECT m FROM Member m WHERE username =:username
```

<br> <br>

## `Spring Data JPA 프로젝트`

![스크린샷 2021-11-10 오후 3 44 37](https://user-images.githubusercontent.com/45676906/141063372-237317e3-4a25-4bd9-965e-ac1587f99027.png)

`Spring Data 프로젝트`는 JPA, MongoDB, NEO4J, Redis, HADDOP, GEMFIRE 같은 다양한 데이터 저장소에 대한 접근을 추상화해서 개발자 편의를 제공하고 지루하게 반복하는 데이터 접근 코드를 줄여줍니다. 

<br> <br>

## `공통 인터페이스 기능`

`Spring Data JPA`는 간단한 CRUD 기능을 공통으로 처리하는 `JpaRepository` 인터페이스를 제공합니다. 

![스크린샷 2021-11-10 오후 3 55 48](https://user-images.githubusercontent.com/45676906/141064807-c666a043-0bdb-4458-bdba-c9f2d87821fc.png)

`JpaRepository` 인터페이스의 계층 구조는 위와 같습니다. 위의 그림을 보면 `CrudRepository` 부터 `Spring Data` 영역이고 그 아래부터는 `Spring Data JPA` 영역입니다. 

- `save(S)`: 새로운 엔티티는 저장하고 이미 있는 엔티티는 수정합니다.
- `delete(T)`: 엔티티 하나를 삭제합니다. 내부에서 `EntityManager.remove()`를 호출합니다.
- `findOne(ID)`: 엔티티 하나를 조회합니다. 내부에서 `EntityManager.find()`를 호출합니다. 
- `getOne(ID)`: 엔티티를 프록시로 조회합니다. 내부에서 `EntityManager.getReference()`를 호출합니다.
- `findAll(...)`: 모든 엔티티를 조회합니다. 정렬이나 페이징 조건을 파라미터로 제공할 수 있습니다. 

<br> 

위의 메소드는 `JpaRepository`를 상속받으면 사용할 수 있는 주요 메소드 몇 가지를 정리하였습니다. 

<br> <br>

## `Query Method 기능`

쿼리 메소드 기능은 `Spring Data JPA`가 제공하는 마법 같은 기능입니다. 

- 메소드 이름으로 쿼리 생성
- 메소드 이름으로 JPA NamedQuery 호출
- @Query 애노테이션을 사용해서 레포지토리 인터페이스에 쿼리 직접 정의

<br>

`Spring Data JPA`가 제공하는 쿼리 메소드 기능은 크게 3가지가 있습니다. 하지만 여기서는 1, 3번 두 가지만 알아보겠습니다.

<br> <br>

## `메소드 이름으로 쿼리 생성`

```java
public interface MemberRepository extends JpaRepository<Member, Long> {
    List<Member> findByEmailAndName(String email, String name);
}
```

인터페이스에 정의한 `findByEmailAndName` 메소드를 실행하면 `Spring Data JPA`는 메소드 이름을 분석해서 `JPQL`을 생성하고 실행합니다. 

```jpaql
SELECT m FROM Member m WHERE m.email = ?1 and m.name = ?2
```

실행되는 `JPQL`은 위와 같습니다. 

<br> <br>

## `@Query Repository 메소드에 쿼리 정의`

```java
public interface MemberRepository extends JpaRepository<Member, Long> {

    @Query("select m from Member m where m.username = ?1")
    Member findByEmailAndName(String username);
}
```

위와 같이 리포지토리 메소드에 직접 쿼리를 정의하려면 `@Query` 애노테이션을 사용하면 됩니다. 이 방법은 실행할 메소드에 정적 쿼리를 직접 작성하므로 이름 없는 `Named` 쿼리라도 가능합니다. 

<br>

### `네이티브 쿼리 작성`

```java
public interface MemberRepository extends JpaRepository<Member, Long> {

    @Query("select m from Member m where m.username = ?1", nativeQuery = ture)
    Member findByEmailAndName(String username);
}
```

위와 같이 `nativeQuery = true`를 설정하면 `Native SQL`을 사용할 수 있습니다. 

<br> <br>

## `페이징과 정렬`

`Spring Data JPA`는 쿼리 메소드에 페이징과 정렬 기능을 사용할 수 있도록 2가지 특별한 파라미터를 제공합니다. 

- `org.springframework.data.domain.Sort`: 정렬 기능
- `org.springframework.data.domain.Pageable`: 페이징 기능

<br>

`Pageable`을 사용하면 반환 타입으로 List나 `Page`를 사용할 수 있습니다. 

<br>

```java
// Count 쿼리 사용
Page<Member> findByName(String name, Pageable pageable);

// count 쿼리 사용 안 함
List<Member> findByName(String name, Pageable pageable)

List<Member> findByName(String name, Sort sort);
```

위와 같이 반환 타입을 `Page`를 사용하면 페이징 기능을 제공하기 위해 검색된 전체 데이터 건수를 조회하면 count 쿼리를 추가로 호출합니다. `Spring Data JPA`에서 페이징을 사용하는 방법에 대해서 정리는 다른 글에서 하였는데 궁금하다면 [여기](https://github.com/wjdrbs96/Today-I-Learn/blob/master/Spring/JPA/Spring%20Data%20JPA%EB%A1%9C%20%ED%8E%98%EC%9D%B4%EC%A7%95%20%EA%B5%AC%ED%98%84%ED%95%98%EB%8A%94%20%EB%B2%95.md) 에서 확인할 수 있습니다.