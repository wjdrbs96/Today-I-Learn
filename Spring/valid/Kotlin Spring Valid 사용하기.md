## `Kotlin Spring Boot에서 @Valid로 @RequstBody DTO 필드 검증하기`

[저번 글](https://devlog-wjdrbs96.tistory.com/402) 에서 `Java`, `Spring Boot`에서 `@Valid`를 사용하는 법에 대해서 다루었는데요. 이번 글에서는 `Kotlin`, `Sprign Boot`에서 `@Valid`로 검증하는 법에 대해서 알아보겠습니다. 

Java에서 Kotlin 으로 언어가 바뀐거 밖에 차이가 없기 때문에 `Valid`에 대한 개념은 완전히 똑같은데요. 하지만 Kotlin 에서는 한가지 더 해주어야 하는 것이 있어서 그거에 대해서 알아보고 왜 그걸 추가해야 가능한 것인지를 알아보겠습니다. 

```
implementation("org.springframework.boot:spring-boot-starter-validation")
```

