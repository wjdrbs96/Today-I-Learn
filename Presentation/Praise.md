# 💙 서비스 한줄 소개 💙

- ### 그거 알아? 칭찬은 하면 할수록 기분이 좋아진다는거? 우리 모두 칭찬 중독이 되어보는거 어때?
- ### [Notion 소개](https://www.notion.so/8cafb7768caa4b8c9cbf8e2e8d8b3361)

<br>

# ✔ 고래단 서버 컨벤션 ✔

- [브랜치 전략](https://github.com/Praise-Whale/Praise_Server/blob/develop/server/info/%EB%B8%8C%EB%9E%9C%EC%B9%98%EC%A0%84%EB%9E%B5%EA%B3%BC%20%ED%92%80%EB%A6%AC%ED%80%98%EC%8A%A4%ED%8A%B8.md)
- [코드 컨벤션](https://github.com/Praise-Whale/Praise_Server/blob/develop/server/info/%EC%BD%94%EB%93%9C%20%EC%BB%A8%EB%B2%A4%EC%85%98.md)
- [커밋 컨벤션](https://github.com/Praise-Whale/Praise_Server/blob/develop/server/info/%EC%BB%A4%EB%B0%8B%EC%BB%A8%EB%B2%A4%EC%85%98.md)

<br>

# 🐋 서비스 명 🐋

- ### `칭찬할고래`

![1](https://user-images.githubusercontent.com/45676906/101926565-b0515780-3c16-11eb-848a-c55b729cabad.png)

<br>

## `models/index.js`

```javascript
db.praise = require('./praise')(sequelize, Sequelize);
db.praiseTarget = require('./praiseTarget')(sequelize, Sequelize);
db.user = require('./user')(sequelize, Sequelize);
db.isPraised = require('./isPraised')(sequelize, Sequelize);
  
/** 1 : 1   Praise : P */
db.praise.hasOne(db.praiseTarget, { onDelete: 'cascade' });
db.praiseTarget.belongsTo(db.praise);

db.user.belongsToMany(db.praise, { through: 'isPraised', as: 'praised' })
db.praise.belongsToMany(db.user, { through: 'isPraised', as: 'praiser'})

db.user.hasMany(db.praiseTarget, { ondDelete: 'cascade' });
db.praiseTarget.belongsTo(db.user, { onDelete: 'cascade' });
```

<br>

## `ERD(Entity Relation Diagram)`

![ERD](https://user-images.githubusercontent.com/68318945/108599728-41db0380-73d6-11eb-9d1c-7523401c8ff7.png)

<br> 

## 서버 아키텍쳐

![server](https://user-images.githubusercontent.com/43840561/104095511-8c973500-52da-11eb-9cea-e8e77deb35f5.png)

<br>

### 사용한 도구

- [Node.js]() - Chrome V8 자바스크립트 엔진으로 빌드된 자바스크립트 런타임
- [Express.js]() - Nodejs 웹 애플리케이션 프레임워크
- [NPM]()  - 자바스크립트 패키지 관리자
- [PM2]() - Express 앱용 프로세스 관리자
- [MySQL]() - DataBase
- [Nginx]() - 웹 서버
- [Sequelize]() - DataBase ORM
- [JWT]() - 토큰기반 인증
- [AWS EC2]() - 클라우드 환경 컴퓨팅 시스템
- [AWS RDS]() - 클라우드 환경 데이터베이스 관리 시스템

<br>

## `Contributor`

- ### [최정균](https://github.com/wjdrbs96)
- ### [최다인](https://github.com/Chedda98)
