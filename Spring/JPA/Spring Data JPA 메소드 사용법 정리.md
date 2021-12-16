# `Spring Data JPA 메소드 사용법 정리`

이번 글에서는 `Spring Data JPA`를 사용하면서 `Repository`에서 메소드 이름을 기반으로 쿼리를 만들 때 알게된 점이 있을 때마다 정리해놓으려 합니다. 

<br> <br>

## `Count 쿼리`

```java
public interface PostRepository extends JpaRepository<Post, Long> {
    
    Long countByUser(User user);
}
```

위와 같이 `Count` 쿼리는 `count` 이름으로 시작하게 메소드를 만들면 쿼리를 만들 수 있습니다. 

<br> <br>

## `_를 사용해서 객체 그래프 탐색하기`

`User` - `Post`는 `1: N` 관계 라고 가정하겠습니다. 그러면 `Post`에서 `User`를 `@ManyToOne`으로 가지고 있을텐데요. 

```java
public interface PostRepository extends JpaRepository<Post, Long> {
    
    Optional<Post> findByIdAndUser_id(Long postId, Long userId);
}
```

위와 같이 `User_id` 처럼 `_`를 사용하면 `User` 엔티티의 객체 그래프 탐색을 할 수 있습니다. 