# `Auto Scaling, CodeDeploy로 Blue/Green 자동화 배포하기`

[저번 글](https://devlog-wjdrbs96.tistory.com/303) 에서는 아래와 같은 아키텍쳐로 진행되었습니다.(저번 글을 한번 가볍게 보고 오는 것을 추천드립니다. AWS 서비스를 생성하는 과정에서 일부 생략되는 것이 있을 수 있습니다.) 

![1121](https://user-images.githubusercontent.com/45676906/114645130-c893c900-9d13-11eb-8da8-1a0b3f8498e8.png)

간단하게 `로드 밸런서`, `Auto-Scaling`에 해당하는 아키텍쳐만 보면 위와 같습니다. 즉, ELB에는 하나의 타켓 그룹(Auto-Scaling 그룹)만이 존재하는 상황입니다. 
그래서 이번 글에서는 [Blue/Green 배포 방식](https://devlog-wjdrbs96.tistory.com/300) 으로 타켓 그룹(Auto-Scaling 그룹)을 2개 만들어서 배포를 진행해보겠습니다. 

<br>

![1](https://user-images.githubusercontent.com/45676906/114645381-540d5a00-9d14-11eb-8ec5-c41d415c9781.png)

이번 글에서 진행할 아키텍쳐는 위와 같습니다. 즉, `Blue/Green 그룹으로 나눠서 무중단으로 자동 배포가 진행되게 할 것` 입니다. 저번 글에서 실습을 했다면 현재 `Auto Scaling 그룹`, `로드 밸런서 1개`, `대상그룹 1개`가 존재할 것입니다. 

<br> <br>

## `로드 밸런서 생성하기`

![스크린샷 2021-04-14 오후 4 33 05](https://user-images.githubusercontent.com/45676906/114671510-24c01280-9d3f-11eb-9735-92ee684763c8.png)

`Application Load Balancer`를 선택하겠습니다. 

<br>

![스크린샷 2021-04-14 오후 4 34 35](https://user-images.githubusercontent.com/45676906/114671819-75d00680-9d3f-11eb-90e7-3be27da1e05a.png)

현재 테스트에서는 HTTPS는 사용하지 않고 HTTP로만 할 것이기 때문에 위와 같이 체크하겠습니다. 

<br> 

![스크린샷 2021-04-14 오후 4 36 34](https://user-images.githubusercontent.com/45676906/114672122-bd569280-9d3f-11eb-86ae-4ee5517e9dc7.png)

위와 같이 가용 영역은 모두 체크를 하고 다음으로 넘어가겠습니다. 그리고 보안그룹은 80, 8080이 열려 있도록 만들겠습니다. (원하는 형태의 보안그룹을 만들어서 사용하면 됩니다.)

<br>

![스크린샷 2021-04-14 오후 4 39 58](https://user-images.githubusercontent.com/45676906/114672641-52598b80-9d40-11eb-8c50-86d97401ff2b.png)

- `대상그룹의 포트는 대상 그룹 내 인스턴스로 로드밸런싱을 할 때 해당 포트로 트래픽을 보낸다는 뜻입니다.`
- `상태 검사는 주기적으로 로드밸런스가 대상그룹 내 인스턴스들에게 요청을 보내 확인하는 경로입니다.`

<br> <br>

![스크린샷 2021-04-14 오후 4 48 18](https://user-images.githubusercontent.com/45676906/114673537-43270d80-9d41-11eb-8956-845950e076d0.png)

- `정상 임계값`: 연속으로 몇 번 정상 응답을 해야만 정상 상태로 볼 것인지 지정하는 항목
- `비정상 임계값`: 연속으로 몇 번 비정상 응답을 해야만 정상 상태로 볼 것인지 지정하는 항목
- `제한 시간`: 타임아웃 시간으로 응답이 몇 초 이내로 오지 않을 경우 비정상 응답으로 판단할지 지정하는 항목
- `간격`: 몇 초 간격으로 인스턴스의 상태를 물어볼지 지정하는 항목

<br>

위와 같이 값을 작게 설정 해주어야 CodeDeploy 배포 과정인 AllowTraffic 구간에서 빠르게 진행될 수 있습니다.
그리고 다음 대상 등록에서는 인스턴스를 지금 등록하지 않고 Auto-Scaling 그룹을 만들면서 자동으로 등록되게 할 것이니 일단 넘어가겠습니다.
                                      
<br> <br>

## `EC2 인스턴스 AMI 만들기`

이미지를 만들기 전에 아래에서 설명할 `사용자 데이터` 방식, `리눅스 내부 설정` 이렇게 방법은 2가지가 있는데 `사용자 데이터`를 사용할 것이라면 지금 이미지를 만들어도 됩니다. 하지만 `리눅스 내부 설정` 방법을 사용할 것이라면 설정을 다 한 후에 이미지를 만들어야 합니다.

<img width="502" alt="스크린샷 2021-04-14 오후 4 56 29" src="https://user-images.githubusercontent.com/45676906/114674674-71f1b380-9d42-11eb-9284-df6b26f01888.png">

이미지로 만들고자 하는 인스턴스를 선택하고 오른쪽 마우스 클릭 후에 위와 같이 `이미지 생성`을 하겠습니다. 

<br>

![스크린샷 2021-04-14 오후 5 01 33](https://user-images.githubusercontent.com/45676906/114675443-31df0080-9d43-11eb-9ac7-30e3138d4a07.png)

시간이 조금 걸리기는 하지만 위와 같이 AMI 이미지가 잘 만들어진 것을 볼 수 있습니다.

<br> <br>

## `EC2 시작 템플릿 만들기`

EC2 시작 템플릿에 설정된 것을 기반으로 Auto-Scaling이 EC2 인스턴스를 만들게 됩니다. 

<br>

![스크린샷 2021-04-14 오후 5 04 20](https://user-images.githubusercontent.com/45676906/114675872-b2056600-9d43-11eb-9484-0b643e08e379.png)

간단하게 이름을 정한 후에 아래에 위와 같이 설정을 하겠습니다. 

![스크린샷 2021-04-14 오후 5 06 40](https://user-images.githubusercontent.com/45676906/114676083-e2e59b00-9d43-11eb-887d-3e0c814296fa.png)

<br>

![스크린샷 2021-04-14 오후 5 08 03](https://user-images.githubusercontent.com/45676906/114676271-188a8400-9d44-11eb-9262-46bad00c6972.png)

그리고 위의 `인스턴스 프로파일` 설정은 중요합니다. 위와 같이 설정을 해주어야 Auto-Scaling을 통해 만들어진 인스턴스도 해당 역할을 가지고 만들어집니다. (자세한 내용은 [저번 글](https://devlog-wjdrbs96.tistory.com/303) 에서 확인하시면 됩니다.)

<br>

![스크린샷 2021-04-15 오후 1 50 26](https://user-images.githubusercontent.com/45676906/114815912-272f7480-9df2-11eb-960f-3676d9242922.png)

그리고 엄청 중요한!! 좀 더 아래에 보면 `사용자 데이터`가 있습니다. 이것은 EC2 인스턴스가 처음 생성될 때 실행될 명령어 입니다. 저는 AMI를 만들 때 원본 EC2에 Spring Boot jar가 실행 중이었기 때문에 Auto-Scaling으로 만들어진 EC2에도 jar를 실행시키기 위해서 위와 같이 명령어를 적었습니다. 

```
#!/bin/bash
cd /home/ec2-user/
nohup java -jar *.jar &
```

- `#!/bin/bash`: 주석이 아닙니다! 중요하니 꼭 적어주어야 합니다.
- `cd/home/ec2-user`: 현재 저는 EC2 Linux2 버전을 사용하고 있습니다.
- `nohup java -jar *.jar &`: jar 파일을 백그라운드로 실행시키는 명령어 입니다. 
- root 권한으로 실행되기 때문에 sudo를 적지 않아도 됩니다.

<br> <br>

## `사용자 데이터가 아닌 다른 방법`

사용자 데이터로 해도 인스턴스가 만들어질 때 해당 명령어가 실행이 됩니다. 이거보다 더 깔끔한 방법은 아래와 같습니다. `만약에 이 방법을 사용한다면 이미지는 아래의 설정들을 한 후에 만들어야 합니다!!` 

```
sudo vi /etc/rc.d/rc.local
```

<img width="595" alt="스크린샷 2021-04-19 오후 4 00 53" src="https://user-images.githubusercontent.com/45676906/115194513-86f68a00-a128-11eb-91f0-88d0d814e87b.png">


들어가면 위와 같이 뜰 것입니다. 여기는 EC2 인스턴스가 부팅될 때 실행되는 명령어들을 설정할 수 있습니다. 위의 영어를 읽어보면 알 수 있지만 다 적은 후에 마지막에 `chmod +x /etc/rc.d/rc.local`을 꼭 해주어야 한다고 나와있습니다.  

<br>

일단 여기서 jar를 실행시키는 명령어를 적겠습니다.  

![스크린샷 2021-04-19 오후 4 03 20](https://user-images.githubusercontent.com/45676906/115194799-e8b6f400-a128-11eb-8d69-6c3d2a82cbc8.png)

```
cd /home/ec2-user/
nohup java -jar *.jar &
```

그렇게 명령어를 입력한 후에 아래와 같이 `꼭!! 입력을 해주어야 합니다.` 이것 때문에도 삽질을 엄청 했습니다. ㅠㅠ

<br>

```
chmod +x /etc/rc.d/rc.local
```

![스크린샷 2021-04-19 오후 4 06 12](https://user-images.githubusercontent.com/45676906/115195237-798dcf80-a129-11eb-8947-9397f5d8272d.png)

위와 같이 초록색으로 바뀌었으면 권한 부여가 잘 된 것입니다. 

<br>

```
sudo reboot
```

위의 명령어를 통해서 EC2 인스턴스를 재부팅한 후에 jar 실행 명령어가 잘 실행되었는지 확인해보겠습니다. 

![스크린샷 2021-04-19 오후 4 10 49](https://user-images.githubusercontent.com/45676906/115195571-e012ed80-a129-11eb-883e-a9265d733f2d.png)

위와 같이 8080 포트도 잘 실행되고 있는 것을 볼 수 있습니다. 


<br>

<img width="655" alt="스크린샷 2021-04-15 오후 2 12 36" src="https://user-images.githubusercontent.com/45676906/114817307-b6d62280-9df4-11eb-9d79-f180927aca68.png">

그리고 위와 같이 AMI를 만들 때 사용한 원본 EC2에 java 11이 설치되어 있었기 때문에 `사용자 데이터`에는 Java 설치하는 명령어는 적지 않았습니다. 이렇게 원본 EC2를 세팅한 후에 AMI 이미지를 만들어야 `rc.local`에서 설정했던 명령어들이
작동하게 됩니다

<br>

<img width="509" alt="스크린샷 2021-04-15 오후 2 00 52" src="https://user-images.githubusercontent.com/45676906/114816430-19c6ba00-9df3-11eb-9ca9-c6ecd21bc53d.png">

AMI를 만들 때의 원본 EC2의 상태는 위와 같습니다.`사용자 데이터`에 jar 실행 명령어를 적거나 또는 위에서 본 rc.local 파일에 jar 실행 명령어를 반드시!! 입력해야 합니다! (이것때매 하루를 삽질했습니다..ㅠㅠ)


<br> <br>


## `Auto-Scaling 그룹 만들기`

![스크린샷 2021-04-14 오후 5 11 03](https://user-images.githubusercontent.com/45676906/114676673-7ae38480-9d44-11eb-806f-ae8e0a903927.png)

그 다음 뷰에서는 `시작 템플릿 준수`, `모든 서브넷 체크` 후에 다음으로 넘어가겠습니다. 

<br>

![스크린샷 2021-04-14 오후 5 12 59](https://user-images.githubusercontent.com/45676906/114677042-e0d00c00-9d44-11eb-994c-f9205a1f2efc.png)

위에서 만든 `로드밸런싱 타겟 그룹`을 지정하고 다음으로 넘어가겠습니다. 

<br> <br>

<img width="895" alt="스크린샷 2021-04-14 오후 5 15 02" src="https://user-images.githubusercontent.com/45676906/114677207-0bba6000-9d45-11eb-9d2a-4800ac04b4ae.png">

저는 인스턴스 2개로 유지할 것이기 때문에 용량을 모두 2로 설정하고 게속 다음을 누른 후에 Auto-Scaling-Group을 만들겠습니다. 

<br>

<img width="1075" alt="스크린샷 2021-04-14 오후 5 17 21" src="https://user-images.githubusercontent.com/45676906/114677559-63f16200-9d45-11eb-846b-554e6c98b4ef.png">

그러면 위에서 `적정 용량`, `최소 용량`, `최대 용량`을 모두 2로 지정했기 때문에 인스턴스가 2개가 시작 템플릿 설정에 맞춰서 자동으로 생성되고 있는 것을 볼 수 있습니다. 그리고 로드밸런서 타겟그룹에 가서 연결이 잘 되었는지 확인 해보겠습니다.

<br>

![스크린샷 2021-04-15 오후 2 07 45](https://user-images.githubusercontent.com/45676906/114816925-108a1d00-9df4-11eb-97f8-9fe860ffcc5a.png)

위와 같이 8080 포트가 `Healthy` 상태인 것도 확인할 수 있습니다. 이제 자동화 배포를 하기 위해서 CodeDeploy를 설정해보겠습니다.

<br> <br>

## `CodeDeploy 설정하기`

이번 글에서는 `Blue/Green` 방식으로 배포할 것이기 때문에 Blue/Green으로 설정하겠습니다. 

![스크린샷 2021-04-14 오후 2 54 14](https://user-images.githubusercontent.com/45676906/114661157-811c3580-9d31-11eb-9378-e8d61b582557.png)

CodeDeploy 애플리케이션을 만든 후에 CodeDeploy 애플리케이션 그룹을 만들겠습니다. 여기서 서비스 역할을 정하는데 이것은 [저번 글](https://devlog-wjdrbs96.tistory.com/303) 을 참고하시길 바랍니다.(역할 매우 중요합니다!! 설정을 제대로 하지 않으면 작동하지 않습니다 ㅠㅠ)
추가로 `블루/그린 배포` 방식은 위의 역할에다 정책을 하나 더 추가해주어야 합니다. 

<br>

![스크린샷 2021-04-15 오후 2 31 43](https://user-images.githubusercontent.com/45676906/114818812-601e1800-9df7-11eb-9acd-099646a6cdbe.png)

<br>

![스크린샷 2021-04-15 오후 2 33 42](https://user-images.githubusercontent.com/45676906/114818969-a2475980-9df7-11eb-915b-a8c5cb4568d1.png)


```json
{
    "Version": "2012-10-17",
    "Statement": [
        {
            "Sid": "VisualEditor0",
            "Effect": "Allow",
            "Action": [
                "iam:PassRole",
                "ec2:CreateTags",
                "ec2:RunInstances"
            ],
            "Resource": "*"
        }
    ]
}
```

위와 같이 입력한 후에 `정책`을 하나 만들겠습니다. 

![스크린샷 2021-04-15 오후 2 35 14](https://user-images.githubusercontent.com/45676906/114819093-dd498d00-9df7-11eb-864b-0276d6cd4b18.png)

<br>

![스크린샷 2021-04-15 오후 2 36 34](https://user-images.githubusercontent.com/45676906/114819205-108c1c00-9df8-11eb-88fc-d7d801ee9c0b.png)

위에서 만든 정책을 CodeDeploy에 부여할 역할에다가 위와 같이 추가하겠습니다. (CodeDeploy 배포 그룹 만들 때 넣은 서비스 역할에다가 위의 정책을 연결시켜야 합니다.)

<br>


![스크린샷 2021-04-14 오후 2 56 24](https://user-images.githubusercontent.com/45676906/114661280-b6c11e80-9d31-11eb-8916-09e27d02d5d3.png)

위에서 `인스턴스 수동 프로비저닝`을 선택하면 직접 새로운 Auto-Scaling 그룹을 만들어야 하지만 `그룹 자동 복사`를 선택하면 자동으로 Auto-Scaling 그룹(Green 그룹)이 생성되고 마지막에 Blue Group 삭제까지 자동으로 진행됩니다.

<br>

![스크린샷 2021-04-14 오후 2 59 54](https://user-images.githubusercontent.com/45676906/114661574-33ec9380-9d32-11eb-9340-3ce0b66b0395.png)

배포 성공한 후에 원본 인스턴스를 종료하는 시간을 테스트를 빨리 하기 위해서 5분으로 설정했습니다.(실제 환경에서는 30분 ~ 1시간 등등 상황에 맞게 설정하면 될 거 같습니다.) 즉, 좀 있다 아래에서 Blue Group의 인스턴스들은 5분이 지나면 자동으로 삭제될 것입니다. 
그리고 배포는 절반씩 진행하려고 `CodeDeployDefault.HalfAtTime`을 선택했습니다.

<br>

![스크린샷 2021-04-14 오후 3 02 37](https://user-images.githubusercontent.com/45676906/114661963-da389900-9d32-11eb-8061-514725b3a7f2.png)

`Blue/Green` 배포 방식이 동작하는 방식은 [Blue/Green 배포 방식이란?](https://devlog-wjdrbs96.tistory.com/300) 에서 자세히 확인할 수 있습니다. 이제 자동화 배포를 해보기 위해서 `Spring Boot`, `Travis CI`를 추가로 사용하겠습니다. (Travis CI 설정에 대해서는 다루지 않겠습니다.)

<br> <br>

## `Spring Boot, Travis CI 사용하기`

![1](https://user-images.githubusercontent.com/45676906/115197950-8102a800-a12c-11eb-8c3e-3b35cc907393.png)

다시 좀 더 정리하면 현재의 아키텍쳐는 위와 비슷합니다. `즉, Travis CI를 통해서 zip 파일을 S3를 전달하는 과정`이 필요합니다. 그래서 Travis CI를 통해서 zip 파일을 S3로 올리는 것을 먼저 해보겠습니다. 

<br>

### `travis.yml`

```yaml
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
  - travis_wait 30

before_deploy:
  - mkdir -p before-deploy # zip에 포함시킬 파일들을 담을 디렉토리 생성
  - cp scripts/*.sh before-deploy/     # *.sh 파일 디렉토리 안으로 복사
  - cp appspec.yml before-deploy/      # appsepc.yml 파일 디렉토리 안으로 복사
  - cp build/libs/*.jar before-deploy/ # jar 파일 디렉토리 안으로 복사
  - cd before-deploy && zip -r before-deploy * # before-deploy로 이동후 디렉토리 내부에 있는 파일들 전체 압축
  - cd ../ && mkdir -p deploy # 상위 디렉토리로 이동후 deploy 디렉토리 생성
  - mv before-deploy/before-deploy.zip deploy/SpringBoot_CI-CD.zip # before-deploy 내부 파일들을 압축한 zip을 deploy 내부에 SpringBoot_CI-CD.zip 이라는 이름으로 옮기기

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

notification:
  email:
    recipients:
      - wjdrbs966@naver.com
```

그리고 Travis CI와 Github Repository가 연결이 되어 있다면 스프링부트 프로젝트를 만든 것을 Github Repo에 push 하면 Travis CI가 작동할 것입니다.

<br>

![스크린샷 2021-04-19 오후 4 33 40](https://user-images.githubusercontent.com/45676906/115198501-156d0a80-a12d-11eb-86f4-a332d5d5affd.png)
 
그리고 위에서 지정한 S3 Bucket으로 들어가보면 위와 같이 zip이 잘 전달된 것을 확인할 수 있습니다. 
 
<br> <br>


그리고 이제 CodeDeploy를 연동해보겠습니다. CodeDeploy를 통해서 자동화 배포하기 위해서 필요한 파일은 `.travis.yml`, `deploy.sh`, `appspec.yml` 총 3개 입니다. 

![스크린샷 2021-04-19 오후 4 21 29](https://user-images.githubusercontent.com/45676906/115196886-5f54f100-a12b-11eb-9397-936c14403c38.png)

루트 경로 바로 아래에 위와 같이 3개의 파일을 만들겠습니다.

- `.travis.yml`: travis.yml에 정의된 대로 Travis CI는 동작하게 됩니다.
- `appspec.yml`: appspec.yml에 정의된 대로 CodeDeploy(CodeAgent)가 EC2에 배포를 한 후에 셸스크립트를 실행하도록 합니다.
- `deploy.sh`: EC2 내부에서 자동으로 명령어들이 실행될 수 있도록 셸 스크립트로 만든 파일입니다.

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
  - travis_wait 30

before_deploy:
  - mkdir -p before-deploy # zip에 포함시킬 파일들을 담을 디렉토리 생성
  - cp scripts/*.sh before-deploy/     # *.sh 파일 디렉토리 안으로 복사
  - cp appspec.yml before-deploy/      # appsepc.yml 파일 디렉토리 안으로 복사
  - cp build/libs/*.jar before-deploy/ # jar 파일 디렉토리 안으로 복사
  - cd before-deploy && zip -r before-deploy * # before-deploy로 이동후 디렉토리 내부에 있는 파일들 전체 압축
  - cd ../ && mkdir -p deploy # 상위 디렉토리로 이동후 deploy 디렉토리 생성
  - mv before-deploy/before-deploy.zip deploy/SpringBoot_CI-CD.zip # before-deploy 내부 파일들을 압축한 zip을 deploy 내부에 SpringBoot_CI-CD.zip 이라는 이름으로 옮기기

deploy:
  - provider: s3
    access_key_id: $AWS_ACCESS_KEY         # Travis CI 에서 IAM 사용자 엑세스 키

    secret_access_key: $AWS_SECRET_KEY     # Travis CI 에서 IAM 사용자 비밀 엑세스 키

    bucket: aws-gyun-s3        # S3 Bucket 이름
    region: ap-northeast-2
    skip_cleanup: true
    acl: private                 # 다른 사람이 다운 받지 못하도록 private
    local_dir: deploy            # S3로 올리고자 하는 디렉토리
    wait-until-deployed: true

  - provider: codedeploy
    access_key_id: $AWS_ACCESS_KEY        # Travis CI 에서 IAM 사용자 엑세스 키

    secret_access_key: $AWS_SECRET_KEY    # Travis CI 에서 IAM 사용자 비밀 엑세스 키

    bucket: aws-gyun-s3         # S3 Bucket 이름
    key: SpringBoot_CI-CD.zip   # Bucket 내부에 존재하는 zip 이름
    bundle_type: zip
    application: Test            # CodeDeploy 애플리케이션 이름

    deployment_group: Test-Group   # CodeDeploy 배포 그룹 이름

    region: ap-northeast-2
    wait-until-deployed: true

notification:
  email:
    recipients:
      - wjdrbs966@naver.com
```
 
Travis CI에서 IAM 엑세스 키, 비밀 엑세스 키를 등록하는 방법은 생략하였습니다. 위와 같이 .travis.yml 파일에 CodeDeploy 부분을 추가하겠습니다. (yml 내용 설명은 주석으로 해놓았습니다.)

<br> <br>

## `appspec.yml`

```yaml
version: 0.0
os: linux
files:
  - source:  /
    destination: /home/ec2-user/app/step3/
    overwrite: yes

permissions:
  - object: /
    pattern: "**"
    owner: ec2-user
    group: ec2-user

hooks:
  ApplicationStart:
    - location: deploy.sh
      timeout: 60
      runas: root     # 이것을 ec2-user 로 해놓아서 거의 일주일 동안 삽질을 했습니다... (해당 셸 스크립트 명령을 실행할 수 있는 권한이 있는지 잘 판단해야 합니다. ㅠㅠ)
```


<br>

그리고 Spring Boot Controller에서 아래와 같이 만들겠습니다. 그러면 `http://{{EC2-IP}}:8080` 으로 접속했을 때 아래의 String이 반환될 것입니다.  


![스크린샷 2021-04-19 오전 11 15 56](https://user-images.githubusercontent.com/45676906/115172622-9d88eb00-a100-11eb-8d75-7e7d84835b2b.png)

그리고 Github에 push를 해서 `Blue/Green 자동화 배포`가 잘 진행되는지 확인해보겠습니다.

<br>

![스크린샷 2021-04-19 오전 11 27 49](https://user-images.githubusercontent.com/45676906/115173470-569bf500-a102-11eb-86df-c6389cec3496.png)

시간이 조금 걸리기는 하지만 배포가 진행되다 보면 아래와 같이 Green Group의 Auto-Scaling Group이 만들어집니다.

<br>

![스크린샷 2021-04-19 오전 11 24 06](https://user-images.githubusercontent.com/45676906/115173314-015fe380-a102-11eb-8105-08aa7c526d97.png)

그리고 EC2 인스턴스도 확인해보면 위와 같이 Green Group의 인스턴스가 자동으로 만들어지는 것도 확인할 수 있습니다. 

<br>

![스크린샷 2021-04-19 오전 11 30 02](https://user-images.githubusercontent.com/45676906/115173663-bf836d00-a102-11eb-9467-f859d4df9a9d.png)

위와 같이 로드밸런서에도 Blue/Green 그룹의 인스턴스 4개가 모두 잘 연결된 것도 확인할 수 있습니다. 참고로 여기서 `시작 템플릿`에 설정해놓은 사용자 데이터를 통해서(또는 rc.local을 통해서) 인스턴스 생성될 때 적었던 명령어를 실행하게 됩니다. 근데 Blue Group의 인스턴스들은 문제없이 잘 되는데.. Green Group으로 만들어진 인스턴스들만 실행이 먹통이 되는 문제가 있었습니다.

![스크린샷 2021-04-19 오후 4 43 53](https://user-images.githubusercontent.com/45676906/115199822-8d880000-a12e-11eb-9e7c-6ed36c706cff.png)

왜 그럴까 하고 `일주일` 동안 삽질을 했는데... (AWS 문제인가 하고 계속 보고보고 질문 하고 했지만,,ㅠㅠ) 알고보니 Green Group으로 만든어지는 인스턴스들은 deploy.sh를 실행할 권한이 없었기 때문에 jar 업데이트가 제대로 되지 않았던 것입니다.. 

<img width="755" alt="스크린샷 2021-04-19 오전 11 48 51" src="https://user-images.githubusercontent.com/45676906/115175010-3f123b80-a105-11eb-95bd-079edd5ff843.png">

그래서 실제로 일주일 동안 삽질했던 것은 위와 같이 Green Group에서 만들어진 EC2 인스턴스에서 8080 포트가 떠있지만 아래와 같이 로드밸런서를 통해서 접속하면 500 에러가 반환됩니다.. ㅠ (대상 그룹에 잘 연결이 되어 있음에도 불구하고..) 

![1](https://user-images.githubusercontent.com/45676906/115203067-050b5e80-a132-11eb-9268-4687cda4daa8.png)

그래서 접속이 안되면 포트 kill 해준 후에 jar 재실행 하고 수동으로 하면.. 그러면 또 접속이 되었습니다.

흑흑 .. 저 때는 왜 그런지 도통 이유를 몰라서 일주일 동안 삽질 했는데.. 알고 보니 appspec.yml의 실행 권한을 root로 바꾸면 됐던 것 !! (해결해서 속 시원하지만 너무 많은 삽질 때문에 머리가 아프네요..)

<br>

![스크린샷 2021-04-19 오전 11 31 34](https://user-images.githubusercontent.com/45676906/115173750-ea6dc100-a102-11eb-99bf-2d51dee98286.png)

하여간,, 다시 본론으로 돌아와서 시간이 지나면 로드밸런서 타겟그룹에도 자동으로 Blue Group 인스턴스들은 삭제가 되는 것을 볼 수 있습니다. 
 
![스크린샷 2021-04-19 오전 11 31 51](https://user-images.githubusercontent.com/45676906/115173793-fc4f6400-a102-11eb-9b72-f7f0b93a8496.png)

그리고 `Green Group`으로 만들어진 Auto-Scaling 그룹을 들어가면 위와 같이 현재 연결된 Target-Group이 없는 것을 볼 수 있습니다. (배포가 진행 중이기 때문에 아직 연결이 안된 것입니다.)

![스크린샷 2021-04-19 오전 11 36 01](https://user-images.githubusercontent.com/45676906/115174063-826baa80-a103-11eb-9664-b3dd4baf10d6.png)

위와 같이 3단계가 끝난 후에 위에서 보았던 Green Group의 Auto-Scaling 그룹의 대상그룹을 다시 확인 해보겠습니다. 

![스크린샷 2021-04-19 오전 11 35 56](https://user-images.githubusercontent.com/45676906/115174044-7a136f80-a103-11eb-8ae1-e7026bca0443.png)

그러면 위와 같이 Green Group의 Auto-Scaling 그룹에 로드 밸런서의 대상그룹이 자동으로 연결된 것을 볼 수 있습니다.

![스크린샷 2021-04-19 오전 11 39 42](https://user-images.githubusercontent.com/45676906/115174315-002fb600-a104-11eb-98d7-b3ff684e00b6.png)

위에서 CodeDeploy 설정을 할 때 기존 인스턴스를 5분 정도 기다렸다가 지우겠다고 설정을 했기 때문에 이제 Blue Group의 Auto-Scaling 그룹과 그 안에 속해 있는 인스턴들이 5분이 지나면 삭제될 것입니다. 

![스크린샷 2021-04-19 오전 11 40 56](https://user-images.githubusercontent.com/45676906/115174406-281f1980-a104-11eb-8add-29a8cc0f80bc.png)

그리고 5분이 지나면 위와 같이 Blue Group의 Auto-Scaling Group이 자동으로 삭제되고 있는 것을 볼 수 있습니다.

![스크린샷 2021-04-19 오전 11 42 17](https://user-images.githubusercontent.com/45676906/115174563-6f0d0f00-a104-11eb-9e76-eb55741969be.png)

또한 Blue Group의 EC2 인스턴스들도 마찬가지로 자동으로 종료되는 것을 볼 수 있습니다.

<br>

![스크린샷 2021-04-19 오전 11 44 19](https://user-images.githubusercontent.com/45676906/115174682-a67bbb80-a104-11eb-987f-2a3725b65177.png)

그리고 로드 밸런서 DNS로 접속해보면 새로운 배포 버전의 인스턴스인 Green Group으로 접속이 잘 되는 것을 볼 수 있습니다. 

![스크린샷 2021-04-19 오전 11 45 30](https://user-images.githubusercontent.com/45676906/115174725-beebd600-a104-11eb-86b0-e94ce96973fc.png)

이렇게 최종적으로 배포에 성공한 것을 볼 수 있습니다. 


이번 실습은 진짜 엄청나게 일주일 동안 삽질하면서 겨우겨우 했습니다.. 정말 일주일 동안 삽질해서 겨우겨우 해결하고 이 글을 작성하는 거 같습니다 ㅠㅠ 이번 실습은 이정도에서 마치겠습니다.
  