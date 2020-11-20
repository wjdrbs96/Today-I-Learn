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

<br>

그리고 바로 Sequelize 문법을 이용해서 `JOIN`을 해보자. 

```javascript
const posts = await Post.findAll({
   // join 옵션 => include
   // User 테이블과 Post 테이블을 조인
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

Sequelize에서 JOIN을 하려면 `include` 옵션을 사용하면 된다. 위의 코드를 보면 Post.findAll({ include: [{ model: 조인할 테이블 이름}] }) 이런 형식으로 작성했다.

<br>

따라서 `Post` 테이블과 `User` 테이블을 JOIN 하는 것이다. 그리고 `attributes` 옵션을 통해서 원하는 컬럼만 뽑아낼 수 있다.

```sql
SELECT `Post`.`id`, `Post`.`title`, `Post`.`contents`, `Post`.`postImageUrl`, `Post`.`createdAt`, `Post`.`updatedAt`, `Post`.`UserId`, `User`.`id` AS `User.id`, `User`.`userName` AS `User.userName`, `User`.`email` AS `User.email` FROM `Posts` AS `Post` LEFT OUTER JOIN `User` AS `User` ON `Post`.`UserId` = `User`.`id`;
```

위와 같이 특별한 옵션을 주지 않는다면 기본 옵션이 `LEFT OUTER JOIN` 이다. 다른 JOIN을 사용하고 싶다면 아래의 옵션을 사용하면 된다.

- ### required: 'false' => `LEFT OUTER JOIN`
- ### required: 'true' => `INNER JOIN`

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
});
```
```sql
SELECT `Post`.`id`, `Post`.`title`, `Post`.`contents`, `Post`.`postImageUrl`, `Post`.`createdAt`, `Post`.`updatedAt`, `Post`.`UserId`, `User`.`id` AS `User.id`, `User`.`userName` AS `User.userName`, `User`.`email` AS `User.email` FROM `Posts` AS `Post` INNER JOIN `User` AS `User` ON `Post`.`UserId` = `User`.`id`;
```

위와 같이 `required: 'true'` 옵션을 주면 `INNER JOIN`으로 바뀐 것도 볼 수 있다. 

<br>

JOIN을 한 후에 `PostMan`을 통해서 응답 결과를 보면 아래와 같이 나온다. (나의 DB에 저장되어 있는 내용이다.)

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
 
위의 결과를 보면 마지막에 `User` 객체가 그대로 반환된 것을 볼 수 있다. 이렇게 이러한 형식으로 응답을 하고 싶지 않다면 `raw: true` 옵션을 적용하면 된다.

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
SELECT `Post`.`id`, `Post`.`title`, `Post`.`contents`, `Post`.`postImageUrl`, `Post`.`createdAt`, `Post`.`updatedAt`, `Post`.`UserId`, `User`.`id` AS `User.id`, `User`.`userName` AS `User.userName`, `User`.`email` AS `User.email` FROM `Posts` AS `Post` LEFT OUTER JOIN `User` AS `User` ON `Post`.`UserId` = `User`.`id`;
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

이번에는 아까와는 응답의 형식이 다른 것을 볼 수 있다. 

<br>

## `다대다` 관계에서 JOIN 사용하기

```javascript
db.User.belongsToMany(db.Post, { through: 'Likes', as: 'Liked'});  // 좋아요 받은 게시글 + as 옵션을 통해서 Post를 Liked로 별칭
db.Post.belongsToMany(db.User, { through: 'Likes', as: 'Liker'});  // 좋아요 누른 사람  + as 옵션을 통해서 User를 Liker로 별칭
```

위와 같이 `belongsToMany`를 통해서 `다대다` 관계를 설정한다. Post 테이블의 별칭은 `Liked(좋아요 받은 게시글)`, User 테이블의 별칭은 `Liker(좋아요 누른 사람)`으로 정하였다.
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