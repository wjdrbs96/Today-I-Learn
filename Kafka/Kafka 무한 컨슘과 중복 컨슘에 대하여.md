## `Kafka 무한 컨슘과 중복 컨슘에 대하여`

- 컨슈머 리밸런싱과 [max.poll.records](https://kafka.apache.org/documentation/#consumerconfigs_max.poll.records), [max.poll.interval.ms](https://kafka.apache.org/documentation/#consumerconfigs_max.poll.interval.ms) 옵션의 관계
  - 컨슈머 poll() 대기시간, 해당 시간동안 poll() 이 일어나지 않을 경우 커밋전에 리밸런싱이 일어나면서 또 다시 컨슘되는 문제가 발생한다.
  - 계속 리밸런싱이 일어난다면 무한 컨슘이 발생한다는 문제도 발생한다.
  - max.poll.records 만큼 가져와서 처리할 때, 처리시간이 max.poll.interval.ms 값보다 작아야 한다.

<br>

### `컨슈머 리밸런싱 예시 상황`

```kotlin
@Configuration
class KafkaTemplateConsumerConfig(
    private val kafkaProperty: KafkaProperty
) {

    @Bean
    fun consumerFactory(): ConcurrentKafkaListenerContainerFactory<String, String> {
        val factory = ConcurrentKafkaListenerContainerFactory<String, String>()
        factory.consumerFactory = DefaultKafkaConsumerFactory(consumerConfig())
        return factory
    }

    fun consumerConfig2(): Map<String, Serializable> =
        mapOf(
            // 생략
            ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG to 3000,  // max.poll.interval.ms (= 3초)
            ConsumerConfig.MAX_POLL_RECORDS_CONFIG to 1,         // max.poll.records (= 1개)
            ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG to "false"  // enable.auto.commit
        )
}
```

```kotlin
@Component
class KafkaConsumer {

    @KafkaListener(topics = ["test-kafka"], containerFactory = "consumerFactory")
    fun basicConsumer(@Payload rawData: String) {
        println("=====Consumer 시작=====")
        Thread.sleep(5000)
        println("Consumer1: $rawData")
        println("=====Consumer 종료=====")
    }
}
```

Consumer에서 데이터 처리하기 위해 시간 소요가 많이 된다는 것을 가정하기 위해 `Thread.sleep()`을 사용하였습니다. (`max.poll.interval.ms(=3초)` 값 보다 더 소요하기 위해서 sleep 5초를 사용)

<img width="1803" alt="스크린샷 2024-04-23 오후 11 23 28" src="https://github.com/wjdrbs96/Today-I-Learn/assets/45676906/54e3af59-cba1-4c8b-aa1a-e0ca760dbf2f">

> You can address this either by increasing max.poll.interval.ms or by reducing the maximum size of batches returned in poll() with max.poll.records.

> Offset commit cannot be completed since the consumer is not part of an active group for auto partition assignment; it is likely that the consumer was kicked out of the group.

에러 메세지를 보면 `max.poll.interval.ms`를 늘리던지, `max.poll.records` 수를 줄이라고 나와 있고, Offset commit에 실패해서 컨슈머 그룹에서 추방 당한 것을 볼 수 있습니다.

Poll 하고 처리하는 시간이 `max.poll.interval.ms` 보다 더 길어서 위처럼 커밋이 실패한 것을 볼 수 있습니다.

<img width="1794" alt="스크린샷 2024-04-23 오후 11 27 01" src="https://github.com/wjdrbs96/Today-I-Learn/assets/45676906/0c46af9d-9146-4f61-ae41-213f98461a63">

즉, 컨슈머가 리밸린싱이 일어나서 방금 처리하려고 시도 했던 Offset에 대한 데이터를 다시 컨슘 해서 진행합니다. (Offset Commit 실패 했기 때문에) 

커밋을 성공하지 못했기 때문에 동일한 Offset에 대해서 중복 컨슘을 처리하게 되는 문제도 발생합니다.

그런데 더 큰 문제는 컨슈머에서 처리하는 속도가 `max.poll.interval.ms` 보다 계속 더 오래 걸린다면 무한 컨슘 문제까지 발생하게 됩니다. (중복 메세지 컨슘, 무한 메시지 컨슘)

`max.poll.interval.ms` 디폴트 값은 5분인데 만약 메세지 처리하는 로직이 무겁다면 해당 시간을 늘리거나, `max.poll.records` 하는 개수를 줄여아 합니다. (한번에 너무 많이 poll 해서 처리해서 오래 걸릴 수도 있기 때문)