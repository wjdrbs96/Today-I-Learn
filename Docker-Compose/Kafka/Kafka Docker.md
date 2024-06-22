## Docker
```dockerfile
version: '3.3'

services:
  zookeeper-1:
    image: confluentinc/cp-zookeeper:7.6.1
    hostname: zookeeper-1
    container_name: "zookeeper-1"
    environment:
      ZOOKEEPER_SERVER_ID: 1
      ZOOKEEPER_CLIENT_PORT: 2181
      ZOOKEEPER_TICK_TIME: 2000
      ZOOKEEPER_INIT_LIMIT: 5
      ZOOKEEPER_SYNC_LIMIT: 2
    ports:
      - "22181:2181"

  zookeeper-2:
    image: confluentinc/cp-zookeeper:7.6.1
    hostname: zookeeper-2
    container_name: "zookeeper-2"
    environment:
      ZOOKEEPER_SERVER_ID: 2
      ZOOKEEPER_CLIENT_PORT: 2181
      ZOOKEEPER_TICK_TIME: 2000
      ZOOKEEPER_INIT_LIMIT: 5
      ZOOKEEPER_SYNC_LIMIT: 2
    ports:
      - "32181:2181"

  zookeeper-3:
    image: confluentinc/cp-zookeeper:7.6.1
    hostname: zookeeper-3
    container_name: "zookeeper-3"
    environment:
      ZOOKEEPER_SERVER_ID: 3
      ZOOKEEPER_CLIENT_PORT: 2181
      ZOOKEEPER_TICK_TIME: 2000
      ZOOKEEPER_INIT_LIMIT: 5
      ZOOKEEPER_SYNC_LIMIT: 2
    ports:
      - "42181:2181"

  kafka-1:
    image: confluentinc/cp-kafka:7.6.1
    hostname: kafka-1
    container_name: "kafka-1"
    depends_on:
      - zookeeper-1
      - zookeeper-2
      - zookeeper-3
    ports:
      - "29092:29092"
    environment:
      KAFKA_BROKER_ID: 1
      KAFKA_ZOOKEEPER_CONNECT: zookeeper-1:2181,zookeeper-2:2181,zookeeper-3:2181
      KAFKA_ADVERTISED_LISTENERS: PLAINTEXT://kafka-1:9092,PLAINTEXT_HOST://localhost:29092
      KAFKA_LISTENER_SECURITY_PROTOCOL_MAP: PLAINTEXT:PLAINTEXT,PLAINTEXT_HOST:PLAINTEXT
      KAFKA_INTER_BROKER_LISTENER_NAME: PLAINTEXT
      KAFKA_TRANSACTION_STATE_LOG_REPLICATION_FACTOR: 3
      KAFKA_TRANSACTION_STATE_LOG_MIN_ISR: 1

  kafka-2:
    image: confluentinc/cp-kafka:7.6.1
    hostname: kafka-2
    container_name: "kafka-2"
    depends_on:
      - zookeeper-1
      - zookeeper-2
      - zookeeper-3
    ports:
      - "39092:39092"
    environment:
      KAFKA_BROKER_ID: 2
      KAFKA_ZOOKEEPER_CONNECT: zookeeper-1:2181,zookeeper-2:2181,zookeeper-3:2181
      KAFKA_ADVERTISED_LISTENERS: PLAINTEXT://kafka-2:9092,PLAINTEXT_HOST://localhost:39092
      KAFKA_LISTENER_SECURITY_PROTOCOL_MAP: PLAINTEXT:PLAINTEXT,PLAINTEXT_HOST:PLAINTEXT
      KAFKA_INTER_BROKER_LISTENER_NAME: PLAINTEXT
      KAFKA_TRANSACTION_STATE_LOG_REPLICATION_FACTOR: 3
      KAFKA_TRANSACTION_STATE_LOG_MIN_ISR: 1

  kafka-3:
    image: confluentinc/cp-kafka:7.6.1
    hostname: kafka-3
    container_name: "kafka-3"
    depends_on:
      - zookeeper-1
      - zookeeper-2
      - zookeeper-3
    ports:
      - "49092:49092"
    environment:
      KAFKA_BROKER_ID: 3
      KAFKA_ZOOKEEPER_CONNECT: zookeeper-1:2181,zookeeper-2:2181,zookeeper-3:2181
      KAFKA_ADVERTISED_LISTENERS: PLAINTEXT://kafka-3:9092,PLAINTEXT_HOST://localhost:49092
      KAFKA_LISTENER_SECURITY_PROTOCOL_MAP: PLAINTEXT:PLAINTEXT,PLAINTEXT_HOST:PLAINTEXT
      KAFKA_INTER_BROKER_LISTENER_NAME: PLAINTEXT
      KAFKA_TRANSACTION_STATE_LOG_REPLICATION_FACTOR: 3
      KAFKA_TRANSACTION_STATE_LOG_MIN_ISR: 1

  kafdrop:
    image: obsidiandynamics/kafdrop:4.0.1
    container_name: "kafka-web-ui"
    restart: "no"
    ports:
      - "9000:9000"
    environment:
      KAFKA_BROKER_CONNECT: "kafka-1:9092"
    depends_on:
      - kafka-1
      - kafka-2
      - kafka-3
```

