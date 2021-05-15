## `들어가기 전에`

![스크린샷 2021-05-14 오후 3 00 14](https://user-images.githubusercontent.com/45676906/118228399-180d1680-b4c5-11eb-84b2-8d90c379f2b5.png)

이번 글에서 `CI/CD`를 할 때는 위의 아키텍쳐로 진행하려고 합니다. (다만, Auto Scaling은 사용하지 않고 운영용 EC2 1대, Jenkins 빌드용 EC2 1대를 사용할 예정입니다.)
그래서 사용한 도구에 대해서 정리하면 아래와 같습니다. 

- `Spring Boot`
- `EC2 Linux2 2대(운영용 프리티어 1대, 젠킨스 빌드용 RAM 16G 1대)`
- `Jenkins(CI)`
- `CodeDeploy(CD)`
- `S3 Bucket`
- `Docker`
- `Github Hook`

이러한 기술들을 사용해서 CI/CD를 진행하는 글을 이어나가겠습니다. 추가로 이번 글에서 EC2 생성, S3 버켓 생성, CodeDeploy 생성, IAM 사용자 권한 부여 등등 같은 것들에 대해서는 다루지 않겠습니다.  

<br>

## `EC2 인스턴스 생성하기`

jenkins를 사용할 때 EC2 프리티어로 사용하니까 빌드를 할 때마다 EC2가 맛이가는 문제가 발생했습니다. 그래서 [여기](https://okky.kr/article/884329) 에서 저와 비슷한 상황의 분을 찾았는데 역시.. EC2 프리티어로는 버티기가 힘들었던 거 같습니다. 
그래서 젠킨스 빌드용 EC2는 성능 좋은 것으로 생성해보겠습니다. 

![1](https://user-images.githubusercontent.com/45676906/113543993-702d3f00-9622-11eb-82bc-5c7096c6a89a.png)

![2](https://user-images.githubusercontent.com/45676906/113544080-95ba4880-9622-11eb-92d8-da084cd219c5.png)

![3](https://user-images.githubusercontent.com/45676906/113544083-96eb7580-9622-11eb-8ec5-d46c4757fc37.png)

위와 같이 RAM 16G로 하나 생성하겠습니다. 

<br>

## `EC2 Linux2에 Docker 설치하기`

```
sudo yum update -y
sudo amazon-linux-extras install -y docker
sudo service docker start
```

그리고 EC2에 접속한 후에 위의 명령어로 EC2에 Docker를 설치하겠습니다. 

<br>

## `docker로 jenkins 설치하기`

```
sudo docker run -d --name jenkins -p 32789:8080 jenkins/jenkins:jdk11
```

![스크린샷 2021-04-05 오전 12 09 37](https://user-images.githubusercontent.com/45676906/113513182-44b73f80-95a3-11eb-9d67-3e71bb445725.png)

위와 같이 컨테이너가 잘 실행되고 있다면 `http://EC2-IP:32789`로 접속하겠습니다. 

![스크린샷 2021-04-05 오전 12 08 58](https://user-images.githubusercontent.com/45676906/113513215-79c39200-95a3-11eb-8461-70555aad41b6.png)

![스크린샷 2021-04-05 오전 12 13 44](https://user-images.githubusercontent.com/45676906/113513288-f8b8ca80-95a3-11eb-8757-356f9504657b.png)

```
sudo docker exec -it jenkins bash  // jenkins bash 쉘 접속
cat /var/jenkins_home/secrets/initialAdminpassword
```

위와 같이 jenkins bash에 접속해서 비밀번호를 알아낸 후 접속하겠습니다. 

![스크린샷 2021-04-05 오전 12 16 43](https://user-images.githubusercontent.com/45676906/113513346-42091a00-95a4-11eb-9727-f40e456886eb.png)

![스크린샷 2021-04-05 오전 12 20 15](https://user-images.githubusercontent.com/45676906/113513440-b5ab2700-95a4-11eb-9f86-1c9a56fe86f0.png)

그리고 원하는 계정으로 가입을 하겠습니다. 

![스크린샷 2021-04-05 오전 12 21 26](https://user-images.githubusercontent.com/45676906/113513469-e723f280-95a4-11eb-9523-47ce65bb7151.png)

![스크린샷 2021-04-05 오전 12 21 32](https://user-images.githubusercontent.com/45676906/113513499-04f15780-95a5-11eb-8632-1d263fe49489.png)

![스크린샷 2021-04-05 오전 12 24 55](https://user-images.githubusercontent.com/45676906/113513652-b85a4c00-95a5-11eb-8b13-7a89af63db96.png)

그리고 현재 Jenkins로 CI(빌드 및 테스트)를 하고 이 결과를 S3에 저장합니다. 그리고 저장된 빌드 파일을 AWS CodeDeploy가 EC2에 CD(자동 배포)를 해주는 것이기 때문에 Jenkins에 CodeDeploy 플러그인을 설치하겠습니다.

![스크린샷 2021-04-05 오전 12 30 05](https://user-images.githubusercontent.com/45676906/113513742-1a1ab600-95a6-11eb-80b8-779bd1365f0d.png)

![스크린샷 2021-04-05 오전 12 31 03](https://user-images.githubusercontent.com/45676906/113513803-51896280-95a6-11eb-8583-51aa56666929.png)

그리고 위와 같이 프로젝트를 하나 생성하겠습니다. 

![스크린샷 2021-04-05 오전 12 32 48](https://user-images.githubusercontent.com/45676906/113513857-944b3a80-95a6-11eb-9cd3-7507d9c53103.png)

![스크린샷 2021-04-05 오후 5 35 27](https://user-images.githubusercontent.com/45676906/113555406-d7a0ba00-9635-11eb-83b3-3db07ad1488a.png)

Github에 push가 되었을 때 Github이 Jenkins에 Hook을 날려 자동 빌드할 수 있도록 `Github hook Trigger`를 선택하겠습니다.(아래에서 Github Hook 설정을 하게 됩니다.) 

```
./gradlew clean build
```

그리고 Build 부분을 보면 위와 같이 적었습니다. Spring Boot `gradle`은 위의 명령어를 통해서 jar 파일을 만들기 때문입니다. 

![스크린샷 2021-04-05 오전 12 41 36](https://user-images.githubusercontent.com/45676906/113514102-b85b4b80-95a7-11eb-914d-cd1dfabc267a.png)

또한 이번에는 `빌드 후 조치`로 Jenkins 빌드가 끝난 이후에 CodeDeploy가 배포하는 단계의 설정입니다. 

![스크린샷 2021-04-05 오후 5 33 38](https://user-images.githubusercontent.com/45676906/113555527-0880ef00-9636-11eb-9e22-6648fbd36bb0.png)

![스크린샷 2021-04-05 오전 12 48 20](https://user-images.githubusercontent.com/45676906/113514291-b776e980-95a8-11eb-935e-c14d74efbe96.png)

- `AWS S3 Bucket, CodeDeploy 애플리케이션, 배포 그룹을 만들었을텐데 그것의 이름을 위에 맞게 적으면 됩니다.`
- `Include Files를 보면 CodeDeploy가 EC2로 옮길 파일들을 적는 것입니다.`
- `마지막으로 IAM 사용자에게 발급받은 액세스 키, 비밀 엑세스 키가 있을 것입니다. 그것을 입력하겠습니다.`

그러면 이제 Jenkins에 대한 기본적인 설정은 끝났습니다. 

<br>

## `Github WebHook 추가`

Github에 push가 되었을 때 Jenkins로 Hook을 날려 빌드가 실행되도록 하는 역할을 합니다.

<img width="818" alt="스크린샷 2021-04-05 오전 12 50 38" src="https://user-images.githubusercontent.com/45676906/113514345-f147f000-95a8-11eb-9f77-3831ed271010.png">

![스크린샷 2021-04-05 오전 12 51 02](https://user-images.githubusercontent.com/45676906/113514398-3835e580-95a9-11eb-9851-61f6a8d0aba5.png)

- `PayLoad URL에 http://{EC2 IP}:32789/github-webhook/`을 입력하겠습니다.(마지막에 / 안쓰면 302에러 뜨니 꼭 써야 합니다!))
- `application/json 선택`

![스크린샷 2021-04-05 오전 12 53 12](https://user-images.githubusercontent.com/45676906/113514405-4dab0f80-95a9-11eb-9554-68f72141330b.png)

그리고 위와 같이 초록색 체크 버튼이 떠야 제대로 등록이 된 것입니다. 

<br>

## `EC2 Linux2 CodeAgent 설치하기`

CodeDeploy가 EC2에 배포를 하기 위해서는 CodeAgent 설치가 필요합니다. 그래서 아래의 명령어를 통해서 설치를 하겠습니다. 

```
sudo yum install -y aws-cli
cd /home/ec2-user/ 
sudo aws configure 
wget https://aws-codedeploy-ap-northeast-2.s3.amazonaws.com/latest/install
chmod +x ./install
sudo ./install auto
sudo service codedeploy-agent status
```

![test](https://t1.daumcdn.net/cfile/tistory/992FB64D5AC566E018)

여기서도 Jenkins에서 입력했던 것과 마찬가지로 `IAM 사용자의 액세스 키, 비밀 엑세스 키`를 입력해주면 됩니다. 

<br>

## `스크립트 파일과 yml 파일 작성하기`

`CodeDeploy`가 배포를 어디에 어떤 과정으로 할 지를 담은 파일은 `appspec.yml`이고, `deploy.sh`는 EC2 내부에서 동작하도록 만든 스크립트 파일입니다.  

![스크린샷 2021-04-06 오전 9 47 37](https://user-images.githubusercontent.com/45676906/113643404-36f5dd00-96bd-11eb-8512-bf7d07af6533.png)

파일의 구조는 위와 같습니다. 하나씩 작성을 해보겠습니다.

<br>

### `appspec.yml`

```yaml
version: 0.0
os: linux
files:
  - source:  /
    destination: /home/ec2-user/jenkins  # EC2 내부 배포 할 위치
    overwrite: yes

permissions:
  - object: /
    pattern: "**"
    owner: ec2-user
    group: ec2-user

hooks:
  ApplicationStart:
    - location: scripts/deploy.sh    # ApplicationStart 단계에서 해당 파일을 실행해라
      timeout: 60
      runas: ec2-user
```

<br>

### `deploy.sh`

```shell script
#!/bin/bash
BUILD_JAR=$(ls /home/ec2-user/jenkins/build/libs/*.jar)     # jar가 위치하는 곳
JAR_NAME=$(basename $BUILD_JAR)
echo "> build 파일명: $JAR_NAME" >> /home/ec2-user/deploy.log

echo "> build 파일 복사" >> /home/ec2-user/deploy.log
DEPLOY_PATH=/home/ec2-user/
cp $BUILD_JAR $DEPLOY_PATH

echo "> 현재 실행중인 애플리케이션 pid 확인" >> /home/ec2-user/deploy.log
CURRENT_PID=$(pgrep -f $JAR_NAME)

if [ -z $CURRENT_PID ]
then
  echo "> 현재 구동중인 애플리케이션이 없으므로 종료하지 않습니다." >> /home/ec2-user/deploy.log
else
  echo "> kill -15 $CURRENT_PID"
  kill -15 $CURRENT_PID
  sleep 5
fi

DEPLOY_JAR=$DEPLOY_PATH$JAR_NAME
echo "> DEPLOY_JAR 배포"    >> /home/ec2-user/deploy.log
nohup java -jar $DEPLOY_JAR >> /home/ec2-user/deploy.log 2>/home/ec2-user/deploy_err.log &
```

위의 스크립트 파일을 통해서 최종 자동 배포의 과정이 일어나는 것입니다. 그리고 Github의 아래와 같이 간단한 Controller를 만든 후에 push를 해보겠습니다. 

![스크린샷 2021-04-06 오전 9 56 48](https://user-images.githubusercontent.com/45676906/113643913-69540a00-96be-11eb-9dfb-18a83d841ceb.png)

![스크린샷 2021-04-05 오후 5 31 36](https://user-images.githubusercontent.com/45676906/113555779-6b728600-9636-11eb-8840-70370695f2ff.png)

그러면 위와 같이 Jenkins CI가 성공한 것도 확인할 수 있습니다. 또한 CodeDeploy도 성공 되었나 확인해보겠습니다.

![스크린샷 2021-04-06 오전 9 58 33](https://user-images.githubusercontent.com/45676906/113644023-b637e080-96be-11eb-883c-5df641524647.png)

그리고 CodeDeploy도 보면 성공한 것을 볼 수 있습니다. 성공했다면 EC2에도 파일들이 잘 배포가 되었을 것입니다.  

![스크린샷 2021-04-06 오전 9 54 15](https://user-images.githubusercontent.com/45676906/113643870-4f1a2c00-96be-11eb-9c17-4720e25f467e.png)

실제로 EC2 내부를 보면 위와 같이 파일들이 잘 전달된 것을 볼 수 있습니다. 

![스크린샷 2021-04-06 오전 10 00 04](https://user-images.githubusercontent.com/45676906/113644106-e7181580-96be-11eb-848b-c0423790f503.png)

그리고 보면 실제로 `8080 포트`의 java가 실행되고 있는 것을 볼 수 있습니다. 

![스크린샷 2021-04-06 오전 10 01 08](https://user-images.githubusercontent.com/45676906/113644168-162e8700-96bf-11eb-8131-1a096c161125.png)

