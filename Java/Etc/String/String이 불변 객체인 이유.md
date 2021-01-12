# `String이 불변 객체인 이유`

String 객체가 `불변 객체`라는 것은 다들 알고 있을 것입니다. 불변 개체는 완전히 생성된 후에도 내부 상태가 일정하게 유지되는 개체입니다. 
즉, 객체가 변수에 할당되면 참조를 업데이트하거나 내부 상태를 어떤 방법으로도 변경할 수 없습니다. 

<br>

## `그러면 자바에서는 왜 String을 불변 객체로 만들었을까요?`

`성능`, `동기화`, `캐싱`, `보안`, 의 이유로 불변 객체로 만들었는데 하나씩 알아보겠습니다. 

<br>

### `1. 성능(Performance)`

자바에서 문자열은 정말 많이 사용됩니다. 그렇기 때문에 자바에서는 `상수 풀`이라는 것을 만들었습니다. 상수 풀이 무엇인지 아래 코드를 보면서 알아보겠습니다.

```java
public class Test {
    public static void main(String[] args) {
        String s1 = "Hello World";
        String s2 = "Hello World";

        System.out.println(s1 == s2);  // true
    }
}
```

위의 코드 실행 과정을 분석해보면 문자열 s1에 해당하는 것을 `상수 풀`에서 검색을 합니다. 없다면 `상수 풀`에 등록하고 해당하는 레퍼런스 값을 반환합니다.

s2 문자열도 마찬가지로 `상수 풀`에서 해당 문자열이 있는지 검색합니다. s1을 이미 `상수 풀`에 등록했기 때문에 같은 레퍼런스를 반환합니다. 

`문자열 리터럴을 캐싱하고 재사용`하면 문자열 풀의 다른 문자열 변수가 동일한 개체를 참조하기 때문에 `힙 공간을 많이 절약`할 수 있습니다. 문자열 상수 풀은 이러한 장점을 가지고 사용됩니다.

![sangsu](https://www.baeldung.com/wp-content/uploads/2018/08/Why_String_Is_Immutable_In_Java.jpg)

<br>

이렇게 `상수 풀`을 사용하는데 String이 불변 객체가 아니라면 어떻게 될까요?

```java
public class Test {
    public static void main(String[] args) {
        String s1 = "Hello World";
        String s2 = "Hello World";
        s1 = "Gyunny";

        System.out.println(s1 == s2);  // true
    }
}
```

String이 불변 객체가 아니면서 상수 풀을 사용하고 있다면 위의 결과도 true가 나오게 될 것입니다. 이것은 말이 안되기 때문에 `상수 풀`을 장점을 얻어 사용하기 위해서 String을 불변 객체로 만든 것입니다. 

<br>

### `2. 동기화(Synchronization)`

불변 객체는 값이 바뀔 일이 없기 때문에 멀티스레드 환경에서 `Thread-safe` 하다는 장점이 있습니다. 

따라서 일반적으로 불변의 개체는 동시에 실행되는 여러 스레드에서 공유할 수 있습니다. 스레드가 값을 변경하면 동일한 문자열을 수정하는 대신 문자열 풀에 새 문자열이 생성되기 때문에 스레드 안전도 됩니다. 

<br>

### `3. 해시코드 캐싱(Hashcode Caching)`

문자열 개체는 데이터 구조로 많이 사용되기 때문에 해시맵, 해시테이블, 해시셋 등과 같은 해시 구현에서도 널리 사용됩니다. 이러한 해시 구현에 따라 작동할 때 버킷을 위해 hashCode() 메서드가 꽤 자주 호출됩니다.

```java
public final class String {
    public int hashCode() {
        int h = hash;
        if (h == 0 && value.length > 0) {
            char val[] = value;

            for (int i = 0; i < value.length; i++) {
                h = 31 * h + val[i];
            }
            hash = h;
        }
        return h;
    }
}
```

String의 hashCode() 메서드 구현을 보면 아직 hash 값을 계산한 적이 없을 때 최초 1번만 실제 계산 로직을 수행하고, 이후부터는 해당 값을 그냥 리턴만 하도록 overriding 되어 있습니다.
(계산해놓았던 해시코드를 재사용하는 것입니다.)

String이 불변이기 때문에 이렇게 caching이 가능하다는 이점을 활용할 수 있는 것이다. (값이 변하지 않기 때문에 위와 같이 캐싱해서 사용할 수 있는 것입니다.)

따라서 문자열 개체로 작동할 때 해시 구현을 사용하는 컬렉션의 성능이 향상시킬 수 있습니다. 

<br>

### `4. 보안(Security)`

문자열은 Java 애플리케이션에서 사용자 이름, 암호, 연결 URL, 네트워크 연결 등과 같은 중요한 정보를 저장하는 데 널리 사용됩니다. 클래스를 로드하는 동안 JVM 클래스 로더에서도 광범위하게 사용됩니다.

따라서 String 클래스 보안은 일반적으로 전체 응용 프로그램의 보안에 매우 중요합니다. 

코드를 보면서 왜 그런지 자세히 알아보겠습니다. 

```java
public class Test {
    void criticalMethod(String userName) {
        // perform security checks
        if (!isAlphaNumeric(userName)) {
            throw new SecurityException(); 
        }
	
        // do some secondary tasks
        initializeDatabase();
	
        // critical task
        connection.executeUpdate("UPDATE Customers SET Status = 'Active' " +
        " WHERE UserName = '" + userName + "'");
    }
}
```

처음에 if문을 통해서 필요한 모든 보안 검사를 수행하여 문자열이 영숫자인지 확인하는 작업을 수행합니다. 

만약에 String이 불변 객체가 아니라면 메소드를 호출했던 클라이언트는 String에 대한 참조가 메소드를 호출한 이후에도 남아있습니다. 

따라서 보안 검사를 실시한 이후에도 이 문자열이 안전하다고 보장할 수 없습니다. 메소드를 호출했던 클라이언트가 String에 대한 참조를 계속 가지고 있기 때문에 문자열을 변경할 수 있다는 가능성이 남아있습니다.

이 경우 SQL 주입을 쉽게 수행할 수 있습니다. 따라서 문자열이 변경되면 시간이 지남에 따라 보안 성능이 저하될 수 있습니다.
또한 문자열 사용자 이름이 다른 스레드에 표시될 수 있으며, 이 스레드는 무결성 검사 후 해당 값을 변경할 수 있습니다.

이러한 보안 이슈가 있기 때문에 자바에서는 `String을 불변 객체`로 만들었습니다. 

<br>

# `Reference`

[https://www.baeldung.com/java-string-immutable](https://www.baeldung.com/java-string-immutable)