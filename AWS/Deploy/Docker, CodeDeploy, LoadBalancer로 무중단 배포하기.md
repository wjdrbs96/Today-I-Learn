# `Docker, CodeDeploy, Load-Balancer로 무중단 배포하기`

이번 글에서는 현재 진행하고 있는 [프로젝트](https://github.com/YAPP-18th/iOS1_Backend) 에서 `무중단 자동화 배포`를 하는 과정에 대해서 정리를 해보겠습니다. 
대신 EC2 생성(jar 배포) 등등 세세한 부분까지 다루지는 않고 큰 부분들만 다루어보겠습니다. 

![YAPP 아키텍쳐](https://user-images.githubusercontent.com/45676906/122323176-a6136b80-cf61-11eb-96ad-f94f34c2fd79.png)

프로젝트 전체의 아키텍쳐는 위와 같습니다. 이 중에서 `Docker`, `Load-Balancer`, `CodeDeploy`로 어떻게 무중단 배포를 하였는지에 대해서만 알아보겠습니다. (Jenkins 관련 설정을 다루지 않습니다.)
간단하게 프로젝트 배포 흐름을 정리하면 아래와 같습니다.

<br>

## `배포 순서 정리`

1. Github에 push를 한다. 
2. Jenkins가 Github Repository를 Clone 받는다.
3. Jenkins가 Build 후에 jar, dockerfile, script file, yml 파일 등등 zip 으로 압축 후에 S3로 전달
4. CodeDeploy가 S3에 업로드 된 파일로 EC2에 현재 위치 배포를 진행합니다.
5. 배포를 하는 도중에 서버가 중단되면 안되기 때문에 `Load-Balancer`와 연결해서 서버 한대씩 배포하도록 설계하였습니다. 
6. 즉, A 서버에 배포를 하면 B 서버가 트래픽을 받고, A 서버 배포가 되면 A 서버가 트래픽을 받고 B 서버에 배포를 진행합니다. 

<br>

이제 `CodeDeploy`, `Load-Balancer` 설정은 어떻게 했고, `Docker` 배포 Shell Script는 어떻게 작성했는지에 대해서 알아보겠습니다. 

<br>

## `로드 밸런서 설정`

![스크린샷 2021-04-14 오후 4 33 05](https://user-images.githubusercontent.com/45676906/114671510-24c01280-9d3f-11eb-9735-92ee684763c8.png)

`Application Load Balancer`를 선택하겠습니다. 

<br>

![스크린샷 2021-04-14 오후 4 34 35](https://user-images.githubusercontent.com/45676906/114671819-75d00680-9d3f-11eb-90e7-3be27da1e05a.png)

실제로는 HTTPS가 적용을 하였지만 이번 글에서는 HTTP로만 할 것이기 때문에 위와 같이 체크하겠습니다.

<br> 

![스크린샷 2021-04-14 오후 4 36 34](https://user-images.githubusercontent.com/45676906/114672122-bd569280-9d3f-11eb-86ae-4ee5517e9dc7.png)

위와 같이 가용 영역은 모두 체크를 하고 다음으로 넘어가겠습니다. 그리고 보안그룹은 80, 8080이 열려 있도록 만들겠습니다. (원하는 형태의 보안그룹을 만들어서 사용하면 됩니다.)

<br>

![스크린샷 2021-04-14 오후 4 39 58](https://user-images.githubusercontent.com/45676906/114672641-52598b80-9d40-11eb-8c50-86d97401ff2b.png)

- `대상그룹의 포트는 대상 그룹 내 인스턴스로 로드밸런싱을 할 때 해당 포트로 트래픽을 보낸다는 뜻입니다.`
- `상태 검사는 주기적으로 로드밸런스가 대상그룹 내 인스턴스들에게 요청을 보내 확인하는 경로입니다.`

<br>

![스크린샷 2021-04-14 오후 4 48 18](https://user-images.githubusercontent.com/45676906/114673537-43270d80-9d41-11eb-8956-845950e076d0.png)

- `정상 임계값`: 연속으로 몇 번 정상 응답을 해야만 정상 상태로 볼 것인지 지정하는 항목
- `비정상 임계값`: 연속으로 몇 번 비정상 응답을 해야만 정상 상태로 볼 것인지 지정하는 항목
- `제한 시간`: 타임아웃 시간으로 응답이 몇 초 이내로 오지 않을 경우 비정상 응답으로 판단할지 지정하는 항목
- `간격`: 몇 초 간격으로 인스턴스의 상태를 물어볼지 지정하는 항목

<br>

위와 같이 값을 작게 설정 해주어야 CodeDeploy 배포 과정인 AllowTraffic 구간에서 빠르게 진행될 수 있습니다. 이렇게 Load-Balancer도 설정을 다 마쳤습니다. 로드 밸런서 설정은 이게 거의 전부입니다. 매우 쉽쥬? 

<br>

그리고 위에서 `Health` 체크를 `/`에 `8080` 포트로 상태 검사를 해놓았는데요. 이 뜻은 Target-Group에 연결된 EC2 인스턴스의 {{base_url}}:8080 에는 올바른 응답이 와야 한다는 뜻입니다. 

<br>

![스크린샷 2021-06-17 오후 1 35 06](https://user-images.githubusercontent.com/45676906/122332138-e7f7de00-cf70-11eb-9bad-7802a2bd15f5.png)

위와 같이 Target-Group에 로드 밸런서에 연결하고자 하는 EC2 인스턴스를 등록하면, 로드 밸런서가 주기적으로 EC2 인스턴스에게 `Health Check`를 합니다. (해당 인스턴스에 문제가 있는지 없는지)
현재 제가 연결한 인스턴스는 잘 연결이 되어서 `Healthy` 한 상태인 것을 볼 수 있습니다. 이게 정말 로드 밸런서 설정의 끝입니다. 이제 CodeDeploy 설정을 해보겠습니다. (이것도 정말 간단합니다. )

<br>

<br>

## `CodeDeploy 설정`

![스크린샷 2021-06-17 오후 12 00 14](https://user-images.githubusercontent.com/45676906/122329363-4078ac80-cf6c-11eb-8074-7c0de1055ecf.png)

CodeDeploy 배포 그룹에서 `역할` 설정이 중요한데요. 

<br>

![스크린샷 2021-06-17 오후 1 03 49](https://user-images.githubusercontent.com/45676906/122329512-83d31b00-cf6c-11eb-9c11-bda91e3b03ef.png)

위의 정책이 들어간 역할을 CodeDeploy 배포 그룹에 연결하겠습니다. (아마 로드밸런서 정책은 없어도 될 거 같습니다.)

<br>

![스크린샷 2021-06-17 오후 1 07 08](https://user-images.githubusercontent.com/45676906/122329815-06f47100-cf6d-11eb-8dc7-502ad70c03c6.png)

배포는 `현재 위치` 방식을 사용할 것인데 현재 위치 배포 방식을 잘 모르겠다면 [여기](https://devlog-wjdrbs96.tistory.com/304?category=885022) 를 참고하시면 좋을 거 같습니다. 
그리고 API Server는 EC2 2대를 사용하고 있는데, EC2 태그(`Key-Value` 형태)를 태그 그룹에다 위와 같이 설정해주면 됩니다. 그러면 CodeDeploy가 태그를 찾아서 해당 EC2에 배포를 진행하게 됩니다. 

<br>

![스크린샷 2021-06-17 오후 1 13 33](https://user-images.githubusercontent.com/45676906/122330370-03adb500-cf6e-11eb-983a-03cadca41ea9.png)

그리고 배포는 `CodeDeploydafult.HalfAtTime`을 사용했는데 이것은 절반씩 배포하겠다는 뜻입니다. 즉, EC2 2대를 사용하니까 1대씩 배포를 진행하겠다는 뜻입니다. 

<br>

이렇게 모든 AWS 설정이 끝났는데요. 마지막으로 배포 스크립트에 대해서 알아보겠습니다. EC2에 CodeAgent를 설치해놓으면 CodeAgent를 통해서 S3에 저장되어 있는 파일이 EC2로 넘어오고, appspec.yml에 정의한 대로 `Shell Script` 파일이 실행되면서 배포가 진행되는 것인데요. 

```shell
#!/bin/bash
CONTAINER_ID=$(docker container ls -f "name=yapp" -q)

echo "> 컨테이너 ID는 무엇?? ${CONTAINER_ID}"

if [ -z ${CONTAINER_ID} ]
then
  echo "> 현재 구동중인 애플리케이션이 없으므로 종료하지 않습니다." >> /home/ec2-user/yapp/deploy.log
else
  echo "> docker stop ${CONTAINER_ID}"
  sudo docker stop ${CONTAINER_ID}
  echo "> docker rm ${CONTAINER_ID}"
  sudo docker rm ${CONTAINER_ID}
  sleep 5
fi

cd /home/ec2-user/yapp && docker build -t yapp .
docker run --name yapp -d -e active=prod -p 8080:8080 yapp
```

배포를 하는 `Shell Script` 파일은 위와 같습니다. 간단하게 요약하면 현재 실행하고 있는 컨테이너가 있다면 해당 컨테이너를 중지시키고 다시 이미지를 빌드해서 새로운 컨테이너를 띄우는 것입니다. 
(참고로 컨테이너를 실행할 때는 prod 환경으로 실행시켜주어야 합니다.)

<br>

### `Dockerfile`

```dockerfile
FROM openjdk:11-jre-slim

WORKDIR /root

COPY ./build/libs/*.jar .

CMD java -jar -Dspring.profiles.active=${active} *.jar
```

Dockerfile은 위와 같습니다. 


<br>


이제 실제로 Github에 push 했을 때 젠킨스가 빌드 되고, CodeDeploy가 잘 작동하는지를 테스트 해보겠습니다. 

<br>

![스크린샷 2021-06-17 오후 1 42 48](https://user-images.githubusercontent.com/45676906/122332751-ebd83000-cf71-11eb-8712-b7f2c727b673.png)

그러면 위와 같이 `Jenkins` 빌드도 계속 잘 성공하는 것도 볼 수 있습니다. 

<br>

![스크린샷 2021-06-17 오후 1 44 27](https://user-images.githubusercontent.com/45676906/122332912-41acd800-cf72-11eb-8e02-8f44181671e3.png)

EC2 한대의 CodeDeploy 배포가 진행될 때 단계들은 위와 같은데요. 문제 없이 배포가 잘 진행되는 것을 볼 수 있습니다. 

<br> 

![스크린샷 2021-06-17 오후 1 44 09](https://user-images.githubusercontent.com/45676906/122332916-41acd800-cf72-11eb-873b-91845446a97b.png)

그리고 EC2 인스턴스가 2개 이다 보니 위처럼 2개가 보이는데 인스턴스 하나 배포하는데 대략 3분 정도가 걸리고, 둘 다 문제 없이 잘 배포되는 것도 확인할 수 있습니다. 

<br>

![스크린샷 2021-06-17 오후 1 43 53](https://user-images.githubusercontent.com/45676906/122332907-3e195100-cf72-11eb-9a5a-8edff62b3a7e.png)

지금까지 CodeDeploy 배포를 하고 있는 것인데요. 큰 문제 없이 배포가 안정화가 되어 `무중단 자동화 배포`를 계속 진행하고 있습니다. 

<br>

## `글을 마무리 하며`

매번 자동화 배포 해야지 했는데 이번 기회에 적용할 수 있게 되었습니다. 기존에는 jar를 직접 빌드하고 FileZila를 통해서 EC2에 업로드 했는데 Github에 push 한번만 하면 모든게 자동으로 진행되니 너무 편리한 거 같습니다.
또한 배포할 때마다 서버가 중단되는 것이 아니라 `무중단`으로 진행하기 때문에 배포할 때마다 서비스가 중단되어야 하는 문제가 사라졌습니다. 

CI/CD는 꼭 한번쯤은 해볼만 한 주제여서 한번쯤은 해보면 좋을 거 같습니다. 

<br>


