# `Spring Valid 사용하여 @ReuqestBody 검증하기`

이번 글에서는 Spring 에서 `@Valid`를 사용해서 @RequestBody를 통해서 들어오는 DTO 값들을 검증하는 법에 대해서 정리해보겠습니다. 그리고 프로젝트는 `Spring Boot gradle` 기반으로 만들어서 해보겠습니다. 

```
implementation 'org.springframework.boot:spring-boot-starter-validation'
```

먼저 `@Valid`를 사용하기 위해서 `build.gradle`에 위의 의존성을 추가하겠습니다.

<br>

## `Valid 적용하기`

```java
@RequestMapping("/api/v1/test")
@RestController
public class TestController {

    @PostMapping
    public String validTest(@RequestBody @Valid TestDto testDto) {
        return "test";
    }
}
```

먼저 위와 같이 `Controller` 코드를 간단하게 작성해보겠습니다. 그리고 `@Valid` 어노테이션을 사용하면 DTO 검증 로직을 적용할 수 있습니다.  

<br>

## `TestDto`

```java
@Getter
public class TestDto {

    @NotNull
    private String name;

    @Email
    private String email;
}
```

DTO 형태는 위와 같이 `name`, `email`을 받고 있습니다. 그리고 `@NotNull`, `@Email` 어노테이션이 있는데요. 각 어노테이션의 역할은 아래와 같습니다. 

- `@NotNull`: 값이 null 일 수 없다.
- `@Email`: 이메일 형식으로 오지 않으면 에러가 발생한다.

<br>

```json
{
    "name" : "Gyun",
    "email": "wjdrbs"   
}
```

그래서 위와 같이 `email`을 형식에 맞지 않게 요청을 보내보겠습니다. 

```json
{
    "timestamp": "2021-10-11T04:40:24.925+00:00",
    "status": 400,
    "error": "Bad Request",
    "path": "/api/v1/test"
}
```

그러면 Spring Boot에서 Default로 설정되어 있는 에러 처리로 위와 같은 형태로 400 에러가 응답으로 오게 됩니다. 

