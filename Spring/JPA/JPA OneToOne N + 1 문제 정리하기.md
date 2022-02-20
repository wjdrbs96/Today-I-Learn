## `@OneToOne 관계에서 발생하는 N + 1 문제 정리하기`

이번 글에서는 `@OneToOne` 관계에서 발생할 수 있는 `N + 1` 문제에 대해서 정리해보겠습니다. `File - Thumbnail_Image` 관계를 예시로 들어 정리해보겠습니다. 

하나의 `File`에는 `Thumbnail_Image` 하나만 존재할 수 있는 상황입니다. 그리고 `File`이 생성된 후에 `Thumbnail Image`가 생성될 수 있습니다.

위와 같은 조건이 있기 때문에 `@OneToOne` 관계인데요. `@OneToOne` 관계는 `외래키를 가지는 테이블`을 정해야 합니다.

1. `File 테이블이 thumbnail_id 외래키를 가지는 경우`
2. `Thumbnail Image 테이블이 file_id 외래키를 가지는 경우`

<br>

두 가지 경우가 있는데요. 먼저 첫 번째 경우인 `File 테이블이 외래키를 가질 때` 어떻게 되는지 알아보겠습니다.

<br> <br>

## `File 테이블에서 thumbnail_id 외래키를 가지고 있을 때`

