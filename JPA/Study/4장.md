# `엔티티 매핑이란?`

JPA에서 가장 중요한 일은 `엔티티`와 `테이블`을 정확히 매핑하는 것입니다.  

- 객체와 테이블 매핑: `@Entity`, `@Table`
- 필드와 컬럼 매핑: `@Column`
- 기본 키 매핑: `@Id`
- 연관관계 매핑: `@ManyToOne`, `JoinColumn`

<br>

## `객체와 테이블 매핑`

### `Entity`

@Entity가 붙은 클래스는 JPA가 관리하는 클래스입니다. JPA를 사용해서 테이블과 매핑할 클래스는 `@Entity`는 필수입니다. 그리고 주의사항이 있는데 아래와 같습니다.

- 기본 생성자는 필수입니다. (파라미터가 없는 public 또는 protected 생성자)
- final 클래스, enum, interface, inner 클래스에는 사용할 수 없습니다.
- 데이터베이스에 저장할 필드에 final을 사용하면 안됩니다.

<br>

![스크린샷 2021-08-25 오후 3 01 32](https://user-images.githubusercontent.com/45676906/130734922-06cc9bad-7c0f-47e8-9853-ef62c0a090bc.png)

<br>

![스크린샷 2021-08-25 오후 3 02 16](https://user-images.githubusercontent.com/45676906/130735003-6d828530-d326-4acf-8246-ce07f516f74d.png)

간단한 코드를 실행해보면 위와 같이 `테이블 이름이 MBR`인 것을 볼 수 있습니다.

<br> <br>

## `필드와 컬럼 매핑`

간단하게 요구사항을 보면서 JPA에서는 `필드와 컬럼을 어떻게 매핑`하는지 간단하게 알아보겠습니다. 

- 회원은 일반 회원과 관리자로 구분해야 합니다.
- 회원 가입일과 수정일이 있어야 합니다.
- 회원을 설명할 수 있는 필드가 있어야 합니다. 이 필드는 길이 제한이 없습니다.

<br>

```java
@Entity
public class Member {

    @Id
    private Long id;

    @Column(name = "name") // DB에는 name 컬럼으로 사용하고 싶을 때
    private String username;

    private Integer age;

    @Enumerated(EnumType.STRING)  // Enum을 사용하고 싶을 때
    private RoleType roleType;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

    @Temporal(TemporalType.TIMESTAMP)
    private Date lastModifiedDate;

    @Lob  // VARCHAR를 넘어서는 큰 용량을 필요로 할 때
    private String description;
    
    // Getter, Setter 존재
}
```

위와 같이 엔티티와 테이블을 매핑한 후에 실행을 해보겠습니다. 

<br>

![스크린샷 2021-08-25 오후 3 13 17](https://user-images.githubusercontent.com/45676906/130736311-d50d03a5-2665-4fe1-bc78-1e85aec15e5e.png)

그러면 위와 같이 JPA에서 테이블을 만들어주는 것을 볼 수 있습니다. `@Enumerated`는 `VARCHAR`로 생성된 것을 볼 수 있고, `@Lob`은 clob이라는 타입으로 생성된 것을 볼 수 있습니다. 

<br>

<img width="1105" alt="스크린샷 2021-08-25 오후 3 16 43" src="https://user-images.githubusercontent.com/45676906/130736563-42e335d0-afe1-411e-bf60-8e8a3cee0e6b.png">

<br> <br>

## `@Column`

<img width="1408" alt="스크린샷 2021-08-25 오후 3 18 12" src="https://user-images.githubusercontent.com/45676906/130736700-5aa138ea-2d9e-4f29-b042-e0fd7890549a.png">

![스크린샷 2021-08-25 오후 3 18 44](https://user-images.githubusercontent.com/45676906/130736823-b1cbaa95-fb93-43f0-9f78-5d39d19c0e3d.png)

첫 번째로 컬럼에 위와 같이 `nullable = true or false` 값을 줄 수 있는데 이름에서 유추할 수 있듯이 false를 주면 NULL이 불가능하다는 뜻입니다. 이 상태로 실행하면 아래와 같은 DDL 문을 JPA에서 만들어줍니다. 

<br>

![스크린샷 2021-08-25 오후 3 20 04](https://user-images.githubusercontent.com/45676906/130736993-5c0eab99-cf09-4108-b08d-dcf12e178380.png)

위와 같이 `not null`인 상태의 필드로 JPA가 생성해줍니다. 

<br>

![스크린샷 2021-08-25 오후 3 22 19](https://user-images.githubusercontent.com/45676906/130737242-21e2296f-0c8d-422a-95d9-190ceef702d1.png)

위와 같이 컬럼의 length도 줄 수 있습니다. length=30 으로 하고 실행하면 아래와 같이 VARCHAR(30)인 필드를 만들어줍니다. 

![스크린샷 2021-08-25 오후 3 23 32](https://user-images.githubusercontent.com/45676906/130737380-e2fa30fe-3684-403e-b719-652cc37f3d36.png)

<br> <br>

## `@Enumerated`

![스크린샷 2021-08-25 오후 3 25 34](https://user-images.githubusercontent.com/45676906/130737636-48fa3bf9-a000-4c11-b8f9-ad8f0d567da1.png)

Enumerated 어노테이션을 보면 위와 같이 `default가 ORDINAL`인 것을 볼 수 있습니다. (하지만 ORDINAL 사용하지 말기!!)

- EnumType.ORDINAL: enum 순서를 데이터베이스에 저장합니다.
- EnumType.STRING: enum 이름을 데이터베이스에 저장합니다. 

<br>

![스크린샷 2021-08-25 오후 3 29 11](https://user-images.githubusercontent.com/45676906/130738054-5815f79d-bc95-4e44-838c-6165eba4d1cf.png)

만약에 위와 같이 `Enumerated`를 default ORDINAL로 놓고 저장했을 때 어떻게 저장되는지 확인해보겠습니다. 

<br>

![스크린샷 2021-08-25 오후 3 28 15](https://user-images.githubusercontent.com/45676906/130738001-382d9a88-8395-431d-ab32-43ddad76d91f.png)

<br>

![스크린샷 2021-08-25 오후 3 30 48](https://user-images.githubusercontent.com/45676906/130738270-4ef141f9-036b-45dd-85c0-4accb72348e4.png)

그러면 JPA에서는 순서를 기반으로 저장하기 때문에 RoleType을 INTERGER로 생성한 것을 볼 수 있습니다.

<br>

![스크린샷 2021-08-25 오후 3 32 31](https://user-images.githubusercontent.com/45676906/130738416-ed4cb162-1378-4bb2-b04d-0b618411b315.png)

그리고 데이터베이스에도 확인을 해보면 순서가 디비에 저장이 된 것도 확인할 수 있습니다. 

<br>

### `그러면 왜 ORDINAL 타입을 사용하지 말라고 할까요?`

<img width="545" alt="스크린샷 2021-08-25 오후 3 34 30" src="https://user-images.githubusercontent.com/45676906/130738621-3fda649a-d2d5-4f10-906a-980916505c41.png">

위와 같이 예시는 2개의 데이터만 저장이 되있지만 만약에 한 100개의 데이터가 저장되어 있다고 생각해보겠습니다. 

<br>

![스크린샷 2021-08-25 오후 3 35 47](https://user-images.githubusercontent.com/45676906/130738781-7f40ddc4-d314-49a9-ae2c-c45356fb0e01.png)

이런 상태에서 요구사항이 바뀌어 RolyType에 GEUST가 추가되었다면 어떻게 될까요?

<br>

![스크린샷 2021-08-25 오후 3 36 57](https://user-images.githubusercontent.com/45676906/130738921-3bc5c4e3-8d7a-4c1c-9dd5-0d28a41e8754.png)

그리고 위와 같이 `GUEST`를 저장하게 되면 아래와 같이 RoleType이 섞여버려 0이 무엇인지 구분하기 힘들어지는 상황이 되어버리는데요.

<br>

<img width="528" alt="스크린샷 2021-08-25 오후 3 38 00" src="https://user-images.githubusercontent.com/45676906/130739036-6ec04089-63da-4cf8-a083-37a80161f013.png">

즉, 데이터 양이 많다면 돌이키기 상당히 힘든 문제가 발생하게 될 것입니다. 

<br>

![스크린샷 2021-08-25 오후 3 40 15](https://user-images.githubusercontent.com/45676906/130739333-14290173-4b2c-46e0-8099-e72e29010770.png)

그래서 `Enumerated`를 사용하게 된다면 반드시 `EnumType.STRING`을 사용하여야 합니다. 

<br>

![스크린샷 2021-08-25 오후 3 41 13](https://user-images.githubusercontent.com/45676906/130739459-60a36968-ec0f-4e6c-8d7e-e0c6590ef1c2.png)

그러면 `ROLETYPE`도 `GUEST`인 STRING으로 저장되는 것을 볼 수 있습니다. 이러면 나중에 요구사항이 바뀌더라도 문제가 될일이 없기 때문에 이렇게 사용하는 것을 권장합니다. 

<br> <br>

## `@Temporal`

날짜 타입(java.util.Date, java.util.Calendar)를 매핑할 때 사용합니다. 참고로 자바 8 이후부터 `LoadDate`, `LocalDateTime`을 사용할 때는 생략 가능합니다. 

![스크린샷 2021-08-25 오후 3 44 30](https://user-images.githubusercontent.com/45676906/130739817-0990c3af-10fc-45bd-9086-79838b6537e2.png)

<br> <br>

## `@Lob`

데이터베이스 BLOB, CLOB 타입과 매핑됩니다. 매핑하는 필드 타입이 문자면 CLOB 매핑, 나머지(byte) 같은 경우면 BLOB으로 매핑이 됩니다.

<br> <br>

## `기본 키 매핑`

자동 생성은 `@GeneratedValue` 어노테이션을 사용합니다. 

- `IDENTITY`: 데이터베이스에 위임(MYSQL)
- `SEQUENCE`: 데이터베이스 시퀀스 오브젝트 사용(ORACLE)
- `AUTO`: 데이터베이스 방언에 따라 자동 지정됩니다.

<br>

## `IDENTITY 전략`

![스크린샷 2021-08-25 오후 4 21 09](https://user-images.githubusercontent.com/45676906/130744791-9a1aa825-6d46-489e-9715-b497f7a6f407.png)

위와 같이 `IDENTITY` 속성을 준 후에 JPA가 만들어주는 DDL 문을 보겠습니다. 

<br>

![스크린샷 2021-08-25 오후 4 22 03](https://user-images.githubusercontent.com/45676906/130744941-e8edd10d-e05d-4921-85f1-99ddf49752ff.png)

현재는 H2 Database로 방언이 되어 있기 때문에 위와 같이 `Identity`라고 생성되는 것을 볼 수 있습니다. 그러면 MySQL로 한번 바꿔서 다시 실행해보겠습니다. 

<br>

![스크린샷 2021-08-25 오후 4 23 40](https://user-images.githubusercontent.com/45676906/130745220-2ac2755d-5c81-4269-97fe-eb46c885b4fc.png)

MySQL 경우 `AUTO_INCREMENT`로 생성이 되는 것을 확인할 수 있습니다. 

<br> <br>

## `IDENTITY 전략 특징`

보통 트랜잭션이 커밋하는 시점에 INSERT 쿼리를 실행하지만, IDENTITY 전략은 예외적으로 `em.persist()`를 할 때 바로 INSERT 쿼리가 실행됩니다. 그 이유가 무엇일까요?
1차 캐시에 키 값은 해당 엔티티의 PK 값이어야 하는데 IDENTITY는 INSERT 쿼리가 실행이 된 이후에야 PK 값을 알 수 있기 때문에 예외적으로 `em.persist()`를 했을 때 바로 INSERT 쿼리가 실행되는 것입니다.