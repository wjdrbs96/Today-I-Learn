# `OUTER JOIN의 주의 사항`

`OUTER JOIN`에서 OUTER로 조인되는 테이블의 컬럼에 대한 조건은 모두 ON 절에 명시해야 합니다. `조건을 ON 절에 명시하지 않고 다음 예제와 같이 OUTER 테이블의 컬럼이 WHERE 절에 명시하면 옵티마이저가 INNER JOIN과 같은 방법으로 처리합니다.`

이 부분은.. 맨날 모르고 WHERE 절에 조건을 썼는데 이런게 있었다니! 책을 읽으면서 알게되었네요~

```sql
SELECT * 
FROM employees
LEFT JOIN dept_manager mgr 
ON mgr.emp_no = e.emp_no
WHERE mgr.dept_no = 'd001'
```

항상 저는 이런식으로 쿼리를 작성해왔지만,, 이렇게 WHERE 절에 조건을 명시하는 것은 잘못된 조인 방법입니다. 위의 LEFT JOIN이 사용된 쿼리는 WHERE 절의 조건 때문에 MySQL 옵티마이저가 `LEFT JOIN`을 다음 쿼리와 같이 `INNER JOIN`으로 변환해버립니다. 

정상적인 OUTER JOIN이 되게 만들려면 다음 쿼리와 같이 WHERE 절의 `mgr.dept_no='d001'` 조건을 LEFT JOIN의 ON 절로 옮겨야 합니다. 

<br>

```sql
SELECT * 
FROM employees
LEFT JOIN dept_manager mgr 
ON mgr.emp_no = e.emp_no AND mgr.dept_no = 'd001'
```

OUTER JOIN을 사용하기 위해서는 위와 같이 ON 절에 조건을 모두 써야 하는걸 명심... ! 

<br>

