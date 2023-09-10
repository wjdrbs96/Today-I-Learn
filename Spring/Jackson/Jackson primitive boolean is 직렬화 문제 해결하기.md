## `Primitive boolean Type에서 isXX에서 is가 사라지는 Jackson 직렬화 문제 해결하기`

이번 글에서는 `Spring Boot`에서 `DTO를 클라이언트에 JSON 으로 Response 할 때 isXXX 네이밍에서 is가 사라지는 문제`를 해결하는 아주 간단한 과정을 공유해보려 합니다. 

![스크린샷 2021-11-07 오후 4 57 37](https://user-images.githubusercontent.com/45676906/140637159-31283fd7-a713-4350-be08-f74f08c8c520.png)

프로젝트를 진행하면서 위와 같은 DTO를 클라이언트에게 반환하고 있었습니다. 보면 `Primitive Type boolean`의 `isFailedGroupQuiz` 라는 필드가 존재하는 것도 볼 수 있습니다. 그래서 이제 해당 DTO가 사용되는 API를 `Postman`으로 호출해보겠습니다. 

<br>

<img width="1008" alt="스크린샷 2021-11-07 오후 5 01 52" src="https://user-images.githubusercontent.com/45676906/140637246-9faadb28-eec7-4d57-9877-31f2a43dd951.png">

응답 값을 보면 DTO에 분명 `isFailedGroupQuiz` 라고 적었지만, `FailedGroupQuiz`라고 오는 것을 볼 수 있습니다. 왜 그렇지..? 라고 생각하고 `Jackson is remove` 식으로 찾아보니 [StackOverflow](https://stackoverflow.com/questions/32270422/jackson-renames-primitive-boolean-field-by-removing-is) 글을 하나 발견 했습니다. 여기에 보면 `@JsonProperty`를 사용해서 이름을 지정하라고 나오는데요. 

<br>

![스크린샷 2021-11-07 오후 5 07 29](https://user-images.githubusercontent.com/45676906/140637385-4bdd751b-594b-4602-83ed-082c652f226e.png)

그래서 위와 같이 `JsonProperty`를 사용해서 이름을 지정하고 위에서 실행했던 것과 똑같이 실행해보았습니다. 

<br>

<img width="1008" alt="스크린샷 2021-11-07 오후 5 01 52" src="https://user-images.githubusercontent.com/45676906/140637246-9faadb28-eec7-4d57-9877-31f2a43dd951.png">

결과는 위에서 했던 거랑 달라진 것이 없었습니다. 왜 그런가 좀 찾아보니 `Spring에서 사용하는 Jackson에서 직렬화를 해줄 때 is를 제거하여 직렬화를 한다`라고 하는데요. `Kotlin-Jackson-Module` 에서도 [같은 이슈](https://github.com/FasterXML/jackson-module-kotlin/issues/80) 가 존재한다고 합니다. 

<br> <br>

## `Jackson 직렬화 문제 해결하기`

![스크린샷 2021-11-07 오후 5 14 55](https://user-images.githubusercontent.com/45676906/140637551-59fced9d-47a9-4745-80a6-fc7639b3df65.png)

결론적으로 제가 해결한 방법은 아주 단순합니다. `isFailedGroupQuiz` 컬럼의 타입을 `Primitive Type`이 아니라 `Reference Type`을 사용하자 입니다. 

<br>

![스크린샷 2021-11-07 오후 5 18 06](https://user-images.githubusercontent.com/45676906/140637645-84455067-1a77-4f5e-982d-533beccf7595.png)

이번에는 `DTO`에 적었던 `isFailedGroupQuiz` 이름 그대로 응답이 오는 것을 볼 수 있습니다. 생각보다 단순했지만? 이렇게 해결한 경험을 공유하고자 글을 작성해보았습니다!   