# `System.out.println()을 사용하지 않아야 하는 이유`

흔히 자바에서 출력을 할 때 `System.out.println()` 메소드를 사용합니다. `하지만 실제 운영상의 소스코드에서는 절대 사용하면 안된다고 합니다.` 그리고 현업에서도 사용하지 않는 메소드라고 하는데요.
그 이유가 무엇인지 알아보겠습니다. 

<br>

## `첫 번째 이유`

![스크린샷 2021-03-27 오후 11 40 51](https://user-images.githubusercontent.com/45676906/112724281-faf0a880-8f55-11eb-8255-478c8fdd69da.png)

println() 메소드의 내부를 보면 위와 같이 내부적으로 `synchronized` 키워드가 붙어있는 것을 볼 수 있습니다. `synchronized` 키워드가 있다는 것은 어떤 것을 의미하는 것일까요?
해당 `synchronized 블럭`이 실행될 때는 다른 쓰레드는 작동하지 못하고 하나의 쓰레드만 실행되게 됩니다. 이게 어떠한 문제가 있을까요? 

간단하게 예를 들어보겠습니다. 실제 운영 중인 어떤 시스템에서 여러 개의 멀티 쓰레드를 운영 중일 때 하나의 쓰레드에서 `System.out.println()`를 사용하면 이거 하나로 인해서 `Lock`을 점유해버리게 됩니다. 즉, 다른 쓰레드는 병목현상이 생기게 될 것입니다.
이는 시스템의 장애를 일으키는 문제가 될 수 있습니다. 

<br>

## `두 번째 이유`

`System.out.println()을 사용하면 print할 로그를 제어하거나 필터링하지 않습니다.` 로그를 분리할 수 있는 유일한 방법은 정보 로그에 System.out.println을 사용하고 오류 로그에 `System.err.println()`을 사용하는 것입니다.
이게 구체적으로 어떤 말일까요? 예를들어 디버그 또는 개발 환경에서는 응용프로그램이 인쇄하는 모든 정보를 보고자 한다고 가정하겠습니다. 그러나 `System.out.println()`은 필터링 기능이 없기 때문에 모든 에러를 출력해야할 것입니다. 

하지만 로그가 엄청나게 쌓여 있다면 이 또한 매번 필요하지 않은 로그까지 모두 다 보는 것은 비효율적일 것입니다.  
 
<br>

### `Log4J2 Logging Level2`

- `FATAL`
- `ERROR`
- `WARN`
- `INFO`
- `DEBUG`
- `TRACE`
- `ALL`

`Log4J2`는 위와 같이 프린트할 로깅을 나누어 놓아 필터링 할 수 있습니다. 즉, Log4J2와 같은 Logger 프레임워크는 다음과 같은 다양한 로그 수준 제어를 제공한다는 장점이 있습니다. 

```
logger.trace("Trace log message");
logger.debug("Debug log message");
logger.info("Info log message");
logger.error("Error log message");
logger.warn("Warn log message");
logger.fatal("Fatal log message");
```

이외에도 여러 내용이 있지만 간단하게 `System.out.println()`을 사용하지 말아야 하는 이유에 대해 알아보았습니다. 