![스크린샷 2022-02-19 오후 9 35 34](https://user-images.githubusercontent.com/45676906/154800995-063dc093-1845-486a-96de-58f077c14725.png)

1. `File Table`에서 `thumbnail_id` 라는 외래키를 가진다.
2. `thumbnail_id`는 `nullable`한 컬럼이어야 한다.
3. `Thumbnail_Image Entity`를 저장하기 위해서는 2번의 쿼리를 실행해야 한다.(`Thumbnail 저장 쿼리`, `File 테이블에 thumbnail_id 외래키 업데이트 쿼리`)

<br>

여기서 `3번`의 경우가 어떤 경우인지 코드의 예시를 보겠습니다. 

```java
@RequiredArgsConstructor
@Service
public class ThumbnailImageService {

    private final FileRepository fileRepository;
    private final ThumbnailRepository thumbnailRepository;

    @Transactional
    public void saveThumbnail(final Long fileId, final ThumbnailImageRequestDto thumbnailImageRequestDto) {
        var file = fileRepository.findById(fileId).orElseThrow(EntityNotFoundException::new);
        file.setThumbnailImage(thumbnailImageRequestDto.toEntity());
    }
}
```

```sql
Hibernate: 
    select
        file0_.id as id1_0_0_,
        file0_.file_size as file_siz2_0_0_,
        file0_.filename as filename3_0_0_,
        file0_.thumbnail_image_id as thumbnai4_0_0_ 
    from
        file file0_ 
    where
        file0_.id=?
Hibernate: 
    insert 
    into
        thumbnail_image
        (thumbnail_image_name, thumbnail_image_size) 
    values
        (?, ?)
Hibernate: 
    update
        file 
    set
        file_size=?,
        filename=?,
        thumbnail_image_id=? 
    where
        id=?
```

위의 `saveThumbnail` 메소드를 보면 `Thumbnail Image`를 먼저 저장을 합니다. 그리고 저장한 후에 나온 `thumbnail_id` 값을 `File 테이블에 존재하는 thumbnail_id`에 업데이트 쿼리를 한번 더 실행을 해주어야 합니다. `File과 Thumbnail Image를 연결하는 작업`이 필요합니다.

즉, `Write` 작업이 2번 필요한데요. 이 때 저는 `Write` 작업이 2번 일어나서 성능이 걱정된다기 보다는 `Thumbnail Image`를 저장하는데 `2번의 Write 작업`이 필요하다는 것이 깔끔하지 않다고 생각했습니다.

그래서 `File 테이블에서 thumbnail_id` 외래키를 가지는 것이 아니라 `thumbnail_image 테이블에서 file_id`를 가지면 한번의 `Write` 작업으로 해결할 수 있기 때문에, `File 테이블에서 외래키`를 가지는 방법보다는 `Thumbnail Image 테이블에서 file_id 외래키`를 가지는 것이 더 좋은 방법이라고 생각했습니다. 

<br> <br>

## `Thumbnail Image 테이블에서 file_id 외래키를 가지고 있을 때`

<img width="555" alt="스크린샷 2022-02-13 오전 11 45 51" src="https://user-images.githubusercontent.com/45676906/153736038-3f449a03-2349-4f44-b5d2-77f0facc24cf.png">

위와 같은 테이블 구조에서는 `Thumbnail Image`를 저장할 때 1번의 `Write` 작업으로 가능하다는 장점이 있습니다. 그런데 위와 같이 설계하면 `JPA`를 사용했을 때 예상치 못했던 이슈가 발생했는데요. 이슈의 내용은 아래와 같습니다.

<br> <br>

## `File Entity를 조회할 때`

현재 `File 테이블에는 5개의 데이터가 존재하고 Thumbnail 테이블에서 1개 이상의 데이터`가 존재하는 상황입니다.

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

이 때 위와 같이 `File`을 전체 조회하는 `findAll()`을 통해서 조회하고 있습니다.(File 5개를 조회하는 것입니다.) 이 때 저는 `File Entity`를 조회하는 쿼리 한번만 실행될 것이라 예측했습니다.

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

하지만 실행된 쿼리를 보았을 때는 예상과 달랐습니다. `File` 테이블 조회했을 때의 결과 `row`의 수 만큼 `Thumbnail Image Entity`를 조회하는 쿼리가 실행된 것을 볼 수 있습니다. 즉, 위에서 5개의 `File` 데이터가 있다고 했으니 `Thumbnail Image`를 조회하는 쿼리 5번이 더 실행되어 `총 6번의 쿼리가 실행`된 것을 볼 수 있습니다.

저는 여기서 `왜 이러한 현상이 발생하는 것인지?`가 궁금했습니다. 이러한 현상이 발생하는 이유를 알아보기 위해서 이번에는 `Entity` 관점에서 알아보겠습니다. 

<br> <br>

## `File Entity`

```java
@Entity
public class File {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String filename;

    private String fileSize;

    @OneToOne(mappedBy = "file", fetch = FetchType.LAZY)
    private ThumbnailImage thumbnailImage;
}
```

현재 `File` 테이블에서 외래키를 가지기 때문에 `File Entity`는 `mappedBy` 속성을 가진 연관관계의 주인이 아닌 것을 알 수 있습니다. 그리고 `지연로딩(LAZY)`이 적용되어 있습니다.

테이블 관점에서 보면 `File 테이블`(외래키가 없는 테이블)에서 `Thumbnail Image`(외래키가 있는 테이블)을 조회할 수 있다는 특징이 있습니다. 

하지만 객체 관점에서는 `File -> Thumbnail`을 참조하기 위해서는 `File -> Thumbnail Image`를 참조하는 관계가 필요합니다. 즉, `양방향` 매핑이 필요한데요. 그런데 이렇게 `@OneToOne` 관계에서 양방향 매핑이 되어 `연관관계 주인`이 아닌 곳에서 조회했을 때 발생하는 문제가 있습니다. 그 부분을 아래에서 자세히 알아보겠습니다.

양방향 매핑을 한 이유는 비즈니스에 따라 다를 수 있겠지만 일반적으로는 `File을 통해서 Thumbnail Image`를 조회하는 경우가 대부분이고, `Thumbnail Image를 통해서 File`을 조회하는 경우는 거의 없을 것이기 때문입니다.

<br> <br>

## `Thumbnail Image Entity`

```java
@Entity
public class ThumbnailImage {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String thumbnailImageName;

    private String thumbnailImageSize;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private File file;

}
```

`ThumbnailImage Entity`는 외래키를 가지고 있는 테이블과 연결되어 있는 엔티티이기 때문에 객체의 관점에서도 `연관관계의 주인`이 됩니다.

<br> <br>

## `@OneToOne 관계에서는 연관관계 주인이 아닌 곳에서 조회할 때 N + 1 문제가 발생한다.`

`저는 N + 1 문제를 어떻게 해결해야 하는지? 보다는 왜 연관관계 주인이 아닌 곳에서 조회할 때 조회하지 않은 엔티티가 조회되어 N + 1 문제가 발생하는지?`가 궁금했습니다. 

<br>

### `연관관계 주인이 아닌 File Entity`

```java
@Entity
public class File {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String filename;

    private String fileSize;

    @OneToOne(mappedBy = "file", fetch = FetchType.LAZY)
    private ThumbnailImage thumbnailImage;
}
```

<img width="555" alt="스크린샷 2022-02-13 오전 11 45 51" src="https://user-images.githubusercontent.com/45676906/153736038-3f449a03-2349-4f44-b5d2-77f0facc24cf.png">

`File 테이블에는 thumbnail_id 라는 외래키가 없기 때문에 File Entity` 입장에서는 `File에 연결되어 있는 Thumbnail Image`가 `null` 인지 아닌지를 조회해보기 전까지는 알 수 없습니다. 

그리고 `LAZY` 로딩이어서 `프록시 객체`를 사용할 것처럼 보이지만, 실제로는 `Proxy` 객체를 사용하지 않고 있습니다. 그 이유는 `Proxy 객체를 만들기 위해서는 Thumbnail Image 객체가 null인지 값이 있는지를 알아야 하는데, File Entity 객체 관점으로는 알 수 없기 때문입니다.`

그래서 `Thumbnail Image`를 조회하는 쿼리들이 실행되는 것입니다. 이렇게 쿼리들을 실제로 조회를 하면 `영속성 컨텍스트에 엔티티들이 올라오기 때문에` 프록시 객체를 사용할 이유가 없어져서 `LAZY` 로딩으로 설정하여도 `즉시 로딩`처럼 동작하는 것입니다.

> 지연 로딩을 설정하여도 `즉시 로딩`으로 동작하는 이유는 `JPA의 구현체인 Hibernate 에서 프록시 기능의 한계로 지연 로딩을 지원하지 못하기 때문`에 발생한다. `bytecode instrumentation`을 사용하면 해결할 수 있다.
> <br> <br> 
> Reference: JPA ORM 프로그래밍

<br>

좀 더 자세한 설명은 [여기](https://stackoverflow.com/questions/1444227/how-can-i-make-a-jpa-onetoone-relation-lazy) 에서도 확인할 수 있는데요.

>  The reason for this is that owner entity MUST know whether association property should contain a proxy object or NULL and it can't determine that by looking at its base table's columns due to one-to-one normally being mapped via shared PK, so it has to be eagerly fetched anyway making proxy pointless.

위의 링크를 보면 위와 같이 설명하고 있습니다. 즉, 연관관계 주인이 아닌 테이블에서는 프록시로 만들 객체가 `null` 인지 아닌지 알 수 없기 때문에 조회하는 쿼리가 실행되는 것입니다.

<br> <br>

## `@OneToMany 에서 Lazy Loading이 적용되는 이유가 무엇일까?`

이제 `@OneToOne` 관계에서 연관관계 주인이 아닌 쪽에서 조회를 하면 `참조하고 있는 객체가 null 인지 아닌지 알 수 없기 때문에 프록시를 사용할 수 없기 때문에 N + 1 문제`가 발생하는 것은 이해했는데요.   

그러면 `@OneToMany` 관계에서도 `연관관계 주인`이 아니기 때문에 똑같이 `Proxy`가 적용되지 않아야 맞는거 아닐까? 라는 생각을 했습니다. 

```java
@Entity
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nickname;

    private String part;

    @OneToMany(mappedBy = "user")
    private List<Post> posts = new ArrayList<>();

}
```

`@OneToMany` 경우라면 위와 같이 `List` 형태로 참조하고 있을 것인데요. `@OneToMany`는 `OneToOne`과 다르개 `Lazy Loading`이 적용이 됩니다. 적용이 되는 이유는 무엇일까요? 

위에서 말했던 [링크](https://stackoverflow.com/questions/1444227/how-can-i-make-a-jpa-onetoone-relation-lazy) 에서 답이 나와 있는데요. 

> many-to-one associations (and one-to-many, obviously) do not suffer from this issue. Owner entity can easily check its own FK (and in case of one-to-many, empty collection proxy is created initially and populated on demand), so the association can be lazy.

요약하자면, `@OneToMany 관계는 빈 컬렉션이 초기화될 때(new ArrayList<>() 할 때) Proxy가 생긴다.` 입니다. 다시 말하면 `posts` 자체는 `null`이 아니고 `size 자체가 0`일 수 있는 것이기 때문에 `@OneToMany` 관계는 `@OneToOne`과 다르게 `Lazy Loading`이 가능했던 것입니다.

그러면 이번에는 다시 `@OneToOne` 관계로 돌아와서 `연관관계 주인인 Thumbnail Image Entity`에서 조회를 해보겠습니다.

<br> <br>

## `Thumbnail Image에서는 지연로딩이 적용이 될까?`

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

`Thumbnail Image Entity`를 보면 `연관 관계의 주인`입니다. 즉, `Thumbnail Image` 테이블에서 `file_id 외래키`를 가지고 있기 때문에 `Thumbnail Image` 객체 입장에서 굳이 `File Entity`를 조회해보지 않아도 `File Entity`가 존재하는지 안하는지를 알 수 있습니다. 

그렇기에 프록시 객체도 만들 수 있어서 `Thumbnail Image`를 통해서 `File`을 조회했을 때 `지연로딩`이 적용될 수 있는 것입니다.

<br> <br>

## `N + 1 문제를 해결해보기`

`N + 1` 문제를 해결하려면 `fetct join`, `entity graph`, `batch size` 같은 것들을 사용하면 됩니다. 이 중에서 `fetch join`과 `Batch Size`로 해결할 수 있는지 알아보겠습니다.

<br> <br>

## `Batch Size는 @OneToOne의 N + 1 문제를 해결할 수 있을까?`

```yaml
spring:
  jpa:
    properties:
      hibernate.default_batch_fetch_size: 1000
```

`N + 1`을 해결하는 대표적인 방법 중에 하나가 `Batch Size` 입니다. 사용 방법은 위와 같이 `application.yml`에 `batch size` 설정을 주는 것입니다. `Batch Size`는 `N + 1` 쿼리 처럼 쿼리를 나눠서 실행하지 않고 `IN` 절을 통해서 쿼리를 실행하는 것입니다.

그래서 저는 위의 설정을 한 후에 `File`을 조회하면 `N + 1` 문제가 발생하지 않고 `IN` 절을 통해서 한번의 쿼리가 실행될 것이라고 예상했습니다. 하지만 여전히 `N + 1` 문제가 발생했습니다. 즉, 결과가 달라지지 않았는데요. `@OneToOne` 문제에서 발생하는 `N + 1` 문제는 `Batch Size`로는 해결할 수 없었습니다.

<br> <br>

## `fetch join을 사용해서 N + 1 문제를 해결해보기`

그래서 이번에는 `N + 1` 문제를 해결하는 가장 대표적인 `fetch join`을 사용해보겠습니다.

```sql
SELECT f FROM File f join fetch f.thumbnailImage
```

위와 같은 `JPQL`을 사용하여 `fetch join`을 사용했을 때 어떤 쿼리들이 실행되는지 알아보겠습니다. 

```sql
Hibernate: 
    select
        file0_.id as id1_0_0_,
        thumbnaili1_.id as id1_2_1_,
        file0_.file_size as file_siz2_0_0_,
        file0_.filename as filename3_0_0_,
        thumbnaili1_.file_id as file_id4_2_1_,
        thumbnaili1_.thumbnail_image_name as thumbnai2_2_1_,
        thumbnaili1_.thumbnail_image_size as thumbnai3_2_1_ 
    from
        file file0_ 
    inner join
        thumbnail_image thumbnaili1_ 
            on file0_.id=thumbnaili1_.file_id
```

이번에는 `N + 1` 쿼리가 발생하지 않고 `JOIN`을 통해서 1번의 쿼리로 조회할 수 있습니다. 하지만 `N + 1 문제를 해결하는 fetch join`을 사용하면 저는 `File Entity`만 조회하고 싶은데 `Thumbnail Image Entity` 까지 같이 조회하게 되어 이것도 마냥 해법은 아니라는 생각도 들었습니다.

<br> <br>

## `정리하기`

저는 `DB 테이블` 관점에서 `Entity` 설계를 해야 좀 더 적절하다고 생각이 드는데요. `File 테이블`에서 `thumbnail_id` 외래키를 가지면 `File 테이블`에서 `nullable` 컬럼을 가져야 하는 것이 좋지는 않다고 생각했습니다. `nullable 컬럼`을 가진다면 비즈니스 로직에서 검증 로직이 추가될 가능성이 있기에 좋지 않다고 생각했습니다.

뿐만 아니라 확장성을 고려했을 때 `File`이 여러 개의 `Thumbnail Image`를 가질 수 있게 된다고 고려했을 때를 생각했을 때도 `Thumbnail Image`에서 `file_id`로 가지는 것이 더 적절하다고 생각했습니다.

하지만 `상황에 따라 다를 수 있다` 라고 생각합니다. 만약에 `File`만 조회하는 경우는 거의 없고, `File, Thumbnail Image`가 같이 필요할 때가 많다면 `fetch join`으로 사용해서 계속 `File-Thumbnail Image`를 같이 조회해도 엄청 큰 부하는 준다고 생각하지 않기 때문입니다.

그런데 만약 대부분의 곳에서 `File` 조회만 필요하고 일부분에서 `File-Thumbnail`을 같이 조회하는 것이 필요하고, `JOIN`을 하는 것 자체가 부담이라면 처음 말했던 방식처럼 `File Entity에서 thumbnail_id` 외래키를 가지도록 설계하여 `Thumbnail Image Entity`를 저장할 때 `Write` 작업을 2번하도록 하는 것도 하나의 방법이라 생각합니다.

<br> <br>

## `Reference`

- [자바 ORM 표준 JPA 프로그래밍]()
- [https://stackoverflow.com/questions/1444227/how-can-i-make-a-jpa-onetoone-relation-lazy](https://stackoverflow.com/questions/1444227/how-can-i-make-a-jpa-onetoone-relation-lazy)