![스크린샷 2021-10-11 오후 1 44 05](https://user-images.githubusercontent.com/45676906/136734462-1ef9c007-8503-48fe-87bd-05f82869a24a.png)

그리고 위와 같이 `올바른 형식의 이메일 주소여야 합니다.` 라는 로그가 찍히게 됩니다. 이번에는 `name`을 null 값으로 보내보겠습니다. 

<br>

```json
{
  "email": "wjdrbs@naver.com"
}
```

위와 같이 email 형식은 맞추고 name은 null로 요청을 보내보겠습니다. 

```json
{
    "timestamp": "2021-10-11T04:46:39.921+00:00",
    "status": 400,
    "error": "Bad Request",
    "path": "/api/v1/test"
}
```

![스크린샷 2021-10-11 오후 1 48 12](https://user-images.githubusercontent.com/45676906/136734732-36ae8ee8-ac9d-468c-ba95-5cc48217ed29.png)

그리고 응답은 위에서 보았던 것과 마찬가지로 Spring Boot Default 400 Error로 응답이 오게 됩니다. (`널이어서는 안됩니다` 라는 로그도 찍힌 것을 볼 수 있습니다.) 

<br> <br>

## `@Valid시 Exception Handling 하기`

위에서는 Spring Boot에서 Default로 만들어준 에러 형태를 사용했는데요. 이번에는 에러 Response를 직접 커스텀해서 한번 만들어보겠습니다. 

![스크린샷 2021-10-11 오후 1 56 44](https://user-images.githubusercontent.com/45676906/136735339-5f1a58d9-36fd-486a-8733-11ee53f4bbec.png)

위에서 보았던 에러 로그를 보면 Valid 에러가 발생 했을 때 `MethodArgumentNotValidException` 예외 클래스가 호출되는 것을 볼 수 있습니다. 즉, `ControllerAdvice`, `ExceptionHandler`로 예외를 처리하기 위해서는 저 클래스를 예외로 잡아서 핸들링 해주면 됩니다. 이번 글에서 간단하게 ExceptionHanding 하는 예제 코드에 대해서 알아보겠습니다. 

<br> <br>

## `ErrorCdoe`

```java
public enum ErrorCode {
    
    INVALID_INPUT_VALUE(400, "INVALID INPUT VALUE");

    private final int status;
    private final String message;

    ErrorCode(final int status, final String message) {
        this.status = status;
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }

    public int getStatus() {
        return status;
    }

}
```

먼저 HTTP Status Code, Response Message 를 보내주기 위해서 위와 같이 `ErrorCode`를 Enum 으로 만들었습니다. 그리고 실제 ErrorCode를 가지고 ErrorResponse를 응답으로 주기 위해서 ErrorResponse Class를 아래와 같이 만들었습니다. 

<br> <br>

## `ErrorResponse`

```java
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ErrorResponse {

    private String message;
    private int status;
    private List<FieldError> errors;

    private ErrorResponse(final ErrorCode code, final List<FieldError> errors) {
        this.message = code.getMessage();
        this.status = code.getStatus();
        this.errors = errors;
    }

    public static ErrorResponse of(final ErrorCode code, final BindingResult bindingResult) {
        return new ErrorResponse(code, FieldError.of(bindingResult));
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class FieldError {
        private String field;
        private String value;

        private FieldError(final String field, final String value) {
            this.field = field;
            this.value = value;
        }

        private static List<FieldError> of(final BindingResult bindingResult) {
            final List<org.springframework.validation.FieldError> fieldErrors = bindingResult.getFieldErrors();
            return fieldErrors.stream()
                    .map(error -> new FieldError(
                            error.getField(),
                            error.getRejectedValue() == null ? "" : error.getRejectedValue().toString()))
                    .collect(Collectors.toList());
        }
    }

}
```

ErrorResponse 에서 `FieldError` 클래스를 만들어준 이유는 어떤 필드에서 어떤 값으로 에러가 났는지를 응답 값으로 주기 위해서 입니다. 다른 원하는 형식의 Response가 있다면 커스텀 해서 사용해도 좋을 거 같습니다.   

<br> <br>

## `ExceptionHandling`

```java
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        final var response = ErrorResponse.of(ErrorCode.INVALID_INPUT_VALUE, e.getBindingResult());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

}
```

그리고 `@ControllerAdvice`, `@ExceptionHandler`를 통해서 예외 처리를 하고 있습니다. 즉 `@Valid`를 통해서 `RequestBody`로 요청이 오는 DTO 필드를 검증하려 하는데, 만약 잘못되어 있다면 `MethodArgumentNotValidException` 에러가 발생하여 위의 메소드가 실행이 됩니다.  

![스크린샷 2021-10-11 오후 3 19 29](https://user-images.githubusercontent.com/45676906/136742017-b00f09e1-1f1c-42af-bcd0-f367e00fdc5b.png)

이번에도 `email` 형식에 맞지 않게 보내보면 `ErrorResponse`에 정의한 형식대로 응답이 오는 것을 볼 수 있습니다. 

<br> <br>

## `Bean Validation의 주요 어노테이션`

| 애노테이션 | 주요 속성 | 설명 | 지원 타입 |
|----------|---------------|-------------|------------|
| @AssertTrue, @AssertFalse |  | 값이 true인지 또는 false인지 검사한다. null은 유효하다고 판단한다. | boolean, Boolean |
| @DecimalMax, @DecimalMin | String value (최대값 또는 최소 값) | 지정한 값보다 작거나 같은지 또는 크거나 같은지 검사한다.  | BigDecimal, BigInteger, CharSequence, 정수타입 |
| @Max, @Min | long value | 지정한 값보다 작거나 같은지 또는 크거나 같은지 검사한다.(`@max는 100 까지 가능하고 100 미만의 값이 오면 에러가 발생합니다. @min은 최소 100이 되어야 정상적으로 넘어가고 100 미만인 경우에는 에러가 발생합니다.`) null은 유효하다고 판단한다. | BigDecimal, BigInteger, 정수타입 |
| @Digits | int Integer(최대 정수 자릿수) <br> int traction (최대 소수점 자릿수) | 자릿수가 지정한 크기를 넘지 않는지 검사한다. null은 유효하다고 판단한다. | BigDecimal, BigInteger, CharSequence 정수타입 |
| @Size | int min(최소 크기, 기본 값 0) <br> int max(최대 크기, 기본 값 정수 최대 값) | 길이나 크기가 지정한 값 범위에 있는지 검사한다. null은 유효하다고 판단한다. | CharSequence, Collection, Map, 배열 |
| @Null, @NotNull | | 값이 null 인지 또는 null이 아닌지 검사한다. | |
| @Pattern | String regexp (정규 표현식) | 값이 정규표현식에 일치하는지 검사한다. null은 유효하다고 판단한다. | CharSequence |

<br>

| 애노테이션 | 설명 | 지원 타입 |
|----------|-------------|------------|
| @NotEmpty | 문자열이나 배열의 경우 null이 아니고 길이가 0이 아닌지 검사한다. 컬렉션의 경우 null이 아니고 크기가 0이 아닌지 검사한다. | CharSequence, Collection, Map, 배열 |
| @NotBlank | null이 아니고 최소한 한 개 이상의 공백아닌 문자를 포함하는지 검사한다. | CharSequence |
| @Positive, @PositiveOrZero | 양수인지 검사한다. OrZero가 붙은 것은 0 또는 양수인지 검사한다. null은 유효하다고 판단한다.  | BigDecimal, BigInteger, 정수타입 |
| @Negative, @NegativeOrZero | 음수인지 검사한다. OrZero가 붙은 것은 0 또는 음수인지 검사한다. null은 유효하다고 판단한다. | BigDecimal, BigInteger, 정수타입 |
| @Email | 이메일 주소가 유효한지 검사한다. null은 유효하다고 판단한다. | CharSequence |
| @Future, @FutureOrPresent | 해당 시간이 미래 시간인지 검사한다. OrPresent가 붙은 것은 현재 또는 미래 시간인지 검사한다. null은 유효하다고 판단한다. | 시간 관련 타입 |
| @Past, @PastOrPresent | 해당 시간이 과거 시간인지 검사한다. OrPresent가 붙은 것은 현재 또는 과거 시간인지 검사한다. null은 유효하다고 판단한다. | 시간 관련 타입 |