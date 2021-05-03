# `SQL JOIN` 이란?

![title](https://github.com/MinGOODdev/LearnKit/raw/master/Database/RDB/MySQL/img/MySQL_Join.png)

SQL에서 JOIN은 크게 위와 같이 4개로 나눌 수 있다. 조인이란 어디에 어울리다, 들어가다 라는 뜻을 가지고 있는데 데이터베이스에서는 결합을 의미한다. 
한마디로 RDBMS에서는 `테이블의 결합`을 의미한다. 


<br>

## `그러면 조인을 하는 이유는 무엇일까?`

- ### 관계형 DB를 구축하기 위해서이다. 
- ### 데이터가 중복되는 현상을 막고, 데이터 UPDATE시 변경의 연산이 적어지며 처리 속도 역시 빨라진다. 

<br>

## `데이터의 중복되는 현상을 막고, 변경의 연산이 적다는 것이 무엇인지 알아보자.`

![title1](https://user-images.githubusercontent.com/45676906/99412715-625b7380-2938-11eb-99ef-d3b02e1996be.png)

예를 들어, 위와 같이 `User` 테이블, `Post` 테이블이 있다고 생각해보자. 일반적으로 유저는 여러 개의 게시글을 쓸 수 있고, 게시글의 주인은 한명이기 때문에 `일대다` 관계이다. 

<br>

이 때 위와 같이 데이터를 저장하지 않고 `Post 테이블`에 게시글의 작성자 컬럼도 넣었다고 생각해보자. 그러면 일단 `User 테이블`에도 `userName` 컬럼이 존재하고
`Post 테이블`에도 userName이 존재하게 된다. `그러면 데이터가 중복되어 저장된다.` 

<br>

### `이 때 User 테이블의 이름을 변경한다면 어떻게 될까?`

`Post` 테이블의 게시글 작성자도 유저의 이름이기 때문에 변경이 되어야 할 것이다. 그러면 UPDATE로 변경의 연산이 많아지게 되며, 실수를 한다면 데이터 동기화가 제대로 되지 않아 나중에 문제가 발생할 수도 있다. 

<br>

### `JOIN을 하는 방법`

두 테이블의 JOIN을 할 때는 A 테이블의 PK와 B 테이블의 FK를 연결시켜서 테이블을 합친다. 예를들어 위의 예시에서는 `User 테이블`의 주 키는 id와 `Post 테이블` 외래 키 UserId를 연결시키면 된다. 

```sql
SELECT * FROM User as u JOIN Post as p ON u.id = p.userId
```

위와 같이 쿼리로 작성할 수 있다. (JOIN만 사용하게 되면 INNER가 생략된 조인이라 `INNER JOIN`이다. 한마디로 두 테이블의 `교집합`을 구하는 것이다.)

<br>

## `예제 테이블`

### `users`

| id | email | userName | 
|----|-----|------|
| 1 | Gyunny@naver.com | 규니 |
| 2 | Gyunny@daum.net | 균이 |
| 3 | Gyunny@gmail.com | 규미 |
| 4 | Gyunny@gmail.com | 규미 |

<br>

### `post`

| id | title | contents | userId |
|----|-----|------|--- |
| 1 | 제목1 | 내용1 | 1 |
| 2 | 제목2 | 내용2 | 1 |
| 3 | 제목3 | 내용3 | 2 |
| 4 | 제목3 | 내용3 | 3 |
| 4 | 제목3 | 내용3 | 5 |    

위와 같이 있다고 가정을 하고 예제를 진행해보자. (`위의 userId = 5인 경우는 users 테이블의 없는 인덱스이다. 외래키로 연결되어 있다면 절대로 가능한 상황이 아니지만
예시를 위해서 적어 놓았다.`)

<br>

## `1. INNER JOIN`

INNER JOIN은 테이블 2개의 교집합을 구하는 것이라고 했다. 


### `SQL`

```sql
SELECT * FROM users INNER JOIN post ON users.id = post.userId
```


### `결과`

| id | email | userName | id | title | contents | userId |
|----|-----|------|----|-----|------|--- |
| 1 | Gyunny@naver.com | 규니 | 1 | 제목1 | 내용1 | 1 |
| 1 | Gyunny@naver.com | 규니 | 2 | 제목2 | 내용2 | 1 |
| 2 | Gyunny@daum.net | 균이 | 3 | 제목3 | 내용3 | 2 |
| 3 | Gyunny@gmail.com | 규미 | 4 | 제목3 | 내용3 | 3 |

이와 같이 `교집합`이 결과로 나온 것을 볼 수 있다. 

<br>

## `2. LEFT JOIN`

### `SQL`

```sql
SELECT * FROM users LEFT JOIN post ON user.id = post.userId
```

### `결과`

| id | email | userName | id | title | contents | userId |
|----|-----|------|----|-----|------|--- |
| 1 | Gyunny@naver.com | 규니 | 1 | 제목1 | 내용1 | 1 |
| 1 | Gyunny@naver.com | 규니 | 2 | 제목2 | 내용2 | 1 |
| 2 | Gyunny@daum.net | 균이 | 3 | 제목3 | 내용3 | 2 |
| 3 | Gyunny@gmail.com | 규미 | 4 | 제목3 | 내용3 | 3 |
| 4 | Gyunny@gmail.com | 규미 | NULL | NULL | NULL | NULL |


위와 같이 `NULL`로 값이 채워져 결과가 나오게 된다. LEFT 테이블의 정보만 나오게 되는데 RIGHT 테이블은 NULL로 채워서 나오게 된다.

<br>

## `RIGHR JOIN`

### `SQL`

```sql
SELECT * FROM users RIGHT JOIN post ON users.id = post.userId
```

### `결과`

| id | email | userName | id | title | contents | userId |
|----|-----|------|----|-----|------|--- |
| 1 | Gyunny@naver.com | 규니 | 1 | 제목1 | 내용1 | 1 |
| 1 | Gyunny@naver.com | 규니 | 2 | 제목2 | 내용2 | 1 |
| 2 | Gyunny@daum.net | 균이 | 3 | 제목3 | 내용3 | 2 |
| 3 | Gyunny@gmail.com | 규미 | 4 | 제목3 | 내용3 | 3 |
| NULL | NULL| NULL | 4 | 제목3 | 내용3 | 5 | 

<br>

## `FULL OUTER JOIN`

### `SQL`

```sql
SELECT * FROM users FULL OUTER JOIN posts ON users.id = p.userId
```

하지만 `MySQL`에서는 `FULL OUTER JOIN`을 사용할 수 없기 때문에 아래와 같이 사용해야 한다. 

```sql
SELECT * FROM users u LEFT OUTER JOIN posts pON u.id = p.userId 
UNION
SELECT * FROM users u RIGHT OUTER JOIN posts pON u.id = p.userId;
```

## `결과`

| id | email | userName | id | title | contents | userId |
|----|-----|------|----|-----|------|--- |
| 1 | Gyunny@naver.com | 규니 | 1 | 제목1 | 내용1 | 1 |
| 1 | Gyunny@naver.com | 규니 | 2 | 제목2 | 내용2 | 1 |
| 2 | Gyunny@daum.net | 균이 | 3 | 제목3 | 내용3 | 2 |
| 3 | Gyunny@gmail.com | 규미 | 4 | 제목3 | 내용3 | 3 |
| 4 | Gyunny@gmail.com | 규미 | NULL | NULL | NULL | NULL |
| NULL | NULL| NULL | 4 | 제목3 | 내용3 | 5 | 