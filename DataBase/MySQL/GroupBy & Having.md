# `Group By & Having`이란?

쿼리를 짜다 보면 그룹화 해서 그룹의 어떤 특정 컬럼을 카운트 하고 싶을 때가 반드시 있다. 예를 들어 남녀 분반인 반에서 남자가 몇명인지 알고 싶다던지 등등의 상황이 있을 것이다. 

<br>

이 때 사용하는 것이 `Group By`와 `Having`이다. 

<br>

### `컬럼 그룹화`

```sql
SELECT 컬럼 FROM 테이블이름 GROUP BY 그룹화할 컬럼;
``` 

<br>

### `조건 처리 후에 컬럼 그룹화`

```sql
SELECT 컬럼 FROM 테이블이름 GROUP BY 그룹화할 컬럼 HAVING 조건식;
```

<br>

### `조건 처리 후에 컬럼 그룹화 후에 조건 처리`

```sql
SELECT 컬럼 FROM 테이블이름 WHERE 조건식 GROUP BY 그룹화할 컬럼 HAVING 조건식;
```

<br>

### `ORDER BY가 존재하는 경우`

```sql
SELECT 컬럼 FROM 테이블이름 WHERE 조건식 GROUP BY 그룹화할 컬럼 HAVING 조건식 ORDER BY 컬럼1
```

<br>

## `예제 쿼리(Example Query)`

<br>

### `예제 테이블: collection`

| idx | type | name |
|------|-------|--------|
| 1 | 남 | 김철수 |
| 2 | 여 | 신짱아 |
| 3 | 여 | 신장미 |
| 4 | 여 | 안영미 |
| 5 | 남 | 신형만 |

<br>

### `type을 그룹화하여 name의 개수를 조회해보자`

```sql
SELECT type, COUNT(name) as cnt FROM collection GROUP BY type
```

이러면 type이 같은 것끼리 그룹화해서 묶여진다. 한마디로 남자, 여자의 그룹이 생기게 된다. 결과는 어떻게 나오는지 봐보자.


### `결과`

| type | cnt | 
|------|-------|
| 남 | 2 |
| 여 | 3 |

<br>

### `남자만 그룹화하여 몇명인지 알고 싶다면?`

```sql
SELECT type, COUNT(name) as cnt FROM collection WHERE type = '남' GROUP BY type;
```

### `결과`

| type | cnt | 
|------|-------|
| 남 | 2 |

<br>

### `그룹화의 인원의 조건을 걸고 싶을 때`

```sql
SELECT type, COUNT(name) as cnt FROM collection GROUP BY type HAVING cnt >= 3;
```

### `결과`

| type | cnt | 
|------|-------|
| 여 | 3 |

<br>

### `WHERE과 HAVING을 같이 써서 조건 주기`

```sql
SELECT type, COUNT(name) as cnt FROM collection WHERE type = '여' GROUP BY type HAVING cnt >= 3;
```

### `결과`

| type | cnt | 
|------|-------|
| 여 | 3 |
 
 
<br>

### `ORDER BY를 이용해서 정렬을 하고 싶을 때`

```sql
SELECT type, COUNT(name) as cnt FROM collection WHERE type = '여' GROUP BY type HAVING cnt >= 3 ORDER BY type DESC;
```

<br>

## `정리하기`

- 특정 컬럼을 그룹화하는 `GROUP BY`
- 특정 컬럼을 그룹화한 결과에 조건을 거는 `HAVING`
- ### `WHERE는 그룹화 하기 전에 조건을 거는 것`이고, `HAVING은 그룹화 한 후에 조건을 거는 것`이다.



