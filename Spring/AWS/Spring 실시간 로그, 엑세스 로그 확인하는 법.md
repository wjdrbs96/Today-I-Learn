## `Spring 실시간 로그, 엑세스 로그 확인하는 법`

이번 글에서는 `Spring Boot`를 `AWS EC2 Linux2`에 배포했을 때 실시간으로 로그가 찍히는 것을 확인할 수 있는 방법과 엑세스 로그를 확인하는 법에 대해서 공유해보려 합니다. 실시간 로그 보는 방법은 여러가지가 있겠지만 제가 생각하기에 괜찮다 싶은 방법입니다.   

이번 글은 이미 `jar` 파일이 `AWS EC2`에 있다고 가정하고 작성하겠습니다. 만약 `jar`를 `EC2`에 올리는 법이 궁금하시다면 [여기](https://devlog-wjdrbs96.tistory.com/408?category=882690) 를 보고 오셔도 좋을 거 같습니다.

<br> <br>

## `jar 실시간 로그 볼 수 있도록 실행하기`

```shell
nohup java -jar /home/ec2-user/*.jar --logging.file.path=/home/ec2-user/ --logging.level.org.hibernate.SQL=DEBUG >> /home/ec2-user/deploy.log 2>/home/ec2-user/deploy_err.log &
```

현재 `EC2`에 `jar` 파일이 존재하는 상황이라면 위와 같이 각자에 맞는 경로를 정해서 실행하면 됩니다.

- `logging.file.path`: 로그들이 저장된 파일의 경로를 저장하는 곳입니다. `jar`를 실행하면 해당 경로에 `spring.log`가 자동으로 생깁니다.
- `logging.level.org.hibernate.SQL`: 로깅 레벨은 상황에 따라 다르겠지만, 저는 `DEBUG` 모드로 실행하였습니다.

<br>

<img width="733" alt="스크린샷 2021-12-09 오후 7 28 48" src="https://user-images.githubusercontent.com/45676906/145379773-f0128417-a6d6-4112-8c89-f11815e6835f.png">

위의 명령어를 실행하면 위처럼 `spring.log`가 자동으로 생긴 것을 볼 수 있습니다. 

<br>

```
tail -f spring.log
```

위의 명령어를 통해서 로그를 본 상태에서 해당 `jar`가 가진 `Error`가 날 수 있는 상황으로 요청을 보내보겠습니다. 

<br>

![스크린샷 2021-12-09 오후 7 31 07](https://user-images.githubusercontent.com/45676906/145380161-1b2ca88d-c5d0-46df-a7d5-62f686fed761.png)

그래서 저는 위처럼 `예제 로그인 API에서 비밀번호가 틀리는 상황`을 가정하고 에러를 발생시켰습니다. 

<br>

![스크린샷 2021-12-09 오후 7 32 51](https://user-images.githubusercontent.com/45676906/145380358-ef00643e-3c4e-448d-8e76-33b23ec4be45.png)

그러면 위와 같이 실시간으로 로그들이 찍히는 것을 볼 수 있고, 실시간으로 보고 있지 않더라도 나중에 어떤 에러인지 잘 모르겠을 때 `spring.log`를 확인해서 어떤 에러가 발생했는지 좀 더 명확하게 알 수 있습니다. 

<br> <br>

## `엑세스 로그 확인하는 법`

이번에는 명령어 양이 많다 보니 `Shell Script`로 만들어서 실행시키겠습니다. 

```shell
JAVA_OPTS="${JAVA_OPTS} -Dserver.tomcat.accesslog.enabled=true"
JAVA_OPTS="${JAVA_OPTS} -Dserver.tomcat.basedir=."

nohup java ${JAVA_OPTS} -jar /home/ec2-user/*.jar --logging.file.path=/home/ec2-user/ --logging.level.org.hibernate.SQL=DEBUG >> /home/ec2-user/deploy.log 2>/home/ec2-user/deploy_err.log &
```

위처럼 `JAVA_OPTS`를 통해서 `tomcat accesslog를 활성화`하고 해당 `access.log`가 저장되는 `디렉토리`를 지정해주면 됩니다. 

<br>

![스크린샷 2021-12-09 오후 7 52 25](https://user-images.githubusercontent.com/45676906/145383337-b55fb4a1-6aa2-436d-8fba-bdd4a72d4242.png)

그리고 위의 셸 스크립트를 실행하면 `logs` 디렉토리가 생긴 것을 볼 수 있습니다. `access_log.2021-12-09.log` 그러면 이런식으로 날짜별로 `log` 파일이 자동으로 만들어집니다. 

<br>

```
tail -f logs/access_log.2021-12-09.log
```

![스크린샷 2021-12-09 오후 7 54 46](https://user-images.githubusercontent.com/45676906/145383638-6fa9fce6-ac85-4dc9-ba4a-f026efd9610a.png)

위의 명령어로 확인해보면 `access` 기록이 남을 때마다 해당 파일에 로그가 남는 것을 확인할 수 있습니다.