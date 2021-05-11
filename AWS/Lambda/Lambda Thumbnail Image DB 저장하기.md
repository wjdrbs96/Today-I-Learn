# `AWS Image Origin, Resize Url DB 저장하기`

[저번 글](https://devlog-wjdrbs96.tistory.com/325) 에서 `Origin Image`를 가지고 `Thumbnail Image`를 만드는 법에 대해서 정리해보았습니다. (이번 글은 저번 글에 이어가는 내용이기 때문에 꼭 먼저 보고 오셔야 합니다!)

이번 글에서는 `Origin Image Url`, `Resize Image Url`을 `MySQL DataBase`에 저장하는 법에 대해서 알아보겠습니다. ([Spring Boot 파일업로드](https://devlog-wjdrbs96.tistory.com/323), [Lambda로 Image Resize의 글](https://devlog-wjdrbs96.tistory.com/325)이 혼합되어 있는 형태인 것 같습니다,,)

![스크린샷 2021-05-11 오후 2 51 36](https://user-images.githubusercontent.com/45676906/117765125-77bab600-b268-11eb-942d-f93c183c1820.png)

이번 글의 아키텍쳐는 위와 같습니다. 즉 필요한 도구는 아래와 같습니다.

- `Spring Boot gradle`
- `MySQL, RDS`
- `S3 Bucket 2개`
- `Lambda`

<br>

그리고 이번 글에서는 [Spring Boot로 S3로 File 업로드 하는 법](https://devlog-wjdrbs96.tistory.com/323) 에 대한 개념도 필요한데요. 이 개념에 대해서 잘 모르겠다면 위의 글을 읽고 오셔야 합니다(아래에서 설명하지 않을 것이기 때문에..). 또한 DB에 관한 글이 아니기 때문에 DB 접근 관련 내용에 대한 설명은 생략하겠습니다.

<br>

## `build.gradle`

```
dependencies {
    implementation 'org.springframework.boot:spring-boot-starter-web'
    compileOnly 'org.projectlombok:lombok'
    annotationProcessor 'org.projectlombok:lombok'

    implementation 'org.mybatis.spring.boot:mybatis-spring-boot-starter:2.0.1'
    implementation 'mysql:mysql-connector-java'

    compile 'org.springframework.cloud:spring-cloud-starter-aws:2.0.1.RELEASE'
    testImplementation 'org.springframework.boot:spring-boot-starter-test'
}
```

위의 의존성을 추가하고 진행하겠습니다.

<br>

## `application.yml`

```yml
spring:
  datasource:
    url: jdbc:mysql://엔드포인트/스키마이름?allowPublicKeyRetrieval=true&autoReconnect=true&useSSL=false&useUnicode=true&characterEncoding=utf8&mysqlEncoding=utf8&serverTimezone=UTC
    username: 유저네임
    password: 비밀번호
    driver-class-name: com.mysql.cj.jdbc.Driver
```

위의 `엔드포인트`, `스키마이름`, `유저이름`, `비밀번호`는 자신의 RDS에 맞는 정보를 입력하면 됩니다.

<br>

## `Image 테이블 스키마`

![스크린샷 2021-05-11 오후 3 10 50](https://user-images.githubusercontent.com/45676906/117766838-1e07bb00-b26b-11eb-9eaa-4dbc870810b6.png)

`Image Url` 주소는 위의 테이블에다 `INSERT`를 해보겠습니다.

<br>

## `프로젝트 구조`

![스크린샷 2021-05-11 오후 3 24 53](https://user-images.githubusercontent.com/45676906/117768219-0c271780-b26d-11eb-8ab6-62b5f3b09590.png)

프로젝트에 대한 자세한 코드를 보고 싶다면 [Github](https://github.com/wjdrbs96/Spring_S3_Lambda/blob/master/src/main/java/com/example/image/service/S3UploaderService.java) 에서 확인하실 수 있습니다.

<br>

![스크린샷 2021-05-11 오후 3 28 05](https://user-images.githubusercontent.com/45676906/117768643-996a6c00-b26d-11eb-8c42-580dc8f52563.png)

그 중에서 `파일 업로드` 하는 코드에서 제가 위와 같이 `OriginUrl`, `ResizeUrl`을 DB에 저장하도록 코드를 작성하였습니다. (그냥 일단 임시로... 짠 것입니다. )

기존에 S3에 파일 업로드 하는 코드에서 `ResizeBucket`에 저장된 `Url`을 호출하는 코드와 DB 저장 코드만 추가된 것입니다. (아주 간단합니다.)

<br>

![스크린샷 2021-05-11 오후 3 30 08](https://user-images.githubusercontent.com/45676906/117768773-cdde2800-b26d-11eb-8505-03a4ac96725f.png)

위와 같이 `Query`를 작성하여 DB에 저장하도록 하겠습니다.

<br>

## `PostMan 테스트`

<img width="619" alt="스크린샷 2021-05-11 오후 3 31 51" src="https://user-images.githubusercontent.com/45676906/117769039-26adc080-b26e-11eb-8ef0-b360f339d81b.png">

위와 같이 파일 업로드 API에 사진 하나를 업로드 해보겠습니다.

<br>

<img width="555" alt="스크린샷 2021-05-11 오후 3 35 28" src="https://user-images.githubusercontent.com/45676906/117769447-afc4f780-b26e-11eb-81a1-2aa99a87d219.png">

그러면 위와 같이 `DB`에도 문제 없이 `Image Url`들이 잘 저장된 것을 확인할 수 있습니다. 

<br>

## `정리하기`

꽤나 많이 생략을 하고 진행을 하였는데요! 이 글을 처음 보시거나 설명이 부족하시다면 위에서 말했던 것 처럼 아래의 글들을 같이 보시는 것을 추천드립니다. 

- [Github](https://github.com/wjdrbs96/Spring_S3_Lambda/blob/master/src/main/java/com/example/image/service/S3UploaderService.java)
- [Lambda Thumbnail Image 만들기](https://devlog-wjdrbs96.tistory.com/325)
- [Spring Boot로 S3 파일 업로드 하기](https://devlog-wjdrbs96.tistory.com/323)