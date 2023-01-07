## `Kafka 조금 아는 척하기 3`

```java
public class Kafka {
    public void consumer() {
        Properties prop = new Properties();
        prop.put("bootstrap.servers", "localhost:9002");
        prop.put("group.id", "group1");
        prop.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        prop.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        KafkaConsumer<Integer, String> consumer = new KafkaConsumer<>(prop);

        consumer.subscribe(Collections.singleton("simple")); // 토픽 구독
        
        while (조건) {
            ConsumerRecords<String, String> records = consumer.poll(Doration.ofMills(100));
            for (ConsumerRecord<String, String> record: records) {
                System.out.println(record.value() + "." + record.topic() + "." + record.partition() + "." + record.offset());
            }
        }

        consumer.close();
    }
}
```

<br>

## `토픽 파티션은 그룹 단위 할당`

<img width="1162" alt="스크린샷 2022-10-05 오후 1 49 51" src="https://user-images.githubusercontent.com/45676906/193983595-4ac1d8da-2b03-4692-b07c-80e77180d020.png">

- 파티션 보다 많은 컨슈머가 생긴다면 이후에 생긴 컨슈머는 놀게 됩니다. 중요한 점은 파티션 수보다 컨슈머 수가 더 많아서는 안됩니다.

<br>

## `커밋과 오프셋`

<img width="889" alt="스크린샷 2022-10-05 오후 1 52 07" src="https://user-images.githubusercontent.com/45676906/193983819-6dc166f0-7528-46fa-851b-4d0b50cb59ec.png">

- Consumer poll 메소드는 이전에 커밋 오프셋이 있으면 커밋 오프셋 이후부터 읽어옵니다. 그리고 마지막으로 읽은 오프셋을 커밋합니다.

<br>

## `커밋된 오프셋이 없는 경우`

- 처음 접근이거나 커밋한 오프셋이 없는 경우
- auto.offset.reset 설정 사용
  - earliest: 맨 처음 오프셋 사용
  - latest: 가장 마지막 오프셋 사용 (기본값)
  - none: 컨슈머 그룹에 대한 이전 커밋이 없으면 익셉션 발생

<br>

<img width="846" alt="스크린샷 2022-10-05 오후 1 54 50" src="https://user-images.githubusercontent.com/45676906/193984102-1212b3e3-6b0d-426a-be49-7c0e0b3b49cb.png">

<br>

## `컨슈머 설정`

### `조회에 영향을 주는 주요 설정`

- fetch.min.bytes: 조회시 브로커가 전송할 최소 데이터 크기
  - 기본값: 1
  - 이 값이 크면 대기 시간은 늘지만 처리량이 증가
- fetch.max.wait.ms: 데이터가 최소 크기가 될 때까지 기다릴 시간
  - 기본값: 500
  - 브로커가 리턴할 때까지 대기하는 시간으로 poll() 메소드의 대기 시간과 다름
- max.partition.fetch.bytes: 파티션 당 서버가 리턴할 수 있는 최대 크기
  - 기본값: 1048586 (1MB)

<br>

## `수동 커밋: 동기/비동기 커밋`

```
consumer.commitSync();
consumer.commitASync();
```

<br>

## `재처리와 순서`

- 동일 메세지 조회 가능성
  - 일시적 커밋 실패, 리밸런스 등에 의해 발생
- 컨슈머는 멱등성을 고려해야 함
- 데이터 특성에 따라 타임스탬프, 일련 번호 등을 활용

<br>

## `쓰레드 안전하지 않음`

- Kafka Consumer는 쓰레드에 안전하지 않음
  - 여러 쓰레드에서 동시에 사용하지 말 것
  - wakeup() 메소드는 예외