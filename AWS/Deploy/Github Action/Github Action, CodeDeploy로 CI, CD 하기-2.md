# `Github Action, CodeDeploy로 CI/CD 하기 - 2편`

[저번 글](https://devlog-wjdrbs96.tistory.com/361) 에서 `Github Actions`로 프로젝트 빌드를 수행한 후에 jar 파일을 S3로 업로드 하는 것까지 알아보았습니다. 

<br>

<img width="617" alt="스크린샷 2021-07-12 오후 1 44 58" src="https://user-images.githubusercontent.com/45676906/125232329-5aad6c80-e317-11eb-945c-7ec58c011a80.png">

이번 글에서는 S3에 업로드 된 zip 파일을 `CodeDeploy-Agent`를 통해서 EC2에 자동으로 배포를 하는 CD를 하는 법에 대해서 알아보겠습니다.

<br>

## `CodeDeploy 배포 과정`

- 개발하고 있는 프로젝트 최상단 경로에 `appspec.yml` 이라는 파일을 작성합니다. (이 파일에서 각 배포 단계에서 어떤 스크립트 파일을 실행할 것인지 명시할 수 있습니다.)

- Agent는 S3에서 zip 파일을 내려 바고, `appspec.yml` 파일을 읽어 해당 파일에 적힌 배포 단계에 맞게 스크립트 파일을 실행시키면서 진행합니다.

- Agent는 배포를 진행한 후 CodeDeploy에게 성공/실패 등의 결과를 알려줍니다. 

<br>

그리고 EC2 Linux2 버전으로 생성하고 IAM 역할 설정을 해주어야 하는데요. 그 설정하는 것은 이 글에서는 생략하겠습니다. 설정하고 생성하는 방법이 궁금하다면 [여기](https://wbluke.tistory.com/40?category=418851) 를 참고해주세요.

<br>

## `CodeDeploy Agent 설치하기`

```
# 패키지 매니저 업데이트, ruby 설치 
sudo yum update 
sudo yum install ruby 
sudo yum install wget 

# 서울 리전에 있는 CodeDeploy 리소스 키트 파일 다운로드 
cd /home/ec2-user wget 

https://aws-codedeploy-ap-northeast-2.s3.ap-northeast-2.amazonaws.com/latest/install 

# 설치 파일에 실행 권한 부여 
chmod +x ./install 

# 설치 진행 및 Agent 상태 확인 
sudo ./install auto 
sudo service codedeploy-agent status
```

위의 명령어를 통해서 `EC2 Linux2`에서 CodeDeploy-Agent를 설치하겠습니다. 

<br>

![스크린샷 2021-07-12 오후 4 09 19](https://user-images.githubusercontent.com/45676906/125245442-89354280-e32b-11eb-8be0-35b4160affc8.png)

위와 같이 설치한 후에 CodeDeploy-Agent가 잘 실행되고 있는지 확인할 수 있습니다. 

<br> <br>

## `CodeDeploy 생성하기`

<img width="911" alt="스크린샷 2021-07-12 오후 4 11 38" src="https://user-images.githubusercontent.com/45676906/125245774-eaf5ac80-e32b-11eb-8a66-1d23d0443113.png">

<br>

<img width="882" alt="스크린샷 2021-07-12 오후 4 14 25" src="https://user-images.githubusercontent.com/45676906/125246141-59d30580-e32c-11eb-892c-4ad53f630863.png">

<br>

![스크린샷 2021-07-12 오후 4 16 37](https://user-images.githubusercontent.com/45676906/125246356-a4548200-e32c-11eb-9406-7de7a4a15429.png)

- [현재 위치 배포](https://devlog-wjdrbs96.tistory.com/304?category=885022) 방식으로 진행하겠습니다.

- 태그 그룹에 EC2에서 지정한 태그를 정확하게 설정해야 CodeDeploy를 통해서 해당 EC2에 배포를 진행할 수 있습니다. 

<br>

<img width="948" alt="스크린샷 2021-07-12 오후 4 19 24" src="https://user-images.githubusercontent.com/45676906/125246669-0a410980-e32d-11eb-91c1-b06cbdf5b7e1.png">

<br>

<img width="884" alt="스크린샷 2021-07-12 오후 4 26 15" src="https://user-images.githubusercontent.com/45676906/125247469-f21dba00-e32d-11eb-8676-cdc6ca9ddee2.png">


<br> <br>

## `스크립트 추가하기`

위에서 CodeDeploy를 동작시키기 위해서는 `appspec.yml` 파일이 필요하다고 했습니다. 

<img width="464" alt="스크린샷 2021-07-12 오후 4 21 54" src="https://user-images.githubusercontent.com/45676906/125246923-55f3b300-e32d-11eb-9673-e8e3eb6f410c.png">

위와 같이 프로젝트 최상단에 `appspec.yml`을 작성하겠습니다. 

<br>

## `appspec.yml`

```yaml
version: 0.0
os: linux
files:
  - source:  /
    destination: /home/ec2-user/action
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

- ### files.destination
    - S3에 있는 zip 파일이 EC2에 배포될 위치를 지정합니다. 
    
- ### ApplicationStart
    - ApplicationStart 단계에서 deploy.sh를 실행시키도록 합니다.

<br> <br>

## `Shell Script 파일 작성하기`

```shell
#!/bin/bash
BUILD_JAR=$(ls /home/ec2-user/action/build/libs/*.jar)
JAR_NAME=$(basename $BUILD_JAR)
echo "> build 파일명: $JAR_NAME" >> /home/ec2-user/action/deploy.log

echo "> build 파일 복사" >> /home/ec2-user/action/deploy.log
DEPLOY_PATH=/home/ec2-user/action/
cp $BUILD_JAR $DEPLOY_PATH

echo "> 현재 실행중인 애플리케이션 pid 확인" >> /home/ec2-user/action/deploy.log
CURRENT_PID=$(pgrep -f $JAR_NAME)

if [ -z $CURRENT_PID ]
then
  echo "> 현재 구동중인 애플리케이션이 없으므로 종료하지 않습니다." >> /home/ec2-user/action/deploy.log
else
  echo "> kill -15 $CURRENT_PID"
  kill -15 $CURRENT_PID
  sleep 5
fi

DEPLOY_JAR=$DEPLOY_PATH$JAR_NAME
echo "> DEPLOY_JAR 배포"    >> /home/ec2-user/action/deploy.log
nohup java -jar $DEPLOY_JAR >> /home/ec2-user/deploy.log 2>/home/ec2-user/action/deploy_err.log &
```

위의 스크립트 파일은 현재 jar가 실행 중이라면 kill 한 후에 jar를 다시 실행하는 간단한 스크립트 파일입니다. 하나씩 의미를 파악해보면 그리 어렵지 않게 이해하실 수 있습니다.

<br> <br>

## `Github Actions yml 작성`

```yaml
name: gyunny-action

on:
  push:
    branches:
      - master
  workflow_dispatch:

env:
  S3_BUCKET_NAME: aws-gyun-s3

jobs:
  build:
    runs-on: ubuntu-latest

    steps:
      - name: Checkout
        uses: actions/checkout@v2

      - name: Set up JDK 11
        uses: actions/setup-java@v1
        with:
          java-version: 11

      - name: Grant execute permission for gradlew
        run: chmod +x ./gradlew
        shell: bash

      - name: Build with Gradle
        run: ./gradlew build
        shell: bash
        
      - name: Make zip file
        run: zip -r ./$GITHUB_SHA.zip .
        shell: bash

      - name: Configure AWS credentials
        uses: aws-actions/configure-aws-credentials@v1
        with:
          aws-access-key-id: ${{ secrets.AWS_ACCESS_KEY_ID }}
          aws-secret-access-key: ${{ secrets.AWS_SECRET_ACCESS_KEY }}
          aws-region: ${{ secrets.AWS_REGION }}

      - name: Upload to S3
        run: aws s3 cp --region ap-northeast-2 ./$GITHUB_SHA.zip s3://$S3_BUCKET_NAME/$GITHUB_SHA.zip

      # 새로 추가 된 부분
      - name: Code Deploy
        run: aws deploy create-deployment --application-name Github-Actions-Deploy --deployment-config-name CodeDeployDefault.AllAtOnce --deployment-group-name Github-Actions-Group --s3-location bucket=$S3_BUCKET_NAME,bundleType=zip,key=$GITHUB_SHA.zip
```

- ### application-name
    - CodeDeploy 애플리케이션의 이름을 지정합니다. 
    
- ### deployment-config-name
    - 배포 그룹 설정에서 선택했던 배포 방식을 지정합니다. 
    
- ### deployment-group-name
    - 배포 그룹의 이름입니다.
    
- ### s3-location
    - jar를 S3에서 가지고 오기 위해 차례로 S3 Bucket 이름, 파일 타입(ex: zip), 파일 경로를 입력합니다.
    
<br>

위와 같이 `shell script`, `yml` 파일을 수정 한 후에 Github으로 push 하겠습니다. 

<br>

<img width="742" alt="스크린샷 2021-07-12 오후 5 13 03" src="https://user-images.githubusercontent.com/45676906/125253508-74a97800-e334-11eb-82a9-e4cc157249c3.png">

그러면 위와 같이 Github Action은 잘 성공한 것을 볼 수 있습니다. 그리고 바로 `CodeDeploy`로 가서 잘 실행되고 있는지 확인해보겠습니다. 

<br>

<img width="1636" alt="스크린샷 2021-07-12 오후 5 14 30" src="https://user-images.githubusercontent.com/45676906/125253672-a0c4f900-e334-11eb-85e2-b68121757e4b.png">

CodeDeploy도 모두 문제 없이 잘 성공한 것을 볼 수 있습니다. 이제 EC2로 접속해서 jar가 잘 실행되었는지, 프로젝트는 제대로 넘어왔는지를 확인해보겠습니다. 

<br>

<img width="794" alt="스크린샷 2021-07-12 오후 5 18 07" src="https://user-images.githubusercontent.com/45676906/125254240-3cef0000-e335-11eb-979c-dea53077523a.png">

EC2에 접속해서 `action` 폴더 아래에 확인해보면 위와 같이 프로젝트가 제대로 EC2로 전달된 것도 확인할 수 있고 jar도 8081 포트로 잘 실행되고 있는 것을 볼 수 있습니다.(일부로 8081 포트로 실행시켰습니다.)

이렇게 이번 글에서 `Github actions`, `CodeDeploy`로 CI/CD 하는 법에 대해서 알아보았습니다. 