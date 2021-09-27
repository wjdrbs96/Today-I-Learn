# `Github, CodeBuild, CodeDeploy로 CI/CD 하는 법`

이번 글에서는 `CodeBuild`, `CodeDeploy`를 사용해서 Spring Boot Application을 자동화 배포하는 법에 대해서 정리해보겠습니다. 

![스크린샷 2021-09-26 오후 4 13 53](https://user-images.githubusercontent.com/45676906/134797709-47382c50-ebcb-49c8-8861-417fe3472d08.png)

- Kotlin, Spring Boot
- EC2, S3
- Github, CodeBuild, CodeDeploy

<br>

사용하고자 하는 기술 스택은 위와 같고 제가 현재 하고자 하는 아키텍쳐는 위와 같습니다. 이번 글에서 `EC2 생성`, `S3 버킷 생성`에 대해서는 다루지 않고, CodeBuild, CodeDeploy를 생성해서 전체 서비스를 연결하는 것을 해보려합니다. 바로 CodeBuild 부터 진행해보겠습니다. 

<br> <br>

## `CodeBuild 설정하기`

CI 도구를 선택할 때 Github Actions, Jenkins, Travis CI, CodeBuild가 선택지가 있었습니다. 어떤 것을 사용할까 하다가 많이 사용해보지 않아서 공부해보기도 좋고, 프리티어를 제공해서 무료로 사용할 수 있고 사이트 프로젝트 용으로도 나쁘지 않은? CodeBuild를 선택하기로 했습니다. 

![스크린샷 2021-09-26 오후 4 20 32](https://user-images.githubusercontent.com/45676906/134797926-5b6b8c38-2ffa-4bb5-b6b4-3f5a24a16ac1.png)

AWS에서 CodeBuild를 들어가면 위와 같은 화면을 만날 수 있는데요. 여기서 `빌드 프로젝트 생성`을 누르겠습니다. 

<br>

![스크린샷 2021-09-26 오후 4 21 55](https://user-images.githubusercontent.com/45676906/134797963-d839f288-047f-4ebc-b35d-25a75493ea43.png)

<br>

![스크린샷 2021-09-26 오후 4 29 54](https://user-images.githubusercontent.com/45676906/134798218-e12f240f-d2e6-48c3-a7dc-a1afdbb232fc.png)

저는 소스 코드 관리 도구를 Github을 사용할 것이기 때문에 위와 같이 `소스 공급자`를 Github을 설정하고 `OAuth를 사용하여 Github과 연결해보겠습니다.` 그리고 소스버전에 `feature/ci-cd`라고 해놓았는데 나중에는 develop, master로 바꿀 예정입니다. 즉, feature/ci-cd 브랜치가 변경되었을 때 CodeBuild가 작동되도록 설정하는 것입니다. 

<br>

![스크린샷 2021-09-26 오후 4 22 43](https://user-images.githubusercontent.com/45676906/134798047-8fe3df5f-1996-45bb-9fb6-8b67735c6d77.png)

<br>

![스크린샷 2021-09-26 오후 4 32 00](https://user-images.githubusercontent.com/45676906/134798286-88af7943-732e-4207-a916-84ed6e69fb10.png)

그리고 환경 설정에서 위에 보이는 것처럼 체크하겠습니다. 

<br>

![스크린샷 2021-09-26 오후 4 33 27](https://user-images.githubusercontent.com/45676906/134798335-8d39aa09-f968-4a22-8955-e75a5cd55e4d.png)

그리고 위에 보이는 것처럼 `새로운 IAM 역할`을 만들고, 나중에 CodeBuild를 위해 사용할 파일 이름을 `buildspec.yml` 이라고 지정하겠습니다. 

<br>

![스크린샷 2021-09-26 오후 4 42 39](https://user-images.githubusercontent.com/45676906/134798588-6a20bd2b-03d7-4145-8471-2ea14e23f572.png)

위와 같이 선택하고 CodeBuild를 생성하겠습니다. 그리고 이제 `buildspec`을 정의해야 하는데요. `buildspec.yml`에 작성해보겠습니다.  

<br> <br>

## `buildspec.yml`

```yaml
version: 0.2
phases:
  build:
    commands:
      - echo Build Starting on `date`
      - chmod +x ./gradlew
      - ./gradlew clean build
  post_build:
    commands:
      - echo $(basename ./build/libs/*.jar) 
      - pwd
artifacts:
  files:
    - build/libs/*.jar
  discard-paths: yes
cache:
  paths:
    - '/root/.gradle/caches/**/*'
```

- `version` : 0.2 권장
- `phases.build.commands` : 빌드 시 수행되는 명령어. gradlew에 실행권한을 준 후 gradlew build 수행
- `phases.post_build step.commands` : 빌드 후 수행되는 명령어. build/libs에 있는 jar파일명 화면 출력
- `artifacts` : 빌드결과파일. [discard-paths]에 yes값을 줘서 해당 빌드결과파일이 s3로 업로드 될 때 [files]에 기술된 path(build/libs)는 무시되고 파일명으로만 업로드 될 수 있도록 함`
- `cache.paths` : 이곳의 파일을 S3 cache에 등록. maven의 경우는 '/root/.m2/**/*'

<br>

이제 위의 yml 파일을 Spring Boot 프로젝트에 추가하겠습니다.

![스크린샷 2021-09-26 오후 4 48 37](https://user-images.githubusercontent.com/45676906/134798763-6b511307-eb3d-4b31-b077-4c5fc2872a99.png)

위와 같이 `buildspec.yml`을 지정하고 위의 코드를 넣어주겠습니다. 

<br>




