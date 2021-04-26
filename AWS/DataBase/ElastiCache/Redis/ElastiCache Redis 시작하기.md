# `AWS ElastiCache 시작하기`

이번 글에서는 `AWS ElastiCache`를 생성하고 아주 간단한 실습을 하는 것을 정리해보겠습니다. 

![스크린샷 2021-04-26 오후 4 50 37](https://user-images.githubusercontent.com/45676906/116047923-925d2e80-a6af-11eb-8b2e-67e475ee3f83.png)

이번 실습에서는 `Redis`를 사용해서 할 것이기 때문에 `Redis`를 체크하겠습니다.

<br>

![스크린샷 2021-04-26 오후 3 47 35](https://user-images.githubusercontent.com/45676906/116041177-de0bda00-a6a7-11eb-94a5-9a2cd5ff9e13.png)

위의 `노드 유형`을 `t2.micro(프리티어)`로 해야 합니다! (Default로 되어 있는 거 쓰면 좋은 성능이라 요금 많이 나옵니다..)

<br>

![스크린샷 2021-04-26 오후 3 47 41](https://user-images.githubusercontent.com/45676906/116042925-f846b780-a6a9-11eb-8a1f-ede35d2d8859.png)

보안그룹은 따로 생성해서 지정해주어도 되고 기존이 6379 포트가 열려있는 보안그룹이 있다면 그것을 사용해도 됩니다.(저도 열려있는 보안그룹이 있기 때문에 그것을 사용하겠습니다.)

<br>

![스크린샷 2021-04-26 오후 3 50 23](https://user-images.githubusercontent.com/45676906/116042929-faa91180-a6a9-11eb-823e-e0e82705c4fe.png)

그리고 나머지는 `Default`로 놓고 생성하겠습니다. 그러면 위와 같이 생성이 되는데 5분 정도 기다려야 생성이 완료됩니다.

<br> <br>

## `EC2 접속 후 redis-cli 설치하기`

```
sudo yum install -y gcc

# redis-cli 다운로드 & 설치
wget http://download.redis.io/redis-stable.tar.gz && tar xvzf redis-stable.tar.gz && cd redis-stable && make
```

<br>

### `ElastiCache 접속하기`

```
redis-cli -h 본인의엔드포인트 -p 6379
ex) redis-cli -h test-redis.7ab7ab.ab.0001.apn2.cache.amazonaws.com -p 6379 
```

![스크린샷 2021-04-26 오후 4 23 34](https://user-images.githubusercontent.com/45676906/116044600-14e3ef00-a6ac-11eb-9b51-072fa861faac.png)

<br>

`ElastiCache`를 들어가면 위와 같이 `엔드포인트`가 존재합니다. 여기서 `primary`의 엔드포인트를 사용하여 접속하겠습니다.

<br>

![스크린샷 2021-04-26 오후 4 27 37](https://user-images.githubusercontent.com/45676906/116044882-69876a00-a6ac-11eb-99ee-ab2a388f5eac.png)

위와 같이 `Redis-cli`를 사용하여 `ElastiCache`를 접속해서 간단한 테스트를 해보았습니다. 

<br>

## `Spring Boot에서 Redis 간단한 실습`

간단하게 아키텍쳐로 표현하면 아래와 같습니다.

![스크린샷 2021-04-26 오후 5 48 27](https://user-images.githubusercontent.com/45676906/116055389-9bea9480-a6b7-11eb-84a8-eab4207fac01.png)

<br> 

위의 아키텍쳐와 같이 실습을 진행해보겠습니다. 먼저 `Spring Boot Gradle` 기반의 프로젝트를 하나 만들겠습니다. 

<br>

### `build.gradle`

```yaml
dependencies {
    implementation 'org.springframework.boot:spring-boot-starter-data-redis'
}
```

그리고 `Spring-Data-Redis` 의존성을 추가하겠습니다. 그리고 Redis 설정 파일을 만들겠습니다. 

<br>

## `Redis config 생성`

```java
@Configuration
public class RedisConfig {

    @Value("${spring.redis.host}")
    private String redisHost;

    @Value("${spring.redis.port}")
    private int redisPort;

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        LettuceConnectionFactory lettuceConnectionFactory = new LettuceConnectionFactory(redisHost, redisPort);
        return lettuceConnectionFactory;
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory());
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        return redisTemplate;
    }
}
```

`LuttuceConnectionFactory`를 사용해서 Redis 서버와 연결을 하고, `RedisTemplate`을 사용해서 데이터를 직렬화해서 저장하고 꺼내오는 역할을 하는 거 같습니다.(아직 입문이라.. 틀릴 수도 있습니다 ㅠ,ㅠ)

<br>

## `application.yml`

```yaml
spring:
    redis:
      host: 자신의 엔드포인트
      port: 6379
```

위의 AWS ElasiCache의 엔드포인트를 적었다면 Github에 올리기 전에 꼭!! `gitignore` 등록을 하셔야 합니다. 

<br>

## `Controller 코드`

```java
@RequiredArgsConstructor
@RestController
public class RedisController {

    private final RedisService redisService;

    @GetMapping("/")
    public String test() {
        redisService.redisString();
        return "test";
    }
}
```

위와 같이 루트 경로의 간단한 API를 하나 만들겠습니다. 

<br>

## `Service 코드`

```java
@Slf4j
@RequiredArgsConstructor
@Service
public class RedisService {

    private final RedisTemplate<String, Object> redisTemplate;

    public void redisString() {
        ValueOperations<String, Object> operations = redisTemplate.opsForValue();
        operations.set("test", "test");
        String redis = (String)operations.get("test");
        log.info(redis);
    }
}
```

간단하게 Redis에 `key = test, value = test`로 저장을 하는 코드를 작성했습니다. 그리고 해당 프로젝트의 jar 파일을 EC2로 올리겠습니다. (Local에서는 ElastiCache가 접속이 안되어서 EC2에서 테스트를 해야합니다.)

```
./gradlew clean build
```

Jar를 EC2에 올리는 것은 FileZila를 사용했는데 이 과정은 생략하겠습니다. 

<br>

<img width="724" alt="스크린샷 2021-04-26 오후 5 14 32" src="https://user-images.githubusercontent.com/45676906/116050851-e9b0ce00-a6b2-11eb-82f0-cf12e0722332.png">

현재 EC2에 `redis.jar` 파일이 존재합니다. 이것을 실행시켜보겠습니다. 

```
nohup java -jar redis-0.0.1-SNAPSHOP.jar &
```

![스크린샷 2021-04-26 오후 5 16 24](https://user-images.githubusercontent.com/45676906/116051083-2f6d9680-a6b3-11eb-8b61-971f1ee53730.png)

그러면 위와 같이 `8080` 포트가 잘 실행되는 것을 볼 수 있습니다. 

<br>

![스크린샷 2021-04-26 오후 5 17 34](https://user-images.githubusercontent.com/45676906/116051276-5cba4480-a6b3-11eb-9a5a-d8626d6b389e.png)

그리고 EC2 IP의 8080 포트로 접속해보면 위와 같이 응답이 에러 없이 잘 뜨는 것을 볼 수 있습니다. 

<br>

EC2에서 다시 `Redis-cli`를 사용해서 `ElasiCache`에 접속해보겠습니다. 

<img width="821" alt="스크린샷 2021-04-26 오후 5 10 18" src="https://user-images.githubusercontent.com/45676906/116050491-7eff9280-a6b2-11eb-95a6-a6a0f10772c3.png">

접속한 후에 아까 `key=test, value=test`로 했기 때문에 `get test` 했을 때 value가 test로 잘 반환되는 것을 볼 수 있습니다. 즉, 잘 저장이 된 것입니다. 

이렇게 이번 글에서는 간단하게 `ElastiCache Redis` 실습을 해보았습니다. 