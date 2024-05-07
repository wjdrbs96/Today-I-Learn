## `Cassandra Compaction이란 무엇일까`

카산드라는 `SSTable` 이라고 하는 파일에 데이터를 쓰고 저장한다. INSERT, UPDATE를 할 때 SSTable에 새로운 타임스탬프로 새로운 버전으로 저장된다.

카산드라는 데이터를 진짜 삭제하지는 않고, `tombstones` 이라는 표시를 통해서 삭제 되었다고 표시한다.

데이터가 많이 쌓이고 시간이 지나면서, 카산드라는 SStables에 Row의 여러 버전들을 기록할 수 있다. 각각의 버전은 서로 다른 타임스탬프와 함께 저장된 고유한 columns Set를 가질 수 있다. 

카산드라는 주기적으로 SStables를 병합하고 오래된 데이터를 폐기하는데, 이 과정을 `compaction` 이라고 한다.

- 정리 중

<br>

### `Referenece`

- [https://docs.datastax.com/en/cassandra-oss/3.x/cassandra/dml/dmlHowDataMaintain.html#dmlHowDataMaintain__dml-compaction](https://docs.datastax.com/en/cassandra-oss/3.x/cassandra/dml/dmlHowDataMaintain.html#dmlHowDataMaintain__dml-compaction)