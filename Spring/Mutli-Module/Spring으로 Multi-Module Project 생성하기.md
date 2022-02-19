# `Spring으로 Multi-Module Project 설정하는 법`

이번 글에서는 Spring Project로 `Mutli-Module`을 구성하는 법에 대해서 알아보겠습니다.(`gradle`에 대해서 자세히 다루지는 않고, 현재 글을 쓰는 방법이 `Best Practice`라고 생각하지는 않습니다. 단순히 멀티 모듈로 설정하는 방법 중에 하나에 대해서 공유합니다.)

`admin`, `api`, `domain` 3개의 모듈로 나눠보겠습니다. 

![스크린샷 2022-02-13 오전 1 24 38](https://user-images.githubusercontent.com/45676906/153719410-6f6fed48-7155-4575-93bb-ce7ac72d21a6.png)

`Spring Boot`로 프로젝트를 하나 만들면 위와 같은 형태로 프로젝트가 만들어질 것입니다. 여기서 모듈을 생성해보겠습니다. 

<br>

![스크린샷 2022-02-13 오전 1 28 14](https://user-images.githubusercontent.com/45676906/153719548-24902b1b-05d9-45e2-b6a3-f8ffbd1073c7.png)

프로젝트 최상단 폴더 오른쪽 마우스를 클릭한 후에 `New -> Module`을 선택하겠습니다. 

<br>

![스크린샷 2022-02-13 오전 1 56 01](https://user-images.githubusercontent.com/45676906/153720494-b0c2197e-8833-4660-97b4-740f3795dda1.png)

`Gradle`을 선택하고 `NEXT`를 누르겠습니다. 

<br>

![스크린샷 2022-02-13 오전 1 57 02](https://user-images.githubusercontent.com/45676906/153720525-7dc6ceed-9290-4590-b315-852a24bf5b7e.png)

그리고 모듈의 이름을 적은 후에 `NEXT`를 누르겠습니다. 

<br>

![스크린샷 2022-02-13 오전 1 58 48](https://user-images.githubusercontent.com/45676906/153720579-9d31c9a5-833a-46a4-8238-64b772c61675.png)

`api`, `domain` 두개의 모듈을 만든 상태는 위와 같습니다. 처음 프로젝트를 만들었을 때 존재하는 `Main` 클래스를 `api` 모듈 안으로 옮겼습니다. 즉 기존에 존재하는 `src` 폴더는 삭제했습니다. 

<br>

## `전체 build.gradle`

```
plugins {
    id 'org.springframework.boot' version '2.4.3'
    id 'io.spring.dependency-management' version '1.0.11.RELEASE'
    id 'java'
}

allprojects {
    repositories {
        mavenCentral()
    }
}

subprojects {
    apply {
        plugin 'java'
        plugin 'io.spring.dependency-management'
        plugin 'org.springframework.boot'
    }

    group = 'com.maru'
    version = '0.0.1-SNAPSHOT'
    sourceCompatibility = '11'

    repositories {
        mavenCentral()
    }

    configurations {
        compileOnly {
            extendsFrom annotationProcessor
        }
    }

    sourceCompatibility = '11'

    dependencies {
        implementation 'org.springframework.boot:spring-boot-starter-web'
        implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
        testImplementation 'org.springframework.boot:spring-boot-starter-test'

        compileOnly 'org.projectlombok:lombok'
        annotationProcessor 'org.projectlombok:lombok'
    }

    test {
        useJUnitPlatform()
    }
}

bootJar {
    enabled = false
}
```

`Root Gradle`에는 공통된 설정들을 넣습니다.

<br>

## `api-module build.gradle`

```
dependencies {
    implementation project(":domain")
    implementation 'org.springframework.boot:spring-boot-starter-web'

    compileOnly 'org.projectlombok:lombok'
    annotationProcessor 'org.projectlombok:lombok'

    testImplementation 'org.springframework.boot:spring-boot-starter-test'
}

test {
    useJUnitPlatform()
}

jar {
    enabled = true
}

bootJar {
    enabled = false
}
```

`implementation project(":domain")`를 통해서 `api` 모듈에서 `domain` 모듈을 참조한다. 

<br> <br>

## `domain module build.gradle`

```
dependencies {
    implementation 'org.springframework.boot:spring-boot-starter-web'
    implementation 'org.springframework.boot:spring-boot-starter-data-jpa'

    compileOnly 'org.projectlombok:lombok'
    annotationProcessor 'org.projectlombok:lombok'
}

test {
    useJUnitPlatform()
}

bootJar {
    enabled = false
}

jar {
    enabled = true
}
```

위와 같이 각 모듈별로 필요한 의존성들을 넣어줍니다.

<br>

```
bootJar {
    enabled = false
}

jar {
    enabled = true
}
```

그리고 모듈별로 위의 `boojar`, `jar`를 넣어줍니다.