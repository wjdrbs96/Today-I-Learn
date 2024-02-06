## `Cassandra는 데이터를 어떻게 삭제할까?`

이번 글에서는 카산드라는 데이터 삭제를 어떻게 하고 `tombstone` 이 무엇인지 알아보겠습니다.



Cassandra의 데이터 삭제 프로세스는 성능을 향상시키고 데이터 분배?(distribution) 및 내결함성을 위해 Cassandra의 기본 제공 속성과 함께 작동하도록 설계되었습니다.

> Cassandra treats a delete as an insert or upsert. The data being added to the partition in the DELETE command is a deletion marker called a tombstone.

Cassandra는 삭제를 INSERT 또는 UPSERT로 처리합니다. DELETE 명령에서 파티션에 추가되는 데이터는 삭제 마커인 [tombstone](https://docs.datastax.com/en/glossary/docs/index.html#tombstone) 입니다.

#### `tombstone`
- A marker in a row that indicates a column was deleted. During compaction, marked columns are deleted.

> The tombstones go through Cassandra's write path, and are written to SSTables on one or more nodes. The key difference feature of a tombstone: it has a built-in expiration date/time. At the end of its expiration period (for details see below) the tombstone is deleted as part of Cassandra's normal compaction process.

`tombstones`은 카산드라의 `write path`에 하나 이상의 노드에 있는 SSTables에 기록됩니다. `tombstones`의 주요 차이점은 만료 날짜/시간이 내장되어 있다는 것입니다. 만료 기간이 끝나면 `tombstones`은 카산드라의 일반 [compaction](https://docs.datastax.com/en/cassandra-oss/3.x/cassandra/dml/dmlHowDataMaintain.html#dmlHowDataMaintain__dml-compaction) 프로세스의 일부로 삭제됩니다.

> In a multi-node cluster, Cassandra can store replicas of the same data on two or more nodes. This helps prevent data loss, but it complicates the delete process.

다중 노드 클러스터에서 Cassandra는 동일한 데이터의 복제본을 두 개 이상의 노드에 저장할 수 있습니다. 이렇게 하면 데이터 손실을 방지할 수 있지만 삭제 프로세스가 복잡해집니다.

> If a node receives a delete for data it stores locally, the node tombstones the specified record and tries to pass the tombstone to other nodes containing replicas of that record.

노드가 로컬에 저장된 데이터에 대한 삭제 요청을 받으면 노드는 지정된 레코드를 삭제하고 해당 레코드의 복제본을 포함하는 다른 노드에 `tombstone`을 전달하려고 시도합니다.

>  But if one replica node is unresponsive at that time, it does not receive the tombstone immediately, so it still contains the pre-delete version of the record.

그러나 하나의 복제본 노드가 응답하지 않는 경우, 해당 노드는 즉시 `tombstone`를 전달 받지 못하므로 레코드의 삭제 전 버전을 여전히 가지고 있습니다.

>  If the tombstoned record has already been deleted from the rest of the cluster befor that node recovers, Cassandra treats the record on the recovered node as new data, and propagates it to the rest of the cluster. This kind of deleted but persistent record is called a zombie.

해당 노드가 복구되기 전에 나머지 클러스터에서 `tombstone` 레코드가 이미 삭제된 경우, Cassandra는 복구된 노드의 레코드를 새 데이터로 처리하여 클러스터의 나머지 부분에 전파합니다. 이렇게 데이터가 삭제되었지만 저장되어 있는 데이터를 `Zombie`라고 합니다.

> To prevent the reappearance of zombies, Cassandra gives each tombstone a grace period. The purpose of the grace period is to give unresponsive nodes time to recover and process tombstones normally. 

좀비의 재등장을 막기 위해, 카산드라는 각 `tombstone`에 유예 기간을 줍니다. 유예 기간의 목적은 응답하지 않는 노드가 정상적으로 tombstoned을 복구하고 처리할 수 있는 시간을 주기 위한 것입니다.

> If a client writes a new update to the tombstoned record during the grace period, Cassandra overwrites the tombstone. If a client sends a read for that record during the grace period, Cassandra disregards the tombstone and retrieves the record from other replicas if possible.

클라이언트가 유예 기간 동안 tombstoned 레코드에 새 업데이트를 쓰면 카산드라가 tombstoned을 덮어씁니다. 클라이언트가 유예 기간 동안 해당 레코드에 대한 읽기를 전송하는 경우, Cassandra는 tombstoned을 무시하고 가능한 경우 다른 복제본에서 레코드를 검색합니다.

> The grace period for a tombstone is set by the property gc_grace_seconds. Its default value is 864000 seconds (ten days). Each table can have its own value for this property.

`gc_grace_seconds` 속성으로 설정됩니다. 기본값은 `864000초(10일)`입니다. 각 테이블은 이 속성에 대한 고유한 값을 가질 수 있습니다.

<br>

### `Details`

> The expiration date/time for a tombstone is the date/time of its creation plus the value of the table property gc_grace_seconds.

`tombstone`의 만료 날짜/시간은 `tombstone` 생성 날짜/시간에 `gc_grace_seconds` 테이블 속성 값을 더한 값입니다.

<br>

### `Referenece`

- [https://blog.voidmainvoid.net/469](https://blog.voidmainvoid.net/469)
- [https://docs.datastax.com/en/cassandra-oss/3.x/cassandra/dml/dmlAboutDeletes.html](https://docs.datastax.com/en/cassandra-oss/3.x/cassandra/dml/dmlAboutDeletes.html)
- [https://docs.datastax.com/en/cassandra-oss/3.x/cassandra/operations/opsRepairNodesHintedHandoff.html](https://docs.datastax.com/en/cassandra-oss/3.x/cassandra/operations/opsRepairNodesHintedHandoff.html)
- [https://thelastpickle.com/blog/2016/07/27/about-deletes-and-tombstones.html](https://thelastpickle.com/blog/2016/07/27/about-deletes-and-tombstones.html)