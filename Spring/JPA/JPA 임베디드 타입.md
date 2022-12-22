## `JPA 임베디드 타입`

```kotlin
allOpen {
    annotation("javax.persistence.Entity")
    annotation("javax.persistence.MappedSuperclass")
    annotation("javax.persistence.Embeddable")
}
```

- 기본 생성자(인자 없는 생성자)가 있어야 한다. → kotlin-jpa 컴파일러 플러그인 추가
- final이면 안된다. → kotlin-allopen 컴파일러 플러그인에 javax.persistence.Embeddable 추가
- 프로퍼티에는 getter와 setter가 존재해야 한다. → val이 아니라 var로 선언

<br>

```kotlin
@Entity
class Users(
    @Enumerated(EnumType.STRING)
    val socialType: SocialType,
    val socialId: String,
    @Embedded
    val address: Address
) : BaseEntity()

@Embeddable
data class Address(
    var city: String,
    var street: String,
    var zipcode: String
)
```

하나의 코드 예시를 들면 위와 같다. 