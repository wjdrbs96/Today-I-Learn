# `@Transactional Propagation 알아보기`

이번 글에서는 `Spring Transactional 어노테이션에서 propagation 특징`에 대해서 정리해보려 합니다. 

<br> 

## `Propagation`

| 옵션 | 설명 |
|--------|-------|
| REQUIRED | 기본 옵션 <br> 부모 트랜잭션이 존재한다면 부모 트랜잭션에 합류, 그렇지 않다면 새로운 트랜잭션을 만든다. <br> 중간에 자식/부모에서 rollback이 발생된다면 자식과 부모 모두 rollback 한다. |
| REQUIRES_NEW | 무조건 새로운 트랜잭션을 만든다. <br> nested한 방식으로 메소드 호출이 이루어지더라도 rollback은 각각 이루어 진다. |
| MANDATORY | 무조건 부모 트랜잭션에 합류시킨다. <br> 부모 트랜잭션이 존재하지 않는다면 예외를 발생시킨다. |
| SUPPORTS |메소드가 트랜잭션을 필요로 하지는 않지만, 진행 중인 트랜잭션이 존재하면 트랜잭션을 사용한다는 것을 의미한다. 진행 중인 트랜잭션이 존재하지 않더라도 메소드는 정상적으로 동작한다. |
| NESTED | 부모 트랜잭션이 존재하면 부모 트랜잭션에 중첩시키고, 부모 트랜잭션이 존재하지 않는다면 새로운 트랜잭션을 생성한다. <br> 부모 트랜잭션에 예외가 발생하면 자식 트랜잭션도 rollback한다. <br> 자식 트랜잭션에 예외가 발생하더라도 부모 트랜잭션은 rollback하지 않는다. 이때 롤백은 부모 트랜잭션에서 자식 트랜잭션을 호출하는 지점까지만 롤백된다. 이후 부모 트랜잭션에서 문제가 없으면 부모 트랜잭션은 끝까지 commit 된다.  <br> 현재 트랜잭션이 있으면 중첩 트랜잭션 내에서 실행하고, 그렇지 않으면 REQUIRED 처럼 동작합니다. |
| NEVER | 메소드가 트랜잭션을 필요로 하지 않는다. 만약 진행 중인 트랜잭션이 존재하면 익셉션이 발생한다. |

<br>

대부분의 글에서 `Propagation`에 대해서 위와 같이 설명합니다. 말로만 들어도 어떤 말인지 알 수 있지만 저는 완벽하게 이해가 되지 않아서 직접 예제 코드를 작성해보면서 테스트 해보았습니다. 하나씩 알아보겠습니다. 

<br> <br>

## `REQUIRED`

