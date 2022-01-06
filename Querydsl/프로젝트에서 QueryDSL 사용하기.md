## `QueryDSL 사용하기`

이번 글에서는 프로젝트에서 `QueryDSL` 사용하는 법에 대해서 간단하게 정리해보겠습니다. [QueryDSL 설정](https://github.com/wjdrbs96/Today-I-Learn/blob/master/Querydsl/1.%20gradle%20Querydsl%20%EC%84%A4%EC%A0%95%ED%95%98%EB%8A%94%20%EB%B2%95.md) 에서 설정을 하고 왔다고 가정하겠습니다.

```java
@Entity
public class Board {
    
    private Long id;
    
    private String title;
    
    private String content;
    
}
```


위와 같이 `Board` 엔티티가 존재한다고 가정하겠습니다.

<br>

```java
public interface BoardRepository extends JpaRepository<Board, Long> {}
```

보통 `Entity` 하나당 `Repository`도 `1:1`로 만드는데요. 여기서 `QueryDSL` 설정을 해보겠습니다. 

<br>

```java
public interface BoardCustomRepository {
    List<Board> findByUserFetch(User user);
}
```

먼저 위와 같이 `BoardCustomRepository`와 같이 `Custom Repository`를 만듭니다. 그리고 `QueryDSL`로 사용할 `메소드`들을 위에다 적어줍니다. 

<br>

```java
public interface BoardRepository extends JpaRepository<Board, Long>, BoardCustomRepository {}
```

그리고 위와 같이 `BoardCustomRepository`도 `extends` 하도록 수정하겠습니다. 

<br>

```java
public class BoardCustomRepositoryImpl extends QuerydslRepositorySupport implements BoardCustomRepository {
    
    private final JPAQueryFactory jpaQueryFactory;
    private final QBoardEntity board = QBoardEntity.boardEntity;
    private final QUserEntity user = QUserEntity.userEntity;
    
    public BoardCustomRepositoryImpl(JPAQueryFactory jpaQueryFactory) {
        super(BoardEntity.class);
        this.jpaQueryFactory = jpaQueryFactory;
    }
    
    @Override
    public List<Board> findByUserFetch(User user) {
        return setFetchJoinQuery()
                .where(eqBoard(board()))
                .fetch();
    }
} 
```

먼저 `QueryDSL` 의존성을 추가했다면 자동으로 엔티티 이름 앞에 `Q`가 붙어있는 것들이 생성됩니다. (ex: `QBoard`) 그래서 위와 같이 필드에 넣어주면 됩니다. 그리고 여기서 `BoardCustomRepository` 정의한 메소드들을 오버라이딩 해서 `QueryDSL`로 작성해주면 됩니다.

위와 같이 작성해서 `QueryDSL` 사용할 수 있습니다.