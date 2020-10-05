## SQL - 테이블 생성

* ### 테이블 생성 : CREATE TABLE 문

```
CREATE TABLE 테이블이름 (
    속성 데이터타입 NOT NULL DEFAULT 값
    PRIMARY KEY (속성리스트)
    UNIQUE (속성리스트)
    FOREIGN KEY (속성리스트) REFERENCES 테이블이름(속성리스트)
``` 

<br>

`과목` 테이블을 만든다고 가정하면 아래와 같다.

```sql
CREATE TABLE course (
    studentId varchar(45) NOT NULL,
    courseName varchar(45) NOT NULL,
    date DATE NULL DEFAULT '미정',
    grade int NOT NULL,
    phoneNumber varchar(14) NULL,
    department varchar(20) NULL,
    PRIMARY KEY (studentId)
);
```

* `NOT NULL` : 속성이 널 값을 허용하지 않음을 의미하는 키워드
* `DEFAULT` : 속성의 기본 값을 지정하는 키워드


<br>

## 키의 정의
### - `PRIMARY KEY`
- 기본키를 지정하는 키워드


### - UNIQUE
- 대체키를 지정하는 키워드
- 대체키로 지정되는 속성의 값은 유일성을 가지며 기본키와 달리 널 값이 허용됨


### - `FOREIGN KEY`
- 외래키를 지정하는 키워드
- 외래키가 어떤 테이블의 무슨 속성을 참조하는지 REFERENCES 키워드 다음에 제시
- 참조 무결성 제약조건 유지를 위해 참조되는 테이블에서 투플 삭제 및 변경 시 처리 방법을 지정하는 옵션
    - `ON DELETE NO ACTION`: 투플을 삭제하지 못하게 함
    - `ON DELETE CASCADE`: 관련 투플을 함께 삭제함
    - `ON DELETE SET NULL`: 관련 투플의 외래키 값을 NULL로 변경함
    - `ON DELETE SET DEFAULT`: 관련 투플의 외래키 값을 미리 지정한 기본 값으로 변경함
    - `ON UPDATE NO ACTION`: 투플을 변경하지 못하게 함
    - `ON UPDATE CASCADE` : 관련 투플에서 외래키 값을 함께 변경함
    - `ON UPDATE SET NULL` : 관련 투플의 외래키 값을 NULL로 변경함
    - `ON UPDATE SET DEFAULT` : 관련 투플의 외래키 값을 미리 지정한 기본 값으로 변경함
    - 예) FOREIGN KEY(소속부서) REFERENCES 부서(부서번호) ON DELETE CASCADE ON UPDATE CASCADE
    
    
<br>

### SQL - 예제

```
주문 테이블은 주문번호, 주문고객, 주문제품, 수량, 배송지, 주문일자 속성으로 구성되고, 주문번호 속성이 기본키다.
주문고객 속성이 고객 테이블의 고객아이디 속성을 참조하는 외래키이고, 주문제품 속성이 제품 테이블의 제품번호 속성을 참조하는 외래키가 되도록 주문
테이블을 생성해보자.
```

```sql
CREATE TABLE 주문 (
    주문번호 CHAR (3) NOT NULL,
    주문고객 VARCHAR(20),
    주문제품 CHAR (3),
    수량 INT,
    배송지 VARCHAR (30),
    주문일자 DATE,
    PRIMARY KEY (주문번호),
    FOREIGN KEY (주문고객) REFERENCES 고객(고객아이디)
)
```

