# `AWS Auto-Scaling, CodeDeploy로 배포 자동화 하기`

CodeDeploy를 사용하기 위해서는 `역할`, `정책`, `사용자` 개념에 대해서 알아야 합니다. 아래의 글을 읽기 전에 [여기](https://devlog-wjdrbs96.tistory.com/302) 에서 간단하게 IAM 개념에 대해 학습하고 오시는 걸 추천합니다. 

<br>

## `역할 생성`

![스크린샷 2021-04-13 오후 3 23 04](https://user-images.githubusercontent.com/45676906/114506021-31276b00-9c6c-11eb-8344-e46ff86398b1.png)

![스크린샷 2021-04-13 오후 3 24 11](https://user-images.githubusercontent.com/45676906/114506139-574d0b00-9c6c-11eb-80ca-b535f48d0828.png)

![스크린샷 2021-04-13 오후 3 24 58](https://user-images.githubusercontent.com/45676906/114506221-721f7f80-9c6c-11eb-9650-923a2ac9dced.png)

위와 같이 기본으로 `[AWSCodeDeployRole]`이 존재하는 것을 볼 수 있습니다. 

![스크린샷 2021-04-13 오후 3 26 52](https://user-images.githubusercontent.com/45676906/114506418-b7dc4800-9c6c-11eb-85da-04f12d5b104c.png)

즉, 해당 역할은 CodeDeploy에 접근할 수 있도록 하기 위해서 만드는 것입니다. 

<br>

## `정책 생성`

![스크린샷 2021-04-13 오후 3 29 07](https://user-images.githubusercontent.com/45676906/114506740-2b7e5500-9c6d-11eb-9978-2c63e14a2014.png)

```json
{
    "Version": "2012-10-17",
    "Statement": [
        {
            "Action": [
                "s3:Get*",
                "s3:List*"
            ],
            "Effect": "Allow",
            "Resource": "*"
        }
    ]
}
```

![스크린샷 2021-04-13 오후 3 29 53](https://user-images.githubusercontent.com/45676906/114506798-40f37f00-9c6d-11eb-8a3e-d6a7d737a9b2.png)

S3에 접근할 수 있도록 정책을 하나 만들겠습니다. 기존에 존재하는 역할을 사용해도 되지만 본인이 원하는 정책으로 커스텀해서 역할을 만들고 싶을 때 위와 같이 할 수 있습니다.

<br>

## `역할 만들기`

![스크린샷 2021-04-13 오후 3 31 39](https://user-images.githubusercontent.com/45676906/114506932-697b7900-9c6d-11eb-8204-94488c5497ac.png)

![스크린샷 2021-04-13 오후 3 32 44](https://user-images.githubusercontent.com/45676906/114507057-8fa11900-9c6d-11eb-8c3f-dda85200a7ef.png)

사용자나 그룹들에 권한을 직접 적용할 수는 없고 권한들로 만든 정책을 적용해야 하기 때문에 방금 만든 정책을 추가하겠습니다. 

![스크린샷 2021-04-13 오후 3 33 52](https://user-images.githubusercontent.com/45676906/114507158-b2333200-9c6d-11eb-9a17-1aec68299a6b.png)

![스크린샷 2021-04-13 오후 3 35 42](https://user-images.githubusercontent.com/45676906/114507425-1229d880-9c6e-11eb-9dd7-e255dbb44ff0.png)

즉 해당 역할은 `Auto-Scaling`과 CodeDeploy로 배포하는 과정에서 통해 각 EC2가 S3 버켓에 파일들을 읽어올 수 있도록 부여하는 역할입니다. 

<br>

## `시작 템플릿 만들기`

![스크린샷 2021-04-13 오후 2 14 56](https://user-images.githubusercontent.com/45676906/114500275-a9892e80-9c62-11eb-8faf-6122819b20db.png)

![스크린샷 2021-04-13 오후 2 15 31](https://user-images.githubusercontent.com/45676906/114500383-e05f4480-9c62-11eb-983b-84794f814794.png)

![스크린샷 2021-04-13 오후 2 36 09](https://user-images.githubusercontent.com/45676906/114501823-ae9bad00-9c65-11eb-91c6-9f638fad2a7a.png)

![스크린샷 2021-04-13 오후 3 39 31](https://user-images.githubusercontent.com/45676906/114507757-8bc1c680-9c6e-11eb-866f-ae681881117e.png)

위와 같이 `시작 템플릿`에서 인스턴스 프로파일을 설정해주어야 템플릿으로 생성된 인스턴스들은 `[Auto-Scaling-Gyunny-Role]` 역할을 갖고 시작될 것입니다. (그래야 배포에 성공할 수 있기 때문에 꼭 잘 설정을 해주어야 합니다.)

즉 해당 역할은 EC2에 설치되어 있는 CodeAgent가 S3 버켓을 잘 읽어올 수 있도록 하기 위해서 설정하는 것입니다.

<br>

<br>

## `Auto Scaling Group 만들기`

![스크린샷 2021-04-13 오후 5 15 39](https://user-images.githubusercontent.com/45676906/114520051-fb8a7e00-9c7b-11eb-8e32-fb86c50dc7fd.png)

[Auto Scaling Group 만들기](https://devlog-wjdrbs96.tistory.com/301) 를 참고해서 만들고 오시면 됩니다. 

- 최소 인스턴스, 적정 인스턴스, 최대 인스턴스 모두 2로 만들었습니다.
- 기존 로드 밸런서에 연결을 했습니다. 

<br>

![스크린샷 2021-04-13 오후 5 19 54](https://user-images.githubusercontent.com/45676906/114520586-8ff4e080-9c7c-11eb-9741-8eb7393bd815.png)

그렇게 Auto Scaling Group을 만들고 설정할 때 희망 인스턴스 수를 2로 했다면 위와 같이 인스턴스 2개가 새로 만들어졌을 것입니다.

![스크린샷 2021-04-13 오후 5 22 02](https://user-images.githubusercontent.com/45676906/114520894-dc402080-9c7c-11eb-8bed-9f6ccfe4af6e.png)

그리고 `로드 밸런서 대상 그룹`에 가보면 위와 같이 정상적으로 로드밸런서에 연결이 되어 있는 것을 볼 수 있습니다. 

<br>

## `CodeDeploy 생성하기`

![스크린샷 2021-04-13 오후 5 23 56](https://user-images.githubusercontent.com/45676906/114521162-1dd0cb80-9c7d-11eb-9150-948d3aa1371e.png)

![스크린샷 2021-04-13 오후 5 25 55](https://user-images.githubusercontent.com/45676906/114521701-a7809900-9c7d-11eb-8b9f-5b972aac101a.png)

이번에 지정하는 역할은 어떤 것일까요? 저 해당 역할의 내용들을 보면 `Auto-Scaling`, `Load-Balancer` 등등에 관련된 설정이 되어 있는 정책이 있는 것을 볼 수 있습니다. 
즉, CodeDeploy에게 해당 역할을 부여해야 CodeDeploy를 통해 Load-Balancer에 연결된 Auto-Scaling 그룹에 배포를 할 수 있는 것입니다.

![스크린샷 2021-04-13 오후 5 29 00](https://user-images.githubusercontent.com/45676906/114521921-d26aed00-9c7d-11eb-9d64-42b106a63ba5.png)

![스크린샷 2021-04-13 오후 5 30 24](https://user-images.githubusercontent.com/45676906/114522096-fc241400-9c7d-11eb-8a5d-1e5187436732.png)

배포 설정의 종류는 3가지가 존재합니다. 각각의 특징은 아래와 같습니다. 

- `CodeDeployDefault.OneAtTime`: 한 번에 하나씩 배포 
- `CodeDeployDefault.HalfAtTime`: 절반씩 배포
- `CodeDeployDefault.AllAtOnce`: 한꺼번에 배포

<br>

여기서는 그냥 `CodeDeployDefault.AllAtOnce`로 진행하겠습니다. (나중에 좀 더 커스텀하게 해서 사용해보겠습니다.)

<br> <br>

## `Spring Boot로 자동화 배포 테스트`

![1](https://user-images.githubusercontent.com/45676906/114522539-6f2d8a80-9c7e-11eb-8754-f936c36fc014.png)

현재 진행하고자 하는 아키텍쳐는 위와 같습니다. 

- CI 도구는 Travis CI를 사용하였습니다. 
- CD는 CodeDeploy 를 사용하여 Auto-Scaling Group에 배포를 진행하고 있습니다. 

참고로 이번 글에서는 Travis CI에서 IAM 사옹자 엑세스 키 설정에 대해서는 다루지 않습니다.

<br>

![스크린샷 2021-04-13 오후 5 35 50](https://user-images.githubusercontent.com/45676906/114523019-e06d3d80-9c7e-11eb-9bd3-2520b781676b.png)

전체 프로젝트 구조는 위와 같습니다. 

- `travis.yml`: Travis CI가 어떻게 작동해야 하는지에 대해 작성합니다.
- `appspec.yml`: CodeDeploy가 배포할 때 각 과정마다 어떻게 동작해야 하는지에 대해 작성합니다. 
- `deploy.sh`: 배포가 된 후에 EC2 내부에서 작동해야 할 명령어들에 대해서 적습니다. 

각각 하나씩 어떤 것인지 알아보겠습니다. 

<br>

## `travis.yml`

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

# before_deploy 과정에서 S3에 파일을 저장하고 EC2에 배포할 것들에 대해서 작성합니다. 
before_deploy:
  - mkdir -p before-deploy # zip에 포함시킬 파일들을 담을 디렉토리 생성
  - cp scripts/*.sh before-deploy/
  - cp Dockerfile before-deploy/    # Dockerfile 디렉토리로 이동
  - cp appspec.yml before-deploy/   # appspec.yml  디렉토리로 이동
  - cp build/libs/*.jar before-deploy/  # jar 파일도 디렉토리로 이동
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
    bundle_type: zip                 # zip 형태라는 걸 명시
    application: Gyunny              # CodeDeploy 애플리케이션 이름

    deployment_group: Gyunny-Group   # CodeDeploy 배포 그룹 이름

    region: ap-northeast-2
    wait-until-deployed: true

notification:
  email:
    recipients:
      - wjdrbs966@naver.com    # 해당 메일로 알림이 옴
```


각각 어떤 뜻인지는 주석으로 적어놓았습니다. CodeDeploy 애플리케이션 이름, 애플리케이션 그룹, S3 버켓 이름들 잘 맞게 썼는지 확인을 해야 합니다. 
또한 `before_deploy` 과정에서 배포에 필요한 파일들을 zip으로 압축하기 때문에 어떤 파일을 옮겨야 하는지 잘 보아야 합니다.

<br>

## `appspec.yml`

```yaml
version: 0.0
os: linux         # 현재 EC2 Linux2를 사용하고 있습니다.
files:
  - source:  /   # 프로젝트 전체를 의미
    destination: /home/ec2-user/app/step2/zip/   # EC2 내부에 해당 위치에 파일을 옮기겠다는 뜻 (본인이 원하는 곳을 지정하면 됩니다.)
    overwrite: yes

permissions:
  - object: /
    pattern: "**"
    owner: ec2-user
    group: ec2-user

hooks:
  ApplicationStart:
    - location: deploy.sh  # ApplicationStart 에서 deploy.sh를 실행합니다. 
      timeout: 60
      runas: ec2-user
```

<br>

## `deploy.sh`

```shell script
echo "> 현재 실행 중인 Docker 컨테이너 pid 확인" >> /home/ec2-user/deploy.log
CURRENT_PID=$(sudo docker container ls -q)

if [ -z $CURRENT_PID ]
then
  echo "> 현재 구동중인 Docker 컨테이너가 없으므로 종료하지 않습니다." >> /home/ec2-user/deploy.log
else
  echo "> sudo docker stop $CURRENT_PID"
  sudo docker stop $CURRENT_PID
  sleep 5
fi

cd /home/ec2-user/app/step2/zip/     # 해당 디렉터리로 이동
sudo docker build -t gyunny ./       # 이미지 만들기
sudo docker run -d -p 8080:8080 gyunny  # Docker Container 생성
sudo docker run -d -p 8081:8080 gyunny  # Docker Container 생성
```

쉘 스크립트도 위에 적혀있는 거 같이 현재 실행 중인 것이 있다면 죽이고 새로 컨테이너를 띄우는 식으로 스크립트 파일을 작성하였습니다. (도커를 좀 더 깔끔하게 커스텀 할 수 있을 거 같긴 하지만..)

<br>

## `자동화 배포 시작해보기`

![스크린샷 2021-04-13 오후 5 44 47](https://user-images.githubusercontent.com/45676906/114524163-f16a7e80-9c7f-11eb-97a7-26557b0eb7b1.png)

위와 같이 간단한 Controller를 만든 후에 Github에 push를 해보겠습니다. 

![스크린샷 2021-04-13 오후 5 46 10](https://user-images.githubusercontent.com/45676906/114524433-2bd41b80-9c80-11eb-90ed-5a4594dc001b.png)

그러면 Travis CI에서 위와 같이 자동으로 해당 커밋에 대해서 CI를 수행합니다. (travis.yml에 적은 대로)

![스크린샷 2021-04-13 오후 5 47 21](https://user-images.githubusercontent.com/45676906/114524662-5e7e1400-9c80-11eb-98fb-15c17f64c7ca.png)

![스크린샷 2021-04-13 오후 5 48 02](https://user-images.githubusercontent.com/45676906/114524712-6938a900-9c80-11eb-9a6b-2d291000744c.png)

그리고 CodeDeploy에 보면 위와 같이 배포가 성공적으로 잘 된 것을 볼 수 있습니다. 이제 Auto Scaling으로 만들어졌던 EC2에 각각 접속해서 확인해보겠습니다.

<img width="1176" alt="스크린샷 2021-04-13 오후 5 49 20" src="https://user-images.githubusercontent.com/45676906/114525012-b157cb80-9c80-11eb-82f3-2ceb88d4bdce.png">

<img width="1163" alt="스크린샷 2021-04-13 오후 5 48 59" src="https://user-images.githubusercontent.com/45676906/114525085-c3d20500-9c80-11eb-93d2-8635930fe7ee.png">

그러면 위와 같이 EC2 각각에 Docker Container가 실행되고 있는 것도 확인할 수 있습니다. 

![스크린샷 2021-04-13 오후 5 53 37](https://user-images.githubusercontent.com/45676906/114525635-38a53f00-9c81-11eb-80bd-4a322a25974b.png)

![스크린샷 2021-04-13 오후 5 53 42](https://user-images.githubusercontent.com/45676906/114525668-3fcc4d00-9c81-11eb-92dc-c615ce4bd33f.png)

그리고 실제 EC2 주소로 접속했을 때 위와 같이 잘 뜨는 것을 볼 수 있습니다. 
