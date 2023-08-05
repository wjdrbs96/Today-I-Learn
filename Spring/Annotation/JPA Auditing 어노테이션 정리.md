## `JPA Auditing 어노테이션 정리`

### `MappedSuperclass`

부모 클래스는 테이블과 매핑하지 않고 부모 클래스를 상속 받는 자식 클래스에게 매핑 정보만 제공하고 싶을 때 사용하는 것이 `@MappedSuperclass` 어노테이션이다.

즉, `@MappedSuperclass`는 테이블과는 관계가 없고 단순히 엔티티가 공통으로 사용하는 매핑 정보를 모아주는 역할을 한다.

<br>

### `@EntityListeners (AuditingEntityListener.class)`

`BaseEntity` 클래스에 Auditing 기능을 제공한다.

<br>

### ` @CreatedDate`

Entity가 생성되고 저장 될 때 시간이 자동으로 저장된다.

<br>

### `@LastModifiedDate`

조회 한 Entity의 값을 변경할 때 시간이 자동으로 저장된다.

<br>

### `@EnableJpaAuditing`

JPA Auditing 기능을 활성화 시키는 역할을 합니다.