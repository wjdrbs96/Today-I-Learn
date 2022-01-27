## `JPA AttributeOverride Tip`

```java
@AttributeOverride(name = "id", column = @Column(name = "post_id"))
@Entity
public class Post {
   
     private Long id;
     private String name;
     private String title;
}
```

위와 같이 사용하면 `id` 컬럼이 `DB에 저장될 때는 post_id`로 저장된다.