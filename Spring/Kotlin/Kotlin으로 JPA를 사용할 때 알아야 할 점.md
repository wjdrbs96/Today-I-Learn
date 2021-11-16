## `Kotlin으로 JPA를 사용할 때 알아야 할 점`

이번 글에서는 `Kotlin`으로 `Spring Boot`, `JPA`를 사용할 때 알야아 할 점에 대해서 알아보고 `build.gralde`에 적용하는 것을 정리해보겠습니다. 

<br> <br>

## `코틀린의 특징`

코틀린 클래스는 자바와 달리 `default`가 `final class` 입니다. 

```kotlin
class Gyunny {}  // Kotlin

public final class Gyunny {}  // Java
```

위의 코틀린 클래스는 자바로 디컴파일 해보면 `final class`로 나옵니다. final 클래스 라는건 부모 클래스를 둘 수 없다라고 할 수 있는데요. 대표적으로 `Spring AOP`, `JPA`를 사용할 때 문제가 되는 부분이 있어 따로 설정을 해주어야 합니다. 이럴 때 사용하는 것이 `allOpen plugin` 입니다. (JPA나 AOP 모두 `Proxy` 기술을 사용하기 때문에 상속이 필요하기 때문입니다.)

<br>

![스크린샷 2021-11-17 오전 12 47 42](https://user-images.githubusercontent.com/45676906/142018255-72b469af-5175-4fd2-916b-6f91333afcab.png)

위처럼 `allopen` 플러그인을 사용하면 위에서 정의한 `Entity`, `MappedSuperclass`, `Embeddable` 애노테이션이 붙어 있는 클래스들은 `open class`로 자동으로 변환해주는 역할을 합니다.

<br> <br>

```
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "2.5.5"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    kotlin("jvm") version "1.5.31"
    kotlin("plugin.spring") version "1.5.31"
    kotlin("plugin.jpa") version "1.5.31"
    kotlin("plugin.allopen") version "1.4.32"
    kotlin("kapt") version "1.4.32"
}

allOpen {
    annotation("javax.persistence.Entity")
    annotation("javax.persistence.MappedSuperclass")
    annotation("javax.persistence.Embeddable")
}

group = "com.example"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "11"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
```

<br> <br>

## `Refernece`

- [https://github.com/spring-guides/tut-spring-boot-kotlin](https://github.com/spring-guides/tut-spring-boot-kotlin)
- [https://kotlinlang.org/docs/all-open-plugin.html](https://kotlinlang.org/docs/all-open-plugin.html)
- [참고하면 좋은 글](https://blog.junu.dev/37)