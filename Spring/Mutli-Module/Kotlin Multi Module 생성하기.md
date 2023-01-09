## `Kotlin Multi Module 생성하기`

<img width="393" alt="스크린샷 2023-01-09 오후 11 01 06" src="https://user-images.githubusercontent.com/45676906/211325725-b7f6fc93-5f1b-4bb1-9415-a3b1407fae60.png">

1. 프로젝트를 생성한 후에 위처럼 `New` -> `Module` 버튼을 누른다.

<br>

<img width="596" alt="스크린샷 2023-01-09 오후 11 03 33" src="https://user-images.githubusercontent.com/45676906/211326168-0c2fe376-d2a3-445e-a3a0-5937c328ccb5.png">

`New Module` 버튼을 통해서 모듈 설정을 한다. 

<br>

```kotlin
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "2.7.7"
	id("io.spring.dependency-management") version "1.0.15.RELEASE"
	kotlin("jvm") version "1.6.21"
	kotlin("plugin.spring") version "1.6.21"
}

group = "com.example"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.kafka:spring-kafka-test")
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

처음에 `Kotlin`, `Spring` 프로젝트를 만들었을 때 `build.gradle.kts`는 위와 같다.

<br>

```kotlin
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "2.7.7"
    id("io.spring.dependency-management") version "1.0.15.RELEASE"
    kotlin("jvm") version "1.6.21"
    kotlin("plugin.spring") version "1.6.21"
}

java.sourceCompatibility = JavaVersion.VERSION_11

allprojects {
    group = "com.example"
    version = "0.0.1-SNAPSHOT"

    repositories {
        mavenCentral()
    }
}

subprojects {
    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "org.jetbrains.kotlin.plugin.spring")
    apply(plugin = "org.springframework.boot")
    apply(plugin = "io.spring.dependency-management")

    dependencies {
        implementation("org.springframework.boot:spring-boot-starter-web")
        implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
        implementation("org.jetbrains.kotlin:kotlin-reflect")
        implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
        implementation("org.springframework.kafka:spring-kafka")
        testImplementation("org.springframework.boot:spring-boot-starter-test")
        testImplementation("org.springframework.kafka:spring-kafka-test")
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
}
```

- `allprojects`를 사용하여 `group`, `version`, `repositories`를 묶음
- `subprojects`를 사용하여 `dependencies`, `apply`를 안에다 넣음

<br>

<img width="257" alt="스크린샷 2023-01-09 오후 11 13 05" src="https://user-images.githubusercontent.com/45676906/211328239-651151bb-4a81-44f8-9f1f-dfa7cc390a7f.png">

<br>

<img width="275" alt="스크린샷 2023-01-09 오후 11 17 09" src="https://user-images.githubusercontent.com/45676906/211328884-40d668ed-f298-404a-a986-d2cffd5c61d7.png">

기존에 존재하던 코드를 `Producer` 모듈로 옮기고, 삭제하면 위와 같은 구조가 된다.

<br>

```kotlin
dependencies {
}
```

`Producer` 모듈의 `build.gradle.kts`는 일단은 필요한 것이 없기 때문에 위와 같이 둔다.

<br>

```kotlin
dependencies {
    implementation(project(":모듈명"))
}
```
```
ex) implementation(project(":core"))
```

다른 모듈을 참조해야 한다면 위와 같이 사용할 수 있다.(`producer` -> `core` 모듈 참조하는 상황이 됨)

<br>

![스크린샷 2023-01-09 오후 11 20 11](https://user-images.githubusercontent.com/45676906/211329556-7beb032c-0ad2-4406-994e-a9c8caac272b.png)

그리고 `Producer` 모듈을 실행하면 정상적으로 실행이 된다.