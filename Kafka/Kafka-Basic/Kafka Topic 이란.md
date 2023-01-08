## `Kafka Topic 이란?`

<img width="194" alt="스크린샷 2022-08-18 오후 6 42 03" src="https://user-images.githubusercontent.com/45676906/185364408-f825734d-f725-4509-9922-baf9293e0f18.png">

Kafka는 여러 개의 Topic을 생성할 수 있습니다.

<br>

<img width="1132" alt="스크린샷 2022-08-18 오후 6 43 06" src="https://user-images.githubusercontent.com/45676906/185364645-f8417f52-766f-48b4-9078-8993af802cad.png">

그리고 토픽을 `Producer`, `Consumer` 들이 존재합니다. (토픽에는 이름을 줄 수 있음)

<br>

하나의 `토픽`은 여러 개의 `파티션`으로 구성될 수 있습니다.

<br>

<img width="1252" alt="스크린샷 2022-08-18 오후 6 45 35" src="https://user-images.githubusercontent.com/45676906/185365223-48d680cf-b876-461a-8735-137e46d134b1.png">

여기서 알아야 할 점은 `Consumer`가 데이터를 꺼내더라도 파티션 내부에 데이터들이 삭제되지는 않고 `Offset` 값만 변경됩니다.

<br>

![스크린샷 2022-08-18 오후 6 47 25](https://user-images.githubusercontent.com/45676906/185365668-4bb3fecd-f24e-4648-9a9d-a080c19329cb.png)

새로운 컨슈머가 생겼을 때 다시 0번부터 데이터를 가져갈 수 있습니다. 단, 기존 컨슈머 그룹과 달라야 하고 `auto-offset-reset=earliest`가 설정되어 있어야 합니다.

<br>

## `Reference`

- [https://www.youtube.com/watch?v=7QfEpRTRdIQ](https://www.youtube.com/watch?v=7QfEpRTRdIQ)