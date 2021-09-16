# `Spring Gradle 에서 Querydsl 설정하는 법`

`Querydsl`은 Spring boot starter에 없기 때문에 따로 설정을 해주어야 하는데요. 그래서 Spring Boot 프로젝트는 만들었다고 가정하고 `build.gradle`에 `Querydsl` 설정 하는 법에 대해서만 정리해보겠습니다. 

![스크린샷 2021-09-16 오후 1 19 07](https://user-images.githubusercontent.com/45676906/133548721-1cbd0924-52e9-4ff5-b5c8-f323fef06674.png)

build.gradle에 보면 맨 위에 `plugins`가 존재합니다. 여기에 위처럼 `querydsl`을 추가하겠습니다.

<br> 

![스크린샷 2021-09-16 오후 1 21 58](https://user-images.githubusercontent.com/45676906/133548954-9959dac5-58d8-42f4-875c-b0a22b047db9.png)

그리고 Querydsl 의존성을 `dependencies`에 추가해주겠습니다. 

<br> 

![스크린샷 2021-09-16 오후 1 23 38](https://user-images.githubusercontent.com/45676906/133549081-3e54dd34-7e95-4b7f-8b06-58f7f28c8716.png)

그리고 마지막에 위의 코드를 추가해야 합니다. 그리고 gradle 새로고침을 누르면 아래와 같이 `Querydsl` 의존성들이 추가된 것을 볼 수 있습니다. 

<br>

![스크린샷 2021-09-16 오후 1 25 05](https://user-images.githubusercontent.com/45676906/133549192-6ee0aff2-6cbc-4441-b776-8b2f4f5ecddd.png)

<br> <br>

## `build.gradle 전체 코드`

```
plugins {
    id 'org.springframework.boot' version '2.5.4'
    id 'io.spring.dependency-management' version '1.0.11.RELEASE'
    id "com.ewerk.gradle.plugins.querydsl" version "1.0.10"
    id 'java'
}

group = 'com'
version = '0.0.1-SNAPSHOT'
sourceCompatibility = '11'

configurations {
    compileOnly {
        extendsFrom annotationProcessor
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation 'org.springframework.boot:spring-boot-starter-web'

    compileOnly 'org.projectlombok:lombok'
    annotationProcessor 'org.projectlombok:lombok'

    implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
    implementation 'com.querydsl:querydsl-jpa'
    runtimeOnly 'com.h2database:h2'

    testImplementation 'org.springframework.boot:spring-boot-starter-test'
}

test {
    useJUnitPlatform()
}

def querydslDir = "$buildDir/generated/querydsl"

querydsl {
    jpa = true
    querydslSourcesDir = querydslDir
}
sourceSets {
    main.java.srcDir querydslDir
}
configurations {
    querydsl.extendsFrom compileClasspath
}

compileQuerydsl {
    options.annotationProcessorPath = configurations.querydsl
}
```