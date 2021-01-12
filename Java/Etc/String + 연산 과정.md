# `String + 연산이 일어나는 과정`

String에서 `+ 연산`은 사용하지 말고 `StringBuilder`를 사용하라는 말을 들어보셨을 것입니다. 이유가 무엇인지 모르겠다면 [String vs StringBuilder vs StringBuffer](https://github.com/wjdrbs96/Today-I-Learn/blob/master/Java/Java_lang/String%20vs%20StringBuffer%20vs%20StringBuilder.md) 를 먼저 읽고 오시면 됩니다.

`문자열 연결 연산자(+)`는 여러 문자열을 하나로 합쳐주는 편리한 수단입니다. 그런데 한 줄짜리 출력값 혹은 작고 크기가 고정된 객체의 문자열 표현을 만들 때라면 괜찮지만, 
본격적으로 사용하기 시작하면 성능 저하를 피하기 어렵습니다. 

`문자열 연결 연산자로 문자열 n개를 잇는 시간은 n^2에 비레`하기 때문입니다. 문자열은 불변이라서 두 문자열을 연결할 경우 양쪽의 내용을 모두 복사해야 하므로 성능 저하는 피할 수 없는 결과입니다.


```java
public class Test {
    public static void main(String[] args) {
        String a = "Gyunny";
        a += " Love";
    }
}
```

위와 같이 문자열을 `+ 연산`으로 연결한다면 내부적으로 어떤 일이 일어날까요? 

```
public static String valueOf(Object obj) {
    return (obj == null) ? "null" : obj.toString();
}
```

우선 `+ 연산자`를 사용하게 되면 String 클래스의 `valueOf()` 메소드가 호출됩니다. 그리고 `디버깅`을 통해서 확인해보면 아래의 메소드가 호출됩니다. 

```java
public class StringBuilder {
    @Override
    public StringBuilder append(Object obj) {     // 1번
        return append(String.valueOf(obj));
    }

    @Override
    public StringBuilder append(String str) {     // 2번
        super.append(str);
        return this;
    }
}
```

StringBuilder 클래스의 `append()` 메소드가 1번 -> 2번 순으로 호출됩니다. 내부를 보면 `super.append()`로 상위 클래스의 append를 호출하게 됩니다. 

```java
public class AbstractStringBuilder {
    public AbstractStringBuilder append(String str) {
        if (str == null)
            return appendNull();
        int len = str.length();
        ensureCapacityInternal(count + len);
        str.getChars(0, len, value, count);
        count += len;
        return this;
    }
}
```

StringBuilder와 StringBuffer의 상위 클래스인 `AbstractStringBuilder` 클래스의 append를 호출합니다. 

![스크린샷 2021-01-12 오후 8 05 21](https://user-images.githubusercontent.com/45676906/104306630-80e18380-5511-11eb-98f9-e8b953ba7a58.png)

그리고 `ensureCapacityInternal` 메소드를 보면 위와 같이 되어 있는데 `Arrays.copy()`를 통해서 새로운 `char 배열`에 복사하는 것을 볼 수 있습니다.

그리고 `getChars()` 메소드 내부를 보면 아래와 같습니다. 

![스크린샷 2021-01-12 오후 8 15 50](https://user-images.githubusercontent.com/45676906/104307745-f863e280-5512-11eb-9b41-4cd570cf6a4e.png)

여기서도 `System.arraycopy()`를 통해 복사하는 과정을 진행합니다. 




