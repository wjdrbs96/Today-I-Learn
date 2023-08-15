## `CHAR, VARCHAR, TEXT 차이는 무엇일까?`

문자열을 사용한다면 `CHAR`, `VARCHAR` 타입 중에 고민할 것이다. 항상 정확하게는 모르고 대략적으로만 `VARCHAR`가 더 효율적이다 이렇게 막연하게만 알고 있었고, 어떤 기준으로 선택해야 하는지는 정확히 잘 모르고 있어서 이번 기회에 정리해보려 한다.

- 버전마다 차이점이 있을텐데, 그 점에 대해 자세하게 고려하지 못하고 전반적인 특징으로 정리한 점을 고려해주세요!

<br>

### `CHAR, VARCHAR, TEXT 특징`

|     | CHAR                                                             | VARCHAR                                                                                                                                                                                                                      | TEXT                                                              |
|-----|------------------------------------------------------------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|-------------------------------------------------------------------|
| 공통점 | 문자열을 저장할 수 있는 데이터 타입                                             | 문자열을 저장할 수 있는 데이터 타입                                                                                                                                                                                                         | 문자열을 저장할 수 있는 데이터 타입                                              |
| 특징  | CHAR 타입은 이미 저장 공간의 크기가 고정적이라서 실제 저장된 값의 크기가 얼마인지 별도로 저장할 필요가 없다. | VARCHAR 타입은 가변 길이로 최대로 저장할 수 있는 값의 길이는 제한되어 있지만 그 이하 크기의 값이 저장되면 그만큼 저장 공간이 줄어든다. VARCHAR 타입은 실제 저장된 유효 크기가 얼마인지 알아야 하기 때문에 1 ~ 2 바이트의 추가 저장 공간이 필요하다. (VARCHAR 타입의 길이가 255바이트 이하면 1바이트만 사용하고, 256바이트 이상으로 설정되면 2바이트를 사용한다.) | 최대 길이가 크거나, 해당 테이블의 컬럼이 많거나, 자주 사용되지 않는 컬럼이라면 TEXT 사용해도 괜찮을 것 같다. |
| 차이점 | 고정 길이                                                            | 가변 길이                                                                                                                                                                                                                        | 대용량                                                               |

<br>

### `CHAR, VARCHAR 중에 어떤 것을 사용하면 좋을까?`

문자열 값의 길이가 항상 일정하다면 `CHAR` 타입을 사용하고, 가변적이라면 `VARCHAR` 타입을 사용하는 것이 일반적이다.

- 저장되는 문자열의 길이가 대게 비슷한가?
- 컬럼의 값이 자주 변경되는가?

하지만 좀 더 정확히 말하면 위의 2가지를 고려하여 데이터 타입을 결정하는 것이 필요하다.

<br>

```sql
CREATE TABLE tb_test (
    fd1 INT NOT NULL,
    fd2 CHAR(10) NOT NULL,
    fd3 DATETIME NOT NULL
)
```

<img width="1459" alt="스크린샷 2023-08-15 오후 9 39 05" src="https://github.com/wjdrbs96/Today-I-Learn/assets/45676906/62121b57-693c-4628-8248-be7109e0e3d1">

```sql
INSERT INTO tb_test (fd1, fd2, fd3) VALUES (1, 'ABCD', '2023-08-15 21:40:11')
```

- `fd1`: INTEGER 타입은 고정길이 4바이트 사용
- `fd2`: 4바이트만 채워져 있고, 나머지 6바이트는 공백 문자로 채워진 것을 볼 수 있음
- `fd3`: DATETIME 이므로 고정 길이로 8바이트를 사용

<br>

```sql
CREATE TABLE tb_test (
    fd1 INT NOT NULL,
    fd2 VARCHAR(10) NOT NULL,
    fd3 DATETIME NOT NULL
)
```

<img width="1128" alt="스크린샷 2023-08-15 오후 9 46 53" src="https://github.com/wjdrbs96/Today-I-Learn/assets/45676906/ba70ab37-f767-47fd-b207-6fdaaeb780ef">

- `fd1`: INTEGER 타입은 고정길이 4바이트 사용
- `fd2`: 첫 번째 바이트에 저장된 컬럼 값의 유효한 바이트 숫자가 저장되고, 두 번째 바이트부터 다섯 번째 바이트 까지 실제 컬럼값이 저장된다.
- `fd3`: DATETIME 이므로 고정 길이로 8바이트를 사용

<br>

## `VARCHAR 타입 fd2 컬럼의 값을 업데이트 했다면 어떤 일이 발생할까?`

`CHAR` 타입이라면 정해진 공간 안에서 변경되는 컬럼의 값만 업데이트 하면 된다.

하지만 `VARCHAR 라면 어떻게 업데이트 하게 될까?`

위의 그림을 보면 알 수 있듯이 `VARCHAR(10)` 이어도, 4바이트의 글자가 저장되었다면 위처럼 fd2에 4바이트밖에 저장할 수 없는 구조로 만들어져 있다.

<img width="1251" alt="스크린샷 2023-08-15 오후 9 56 07" src="https://github.com/wjdrbs96/Today-I-Learn/assets/45676906/ecb26818-0250-462b-90cf-dd47fe10b4e9">

