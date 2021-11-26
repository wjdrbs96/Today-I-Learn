# `Spring Data Redis TTL 사용하는 법`

이번 글에서는 `Spring Data Redis`에서 `TTL` 시간을 지정해서 시간이 지나면 `Expired` 되는 것을 구현해보는 것에 대해서 정리해보겠습니다. 

<br>

## `gradle`

```
implementation 'org.springframework.boot:spring-boot-starter-data-redis'
```

프로젝트는 `Java`, `Spring Boot`, `gradle` 기반으로 진행할 것입니다.

<br>

## `Redis TTL 적용하기`

`Redis`는 `Key`- `Value`로 이루어졌다는 특징을 가지고 있습니다. 즉, `Key`마다 `TTL`을 적용할 수 있는데요. 특정 시간이 지나면 `Expired` 되어야 하는 기능을 간단하게 구현해보겠습니다. 

![스크린샷 2021-11-27 오전 1 25 49](https://user-images.githubusercontent.com/45676906/143609432-9169efaa-552c-49aa-8679-9a838a628f69.png)

그래서 `Spring Data Redis`에서 보면 `RedisTemplate`의 자식 클래스인 `StringRedisTemplate`이 존재합니다. 그리고 `opsForValue()` 메소드로 `ValueOperations` 인터페이스를 얻을 수 있습니다. 

<br>

![스크린샷 2021-11-27 오전 1 27 44](https://user-images.githubusercontent.com/45676906/143609727-1d49a87b-b535-4d0e-b35a-dd2c8362c877.png)

위와 같이 `ValueOperations`를 통해서 저장하고자 하는 값을 `set`으로 저장하는데 저장할 때 `Duration` 으로 해당 `Key`의 만료 시간을 정해서 저장할 수 있습니다. 이런 기능을 사용하여 프로젝트에서 `이메일 인증 코드`를 `5분 동안만 사용할 수 있도록` 구현 하였습니다. 