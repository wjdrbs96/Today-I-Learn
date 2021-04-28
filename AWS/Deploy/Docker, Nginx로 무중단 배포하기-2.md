# `Nginx, Docker를 사용하여 무중단 배포하기 - 2`

[Nginx, Docker를 사용하여 무중단 배포하기 - 1]() 에서 간단한 초기 설정들에 대해서 알아보았습니다. 이번에는 실제로 Docker, Nginx를 설정하고 shell script 파일을 작성하면서 배포를 진행해보겠습니다. 

꼭!! [Nginx 무중단 배포](https://devlog-wjdrbs96.tistory.com/309) 의 글과 많이 관련이 되어 있으니 같이 참고하시는 것을 추천드립니다.

![스크린샷 2021-04-27 오후 9 25 17](https://user-images.githubusercontent.com/45676906/116240690-13472380-a79f-11eb-92e5-af383f1693b3.png) 

<br> <br>

## `EC2 CodeAgent 설치하기`

```
sudo yum install -y aws-cli
cd /home/ec2-user/ 
sudo aws configure 
wget https://aws-codedeploy-ap-northeast-2.s3.amazonaws.com/latest/install
chmod +x ./install
sudo ./install auto
sudo service codedeploy-agent status
```

위의 명령어를 EC2 Linux2 에서 입력하면 `CodeAgent`가 설치됩니다.(자세한 내용은 [여기](https://github.com/wjdrbs96/Today-I-Learn/blob/master/AWS/Deploy/Jenkins%2C%20CodeDeploy%EB%A1%9C%20CI%2C%20CD%20%ED%95%98%EA%B8%B0.md#ec2-linux2-codeagent-%EC%84%A4%EC%B9%98%ED%95%98%EA%B8%B0) 에서 확인하실 수 있습니다.)

<br>

```
sudo service codedeploy-agent status
```

![test](https://user-images.githubusercontent.com/45676906/111948931-325cf080-8b23-11eb-8c54-d31a7a89b880.png)

위의 명령어를 통해서 `CodeAgent` 상태를 확인할 수 있습니다. 위와 같이 뜬다면 잘 설치가 된 것입니다.

<br>

## `Docker, Nginx 세팅`

EC2에 `Docker`, `Docker-Compose`, `Nginx`를 설치해야 하는데 그 내용은 [여기](https://devlog-wjdrbs96.tistory.com/313) 에서 보고 오시면 됩니다.(다른 글들과 중복되는 내용이 많아서 일부 생략하면서 진행하겠습니다.)

위의 설치 및 Nginx를 세팅하는 것은 위의 글에서 다루었기 때문에 꼭 먼저 보고 오셔야 아래의 글을 이해할 수 있습니다. 지금부터 진행할 것들이 `Docker`, `Nginx` 무중단 배포의 중요한 내용들입니다.

<br>

## `Dockerfile 작성`

```dockerfile
FROM openjdk:11-jre-slim

WORKDIR /root

COPY ./demo-0.0.1-SNAPSHOT.jar .

CMD java -jar -Dspring.profiles.active=${active} demo-0.0.1-SNAPSHOT.jar
```

Dockerfile은 저번 글에서 작성했던 것과 거의 똑같지만 CMD 부분이 다른 것을 볼 수 있습니다. `-Dspring.profiles.active=${active}`라는 것이 추가되어 있습니다. 이렇게 쓰게되면 Dockerfile을 실행할 때 `active`라는 환경변수 값을 읽어와서 값을 넣어주게 됩니다. 

즉, active=real1 이라면 `java -jar -Dspring.profiles.active=real1 demo-0.0.1-SNAPSHOT.jar`으로 명령어가 실행되는 것입니다. 

<br>

## `docker-compose.yml 작성`

```yaml
version: "3"
services:
  web:
    image: nginx  # 존재하는 nginx 이미지 사용
    container_name: nginx   # Nginx Container 이름 지정
    ports:
      - 80:80 
    volumes:
      - /etc/nginx/:/etc/nginx/      # EC2 Nginx와 Docker Nginx Container 를 매핑
  spring1:
    build: .   # Dockerfile 실행
    image: spring   # 내가 만든 이미지 이름을 지정
    container_name: real1  # 컨테이너 이름 지정
    ports:
      - 8081:8081   
    volumes:
      - ./:/root/    # 요것은.. 필요 없을 수도 있는데 그 이유는 아래에서..
    environment:
      active: real1  # Dockerfile 실행될 때 환경변수를 사용할 수 있게 지정
  spring2:  
    build: .   # Dockerfile 실행
    image: spring
    container_name: real2
    ports:
      - 8082:8082
    volumes:   
      - ./:/root/
    environment:
      active: real2  # Dockerfile 실행될 때 환경변수를 사용할 수 있게 지정
```

위와 같이 `docker-compose.yml` 파일을 작성하겠습니다. 파일 내용의 의미는 주석으로 간단하게 적어놓은 것을 참고하시면 됩니다. 
여기서 중요하게 볼 점은 각 이미지마다 `컨테이너의 이름`을 지어주었는데 그 이유는 나중에 컨테이너 이름을 가지고 `컨테이너 중지(stop)`, `컨테이너 삭제(rm)`을 할 때 사용할 것이기 때문에 중요합니다.

그리고 위에서 `volumes`를 지정하였습니다. 특히 Nginx 컨테이너의 디렉터리와 EC2 Nginx 디렉터리를 매핑 시켜주어야 EC2 Nginx 파일이 변경되었을 때 Nginx Container 파일도 바뀔 수 있기 때문에 `꼭! 적어주어야 합니다.` 

그리고 `jar` 파일과 두개의 파일을 EC2에다 `vim`을 이용해서 직접 만들어주겠습니다.(초기 세팅을 위해서..)

![스크린샷 2021-04-28 오전 9 15 40](https://user-images.githubusercontent.com/45676906/116327722-61d9d980-a802-11eb-95fa-fa7ba2784256.png)

- `1편에서 만들었던 프로젝트의 jar 파일을 EC2에 올리면 됩니다.(저는 FileZila를 사용해서 올렸습니다.)`
- `/home/ec2-user/app/step4`의 경로에다 만들었습니다. 
- jar, dockerfile, docker-compose.yml은 항상 같은 위치에 존재해야 합니다. 

<br>

<img width="934" alt="스크린샷 2021-04-28 오전 9 28 53" src="https://user-images.githubusercontent.com/45676906/116328589-5091cc80-a804-11eb-9f0c-eb514457acf4.png">

```
docker-compose up -d 
```

위의 명령어를 통해서 docker-compose를 실행시키면 위와 같이 `spring Container 2대`와 `Nginx Container 1대`가 성공적으로 실행되는 것을 볼 수 있습니다. 

<br>

```
sudo vim /etc/nginx/conf.d/service-url.inc
```

<img width="343" alt="스크린샷 2021-04-28 오전 9 31 53" src="https://user-images.githubusercontent.com/45676906/116328707-92227780-a804-11eb-885e-7a20ab773e2c.png">

[여기](https://devlog-wjdrbs96.tistory.com/309) 에서 Nginx 설정을 다 했다면 위와 같이 경로가 설정되어 있을 것입니다.(현재 Nginx가 바라보는 포트는 8081이라는 뜻입니다.)

![nginx](https://user-images.githubusercontent.com/45676906/115637417-0fed0b80-a34b-11eb-92ab-c5c4303ec2a7.png) 

<br>

![스크린샷 2021-04-28 오전 9 34 05](https://user-images.githubusercontent.com/45676906/116328874-f04f5a80-a804-11eb-98f7-d415624e8df0.png)

그러면 위와 같이 `EC2 IP 80번 포트`로 접속했을 때 `real1`이 잘 반환되는 것을 볼 수 있습니다. 이제 무중단 배포의 아주 중요한.. 배포 스크립트를 만들어야 합니다. 이 부분에서 상당히 많이 삽질을 했는데요..
그 부분에 대해서 한번 정리를 해보겠습니다. 

<br>


## `배포 스크립트 만들기`

- `stop.sh`: 기존 엔직엔스에 연결되어 있지 않지만, 실행 중이던 스프링 부트 종료
- `start.sh`: 배포할 신규 버전 스프링 부트 프로젝트를 stop.sh로 종료한 `profile` 실행
- `health.sh`: start.sh로 실행시킨 프로젝트가 정상적으로 실행됐는지 체크
- `switch.sh`: 엔직엑스가 바라보는 스프링 부트를 최신 버전으로 변경
- `profile.sh`: 앞선 4개 스크립트 파일에서 공용으로 사용할 `profile`과 포트 체크 로직

만들 스크립트 파일은 총 5개 입니다. [저번 글](https://devlog-wjdrbs96.tistory.com/309) 에서 작성한 스크립트 파일과 크게 다르지는 않지만 이번에는 Docker를 기반으로 할 것이기 때문에 Docker 명령어를 적어주어야 합니다. 

<br>

이번 글에서 사용할 셸 스크립트의 문법은 [여기](https://devlog-wjdrbs96.tistory.com/308) 에서도 정리를 했으니 혹시.. 잘 모르겠다면 같이 보시는 것을 추천드립니다. 이 글에서는 Docker 명령어 위주로 정리할 것입니다.

<br>

## `stop.sh`

```shell script
#!/usr/bin/env bash

ABSPATH=$(readlink -f $0)
ABSDIR=$(dirname $ABSPATH)
source ${ABSDIR}/profile.sh

IDLE_PROFILE=$(find_idle_profile)

CONTAINER_ID=$(docker container ls -f "name=${IDLE_PROFILE}" -q)

echo "> 컨테이너 ID는 무엇?? ${CONTAINER_ID}"
echo "> 현재 프로필은 무엇?? ${IDLE_PROFILE}"

if [ -z ${CONTAINER_ID} ]
then
  echo "> 현재 구동중인 애플리케이션이 없으므로 종료하지 않습니다."
else
  echo "> docker stop ${IDLE_PROFILE}"
  sudo docker stop ${IDLE_PROFILE}
  echo "> docker rm ${IDLE_PROFILE}"
  sudo docker rm ${IDLE_PROFILE}    # 컨테이너 이름을 지정해서 사용하기 때문에.. 꼭 컨테이너 삭제도 같이 해주셔야 합니다. (나중에 다시 띄울거기 때문에..)
  sleep 5
fi
```

위의 파일에서도 상당히.. 많은 삽질을 하며 에러를 겪어서 쉽지 않았던 것이 기억납니다. 일단 찾고 싶은 컨테이너의 ID를 어떻게 찾을 수 있을까? 라는 고민을 많이하였습니다. 

```
docker container ls -f "name=${IDLE_PROFILE}" -q
ex) docker container ls -f "name=real1" -q
```

다행히 위와 같이 `컨테이너 이름으로 Container ID`를 찾을 수 있다는 것을 알게되어 위와 같이 사용하였습니다.

<br>

![스크린샷 2021-04-28 오전 9 43 10](https://user-images.githubusercontent.com/45676906/116329435-4ec90880-a806-11eb-8db6-65047c90c2eb.png)

즉, `stop.sh`는 현재 Nginx가 바라보고 있지 않은 컨테이너가 실행 중일 때 그 컨테이너를 중지, 삭제하는 역할을 합니다. (Nginx가 바라보는 곳은 한 곳이지만 현재 실행 중인 포트는 8081, 8082 둘다 이기 때문입니다.)

<br>

<img width="754" alt="1" src="https://user-images.githubusercontent.com/45676906/116329644-c72fc980-a806-11eb-9b6e-f92490e0aea3.png">

<br>

## `start.sh`

```shell script
#!/usr/bin/env bash

ABSPATH=$(readlink -f $0)
ABSDIR=$(dirname $ABSPATH)
source ${ABSDIR}/profile.sh

IDLE_PORT=$(find_idle_port)
REPOSITORY=/home/ec2-user/app/step4

echo "> Build 파일 복사"
echo "> cp $REPOSITORY/*.jar $REPOSITORY/"

cp $REPOSITORY/zip/*.jar $REPOSITORY      # 새로운 jar file 계속 덮어쓰기

echo "> 새 어플리케이션 배포"
JAR_NAME=$(ls -tr $REPOSITORY/*.jar | tail -n 1)

echo "> JAR Name: $JAR_NAME"

echo "> $JAR_NAME 에 실행권한 추가"

chmod +x $JAR_NAME

echo "> $JAR_NAME 실행"

IDLE_PROFILE=$(find_idle_profile)

echo "> $JAR_NAME 를 profile=$IDLE_PROFILE 로 실행합니다."

cd $REPOSITORY

docker build -t spring ./  
docker run -it --name "$IDLE_PROFILE" -d -e active=$IDLE_PROFILE -p $IDLE_PORT:$IDLE_PORT spring
```

`start.sh`에서 중요하게 볼 부분은 아래 Docker 명령어 두 줄입니다. 

- 먼저 이미지를 재 빌드하고 있는데.. 이러면 docker-compose 에서 Spring Container에 volume을 정했던 의미가 사라집니다.(왜냐하면 stop.sh에서 Docker 컨테이너를 중지-삭제까지 하기 때문에.. 재빌드해야 하는..) 분명 더 좋은 방법은 있겠지만 현재는 이렇게 진행하였습니다.
- 그리고 Docker run은 --name 옵션을 통해서 컨테이너 이름을 지정해주었습니다.(이것으로 stop.sh에서 사용하기 때문에 필수!!)
- 또한 -e 옵션과 함 께 `Dockerfile`에서 사용할 환경변수를 위와 같이 지정할 수 있습니다. 즉, 현재 Nginx가 바라보고 있지 않은 포트가 새로운 버전으로 재 배포가 되는 것입니다.

<br>

<img width="754" alt="1" src="https://user-images.githubusercontent.com/45676906/116330517-acf6eb00-a808-11eb-8b87-be509aef870f.png">

<br>

## `profile.sh`

```shell script
#!/usr/bin/env bash

function find_idle_profile()
{
    RESPONSE_CODE=$(sudo curl -s -o /dev/null -w "%{http_code}" http://3.36.209.141/)

    if [ ${RESPONSE_CODE} -ge 400 ] # 400 보다 크면 (즉, 40x/50x 에러 모두 포함)
    then
        CURRENT_PROFILE=real2
    else
        CURRENT_PROFILE=$(sudo curl -s http://3.36.209.141/)
    fi

    if [ ${CURRENT_PROFILE} == real1 ]
    then
      IDLE_PROFILE=real2
    else
      IDLE_PROFILE=real1
    fi

    echo "${IDLE_PROFILE}"
}
# 쉬고 있는 profile의 port 찾기
function find_idle_port()
{
    IDLE_PROFILE=$(find_idle_profile)

    if [ ${IDLE_PROFILE} == real1 ]
    then
      echo "8081"
    else
      echo "8082"
    fi
}
```

`profile.sh` 파일은 [저번 글](https://devlog-wjdrbs96.tistory.com/309) 의 내용과 동일합니다. 즉, 현재 Nginx가 바라보고 있지 않은 것이 `real`인지? `real2`인지를 확인하는 셸 스크립트입니다.

<br>

## `health.sh`

```shell script
#!/usr/bin/env bash

ABSPATH=$(readlink -f $0)
ABSDIR=$(dirname $ABSPATH)
source ${ABSDIR}/profile.sh
source ${ABSDIR}/switch.sh

IDLE_PORT=$(find_idle_port)

echo "> Health Check Start!"
echo "> IDLE_PORT: $IDLE_PORT"
echo "> curl -s http://3.36.209.141:$IDLE_PORT/"
sleep 10

for RETRY_COUNT in {1..10}
do
  RESPONSE=$(curl -s http://3.36.209.141:${IDLE_PORT})
  UP_COUNT=$(echo ${RESPONSE} | grep 'real' | wc -l)

  if [ ${UP_COUNT} -ge 1 ]
  then # $up_count >= 1 ("real" 문자열이 있는지 검증)
      echo "> Health check 성공"
      switch_proxy
      break
  else
      echo "> Health check의 응답을 알 수 없거나 혹은 실행 상태가 아닙니다."
      echo "> Health check: ${RESPONSE}"
  fi

  if [ ${RETRY_COUNT} -eq 10 ]
  then
    echo "> Health check 실패. "
    echo "> 엔진엑스에 연결하지 않고 배포를 종료합니다."
    exit 1
  fi

  echo "> Health check 연결 실패. 재시도..."
  sleep 10
done
```

`health.sh`도 저번 글과 동일합니다. 여기서는 `start.sh`가 실행했던 Docker Spring Container가 잘 실행되었는지 체크하는 스크립트입니다. 잘 실행되기 않았다면 여기서 Error가 발생하게 됩니다.

<br>

## `switch.sh`

```shell script
#!/usr/bin/env bash

ABSPATH=$(readlink -f $0)
ABSDIR=$(dirname $ABSPATH)
source ${ABSDIR}/profile.sh

function switch_proxy() {
    IDLE_PORT=$(find_idle_port)

    echo "> 전환할 Port: $IDLE_PORT"
    echo "> Port 전환"
    echo "set \$service_url http://3.36.209.141:${IDLE_PORT};" | sudo tee /etc/nginx/conf.d/service-url.inc

    sudo docker exec -d nginx nginx -s reload
    echo "> docker exec -it nginx nginx -s reload"
}
```

여기서 정말 많은 삽질을 했습니다.. 원인은 바로 `Docker Container Nginx reload` 하는 것 때문인데요. 

```shell script
sudo docker exec -it nginx nginx -s reload
```

처음에는 위와 같이 적었습니다. 명령어 자체에 문제는 없기 때문에 배포는 성공을 하지만 계속 `Docker Nginx Container`가 reload가 안되는 문제가 있었습니다. 이것 때문에 1~2일을 삽질 했던 거 같습니다 ㅠ,ㅠ

셸 스크립트 통해서는 reload가 안되지만 직접 EC2에서 입력하면 reload가 되었습니다. 그래서 `왜 안되지?`라는 생각만 하다 보니.. 더 이유를 알기가 힘든 문제 였던 거 같습니다.

<br>

```
sudo docker exec -d nginx nginx -s reload
```

그런데 이번에는 `-it` 옵션이 아닌 `-d` 옵션을 사용해보았습니다. 그러니 바로!! Nginx가 reload가 되었습니다.. ! (유레카.. 같은 순간이었습니다.) 이렇게 많은 삽질 끝에 스크립트 파일을 작성할 수 있었고.. 마지막으로 `appspec.yml`을 작성하고 자동배포를 진행해보겠습니다.

![reload](https://user-images.githubusercontent.com/45676906/115637611-87bb3600-a34b-11eb-9192-134417ce5736.png)

즉 switch.sh로 위와 같이 Nginx가 바라 보는 방향이 `reload` 되게 됩니다. 

<br>

<br>

## `appspec.yml`

```yaml
version: 0.0
os: linux
files:
  - source:  /
    destination: /home/ec2-user/app/step4/zip/
    overwrite: yes

permissions:
  - object: /
    pattern: "**"
    owner: ec2-user
    group: ec2-user

hooks:
  AfterInstall:
    - location: stop.sh
      timeout: 60
      runas: root

  ApplicationStart:
    - location: start.sh
      timeout: 60
      runas: root

  ValidateService:
    - location: health.sh
      timeout: 60
      runas: root
```

이것 또한 [저번 글](https://devlog-wjdrbs96.tistory.com/309) 과 똑같습니다. 다만 destination을 잘 설정해주어야 합니다.

![스크린샷 2021-04-28 오전 10 14 07](https://user-images.githubusercontent.com/45676906/116331328-81750000-a80a-11eb-8392-0d569108b7d9.png)

저는 위와 같이 `/home/ec2-user/app/step4/zip`으로 파일들을 옮기기 위해서 경로를 지정하였습니다.

<br> <br>

## `무중단 배포 진행해보기`

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

    @GetMapping("/hello")
    public String hello() {
        return "무중단 배포 !!";
    }
}
```
    
새로 만든 API도 잘 반영이 되는지 까지 확인을 하기 위해서 Contoller를 수정한 후에 Gituhb에 push를 하겠습니다.

<br>

<img width="994" alt="스크린샷 2021-04-28 오전 10 18 23" src="https://user-images.githubusercontent.com/45676906/116331593-137d0880-a80b-11eb-8550-dc79950f6580.png">

그러면 위와 같이 Gituhb이 Travis CI로 Hook을 날리기 때문에 자동으로 동작을 하게 됩니다.

<br>

<img width="998" alt="스크린샷 2021-04-28 오전 10 20 00" src="https://user-images.githubusercontent.com/45676906/116331795-71a9eb80-a80b-11eb-8c74-ff3605066b2b.png">
    
그리고 배포가 진행이 되면 `stop.sh`가 실행될 때 위와 같이 `real2` 컨테이너(Nginx가 바라보고 있지 않은)가 중지 및 삭제가 된 것을 볼 수 있습니다.

<br>

![스크린샷 2021-04-28 오전 10 20 19](https://user-images.githubusercontent.com/45676906/116331908-b170d300-a80b-11eb-8889-0da01f4ebf77.png)

그리고 다시 `start.sh`가 실행될 때 위와 같이 `real2` 컨테이너가 새로운 버전의 jar를 담고 실행되는 것을 볼 수 있습니다.

```
sudo vim /etc/nginx/conf.d/service-url.inc
```

<br>

<img width="343" alt="스크린샷 2021-04-28 오전 10 24 17" src="https://user-images.githubusercontent.com/45676906/116332006-e41acb80-a80b-11eb-836d-fdb216c03ef1.png">

그리고 위와 같이 Nginx가 바라보는 포트도 `8081 -> 8082`로 바뀐 것을 볼 수 있습니다.

<br>

<img width="330" alt="스크린샷 2021-04-28 오전 10 25 17" src="https://user-images.githubusercontent.com/45676906/116332128-1debd200-a80c-11eb-911e-2f624caaa3f0.png">

<br>

<img width="352" alt="스크린샷 2021-04-28 오전 10 25 25" src="https://user-images.githubusercontent.com/45676906/116332152-29d79400-a80c-11eb-9933-8786925d176c.png">
  
위와 같이 Nginx reload, Controller에서 만든 API 모두가 잘 반영이 되었고, 중단 없이 배포까지 되었습니다! 정말 어쩌면 단순한..? 부분에서 삽질을 많이해서 머리 아프기도 했지만 좋은 경험이었던 거 같습니다.

자세한 코드는 [Github](https://github.com/wjdrbs96/SpringBoot-Docker-Nginx) 에서 확인할 수 있습니다. 