# `아이템63 : 문자열 연결은 느리니 주의하라`

문자열 연결 연산자(+)는 여러 문자열을 하나로 합쳐주는 편리한 수단입니다. 

```java
public class Test {
    public String statement() {
        String result = "";
        for (int i = 0; i < 10; ++i) {
            result += "t";
        }
        return result;
    }
}
```

하지만 `문자열 연결 연산자로 문자열 n개를 잇는 시간은 n^2에 비례합니다.` 문자열은 불변이라서 두 문자열을 연결할 경우 양쪽의 내용을 모두 복사해야 하므로 성능 저하가 있습니다.

<br>

```java
public class Test {
    public String statement() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 10; ++i) {
            sb.append("t");
        }
        return sb.toString();
    }
}
```

문자열을 추가해야 할 것이 많다면 이 메소드는 심각하게 느려질 수 있습니다. 따라서 `성능을 포기하고 싶지 않다면 String 대신 StringBuilder를 사용하는 것이 좋습니다.`

<br>

## `핵심 정리`

> 성능에 신경써야 한다면 많은 문자열을 연결할 때는 문자열 연결 연산자(+)를 피하자.
> 대신 StringBuilder의 append를 사용하자. 