# `API Gateway로 S3 파일 조회하는 API 만들기`

`API Gateway`에 대한 기본적인 세팅은 되어 있다고 가정하고 진행하겠습니다. (안되어 있다면 [여기](https://devlog-wjdrbs96.tistory.com/328) 를 보고 오시면 될 것 같습니다.)
이번 글에서는 `S3 파일 조회`하는 API를 만들 것이기 때문에 `GET` 메소드를 생성하면 됩니다.

```
http://localhost:8080?directory=name
```

위와 같이 `Query String` 형태로 조회할 것이기 때문에 `API Gateway`에서 쿼리 스트링을 사용할 수 있게 설정을 해주어야 합니다. 그 설정은 [여기](https://devlog-wjdrbs96.tistory.com/332) 를 참고하고 아래의 글을 보시면 됩니다.

<br>

## `Lambda 함수(NodeJS) 코드`

```
npm install aws-sdk
```

위의 모듈을 먼저 설치하겠습니다.

<br>

<img width="287" alt="스크린샷 2021-05-19 오전 11 14 15" src="https://user-images.githubusercontent.com/45676906/118746516-5cb4fb00-b893-11eb-902e-8667c9483337.png">

그리고 위와 같은 파일 구조로 `index.js`에 아래의 코드를 넣겠습니다.

<br>

```js
const AWS = require("aws-sdk");
const s3 = new AWS.S3();

exports.handler = function (event, context) {
  const { location } = event;  // Query String 파싱
  
  var params = {
    Bucket: "nodejs-resize-bucket",  // S3 버킷 이름
    Prefix: `images/${location}`,    // S3 내부에 버킷 디렉토리
    MaxKeys: 2
   };
   
   s3.listObjects(params, function(err, data) {  // listObjects 가 S3 버킷 내부의 객체들을 읽어오는 메소드
     if (err) {
       console.log(err, err.stack); // an error occurred
     }
     else {
      console.log(data);
      return context.succeed(data); // successful response
     }           
   });
};
```

![스크린샷 2021-05-19 오전 11 16 11](https://user-images.githubusercontent.com/45676906/118746779-cc2aea80-b893-11eb-810c-ccb63c69ceb7.png)

포스트맨에서 테스트 하면 위와 같이 S3 버킷 내부에 존재하는 파일들을 읽어오는 것을 볼 수 있습니다. (쿼리스트링에 S3 내부에 존재하는 디렉토리 이름으로 검색하면 됩니다.)
