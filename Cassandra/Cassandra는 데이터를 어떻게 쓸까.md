## `Cassandra는 데이터를 어떻게 쓸까`

### `Logging writes and memtable storage`

> When a write occurs, Cassandra stores the data in a memory structure called memtable, and to provide configurable durability, it also appends writes to the commit log on disk. The commit log receives every write made to a Cassandra node, and these durable writes survive permanently even if power fails on a node. The memtable is a write-back cache of data partitions that Cassandra looks up by key. The memtable stores writes in sorted order until reaching a configurable limit, and then is flushed.

쓰기가 발생하면 Cassandra는 memtable이라는 메모리 구조에 데이터를 저장하고 구성 가능한 내구성을 제공하기 위해 `디스크의 커밋 로그`에도 쓰기를 추가합니다. 

커밋 로그는 Cassandra 노드에 대한 모든 쓰기를 저장합니다. 이러한 영구적인 쓰기(저장)는 노드에서 전원이 공급되지 않더라도 영구적으로 유지됩니다. `memtable은 카산드라가 키로 조회하는 데이터 파티션의 Write-Back 캐시입니다.`(Write-Back 캐시..?) `memtable 저장소`는 정해진 용량 제한에 도달할 때까지 정렬된 순서로 쓴 다음 `플러시`됩니다.

<br>

### `Flushing data from the memtable`

> To flush the data, Cassandra writes the data to disk, in the memtable-sorted order. 

> A partition index is also created on the disk that maps the tokens to a location on disk. When the memtable content exceeds the configurable threshold or the commitlog space exceeds the commitlog_total_space_in_mb, the memtable is put in a queue that is flushed to disk. The queue can be configured with the memtable_heap_space_in_mb or memtable_offheap_space_in_mb setting in the cassandra.yaml file. 

> If the data to be flushed exceeds the memtable_cleanup_threshold, Cassandra blocks writes until the next flush succeeds. You can manually flush a table using nodetool flushor nodetool drain (flushes memtables without listening for connections to other nodes). To reduce the commit log replay time, the recommended best practice is to flush the memtable before you restart the nodes. If a node stops working, replaying the commit log restores to the memtable the writes that were there before it stopped.

`memtable`의 데이터를 플러시하면 Cassandra는 memtable-sorted 순서로 데이터를 디스크에 쓰기(Write) 작업을 진행합니다.

토큰을 디스크의 위치에 매핑하는 파티션 인덱스가 디스크에 생성됩니다. memtable 내용이 구성 가능한 임계값을 초과하거나([참고 Link](https://docs.datastax.com/en/cassandra-oss/3.x/cassandra/operations/opsMemtableThruput.html)) 커밋 로그 공간이 [commitlog_total_space_in_mb](https://docs.datastax.com/en/cassandra-oss/3.x/cassandra/configuration/configCassandra_yaml.html#configCassandra_yaml__commitlog_total_space_in_mb)를 초과하면 memtable이 Disk로 플러시되는 대기열에 저장됩니다.

플러시할 데이터가 `memtable_cleanup_threshold`를 초과하면 다음 플러시가 성공할 때까지 Cassandra Block을 기록합니다. ` nodetool flushor nodetool drain`를 사용하여 수동으로 테이블을 플러시할 수 있습니다.(다른 노드에 대한 ` listening for connections` 없이 `memtable`을 플러시) 커밋 로그 `Replay` 시간을 줄이려면 노드를 다시 시작하기 전에 `memtable`을 플러시하는 것이 좋습니다. 노드가 작동을 중지하면 커밋 로그를 재생(Reply)하여 중지되기 전에 있었던 쓰기를 `memtable`에 복원합니다.

> Data in the commit log is purged after its corresponding data in the memtable is flushed to an SSTable on disk.

memtable의 해당 데이터가 Disk의 SST 테이블로 플러시된 후 커밋 로그의 데이터가 제거됩니다.

<br>

### `Storing data on disk in SSTables`

> Memtables and SSTables are maintained per table. The commit log is shared among tables. SSTables are immutable, not written to again after the memtable is flushed. Consequently, a partition is typically stored across multiple SSTable files. A number of other SSTable structures exist to assist read operations:

`Memtable` 및 `SSTables`은 테이블별로 유지 관리됩니다. 커밋 로그는 테이블 간에 공유됩니다. SSTables는 변경할 수 없으며, memtable이 플러시된 후 다시 기록되지 않습니다. 따라서 파티션은 일반적으로 여러 SSTable 파일에 걸쳐 저장됩니다. 읽기 작업을 지원하기 위해 다음과 같은 다양한 SSTable 구조가 존재합니다.

SSTables는 변경할 수 없으며, memtable이 플러시된 후 다시 기록되지 않습니다. 따라서 파티션은 일반적으로 여러 SSTable 파일에 걸쳐 저장됩니다. 읽기 작업을 지원하기 위해 다음과 같은 다양한 SSTable 구조가 존재합니다.

![image](https://github.com/wjdrbs96/Today-I-Learn/assets/45676906/4d77b54f-c568-4fb1-b5a8-4f52f521b043)

<br>

### `Data (Data.db)`
- The SSTable data

### `Primary Index (Index.db)`
- 데이터 파일의 위치에 대한 포인터가 있는 row keys Index
  - Index of the row keys with pointers to their positions in the data file

### `Bloom filter (Filter.db)`
- Disk의 SSTables에 액세스하기 전에 memtable에 행 데이터가 있는지 확인하는 메모리에 저장된 구조
  - A structure stored in memory that checks if row data exists in the memtable before accessing SSTables on disk

### `Compression Information (CompressionInfo.db)`
- 압축되지 않은 데이터 길이, 청크 오프셋 및 기타 압축 정보에 대한 정보가 들어 있는 파일
  - A file holding information about uncompressed data length, chunk offsets and other compression information

<br>

### `Reference`

- [https://docs.datastax.com/en/cassandra-oss/3.x/cassandra/dml/dmlHowDataWritten.html](https://docs.datastax.com/en/cassandra-oss/3.x/cassandra/dml/dmlHowDataWritten.html)