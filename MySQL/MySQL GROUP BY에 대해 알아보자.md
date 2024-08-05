## `MySQL GROUP BY에 대해 알아보자`

이번 글에서는 MySQL GROUP BY를 사용할 때 고려해야 할 점에 대해서 알아보려 합니다.

<br>

## `요약`

- GROUP BY 절에 명시된 칼럼과 ORDER BY에 명시된 칼럼이 순서와 내용이 모두 같아야 한다.
- GROUP BY와 ORDER BY가 같이 사용된 쿼리에서는 둘 중 하나라도 인덱스를 이용할 수 없을때는 둘다 인덱스를 사용하지 못한다.
- MySQL의 GROUP BY는 ORDER BY 칼럼에 대한 정렬까지 함께 수행하는 것이 기본 작동 방식이므로 GROUP BY와 ORDER BY 칼럼이 내용과 순서가 같은 쿼리에서는 ORDER BY 절을 생략해도 같은 결과를 얻게 된다.
  - MySQL 8.0 이전 버전까지는 GROUP BY가 사용된 쿼리는 그룹핑 되는 컬럼을 기준으로 묵시적인 정렬까지 수행되었지만, MySQL 8.0 버전 부터는 이 값은 묵시적인 정렬은 더 이상 수행되지 않는다.

<br>

## `WHERE + GROUP BY + ORDER BY 인덱스 조건`

<img width="748" alt="스크린샷 2024-04-13 오후 1 36 38" src="https://github.com/wjdrbs96/Today-I-Learn/assets/45676906/c855938b-86d8-4d57-b984-28312ace7bf5">

1. WHERE 절이 인덱스 사용할 수 있는가?
2. GROUP BY 절이 인덱스 사용할 수 있는가?
3. GROUP 절과 ORDER BY 절이 동시에 인덱스 사용할 수 있는가?

위의 3가지 질문에 대해서 Flow Chart를 보면 위와 같습니다.

<br>

### `GROUP BY 처리`

GROUP BY 또한 ORDER BY와 같이 쿼리가 스트리밍된 처리를 할 수 없게 하는 처리 중 하나입니다.

참고로 GROUP BY 절이 있는 쿼리에서는 HAVING 절을 사용하여 GROUP BY 결과를 필터링 역할을 하는데, GROUP BY에 사용된 조건은 인덱스를 사용해서 처리될 수 없으므로 HAVING 절을 튜닝하려고 인덱스를 생성할 필요는 없습니다.

- 인덱스를 이용하여 GROUP BY 실행
  - 인덱스 스캔 방법 (인덱스를 차례대로 읽음)
  - 루스 인덱스 스캔 (인덱스를 건너뛰면서 읽음)
- 인덱스를 이용하지 못하고 GROUP BY 실행
  - 임시 테이블 생성

즉, GROUP BY를 사용했을 때 인덱스를 사용했을 때 사용하지 못했을 때 2가지 경우로 볼 수 있습니다.

<br>

## `인덱스를 이용하여 GROUP BY 실행`

- `인덱스 스캔 (인덱스를 차례대로 읽음)`
  - 쿼리 조건에 따라 전체 인덱스 스캔 또는 범위 인덱스 스캔 중 하나임
  - 실행 계획 Extra 컬럼에 `Using index for group-by` 표시
- `루스 인덱스 스캔 (인덱스를 건너뛰면서 읽음)`
  - 인덱스의 레코드를 건너뛰면서 필요한 부분만 읽어오는 방식
  - 실행 계획 Extra 컬럼에 `Using index for group-by` 표시
  - 단일 테이블에 대해 수행되는 GROUP BY 처리에만 사용할 수 있음

