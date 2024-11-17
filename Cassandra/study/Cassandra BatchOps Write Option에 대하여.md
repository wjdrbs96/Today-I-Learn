## `Cassandra BatchOps Write Option에 대하여`

Cassandra에서 insert, update는 둘 다 upsert 방식으로 동작하기 때문에 근본적으로는 큰 차이가 없습니다. 자세히 들어가면 약간의 차이가 있지만 해당 내용은 이 글에서 다루지 않겠습니다.([참고 Link](https://medium.com/@HobokenDays/cassandra-commands-unveiled-exploring-the-nuances-of-update-insert-39fdb06eadb0))

```
cassandraTemplate.batchOps()
    .insert(Entities)
    .insert(Entities2)
    .execute();
```

카산드라에서 batchOps를 통해서 경량 트랙잭션을 사용하여 원자성을 보장하도록 저장할 수 있습니다.

- 존재하지 않는 PK에 대하여 INSERT -> 저장
- 존재하는 PK에 대하여 INSERT -> 업데이트

위처럼 동작하게 될텐데 존재하는 PK의 row 필드 중에서 값이 존재하는데 null로 변경하려고 할 때는 적용이 되지 않았습니다.

왜 적용이 안되지 하면서 찾아보았는데 원인은 batchOps insert 할 때 특정 필드가 null 이면 제외하는 것이 디폴트 옵션이라 그렇습니다.

<br>

```
cassandraTemplate.batchOps()
    .insert(Entities, InsertOptions.builder().withInsertNulls().build())
    .execute();
```

만약에 null로 업데이트 하고 싶다면 위와 같이 `InsertOptions`을 사용하면 됩니다.

<br>

## `Reference`

- [https://docs.spring.io/spring-data/cassandra/docs/current/api/org/springframework/data/cassandra/core/InsertOptions.InsertOptionsBuilder.html#withInsertNulls-boolean-](https://docs.spring.io/spring-data/cassandra/docs/current/api/org/springframework/data/cassandra/core/InsertOptions.InsertOptionsBuilder.html#withInsertNulls-boolean-)