# `WebSocket-Stomp 시작하기`

이번 글에서는 `Spring WebSocket`과 `Stomp`를 사용하여 아주 간단한 채팅을 구현해보는 예제를 살펴보겠습니다. Spring Boot Gradle 기반의 프로젝트가 세팅되어 있다 가정하겠습니다.

먼저 `Stomp`가 무엇인지 알아보겠습니다. 

<br> <br>

## `Stomp란?`




<br> <br>

## `gradle`

```
implementation 'org.springframework.boot:spring-boot-starter-websocket'
```

채팅을 구현하기에 가장 먼저 떠오르는 것은 SOCKET 입니다. 그래서 `WebSocket` 의존성을 추가해주겠습니다. 그리고 WebSocket 관련 설정을 해보겠습니다. 

<br> <br>

## `WebSocket 설정`

```java
@Configuration
@EnableWebSocketMessageBroker 
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    // 한 클라이언트에서 다른 클라이언트로 메시지를 라우팅 하는 데 사용될 메시지 브로커를 구성
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // 클라이언트에서 메세지 송신시 붙여줄 prefix 정의
        registry.setApplicationDestinationPrefixes("/app");
        
        // 클라이언트로 메세지를 응답해줄 때 prefix 정의
        registry.enableSimpleBroker("/topic");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // 최초 소켓을 연결하는 경우, endpoint가 되는 url
        registry.addEndpoint("/ws").withSockJS();
    }

}
```

- `registerStompEndpoints` 메소드는 최초의 소켓을 연결하는 `URI` 입니다. 즉, 클라이언트가 WebSocket 핸드셰이크를 위해 연결해야 하는 엔드포인트 URL입니다.

- `setApplicationDestinationPrefixes`: 메소드는 대상 헤더가 `"/app"`로 시작하는 STOMP 메시지는 `@Controller` 클래스의 `@MessageMapping 메서드`로 라우팅됩니다. 즉, 클라이언트에서 서버로 메세지를 전송할 때 넣어야 하는 URL Prefix 입니다.

- `enableSimpleBroker`: 메소드는 Subscribe 및 브로드캐스트에 기본 제공 메시지 브로커를 사용합니다. 대상 헤더가 `"/topic"` 으로 시작하는 메시지를 브로커로 라우팅합니다.

<br>

그리고 

