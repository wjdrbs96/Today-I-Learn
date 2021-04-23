# `Docker Compose를 사용하는 이유는?`

먼저 Docker Compose를 사용하지 않고 NodeJS, Redis 컨테이너를 만들어서 컨테이너간 통신을 해보겠습니다. 

<br>

## `NodeJS 환경 세팅`

![스크린샷 2021-04-23 오후 2 59 37](https://user-images.githubusercontent.com/45676906/115825793-e4e6e280-a444-11eb-8879-ae6112905f01.png)

```
npm init
Enter (계속)
```

`npm init` 명령을 치면 어떤 것들이 여러 개 뜨는데 Enter를 계속 치겠습니다.

<br>

![스크린샷 2021-04-23 오후 3 01 20](https://user-images.githubusercontent.com/45676906/115825835-f03a0e00-a444-11eb-9a54-79f14a74ebe1.png)

그러면 위와 같이 `package.json`이 만들어집니다.

<br>

![스크린샷 2021-04-23 오후 3 07 29](https://user-images.githubusercontent.com/45676906/115826336-aef62e00-a445-11eb-990c-1e519fdfd8cb.png)

```json
{
  "name": "compose",
  "version": "1.0.0",
  "description": "",
  "main": "index.js",
  "scripts": {
    "start": "node server.js",
    "test": "echo \"Error: no test specified\" && exit 1"
  },
  "dependencies": {
    "express": "4.17.1",
    "redis": "3.0.2"
  },
  "author": "",
  "license": "ISC"
}
```

위와 같이 서버를 실행할 때 사용하기 위해서 scripts에 `start`와 `Express, Redis` 의존성을 추가하겠습니다. 그리고 `server.js` 파일을 하나 만든 후에 아래와 같이 작성해주겠습니다. 

```js
const express = require('express');
const redis = require('redis');

const client = redis.createClient({
  host: "redis-server",
  port: 6379
});

const app = express();

// 숫자는 0부터 시작

client.set("number", 0);

app.get('/', (req, res) => {
  client.get("number", (err, number) => {
    client.set("number", parseInt(number) + 1);
    res.send("숫자가 1씩 증가합니다." + number);
  })
})

app.listen(8080);        // 원하는 포트 지정

console.log('Server is Running !!');
``` 

해당 API 요청이 들어올 때마다 Redis를 이용하여 숫자가 1씩 증가하는 것을 화면으로 Response를 주는 간단한 코드입니다. 이제 테스트를 해보기 위해서 `Dockerfile` 부터 작성해보겠습니다. 

<br>

## `Dockerfile 작성`

```dockerfile
FROM node:10

WORKDIR /usr/src/app

COPY ./ ./

RUN npm install

CMD ["node", "server.js"]
``` 

- `FROM`: node:10을 베이스 이미지로 사용합니다.
- `WORKDIR`: 컨테이너 안에서 /usr/src/app 의 경로를 지정합니다.
- `COPY`: 현재 위치에 있는 파일들을 컨테이너에 복사합니다.
- `RUN`: 컨테이너 안에서 npm install을 해줍니다.
- `CMD`: script에서 설정해준 것처럼 `node server.js`로 서버를 실행합니다.

<br> <br>

## `Docker Containers간 통신할 때 나타나는 에러`

위의 코드를 실행하기 위해서는 NodeJS 컨테이너와 Redis 컨테이너가 필요합니다. 먼저 NodeJS를 실행하기 위해서는 Redis 서버가 실행되어야 합니다.

<img width="779" alt="스크린샷 2021-04-23 오후 3 18 59" src="https://user-images.githubusercontent.com/45676906/115827421-3b552080-a447-11eb-9dad-08a673d14ca6.png">

먼저 Redis 서버를 실행시키겠습니다.

<br>

![스크린샷 2021-04-23 오후 3 20 41](https://user-images.githubusercontent.com/45676906/115827645-8b33e780-a447-11eb-9c51-56169a9f06a0.png)

```
docker run redis
```

위의 명령어를 통해서 Redis 서버를 실행시켰습니다. 이번에는 다른 터미널에서 위에서 만든 Dockerfile을 가지고 이미지를 만들겠습니다.

<br>

![스크린샷 2021-04-23 오후 3 24 27](https://user-images.githubusercontent.com/45676906/115828005-101f0100-a448-11eb-937d-9abe46481615.png)

```
docker build -t 이미지이름 ./
ex) docker build -t gyunny ./
```

Dockerfile이 존재하는 같은 위치에서 위의 명령어를 치면 images를 만들게 됩니다.

<br>

![스크린샷 2021-04-23 오후 3 25 05](https://user-images.githubusercontent.com/45676906/115828144-42c8f980-a448-11eb-9baa-25e02347d3fa.png)

그러면 위와 같이 `이미지`가 만들어진 것도 확인할 수 있습니다. 

<br>

![스크린샷 2021-04-23 오후 3 28 22](https://user-images.githubusercontent.com/45676906/115828404-a3f0cd00-a448-11eb-840d-8631cf94d266.png)

```
docker run 이미지이름
ex) docker run gyunny
```

위와 같이 실행하면 에러가 발생하는 것을 볼 수 있습니다.

<br>

### `에러가 발생하는 이유가 무엇일까요?`

<img width="788" alt="스크린샷 2021-04-23 오후 3 29 58" src="https://user-images.githubusercontent.com/45676906/115828520-c387f580-a448-11eb-9847-7ec5acc9b904.png">

에러를 읽어보면 `Redis 연결이 실패`했다고 나옵니다. 이유는 원래 컨테이너간 통신을 할 때 아무런 설정을 해주지 않으면 접근이 가능하지 않기 때문에 에러가 발생하는 것입니다. 

<br>

### `어떻게 컨테이너 사이에 통신을 할 수 있게 할까요?`

CLI 명령어에서 --link와 같은 것들을 이용할 수도 있겠지만, 이러한 `멀티 컨테이너 상황에서 쉽게 네트워크를 연결시켜주기 위해서는 Docker Compose`를 이용하는 것이 좋습니다.

<br>

### `Docker Compose 파일 작성하기`

```
docker-compose.yml 이라는 이름의 파일을 만들겠습니다.
```

![스크린샷 2021-04-23 오후 3 33 27](https://user-images.githubusercontent.com/45676906/115828899-414c0100-a449-11eb-9267-4055e7a64f1d.png)

현재 폴더의 구조는 위와 같습니다. 바로 `docker-compose.yml` 파일을 작성해보겠습니다.

<br>

```yaml
version: "3"              # 도커 컴포즈의 버전
services:                 # 이곳에 실행하려는 컨테이너들을 정의
  redis-server:           # 컨테이너 이름을 지정 (원하는 이름)
    image: "redis"        # 사용할 이미지 이름
  gyunny:                 # 컨테이너 이름을 지정 (원하는 이름)
    build: .              # 현 디렉토리에 있는 Dockerfile을 사용하여 빌드
    ports:
      - "5000:8080"       # 로컬포트:컨테이너포트 (서로 매핑)
```

위와 같이 컴포즈 파일을 작성하겠습니다.

<br>

![스크린샷 2021-04-23 오후 3 38 11](https://user-images.githubusercontent.com/45676906/115830784-bfa9a280-a44b-11eb-9556-e259a8250295.png)

```
docker-compose up         (이미지가 없을 때 이미지를 빌드하고 컨테이너 시작)
docker-compose up --build (이미지가 있든 없든 다시 빌드하고 실행)
docker-compose down       (컴포즈 중지하기)
```

위의 명령어를 통해서 실행하면 위와 같이 에러 없이 서버가 실행되는 것을 볼 수 있습니다. 

<br>

![스크린샷 2021-04-23 오후 3 53 21](https://user-images.githubusercontent.com/45676906/115831097-1ca55880-a44c-11eb-84e8-6b68e2a0661c.png)

그리고 현재 실행 중인 컨테이너 목록을 보면 위와 같이 `Redis`, `Node.JS` 두 개의 컨테이너가 실행되고 있는 것을 볼 수 있습니다.

<br>

![스크린샷 2021-04-23 오후 3 55 13](https://user-images.githubusercontent.com/45676906/115831292-5ece9a00-a44c-11eb-81d1-3582754f10e9.png)

`http://localhost:5000`으로 계속 들어가보면 숫자가 하나씩 증가하는 것을 볼 수 있습니다. 

<br>

## `정리하기`

![스크린샷 2021-04-23 오후 3 34 11](https://user-images.githubusercontent.com/45676906/115829076-7ce6cb00-a449-11eb-94e5-54b82af4d6ac.png)

`Docker Compose`를 사용하면 위와 같이 두 개의 컨테이너를 `Service`를 통해서 연결시켜줘서 컨테이너 간의 통신을 할 수 있게 됩니다.

<br> <br>

## `Docker Compose 다른 장점`

[여기](https://github.com/wjdrbs96/Today-I-Learn/blob/master/DevOps/Docker/%EB%8F%84%EC%BB%A4%20%EA%B8%B0%EB%B3%B8%EB%AA%85%EB%A0%B9%EC%96%B4.md) 에서 도커 기본 명령어를 공부했을 때
띄어쓰기라던지 오타 등등 되게 사소한 것을 실수하면 다시 명렁어를 쳐야하기 때문에 귀찮기도 하고 복잡하기도 한 느낌을 얻었을 것입니다.

그래서 이것을 편하게 해주는 것이 `docker compose` 입니다.

```
docker-compose version
```

![스크린샷 2021-04-23 오후 3 59 41](https://user-images.githubusercontent.com/45676906/115831736-f6cc8380-a44c-11eb-8463-8be22529188e.png)

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

그리고 위에서 작성 했던 명렁을 yml 파일 형식에 맞춰서 위와 같이 작성했습니다. 대략적인 문법의 의미는 아래와 같습니다.

- `volume`: 현재(로컬 또는 리눅스 서버 등)에 존재하는 디렉토리와 컨테이너 내부 디렉터리를 매핑시킵니다. 
- `environment`: 서비스 컨테이너 내부에서 사용할 환경변수를 지정합니다.
- `restart`: 항상 재시작한다는 뜻입니다.

<br>

`docker-compose.yml` 파일과 같은 위치에서 아래의 명령어를 실행해보겠습니다.

```
docker-compose up         (이미지가 없을 때 이미지를 빌드하고 컨테이너 시작)
docker-compose up --build (이미지가 있든 없든 다시 빌드하고 실행)
```

<img width="1131" alt="스크린샷 2020-12-10 오전 2 36 13" src="https://user-images.githubusercontent.com/45676906/101665569-78b8a300-3a90-11eb-9dac-e40cef7c50aa.png">

그러면 위와 같이 `mysql`, `wordpress`가 실행되는 것을 볼 수 있습니다. 그리고 위에서 지정했던 `http://localhost:8000`으로 접속을 해보겠습니다.

![스크린샷 2020-12-10 오전 2 37 11](https://user-images.githubusercontent.com/45676906/101665666-9d147f80-3a90-11eb-8b43-6cf3834c0856.png)

이렇게 워드프레스가 잘 뜨는 것을 확인할 수 있습니다.

<br>

## `정리하기`

도커 명령어를 하나하나 입력하는 것보다는 yml 파일에서 `docker-compose`를 이용해서 사용하는 것이 더 편리하다는 장점이 있습니다.
