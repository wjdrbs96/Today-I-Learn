# `NodeJS, Lambda로 Image Resize 하는 법`

[저번 글](https://devlog-wjdrbs96.tistory.com/325) 에서 `Lambda로 Thumbnail Image`를 생성하는 법에 대해서 정리를 해보았는데요. 저번 글에서는 `Lambda` 함수를 `Python`을 사용했습니다.

이번 글에서는 `NodeJS`를 사용하여 `Lambda` 함수를 만들고 `Image Resize`를 하는 것에 대해서 정리해보겠습니다.

<br>

![스크린샷 2021-05-13 오후 1 47 41](https://user-images.githubusercontent.com/45676906/118078762-c9db1300-b3f1-11eb-91a0-66c904171ac5.png)

이번 글에서 해보고자 하는 아키텍쳐는 위와 같습니다. 즉 사용될 도구는 아래와 같은데요.

- `JavaScript, NodeJS, Express`
- `AWS S3, Lambda`

하나씩 어떻게 설정해서 어떻게 진행하는지에 대해서 정리해보겠습니다.

<br>

## `AWS Cli 설치하기`

```
brew install awscli
```

![스크린샷 2021-05-13 오후 1 53 39](https://user-images.githubusercontent.com/45676906/118079157-a06eb700-b3f2-11eb-96b2-142288f89df3.png)

<br>

그리고 `IAM 사용자`의 `엑세스 키`, `비밀 엑세스 키`를 등록을 해야 하는데요. IAM 사용자를 만들고 엑세스 키를 발급받는 것은 되어 있다 가정하고 진행하겠습니다. 
자세한 것은 [여기](https://docs.aws.amazon.com/ko_kr/cli/latest/userguide/cli-configure-quickstart.html) 를 참고하시면 됩니다.

<br>

![스크린샷 2021-05-13 오후 1 57 52](https://user-images.githubusercontent.com/45676906/118079539-576b3280-b3f3-11eb-8839-592115438f12.png)

```
aws configure
AWS Access Key ID: IAM 엑세스 키 입력
AWS Secret Access Key ID: IAM 비밀 엑세스 키 입력
Default region name: ap-northeast-2
Default output format: json
```

위와 같이 `aws configure`를 통해서 `IAM 사용자 엑세스 키`, `비밀 엑세스 키`, `리전`을 미리 등록해놓겠습니다.

<br>

## `IAM 설정하기`

![스크린샷 2021-05-13 오후 2 14 27](https://user-images.githubusercontent.com/45676906/118081090-a6b26280-b3f5-11eb-81d7-bd49be6e3301.png)

`AWS Lambda` 서비스를 사용하기 위해서는 해당 사용자에게 Lambda 접근 권한을 주어야 합니다. 그래서 위와 같이 `사용자 -> 권한 추가`를 누르고 `AWSLambda_FullAccess`를 추가하겠습니다. 그러면 위와 같이 추가가 된 화면을 볼 수 있습니다.
그리고 사진 업로드도 할 것이기 때문에 `S3FullAccess` 권한이 없다면 같이 추가해주시면 됩니다.

위에서는 사용자에게 람다를 사용할 수 있게 권한을 부여했다면 지금부터는 우리가 올릴 lambda Function 개체에 권한을 부여하겠습니다. 

<br>

## `IAM 역할 생성하기`

![스크린샷 2021-05-13 오후 2 19 26](https://user-images.githubusercontent.com/45676906/118081420-4a9c0e00-b3f6-11eb-86b7-fa6540e8e33b.png)

![스크린샷 2021-05-13 오후 2 20 37](https://user-images.githubusercontent.com/45676906/118081491-6b646380-b3f6-11eb-8c86-21ea39a31008.png)

![스크린샷 2021-05-13 오후 4 18 36](https://user-images.githubusercontent.com/45676906/118092310-e33a8a00-b406-11eb-9178-d65e479c7e8d.png)

`S3FullAccess`, `AWSLambdaBasicExecutionRole`을 선택하겠습니다.(`LambdaBasicExecutionRole` 은 AWS 서비스 및 리소스에 액세스 할 수 있는 권한을 부여합니다.)
이런 권한이 있어야 `Lambda` 함수가 제대로 작동할 수 있습니다.

<br>

![스크린샷 2021-05-13 오후 2 22 31](https://user-images.githubusercontent.com/45676906/118081624-b2525900-b3f6-11eb-92f6-bdf153a51c75.png)


그리고 역할 이름을 정하고 역할을 만들겠습니다. 

<br>

<br>

## `Lambda 함수 코드`

![스크린샷 2021-05-13 오후 2 27 45](https://user-images.githubusercontent.com/45676906/118082015-6ce25b80-b3f7-11eb-9025-8fb55ae3c9ab.png)

위와 같이 `index.js` 파일을 하나 만든 후에 아래의 코드를 추가하겠습니다.

```js
const sharp = require("sharp");
const aws = require("aws-sdk");
const s3 = new aws.S3();
 
const Bucket = "serverless-gyunny-bucket";
const transforms = [
  { name: "w_200", width: 200 },
  { name: "w_400", width: 400 }
];
 
exports.handler = async (event, context, callback) => {
  const key = event.Records[0].s3.object.key;
  const sanitizedKey = key.replace(/\+/g, " ");
  const parts = sanitizedKey.split("/");
  const filename = parts[parts.length - 1];
 
  try {
    const image = await s3.getObject({ Bucket, Key: sanitizedKey }).promise();
 
    await Promise.all(
      transforms.map(async item => {
        const resizedImg = await sharp(image.Body)
          .resize({ width: item.width })
          .toBuffer();
        return await s3
          .putObject({
            Bucket,
            Body: resizedImg,
            Key: `images/${item.name}/${filename}`
          })
          .promise();
      })
    );
    callback(null, `Success: ${filename}`);
  } catch (err) {
    callback(`Error resizing files: ${err}`);
  }
};
```

![스크린샷 2021-05-13 오후 2 30 44](https://user-images.githubusercontent.com/45676906/118082354-fe51cd80-b3f7-11eb-9a96-8fa223ef1153.png)

코드에서 볼 부분은 표시한 부분인데요. 사진 파일을 저장할 S3 Bucket 이름을 지정하고 이미지를 몇 사이즈로 줄일지를 정하는 코드가 있습니다. 좀 더 자세히 보고 싶다면 [Github](https://github.com/wjdrbs96/NodeJS_Image-Resize) 에서 확인할 수 있습니다.

<br>

```js
const trasforms = [
    { name: "w_200", width: 200 },  // w_200 디렉토리에 200x200 사이즈로 이미지 리사이징
    { name: "w_400", width: 400 }   // w_400 디렉토리에 400x400 사이즈로 이미지 리사이징
]
```

코드의 의미는 위처럼 `200x200`, `400x400`의 Resize 파일이 저장되도록 하였습니다. 

<br>

```
npm install --arch=x64 --platform=linux sharp
```

위의 명령어를 통해서 모듈을 설치하겠습니다.

![스크린샷 2021-05-13 오후 2 37 02](https://user-images.githubusercontent.com/45676906/118082765-b7180c80-b3f8-11eb-8e9c-43eb9e72747b.png)

![스크린샷 2021-05-13 오후 2 38 37](https://user-images.githubusercontent.com/45676906/118082906-fb0b1180-b3f8-11eb-92fd-8135f493859e.png)

그러면 위와 같이 모듈 관련 파일들이 생겼을 것입니다. 이제 전체 프로젝트를 zip으로 압축하겠습니다.

<br>

```
zip -r 원하는이름.zip 압축할폴더이름
ex) zip -r function.zip .
```

위의 명령어를 치면 `function.zip`으로 프로젝트가 압축이 됩니다.

![스크린샷 2021-05-13 오후 4 12 33](https://user-images.githubusercontent.com/45676906/118091668-0fa1d680-b406-11eb-8314-68bb8cc8bac4.png)

<br>

그리고 아까 위에서 `LambdaBasicExecutionRole` 권한을 주었던 역할로 다시 가보겠습니다.

![스크린샷 2021-05-13 오후 2 43 38](https://user-images.githubusercontent.com/45676906/118083352-b59b1400-b3f9-11eb-9da6-dae19c2cfd64.png)

들어가면 위처럼 `역할 ARN`이 존재합니다. 이것을 복사하겠습니다.

<br>

```
aws lambda create-function --function-name imageResizing \
--zip-file fileb://function.zip --handler index.handler --runtime nodejs12.x \ --role 각자역할ARM 넣기
```

![스크린샷 2021-05-13 오후 2 47 55](https://user-images.githubusercontent.com/45676906/118083753-69040880-b3fa-11eb-8317-c2d3bf5c1931.png)

![스크린샷 2021-05-13 오후 2 50 08](https://user-images.githubusercontent.com/45676906/118083831-99e43d80-b3fa-11eb-96db-e00f7f0fab08.png)

그리고 `AWS Lambda`로 가보면 위와 같이 CLI에서 지정했던 `Lambda Function`이 생성된 것을 확인할 수 있습니다.

<br>

## `S3 이벤트 트리거 설정`

![스크린샷 2021-05-13 오후 2 52 12](https://user-images.githubusercontent.com/45676906/118084011-e3cd2380-b3fa-11eb-92b5-624aa3ac5ead.png)

![스크린샷 2021-05-13 오후 2 54 00](https://user-images.githubusercontent.com/45676906/118084159-30186380-b3fb-11eb-9dc8-724f51d5f420.png)

![스크린샷 2021-05-13 오후 3 35 46](https://user-images.githubusercontent.com/45676906/118087723-fb0f0f80-b400-11eb-891c-30a2d1beb54e.png)

위와 같이 S3 Bucket 내부 `/images/origin` 경로에 파일이 저장되면 위에서 지정한 트리거가 발생해서 람다 함수가 작동할 것입니다.

<br>

![스크린샷 2021-05-13 오후 3 40 19](https://user-images.githubusercontent.com/45676906/118088234-b3d54e80-b401-11eb-883a-a1afa1207b3e.png)

그리고 `Lambda` 함수를 들어가보면 위와 같이 `S3 Trigger`가 추가되어 있는 것을 볼 수 있습니다.

<br> 

## `S3 파일 업로드 해보기`

![스크린샷 2021-05-13 오후 3 49 42](https://user-images.githubusercontent.com/45676906/118089069-ecc1f300-b402-11eb-8d47-20d79f7572e8.png)

위와 같이 `/images/origin` 의 디렉토리를 만든 후에 업로드를 S3에서 직접 해보겠습니다.

<br>

![스크린샷 2021-05-13 오후 4 09 56](https://user-images.githubusercontent.com/45676906/118092680-61972c00-b407-11eb-824f-9a03a4fc89d1.png)

`429.4KB` 크기의 사진 파일을 업로드 하였습니다.

<br>

![스크린샷 2021-05-13 오후 4 23 23](https://user-images.githubusercontent.com/45676906/118092906-aae77b80-b407-11eb-958a-c525dcdddfb2.png)

그리고 `/images`에 경로를 보면 위와 같이 `w_200`, `w_400`의 디렉토리가 자동으로 생긴 것을 볼 수 있습니다.(람다 함수 코드에서 지정한 이름대로 생성된 것도 확인할 수 있습니다.)

<br>

![스크린샷 2021-05-13 오후 4 25 15](https://user-images.githubusercontent.com/45676906/118093057-d9655680-b407-11eb-8359-0c37c9ad8e0d.png)

일단 `200x200`으로 줄인 사진의 크기는 `429.4KB`-> `7.6KB`로 줄어든 것을 볼 수 있습니다. 

<br>

![스크린샷 2021-05-13 오후 4 26 26](https://user-images.githubusercontent.com/45676906/118093145-00238d00-b408-11eb-8a96-be54be73baf4.png)

그리고 `400x400`으로 줄인 사이즈는 `429.4KB` -> `32.4KB`로 줄어든 것도 확인할 수 있습니다. 

<br>

이렇게 이번 글에서 `NodeJS`로 `Lambda 함수`를 생성해서 이미지 사이즈를 줄이는 것을 해보았습니다. 확실히 사이즈가 많이 줄어든 것을 보니 이미지가 많고 썸네일을 필요로 하는 화면에서 사용하면 여러므로 도움이 많이 될 거 같습니다.