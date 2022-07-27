## `Swagger Request, Response 데이터 표시하기`

```java
@ApiModelProperty(value = "키워드", example = "유머 있는")
```

위의 어노테이션을 사용해서 `Reqeust DTO`, `Response DTO`에 넣어주면 스웨거에 `example`에 있는 값이 보여서 보기 이뻐진다.

<br>

```java
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class WeddingParticipateListDTO {

    @ApiModelProperty(value = "웨딩 Id", example = "1L")
    private Long weddingId;

    @ApiModelProperty(value = "신랑 이름", example = "자바")
    private String groomName;

    @ApiModelProperty(value = "신부 이름", example = "코틀린")
    private String brideName;

    @ApiModelProperty(value = "결혼식 날짜", example = "2022-07-27")
    private LocalDate weddingDate;
}
```

![스크린샷 2022-07-28 오전 1 02 13](https://user-images.githubusercontent.com/45676906/181294841-f437c893-a035-4976-ba92-6e1e2920a2fa.png)

