## `Kafka 조금 아는 척하기 2`

```java
public class Kafka {
    public void producer() {
        Properties prop = new Properties();
        prop.put("bootstrap.servers", "kafka01:9092,kafka01:9092,kafka01:9092");
        prop.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        prop.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        
        KafkaProducer<Integer, String> producer = new KafkaProducer<>(prop);
        
        producer.send(new ProducerRecord<>("topicName", "key", "value"));
        producer.send(new ProducerRecord<>("topicName", "value"));
        
        producer.close();
    }
}
```

위의 코드는 `Producer`를 사용해서 메세지를 보내는 코드입니다. 

<br>

<img width="1243" alt="스크린샷 2022-10-05 오전 11 45 10" src="https://user-images.githubusercontent.com/45676906/193970111-8f1c2049-8e1a-4ff0-8e46-6abd2727812a.png">

1. Serializer를 사용하여 byte 배열로 변환한다.
2. Partitioner를 사용하여 메세지를 어떤 토픽의 어떤 파티션으로 보낼지 결정한다.
3. 배치로 묶어서 메세지를 묶어서 버퍼에 저장한다.
4. Sender가 메세지를 차례대로 가져와서 카프카 브로커에 전달한다.

<br>

<img width="1234" alt="스크린샷 2022-10-05 오전 11 46 17" src="https://user-images.githubusercontent.com/45676906/193970105-5f65ed18-1898-4540-87fd-20f0ce0cbc77.png">

<br>

<img width="1659" alt="스크린샷 2022-10-05 오후 1 36 00" src="https://user-images.githubusercontent.com/45676906/193982189-5e9695e4-a39b-43ef-8733-dbbc06d5edbb.png">

- `batch.size`: 설정한 크기만큼 배치에 메세지가 차게 되면 전송을 하게 된다. 즉, 사이즈가 작다면 전송 횟수가 많아진다는 뜻

<br>

## `전송 보장과 ack`

- ack = 0
  - 서버 응답을 기다리지 않음
  - 전송 보장도 zero
- ack = 1
  - 파티션의 리더에 저장되면 응답 받음
  - 리더 장애시 메시지 유실 가능
- ack = all (또는 -1)
  - 모든 리플레카에 저장되면 응답 받음

<br>

## `Reference`

- [https://www.youtube.com/watch?v=geMtm17ofPY](https://www.youtube.com/watch?v=geMtm17ofPY)