## `Kotlin, Spring Boot에서 @RequestBody DTO 필드에 Valid 사용하는 방법`

[저번 글](https://devlog-wjdrbs96.tistory.com/402) 에서 `Java, Spring Boot`에서 `RequestBody DTO 필드에 Valid`를 적용하는 법에 대해서 알아보았는데요. 이번 글에서는 `Kotlin, Spring Boot`로 `Valid`를 적용하는 법에 대해서 알아보겠습니다. `Valid`에 대한 개념은 똑같기 때문에 Valid 개념이 헷갈리신다면 [저번 글](https://devlog-wjdrbs96.tistory.com/402) 을 읽고 오시는 것을 추천드립니다.

그리고 `Kotlin, Spring Boot, gradle` 기반으로 프로젝트를 하나 생성한 후에 진행해보겠습니다. 

<br> <br>

## `@RequestBody DTO 필드 Valid로 검증하기`

```
implementation("org.springframework.boot:spring-boot-starter-validation")
```

valid를 사용하기 위해서는 먼저 위의 의존성을 `build.gradle`에 추가 하겠습니다.

<br> <br>

## `1번 DTO`

```kotlin
class ValidOneDTO {

    @NotBlank
    val name: String? = null

    @NotBlank
    val part: String? = null
}
```

위의 DTO는 주 생성자 위치에 필드를 넣지 않고 자바 클래스 처럼 필드를 선언하고 `@NotBlank` 애노테이션을 추가하였습니다.

<br>

```kotlin
@RestController
class ValidController {

    @PostMapping
    fun validTest(@RequestBody @Valid validDTO: ValidDTO): String {
        println(validDTO.name + " " + validDTO.part)
        return "Valid Test"
    }
}
```

그리고 위와 같이 `POST 방식에 @RequestBody에 1번 DTO를 받도록 API`를 아주 간단하게 작성해보았는데요. name 필드를 공백으로 보냈을 때 Valid가 잘 적용이 되는지 알아보겠습니다. 

![스크린샷 2021-10-21 오후 4 12 23](https://user-images.githubusercontent.com/45676906/138229217-eaedc527-a59f-4287-9283-6db833871e38.png)

`필드`에 Valid 애노테이션을 추가했기 때문에 자바에서와 동일하게 동작하는 것을 볼 수 있습니다. 그래도 한번 `ValidOneDTO`를 Java 코드로 디컴파일 해보겠습니다. 

![스크린샷 2021-10-21 오후 4 14 38](https://user-images.githubusercontent.com/45676906/138229638-0acdfda7-f97a-466c-a26c-22c2aec1b90b.png)

<br>

![스크린샷 2021-10-21 오후 4 17 03](https://user-images.githubusercontent.com/45676906/138229848-454b8b1a-8ebb-4409-986d-db8ce7c521bd.png)

위의 Kotlin -> Java로 디컴파일 된 것을 보면 당연하다고 할 수 있듯이 필드에 `@NotBlank` 애노테이션이 붙어있는 것을 볼 수 있습니다. 여기까지는 자바와 똑같습니다. 그래서 이번에는 2번 DTO를 가지고 테스트를 해보겠습니다. 

<br> <br>

## `2번 DTO`

```kotlin
class ValidTwoDTO(

    @NotBlank
    val name: String,

    @NotBlank
    val part: String
)
```

2번 `DTO`는 주 생성자의 위치에 필드를 위치시키고, 1번 DTO와 똑같이 `@NotBlank` 애노테이션을 추가하였습니다. 그리고 위에서 실행했던 똑같은 Controller 코드에서 2번 DTO로 교체한 후에 실행해보겠습니다.

<br>

![스크린샷 2021-10-21 오후 3 33 22](https://user-images.githubusercontent.com/45676906/138223986-bbf8f858-2896-4a4f-814a-15fa2836a436.png)

이번에는 `name`을 공백으로 보냈음에도 결과는 `200`으로 성공한 것을 볼 수 있는데요. 자바와 1번 DTO에는 Valid 애노테이션만 추가해주면 바로 적용이 됐는데 `2번 DTO에서는 적용이 안되는 이유는 무엇일까요?`

<br>

![스크린샷 2021-10-21 오후 4 22 01](https://user-images.githubusercontent.com/45676906/138230532-e177cd09-41d4-420d-a31c-0c0cf79a24e0.png)

왜 그런지 알아보기 위해서 위와 같이 2번 DTO를 `Kotlin -> Java`로 디컴파일 해보았습니다. 디컴파일 결과를 보면 2번 DTO는 필드가 주 생성자 위치에 있어서, `@NotBlank` 애노테이션이 필드에 있는 것이 아니라 생성자 파라미터에 존재하는 것을 볼 수 있습니다. 

<br> <br>

## `생성자 파라미터에 @NotBlank가 추가된 이유가 무엇일까요?`

[Kotlin 공식문서](https://kotlinlang.org/docs/annotations.html#annotation-use-site-targets) 를 보면 다음과 같이 나와 있습니다. 

![스크린샷 2021-10-21 오후 4 36 49](https://user-images.githubusercontent.com/45676906/138232544-e02ef456-eb48-40cd-bd21-b18e75ebac5e.png)

<br>

> 사용 타겟 대상을 지정하지 않으면 사용 중인 주석의 @Target 주석에 따라 대상이 선택됩니다. 적용 가능한 대상이 여러 개 있는 경우 다음 목록에서 첫 번째 적용 대상이 사용됩니다.

- `param(constructor parameter)`
- `property(annotations with this target are not visible to Java)`
- `field`

<br>

영어를 해석해보면 위와 같은데요. 제가 이해하기로는 만약에 `@field, @get, @param` 같은 타겟 대상을 지정하지 않으면 `사용하려는 애노테이션(@NotBlank)이 가진 @Target` 목록을 보고 판단합니다. 그런데 이 목록도 여러 개라면 위의 보이는 `param`, `property`, `field` 중에 첫 번째가 적용 대상이 된다고 합니다. 

![스크린샷 2021-10-21 오후 4 40 38](https://user-images.githubusercontent.com/45676906/138233116-d207a141-4ecf-4069-ba69-faf1909d5475.png)

즉, `@NotBlank`의 `@Target`은 여러 개이기 때문에 `param`이 첫 번째 적용 대상이 되어서 `생성자 파라미터에 애노테이션이 추가된 것`입니다.

<br> <br>

## `해결하는 방법`

```kotlin
class ValidTwoDTO(

    @field:NotBlank
    val name: String,

    @field:NotBlank
    val part: String
)
```

위와 같이 `@field`를 사용해서 해당 애노테이션을 `필드`에 사용하겠다고 명시해주면 됩니다. 위의 코드를 다시 디컴파일 해보겠습니다.

![스크린샷 2021-10-21 오후 4 44 08](https://user-images.githubusercontent.com/45676906/138233693-0f8005ce-cabf-44ac-b2e0-58f9ea2769c6.png)

그러면 이번에는 `필드`에 `@NotBlank` 애노테이션이 존재하는 것을 볼 수 있고, `Valid`도 잘 적용이 되는 것을 볼 수 있습니다.