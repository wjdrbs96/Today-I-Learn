## `들어가기 전에`

먼저 `Sequelize 관계`에 대해 잘 알지 못한다면 [여기](https://github.com/wjdrbs96/Today-I-Learn/blob/master/NodeJS/Sequelize%20%EA%B4%80%EA%B3%84.md) 를 먼저 읽고 오는 것을 추천한다. 

<br>

## `일대다` 관계에서 JOIN 하는 법을 알아보자. 

<br>

`User`와 `Post`의 관계는 `일대다`관계이다. 왜냐하면 하나의 유저는 여러 개의 게시글을 쓸 수 있고, 게시글을 쓴 사람은 한명이기 때문이다. 

```javascript
// 1 : N  => User : Post 관계
db.User.hasMany(db.Post, { onDelete: 'cascade' });
db.Post.belongsTo(db.User);
```

위와 같이 `hasMany`를 이용해서 `일대다` 관계를 만들었다.

```javascript
const posts = await Post.findAll({
   include: [{
     model: User,  // 작성자
     attributes: ['id', 'userName', 'email' ],
   }],
});
```
```sql
SELECT `Post`.`id`, `Post`.`title`, `Post`.`contents`, `Post`.`postImageUrl`, `Post`.`createdAt`, `Post`.`updatedAt`, `Post`.`UserId`, `User`.`id` AS `User.id`, `User`.`userName` AS `User.userName`, `User`.`email` AS `User.email` 
FROM `Posts` AS `Post` LEFT OUTER JOIN `User` AS `User` ON `Post`.`UserId` = `User`.`id`;
```

Sequelize에서 JOIN을 하려면 `include` 옵션을 사용하면 된다. 그러면 위와 같은 쿼리가 생성된다. 따로 특별한 옵션을 주지 않는다면 `default`는 `LEFT OUTER JOIN`이다.
따라서 아래와 같이 `required` 옵션을 이용해서 `JOIN`을 선택할 수 있다. 

- ### required: 'false' => `LEFT OUTER JOIN`
- ### required: 'true' => `INNER JOIN`


<br>

## `다대다` 관계에서 JOIN 사용하기

```javascript
db.User.belongsToMany(db.Post, { through: 'Likes', as: 'Liked'});  // 좋아요 받은 게시글 + as 옵션을 통해서 Post를 Liked로 별칭
db.Post.belongsToMany(db.User, { through: 'Likes', as: 'Liker'});  // 좋아요 누른 사람  + as 옵션을 통해서 User를 Liker로 별칭
```

`다대다` 관계에서는 `belongsToMany`를 이용해야 한다. 관계형 디비에서는 `다대다` 관계에 있는 테이블을 표현할 수 없기 때문에 중간에 연결을 시켜줄 수 있는 `매핑테이블`이 필요하다.

<br>

위의 코드의 `through: '매핑테이블이름'`을 이용하면 아래처럼 `매핑테이블`이 생긴다. 그러면 시퀄라이즈에서 `User` 테이블의 PK값, `Post` 테이블의 PK값을 담은 `매핑테이블`을 만들어준다.

<br>

Post 테이블의 별칭은 `Liked(좋아요 받은 게시글)`, User 테이블의 별칭은 `Liker(좋아요 누른 사람)`으로 정하였다.
그리고 `through`를 통하여 `User` 테이블과 `Post` 테이블의 `매핑 테이블`인 `Likes` 테이블을 만들었다. (User 테이블의 PK, Post 테이블의 PK를 가진다.)

![스크린샷 2020-11-20 오전 12 36 05](https://user-images.githubusercontent.com/45676906/99687885-8221a180-2ac8-11eb-9adc-3d8d494b5f7f.png)


<br>

```javascript
const posts = await Post.findAll({
    include: [{
       model: User,  // 작성자
       attributes: ['id', 'userName', 'email' ],
    }, {
      model: User,  // 좋아요 누른 사람
      as: 'Liker', // 별칭 사용(index.js에서 만든 것)
      through: { attributes: [] }   // through를 통해서 생성된 테이블에만 적용됨
    }],
});
```

이번에는 테이블 2개를 이용해서 JOIN을 하는 것이 아니라 3개 이상으로 JOIN을 해보려 한다. 그러면 위와 같이 작성하면 된다.
위의 코드를 그냥 해석해보면 `그러면 Post 테이블과 User 테이블을 JOIN`을 하고 그 다음에 또 Post 테이블과 User 테이블을 JOIN 한다. 

<br>

## 똑같은 JOIN을 두 번 하라는 건가? 근데 두 번째는 as로 별칭이 붙어있네?

위에서 JOIN 할 때 사용한 `as: 'Liker'`의 별칭은 `index.js`에서 설정한 별칭을 사용하는 것이다.

```javascript
db.Post.belongsToMany(db.User, { through: 'Likes', as: 'Liker'}); 
```

<br>

이게 Sequelize가 매우 헷갈리면서도 중요한 부분이다. 왜 헷갈리는지는 쿼리를 작성했을 때와 비교해서 확인해보자. 

```sql
# 게시글을 작성한 유저
SELECT users.userId FROM post JOIN users ON post.postId = users.userId; 

# 좋아요를 받은 게시글
SELECT post.postId FROM post JOIN likes ON post.postId = likes.postId; 

# 게시글에 좋아요를 누른 유저
SELECT users.userId FROM users JOIN likes ON users.userId = likes.userId;
```

보통 쿼리에서 `게시글에 좋아요를 누른 유저`를 찾기 위해서는 위와 같이 `users` 테이블과 `likes` 테이블을 JOIN 하면 된다.

<br>

### 하지만 Sequelize에서는 `as로 만든 별칭으로 JOIN`을 하면 자동으로 `다대다 관계`를 찾아서 JOIN을 시켜준다.

```
{
   model: User,  
   as: 'Liker', 
   through: { attributes: [] }  
}
```

한마디로, model에 쿼리처럼 likes를 적고 JOIN을 하는 것이 아니라 User를 적는 것이 헷갈렸다. 그런데 위와 같이 `다대다 관계`에서 별칭을 만들었던
것으로 JOIN을 하면 `Sequelize 에서 내부적으로 판단을 해서 likes 테이블과 JOIN을 해주는 것이다.` 

<br>

그리고 `through` 옵션은 무엇인가 하면 위에서 through를 통해서 `다대다 관계`의 `매핑 테이블`을 만들었다. 이 때 매핑 테이블에 적용할 수 있는 옵션이다.  

<br>

따라서 그냥 시퀄라이즈의 문법?이라 생각하고 받아들이면 될 것 같다. 그래서 코드를 실행한 후에 시퀄라이즈가 만든 쿼리를 보면 아래와 같다.

```sql
SELECT `Post`.`id`, `Post`.`title`, `Post`.`contents`, `Post`.`postImageUrl`, `Post`.`createdAt`, `Post`.`updatedAt`, `Post`.`UserId`, `User`.`id` AS `User.id`, `User`.`userName` AS `User.userName`, `User`.`email` AS `User.email`, `Liker`.`id` AS `Liker.id`, `Liker`.`email` AS `Liker.email`, `Liker`.`userName` AS `Liker.userName`, `Liker`.`password` AS `Liker.password`, `Liker`.`salt` AS `Liker.salt` 
FROM `Posts` AS `Post` 
LEFT OUTER JOIN `User` AS `User` ON `Post`.`UserId` = `User`.`id`  # 처음에 User와 Post 테이블의 JOIN
LEFT OUTER JOIN ( `Likes` AS `Liker->Likes` INNER JOIN `User` AS `Liker` ON `Liker`.`id` = `Liker->Likes`.`UserId`) 
ON `Post`.`id` = `Liker->Likes`.`PostId`;
```

<br>

## PostMan 응답 결과

```json
{
    "status": 200,
    "message": "조회 성공",
    "data": [
        {
            "id": 1,
            "title": "제목",
            "contents": "내용",
            "createdAt": "2020-11-20T04:11:37.000Z",
            "updatedAt": "2020-11-20T04:11:37.000Z",
            "UserId": 1,
            "User": {
                "id": 1,
                "userName": "test",
                "email": "test@@naver.com"
            },
            "Liker": [
                {
                    "id": 1,
                    "email": "test@@naver.com",
                    "userName": "test"
                }
            ]
        }
    ]
}
```

<br>

## 추가 기능

```json
{
    "status": 200,
    "message": "조회 성공",
    "data": [
        {
            "id": 1,
            "title": "제목",
            "contents": "내용",
            "postImageUrl": null,
            "createdAt": "2020-11-20T04:11:37.000Z",
            "updatedAt": "2020-11-20T04:11:37.000Z",
            "UserId": 1,
            "User": {
                "id": 1,
                "userName": "test",
                "email": "test@@naver.com"
            }
        }
    ]
}
```
 
JOIN 해서 반환되는 형태를 보면 위의 같이 마지막에 `User` 객체가 그대로 반환된 것을 볼 수 있다. 이렇게 객체의 형식으로 데이터 구조를 만들기 싶지 않다면 `raw: true` 옵션을 적용하면 된다.

<br>

```javascript
const posts = await Post.findAll({
   // join 옵션 => include
   // User 테이블과 Post 테이블을 조인
   include: [{
     model: User,  // 작성자
     attributes: ['id', 'userName', 'email' ],
     required: 'true',
   }, {
     model: User,  // 좋아요 누른 사람
     as: 'Liker', // 별칭 사용(index.js에서 만든 것)
     through: { attributes: [] }   // through를 통해서 생성된 테이블에만 적용됨
   }],
   raw: true,
});
```
```sql
SELECT `Post`.`id`, `Post`.`title`, `Post`.`contents`, `Post`.`postImageUrl`, `Post`.`createdAt`, `Post`.`updatedAt`, `Post`.`UserId`, `User`.`id` AS `User.id`, `User`.`userName` AS `User.userName`, `User`.`email` AS `User.email` 
FROM `Posts` AS `Post` LEFT OUTER JOIN `User` AS `User` ON `Post`.`UserId` = `User`.`id`;
```
 
위와 같이 `raw: true`를 적용했을 때, 쿼리는 위와 같고 응답의 결과는 어떻게 나오는지 확인해보자.


```json
{
    "status": 200,
    "message": "조회 성공",
    "data": [
        {
            "id": 1,
            "title": "제목",
            "contents": "내용",
            "postImageUrl": null,
            "createdAt": "2020-11-20T04:11:37.000Z",
            "updatedAt": "2020-11-20T04:11:37.000Z",
            "UserId": 1,
            "User.id": 1,
            "User.userName": "test",
            "User.email": "test@@naver.com"
        }
    ]
}
```

이번에는 데이터 구조와 User 안에 데이터들이 있는 것이 아니라 위와 같이 바뀐 것을 볼 수 있다. 