<br>

### `정리하기`

- 주민번호 같이 고정되어 있는 값이라면 무조건 `CHAR` 타입을 사용하는 것이 좋고, 2 ~ 3 바이트 정도의 차이로 문자열 차이가 나는 수준이라면 CHAR 타입을 쓰는 것이 더 좋을 것 같다.
- 저장되는 문자열의 길이가 예측이 안된다면 VARCHAR를 사용해야 겠지만, 해당 값이 자주 바뀐다면 바뀔 때마다 레코드의 이동이 일어나기 때문에 이 점은 단점이 될 것 같다. 

<br> <br>

## `TEXT 타입이란?`

TEXT 타입에 대해서 알아보기 전에, 먼저 MySQL 레코드 사이즈 제한 및 일부 특징들에 대해서 먼저 알아보려 한다.

<br>

### `들어가기 전에`

```
MySQL에서는 하나의 레코드에서 TEXT와 Blob 타입을 제외한 컬럼의 전체 크기가 64KB를 초과할 수 없다.

테이블에서 VARCHAR 타입의 컬럼 하나만 있다면 이 VARCHAR 타입은 최대 64KB 크기의 데이터를 저장할 수 있다. 

하지만 이미 다른 컬럼에서 40KB의 크기를 사용하고 있다면 VARCHAR 타입은 24KB만 사용할 수 있다.

VARCHAR 16383이 최대인 이유는 모두 4바이트의 글자가 저장된다고 가정했을 때, 16383 x 4 = 65,532 이기 때문이다.

(64KB = 65,532 바이트 이다.)
```

<br>

### `MySQL 레코드 사이즈 제한`

```sql
CREATE TABLE tb_long_varchar (id INT PRIMARY KEY, fd1 VARCHAR(1000000));
```
```
0 row(s) affected, 1 warning(s): 1246 Converting column 'fd1' from VARCHAR to TEXT	0.147 sec
```

`16383` 제한을 넘어서는 숫자면 `Warning` 로그와 함께 `VARCHAR -> MEDIUMTEXT`로 변환해서 테이블을 생성한다.

<br>

```sql
CREATE TABLE tb_long_varchar (id INT PRIMARY KEY, fd1 VARCHAR(16383));
```
```
Error Code: 1118. Row size too large. The maximum row size for the used table type, not counting BLOBs, is 65535. This includes storage overhead, check the manual. You have to change some columns to TEXT or BLOBs	0.057 sec
```

16383 제한에 걸리도록 설정하여 생성하는 경우는 `VARCHAR -> MEDIUMTEXT`로 변환되지 않고 에러가 발생한다.

<br>

```sql
CREATE TABLE tb_long_varchar (id INT PRIMARY KEY, fd VARCHAR(16382));
```

제한 보다 낮게 설정하면 위처럼 테이블 생성하면 테이블이 정상적으로 생성된다.

```sql
ALTER TABLE tb_long_varchar ADD fd2 VARCHAR(10);
```

```
Error Code: 1118. Row size too large. The maximum row size for the used table type, not counting BLOBs, is 65535. This includes storage overhead, check the manual. You have to change some columns to TEXT or BLOBs	0.013 sec
```

하지만 위처럼 `ALTER`를 진행 했을 때는 위와 같은 에러가 발생한다. 

`에러가 발생하는 이유는 65,528 + 4(INT) = 65,532 바이트(64KB) 이기 때문에, 하나의 컬럼에 최대 64KB 제한을 넘을 수 없기 때문에 에러가 발생하는 것이다.`

위와 같은 이유로 MySQL 서버에서는 레코드 사이즈 한계로 인해서, VARCHAR 타입의 최대 저장 길이 설정시에 공간을 아껴서 설정해야 한다.

> 참고로 TEXT나 BLOB와 같은 LOB 컬럼은 이 제한 사항에 거의 영향을 미치지 않는다. 그래서 많은 컬럼을 가진 테이블에서는 VARCHAR 타입 대신 TEXT 타입을 사용해야 할 수도 있다. 

<br>

## `Inline 저장소와 Off-Page 저장소`

MySQL 서버도 레코드의 컬럼 데이터는 B-Tree (Clustering Index)에 저장(이를 `Inline` 저장이라고 함)하지만, 용량이 큰 LOB 데이터는 B-Tree 외부의 `Off-Page` 페이지에 저장한다.

하지만 MySQL 서버는 LOB 타입의 컬럼을 항상 Off-Page로 저장하지는 않고, 길이가 길어서 저장 공간이 많이 필요한 경우에만 Off-Page로 저장한다.

```sql
INSERT INTO tb_lob VALUES (1, REPEAT('A',8100));  -- // Inline 저장소
INSERT INTO tb_lob VALUES (2, REPEAT('A',8101));  -- // Off-Page 저장소
```

예를 들어서 아래와 같이 2개의 레코드가 있을 때, 1번 레코드(id=1)의 fd 컬럼에 8,100 글자(8,100바이트)를 저장하면 Off-Page가 아닌 B-Tree(Clustering Index)에 Inline으로 저장한다.

