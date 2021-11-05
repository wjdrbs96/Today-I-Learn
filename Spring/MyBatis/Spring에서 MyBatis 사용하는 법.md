## `Spring에서 MyBatis 사용하는 법`

이번 글에서는 `Spring에서 MyBatis를 사용하는 법`에 대해서 정리해보겠습니다. Spring Boot에서 MyBatis 의존성 하나만 추가하면 많은 부분의 설정을 해주기 때문에 개발자는 아주 간단한 부분만 작성을 해주면 되는데요. 그 간단한 것을 어떻게 작성해서 사용하는지에 대해서 알아보겠습니다.

### `Maven`

```
<dependency>
    <groupId>org.mybatis.spring.boot</groupId>
    <artifactId>mybatis-spring-boot-starter</artifactId>
    <version>1.3.2</version>
</dependency>
```

### `Gradle`

```
implementation 'org.mybatis.spring.boot:mybatis-spring-boot-starter:2.1.4'
```

Spring Boot Project를 만들고 위의 의존성을 추가하겠습니다. Spring Boot에서 MyBatis를 사용하는 방법은 크게 2가지 방법이 존재하는데요. 하나씩 어떻게 사용하는지 알아보겠습니다.

<br> <br>

## `쿼리 어노테이션 사용하기`

![mapper](https://user-images.githubusercontent.com/45676906/119588523-f50b2c80-be0b-11eb-8514-2db4a84e95d1.png)

MyBatis 의존성을 추가하면 `@Mapper`라는 어노테이션을 위와 같이 사용할 수 있는데요. `Mapper` 어노테이션을 사용해서 `Mapper interface`를 만들고 해당 인터페이스에서 `@Select`, `@Insert`, `Update`, `@Delete` 어노테이션을 사용해서 쿼리를 작성할 수 있습니다. 위의 코드의 예시를 들면 `getGroup()` 메소드가 호출되면 `@Select`로 작성한 쿼리가 실행되는 것입니다.

하지만 쿼리가 복잡해지고 길어지면 메소드 위에다 전부 작성하는 것이 쉽지도 않고 가독성도 좋지 않다고 생각합니다. 더군다나 자바 코드와 SQL 코드가 같이 있다는 것도 좋지 않은 느낌인데요. 그래서 저는 두 번째로 소개할 방법처럼 XML을 사용하는 것을 좋아합니다.

<br> <br>

## `XML 사용하는 방법`

XML을 사용한다는 것도 그렇게 좋진 않지만, MyBatis를 사용한다면 이 방법이 최선의 방법이라 개인적으로 생각합니다.

![xml](https://user-images.githubusercontent.com/45676906/119589110-1caec480-be0d-11eb-8f11-ab8d3dd589ab.png)

이번에도 똑같이 인터페이스에 `Mapper` 어노테이션을 추가해주고 메소드를 작성하겠습니다. 여기서 작성한 메소드와 XML에 작성한 쿼리를 1:1로 매칭시켜서 해당 메소드가 실행될 때 연결 되어 있는 쿼리가 실행되도록 하는 것인데요. 바로 XML을 작성해보겠습니다.

<br>

![mybatis](https://user-images.githubusercontent.com/45676906/119589237-5b447f00-be0d-11eb-9315-4d554755934d.png)

XML 파일은 위의 보이는 것처럼 `resources` 디렉토리 아래다 작성해야 합니다.

<br>

```yml
mybatis:
  mapper-locations: /mapper/**/*.xml
```

이 때 `application.yml` 파일에 위와 같이 작성해주면 좀 더 편리한데요. 위 설정의 의미는  **는 하위 폴더 레벨에 상관없이 모든 경로를 뜻하며, *.XML은 어떠한 이름도 가능하다는 뜻입니다. 예를들면 mapper/me/gyun/Member.XML도 가능하고, mapper/Member.XML, mapper/a/b/Member.XML도 가능하다는 뜻입니다.

<br>

![xml](https://user-images.githubusercontent.com/45676906/119589548-fccbd080-be0d-11eb-94de-c4bf288f5441.png)

그리고 위와 같이 XML을 작성하면 Intellij를 쓰면 자동완성도 지원해줍니다. 즉, 이름에서 알 수 있듯이, `select, insert, update, delete` 모두 지원합니다. 여기서 id 라는 값을 HelloMapper 인터페이스에 존재하는 메소드와 1:1 매칭을 시켜주면 됩니다.

<br>

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">

<mapper namespace="com.example.demo.mapper.HelloMapper">
    <select id="getGroup">
        SELECT *
        FROM user
    </select>

    <insert id="insertGroup">
        INSERT INTO ~
    </insert>

    <update id="update">
        UPDATE ~~
    </update>

    <delete id="delete">
        DELETE ~~
    </delete>
</mapper>
```

전체 코드는 위와 같습니다. 현재 글에서 설명한 것외에도 `resultMap`, `resultSet`, `동적 쿼리` 등등 MyBatis에서 지원해주는 편리한 것들이 많습니다. 좀 더 궁금하다면 [MyBatis 공식 문서](https://mybatis.org/mybatis-3/ko/sqlmap-xml.html) 에서 참고하면 좋을 거 같습니다.