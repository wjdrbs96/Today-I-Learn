## `Kafka Lag란?`

<img width="318" alt="스크린샷 2022-08-18 오후 6 51 19" src="https://user-images.githubusercontent.com/45676906/185366548-ace54a3d-43c1-462b-bb0f-d0e98dc929b0.png">

하나의 파티션에 `Producer`가 데이터를 넣게 되고 `Consumer`가 데이터를 소비하게 될 것입니다.

만약 `Producer`가 데이터를 넣는 속도가 `Consumer`가 데이터를 소비하는 속도보다 빠르다면 `컨슈머가 마지막으로 읽은 offset과 Producer가 마지막으로 넣은 offset의 차이`가 발생합니다. 이 차이를 `Consumer Lag` 이라고 합니다. 

`Kafka에서 Lag 값을 통해 Producer, Consumer의 상태를 유추할 수 있습니다. Lag 값은 주로 Consumer의 상태를 볼 때 사용합니다. 즉, Lag이 높다면 Consumer에 문제가 있다는 뜻일 수 있습니다.`

<br>

## `Reference`

- [https://www.youtube.com/watch?v=D7C_CFjrzBk](https://www.youtube.com/watch?v=D7C_CFjrzBk)