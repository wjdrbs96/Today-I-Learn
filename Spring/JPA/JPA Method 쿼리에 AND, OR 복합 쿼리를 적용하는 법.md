## `JPA Method 쿼리에 괄호를 적용하는 법`

```sql
SELECT * 
FROM test
WHERE (status = 'A' OR status = 'B') AND id = 8
```

JPA를 사용하면서 위와 같이 괄호를 사용하여 우선 순위가 있는 쿼리는 어떻게 사용하는지 궁금했다.

<br>

```
A and (B or C) <=> (A and B) or (A and C)
```  

구조를 확인해보면 위와 같이 풀어쓸 수가 있었다.

```sql
SELECT * 
FROM test
WHERE status = 'A' And id = 8 OR status = 'B' AND id = 8
```

위와 같이 쿼리를 작성하면 동일한 결과를 얻을 수 있었다.

<br>

## `Reference`

- [https://stackoverflow.com/questions/35788856/spring-data-jpa-how-to-combine-multiple-and-and-or-through-method-name](https://stackoverflow.com/questions/35788856/spring-data-jpa-how-to-combine-multiple-and-and-or-through-method-name)