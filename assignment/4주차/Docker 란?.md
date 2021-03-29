# `Docker를 사용하는 이유`

<img width="765" alt="스크린샷 2021-03-29 오전 9 16 27" src="https://user-images.githubusercontent.com/45676906/112773068-72fec180-906f-11eb-8363-d5bae76e20d1.png">

어떤 프로그램을 다운받아 사용한다면 일반적으로 위와 같은 과정을 거치게 됩니다. 하지만 실제로 많은 서버들과 다양한 운영체제, 패키지 버전 등등 이유로 프로그램을 설치하는 과정에서 에러가 발생하게 됩니다. 

A라는 서버에서는 됐는데 B라는 서버에서는 안될 수도 있고.. 이러한 문제들을 가지고 있습니다. 또한 설치 과정도 그렇게 쉽지만은 않습니다. 

`도커는 이러한 설치 과정을 엄청나게 단순하게 만들 수 있습니다.`

<br>

### `Redis 설치 비교해보기`

Docker를 이용해서 Redis를 설치할 때와 Redis를 직접다운 받을 때의 차이를 비교해보겠습니다. 

```
$ wget https://download.redis.io/releases/redis-6.2.1.tar.gz
$ tar xzf redis-6.2.1.tar.gz
$ cd redis-6.2.1
$ make
```

먼저 Docker를 사용하지 않고 Redis를 다운 받으면 위와 같이 명령어를 입력해주라고 나옵니다. 하지만 이것을 사용하기 위해서는 wget도 설치해야 하고 생각보다는? 번거로운 일들이 조금씩 존재합니다.

이번에는 `Docker`를 이용해서 Redis를 다운받아 보겠습니다. (물론 Docker도 설치하긴 해야 합니다..)

```
docker run -it redis
```

![스크린샷 2021-03-29 오전 9 24 42](https://user-images.githubusercontent.com/45676906/112773320-99712c80-9070-11eb-8592-2b7274b75621.png)

그러면 위와 같이 바로 `Redis`를 사용할 수 있는 터미널로 접속하는 것을 볼 수 있습니다. 훨씬 간단하고 편리한 것을 느낄 수 있을 것입니다. 

<br>

## `Docker란 무엇일까요?`

한마디로 무엇이다 라고 표현하기는 힘들지만, 간단하게라도 Docker가 무엇인지를 알아보겠습니다. 

> `컨테이너`를 사용하여 응용프로그램을 더 쉽게 만들고 배포하고 실행할 수 있도록 설계된 도구이며 컨테이너 기반의 오픈소스 가상화 플랫폼이며 생태계입니다. 

<br>

### `컨테이너`란 무엇일까요?

<img width="604" alt="스크린샷 2021-03-29 오전 9 31 10" src="https://user-images.githubusercontent.com/45676906/112773579-81e67380-9071-11eb-9109-68a34d1ab6c0.png">

실제 현실에서 사용하는 `컨테이너`처럼 컨테이너 안에 `다양한 프로그램`, `실행환경`을 컨테이너 안에 추상화하고 동일한 인터페이스를 제공하여 프로그램의 배포 및 관리를 단순하게 해주는 것입니다.

> 컨테이너는 코드와 모든 종속성을 패키지화하여 응용 프로그램이 한 컴퓨팅 환경에서 다른 컴퓨팅 환경으로 빠르고 안정적으로 실행되도록 하는 소프트웨어의 표준 단위이다.

<br>

### `Docker 이미지와 컨테이너란?`

- `Docker 이미지`: 프로그램을 실행하는데 필요한 설정이나 종속성들을 갖고 있습니다.  
- `Docker 컨테이너`: 이미지의 인스턴스이며 프로그램을 실행합니다.

<img width="850" alt="스크린샷 2021-03-23 오전 11 45 07" src="https://user-images.githubusercontent.com/45676906/112085085-37767a00-8bcd-11eb-81f7-c410b52eea49.png">

<br>

## `Docker 실행 흐름`

1. `CLI에서 Docker 명령어 입력`
2. `도커 서버(Docker Daemon)이 커맨드를 받아서 그것에 따라 이미지를 생성하든 컨테이너를 실행하든 모든 작업을 하게 됩니다.`

<img width="1092" alt="스크린샷 2021-03-29 오전 9 39 11" src="https://user-images.githubusercontent.com/45676906/112773860-a000a380-9072-11eb-9989-fe1a1c55117e.png">

![스크린샷 2021-03-29 오전 9 43 27](https://user-images.githubusercontent.com/45676906/112774043-4e0c4d80-9073-11eb-9b67-e607e5adb07a.png)

```
docker run hello-world
```

위와 같이 `hello-world` 이미지를 Docker hub에서 다운 받은 후에 바로 컨테이너를 실행하는 것을 볼 수 있습니다.

<img width="1250" alt="스크린샷 2021-03-29 오전 9 46 30" src="https://user-images.githubusercontent.com/45676906/112774136-a5aab900-9073-11eb-8013-4161cfb2b9b9.png">

전체적인 흐름을 정리하면 위와 같이 진행됩니다. `Docker hub`에는 정말 많은 이미지들이 있는데 여기서 나의 로컬 컴퓨터에 다운 받지 않은 이미지라면 새로 다운받게 되고, 다운 받은 적이 있다면 `Image Cache`에 저장이 되어 있어 새로 다운 받지 않습니다.



<br>

## `Docker Image 생성하기`

- ### `Dockerfile 이란?`

```
Docker Image를 만들기 위한 설정 파일입니다. 컨테이너가 어떻게 행동해야 하는지에 대한 설정들을 정의하는 파일입니다.
```

<img width="895" alt="스크린샷 2021-03-23 오후 2 26 48" src="https://user-images.githubusercontent.com/45676906/112097697-cee6c780-8be3-11eb-9297-8d76b9a43ff7.png">

<br>

## `Docker Image에 이름 주기`

```
docker build -t 도커아이디/이미지이름 디렉토리경로(dockerfile 존재하는)
docker build -t wjdrbs96/hello:latest .

docker run -it wjdrbs96/hello:latest (잘 실행되는 것을 알 수 있음)
```

