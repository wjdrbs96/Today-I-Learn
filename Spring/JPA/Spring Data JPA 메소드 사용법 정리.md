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