## `Kafka 처음 사용할 때 알면 좋은 것들`

이 글은 제가 Kafka를 사용하면서 겪은 경험보다는 `Kafka를 공부하면서 처음 사용할 때 알면 좋은 것들의 이론`에 대해 정리한 글입니다. 참고한 곳은 맨 아래에 있습니다.

- [Lag 란?]()
- [Topic, Partition 이란?]()
- [Replication Factor란?]()
- [리더 팔로워란?]()
- [ISR 이란?]()

<br>

### `Cluster, Broker란?`

<img width="487" alt="스크린샷 2023-01-07 오후 10 02 47" src="https://user-images.githubusercontent.com/45676906/211151988-bbd39323-3010-4e20-9417-9021045d50b4.png">

Kafka Broker가 모여서 Cluster를 이룬다고 할 수 있다.(쉽게 말하면 Broker는 하나의 서버라고 할 수 있다.)

<br>

### `Lag란?`

<img width="1023" alt="스크린샷 2023-01-07 오후 4 25 41" src="https://user-images.githubusercontent.com/45676906/211139263-ccb27719-6b14-42d0-9fea-b41283461794.png">

만약 Producer가 데이터를 넣는 속도가 Consumer가 데이터를 소비하는 속도보다 빠르다면 컨슈머가 마지막으로 읽은 offset과 Producer가 마지막으로 넣은 offset의 차이가 발생한다. 이 차이를 `Consumer Lag` 이라고 한다.

Kafka에서 Lag 값을 통해 Producer, Consumer의 상태를 유추할 수 있다. 즉, Lag이 높다면 Consumer에 문제가 있다는 뜻일 수 있다.

<br>

#### `Lag 값 확인하기`



<br>

### `Topic, Partition 이란?`

<img width="1059" alt="스크린샷 2023-01-07 오후 4 28 28" src="https://user-images.githubusercontent.com/45676906/211139367-b25497be-eee7-4e59-a8ee-c598da0eacd0.png">

- Kafka는 여러 개의 Topic을 가질 수 있다.
- Topic 안에 여러 개의 파티션을 가질 수 있다.(즉, 파티션이란 토픽을 분할한 것이라 할 수 있다.)

<br>

### `레코드란(Record)?`

파티션 안에 프로듀서가 보낸 데이터가 존재하는데 이것을 `레코드(Record)` 라고 한다.
  
- 레코드는 `타임스탬프`, `메세지 키`, `메세지 값`, `오프셋`, `헤더`로 구성되어 있고, 프로듀서가 생성한 레코드가 브로커로 전송되면 오프셋과 타임스탬프가 지정되어 저장된다.
- 브로커에 한번 적재된 레코드는 수정할 수 없고 `로그 리텐션 기간 또는 용량`에 따라서만 삭제된다.
- `메세지 키는 메세지 값을 순서대로 처리하거나 메세지 값의 종류를 나타내기 위해 사용한다.`
  - `메세지 키를 사용하면 프로듀서가 토픽에 레코드를 전송할 때 메세키 키의 해시값을 토대로 파티션을 지정한다.`
  - `즉, 메세지 키를 지정하면 동일한 파티션에 들어가게 된다. 다만, 어떤 파티션에 지정될지는 모르고 파티션의 개수가 변경되면 메세지 키와 파티션 매칭이 달라지게 되므로 주의해야 한다.`
  - <img width="497" alt="스크린샷 2023-01-07 오후 11 38 58" src="https://user-images.githubusercontent.com/45676906/211156184-a4cd08d5-e7e2-42b4-a551-ca19c45c6786.png">

- 메세지 키와 메세지 값은 직렬화되어 브로커로 전송되기 때문에 컨슈머가 이용할 때는 직렬화한 형태와 동일한 형태로 역직렬화를 해야 한다.
  - <img width="670" alt="스크린샷 2023-01-07 오후 11 43 58" src="https://user-images.githubusercontent.com/45676906/211156393-140ad193-5879-4d75-8b3e-eb8fc23b3ce4.png">
  - <img width="845" alt="스크린샷 2023-01-07 오후 11 43 22" src="https://user-images.githubusercontent.com/45676906/211156366-487c7263-8880-4ea3-9ee5-2a2b62961434.png">

<br>

### `컨트롤러(Contoller)`

