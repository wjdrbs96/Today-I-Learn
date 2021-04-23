# `EC2에 Docker를 이용해서 웹 서버 띄우기`
    
이번 글에서는 EC2 Linux2를 이용해서 실습을 진행해보겠습니다. 
    
### `docker 설치`

```
sudo yum update -y
sudo amazon-linux-extras install -y docker
sudo service docker start
```

<br>

### `Amazon Linux 2에 LAMP 웹 서버 설치`

EC2 linux2 버전에서 웹 서버를 설치하는 명령어를 정리해보겠습니다.

```
sudo yum update -y
sudo amazon-linux-extras install -y php7.2
sudo yum install -y httpd  
sudo systemctl start httpd (Apache 웹 서버 시작(d는 daemon 임))
sudo systemctl enable httpd (Apache 웹 서버가 매번 시스템이 부팅할 때마다 시작되도록 함)
sudo systemctl is-enabled httpd (httpd 가 실행되고 있는지 확인하는 명령어)
```

그리고 본인의 EC2 IP 주소로 접속하면 아래와 같이 웹 서버가 잘 설치된 것을 볼 수 있습니다. 

![스크린샷 2021-03-29 오후 4 00 04](https://user-images.githubusercontent.com/45676906/112798439-db699500-90a7-11eb-89bd-7773895346df.png)

<br>

그리고 `docker` 라는 디렉토리를 만들고 그 안에 `Dockerfile`을 작성해보겠습니다. 

<br>

### `Dockerfile`

```
# 베이스 이미지는 ubuntu:18.04 를 사용
FROM ubuntu:18.04
MAINTAINER Gyunny <wjdrbs966@naver.com>

RUN apt-get update
RUN apt-get install -y apache2 # Install Apache web server (Only 'yes')

# 컨테이너를 80번 포트로 하겠다. 
EXPOSE 80

# apache 서버가 죽지 않고 백그라운드로 돌아가기 위해서 아래의 명령어 실행
CMD ["apachectl", "-D", "FOREGROUND"]
```

위와 같이 `Dockerfile`을 작성하겠습니다. 

```
docker build -t gyunny .
```

그리고 해당 Dockerfile이 있는 위치에서 위의 명령어를 입력하면 `gyunny` 라는 이름의 Image가 만들어지게 됩니다. 

![스크린샷 2021-03-29 오후 3 53 17](https://user-images.githubusercontent.com/45676906/112797863-ff78a680-90a6-11eb-8cac-2b5dcfcd758d.png)

`docker images` 라는 명령어를 통해서 확인해보면 위와 같이 이미지가 잘 만들어진 것을 볼 수 있습니다. 그리고 `gyunny Image`를 가지고 8000, 8001번 Port 컨테이너를 실행시켜 보겠습니다. 

```
docker run -d -p 8000:80 gyunny
docker run -d -p 8001:80 gyunny

docker ps (실행중인 컨테이너 확인)
``` 

위의 명령어를 통해서 `컨테이너 80번 포트와 EC2 8000, 8001 포트를 연결`해서 2개의 컨테이너를 실행 시켜보겠습니다. 

![스크린샷 2021-03-29 오후 3 52 43](https://user-images.githubusercontent.com/45676906/112797860-fe477980-90a6-11eb-990d-928a358c4f61.png)

그러면 위와 같이 컨테이너 두개가 실행되고 있는 것을 볼 수 있습니다.

<img width="1792" alt="스크린샷 2021-03-29 오후 3 55 54" src="https://user-images.githubusercontent.com/45676906/112798131-6d24d280-90a7-11eb-9f0b-0c84b7e5080f.png">

<img width="1792" alt="스크린샷 2021-03-29 오후 3 56 02" src="https://user-images.githubusercontent.com/45676906/112798116-68601e80-90a7-11eb-9bfc-9ad388fd416b.png">

그리고 위와 같이 `8000`, `8001` 포트로 접근하면 위와 같이 화면이 뜨는 것을 볼 수 있습니다. 

<br>

# `Reference`

- [https://docs.aws.amazon.com/ko_kr/AWSEC2/latest/UserGuide/ec2-lamp-amazon-linux-2.html](https://docs.aws.amazon.com/ko_kr/AWSEC2/latest/UserGuide/ec2-lamp-amazon-linux-2.html)
