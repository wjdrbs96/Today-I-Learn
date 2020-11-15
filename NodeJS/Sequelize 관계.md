# Sequelize associations

테이블끼리 관계를 맺을 수 있는데 (ex: `일대일`, `일대다`, `다대일`, `다대다`) 이것을 시퀄라이즈에서는 어떻게 사용하는지 알아보자. 


<img width="1244" alt="스크린샷 2020-11-15 오후 11 57 27" src="https://user-images.githubusercontent.com/45676906/99188248-5176fa80-279e-11eb-87aa-60e4d41265ae.png">

관계에 맞게 사용하면 되는데 어떻게 사용하는 건지 차근차근 알아보자.

<br>

## 한명의 사용자가 하나의 프로필을 가진다. 

<img width="687" alt="스크린샷 2020-11-16 오전 12 01 40" src="https://user-images.githubusercontent.com/45676906/99188363-e843b700-279e-11eb-88cf-1d08472ed191.png">

<br>

![스크린샷 2020-11-16 오전 12 03 03](https://user-images.githubusercontent.com/45676906/99188405-19bc8280-279f-11eb-8cd2-6c58373858c4.png)

여기서 `hasOne`과 `belongsTo`가 반대가 되면 안된다. `Profile` 테이블에 `User`테이블의 PK값이 `외래키`로 들어가야 하기 때문에 순서를 지켜야 한다. 

<br> <br>

## 한명의 사용자는 여러 개의 게시글을 작성할 수 있다.

<img width="687" alt="스크린샷 2020-11-16 오전 12 03 54" src="https://user-images.githubusercontent.com/45676906/99188428-3789e780-279f-11eb-9e62-3b130c638be9.png">

<br>

![스크린샷 2020-11-16 오전 12 00 20](https://user-images.githubusercontent.com/45676906/99188312-baf70900-279e-11eb-928f-c0f8c103e474.png)

한명의 사용자가 여러 개의 게시글을 쓸 수 있기 때문에 `일대다`관계이다. 
`User`테이블의 PK값이 `Post`테이블에 `외래키`로 들어가야 하기 때문에 이번에도 `hasMany`와 `belongsTo`의 순서를 지켜야 한다.

![스크린샷 2020-11-16 오전 12 11 05](https://user-images.githubusercontent.com/45676906/99188631-41f8b100-27a0-11eb-81fc-3a31d2242ac6.png)
 
위와 같이 외래키 이름을 정해주지 않으면 `테이블이름 + id`로 설정된다.

<br>

## `M:N 관계` 설정하기

<img width="719" alt="스크린샷 2020-11-16 오전 1 33 03" src="https://user-images.githubusercontent.com/45676906/99190737-ac631e80-27ab-11eb-8e9b-3b714f3dd351.png">

지금 상황은 `여러명의 사용자`가 `여러 개의 좋아요`를 누를 수 있기 때문에 `다대다`관계이다. 

![스크린샷 2020-11-16 오전 1 26 56](https://user-images.githubusercontent.com/45676906/99190628-e253d300-27aa-11eb-800e-c208cad8b040.png)

그리고 `belongsToMany`를 통해서 `M:N`관계를 설정 할 수 있고, 