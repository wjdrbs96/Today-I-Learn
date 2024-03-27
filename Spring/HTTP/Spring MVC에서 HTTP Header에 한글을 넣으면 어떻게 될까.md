## `Spring MVC에서 HTTP Header에 한글을 넣으면 어떻게 될까?`

이번 글에서는 `HTTP Header에 한글을 사용한다면 어떻게 되는지` 간단하게 Spring MVC를 사용하여 알아보겠습니다.

- [String 인코딩, 디코딩]()
- [HTTP Message](https://developer.mozilla.org/ko/docs/Web/HTTP/Messages)

위의 2가지에 대한 개념이 부족하다면 먼저 읽고 오는 것을 추천 드립니다. 

<br>

```java
@RestController
public class TestController {

    @GetMapping("/test/header")
    public String test(
        HttpServletRequest httpServletRequest
    ) throws UnsupportedEncodingException {
        String korean = httpServletRequest.getHeader("korean");
        System.out.println("korean: " + korean);
        return "success";
    }
}
```
```
korean:íì´
```

```
curl -H "korean: 하이" -XGET "http://localhost:8080/test/header"
```

위처럼 HTTP Header에 한글로 요청하면 위처럼 알 수 없는 문자로 결과가 출력되는 것을 볼 수 있습니다. 한글이 정상적으로 출력되지 못하고 깨지는 이유가 무엇일까요? 

<br>

## `HTTP Header default charset`

> By default, message header field parameters in Hypertext Transfer Protocol (HTTP) messages cannot carry characters outside the ISO-8859-1 character set.

우선 HTTP [rfc-5987](https://datatracker.ietf.org/doc/html/rfc5987) 문서를 보면 위와 같이 `ISO-8859-1` 이외의 문자는 HTTP Header로 전달할 수 없다고 나와있습니다.

즉, 한글은 `표준 규격(ISO-8859-1)`에 맞지 않기 때문에 HTTP Message에 한글을 사용하면 깨져서 출력되는 것입니다.

그렇다면 HTTP 메세지에 한글을 보낼 때 Spring MVC의 어떤 과정을 거쳐서 Controller 까지 전달되는 것일까요? 추가로 표준 규격에 맞지 않기 때문에 HTTP 메세지에 한글을 보내야 하는 경우라면 어떻게 할지 좀 더 알아보겠습니다.

<br>

## `Spring MVC HTTP Header Charset`



```java
@RestController
public class TestController {

    @GetMapping("/test")
    public void test(@RequestHeader String header) {
        String result = new String(header.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
        System.out.println("header : " + result);
    }
}
```

즉, 헤더에 들어온 값을 `UTF-8`로 인코딩하고 `ISO_8859_1`로 디코딩 하여 넘어왔기 때문에 값이 이상하게 깨져있는 것입니다.

다시 한글로 돌려놓기 위해서는 `ISO_8859_1`로 디코딩 되어 있는 문자열을 `ISO_8859_1`로 인코딩 시키고, `UTF-8`로 디코딩 시키면 원하는 한글을 확인할 수 있습니다.

- CharacterEncodingFilter : 인코딩 (UTF-8)
- ByteChunk: 디코딩 (ISO)

<br>

## `Referenee`

- [https://developer.mozilla.org/ko/docs/Web/HTTP/Messages](https://developer.mozilla.org/ko/docs/Web/HTTP/Messages)
- [https://jcloud.pro/hangul-should-not-enter-in-http-header](https://jcloud.pro/hangul-should-not-enter-in-http-header)
- [https://stackoverflow.com/questions/37793628/encoding-of-request-headers-in-rest-spring-boot-controller](https://stackoverflow.com/questions/37793628/encoding-of-request-headers-in-rest-spring-boot-controller)
- [https://www.w3.org/Protocols/rfc2068/rfc2068.txt](https://www.w3.org/Protocols/rfc2068/rfc2068.txt)
- [https://datatracker.ietf.org/doc/html/rfc5987](https://datatracker.ietf.org/doc/html/rfc5987)
- [https://datatracker.ietf.org/doc/html/rfc5987](https://datatracker.ietf.org/doc/html/rfc5987)
- [https://github.com/spring-projects/spring-framework/blob/main/spring-web/src/main/java/org/springframework/web/filter/CharacterEncodingFilter.java#L193](https://github.com/spring-projects/spring-framework/blob/main/spring-web/src/main/java/org/springframework/web/filter/CharacterEncodingFilter.java#L193)
- [https://github.com/apache/tomcat/blob/main/java/org/apache/tomcat/util/buf/ByteChunk.java](https://github.com/apache/tomcat/blob/main/java/org/apache/tomcat/util/buf/ByteChunk.java)
- [https://github.com/spring-projects/spring-boot/issues/1182](https://github.com/spring-projects/spring-boot/issues/1182)