## `JUnit 어노테이션 정리`

## `@WebMvcTest`

`Spring MVC` 관련 `Bean`만 주입 된다. (Service, Repository는 Bean으로 등록되지 않음)

> @WebMvcTest will auto-configure the Spring MVC infrastructure and limit scanned beans to @Controller, @ControllerAdvice, @JsonComponent, Filter, WebMvcConfigurer and HandlerMethodArgumentResolver. Regular @Component beans will not be scanned when using this annotation.

<br> <br>

## `Spring Security WebMvcTest`

```java
@WebMvcTest(value = PostController.class,
    excludeFilters = {
        @ComponentSacn.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfig.class)  
    })
class PostControllerTest {
    
    @MockBean
    LoginUserIdArgumentResolver loginUserIdArgumentResolver;
    
    @MockBean
    JpaMetamodelMappingContext jpaMetamodelMappingContext;
}
```

