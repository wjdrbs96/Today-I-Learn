# `해시코드(hashCode)란 무엇인가? `

해시코드를 보기 전에 아래의 코드를 먼저 보고 가겠습니다.  

```java
public class Test {
    public static void main(String[] args) {
        List<String> words = Arrays.asList("Gyunny", " Java", " Study");
        if (words.contains("Gyunny")) {
            System.out.println("Gyunny Java Love");
        }
    }
}
```

List의 `contains()` 메소드의 시간복잡도는 어떻게 될까요? 답은 `O(n)` 입니다. List의 원소들 중에서 하나씩 찾아서 존재 여부를 탐색해야 하기 때문입니다. 

만약 List의 개수가 엄청나게 많다면 탐색할 때 상당히 오래걸릴 것입니다. 이 때 `HashTable`을 사용하면 아주 효과적입니다. 

`HashTable`이 무엇인지는 다들 알고 있을 것입니다. 해시테이블은 hashCode() 메소드를 사용하여 주어진 키에 대한 해시 값을 계산하고 내부적으로 이 값을 사용하여 데이터를 저장하기 때문에 접근할 때 훨씬 더 효율적입니다.

이제 본격적으로 `hashCode()`에 대해서 알아보겠습니다. 

<br>

## `hashCode()는 어떻게 작동하는가?`

해시코드를 간단하게 말하면 `해시 알고리즘에 의해 생성된 정수 값`입니다. 

아래의 해시코드 규약을 보면서 자세히 알아보겠습니다. 

- `equals 비교에 사용되는 정보가 변경되지 않았다면, 애플리케이션이 실행되는 동안 그 객체의 hashCode 메소드는 몇 번을 호출해도 일관되게 항상 같은 값을 반환해야 한다.(단, 애플리케이션을 다시 실행한다면 이 값이 달라져도 상관없다.)`
- `equals(Object)가 두 객체를 같다고 판단했다면, 두 객체의 hashCode는 똑같은 값을 반환해야 한다.`
- `equals(Object)가 두 객체를 다르다고 판단했더라도, 두 객체의 hashCode가 서로 다른 값을 반환할 필요는 없다. 단, 다른 객체에 대해서는 다른 값을 반환해야 해시테이블의 성능이 좋아진다.`

Object 클래스에 의해 정의된 hashCode() 방법은 별개의 개체에 대해 고유한 정수를 반환합니다. (이것은 일반적으로 객체의 내부 주소를 정수로 변환하여 구현됩니다.)

<br>

## `잘못된 해시코드(hashCode) 구현`

```java
public class User {

    private long id;
    private String name;
    private String email;

    // standard getters/setters/constructors
        
    @Override
    public int hashCode() {
        return 1;
    }
        
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (this.getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id 
          && (name.equals(user.name) 
          && email.equals(user.email));
    }
    
    // getters and setters here
}
```

위와 같이 계속 똑같은 `해시코드(hashCode)` 값을 반환하는 메소드를 사용하면 어떻게 될까요?

해시테이블 하나의 버킷 내에 계속 원소들이 쌓여 리스트 형태로 연결될 것입니다. 그러면 해시테이블의 검색 시간복잡도 O(1)의 이점을 누리지 못하고 O(n)으로 늘어나게 됩니다.

<br>

## `해시코드(hashCode) 성능 향상`

```java
public class User {

    private long id;
    private String name;
    private String email;

    @Override
    public int hashCode() {
        return (int) id * name.hashCode() * email.hashCode();
    }
}
```

위와 같은 hashCode() 메소드는 `id`, `name`, `email` 필드를 곱하여 개체의 해시 코드를 계산하기 때문에 이전 것보다 값이 중복될 확률이 훨씬 낮습니다. 

<br>

## `표준 hashCode() 구현`

해시코드 내부 구현을 잘하면 잘 할수록 `HashTable`의 성능을 좋아질 것입니다.(그만큼 충돌이 덜 발생하기 때문에..)

```java
public class User {

    private long id;
    private String name;
    private String email;

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + (int) id;
        hash = 31 * hash + (name == null ? 0 : name.hashCode());
        hash = 31 * hash + (email == null ? 0 : email.hashCode());
        return hash;
    }
}
```

해시코드를 위와 같이 사용하는 것이 가장 보편적입니다. `IntelliJ`를 이용해서 hashCode()를 만들어도 편리하게 알아서 만들어주기 때문에 직접 구현하지 않아도 됩니다.

```
Objects.hash(name, email)
```

