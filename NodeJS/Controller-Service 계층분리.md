# `NodeJS-Express`에서 `Controller`계층과 `Service`계층 분리하기

| 계층(Layer) | 설명 |
|---------|----------------------------|
| MODEL | 모델의 스키마 정의 및 데이터 접근 |
| SERVICE | 비즈니스 로직을 작성 |
| Controller | 요청을 받고 Service 계층에서 받은 데이터로 응답 |
| ROUTER | URI(또는 경로) 및 특정한 HTTP 요청 메소드(GET, POST 등)인 특정 엔드포인트에 대한 클라이언트 요청에 애플리케이션이 응답하는 방법을 결정하는 것을 말한다 |

<br>

## `UserController.js`

```javascript
const responseMessage = require('../modules/responseMessage');
const statusCode = require('../modules/statusCode');
const util = require('../modules/util');
const { User } = require('../models');
const crypto = require('crypto');

module.exports = {
  signup: async (req, res) => {
    const { email, password, userName } = req.body;
  
    if (!email || !password || !userName) {
      console.log('필요한 값이 없습니다');
      return res.status(statusCode.BAD_REQUEST).send(util.fail(statusCode.BAD_REQUEST, responseMessage.NULL_VALUE));
    }
  
    try {
      const alreadyEmail = await User.findOne({
        where: {
          email: email,
        }
      });
  
      if (alreadyEmail) {
        console.log('이미 존재하는 이메일 입니다');
        return res.status(statusCode.BAD_REQUEST).send(util.fail(statusCode.BAD_REQUEST, responseMessage.ALREADY_ID));
      }
  
      const salt = crypto.randomBytes(64).toString('base64');
      const hashPassword = crypto.pbkdf2Sync(password, salt, 10000, 64, 'sha512').toString('base64');
  
      const user = await User.create({
        email: email,
        password: hashPassword,
        userName: userName,
        salt: salt,
      })
  
      console.log(user);
      return res.status(statusCode.OK).send(util.success(statusCode.OK, responseMessage.SIGN_IN_SUCCESS, { id: user.id, email, userName }));
    } catch (err) {
      console.error(err);
      return res.status(statusCode.INTERNAL_SERVER_ERROR).send(util.fail(statusCode.INTERNAL_SERVER_ERROR, responseMessage.SIGN_IN_FAIL));
    }
  }
}
```

위와 같이 `회원가입`을 하는 코드가 있다. 위의 코드를 보면 `비즈니스 로직(Business Logic)`이랑 섞여 있어서 코드를 유지보수 하는데 좋지 않다. 

<br>

### `Business Logic 1번`

```javascript
const alreadyEmail = await User.findOne({
  where: {
     email: email,
   }
});
```

### `Business Logic 2번`

```javascript
const salt = crypto.randomBytes(64).toString('base64');
const hashPassword = crypto.pbkdf2Sync(password, salt, 10000, 64, 'sha512').toString('base64');
  
const user = await User.create({
  email: email,
  password: hashPassword,
  userName: userName,
  salt: salt,
})
```

위의 Controller 코드에는 2가지의 비즈니스 로직의 코드가 섞여있다. 1번은 `이메일 유효 검사`, 2번은 비밀번호 암호화의 작업을 한 후에 INSERT를 하는 코드이다. 

<br>

따라서 2개의 코드를 `Service`계층으로 분리를 시켜보려 한다. 

<br>

## `userService.js`

```javascript
const crypto = require('crypto');
const { User } = require('../models');

module.exports = {
  readOneEmail: async ( email ) => {
    try {
      const user = await User.findOne({
        where: {
          email
        }
      });
    } catch (err) {
      throw err;
    }
  },

  signup: async (email, userName, password) => {
    try {
      const salt = crypto.randomBytes(64).toString('base64');
      const hashedPassword = crypto.pbkdf2Sync(password, salt, 10000, 64, 'sha512').toString('base64');
      const user = await User.create({
        email,
        password: hashedPassword,
        userName,
        salt
      });
      return user;
    } catch (err) {
      throw err;
    }
  }
}
```

위와 같이 비즈니스 로직을 모듈화 시켜서 넣는다. 

<br>

## `userController.js`

```javascript
const responseMessage = require('../modules/responseMessage');
const statusCode = require('../modules/statusCode');
const util = require('../modules/util');
const { User } = require('../models');
const { userService } = require('../service');

module.exports = {
  signup: async (req, res) => {
    const { email, password, userName } = req.body;
  
    if (!email || !password || !userName) {
      console.log('필요한 값이 없습니다');
      return res.status(statusCode.BAD_REQUEST).send(util.fail(statusCode.BAD_REQUEST, responseMessage.NULL_VALUE));
    }
  
    try {
      const alreadyEmail = await userService.readOneEmail(email);
  
      if (alreadyEmail) {
        console.log('이미 존재하는 이메일 입니다');
        return res.status(statusCode.BAD_REQUEST).send(util.fail(statusCode.BAD_REQUEST, responseMessage.ALREADY_ID));
      }
  
      const salt = crypto.randomBytes(64).toString('base64');
      const hashPassword = crypto.pbkdf2Sync(password, salt, 10000, 64, 'sha512').toString('base64');
  
      const user = await userService.signup(email, userName, password);
  
      return res.status(statusCode.OK).send(util.success(statusCode.OK, responseMessage.SIGN_IN_SUCCESS, { id: user.id, email, userName }));
    } catch (err) {
      console.error(err);
      return res.status(statusCode.INTERNAL_SERVER_ERROR).send(util.fail(statusCode.INTERNAL_SERVER_ERROR, responseMessage.SIGN_IN_FAIL));
    }
  },
}
```

위와 같이 `Controller`의 계층과 `Service`의 계층을 분리시켜서 좀 더 효과적으로 코드를 관리할 수 있다. 

