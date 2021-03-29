# `Docker 클라이언트 명령어 알아보기`

```
docker run <이미지이름>
```

앞에서 위와 같은 명령어를 사용했었습니다. 위의 명령어는 어떤 의미를 가지고 있을까요?

<img width="802" alt="스크린샷 2021-03-29 오전 10 37 36" src="https://user-images.githubusercontent.com/45676906/112776486-c9253200-907a-11eb-958c-ef08527e9e9f.png">

1. 도커 클라이언트에 명령어 입력후 도커 서버로 보냄
2. 도커 서버에서 컨테이너를 위한 이미지가 이미 캐쉬가 되어 있는지 확인
3. 없으면 도커 허브에서 다운 받아옴(Pulling) 있다면 이미 가지고 있는 이미지 컨테이너 생성

```
docker run alpine ls
```

![스크린샷 2021-03-29 오전 10 39 56](https://user-images.githubusercontent.com/45676906/112776611-1d301680-907b-11eb-994d-d22484bc694a.png)

위와 같이 뜨는 것을 볼 수 있습니다. 

<br>

## `ubuntu 컨테이너`

```
docker run --rm -it ubuntu:20.04 /bin/sh
```

![스크린샷 2021-03-29 오전 11 21 31](https://user-images.githubusercontent.com/45676906/112779089-eb21b300-9080-11eb-8d63-9fbd01d301df.png)

<br>

## `웹 어플리케이션 실행하기`

```
docker run --rm -p 5678:5678 hashicorp/http-echo -text="hello world"
```

![스크린샷 2021-03-29 오전 11 23 21](https://user-images.githubusercontent.com/45676906/112779216-2d4af480-9081-11eb-9773-109290e691ce.png)

![스크린샷 2021-03-29 오전 11 23 59](https://user-images.githubusercontent.com/45676906/112779259-481d6900-9081-11eb-9d75-d05b342128b1.png)

위와 같이 `http://localhost:5678`로 접속했을 때 명령어가 뜨는 것을 볼 수 있습니다. 

<br>

## `MySQL 실행하기`

```
docker run -d -p 3306:3306 -e MYSQL_ALLOW_EMPTY_PASSWORD=true --name mysql mysql:5.7
```

![스크린샷 2021-03-29 오전 11 26 11](https://user-images.githubusercontent.com/45676906/112779366-929ee580-9081-11eb-8b17-7c5094ce7a39.png)

그러면 위와 같이 `Dockerhub`에서 이미지를 다운받는 것을 볼 수 있습니다.

```
docker exec -it mysql mysql
```

- `exec 명령어`: exec 명령어는 run 명령어와 달리 실행중인 도커 컨테이너에 접속할 때 사용하며 컨테이너 안에 ssh server 등을 설치하지 않고 exec 명령어로 접속합니다. 

위와 같이 mysql 이미지를 실행하여 컨테이너를 만들어 접속할 수 있습니다. 

```
create database wp CHARACTER SET utf8;
grant all privileges on wp.* to wp@'%' identified by 'wp';
flush privileges;
```

<br>

## `워드프레스 블로그 실행하기`

```
docker run -d -p 8080:80 -e WORDPRESS_DB_HOST=host.docker.internal -e WORDPRESS_DB_NAME=wp -e WORDPRESS_DB_USER=wp -e WORDPRESS_DB_PASSWORD=wp wordpress
```

![스크린샷 2021-03-29 오전 11 33 01](https://user-images.githubusercontent.com/45676906/112779831-86ffee80-9082-11eb-8faa-ecdad8e81f54.png)

![스크린샷 2021-03-29 오전 11 33 29](https://user-images.githubusercontent.com/45676906/112779856-9717ce00-9082-11eb-9432-4da1b0d9aba6.png)

위와 같이`http://localhost:8080`에서 `WordPress`가 실행되는 것을 볼 수 있습니다. 

<br>

## `컨테이너 나열하기`

```
docker ps
```

![스크린샷 2021-03-29 오전 10 43 11](https://user-images.githubusercontent.com/45676906/112776832-a8a9a780-907b-11eb-9632-51a08938b0d1.png)

위와 같이 `docker Container ID`, `Image` 등등을 볼 수 있습니다.

<br>

## `Docker 컨테이너 생명주기`

<img width="1121" alt="스크린샷 2021-03-29 오전 10 45 20" src="https://user-images.githubusercontent.com/45676906/112776932-ddb5fa00-907b-11eb-85d1-ed1197209dc2.png">

Docker 컨테이너는 위와 같은 생명주기를 갖고 있습니다. 엄청 많은 명령어를 가지고 있지만 간단하게 몇 개 명령어만 알아보겠습니다. 

```
docker run <이미지 이름>
docker stop <컨테이너 ID> 
docker kill <컨테이너 ID>
docker rm <컨테이너 이름> => 먼저 실행 중인 컨테이너가 중지 되어 있어야 함
```


