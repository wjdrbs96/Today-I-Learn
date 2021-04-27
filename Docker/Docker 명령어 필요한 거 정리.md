# `Docker Container 접속하는 법`

```
docker exec -t -i container_name /bin/bash (Docker Container 접속)
ex) Ddocker exec -t -i nginx /bin/bash (Docker Container 접속)
```

- `Docker Cotainer에 접속하고 싶을 때`

<br>

## `Docker Compose로 Container 이름 지정하는 법`

```yaml
version: "3"
services:
  web:
    image: nginx
    container_name: nginx
    ports:
      - 81:80
    volumes:
      - /etc/nignx/conf.d/:/etc/nginx/conf.d
```

- 위와 같이 `container_name`을 지정하면 Container가 실행될 때 지정한 이름으로 만들어짐

<br>

## `Docker Container 환경변수 등록하는 법`

```
docker run -it --name "spring" -d -p $IDLE_PORT:$IDLE_PORT leebal
```

- 위와 같이 `-it --name "이름"`을 지정해주면 해당 이름을 가지고 컨테이너가 실행됨

<br>

## `Docker 컨테이너 실행시 환경변수 등록하는 법`

```
docker run -it --name "spring" -d -e active=real1 -p $IDLE_PORT:$IDLE_PORT leebal
```

- 위와 같이 -e `이름=환경변수이름`으로 지정하면 dockerfile 에서 꺼내서 사용할 수 있음.

```dockerfile
FROM openjdk:11-jre-slim

WORKDIR /root

COPY ./demo-0.0.1-SNAPSHOT.jar .

CMD java -jar -Dspring.profiles.active=${active} demo-0.0.1-SNAPSHOT.jar
```

위와 같이 `${이름}`을 사용하면 환경변수 꺼낼 수 있음

<br>

## `Nginx Docker Container reload 하는 법`

```
sudo docker exec -it nginx nginx -s reload

```

<br>

## `실행중인 Docker Container ID 가져오기`

```
docker ps | awk 'NR > 1 {print $1}'       (실행 중인 Docker Container ID 모두)
docker ps | awk 'NR > 1 {print $1; exit}' (맨 위의 Docker Container ID 하나만)
```

<br>

## `Docker-Compose yml`

```yaml
version: "3"
services:
  web:
    image: nginx
    container_name: nginx
    ports:
      - 80:80
    volumes:
      - /etc/nginx/:/etc/nginx/
      - /etc/nginx/conf.d/:/etc/nginx/conf.d
  spring:
    build: .
    ports:
      - 8081:8081
    volumes:
      - ./:/root/
    environment:
      active: real1
```

- 보일로 플레이트 코드처럼 남겨놓음

<br>

## `현재 실행 중인 Docker Container ID`

```
docker ps -q
```

<br>

## `현재 실행 중인 Docker Container Names`

```
docker ps --format "{{.Names}}"
```

<br>

## `Docker Container Name으로 찾기`

```
docker container ls -f "name=real1" 
docker container ls -f "name=real1" -q  (해당 컨테이너 이름의 Container ID만 뽑아내기)
```

<br>

# `Reference`

- [https://linuxize.com/post/how-to-list-docker-containers/](https://linuxize.com/post/how-to-list-docker-containers/)