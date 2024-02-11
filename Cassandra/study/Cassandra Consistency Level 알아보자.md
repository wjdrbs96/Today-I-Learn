## `Cassandra Consistency Level 알아보자`

이번 글에서는 카산드라 Write, Read 각각 `Consistency Level`에 대해서 어떤 특징이 있는지 알아보겠습니다.

<br>

### `Casssandra CAP 특징`

![image](https://github.com/wjdrbs96/Today-I-Learn/assets/45676906/58ac2c32-09cd-4c4b-ad69-cba5b0b966d7)

카산드라는 [CAP](https://en.wikipedia.org/wiki/CAP_theorem) 3가지 특징 중에 `AP` 시스템으로 `높은 가용성`과 `파티션 허용 오차`를 제공합니다. 상황에 따라 `CP`시스템으로 동작하도록 설정할 수도 있습니다.

즉, 카산드라에서는 `Wrtie`, `Read`의 `Consitency Level`을 어떤 값으로 설정하냐에 따라서 `AP` 시스템이 될 수도 있고, `CP` 시스템이 될 수도 있습니다.

<br>

### `Consistency Level 이란?`

일관성 수준은 `코디네이터(coordinator) 노드`가 `non-lightweight transaction`을 성공적으로 처리하기 위해 복제본에서 응답해야 하는 노드 수를 결정하는 옵션입니다. 

읽기 일관성 레벨은 클라이언트에게 데이터를 응답하기 전에 읽기 요청에 응답해야 하는 복제본 수를 지정합니다.

쓰기 일관성 레벨은 쓰기 요청이 카산드라에 성공적으로 반영되어 클라이언트에게 응답되기 전에 얼마나 많은 복제본이 쓰기 요청에 응답해야 하는지를 지정합니다.

<br>

### `Write Consistency Level`

| Level        | Description                                                                                           | Usage                                                                                                                     |
|--------------|-------------------------------------------------------------------------------------------------------|---------------------------------------------------------------------------------------------------------------------------|
| ALL          | 클러스터의 모든 복제 노드들의 commit log와 memtable에 쓰기 동작이 완료되어야 하는 level                                          | 가장 높은 일관성을 제공하지만, 가장 낮은 가용성을 제공                                                                                           |
| EACH_QUORUM  | 각 datacenter에서 QUORUM 만큼의 복제 노드들의 commit log와 memtable에 쓰기 동작이 완료되어야 하는 level                         | 특정 dc가 다운되면 QUORUM수를 만족하지 못해 쓰기 동작이 실패할 것이므로 각 datacenter를 같은 수준의 일관성으로 유지하고 싶을 때 사용할 수 있다.                                                 |
| QUORUM       | 모든 datacenter에서 QUORUM 만큼의 복제 노드들의 commit log와 memtable에 쓰기 동작이 완료되어야 하는 level                        | EACH_QUORUM의 경우 각 dc를 기준으로 하므로 dc1에서 1개, dc2에서 1개의 노드가 다운되었을 경우에만 정상 동작할 것이고 QUORUM은 dc1에서는 0개, dc2에서 2개의 노드가 다운되어도 정상 동작 |
| LOCAL_QUORUM | coordinator node가 존재하는 같은 datacenter에서 QUORUM 만큼의 복제 노드들의 commit log와 memtable에 쓰기 동작이 완료되어야 하는 level | 같은 dc 안에서 동작하기 때문에 dc간의 통신 지연을 방지할 수 있다.                                                                                  |
| ONE          | 적어도 1개의  replica 노드에 commit log와 memtable에 쓰기 동작이 완료되어야 하는 level                                      | 같은 dc 안에서 동작하기 때문에 dc간의 통신 지연을 방지할 수 있다.                                                                                  |
| TWO          | 적어도 2개의  replica 노드에 commit log와 memtable에 쓰기 동작이 완료되어야 하는 level                                      | ---                                                                                                                       |
| THREE        | 적어도 3개의  replica 노드에 commit log와 memtable에 쓰기 동작이 완료되어야 하는 level                                      | ---                                                                                                                       |
| LOCAL_ONE    | local datacenter에서 적어도 하나의 replica 노드에 commit log와 memtable에 쓰기 동작이 완료되어야 하는 level                    | multiple datacenter에서 보통 ONE level이 적절하지만, 운영 중이지 않은 데이터 센터의 노드가 운영 중인 데이터 센터의 노드에 자동으로 연결되지 않도록 함                        |
| ANY          | 모든 replica 노드들이 다운되어도 쓰기 동작이 가능한 level                                                                | 쓰기가 성공한 데이터는 복제본 노드들이 정상 동작할 때까지 읽을 수 없고, 가장 높은 가용성을 보장하지만 일관성은 가장 낮다.                                                    |

<br>

### `Read Consistency Level`

| Level        | Description                                                                       | Usage                                             |
|--------------|-----------------------------------------------------------------------------------|---------------------------------------------------|
| ALL          | 모든 복제본이 응답을 확인한 후 클라이언트에게 응답한다. 즉, 복제본이 응답하지 않으면 읽기 작업이 실패한다.                     | 가장 높은 일관성을 제공하지만, 가장 낮은 가용성을 제공한다.                |
| EACH_QUORUM  | 읽기에서 지원되지 않음                                                                      | ---                                               |
| QUORUM       | 모든 데이터 센터의 복제본 쿼럼이 응답한 후 레코드를 반환한다.                                               | ---                                               |
| LOCAL_QUORUM | coordinator node가 존재하는 같은 datacenter에서 QUORUM 만큼의 복제 노드들의 응답을 확인한 후 클라이언트에게 응답한다. | ---                                               |
| ONE          | 가장 가까운 1개의 replica 노드가 응답하는 경우 값을 반환하는 level                                      | 오래된 데이터를 읽어도 큰 문제가 없는 경우 가장 높은 가용성을 제공하는 level이다. |
| TWO          | 가장 가까운 2개의 replica 노드가 응답하는 경우 값을 반환하는 level                                      | ---                                               |
| THREE        | 가장 가까운 3개의 replica 노드가 응답하는 경우 값을 반환하는 level                                      | ---                                               |
| LOCAL_ONE    | local datacenter에서 가장 가까운 하나의 replica 노드가 응답하는 경우 값을 반환하는 level                   | ---                                               |
| SERIAL       | 모든 datacenter에서 QUORUM 만큼의 replica 노드에서 커밋 되지 않은 상태의 데이터를 읽을 수 있는 level           | 경량 트랜잭션을 실행하여 컬럼에 쓴 후에 컬럼의 최신 값을 읽으려면 SERIAL 사용   |
| LOCAL_SERIAL | SERIAL과 동일하나 local dc로 제한한다.                                                      | ---                                               |


<br>

### `How QUORUM is calculated`

```
quorum = (sum_of_replication_factors / 2) + 1
```

- ex) replication factors = 3인 경우 QUORUM = 2가 되므로 1개의 노드가 다운되어도 정상 동작

<br>

```
sum_of_replication_factors = datacenter1_RF + datacenter2_RF + . . . + datacentern_RF
```

- ex) 데이터 센터가 2개 이상일 때 replication factors = 3인 경우 QUORUM = 4가 되므로 2개 노드가 다운되어도 정상 동작

