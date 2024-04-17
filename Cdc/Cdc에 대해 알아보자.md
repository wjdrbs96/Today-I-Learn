## `CDC란 무엇일까`

- Cdc는 `Change Data Capture`의 약자로 변경된 데이터를 캡쳐하는 것을 의미
- 주로 데이터베이스같은 데이터 스토어의 데이터 변경을 포착하여 후속 처리를 하는데 사용됨
    - 데이터베이스 실시간 동기화
    - 데이터 변경 이벤트를 외부 시스템에 보내야 할 때
- Source DB 부하 매우 적음
- 누락 없이 실시간 전송 보장

![image](https://github.com/wjdrbs96/Today-I-Learn/assets/45676906/c97620d9-db6e-4b52-a824-fdacbeb23c3d)

Cdc는 Source DB의 트랜잭션 로그를 통해서 변경점을 캡쳐해서 Target 으로 전송하는 것을 의미한다고 할 수 있습니다.

<br>

### `MySQL Bin log`

- 트랜잭션 커밋시 기록
- 커밋된 순서대로 데이터 스키마 및 데이터 변경 기록
- 신뢰할 수 있는 확정된 변경 이력
- MySQL은 복제 및 복구를 위해 binlog를 사용


트랜잭션 로그란 MySQL로 치면 Bin log를 의미하고, Bin log는 위와 같은 특징을 가지고 있습니다.

<br>

## `debezium 이란?`

위의 그림에서 보면 Source DB의 트랜잭션 로그를 통해서 변경점을 캡쳐하여 Target 시스템으로 전송하는 과정에 대해서 설명 하였는데요.

이러한 역할을 하는 대표적인 플랫폼 중에 `debezium`이 존재하는데, 간략하게 알아보겠습니다.

![image](https://github.com/wjdrbs96/Today-I-Learn/assets/45676906/9909e276-9ce2-4840-a165-6014e9367448)

- Debezium은 CDC를 수행하기 위한 오픈소스 분산 플랫폼
- Debezium은 Kafka Source 커넥터 집합
- Debezium은 MongoDB, MySQL, PostgreSQL, SQL Server, Oracle, Db2, Cassandra 다양한 커넥터들을 제공([debezium - connectors](https://debezium.io/documentation/reference/stable/connectors/index.html))

데비지움 MySQL 커넥터는 binlog를 읽고 INSERT, UPDATE 및 DELETE 작업에 대한 변경 이벤트를 생성하여 Kafka의 Topic에 메시지를 전달합니다. Source Connector는 Source DB의 변경점을 캡쳐하여 Kafka로 전송하는 Producer 역할이라고 볼 수 있습니다.

<br>

## `Kafka Connect란 무엇일까?`

`카프카 커넥트(Kafka Connect)`는 아파치 카프카의 오픈소스 프로젝트 중 하나로, 데이터베이스 같은 외부 시스템과 카프카를 쉽게 연결하기 위한 프레임워크 입니다.

카프카가 커넥트 프레임워크를 이용해 대용량의 데이터를 코드를 작성하지 않고도 쉽게 이동시킬 수 있다는 특징이 있습니다.

<img width="1322" alt="스크린샷 2024-04-15 오전 11 04 52" src="https://github.com/wjdrbs96/Today-I-Learn/assets/45676906/334abc38-9a23-44b9-b579-070b0f5de6f8">

- 소스 방향에 있는 커넥트를 소스 커넥트 라고 함 (Kafka 에서 Producer 역할 한다고 보면 됨)
- 싱크 방향에 있는 커넥트를 싱크 커넥트 라고 함 (Kafka 에서 Consumer 역할 한다고 보면 됨)

예를들어 RDBMS 데이터를 Kafka로 전송하고 싶다면 JDBC 소스 커넥터를 사용할 수 있고, Kafka에 적재된 데이터를 HDFS로 적재하고자 한다면 HDFS 싱크 커넥트가 필요합니다.

<br>

## `Referenece`

- [https://docs.confluent.io/platform/current/connect/index.html](https://docs.confluent.io/platform/current/connect/index.html)
- [https://debezium.io/documentation/reference/stable/architecture.html](https://debezium.io/documentation/reference/stable/architecture.html)
- [https://debezium.io/documentation/reference/stable/connectors/mysql.html](https://debezium.io/documentation/reference/stable/connectors/mysql.html)
- [https://www.qlik.com/us/change-data-capture/cdc-change-data-capture](https://www.qlik.com/us/change-data-capture/cdc-change-data-capture)
- [https://tv.naver.com/v/23651043](https://tv.naver.com/v/23651043)
- [https://www.striim.com/blog/change-data-capture-cdc-what-it-is-and-how-it-works/](https://www.striim.com/blog/change-data-capture-cdc-what-it-is-and-how-it-works/)
- [https://mk.kakaocdn.net/dn/if-kakao/conf2019/%EB%B0%9C%ED%91%9C%EC%9E%90%EB%A3%8C_2019/T03-S01.pdf](https://mk.kakaocdn.net/dn/if-kakao/conf2019/%EB%B0%9C%ED%91%9C%EC%9E%90%EB%A3%8C_2019/T03-S01.pdf)
- [https://m0rph2us.github.io/mysql/cdc/debezium/2020/05/23/mysql-cdc-with-debezium-1.html](https://m0rph2us.github.io/mysql/cdc/debezium/2020/05/23/mysql-cdc-with-debezium-1.html)
- [https://medium.com/uplusdevu/debezium%EC%9C%BC%EB%A1%9C-db-synchronization-%EA%B5%AC%EC%B6%95%ED%95%98%EA%B8%B0-1b6fba73010f](https://medium.com/uplusdevu/debezium%EC%9C%BC%EB%A1%9C-db-synchronization-%EA%B5%AC%EC%B6%95%ED%95%98%EA%B8%B0-1b6fba73010f)