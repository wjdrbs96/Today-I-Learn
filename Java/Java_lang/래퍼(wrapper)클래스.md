# 래퍼(wrapper) 클래스란?

자바에서는 8개의 기본형이 존재한다. (int, double, float, boolean . . .)

<br>

프로그래밍을 하다 보면 때로는 기본형(primitive type) 변수도 어쩔 수 없이 객체로 다뤄야 하는 경우가 있다. 
예를 들면, 매게변수로 객체를 요구할 때, 기본형 값이 아닌 객체로 저장해야할 때, 
객체간의 비교가 필요할 때 등등의 경우에는 기본형 값들을 객체로 변환하여 작업을 수행해야 한다. 

<br>

이 때 사용되는 것이 `래퍼(wrapper class)`이다. 

<br>

### 박싱(Boxing)과 억박싱(Unboxing)

![test](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2F38MsL%2FbtqEbRcxIfZ%2FfOMbL4b3wCzqeO1aKKbFZ0%2Fimg.png)

JDK 1.5이전에는 기본형과 참조형 간의 연산이 불가능했기 때문에, 래퍼 클래스로 기본형을 객체로 만들어서 연산해야 했다. 

```java
public class Test {
    public static void main(String[] args) {
        int i = 5;
        Integer iObj = new Integer(7);
        
        int sum = i + iObj;  // JDK 1.5 이전에는 에러
    }
}
```

그러나 이제는 기본형과 참조형 간의 덧셈이 가능하다. 컴파일러가 자동으로 변환되는 코드를 넣어주기 때문이다. 

```java
public class Test {
    public static void main(String[] args) {
        int i = 5;
        Integer iObj = new Integer(7);
        
        int sum = i + iObj;             // 컴파일 전의 코드
        int sum = i + iObj.intValue();  // 컴파일 후의 코드 
    }
}
```

기본형 값을 래퍼 클래스의 객체로 자동 변환해주는 것을 `오토박싱(autoboxing)`이라 하고, 반대로 변환하는 것은 `언박싱(unboxing)`이라 한다. 

```java
import java.util.ArrayList;
import java.util.List;

public class Test {
    public static void main(String[] args) {
        List<Integer> list = new ArrayList<>();
        
        list.add(10);  // autoboxing. 10 -> new Integer(10);
        int value = list.get(0);   // unboxing. new Integer(10) -> 10
    }
}
```

위와 같이 ArrayList에 숫자를 저장하거나 꺼낼 때, 기본형 값을 래퍼 클래스의 객체로 변환하지 않아도 되므로 편리하다. 

<br>

```java
public class Test {
    public static void main(String[] args) {
        int i = 10;
        
        Integer integer = i;                          // 컴파일 전 
        // Integer integer = Integer.valueOf(i);      // 컴파일 후 해석
        
        Long l = 100L;                                // 컴파일 전
        // Long l = new Long(100L);                   // 컴파일 후 해석
    }
}
```

위와 같이 코드를 작성해도 컴파일러가 알아서 코드를 유추해 박싱 & 언박싱을 적용해준다는 것을 알 수 있다. 

<br>

## 래퍼 클래스 정리

| 기본형 | 래퍼클래스 | 생성자 | 활용예 | 
|-------|------|--------------|-------------------|
| boolean | Boolean | Boolean(boolean value) <br> Boolean(String s) | Boolean b = new Boolean(true); <br> Boolean b2 = new Boolean("true")  |
| char | Character |Character (char value)| Character c = new Character('a');  |
| byte | Byte | Byte (byte value) <br> Byte (String s) | Short s = new Short(10); <br> Short s2 = new Short("10");  |
| short | Short | Short(short value) <br> Short (String s) | Short s = new Short(10) <br> Short s2 = new Short("10");  |
| int | Integer | Integer (int value) <br> Integer (String s) | Integer i = new Integer(100) <br> Integer i2 = new Integer("100") |
| long | Long | Long (long value) <br> Long (String s) | Long l = new Long(100); <br> Long l2 = new Long("100");   |
| float | Float | Float(double value) <br> Float(float value) <br> Float(String s) | Float f = new Float(1.0) <br> Float f2 = new Float(1.0f) <br> Float f3 = new Float("1.0f");   |
| double | Double | Double(double value) <br> Double (String s) | Double d = new Double(1.0) <br> Double d2 = new Double("1.0"); |

<br>

### 래퍼 클래스 구조도 

![title](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2Fbvzp79%2FbtqEbacB01v%2FQQjO7cSc9tTvKJkyzFsK90%2Fimg.png)