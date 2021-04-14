# `CodeBuild 실습하기`

![스크린샷 2021-03-19 오후 4 04 25](https://user-images.githubusercontent.com/45676906/111743335-e153bf00-88cc-11eb-856d-a4b08ad29faf.png)

`CodeBuild` 프로젝트를 하나 만들겠습니다. 

![스크린샷 2021-03-19 오후 4 06 18](https://user-images.githubusercontent.com/45676906/111743499-24ae2d80-88cd-11eb-8dd6-b3ae661d093d.png)

위와 같이 `프로젝트 이름`에 원하는 이름을 적어준 후에 아래로 내려가겠습니다. 

![스크린샷 2021-03-19 오후 4 06 43](https://user-images.githubusercontent.com/45676906/111743642-5d4e0700-88cd-11eb-89b2-3ba1d4de6ea3.png)

- `소스 공급자`: AWS CodeCommit을 사용할 것이기 때문에 CodeCommit을 선택합니다.
- `레포지토리`: CodeCommit에서 만들었던 레포지토리 이름을 적겠습니다. 
- `브랜치`: 저는 master 브랜치를 빌드할 것이기 때문에 master 브랜치를 선택하겠습니다. 

![스크린샷 2021-03-19 오후 4 10 34](https://user-images.githubusercontent.com/45676906/111743882-aef69180-88cd-11eb-8081-4ec11e66aa06.png)

거의 대부분은 `default` 설정 그대로 두고, 저는 linux를 사용하기 때문에 linux를 선택하고 다음 누르겠습니다. 

![스크린샷 2021-03-19 오후 4 12 24](https://user-images.githubusercontent.com/45676906/111744101-009f1c00-88ce-11eb-84c3-9525e35ad8c3.png)

![스크린샷 2021-03-19 오후 4 16 42](https://user-images.githubusercontent.com/45676906/111744397-87ec8f80-88ce-11eb-9296-df27752fb4c6.png)

![스크린샷 2021-03-19 오후 4 18 39](https://user-images.githubusercontent.com/45676906/111744605-d1d57580-88ce-11eb-9023-567d4aacb4b2.png)

![스크린샷 2021-03-19 오후 4 20 00](https://user-images.githubusercontent.com/45676906/111744713-00ebe700-88cf-11eb-9bba-bc7ad89117c9.png)

위와 같이 마지막 로그까지 설정을 해준 후에 `프로젝트 생성`을 누르겠습니다. 

그리고 Spring Boot gradle 기반으로 프로젝트 하나 생성한 후에 root 디렉토리 바로 밑에 아래의 파일을 추가하겠습니다. 

<br>

## `buildspec.yml`

```yaml
version: 0.2
phases:
  build:
    commands:
      - echo Build Starting on `date`
      - chmod +x ./gradlew
      - ./gradlew build
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
- `artifacts` : 빌드결과파일. [discard-paths]에 yes값을 줘서 해당 빌드결과파일이 s3로 업로드 될 때 [files]에 기술된 path(build/libs)는 무시되고 파일명으로만 업로드 될 수 있도록 한다.
- `cache.paths` : 이곳의 파일을 S3 cache에 등록. maven의 경우는 '/root/.m2/**/*'

그리고 `Build`를 눌러보면 아래와 같이 성공하는 것을 볼 수 있습니다.

![스크린샷 2021-03-19 오후 5 28 00](https://user-images.githubusercontent.com/45676906/111752053-8f189b00-88d8-11eb-9bff-571e28bc54c0.png)

![스크린샷 2021-03-19 오후 5 31 39](https://user-images.githubusercontent.com/45676906/111752410-04846b80-88d9-11eb-9f81-67c4ccc55a0a.png)

그리고 S3 bucket에 들어가서 확인해보면 빌드 파일이 존재하는 것을 볼 수 있습니다. 