# `들어가기 전에`

이번 글에서는 `Kotlin`에서 `ModelMapper`를 사용하는 방법에 대해서 간단하게 정리 해보겠습니다. 

Spring으로 백엔드를 개발하다 보면 `DTO`로 클라이언트로 부터 받아오고 JPA를 통해서 디비에 접근할 때는 Entity로 변환해서 접근을 합니다. 그리고 JPA를 통해서 나온 결과를 그대로 반환하기 것이 아니라 DTO로 변환해서 클라이언트로 반환하게 되는데요. 

그러면 매번 DTO 필드가 2개, 3개 달라질텐데 그에 해당하는 생성자를 만들어야 할 것입니다. 상당히 번거로운 작업이 될텐데요. 이럴 때 사용하면 좋은 것이 `ModelMapper` 입니다.
`ModelMapper`는 `서로 다른 Object 간의 필드 값을 자동으로 Mapping 해주는 라이브러리` 입니다.

<br> <br>

## `gradle`

```
implementation("org.modelmapper:modelmapper:2.4.4")
```

위와 같이 `modelmapper` 의존성을 추가해주겠습니다.

<br>

## `Kotlin에서 ModelMapper 간단하게 사용해보기`

간단하게 `ReustDTO`로 name 필드만 존재하는 것을 받아와서 `Entity`에 저장하고 반환하는 예제를 해보겠습니다. 먼저 엔티티를 보면 아래와 같습니다.

![스크린샷 2021-08-25 오후 10 53 44](https://user-images.githubusercontent.com/45676906/130803607-0cab7cfa-5904-4ac6-aeed-3398139f5bf4.png)

엔티티는 위와 같은데요. 다른 거는 볼 필요 없고 그냥 필드 이름이 name이 존재한다는 것만 알면 될 것 같습니다. 그리고 Service Layer에서 DTO <-> Entiy 변환하는 곳의 코드를 보겠습니다. 

<br>

![스크린샷 2021-08-25 오후 10 55 20](https://user-images.githubusercontent.com/45676906/130803959-01bdb3ac-7e60-43d8-885d-9743c391ed65.png)

위와 같이 `modelMapper.map()`을 사용해서 `DTO -> Entity`로 변환해서 저장하고, 저장한 결과의 Entity를 DTO로 다시 반환해서 클라이언트로 반환하는 코드입니다. 
원래 같으면 해당 DTO 필드의 수만큼의 생성자를 만들고 변환하는 메소드를 만들고 귀찮은 작업들을 해주어야 했을 것입니다.

<br> <br>

## `ModelMapper 설정하기`

크게 설정할 건 없고 간단한 거 설정만 하겠습니다. 

```kotlin
@Configuration
class ModelMapperConfig {

    @Bean
    fun modelMapper(): ModelMapper {
        val modelMapper = ModelMapper()
        modelMapper.configuration.isFieldMatchingEnabled = true
        modelMapper.configuration.fieldAccessLevel = org.modelmapper.config.Configuration.AccessLevel.PRIVATE
        return modelMapper
    }
}
```

위와 같이 설정을 해주면 됩니다. `isFieldMatchingEnabled`를 true로 하면 필드 이름이 같은 것끼리 매칭을 하겠다는 뜻입니다. 그리고 `fieldAccessLevel`을 PRIVATE로 해놓으면 private 필드여도 접근할 수 있도록 하게 해줍니다. 
즉, Entity가 private 이기 때문에 매핑을 하기 위해서는 위의 설정이 필요합니다. 