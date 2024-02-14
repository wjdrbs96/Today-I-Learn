## `Cassandra는 데이터를 어떻게 쓸까?`

이번 글에서는 카산드라가 데이터를 저장(INSERT)할 때 내부적으로 어떤 과정이 있는지에 대해서 정리해보겠습니다.

![image](https://github.com/wjdrbs96/Today-I-Learn/assets/45676906/4d77b54f-c568-4fb1-b5a8-4f52f521b043)

- 커밋 로그(Commit log) 데이터 쓰기
- memtable 데이터 쓰기
- memtable 데이터 플러시(flush)
- SSTables Disk에 데이터 저장

카산드라에서 쓰기(INSERT) 작업이 발생하면 크게 위와 같이 4가지의 작업이 발생합니다. 그림에서 보면 memtable은 메모리에 있고, 커밋 로그, SSTable은 Disk에 있는 것을 알 수 있습니다.

- `Memtable` – Memtable은 메모리 내 캐시로, 키-값 쌍으로 저장되는 해시 테이블 구조입니다. Memtable이 정렬된 순서로 저장되며 설정한 크기를 초과하면 플러시(flush) 되어 Memtable에서 SSTable로 데이터가 이동합니다.
- `Commit Log` - Cassandra가 쓰기 작업을 처리할 때마다 데이터가 Memtable & Commit Log에 동시에 기록됩니다. Commit Log의 주요 목적 중에 하나는 노드가 다운될 경우를 대비하여 미리 디스크에 써놓고, 노드가 다시 살아나면 커밋 로그 기반으로 Memtable 데이터를 복구 시키기 위한 용도입니다. Memtable이 가득 차면 데이터가 SSTable로 플러시(flush) 되는데 이때 Commit Log의 데이터는 삭제됩니다.
- `SSTable(Sorted Strings Table)` – SSTable 키별로 정렬된 Key-Value 쌍의 파일로 Cassandra가 디스크의 데이터를 저장하는데 사용합니다. SSTable 파일은 불변이며, 각 SSTable에는 기본적으로 각 블록의 크기가 64KB 입니다.

각각 역할에 대해 간단하게 설명하면 위와 같습니다.

1. 커밋 로그(Commit log)와 memtable 데이터 쓰기 
2. memtable에 용량이 꽉차게 되면 flush 되어 memtable 데이터가 SSTable로 이동 (Commit log 데이터 삭제) 

그리고 알아두어야 할 점은 memtable 및 SSTables은 테이블마다 관리되고, 커밋 로그는 테이블 간에 공유하여 사용합니다. SSTables은 불변이라 변경할 수 없고 변경점이 있다면 새로운 SSTables에 기록됩니다.

추가로 memtable에서 flush 될 때 partition index도 디스크에 같이 저장되는데 이는 `모든 파티션 키의 인덱스를 저장`하는 것입니다. 나중에 `카산드라 어떻게 데이터를 읽어올까?` 라는 주제에서 `Partition Index`가 무엇인지 정리 해보겠습니다. 지금은 이런 것이 있구나 정도로 알아두면 좋을 것 같습니다.  

<br>

## `커밋 로그(Commit Log)는 사용하는 이유가 무엇일까?`

memtable 데이터를 flush 하여 디스크에 쓰면 이를 SSTables 라고 부릅니다. SSTables는 불변이기에 카산드라가 디스크에 쓰면 업데이트 되지 않는다는 것을 의미합니다.

그렇다면 만약에 memtable과 커밋 로그가 없다면 어떻게 될까요? 

카산드라에서 변경이 있을 때마다 SSTables에 쓰면 디스크에 매번 접근하는 것이기 때문에 Disk IO도 늘어나고 속도가 매우 느릴 것입니다.

그래서 카산드라는 좋은 성능을 위해서 모든 변경점에서 디스크에 접근하여 SSTables에 쓰는 대신, 메모리에 memtable을 두고 memtable이 가득 찼을 때만 flush 하여 디스크에 접근하여 Disk IO를 줄이도록 설계하였습니다.  

당연히 디스크에 매번 접근하는 것보다 모아서 한번에 접근하면 데이터를 저장할 때나 읽어올 때 모두 효율적일 것입니다. 

그러면.. 커밋 로그(Commit log)는 왜 필요한 것일까요? 커밋 로그는 노드가 다운되었을 때 데이터를 복구하기 위해서 필요합니다.

예를들어, 노드가 다운되어 메모리에 존재하던 memtable 데이터를 flush 하지 못했다면 SSTable에 저장되지 못했을 것이고, 메모리 데이터는 모두 유실될 것입니다. 

즉, 카산드라는 노드가 다운되어서 데이터가 유실되는 것을 방지하기 위해서 memtable과 커밋 로그(디스크)에 데이터를 같이 저장하는 구조를 사용하고 있습니다.



<br>

### `커밋 로그도 디스크에 저장하는 것인데 SSTable에 저장하는 것과 무엇이 다를까?`

CommitLog는 쓰기에 최적화되어 있습니다. 행을 정렬된 순서로 저장하는 SSTables와 달리 CommitLog는 Cassandra가 처리한 순서대로 업데이트를 저장합니다. CommitLog는 또한 모든 열 패밀리에 대한 변경사항을 단일 파일에 저장하므로 디스크가 여러 열 패밀리에 대한 업데이트를 동시에 받을 때 여러 번 검색할 필요가 없습니다.

기본적으로 디스크에 CommitLog를 쓰는 것이 SSTables를 쓰는 것보다 적은 데이터를 써야 하고 디스크의 한 곳에 모든 데이터를 쓰기 때문에 더 좋습니다.

Cassandra는 SSTables에 플러시된 데이터를 추적하고 특정 지점보다 오래된 모든 데이터가 기록되면 Commit 로그를 잘라낼 수 있습니다.

<br>

## `마무리 하며`

카산드라가 데이터를 어떻게 READ, UPDATE, DELETE 하는지, Compaction은 무엇인지 카산드라 전반적인 동작을 공부한다면, 카산드라는 왜 SSTable을 immutable 구조로 만들었는지와 같이 좀 더 깊은 내용에 대해서 잘 이해할 수 있을 것 같습니다.

이번 글에서도 얕고 가볍게 카산드라가 어떻게 쓰기 작업을 진행하는지 알아보았는데, 다음 글에서도 카산드라에 대해서 하나씩 정리해보도록 하겠습니다.

<br>

### `Reference`

- [https://copyprogramming.com/howto/what-is-the-purpose-of-cassandra-s-commit-log](https://copyprogramming.com/howto/what-is-the-purpose-of-cassandra-s-commit-log)
- [http://distributeddatastore.blogspot.com/2021/05/commit-log.html](http://distributeddatastore.blogspot.com/2021/05/commit-log.html)
- [https://cassandra.apache.org/_/blog/Learn-How-CommitLog-Works-in-Apache-Cassandra.html](https://cassandra.apache.org/_/blog/Learn-How-CommitLog-Works-in-Apache-Cassandra.html)
- [https://stackoverflow.com/questions/34592948/what-is-the-purpose-of-cassandras-commit-log](https://stackoverflow.com/questions/34592948/what-is-the-purpose-of-cassandras-commit-log)
- [https://nicewoong.github.io/development/2018/02/11/cassandra-internal/](https://nicewoong.github.io/development/2018/02/11/cassandra-internal/)
- [https://docs.datastax.com/en/cassandra-oss/3.x/cassandra/dml/dmlHowDataWritten.html](https://docs.datastax.com/en/cassandra-oss/3.x/cassandra/dml/dmlHowDataWritten.html)