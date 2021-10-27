## `Spring Redis Port, Host Tip`

```
implementation 'org.springframework.boot:spring-boot-starter-data-redis'
```

Spring에서 Redis를 사용할 때 위처럼 `starter-data-redis`를 사용해서 개발할 것입니다.

<br>

```java
@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String, Chat> redisTemplate() {
        RedisTemplate<String, Chat> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory());
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(Chat.class));
        return redisTemplate;
    }

}
```

여기서 위와 같이 `Redis Host`, `Port`를 지정하지 않고도 사용할 수 있는데요. 이렇게 지정하지 않으면, `Default`로 `host: localhost`, `port: 6379`가 설정되게 됩니다. 

```
spring:
  redis:
    host: test.apn2.cache.amazonaws.com
    port: 6379
```

그래서 저는 `application.yml`에 위와 같이 설정을 해주면 이것도 자동으로 읽어서 설정을 해주나? 라고 생각했습니다. 하지만 위와 같이 `application.yml`에는 적어놓고 이것을 Java 코드에서 Redis에 연결시켜주지 않으면 `locahost`, `6379`로만 잡히게 됩니다. 

<br> <br>

## `Redis Host, Port 주입받기`

```java
@Configuration
public class RedisConfig {

    @Value("${spring.redis.host}")
    private String host;

    @Value("${spring.redis.port}")
    private int port;

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory(host, port);
    }

    @Bean
    public RedisTemplate<String, Chat> redisTemplate() {
        RedisTemplate<String, Chat> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory());
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(Chat.class));
        return redisTemplate;
    }
}
```

즉, 위와 같이 `Redis Host, Port`를 주입 받은 후에 `LettuceConnectionFactory` 생성자에 연결시켜주어야 `application.yml`에 적은 것으로 적용이 됩니다.

어쩌면 당연한 얘기이지만, 오늘 실수 했던 것이라 기록하며 정리!!