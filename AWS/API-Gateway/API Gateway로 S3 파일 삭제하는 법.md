# `API Gateway로 파일 삭제하는 법`

이번 글에서는 API Gateway로 DELETE 기반의 REST API를 만들어 파일 삭제하는 법에 대해서 알아보겠습니다. 

![스크린샷 2021-05-24 오전 9 09 04](https://user-images.githubusercontent.com/45676906/119281240-edaf1c00-bc6f-11eb-8149-7316912eeb2f.png)

먼저 람다 함수를 하나 만들겠습니다. 

<br>

![스크린샷 2021-05-24 오전 9 13 14](https://user-images.githubusercontent.com/45676906/119281340-58f8ee00-bc70-11eb-94da-16c4adb43b05.png)

그리고 API Gateway로 이동해서 `DELETE` 메소드를 만들고 위에서 만든 람다 함수와 연결시키겠습니다. (API Gateway를 만드는 법은 [여기](https://devlog-wjdrbs96.tistory.com/328) 를 참고해주세요.)
또한 이번 API는 Query String에다 파일 이름을 넘기면 해당 파일을 삭제하도록 만들것인데요. 그래서 Query String에 대한 설정도 필요합니다. 해당 설정은 [여기](https://devlog-wjdrbs96.tistory.com/332) 를 참고해주세요.

<br>

<br>

## `Lambda Function Code`

```js
const AWS = require("aws-sdk");
const s3 = new AWS.S3();

exports.handler = function (event, context) {
  const { filename } = event;
  console.log(filename);
  
  var params = {
    Bucket: "nodejs-resize-bucket",
    Key: `${filename}`
   };
   
   s3.deleteObject(params, function(err, data) {
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

![스크린샷 2021-05-24 오전 9 30 04](https://user-images.githubusercontent.com/45676906/119281941-a1191000-bc72-11eb-9415-dc1e8f0cf0fa.png)

그리고 람다 함수에 위에서 압축한 것을 올리겠습니다. 

```
zip -r lambda.zip .
```

<br>

## `API 배포`

![스크린샷 2021-05-24 오전 9 58 34](https://user-images.githubusercontent.com/45676906/119283024-ac6e3a80-bc76-11eb-960f-080c32a3905e.png)

그리고 위와 같이 해당 코드가 실행될 수 있도록 API 배포를 하겠습니다.

<br>

![스크린샷 2021-05-24 오전 10 00 22](https://user-images.githubusercontent.com/45676906/119283122-01aa4c00-bc77-11eb-8f9d-543e49535e76.png)

S3에 `nodejs-resize-bucket` 버켓 이름에 /images/origin 디렉토리 아래의 `spring.jpeg` 파일을 삭제해보겠습니다. 

<br>

## `PostMan 테스트`

![스크린샷 2021-05-24 오전 10 03 51](https://user-images.githubusercontent.com/45676906/119283336-a62c8e00-bc77-11eb-85db-1981c41e2527.png)

그리고 PostMan으로 파일 삭제하는 API Gateway 메소드를 호출해보겠습니다.

<br>

![스크린샷 2021-05-24 오전 10 06 59](https://user-images.githubusercontent.com/45676906/119283401-dd9b3a80-bc77-11eb-8173-03e5f204512a.png)


<br> <br>

## `Reference`

- [https://docs.aws.amazon.com/ko_kr/sdk-for-javascript/v2/developer-guide/s3-example-creating-buckets.html](https://docs.aws.amazon.com/ko_kr/sdk-for-javascript/v2/developer-guide/s3-example-creating-buckets.html)
- [https://docs.aws.amazon.com/AWSJavaScriptSDK/latest/AWS/S3.html#deleteObject-property](https://docs.aws.amazon.com/AWSJavaScriptSDK/latest/AWS/S3.html#deleteObject-property)

