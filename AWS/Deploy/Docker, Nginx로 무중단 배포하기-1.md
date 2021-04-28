# `Nginx, Docker를 사용하여 무중단 배포하기 - 1`

[저번 글](https://devlog-wjdrbs96.tistory.com/309) 에서는 `Docker`는 사용하지 않고 Nginx로 무중단 배포를 했었습니다. 이번 글에서는 저번 글에서 `Docker`가 추가되었다고 생각하면 됩니다.
그리고 Docker로 무중단 배포를 진행하기 전에 간단히 [Docker로 Nginx Reverse Proxy](https://devlog-wjdrbs96.tistory.com/313) 하기에 대해서 다루기도 하였습니다. 이러한 글들을 읽고오시지 않았다면 꼭 먼저 읽고 오시는 것을 추천드립니다. 

<br>

![스크린샷 2021-04-27 오후 9 25 17](https://user-images.githubusercontent.com/45676906/116240690-13472380-a79f-11eb-92e5-af383f1693b3.png)

위의 아키텍쳐를 만드는데 지금 생각해보면 그렇게~? 어렵지는 않지만.. 정말 삽질을 많이하고 힘들게 해결하기는 했습니다. 그 과정들에 대해서 하나씩 알아보면서 진행해보겠습니다.
(현재는 DB는 연결하지 않았지만 다음 글에서는 `ElastiCache`와 `RDS`를 연결하는 것까지 해볼 예정입니다.)

<br>

## `사용할 도구`

- `Java 11`
- `Spring Boot`
- `Travis CI`
- `AWS EC2 Linux2, S3`
- `AWS CodeDeploy`
- `Docker`

사용할 도구는 위와 같습니다. 이제 본론으로 들어가겠습니다. 참고로 이 글에서는 AWS IAM 역할 생성, EC2 생성 등등 간단한 것은 일부 설명이 생략된 것이 존재한다는 것을 참고해주세요.

<br>

## `Spring Boot yml 파일 설정하기`

`Spring Boot` gradle 프로젝트를 만들고 `application.properties` -> `application.yml`으로 바꾸고 8081, 8082 포트 환경을 분리해보겠습니다. 

```yaml
spring:
  profiles:
    active: real1
server:
  port: 8081

---      # 위 아래를 구분해주는 것
spring:
  profiles:
    active: real2
server:
  port: 8082
```

이렇게 나누면 `active=real1` 일 때는 `8081 포트`가 실행되고, `active=real2` 일 때는 `8082 포트`가 실행됩니다. 

<br>

```
./gradlew clean build
```

그리고 위의 명령어를 통해서 `jar` 파일을 만들어보겠습니다. 

![스크린샷 2021-04-27 오후 9 43 34](https://user-images.githubusercontent.com/45676906/116243264-bf8a0980-a7a1-11eb-9aa8-e8ba39c9c511.png)

그러면 위와 같이 `build/libs/*.jar`가 생긴 것을 확인할 수 있을 것입니다. 

<br>

```
java -jar -Dspring.profiles.active=real1 ./build/libs/*.jar   // 8081 포트를 실행하겠다는 뜻
```

이렇게 `Dspring.profiles.active={}`를 통해서 실행할 환경을 구분할 수 있습니다. 저는 real1인 8081 포트를 실행해보기 위해 위의 명령어로 테스트를 해보겠습니다. 

<br>

![115525764-5b130a00-a2ca-11eb-970c-590d9dfa7ea2](https://user-images.githubusercontent.com/45676906/116244339-cd8c5a00-a7a2-11eb-9459-ac6349d79b97.png)

아니 근데 제대로 시작 하기도 전에 뭔가 잘 안되는 이슈가 생겼습니다... 왜 8081이 아니라 8082가 뜨는 것인지..? 여기서부터 삽질의 시작이었습니다. 하지만 이번 글은 이런 삽질에 대해서 하나하나 다룰 것은 아니기 때문에 이러한 이슈가 있었다는 것 정도를 공유하고 넘어가겠습니다.

<br>

```yaml
spring:
  profiles:
    group:
      "real1": "real1_port"

---
spring:
  config:
    activate:
      on-profile: "real1_port"

server:
  port: 8081

---
spring:
  profiles:
    group:
      "real2": "real2_port"


---
spring:
  config:
    activate:
      on-profile: "real2_port"

server:
  port: 8082
```

그래서 많은 삽질을 하다 위와 같이 `application.yml`을 바꾸면 된다 해서 수정을 하니까 잘 되었습니다!

<br>

![스크린샷 2021-04-27 오후 9 56 06](https://user-images.githubusercontent.com/45676906/116245005-763ab980-a7a3-11eb-99e8-cb32f2e6b7ff.png)

<br>

## `간단한 Controller 하나 만들기`

```java
@RequiredArgsConstructor
@RestController
public class HelloController {

    private final Environment env;

    @GetMapping("/")
    public String gyunny() {
        List<String> profile = Arrays.asList(env.getActiveProfiles());
        List<String> realProfiles = Arrays.asList("real1", "real2");
        String defaultProfile = profile.isEmpty() ? "default" : profile.get(0);

        return profile.stream()
                .filter(realProfiles::contains)
                .findAny()
                .orElse(defaultProfile);
    }
}
```

위의 코드는 현재 `application.yml`에서 설정했던 것처럼 `active.profile`이 무엇이냐에 따라 응답 결과가 달라집니다. active.profiles=real1 라면 real1이 그대로 반환되고, active.profiles=real1 라면 real2가 반환됩니다.

<br>

![스크린샷 2021-04-28 오전 9 23 19](https://user-images.githubusercontent.com/45676906/116328218-84202700-a803-11eb-8a73-a636d7ffca0c.png)

위와 같이 `active.profile=real1`으로 다시 한번 실행시켜서 `http://localhost:8081`로 접속해보겠습니다.

<br>

![스크린샷 2021-04-28 오전 9 23 39](https://user-images.githubusercontent.com/45676906/116328250-926e4300-a803-11eb-88c4-700153ae53d6.png)

그러면 위와 같이 `real1`이 잘 응답이 오는 것을 확인할 수 있습니다. (이것은 무중단 배포할 때 사용되는 중요한 것이기 때문에 잘 되는지 꼭 확인하고 다음으로 넘어가셔야 합니다.)

<br>

## `Travis CI 설정하기`

[https://travis-ci.org/](https://travis-ci.org/) 에 들어간 후에 아래와 같이 버튼 활성화를 하겠습니다. 

![test](https://user-images.githubusercontent.com/45676906/116095312-41672d80-a6e3-11eb-90bc-b4522963def2.png)

사용할 Github Repository를 위와 같이 `버튼 활성화`를 시켜주겠습니다. 그리고 Github WebHook이 잘 등록되어 있는지 확인을 [여기](https://devlog-wjdrbs96.tistory.com/315) 를 참고하고 확인해주시면 됩니다. 
(버튼 활성화를 해도 Github Repository에 WebHook이 등록되어 있지 않아서 삽질을 했었던 기억도 있습니다..)

<br>

그리고 `AWS IAM 사용자`를 생성하여야 하는데 이 과정은 생략하겠습니다. IAM 사용자를 만들면 `엑세스 키`, `액세스 비밀 키`를 제공해주는데요. 이것을 사용해야 AWS 서비스에 접근을 할 수가 있습니다. 

![스크린샷 2021-04-27 오후 10 04 41](https://user-images.githubusercontent.com/45676906/116246150-ad5d9a80-a7a4-11eb-8b23-5cce50373d48.png)

그래서 `IAM 사용자 키`를 Travis CI에 등록을 하겠습니다. 

<br>

![스크린샷 2021-04-27 오후 10 12 22](https://user-images.githubusercontent.com/45676906/116247409-c7e44380-a7a5-11eb-8638-b613f80bf3d8.png)

이제 `.travis.yml` 파일을 만들어서 `Travis CI` 테스트를 해보겠습니다. 

<br>

## `.travis.yml`

```yaml
language: java
jdk:
  - openjdk11

brances:
  only:
    - master   # master 브랜치에 push 가 되었을 때 Travis CI 반응

cache:
  directories:
    - '$HOME/.m2/repository'
    - '$HOME/.gradle'

script:
  - "./gradlew clean build"   # jar 만들기

before_deploy:
  - mkdir -p before-deploy # zip에 포함시킬 파일들을 담을 디렉토리 생성
  - cp build/libs/*.jar before-deploy/   # jar 파일 넘기기
  - cd before-deploy && zip -r before-deploy * # before-deploy로 이동후 전체 압축 (폴더 이름은 원하는 이름으로 가능)
  - cd ../ && mkdir -p deploy # 상위 디렉토리로 이동후 deploy 디렉토리 생성
  - mv before-deploy/before-deploy.zip deploy/SpringBoot_Nginx.zip # deploy로 zip파일(원하는 이름으로) 이동

deploy:
  - provider: s3
    access_key_id: $AWS_ACCESS_KEY         # Travis CI 에서 IAM 사용자 엑세스 키

    secret_access_key: $AWS_SECRET_KEY     # Travis CI 에서 IAM 사용자 비밀 엑세스 키

    bucket: aws-gyun-s3        # S3 Bucket 이름 
    region: ap-northeast-2
    skip_cleanup: true
    acl: private               # zip 파일 공개 여부
    local_dir: deploy
    wait-until-deployed: true

notification:  # 이메일 알림
  email: 
    recipients:
      - wjdrbs966@naver.com
```

`S3 버킷은 따로 만들어주어야 합니다.(만드는 과정은 생략하겠습니다.)` 위와 같이 파일 안에 간단한 설명을 적어놓았습니다. 

즉, 간단히 말하면 Travis CI를 통해서 Spring Boot를 Build 해서 jar 파일을 만들고 필요한 파일들을 담아서 zip 형태로 만들어 S3로 전달하는 것입니다. 

<br>

![스크린샷 2021-04-28 오전 12 01 56](https://user-images.githubusercontent.com/45676906/116264141-033a3e80-a7b5-11eb-8d5c-d198f25c224c.png)

위와 같이 프로젝트 루트 경로 바로 아래에 `.travis.yml` 파일을 만든 후에 위의 내용을 넣으시면 됩니다. 그리고 Github에 push를 해보겠습니다. 

<br>

![스크린샷 2021-04-27 오후 11 36 47](https://user-images.githubusercontent.com/45676906/116260465-ad17cc00-a7b1-11eb-976c-5a1483f80ee3.png)

<br>

![스크린샷 2021-04-27 오후 11 36 59](https://user-images.githubusercontent.com/45676906/116261108-4b0b9680-a7b2-11eb-9c0d-319812a2a394.png)

그러면 Github에서 webHook을 날려서 Travis CI가 자동으로 작동하게 되고, 좀 기다리면 위와 같이 성공했다는 표시를 볼 수 있습니다. 그리고 `AWS S3`에 실제로 zip 파일이 저장이 되었는지 확인해보겠습니다.

<br> 
  
![스크린샷 2021-04-28 오전 12 05 38](https://user-images.githubusercontent.com/45676906/116264921-a428f980-a7b5-11eb-9e0d-28cea7afb423.png)
  
AWS S3에서 확인해보면 위와 같이 `.travis.yml`에서 적었던 이름의 zip 파일이 넘어온 것을 볼 수 있습니다. 이러면 `Travis CI와 S3 연동`이 된 것입니다. 이제.. CI(테스트 코드는 없어서.. 정확히 말하면 빌드서버?)가 완료 되었고
이제 CD의 역할을 할 CodeDeploy 세팅을 해보겠습니다. 

<br>

## `CodeDeploy 세팅`

<img width="924" alt="스크린샷 2021-04-28 오전 12 15 57" src="https://user-images.githubusercontent.com/45676906/116267056-05050180-a7b7-11eb-825a-fe9726fc59c0.png">

위와 같이 CodeDeploy 애플리케이션을 만들겠습니다. 

<br>

<img width="909" alt="스크린샷 2021-04-28 오전 12 17 24" src="https://user-images.githubusercontent.com/45676906/116267293-3ed60800-a7b7-11eb-8014-ab6eec202fe5.png">

그리고 `배포 그룹`을 만들어야 합니다. 여기서 `서비스 역할`은 매우 중요합니다. IAM 사용자에게 CodeDeploy 접근 권한을 주어야 합니다.(즉, 역할 주어야 함) 그 과정은 [여기](https://jojoldu.tistory.com/281) 에서 참고하시면 될 거 같습니다.
 

<br>

![스크린샷 2021-04-28 오전 12 21 09](https://user-images.githubusercontent.com/45676906/116267901-c459b800-a7b7-11eb-9b24-9610adb3b824.png)

위의 `태그(키- 값)`은 EC2 인스턴스에서 지정한 것을 제대로 넣어주셔야 합니다.(이것을 기반으로 CodeDeploy가 찾아서 배포를 하기 때문에 다르면 제대로 배포를 하지 못합니다.)

<br>

![스크린샷 2021-04-28 오전 12 22 54](https://user-images.githubusercontent.com/45676906/116268133-fcf99180-a7b7-11eb-8c0d-db3ff45be136.png)

위와 같이 세팅을 한 후에 CodeDeploy 배포 그룹도 만들겠습니다. 이제 CodeDeploy도 만들었기 때문에 `.travis.yml` 파일에 CodeDeploy 내용을 추가해야합니다.

<br>

## `.travis.yml`

```yaml
language: java
jdk:
  - openjdk11

brances:
  only:
    - master

cache:
  directories:
    - '$HOME/.m2/repository'
    - '$HOME/.gradle'

script:
  - "./gradlew clean build"

before_deploy:
  - mkdir -p before-deploy # zip에 포함시킬 파일들을 담을 디렉토리 생성
  - cp scripts/*.sh before-deploy/   # 배포 스크립트 디렉토리에 담기
  - cp appspec.yml before-deploy/    # 배포에 필요한 appspec.yml
  - cp dockerfile before-deploy/     # dockerfile 넘기기
  - cp build/libs/*.jar before-deploy/  # jar 파일 넘기기
  - cd before-deploy && zip -r before-deploy * # before-deploy로 이동후 전체 압축
  - cd ../ && mkdir -p deploy # 상위 디렉토리로 이동후 deploy 디렉토리 생성
  - mv before-deploy/before-deploy.zip deploy/SpringBoot_Nginx.zip # deploy로 zip파일 이동

deploy:
  - provider: s3
    access_key_id: $AWS_ACCESS_KEY         # Travis CI 에서 IAM 사용자 엑세스 키

    secret_access_key: $AWS_SECRET_KEY     # Travis CI 에서 IAM 사용자 비밀 엑세스 키

    bucket: aws-gyun-s3        # S3 Bucket 이름
    region: ap-northeast-2
    skip_cleanup: true
    acl: private
    local_dir: deploy
    wait-until-deployed: true

  - provider: codedeploy
    access_key_id: $AWS_ACCESS_KEY        # Travis CI 에서 IAM 사용자 엑세스 키

    secret_access_key: $AWS_SECRET_KEY    # Travis CI 에서 IAM 사용자 비밀 엑세스 키

    bucket: aws-gyun-s3         # S3 Bucket 이름
    key: SpringBoot_Nginx.zip   # 위에서 만든 zip 파일 이름 !! (꼭 맞춰서 적어야함)
    bundle_type: zip
    application: Nginx          # CodeDeploy 애플리케이션 이름

    deployment_group: Nginx_Group   # CodeDeploy 배포 그룹 이름

    region: ap-northeast-2
    wait-until-deployed: true

notification:
  email:
    recipients:
      - wjdrbs966@naver.com
```

파일 전체는 위와 같습니다. 보면 `before-deploy`에서 zip 파일을 만들 때 담는 파일들이 추가되었고, deploy에서 S3 말고도 CodeDeploy가 추가되었습니다. `before-deploy`에서 담는 파일들에 대해서는 다음 글에서 정리할 예정이고 CodeDeploy 설정에 대해서 먼저 알아보겠습니다.

- codeDeploy도 AWS 서비스이기 때문에 IAM 사용자의 액세스 키, 비밀 액세스 키를 적어주어야 합니다.
- `key`: S3 버킷에 생성된 zip 파일 이름을 적어주어야 합니다.(다르게 적으면 오류나는..)
- `application`: CodeDeploy Application 이름을 적어줍니다.
- `deployment_group`: CodeDeploy 배포그룹 이름을 적어줍니다.

<br>

위와 같이 `.travis.yml` 파일을 수정 하였습니다. 그러면 일단은 `배포 자동화`는 완성이 되었습니다. (간단히 요약해서 설명했지만..) 
하지만 문제는 배포하는 동안에는 서버가 잠시 멈춘다는 점이었습니다. 이러한 문제를 해결하기 위해서 `Nginx`를 사용해서 무중단 배포를 하려고 하는 것입니다.([참고하기](https://devlog-wjdrbs96.tistory.com/309)) 

이번 편에서는 `무중단 배포`를 시작하기 전에 초기 세팅을 하였습니다. 다음 글에서 본격적으로 `Docker`, `Nginx`로 무중단 배포를 진행해보겠습니다. 

<br>

추가로 자세한 코드는 [Github](https://github.com/wjdrbs96/SpringBoot-Docker-Nginx) 에서 확인할 수 있습니다. 