그리고 `Objects.hash()` 라는 것도 생겼기 때문에 위와 같이 이용해서 해시코드를 구해도 됩니다. 

<br>

### `숫자 31을 곱해주는 이유는 무엇일까?`

- `31이 홀수이면서 소수(prime)이기 때문입니다. 만약 이 숫자가 짝수이고 오버플로가 발생한다면 정보를 잃게 됩니다. 2를 곱하는 것은 시프트 연산과 같은 결과를 내기 때문입니다.`
- `소수를 곱하는 이유는 전통적으로 그래왔습니다. 그리고 31 숫자는 곱셀을 시프트 연산과 뺄셈으로 대체해 최적화 할 수 있습니다.`
    - `(31 * i는 (i << 5) - i)와 같습니다.`
    

<br>

## `해시 충돌 관리하기`

아무리 좋은 해시 알고리즘을 사용하더라도 충돌이 발생할 수 있습니다. 왜냐하면 같은 객체라면 반드시 같은 해시코드 값을 반환해야 하는 것은 당연하지만 서로 다른 객체임에도 같은 해시코드 값을 가질 수 있습니다. 

그래서 서로 다른 객체임에도 같은 버킷에 저장이 되어 `충돌`이 발생하게 됩니다. (이를 처리하기 위한 다양한 방법론이 존재하며, 각각 장단점이 있습니다. Java의 HashMap은 충돌을 처리하기 위해 별도의 체인 방식을 사용합니다. 자세한 내용은 다음글에서...)

자주 얘기하지만, 충돌이 일어나면 버킷 내부에서 원소들은 `리스트`의 형태로 저장하게 됩니다. 원소 수가 많아질수록 해시 성능이 떨어지게 되겠죠? ? ...

그래서 이를 보완하기 위해 Java 8부터는 버킷 크기가 특정 임계값을 초과하면 연결된 목록이 트리 맵으로 바뀝니다. 이를 통해 시간복잡도 O(n)에서 O(logn)으로 성능을 올릴 수 있습니다. 

<br>

## `예제 코드`

```java
import java.util.Objects;

public class User {

    private long id;
    private String name;
    private String email;

    public User(long id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + (int) id;
        hash = 31 * hash + (name == null ? 0 : name.hashCode());
        hash = 31 * hash + (email == null ? 0 : email.hashCode());
        System.out.println("hashCode() called - Computed hash: " + hash);
        return hash;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id &&
                Objects.equals(name, user.name) &&
                Objects.equals(email, user.email);
    }
}
```

위와 같이 User 클래스의 `hashCode()`, `equals()` 메소드를 오버라이딩 하였습니다. 

```java
import java.util.HashMap;
import java.util.Map;

public class Application {

    public static void main(String[] args) {
        Map<User, User> users = new HashMap<>();
        User user1 = new User(1L, "Gyunny", "Gyunny@naver.com");
        User user2 = new User(2L, "Hyungil", "Hyungil@naver.com");
        User user3 = new User(3L, "Bobae", "Bobae@naver.com");

        users.put(user1, user1);
        users.put(user2, user2);
        users.put(user3, user3);
        
        if (users.containsKey(user1)) {
            System.out.print("User found in the collection");
        }
    }
}
```

그리고 유저 3명을 버킷에 저장한 후에 `containsKey()`를 통해서 유저 한명을 찾는 과정에 대한 예시 코드입니다. 

내부적으로 어떤 과정을 거치고 유저를 찾게될까요?

```
hashCode() called - Computed hash: 657019133
hashCode() called - Computed hash: -1705551490
hashCode() called - Computed hash: -1069061857
hashCode() called - Computed hash: 657019133
User found in the collection
```

예제 코드의 결과를 보면 위와 같습니다. 

먼저 1 ~ 3번째 줄은 `put()` 메소드를 호출할 때 콘솔이 찍히게 됩니다. 왜냐하면 put 메소드의 역할이 key에 해당하는 해시코드를 만들어 버킷의 위치를 지정하는 역할을 하기 때문입니다. 
그래서 키에 해당하는 해시코드 값을 구하게 됩니다. 

그리고 마지막에 `containsKey()` 메소드를 사용하는 것을 볼 수 있습니다. 이 메소드는 매개변수의 key가 현재 존재하는지 안하는지 여부를 알려주는 메소드입니다. 

이 때도 매개변수에 넘어온 값의 해시코드를 구한 후에 해당 버킷으로 간 후에 키가 존재하는지를 찾기 때문에 이 때도 콘솔에 해시코드 결과가 출력되게 됩니다. 
