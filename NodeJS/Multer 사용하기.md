# `Multer`란?

`Multer`란 NodeJS에서 파일 업로드를 위해 사용되는 `multipart/form-data`를 다루기 위한 `Middleware`이다. 
  
<br>

`multipart/form-data`에 대해 좀 더 알고 싶다면 [여기](https://github.com/wjdrbs96/Today-I-Learn/blob/master/Web/Multipart-Form-Data%EB%9E%80%3F.md) 에서 확인하자. 

<br>

## `NodeJS-Express에서 Multer 사용하기`

```
Express 프로젝트이름
npm install
npm install multer
```

위와 같이 프로젝트 세팅과 모듈 설치를 한 후에 `routes`설정을 해보자. 

<br>

## `routes/index.js`

```javascript
const express = require('express');
const router = express.Router();

router.use('/multer', require('./multer'));

module.exports = router;
```

<br>

## `routes/multer.js`

```javascript
const express = require('express');
const router = express.Router();
const multer = require('multer');
const upload = multer({
  dest: 'upload/'
})

router.post('/single', upload.single('image'), async (req, res) => {  
  const image = req.file.location;
  res.send({
    imageUrl: image,
    file: req.file,
  });
});

module.exports = router;
```

위와 같이 중간에 `multer middleware`를 추가한 후에 `PostMan`으로 테스트 해보자. 

<img width="1296" alt="스크린샷 2020-11-16 오전 2 05 07" src="https://user-images.githubusercontent.com/45676906/99191472-4200ad00-27b0-11eb-83b4-de2fb80a3542.png">

위와 같이 `form-data`를 체크한 후에 `file`을 체크하고 원하는 이미지를 등록한 후에 `Send`를 눌러보자.


![스크린샷 2020-11-16 오전 1 48 50](https://user-images.githubusercontent.com/45676906/99191503-6fe5f180-27b0-11eb-90cf-1bcf74240a83.png)

그리고 프로젝트를 보면 `upload` 폴더가 생기고 그 아래 우리가 보낸 이미지 파일이 생긴 것도 확인할 수 있다. 

<br>

## 이미지 파일 여러개 업로드 하기

```javascript
const express = require('express');
const router = express.Router();
const multer = require('multer');
//const upload = require('../modules/multer');
const upload = multer({
  dest: 'upload/'
})

router.post('/array', upload.array('images', 2), async (req, res) => {
  const image = req.files.location;
  res.send({
    file: req.files
  })
})

module.exports = router;
```

`upload.array('이미지이름', 이미지 개수)`로 middleware를 만들어주면 된다. 그리고 참조해야 할 것은 위와 다르게 `file`이 아니라 `files`이다.

![스크린샷 2020-11-17 오전 12 39 05](https://user-images.githubusercontent.com/45676906/99274179-5a82cd00-286d-11eb-8f9a-faa956b590f5.png)

그리고 위와 같이 `multipart/form-data`형식으로 포스트맨으로 테스트를 해보면 성공하는 것을 볼 수 있다.


<br>

## AWS S3에 파일 업로드를 해보자.

```
npm install multer-s3 aws-sdk
```

<br>

## `config/s3.json`을 만들자. 

```json
{
  "accessKeyId" : "AWS S3 Id 값",
  "secretAccessKey" : "엑세스 비밀 키",
  "region": "ap-northeast-2"
}
```

`s3.json`을 본인이 AWS S3를 설정할 때 발급 받았던 accessKey, secretAccessKey를 위와 같이 적어주자.

<br>

## `modules/multer.js`를 만들어보자.

```javascript
const multer = require('multer');
const multerS3 = require('multer-s3');
const aws = require('aws-sdk');
aws.config.loadFromPath(__dirname + '/../config/s3.json');

const s3 = new aws.S3();
const upload = multer({
    storage: multerS3({
        s3,
        bucket: 'sopt-26th',
        acl: 'public-read',
        key: function(req, file, cb) {
            cb(null, 'images/origin/' + Date.now() + '.' + file.originalname.split('.').pop()); // 이름 설정
        }
    })
});

module.exports = upload;
```

그리고 위와 같이 s3 업로드를 위한 `modules`을 하나 만들어보자. 

<br>

## `routes/multer.js`

```javascript
const express = require('express');
const router = express.Router();
const multer = require('multer');
const upload = require('../modules/multer');

router.post('/single', upload.single('image'), async (req, res) => {  
  const image = req.file.location;
  res.send({
    imageUrl: image,
    file: req.file,
  });
});

module.exports = router;
```

위에서 작성했던 `module`을 가져와서 위와 같이 코드를 작성해보자.

<img width="1296" alt="스크린샷 2020-11-16 오전 2 05 07" src="https://user-images.githubusercontent.com/45676906/99191472-4200ad00-27b0-11eb-83b4-de2fb80a3542.png">

그리고 포스트맨에서 다시 사진을 업로드 해서 `Send`를 누르면 올바르게 응답이 오는 것을 확인할 수 있다.




