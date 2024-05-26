## `Cassandra Statement 정리`

1. 쿼리 문장 분석
2. 쿼리 실행
3. 결과 반환

DB 마다 상세하게 보면 더 복잡하겠지만 요약하면 쿼리가 실행될 때 위의 과정이 진행되며, 카산드라도 마찬가지로 위의 과정을 거치며 실행 됩니다.

<br>

### `SimpleStatement`

```sql
SELECT * FROM application_params WHERE name = 'greeting_message' and part = 'server';
```

![image](https://github.com/wjdrbs96/Today-I-Learn/assets/45676906/3183a412-56cf-4ab5-8d09-0744bbe75562)

- 실제 값을 사용하여 바로 실행 가능한 쿼리
- 쿼리 문장 분석, 쿼리 실행 단계를 매번 실행함
- 바로 실행 가능한 static 한 쿼리이기 때문에, 캐싱 불가능
  - 매번 분석-실행 단계를 수행하기에, PrepareStatement와 비교하여 성능이 떨어짐 
  - 동일한 쿼리를 자주 실행한다면 PrepareStatement를 사용하는 것이 효율적

<br>

### `PrepareStatement`

```sql
SELECT * FROM application_params WHERE name = '?' and part = '?';
```

- 실제 값이 아닌 bind 될 수 있는 값(=?)을 넣어 바로 실행할 수 없는 쿼리
- 캐시가 되었다면 캐시 조회 -> 쿼리 실행 단계만 거치면 됨
- 동일 쿼리에서 조건문의 변수값만 변경되어 호출되는 경우에 효율적으로 사용할 수 있음

![image](https://github.com/wjdrbs96/Today-I-Learn/assets/45676906/b1dfb9e7-914f-4751-a8c2-caf31069c9b7)

- 캐시를 사용하지 못하고 PrepareStatement를 사용했을 때

<br>

![image](https://github.com/wjdrbs96/Today-I-Learn/assets/45676906/60cd23b2-b4f9-4a10-b813-f8fab313e9be)

- 캐시를 사용하고 PrepareStatement를 사용했을 때

<br>

### `BatchStatement`

BatchStatement를 사용하면 여러 쿼리들을 원자적으로 실행할 수 있도록 할 수 있습니다.

<br>

## `Reference`

- [https://docs.datastax.com/en/developer/java-driver/4.0/manual/core/statements/simple/index.html](https://docs.datastax.com/en/developer/java-driver/4.0/manual/core/statements/simple/index.html)
- [https://docs.datastax.com/en/developer/java-driver/4.17/manual/core/statements/prepared/index.html]https://docs.datastax.com/en/developer/java-driver/4.17/manual/core/statements/prepared/index.html
- [https://docs.datastax.com/en/developer/java-driver/4.17/manual/core/statements/batch/index.html](https://docs.datastax.com/en/developer/java-driver/4.17/manual/core/statements/batch/index.html)