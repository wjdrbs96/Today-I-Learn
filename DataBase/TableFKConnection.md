## Foreign key로 blogs와 연결된 articles Database 만들기

MySQL workbench를 들어간 후에 `article` 테이블과 `blog` 테이블을 만들어보자. 

```sql
CREATE TABLE article (
    articleIdx INT NOT NULL PRIMARY KEY,
    title VARCHAR (100) NOT NULL,
    articleContent VARCHAR (200) NOT NULL
);
```

```sql
CREATE TABLE blog (
    blogIdx INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    blogTile VARCHAR (100) NOT NULL,
    articleIdx INT NOT NULL
);
```

예를들면 위와 같이 `FK`는 설정하지 않고 테이블을 만들어보자. `DDL`을 사용하지 않고 워크벤치에서 만들어도 상관없다.

<br> 

<img src="https://user-images.githubusercontent.com/45676906/95062345-cedb3400-0737-11eb-8094-d3ad2e808499.png">

<br> 

#### 테이블을 만들고 나면 위와 같은 화면을 볼 수 있다.

<img src="https://user-images.githubusercontent.com/45676906/95059777-532bb800-0734-11eb-8b3b-a411e541e7db.png">

<br>

그리고 위와 같이 `Foreign Key` 탭으로 이동하자. 

<br>

그리고 여기서 `Foreign key` 이름을 적어준 후에 참조 하려는 테이블인 `Reference Table`
을 선택한 후에 `Foreign Key detatils`에서 현재 테이블에서 `외래키`로 설정하고자 하는 컬럼을 선택하고 `apply`하면 된다. 

