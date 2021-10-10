# `Spring Valid DTO 감싸져 있을 때 사용 법`

```java
@Getter
public class GroupMakeDto {
    
    @JsonProperty("group")
    private GroupInsertRequestDto groupInsertDto;
    
    @JsonProperty("book")
    private BookInsertRequestDto bookInsertRequestDto;
    
    @JsonProperty("question")
    private QuestionInsertRequestDto questionInsertDto;

}
```

위와 같이 `DTO 안에 DTO가 존재할 때 Valid를 어떻게 할 수 있을까?` 인데요. 결론을 알면 정말 단순하지만 알기 전에는 약간 애매해서 한번 간단하게 정리를 해보려 합니다.

![스크린샷 2021-10-11 오전 2 07 11](https://user-images.githubusercontent.com/45676906/136706078-a22a3a02-f2e0-440a-8271-d9bf83fd6589.png)

위와 같이 `@Valid`를 사용해서 하는 법은 알고 있을텐데요. 이거 하나만 사용하면 `Valid`가 적용되지 않아서 여기에 추가로 아래와 같이 설정하여야 합니다. 

```java
@Getter
public class GroupMakeDto {

    @Valid
    @JsonProperty("group")
    private GroupInsertRequestDto groupInsertDto;

    @Valid
    @JsonProperty("book")
    private BookInsertRequestDto bookInsertRequestDto;

    @Valid
    @JsonProperty("question")
    private QuestionInsertRequestDto questionInsertDto;

}
```

DTO 클래스마다 `@Valid`를 선언해주면 정상적으로 Valid가 작동하는 것을 볼 수 있습니다. 