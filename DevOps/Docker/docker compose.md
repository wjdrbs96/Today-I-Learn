# `docker compose란?`

[여기](https://github.com/wjdrbs96/Today-I-Learn/blob/master/DevOps/Docker/%EB%8F%84%EC%BB%A4%20%EA%B8%B0%EB%B3%B8%EB%AA%85%EB%A0%B9%EC%96%B4.md) 에서 도커 기본 명령어를 공부했을 때
띄어쓰기라던지 오타 등등 되게 사소한 것을 실수하면 다시 명렁어를 쳐야하기 때문에 귀찮기도 하고 복잡하기도 한 느낌을 얻었을 것입니다.

그래서 이것을 편하게 해주는 것이 `docker compose` 입니다.

```
docker-compose version
```

도커를 설치하면 docker-compose도 같이 설치가 되기 때문에 위의 버전 확인 명령어로 버전을 확인할 수 있습니다. 

바로 실습을 해보겠습니다. Docker라는 디렉토리를 만든 후에 그 안에 `docker-compose.yml` 이라는 파일을 만들겠습니다.

```yaml
version: '2'
services:
  db:
    image: mysql:5.7 
    volumes:
      - ./mysql:/var/lib/mysql 
    restart: always 
    environment:
      MYSQL_ROOT_PASSWORD: wordpress
      MYSQL_DATABASE: wordpress 
      MYSQL_USER: wordpress 
      MYSQL_PASSWORD: wordpress
  wordpress:
    image: wordpress:latest
    volumes:
      - ./wp:/var/www/html 
    ports:
      - "8000:80" 
    restart: always 
    environment:
      WORDPRESS_DB_HOST: db:3306 
      WORDPRESS_DB_PASSWORD: wordpress
```

그리고 위에서 작성 했던 명렁을 yml 파일 형식에 맞춰서 위와 같이 작성했습니다. docker 디렉토리 아래에서 아래의 명령어를 실행해보겠습니다.

```
docker-compose up
```

<img width="1131" alt="스크린샷 2020-12-10 오전 2 36 13" src="https://user-images.githubusercontent.com/45676906/101665569-78b8a300-3a90-11eb-9dac-e40cef7c50aa.png">

그러면 위와 같이 `mysql`, `wordpress`가 실행되는 것을 볼 수 있습니다. 그리고 위에서 지정했던 `http://localhost:8000`으로 접속을 해보겠습니다.

![스크린샷 2020-12-10 오전 2 37 11](https://user-images.githubusercontent.com/45676906/101665666-9d147f80-3a90-11eb-8b43-6cf3834c0856.png)

이렇게 워드프레스가 잘 뜨는 것을 확인할 수 있습니다.

<br>

## 정리

도커 명령어를 하나하나 입력하는 것보다는 yml 파일에서 `docker-compose`를 이용해서 사용하면 됩니다.
