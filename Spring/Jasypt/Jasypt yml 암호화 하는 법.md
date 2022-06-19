## `Jasypt를 사용해서 yml 암호화 하는 법`

`Spring`을 사용하면 `application.yml`에 `DB`와 같은 중요 정보를 적게 되어 있습니다. `DB`에 접속할 수 있는 정보를 그대로 노출하여 Github에 노출할 수 없기 때문에 `application.yml` 파일을 `.gitignore`에 등록하여 Github에 올리지 않는 방식을 사용하기도 했습니다.

하지만, `Github Action` 같은 CI 도구를 사용한다면 결국 application.yml 설정에 들어가는 값들을 따로 다시 어디선가 주입해주어야 한다는 번거로움이 존재합니다.

즉, 이번 글에서는 `application.yml`의 중요한 정보들을 `Jasypt`를 사용하여 암호화 하는 법에 대해서 알아보겠습니다.

`Jasypt`를 사용하여 암호화를 한다면 위와 같이 `ENC(암호화된 문자)` 형태로 저장할 수 있습니다.

<br>

## `Jasypt 적용하기`

```
implementation "com.github.ulisesbocchio:jasypt-spring-boot-starter:3.0.4"
```

build.gradle에 위의 의존성을 추가하겠습니다.

<br>


<img width="1037" alt="스크린샷 2022-06-19 오후 10 32 34" src="https://user-images.githubusercontent.com/45676906/174483648-05703aee-b0e1-4fc3-aa1d-c34705b131e8.png">

```
jasypt.encryptor.password=dasdas
```

위의 형태로 원하는 암호화 키 문자열을 인텔리제이 환경변수에 등록하겠습니다. 등록해놓으면 자동으로 이 키 값을 찾아서 `jasypt`에서 `ENV(암호화문자열)`을 복호화를 진행해주는 것 같습니다.

<br>

```java
@Disabled
@ContextConfiguration(classes = {
    JasyptConfig.class
})
@SpringBootTest
public class JasyptConfigTest {

    @Value("${jasypt.encryptor.password}")
    private String jasyptEncryptorPassword;

    @Autowired
    private ConfigurableEnvironment configurableEnvironment;

    private DefaultLazyEncryptor encryptor;

    @BeforeEach
    void setUp() throws Exception {
        System.out.println(jasyptEncryptorPassword);
        if(StringUtils.isBlank(jasyptEncryptorPassword)) {
            throw new Exception("jasypt.encryptor.password must not be null, empty or blank.");
        }
        encryptor = new DefaultLazyEncryptor(configurableEnvironment);
    }

    @ParameterizedTest
    @MethodSource("stringForEncrypt")
    void encryptTest(String source) {
        System.out.println("source: " + source);
        System.out.println("encrypted: " + encryptor.encrypt(source));
        assertThat(true).isTrue();
    }

    private static Stream<Arguments> stringForEncrypt() { // argument source method
        return Stream.of(
                Arguments.of("Test!")
        );
    }
}
```

![스크린샷 2022-06-19 오후 10 31 02](https://user-images.githubusercontent.com/45676906/174483594-153469bf-b0a8-41a5-9917-53120b6357f9.png)

그리고 위의 코드처럼 `암호화 Key`를 바탕으로 `Jasypt`로 암호화 할 대상을 암호화 한 문자열을 얻을 수 있습니다. 암호화된 문자열을 `application.yml`에 넣고 실행하면 됩니다.