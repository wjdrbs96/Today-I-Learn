# `Spring Boot S3 File Upload 하는 법`

이번 글에서는 `Spring Boot`로 `AWS S3`로 `File Upload` 하는 법에 대해서 정리해보겠습니다. 먼저 AWS S3 Bucket 생성을 하겠습니다.

<br>

## `AWS S3 Bucket 생성`

<img width="676" alt="스크린샷 2021-05-03 오후 3 24 12" src="https://user-images.githubusercontent.com/45676906/116846665-c4c4d980-ac23-11eb-9731-1ca332c136da.png">

<br>

![스크린샷 2021-05-03 오후 3 27 20](https://user-images.githubusercontent.com/45676906/116846793-1e2d0880-ac24-11eb-93c3-ec2d23f9299f.png)

<br>

<img width="1427" alt="스크린샷 2021-05-03 오후 3 28 15" src="https://user-images.githubusercontent.com/45676906/116846872-474d9900-ac24-11eb-93c0-060658b1de62.png">

그리고 `권한` 탭을 들어가겠습니다.

<br>

<img width="819" alt="스크린샷 2021-05-03 오후 3 29 27" src="https://user-images.githubusercontent.com/45676906/116846975-87ad1700-ac24-11eb-981b-a518e4929097.png">

`ARN`을 복사하고 `정책 생성기`를 누르겠습니다.

<br>

![스크린샷 2021-05-03 오후 3 33 32](https://user-images.githubusercontent.com/45676906/116847272-2f2a4980-ac25-11eb-86c3-7c903af11fff.png)

- `principal`: * 를 입력해줍니다.
- `Actions`: `GetObject`, `PutObject`를 선택해줍니다.
- `ARN`: 위에 있던 ARN 복사 + `/*`를 해줍니다.

<br>

![스크린샷 2021-05-03 오후 3 36 00](https://user-images.githubusercontent.com/45676906/116847371-5bde6100-ac25-11eb-92d4-f5eb21ca1302.png)

그러면 위에서 입력했던 대로 잘 입력됐는지 확인하고 `Generate Policy`를 누르겠습니다.

<br>

![스크린샷 2021-05-03 오후 3 38 38](https://user-images.githubusercontent.com/45676906/116847625-e0c97a80-ac25-11eb-94db-c450cb54a8a7.png)

위의 정책 내용을 복사하겠습니다.

<br>

<img width="822" alt="스크린샷 2021-05-03 오후 3 41 13" src="https://user-images.githubusercontent.com/45676906/116847714-10788280-ac26-11eb-9bd7-176238e4a50c.png">

그리고 `변경사항 저장`을 누르겠습니다.

<br>

## `IAM 사용자 권한 추가`

S3에 접근하기 위해서는 `IAM` 사용자에게 S3 접근 권한을 주고, 그 사용자의 `액세스 키`, `비밀 엑세스 키`를 사용해야 합니다.

<img width="916" alt="스크린샷 2021-05-03 오후 3 52 10" src="https://user-images.githubusercontent.com/45676906/116848434-95b06700-ac27-11eb-8f13-a1b51a764785.png">

먼저 IAM 사용자를 만들고 사용자에게 `S3 접근 권한`을 부여하겠습니다. (사용자, 권한, 역할의 개념을 잘 모르겠다면 [여기](https://devlog-wjdrbs96.tistory.com/302) 에서 참고하시면 됩니다.)

<br>

![스크린샷 2021-05-03 오후 3 55 08](https://user-images.githubusercontent.com/45676906/116848648-0192cf80-ac28-11eb-9de3-70cd6b9ed0f8.png)

여기서 `사용자`에게 `S3FullAccess` 권한을 부여해야 합니다. (이것이 있어야 S3에 파일 업로드를 할 수 있습니다.)

<br>

<img width="936" alt="스크린샷 2021-05-03 오후 3 57 13" src="https://user-images.githubusercontent.com/45676906/116848812-5f271c00-ac28-11eb-9c99-32d51885e7b4.png">

위의 `액세스 키`, `비밀 엑세스 키`는 현재 화면에서 밖에 볼 수 없습니다. 즉, `.csv` 파일을 다운받아 로컬에 꼭 가지고 있어야 합니다.

<br>

## `Spring Boot로 파일 업로드`

```
compile 'org.springframework.cloud:spring-cloud-starter-aws:2.0.1.RELEASE'
```

Spring Boot는 `gradle` 기반으로 만들었고 위의 의존성을 추가하겠습니다.

<br>

![스크린샷 2021-05-03 오후 4 02 33](https://user-images.githubusercontent.com/45676906/116849178-2dfb1b80-ac29-11eb-8d9d-b34523406240.png)

최종 폴더 구조는 위와 같습니다. 하나씩 작성하면서 파일 업로드를 진행해보겠습니다. (`S3Uploader`는 service 보다는 commons와 같은 곳에 넣어놓는게 더 나을 꺼 같기도합니다!)

<br>

### `aws.yml 작성`

```yaml
cloud:
  aws:
    credentials:
      accessKey: IAM 사용자 엑세스 키
      secretKey: IAM 사용자 비밀 엑세스 키
    s3:
      bucket: 버킷 이름
    region:
      static: ap-northeast-2
    stack:
      auto: false
```


`application.yml`에 작성해도 되지만, 저는 aws 설정들만 따로 관리하기 위해서 `aws.yml` 파일을 따로 만들었습니다. 위에 형식대로 자신의 IAM 키, 버킷 이름, 리전 등등을 입력해줍니다.

<br>

## `S3config 작성`

```java
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AmazonS3Config {

    @Value("${cloud.aws.credentials.access-key}")
    private String accessKey;

    @Value("${cloud.aws.credentials.secret-key}")
    private String secretKey;

    @Value("${cloud.aws.region.static}")
    private String region;

    @Bean
    public AmazonS3Client amazonS3Client() {
        BasicAWSCredentials awsCreds = new BasicAWSCredentials(accessKey, secretKey);
        return (AmazonS3Client) AmazonS3ClientBuilder.standard()
                .withRegion(region)
                .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
                .build();
    }
}
```


따로 `config` 디렉토리에서 설정 값을 넣기 위해서 `AmazonS3Config` 설정 클래스를 만들었습니다. `aws.yml` 파일에 작성한 값들을 읽어와서 `AmazonS3Client` 객체를 만들어 Bean으로 주입하는 것입니다.

<br>

## `MainApplication`

```java
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class ImageApplication {

    public static final String APPLICATION_LOCATIONS = "spring.config.location="
            + "classpath:application.yml,"
            + "classpath:aws.yml";

    public static void main(String[] args) {
        new SpringApplicationBuilder(ImageApplication.class)
                .properties(APPLICATION_LOCATIONS)
                .run(args);
    }
}
```

Spring Boot Main 코드를 위와 같이 수정해주겠습니다. 즉, 위의 코드로 `application.yml`과 `aws.yml` 두개의 파일 모두를 설정 파일로 읽어서 사용하겠다는 뜻입니다.


<br>

## `S3Uploader 작성`

```java
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Component
public class S3Uploader {

    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    public String bucket;  // S3 버킷 이름

    public String upload(MultipartFile multipartFile, String dirName) throws IOException {
        File uploadFile = convert(multipartFile)  // 파일 변환할 수 없으면 에러
                .orElseThrow(() -> new IllegalArgumentException("error: MultipartFile -> File convert fail"));

        return upload(uploadFile, dirName);
    }

    // S3로 파일 업로드하기
    private String upload(File uploadFile, String dirName) {
        String fileName = dirName + "/" + UUID.randomUUID() + uploadFile.getName();   // S3에 저장된 파일 이름 
        String uploadImageUrl = putS3(uploadFile, fileName); // s3로 업로드
        removeNewFile(uploadFile);
        return uploadImageUrl;
    }
    
    // S3로 업로드
    private String putS3(File uploadFile, String fileName) {
        amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, uploadFile).withCannedAcl(CannedAccessControlList.PublicRead));
        return amazonS3Client.getUrl(bucket, fileName).toString();
    }

    // 로컬에 저장된 이미지 지우기
    private void removeNewFile(File targetFile) {
        if (targetFile.delete()) {
            log.info("File delete success");
            return;
        }
        log.info("File delete fail");
    }

    // 로컬에 파일 업로드 하기
    private Optional<File> convert(MultipartFile file) throws IOException {
        File convertFile = new File(System.getProperty("user.dir") + "/" + file.getOriginalFilename());
        if (convertFile.createNewFile()) { // 바로 위에서 지정한 경로에 File이 생성됨 (경로가 잘못되었다면 생성 불가능)
            try (FileOutputStream fos = new FileOutputStream(convertFile)) { // FileOutputStream 데이터를 파일에 바이트 스트림으로 저장하기 위함
                fos.write(file.getBytes());
            }
            return Optional.of(convertFile);
        }

        return Optional.empty();
    }
}
```

이제 파일 업로드는 하는 코드입니다.

- convert() 메소드에서 로컬 프로젝트에 사진 파일이 생성되지만, removeNewFile()을 통해서 바로 지우고 있는 로직입니다.
- `System.getProperty("user.dir")`: 현재 프로젝트의 절대 경로를 꺼내올 수 있습니다.
- 즉, 저는 프로젝트 루트 경로 아래에 제가 업로드한 사진 파일도 생성이 됩니다. 그리고 바로 removeNewFile를 통해서 로컬에 있는 파일은 삭제를 합니다.

대부분의 설명은 주석으로 해놓았습니다. 생소한 코드들이 많아서 처음에 볼 때는 아리송 할 수 있지만, 계속 보다 보면 이해가 점점 갈 것이라 생각합니다.

<br>

## `Controller 작성`

```java
@RequiredArgsConstructor
@RestController
public class HelloController {

    private final S3Uploader s3Uploader;

    @PostMapping("/images")
    public String upload(@RequestParam("images") MultipartFile multipartFile) throws IOException {
        s3Uploader.upload(multipartFile, "static");
        return "test";
    }
}
```

- 파일 업로드를 할 때는 `MultipartFile`을 사용합니다.
- 두 번째 매개변수의 이름에 따라 S3 Bucket 내부에 해당 이름의 디렉토리가 생성이 됩니다.

<br>

## `PostMan 테스트`

![스크린샷 2021-05-03 오후 4 22 08](https://user-images.githubusercontent.com/45676906/116850656-f9d52a00-ac2b-11eb-8e39-e3a5ba5d407c.png)

PostMan에서 파일 업로드를 하려면 위와 같이 선택하고 파일 업로드를 하면 됩니다.

<br>

## `S3 확인하기`

<img width="559" alt="스크린샷 2021-05-03 오후 4 25 29" src="https://user-images.githubusercontent.com/45676906/116850850-55071c80-ac2c-11eb-9faf-241773cf5b57.png">

그러면 위와 같이 `static` 아래에 파일 업로드가 잘 된 것을 볼 수 있습니다.

프로젝트의 자세한 코드를 확인하고 싶다면 [여기](https://github.com/wjdrbs96/Spring_S3_Lambda) 에서 확인하실 수 있습니다.

<br> <br>

# `Reference`

- [https://jojoldu.tistory.com/300](https://jojoldu.tistory.com/300)