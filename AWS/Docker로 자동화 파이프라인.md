# `Docker를 사용하여 CI/CD 파이프라인 구축하기`

![test](https://user-images.githubusercontent.com/45676906/95607939-6a9dd480-0a97-11eb-8d68-55000fda8654.png)

위의 아키텍쳐와 비슷하게 예제를 진행해보려 합니다. 다만 EC2 인스턴스 내부에 `Nginx`를 사용하지는 않고 `Docker`를 사용하여 Spring Boot jar 파일을 실행시켜보겠습니다. 
(이 글에서는 EC2, S3, CodeDeploy, Travis CI를 생성하고 연결하는 법에 대해서는 다루지 않겠습니다.)

사용하고자 하는 도구는 아래와 같습니다. 

- `Spring Boot(gradle)`
- `Travis CI(CI 담당)`
- `AWS S3 Bucket(CI 빌드 결과를 저장하기 위함)`
- `AWS CodeDeploy(CD, 자동화 배포를 담당)`
- `AWS EC2`
- `Docker`

<br>

## `Travis CI 사용하기`

Travis CI의 레포지토리, Github 연결은 했다고 가정하고 AWS IAM 사용자 키를 등록하는 과정만 보겠습니다. 

<img width="1433" alt="1" src="https://user-images.githubusercontent.com/45676906/113477296-d3e92800-94bb-11eb-8ed9-3d86e535d1f1.png">

`Settings`를 눌러서 Travis CI에 키를 등록하겠습니다.

![1](https://user-images.githubusercontent.com/45676906/113477319-e95e5200-94bb-11eb-89d8-412efc64e670.png)

S3 버켓에 접근 권한이 있는 IAM 사용자 `엑세스 키`와 `비밀 엑세스 키`를 등록하겠습니다. (나중에 travis.yml 작성할 때 사용되니 이름을 잘 기억해야 합니다.)

<br>

![스크린샷 2021-04-03 오후 8 49 35](https://user-images.githubusercontent.com/45676906/113477622-30e5dd80-94be-11eb-960f-38b22f4b5023.png)

앞으로 작성할 파일은 위의 디렉토리 위치에 맞춰서 작성하겠습니다.

<br>

### `travis.yml 작성하기`

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
  - cp scripts/*.sh before-deploy/
  - cp Dockerfile before-deploy/     # Dockerfile 복사하기 
  - cp appspec.yml before-deploy/    # CodeDeploy 에게 필요한 appspec.yml 파일
  - cp build/libs/*.jar before-deploy/
  - cd before-deploy && zip -r before-deploy * # before-deploy로 이동후 전체 압축
  - cd ../ && mkdir -p deploy # 상위 디렉토리로 이동후 deploy 디렉토리 생성
  - mv before-deploy/before-deploy.zip deploy/SpringBoot_CI-CD.zip # deploy로 zip파일 이동

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
    key: SpringBoot_CI-CD.zip
    bundle_type: zip
    application: Test              # CodeDeploy 애플리케이션 이름

    deployment_group: Test-Group   # CodeDeploy 배포 그룹 이름

    region: ap-northeast-2
    wait-until-deployed: true

notification:
  email:
    recipients:
      - wjdrbs966@naver.com
```

- ### `before-deploy`
    - deploy 명령어가 실행되기 전에 수행됩니다.
    - CodeDeploy는 jar 파일을 인식하지 못하므로 jar + 기타 파일들을 모아 압축(zip) 합니다.
    
- ### `deploy`
    - S3 파일 업로드 혹은 CodeDeploy로 배포 등 외부 서비스와 연동하는 것들에 대해 작성한 것입니다.
    

즉 위의 과정을 통해서 `CI` 과정을 거치게 되고 이 과정 속에서 `jar` 파일을 만들어 이 파일을 S3에 저장하는 것입니다. 

<br>

## `EC2 Linux2 인스턴스에 CodeAgent 설치하기`

```
yum -y update
yum install -y ruby
yum install -y aws-cli
cd /home/ec2-user
aws s3 cp s3://aws-codedeploy-us-east-2/latest/install . --region us-east-2
chmod +x ./install
./install auto
```

위와 같이 차례대로 설치를 하겠습니다. 

<br>

### `CodeAgent 상태 확인`

```
sudo service codedeploy-agent status
```

![스크린샷 2021-03-22 오후 3 27 38](https://user-images.githubusercontent.com/45676906/111948931-325cf080-8b23-11eb-8c54-d31a7a89b880.png)

위와 같이 `CodeAgent`가 잘 작동 중인지 확인했을 때 위와 같이 나오면 잘 작동 중인 것입니다! 그리고 CodeDeploy 애플리케이션 생성 및 배포 그룹 생성은 생략하겠습니다. 

그러면 이제 CodeDeploy에서 필요한 `appspec.yml` 파일과 EC2 내부에서 동작하게 하는 스크립트 파일에 대해서 작성해보겠습니다. 

<br>

## `appspec.yml 파일 작성`

```yaml
version: 0.0
os: linux    # EC2 운영체제
files:
  - source:  /         
    destination: /home/ec2-user/app/step2/zip/  # EC2 내부에 목적지 경로
    overwrite: yes   # 덮어쓸 것인지?

permissions:
  - object: /
    pattern: "**"
    owner: ec2-user
    group: ec2-user

hooks:
  ApplicationStart:
    - location: deploy.sh # 아래에서 작성한 스크립트 파일 이름
      timeout: 60
      runas: ec2-user
```

여기서 띄어쓰기 or 파일이름 오타 등등 많은 이유로 에러가 발생하니까 신중하게 작성해야 합니다. 위의 appspec.yml 파일을 통해서 CodeDeploy가 S3의 jar 파일을 받아서 EC2에 전달할 수 있는 것입니다.

<br>

## `deploy.sh 작성`

```shell script
echo "> 현재 실행 중인 Docker 컨테이너 pid 확인" >> /home/ec2-user/deploy.log
CURRENT_PID=$(sudo docker container ls -q)

if [ -z $CURRENT_PID ]
then
  echo "> 현재 구동중인 Docker 컨테이너가 없으므로 종료하지 않습니다." >> /home/ec2-user/deploy.log
else
  echo "> sudo docker stop $CURRENT_PID"   # 현재 구동중인 Docker 컨테이너가 있다면 모두 중지
  sudo docker stop $CURRENT_PID
  sleep 5
fi

cd /home/ec2-user/app/step2/zip/        # 해당 디렉토리로 이동 (Dockerfile 을 해당 디렉토리에 옮겼기 때문에)
sudo docker build -t gyunny ./          # Docker Image 생성 
sudo docker run -d -p 8080:8080 gyunny  # Docker Container 생성
sudo docker run -d -p 8081:8080 gyunny  # Docker Container 생성
```

![스크린샷 2021-04-03 오후 8 51 38](https://user-images.githubusercontent.com/45676906/113477663-64286c80-94be-11eb-9fe8-da3d8d8a3c30.png)

그리고 위와 같이 실제로 테스트 해볼 수 있는 간단한 API를 하나 만들어보겠습니다. 

```
git add .
git status
git commit -m "커밋 메세지"
git push origin master
```

위의 명령어를 통해서 Github push를 해보겠습니다. 그 후에 Travis CI를 들어간 후에 확인해보겠습니다. 

![스크린샷 2021-04-03 오후 8 53 39](https://user-images.githubusercontent.com/45676906/113477706-b7022400-94be-11eb-883e-8d26c3e8f2b6.png)

그러면 자동으로 위에서 작업했던 자동화 파이프라인 CI/CD가 작동할 것입니다. 

<br>

## `EC2 인스턴스`

![스크린샷 2021-04-03 오후 8 55 10](https://user-images.githubusercontent.com/45676906/113477734-f466b180-94be-11eb-9adb-2fd2b2c53132.png)

EC2 인스턴스를 보면 위와 같이 파일들이 잘 전달된 것도 확인할 수 있습니다. 

![스크린샷 2021-04-03 오후 8 56 27](https://user-images.githubusercontent.com/45676906/113477757-1e1fd880-94bf-11eb-9a86-0f1af618c648.png)

그리고 `sudo docker ps` 명령어를 통해서 현재 실행 중인 컨테이너를 확인하면 위와 같이 떠있는 것을 볼 수 있습니다. 

<img width="359" alt="스크린샷 2021-04-03 오후 8 58 17" src="https://user-images.githubusercontent.com/45676906/113477788-5de6c000-94bf-11eb-9b78-14553c0e1231.png">

<img width="362" alt="스크린샷 2021-04-03 오후 8 58 01" src="https://user-images.githubusercontent.com/45676906/113477793-6b03af00-94bf-11eb-8df6-afcf4fe36a66.png">

위와 같이 잘 뜨는 것을 확인할 수 있습니다.