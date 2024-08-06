## `MySQL DISTINCT에 대해 알아보자`

특정 컬럼의 유니크한 값만 조회할 때 SELECT 쿼리에 DISTINCT를 사용합니다.

- 집합 함수가 없는 경우
- MIN(), MAX(), COUNT() 집합 함수와 함께 사용되는 경우

DISTINCT 키워드가 2가지 상황에 따라 다르게 동작하기 때문에 각각 상황에 대해서 정리해보겠습니다.

그리고 집합 함수와 같이 DISTINCT가 사용되는 쿼리의 실행 계획에서 DISTINCT 처리가 인덱스를 사용하지 못할 때는 항상 임시 테이블 생성이 필요합니다. `하지만 실행 계획 Extra 컬럼에서 Using temporary 메세지가 출력되지 않습니다.`

<br>

### `SELECT DISTINCT ...`

SELECT 쿼리에서 유니크한 레코드만 가져오고자 할 때 사용합니다.

```sql
SELECT DISTINCT emp_no FROM salaries;
```
```sql
SELECT emp_no FROM salaries GROUP BY emp_no;
```

MySQL 8.0 버전 부터는 GROUP BY를 수행하는 쿼리에 ORDER BY 절이 없으면 정렬을 사용하지 않기 때문에 위의 두 쿼리는 내부적으로 같은 작업을 수행합니다.

<br>

```sql
SELECT DISTINCT first_name, last_name FROM salaries;
```
```sql
SELECT DISTINCT(first_name), last_name FROM salaries;
```

DISTINCT는 SELECT 하는 레코드를 유니크하게 가져오는 것이지, 특정 컬럼만 유니크하게 조회하는 것이 아닙니다.

즉, 두 번째 쿼리도 first_name만 유니크하게 가져오는 것이 아니라 `(first_name, last_name)`가 유니크한 컬럼을 가져오는 것입니다.

<br>

### `집합 함수와 함께 사용된 DISTINCT`

```sql
EXPLAIN SELECT COUNT(DISTINCT salary) FROM salaries;
```

위의 쿼리는 `COUNT(DISTINCT salary)`를 처리하기 위해 임시 테이블을 사용합니다. 임시 테이블의 salary 컬럼에는 유니크 인덱스가 생성되기 때문에 레코드 건수가 많아진다면 상당히 느려질 수 있는 형태의 쿼리입니다.

<br>

```sql
EXPLAIN SELECT COUNT(DISTINCT salary), COUNT(DISTINCT last_name) FROM salaries;
```

COUNT() 함수가 두 번 사용된 쿼리는 2개의 임시 테이블을 사용합니다.

<br>

```sql
EXPLAIN SELECT COUNT(DISTINCT emp_no) FROM employees;
```

DISTINCT를 사용할 때 인덱스를 이용할 수 없으면 임시 테이블을 사용하지만, 인덱스 컬럼에 대해서 DISTINCT 처리를 수행할 때는 인덱스를 풀 스캔하거나 레인지 스캔하면서 임시 테이블 없이 최적화 처리를 하게 됩니다.

<br>

### `Reference`

- [Real MySQL - 1]()
- [https://dev.mysql.com/doc/refman/8.0/en/distinct-optimization.html](https://dev.mysql.com/doc/refman/8.0/en/distinct-optimization.html)