GROUP BY 인덱스를 이용할 때는 위처럼 2가지 상황이 존재할 수 있습니다.([참고 Link](https://dev.mysql.com/doc/refman/8.0/en/group-by-optimization.html))

<br>

### `GRROUP BY 인덱스 사용 예시`

```sql
index idx(c1,c2,c3) on table t1(c1,c2,c3,c4)
```

테이블에 c1, c2, c3 순서로 생성되어 있는 인덱스가 있다고 가정하고 예시를 보면서 알아보겠습니다.

<br>

#### `WHERE, GROUP 절 같이 사용할 때`

```sql
SELECT * FROM t1 WHERE c1 = 'c1' GROUP BY c2, c3                # 사용 가능
SELECT * FROM t1 WHERE c1 = 'c1' AND c2 = 'c2' GROUP BY c3      # 사용 가능
```

1. WHERE 조건 인덱스 사용 가능
2. GROUP BY 인덱스 사용 가능

위의 두 쿼리 예시를 보면 Flow Chart에서 볼 수 있듯이 WHERE, GROUP 에서 사용하는 컬럼이 인덱스에서 생성된 순서대로 컬럼을 사용하고 있어서 인덱스를 모두 이용할 수 있습니다.

그리고 WHERE 조건은 인덱스 첫 번째 칼럼으로 한 번 걸려졌기 때문에 GROUP BY 절에는 인덱스 두 번째 컬럼부터 사용해도 인덱스를 사용할 수 있다는 특징을 가지고 있습니다.

<br>

#### `GROUP BY 절만 사용할 때`

```sql
SELECT * FROM t1 GROUP BY c1               # 사용 가능
SELECT * FROM t1 GROUP BY c1, c2           # 사용 가능
SELECT * FROM t1 GROUP BY c1, c2, c3       # 사용 가능
```

GROUP BY 조건만 사용할 때도 마찬가지로 인덱스 생성 순서대로 컬럼을 사용하고 있기 때문에 모두 인덱스 사용이 가능합니다.

<br>

```sql
SELECT * FROM t1 GROUP BY c2, c1                # 순서 불일치, 사용 불가능
SELECT * FROM t1 GROUP BY c1, c3, c2            # 순서 불일치, 사용 불가능
SELECT * FROM t1 GROUP BY c1, c3                # 순서는 일치하나, c2가 누락되서 사용 불가능
SELECT * FROM t1 GROUP BY c1, c2, c3, c4        # c4는 인덱스에 들어있지 않아서 사용 불가능
```

위와 같은 특징에 대해서도 참고하면 좋을 것 같습니다.

<br>

## `인덱스를 이용하지 못하고 GROUP BY 실행`

### `임시 테이블 생성`

GROUP BY 기준 컬럼이 드라이빙 테이블에 있든 드리븐 테이블에 있든 관계없이 인덱스를 전혀 사용하지 못할 때 임시 테이블 방식이 사용됩니다.

임시 테이블을 사용할 때 쿼리의 실행 계획을 보면 Extra 컬럼에 `Using temporary;`와 같이 나오는 것을 확인할 수 있습니다.

그런데 제가 테스트 해보았던 쿼리의 실행 계획에서는 `Using temporary; Using filesort` 와 같이 `filesort`도 나타난 것을 보았는데요.

GROUP BY가 인덱스를 사용하지 못해서 임시 테이블(`Using temporary`)을 생성한 것은 알겠는데, ORDER BY를 사용하지 않았는데 `Filesort 정렬이 왜 일어나는 것일까` 라는 생각이 들었는데요.

> MySQL의 GROUP BY는 ORDER BY 칼럼에 대한 정렬까지 함께 수행하는 것이 기본 작동 방식이므로 GROUP BY와 ORDER BY 칼럼이 내용과 순서가 같은 쿼리에서는 ORDER BY 절을 생략해도 같은 결과를 얻게 된다.

> MySQL 8.0 이전 버전까지는 GROUP BY가 사용된 쿼리는 그룹핑 되는 컬럼을 기준으로 묵시적인 정렬까지 수행되었지만, MySQL 8.0 버전 부터는 이 값은 묵시적인 정렬은 더 이상 수행되지 않는다.

관련하여 좀 더 찾아보니 MySQL에서 GROUP BY는 위와 같은 특징을 가지고 있어서 ORDER BY가 디폴트로 실행된 것 같습니다. (테스트 한 MySQL 버전은 5.7.33)

<br>

## `Reference`

- [Real MySQL - 1]()
- [https://lannstark.tistory.com/40](https://lannstark.tistory.com/40)
- [https://dev.mysql.com/doc/refman/8.0/en/group-by-optimization.html](https://dev.mysql.com/doc/refman/8.0/en/group-by-optimization.html)