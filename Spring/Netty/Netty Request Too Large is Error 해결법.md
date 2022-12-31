## `Netty Request Too Large is Error 해결법`

### `Kotlin`

```kotlin
@Component
class NettyCustomizer : WebServerFactoryCustomizer<NettyReactiveWebServerFactory> {

    override fun customize(factory: NettyReactiveWebServerFactory?) {
        factory?.addServerCustomizers(NettyServerCustomizer {
                httpServer: HttpServer? -> httpServer?.httpRequestDecoder {
                    httpRequestDecoderSpec: HttpRequestDecoderSpec -> httpRequestDecoderSpec.maxInitialLineLength(80960)
            }
        })
    }
}
```

<br>

### `Java`

```java
@Component
public class NettyCustomizer implements WebServerFactoryCustomizer<NettyReactiveWebServerFactory> {

    @Override
    public void customize(NettyReactiveWebServerFactory serverFactory) {
        serverFactory.addServerCustomizers(httpServer -> httpServer.httpRequestDecoder(
            httpRequestDecoderSpec -> httpRequestDecoderSpec
                .maxInitialLineLength(80960)
        ));
    }
}
```

- Netty Request Header Size Default 값은 4KB 인데, 이 값을 늘려주는 설정이다.