## `Cassandra Key`

### `primary key`

DB의 pk와 비슷하다. row를 유일무이하게 해주는 key를 의미한다. 1개 이상의 키가 필요하다.

<br>

### `partition key`

partition key는 primary key의 1번째 key(예시에서는 col1)를 의미한다. 저장소 row key로 직접 변환하고 해시 알고리즘에 따라 클러스터에 저장(분배)된다. 대부분의 질의는 partition key를 제공해서 카산드라는 요청된 데이터가 어느 노드에 있는지 알게 된다.

<br>

### `clustering key`

primary key의 1번째 key외 나머지 key를 clustering key(또는 clustering column)라 한다. 해당 key는 디스크에 데이터 순서를 안다. 하지만 어느 노드에 저장될지는 결정하지 않는다.

순서 관련해서 오름차순, 내림차순으로 변경할 수 있다.