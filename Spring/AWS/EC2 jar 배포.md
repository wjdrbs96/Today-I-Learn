# `Spring Boot EC2에서 배포하는 법`

이번 글에서는 `Spring Boot` jar 파일을 `AWS EC2`에서 배포하는 법에 대해서 정리해보겠습니다. 지금은 많이 경험해보았기 때문에 많이 익숙하지만, 스프링을 처음 다루어 서버에 배포할 때는 항상 헷갈렸기에 다른 처음 해보시는 분들에게 정보를 공유하기 위해 한번 정리해보려 합니다.  

먼저 `Spring Boot` 프로젝트, AWS EC2 인스턴스 설정은 다 되어 있다고 가정하고 글을 시작해보겠습니다. 

<br>

## `maven`

```
mvn package 
```

만약에 `maven` 프로젝트를 사용하고 있다면 위와 같이 `mvn package` 명령어를 사용하면 `jar` 파일이 만들어집니다. 

![스크린샷 2021-10-22 오후 1 13 36](https://user-images.githubusercontent.com/45676906/138392534-157ac90a-2c27-46e5-8457-647c36424397.png)

그러면 위와 같이 `target` 디렉토리 아래에 `jar` 파일이 만들어진 것을 볼 수 있습니다. 

<br>

```
1. java -jar 파일이름.jar &  
ex) java -jar demo-0.0.1-SNAPSHOT.jar &
```

![스크린샷 2021-10-22 오후 1 15 54](https://user-images.githubusercontent.com/45676906/138392682-1e645d88-4644-4164-86ec-319990b091da.png)

이렇게 만들어진 jar 파일로 위와 같이 실행하면 Spring Boot를 그냥 실행한 것처럼 똑같이 실행되는 것을 볼 수 있습니다.

<br>

## `gradle`

```
$ ./gradlew clean build
$ sudo chmod 777 ./gradlew  (위의 명령어가 안된다면)
```

![스크린샷 2021-10-22 오후 1 18 14](https://user-images.githubusercontent.com/45676906/138392985-b3deb68f-19a4-4061-ac70-a18beb15712e.png)

만약 gradle 기반으로 Spring Boot 프로젝트를 만들었다면 `./gradlew clean build` 명령어를 통해서 jar 파일을 만들 수 있는데요. 빌드가 성공적으로 되면 위와 같이 `build/libs` 디렉토리 아래에 jar 파일이 만들어지는 것을 볼 수 있습니다. 

![스크린샷 2021-10-22 오후 1 21 43](https://user-images.githubusercontent.com/45676906/138393125-ea52f369-af69-4003-9a63-95fb70864864.png)

이번에도 `java -jar *.jar` 형태로 jar 파일을 실행하면 Spring Boot가 실행되는 것을 볼 수 있습니다. 

이제 이렇게 빌드가 성공된 jar 파일을 EC2로 옮겨서 EC2에서 실행하는 것을 해보겠습니다. 

<br> <br>

## `FileZila를 통해서 EC2 jar 배포하기`

![스크린샷 2021-05-13 오전 11 10 34](https://user-images.githubusercontent.com/45676906/118067776-2e8b7300-b3dc-11eb-804f-b82c853745b1.png)

<br>

![스크린샷 2021-05-13 오전 11 13 41](https://user-images.githubusercontent.com/45676906/118067914-6eeaf100-b3dc-11eb-9ae5-7fd5ca0a9984.png)

본인이 사용하는 EC2 운영체제에 따라 조금씩 차이는 있겠지만, 위와 같이 하면 `EC2`로 jar 파일이 전송할 수 있습니다. 

<br> <br>

## `EC2 jar 파일 배포하는 법`

```
sudo netstat -tnlp
```

![스크린샷 2021-02-06 오후 9 26 59](https://user-images.githubusercontent.com/45676906/107118111-4bd31180-68c2-11eb-8800-aa6cf2a6d322.png)

위의 명령을 통해서 현재 EC2에서 실행 중인 것들을 볼 수 있습니다. 

<br>

```
sudo kill -9 pid
ex) sudo kill -9 15212
sudo nohup java -jar server.jar &
```

그리고 `sudo kill -9`를 통해서 실행 중인 프로세스를 죽이고 전송한 jar 파일을 다시 실행하면 됩니다. 이번에는 `nohup` 명령어를 사용한 것을 볼 수 있는데요.

> nohup 명령어는 리눅스에서 프로세스를 실행한 터미널의 세션 연결이 끊어지더라도 지속적으로 동작 할 수 있게 해주는 명령어입니다

<br>

nohup 명령어의 의미는 위와 같습니다. 그리고 마지막에 &를 추가하면 jar 파일이 백그라운드로 실행될 수 있게 합니다.
