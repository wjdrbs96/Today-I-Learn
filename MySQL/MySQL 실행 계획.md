# `MySQL 실행 계획`

MySQL 서버의 실행 계획은 DESC 또는 EXPLAIN 명령으로 확인할 수 있습니다.

## `쿼리의 실행 계획 확인`

```sql
EXPLAIN SELECT * FROM bbang_map.Bakery WHERE id > 100 ORDER BY id desc;
```

<br>

## `쿼리의 실행 시간 확인`

MySQL 8.0.18 버전부터는 쿼리의 실행 계획과 단계별 소요된 시간 정보를 확인할 수 있는 `EXPLAIN ANALYZE` 기능이 추가되었습니다.

<br>

# `실행 계획 분석`

```
EXPLAIN SELECT * FROM bbang_map.Bakery WHERE id > 100 ORDER BY id desc;
```

<img width="814" alt="스크린샷 2024-03-28 오후 12 03 26" src="https://github.com/wjdrbs96/Today-I-Learn/assets/45676906/36d2d464-6766-47f7-b100-4a8eb084a1db">

저의 사이드 프로젝트 DB에서 임의의 쿼리로 EXPLAIN 확인해보면 위와 같이 나오는데요. 하나씩 어떤 의미를 가지고 있는지 확인 해보겠습니다.

<br>

## `id 컬럼`

쿼리에서 서브 쿼리를 사용할 수도 있고 여러 개의 SELECT가 존재할 수 있기 때문에 id 컬럼은 SELECT 쿼리 별로 부여하는 식별자 값입니다.

<br>

## `select type 컬럼`

각 SELECT 쿼리가 어떤 타입의 쿼리인지 표시되는 컬럼입니다.

<br>

### `SIMPLE`

- UNION 이나 서브쿼리를 사용하지 않는 단순한 SELECT 쿼리인 경우 해당 쿼리 문장의 SELECT TYPE은 SIMPLE로 표시됩니다.

<br>

### `PRIMARY`

- UNION 이나 서브쿼리를 가지는 SELECT 

<br>

<br>

## `Extra`

### `Using Index (커버링 인덱스)`

데이터 파일을 전혀 읽지 않고 인덱스만 읽어서 쿼리를 몯 ㅜ처리할 수 있을 때 Extra 컬럼에 `Using Index`가 표시됩니다.