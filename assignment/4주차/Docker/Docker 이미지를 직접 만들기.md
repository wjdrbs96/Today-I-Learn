# `Docker 이미지를 직접 만들기`

1. Docker 이미지는 컨테이너를 만들기 위해 필요한 설정이나 종속성들을 갖고 있는 소프트웨어 패키지
2. Docker 이미지는 Dockerhub에 다른 사람들이 만들어놓은 이미지를 다운받아 사용할 수도 있고 직접 이미지를 만들어 사용할 수도 있습니다. 

<img width="849" alt="스크린샷 2021-03-29 오전 10 52 41" src="https://user-images.githubusercontent.com/45676906/112777322-e3f8a600-907c-11eb-8f82-668179b2685c.png">

<br>

## `이미지의 읽기 영역 vs 쓰기 영역`

<img width="1072" alt="스크린샷 2021-03-29 오전 11 44 36" src="https://user-images.githubusercontent.com/45676906/112780597-2671b100-9084-11eb-8257-a73ac9a8212f.png">

여기서 빨간 테두리는 `읽기 영역`, 초록색 테두리는 `쓰기 영역`을 할 수 있습니다.

- `읽기 영역`: ubuntu, centOS 등등
- `쓰기 영역`: Git 설치, 어떤 모듈 설치 등등


```
docker run -it --name git ubuntu:latest bash
```

![스크린샷 2021-03-29 오후 1 51 00](https://user-images.githubusercontent.com/45676906/112788438-dac80300-9095-11eb-9663-9b6073511a8e.png)

위와 같이 `ubuntu:bash` 이미지에는 `Git`이 설치되어 있지 않습니다. 

```
apt-get update
apt-get install -y git
```

![스크린샷 2021-03-29 오후 1 54 32](https://user-images.githubusercontent.com/45676906/112788649-4e6a1000-9096-11eb-9a67-3fb9b0f623fc.png)

`git`이라는 컨테이너를 `ubuntu:git` 태그로 commit을 해보겠습니다.

![스크린샷 2021-03-29 오후 1 57 25](https://user-images.githubusercontent.com/45676906/112788841-bf112c80-9096-11eb-8634-2b27d7e01062.png)

위와 같이 `images`가 생긴 것을 볼 수 있습니다. 

![스크린샷 2021-03-29 오후 1 59 47](https://user-images.githubusercontent.com/45676906/112789043-20d19680-9097-11eb-99ac-56ca38dbee11.png)

위와 같이 새로 만든 이미지로 다시 접속을 했을 때는 `git`이 설치되어 있는 것을 볼 수 있습니다. 

<br>

### `Dockerfile 이란?`

Dockerfile 이란 Docker Image를 만들기 위한 설정 파일입니다. 컨테이너가 어떻게 행동해야 하는지에 대한 설정들을 정의하는 파일입니다.

이번 글에서는 `Dockerfile`을 만들어서 이미지를 직접 만들어보겠습니다. 


<br>

### `Dockerfile 만드는 순서`

1. `베이스 이미지`를 명시하기(파일 스냅샷에 해당)
2. 추가적으로 필요한 파일을 다운 받기 위한 몇가지 명령어를 명시
3. 컨테이너 시작시 실행 될 명령어를 명시

<br>

### `베이스 이미지란?`

<img width="829" alt="스크린샷 2021-03-29 오전 11 04 03" src="https://user-images.githubusercontent.com/45676906/112777988-7a799700-907e-11eb-8642-b874d9e81121.png">

이미지는 `layer`들로 구성되어 있습니다. 이미지에 하나씩 레이어들을 더 추가할 수 있습니다. 

<br>

### `간단하게 Dockerfile로 hello 이미지 만들기`

```
# 베이스 이미지를 명시
FROM alpine 

# 추가적으로 필요한 파일들을 다운 받기
# RUN ~~  (이번 실습에서는 적지 않음)

# 컨테이너 시작 시 실행 될 명령어
CMD ["echo", "hello"]
```

```
docker build . (Dockerfile 실행하기) => Dockerfile이 있는 디렉토리에서 실행을 해야 함
```

![스크린샷 2021-03-29 오전 11 00 14](https://user-images.githubusercontent.com/45676906/112777752-f1faf680-907d-11eb-87e6-fb1fc5495a51.png)

<br>

### `실행과정 정리`

![스크린샷 2021-03-29 오전 11 10 08](https://user-images.githubusercontent.com/45676906/112778406-6edaa000-907f-11eb-9f2a-06f1599fa01b.png)

1. `베이스 이미지 alpine 이미지를 먼저 다운 받기`
2. `alpine 파일 스냅샷은 임시 컨테이너 하드디스크에 넣기`
3. `임시 컨테이너를 기반으로 새로운 이미지가 만들어지는 것`

<img width="1101" alt="스크린샷 2021-03-29 오전 11 12 40" src="https://user-images.githubusercontent.com/45676906/112778517-aea18780-907f-11eb-8eef-c5e0def35aa7.png">

![스크린샷 2021-03-29 오전 11 15 20](https://user-images.githubusercontent.com/45676906/112778703-0e982e00-9080-11eb-800f-21b5eb87fe30.png)

그래서 직접 만든 이미지를 위와 같이 실행을 하면 `hello`가 출력되는 것을 볼 수 있습니다. 

```
docker run -it ImageID
```

근데 ImageID를 기억하기란 쉽지가 않습니다. 그래서 이번에는 Image에 직접 이름을 지어보겠습니다. 

<br>

## `Docker Image에 이름 주기`

```
docker build -t 도커아이디/이미지이름 디렉토리경로(dockerfile 존재하는) . (이름은 아무거나 주어도 되지만 규칙은 이러함)
docker build -t wjdrbs96/hello:latest .

docker run -it wjdrbs96/hello:latest (잘 실행되는 것을 알 수 있음)
```

![스크린샷 2021-03-29 오전 11 17 59](https://user-images.githubusercontent.com/45676906/112778910-89f9df80-9080-11eb-87f9-85bffd0bb3a5.png)

위와 같이 이름을 주어서 잘 실행할 수 있는 것을 볼 수 있습니다.

## `Docker Image 생성하기`

- ### `Dockerfile 이란?`

```
Docker Image를 만들기 위한 설정 파일입니다. 컨테이너가 어떻게 행동해야 하는지에 대한 설정들을 정의하는 파일입니다.
```

<img width="895" alt="스크린샷 2021-03-23 오후 2 26 48" src="https://user-images.githubusercontent.com/45676906/112097697-cee6c780-8be3-11eb-9297-8d76b9a43ff7.png">