Kafka 클러스터의 다수 브로커 중 한 대가 컨트롤러 역할을 한다. 컨트롤러는 다른 브로커들의 상태를 체크하고 브로커가 클러스터에서 빠지는 경우 해당 브로커에 존재하는 `리더 파티션을 재분배`한다.

<br>

### `코디네이터(coodinator)`

클러스터의 다수 브로커 중 한 대는 코디네이터 역할을 수행한다. 코데네이터는 컨슈머 그룹의 상태를 체크하고 파티션을 컨슈머와 매칭하도록 분배하는 역할을 한다. 이렇게 파티션을 컨슈머로 재할당하는 과정을 `리밸런스` 라고 한다.

<br>

### `데이터 삭제`

카프카는 다른 메세징 플롯팸과 다르게 컨슈머가 데이터를 가져가더라도 토픽의 데이터는 삭제되지 않는다. 컨슈머, 프로듀서가 데이터 삭제 요청을 할 수 없고 요청 브로커만이 데이터를 삭제할 수 있다.

데이터 삭제는 파일 단위로 이루어지는데 기본 값은 1GB 이다. 1GB 용량에 도달하면 데이터를 삭제하게 된다. 

[retention.bytes](https://docs.confluent.io/platform/current/installation/configuration/topic-configs.html#retention-bytes), [retention.ms](https://docs.confluent.io/platform/current/installation/configuration/topic-configs.html#retention-ms) 옵션을 참고하자.

<br>

### `Replication Factor란?`

- 브로커에게 `리플리케이션 팩터 수 만큼 토픽안의 파티션들을 복제`하도록 하는 설정 값이다.
- 복제본이 생기기 때문에 리더와 팔로워 개념이 존재하고, 가장 중요한 것은 모든 읽기와 쓰기가 리더를 통해서만 일어난다는 것이다.

<br>

### `리더, 팔로워란?`

<img width="816" alt="스크린샷 2023-01-07 오후 4 53 19" src="https://user-images.githubusercontent.com/45676906/211140249-e31556b5-ebf6-443b-a455-e3d08ab82940.png">

- 리더는 모든 데이터의 읽기 쓰기 작업을 처리히고, 팔로워는 리더를 주기적으로 보면서 자신에게 없는 데이터를 리더로부터 주기적으로 가져오는 방법으로 리플리케이션을 유지한다.

<br>

### `ISR 이란?`

프로듀서가 리더에게 메세지를 발행하면 팔로워가 해당 메세지를 복제한다. 그런데 팔로워에 문제가 생겨서 데이터 복제가 제대로 되지 않은 상황에서 리더가 문제가 생기면 `데이터 정합성`에 문제가 생긴다.

Kafka 에서는 이러한 현상을 막기 위해 `ISR` 이라는 개념이 존재한다.

<img width="786" alt="스크린샷 2023-01-07 오후 4 57 14" src="https://user-images.githubusercontent.com/45676906/211140369-36f84961-662a-4c93-b636-80bd4109db57.png">

- Producer가 리더에게 메세지를 보내면 팔로워가 리더로부터 메세지를 복제하게 된다.

<br>

<img width="849" alt="스크린샷 2023-01-07 오후 5 01 35" src="https://user-images.githubusercontent.com/45676906/211140503-da38b674-9e78-4cb6-a936-3641256951ab.png">

- Broker 2 팔로워는 복제를 했지만, `Broker 3 팔로워에는 문제가 생겨 복제를 하지 못했다.`

<br>

<img width="805" alt="스크린샷 2023-01-07 오후 5 14 18" src="https://user-images.githubusercontent.com/45676906/211140935-8450b400-bdd9-402c-a119-7b7f2a8d587c.png">

- 메세지를 받은 후에 리더 브로커는 Producer 에게 `ack`를 응답으로 돌려준다.  

<br>

<img width="810" alt="스크린샷 2023-01-07 오후 5 03 11" src="https://user-images.githubusercontent.com/45676906/211140553-6bf78afc-a3c3-42f2-a258-87cd281a7800.png">

- [replica.lag.time.max.ms](https://docs.confluent.io/platform/current/installation/configuration/broker-configs.html#replica-lag-time-max-ms) 값 만큼 확인 요청이 오지 않는다면, 문제가 있는 팔로워를 `ISR` 그룹에서 제거한다.

<br>

### `Producer 주요 옵션`

- [bootstrap.servers](https://kafka.apache.org/documentation/#producerconfigs_bootstrap.servers)
  - 카프카 클러스터에 처음 연결을 하기 위한 호스트와 포트 정보로 구성된 리스트 정보를 나타낸다.

- [acks](https://kafka.apache.org/documentation/#producerconfigs_acks)
  - 프로듀서가 카프카 토픽의 리더에게 메세지를 보낸 후 요청을 완료하기 전 ack(승인) 수
  - `ack=0`: 프로듀서는 서버로부터 어떠한 ack도 기다리지 않는다. ack 요청을 기다리지 않기 때문에 매우 빠르게 메세지를 보낼 수 있지만 메세지가 손실될 가능성이 높다.
  - `ack=1`: 리더는 데이터를 기록하지만, 모든 팔로워는 확인하지 않기 때문에 메세지 손실이 발생할 수도 있다.
  - `ack=all`: 리더는 ISR의 팔로워로부터 데이터에 대한 ack를 기다리기 때문에, 팔로워가 있는 한 데이터 무손실에 대해 강력하게 보장할 수 있다. 완벽하게 사용하기 위해서는 `Broker 설정`도 같이 조정해야 한다.
  - `기본 깂: all`

<br>

### `ack=all과 브로커의 min.insync.replicas 옵션의 관계`

[min.insync.replicas](https://kafka.apache.org/documentation/#brokerconfigs_min.insync.replicas): 성공적인 것으로 간주되는 메세지에 쓰기를 승인해야 하는 `최소 복제본 수`를 지정하는 옵션이다. 즉, `최소 리플리케이션을 유지해야 하는 수`라고 할 수 있다.

<br>

#### `ack=all, min.insync.replicas=1`

<img width="610" alt="스크린샷 2023-01-07 오후 5 32 48" src="https://user-images.githubusercontent.com/45676906/211141530-4f964427-8d8d-4760-92af-73af12bddd36.png">

`min.insync.replicas` 옵션이 `1` 이기 때문에, 프로듀서가 리더에게 메세지를 보냈을 때 리더는 최소 1개의 브로커에게만 메세지를 잘 받았는지 확인하면 된다. 즉, 리더 자신 하나가 잘 받았기 때문에 바로 ack를 응답한다.

<br>

#### `ack=all, min.insync.replicas=2`

<img width="809" alt="스크린샷 2023-01-07 오후 5 43 03" src="https://user-images.githubusercontent.com/45676906/211141989-3d3b4e54-491d-4259-a167-67fab09ab38e.png">

1. 프로듀서는 메세지를 리더에게 보낸다.
2. 리더는 메세지를 받은 후에 저장하고, 팔로워는 해당 메세지를 가져와 저장한다.
3. 리더는 팔로워에게 메세지가 잘 복제되었는지 확인한다. `min.insync.replicas` 옵션이 `2`이기 때문에 `리더 1개`, `팔로워 1개`만 확인한다.
4. 리더는 acks 응답을 프로듀서에게 보낸다.

<br>

#### `ack=all, min.insync.replicas=3`

<img width="837" alt="스크린샷 2023-01-07 오후 5 49 21" src="https://user-images.githubusercontent.com/45676906/211142238-ef4c58a3-d488-41f5-9ebb-4663d9643feb.png">

1. 프로듀서는 메세지를 리더에게 보낸다.
2. 리더는 메세지를 받은 후에 저장하고, 팔로워는 해당 메세지를 가져와 저장한다.
3. 리더는 팔로워에게 메세지가 잘 복제되었는지 확인한다. `min.insync.replicas` 옵션이 `3`이기 때문에 `리더 1개`, `팔로워 2개`에 대해서 확인한다.
4. 리더는 acks 응답을 프로듀서에게 보낸다.

<br>

#### `ack=all, min.insync.replicas=2 설정을 권장하는 이유`

[Kafka 문서](https://www.conduktor.io/kafka/kafka-topic-configuration-min-insync-replicas)를 확인해보면 `ack=all 일 때, min.insync.replicas=2` 설정을 권장하고 있다.

![image](https://user-images.githubusercontent.com/45676906/211142618-ef14ff83-a7c2-42c6-bd80-b5d8c6c45a97.png)

`min.insync.replicas` 옵션이 `2` 라면 위와 같이 하나의 브로커가 문제가 생겨도 클러스터 전체 장애로 이어지지 않는다. 

하지만 `min.insync.replicas` 옵션을 `3`을 사용했을 때는 하나의 브로커에 문제가 생겨도 클러스터 전체 장애로 이어지게 된다.

그렇기 때문에 `ack=all`를 사용할 때 브로커 설정 `min.insync.replicas=2` 옵션 사용을 권장하고 있다.

acks와 관련된 자세한 것은 [여기](https://www.conduktor.io/kafka/kafka-topic-configuration-min-insync-replicas)에서 확인할 수 있다.

<br>

### `Consumer 대표 옵션`

- [group.id](https://kafka.apache.org/documentation/#connectconfigs_group.id)
  - 컨슈머가 속한 컨슈머 그룹을 식별하는 식별자이다. group id는 매우 중요하다.

- [enable.auto.commit](https://kafka.apache.org/documentation/#consumerconfigs_enable.auto.commit)
  - 백그라운드로 주기적으로 오프셋을 커밋한다.
  - 기본 값: `true` 

- [auto.offset.reset](https://kafka.apache.org/documentation/#consumerconfigs_auto.offset.reset)
  - Kafka에서 초기 오프셋이 없거나 현재 오프셋이 더 이상 존재하지 않은 경우(데이터가 삭제)에 다음 옵션으로 리셋한다.
  - `earliest`: 가장 초기의 오프셋값으로 설정한다.
  - `latest`: 가장 마지막의 오프셋값으로 설정한다.(기본 값)
  - `none`: 이전 오프셋값을 찾지 못하면 에러를 나타낸다.

- [max.poll.records](https://kafka.apache.org/documentation/#consumerconfigs_max.poll.records)
  - 단일 호출 poll()에 대한 최대 레코드 수를 조정한다.
  - 기본 값은 500 이다.

- [max.poll.interval.ms](https://kafka.apache.org/documentation/#consumerconfigs_max.poll.interval.ms)
  - 해당 옵션 시간 만큼 컨슈머 그룹에서 컨슈머가 살아 있지만 poll() 메소드를 호출하지 않을 때, 장애라고 판단하여 컨슈머 그룹에서 제외한 후 다른 컨슈머가 해당 파티션에서 메세지를 가져가게 한다.
  - `기본 값: 5분`

- [auto.commit.interval.ms](https://kafka.apache.org/documentation/#consumerconfigs_auto.commit.interval.ms)
  - 주기적으로 오프셋을 커밋하는 시간
  - `기본 값: 5초`

<br>



### `컨슈머 그룹이란?`

- 하나의 토픽에 여러 컨슈머 그룹이 동시에 메세지를 가져올 수 있다.
- 동일한 컨슈머 그룹 내 컨슈머가 추가되면 위와 같이 파티션 소유권이 바뀌게 되고, 이렇게 소유권이 이동하는 것을 `리밸런스` 라고 한다.
- 컨슈머 그룹의 리밸런스를 통해 컨슈머 그룹에는 컨슈머를 쉽고 안전하게 추가할 수 있고 제거할 수도 있어 높은 가용성과 확장성을 확보할 수 있다.
- [Consumer Group Document](https://docs.confluent.io/platform/current/clients/consumer.html#concepts), [읽어보기 좋은 글 참고하기](https://www.popit.kr/kafka-consumer-group/)

<br>

#### `컨슈머 리밸런스 특징`

- 리밸런스가 발생하면 컨슈머 그룹 전체가 일시적으로 메세지를 가져올 수 없다는 특징이 있다. 
  - 컨슈머 그룹 내에서 리밸런스가 일어나면 토픽의 각 파티션마다 하나의 컨슈머가 연결된다.

<br>

#### `컨슈머의 하트비트`

- 컨슈머가 컨슈머 그룹 안에서 멤버로 유지하고 할당된 파티션의 소유권을 유지하는 방법은 `하트비트`를 보내는 것이다. 즉, 컨슈머가 일정한 주기로 하트비트를 보낸다는 사실은 해당 파티션의 메세지를 잘 처리하고 있다는 것이다.
- `하트비트는 컨슈머가 poll 할 때와 가져간 메세지의 오프셋을 커밋할 때 보내게 된다.`
  - 만약 컨슈머가 오랫동안 하트비트를 보내지 않으면 세션은 타임아웃되고 해당 컨슈머가 다운되었다고 판단하여 리밸런스가 시작된다.


<br>

#### `컨슈머 그룹 특징`

- 여러 컨슈머 그룹들이 하나의 토픽에서 메세지를 가져갈 수 있는 이유는 컨슈머 그룹마다 각자의 오프셋을 별도로 관리하기 때문이다. 
- 즉, 하나의 토픽에 두 개의 컨슈머 그룹뿐만 아니라 더 많은 컨슈머 그룹이 연결되어도 다른 컨슈머 그룹에게 영향 없이 메세지를 가져갈 수 있다는 특징이 있다.

<br>

#### `토픽의 파티션에는 하나의 컨슈머만 연결 가능`

- 메세지 처리량을 올리기 위해서 파티션 수는 늘리지 않고 컨슈머 수만 늘리는 것은 의미가 없다. 토픽의 파티션에는 하나의 컨슈머만 연결이 가능하기 때문에 컨슈머를 추가해도 메세지를 소비할 수 없기 때문이다.
- 하나의 파티션에 하나의 컨슈머만 붙을 수 있는 이유는 `각각의 파티션에 대해서는 메세지 순서를 보장하기` 위해서다.
- 즉, 컨슈머를 늘리고 싶으면 토픽의 파티션 수를 먼저 늘려야 한다.

<img width="410" alt="스크린샷 2023-01-07 오후 5 32 48" src="https://user-images.githubusercontent.com/45676906/211155139-42a5c620-d6b6-427e-a91a-579a81ba7608.png">

<br>

### `커밋과 오프셋이란?`

- 카프카에서는 각 파티션마다 메세지가 저장되는 위치를 `오프셋(offset)`이라고 부르고, 오프셋은 파티션 내에서 유일하고 순사적으로 증가하는 숫자(64비트 정수) 형태로 되어 있다.
- 각 파티션에 대해 현재 위치(오프셋)를 업데이트하는 동작을 `커밋`한다고 한다.

<br>

#### `자동 커밋`

- [enable.auto.commit](https://kafka.apache.org/documentation/#consumerconfigs_enable.auto.commit)를 `true`로 설정하면 컨슈머는 `poll()`을 호출할 때 가장 마지막 오프셋을 자등으로 커밋한다. (비명시 오프셋 커밋이라고 할 수 있다.)
- 커밋 주기는 5초가 기본 값이며, [auto.commit.interval.ms](https://kafka.apache.org/documentation/#consumerconfigs_auto.commit.interval.ms) 옵션을 통해 조정이 가능하다.
- 커밋 주기 5초가 지나기 전, 3초가 지났을 때 컨슈머 리밸런스가 일어난다면 메세지 중복 처리가 될 수 있다.
  - ex) A, B 메세지를 소비한 후에 3초가 지난 시기에 리밸런스가 일어났다면, A, B 메세지는 소비가 되었는데 커밋이 되지 않은 상황이다. 즉, 리밸런스 후에 컨슈머가 다시 A,b 메세지를 소비하여 메세지가 중복 처리될 수 있다.)
- `데이터 중복이나 유실을 허용하지 않는 서비스라면 자동 커밋을 사용해서는 안 된다.`

<br>

#### `수동 커밋`

- [enable.auto.commit](https://kafka.apache.org/documentation/#consumerconfigs_enable.auto.commit)를 `false`로 설정하여 가장 마지막 오프셋을 수동으로 커밋한다.(명시적인 오프셋 커밋이라고 할 수 있다.)
- 수동 커밋의 경우에도 컨슈머가 메세지를 읽어온 후에 DB에 반영한 후에 커밋할 때 메세지 중복이 발생할 수 있다. 
  - ex) 메세지들을 데이터베이스에 저장하는 도중에 실패하게 된다면, 마지막 커밋된 오프셋부터 메세지를 다시 가져오기 때문에 일부 메세지들은 데이터베이스에 중복으로 저장될 수 있다.

<br>

#### `동기 오프셋 커밋`

poll() 메소드가 호출된 이후에 `commitSync()` 메소드를 호출하여 오프셋 커밋을 명시적으로 수행할 수 있다.

동기 커밋의 경우 브로커로부터 컨슈머 오프셋 커밋이 완료되었음을 받기까지 컨슈머는 데이터를 더 처리하지 않고 기다리기 때문에 자동 커밋이나 비동기 오프셋 커밋보다 동일 시간당 데이터 처리량이 적다는 특징이 있다.

- [commitSync](https://kafka.apache.org/090/javadoc/org/apache/kafka/clients/consumer/KafkaConsumer.html#commitSync(java.util.Map))

<br>

#### `비동기 오프셋 커밋`

비동기 오프셋 커밋은 `commitAsync()` 메소드를 호출하여 사용할 수 있다. 비동기 오프셋 커밋도 마찬가지로 가장 마지막 레코드를 기준으로 오프셋 커밋을 한다. `다만, 비동기 오프셋은 커밋이 완료될 때까지 응답을 기다리지 않는다는 특징`이 있다.

- [commitAsync](https://kafka.apache.org/090/javadoc/org/apache/kafka/clients/consumer/KafkaConsumer.html#commitAsync(org.apache.kafka.clients.consumer.OffsetCommitCallback))

<br>

### `Zookeeper란?`


<br>

### `필수 카프카 명령어`

```shell
/bin/kafka-topics.sh \
    --bootstrap-server localhost:9092 \
    --replication-factor 1 \
    --partitions 1 \
    --topic test_topic \
    --create 
```

- 토픽 생성하기

<br>

```shell
bin/kafka-topics.sh \
    --bootstrap-server localhost:9092 \
    --topic my_topic \
    --delete
```

- 토픽 제거하기

<br>

```shell
/bin/kafka-topics.sh \
    --bootstrap-server localhost:9092 \
    --list
```

- 토픽 리스트 확인

<br>

```shell
/bin/kafka-topics.sh \
    --bootstrap-server localhost:9092 \
    --topic test_topic \
    --describe
```

- 토픽 상세보기

<br>

```shell
/bin/kafka-topics.sh \
    --bootstrap-server localhost:9092 \
    --alter --topic test_topic --partition 2
```

- 토픽의 파티션 수 변경

<br>

```shell
/bin/kafka-consumer-groups.sh \
    --bootstrap-server localhost:9092 \
    --list
```

- 컨슈머 그룹 리스트 확인

<br>

```shell
./bin/kafka-consumer-groups.sh \
    --bootstrap-server localhost:9092 \
    --group test_topic --describe
```

- 컨슈머 상태와 오프셋 확인

<br>

`Kafka Manager` 라는 GUI 툴을 사용하면 어느정도는 CLI를 사용하지 않고 해결할 수 있다. 하지만 특정한(?) 상황에서 내부 정보를 확인할 때는 CLI를 사용하는 상황도 있을 것이다.  

Kafka CLI에 대한 좀 더 자세한 것은 [여기](https://akageun.github.io/2020/05/07/kafka-cli.html)와 [카프카, 데이터 플랫폼의 최강자 6장](http://www.yes24.com/Product/Goods/59789254)을 참고하자. 

<br>

### `Reference`

- [카프카, 데이터 플랫폼의 최강자](http://www.yes24.com/Product/Goods/59789254) - `3, 4, 5, 6장 참고`
- [아파치 카프카 애플리케이션 프로그래밍 with 자바](http://www.yes24.com/Product/Goods/99122569) - `3, 4장 참고` 
- [https://www.openlogic.com/blog/using-kafka-zookeeper](https://www.openlogic.com/blog/using-kafka-zookeeper) - `Zookeeper 참고`
- [https://zookeeper.apache.org/doc/current/zookeeperUseCases.html](https://zookeeper.apache.org/doc/current/zookeeperUseCases.html) - `Zookeeper 참고`
- [https://www.conduktor.io/kafka/kafka-topic-configuration-min-insync-replicas](https://www.conduktor.io/kafka/kafka-topic-configuration-min-insync-replicas) - `ack=all, min.insync.replicas=2 내용 참고`
- [https://kafka.apache.org/documentation/](https://kafka.apache.org/documentation/) - `Kafka 설정 참고` 
- [https://docs.confluent.io/platform/current/installation/configuration/topic-configs.html](https://docs.confluent.io/platform/current/installation/configuration/topic-configs.html) - `Topic 설정들 참고`
- [https://docs.confluent.io/platform/current/installation/configuration/broker-configs.html](https://docs.confluent.io/platform/current/installation/configuration/broker-configs.html) - `Broker 옵션들 참고`
- [https://stackoverflow.com/questions/62326946/kafka-min-insync-replicas-interpretation](https://stackoverflow.com/questions/62326946/kafka-min-insync-replicas-interpretation) - `ack=all, min.insync.replicas 내용 참고`