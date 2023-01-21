## `Rest Docs를 사용해보자.`

```
testImplementation("org.springframework.restdocs:spring-restdocs-mockmvc:2.0.6.RELEASE")
asciidoctorExtensions("org.springframework.restdocs:spring-restdocs-asciidoctor:2.0.6.RELEASE")
```

버전은 상황에 따라 다르겠지만 `Rest Docs` 의존성을 추가한다.

<br>

```kotlin
plugins {
    id("org.asciidoctor.jvm.convert") version "3.3.2"
}

// Multi Module 이라면 추가하기
subprojects {
    apply(plugin = "org.asciidoctor.jvm.convert")
}
```

`root build.gradle`에 위의 내용을 추가해준다.

<br>

```kotlin
val asciidoctorExtensions: Configuration by configurations.creating

tasks.test {
    outputs.dir("build/generated-snippets")
}

tasks.asciidoctor {
    inputs.dir("build/generated-snippets")
    dependsOn(tasks.test)
    configurations(asciidoctorExtensions.name)
    baseDirFollowsSourceFile()
}
```

- `build.gradle.kts`라면 위의 내용을 추가해준다.

<br>

```groovy
configurations {
    create("asciidoctorExtensions")
}

test {
    outputs.dir("build/generated-snippets")
}

asciidoctor {
    inputs.dir("build/generated-snippets")
    dependsOn(test)
    configurations "asciidoctorExtensions"
    baseDirFollowsSourceFile()
}
```

`build.gradle` 이라면 위의 내용을 추가한다.

위의 내용에 대해 설명하자면 `asciidoc` 결과를 `build/generated-snippets`에 생성하겠다는 뜻이다.

<br>

## `Controller Test`

```kotlin
@ExtendWith(RestDocumentationExtension::class)
@AutoConfigureRestDocs
@ActiveProfiles("test")
internal abstract class BaseRestDocsTest {

    @Autowired
    protected lateinit var mockMvc: MockMvc

    private val kotlinModule = KotlinModule.Builder()
        .withReflectionCacheSize(512)
        .configure(KotlinFeature.NullToEmptyCollection, false)
        .configure(KotlinFeature.NullToEmptyMap, false)
        .configure(KotlinFeature.NullIsSameAsDefault, false)
        .configure(KotlinFeature.SingletonSupport, false)
        .configure(KotlinFeature.StrictNullChecks, false)
        .build()

    protected val objectMapper: ObjectMapper = ObjectMapper()
        .registerModules(ParameterNamesModule(), kotlinModule, JavaTimeModule())
        .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
        .enable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
        .enable(SerializationFeature.WRITE_DATES_WITH_ZONE_ID)
        .setSerializationInclusion(JsonInclude.Include.NON_NULL)
}
```

중복으로 사용되는 코드에 대해서는 `BaseRestDocsTest` 라는 클래스를 만들어서 사용하였다.

<br>

```kotlin
@WebMvcTest(AuthController::class)
internal class SignupApiTest : BaseRestDocsTest() {

    @MockBean
    private lateinit var authService: AuthService

    @Test
    fun `Signup Up Test`() {

        val authRequestDTO = AuthRequestDTO(SocialType.KAKAO, "123")

        given(authService.signup(authRequestDTO))
            .willReturn(AuthResponseDTO("AccessToken"))

        // When
        val result = mockMvc.perform(
            RestDocumentationRequestBuilders
                .post("/api/v1/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(authRequestDTO))
        )

        // Then
        result.andExpect(status().isOk)
            .andDo(
                document(
                    "SIGNUP_API",
                    requestFields(
                        fieldWithPath("socialType").type(JsonFieldType.STRING).description("KAKAO or APPLE"),
                        fieldWithPath("socialId").type(JsonFieldType.STRING).description("Third Party 고유 아이디 값"),
                    ),
                    responseFields(
                        fieldWithPath("status").type(JsonFieldType.NUMBER).description("응답 코드"),
                        fieldWithPath("result").type(JsonFieldType.OBJECT).description("결과"),
                        fieldWithPath("result.accessToken").type(JsonFieldType.STRING).description("accessToken").optional()
                    )
                )
            )
    }
}
```

- 위와 같이 Rest Docs에 보여줄 값을 테스트 코드로 작성하면 된다. 
- Request Body는 Object Mapper를 사용해서 JSON -> String으로 저장해서 넣고 있다.
- AuthController에서 AuthService를 사용하고 있기 때문에 given, willReturn을 사용해서 Mocking 하고 있다.

<br>

![스크린샷 2023-01-15 오후 9 08 34](https://user-images.githubusercontent.com/45676906/212539666-c6b40340-a960-4259-8ad8-8c962f8acde9.png)

`docs/asciidoc` 디렉토리를 만들고 `.adoc` 파일을 생성한다.

<br>

## `Ascii Doc 빌드`

<img width="322" alt="스크린샷 2023-01-15 오후 9 11 46" src="https://user-images.githubusercontent.com/45676906/212539784-7af00484-3795-4ab3-88b9-0c3457ff2024.png">

위의 버튼을 눌러서 빌드해보자.

<br>

<img width="247" alt="스크린샷 2023-01-15 오후 9 12 36" src="https://user-images.githubusercontent.com/45676906/212539808-15ccd3fe-da61-4c9e-847e-0816874dd4c1.png">

그러면 위와 같이 `generated-snippets` 아래에 `adoc` 파일들이 생긴다.

<br>

### `index.adoc`

```
ifndef::snippets[]
:snippets: build/generated-snippets
endif::[]
:doctype: book
:icons: font
:source-highlighter: highlightjs
:toc: left
:toclevels: 2

[[Option]]
== link:../index.html[> 이전으로]  // 링크

[[introduction]]
== Introduction

[[common]]
include::./auth/signup.adoc[]
```

`.adoc` 파일에 맞는 문법이 존재한다. `toclevels` 등등 여러가지 문법을 확인해보고 자기가 원하는 Docs 형태로 만들자.

<br>

## `API adoc`

```
=== 회원가입

include::{snippets}/SIGNUP_API/http-request.adoc[]

===== [Request]

===== [Request Body]

include::{snippets}/SIGNUP_API/request-fields.adoc[]

===== [Response]

====== [Response Field]

include::{snippets}/SIGNUP_API/response-fields.adoc[]

include::{snippets}/SIGNUP_API/response-body.adoc[]

===== [HTTP Example]

====== [Request HTTP Example]

include::{snippets}/SIGNUP_API/http-request.adoc[]

====== [Response HTTP Example]

include::{snippets}/SIGNUP_API/http-response.adoc[]

====== [Curl Request HTTP Example]

include::{snippets}/SIGNUP_API/curl-request.adoc[]
```

위에서 만들어진 `adoc` 파일 기반으로 위와 같이 Docs 파일을 구성할 수 있다.

<br>

<img width="303" alt="스크린샷 2023-01-15 오후 9 13 38" src="https://user-images.githubusercontent.com/45676906/212539933-b42f0c6c-5eae-4b0b-b972-00b5476073d9.png">

그러면 위처럼 `html` 파일도 생긴다.

<br>

<img width="1577" alt="스크린샷 2023-01-15 오후 9 14 24" src="https://user-images.githubusercontent.com/45676906/212539951-43d9247c-ba09-42db-b0d6-359ddecf36b7.png">

html 파일을 브라우저에서 열고 확인하면 위와 같이 Rest Docs가 생성된 것을 확인할 수 있다.