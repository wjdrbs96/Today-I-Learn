## `Cassandra는 데이터를 어떻게 쓸까?`

이번 글은 잘못된 정보 또는 뇌피셜 정보가 많이 섞여 있을 수 있음을 미리 말씀드리면서, 카산드라가 데이터를 저장(INSERT)할 때 내부적으로 어떤 과정을 거치는지에 대해서 정리해보겠습니다.  

![image](https://github.com/wjdrbs96/Today-I-Learn/assets/45676906/4d77b54f-c568-4fb1-b5a8-4f52f521b043)

- 커밋 로그(Commit log) 데이터 쓰기
- memtable 데이터 쓰기
- memtable 데이터 플러시(flush) => memtable이 설정한 용량을 넘었을 경우
- SSTables Disk에 데이터 저장

카산드라에서 쓰기(INSERT) 작업이 발생했을 때 위와 같은 동작이 존재합니다. 그림에서 보면 memtable은 메모리에 있고, 커밋 로그, SSTable은 Disk에 있는 것을 알 수 있습니다. 각 특징은 아래와 같습니다.

- `Memtable` – Memtable은 메모리에서 정렬된 순서로 저장되며 설정한 크기를 초과하면 플러시(flush) 되어 Memtable에서 SSTable로 데이터가 이동
- `Commit Log`:
  - Cassandra가 쓰기 작업을 처리할 때마다 데이터가 Memtable & Commit Log에 동시에 기록
  - Commit Log의 주요 목적 중에 하나는 노드가 다운될 경우를 대비하여 미리 디스크에 써놓고, 노드가 다시 살아나면 커밋 로그 기반으로 Memtable 데이터를 복구 시키기 위함
  - Memtable이 가득 차면 SSTable로 플러시(flush) 되는데 이때 Commit Log의 데이터는 삭제
- `SSTable(Sorted Strings Table)` 
  – `SSTable Partition Key 기준으로 정렬하여` Cassandra가 디스크의 데이터를 저장 
  - `SSTable 파일은 불변`이며, 각 SSTable에는 기본적으로 각 블록의 크기가 64KB 

memtable 및 SSTables은 테이블마다 관리되고, 커밋 로그는 테이블 간에 공유하여 사용합니다. 

SSTables은 `불변`이라는 특징으 중요합니다. 즉, SSTable은 변경할 수 없고 변경점이 있다면 새로운 SSTables에 기록됩니다. `Memtable`, `SSTable`을 보면 `정렬된` 순서로 저장한다는 특징이 있는데, 이러한 특징 때문에 카산드라가 순차 조회에 장점이 있다고 할 수 있습니다. 

<br>

## `커밋 로그(Commit Log)가 필요한 이유가 무엇일까?`

memtable 데이터를 flush 하여 디스크에 쓰면 이를 SSTables 라고 부르고, SSTables는 불변이기에 카산드라가 디스크에 쓰면 업데이트 되지 않고 새로운 SSTable을 만든다고 하였는데요.

`카산드라는 데이터를 바로 SSTable에 저장하지 않고, memtable에 저장하고 flush 할 때 SSTable에 저장하는 이유가 무엇일까요?` 

memtable 없이 SSTable만 사용한다면 변경이 있을 때마다 SSTables에 데이터를 저장 하기 위해 디스크에 매번 접근하게 될 것이고, Disk IO가 계속 발생하기 때문에 속도가 매우 느리고 카산드라는 쓰기에 장점을 가질 수 없을 것입니다.

그래서 카산드라는 좋은 성능 및 효율적으로 동작하기 위해서 모든 변경점에서 디스크에 접근하여 SSTables에 쓰는 대신, 메모리에 memtable을 두고 memtable이 가득 찼을 때만 flush 하여 Disk IO를 줄이도록 설계 하였습니다.   

그렇다면 `커밋 로그(Commit log)는 왜 필요한 것일까요?` 커밋 로그가 없을 때 어떤 일이 발생할 수 있을지 가정 해보겠습니다.

만약에 커밋 로그 없고 `memtable`, `SSTable`만 존재한다면, 노드에 장애가 발생했을 때 해당 노드 memtable 데이터는 어떻게 될까요? 메모리에 있는 데이터이기 때문에 유실될 수 있다는 크리티컬한 이슈가 존재합니다.

즉, 커밋 로그는 디스크에 저장하여 노드가 다운될 때와 같이 문제가 생겼을 때 memtable 데이터(메모리에 있고 SSTable flush 하지 못한)를 복구하기 위해서 필요합니다.

그리고 위에서 설명한 것처럼 `memtable 데이터가 flush 되면 SSTable에 저장되고 commit log 데이터는 삭제`됩니다.

<br>

### `커밋(Commit log) 로그와 SSTable의 차이점`

그러면 `커밋 로그도 디스크에 접근하여 저장하는 것이니 SSTable 처럼 Disk IO가 많이 발생하니 속도가 느리지 않을까?` 라는 의문을 가질 수 있습니다.   

어떤 점이 다르기에 커밋 로그는 SSTable에 접근하는 것보다 좋은 성능을 가지고 있는 것일까요?

커밋 로그가 필요한 목적에서 정리한 것처럼 데이터를 영구적으로 저장하기 위한 목적이 아니라, memtable 데이터를 SSTable에 저장하기 전에 가지고 있을 용도이기 때문에 쓰기에 최적화되도록 설계되어 있습니다. 

- SSTable은 정렬된 순서로 저장하고, 불변이기에 변경이 발생하면 새로운 SSTable이 생성된다. 
- 커밋 로그는 정렬하지 않고 카산드라가 처리한 순서대로 저장하고, 단일 파일에 저장한다.

변경이 있을 때마다 새로운 SSTable을 만들고 디스크 접근이 많아질 수 있지만, 커밋 로그는 디스크의 한 곳에 모든 데이터를 쓰기 때문에 더 효율적이라 할 수 있습니다. 정리하면 SSTable은 랜덤 IO가 발생하는데, 커밋 로그에 쓰는 순차 IO가 발생하여 더 효율적이라고 생각합니다.

커밋 로그에 대해 자세히 알고 싶다면 [여기](https://cassandra.apache.org/_/blog/Learn-How-CommitLog-Works-in-Apache-Cassandra.html)를 참고하면 좋을 것 같습니다.

`여러 개의 SSTable이 생기면 카산드라는 데이터를 어떻게 유지하는 것인지?` 에 대해서 다음 글에 정리 해보겠습니다.

<br>

## `마무리 하며`

이번 글에서는 카산드라가 데이터를 어떻게 WRITE 하는지에 대해서만 알아보았습니다. 카산드라가 데이터를 어떻게 READ, UPDATE, DELETE 하는지, Compaction은 무엇인지에 대해서 공부하다보면 카산드라에 대해서 좀 더 깊은 이해를 할 수 있을 것 같습니다.

얕고 가볍게 카산드라가 어떻게 쓰기 작업을 진행하는지 알아보았는데, 다음 글에서도 카산드라에 대해서 하나씩 정리해보도록 하겠습니다.

<br>

### `Reference`

- [https://copyprogramming.com/howto/what-is-the-purpose-of-cassandra-s-commit-log](https://copyprogramming.com/howto/what-is-the-purpose-of-cassandra-s-commit-log)
- [http://distributeddatastore.blogspot.com/2021/05/commit-log.html](http://distributeddatastore.blogspot.com/2021/05/commit-log.html)
- [https://cassandra.apache.org/_/blog/Learn-How-CommitLog-Works-in-Apache-Cassandra.html](https://cassandra.apache.org/_/blog/Learn-How-CommitLog-Works-in-Apache-Cassandra.html)
- [https://stackoverflow.com/questions/34592948/what-is-the-purpose-of-cassandras-commit-log](https://stackoverflow.com/questions/34592948/what-is-the-purpose-of-cassandras-commit-log)
- [https://nicewoong.github.io/development/2018/02/11/cassandra-internal/](https://nicewoong.github.io/development/2018/02/11/cassandra-internal/)
- [https://docs.datastax.com/en/cassandra-oss/3.x/cassandra/dml/dmlHowDataWritten.html](https://docs.datastax.com/en/cassandra-oss/3.x/cassandra/dml/dmlHowDataWritten.html)