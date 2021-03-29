# `Docker compose란?`

앞에서 입력했던 명령어들이 너무나 길고 입력하기가 쉽지 않았습니다. 이러한 명령어들을 쉽게 사용할 수 있게 나온 것이 `docker compose` 입니다.
 
<br>

## `docker-compose.yml`

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
      MYSQL_DATABSE: wordpress
      MYSQL_USER: wordpress
      MYSQL_PASSWORD: wordpress
  wordpress:
    image: wordpress:latest
    volumes: 
      - ./wp:/var/www/html
    ports:
      - "8080:80"
    restart: always
    environment: 
      WORDPRESS_DB_HOST: db:3306
      WORDPRESS_DB_PASSWORD: wordpress
```

명령어를 위와 같이 `yml` 형식으로 만들 수 있습니다. 

![스크린샷 2021-03-29 오전 11 39 48](https://user-images.githubusercontent.com/45676906/112780282-79973400-9083-11eb-9b68-ef79d41d7ecd.png)

```
docker-compose up
```

그러면 위와 같이 이것저것 뜨면서 컨테이너가 실행되는 것을 볼 수 있습니다. 

 