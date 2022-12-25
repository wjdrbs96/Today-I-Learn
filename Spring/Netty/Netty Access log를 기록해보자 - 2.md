## `Netty Access log를 기록해보자 - 2`

```kotlin
@Component
class LoggingFilter(val requestLogger: RequestLogger, val requestIdFactory: RequestIdFactory) : WebFilter {
    val logger = logger()

    override fun filter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void> {
        logger.info(requestLogger.getRequestMessage(exchange))
        val filter = chain.filter(exchange)
        exchange.response.beforeCommit {
            logger.info(requestLogger.getResponseMessage(exchange))
            Mono.empty()
        }
        return filter
    }
}

@Component
class RequestLogger {

    fun getRequestMessage(exchange: ServerWebExchange): String {
        val request = exchange.request
        val method = request.method
        val path = request.uri.path
        val acceptableMediaTypes = request.headers.accept
        val contentType = request.headers.contentType
        return ">>> $method $path ${HttpHeaders.ACCEPT}: $acceptableMediaTypes ${HttpHeaders.CONTENT_TYPE}: $contentType"
    }

    fun getResponseMessage(exchange: ServerWebExchange): String {
        val request = exchange.request
        val response = exchange.response
        val method = request.method
        val path = request.uri.path
        val statusCode = getStatus(response)
        val contentType = response.headers.contentType
        return "<<< $method $path HTTP${statusCode.value()} ${statusCode.reasonPhrase} ${HttpHeaders.CONTENT_TYPE}: $contentType"
    }

    private fun getStatus(response: ServerHttpResponse): HttpStatus =
        try {
            response.statusCode
        } catch (ex: Exception) {
            HttpStatus.CONTINUE
        }
}
```

<br>

###  `Reference`

- [https://stackoverflow.com/questions/45240005/how-to-log-request-and-response-bodies-in-spring-webflux](https://stackoverflow.com/questions/45240005/how-to-log-request-and-response-bodies-in-spring-webflux)