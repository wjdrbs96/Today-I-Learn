## `Spring MVC에서 HTTP Header에 한글을 넣으면 어떻게 될까?`

> Spring MVC에서 Tomcat 사용한다고 가정하고 간단하게 작성한 글입니다.

문자열 인코딩, 디코딩에 대해 이해가 필요하다면 [여기](https://github.com/wjdrbs96/Today-I-Learn/blob/master/Java/String/%EB%AC%B8%EC%9E%90%EC%97%B4%20%EC%9D%B8%EC%BD%94%EB%94%A9%2C%20%EB%94%94%EC%BD%94%EB%94%A9.md) 글을 먼저 읽고 오시는 것을 추천드립니다.

이번 글에서는 `Spring Boot에서 HTTP Header에 한글 이름을 넣으면 어떻게 되는지, 깨진다면 왜 깨지는 것`인지 간단하게 알아보겠습니다.

<br>

```java
public class Test {
    public static void main(String[] args) throws UnsupportedEncodingException {
        String korean = "하이";

        byte[] isoBytes = korean.getBytes(StandardCharsets.ISO_8859_1);
        byte[] utf8Bytes = korean.getBytes(StandardCharsets.UTF_8);

        String result1 = new String(isoBytes, StandardCharsets.ISO_8859_1);
        String result2 = new String(utf8Bytes, StandardCharsets.UTF_8);

        System.out.println("result1: " + result1);
        System.out.println("result2: " + result2);
    }
}
```

먼저 위의 코드에 대해 결과가 어떻게 될까요? 

<details>
<summary>자세히 보기</summary>

```
result1: ??
result2: 하이
```

`ASCII(ISO_8859_1)` 로는 한글을 표현할 수 없기 때문에, `ISO_8859_1`로 인코딩한 경우 올바르게 디코딩 되지 않고 깨지는 것을 볼 수 있습니다.

</details>

위의 코드에 대해 이해했다면 Spring Boot에서 HTTP Header에 한글 이름을 넣으면 어떻게 될지 알아보겠습니다. 

<br>

```java
@RestController
public class TestController {

    @GetMapping("/test/header")
    public String test(
        @RequestHeader String korean,
        HttpServletRequest httpServletRequest
    ) throws UnsupportedEncodingException {
        String koreanServlet = httpServletRequest.getHeader("korean");
        System.out.println("koreanServlet: " + koreanServlet);
        System.out.println("korean:" + korean);
        return "success";
    }
}
```

```
curl -H "korean: 하이" -XGET "http://localhost:8080/test/header"
```

위처럼 HTTP Header에서 한글 이름을 받으면 결과는 어떻게 될까요? 

<details>
<summary>자세히 보기</summary>

```
header1: íì´
header:íì´
```

알 수 없는 문자로 깨져서 한글로 올바르게 출력되지 않는 것을 볼 수 있습니다.

이유는 Spring MVC에서 default charset을 `ISO_8859_1`을 쓰기 때문입니다.

즉, 한글을 `ISO_8859_1`로 인코딩 하려 했기 때문에 한글이 깨진 것이기 때문에 ``

</details>



<br>

## `Referenee`

- [https://stackoverflow.com/questions/37793628/encoding-of-request-headers-in-rest-spring-boot-controller](https://stackoverflow.com/questions/37793628/encoding-of-request-headers-in-rest-spring-boot-controller)
- [https://github.com/spring-projects/spring-boot/issues/1182](https://github.com/spring-projects/spring-boot/issues/1182)
- [https://www.w3.org/Protocols/rfc2068/rfc2068.txt](https://www.w3.org/Protocols/rfc2068/rfc2068.txt)