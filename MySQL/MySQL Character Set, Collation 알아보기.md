# `MySQL Character Set, Collation 알아보기`

- [MySQL Character Set]()
- [문자 집합(Character Set)]()
- [콜레이션(Collation)]()
- [예제를 통해 확인하기]()
    - [이모지 저장]()
    - [대소문자]()
    - [공백 문자]()
    - [악센트 (Accent)]()
- [각 컬럼의 Character Set 및 Collation 확인]()
- [테이블 정보 확인]()

<br>

## `MySQL Character Set`

![image](https://github.com/user-attachments/assets/5d64af55-b0d0-49c2-97a1-9c8489f6c7c4)

- `character_set_system`: MySQL 서버가 식별자(테이블 명이나 컬럼 명 등)를 저장할 때 사용하는 문자 집합이다. 이 값은 기본적으로 utf8로 설정되며, 사용자가 설정하거나 변경할 필요가 없다.
- `character_set_server`: MySQL 서버의 기본 문자 집합이다. DB나 테이블 또는 컬럼에 아무런 문자 집합이 설정되지 않을 때 이 시스템 변수에 명시된 문자 집합이 기본으로 사용된다. 이 시스템 변수의 기본 값은 utf8mb4 이다.
- `character_set_database`: MySQL DB의 기본 문자 집합이다. DB를 생성할 때 아무런 문자 집합이 명시되지 않았다면 이 시스템 변수에 명시된 문자 집합이 기본값으로 사용된다. 이 변수가 정의되지 않으면 character_set_server 시스템 변수에 명시된 문자 집합이 기본으로 사용된다. 이 시스템 변수의 기본값은 utf8mb4 이다.
- `character_set_file_system`: 파일 이름을 해당할 때 사용되는 문자 집합
- `character_set_client`: MySQL 클라이언트가 보낸 SQL 문장은 character_set_client에 설정된 문자 집합으로 인코딩해서 MySQL 서버로 전송한다.
- `character_set_connection`: MySQL 서버가 클라이언트로부터 전달받은 SQL 문장을 처리하기 위해 character_set_connection의 문자 집합으로 변환한다.
- `character_set_results`: MySQL 서버가 쿼리의 처리 결과를 클라이언트로 보낼 때 사용하는 문자 집합을 설정하는 시스템 변수다.

<br>

## `문자 집합(Character Set)`

- MySQL 서버에서 각 테이블의 컬럼은 모두 서로 다른 문자 집합을 사용해 문자열 값을 저장할 수 있다.
- MySQL에서 최종적으로는 컬럼 단위로 문자 집합을 관리하지만 관리의 편의를 위해 MySQL 서버와 DB, 그리고 테이블 단위로 기본 문자 집합을 설정할 수 있는 기능을 제공한다.

```sql
SHOW CHARACTER SET;
```

| Charset | Description               | Default collation   | Maxlen(Length) |
|---------|---------------------------|---------------------|----------------|
| ascii   | US ASCII                  | ascii_general_ci    | 1              |
| binary  | Binary pseudo charset     | binary              | 1              |
| cp932   | SJIS for Windows          | cp932_japanese_ci   | 2              |
| eucjpms | UJIS for Windows Japanese | eucjpms_japanese_ci | 3              |
| euckr   | EUC-KR Korean             | euckr_korean_ci     | 2              |
| latin1  | cp1252 West European      | latin1_swedish_ci   | 1              |
| utf8    | UTF-8 Unicode             | utf8_general_ci     | 3              |
| utf8mb4 | UTF-8 Unicode             | utf8mb4_0900_ai_ci  | 4              |

- `latin`
    - latin 계열의 문자 집합은 알파벳이나 숫자, 그리고 키보드의 특수 문자로만 구성된 문자열만 저장해도 될 때 저장 공간은 절약하면서 사용할 수 있는 문자 집합 (대부분 해시 값이나 16진수로 구성된 "Hex String" 또는 단순한 코드 값을 저장하는 용도로 사용)
- `euckr`
    - 한국어 전용으로 사용되는 문자 집합이며, 모든 글자는 1 ~ 2Byte를 사용한다.
- `utf8(utf8mb3)` ⭐⭐⭐⭐⭐
    - utf8은 utf8mb4의 부분 집합
    - unicode 3Byte를 지원하기 위한 Character Encoding
    - 원래 설계는 가변 4Byte임.
        - `공간절약 + 속도향상을 위해서 MySQL에서 사용하는 utf8의 경우 가변 3Byte로 설계` ⭐⭐⭐⭐⭐
            - `3byte 이모지는 저장 가능함` ([Link](https://gist.github.com/watson/ea44886af36c5495b00e))
    - 영문의 경우 1Byte / 한글의 경우 3Byte 사용 (디스크의 경우 Dynamic / 메모리 3Byte Fix)
    - 8.0부터 utf8mb3으로 표기함
- `utf8mb4` ⭐⭐⭐⭐⭐
    - 다국어 문자를 포함할 수 있는 컬럼에 사용하기에 적합하다.
    - 4Byte(emoji)를 지원하기 위한 Character Encoding ⭐⭐⭐⭐⭐
    - 애플이 IOS5로 업그레이드 하면서 이모티콘(emoji)의 UNICODE를 기존 3Byte 영역에서 4Byte 영역으로 변경 ⭐⭐⭐⭐⭐
    - MySQL 5.1에서 캐릭터셋을 UTF-8(3Byte)로 사용할 시 Incorrect string value (error code [1366]) 에러가 발생
    - MySQL 5.5.3 이후 버전에 utf8mb4 (가변 4Byte)를 지원하도록 변경
    - 기존 Character Set의 경우 UTF8과 동일 / 이후 4Byte 사용문자의 경우 확장하여 사용 (디스크의 경우 Dynamic / 메모리의 4Byte Fix)

<br>

## `콜레이션(Collation)`

콜레이션은 문자열 컬럼의 값에 대한 비교나 정렬 순서를 위한 규칙을 의미한다. 즉, 비교나 정렬 작업에서 영문 대소문자를 같은 것으로 처리할지, 아니면 더 크거나 작은 것으로 판단할지에 대한 규칙을 정의하는 것이다.

|    |                        | Charset | Default   | 대소문자 구분 | 엑센트 구분 | 전각/반각 구분 | emoji 지원 | emoji 정렬구분 | 히라가나/가타가나 구분 | unicode | NO_PAD | WEIGHT_STRING('WIKI')                                      |
|----|------------------------|---------|-----------|---------|--------|----------|----------|------------|--------------|---------|--------|------------------------------------------------------------|
| 1  | euckr_korean_ci        | euckr   |           |         |        | ✅        |          |            | ✅            |         |        | 0x57494B49                                                 |
| 2  | euckr_bin              | euckr   |           | ✅       |        | ✅        |          |            | ✅            |         |        | 0x57494B49                                                 |
| 3  | utf8_general_ci        | utf8    | MySQL 5.6 |         |        | ✅        |          |            | ✅            |         |        | 0x00570049004B0049                                         |
| 4  | utf8_unicode_ci        | utf8    |           |         |        |          |          |            |              | 4.0     |        | 0x10510EFB0F210EFB                                         |
| 5  | utf8_bin               | utf8    |           | ✅       | ✅      | ✅        |          |            | ✅            |         |        | 0x00570049004B0049                                         |
| 6  | utf8mb4_general_ci     | utf8mb4 | MySQL 5.7 |         |        | ✅        | ✅        |            | ✅            |         |        | 0x00570049004B0049                                         |
| 7  | utf8mb4_unicode_ci     | utf8    |           |         |        |          | ✅        |            |              | 4.0     |        | 0x10510EFB0F210EFB                                         |
| 8  | utf8mb4_unicode_520_ci | utf8    |           |         |        |          | ✅        | ✅          |              | 5.2     |        | 0x148D12EC131E12EC                                         |
| 9  | utf8mb4_bin            | utf8mb4 |           | ✅       | ✅      | ✅        | ✅        | ✅          | ✅            |         |        | 0x148D12EC131E12EC                                         |
| 10 | utf8mb4_0900_ai_ci     | utf8mb4 | MySQL 8.0 |         |        |          | ✅        | ✅          |              | 9.0     | ✅      | 0x1EF51D321D651D32                                         |
| 11 | utf8mb4_0900_as_ci     | utf8mb4 |           |         | ✅      |          | ✅        | ✅          |              | 9.0     | ✅      | 0x1EF51D321D651D3200000020002000200020                     |
| 12 | utf8mb4_0900_as_cs     | utf8mb4 |           | ✅       | ✅      | ✅        | ✅        | ✅          | ✅            | 9.0     | ✅      | 0x1EF51D321D651D320000002000200020002000000008000800080008 |
| 13 | utf8mb4_0900_bin       | utf8mb4 |           | ✅       | ✅      | ✅        | ✅        | ✅          | ✅            | 9.0     | ✅      | 0x57494B49                                                 |

- `case` ⭐⭐⭐
    - `ci (case Insensitive)`: 대소문자를 구분하지 않음
    - `cs (case sensitive)`: 대소문자 구분함
    - 문자열 비교나 인덱싱을 할 때, 악센트 부호 제거 후 UPPER 처리를 해버리기 때문에 대소문자를 포함, 악센트 부호가 달린 알파벳들을 동일 문자로 취급
- `accent` ⭐⭐⭐
    - `ai ( accent Insensitive)`: accent를 구분하지 않음
    - `as ( accent sensitive)`: accent를 구분함
    - ```
    ก = ก์ // 'a' = 'à'
    ```
- `pad` ⭐⭐⭐
    - pad space
        - MySQL 5.7 이하 Collation의 경우 문자열 뒤의 PAD가 있는경우 인위적으로 제거하고 처리
    - no pad
        - MySQL 8.0 +0900_ai_ci를 사용하는 경우 문자열 뒤의 PAD에 대해 공백 제거하지 않고 처리
- `binary(bin)`
    - 인위적인 변환없이 Hex 값으로 저장한 데이터로 구분
    - 바이너리 저장 값 그대로 정렬
    - A는 41, B는 42, a는 61, b는 62 순서로 정렬됨
- `general_ci`
    - 다국어에서 사용되는 억양, 강세를 모두 제거한 후 비교
    - 텍스트 정렬할 때 a 다음에 b가 나타나야 한다는 생각으로 나온 정렬방식
    - 라틴계열 문자를 사람의 인식에 맞게 정렬
- `Unicode Collation Algorithm(uca)`
    - 일반 영어, CJK 등의 문자의 정렬은 utf8_general_ci와 동일
    - 그 외 확장영역의 (ex:accents(예, ÀÁÅåāă), 확장문자("ß", "A", ...) 나 연자활자("Œ", ...) )까지 정렬이 필요하다면 사용
    - 4.0.0 : 기본적인 unicode 값
    - 5.2.0 : 확장된 emoji 사용이 가능함
    - 9.0.0 :  5.2.0보다 확장된 코드값 + 더 빠른 정렬을 지원 + 9.0 기준 가능한 emoji 사용 ([Link](https://bugs.mysql.com/bug.php?id=87700))
- `WEIGHT_STRING`
    - collation 비교를 위해 내부적으로 사용하는 값을 표시 (ex: `hex(column_name)`)
- `전각, 반각 문자`
    - 전각 문자는 정사각형 안에 문자가 들어가는 형태
    - 반각 문자는 직사각형 안에 문자가 들어가는 형태로, 전각 문자를 반으로 나눈 사이즈
    - ```
    -- 알파벳 대문자(반각) WIKI, 알파벳 소문자(반각) wiki, 알파벳 소문자(전각) ｗｉｋｉ`
    -- 숫자(반각) 123, 숫자(전각) １２３`
    -- 가타가나(전각) アリガトウ, 가타가나(반각) ｱﾘｶﾄｳ`
    ```

- `Character Set / Collation` ⭐⭐⭐
    - `utf8_general_ci` = utf8 3byte + accent Insensitive (엑센트 구분 ❌) + case Insensitive (대소문자 구분 ❌) + pad space (공백 제거 ✅)
    - `utf8mb4_general_ci` = utf8 4byte + accent Insensitive (엑센트 구분 ❌) + case Insensitive (대소문자 구분 ❌) + pad space + emoji (공백 제거 ✅ + 이모지 지원 ✅)
    - `utf8mb4_0900_ai_ci` = utf8 4byte + uca(unicode) 9.0 + accent Insensitive (엑센트 구분 ❌) + case Insensitive (대소문자 구분 ❌) + no pad + emoji (공백 제거 ❌ + 이모지 지원 ✅)
    - MySQL 8.0.17 utf8mb4 Collations: ([참고 Link](http://mysql.rjweb.org/utf8mb4_collations.html))
    - MySQL 5.6.12-log utf8 Collations: ([참고 Link](https://mysql.rjweb.org/utf8_collations.html))

![image](https://github.com/user-attachments/assets/1bd9a7af-fe5d-4fc9-bace-8cd1a0584354)

<br>

## `예제를 통해 확인하기` ⭐⭐⭐⭐

### `이모지 저장`

이모지는 3byte or 4byte 이모지가 존재합니다. ([참고 Link](https://gist.github.com/watson/ea44886af36c5495b00e))

```sql
CREATE TABLE collation_utf8 (
    seq int auto_increment primary key,
    word varchar(16)
) CHARSET utf8 COLLATE utf8mb3_general_ci;
```

```sql
mysql> select * from collation_utf8;
```
```
+-----+------+
| seq | word |
+-----+------+
|   1 | ✅    |
|   2 | ❌    |
+-----+------+
```

- utf8 이어도 3byte 이모지 까지는 저장 가능함

```sql
INSERT INTO collation_utf8(word) VALUES ('🔀')
```
```
Error Code: 1366. Incorrect string value: '\xF0\x9F\x94\x80' for column 'word'
```

하지만 4byte 이모지를 저장하려고 하면 에러가 발생함 🔥🔥(utf8 3byte 까지만 지원하기 때문)

<br>

```sql
CREATE TABLE collation_general_ci (
    seq int auto_increment primary key, 
    word varchar(16)
) CHARSET utf8mb4 COLLATE utf8mb4_general_ci ;
```

```sql
mysql> select * from collation_general_ci;
```
```
+-----+------+
| seq | word |
+-----+------+
|   1 | 🔀     |
+-----+------+
```

- utf8mb4를 사용하면 4byte 이모지도 저장할 수 있음

<br>

### `대소문자`

`utf8mb4_general_ci`, `utf8mb4_0900_ai_ci` 두 개 collation 모두 대소문자를 구분하지 않습니다.

```sql
mysql> select * from collation_general_ci where word = 'a'; // utf8mb4_general_ci
```
```
+-----+------+
| seq | word |
+-----+------+
|   2 | A    |
|   3 | a    |
+-----+------+
2 rows in set (0.00 sec)
```

```sql
mysql> select * from collation_general_ci where word = 'A'; // utf8mb4_general_ci
```
```
+-----+------+
| seq | word |
+-----+------+
|   2 | A    |
|   3 | a    |
+-----+------+
2 rows in set (0.00 sec)
```

A, a가 있을 때 동일한 문자로 보기 때문에 둘 다 조회가 되는 것을 볼 수 있습니다.

<br>

### `공백 문자`

`utf8mb4_general_ci(PAD = 공백 제거 O)`, `utf8mb4_0900_ai_ci(NO PAD = 공백 제거 X)` 인 것을 볼 수 있습니다.

```sql
INSERT INTO collation_general_ci (word) VALUES ('PAD')    // 공백 없음
INSERT INTO collation_general_ci (word) VALUES ('PAD ')   // 공백 있음
```
```sql
mysql> select * from collation_general_ci where word = 'PAD'; // utf8mb4_general_ci
```
```
+-----+------+
| seq | word |
+-----+------+
|   4 | PAD  |
|   5 | PAD  |
+-----+------+
2 rows in set (0.00 sec)
```

- `utf8mb4_general_ci`은 공백을 제거하기 때문에 둘 다 조회되는 것을 볼 수 있음

<br>

```sql
CREATE TABLE collation_0900_ai_ci (
    seq int auto_increment primary key,
    word varchar(16)
) CHARSET utf8mb4 COLLATE utf8mb4_0900_ai_ci;
```

```sql
mysql> select * from collation_0900_ai_ci where word = 'Dataflow'; // collation_0900_ai_ci
```
```
+-----+----------+
| seq | word     |
+-----+----------+
|   1 | Dataflow |
+-----+----------+
1 row in set (0.00 sec)
```

```sql
mysql> select * from collation_0900_ai_ci where word = 'Dataflow '; // collation_0900_ai_ci
```
```
+-----+-----------+
| seq | word      |
+-----+-----------+
|   2 | Dataflow  |
+-----+-----------+
1 row in set (0.00 sec)
```

`collation_0900_ai_ci`는 공백 문자를 제거하지 않기 때문에 두 개를 구분하고 있는 것을 볼 수 있습니다.

<br>

### `악센트 (Accent)`

```sql
mysql> select * from collation_general_ci where word = 'a'; // utf8mb4_general_ci
```
```
+-----+------+
| seq | word |
+-----+------+
|   8 | a    |
|   9 | à    |
+-----+------+
4 rows in set (0.00 sec)
```

`utf8mb4_general_ci`는 `a`, `à` 처럼 악센트 문자를 구분하지 않는 것을 볼 수 있습니다. 그런데... 아주 흥미로운 사실이 있는데요. 🤔🤔

<br>

```sql
mysql> select * from collation_general_ci where word = 'ก';
```
```
+-----+--------+
| seq | word   |
+-----+--------+
|   6 | ก์     |
+-----+--------+
1 row in set (0.00 sec)
```

`n`, `กั` 두 개 문자는 구분하고 있는 것을 볼 수 있습니다. 어떤 언어인가 찾아보니 태국어 인거 같은데요. 해당 내용에 대해 잘 정리된 블로그가 있었습니다. ([참고 Link](https://juneyr.dev/mysql-collation))

![image](https://github.com/user-attachments/assets/497454e8-b651-44f4-9e38-e7ec96a2ec97)

대략 요약하면... `utf8mb4_general_ci`는 태국어, 일본어에 `ai ( accent Insensitive)` 적용이 안된다는 거 같습니다. (자세한 것은 더 찾아봐야 함 !!)

collation 세계는 넓고 신기한 것이 있다 정도로 공유 드리려고 정리 해보았습니다.

<br>

```sql
mysql> select * from collation_0900_ai_ci where word = 'ก'; // utf8mb4_0900_ai_ci
```
```
+-----+--------+
| seq | word   |
+-----+--------+
|   3 | ก      |
|   4 | ก์     |
+-----+--------+
2 rows in set (0.00 sec)
```

`utf8mb4_0900_ai_ci`는 악센트를 구분하지 않기 때문에 둘 다 같이 조회되는 것을 볼 수 있습니다.

<br>

## `각 컬럼의 Character Set 및 Collation 확인`

```sql
SELECT table_name, column_name, column_type, character_set_name, collation_name
FROM information_schema.columns
WHERE table_schema = 'gyunny' and table_name = 'collation';
```

<br>

## `테이블 정보 확인`

```sql
SHOW TABLE STATUS where name like 'TABLE_NAME'
```
```
+----------------+--------+---------+------------+------+----------------+-------------+-----------------+--------------+-----------+----------------+---------------------+---------------------+------------+--------------------+----------+----------------+---------+
| Name           | Engine | Version | Row_format | Rows | Avg_row_length | Data_length | Max_data_length | Index_length | Data_free | Auto_increment | Create_time         | Update_time         | Check_time | Collation          | Checksum | Create_options | Comment |
+----------------+--------+---------+------------+------+----------------+-------------+-----------------+--------------+-----------+----------------+---------------------+---------------------+------------+--------------------+----------+----------------+---------+
| collation_test | InnoDB |      10 | Dynamic    |   10 |           1638 |       16384 |               0 |            0 |         0 |             11 | 2024-07-23 19:23:37 | 2024-07-23 19:32:26 | NULL       | utf8mb4_0900_ai_ci |     NULL |                |         |
+----------------+--------+---------+------------+------+----------------+-------------+-----------------+--------------+-----------+----------------+---------------------+---------------------+------------+--------------------+----------+----------------+---------+
```

<br>

## `Reference`

- [Real MySQL - 2]()
- [https://dev.mysql.com/doc/mysql-installation-excerpt/8.0/en/upgrading-from-previous-series.html](https://dev.mysql.com/doc/mysql-installation-excerpt/8.0/en/upgrading-from-previous-series.html)
- [https://mysql.rjweb.org/utf8_collations.html](https://mysql.rjweb.org/utf8_collations.html)
- [https://mysql.rjweb.org/utf8mb4_collations.html](https://mysql.rjweb.org/utf8mb4_collations.html)