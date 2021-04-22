# `들어가기 전에`

일단 volume이 무엇인지 알기 전에 `워드프레스`, `MySQL`을 가지고 간단한 실습을 해보겠습니다.

<br>

## `MySQL 실행하기`

```
docker run -d -p 3306:3306 -e MYSQL_ALLOW_EMPTY_PASSWORD=true --name mysql mysql:5.7
```

위의 명령어를 터미널에서 실행해보겠습니다. 

![스크린샷 2021-04-22 오후 2 20 00](https://user-images.githubusercontent.com/45676906/115659685-e4cbe180-a375-11eb-96d0-5065d0c6d51d.png)

그러면 위와 같이 Docker Image(MySQL)를 다운받고 컨테이너가 실행되는 것을 볼 수 있습니다. 

<br>

### `MySQL 접속하기`

```
docker exec -it mysql mysql
```

<br>

![스크린샷 2021-04-22 오후 2 22 19](https://user-images.githubusercontent.com/45676906/115659912-383e2f80-a376-11eb-9769-41944b780297.png)

위와 같이 `exec 명령어`를 통해서 mysql에 접속을 했습니다.

```
create database wp CHARACTER SET utf8;
grant all privileges on wp.* to wp@'%' identified by 'wp';
flush privileges;
quit
```

<br>

![스크린샷 2021-04-22 오후 2 31 11](https://user-images.githubusercontent.com/45676906/115660762-74be5b00-a377-11eb-8272-66dc2bac5c1b.png)

![스크린샷 2021-04-22 오후 2 32 10](https://user-images.githubusercontent.com/45676906/115660851-8f90cf80-a377-11eb-89b4-d8c04fea9f73.png)

위의 명령어를 mysql 컨테이너에서 실행하겠습니다. 그리고 확인해보면 wp 테이블이 잘 만들어 진 것을 볼 수 있습니다.

<br>

## `워드플레스 블로그 실행하기`

```
docker run -d -p 8080:80 -e WORDPRESS_DB-HOST=host.docker.internal -e WORDPRESS_DB_NAME=wp -e WORDPRESS_DB_USER=wp -e WORDPRESS_DB_PASSWORD=wp wordpress
```

![스크린샷 2021-04-22 오후 2 36 15](https://user-images.githubusercontent.com/45676906/115661281-28bfe600-a378-11eb-94c6-1eae35543c23.png)

그러면 위와 같이 WordPress 이미지를 다운 받은 후에 컨테이너를 실행하게 됩니다.

<br>

![스크린샷 2021-04-22 오후 2 37 01](https://user-images.githubusercontent.com/45676906/115661364-4c832c00-a378-11eb-929b-ba899d0151f2.png)

문제 없이 작동하는지 확인해보면 컨테이너도 잘 실행되고 있는 것을 볼 수 있습니다.


 

<br>

# `Docker volume 이란 무엇일까?`

