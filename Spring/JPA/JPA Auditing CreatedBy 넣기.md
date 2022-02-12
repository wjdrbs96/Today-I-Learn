## `JPA Auditing CreatedBy 넣기`

```java
@EnableJpaAuditing
@Configuration
public class JpaAuditConfig {

    @Bean
    public AuditorAware<String> auditorProvider() {
        return () -> Optional.of(UUID.randomUUID().toString());
    }
}
```

위와 같이 사용하면 `createdBy 와 같이 ~By에 자동으로 값`이 들어간다.