# `Kotlin, Spring Boot로 S3에 파일 업로드 하는 법`

이번 글에서는 `Kotlin 코드로 AWS S3에 파일 업로드 하는 방법에 대해서 알아보겠습니다.` AWS S3 버킷을 생성하는 방법에 대해서는 다루지 않을 것이라 혹시 알고 싶다면 [여기](https://devlog-wjdrbs96.tistory.com/323?category=882690) 에서 참고하고 오시면 될 거 같습니다.

```
implementation("org.springframework.cloud:spring-cloud-starter-aws:2.0.1.RELEASE")
```

Spring boot는 gradle 기반으로 사용할 것이기 때문에 위의 의존성을 `build.gradle`에 추가하겠습니다.

<br>

```kotlin
@Service
class S3Service(
    private val s3Client: AmazonS3Client
) {

    @Value("\${cloud.aws.s3.bucket}")
    lateinit var bucket: String

    @Value("\${cloud.aws.s3.dir}")
    lateinit var dir: String

    @Throws(IOException::class)
    fun upload(file: MultipartFile): String {
        val fileName = UUID.randomUUID().toString() + "-" + file.originalFilename
        val objMeta = ObjectMetadata()

        val bytes = IOUtils.toByteArray(file.inputStream)
        objMeta.contentLength = bytes.size.toLong()

        val byteArrayIs = ByteArrayInputStream(bytes)

        s3Client.putObject(PutObjectRequest(bucket, dir + fileName, byteArrayIs, objMeta)
            .withCannedAcl(CannedAccessControlList.PublicRead))

        return s3Client.getUrl(bucket, dir + fileName).toString()
    }

}
```

사진 업로드를 하기 위해서는  `application.yml`에서 주입 받을 수 있는 코드가 필요하기에,  `@Value`로 `AWS S3 버킷 이름`, `S3 내부의 디렉토리 이름`을 주입 받고 있습니다. (이 때 `lateinit var`를 사용해서 지연초기화를 이용한 것도 볼 수 있습니다.) 그리고 나머지 코드는 파일 이름과 UUID를 조합해서 S3에 저장하기 위해서 위와 같이 코드를 작성하였습니다. 

<br>

```yaml
cloud:
  aws:
    credentials:
      accessKey: IAM Access Key
      secretKey: IAM Secret Key
    s3:
      bucket: S3 Bucket Name
      dir: 디렉토리이름
    region:
      static: ap-northeast-2
    stack:
      auto: false 
```

`application.yml`을 보면 위와 같은데요. AWS 계정의 IAM Access Key, IAM Secret Key가 존재할 것입니다. 그거를 yml 파일에 적겠습니다. (참고로 IAM 키는 중요한 키이기 때문에 Github 같은 공개 저장소에는 절대 올리면 안됩니다. 설정 파일을 .gitignore로 제외 시키거나, [인텔리제이로 환경변수 등록하는 방법](https://devlog-wjdrbs96.tistory.com/363?category=960077) 을 참고해서 사용할 수 있습니다.) 

<br> <br>

## `Postman으로 파일 업로드 해보기`

```kotlin
@RequestMapping("/api/v1")
@RestController
class S3Controller(
    private val s3Service: S3Service
) {

    @PostMapping("/upload")
    fun fileUpload(@RequestParam("image") multipartFile: MultipartFile): String {
        return s3Service.upload(multipartFile)
    }

}
```

위처럼 `Controller` 코드를 작성하고 postman으로 파일 업로드를 해보겠습니다. 

<br>

![스크린샷 2021-10-18 오전 9 37 55](https://user-images.githubusercontent.com/45676906/137651284-08df3fee-a969-4f11-a20c-8824557b432e.png)

Postman에서 위와 같이 `form-data`에 `file`을 선택하고 파일 하나를 선택하고 API 요청을 해보겠습니다. 그러면 코드에서 작성한대로 S3에 저장된 파일 URL이 응답으로 오는 것을 볼 수 있습니다.

이번 글의 코드가 자세히 궁금하다면 [Github](https://github.com/wjdrbs96/Spring_Kotlin_S3) 에서 확인할 수 있습니다.