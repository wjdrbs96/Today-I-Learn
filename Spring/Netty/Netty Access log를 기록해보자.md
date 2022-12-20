## `Netty Access log를 기록하기`

```java
    @Component
public class MyNettyWebServerCustomizer implements WebServerFactoryCustomizer<NettyReactiveWebServerFactory> {
    
    @Override
    public void customize(NettyReactiveWebServerFactory factory) {
        factory.addServerCustomizers(httpServer -> httpServer.accessLog(true, x -> AccessLog.create("method={}, uri={}", x.method(), x.uri())));
    }
}
```

<br>

```kotlin
@Configuration
class MyNettyWebServerCustomizer : WebServerFactoryCustomizer<NettyReactiveWebServerFactory> {

    override fun customize(factory: NettyReactiveWebServerFactory?) {
        factory?.addServerCustomizers(NettyServerCustomizer {
                httpServer: HttpServer? -> httpServer?.accessLog(true) {
                    x -> AccessLog.create("method={}, uri={}, status={}, accessDateTime={}",
                        x.method(),
                        x.uri(),
                        x.status(),
                        x.accessDateTime()?.toLocalDateTime()
                )
            }
        })
    }
}
```

위처럼 필요한 부분만 Access log 출력하도록 커스텀할 수 있다.

<br>

```xml
<configuration>
    <appender name="accessLog" class="ch.qos.logback.core.FileAppender">
        <file>access_log.log</file>
        <encoder>
            <pattern>%msg%n</pattern>
        </encoder>
    </appender>

    <appender name="async" class="ch.qos.logback.classic.AsyncAppender">
        <appender-ref ref="accessLog" />
    </appender>

    <logger name="reactor.netty.http.server.AccessLog" level="INFO" additivity="false">
        <appender-ref ref="async"/>
    </logger>
</configuration>
```

Logback에 위의 내용을 추가해준다. 