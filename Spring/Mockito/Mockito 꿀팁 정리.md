## `Mockito 유용한 것 정리`

```
given(userRepsotiroy.save(any(User.class))).willReturn(user);
```

- save 메소드를 위처럼 `Mocking` 할 수 있음

<br>

## `Mockito Throw Mocking 하기`

```
Mokito.doThrow(new Exception()).when(instance).methodName();
doThrow(new Exception()).when(userValidator).validate(anyString(), anyString());
```

<br>

## `Mockito verify 사용하기`

```
verify(userValidator).methodName()
verify(userValidator, time(1)).methodName()
```

위처럼 사용하면 1번 호출된 걸 검증할 수 있음

<br>

## `Class Mocking 안될 때`

`@Mock`으로 클래스를 `Mocking` 할 때 `Mock 객체 주입이 안된다면 인터페이스로 감싸서 해보기`

<br>

