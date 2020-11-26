# `dotenv 사용하는 법`

개발을 하다 보면 Cookie Secret Key 등등 비밀스러운 것을 코드 상의 노출시키면 안되는 상황이 반드시 존재한다. 예를들어 `config` 디렉토리 안에 DB 연결 정보를
넣기도 한다. 

<br>

이번에는 `dotenv`를 이용하여 `process` 객체에 넣어서 사용해보려 한다. 

<br>

## `module 설치하기`

```
npm i dotenv
```

기본적인 프로젝트 세팅은 했다고 가정하고 아래와 같이 `.env` 파일을 만들어보자. 

![스크린샷 2020-11-27 오전 12 19 08](https://user-images.githubusercontent.com/45676906/100368069-7729bc80-3046-11eb-8eff-7f3cdaa87cce.png)

모듈의 이름이 `dotenv`인 이유는 `dot(.)` + env로 `dotenv`가 된다. .env 파일에 `키=값` 형식으로 저장하면 된다. 
(.env 파일도 .gitignore에 꼭 저장해서 Github에 올리면 안된다는 것도 알고 있자.)

<br>

## `app.js`

![스크린샷 2020-11-27 오전 12 27 52](https://user-images.githubusercontent.com/45676906/100368858-734a6a00-3047-11eb-8a52-fbeeda160d62.png)

위와 같이 `app.js`에 dotenv를 require 해주면 `.env` 파일에 `키=값` 형태로 저장을 했던 것이 `process.env`에 들어간다. 

<br>

따라서 나는 `.env` 파일에 key=value 라고 저장을 했는데 이러면 `process.env.key`로 사용을 할 수 있고 이것의 결과는 `value`가 나오게 된다. 

<br>

## `dotenv 정리하기`

- 포트 번호를 저장할 수 있다. 
- Cookie Secret Key 등 노출시키지 않아야 하는 것을 관리할 수 있다. 
- `파일 하나에서 관리하고 코드상에 노출시키지 않기 때문에 편리하다.`

