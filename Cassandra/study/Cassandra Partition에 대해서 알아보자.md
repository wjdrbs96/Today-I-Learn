## `Cassandra Partition에 대해 알아보자`

고가용성과 확장성을 위해 설계된 분산형 NoSQL 데이터베이스 시스템인 Cassandra에서 파티셔닝은 중요한 개념입니다. 

데이터를 더 작은 파티션으로 나누어 클러스터에 분산하는 과정인 파티셔닝을 통해서 Cassandra는 분산형 아키텍처를 사용하여 여러 노드에 걸쳐 데이터를 저장합니다. 

카산드라를 사용할 때 성능을 최적화하고 데이터 분산을 균일하게 하기 위해서는 파티셔닝을 제대로 이해하고 구성하는 것은 아주 중요하고 필수적입니다. (안그러면 카산드라 사용하는 이유 및 효율성이 매우 떨어지는..)

본격적인 내용을 들어가기 전에 가볍게 Cassandra `Partition Key`, `Clustering Key`, `Primary Key`에 대해서 살짝만 알아보겠습니다.

![image](https://github.com/wjdrbs96/Today-I-Learn/assets/45676906/97618dea-c6d9-4ac5-a92e-7a2a5bf26d38)

- `Primary Key = Partition Key + [Clustering Columns]`

위의 그림을 보면 Cassandra Partition Key, Clustering Key, Primary Key에 대해서 잘 나타내고 있습니다. 

`Sensor`, `Date`를 Partition Key로 사용하여 노드별로 데이터를 분산시키고, 노드 안에서 파티션 키가 같다면 `timestamp`를 사용해서 데이터를 정렬하고 있는 것을 알 수 있습니다. 

Cassandra는 DDL 쿼리에서 Partition Key, Clustering Key, Primary Key를 명시할 수 있는데 이 부분은 해당 글에서 설명하지 않겠습니다. 좀 더 알고 싶다면 [여기](https://www.baeldung.com/cassandra-keys) 글을 참고하면 좋을 것 같습니다.

위의 그림을 보면 Partition Key 별로 서로 다른 노드에 저장되고 있는 것을 볼 수 있습니다. 

카산드라 읽기 쓰기 작업은 Partition 단위로 진행되는데, `토큰(tokens) (a long value out of range -2^63 to +2^63 -1)`을 통해서 데이터를 분산하고 인덱싱 합니다.

이제 토큰이 무엇인지 알아보겠습니다.

<br>

## `Casssandra Token 이란?`

![image](https://github.com/wjdrbs96/Today-I-Learn/assets/45676906/25595181-2c0c-44c6-817e-199b9893cc25)

카산드라 토큰의 범위는 `-2^63 to +2^63 -1` 입니다. 즉, 노드(서버)별로 토큰의 할당 범위를 가지게 됩니다. (노드가 추가되거나 제거되면 토큰 재할당(데이터 재분배) 과정이 필요함)

파티션은 주어진 파티션 키를 토큰으로 변환하기 위해 파티셔너를 사용하고, 카산드라는 파티션 키를 사용하여 노드에 데이터를 분산하여 저장하고 데이터를 읽어올 때 어떤 노드에서 데이터를 찾을지 결정합니다. (즉, 파티션 키가 같다면 같은 노드에 저장됨)

참고로 Cassandra Partitioner는 `Murmur3Partitioner(default)`, `RandomPartitioner`, `ByteOrderedPartitioner` 3가지를 제공하는데 자세한 것은 [여기](https://docs.datastax.com/en/cassandra-oss/3.0/cassandra/architecture/archPartitionerAbout.html)에서 확인할 수 있습니다. 

이렇게 말만 들으면 무슨 말인지 이해가 완벽하게 안될 수 있는데요. 좀 더 구체적인 예시를 들어보겠습니다.

| name   | age | car    | gender |
|--------|-----|--------|--------|
| jim	   | 15  | camaro | M      |
| carol  | 23  | bmw    | F      | 
| johnny | 35  |        | M      |
| suzy   | 29  |        | F      |

위처럼 카산드라 테이블에 데이터가 있다고 가정하겠습니다.

<br>

| partition key | murmur3 hash value (token) |
|---------------|----------------------------|
| jim	          | -2245462676723223822       | 
| carol         | 7723358927203680754        |  
| johnny        | 	-6723372854036780875      | 
| suzy          | 1168604627387940318        | 

파티션 키가 `name` 일 때 `murmur3` 파티셔너를 사용하여 해싱되고 위의 값처럼 토큰이 만들어지게 될 것입니다.

<br>

![image](https://github.com/wjdrbs96/Today-I-Learn/assets/45676906/1043b459-3006-450d-852e-47fa2e79d72d)

그러면 위처럼 노드별로 토큰 할당 범위를 가지고 있어서 데이터가 분산되어 저장되는 것입니다.

| node | start range           | end range             | partition key | hash value           |
|------|-----------------------|-----------------------|---------------|----------------------|
| A	   | 	-9223372036854775808 | 	-4611686018427387904 | johnny	       | -6723372854036780875 |
| B    | -4611686018427387903  | -1                    | jim           | -2245462676723223822 | 
| C    | 0                     | 4611686018427387903   | suzy          | 1168604627387940318  | 
| D    | 4611686018427387904   | 9223372036854775807	  | carol         | 7723358927203680754  |

예시를 보면 카산드라 토큰과 파티셔닝을 통하여 데이터가 분산되어 저장되는 방식에 대해서 잘 이해할 수 있을 것이라 생각합니다

<br>

![image](https://github.com/wjdrbs96/Today-I-Learn/assets/45676906/6920ce7a-fbec-40ae-a0ef-38d0e154ad60)

카산드라 토큰과 데이터 분산되는 방식에 대해 한눈에 보기 좋은 그림이 있어서 이것도 같이 참고하면 좋을 것 같습니다. 

<br>

## `카산드라 Partition 설계할 때 알아야 할 점`

### `파티션 사이즈`

카산드라 실질적인 Partition Size는 20억 셀이라고 하는데 이렇게 큰 것은 비효율적이라 적절한 파티션 수를 가지는 것이 필요합니다.

또한 중요한 것은 카산드라의 최대 파티션 크기는 100MB 이하여야 하는데, 이상적으로는 10MB 이하여야 합니다. 10MB가 넘어가면 데이터를 읽어올 때 성능 저하가 생길 수 있습니다.

<br>

### `파티션 데이터 불균형`

파티션 키를 잘못 설계하면 특정 파티션으로 데이터가 많이 쏠려서 파티션마다의 데이터가 불균형하게 분배되어 저장될 수 있습니다. 예시를 통해서 좀 더 자세히 알아보겠습니다.

```sql
CREATE TABLE server_logs(
   log_hour timestamp,
   log_level text,
   message text,
   server text,
   PRIMARY KEY (server)
)
```

만약에 `server` 컬럼 하나만 파티션 키를 지정했다면 어떻게 될까요? 서버의 로그는 시간이 지나면 계속 쌓이게 되는데 이러면 파티션 사이즈도 무한히 증가할 수 있다는 것을 뜻합니다.

<br>

```sql
CREATE TABLE server_logs(
   log_hour timestamp,
   log_level text,
   message text,
   server text,
   PRIMARY KEY ((log_hour, server))
)
```

이번에는 `log_hour`, `server`를 하나의 파티션 키로 지정 했다면 어떻게 될까요? 파티션은 시간 단위로 나뉘어 지게 될테고 파티션 사이즈의 무한한 증가를 막을 수 있습니다.

`partition skew`는 파티션에 할당된 데이터가 다른 파티션에 비해 더 많고 시간이 지남에 따라 파티션이 무한히 증가하는 조건을 의미하는데 이를 조심해서 설계해야 합니다.

```sql
CREATE TABLE server_logs(
   log_hour timestamp,
   log_level text,
   message text,
   server text,
   slot int
   PRIMARY KEY ((server, slot))
)
```

아니면 특정 컬럼을 추가하여, 특정 개수가 되면 파티션을 분배할 수 있도록 `slot` 이라는 개념을 추가하여 파티션을 분배할 수도 있고.. 등등 다양한 방법이 있을 것입니다.

<br>

### `Replication Factor`

데이터베이스나 Kafka 처럼 데이터를 처리하고 다루는 곳에서 복제본은 내결함성(`fault tolerance`) 및 고가용성(`high availability`)을 제공하기 때문에, `Replcation Factor` 개념은 매우 중요합니다.

카산드라에서도 데이터에 대한 Replication Factor를 설정할 수 있으며, 이는 클러스터 전체에 각 파티션의 복제본 수를 몇개 가지고 있을지 결정하는 옵션입니다.

![image](https://github.com/wjdrbs96/Today-I-Learn/assets/45676906/5508d68d-1b80-4ba5-8fbd-bd2235c79997)

만약에 `Replication Factor = 3` 이라면 위처럼 3개의 노드에 데이터를 복제해서 저장하게 됩니다. 만약에 해당 파티션을 담당하는 메인 노드가 다운되게 되면 복제되어 있는 노드에서 해당 파티션의 데이터를 응답하게 됩니다. 그리고 `카산드라는 백그라운드에서 데이터를 비동기적으로 복제합니다.` 

카산드라에서 복제 전략에는 아래와 같이 2가지가 존재합니다.

- `SimpleStrategy`: 하나의 데이터 센터와 하나의 rack 을 사용하고 있을 때 사용. 두 개 이상의 데이터 센터를 사용한다면 `NetworkTopologyStrategy` 전략을 사용해야 함
- `NetworkTopologyStrategy`: 향후 카산드라 확장이 필요할 때 여러 데이터 센터로 확장하기가 훨씬 쉽기 때문에 대부분의 구축에 권장되는 옵션

<br>

`데이터 복제`에 관한 자세한 것은 [여기](https://docs.datastax.com/en/cassandra-oss/3.0/cassandra/architecture/archDataDistributeReplication.html)를 참고하면 좋을 것 같습니다.

<br>

## `카산드라 클러스터에 미치는 파티션의 영향`

위에서 파티션의 크기는 10MB 이하를 유지하는 것이 이상적이라고 하였는데요. 이는 직관적으로? 단순하게? 생각해보아도, 하나의 파티션 점점 커진다면 해당 노드에서 데이터를 가져오는데 빠르게 효율적으로 가져오기 힘들어질 것입니다.

즉, 각 파티션에 저장된 데이터의 크기를 제어하는 것은 클러스터 전체에 데이터를 균등하게 분산하고 I/O 성능을 높이기 위해 필수적입니다. 

<br>

### `읽기 성능`

Cassandra는 Cache, Index, Index summaries 등등 디스크의 SStables 파일 내에서 파티션을 찾아서 데이터를 가져오는 내부적인 동작 과정이 있는데, 큰 파티션은 이러한 데이터 구조를 유지하는데 비효율적이고 성능 저하를 초래합니다. 이는 다른 글에서 다시 정리해보겠습니다.

<br>

### `Memory Usage`

파티션 크기는 JVM Heap 크기 및 카산드라 Garbage Collection 메커니즘에 직접적인 영향을 미칩니다. 파티션 사이즈가 크다면 Garbage Collection이 비효율적입니다.

카산드라의 내부 동작 및 메모리 사용에 대해 좀 더 자세하 알게 되면 더 자세히 이해가 될 것 같습니다. ([Link](https://docs.datastax.com/en/cassandra-oss/3.0/cassandra/operations/opsTuneJVM.html))

<br>

### `Casssandra Repair`

카산드라 `repair`는 데이터를 일관성 있게 만들 수 있는 것입니다. (자세한 것은 [여기](https://cassandra.apache.org/doc/stable/cassandra/operating/repair.html)를 참고)

`repair`는 노드간에 데이터 일관성이 깨졌을 때, 데이터를 스캔하여 다른 노드의 데이터 복제본과 비교한 후 데이텉 싱크를 맞추는 작업이라고 할 수 있습니다. 

만약에 파티션 크기가 크다면 `repair`를 통해서 데이터를 복구 및 싱크를 맞추는 작업이 부담이 될 수 있습니다.

<br>

### `Tombstone Eviction`

카산드라는 데이터 삭제 할 때 바로 삭제하지 않고 `tombstone` 표시를 남겨놓고, 나중에 삭제를 진행합니다. 이는 `Compaction` 전략이 적절하게 동작하지 않는다면 큰 파티션이 `tombstone` 제거할 때 어려움이 있을 수 있습니다.

`Tombstone`이 무엇인지는 [여기](https://www.instaclustr.com/support/documentation/cassandra/using-cassandra/managing-tombstones-in-cassandra/)와 [여기](https://docs.datastax.com/en/dse/5.1/docs/architecture/database-internals/architecture-tombstones.html) 글을 참고하면 좋을 것 같고 다른 글에서 `Compaction` 등등 개념들을 다루어 보도록 하겠습니다.

<br>

## `마무리 하며`

카산드라는 알면 알수록 재밌으면서 어렵고.. 많은 것을 알게되는 것 같습니다. 카산드라 Partition에 대해 가볍게 정리하려 했는데, 파티션과 연결되어 있는 여러가지 개념 및 용어들이 많이 나온거 같습니다. 앞으로 카산드라 용어 및 내부 동작들에 대해서 앞으로 하나씩 계속 정리해보겠습니다.  

<br>

## `Referenece`

- [https://docs.datastax.com/en/cassandra-oss/3.0/cassandra/architecture/archDataDistributeReplication.html](https://docs.datastax.com/en/cassandra-oss/3.0/cassandra/architecture/archDataDistributeReplication.html)
- [https://docs.datastax.com/en/cassandra-oss/3.x/cassandra/architecture/archDataDistributeHashing.html](https://docs.datastax.com/en/cassandra-oss/3.x/cassandra/architecture/archDataDistributeHashing.html)
- [https://www.baeldung.com/cassandra-replication-partitioning](https://www.baeldung.com/cassandra-replication-partitioning)
- [https://medium.com/@sadigrzazada20/partitioning-in-apache-cassandra-9eda05439a52](https://medium.com/@sadigrzazada20/partitioning-in-apache-cassandra-9eda05439a52)
- [https://sunitc.dev/2021/07/25/partitions-in-cassandra/](https://sunitc.dev/2021/07/25/partitions-in-cassandra/)
- [https://www.instaclustr.com/blog/cassandra-data-partitioning/](https://www.instaclustr.com/blog/cassandra-data-partitioning/)
- [https://www.baeldung.com/cassandra-keys](https://www.baeldung.com/cassandra-keys)
- [https://docs.datastax.com/en/dse/5.1/docs/architecture/database-internals/architecture-tombstones.html](https://docs.datastax.com/en/dse/5.1/docs/architecture/database-internals/architecture-tombstones.html)
- [https://www.instaclustr.com/support/documentation/cassandra/using-cassandra/managing-tombstones-in-cassandra/](https://www.instaclustr.com/support/documentation/cassandra/using-cassandra/managing-tombstones-in-cassandra/)
- [https://cassandra.apache.org/doc/stable/cassandra/operating/repair.html](https://cassandra.apache.org/doc/stable/cassandra/operating/repair.html)
- [https://docs.datastax.com/en/cassandra-oss/3.0/cassandra/operations/opsTuneJVM.html](https://docs.datastax.com/en/cassandra-oss/3.0/cassandra/operations/opsTuneJVM.html)