## `하나의 트랜잭션에서 Update를 여러 번 한다면 몇 번의 쿼리가 실행될까?`

이번 글에서는 `하나의 트랜잭션`에서 여러 번의 `Update` 쿼리를 실행하면 몇 번의 쿼리가 실행되는지 알아보겠습니다. 

`JPA 영속성 컨텍스트`의 대표적인 특징 중에는 `쓰기 지연`, `변경 감지`가 있습니다. 

- `쓰기 지연`: 엔티티 매니져가 트랜잭션이 커밋할 때까지 내부 쿼리 저장소에 `SQL`을 모았다가 한번에 데이터베이스에 보내는 기능을 말합니다.
- `변경 감지`: JPA 영속성 컨텍스트에 스냅샷으로 저장되어 있는 엔티티의 상태와 현재의 엔티티 상태를 비교해서 변경된 부분이 있다면 `Update` 쿼리를 실행시켜 주는 기능을 말합니다. 

<br>

### `하나의 트랜잭션에서 여러 번의 변경 감지를 진행해보기`

```java
@RequiredArgsConstructor
@Service
public class FileService {

    private final FileRepository fileRepository;

    @Transactional
    public void update() {
        var file = fileRepository.findById(1L).orElseThrow(IllegalArgumentException::new);
        for (int i = 0; i < 100; ++i) {
            file.update("changeName" + i, String.valueOf(2000 + i));
        }
    }
}
```

위와 같이 `하나의 트랜잭션`에서 여러 번의 변경 감지를 하면 몇 번의 쿼리가 실행될까요? 

```sql
Hibernate:
select
    file0_.id as id1_0_0_,
    file0_.file_size as file_siz2_0_0_,
    file0_.filename as filename3_0_0_
from
    file file0_
where
    file0_.id=?
    Hibernate:
select
    thumbnaili0_.id as id1_1_0_,
    thumbnaili0_.file_id as file_id4_1_0_,
    thumbnaili0_.thumbnail_image_name as thumbnai2_1_0_,
    thumbnaili0_.thumbnail_image_size as thumbnai3_1_0_
from
    thumbnail_image thumbnaili0_
where
    thumbnaili0_.file_id=?
    Hibernate:
update
    file
set
    file_size=?,
    filename=?
where
    id=?
```

실행된 쿼리를 보면 `Update` 쿼리가 한번만 실행된 것을 볼 수 있습니다. 즉, `영속성 컨텍스트`의 특징처럼 하나의 트랜잭션 내에서는 `쓰기 지연`을 통해서 `변경 감지`가 한번만 발생한 것을 볼 수 있습니다. 

좀 더 말하자면, `@Transactional` 어노테이션이 해당 메소드 작업이 끝날 때 `트랜잭션 commit`을 진행하고 `commit`이 될 때 `flush()` 메소드가 동작해서 `SQL 저장소에 있던 쿼리들`이 `데이터베이스`에 반영됩니다. 

즉, `마지막 i = 99 이면서 100번째`에 해당하는 것만 `Update` 쿼리가 발생하게 됩니다. 

<img width="207" alt="스크린샷 2022-02-19 오후 3 40 51" src="https://user-images.githubusercontent.com/45676906/154789887-bc9bcdf4-8d43-4392-b851-0616f0d22ada.png">

위처럼 `테이블의 id 1번`을 보면 `i = 99`일 때의 값으로 변경된 것을 볼 수 있습니다. 

<br>

## `Reference`

- [김영한 JPA ORM 프로그래밍]()