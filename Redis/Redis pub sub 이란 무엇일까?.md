# `Redis pub sub 이란 무엇일까?`

![111](https://user-images.githubusercontent.com/45676906/116494151-31b23980-a8db-11eb-9959-8c5947b65d5a.png)

`Redis pub/sub` 이란 채널을 구독한 Subscriber에게 메세지를 전달하는 것을 의미합니다. 디자인 패턴 중에 `옵저버 패턴`이랑 비슷하다는 느낌을 받았습니다. 

이런 것은 어디서 쓸 수 있을까요? 어떤 `채팅 방에 속해 있는 유저들에게 어떤 메세지 전송` 또는 `알람 설정을 해놓았던 유저들에게 Push Alarm 보내기`와 같은 상황에서 사용하기에 적절할 거 같습니다. 

그래서 이번 글에서는 간단하게 `pub/sub`에 대해 실습을 진행해보겠습니다.

<img width="650" alt="스크린샷 2021-04-29 오전 11 14 14" src="https://user-images.githubusercontent.com/45676906/116494583-0aa83780-a8dc-11eb-8b3e-96fc17c26869.png">

`redis-cli`를 접속한 후에 `Cluster`라는 채널을 구독하겠습니다. 

```
subscribe 채널이름
ex) subscribe Cluster
```

<br>

<img width="1299" alt="스크린샷 2021-04-29 오전 11 16 22" src="https://user-images.githubusercontent.com/45676906/116494729-6d99ce80-a8dc-11eb-853e-566bfc47c3bd.png">

그리고 `Terminal`을 2개를 열고, 한쪽에서 Cluster 채널에 메세지를 보내보겠습니다. 

```
publish 채널이름 메세지
ex) publish Cluster "Hello"
```

<br>

이번에는 터미널 4개를 열고 테스트를 해보겠습니다.

<img width="1301" alt="스크린샷 2021-04-29 오전 11 21 26" src="https://user-images.githubusercontent.com/45676906/116495154-56a7ac00-a8dd-11eb-8982-14c175cc4ae9.png">

즉, `Cluster` 채널을 3명이 구독하고 있는 상태에서 `Redis Test`라는 메세지를 보냈습니다. 그러니 위와 같이 구독자 3명에게 동시에 모두 화면에 출력되는 것을 볼 수 있습니다. 


