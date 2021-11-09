# `JPA AuditingEntityListener 알아보기`

![스크린샷 2021-11-08 오후 10 05 35](https://user-images.githubusercontent.com/45676906/140747079-6ee37ae6-9a69-4abd-a80a-f415db9ec73c.png)

위의 코드는 제가 작성했던 코드인데요. 누군가 저에게 아래와 같이 물었습니다. 

> LocalDateTime.now() 코드는 왜 작성한거야?

<br>

위의 질문을 듣고 `아.. BaseEntity를 통해서 생성시간, 수정시간`을 저장하기 위해서 만들었는데 `now()` 코드는 없어도 되려나? 라는 생각이 들었습니다. 지금 생각하면 당연히 없어도 될 코드인데 왜 저렇게 작성했나 싶지만.. 어쨋든 `AuditingEntityListener`가 어떻게 동작하는지 잘 모르고 사용하고 있는 거 같아서 이번 글에서는 `JPA AuditingEntityListener`를 어떻게 사용하고 어떤 원리로 동작하는 것인지 알아보겠습니다.     

<br> <br>

## `JPA Auditing 이란?`

![스크린샷 2021-11-08 오후 11 16 43](https://user-images.githubusercontent.com/45676906/140757751-b7812f6a-82be-4d44-baef-37e074ebc105.png)

언제 만들어졌는지, 언제 수정되었는지는 중요한 정보이기 때문에 보통 `Entity`에는 `생성시간`, `수정시간`을 포함합니다. 그런데 모든 `Entity`에 `생성시간`, `수정 시간` 필드를 넣어서 생성하는 것을 상당히 비효율적일 것인데요. 이럴 때 사용하는 것이 `JPA Auditing` 입니다.    

<br>

![스크린샷 2021-11-08 오후 10 27 20](https://user-images.githubusercontent.com/45676906/140750244-e810c4ed-be77-445a-a180-d7dfc6a31a28.png)

모든 엔티티에 중복된 필드를 적용할 때 사용하는 것이 `@MappedSuperclass` 인데요. 위와 같이 `@MappedSuperclass` 애노테이션이 존재하면 `Entity`가 `BaseEntity`를 상속하면 `생성시간`, `수정시간`도 컬럼으로 인식하게 됩니다. 즉, 위의 코드의 애노테이션을 정리하면 아래와 같습니다. 

- `@MappeedSuperClass`: `Entity`가 `BaseEntity`를 상속하면 `생성시간`, `수정시간`도 컬럼으로 인식하게 됩니다.
- `@CreatedDate`: `Entity`가 생성될 때 자동으로 생성 시간이 저장됩니다. 
- `@LastModifiedDate`: `Entity`가 수정될 때 자동으로 수정 시간이 저장됩니다. 

<br> <br>

## `EntityListeners`

![스크린샷 2021-11-09 오전 11 54 31](https://user-images.githubusercontent.com/45676906/140853534-1d3ac94d-3446-4768-bbb5-51e56d9dd966.png)

`@EntityListeners`는 엔티티를 DB에 적용하기 `전, 이후에 커스텀 콜백을 요청`할 수 있는 어노테이션입니다. `@EntityListeners`의 인자로 커스텀 콜백을 요청할 클래스를 지정해주면 되는데, Auditing 을 수행할 때는 JPA 에서 제공하는 `AuditingEntityListener`를 인자로 넘기면 됩니다.

그래서 `AuditingEntityListener` 내부 코드를 보면 `touchForCreate`, `touceForUpdate` 메소드가 존재하고, 그 위에 `@PrePersist`, `@PreUpdate` 애노테이션이 존재하는 것도 볼 수 있는데요. 이것을 통해서 `Entity`에 생성, 수정이 일어나면 콜백이 실행되어 시간을 만들어주는 것입니다. 즉, 저처럼 `now()`를 통해서 직접 시간 생성 코드를 넣지 않아도 됩니다. 

<br> <br>

![스크린샷 2021-11-08 오후 11 32 10](https://user-images.githubusercontent.com/45676906/140760467-e9c9d83a-a517-49ed-80c6-3f5964741b27.png)

그리고 `JPA Auditing` 애노테이션들이 활성화할 수 있도록 `@EnableJpaAuditing` 애노테이션을 위와 같이 추가하겠습니다. 

- `@EnableJpaAuditing`: JPA Auditing 애노테이션을 활성화시킵니다. 

<br> <br>

## `JPA Auditing 테스트 코드 작성하기`

![스크린샷 2021-11-08 오후 11 44 46](https://user-images.githubusercontent.com/45676906/140762585-62113a6e-ae57-4840-895c-a7e45a841d0e.png)

`Member Entity`를 저장했을 때 `JPA Auditing`이 제대로 동작했다면 `생성시간`, `수정시간`에 값이 존재할 것입니다. 즉, 현재 시간보다는 무조건 뒤일 것이기 때문에 `isAfter()`를 사용해서 테스트 코드를 작성하였습니다. 

<br>

![스크린샷 2021-11-08 오후 11 46 43](https://user-images.githubusercontent.com/45676906/140762980-a8e56e36-b474-42f7-9ec4-b09891f641de.png)

그리고 실행해보면 테스트도 성공하여 제대로 동작하는 것을 볼 수 있고 `Member Table`이 만들어질 때 쿼리를 보면 `created_time`, `last_modified_time`이 정상적으로 생성된 것도 확인할 수 있습니다. 

<br>

이번 글의 코드를 보고 싶다면 [Github](https://github.com/wjdrbs96/blog-code/tree/master/auditing) 에서 확인하실 수 있습니다.