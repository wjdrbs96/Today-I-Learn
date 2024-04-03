## `Kafka Connect란 무엇일까?`

`카프카 커넥트(Kafka Connect)`는 아파치 카프카의 오픈소스 프로젝트 중 하나로, 데이터베이스 같은 외부 시스템과 카프카를 쉽게 연결하기 위한 프레임워크 입니다.

카프카가 커넥트 프로엠이워크를 이용해 대용량의 데이터를 카프카의 안팎으로 손쉽게 이동시킬 수 있으며, 코드를 작성하지 않고도 간단히 사용할 수 있습니다.

- `데이터 중심 파이프라인`: 커넥트를 이용해 카프카로 데이터를 보내거나, 카프카로부터 데이터를 가져옴
- `유연성과 확장성`: 커넥트는 테스트 및 일회성 작업을 위한 단독 모드로 실행할 수 있고, 대규모 운영 환경을 위한 분산 모드로 실행할 수 있습니다.

<br>

## `Reference`

- [https://docs.confluent.io/platform/current/connect/index.html#connect-connectors](https://docs.confluent.io/platform/current/connect/index.html#connect-connectors)