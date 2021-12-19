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

<br> <br>

## `MockMvc`

```java
class Test {
    @Test
    @DisplayName("가장 모임이 많은 책을 조회할 수 있다")
    void getBookHavingMostGroupTest() throws Exception {
        // given
        given(bookService.getBookHavingMostGroup()).willReturn(DUMMY_BOOK_RESPONSE_DTO);

        // when
        var actions = mvc.perform(get("/api/v2/book/group/most")
                .contentType(MediaType.APPLICATION_JSON));

        // then
        MvcResult mvcResult = actions
                .andExpect(status().isOk())
                .andReturn();
        
        List<BookResponseDto> bookResponseDtos = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<>() {
        });
    }
}
```

- andExpect() 도 있지만, 위의 것도 있다. 참고합시다.