![스크린샷 2021-11-28 오후 7 31 18](https://user-images.githubusercontent.com/45676906/143764284-48b5671f-3925-41f4-a7bf-770cf2c6d437.png)

`@Transactional`에 아무 설정을 하지 않고 사용하면 `Propagation Default인 REQUIRED`가 설정됩니다.

<br>

![스크린샷 2021-11-28 오후 7 35 00](https://user-images.githubusercontent.com/45676906/143764426-444e9f0c-201c-4890-a34b-e8df3f4800a5.png)

`REQUIRED`는 `DEFAULT` 설정 값이다 보니 많은 사람들이 제일 많이 사용하고 있는 `Propagation` 속성 입니다. 즉, 위의 글에 적혀 있던 대로 `자식/부모에서 rollback이 발생된다면 자식과 부모 모두 rollback 한다.` 라는 말을 이해할 수 있습니다. 즉, 위의 예제코드 처럼 자식 트랜잭션에서 예외가 발생하면 `부모`, `자식` 모두 롤백되어 어떤 유저도 저장되지 않습니다. (부모에서 예외가 발생해도 마찬가지 입니다.)

<br> <br>

## `자식에서 예외 발생한 것을 부모에서 예외 처리`

![스크린샷 2021-11-28 오후 9 27 52](https://user-images.githubusercontent.com/45676906/143767746-5463a1d2-18a3-4939-90ff-4a3fa4477311.png)

그러면 위와 같이 `자식에서 발생한 예외`를 `부모 트랜잭션`에서 `try-catch`로 예외 처리를 하는 경우에는 어떻게 될까요?

<br>

![스크린샷 2021-11-28 오후 9 30 00](https://user-images.githubusercontent.com/45676906/143767822-7aa9399b-5621-4f97-8b82-5bca70de927a.png)

`try-catch`를 하였더라도 `예외`가 발생하면서 전부 `RollBack` 되는 것을 볼 수 있습니다. 

<br> <br>

## `REQUIRES_NEW`

### `자식에서 예외 발생`

![스크린샷 2021-11-28 오후 7 40 25](https://user-images.githubusercontent.com/45676906/143764500-f598a950-3741-4c5c-9560-b8bc503ca116.png)

이번에는 자식에게 `REQUIRES_NEW` 옵션을 준 후에 위의 코드와 같이 테스트 해보았습니다. 위에서 적었던 설명으로 보면 `부모`, `자식` 트랜잭션이 각각 열리기 때문에 자식에서 예외가 발생해도 부모에서 `save` 한 것은 저장이 되는 것을 예상했습니다. 

<img width="169" alt="스크린샷 2021-11-28 오후 7 44 58" src="https://user-images.githubusercontent.com/45676906/143764627-a5ac10f8-2d89-4e4c-ac08-dadf7b7dd525.png">

그런데 부모에서 저장한 유저도 `INSERT` 되지 않은 것을 볼 수 있습니다. `REQUIRES_NEW`는 `자식 트랜잭션에서 발생한 것이 부모 트랜잭션 까지 전파되지 않는다`의 말은 틀린말이 아닌가 생각이 들었습니다.

<br> <br>

### `자식에서 발생한 예외 부모에서 예외 처리`

![스크린샷 2021-11-28 오후 9 33 47](https://user-images.githubusercontent.com/45676906/143767987-a23b80ed-2875-4160-9b06-002f347e1694.png)

그래서 이번에는 `부모에서 예외 처리`를 했을 때는 어떻게 처리되는지 보기 위해서 예외 처리하고 실행해보겠습니다. 이번에는 `자식에서 발생한 예외를 부모에서 try-catch`로 묶어주었는데요. 그랬더니 자식 트랜잭션을 호출하기 전까지의 쿼리만 커밋된 것을 확인할 수 있습니다. 예외 처리를 해주어야 `부모 트랜잭션도 자식 트랜잭션에 영향을 받지 않고 커밋`을 하는 것 같습니다.

<br>

<img width="146" alt="스크린샷 2021-11-28 오후 9 35 55" src="https://user-images.githubusercontent.com/45676906/143768004-413e59fc-1559-41e9-86fd-f65ded073159.png">

<br> <br>

### `부모에서 예외 발생`

![스크린샷 2021-11-28 오후 7 46 02](https://user-images.githubusercontent.com/45676906/143764681-eb35b2b0-4fcb-41af-a707-1bd7ee553e44.png)

이번에는 `부모에서 예외가 발생`한 경우에는 어떻게 되는지 테스트 해보겠습니다. 

<br>

<img width="152" alt="스크린샷 2021-11-28 오후 7 47 58" src="https://user-images.githubusercontent.com/45676906/143764729-038b1203-4ab4-4c9d-8c5c-c4c6d04baecb.png">

`REQUIRES_NEW`는 `부모 트랜잭션`에서 예외가 발생해도 `자식 트랜잭션`에서는 꼭 커밋되어야 하는 상황에서 사용하면 좋을 것 같습니다.

<br> <br>

## `MANDATORY`

![스크린샷 2021-11-28 오후 7 53 56](https://user-images.githubusercontent.com/45676906/143764920-df5dad7b-fae6-4787-9d93-22be71e1a3a6.png)

`MANDATORY`는 부모 트랜잭션이 존재하면 무조건 부모 트랜잭션에 합류시키고, 부모 트랜잭션에 트랜잭션이 시작된 것이 없다면 예외를 발생시킵니다. 즉, 혼자서는 독립적으로 트랜잭션을 진행하면 안되는 경우에 사용합니다. 

<br>

![스크린샷 2021-11-28 오후 7 56 24](https://user-images.githubusercontent.com/45676906/143764979-046446fa-bfb7-4c24-8fd4-53828ea56ce7.png)

예측 했던 대로 부모에서 트랜잭션을 시작하지 않아서 위와 같은 에러가 발생한 것을 볼 수 있습니다. 

<br>

<img width="154" alt="스크린샷 2021-11-28 오후 7 58 52" src="https://user-images.githubusercontent.com/45676906/143765039-d5077bc4-a018-4190-8415-e7202bb184dc.png">

그러면 부모에서 첫 번째로 저장한 `User 1번`만 저장되고 나머지는 저장되지 않은 것을 볼 수 있습니다. 

<br> <br>

## `NESTED`

### `부모에서 예외 발생`

![스크린샷 2021-11-28 오후 9 44 14](https://user-images.githubusercontent.com/45676906/143768277-abd4a2a5-b5f6-4358-94fa-3539c926ec12.png)

`NESTED` 속성에서는 부모 트랜잭션에서 에러가 발생하면 자식 트랜잭션은 어떻게 되는지 알아보겠습니다. 위의 코드를 실행하면 `자식` 트랜잭션도 커밋이 되지 않습니다. 이유는 부모 트랜잭션이 존재하면 자식 트랜잭션도 부모 트랜잭션에 합류하기 때문입니다. 

<br> <br>

### `자식에서 예외 발생`

![스크린샷 2021-11-28 오후 9 41 50](https://user-images.githubusercontent.com/45676906/143768186-64024994-569a-456c-9c75-a3070970b45f.png)
 
그리고 자식에서 예외가 발생하여도 마찬가지로 어떤 값도 `User`에 저장되지 않습니다. 

<br>

![스크린샷 2021-11-28 오후 9 41 30](https://user-images.githubusercontent.com/45676906/143768191-0c70245d-342a-450a-adda-0080639682fa.png)

그리고 예외를 발생했을 때 에러 로그를 보면 `JpaDialect does not support savepoints - check your JPA provider's capabilities`가 발생합니다. `NESTED`는 `JDBC 3.0 드라이버를 사용할 때에만 적용된다` 라는 특징이 있습니다. 

<br> <br>

### `자식에서 예외 발생한 예외 부모에서 예외 처리`

![스크린샷 2021-11-28 오후 9 55 02](https://user-images.githubusercontent.com/45676906/143768681-ad800355-3434-4dcd-8e14-ddddc6d0e903.png)

이번에는 `부모 트랜잭션`에서 예외 처리를 했을 때의 경우를 해보겠습니다. 

<br>

<img width="147" alt="스크린샷 2021-11-28 오후 10 02 35" src="https://user-images.githubusercontent.com/45676906/143768925-8d61c631-d6be-4416-adc6-186c4874dd91.png">

이번에도 `자식 트랜잭션`을 호출하기 전 부모 트랜잭션에서 호출한 `INSERT` 쿼리가 커밋된 것을 볼 수 있습니다. 

<br> <br>

## `부모 트랜잭션이 없을 경우`

![스크린샷 2021-11-28 오후 10 06 20](https://user-images.githubusercontent.com/45676906/143769074-8f216309-377c-4c5f-af88-055b33f1860b.png)

`NESTED` 속성은 부모 트랜잭션이 존재하지 않는다면 새로운 트랜잭션을 생성한다고 했는데요. 

<br>

<img width="147" alt="스크린샷 2021-11-28 오후 10 02 35" src="https://user-images.githubusercontent.com/45676906/143768925-8d61c631-d6be-4416-adc6-186c4874dd91.png">

그래서 위처럼 `부모 트랜잭션`이 없을 때는 자식 트랜잭션에서 새로 열리다 보니 자식 트랜잭션에서 예외가 발생해도 부모 트랜잭션에서는 1번 유저가 저장 커밋이 된 것을 볼 수 있습니다.

<br> <br>

## `NEVER`

![스크린샷 2021-11-28 오후 10 32 35](https://user-images.githubusercontent.com/45676906/143770022-9e9c89b1-8dc4-48ed-8d9d-94b61a02d7e0.png)

`NEVER`는 메소드가 트랜잭션을 필요로 하지 않는다. 만약 진행 중인 트랜잭션이 존재하면 익셉션이 발생합니다. 

<br>

![스크린샷 2021-11-28 오후 10 36 01](https://user-images.githubusercontent.com/45676906/143770124-654ea434-1c0b-4405-bde8-d4459707633b.png)

위처럼 부모에서 트랜잭션이 존재한다면 `Existing transaction found for transaction marked with propagation 'never'` 에러가 발생하는 것을 볼 수 있습니다.

<br>

이번 글의 코드를 확인하고 싶다면 [여기](https://github.com/wjdrbs96/blog-code/tree/master/Spring_Transactional) 에서 확인할 수 있습니다.