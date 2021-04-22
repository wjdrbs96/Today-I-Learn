# `Docker Volume 이란 무엇일까?`

[저번 글](https://devlog-wjdrbs96.tistory.com/310) 에서 `Docker Volume`에 대해서 어느정도 다루었습니다. 저번 글에서는 DataBase의 데이터를 저장하기 위해서 Volume으로 컨테이너의 디렉토리와 로컬 디렉토리를 매핑시켜줘서 데이터를 계속 저장할 수 있도록 하는 글이었습니다. 

그런데 또 다른 문제도 존재합니다. Dockerfile로 Image를 만든 후에, 원래 소스 코드의 내용이 바뀌면 이미지를 새로 만들고 다시 컨테이너를 띄워야한다는 불편함이 있습니다. 이번 글에서는 `Docker Volume`을 이용해서 이러한 단점을 해결해보겠습니다.

<br>

![스크린샷 2021-04-22 오후 4 23 52](https://user-images.githubusercontent.com/45676906/115673062-2b760780-a387-11eb-9e6f-3fb08b4183e4.png)

보통 위와 같이 `Dockerfile 작성`-> `이미지 생성` -> `이미지로 컨테이너 실행`의 과정을 거치게 됩니다. 여기서 소스코드의 수정이 일어나면 이러한 과정을 계속 반복해야 합니다. Docker Volume을 쓰면 이러한 과정을 반복하지 않고 컨테이너만 재실행 시켜주면 수정한 소스코드가 바로 반영된다는 장점이 있습니다. 

바로 실습을 진행해보면서 좀 더 자세히 알아보겠습니다. 

<br>

## `Dockerfile 작성`

```dockerfile
FROM openjdk:11-jre-slim   // 이미지 다운

WORKDIR /root    // 컨테이너 내부에서 작업할 디렉토리

COPY ./build/libs/demo-0.0.1-SNAPSHOT.jar .    // 로컬에 있는 파일을 컨테이너 내부로 복사
 
CMD java -jar demo-0.0.1-SNAPSHOT.jar          // 컨테이너 내부에서 실행할 명령어
``` 

Spring Boot jar를 실행시키는 `dockerfile`을 작성했다고 가정하겠습니다. 

- `COPY`를 보면 로컬에 존재하는 jar 파일을 컨테이너 내부로 복사하는 것입니다. 즉, 새로운 버전의 jar가 생긴다면 이미지를 다시 빌드해서 새로운 jar가 컨테이너로 복사될 수 있도록 위의 과정을 반복해야 하는 것입니다.

<br>

## `Docker Volume 사용하기`

<img width="848" alt="스크린샷 2021-04-22 오후 4 31 43" src="https://user-images.githubusercontent.com/45676906/115674050-3aa98500-a388-11eb-9f3d-b985a94a4bcc.png">

COPY는 말 그대로 위에서 말했던 것처럼 로컬에 존재하는 파일을 컨테이너 내부로 복사하는 명령어입니다.

<br>

<img width="836" alt="스크린샷 2021-04-22 오후 4 31 50" src="https://user-images.githubusercontent.com/45676906/115674067-3e3d0c00-a388-11eb-9b94-7d415a36fb31.png">

`반면에 Volume은 컨테이너 내부의 디렉토리가 로컬 디렉토리를 참조하는 것입니다.` 즉, 로컬 디렉토리가 변경이 되어도 컨테이너는 로컬 디렉토리를 참조하고 있기 때문에 수정된 사항을 다시 이미지 빌드 하지 않아도 됩니다. 다시 말해 Volume은 위의 COPY의 단점을 보완해줄 수 있습니다.

<br>

## `Docker Image 만들기`

![스크린샷 2021-04-22 오후 4 43 34](https://user-images.githubusercontent.com/45676906/115676057-2cf4ff00-a38a-11eb-89b9-e34242162e86.png)

현재 Spring Boot root 경로에 Dockerfile이 존재합니다. Dockerfile과 같은 위치에서 아래의 명령어로 이미지를 만들겠습니다.

```
docker build -t 이미지이름 ./
ex) docker build -t gyunny ./
```

그러면 위와 같이 이미지가 만들어진 것을 확인할 수 있습니다. 그리고 Volume을 이용해서 이미지를 컨테이너로 실행시키겠습니다. 

<br>

### `Volume으로 컨테이너 실행하기`

```
docker run -d -p 원하는포트:8080 -v $(pwd):/root <이미지 이름>   
ex) docker -d run -p 8000:8080 -v $(pwd):/root gyunny  
```

- -v 옵션 뒤를 보면 :를 기준으로 왼쪽 디렉토리는 로컬에 존재하는 곳이고, 오른쪽은 컨테이너 내부에 존재하는 디렉토리 경로입니다. 즉, 컨테이너 내부에 어떤 디렉토리가 로컬에 어떤 디렉토리를 참조할 것인지를 설정하는 것입니다.

<br>

![스크린샷 2021-04-22 오후 4 49 54](https://user-images.githubusercontent.com/45676906/115676759-dcca6c80-a38a-11eb-9241-fbbd0364a6f5.png)

```
docker run -d -p 8000:8080 -v $(pwd)/build/libs:/root gyunny
``` 

저는 jar 파일이 위치하는 경로인 `$(pwd)/build/libs`와 컨테이너 내부 /root 를 매핑 시켰습니다. (컨테이너 내부에 다른 작업 디렉터리를 설정했다면 그것을 매핑시키면 됩니다.)

<br>

![스크린샷 2021-04-22 오후 4 53 01](https://user-images.githubusercontent.com/45676906/115677235-3f236d00-a38b-11eb-8d21-f39fbdb0f76b.png)

그래서 `http://localhost:8000`으로 접속해보면 기존에 존재하던 API가 반환이 되고 있는 것을 볼 수 있습니다.(저는 기존에 default가 반환되도록 만들어놓았습니다.)

<br>

## `간단한 소스 코드 수정 해보기`

간단한 소스 코드 수정하고 jar 파일을 업데이트 하고 컨테이너만 재실행하면 이미지를 다시 빌드하지 않아도 새로운 소스코드 내용이 반영이 되는지를 테스트 해보겠습니다.

![스크린샷 2021-04-22 오후 4 55 51](https://user-images.githubusercontent.com/45676906/115677593-988b9c00-a38b-11eb-906d-88a546a5a545.png)

위와 같이 새로운 API를 추가한 후에 컨테이너를 중지하고 재실행 해보겠습니다.

<br>

![스크린샷 2021-04-22 오후 5 00 15](https://user-images.githubusercontent.com/45676906/115678444-75adb780-a38c-11eb-9aa5-fa59f4d10af0.png)

```
./gradlew clean build  
docker ps
docker stop {중지할 컨테이너 ID}
docker run -d -p 8000:8080 -v $(pwd)/build/lib:/root gyunny 
```

- `./gradlew clean build`: 명령어를 통해서 새로운 버전의 jar를 만들어냅니다.
- `docker stop`: 현재 실행 중인 컨테이너를 중지합니다.
- 기존 이미지를 다시 빌드하지 않고 컨테이너 재실행만 해줍니다.

<br>

![스크린샷 2021-04-22 오후 5 11 07](https://user-images.githubusercontent.com/45676906/115679688-ce318480-a38d-11eb-933a-1da07cbe5954.png)


<br>

위와 같이 이미지를 다시 빌드하지 않고 컨테이너 재실행만 하였음에도 새로운 소스코드가 반영된 것을 볼 수 있습니다. 즉, gyunny 컨테이너가 저의 로컬 PC jar가 위치한 디렉토리를 참조하고 있기 때문에 가능한 것입니다. 

이렇게 이번 글에서는 기존에 COPY의 단점인 새로운 소스코드가 생길 때마다 이미지를 다시 빌드해야 한다는 단점을 `Docker Volume`을 통해서 해결할 것을 정리하였습니다. 