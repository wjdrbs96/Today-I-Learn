## `Cassandra에서 Cluster, Datacenters, Racks and Nodes 란 무엇일까?`

이번 글에서는 Cassandra를 공부할 때 필요한 기본적인 용어가 어떤 것을 의미하는지 정도만 아주 가볍게 정리 해보겠습니다.

![image](https://github.com/wjdrbs96/Today-I-Learn/assets/45676906/79fb5846-dfcd-4964-b38c-94b34659a967)

그림에서 보면 `Cluster`, `DataCenter`, `Rack`, `Node`, `Vnode` 라는 용어들이 보이는데요. 각 용어들이 어떤 것을 의미하는지 알아보겠습니다. 

<br>

### `Cluster`

Cluster는 하나 이상의 데이터 센터를 포함합니다. 즉, Cluster 안에서 단일 데이터 센터를 운영할 수도 있고, 멀티 데이터 센터를 운영할 수도 있습니다.

<br>

### `Dataceneter`

데이터 센터 하나로 클러스터를 구축하여도 좋지만 서비스가 점점 커진다면 여러 데이터 센터를 구축을 통해서 안정성 등등 여러가지를 고려해야 할 것입니다.

Cassandra 에서도 Multi Datacenter를 구축할 수 있고, `Replication Factor` 설정을 통해서 여러 데이터 센터에 복제하여 저장하도록 설정할 수도 있어 데이터의 안정성 올리는데 사용할 수 있습니다.

<br>

### `Rack`

Apache Cassandra Rack은 서버들의 그룹화 해놓은 것입니다. 카산드라의 아키텍처는 하나의 Rack 안에 복제본이 중복되어 저장되지 않도록 하고, 하나의 Rack이 다운될 경우에 대비하여 다른 Rack을 통해 복제본이 전달될 수 있도록 합니다.

다른 글에서 Rack을 구성할 때 알아야 할 것, Rack이 왜 필요한지 같은 개념들에 대해 따로 정리해볼 예정입니다.

<br>

### `Node`

노드라는 것은 물리 서버 or EC2 인스턴스와 같은 가상 머신 같은 것을 의미합니다. 중요한 것은 모든 노드가 독립적이고 링에서 동일한 역할을 하고, 카산드라는 노드를 `peer-to-peer` 구조로 되어 있어서 모든 노드가 읽기 쓰기에 대한 것을 진행할 수 있습니다.

<br>

### `Virtual nodes`

서버당 Virtual nodes 기본 값은 256개 입니다. 각 노드에는 토큰 범위가 할당되는데, 모든 `Virtual node`가 자신이 속한 노드에서 토큰의 하위 범위를 사용합니다.

만약에 더 많은 부하가 걸린 노드가 존재한다면 가상 노드 수를 확장하여 스토리지 용량을 쉽게 확장할 수 있습니다.

<br>

### `계층 구조`

- Cluster
  - Data center(s)
    - Rack(s)
      - Server(s)
        - Node (more accurately, a vnode)

계층구조는 위와 같이 표현할 수 있습니다.

<br>

### `Referenece`

- [https://www.instaclustr.com/blog/cassandra-vnodes-how-many-should-i-use/](https://www.instaclustr.com/blog/cassandra-vnodes-how-many-should-i-use/)
- [https://stackoverflow.com/questions/38423888/significance-of-vnodes-in-cassandra](https://stackoverflow.com/questions/38423888/significance-of-vnodes-in-cassandra)
- [https://www.datastax.com/blog/virtual-nodes-cassandra-12](https://www.datastax.com/blog/virtual-nodes-cassandra-12)
- [https://www.baeldung.com/cassandra-cluster-datacenters-racks-nodes](https://www.baeldung.com/cassandra-cluster-datacenters-racks-nodes)
- [https://www.datastax.com/ko/blog/distributed-database-things-know-cassandra-datacenter-racks](https://www.datastax.com/ko/blog/distributed-database-things-know-cassandra-datacenter-racks)
- [https://stackoverflow.com/questions/28196440/what-are-the-differences-between-a-node-a-cluster-and-a-datacenter-in-a-cassand](https://stackoverflow.com/questions/28196440/what-are-the-differences-between-a-node-a-cluster-and-a-datacenter-in-a-cassand)
- [https://docs.datastax.com/en/cassandra-oss/3.0/cassandra/architecture/archDataDistributeVnodesUsing.html](https://docs.datastax.com/en/cassandra-oss/3.0/cassandra/architecture/archDataDistributeVnodesUsing.html)
- [https://docs.datastax.com/en/cassandra-oss/3.0/cassandra/architecture/archDataDistributeDistribute.html](https://docs.datastax.com/en/cassandra-oss/3.0/cassandra/architecture/archDataDistributeDistribute.html)
- [https://docs.apigee.com/private-cloud/v4.19.06/rack-support?hl=ko](https://docs.apigee.com/private-cloud/v4.19.06/rack-support?hl=ko)

