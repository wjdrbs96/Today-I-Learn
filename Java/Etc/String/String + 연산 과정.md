# `String + 연산이 일어나는 과정`

String에서 `+ 연산`은 사용하지 말고 `StringBuilder`를 사용하라는 말을 들어보셨을 것입니다. 이유가 무엇인지 모르겠다면 [String vs StringBuilder vs StringBuffer](https://github.com/wjdrbs96/Today-I-Learn/blob/master/Java/Java_lang/String%20vs%20StringBuffer%20vs%20StringBuilder.md) 를 먼저 읽고 오시면 됩니다.

`문자열 연결 연산자(+)`는 여러 문자열을 하나로 합쳐주는 편리한 수단입니다. 그런데 한 줄짜리 출력값 혹은 작고 크기가 고정된 객체의 문자열 표현을 만들 때라면 괜찮지만, 
본격적으로 사용하기 시작하면 성능 저하를 피하기 어렵습니다. 


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

뭔가 막~~ 복잡해보이지만.. 저도 이해하기가 어렵습니다. 그래서 결론은 그냥 어떤 복사하는 과정이 일어나기 때문에 성능에 좋지 않다 정도만 일단은 이해하면 될 것 같습니다.

그래서 맨 처음 코드의 동작 원리를 정리해보면 아래와 같이 동작합니다. 

```
StringBuilder builder = new StringBuilder();
builder.append("Gyunny");
builder.append(" Love");
String s = builder.toString();
```

여기서 마지막에 StringBuilder의 toString() 메소드를 호출하는데 내부 코드를 보겠습니다.

```java
public final class StringBuilder {
    @Override
    public String toString() {
        // Create a copy, don't share the array
        return new String(value, 0, count);
    }
}
```

내부적으로 `new String()`으로 String 객체를 새로 만드는 것을 볼 수 있습니다. String의 + 연산을 할 때 많은 일이 일어난다는 것을 알 수 있습니다.

<br>

## `String + 연산을 StringBuilder로 바꿔주는 예시`

```java
public class Test {
    public static void main(String[] args) {
        String str = "Gyuuny";
        String result = str + " Java" + " Love";
        System.out.println(result);
    }
}
```
```
String s = "Gyunny";
String s1 = (new StringBuilder()).append(s).append(" Java")..append(" Love").toString();
```

위와 같은 과정을 수행되기 때문에 이것이 for문 같이 반복문으로 + 연산을 수행한다면 반복문의 횟수 만큼 StringBuilder 객체가 생생되고 append 메소드, toString() 호출이 발생하게 됩니다.

<br>

### `String concat() 메소드`

```java
public final class String {
    public String concat(String str) {
        int otherLen = str.length();
        if (otherLen == 0) {
            return this;
        }
        int len = value.length;
        char buf[] = Arrays.copyOf(value, len + otherLen);
        str.getChars(buf, len);
        return new String(buf, true);
    }
}
```

문자열을 연결해주는 `concat()` 메소드도 위와 코드 같이 새로운 char 배열에 복사를 한 후에 `new String()`을 통해서 새로운 String 객체를 만들어주는 것을 볼 수 있습니다. 

<br>

## `StringBuilder vs String 성능 측정`

```java
public class Test {
    public static void main(String[] args) {
        stringBuilderTest();
        stringTest();
    }

    public static void stringTest(){

        String result = "";
        long start = System.currentTimeMillis();

        for(int i = 0 ; i < 100000; i++){
            result += "test";
        }
        long end = System.currentTimeMillis();

        System.out.println("String exec time : " + (end - start));
    }

    public static void stringBuilderTest(){

        StringBuilder result = new StringBuilder();
        long start = System.currentTimeMillis();

        for(int i = 0 ; i < 100000; i++){
            result.append("test");
        }
        long end = System.currentTimeMillis();

        System.out.println("String builder exec time : " + (end - start));

    }
}
```
```
String builder exec time : 5
String exec time : 14249
```

위와 같이 String에서 + 연산을 사용하면 `StringBuilder`로 바꿔주긴 합니다. 하지만 반복문의 횟수만큼 StringBuilder 객체가 만들어지고 + 연산 내부적으로 많은 일들이 일어나서(위에서 본 것 처럼) 시간이 많이 걸리는 것을 볼 수 있습니다. 

따라서 많은 문자열 연결 연산이 필요하다면 StringBuilder를 사용해야 합니다.