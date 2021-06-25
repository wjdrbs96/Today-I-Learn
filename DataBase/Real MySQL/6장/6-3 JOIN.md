# `JOIN 정리하기`

이번 글에서는 JOIN에 대해서 알아보겠습니다. (JOIN에 대해서는 아는데 INNER, OUTER 용어가 헷갈려서.. 정리를 해보려 합니다.)

<br>

## `JOIN의 종류`

JOIN의 종류는 크게 `INNER JOIN`과 `OUTER JOIN`으로 구분할 수 있고, OUTER JOIN은 다시 `LEFT OUTER JOIN`, `RIGHT OUTER JOIN`, `FULL OUTER JOIN`으로 구분할 수 있습니다.
조인의 처리에서 어느 테이블을 먼저 읽을지를 결정하는 것은 상당히 중요하며, 그에 따라 처리할 작업량이 상당히 달라집니다.

`INNER JOIN`은 어느 테이블을 먼저 읽어도 결과가 달라지지 않으므로 MySQL 옵티마이저가 JOIN의 순서를 조절해서 다양한 방법으로 최적화를 수행할 수 있습니다. 하지만 `OUTER JOIN`은 반드시 OUTER가 되는 테이블을 먼저 읽어야 하기 때문에 조인 순서를 옵티마이저가 선택할 수 없습니다.

<br>

## `JOIN (INNER JOIN)`

일반적으로 `조인`이라 함은 `INNER JOIN`을 지칭하는데, 별도로 `OUTER JOIN`과 구분할 때 `이너 조인`이라고도 합니다. 별로도 명시하지 않고 사용하면 `INNER JOIN`으로 사용됩니다.

<img width="499" alt="스크린샷 2021-06-25 오후 2 54 36" src="https://user-images.githubusercontent.com/45676906/123376325-43574b00-d5c5-11eb-96b3-95be6ef579aa.png">

위의 그림은 `INNER JOIN`이 수행된 결과를 볼 수 있습니다. 즉, `emp_no` 값이 다른 값은 조인 결과에 나오지 않는 것을 볼 수 있습니다.

<br>

## `JOIN (OUTER JOIN)`

INNER JOIN에서는 일치하지 않는 레코드는 모두 버리지만, OUTER JOIN에서는 일치하지 않더라도 버리지 않고 `NULL`로 채워서 결과를 응답합니다.

<img width="497" alt="스크린샷 2021-06-25 오후 2 58 49" src="https://user-images.githubusercontent.com/45676906/123376676-dbedcb00-d5c5-11eb-8620-857f5796e425.png">

<br>

## `JOIN (FULL OUTER JOIN)`

MySQL에서는 `FULL OUTER JOIN`을 지원하지 않습니다. 하지만 INNER JOIN, OUTER JOIN을 잘 섞어서 쓰면 `FULL OUTER JOIN`을 만들 수 있습니다.

<br>

## `JOIN 참고하기`

MySQL의 실행 계획은 `INNER JOIN`을 사용했는지 `OUTER JOIN`을 사용했는지를 알려주지 않으므로 `OUTER JOIN`을 의도한 쿼리가 `INNER JOIN`으로 실행되는지 않는지 주의해야 합니다.
이 부분도 실수하기 쉬운 부분인데, OUTER JON에서 레코드가 없을 수도 있는 쪽의 테이블에 대한 조건은 반드시 LEFT JOIN의 ON 절에 모두 명시하는 것이 좋습니다.

그렇지 않으면 옵미터마이저는 OUTER JOIN을 내부적으로 INNER JOIN으로 변형시켜서 처리해 버릴 수도 있습니다. LEFT OUTER JOIN의 ON 절에 명시되는 조건은 조인되는 레코드가 있을 때만 적용됩니다.
그래서 OUTER JOIN으로 연결되는 테이블이 있는 쿼리에서는 가능하다면 모든 조건을 ON 절에 명시하는 습관을 들이는 것이 좋습니다.

```sql
SELECT *
FROM emploeeys e 
   LEFT OUTER JOIN salaries s ON s.emp_n=e.emp_no
WHERE s.salary > 5000;
```

위의 쿼리가 LEFT OUTER JOIn과 WHERE 절을 충돌해서 사용하고 있는 예시입니다. OUTER JOIN으로 연결되는 테이블의 컬럼에 대한 조건이 ON 절에 명시되지 않고 WHERE 절에 명시됐기 때문입니다.

그래서 MySQL 서버는 이 쿼리를 최적화 단계에서 아래와 같은 쿼리로 변경한 후 실행합니다.

```sql
SELECT *
FROM employees e
  INNER OIN salaries s ON s.emp_no=e.emp_no
WHERE s.salary > 5000;
```

즉, 이런 형태의 쿼리는 아래의 2가지 중에 한 방식으로 수정해야 쿼리 자체의 의도나 결과를 명확히 할 수 있습니다.

<br>

```sql
SELECT *
FROM emploeeys e
         LEFT OUTER JOIN salaries s ON s.emp_n=e.emp_no AND s.salary > 5000;

SELECT *
FROM employees e
    INNER OIN salaries s ON s.emp_no=e.emp_no
WHERE s.salary > 5000;
```

<br> <br>

## `INNER JOIN과 OUTER JOIN의 선택`

> INNER JOIN은 조인의 양쪽 테이블 모두 레코드가 존재하는 경우에만 레코드가 반환된다. 하지만 OUTER JOIN은 아우터 테이블에 존재하면 레코드가 반환된다.  
> 쿼리나 테이블의 구조를 살펴보면 OUTER JOIN을 사용하지 않아도 될 것을 OUTER JOIN으로 사용할 때가 상당히 많다.
> DBMS 사용자 가운데 INNER JOIN을 사용했을 때, 레코드가 결과에 나오지 않을까 걱정하는 사람들이 꽤 있는 듯하다. OUTER JOIN과 INNER JOIN은 저마다 용도가 다르므로 적절한 사용법을 익히고 요구되는 요건에 맞게 사용하는 것이 중요하다.
> <br>
> 때로는 그 반대로 OUTER JOIN으로 실행하면 쿼리의 처리가 느려진다고 생각하고, 억지로 INNER JOIN으로 쿼리를 작성할 때도 있다.
> 하지만 가져오는 쿼리의 결과 건수가 같다면 성능 차이는 거의 생하지 않는다.
> <br> <br>
> Refrence: Real MySQL

<br> <br>

# `Reference`

- Real MySQL