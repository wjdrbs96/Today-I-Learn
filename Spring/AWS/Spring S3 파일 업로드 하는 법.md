## `Spring Boot S3 파일 업로드 하는 법`

이번 글에서는 Spring으로 AWS S3에 파일 업로드 하는 글을 정리해보겠습니다.(기존에 썼던 글은 잘못되거나 부족한 점이 많아서 이번 기회에 다시 써서 정리해보려 합니다.)

<br>

### `AWS S3 Bucket 생성하기`

<img width="817" alt="스크린샷 2022-08-14 오후 4 44 51" src="https://user-images.githubusercontent.com/45676906/184527418-1bb5eebc-7cf7-4526-8fa6-48b16a1b8f3a.png">

먼저 S3 버킷을 생성합니다. 버킷 이름만 설정한 후에 나머지 설정은 Default 설정 그대로 두고 생성하겠습니다.

<br>

### `IAM 사용자 S3 접근 권한 추가`

![image](https://user-images.githubusercontent.com/45676906/184527472-cd07e4c5-26ee-48a8-b0b5-1b481bfe5180.png)

먼저 IAM 사용자를 생성하겠습니다.

<br>

![image](https://user-images.githubusercontent.com/45676906/184527489-8f2df3b8-065c-49ed-8d13-e18f80eb3762.png)

그리고 위와 같이 IAM 사용자 생성 후에 `S3FullAccess 권한`을 추가하겠습니다.

<br>

## `Spring Boot 파일 업로드`

```
implementation 'org.springframework.cloud:spring-cloud-starter-aws:2.2.6.RELEASE'
```

Spring Boot는 gradle 기반으로 이고 `build.gradle`에 위의 의존성을 추가하겠습니다.

<br>

### `application.yml 작성`

```yaml
cloud:
  aws:
    credentials:
      accessKey: ${AWS_ACCESS_KEY_ID}       # AWS IAM AccessKey 적기
      secretKey: ${AWS_SECRET_ACCESS_KEY}   # AWS IAM SecretKey 적기
    s3:
      bucket: 버킷 이름    # ex) marryting-gyunny
      dir: S3 디렉토리 이름 # ex) /gyunny
    region:
      static: ap-northeast-2
    stack:
      auto: false
```

`application.yml`에 위와 같이 작성을 하겠습니다. 

<br>

## `파일 업로드 코드`

```java
@RequiredArgsConstructor
@Service
public class S3Upload {

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.s3.dir}")
    private String dir;

    private final AmazonS3Client s3Client;

    public String upload(InputStream inputStream, String originFileName, String fileSize) {
        String s3FileName = UUID.randomUUID() + "-" + originFileName;

        ObjectMetadata objMeta = new ObjectMetadata();
        objMeta.setContentLength(Long.parseLong(fileSize));

        s3Client.putObject(bucket, s3FileName, inputStream, objMeta);

        return s3Client.getUrl(bucket, dir + s3FileName).toString();
    }
}
```

Spring Boot로 AWS S3로 파일 업로드 하는 코드는 위의 코드가 전부입니다. 한 줄씩 코드의 의미를 간단하게 살펴보겠습니다.

<br>

```
String s3FileName = UUID.randomUUID() + "-" + originFileName;
```

S3에 저장되는 파일의 이름이 중복되지 않기 위해서 UUID로 생성한 랜덤 값과 파일 이름을 연결하여 S3에 업로드 하겠습니다.

<br>

```
ObjectMetadata objMeta = new ObjectMetadata();
objMeta.setContentLength(Long.parseLong(fileSize));
```

그리고 Spring Server에서 S3로 파일을 업로드해야 하는데, 이 때 파일의 사이즈를 ContentLength로 S3에 알려주기 위해서 ObjectMetadata를 사용합니다.

<br>

```
s3Client.putObject(bucket, s3FileName, inputStream, objMeta);
```

그리고 이제 S3 API 메소드인 putObject를 이용하여 파일 Stream을 열어서 S3에 파일을 업로드 합니다.

<br>

```
s3Client.getUrl(bucket, dir + s3FileName).toString();
```

그리고 `getUrl` 메소드를 통해서 S3에 업로드된 사진 URL을 가져오는 방식입니다.

<br>

## `Controller Multipart 사용하기`

```java
@RequiredArgsConstructor
@RestController
public class FileUploadController {
    
    private final S3Upload s3Upload;
    
    @PostMapping("/upload")
    public ApiResponse<String> uploadFile(@RequestParam("images") MultipartFile multipartFile,
                                          @RequestParam String fileSize) throws IOException {
        return ApiResponse.success(
                HttpStatus.CREATED, s3Upload.upload(multipartFile.getInputStream(), multipartFile.getOriginalFilename(), fileSize)
        );
    }
}
```

Controller에서는 Multipart 타입을 사용해서 클라이언트로부터 파일을 받아오면 됩니다.

<br>

## `Postman으로 File Upload 하기`

<img width="1286" alt="스크린샷 2022-08-14 오후 5 25 48" src="https://user-images.githubusercontent.com/45676906/184528840-815b4b9d-6335-4e00-b730-c0f18544b85b.png">

그리고 위와 같이 `Body -> form-data -> File 변경 후 -> 파일 업로드`를 통해서 서버로 요청을 보내면 위와 같이 정상적으로 응답을 받는 것을 확인할 수 있습니다.

<br>

<img width="1052" alt="스크린샷 2022-08-14 오후 5 28 50" src="https://user-images.githubusercontent.com/45676906/184528912-e308c7d9-1036-44c8-b9de-af7960c0928a.png">

S3에도 파일이 정상적으로 업로드된 것을 확인할 수 있습니다.

<br>

## `마무리 하기`

여기까지 Spring Boot로 AWS S3로 파일 업로드 하는 법에 대해서 알아보았습니다. 궁금한 점이나 부족한 점이 있다면 답글로 첨언 부탁드리겠습니다.