## `Cassandra truncate 이후 snaptshot 제거`

### `truncate`

```sql
TRUNCATE test_keyspsace.user_activity;
```

카산드라에서도 `truncate` 명령어로 테이블의 데이터를 지울 수 있다. 하지만 데이터를 다시 복구해야 할 수도 있기 때문에 스냅샷이라는 데이터를 남겨놓는다.

즉, truncate 한다고 해서 바로 Disk 사용량이 줄어들지 않고 스냅샷을 제거해야 서버의 Disk 사용량이 줄어들게 된다.

<br>

### `snapshot 제거`

```sql
./nodetool clearsnapshot {keyspace} --all
ex) ./nodetool clearsnapshot test_keyspsace --all
```

Cassandra TRUNCATE 작업 후에 위의 명령어로 SNAPSHOT 제거도 필요하다. 

하나 알게된 것은 스냅샷 제거는 테이블 단위로 제거하는 줄 알았는데 keyspace 단위로 제거해야 하는 것이었다.

그리고 노드 하나 들어가서 스냅샷 제거하면 알아서 스냅샷이 다 제거될 줄 알았는데, 노드별로 다 들어가서 직접 삭제해야 했다.

다시 생각해보면, 카산드라는 노드별로 토큰의 범위를 가지고 그 범위에 있는 데이터를 가지고 있기 때문에.. 당연한거 같다. (한번에 모든 노드 스냅샷 날리는 명령어가 있는지 모르겠다.) 

그래서 4대의 노드를 쓰고 있었는데 4대 노드에 각각 스냅샷 제거 명령어를 실행 하였다.

<br>

## `Reference`

- [https://docs.datastax.com/en/cql-oss/3.x/cql/cql_reference/cqlTruncate.html](https://docs.datastax.com/en/cql-oss/3.x/cql/cql_reference/cqlTruncate.html)