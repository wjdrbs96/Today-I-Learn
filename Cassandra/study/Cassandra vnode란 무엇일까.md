## `Cassandra vnode란 무엇일까?`

카산드라에서 `node`라는 개념 하위에 `vnode` 라는 것이 존재하는데요. `vnode`는 무엇이며 카산드라에서 `vnode`를 사용하는 이유가 무엇인지 알아보겠습니다.

<br>

## `들어가기 전에`

Cassandra를 사용할 때 노드마다 데이터 분산을 균등하게 하기 위해서는 파티셔닝을 올바르게 진행해야 합니다. 혹시나 카산드라 토큰 할당 방식에 대해 모르고 있다면 [여기](https://devlog-wjdrbs96.tistory.com/443) 에서 토큰 할당 방법에 대해서 보고 오는 것을 추천드립니다.

카산드라 토큰의 범위는 `-2^63 to +2^63 -1` 이고,  Partitioner는 Murmur3Partitioner(default)를 사용하여 데이터 분배를 진행합니다. ([Link](https://docs.datastax.com/en/cassandra-oss/3.0/cassandra/architecture/archPartitionerAbout.html)) 

![image](https://github.com/wjdrbs96/Today-I-Learn/assets/45676906/1043b459-3006-450d-852e-47fa2e79d72d)

| node | start range           | end range             | partition key | hash value           |
|------|-----------------------|-----------------------|---------------|----------------------|
| A	   | 	-9223372036854775808 | 	-4611686018427387904 | johnny	       | -6723372854036780875 |
| B    | -4611686018427387903  | -1                    | jim           | -2245462676723223822 | 
| C    | 0                     | 4611686018427387903   | suzy          | 1168604627387940318  | 
| D    | 4611686018427387904   | 9223372036854775807	  | carol         | 7723358927203680754  |

즉, 위처럼 노드마다 토큰 범위를 가지게 되어 데이터를 저장하고 조회하는 영역을 담당하게 됩니다. 그러면 카산드라는 위처럼 토큰을 사용하면 문제 없을 것 같은데 `Vnode` 라는 개념은 왜 필요했고, 도입한 이유가 무엇일까요? 

<br>

## `Without vnodes`

<img width="545" alt="스크린샷 2024-02-12 오후 9 54 07" src="https://github.com/wjdrbs96/Today-I-Learn/assets/45676906/523d9d7e-0206-4987-a89f-429992521c77">

vnode가 없던 시절인 카산드라 버전 1.1 버전 이하에서는 노드당 `연속된 범위의 토큰 한 개`만 가질 수 있습니다.

```
num_tokens: 1
initial_token: -9223372036854775808
```

그리고 설정 파일인 `cassandra.yml`에서 [initial_token](https://cassandra.apache.org/doc/3.11/cassandra/configuration/cass_yaml_file.html#initial_token) 값에 노드마다 토큰 범위를 계산하고 수작업으로 할당하는 작업이 필요했습니다. ([Token 참고](https://docs.datastax.com/en/archived/cassandra/1.1/docs/initialize/token_generation.html)) 노드의 수가 많다면 하나씩 계산하여 설정 값을 세팅하는 것도 번거롭고 쉽지 않을 것입니다.

`그러면 이 상황에서 노드를 추가하거나 제거해야 한다면 어떻게 진행할 수 있을까요?` 이것을 알아보기 전에 `안정 해시(Consistent hashing)` 라는 것을 먼저 이해해야 합니다.

<br>

## `안정 해시(Consistent hashing)란?`

`안정 해시는 해시 테이블 크키가 조정될 때 평균적으로 오직 k/n개의 키만 재배치하는 해시 기술`입니다. (k는 키의 개수, n은 슬롯(slot)의 개수)

정의만 보면 이해가 잘 안될 것 같아 그림을 통해 좀 더 자세히 알아보겠습니다.

<br> 

<img width="948" alt="스크린샷 2024-02-12 오후 11 27 14" src="https://github.com/wjdrbs96/Today-I-Learn/assets/45676906/a35e4e09-6d1e-41fc-915e-64446a58b299">

카산드라 노드 3대가 존재하는데, 하나의 노드가 추가됐다면 전체 노드의 데이터를 리밸런싱 하는 것이 아니라 추가되는 노드의 토큰 범위 주변 노드들과 리밸런싱 작업만 발생한다는 뜻입니다. (간단히 설명하면 이러함)

즉, 3번 노드가 추가되면서 1번 노드의 토큰 범위가 절반으로 줄어들게 될 것입니다. 

<br>

<img width="611" alt="스크린샷 2024-02-12 오후 11 31 49" src="https://github.com/wjdrbs96/Today-I-Learn/assets/45676906/40cc3272-fe66-4198-9635-4a38b8b67ea8">

그리고 만약에 2번 서버를 제거해야 하는 상황이 됐다면 토큰의 범위가 엄청나게 불균형하게 되고 0번 노드에 많은 데이터가 쌓이게 되고 부하가 심해질 것입니다.

<br>

### `기본 구현법의 두 가지 문제`

- 서버와 키를 균등 분포 해시 함수를 사용해 해시 링에 배치한다.
- 키의 위치에서 링을 시계 방향으로 탐색하다 만나는 최초의 서버가 키가 저장될 서버다.

기존에 위처럼 접근하였는데 서버가 추가되거나 삭제되는 상황을 감안하면 파티션의 크기를 균등하는 게 불가능하다는 것입니다.

이것을 해결하기 위해 `Vnode` 라는 개념이 등장하였습니다.

<br>

## `vnode란?`

![image](https://github.com/wjdrbs96/Today-I-Learn/assets/45676906/79fb5846-dfcd-4964-b38c-94b34659a967)

카산드라에서는 1.2 버전에서 노드당 두 개 이상의 토큰을 가질 수 있도록 `Virtual Node(vnode)` 라는 개념을 도입하였습니다. 즉, 노드당 여러 개의 파티션을 관리해야 한다는 것을 의미합니다.

- 토큰은 자동으로 계산되어 각 노드에 할당된다.
- 노드를 추가하거나 제거할 때 클러스터의 재조정이 자동으로 수행된다.

`vnode`를 도입된 후 노드에 토큰을 수동으로 할당 했던 것을 자동으로 할당하게 됩니다. ([Link](https://docs.datastax.com/en/cassandra-oss/3.0/cassandra/architecture/archDataDistributeVnodesUsing.html))

<img width="780" alt="스크린샷 2024-02-12 오후 11 56 49" src="https://github.com/wjdrbs96/Today-I-Learn/assets/45676906/c70f855c-5c71-4adc-8932-fe8b4ac1b65d">

즉, 가상노드마다 토큰을 가지고 있는 구조가 될 것입니다. 이렇게 가상노드가 여러 개 존재할 때 서버가 추가되거나 삭제되면 주변 가상 노드와 토큰을 재분배하면 되기 때문에 이전에 단일 노드만 사용할 때보다 키가 좀 더 균일하게 분포될 것입니다.

표준 편차가 작아져서 데이터가 고르게 분포되기 때문에 가상 노드의 개수를 늘리면 키의 분포는 점점 더 균등해질 것입니다.

하지만 가상 노드 개수가 많아도 저장할 공간이 더 필요해지기 때문에 무작정 많다고 좋은 것은 아니고, 상황에 맞게 적절한 값을 찾아서 튜닝하는 것이 필요할 것 같습니다.

<br>

### `num_tokens`

위의 설정은 카산드라 노드에 랜덤하게 할당된 토큰의 수를 설정하는 값입니다. 즉, 하나의 노드에서 `vnode`를 몇개 가지고 있을지를 정하는 옵션이라고 할 수 있습니다.

- Cassandra 3.x Default Value: 256 ([Link](https://cassandra.apache.org/doc/3.11/cassandra/configuration/cass_yaml_file.html))
- Cassandra 4.x Default Value: 16 ([Link](https://cassandra.apache.org/doc/stable/cassandra/configuration/cass_yaml_file.html))

카산드라 4.0 버전이 되면서 `vnode` 디폴트 값이 16으로 변경 되었는데요. 256은 너무 많다는 얘기가 나왔는데 카산드라가 이를 반영한 것으로 보입니다. ([Link](https://issues.apache.org/jira/browse/CASSANDRA-13701))

<br>

## `안정 해시의 장점`

- 서버가 추가되거나 삭제될 때 재배치되는 키의 수가 최소화된다. 
- 데이터가 보다 균등하게 분포하게 되므로 수평적 규모 화장성을 달성하기 쉽다.
- 핫스팟 키 문제를 줄인다. 특장한 샤드에 대한 접근이 지나치게 빈번하면 서버 과부하 문제가 생길 수 있다.

<br>

## `마지막으로`

깊게 공부하면 할수록 어렵고 정리하기가 힘들어서 내용이 얕게 정리가 된 것 같지만.. 일단은 이정도만 정리하고 점점 관련 주변 지식 및 경험들을 더 쌓고 나중에 다시 보았을 때 더 넓고 깊게 보일 것 같아서 이정도로 정리를 마무리 해보려 합니다. 

<br>

### `Referenece`

- [가상 면접 사례로 배우는 대규모 시스템 설계 기초 5장 안정 해시]()
- [https://docs.datastax.com/en/cassandra-oss/3.0/cassandra/architecture/archDataDistributeHashing.html](https://docs.datastax.com/en/cassandra-oss/3.0/cassandra/architecture/archDataDistributeHashing.html)
- [https://docs.datastax.com/en/cassandra-oss/3.0/cassandra/architecture/archDataDistributeVnodesUsing.html](https://docs.datastax.com/en/cassandra-oss/3.0/cassandra/architecture/archDataDistributeVnodesUsing.html)
- [https://docs.datastax.com/en/cassandra-oss/3.0/cassandra/architecture/archDataDistributeDistribute.html](https://docs.datastax.com/en/cassandra-oss/3.0/cassandra/architecture/archDataDistributeDistribute.html)
- [https://www.baeldung.com/cassandra-cluster-datacenters-racks-nodes](https://www.baeldung.com/cassandra-cluster-datacenters-racks-nodes)
- [https://stackoverflow.com/questions/38423888/significance-of-vnodes-in-cassandra](https://stackoverflow.com/questions/38423888/significance-of-vnodes-in-cassandra)
- [https://cassandra.apache.org/doc/stable/cassandra/configuration/cass_yaml_file.html](https://cassandra.apache.org/doc/stable/cassandra/configuration/cass_yaml_file.html)
- [https://cassandra.apache.org/doc/3.11/cassandra/configuration/cass_yaml_file.html](https://cassandra.apache.org/doc/3.11/cassandra/configuration/cass_yaml_file.html)
- [https://cassandra.apache.org/doc/4.1/cassandra/configuration/cass_yaml_file.html](https://cassandra.apache.org/doc/4.1/cassandra/configuration/cass_yaml_file.html)
- [https://cassandra.apache.org/doc/stable/cassandra/getting_started/production.html#tokens](https://cassandra.apache.org/doc/stable/cassandra/getting_started/production.html#tokens)
- [https://thelastpickle.com/blog/2021/01/29/impacts-of-changing-the-number-of-vnodes.html](https://thelastpickle.com/blog/2021/01/29/impacts-of-changing-the-number-of-vnodes.html)
- [https://docs.datastax.com/en/archived/cassandra/1.1/docs/initialize/token_generation.html](https://docs.datastax.com/en/archived/cassandra/1.1/docs/initialize/token_generation.html)