# `스프링부트에서 JSP 사용하는 법`

이번 글에서는 스프링 부트에서 jsp를 사용하기 위해 설정하는 법에 대해서 정리하려 합니다.

spring-boot-starter-web 에 포함된 tomcat 은 JSP 엔진을 포함하고 있지 않습니다. jsp 파일은 Springboot 의 templates 폴더안에서 작동하지 않습니다. 그래서 jsp를 적용하기 위해서는 아래와 같은 의존성을 추가해야합니다.

<br>

## `pom.xml`
```
<dependency>
    <groupId>javax.servlet</groupId>
    <artifactId>jstl</artifactId>
</dependency>
<dependency>
    <groupId>org.apache.tomcat.embed</groupId>
    <artifactId>tomcat-embed-jasper</artifactId>
</dependency>
```

<br>

## `build.gradle`

```
dependencies {
    compile('javax.servlet:jstl')
    compile("org.apache.tomcat.embed:tomcat-embed-jasper")
}
```

기본적으로 스프링부트는 내장톰캣을 가지고 있지만, jsp 엔진이 존재하지 않기 때문에 jasper와 jsp의 라이브러리 jstl을 사용할 수 있는 의존성을 추가해줘야 합니다.

<br>

## `뷰 경로 지정`

앞서 말했듯이 스프링 부트에서 jsp를 기본 지원하지 않기 때문에 아래와 같은 정보를 application.properties에 설정해서 스프링 부트에게 jsp 뷰의 경로를 알려주어야 합니다.

<br>

## `application.properties`

```
spring.mvc.view.prefix=/WEB-INF/jsp/
spring.mvc.view.suffix=.jsp
```

위와 같이 추가하게 되면 jsp 폴더 앞에 경로는 `webapp/WEB-INF/test.jsp` 입니다. prefix는 앞에 폴더의 경로이고 suffix는 Controller를 통해서 view의 이름을 통해서 return 하면 뒤에 .jsp가 붙게 해주는 역할을 합니다.

폴더구조는 아래와 같고 webapp 디렉토리를 만들어야 합니다. webapp 이라는 디렉토리 경로가 default로 내부적으로 설정이 되어 있어서 그렇습니다.

![1](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2F4yJ5F%2FbtqGJFV0zgi%2FsWTCaBljs8D7KCwkEG1jG0%2Fimg.png)

<br> 

```java
@EnableWebMvc
@Configuration
public class MvcConfig implements WebMvcConfigurer {
    
    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
        registry.jsp("/WEB-INF/view/", "jsp");
    }
}
```

jsp prefix, suffix를 application.properties에 설정해도 되지만, 위와 같이 `ViewResolverRegistry`를 이용해서 prefix, suffix를 지정하는 것도 가능합니다.  

<br> <br>

## `예제 코드`

```java
@Controller
public class TestController {

    @GetMapping("/post")
    public String test(Model model) {
        Post post1 = new Post(1, "lee", "book1");
        Post post2 = new Post(2, "choi", "book2");
        Post post3 = new Post(3, "kim", "book3");
        List<Post> list = new ArrayList<>();
        list.add(post1);
        list.add(post2);
        list.add(post3);
        model.addAttribute("list", list);
        return "test";
    }
}
```


List에 Post 클래스를 담기위해서 post 객체 3개를 만들어서 list에 담은 후에 model에 데이터를 담았습니다. 그리고 jsp에서 model에 담긴 데이터를 꺼내서 확인해보겠습니다.

<br>

```html
<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="ko">
<body>

<div class="container">
    <table class="table table-hover table table-striped">
        <tr>
            <th>번호</th>
            <th>작성자</th>
            <th>제목</th>
        </tr>

        <c:forEach items="${list}" var="post">
            <tr>
                <th>${post.getPostId()}</th>
                <th>${post.getNickName()}</th>
                <th>${post.getTitle()}</th>
            </tr>
        </c:forEach>
    </table>
</div>

</body>
</html>
```

jstl을 이용해서 list에 담긴 데이터를 꺼내는 예제입니다. 





