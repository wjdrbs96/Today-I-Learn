# `Spring Data Redis로 자료구조 다루어 보기`

[Redis](https://devlog-wjdrbs96.tistory.com/374) 는 간단하게 말하면 `Key`-`Value` 형태의 영속성을 지원하는 인메모리 데이터 저장소입니다. 이번 글에서는 `Spring Data Redis`를 활용해서 `List`, `Set`, `Sorted Set`, `Map`을 사용해서 데이터를 저장하고 조회하는 것에 대해서 정리해보겠습니다.

먼저 `Spring Boot gradle` 기반의 프로젝트에서 의존성 부터 추가해서 시작해보겠습니다. 

## `gradle`

```
implementation 'org.springframework.boot:spring-boot-starter-data-redis'
```

위의 `Spring-data-redis` 의존성을 추가하겠습니다. 이제 Redis 설정을 해보겠습니다. 

<br> <br>

## `Redis 설정`

```java
@Configuration
public class RedisConfig {

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory();  // Lettuce 사용
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>(); 
        redisTemplate.setConnectionFactory(redisConnectionFactory());  
        redisTemplate.setKeySerializer(new StringRedisSerializer());   // Key: String 
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(String.class));  // Value: 직렬화에 사용할 Object 사용하기   
        return redisTemplate;
    }

}
```

먼저 `RedisConnectionFactory` 인터페이스 하위 클래스에는 `LettuceConnectionFactory`, `JedisConnectionFactory` 두 가지가 존재합니다. `Lettuce`와 `Jedis` 둘 다 정말 유명한 것이지만 [Jedis 보단 Lettuce를 쓰자](https://jojoldu.tistory.com/418) 의 글을 보면 Lettuce의 성능이 좀 더 좋은 것을 볼 수 있기에 저는 `Lettuce`를 사용하겠습니다.

그리고 다음에 `RedisTemplate`이 나오는데요. `RedisTemplate`은 Redis 서버에 Redis 커맨드를 수행하기 위한 `high-level-abstractions`을 제공합니다. 또한 `Object 직렬화`, `Connection management`를 수행합니다.
또한 Redis 서버에 데이터 CRUD를 위해, Redis의 다섯가지 데이터 유형에 대한 `Operation Interface`를 제공합니다.

| 메소드명 | 반환 오퍼레이션 | Redis 자료구조 |
|------|---|---|
| opsForValue() | ValueOperations | String |
| opsForList() | ListOperations | List |
| opsForSet() | SetOperations | Set |
| opsForZSet() | ZSetOperations | Sorted Set |
| opsForHash() | HashOperations | Hash |

- RedisTemplate: RedisTemplate은 Thread-Safe 하며 재사용이 가능합니다.

- JacksonJsonSerializer: JSON 포맷으로 데이터를 저장하는 경우

<br>

이렇게 RedisTemplate과 이것을 사용하기 위한 `Operation Interface`들에 대하여 알아보았습니다. 이제 실제로 `List`, `Set`, `Sorted Set`, `Hash`를 Redis에 저장하는 것을 코드로 예제를 진행해보겠습니다. 

<br> <br>

## `Redis List Type 예제`

```java
private final RedisTemplate<String, Object> redisTemplate;

redisTemplate.opsForList().rightPush("key", "value");
RedisOperations<String, Object> operations = redisTemplate.opsForList().getOperations();
System.out.println(operations.opsForList().range("chatNumber" + idx, 0, -1));  // Redis Data List 출력
```

위와 같이 Redis에 `Key`: `List` 형태로 저장을 할 수 있습니다. 그리고 range 메소드를 통해서 List의 Start, end 인덱스를 통하여 가져올 수도 있습니다. 

<br> <br>

## `Redis Set Type 예제`

```java
private final RedisTemplate<String, Object> redisTemplate;

SetOperations<String, Object> setOperations = redisTemplate.opsForSet();
setOperations.add("Key", chatMessage);
System.out.println(setOperations.pop("Key"));       // 하나 꺼내기
System.out.println(setOperations.members("Key"));  // 전체 조회
```

Set은 add, pop 메소드를 사용하여 저장, 조회를 합니다.

<br> <br>

## `Redis Sorted Set Type 예제`

```java
private final RedisTemplate<String, Object> redisTemplate;

ZSetOperations<String, Object> zSetOperations = redisTemplate.opsForZSet();
zSetOperations.add("Key", "Value", score);
System.out.println(zSetOperations.range("ZKey", 0, -1));
```

위와 같이 `Sorted Set`도 손 쉽게 사용할 수 있습니다. 

<br> <br> 

## `Redis Hash Type 예제`

메모리 공간을 작게 차지함으로, 수백만개의 오브젝트를 하나의 Redis 인스턴스에 저장 가능합니다. 

```java
HashOperations<String, Object, Object> hashOperations = redisTemplate.opsForHash();
Map<String, Object> map = new HashMap<>();
map.put("firstName", "Gyunny");
map.put("lastName", "Choi");
map.put("gender", "Man");
hashOperations.putAll("key", map);

String firstName = (String) redisTemplate.opsForHash().get("key", "firstName");
String lastName = (String) redisTemplate.opsForHash().get("key", "lastName");
String gender = (String) redisTemplate.opsForHash().get("key", "gender");
System.out.println(firstName);
System.out.println(lastName);
System.out.println(gender);
```

이렇게 Redis의 여러 가지 자료구조를 사용하여 데이터를 저장하고 조회하는 기능들에 살펴보았습니다. 