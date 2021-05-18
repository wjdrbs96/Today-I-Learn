# `API Gateway, Lambda로 S3 파일 업로드 하기`

[저번 글](https://devlog-wjdrbs96.tistory.com/328) 에서 `API gateway`를 만들고 해당 API가 호출되었을 때 람다 함수가 호출되는 간단한 예제를 진행해보았습니다. 
이번 글에서는 조금 더 응용해서 API gateway가 호출되었을 때 람다 함수로 S3에 파일 업로드 했을 때 이미지 사이즈를 줄이는 작업도 일어나는 것을 해보겠습니다.([NodeJS Lambda로 이미지 사이즈 줄이기](https://devlog-wjdrbs96.tistory.com/330))

[저번 글](https://devlog-wjdrbs96.tistory.com/328) 도 같이 참고해서 보시는 것을 추천합니다. 바로 IAM 설정부터 진행해보겠습니다. 

<br>

## `IAM 역할 설정`

![스크린샷 2021-05-14 오전 9 25 00](https://user-images.githubusercontent.com/45676906/118203098-4de6d680-b496-11eb-81a6-db8b46b31a67.png)

위의 두 권한을 가진 `역할` 하나를 만들겠습니다. 

<br>

## `Lambda 함수 생성하기`

![스크린샷 2021-05-14 오전 9 53 18](https://user-images.githubusercontent.com/45676906/118204921-47f2f480-b49a-11eb-8788-c57a501c3ce5.png)

![스크린샷 2021-05-14 오전 9 54 09](https://user-images.githubusercontent.com/45676906/118204979-62c56900-b49a-11eb-916a-6bfe642e0194.png)

위에서 만들었던 IAM 역할을 선택하고 NodeJS 기반의 `Lambda 함수`를 생성하겠습니다.

<br>

그리고 이미지 업로드를 할 수 있는 코드를 작성하고 람다 함수에 zip 형태로 올리겠습니다.  

![스크린샷 2021-05-14 오전 10 37 44](https://user-images.githubusercontent.com/45676906/118207812-6c51cf80-b4a0-11eb-8878-163e0fcf40ee.png)

로컬에서 코드를 작성하고 `zip`으로 압축해서 생성한 Lambda 함수에 올릴 것인데요. 제가 작성하고 하는 프로젝트의 구조는 위와 같습니다. 어떻게 작성하는지 하나씩 알아보겠습니다. 

먼저 모듈을 설치해야 해서 아래의 명령어로 먼저 모듈을 설치하겠습니다. 

```
npm install aws-sdk parse-multipart bluebird
```

그러면 `node_modules`, `package-lock.json`이 생길 것입니다. 이제 파일 업로드를 할 수 있는 람다 함수를 만들기 위해서는 `index.js`에 작성해야 합니다. 

<br>

## `index.js`

```js
const AWS = require("aws-sdk");
const multipart = require("parse-multipart");
const s3 = new AWS.S3();
const bluebird = require("bluebird");

exports.handler = function (event, context) {
  const result = [];

  const bodyBuffer = Buffer.from(event["body-json"].toString(), "base64");

  const boundary = multipart.getBoundary(event.params.header["Content-Type"]);

  const parts = multipart.Parse(bodyBuffer, boundary);

  const files = getFiles(parts);

  return bluebird
    .map(files, (file) => {
      console.log(`uploadCall!!!`);
      return upload(file).then(
        (data) => {
          result.push({ data, file_url: file.uploadFile.full_path });
          console.log(`data=> ${JSON.stringify(data, null, 2)}`);
        },
        (err) => {
          console.log(`s3 upload err => ${err}`);
        }
      );
    })
    .then((_) => {
      return context.succeed(result);
    });
};

const upload = function (file) {
  console.log(`putObject call!!!!`);
  return s3.upload(file.params).promise();
};

const getFiles = function (parts) {
  const files = [];
  parts.forEach((part) => {

    const buffer = part.data
    const fileFullName = part.filename;

    const filefullPath = "각자 버킷 URL 적기" + fileFullName;

    const params = {
      Bucket: "S3 Bucket 이름",
      Key: fileFullName,
      Body: buffer,
    };

    const uploadFile = {
      size: buffer.toString("ascii").length,
      type: part.type,
      name: fileFullName,
      full_path: filefullPath,
    };

    files.push({ params, uploadFile });
  });
  return files;
};
```

위의 코드에서 `본인의 버킷 이름`, `각자 버킷 URL`만 적고 `index.js`에 넣으시면 됩니다.

<br>

## `프로젝트 압축하기`

```
zip -r 원하는이름.zip .
ex) zip -r lambda.zip .
```

![스크린샷 2021-05-14 오전 10 45 41](https://user-images.githubusercontent.com/45676906/118208355-92c43a80-b4a1-11eb-9b57-4286997e058d.png)

그러면 위와 같이 zip 파일이 생길 것입니다. 이것은 AWS Lambda 함수에 올리겠습니다.

<br>

![스크린샷 2021-05-14 오전 10 46 44](https://user-images.githubusercontent.com/45676906/118208460-c2734280-b4a1-11eb-9fd7-c14469344819.png)

람다 함수에 들어가면 위와 같이 `zip` 파일을 업로드 할 수 있는데 클릭하겠습니다. 

<br>

![스크린샷 2021-05-14 오전 10 48 32](https://user-images.githubusercontent.com/45676906/118208595-01a19380-b4a2-11eb-9116-a330704bb745.png)

그리고 위에서 압축한 파일을 올리면 위와 같이 `zip` 파일이 업로드가 잘 된 것을 확인할 수 있습니다.

<br>

<br>

## `API Gateway 만들기`

![스크린샷 2021-05-14 오전 9 56 46](https://user-images.githubusercontent.com/45676906/118205183-bfc11f00-b49a-11eb-827c-4887adb6bb80.png)

![스크린샷 2021-05-14 오전 9 57 45](https://user-images.githubusercontent.com/45676906/118205262-e41cfb80-b49a-11eb-83c5-8d47419b72e7.png)

REST API 기반으로 사용할 것이기 때문에 위와 같이 선택하고 만들겠습니다. 

<br>

![스크린샷 2021-05-14 오전 9 58 44](https://user-images.githubusercontent.com/45676906/118205328-0a429b80-b49b-11eb-93d9-1909b222eb0a.png)

그리고 리소스를 생성하겠습니다.

<br>

![스크린샷 2021-05-14 오전 10 30 57](https://user-images.githubusercontent.com/45676906/118207378-80490180-b49f-11eb-8ec5-95c791e70eb3.png)

리소스 이름은 원하는 이름으로 아무거나 정하면 됩니다. 저는 `image`로 하겠습니다.

<br>

![스크린샷 2021-05-14 오전 10 31 47](https://user-images.githubusercontent.com/45676906/118207462-af5f7300-b49f-11eb-9cbc-8c2e2a36ce0c.png)

그리고 메소드 API 메소드를 생성하겠습니다. 

<br>

![스크린샷 2021-05-14 오전 10 34 00](https://user-images.githubusercontent.com/45676906/118207626-07967500-b4a0-11eb-9e2e-57d427f5f2e3.png)

위와 같이 체크하고 `POST` 메소드를 하나 만들겠습니다. 

<br>

그리고 파일 업로드를 하기 위해서는 `API Gateway`에 `이진지원`에서 `multipart/form-data`를 적용해주어야 합니다. 

![스크린샷 2021-05-14 오전 10 50 46](https://user-images.githubusercontent.com/45676906/118208771-52b18780-b4a2-11eb-9b6b-88982e108e2a.png)

이진 미디어를 추가해준 이유는 해당 형식, multipart폼 형식으로 요청을 보낸 파일들을 API Gateway에서 받으면 자체적으로 파일데이터를 이진 바이너리 형식으로 변환하여 처리해준다는 것을 의미합니다.

<br>

![스크린샷 2021-05-14 오전 10 53 28](https://user-images.githubusercontent.com/45676906/118208990-b1770100-b4a2-11eb-99d7-659595798c0e.png)

```
multipart/form-data
```

`이진 미디어 형식`에다 위와 같이 `multipart/form-data`를 넣고 저장하겠습니다. 

<br>

## `S3 정책 추가하기`

![스크린샷 2021-05-18 오후 12 23 55](https://user-images.githubusercontent.com/45676906/118585606-367c5600-b7d4-11eb-9237-a0dc9b7ea550.png)

먼저 잠시 정책을 추가할 때만 `S3을 액세스 할 수 있도록 하겠습니다.`

<br>

![스크린샷 2021-05-18 오후 12 29 42](https://user-images.githubusercontent.com/45676906/118585943-d20dc680-b7d4-11eb-9a81-2a2904007668.png)

위와 같이 잠시 퍼블릭으로 열어놓고 S3 Bucket을 생성한 후에 `권한` 탭으로 들어가겠습니다.

<br>

![스크린샷 2021-05-14 오전 10 57 38](https://user-images.githubusercontent.com/45676906/118209273-4da10800-b4a3-11eb-9627-26da24673492.png)

그리고 아래에 보면 `버킷 정책`이 존재하는데요. 여기서 정책을 하나 생성하겠습니다. 

<br>

![스크린샷 2021-05-14 오전 10 59 18](https://user-images.githubusercontent.com/45676906/118209423-89d46880-b4a3-11eb-874f-47c1e8d5da30.png)

`버킷 ARN`을 복사한 후에 `정책 생성`을 누르겠습니다. 

<br>

![스크린샷 2021-05-14 오전 11 01 11](https://user-images.githubusercontent.com/45676906/118209632-f3ed0d80-b4a3-11eb-9b29-77f53a43a87e.png)

위에서 복사한 S3 ARN 뒤에`/*`을 넣고 위와 같이 작성하겠습니다. 

<br>

![스크린샷 2021-05-14 오전 11 03 18](https://user-images.githubusercontent.com/45676906/118209694-12530900-b4a4-11eb-9995-2bb04bb2f4fc.png)

위에서 선택했던 대로 잘 선택되었는지 확인하고 `Generate Policy`를 누르겠습니다.

<br>

![스크린샷 2021-05-14 오전 11 05 02](https://user-images.githubusercontent.com/45676906/118209819-5219f080-b4a4-11eb-96a0-9655dcfa4629.png)

생성된 정책을 복사하겠습니다.

<br>

![스크린샷 2021-05-14 오전 11 06 26](https://user-images.githubusercontent.com/45676906/118209918-84c3e900-b4a4-11eb-823d-7b627dcef9a8.png)

복사한 정책을 넣고 저장을 누르겠습니다.

<br>

![스크린샷 2021-05-18 오후 12 31 21](https://user-images.githubusercontent.com/45676906/118586047-01bcce80-b7d5-11eb-9cac-075bf72e59e2.png)

그리고 다시 `모든 퍼블릭 엑세스 차단`으로 체크하고 저장하겠습니다.

<br>



<br>

## `API Gateway 설정 마무리`

![스크린샷 2021-05-14 오전 11 08 33](https://user-images.githubusercontent.com/45676906/118210076-da989100-b4a4-11eb-9abe-34078a754622.png)

다시 API Gateway로 들어간 후에 위에서 만든 POST 메소드를 누르면 오른쪽에 `통합 요청`이라고 보이는데 이것을 누르겠습니다.

<br>

![스크린샷 2021-05-14 오전 11 12 17](https://user-images.githubusercontent.com/45676906/118210346-66122200-b4a5-11eb-833e-7a5761841218.png)

그리고 아래에 보면 `매핑 템플릿`이 존재하는데요. 여기서 위와 같이 선택하고 `multipart/form-data`를 넣겠습니다.

```
multipart/form-data
```

<br>

## `API Gateway 배포`

![스크린샷 2021-05-14 오전 11 14 29](https://user-images.githubusercontent.com/45676906/118210599-99ed4780-b4a5-11eb-8a26-e7226ddf1bbd.png)

![스크린샷 2021-05-14 오전 11 15 34](https://user-images.githubusercontent.com/45676906/118210656-b7221600-b4a5-11eb-8530-3a9e325fa38a.png)

그리고 이름은 자유롭게 입력한 후에 배포를 하겠습니다.

<br>

![스크린샷 2021-05-14 오전 11 16 07](https://user-images.githubusercontent.com/45676906/118210756-e3d62d80-b4a5-11eb-80fc-128687aaa771.png)

그러면 위와 같이 `호출 URL`이 생성되었을 것입니다. 호출 URL을 가지고 PostMan에서 파일 업로드를 해보겠습니다.

<br>

## `PostMan 테스트`

![스크린샷 2021-05-14 오전 11 18 51](https://user-images.githubusercontent.com/45676906/118211015-5a732b00-b4a6-11eb-9a46-8eba498b0cee.png)

파일 업로드도 잘 된 것을 확인할 수 있습니다. 이제 S3에 파일이 잘 올라와있는지 확인해보겠습니다. 

<br>

## `S3 Bucket 확인`

![스크린샷 2021-05-14 오전 11 21 13](https://user-images.githubusercontent.com/45676906/118211123-95755e80-b4a6-11eb-93a0-58d9421b9e58.png)

위와 같이 파일이 잘 올라와 있는 것을 확인할 수 있습니다.

그동안은 EC2에 API 만든 코드를 올려서 실행을 시켰습니다.(jar를 실행시키던지, pm2로 node 실행시키던지..?) 그런데 이번 글에서는 API-Gateway, Lambda를 이용해서 Serverless 하게 사진 업로드 하는 API를 구축해보았습니다. 

저는 아직은 EC2에 코드를 올려서 기존의 방식대로 하는 것이 익숙하지만 앞으로 Serverless로 서버를 구축해보면서 익숙해지는 것도 좋을 거 같습니다. 