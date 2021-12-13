# `Spring에서 JWT를 사용하는 법`

이번 글에서는 `Spring에서 JWT를 사용하는 방법`을 가볍게 알아보겠습니다.

<br>

## `build.gradle`

```
implementation 'io.jsonwebtoken:jjwt:0.9.1'
```

먼저 `Spring Boot`에 `build.gradle`에 위의 의존성을 추가하겠습니다. 

<br> <br>

## `JwtService 코드 작성하기`

```java
@RequiredArgsConstructor
@Service
public class JwtService {

    @Value("${jwt.access_token_secretKey}")
    private String accessTokenSecretKey;

    @Value("${jwt.refresh_token_secretKey}")
    private String refreshTokenSecretKey;

    @Value("${jwt.access_token_valid_time}")
    private Long accessTokenValidTime;

    @Value("${jwt.refresh_token_valid_time}")
    private Long refreshTokenValidTime;

    private final UserMapper userMapper;
    private final ObjectMapper objectMapper;

    private String createToken(final long payload, final String secretKey, final Long tokenValidTime) {
        return Jwts.builder()
                .setSubject(writeJsonAsString(payload))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .setExpiration(new Date(System.currentTimeMillis() + tokenValidTime))
                .compact();
    }

    private String writeJsonAsString(final Long payload) {
        try {
            return objectMapper.writeValueAsString(payload);
        } catch (JsonProcessingException e) {
            throw new JsonWriteException();
        }
    }

    public String createAccessToken(final long payload) {
        return createToken(payload, accessTokenSecretKey, accessTokenValidTime);
    }

    @Transactional
    public String createRefreshToken(final long payload) {
        var refreshToken = createToken(payload, refreshTokenSecretKey, refreshTokenValidTime);
        userMapper.upsertRefreshToken(refreshToken, payload);
        return refreshToken;
    }

    public TokenResponseDto createTokenResponse(final Long userId) {
        var accessToken = createAccessToken(userId);
        var refreshToken = createRefreshToken(userId);

        return TokenResponseDto.builder()
                .accessToken(accessToken)
                .accessTokenExpiredAt(getAccessTokenPayload(accessToken).getExpiredTime())
                .refreshToken(refreshToken)
                .refreshTokenExpiredAt(getRefreshTokenPayload(refreshToken).getExpiredTime())
                .build();
    }

    public TokenDto getAccessTokenPayload(final String accessToken) {
        return getPayload(accessToken, accessTokenSecretKey);
    }

    public TokenDto getRefreshTokenPayload(final String refreshToken) {
        return getPayload(refreshToken, refreshTokenSecretKey);
    }

    private TokenDto getPayload(final String token, final String secretKey) {
        var claims = Jwts.parser()
                .setSigningKey(DatatypeConverter.parseBase64Binary(secretKey))
                .parseClaimsJws(token)
                .getBody();

        try {
            return new TokenDto(objectMapper.readValue(claims.getSubject(), Long.class), claims.getExpiration());
        } catch (JsonProcessingException e) {
            throw new JsonWriteException();
        }
    }
}
```

위의 코드는 `DTO`, `User Define Exception Class`, `MyBatis Mapper`가 있는데 이 부분을 제외하고 `JWT` 사용하는 코드를 가지고 나머지는 사용 목적에 맞게 커스텀해서 사용하면 될 것 같습니다.

<br> <br>

## `JWT ValidTime, SecretKey yml`

```yaml
jwt:
  access_token_secretKey: adadapdawdaodpaasdasd123123dakwdpakdpakdwpdoadapwdkapdkapdkapdakdpar
  refresh_token_secretKey: 21312412ewadasdsadakwdpakdpakdwpdoadapwdkapdkapdkapdaDWKDL
  access_token_valid_time: 1800000000
  refresh_token_valid_time: 2592000000
```

위처럼 `SecretKey`, `Token Valid Time`을 사용하는 목적에 맞게 값을 설정하면 됩니다.