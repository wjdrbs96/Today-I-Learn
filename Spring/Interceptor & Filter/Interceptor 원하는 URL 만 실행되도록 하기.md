## `Interceptor 원하는 URL만 실행되도록 하기`

```java
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor())
                .addPathPatterns("/api/v1/test1")
                .addPathPatterns("/api/v1/test2")
                .addPathPatterns("/api/v1/test3");
    }
    
    @Bean
    public AuthInterceptor authInterceptor() {
        return new AuthInterceptor();
    }
    
}
```

위와 같이 `addPathPatterns`를 사용하면 원하는 `URL`만 인터셉터를 거치도록 할 수 있다.  