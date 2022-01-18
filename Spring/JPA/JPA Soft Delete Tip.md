## `JPA Soft Delete Tip`

```java
@Where(clause = "deleted_yn=0")
@Entity
public class PostEntity {
    
    private Long id;
    
    private String title;
    
    private boolean deleted_yn;
    
}
```

위처럼 `@Where(clause = "deleted_yn=0")`를 추가하면 자동으로 `Where`절에 조건이 추가된다. `Soft Delete` 편하게 할 수 있다.