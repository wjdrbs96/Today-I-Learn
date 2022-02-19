## `OneToOne 관계에서 N + 1 문제 정리하기`

이번 글에서는 `@OneToOne` 관계에서 발생할 수 있는 `N + 1` 문제에 대해서 정리해보겠습니다. 

<img width="555" alt="스크린샷 2022-02-13 오전 11 45 51" src="https://user-images.githubusercontent.com/45676906/153736038-3f449a03-2349-4f44-b5d2-77f0facc24cf.png">

`File - Thumbnail` 이미지의 관계를 `OneToOne` 관계의 예시로 말해보겠습니다. `File`의 썸네일 이미지가 존재할 수 있는 상황입니다. 즉, `File`이 생성된 후에 `Thumbnail Image`가 생성될 수 있습니다. 

이 때 외래키를 누가 가지냐도 중요한 문제가 될 수 있는데요. 먼저 `File` 테이블이 외래키를 가질 때 어떤 흐름으로 진행되는지 알아보겠습니다. 

<br>

### `File 테이블이 외래키를 가질 때`

1. `File Table`에서 `Thumbnail_id` 라는 외래키를 가진다. 
2. `Thumbnail_id`는 `nullable`한 컬럼이어야 한다.
3. `Thumbnail` 엔티티를 저장하기 위해서는 2번의 쿼리를 실행해야 한다.(`Thumbnail 저장 쿼리`, `File 테이블에 외래키 업데이트 쿼리`)

<br>

저는 여기서 3번의 경우가 좋은 경우는 아니라고 생각해서 외래키를 가지고 있는 테이블을 `Thumbnail_Image`로 정했습니다.

<br>

## `File Entity`

```java
@Entity
public class File {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String filename;

    private String fileSize;

    @OneToOne(mappedBy = "file")
    private ThumbnailImage thumbnailImage;

}
```

위에서 말한 것처럼 `File` 테이블에서 외래키를 가지지 않는다면 `File` 엔티티는 연관관계의 주인이 아니기 때문에 `mappedBy` 속성을 사용한 것을 볼 수 있습니다. 

<br>

## `Thumbnail Image Entity`

```java
@Entity
public class ThumbnailImage {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String thumbnailImageName;

    private String thumbnailImageSize;

    @OneToOne(fetch = FetchType.LAZY)
    private File file;

}
```

`ThumbnailImage`는 외래키를 가지고 있는 테이블이기 때문에 객체의 관점에서도 `연관관계의 주인`이 됩니다.

비즈니스에 따라 다르겠지만 일반적으로는 `File을 통해서 Thumbnail`을 조회하는 경우가 존재하지, `Thumbnail을 통해서 File`을 조회하는 경우는 거의 없을 것입니다. 

그렇기에 `File -> Thumbnail`을 참조하는 `양방향` 매핑이 필요했던 것입니다. 그런데 이제부터 이렇게 `@OneToOne` 관계에서 발생할 수 있는 문제점에 대해서 알아보겠습니다.

<br>

## `File만 조회해보자.`

현재 `File 테이블에는 5개의 row 데이터가 존재하고 Thumbnail 테이블에서 1개 이상의 데이터`가 존재하는 상황입니다. 

```java
@RequiredArgsConstructor
@Service
public class FileService {

    private final FileRepository fileRepository;

    public List<FileResponseDto> getFiles() {
        return fileRepository.findAll().stream()
                .map(FileResponseDto::from)
                .collect(Collectors.toList());
    }
}
```

이 때 위와 같이 `File`을 전체 조회하는 `findAll()`을 통해서 조회하고 있습니다. 처음에 생각했을 때는 `File 엔티티`를 조회하는 쿼리 한번만 실행될 것이라 예측했습니다. 

```sql
Hibernate: 
    select
        file0_.id as id1_0_,
        file0_.file_size as file_siz2_0_,
        file0_.filename as filename3_0_ 
    from
        file file0_
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
    select
        thumbnaili0_.id as id1_1_0_,
        thumbnaili0_.file_id as file_id4_1_0_,
        thumbnaili0_.thumbnail_image_name as thumbnai2_1_0_,
        thumbnaili0_.thumbnail_image_size as thumbnai3_1_0_ 
    from
        thumbnail_image thumbnaili0_ 
    where
        thumbnaili0_.file_id=?

```

그런데 실행되는 쿼리를 보면 위와 같이 `총 6번`의 쿼리가 실행된 것을 볼 수 있습니다. 이처럼 `@OneToOne 관계에서 연관관계의 주인이 아닌 곳에서 조회하게 되면 N + 1`이 발생하는 문제점이 존재합니다.

즉, 프록시를 사용할 때 `외래 키를 직접 관리하지 않은 일대일 관계는 지연로딩으로 설정해도 즉시 로딩으로 처리됩니다.` `File -> Thumbnail`을 조회하면 `지연로딩으로 설정해도 즉시로딩`으로 실행됩니다.

위와 같이 `N + 1`이 발생하는 이유는 JPA의 구현체인 Hibernate 에서 프록시 기능의 한계로 지연 로딩을 지원하지 못하기 때문에 발생한다고 합니다.(`Reference: JPA ORM 프로그래밍`)

(왜 그런지 좀 더 찾아보기..)

`N + 1` 문제를 해결하려면 `fetct join`, `entity graph`를 사용하면 됩니다. 하지만 나는 `File 엔티티`만 조회하고 싶은데 썸네일 이미지까지 같이 조회하는 것이 썩 좋지만은 않은데요. 저는 외래키를 썸네일 이미지가 가지도록 했지만, `File Entity`를 조회하는 일이 많기에 File 엔티티에서 외래키를 가지는 것이 더 적절한 것인가 라는 생각도 드는 것 같습니다. 

<br>

## `Reference`

- [김영한 JPA ORM 프로그래밍]()