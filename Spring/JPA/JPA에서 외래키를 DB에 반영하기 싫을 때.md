## `JPA에서 외래키를 DB에 반영하기 싫을 때`

```yaml
spring:
  h2:
    console:
      enabled: true
      path: /h2-console
  jpa:
    show-sql: true
    generate-ddl: false
    hibernate:
      ddl-auto: none
```

위처럼 `ddl-auto`를 `none` 으로 해놓으면 `DB` 설정에 `Entity` 설정이 반영되지 않는다.