## String 클래스란?

그리고 String 클래스를 보면 final인 것을 볼 수 있습니다. final class는 `더 이상 확장할 수 없다`는 특징을 가지고 있습니다. 
다시 말해서 String 클래스는 다른 클래스의 부모가 될 수 없고, 있는 그대로 써야한다는 뜻입니다. 

<br>

### 변경 불가능한(immutable) 클래스 

한번 생성된 String 인스턴스가 갖고 있는 문자열은 읽어 올 수만 있고, 변경할 수는 없습니다. 어떤 말인지 자세히 알아보겠습니다.

```java
public class Test {
    public static void main(String[] args) {
        String javaStr = "java";
        String android = "android";
        javaStr += android;
    }
}
```

위와 같은 코드를 봤을 때, `javsStr`의 값은 바뀌었는데? 라고 생각할 수 있습니다. 하지만 값을 바뀐 것이 아니라 새로운 `상수풀`의 값을 
가리키는 것입니다.

<img src="https://user-images.githubusercontent.com/45676906/92989377-efd0b080-f50e-11ea-8e7a-2d7547801d66.png">

<br> <br>

위의 사진처럼 `+`연산자를 이용해서 문자열을 결합하는 경우 인스턴스내의 문자열이 바뀌는 것이 아니라 `String pool`이라는 공간 안에
메모리를 할당받아 새로운 문자열("ab")이 담긴 String 인스턴스가 생성되는 것입니다.

<br>

### String 클래스가 적절한 경우

* 문자열 연산이 적고 자주 참조(조회)하는 경우에 사용하면 좋습니다.
* 멀티쓰레드 환경에서 동기화를 신경쓰지 않아도 됩니다.(final 클래스이면서 불변 클래시이기 때문)

<br>

### String 클래스가 적절하지 않은 경우

* `+`나 `concat`을 이용하여 계속 새로운 문자열을 만들게 되면 `기존의 문자열`은 가비지 컬렉터에 의해 제거되어야 한다는 단점이 있습니다.
* 즉, 문자열 연산이 많아진다면 String 클래스는 적절하지 않습니다.

<br>

### `JDK 5 이상에서는 String의 더하기 연산`

JDK 5 이상에서는 여러분들이 String의 더하기 연산을 할 경우, 컴파일할 때 자동으로 해당 연산을 `StringBuilder`로 변환해 줍니다. 
따라서, 일일이 더하는 작업을 직접 변환할 필요는 없습니다. 

하지만 `for 루프와 같이 반복 연산을 할 때에는 자동으로 변환해주지 않는다는`점을 기억해야 합니다.

간략하게 설명하면 위와 같은데 `String 문자열 연산`에 자세히 알고 싶다면 [여기]() 에서 확인하면 됩니다.
 
<br>

### 그러면 `new`를 이용한 것과 `상수 풀(Constant pool)`을 이용해서 인스턴스를 만드는 것은 어떤 차이일까?

```java
public class Test {
    public static void main(String[] args) {
        String str1 = new String("abc");     // 인스턴스로 생성됨
        String str2 = "test";                // 상수풀에 있는 문자열을 가르킴
        String str3 = "test";                // 상수풀에 있는 문자열을 가르킴
    }
}
```

`new`를 이용해서 메모리에 할당하면 `힙 영역`에 메모리가 할당이 됩니다. 그리고 `new`없이 String에 `""리터럴"`을 사용하여 생성할 경우
내부적으로 `new String()`이 호출되고 바로 intern() 메소드를 호출해서 String 객체를 `상수풀`에 등록하게 합니다.
(만약 `상수 풀`내에 동일한 문자열이 존재 하고 있다면, new로 생성된 String 객체는 힙에서 사라지고 상수 풀의 동일한 문자열을 가진 레퍼런스를 반환한다.)

- `intern() 메소드`: new String()으로 생성한 문자열 객체라고 할지라도, 풀에 해당 값이 있으면, 풀에 있는 값을 참조하는 객체를 리턴합니다.
만약 동일한 문자열이 존재하지 않는다면 풀에 해당 값을 추가합니다. 

<img src="https://user-images.githubusercontent.com/45676906/92989337-98324500-f50e-11ea-9004-2e4ed6c1adf8.png">

<br> <br>

### 그러면 `상수 풀`은 어떤 곳일까?

너무 내용이 길어져서 따로 빼서 정리하려 합니다. `상수 풀`에 대한 자세한 내용은 [여기]() 에서 확인하면 됩니다.

<br>

### `코드 예제`

```java
public class Test {
    public static void main(String[] args) {
        String a = "JAVA";
        String b = "JAVA";
        System.out.println(a == b);           // true

        String str3 = new String("JAVA");
        String str4 = new String("JAVA");
        System.out.println(str3 == str4);     // false

    }
}
```

