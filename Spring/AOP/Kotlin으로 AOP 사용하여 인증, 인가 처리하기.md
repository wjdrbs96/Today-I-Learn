## `Koltin으로 AOP 사용하여 인증, 인가 처리하기`

```
implementation("org.springframework.boot:spring-boot-starter-aop")
```

- aop 의존성 추가한다.

<br>

### `어노테이션 생성`

```kotlin
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class Auth
```

- 해당 어노테이션이 붙어 있으면 AOP 로직이 실행될 수 있도록 한다.

<br>

### `Aspect 로직 만들기`

```kotlin
@Aspect
@Component
class AuthAspect(
    private val httpServletRequest: HttpServletRequest,
    private val usersRepository: UsersRepository,
    private val jwtService: JwtService
) {

    @Around("@annotation(kr.spring.nada.infrastructure.aop.Auth)")
    fun authorization(pjp: ProceedingJoinPoint): Any {
        try {
            val accessToken = resolveToken(httpServletRequest)
                ?: throw UnAuthorizedException("Request Header에 AccessToken이 존재하지 않습니다.")

            val userId = jwtService.decode(accessToken)
                ?: throw UnAuthorizedException("AccessToken이 올바르지 않습니다. accessToken: $accessToken")

            val user = usersRepository.findByIdOrNull(userId)
                ?: throw BadRequestException("존재하지 않는 유저입니다. userId : $userId", ErrorCode.E400_NOT_EXIST)

            AuthContext.USER_CONTEXT.set(userId)
            return pjp.proceed(pjp.args)
        } catch (e: Throwable) {
            throw UnAuthorizedException("인증 과정에 문제가 발생했습니다.") // TODO Error 계층구조 전체적으로 점검 예정
        }
    }

    private fun resolveToken(request: HttpServletRequest): String? {
        val bearerToken = request.getHeader(AUTHORIZATION)
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(PREFIX_BEARER)) {
            return bearerToken.substring(7)
        }

        return null
    }

    companion object {
        private const val AUTHORIZATION = "Authorization"
        private const val PREFIX_BEARER = "Bearer "
    }
}
```

위의 로직은 `accessToken`을 `Request Header`에서 꺼내서 `인증/인가` 로직을 처리하는 것이다.

<br>

### `ThreadLocal 코드`

```kotlin
object AuthContext {

    val USER_CONTEXT: ThreadLocal<Long> = ThreadLocal()

    fun getUserIdByThreadLocal(): Long {
        USER_CONTEXT.get()?.let {
            return USER_CONTEXT.get()
        } ?: throw UnAuthorizedException(message = "인증 체크 하는 중에 문제가 발생했습니다.")
    }
}
```

