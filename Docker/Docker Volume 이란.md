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
docker run -d -p 8080:80 -e WORDPRESS_DB_HOST=host.docker.internal -e WORDPRESS_DB_NAME=wp -e WORDPRESS_DB_USER=wp -e WORDPRESS_DB_PASSWORD=wp wordpress
```

![스크린샷 2021-04-22 오후 3 07 36](https://user-images.githubusercontent.com/45676906/115664211-85250480-a37c-11eb-9abe-b1e4e8adf2f5.png)

그러면 위와 같이 WordPress 이미지를 다운 받은 후에 컨테이너를 실행하게 됩니다.

<br>

![스크린샷 2021-04-22 오후 2 37 01](https://user-images.githubusercontent.com/45676906/115661364-4c832c00-a378-11eb-929b-ba899d0151f2.png)

문제 없이 작동하는지 확인해보면 컨테이너도 잘 실행되고 있는 것을 볼 수 있습니다.

![스크린샷 2021-04-22 오후 3 14 18](https://user-images.githubusercontent.com/45676906/115664927-80ad1b80-a37d-11eb-89bb-bcfdf0e2f952.png)

그리고 `http://localhost:8080`으로 접속하면 위와 같은 워드프레스가 뜨는 것을 볼 수 있습니다. 한국어를 체크하고 `계속`을 누르겠습니다. 다음에 뜨는 것에 간단하게 원하는 정보들을 입력해서 회원가입을 하겠습니다.

<br>

그리고 다시 MySQL 컨테이너에 접속해보겠습니다.

```
docker exec -it mysql mysql
```

![스크린샷 2021-04-22 오후 3 18 25](https://user-images.githubusercontent.com/45676906/115665325-1cd72280-a37e-11eb-8881-4b956c2d107d.png)

접속하면 위와 같이 위에서 만들었던 wp(워드프레스) 테이블이 존재하는 것을 볼 수 있습니다.

<br>

![스크린샷 2021-04-22 오후 3 19 42](https://user-images.githubusercontent.com/45676906/115665411-3f693b80-a37e-11eb-8d1a-f143aaaa9d7a.png)

그리고 `show tables`를 하면 워드프레스 관련 테이블이 존재하는 것도 볼 수 있습니다. 이번에는 워드프레스에서 회원가입 했던 데이터들이 존재하는지 user 테이블을 조회해보겠습니다.

<br>

![스크린샷 2021-04-22 오후 3 23 01](https://user-images.githubusercontent.com/45676906/115665762-b0a8ee80-a37e-11eb-96f8-69c91cc8e8ea.png)

위에서 회원가입 했던 데이터들이 잘 저장되어 있는 것을 확인할 수 있습니다. `그런데 만약 MySQL 컨테이너를 멈췄다고 다시 실행시키면 데이터들은 어떻게 될까요?`

어떻게 되는지 MySQL 컨테이너를 중지해보겠습니다.

<br>

![스크린샷 2021-04-22 오후 3 28 52](https://user-images.githubusercontent.com/45676906/115666444-8e63a080-a37f-11eb-8f26-46eb658630d6.png)

위와 같이 MySQL 컨테이너를 중지하겠습니다. 그리고 다시 `http://localhost:8080`으로 접속해보겠습니다.

<br>

<img width="809" alt="스크린샷 2021-04-22 오후 3 30 26" src="https://user-images.githubusercontent.com/45676906/115666538-aaffd880-a37f-11eb-8a5e-439268fce74f.png">

이번에는 위와 같이 접속이 안되는 것을 볼 수 있습니다. 즉, MySQL 컨테이너에 존재하던 데이터들이 다 삭제가 된 것입니다. 이렇게 데이터가 삭제되면 나중에 문제가 될 수 있기 때문에 컨테이너가 중지되더라도 데이터를 보존하는 것이 필요합니다.

이럴 때 사용하는 것이 `Volume` 입니다. 한번 사용해보면서 좀 더 자세히 알아보겠습니다.

<br>

# `Docker volume 이란 무엇일까?`

```
docker run -d -p 3306:3306 -e MYSQL_ALLOW_EMPTY_PASSWORD=true --name mysql -v /Users/choejeong-gyun/Documents/mysql:/var/lib/mysql mysql:5.7
```

- `-v /Users/choejeong-gyun/Documents/mysql:/var/lib/mysql`: -v 옵션은 Docker Volume을 사용하겠다는 뜻이고 :를 기준으로 왼쪽은 나의 로컬 PC에 존재하는 디렉토리, 오른쪽은 MySQL 컨테이너 데이터가 저장되는 디렉토리 입니다. 즉, 두 디렉토리를 서로 연결시키겠다는 뜻입니다.
(MySQL 컨테이너 디렉토리가 나의 로컬 디렉토리를 참조하겠다는 뜻이기도 합니다.)

<br>

그리고 다시 한번 MySQL 컨테이너를 접속해서 아래의 명령어들을 입력하겠습니다.

```
docker exec -it mysql mysql
create database wp CHARACTER SET utf8;
grant all privileges on wp.* to wp@'%' identified by 'wp';
flush privileges;
quit
```

그리고 다시 `http://localhost:8080`으로 접속하면 워드프레스가 뜰 것입니다. 다시 한번 회원가입 절차를 진행해보겠습니다. 

![스크린샷 2021-04-22 오후 3 43 59](https://user-images.githubusercontent.com/45676906/115668046-91f82700-a381-11eb-80db-d29045fdb939.png)

그리고 위에서 MySQL 컨테이너 디렉토리와 매핑시켰던 로컬 PC 디렉토리를 보면 위와 같이 어떤 파일들이 새로 생긴 것을 볼 수 있습니다.

<br>

![스크린샷 2021-04-22 오후 3 45 56](https://user-images.githubusercontent.com/45676906/115668301-e9969280-a381-11eb-852b-db7f5f3aca09.png)

그리고 위와 같이 MySQL 컨테이너를 중지시킨 후에 다시 `http://localhost:8080`으로 접속해보겠습니다.

<br>

<img width="757" alt="스크린샷 2021-04-22 오후 3 47 24" src="https://user-images.githubusercontent.com/45676906/115668398-09c65180-a382-11eb-9eba-b35254eb3811.png">

그러면 위와 같이 컨테이너가 중지된 상태이기 때문에 DataBase Connection Error가 발생합니다. 하지만 이번에는 데이터가 로컬 PC 디렉토리에 저장되어 있기 때문에 다시 MySQL 컨테이너를 실행시키면 그대로 남아있을 것입니다.(docker rm 을 통해서 해당 컨테이너를 삭제도 해주겠습니다.) 

```
docker run -d -p 3306:3306 -e MYSQL_ALLOW_EMPTY_PASSWORD=true --name mysql -v /Users/choejeong-gyun/Documents/mysql:/var/lib/mysql mysql:5.7
```

위의 명령어를 다시 실행시켜보겠습니다.

<br>

<img width="1792" alt="스크린샷 2021-04-22 오후 3 49 56" src="https://user-images.githubusercontent.com/45676906/115668698-64f84400-a382-11eb-8788-27805344afce.png">

그리고 다시 `http://localhost:8080`으로 접속해보면 위와 같이 잘 뜨는 것을 볼 수 있습니다. 