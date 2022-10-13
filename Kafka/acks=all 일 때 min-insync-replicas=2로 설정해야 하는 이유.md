## `acks=all 일 때 min.insync.replicas=2로 설정해야 하는 이유는 무엇일까?`

- `acks=all` : 리더는 ISR의 팔로워로부터 데이터에 대한 ack를 기다리고, 하나의 팔로워가 있는 한 데이터는 손실되지 않으며 데이터 무손실에 대해 가장 강력하게 보장
- `ISR`: In Sync Replica의 약어로 현재 리플리케이션이 되고 있는 리플리케이션 그룹(replication group)을 의미
- `min.insync.replicas`: 최소 리플리케이션 팩터를 지정하는 옵션
- `Replication Factor` 는 토픽의 파티션의 복제본을 몇 개를 생성할 지에 대한 설정

<br>

본 내용에 들어가기 전에 카프카 용어에 대해 간략하게 정리하면 위와 같습니다.

여기서 이번 글에서는 `min.insync.replicas` 옵션에 대해서 알아볼 것입니다.

`아파치 카프카 문서에서는 손실 없는 메세지 전송을 위한 조건으로 프로듀서는 acks=all, 브로커의 min.insync.replicas=2, Topic의 Replication Factor는 3`으로 권장하고 있습니다.

`min.insync.replicas` 옵션 값을 올리면 복제본이 올라가니까 손실 없는 메세지 전송을 위해 `min.insync.replicas=3`으로 설정해야 하는 것이 아닌가라고 생각할 수 있는데요. 아파치 카프카에서는 왜 `min.insync.replicas=2`로 설정을 권장하는 것인지에 대해서 알아보겠습니다.

<br>

### `min.insync.replicas=3으로 설정했을 때 동작방식`

![image](https://user-images.githubusercontent.com/45676906/195646636-4a2c58ef-8940-4192-be50-0d771a40212f.png)

1. 프로듀서가 `acks=all` 옵션으로 리더에게 메세지를 보냅니다.
2. 리더는 메세지를 받은 후에 저장합니다. 브로커 1에 있는 팔로워는 변경된 사항이나 새로 받은 메세지가 없는지를 리더로부터 주기적으로 확인하면서, 새로운 메세지가 전송된 것을 확인하면 자신도 리더로부터 메세지를 가져와서 저장합니다.
3. 리더는 `min.insync.replicas`가 3으로 설정되어 있기 때문에 `acks`를 보내기 전 최소 3개의 복제를 유지하는지 확인해야 합니다.
4. 리더는 프로듀서가 전송한 메세지에 대해 acks를 프로듀서에게 보냅니다.

<br>

위의 그림은 `min.insync.replicas=2`로 설정되어 있을 때지만 그림처럼 `acks=all`로 되어 있기 때문에 `acks`를 보내기 전에 `min.insync.replicas` 값만큼 리플리케이션을 확인한 후에 acks를 응답으로 보내게 됩니다. 

이렇게 보아도 `min.insync.replicas`를 3으로 올리는 것이 더 좋아보이는데 그러면 왜 3이 아닌 2로 권장하는 것일까요?

하나의 시나리오를 생각해보겠습니다.

1. 프로듀서는 `acks=all` 옵션으로 메세지를 전송합니다.
2. 브로커 중 팔로워가 위치하고 있는 브로커 하나를 강제로 종료합니다.
3. 카프카 서버의 로그를 확인합니다.

<br>

팔로워 중에 하나인 브로커를 강제 종료하고 에러 로그를 확인해보면 `Replication Factor가 부족하다`는 내용의 메세지가 나타나게 될 것인데요.

<br>

![image](https://user-images.githubusercontent.com/45676906/195647144-c7049206-c4ea-4e87-95d2-d68e16b37080.png)

왜 이러한 로그들이 발생하는지 알아보면 우리는 `acks=all, min.insync.replicas=3`으로 설정했습니다. 이렇게 설정한 경우 프로듀서가 토픽의 리더에게 메세지를 전송하게 되면, 리더 + 팔로워 + 팔로워 이렇게 3곳에서 모두 메세지를 받아야만 리더는 프로듀서에게 메세지를 잘 받았다는 `확인(ack)`을 보낼 수 있습니다.

하지만 여기서 팔로워 중에 하나인 브로커 하나에 문제가 발생했으니 ISR에는 리더와 팔로워 하나만 남아 있습니다. 결국 옵션으로 설정한 조건을 충족시킬 수 없는 상황이 발생했기 때문에 위와 같은 에러 로그가 발생하는 것입니다.

카프카는 브로커 하나가 다운되더라도 크리티컬한 장애 상황없이 서비스를 잘 처리할 수 있도록 구성되어 있는데, 만약 `acks=all + min.insync.replicas=3`으로 설정하게 되면 브로커 하나만 다운되더라도 카프카 메세지를 보낼 수 없는 클러스터 전체 장애와 비슷한 상황이 발생하게 됩니다.

즉, 그림 같이 `min.insync.replicas` 값이 2이고 브로커가 1대에만 문제가 생겼다면 동작하는데 지장을 주진 않지만, `min.insync.replicas=3`으로 했다면 문제가 생기게 됩니다.

`이러한 이유로 카프카에서는 손실없는 메세지 전송을 위해 프로듀서의 acks=all로 사용하는 경우 브로커의 min.insync.replicas=2로 설정하고 토픽의 리플리케이션 팩터는 3으로 설정하기를 권장하고 있습니다.`

<br>

## `Reference`

- [https://stackoverflow.com/questions/62326946/kafka-min-insync-replicas-interpretation](https://stackoverflow.com/questions/62326946/kafka-min-insync-replicas-interpretation)
- [카프카 데이터 플랫폼의 최강자 4장](http://www.yes24.com/Product/Goods/59789254)