* version
    * docker-compose version
* image
    * https://hub.docker.com/r/confluentinc/cp-zookeeper
    * https://hub.docker.com/r/confluentinc/cp-kafka
    * 2024.06.22 기준 가장 최신 버전인 7.6.1 을 사용하였다.
* ZOOKEEPER_SERVER_ID
    * zookeeper 클러스터에서 유일하게 zookeeper 를 식별할 아이디
    * 동일 클러스터 내에서 중복되면 안된다.
* ZOOKEEPER_CLIENT_PORT
    * zookeeper port 지정
* ZOOKEEPER_TICK_TIME
    * zookeeper 에서 사용하는 기본 시간 단위 (ms)
    * 하트 비트를 수행하는데 사용되며 최소 세션 시간 초과는 tickTime 의 두 배이다.
* ZOOKEEPER_INIT_LIMIT
    * 팔로워가 리더와 초기에 연결을 시도할 때 갖는 tick 의 수
    * zookeeper 서버가 리더에 연결해야하는 시간을 제한하는데 사용
    * 위에서는 tickTime 을 2초, initLimit 을 5로 설정했으니 10초가 된다.
* ZOOKEEPER_SYNC_LIMIT
    * zookeeper 리더와 나머지 서버들의 싱크 타임
    * 이 시간내로 싱크 응답으로 들어오는 경우 클러스터가 정상적으로 구성되어 있음을 확인하는 시간
    * 여기서 2로 설정했으므로 tickTime 2000 * 2 = 4초가 된다.
* depends_on
    * docker-compose 에서는 서비스들의 우선순위를 지정해 주기 위해서 depends_on 을 이용한다.
    * kafka 는 zookeeper 가 먼저 실행되어 있어야 컨테이너가 올라가게 된다.
* KAFKA_BROKER_ID
    * kafka 브로커 아이디를 지정
    * 유니크
* KAFKA_ZOOKEEPER_CONNECT
    * kafka 가 zookeeper 에 커넥션하기 위한 대상을 지정
* KAFKA_ADVERTISED_LISTENERS
    * 외부에서 접속하기 위한 리스너 설정을 한다.
* KAFKA_LISTENER_SECURITY_PROTOCOL_MAP
    * 보안을 위한 프로토콜 매핑이디.
    * 이 설정값은 KAFKA_ADVERTISED_LISTENERS 과 함께 key/value 로 매핑된다.
* KAFKA_INTER_BROKER_LISTENER_NAME
    * 도커 내부에서 사용할 리스너 이름을 지정한다.
    * 이전에 매핑된 PLAINTEXT 가 사용되었다.
* KAFKA_TRANSACTION_STATE_LOG_REPLICATION_FACTOR
    * 트랜잭션 로그 토픽의 복제 개수를 지정하는 데 사용된다.
    * kafka 는 트랜잭션 로그를 통해 트랜잭션 상태를 관리하는데, 이 로그는 브로커 간에 복제되어야 한다
    * 3으로 설정하면, 트랜잭션 로그 토픽의 각 파티션은 3개의 브로커에 복제된다. 이는 데이터의 내구성을 보장하고, 브로커 장애 시에도 데이터 손실을 방지하는 데 중요하다.
    * 일반적으로 클러스터의 브로커 수보다 작거나 같은 값으로 설정하는 것이 좋다.
* KAFKA_TRANSACTION_STATE_LOG_MIN_ISR
    * 트랜잭션 로그 토픽의 최소 ISR(In-Sync Replica) 개수를 설정하는 데 사용된다.
    * ISR(In-Sync Replica)는 현재 리더 파티션과 동기화된 상태를 유지하고 있는 팔로워 파티션들을 의미한다.
    * 트랜잭션 로그 토픽의 파티션이 커밋을 완료하기 위해 필요한 최소 ISR 의 개수를 지정한다.
* kafdrop
    * kafka-web-ui 로 kafka admin 툴이다.