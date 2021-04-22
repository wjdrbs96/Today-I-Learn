# `들어가기 전에`

![test](https://user-images.githubusercontent.com/45676906/95607939-6a9dd480-0a97-11eb-8d68-55000fda8654.png)

이러한 구조로 자동화 배포를 진행한 적이 있습니다. Github에 push만 하면 자동으로 EC2에 새로운 버전의 jar가 배포되고 재 실행되어서 배포가 되었습니다. 하지만 위의 방식에는 단점이 존재합니다. 배포가 진행되는 도중에는 서버가 잠시 중단되야 한다는 점입니다.

CodeDeploy가 EC2에 새로운 버전의 jar를 배포할 때, 기존의 실행 중인 jar를 kill 한 후에 새로운 버전의 jar를 실행시킬 것입니다. 이 과정에서 서버가 잠시 중단되어 사용자들은 서비스에 접속할 수 없게 됩니다. 현재 우리가 많이 사용하고 있는 서비스들을 보면 새로운 버전의 업데이트가 일어나더라도 서비스가 중단되거나 하지 않고 `무중단`으로 배포가 이루어집니다.

무중단 배포에는 `인스턴스를 여러 개` 만들어 [현재 위치 배포 방식](https://devlog-wjdrbs96.tistory.com/304) 으로 할 수도 있고, [블루 그린 배포 방식](https://devlog-wjdrbs96.tistory.com/300) 으로 진행할 수도 있습니다.
하지만 프리티어를 유지하기 위해서는 1대의 EC2로만 운영해야 하기 때문에.. 2대 이상의 EC2를 만드는게 은근? 부담이 될 수 있습니다. 

그래서 이번 글에서는 `Nginx`를 이용해서 EC2 내부에 포트를 나눠서 `무중단 배포`를 진행해보겠습니다.   

<br>

![art](https://camo.githubusercontent.com/cc9fd5ff1b28c4d41067f7bdfd6563f7d01c610f14305a74b5c788d5f91821d1/68747470733a2f2f74312e6461756d63646e2e6e65742f6366696c652f746973746f72792f393936463736334435413733463931453236)

위와 같이 Nginx를 이용해서 실제 배포 중에도 서비스가 중단되지 않도록 하는 아키텍쳐를 만들어보겠습니다. 

<br> <br>

## `Nginx는 왜 사용하는가?`

- `Nginx는 고성능 웹서버입니다.`
- `Nginx의 리버스 프록시 기능이 존재합니다.`
    - `리버스 프록시란 외부의 요청을 받아 백엔드 서버로 요청을 전달하는 것을 의미합니다.`
- `다른 배포 방식에 비해서 저렴하고 쉽습니다.`

위에서 말했던 것처럼 이번 글의 예제에서는 EC2 인스턴스 하나로만 진행할 수 있기 때문에 비용이 들지 않는다는 장점이 있습니다. 

<br> <br>

## `Nginx 무중단 배포 방식`

위에서 보았던 것처럼 `EC2 Linux2 버전의 1대에 Nginx 1대와 Spring Boot jar 2대`를 사용하여 진행할 것입니다.

<img width="754" alt="스크린샷 2021-04-22 오전 9 13 53" src="https://user-images.githubusercontent.com/45676906/115637417-0fed0b80-a34b-11eb-92ab-c5c4303ec2a7.png">

사용자는 Nginx의 포트 번호로 접속합니다. 이번 실습에서는 HTTPS는 적용하지 않았기 때문에 80번 포트로 접속하게 될 것입니다. 80번 포트로 접속하면 Nginx의 리버스 프록시 기능으로 8081 포트로 전달을 해줍니다. 

여기서 새로운 버전으로 배포를 해야한다면 어떻게 될까요? 8082 포트에 새로운 버전의 jar를 업데이트하고 Nginx를 8082 포트로 Reload 시키면 됩니다. 

<img width="739" alt="스크린샷 2021-04-22 오전 9 17 14" src="https://user-images.githubusercontent.com/45676906/115637611-87bb3600-a34b-11eb-9192-134417ce5736.png">

위와 같이 Nginx가 바라보는 곳을 8082로 바꾸면 됩니다. Reload 하는 시간은 1초 이내이기 때문에 `무중단 배포`가 가능합니다. 

<br> <br>

## `Spring Boot 세팅하기`

Spring Boot에서 포트 번호 설정은 `application.properties` 파일에서 설정할 수 있습니다.

![스크린샷 2021-04-22 오전 9 40 58](https://user-images.githubusercontent.com/45676906/115639187-f057e200-a34e-11eb-9660-a68396fd1f47.png)

위와 같이 `application-real1.properties`, `application-real2.properties` 2개의 파일을 만들겠습니다.(기존의 application.properties 삭제해도 되고 안해도 됩니다.)

![스크린샷 2021-04-22 오전 9 42 52](https://user-images.githubusercontent.com/45676906/115639317-3d3bb880-a34f-11eb-977a-9dd318fd706d.png)

![스크린샷 2021-04-22 오전 9 43 07](https://user-images.githubusercontent.com/45676906/115639313-3c0a8b80-a34f-11eb-9c8c-2a3eb84af884.png)

위와 같이 `real1은 8081`, `real2은 8082`로 포트 번호를 세팅하겠습니다. 

<br>

### `real1, real2 properties로 실행하는 법`

먼저 jar 파일을 만들겠습니다. (현재 저는 `gradle` 기반으로 Spring Boot 프로젝트를 만들었습니다.)

```
./gradlew clena build
```

![스크린샷 2021-04-22 오전 9 49 34](https://user-images.githubusercontent.com/45676906/115639656-1c279780-a350-11eb-8a67-9603a152727c.png)

그러면 위와 같이 `build/libs` 디렉토리 아래에 jar 파일이 생겼을 것입니다.

<br>

```
java -jar -Dspring.profiles.active=real1 ./build/libs/*.jar    // real1.properties 실행 (8081)
java -jar -Dspring.profiles.active=real2 ./build/libs/*.jar    // real2.properties 실행 (8082)
```

위와 같이 `-Dspring.profiles.active={이름}`의 형태만 정해서 실행시켜 주면 해당 설정파일이 적용이 됩니다.

![스크린샷 2021-04-22 오전 9 47 16](https://user-images.githubusercontent.com/45676906/115639541-d36fde80-a34f-11eb-968d-a05d2e9ec718.png)

저는 `real1`을 적용시켰는데 그러면 위와 같이 8081 포트로 톰캣이 뜨는 것을 볼 수 있습니다. 

<br> <br>

## `EC2 Linux2 에서 jar 파일 실행하기`

- `Github Repository를 git pull 받은 후에 jar 실행`
- `Local에서 jar를 만든 후에 Filezila로 EC2로 전송 후 실행`

jar를 EC2에서 실행시키는 방법은 2가지 정도 됩니다. 저는 Filezila를 사용해서 로컬에서 만든 jar를 EC2에 보낸 후에 실행시키겠습니다.

![스크린샷 2021-04-22 오전 9 53 51](https://user-images.githubusercontent.com/45676906/115639906-b2f45400-a350-11eb-8c2d-4592e95ff83c.png)

현재 저의 EC2는 위와 같이 jar 파일을 가지고 있는 상태입니다.

![스크린샷 2021-04-22 오전 9 55 17](https://user-images.githubusercontent.com/45676906/115640046-fea6fd80-a350-11eb-94f0-22dca3dcb118.png)

```
nohup java -jar -Dspring.profiles.active=real1 *.jar &
```

위와 같이 `nohup`과 `&`를 사용하여야 백그라운드로 실행할 수 있습니다. 

<br>

![스크린샷 2021-04-22 오전 9 57 41](https://user-images.githubusercontent.com/45676906/115640175-462d8980-a351-11eb-87be-6c1d53c8cfb2.png)

`sudo netstat -tnlp` 명령어로 현재 실행 중인 포트를 확인해보면 위와 같이 8081로 실행되고 있는 것을 볼 수 있습니다.


<br> <br>

# `Nginx로 무중단 배포하기`

먼저 EC2 Linux2에 Nginx를 설치하겠습니다.

```
sudo yum install nginx       // 설치
sudo service nginx start     // 시작
sudo service nginx stop      // 중지
sudo service nginx restart   // Nginx 서비스를 중지했다가 시작합니다.
sudo service nginx reload    // Nginx 서비스를 정상적으로 다시 시작합니다. 다시로드 할 때 기본 Nginx 프로세스는 자식 프로세스를 종료하고 새 구성을로드하며 새 자식 프로세스를 시작합니다.
sudo service nginx status    // 상태 확인
```

![스크린샷 2021-04-21 오후 4 27 13](https://user-images.githubusercontent.com/45676906/115513921-8db70580-a2be-11eb-8482-19e7f2505a8e.png)

설치를 한 후에 상태 확인을 하면 위와 같이 `active (running)`이 뜨는 것을 확인할 수 있습니다. 그리고 80번 포트로 접속했을 때 jar 실행 포트인 8081로 프록싱이 될 수 있도록 설정하겠습니다. 

```
sudo vim /etc/nginx/nginx.conf
```

위의 경로의 파일을 vim으로 편집을 하겠습니다.

![스크린샷 2021-04-22 오전 9 25 48](https://user-images.githubusercontent.com/45676906/115638184-da492200-a34c-11eb-9122-56c722e31bf2.png)

<br>

```shell script
include /etc/nginx/conf.d/service-url.inc;

location / {
        proxy_pass $service_url;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header Host $http_host;
}
```

- `proxy_pass`: Nginx로 요청이 오면 service_url에 등록된 곳으로 전달합니다.
- `proxy_set_header_XXX`: 실제 요청 데이터를 header의 각 항목에 할당합니다.

<br>

그리고 이번에는 `service-url.inc`를 수정하겠습니다.

```
sudo vim /etc/nginx/conf.d/service-url.inc
```

위의 경로의 파일을 vim으로 편집하겠습니다.

<br>

![스크린샷 2021-04-22 오전 10 04 08](https://user-images.githubusercontent.com/45676906/115640523-221e7800-a352-11eb-90c1-09c59e4dd68b.png)

그리고 위와 같이 설정하고 하는 url을 입력하겠습니다.(저의 EC2 IP 주소를 적었습니다.)

```
sudo service nginx restart (재시작)
```

그리고 Nginx를 재시작 한 후에 80번 포트로 접속해보겠습니다.

![스크린샷 2021-04-22 오전 10 01 11](https://user-images.githubusercontent.com/45676906/115640409-d370de00-a351-11eb-9d7e-1df462b781d4.png)

![스크린샷 2021-04-22 오전 10 02 42](https://user-images.githubusercontent.com/45676906/115640451-f00d1600-a351-11eb-95ec-b11ae60094d8.png)

그러면 위와 같이 80번 포트의 결과와 8081의 결과가 똑같은 것을 볼 수 있습니다. 즉 80번 포트로 접속해서 Nginx가 8081 포트로 잘 전달하고 있습니다.

<br>

## `무중단 배포 스크립트 만들기`

무중단 배포 스크립트를 만들기 전에 현재 8081이 실행 중인지 8082가 실행 중인지 판단할 수 있는 API 하나를 만들겠습니다.

![스크린샷 2021-04-22 오전 10 09 05](https://user-images.githubusercontent.com/45676906/115640873-e637e280-a352-11eb-80b5-3d5792e04181.png)

```java
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class TestController {

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
}
```

위의 API에 대해서 설명하자면 8081로 요청을 보냈을 때 응답이 오면 `real1`을 String으로 응답하고, 8082로 요청을 보냈을 때 응답이 오면 `real2`를 String으로 응답하는 API 입니다.

- `env.getActiveProfiles()`: 현재 실행 중인 ActiveProfiles를 모두 가져옵니다. (위에서 jar 실행 시킬 때 `-Dspring.profiles.active`에 적어주었던 값들 전부 다 읽어옵니다.)

<br>

이제 배포 스크립트를 작성하겠습니다. 배포 스크립트를 작성하는 것이 쉽지 않지만.. 셸 스크립트는 서버 개발자에게 중요한 것이기 때문에 공부를 하면서 계속 진행해보겠습니다 

![스크린샷 2021-04-22 오전 10 14 03](https://user-images.githubusercontent.com/45676906/115641187-8aba2480-a353-11eb-8704-5f4a4f4d1508.png)

위와 같은 디렉토리 구조로 `배포 스크립트`를 작성하겠습니다. 이번 글에서 필요한 파일은 `health.sh`, `profile.sh`, `start.sh`, `stop.sh`, `switch.sh` 파일들 입니다.

- `stop.sh`: 기존 엔직엔스에 연결되어 있지 않지만, 실행 중이던 스프링 부트 종료
- `start.sh`: 배포할 신규 버전 스프링 부트 프로젝트를 stop.sh로 종료한 `profile` 실행
- `health.sh`: start.sh로 실행시킨 프로젝트가 정상적으로 실행됐는지 체크
- `switch.sh`: 엔직엑스가 바라보는 스프링 부트를 최신 버전으로 변경
- `profile.sh`: 앞선 4개 스크립트 파일에서 공용으로 사용할 `profile`과 포트 체크 로직

<br>

각 스크립트 파일의 역할을 정리하자면 위와 같습니다. 이제 하나씩 작성해보면서 자동 배포를 진행해보겠습니다. 현재 CI/CD 중 CD의 역할은 CodeDeploy로 하고 있습니다. CodeDeploy가 작동하는 Flow는 `appspec.yml` 파일에 작성을 하게 되는데요.
이 파일에 대해서도 한번 알아보겠습니다. 그런데 그 전에 CodeDeploy가 배포를 진행하는 단계에 대해서 알아야 합니다.

![스크린샷 2021-04-22 오전 10 21 25](https://user-images.githubusercontent.com/45676906/115641738-abcf4500-a354-11eb-94d1-dc59f4a6e8dc.png)

CodeDeploy는 위의 단계를 거쳐서 배포를 하게 됩니다. 각 단계는 어떤 의미를 담고 있을까요?

- `BeforeInstall` – 파일 암호화 해제 및 현재 버전의 백업 만들기와 같은 사전 설치 작업에 이 배포 수명 주기 이벤트를 사용할 수 있습니다.

- `Install` – 이 배포 수명 주기 이벤트 중에 CodeDeploy 에이전트는 개정 파일을 임시 위치에서 최종 대상 폴더로 복사합니다. 이 이벤트는 CodeDeploy 에이전트에 예약되어 있으므로 스크립트 실행에 사용할 수 없습니다.

- `AfterInstall` – 애플리케이션 구성 또는 파일 권한 변경과 같은 작업에 이 배포 수명 주기 이벤트를 사용할 수 있습니다.

- `ApplicationStart` – 중지된 서비스를 다시 시작하려면 일반적으로 이 배포 수명 주기 이벤트를 사용합니다

- `ValidateService` – 마지막 배포 수명 주기 이벤트입니다. 배포가 성공적으로 완료되었는지 확인하는 데 사용됩니다.

간단하게 정리하면 위와 같습니다. 이번 글에서는 `AfterInstall`, `ApplicationStart`, `ValidateService` 단계에 스크립트 파일을 실행시키겠습니다.

<br> <br>

## `appspec.yml`

```yaml
version: 0.0
os: linux
files:
  - source:  /     # 현재 프로젝트의 루트 경로
    destination: /home/ec2-user/app/step3/zip/   # EC2 내부에서 어떤 디렉토리로 전달할 것인지? 
    overwrite: yes  # 덮어쓰기 할 것인지?

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

- `AfterInstall`: 현재 단계에서 stop.sh 스크립트 파일을 실행시켜서 실행 중이던 스프링 부트 jar를 종료시킵니다.
- `ApplicationStart`: start.sh 스크립트 파일을 실행시켜서 배포할 새로운 버전의 jar 파일을 실행시킵니다.
- `ValidateSerivce`: health.sh 스크립트 파일을 통해서 현재 jar가 잘 실행 중인지 체크를 합니다.
- 참고로 EC2 내부에 `/home/ec2-user/app/step3/zip/` 저는 이렇게 목적지 설정을 해놓았기 때문에 해당 디렉토리를 미리 만들어야 합니다.

<br>

위의 과정이 모두 성공이 된다면 배포는 성공할 것입니다. 이제 드디어.. 스크립트 파일을 작성하로 가보겠습니다. 스크립트 파일에서 쓰이는 문법들을 [여기](https://devlog-wjdrbs96.tistory.com/308) 에서 간단히 정리해놨는데
같이 참고해서 보면 도움이 될 거 같습니다.

<br>

### `profile.sh`

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

    echo "${IDLE_PROFILE}"    # bash script는 return 기능이 없기 떄문에 echo를 통해서 출력하면 이 값을 클라이언트가 사용할 수 있습니다.
}
# 쉬고 있는 profile의 port 찾기
function find_idle_port()
{
    IDLE_PROFILE=$(find_idle_profile)

    if [ ${IDLE_PROFILE} == real1 ]
    then
      echo "8081"   # 여기도 마찬가지로 return 기능의 느낌
    else
      echo "8082"   # 여기도 마찬가지로 return 기능의 느낌
    fi
}
``` 

- `sudo curl -s -o /dev/null -w "%{http_code}" http://3.36.209.141/`: 해당 주소로 요청을 보낸 후에 상태 코드를 http_code에 담습니다.
- `echo "${IDLE_PROFILE}`: bash script는 return 기능이 없기 때문에 함수 마지막에 echo를 사용해주면 이 값을 클라이언트가 사용할 수 있습니다.
- `IDLE_PROFILE=$(find_idle_profile)`: 즉, find_idle_port 함수에서 해당 구문을 통해 real1 or real2의 값을 사용할 수 있게 됩니다.

<br>

### `stop.sh`

```shell script
#!/usr/bin/env bash

ABSPATH=$(readlink -f $0)
ABSDIR=$(dirname $ABSPATH)
source ${ABSDIR}/profile.sh

IDLE_PORT=$(find_idle_port)

echo "> $IDLE_PORT 에서 구동중인 애플리케이션 pid 확인"
IDLE_PID=$(sudo lsof -ti tcp:${IDLE_PORT})

if [ -z ${IDLE_PID} ]
then
  echo "> 현재 구동중인 애플리케이션이 없으므로 종료하지 않습니다."
else
  echo "> kill -15 $IDLE_PID"   # Nginx에 연결되어 있지는 않지만 현재 실행 중인 jar 를 Kill 합니다.
  kill -15 ${IDLE_PID}
  sleep 5
fi
```

해당 파일은 profile.sh에 존재하는 find_idle_port 함수를 통해서 현재 구동되고 있는 포트의 PID를 확인 한 후에 Kill 하는 스크립트 입니다.

<br>

### `start.sh`

```shell script
#!/usr/bin/env bash

ABSPATH=$(readlink -f $0)     
ABSDIR=$(dirname $ABSPATH)
source ${ABSDIR}/profile.sh   # import profile.sh 

REPOSITORY=/home/ec2-user/app/step3

echo "> Build 파일 복사"
echo "> cp $REPOSITORY/zip/*.jar $REPOSITORY/"

cp $REPOSITORY/zip/*.jar $REPOSITORY/

echo "> 새 어플리케이션 배포"
JAR_NAME=$(ls -tr $REPOSITORY/*.jar | tail -n 1)    # jar 이름 꺼내오기

echo "> JAR Name: $JAR_NAME"

echo "> $JAR_NAME 에 실행권한 추가"

chmod +x $JAR_NAME

echo "> $JAR_NAME 실행"

IDLE_PROFILE=$(find_idle_profile)

echo "> $JAR_NAME 를 profile=$IDLE_PROFILE 로 실행합니다."
nohup java -jar \
    -Dspring.profiles.active=$IDLE_PROFILE \   # 위에서 보았던 것처럼 $IDLE_PROFILE에는 real1 or real2가 반환되는데 반환되는 properties를 실행한다는 뜻입니다.
    $JAR_NAME > $REPOSITORY/nohup.out 2>&1 & 
```

- `$(readlink -f $0)`: 이것을 통해서 해당 파일의 절대경로를 알아냅니다.
- `$(dirname $ABSPATH)`: 디렉터리 경로를 출력하기 위한 명령어입니다. 
- `source ${ABSDIR}/profile.sh`: Java에서 import와 비슷한 기능입니다. profile.sh를 start.sh에서 사용하겠다는 뜻입니다.
- `IDLE_PROFILE=$(find_idle_profile)`: profile.sh에 존재하는 find_idle_profile 함수에서 real1 or real2를 echo로 출력했던 값을 이렇게 꺼내올 수 있습니다. (return 받은 것과 비슷합니다.)
- `-Dspring.profiles.active`: $IDLE_PROFILE을 통해서 받은 real1 or real2로 jar 파일을 실행합니다.

<br>

### `health.sh`

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

for RETRY_COUNT in {1..10}  # for문 10번 돌기
do
  RESPONSE=$(curl -s http://3.36.209.141:${IDLE_PORT})   # 현재 문제 없이 잘 실행되고 있는 요청을 보내봅니다.
  UP_COUNT=$(echo ${RESPONSE} | grep 'real' | wc -l)     # 해당 결과의 줄 수를 숫자로 리턴합니다.

  if [ ${UP_COUNT} -ge 1 ]
  then # $up_count >= 1 ("real" 문자열이 있는지 검증)
      echo "> Health check 성공"
      switch_proxy   # switch.sh 실행
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

해당 스크립트 파일은 jar가 정말 실행이 잘 되고 있는지 확인하는 것입니다.(리눅스 명령어나 스크립트 파일 문법 관련은 [여기](https://devlog-wjdrbs96.tistory.com/308) 에서 정리 했으니 위에서 안보고 왔다면 보고 오는 것을 추천드립니다.)

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
    # 아래 줄은 보면 echo를 통해서 나온 결과를 | 파이프라인을 통해서 service-url.inc에 덮어쓸 수 있습니다.
    echo "set \$service_url http://3.36.209.141:${IDLE_PORT};" | sudo tee /etc/nginx/conf.d/service-url.inc 
    echo "> 엔진엑스 Reload"
    sudo service nginx reload    # Nginx reload를 합니다.
}
```

주석으로 남겨놓았지만 echo를 통해서 전환할 포트가 담긴 주소를 service-url.inc에 새로 쓰게 됩니다.

<br>

## `무중단 배포 진행해보기`

![스크린샷 2021-04-22 오전 11 07 52](https://user-images.githubusercontent.com/45676906/115645243-fc49a100-a35a-11eb-892a-f03e8ed5ff84.png)

그리고 Github에 push를 하면 Github이 Travis CI로 Hook를 날려서 CI가 시작됩니다. (과정은 맨 위에서 말한 아키텍쳐와 같습니다.)

<br> 

![스크린샷 2021-04-22 오전 11 07 26](https://user-images.githubusercontent.com/45676906/115645218-f0f67580-a35a-11eb-8430-0a58ea47c739.png)

![스크린샷 2021-04-22 오전 11 09 29](https://user-images.githubusercontent.com/45676906/115645403-4763b400-a35b-11eb-9b50-3b8ff0508d74.png)

그리고 위와 같이 appspec.yml에서 지정한 목적지에 파일이 잘 전달된 것도 확인할 수 있습니다.

<br>

```
sudo vim /etc/nginx/conf.d/service-url.inc
```

그리고 다시 해당 파일을 열어서 Nginx Reload가 잘 되어는지 확인해보겠습니다.

<br>

![스크린샷 2021-04-22 오전 11 10 47](https://user-images.githubusercontent.com/45676906/115645464-64988280-a35b-11eb-8575-e40122a6311d.png)

그러면 처음에는 8081 이었지만 `switch.sh`에 의해서 Nginx가 Reload 되고 8082로 바뀐 것을 확인할 수 있습니다.

<br>

![스크린샷 2021-04-22 오전 11 12 18](https://user-images.githubusercontent.com/45676906/115645630-a5909700-a35b-11eb-96bd-cd44787794d5.png)

그러면 위와 같이 현재 8081, 8082 두개의 jar가 실행되는 것을 볼 수 있습니다.(현재 Nginx에 연결된 것은 위에서 볼 수 있듯이 8082로 바뀌었습니다.)

<br>

![스크린샷 2021-04-22 오전 11 13 31](https://user-images.githubusercontent.com/45676906/115645716-d244ae80-a35b-11eb-9f4e-a07ac2fa0469.png)