<br>

이번에는 그림을 통해서 `Write Consistency Level`에 대해서 조금만 더 알아보겠습니다.

<br>

## `Pictures Write Consistency Level`

### `CL = ALL`

![image](https://github.com/wjdrbs96/Today-I-Learn/assets/45676906/9c2f4c8c-0fdf-4d26-9dcc-5d9d28f14354)

일관성 레벨 `ALL`은 모든 복제본에서 응답해야 클라이언트에서 응답을 할 수 있기 때문에 카산드라에서 가장 높은 일관성을 제공하지만, 하나의 복제본이라도 실패한다면 클라이언트 응답에 실패하기 때문에 가용성은 낮다는 단점이 존재합니다.

카산드라의 큰 장점은 가용성이 높다는 것인데 일관성 레벨 `ALL`을 사용하면 가용성과 성능을 낮추기 때문에 카산드라를 제대로 사용한다 할 수 없을 것입니다.

가용성이 아닌 높은 일관성을 유지하는 것이 필요하다면 카산드라 보다는 RDB를 사용하는 것이 더 좋은 선택일 것입니다.

<br>

### `CL = EACH_QUORUM`

![image](https://github.com/wjdrbs96/Today-I-Learn/assets/45676906/5b3d91d3-38df-4e09-8d45-51887e2f054d)

모든 복제본 노드에서 응답해야 하는 것은 아니기 때문에 `ALL` 보다는 가용성이 좀 더 높다고 할 수 있습니다.

`EACH_QUORUM`는 데이터 센터마다의 동일한 일관성 유지를 목표로 하는데, 이 뜻은 `QUORUM = 2` 이기 때문에 각 데이터 센터에서 최소 2개 노드에서 응답해야 성공할 수 있다는 것을 의미합니다.

<br>

### `CL = QUORUM`

![image](https://github.com/wjdrbs96/Today-I-Learn/assets/45676906/f865c5ac-e81d-4fab-830d-460ea6d362b8)

`QUORUM`도 `EACH_QUORUM`가 동일하지만, 하나 다른 점이 존재합니다. `EACH_QUORUM`의 경우 `QUORUM = 2` 일 때 데이터 센터 각각에서 최소 2개의 노드씩 응답을 해야 성공할 수 있었습니다.

하지만 `QUORUM` 레벨은 `QUORUM = 2` 일 때 모든 데이터 센터 노드에서 최소 2개 이상만 응답하면 된다는 특징이 있습니다. (`QUORUM 일 때는 datacenter 2에서 replication factor = 1`)   

<br>

### `CL = LOCAL_QUORUM`

![image](https://github.com/wjdrbs96/Today-I-Learn/assets/45676906/65c4387d-66aa-4576-880c-e9f8eeec1348)

`LOCAL_QUORUM` 레벨은 같은 데이터 센터 복제본 노드에서만 응답하면 클라이언트에게 성공으로 응답할 수 있습니다.

또한 요청이 현재 데이터 센터의 경계를 벗어나지 않기 때문에 지연 시간이 줄어든다는 장점이 있습니다.

<br>

### `CL = ONE, TWO, THREE`

숫자 일관성 레벨은 숫자에 맞게만 복제본 노드에서 응답을 받으면 되는데, 복제본 수에 따라서 일관성이 높아질 수도 낮아질 수도 있다는 특징이 있습니다. 예를들어, `Replication Factor = 3`, `CL = TWO` 경우로 알아보겠습니다.

![image](https://github.com/wjdrbs96/Today-I-Learn/assets/45676906/a9184926-019d-4b15-9b2f-646f07404173)

위처럼 복제 노드가 3개라면, 3개 중에 2개만 응답하면 되기 때문에 일관성이 높은 편이라고 할 수 있습니다.

<br>

![image](https://github.com/wjdrbs96/Today-I-Learn/assets/45676906/a353447e-a84b-4e8e-aeb0-1576871a8f7e)

하지만 위처럼 복제본 수가 늘어난다면 5개 복제 노드 중에 2개만 응답 받으면 되기 때문에 일관성이 조금 떨어질 수 있다는 특징이 있습니다.

<br>

### `CL = LOCAL_ONE`

![image](https://github.com/wjdrbs96/Today-I-Learn/assets/45676906/79343d33-e2af-40cc-8700-539fafb06f71)

LOCAL_QUORM 처럼 일관성 검사는 로컬 데이터 센터에서만 적용됩니다. 하나의 복제본에 대해서만 일관성 체크를 하면 되기 때문에 가용성 높이고, 일관성을 낮추게 됩니다. 

SNS 리액션 수 같이 당장 일관성을 유지하는 것이 중요하지 않은 데이터에 낮은 일관성 수준을 사용하는 것이 좋습니다.

<br>

### `CL = ANY`

![image](https://github.com/wjdrbs96/Today-I-Learn/assets/45676906/dd158c79-33d9-4fd1-a994-2bf58ddb8968)

ANY 레벨은 복제본의 응답을 필요로 하지 않습니다. 또한 카산드라에 데이터를 저장하지 않고도 성공으로 응답할 수 있습니다. 

복제 노드가 다운되어 데이터를 처리하지 못한 경우 코디네이터 노드는 힌트(hint)를 저장합니다. 힌트(hint)가 만료 시간은 3시간이며, 이는 해당 시간 내에 복제 노드가 다시 정상 상태로 돌아오지 않으면 데이터가 손실됨을 의미합니다.

일관성 레벨 ANY는 카산드라에서 가장 높은 가용성과 가장 낮은 일관성을 제공합니다. 데이터 저장에 대한 보장이 없으므로 데이터 손실을 초래할 수 있으므로 운영 환경에서는 권장되지 않습니다.

<br>

## `Referenece`

- [https://docs.datastax.com/en/cassandra-oss/3.x/cassandra/dml/dmlAboutDataConsistency.html](https://docs.datastax.com/en/cassandra-oss/3.x/cassandra/dml/dmlAboutDataConsistency.html)
- [https://docs.datastax.com/en/cassandra-oss/3.0/cassandra/dml/dmlConfigConsistency.html](https://docs.datastax.com/en/cassandra-oss/3.0/cassandra/dml/dmlConfigConsistency.html)
- [https://www.pythian.com/blog/cassandra-consistency-level-guide](https://www.pythian.com/blog/cassandra-consistency-level-guide)
- [https://pompitzz.github.io/blog/Cassandra/Cassandra_Consistency_Level.html#write-consistency-levels](https://pompitzz.github.io/blog/Cassandra/Cassandra_Consistency_Level.html#write-consistency-levels)