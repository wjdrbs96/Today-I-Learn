# `Travis CI 연동하기`

![스크린샷 2021-03-06 오후 11 36 08](https://user-images.githubusercontent.com/45676906/110210395-dcf2d380-7ed4-11eb-8ecc-ad046309faa3.png)

위와 같이 `Travis CI` 홈페이지에 들어간 후에 Github 계정으로 로그인 하고 `Settings`에 들어가겠습니다. 

![스크린샷 2021-03-06 오후 11 42 04](https://user-images.githubusercontent.com/45676906/110210588-a5385b80-7ed5-11eb-8e58-ad28ea86bfbf.png)

그리고 위와 같이 `사용하고자 하는 레포를 검색한 후에 버튼을 활성화 시키겠습니다.`

![스크린샷 2021-03-06 오후 11 45 24](https://user-images.githubusercontent.com/45676906/110210667-06602f00-7ed6-11eb-95c4-351d12ed7829.png)

그리고 레포를 클릭해서 들어가면 위와 같은 화면을 볼 수 있습니다. 

<br>

## `프로젝트 설정하기`

![스크린샷 2021-03-06 오후 11 49 02](https://user-images.githubusercontent.com/45676906/110210762-9605dd80-7ed6-11eb-9cca-1365798162ab.png)

`Spring Boot Gradle` 프로젝트에서 위와 같이 `.travis.yml` 파일을 만들겠습니다. 

```yaml
language: java
jdk:
  - openjdk11

brances:
  only:
    - master

cache:
  directories:
    - '$HOME/ .2/repository'
    - '$HOME/ .gradle'

script: "./gradlew clean build"

notification:
  email:
    recipients:
      - wjdrbs966@naver.com
```

위와 같이 작성을 하겠습니다. 

- `cache`: 그레들을 통해 의존성을 받게 되면 이를 해당 디렉토리에 캐시하여, `같은 의존성을 다음 배포 때부터 다시 받지 않도록` 설정합니다.
- `script`
    - master 브랜치에 푸시되었을 때 수행하는 명령어
    - 여기서는 프로젝트 내부에 둔 gradlew을 통해 clean & build를 수행
   
- `notifications`
    - Trivais CI 실행 완료 시 자동으로 알림이 가도록 설정
    

![스크린샷 2021-03-06 오후 11 59 40](https://user-images.githubusercontent.com/45676906/110211108-6c4db600-7ed8-11eb-957f-a0937d8ff9c0.png)

<br>

## `Travis CI와 S3 연동하기`

S3란 AWS에서 제공하는 `일종의 파일 서버`입니다.

![구조](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FcbqRQ4%2FbtqH3B5mjvi%2FfbbTg0AdqIgGpaWk4YNAg0%2Fimg.png)

위와 같은 구조로 설계해보겠습니다. 첫 번째로 Travis CI와 S3를 연동하겠습니다. `실제 배포는 CodeDeploy라는 서비스를 이용합니다.` 하지만, S3 연동이 먼저 필요한 이유는 `Jar 파일을 전달하기 위해서`입니다.

<br>

### `AWS Key 발급하기`

AWS 서비스에 외부 서비스가 접근할 수 없습니다. 그러므로 접근 가능한 권한을 가진 Key를 생성해서 사용해야 합니다. `AWS에서는 이러한 인증과 관련된 기능을 제공하는 서비스로 IAM이 있습니다.`

![스크린샷 2021-03-07 오전 12 07 47](https://user-images.githubusercontent.com/45676906/110211263-3230e400-7ed9-11eb-89f3-25955719d25b.png)

![스크린샷 2021-03-07 오전 12 08 59](https://user-images.githubusercontent.com/45676906/110211319-6a382700-7ed9-11eb-9b74-924ad35f2681.png)

![스크린샷 2021-03-07 오전 12 10 32](https://user-images.githubusercontent.com/45676906/110211356-994e9880-7ed9-11eb-80d3-1366a2594b86.png)

![스크린샷 2021-03-07 오전 12 12 40](https://user-images.githubusercontent.com/45676906/110211419-e6326f00-7ed9-11eb-885e-cdd4a913cda1.png)

![스크린샷 2021-03-07 오전 12 13 53](https://user-images.githubusercontent.com/45676906/110211478-11b55980-7eda-11eb-9b6c-226cbee23c3a.png)

![스크린샷 2021-03-07 오전 12 15 15](https://user-images.githubusercontent.com/45676906/110211521-3c071700-7eda-11eb-885e-f81fa001acbb.png)

![스크린샷 2021-03-07 오전 12 16 21](https://user-images.githubusercontent.com/45676906/110211562-71ac0000-7eda-11eb-8065-3b4eafe5c491.png)

최종 생성 완료되면 다음과 같이 `엑세스 키`와 `비밀 엑세스 키`가 생성됩니다. 이 두 값이 `Travis CI에서 사용될 키입니다.`

![스크린샷 2021-03-07 오전 12 18 20](https://user-images.githubusercontent.com/45676906/110211603-b0da5100-7eda-11eb-8a3f-36ca7987b390.png)

![스크린샷 2021-03-07 오전 12 20 31](https://user-images.githubusercontent.com/45676906/110211669-031b7200-7edb-11eb-818d-b171c8e0b27c.png)

![스크린샷 2021-03-07 오전 12 31 28](https://user-images.githubusercontent.com/45676906/110211987-94d7af00-7edc-11eb-9872-ef1e68a4f8e3.png)

위와 같이 `AWS_ACCESS_KEY`, `AWS_SECRET_KEY`를 변수로 해서 IAM 사용자에서 발급 받은 키 값들을 등록합니다. 

- `AWS_ACCESS_KEY: 액세스 키 ID`
- `AWS_SECRET_KEY: 비밀 엑세스 키`

<br>

## `S3 버킷 생성`

S3에 Travis CI에서 생성된 Build 파일을 저장하도록 구성하겠습니다. `S3에 저장된 Build 파일은 이후 AWS의 CodeDeploy에서 배포할 파일로 가져가도록 구성할 예정입니다.`

![스크린샷 2021-03-07 오전 12 36 09](https://user-images.githubusercontent.com/45676906/110212106-26dfb780-7edd-11eb-851f-e6f6a7c9f98b.png)

![스크린샷 2021-03-07 오전 12 37 03](https://user-images.githubusercontent.com/45676906/110212132-4a0a6700-7edd-11eb-8dc9-6fae61018c67.png)

![스크린샷 2021-03-07 오전 12 39 17](https://user-images.githubusercontent.com/45676906/110212217-a79eb380-7edd-11eb-90fa-b114f529070a.png)

그리고 나머지는 그대로 두고 버킷을 만들겠습니다. 

<br>

### `.travis.yml 추가`

위에서 만든 파일 아래에 코드를 추가하겠습니다. 

```yaml
language: java
jdk:
  - openjdk11

brances:
  only:
    - master

cache:
  directories:
    - '$HOME/ .2/repository'
    - '$HOME/ .gradle'

script: "./gradlew clean build"

before_deploy:
  - zip -r SpringBoot_CI-CD *
  - mkdir -p deploy
  - mv SpringBoot_CI-CD.zip deploy/SpringBoot_CI.CD.zip

deploy:
  - provider: s3
    access_key_id: $AWS_ACCESS_KEY

    secret_access_key: $AWS_SECRET_KEY

    bucket: gyunny
    region: ap-northeast-2
    skip_cleanup: true
    acl: private
    local_dir: delpoy
    wait-until-deployed: true

notification:
  email:
    recipients:
      - wjdrbs966@naver.com
```

전체 코드는 위와 같습니다. 

![스크린샷 2021-03-07 오전 12 56 32](https://user-images.githubusercontent.com/45676906/110212693-06652c80-7ee0-11eb-8dd2-3831c2f5d951.png)

그리고 Github에 push를 하면 위와 같이 성공하는 것을 볼 수 있습니다. 