하지만 8101 바이트 부터는 `Off-Page`에 저장한다.

<br>

### `Inline 저장소`

```
- 데이터 레코드 내부에 직접적으로 저장되는 저장 방식을 의미 
- 데이터 레코드의 열(column) 값들이 해당 레코드에 인라인으로 저장되는 것을 말함
```

- 장점
  - 작은 크기의 데이터 저장에 효율적입니다. 
  - 쿼리 실행 시, 데이터를 읽는 속도가 빠릅니다. 
- 단점: 큰 데이터 값이나 긴 문자열과 같이 데이터 크기가 큰 경우에는 성능이 저하될 수 있습니다.

### `Off-Page 저장소`

```
- Off-Page 저장소는 데이터 레코드의 내부에 직접 저장되지 않고, 외부의 저장소 영역에 저장되는 방식
- 이렇게 함으로써 레코드의 크기를 작게 유지하고, 대량의 데이터나 긴 문자열과 같은 데이터를 효율적으로 저장할 수 있음
```

- 장점:
  - 대량의 데이터를 효율적으로 저장할 수 있다. 
  - 인라인 저장소의 크기 증가에 따른 부담을 줄일 수 있다.
- 단점:
  - 데이터를 읽거나 쿼리를 실행할 때 추가적인 디스크 I/O 작업이 필요하다.
  - 인라인 저장소에 비해 약간의 성능 저하가 발생할 수 있다.

<br>

## `VARCHAR와 TEXT의 메모리 활용`

[MySQL 코드 참고 Link](https://github.com/mysql/mysql-server/blob/mysql-cluster-8.0.33/sql/table.h#L1438)를 보면 `uchar *record[2]` 값이 존재한다. 

MySQL 엔진과 InnoDB 스토리지 엔진은 uchar* records[2] 메모리 포인터를 이용해서 레코드 데이터를 주고 받는다고 한다. (그냥 그렇다고 받아들였다.)

그래서 VARCHAR 타입은 최대 크기가 설정되기 때문에 메모리 공간을 records[2] 버퍼에 미리 할당받아둘 수 있지만, TEXT나 BLOB와 같은 LOB 컬럼 데이터의 경우 실제 최대 크기만큼 메모리를 할당해 두면 메모리 낭비가 너무 심해지는 문제가 있어서 요청이 올 때 메모리 할당을 하고 해제한다고 한다.

즉, TEXT, BLOB 같은 경우는 요청이 빈번하게 오면 매번 메모리를 할당하고 해제해야 하기 때문에 사용할 때 주의가 필요하다고 한다.

- [당근 마켓 기술 블로그](https://medium.com/daangn/varchar-vs-text-230a718a22a1)에서 나온 내용을 참고하여 요약하였다.

<br>

## `TEXT, VARCHAR 컬럼 타입 선정`

- `VARCHAR`
  - 최대 길이가 크지 않은 경우 
  - 테이블 데이터를 읽을 때 항상 해당 컬럼이 필요한 경우 => 미리 할당 해놓은 메모리를 재사용해서 사용할 수 있기 때문에?
  - DBMS 서버의 메모리가 충분한 경우 => 이는 VARCHAR는 크기 만큼 미리 메모리를 할당 해놓기 때문에
- `TEXT`
  - 최대 길이가 큰 경우 
  - 테이블에 길이가 긴 문자열 타입 컬럼이 많이 필요한 경우 => MySQL은 하나의 레코드 사이즈가 64KB로 제한되어 있기 때문에
  - 테이블 데이터를 읽을 때 해당 컬럼이 자주 필요치 않은 경우 => TEXT, BLOB은 요청이 올 때마다 메모리를 할당하고 해제하는 과정이 있기 때문에 자주 요청된다면 비효율적

<br>

## `알아두기`

MySQL 에서 `CHAR`, `VARCHAR` 뒤에 지정하는 숫자는 그 컬럼의 바이트 크기가 아니라 문자의 수를 의미한다.

즉, `CHAR(10)`, `VARCHAR(10)`으로 컬럼을 정의하면 이 컬럼은 10바이트를 저장할 수 있는 공간이 아니라 10글자를 저장할 수 있는 공간을 의미한다.

- 일반적으로 영어를 포함한 서구권 언어는 각 문자가 1바이트 사용
- 한국어나 일본어와 같은 아시아권 언어는 각 문자가 최대 2바이트를 사용
- UTF-8 같은 유니코드는 최대 4바이트까지 사용

<br>

### `Referenece`

- [Real MySQL - 2](https://product.kyobobook.co.kr/detail/S000001766482) (P356 ~ 359)
- [https://medium.com/daangn/varchar-vs-text-230a718a22a1](https://medium.com/daangn/varchar-vs-text-230a718a22a1)
- [https://stackoverflow.com/questions/2023481/mysql-large-varchar-vs-text](https://stackoverflow.com/questions/2023481/mysql-large-varchar-vs-text)
- [https://stackoverflow.com/questions/13506832/what-is-the-mysql-varchar-max-size](https://stackoverflow.com/questions/13506832/what-is-the-mysql-varchar-max-size)