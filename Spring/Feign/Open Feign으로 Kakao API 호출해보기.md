## `Oepn Feign으로 Kakao API 호출해보기`

```kotlin
dependencies {
    // Spring Cloud OpenFeign
    implementation("org.springframework.cloud:spring-cloud-starter-openfeign")
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:2021.0.2")
    }
}
```

위의 의존성을 추가해준다.

```kotlin
@EnableFeignClients(basePackages = ["kr.spring.nada"])
@Configuration
class FeignClientConfig
```

- `@EnableFeignClients` 어노테이션을 사용해서 활성화 시켜준다. 

<br>

```yaml
infra:
  client:
    kakao:
      profile:
        base-url: https://kapi.kakao.com
        uri: /v2/user/me

```

<br>

```kotlin
@FeignClient(
    name = "KakaoLoginApiClient",
    url = "\${infra.client.kakao.profile.base-url}",
    configuration = [KaKaoFeignConfig::class]
)
interface KaKaoLoginApiClient {

    @GetMapping("\${infra.client.kakao.profile.uri}")
    fun getProfileInfo(@RequestHeader("Authorization") accessToken: String): KaKaoLoginResponseDto
}
```

- Kakao AccessToken을 담아서 Kakao 서버로 API 요청을 하는 인터페이스이다.

<br>

```kotlin
class KaKaoFeignConfig {

    @Bean
    fun feignLoggerLevel(): Logger.Level {
        return Logger.Level.FULL;
    }

    @Bean
    fun errorDecoder(): ErrorDecoder {
        return KakaoApiErrorDecoder();
    }

    companion object {
        private class KakaoApiErrorDecoder : ErrorDecoder {
            override fun decode(methodKey: String?, response: Response?): Exception {
                val exception = errorStatus(methodKey, response)
                when (response?.status()) {
                    400 -> throw InvalidException("카카오 API 호출에 필요한 필수 값이 존재하지 않습니다. status: ${response.status()}, message: ${response.body()}")
                    403 -> throw InvalidException("카카오 API 호출에 필요한 토큰이 올바르지 않습니다. status: ${response.status()}, message: ${response.body()}")
                    else -> throw InternalServerException("카카오 API 호출 중 문제가 생겼습니다. status: ${response?.status()}, message: ${exception.message}")
                }
            }
        }
    }
}
```

- Feign Client로 Kakao 서버에 API 요청을 하다가 문제가 발생했을 때 정의하는 Config 파일이다.