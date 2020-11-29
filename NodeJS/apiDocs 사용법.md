# `apiDocs 사용하는 법`

nodejs에서 `apiDocs`를 사용하는 법에 대해서 정리해보려 한다. 먼저 express 프로젝트 세팅을 해보자. 

```
express 프로젝트이름
cd 프로젝트이름
npm install 
npm install -g apidoc (전역으로 설치해야 함)
mkdir app (app 디렉토리 생성)
```

<br>

## `app/apidoc.json`

```json
{
  "name": "example",
  "version": "0.1.0",
  "description": "apiDoc basic example",
  "title": "Custom apiDoc browser title",
  "url" : "https://api.github.com/v1"
}
```

위에서 만든 `app` 디렉토리 아래에 `apidoc.json`이라는 파일을 만든 후에 위의 코드를 넣자.

<br>

## `app/example.js`

```js
/**
 * @api {get} /user/:id Request User information
 * @apiName GetUser
 * @apiGroup User
 *
 * @apiParam {Number} id Users unique ID.
 *
 * @apiSuccess {String} firstname Firstname of the User.
 * @apiSuccess {String} lastname  Lastname of the User.
 *
 * @apiSuccessExample Success-Response:
 *     HTTP/1.1 200 OK
 *     {
 *       "firstname": "John",
 *       "lastname": "Doe"
 *     }
 *
 * @apiError UserNotFound The id of the User was not found.
 *
 * @apiErrorExample Error-Response:
 *     HTTP/1.1 404 Not Found
 *     {
 *       "error": "UserNotFound"
 *     }
 */
```

그리고 `app` 디렉토리 아래에 `example.js` 파일도 만들어 위의 코드를 넣자.

<br>

## `문서 생성`

```
apidoc -i app/ -o apidoc/
```

- i 옵션: input이 되는 폴더(example.js가 있는 폴더)
- o 옵션: output이 되는 폴더(apidoc이 자동빌드하여 결과물로 나오는 소스 위치)

![스크린샷 2020-11-30 오전 2 09 19](https://user-images.githubusercontent.com/45676906/100548634-1eaa2780-32b1-11eb-8e87-75193b696506.png)

그러면 위와 같이 `apidoc` 파일이 생겼을 것이다. 

<br>

## `app.js`

![스크린샷 2020-11-30 오전 2 11 33](https://user-images.githubusercontent.com/45676906/100548682-6fba1b80-32b1-11eb-8dca-6ac9a7ee897d.png)

그리고 `app.js`에 위의 코드 한 줄을 추가하자. 

<br>

## `서버 실행`

```
npm start 
```

<img width="1792" alt="스크린샷 2020-11-30 오전 2 13 06" src="https://user-images.githubusercontent.com/45676906/100548714-96785200-32b1-11eb-88a9-4aeb8e3b3480.png">

그리고 `http://localhost:3000`으로 접속하면 위와 같은 화면을 볼 수 있다. 

<br>

## 정리

- 추가적으로 정리 필요함