위의 코드의 경우 a == b는 true 입니다. 왜냐하면 `""리터럴`을 이용해서 생성하였기 때문에 상수풀을 이용하기 때문입니다. 
(상수풀을 이용하게 됐을 때, 상수풀에 존재하지 않는 문자열이라면 내부적으로 new String("Java")가 됩니다. 그러나 다시 `intern()메소드에 의해서 Heap의 String 객체를 해제 하고 상수풀의 String 객체의 레퍼런스를 반환합니다.`)



<br>

```java
public class Test {
    public static void main(String[] args) {
        String a = "JAVA";
        String b = new String("JAVA");
        System.out.println(a == b);   // false
    }
}
```

위의 코드는 false이다. 왜냐하면 `a`는 `"리터럴"`을 이용해서 객체를 만들었기 때문에 `상수풀`을 가리키게 되고 'b'는 `new`를 이용해서
만들었기 때문에 `Heap`영역에 메모리가 할당된다. 따라서 두개의 메모리 값은 다르다.

<br>

```java
public class Test {
    public static void main(String[] args) {
        String a = "JAVA";
        String b = new String("JAVA");
        b = b.intern();
        System.out.println(a == b);   // true
    }
}
```

위의 결과는 true 입니다. `a`는 `상수풀`을 가리키고 `b`는 `Heap`영역에 메모리가 할당된다고 하였습니다. 
하지만 `intern()`메소드는 `Heap의 String 객체를 해제 하고 상수풀의 String 객체의 레퍼런스를 반환합니다.`(상수풀에 없으면 등록하고 레퍼런스 반환) 
(여기서는 `상수풀에 JAVA 라는 문자열이 존재`하기 때문에 상수풀에 있는 "JAVA"를 찾아 레퍼런스 값을 반환할 수 있다)

<br>

### `intern() 메소드는 절대 사용하면 안됩니다.`

새로운 문자열을 많이 만드는 프로그램에서 itern() 메소드를 사용하여 억지로 `상수 풀`에 값을 할당하도록 하면, 저장되는 영역은 한계가 있기 때문에 그 영역에 대해서 별도로 가비지 컬렉팅하는 과정을 거쳐야 합니다.
 
<br>

## `StringBuffer vs StringBuilder` 

`StringBuffer`와 `StringBuilder`는 `String`과는 다르게 `mutable(변경가능)`합니다.

<br>

### `StringBuffer`

내부적으로 문자열 편집을 위한 버퍼(buffer)를 가지고 있으며, StringBuffer 인스턴스를 생성할 때 그 크기를 지정할 수 있습니다.
그리고 `멀티스레드에 안전(thread safe)하도록 동기화`되어 있기에 `동기화`가 `StringBuffer`의 성능을 떨어뜨립니다.
따라서 멀티쓰레드로 작성된 프로그램이 아닌 경우, StringBuffer의 동기화는 불필요하게 성능만 떨어트리게 됩니다. 

```java
 public final class StringBuffer
    extends AbstractStringBuilder
    implements java.io.Serializable, CharSequence {

    public StringBuffer() {
        super(16);
    }

    public StringBuffer(int capacity) {
        super(capacity);
    }

    public StringBuffer(String str) {
        super(str.length() + 16);
        append(str);
    }
}
```

`StringBuffer`인스턴스를 생성할 때, 버퍼의 크기를 지정해주지 않으면 default로 16개의 문자를 저장할 수 있는 크기의 버퍼를 생성합니다.

버퍼의 크기가 작업하려는 문자열의 길이보다 작을 때는 내부적으로 버퍼의 크기를 증가시키는 작업이 수행됩니다. `배열의 길이는
변경될 수 없으므로 새로운 길이의 배열을 생성한 후에 이전 배열의 값을 복사해야 합니다.` 

<br>

### `StringBuffer 비교`

```java
public class Test {
    public static void main(String[] args) {
        StringBuffer sb1 = new StringBuffer("ABC");
        StringBuffer sb2 = new StringBuffer("ABC");
        System.out.println(sb1 == sb2);        // false
        System.out.println(sb1.equals(sb2));   // false
    }
}
```

String클래스에는 `equals`메소드가 오버라이딩 되어 문자열의 내용을 비교하도록 구현되어 있지만, StringBuffer클래스는 `equals`메소드를
오버라이딩 하지 않아서 StringBuffer클래스의 `equals`메소드를 사용해도 등가비교연산자(==)로 비교한 것과 같은 결과를 얻습니다.

<br>

### `StringBuilder`

`StringBuffer`에서 `쓰레드의 동기화`만 뺀 `StringBuilder`가 새로 추가되었습니다. `StringBuffer`와 `StringBuilder`는 완전히 똑같은 기능으로
작성되어 있습니다.

 
```
따라서 멀티쓰레드 환경에서는 StringBuffer를 사용하고, 그렇지 않다면 StringBuilder를 사용하는 것이 좋습니다.
```