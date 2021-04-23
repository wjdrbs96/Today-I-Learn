# `Docker로 Spring Boot, Nginx 사용하기`

이번 글에서는 Docker로 Spring Boot, Nginx 컨테이너를 실행시키고, Nginx로 리버스 프록싱 하는 것에 대해서 알아보겠습니다.
먼저 `Nginx를 MacOS`에서 설치하겟습니다.

<br>

## `Nginx 설치`

```
brew install nginx
brew service start nginx
```

설치한 후에 Nginx를 시작하고 `http://localhost:8080`으로 접속해보겠습니다.

<br>

<img width="573" alt="스크린샷 2021-04-23 오후 6 50 23" src="https://user-images.githubusercontent.com/45676906/115854009-c395ee80-a464-11eb-9efb-143eabe5124e.png">

위와 같이 뜨면 잘 설치가 된 것입니다. 그리고 실제 nginx 설정들이 모여있는 경로도 잘 존재하는지 확인해보겠습니다.

```
cd /usr/local/etc/nginx
```

<br>

![스크린샷 2021-04-23 오후 6 51 51](https://user-images.githubusercontent.com/45676906/115854318-15d70f80-a465-11eb-9931-1c301010ddac.png)

위와 같이 파일들이 잘 존재하는지 확인을 하겠습니다. 


<br>

## `EC2 Linux2에서 Docker-Compose 설치하기`

```
sudo curl -L https://github.com/docker/compose/releases/download/1.25.0\
-rc2/docker-compose-`uname -s`-`uname -m` -o \
/usr/local/bin/docker-compose
```

<br>

## `실행권한 추가하기`

```
sudo chmod +x /usr/local/bin/docker-compose
docker-compose version (버전 확인)
```

<br>

## `Compose 명령어 실행 안될 때`

```
sudo chown $USER /var/run/docker.sock
```

위의 명령어 후에 `docker-compose up` 하기

<br>

## `Docker Nginx 컨테이너 접속하기`

```
docker exec -it NGINX_container_ID bash
```
