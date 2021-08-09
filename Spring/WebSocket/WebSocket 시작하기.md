# `WebSocket-STOMP 시작하기`

이번 글에서는 `Spring WebSocket`과 `STOMP`를 사용하여 아주 간단한 채팅을 구현해보는 예제를 살펴보겠습니다. Spring Boot Gradle 기반의 프로젝트가 세팅되어 있다 가정하고 시작해보겠습니다. 

## `gradle`

```
implementation 'org.springframework.boot:spring-boot-starter-websocket'
```

채팅을 구현하기에 가장 먼저 떠오르는 것은 SOCKET 입니다. 먼저 WebSocket 의존성을 추가해주겠습니다. 
