## `QueryProjection 사용하기`

이번 글에서는 `QueryProjection`을 어노테이션으로 사용하는 방법에 대해서 아주 간단하게 공유 해보려 합니다.

`@QueryProjection` 어노테이션을 사용하려면 `QueryDSL` 설정이 되어 있어야 햐는데 설정이 되어 있다고 가정하고 진행하겠습니다.

<br>

### `QueryProjection 무엇일까?`

`JPA`를 쓰면 DB에서 데이터 조회하면 `Entity`가 가지는 모든 필드를 다 조회하게 됩니다. 즉, 필요없는 필드도 조회하게 될 수 있다는 특징이 있습니다. MyBatis를 사용하면 원하는 필드만 조회하여 성능을 좀 더 최적화할 수 있는 특징이 있는데요.

JPA에서도 필요한 필드만 조회하여 쿼리 성능 최적화를 할 수 있는데 이럴 때 사용하는 것이 `QueryProjection` 입니다.

<br>

### `DTO 생성하기`

```java
@Getter
public class QueryProjectionDTO {

    private final String name;
    private final Integer age;
    private final String address;
    private final String career;

    @QueryProjection
    public QueryProjectionDTO(String name, Integer age, String address, String career) {
        this.name = name;
        this.age = age;
        this.address = address;
        this.career = career;
    }
}
```

위와 같이 추출하고자 하는 필드만 선택해서 DTO 클래스를 생성한 후에 `@QueryProjection` 어노테이션을 사용하면 됩니다.

<br>

### `QueryDSL에서 QueryProjection 사용하기`

<img width="598" alt="스크린샷 2022-07-03 오후 9 39 09" src="https://user-images.githubusercontent.com/45676906/177040076-808606ef-3947-4a73-94fc-3a483ee7f3e5.png">

`compileQuerydsl` 버튼을 통해서 `QClass`를 생성하겠습니다.

<br>

![스크린샷 2022-07-03 오후 9 38 16](https://user-images.githubusercontent.com/45676906/177040047-057974b0-46da-44cb-ac88-835f02ea7f31.png)

그러면 위와 같이 `QQueryProjectDTO` 클래스가 생성이 됩니다.

```java
public class ProfileKeywordCustomRepositoryImpl extends QuerydslRepositorySupport implements ProfileKeywordCustomRepository {
    
    private final QProfile profile = QProfile.profile;

    private final JPAQueryFactory jpaQueryFactory;

    public ProfileKeywordCustomRepositoryImpl(JPAQueryFactory jpaQueryFactory) {
        super(ProfileKeyword.class);
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Override
    public List<QueryProjectionDTO> findByProfileByFetch(final Long userId, final Profile profile) {
        return jpaQueryFactory.select(
                new QQueryProjectionDTO(
                        QProfile.profile.name,
                        QProfile.profile.age,
                        QProfile.profile.address,
                        QProfile.profile.career
                )
        ).from(this.profile).fetch();
    }
}
```

그리고 위와 같이 `QQueryProjection`을 사용해서 조회하면 됩니다.

<br>

```sql
Hibernate: 
    select
        profile0_.name as col_0_0_,
        profile0_.age as col_1_0_,
        profile0_.address as col_2_0_,
        profile0_.career as col_3_0_ 
    from
        profile profile0_
```

그러면 추출하고자 했던 필드만 조회하는 쿼리가 실행되는 것을 볼 수 있습니다.