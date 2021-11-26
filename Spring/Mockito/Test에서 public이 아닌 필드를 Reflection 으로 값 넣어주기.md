# `Test에서 public이 아닌 필드를 Reflection 으로 값 넣어주는 법`

이번 글에서는 `테스트 코드`를 작성하면서 `public`이 아닌 필드에 값을 넣어주어야 하는 상황에서 `Reflection`을 사용해서 값을 넣어주는 법에 대해서 정리해보겠습니다. 이번 글은 `Reflection`이 무엇인지 설명하는 글은 아니다 보니 `Reflection`을 알고 싶다면 [Reflection 알아보기](https://www.baeldung.com/java-reflection) 를 참고하시면 좋을 거 같습니다.  

먼저 제가 진행하고 있는 프로젝트에서 겪은 상황에 대해서 공유해보겠습니다. 

![스크린샷 2021-11-26 오후 8 48 59](https://user-images.githubusercontent.com/45676906/143576639-f154c24c-351d-4a05-b494-14f7301c6dd8.png)

현재 `Repository`는 `Mockito`를 사용해서 `Mocking` 하고 `Service Layer`의 메소드만 `Unit Test`를 해보려 하고 있습니다. 내부 로직이 없고 단순히 `Entity` -> `DTO`로 변환하는 메소드 입니다.

<br>

![스크린샷 2021-11-26 오후 8 51 21](https://user-images.githubusercontent.com/45676906/143576845-974da5f6-cfff-4d8b-acc1-236315b547d6.png)

`DTO` 내부를 보면 `Post`가 만들어진 시간인 `LocalDate` 타입의 `createdAt`을 클라이언트에게 반환해주어야 하는 상황입니다. 

<br>

![스크린샷 2021-11-26 오후 8 48 13](https://user-images.githubusercontent.com/45676906/143576994-bf178014-0bd3-47b3-b896-0b227188946b.png)

`Post Entity`는 [JPA Auditing](https://devlog-wjdrbs96.tistory.com/415) 기능을 사용하여 `createdAt`, `updatedAt`을 자동으로 만들어주는 기능을 사용하고 있습니다. 즉, `Post Entity`는 `BaseEntity`를 부모 클래스로 `extends` 하고 있는 것을 볼 수 있습니다. 

<br>

![스크린샷 2021-11-26 오후 8 54 53](https://user-images.githubusercontent.com/45676906/143577204-202bf711-1ee3-46d2-ae40-6c5ddd0104b6.png)

`createdTime`의 접근 지정자를 보면 `protected`인 것을 볼 수 있습니다. `private`여도 마찬가지고 `public`이 아니라는 것을 알 수 있습니다. 이러한 상황에서 테스트 코드를 작성해보겠습니다.

<br>

![스크린샷 2021-11-26 오후 9 00 56](https://user-images.githubusercontent.com/45676906/143578002-900b730b-35e4-44fd-8bd8-5cf56421493c.png)

위처럼 `Repository`를 `Mocking` 하고(given) `Service`를 호출(when) 하였습니다. 문제 없이 잘 실행 되겠구나 했습니다. 

<br>

![스크린샷 2021-11-26 오후 9 03 12](https://user-images.githubusercontent.com/45676906/143578238-b5f6d019-d451-4146-a60f-d952ad51b648.png)

`NPE`가 발생하는 것을 볼 수 있는데요. 원인은 찾아가보면 위의 `DTO` 클래스에서 `Entity -> DTO` 변환할 때 `createdAt` 값이 필요한데 제가 넣지 않아서 `NPE`가 발생하는 것입니다. 

<br>

![스크린샷 2021-11-26 오후 9 04 36](https://user-images.githubusercontent.com/45676906/143578435-89fabcee-20ff-449f-b509-8bdc51ce20a3.png)

그래서 `Post Entity`를 만드는 `createPost()` 메소드에서 `createdAt`을 넣어주고 싶었지만 `createdAt`은 부모 클래스인 `BaseEntity`에 존재하기 때문에 값을 넣을 수 없는 문제가 발생했습니다.

<br>

![스크린샷 2021-11-26 오후 9 07 56](https://user-images.githubusercontent.com/45676906/143578795-d7cd9e93-5b23-44fa-94f3-d6bfd3f86e3c.png)

그래서 위와 같이 값을 넣어주기 위해서 필드에 `Setter` 어노테이션을 만들었습니다. 

<br>

![스크린샷 2021-11-26 오후 9 09 31](https://user-images.githubusercontent.com/45676906/143579017-678cafd6-034e-43d9-b731-dc60f7e96711.png)

그래서 위와 같이 `setter`를 사용해서 `Post Entity`에 `LocalDateTime` 값을 넣었습니다. 이렇게 해도 가능한 방법이지만 테스트 코드로 인해서 운영 코드에 `Setter`를 추가해야 하는 것이 맘에 들지 않았습니다. 그래서 이럴 때 사용할 수 있는 것이 `ReflectionTestUtils` 클래스 입니다. 

<br> <br>

## `ReflectionTestUtils`

[ReflectionTestUtils](https://www.baeldung.com/spring-reflection-test-utils) 를 보면 `public`이 아닌 필드를 `리플랙션을 통해서 값`을 넣어줄 수도 있고 `private` 메소드에게도 사용할 수 있고 기능은 여러가지가 있는 것 같습니다. 하지만 이렇게 `Superclacss`의 필드를 어떻게 값을 넣는지 찾기가 쉽지 않았습니다.

<br>

![스크린샷 2021-11-26 오후 9 14 41](https://user-images.githubusercontent.com/45676906/143579602-a458ec14-eaec-4519-88bc-0508f60b4b6f.png)

해결 방법은 `setField`를 사용하여 값을 넣는 것인데요. 매개 변수를 잘 넘겨주어야 합니다. 

1. 자식 클래스 객체 (getClass 아님)
2. 부모 클래스.class
3. 부모 클래스에서 set Reflection 할 필드 이름
4. 필드 타입의 값 (현재라면 LocalDateTime)
5. 필드 타입.class

<br>

위처럼 5개의 매개변수를 넘기면 `Post Entity`의 `createdAt` 필드의 값을 `Reflection`으로 넣을 수 있습니다. 

<br>

![스크린샷 2021-11-26 오후 9 17 33](https://user-images.githubusercontent.com/45676906/143579904-901d71e7-2aa7-4e71-a096-f1a108ac2fb6.png)

그리고 테스트를 실행하면 문제 없이 테스트가 성공하는 것을 볼 수 있습니다. 