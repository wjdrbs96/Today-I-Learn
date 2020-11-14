# [NodeJS] Sequelize 시작하기

![sequelize](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FdMh0wX%2FbtqLcUGTeCN%2FtdVXLlonI0ZT1IuK5aZPb0%2Fimg.png)


노드에서 MySQL 작업을 쉽게 할 수 있도록 도와주는 라이브러리가 있다.(이번 글에서는 MySQL을 사용할 것이다) 바로 `시퀄라이즈(Sequelize)`이다. 
시퀄라이즈는 ORM(Object-relational-Mapping)이다. ORM은 자바스크립트 객체와 데이터베이스의 릴레이션을 매핑해주는 도구이다.

<br>

## Express 프로젝트 생성하기 

```
Express 프로젝트이름
npm install 
npm install sequelize sequelize-cli mysql2
```

위의 명령어를 통해서 NodeJS에 필요한 모듈과 `Sequelize`에 필요한 모듈을 설치하자. 
(나는 sequelize-cli를 설치할 때 권한거부가 떠서 sudo를 붙혀서 설치하니까 되었다)

<br>

그리고 아래의 명령어를 치면 

```
sequelize init
```


![project](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FbDXqtq%2FbtqK7zxat4j%2FWYKJ1D1wYNujayRF5t3G80%2Fimg.png)

그러면 위와 같이 `config`, `models`, `migrations`, `seeders` 폴더가 추가로 생기게 되는데 이번에는 우리는 `models`, `config` 디렉토리만
이용할 것이다. 

<br>

간략하게 말하면 `models` 폴더는 테이블과 대응이 되며 테이블에 관한 설정을 담당하는 곳이고, `config` 폴더는 DB와 연결을 위한 설정파일 이라고 생각하면 될 것 같다.

<br>

### `models/index.js` 코드를 바꿔보자!

```javascript
const Sequelize = require('sequelize');

const env = process.env.NODE_ENV || 'development';         // 개발용 환경 설정
const config = require('../config/config.json')[env];      // Sequelize 설정 파일
const db = {};

// Sequelize 인스턴스화
let sequelize;

if (config.use_env_variable) {
  sequelize = new Sequelize(process.env[config.use_env_variable], config);
} else {
  sequelize = new Sequelize(config.database, config.usename, config.password, config);
}

db.sequelize = sequelize;
db.Sequelize = Sequelize;
```

현재 위의 코드는 config 파일에 설정되어 있는 파일과 연결을 한 후에 sequelize 객체를 만들어서 모듈화한 것이라고 생각하면 좋을 것 같다.

<br>

### `config/config.json` 설정하기

```json
{
  "development": {        // development(개발용) 사용할 것임
    "username": "root",   // DB 접속 아이디
    "password": "root",   // DB 접속 비번
    "database": "nodeJS", // DB 스키마 이름 설정 
    "host": "127.0.0.1",  // RDS를 사용한다면 RDS 엔드포인트를 적기
    "dialect": "mysql"
  },
  "test": {
    "username": "root",
    "password": null,
    "database": "database_test",
    "host": "127.0.0.1",
    "dialect": "mysql"
  },
  "production": {
    "username": "root",
    "password": null,
    "database": "database_production",
    "host": "127.0.0.1",
    "dialect": "mysql"
  }
}
```

본인의 설정에 맞게 위에 `development`에 설정 값을 넣어주면 된다. 

<br>

### app.js도 코드 추가를 해보자! 

![스크린샷 2020-11-15 오전 12 07 49](https://user-images.githubusercontent.com/45676906/99150274-a5b6a780-26d6-11eb-82cf-88122357e226.png)

```javascript
const { sequelize } = require('./models');

sequelize.sync({ alter: false })
.then(() => {
  console.log('데이터베이스 연결 성공');
})
.catch((error) => {
  console.error(error);
})
```

- sequelize.sync({})
    - 테이블이 존재하지 않는 경우 생성한다.(이미 존재하면 아무 작업도 수행하지 않는다.)
- sequelize-sync({ force: true })
    - 테이블이 생성되고 이미 존재하는 경우 먼저 삭제 후 생성
- sequelize-sync({ alter: true })
    - 데이터베이스에 있는 테이블의 상태를 확인한 후 테이블에서 필요한 변경을 수행하여 모델과 일치시킨다. 

그리고 `npm start`를 해보면 콘솔에 `데이터베이스 연결 성공`이라는 로그를 확인하면 지금까지 성공이다.

<br>

### `models/user.js` 만들기

이번에는 `user 테이블`을 만들 것이기 때문에 `models` 디렉토리 아래에 `user.js` 파일을 하나 만들어보자. 

```javascript
module.exports = (sequelize, DataTypes) => {
  return sequelize.define('User', {
    email: {
      type: DataTypes.STRING(30),
      unique: true,
      allowNull: false,
    },
    userName: {
      type: DataTypes.STRING(20),
      allowNull: false,
    },
    password: {
      type: DataTypes.STRING(200),
      allowNull: false,
    },
    salt: {
      type: DataTypes.STRING(200),
      allowNull: false,
    },
  }, {
    freezeTableName: true,
    timestamps: false,
  })
}
```

전체 코드는 위와 같고, 하나씩 어떤 의미인지 알아보자. 

```
sequelize.define('모델이름', { 테이블 설정 }, { 테이블 옵션 });
```

<br>

### 테이블 설정에는 어떤 것이 들어갈까?

테이블을 만들 때 필드는 어떤 것을 넣을 것이며, 필드의 타입은 어떤 것이고, NotNull을 허용할 것인지 아닌지 등등의 설정을 할 수 있다. 이러한 설정들을 정의하는 곳이다. 

<img width="686" alt="스크린샷 2020-11-15 오전 12 20 24" src="https://user-images.githubusercontent.com/45676906/99150498-5c675780-26d8-11eb-8dc8-a4efcbfbf852.png">

MySQL에서의 데이터 타입과 Sequelize에서의 데이터 타입을 비교한 표이다. 

<br>

그래서 테이블 설정 코드를 봐보자.

- ### `type`: 자료형 정의
- ### `allowNull`: NULL이 가능한지 여부(true라면 NULL이 가능, false라면 NULL이 불가능)
- ### `primaryKey`: 기본키 여부 (위의 코드처럼 sequelize 에서 primaryKey를 설정해주지 않으면 자동으로 `id`라는 컬럼의 primaryKey가 생성됨) 
- ### `timestamps`: 생성일을 Sequelize가 자동으로 생성할지 말지를 결정하는 옵션
- ### `FreezeTableName`: 속성값이 true이면 모델명과 DB 테이블 이름을 동일하게 설정해준다. 

<br>

### `models/index.js`를 다시 봐보자. 

![스크린샷 2020-11-15 오전 12 31 19](https://user-images.githubusercontent.com/45676906/99150730-f7146600-26d9-11eb-9bfc-6b48a8121fa2.png)

위와 같이 코드 한 줄을 추가해서 `models/user.js`와 연결을 시키자. 

![스크린샷 2020-11-15 오전 12 34 54](https://user-images.githubusercontent.com/45676906/99150808-743fdb00-26da-11eb-8901-8c453a22a5d4.png)

그리고 `npm start`를 해보면 위와 같이 Sequelize가 테이블을 만들어주는 것을 알 수 있다. (내부적으로 쿼리를 작성해서 만든 것이기 때문에 콘솔에 어떤 쿼리가 적용되었는지 나오는 것을 볼 수 있다.)

<br>

진짜 만들어졌는지 `MySQL Workbench`를 들어가서 본인이 설정한 스키마에서 확인을 해보면 알 수 있다.


