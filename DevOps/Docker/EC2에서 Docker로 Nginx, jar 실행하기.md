# `EC2에서 Docker로 Nginx Reverse Proxy 하는 법`

이번 글에서는 EC2에서 `Docker-Compose`를 이용해서 `Nginx`, `Spring Boot` 컨테이너 두 대를 실행시켜서 `Reverse Proxy` 하는 것을 해보겠습니다. 

<br>

## `서버 아키텍쳐`

<img width="518" alt="스크린샷 2021-04-23 오후 9 05 02" src="https://user-images.githubusercontent.com/45676906/115868537-930b8000-a477-11eb-8d6b-43c4b3df7dd7.png">

이번 글의 아키텍쳐를 보면 위와 같습니다. 그럼 바로 실습을 해보겠습니다. 

<br>

## `EC2 Docker 설치`

현재 저는 `EC2 Linux2`를 사용하고 있습니다. 

```
sudo yum update -y
sudo amazon-linux-extras install -y docker
sudo service docker start
```

![스크린샷 2021-04-23 오후 9 08 31](https://user-images.githubusercontent.com/45676906/115868929-2349c500-a478-11eb-8b0a-8bce38aee6b8.png)

도커가 잘 설치되었는지 `docker --version`으로 확인할 수 있습니다. 그리고 `Docker-compose`도 사용할 것이기 때문에 이것도 설치하겠습니다.

<br>

## `EC2 Docker-compose 설치`

```
sudo curl -L https://github.com/docker/compose/releases/download/1.25.0\
-rc2/docker-compose-`uname -s`-`uname -m` -o \
/usr/local/bin/docker-compose
```

![스크린샷 2021-04-23 오후 9 11 30](https://user-images.githubusercontent.com/45676906/115869215-89364c80-a478-11eb-80d0-b66e591648f2.png)

`docker-compose -v`로 Docker-compose가 잘 설치되었는지도 확인하겠습니다.

<br>

## `실행권한 추가하기`

설치가 되었더도 `docker-compose up` 명령어를 실행하기 위해서는 실행권한을 주어야 합니다. 

```
sudo chmod +x /usr/local/bin/docker-compose
```

위의 명령어를 통해서 권한까지 주면 Docker 설정은 다 되었습니다. 이제 EC2에 Nginx를 설치하겠습니다.

<br> <br>

## `EC2에 Nginx 설치`

```
sudo yum install nginx       // 설치
sudo service nginx start     // 시작
sudo service nginx stop      // 중지
sudo service nginx restart   // Nginx 서비스를 중지했다가 시작합니다.
sudo service nginx reload    // Nginx 서비스를 정상적으로 다시 시작합니다. 다시로드 할 때 기본 Nginx 프로세스는 자식 프로세스를 종료하고 새 구성을로드하며 새 자식 프로세스를 시작합니다.
sudo service nginx status    // 상태 확인
```

맨 위의 명령어로 Nginx를 설치하겠습니다. 그리고 80번 포트는 `Docker Container`가 사용할 것이기 때문에 Nginx는 중지 시키겠습니다.

<br>

![스크린샷 2021-04-23 오후 9 35 21](https://user-images.githubusercontent.com/45676906/115871713-dff15580-a47b-11eb-82c4-e753a09f792c.png)

그러면 위와 같이 `inactive` 상태가 될 것입니다.    

<br>

그리고 80번 포트로 접속했을 때 jar 실행 포트인 8081로 프록싱이 될 수 있도록 설정하겠습니다. 

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

<br>

![스크린샷 2021-04-22 오전 10 01 11](https://user-images.githubusercontent.com/45676906/115640409-d370de00-a351-11eb-9d7e-1df462b781d4.png)

![스크린샷 2021-04-22 오전 10 02 42](https://user-images.githubusercontent.com/45676906/115640451-f00d1600-a351-11eb-95ec-b11ae60094d8.png)

그러면 위와 같이 80번 포트의 결과와 8081의 결과가 똑같은 것을 볼 수 있습니다. 즉 80번 포트로 접속해서 Nginx가 8081 포트로 잘 전달하고 있습니다
(참고로 jar 파일의 루트 경로에 real1이 반환되도록 간단한 API를 작성해놓았습니다.)

<br>

![스크린샷 2021-04-23 오후 9 47 59](https://user-images.githubusercontent.com/45676906/115873151-93a71500-a47d-11eb-8b18-658b517f51f4.png)

그리고 이제 Dockerfile과 docker-compose.yml 파일을 작성하겠습니다.

<br> <br>

## `Dockerfile 작성`

```dockerfile
FROM openjdk:11-jre-slim

WORKDIR /root

COPY ./*.jar .

CMD java -jar demo-0.0.1-SNAPSHOT.jar
```

- `FROM`: 사용한 이미지를 적어줍니다.
- `WORKDIR`: 컨테이너에서 작업할 디렉토리를 지정해줍니다.
- `COPY`: 현재 위치에 있는 파일을 컨테이너 안으로 복사합니다.
- `CMD`: 컨테이너에서 실행할 명령어를 적습니다.

<br> <br>

## `docker-compose.yml 작성`

```yaml
version: "3"
services:
  web:             # nginx 컨테이너 이름 (원하는 이름)
    image: nginx  
    ports:
      - 80:80
    volumes:
      - /etc/nginx/:/etc/nginx/    # Nginx 컨테이너 내부 /etc/nginx 디렉토리가 위에서 설치한 EC2 내부에 /etc/nginx 디렉토리를 참조함
  spring:         # Spring Boot 컨테이너 이름 (원하는 이름)
    build: .
    ports:
      - 8081:8080
    volumes:
      - ./:/root/  
```

- `version`: 도커 컴포즈의 버전
- `services`: 이곳에 실행하려는 컨테이너들을 정의
- `image`: 사용할 이미지 적기
- `build`: 경로에 해당하는 Dockerfile 실행
- `ports`: EC2 포트:컨테이너 포트 매핑
- `volumes`: EC2 디렉토리와 컨테이너 디렉토리 매핑

여기서 주의 깊게 볼 곳은 `volumes` 입니다. yml 파일 내부에 Nginx 이미지에 volumes를 보면 현재 EC2에 설치된 Nginx의 디렉토리와 컨테이너 디렉토리를 매핑시키는 것입니다. 

<br>

![스크린샷 2021-04-23 오후 9 14 25](https://user-images.githubusercontent.com/45676906/115869512-f1852e00-a478-11eb-9ddf-84842655ba64.png)

그리고 저는 `Spring Boot`를 실행시킬 jar를 EC2에 만들어놓았습니다. 그래서 현재 EC2에 `jar`, `docker-compose.yml`, `dockerfile`이 존재하는 상황입니다.

```
docker-compose up -d (docker-compose.yml을 detach 모드로 실행)
```

<br>

![스크린샷 2021-04-23 오후 9 45 06](https://user-images.githubusercontent.com/45676906/115872899-4aef5c00-a47d-11eb-8cef-2c0b1bc4833f.png)

그러면 위와 같이 Docker Container가 잘 실행되는 것을 볼 수 있습니다. 

![스크린샷 2021-04-22 오전 10 01 11](https://user-images.githubusercontent.com/45676906/115640409-d370de00-a351-11eb-9d7e-1df462b781d4.png)

그러면 위에서 보았던 결과랑 똑같은 결과를 얻을 수 있습니다. 
 