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

![스크린샷 2021-09-27 오후 6 15 26](https://user-images.githubusercontent.com/45676906/134880348-bdd2deb0-ad62-4ba6-9ec2-20bd9305a54a.png)

저는 소스 코드 관리 도구를 Github을 사용할 것이기 때문에 위와 같이 `소스 공급자`를 Github을 설정하고 `Github 개인용 엑세스 토큰으로 연결`을 선택하겠습니다. 그러면 위와 같이 `Github 개인용 엑세스 토큰`을 넣으라는 곳이 생기는데요. 이제 Github으로 가서 Token을 생성하겠습니다. 

<br>

![스크린샷 2021-09-27 오후 6 17 32](https://user-images.githubusercontent.com/45676906/134880655-6e2cb50c-03a8-4bee-a3a7-736322588de7.png)

그리고 자신의 Github에 들어간 후에 위와 같이 `Settings`를 눌러서 들어가겠습니다. 

<br>

![스크린샷 2021-09-27 오후 6 18 56](https://user-images.githubusercontent.com/45676906/134880868-54008fed-7f12-458e-9846-6e70d89c5ce0.png)

그리고 왼쪽을 보면 `Developer settings`가 있는데 이것을 누르겠습니다. 

<br>

![스크린샷 2021-09-27 오후 6 19 50](https://user-images.githubusercontent.com/45676906/134881035-0e892114-86ac-4f91-b956-740a8dce485e.png)

그리고 개인용 엑세스 토큰을 만들어야 하기 때문에 `Personal access tokens`을 누르고 다음에 `Generate new Token`을 누르겠습니다. 

<br>

![스크린샷 2021-09-27 오후 6 22 15](https://user-images.githubusercontent.com/45676906/134881485-0966b7c0-fe79-4e64-956d-34a16d0e26bf.png)

저는 위와 같이 체크한 후에 토큰을 하나 생성하겠습니다. 이 토큰을 저장한 후에 다시 `AWS CodeBuild`로 가겠습니다. 

<br>

![스크린샷 2021-09-27 오후 6 24 05](https://user-images.githubusercontent.com/45676906/134881841-af1c1cad-6514-4294-a49a-eaf77726ecc5.png)

위에서 생성한 Githb Token을 저장하면 위와 같이 Github 레포지토리를 입력하라는 것으로 바뀌는데요. 여기서 자신이 사용할 레포지토리 주소를 입력하겠습니다. 

<br>

![스크린샷 2021-09-27 오후 6 25 18](https://user-images.githubusercontent.com/45676906/134882065-34692b9c-bb6f-4547-9bb4-ac8c016e416b.png)

그러면 위와 같이 `Webhook 이벤트`를 설정할 수 있는 화면이 생길텐데요. 여기서 위와 같이 설정을 하겠습니다. 이벤트 유형에는 저는 `PUSH`가 발생했을 때만 Webhook을 작동하기 위해서 `PUSH`만 선택하였습니다. 그리고 아래에 `이러한 조건에서 빌드 시작`에서 `HEAD REF`에 설정을 해주어야 하는데요. 여기서 `refs/heads/release` 라고 하면 release 브랜치에 push가 일어나면 Github 에서 CodeBuild로 WebHook을 날린다는 뜻입니다.

<br>

![스크린샷 2021-09-26 오후 4 32 00](https://user-images.githubusercontent.com/45676906/134798286-88af7943-732e-4207-a916-84ed6e69fb10.png)

그리고 다시 아래로 내려가서 환경 설정에서 위에 보이는 것처럼 체크하겠습니다. 

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

![스크린샷 2021-09-27 오후 6 29 44](https://user-images.githubusercontent.com/45676906/134882905-f435e302-92d3-44c7-bc79-16155df12814.png)

그리고 Github Release 브랜치로 push를 하니까 위와 같이 Github에서 CodeBuild로 WebHook을 날려 CodeBuild가 자동으로 작동할 것을 볼 수 있습니다.

<br>

![스크린샷 2021-09-27 오후 6 33 42](https://user-images.githubusercontent.com/45676906/134883383-5b4fb807-28c2-4d09-b106-b7d2ba8888d6.png)

그러면 위와 같이 최종 Build가 성공한 것을 볼 수 있습니다. 

<br> 

![스크린샷 2021-09-27 오후 6 35 47](https://user-images.githubusercontent.com/45676906/134883877-dcbbc118-8f38-41e4-b6ed-908d5326ce1a.png)

그리고 생성해놓았던 AWS S3로 가서 확인해보면 CodeBuild를 통해 만들어진 jar 파일이 S3에 잘 저장된 것도 확인할 수 있습니다. 이제 CD인 AWS CodeDeploy를 생성하여 EC2에 배포를 해보겠습니다.  

<br> <br>

## `CodeDeploy IAM 역할 생성하기`

CodeDeploy에게 적용 하기 위한 `IAM 역할` 하나를 생성하겠습니다. 

![스크린샷 2021-09-27 오후 6 44 53](https://user-images.githubusercontent.com/45676906/134885471-fd8661ad-06ce-40ba-8acb-8423372bdccb.png)

이미 생성해놓은 사용자에게 `S3FullAccess` 권한이 존재하는데요. 여기에 `CodeDeployFullAccess`를 하나 더 추가하겠습니다. 그래서 위의 보이는 `권한 추가`를 누르겠습니다. 

<br>

![스크린샷 2021-09-27 오후 6 46 48](https://user-images.githubusercontent.com/45676906/134885749-0030c384-48e8-4799-adab-fb365ee139ef.png)

위처럼 `CodeDeployFullAccess`를 추가하겠습니다. 그리고 EC2가 CodeDeploy를 연동받을 수 있게 EC2에게 IAM 역할 하나를 만들겠습니다. 

<br>

![EC2](https://user-images.githubusercontent.com/45676906/117380479-b83acc80-af14-11eb-87ab-c8439ff583ac.png)

<br>

![EC2](https://user-images.githubusercontent.com/45676906/117380593-0354df80-af15-11eb-93c9-837e061245ba.png)

<br>

![EC2](https://user-images.githubusercontent.com/45676906/117380723-562e9700-af15-11eb-99bc-bf5dbbacfcf2.png)

그리고 EC2로 가서 위에서 만든 역할을 적용시켜주겠습니다. 

<br>

![스크린샷 2021-09-27 오후 6 50 40](https://user-images.githubusercontent.com/45676906/134886480-b89cdf17-aaea-4dc1-ae7f-5652425df13d.png)

<br>

![스크린샷 2021-09-27 오후 6 52 01](https://user-images.githubusercontent.com/45676906/134886730-833e1b39-98fc-4fa9-a5a0-dacc389ea7cb.png)

위에서 만든 역할을 위처럼 적용시켜 주면 됩니다. 

<br> <br>

## `EC2 태그 설정`

CodeDeploy가 EC2에 배포를 할 때 태그를 기반으로 배포를 진행하기 때문에 EC2에 태그를 지정하겠습니다. 

![스크린샷 2021-09-27 오후 6 54 10](https://user-images.githubusercontent.com/45676906/134887037-a791f461-4daa-48f9-a8c6-4b6d6c0544ad.png)

<br>

![스크린샷 2021-09-27 오후 6 55 18](https://user-images.githubusercontent.com/45676906/134887115-8b0c5796-fa0e-4fa1-9a6b-86ba8c081866.png)

위와 같이 키(Name) : 값(Gyunny)로 지정하고 저장하겠습니다. 

<br> <br>

## `CodeDeploy를 위한 역할 생성하기`

이번에도 `IAM 역할 만들기`를 통해서 CodeDeploy를 위한 역할 하나를 생성하겠습니다.

![CodeDeploy](https://user-images.githubusercontent.com/45676906/117381377-e7523d80-af16-11eb-81e9-89e03dc97ae7.png)

<br>

![CodeDeploy](https://user-images.githubusercontent.com/45676906/117381411-f802b380-af16-11eb-94f1-224096f1efb8.png)

이름만 지정하고 다음을 눌러서 역할을 생성하겠습니다. 

<br> <br>

## `AWS CodeDeploy 생성하기`

![스크린샷 2021-09-27 오후 6 40 59](https://user-images.githubusercontent.com/45676906/134884722-6b9b1378-f868-4f46-a1f5-9985d66c66de.png)

<br>

![스크린샷 2021-09-27 오후 6 41 37](https://user-images.githubusercontent.com/45676906/134884820-9208950c-55fa-463e-9abd-5a700c7e2c7d.png)

위와 같이 먼저 애플리케이션을 생성하겠습니다. 

<br>

![스크린샷 2021-09-27 오후 6 42 23](https://user-images.githubusercontent.com/45676906/134884953-3f11945c-9995-46e4-bcef-66103ca83235.png)

그리고 이번에는 `배포 그룹`을 생성하겠습니다. 

<br>

![스크린샷 2021-09-27 오후 6 59 24](https://user-images.githubusercontent.com/45676906/134887825-115a8387-8ebf-4cc4-a68e-6464ebc834f6.png)

위에서 생성한 CodeDeploy 역할을 서비스 역할에다 넣어주면 됩니다. 

<br>

![스크린샷 2021-09-27 오후 7 00 45](https://user-images.githubusercontent.com/45676906/134888015-cd12aea5-e01a-4c8e-abe0-8b91f7b110b9.png)

그리고 현재는 EC2 1대로만 개발을 할 것이기 때문에 따로 [블루/그린 배포](https://devlog-wjdrbs96.tistory.com/300?category=885022) 까지는 필요 없을 거 같아서 [현재 위치 배포](https://devlog-wjdrbs96.tistory.com/304?category=885022) 로 진행하겠습니다.

<br>

![스크린샷 2021-09-27 오후 7 03 15](https://user-images.githubusercontent.com/45676906/134888317-8c2ab3ff-017c-44a1-89a8-1d23bdb0070b.png)

그리고 여기서도 현재는 EC2 1대만 사용하기 때문에 배포 구성을 `CodeDeployDefault.AllAtOnce`로 놓겠습니다. 또한 로드밸런서는 사용하지 않기 때문에 활성화 하지 않는 걸로 체크하고 배포 그룹을 만들겠습니다. 

<br> <br>

## `EC2 CodeDeploy Agent 설치하기`

CodeDeploy를 통해서 EC2로 빌드 파일들을 가져오기 위해서는 EC2에 CodeDeployAgent 설치가 필요합니다. EC2 Linux2 환경에서 설치해보겠습니다. 

```
# 패키지 매니저 업데이트, ruby 설치 
sudo yum update 
sudo yum install ruby  

sudo yum install -y aws-cli
cd /home/ec2-user/ 
sudo aws configure 

https://aws-codedeploy-ap-northeast-2.s3.ap-northeast-2.amazonaws.com/latest/install 

# 설치 파일에 실행 권한 부여 
chmod +x ./install 

# 설치 진행 및 Agent 상태 확인 
sudo ./install auto 
sudo service codedeploy-agent status // 설치가 잘 되었는지 확인하는 명령어
```

위의 명령어를 통해서 EC2에서 CodeDeploy Agent를 설치하였으면 이제 Shell Script 파일을 작성해보겠습니다. 

<br> <br>

## `appspec.yml 작성하기`

CodeBuild를 사용할 때는 `buildspec.yml`을 사용하였다면, CodeDeploy를 사용할 때는 `appspec.yml` 파일을 작성합니다. 

```yaml
version: 0.0
os: linux
files:
  - source:  /
    destination: /home/ec2-user/mash
    overwrite: yes

permissions:
  - object: /
    pattern: "**"
    owner: ec2-user
    group: ec2-user

hooks:
  ApplicationStart:
    - location: scripts/deploy.sh
      timeout: 60
      runas: ec2-user
```

- `files.destination` : S3에 있는 zip 파일이 EC2에 배포될 위치를 지정합니다.
- `ApplicationStart` : ApplicationStart 단계에서 deploy.sh를 실행시키도록 합니다.


<br> <br>

## `Shell Script 작성하기`

```shell
#!/bin/bash
BUILD_JAR=$(ls /home/ec2-user/mash/build/libs/*.jar)
JAR_NAME=$(basename $BUILD_JAR)
echo "> build 파일명: $JAR_NAME" >> /home/ec2-user/action/deploy.log

echo "> build 파일 복사" >> /home/ec2-user/mash/deploy.log
DEPLOY_PATH=/home/ec2-user/mash/
cp $BUILD_JAR $DEPLOY_PATH

echo "> 현재 실행중인 애플리케이션 pid 확인" >> /home/ec2-user/mash/deploy.log
CURRENT_PID=$(pgrep -f $JAR_NAME)

if [ -z $CURRENT_PID ]
then
  echo "> 현재 구동중인 애플리케이션이 없으므로 종료하지 않습니다." >> /home/ec2-user/mash/deploy.log
else
  echo "> kill -15 $CURRENT_PID"
  kill -15 $CURRENT_PID
  sleep 5
fi

DEPLOY_JAR=$DEPLOY_PATH$JAR_NAME
echo "> DEPLOY_JAR 배포"    >> /home/ec2-user/mash/deploy.log
nohup java -jar $DEPLOY_JAR >> /home/ec2-user/deploy.log 2>/home/ec2-user/mash/deploy_err.log &
```

그리고 모든 과정을 연결시켜줄 `AWS CodePipeline`을 생성해보겠습니다. 

<br> <br>

## `CodePipeline 생성하기`

![스크린샷 2021-09-27 오후 7 24 24](https://user-images.githubusercontent.com/45676906/134891391-6f337ab8-4326-4f81-abe5-dd7194714a1a.png)

<br>

![스크린샷 2021-09-27 오후 8 29 12](https://user-images.githubusercontent.com/45676906/134900015-cdbd4ea0-da0c-4f4f-bb41-23a07ec5617e.png)

- [기본 위치]를 선택할 경우 새로운 S3 버킷을 자동으로 생성해줍니다.
- [사용자 지정 위치]를 선택할 경우 미리 만들어 놓은 버킷을 선택할 수 있습니다.

<br>
