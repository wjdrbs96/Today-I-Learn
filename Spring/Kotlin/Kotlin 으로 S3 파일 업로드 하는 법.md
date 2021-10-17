# `Kotlin으로 S3에 파일 업로드 하는 법`

이번 글에서는 `Kotlin 코드로 AWS S3에 파일 업로드 하는 방법에 대해서 알아보겠습니다.` AWS S3 버킷을 생성하는 방법에 대해서는 다루지 않을 것이라 혹시 알고 싶다면 [여기](https://devlog-wjdrbs96.tistory.com/323?category=882690) 에서 참고하고 오시면 될 거 같습니다. 

```kotlin
@Service
class S3Service(
    private val s3Client: AmazonS3Client
) {

    @Value("\${cloud.aws.s3.bucket}")
    val bucket: String? = null

    @Value("\${cloud.aws.s3.dir}")
    val dir: String? = null

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

파일 업로드에 사용되는 주요 코드는 위와 같습니다. 위의 보면 `@Value`로 `AWS S3 버킷 이름`, `S3 내부의 디렉토리 이름`을 주입 받고 있습니다. 즉, `application.yml`에서 주입 받을 수 있는 코드가 필요합니다. 

<br>

```yaml
cloud:
  aws:
    credentials:
      accessKey: IAM Access Key
      secretKey: IAM Access SecretKey
    s3:
      bucket: S3 Bucket Name
      dir: 디렉토리이름
    region:
      static: ap-northeast-2
    stack:
      auto: false 
```

