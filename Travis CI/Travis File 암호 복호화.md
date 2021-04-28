# `Travis CI에서 File 암호화 복호화 하는 법`

Github에 push를 하면 Github이 Travis CI로 Hook을 날려서 CI가 진행이 됩니다. 진행을 할 때 로그를 보면 위와 같이 `Git Project`를 clone 받아서 진행하는 것을 볼 수 있습니다. 

<img width="947" alt="스크린샷 2021-04-28 오후 2 21 50" src="https://user-images.githubusercontent.com/45676906/116350617-315b6500-a82d-11eb-9755-8272bdde8772.png">

그러면 여기서 의문점이 생겼습니다. `.gitignore` 파일에 등록된 파일들은 어떻게 다운 받지? 라는 생각을 했습니다. 그래서 이번 글에서는 그 방법에 대해서 알아보겠습니다. 

<br>

## `Spring Boot 프로젝트 만들기`

먼저 간단한 Spring Boot gradle 프로젝트를 하나 만들겠습니다. 

![스크린샷 2021-04-28 오후 2 30 01](https://user-images.githubusercontent.com/45676906/116351265-57353980-a82e-11eb-93ce-931d80140d57.png)

그리고 위와 같이 `application.properties -> application.yml`로 바꾼 후에 `.gitignore`에 등록하겠습니다. 그리고 간단하게 `AWS RDS`를 연결하는 세팅을 해보겠습니다.
(RDS가 없다면 그냥 진행하셔도 될 거 같습니다. 파일이 암호화, 복호화가 잘 되는지를 중점적으로 보시면 됩니다.)

<br>

## `build.gradle`

```yaml
compile 'org.springframework.boot:spring-boot-starter-jdbc'
compile 'mysql:mysql-connector-java'
```

build.gradle에 `jdbc`, `mysql` 의존성을 추가하겠습니다.

<br>

### `application.yml`

```yaml
spring:
  datasource:
    url: jdbc:mysql://엔드포인트/스키마이름?allowPublicKeyRetrieval=true&autoReconnect=true&useSSL=false&useUnicode=true&characterEncoding=utf8&mysqlEncoding=utf8&serverTimezone=UTC
    username: 유저이름
    password: 비밀번호
    driver-class-name: com.mysql.cj.jdbc.Driver
```

위와 같은 형태로 설정 값에 맞게 작성하겠습니다.

<br>

## `Travis CI 설정`

<img width="1043" alt="스크린샷 2021-04-28 오후 2 41 16" src="https://user-images.githubusercontent.com/45676906/116352296-e8f17680-a82f-11eb-91de-15b2e8d50208.png">

[https://travis-ci.org/](https://travis-ci.org/) 여기에 들어간 후에 위와 같이 사용할 Repository에 버튼 활성화를 해주겠습니다.

<br>

![스크린샷 2021-04-28 오후 2 46 29](https://user-images.githubusercontent.com/45676906/116352753-a2504c00-a830-11eb-8a5e-67090f355d71.png)

그리고 위와 같이 Github에 `Webhooks`가 등록되어 있는지까지 확인하겠습니다. 혹시라도 등록이 안되어 있다면 [여기](https://devlog-wjdrbs96.tistory.com/315) 를 참고하시면 됩니다.

<br>

## `.travis.yml 작성`

![스크린샷 2021-04-28 오후 2 51 36](https://user-images.githubusercontent.com/45676906/116353185-4a661500-a831-11eb-8786-f88c20d4bda2.png)

<br>

```yaml
language: java
jdk:
  - openjdk11

brances:
  only:
    - master   # master 브랜치에 push 가 되었을 때 Travis CI 반응

cache:
  directories:
    - '$HOME/.m2/repository'
    - '$HOME/.gradle'

script:
  - "./gradlew clean build"   # jar 만들기

notification:  # 이메일 알림
  email: 
    recipients:
      - wjdrbs966@naver.com
```

위와 같이 간단하게 `.travis.yml` 파일에 작성을 하겠습니다. 그냥 정말 간단하게 `빌드`만 수행하도록 작성하였습니다.  

먼저 일단 `application.yml`이 `.gitignore`가 되어 있는 상태에서 정말 에러가 나는지 확인을 먼저 해보겠습니다. 

<br>

![스크린샷 2021-04-28 오후 2 58 01](https://user-images.githubusercontent.com/45676906/116353702-3c64c400-a832-11eb-9bf0-09ff5df0d709.png)

<br>

![스크린샷 2021-04-28 오후 2 58 18](https://user-images.githubusercontent.com/45676906/116353738-47b7ef80-a832-11eb-853a-4da2eb872f51.png)

그러면 위와 같이 `./gradlew clean build` 명령을 수행할 때 에러가 발생했다는 것을 볼 수 있습니다. 즉, 설정파일이 존재하지 않기 때문에 에러가 난 것입니다. 

<br>

그래서 지금부터 설정 파일을 `암호화`, `복호화`하는 것을 진행해보겠습니다.

<br>

## `Secret 파일 압축`

```
tar -cvf 원하는이름.tar 암호화할 파일 이름
ex) tar -cvf gyunny.tar application.yml
``` 


![스크린샷 2021-04-28 오후 3 05 41](https://user-images.githubusercontent.com/45676906/116354396-4fc45f00-a833-11eb-851d-01884e52b970.png)

위와 같이 입력하면 tar 파일이 하나 생겼을 것입니다. 

<br>

![스크린샷 2021-04-28 오후 3 28 10](https://user-images.githubusercontent.com/45676906/116356555-6324f980-a836-11eb-9eab-c1a2029d4823.png)

<br>

## `Secret 파일 암호화`

```
gem install travis  // travis 설치
travis login        // travis login
```

![스크린샷 2021-04-28 오후 3 10 56](https://user-images.githubusercontent.com/45676906/116355000-2f48d480-a834-11eb-8116-062dcd5af8bd.png)

그리고 `Travis CI`와 연결되어 있는 Github 계정을 입력하면 된다 해서 입력을 했는데 저는 되지 않습니다.. (이것 때매 삽질을 꽤나 한거 같습니다.)

그래서 그냥 Github Token을 사용해서 로그인을 하겠습니다. [https://github.com/settings/tokens](https://github.com/settings/tokens) 로 들어가겠습니다.

<br>

![스크린샷 2021-04-28 오후 3 17 39](https://user-images.githubusercontent.com/45676906/116355559-fa894d00-a834-11eb-90a6-3b219a65a760.png)

위의 정도만 체크를 한 후에 토큰을 만들겠습니다. 그리고 `토큰을 복사`하겠습니다. 

<br>

```
travis login --github-token Github_TOKEN
```

![스크린샷 2021-04-28 오후 3 25 14](https://user-images.githubusercontent.com/45676906/116356365-248f3f00-a836-11eb-950e-9bb46637d2cd.png)

위와 같이 입력하며 로그인이 되었다는 표시를 볼 수 있습니다. 

<br>

```
travis encrypt-file 파일이름.tar --add
ex) travis encrypt-file gyunny.tar --add
```

![스크린샷 2021-04-28 오후 3 31 13](https://user-images.githubusercontent.com/45676906/116356922-dcbce780-a836-11eb-8df2-b4c37b544989.png)

그러면 위와 같이 뭐가 뜨면서 암호화가 된 것을 볼 수 있습니다. 

<br>

![스크린샷 2021-04-28 오후 3 34 13](https://user-images.githubusercontent.com/45676906/116357262-43da9c00-a837-11eb-8f4c-d6f9e164175e.png)

그리고 위와 같이 `.travis.yml`에도 자동으로 어떤 것이 추가된 것을 볼 수 있습니다.

<br>

![스크린샷 2021-04-28 오후 3 45 52](https://user-images.githubusercontent.com/45676906/116358671-dcbde700-a838-11eb-8aad-144bd21ab7a0.png)

또한 위와 같이 travis Repository Setting을 보면 자동으로 추가된 것을 볼 수 있습니다.  

<br>

![스크린샷 2021-04-28 오후 3 35 50](https://user-images.githubusercontent.com/45676906/116357458-74223a80-a837-11eb-9fe6-c2d8f727676a.png)

<br>

## `.gitignore에 .tar 파일 추가`

![스크린샷 2021-04-28 오후 3 37 44](https://user-images.githubusercontent.com/45676906/116357718-c4010180-a837-11eb-9897-040e785230b0.png)

암호화를 하면 위와 같이 `gyunny.tar`, `gyunny.tar.enc` 파일이 생긴 것을 볼 수 있습니다. 이 중에서 `.tar` 파일은 올라가면 안되기 때문에 `.gitignore`에 등록하겠습니다.

<br>

![스크린샷 2021-04-28 오후 3 39 43](https://user-images.githubusercontent.com/45676906/116357953-12160500-a838-11eb-9ff4-9d68c61a2abd.png)

암호화를 했을 때 위와 같은 .tar 파일은 절대 올리지 말고, .tar.enc 파일은 올리라고 하는 것을 볼 수 있습니다. 

<br>

![스크린샷 2021-04-28 오후 3 42 34](https://user-images.githubusercontent.com/45676906/116358256-67eaad00-a838-11eb-9688-581092b64660.png)

`.gitignore`에 등록하면 위와 같이 `.tar` 파일은 제외가 된 것을 확인할 수 있습니다.

<br>

![스크린샷 2021-04-28 오후 3 49 12](https://user-images.githubusercontent.com/45676906/116359542-df6d0c00-a839-11eb-8329-31ee3d35b2ee.png)

그리고 Github에 push를 해보니 위와 같이 파일을 찾을 수 없다는 에러가 났습니다.. 그래서 `gyunny.tar.env` 파일을 루트경로로 옮겨야 하나 해서 아래와 같이 옮겼습니다.

<br>

![스크린샷 2021-04-28 오후 4 44 19](https://user-images.githubusercontent.com/45676906/116366394-362a1400-a841-11eb-9867-76e630685a0f.png)

기존에 `./src/main/resources/` 경로에 존재하던 파일을 위와 같이 프로젝트 루트 경로로 옮겼습니다.

<br>

![스크린샷 2021-04-28 오후 4 47 32](https://user-images.githubusercontent.com/45676906/116366655-7ab5af80-a841-11eb-87cd-562872bd3e7b.png)

```
tar xvf 파일이름.tar
ex) tar xvf gyunny.tar
```

암호화 했던 파일을 Travis CI에서 복호화 하는 코드를 `.travis.yml`에 적어주겠습니다.

<br>

![스크린샷 2021-04-28 오후 4 44 05](https://user-images.githubusercontent.com/45676906/116366788-9e78f580-a841-11eb-9661-bb5f8577ca3a.png)

그러면 위와 같이 `빌드`에 성공한 것을 볼 수 있습니다. 
