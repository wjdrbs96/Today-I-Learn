# Sequelize associations

테이블끼리 관계를 맺을 수 있는데 (ex: `일대일`, `일대다`, `다대일`, `다대다`) 이것을 시퀄라이즈에서는 어떻게 사용하는지 알아보자. 


<img width="1244" alt="스크린샷 2020-11-15 오후 11 57 27" src="https://user-images.githubusercontent.com/45676906/99188248-5176fa80-279e-11eb-87aa-60e4d41265ae.png">


<br>

## `일대일`: 한명의 사용자가 하나의 프로필을 가진다. 

<img width="687" alt="스크린샷 2020-11-16 오전 12 01 40" src="https://user-images.githubusercontent.com/45676906/99188363-e843b700-279e-11eb-88cf-1d08472ed191.png">

<br>

![스크린샷 2020-11-16 오전 12 03 03](https://user-images.githubusercontent.com/45676906/99188405-19bc8280-279f-11eb-8cd2-6c58373858c4.png)

여기서 주의할 점은 `hasOne`과 `belongsTo`가 반대가 되면 안된다. `Profile` 테이블에 `User`테이블의 PK값이 `외래키`로 들어가야 하기 때문에 순서를 지켜야 한다. 
영어를 그대로 해석해봐도 이해할 수 있다. `User - hasOne - Profile`을 해석해보면 User가 Profile 1개를 가진다. 그리고 `Profile - belongsTo - User`는 Profile이 User에 속한다. 라고 해석하면 된다.

<br>

따라서 Profile 테이블이 User 테이블에 속한다라는 의미는 Profile 테이블에 외래키(FK: userIdx)가 들어간다고 생각할 수 있다. 위와 같이 외래키를 지정해주지 않으면 `테이블이름 +id`로 외래키가 지정된다.

<br>

```javascript
db.User.hasMany(db.Comment, { foreignKey: 'commenter', sourceKey: 'id'});
db.Post.belongsTo(db.User, { foreignKey: 'commenter', targetKey: 'id'});
```

`User와 Comment의 관계는 일다다`이다. 한명의 유저는 여러개의 댓글을 쓸 수 있고, 댓글을 쓴 사람은 한명이기 때문이다.
위와 같이 외래키의 이름을 직접 지정할 수 있다. `hasMany가 sourceKey 속성`이고, `belongsTo가 targetKey 속성`을 갖는다. id는 유저 테이블의 PK 값이다.



<br> <br>

## `일대다`: 한명의 사용자는 여러 개의 게시글을 작성할 수 있다.

<img width="687" alt="스크린샷 2020-11-16 오전 12 03 54" src="https://user-images.githubusercontent.com/45676906/99188428-3789e780-279f-11eb-9e62-3b130c638be9.png">

<br>

![스크린샷 2020-11-16 오전 12 00 20](https://user-images.githubusercontent.com/45676906/99188312-baf70900-279e-11eb-928f-c0f8c103e474.png)

한명의 사용자가 여러 개의 게시글을 쓸 수 있기 때문에 `일대다`관계이다. 
`User`테이블의 PK값이 `Post`테이블에 `외래키`로 들어가야 하기 때문에 이번에도 `hasMany`와 `belongsTo`의 순서를 지켜야 한다.

![스크린샷 2020-11-16 오전 12 11 05](https://user-images.githubusercontent.com/45676906/99188631-41f8b100-27a0-11eb-81fc-3a31d2242ac6.png)
 
위와 같이 외래키 이름을 정해주지 않으면 기본 이름인 `테이블이름 + id`로 설정된다.

<br>

## `M:N 관계` 설정하기

<img width="719" alt="스크린샷 2020-11-16 오전 1 33 03" src="https://user-images.githubusercontent.com/45676906/99190737-ac631e80-27ab-11eb-8e9b-3b714f3dd351.png">

지금 상황은 `여러명의 사용자`가 `여러 개의 좋아요`를 누를 수 있기 때문에 `다대다`관계이다. 

<br>

### `관계형 데이터베이스에서 다대다 관계는 2개의 테이블 사이에 매핑 테이블이 필요하다.`

논리적으로 `M:N관계`의 표현은 가능하지만, `2개의 테이블만으로 구현하는 것은 불가능하다.`(생각해보면 당연한 것 같다.) `다대다`관계를 실제로 구현하기 위해서는 각 테이블의 Primary Key를 외래키(FK)로 
참조 하고 있는 연결테이블(매핑테이블)을 사용해야 한다. (`M:N관계가 완전히 해소될 때까지 분리해야 한다.`)

<br>

예를들어, User - User 사이에 팔로잉, 팔로워의 관계도 `다대다`관계이다. 이 때 어떤 사람이 팔로우 하는 사람이 누구인지, 팔로워는 누가 있는지에 대한 정보를 `매핑 테이블에` 담는다. 

<br>

또 다른 예시를 보면서 이해해보자. `N : M 관계`는 두 테이블의 기본키를 컬럼으로 갖는 또 다른 테이블을 생성해서 관리한다.

![스크린샷 2020-11-16 오전 1 26 56](https://user-images.githubusercontent.com/45676906/99190628-e253d300-27aa-11eb-800e-c208cad8b040.png)

- `belongsToMany`를 통해서 `M:N`관계를 설정 한다.
- `through`를 통해서 `매핑 테이블`의 이름을 정할 수 있다. (`매핑테이블`을 만들어 M:N 관계를 분리하는 과정이다. through를 통해서 User 테이블의 PK값과 Post 테이블의 PK값을 가진 Like 테이블이 생성된다.)
- 매핑테이블을 통해서 인덱스로 다대다 관계를 연결시킨다.
- `as`를 통하여 별칭을 지정할 수도 있다. 위의 예에서는 Post는 `Liked(좋아요 받은 게시글)`, User는 `Liker(좋아요 누른 사람)`로 지정하였다. 


![스크린샷 2020-11-20 오전 12 36 05](https://user-images.githubusercontent.com/45676906/99687885-8221a180-2ac8-11eb-9adc-3d8d494b5f7f.png)

그리고 `npm start`를 해보면 위와 같은 관계를 갖는 테이블이 만들어지게 된다.