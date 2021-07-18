# `MyBatis ResultMap 에러 정리`

이번 글에서는 프로젝트를 하면서 만났던 MyBatis 에러에 대해서 간단하게 정리를 해보려 합니다. 

![스크린샷 2021-07-19 오전 12 24 01](https://user-images.githubusercontent.com/45676906/126072846-71432d7b-815d-4d19-9748-0dee9c9d323f.png)

현재 `UserLeaderScore` 모델과 `User` 모델을 `ResultMap`을 사용해서 JOIN 해서 데이터를 가져오려는 상황입니다. 

<br>

![스크린샷 2021-07-19 오전 12 33 59](https://user-images.githubusercontent.com/45676906/126073155-bc996705-f13a-4b8f-9e74-f32b4e849c52.png)

DB 테이블은 위와 같습니다. 이 테이블과 매칭이 되는 자바 클래스 모델은 아래와 같습니다. 

<br>

## `User Model`

```java
@NoArgsConstructor
@Getter
public class User {

    private Long id;
    private String nickname;
    private String gender;
    private int birth;
    private String socialType;
    private String signupCode;
    private int alarm;
    private double leaderScore;
    private String profileImage;
    
}
```

<br> 

## `UserLeaderScore Model`

```java
//@NoArgsConstructor
@Getter
public class UserLeaderScore {

    private Long id;
    private User user;
    private DiscussionGroup group;
    private int leaderScore;

    public UserLeaderScore(User user, DiscussionGroup group, int leaderScore) {
        this.user = user;
        this.group = group;
        this.leaderScore = leaderScore;
    }

}
```

두 모델과 MyBatis의 ResultMap을 사용해서 JOIN을 하면 아래와 같이 할 수 있습니다. 

<br>

```xml
 <resultMap type="UserLeaderScore" id="getMyProfileByBookCase">
    <result column="leader_score" property="leaderScore"/>
    <association column="leader_id" property="user" javaType="User">
        <result column="nickname" property="nickname"/>
        <result column="profile_url" property="profileImage"/>
    </association>
</resultMap>
```

위처럼 UserLeaderScore에서 leader_score를 가져오고, User에서 nickname, profile_url을 가져오려고 생각하고 실행을 해보았습니다. 

<br>

![스크린샷 2021-07-19 오전 12 41 54](https://user-images.githubusercontent.com/45676906/126073428-b5f3e0c3-bdce-449e-8d21-240f5c01c7c5.png)

```
org.mybatis.spring.MyBatisSystemException: nested exception is org.apache.ibatis.reflection.ReflectionException: Error instantiating class com.maru.model.UserLeaderScore with invalid types
```

위와 같이 어떤 `reflection` 관련해서 에러가 발생한 것을 볼 수 있습니다. 처음에는 `ResultType`에도 문제가 없는데 왜 에러가 날까 생각을 했습니다.

대략 찾아보고 유추해보니 MyBatis의 결과가 객체가 returnType 이라면 MyBatis 내부적으로 해당 객체의 기본 생성자를 만든 후에 필드에 내부적인 직렬화 과정을 거쳐서 DB 컬럼의 값을 클래스 필드에 넣는 식으로 진행되는 거 같습니다.
그래서 위의 ResultMap으로 만들었던 쿼리의 결과는 UserLeaderScore 모델 객체이기 때문에 UserLeaderScore에 `@NoArgsConstructor`인 기본 생성자가 존재하지 않아서 에러가 발생했던 것입니다.

즉, 결론은 MyBatis에서 resultType이 객체라면 해당 객체의 `기본 생성자`가 필요하다 입니다! 오늘의 에러 정리는 여기까지~ 
