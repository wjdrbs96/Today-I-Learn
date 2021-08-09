# `WebSocket SendTo PathVariable`

```java
@Controller
public class ChatController {
    

    @MessageMapping("/chat.sendMessage/{idx}")
    @SendTo("/topic/public/{idx}")
    public ChatMessage sendMessage(@Payload ChatMessage chatMessage, @DestinationVariable String idx) {
        chatMapper.saveChat(chatMessage.getContent());
        return chatMessage;
    }

    @MessageMapping("/chat.addUser/{idx}")
    @SendTo("/topic/public/{idx}")
    public ChatMessage addUser(@Payload ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor, @DestinationVariable String idx){
        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
        return chatMessage;
    }
}
```

`@SendTo`와 `MessageMapping`에서 `PathVariable` 처럼 사용하려면 어떻게 해야 할까 하고 보니, `@DestinationVariable` 어노테이션이 존재했음.
`sendTo`에만 `/{idx}` 하고 사용하니 적용이 안됐고, `SendTo`, `MessaginMapping` 같이 `/{idx}`를 적용하면 됨