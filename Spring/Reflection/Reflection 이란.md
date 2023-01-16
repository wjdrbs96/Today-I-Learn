## `리플렉션(Reflection) 이란?`

(정리중)

리플렉션은 `Class<?>` 타입을 사용한다. 

<br>

### `리플렉션을 왜 쓸까?`

- 컴파일 시점엔 객체의 타입을 모르기 때문에 동적으로 해결하기 위해 `리플렉션` 사용

<br>

### `어디서 사용되는가`

- JPA
- Jackson
- Mockito
- JUnit

<br>

## `객체의 기본 생성자가 필요한 이유`

- JPA Entity
- Request DTO, Response DTO

<br>

모두 리플렉션 때문에 `기본 생성자`가 필요하다.

<br>

## `Refernece`

- [https://www.youtube.com/watch?v=67YdHbPZJn4](https://www.youtube.com/watch?v=67YdHbPZJn4)