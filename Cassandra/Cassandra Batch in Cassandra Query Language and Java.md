## `Cassandra Batch in Cassandra Query Language and Java`

- 정리가 완벽하지 않고 아주아주 가볍게 대충 이런 느낌이구나만 파악해보았다. 추후 계속 내용 업데이트 예정

<br>

카산드라는 `ACID (Atomicity, Consistency, Isolation, and Durability)`를 일부만 지원한다.

Cassandra Batch 쿼리(ex: INSERT, UPDATE, DELETE)는 단일 파티션을 대상으로 하거나 여러 파티션을 대상으로 할 때만 원자성(Atomicity) 및 독립성(Isolation) 달성한다.

```sql
BEGIN [ ( UNLOGGED | COUNTER ) ] BATCH
[ USING TIMESTAMP [ epoch_microseconds ] ]
dml_statement [ USING TIMESTAMP [ epoch_microseconds ] ] ;
[ dml_statement [ USING TIMESTAMP [ epoch_microseconds ] ] [ ; ... ] ]
APPLY BATCH;
```
```sql
BEGIN BATCH 

INSERT INTO product (product_id, variant_id, product_name) 
VALUES (2c11bbcd-4587-4d15-bb57-4b23a546bd7f,0e9ef8f7-d32b-4926-9d37-27225933a5f3,'banana'); 

INSERT INTO product (product_id, variant_id, product_name) 
VALUES (2c11bbcd-4587-4d15-bb57-4b23a546bd7f,0e9ef8f7-d32b-4926-9d37-27225933a5f5,'banana'); 

APPLY BATCH;
```

Batch 쿼리는 롤백을 지원하지 않는다고 한다.

<br>

### `Single Partition`

단일 파티션 배치 작업은 기본적으로 로그가 해제되므로 로그로 인한 성능 저하를 겪지 않는다. (좀 더 파악 필요)

<br>

### `Multiple Partitions`

다중 파티션 배치 작업은 배치 로그 메커니즘을 사용하여 원자성을 보장한다.

Coordination 노드는 배치 로그 요청을 배치 로그 노드에 전송하고 확인된 응답을 받으면 배치 문을 실행하고 노드에서 배치 로그를 제거하여 클라이언트에 확인을 전송합니다.

다중 파티션 배치 쿼리는 사용하지 않는 것이 좋다. 이러한 쿼리는 Coordination 노드에 부하를 준다.



<br>

## `Reference`

- [https://www.baeldung.com/java-cql-cassandra-batch](https://www.baeldung.com/java-cql-cassandra-batch)