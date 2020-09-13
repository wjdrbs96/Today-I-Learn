## `equals`메소드란?

```java
public class Object {
    public boolean equals(Object obj) {
        return (this == obj);
    }
}
```

`equals`메소드는 `Object`클래스의 메소드이다. `Object`클래스 내부에 정의된 `equals`메소드를 보면 위와 같다. 내용은 `매게변수`로
객체의 `참조변수`를 가지고 비교한다. 위에서 보면 `==` 연산자가 나오는데 이것의 의미부터 정리하고 가자.

<br>

### `==` 연산자란?

`==` 는 `int, boolean`과 같은 `primitive type`에 대해서는 값을 비교한다. 하지만 `reference Type`에 대해서는 `주소값`을
비교해서 같으면 `true`, 다르면 `false`를 반환해주는 연산자이다. 예를들어 아래의 예시를 봐보자.

```java
public class Test {
    public static void main(String[] args) {
        int a = 3;
        int b = 3;

        if (a == b) {
            System.out.println("3 == 3 으로 같습니다");
        }
        else {
            System.out.println("3 != 3으로 다릅니다");
        }

        String str1 = "ab";
        String str2 = "ab";

        if (str1 == str2) {
            System.out.println("ab == ab으로 같습니다");
        }
        else {
            System.out.println("ab != ab으로 다릅니다");
        }
    }
}
```

```
3 == 3 으로 같습니다
ab == ab으로 같습니다
```


`int`는 `primitive Type`이기 때문에 `값`을 비교해서 `true`가 나오는 것을 알 수 있다. `String`클래스는 `new`연산자를 이용한 것이
아니라 `객체 리터럴 ""`을 이용해서 `상수풀`의 값을 참조하기 때문에 같은 주소 값을 참조하고 있기 때문에 `true`를 반환한다.

```java
public class Test {
    public static void main(String[] args) {
        Test test1 = new Test();
        Test test2 = new Test();

        if (test1 == test2) {
            System.out.println("true");
        } else {
            System.out.println("false");
        }
    }
}
```

따라서 위와 같이 `new`연산자를 이용해서 `Heap`영역에 객체를 만들었을 때는 각자의 메모리 영역이 존재하기 때문에 주소값이 달라 
`false`를 반환하게 된다.

<br>

### 그러면 이제 다시 `equals`메소드에 대해 얘기해보자.

```java
public class Object {
    public boolean equals(Object obj) {
        return (this == obj);
    }
}
```

다시 `Object`클래스 내부의 `equals`메소드를 보면 `==`연산자를 이용해서 `주소값`을 비교하는 코드인 것을 알 수 있다.
또한 가장 최상위 클래스 `Object`에 정의되어 있기 때문에 하위클래스에서 `오버라이딩`해서 사용할 수 있다.

```java
public class Test {
    int a;

    public Test(int a) {
        this.a = a;
    }

    public static void main(String[] args) {
        Test test1 = new Test(10);
        Test test2 = new Test(10);

        if (test1.equals(test2)) {
            System.out.println("test1과 test2는 같다");
        }
        else {
            System.out.println("test1과 test2는 다르다");
        }
    }
}
```

따라서 위의 코드를 보면 `test1`과 `test2`는 `주소값`이 다르기 때문에 `else`문 안에 있는 결과가 나오게 된다.

<br>

### 그러면 아래 코드의 결과는 어떻게 나올까?

```java
public class Test {
    public static void main(String[] args) {
        String str1 = new String("abc");
        String str2 = new String("abc");
        
        if (str1.equals(str2)) {
            System.out.println("같습니다");
        }
        else {
            System.out.println("다릅니다");
        }
    }
}
```

결과는 `같습니다`가 나오게 된다. `아까는 주소값을 비교한다 했는데 주소값이 다른데 왜지?`라고 생각할 수 있다. 다르게 나오는 이유는
`String`클래스에서 `equals 메소드를 오버라이딩`했기 때문에 `같습니다`가 나오는 것이다.

```java
public class String {
    public boolean equals(Object anObject) {
        if (this == anObject) {
            return true;
        }
        if (anObject instanceof String) {
            String anotherString = (String)anObject;
            int n = value.length;
            if (n == anotherString.value.length) {
                char v1[] = value;
                char v2[] = anotherString.value;
                int i = 0;
                while (n-- != 0) {
                    if (v1[i] != v2[i])
                        return false;
                    i++;
                }
                return true;
            }
        }
        return false;
    }
}
```

위와 같이 `String`클래스에서 `equals 메소드를 오버라이딩`했기 때문에 `주소값`이 아니라 `내용 값`으로 비교하여 `true`, `false`를
반환하게 되는 것이다.


```java
public class Test {
    int id;

    public Test(int id) {
        this.id = id;
    }

    public boolean equals(Object obj) {
        if (obj instanceof Test) {
            return id == ((Test)obj).id;
        }
        else {
            return false;
        }
    }

    public static void main(String[] args) {
        Test test1 = new Test(123);
        Test test2 = new Test(123);

        if (test1 == test2) {
            System.out.println("Test1과 Test2는 주소가 같다");
        }
        else {
            System.out.println("Test1과 Test2는 주소가 다르다");
        }

        if (test1.equals(test2)) {
            System.out.println("test1과 test2의 내용은 같다");
        }
        else {
            System.out.println("test1과 test2의 내용은 다르다");
        }
    }
}
```

```
Test1과 Test2는 주소가 다르다
test1과 test2의 내용은 같다
```

위와 같이 `equals`메소드를 `주소`가 아니라 `내용`으로 비교하게 직접 `오버라이딩`해서 사용할 수도 있다.

<br>

### HashCode 메소드란?

`해싱(hashing)`기법에 사용되는 `해시함수(hash function)`를 구현한 것이다. 해시함수는 찾고자하는 값을 입력하면, 그 값이 저장된
위치를 알려주는 `해시코드(hash code)`를 반환한다.

<br> 

일반적으로 해시코드가 같은 두 객체가 존재하는 것은 가능하지만, `Object클래스`에 정의된 `hashcode메소드`는 객체의 주소값을 이용해서
해시코드를 만들어 반환하기 때문에 서로 다른 두 객체는 결코 같은 해시코드를 가질 수 없다. <br>

위에서 본 것처럼 클래스의 인스턴스 변수 값으로 객체의 같고 다름을 판단해야 한다면 `equals`메소드 뿐만 아니라 `hashCode`메소드도 적절히
`오버라이딩`해야 한다. 같은 객체라면 `hashCode`의 값도 같아야 하기 때문이다.

```java
public class Test {
    public static void main(String[] args) {
        String str1 = new String("abc");
        String str2 = new String("abc");

        System.out.println(str1.equals(str2));
        System.out.println(str1.hashCode());
        System.out.println(str2.hashCode());
        System.out.println(System.identityHashCode(str1));
        System.out.println(System.identityHashCode(str2));
    }
}
```

```
true
96354
96354
1846274136
1639705018
```

<br>

### hashCode값이 다르게 나오는 이유는 무엇일까?

String 클래스는 `hashCode`메소드를 문자열이 같으면, 동일한 해시코드를 반환하도록 `오버라이딩`하였다. 그리고
`System.identityHashCode()`는 Object클래스의 `hashCode`메소드처럼 객체의 주소값으로 해시코드를 생성하기 때문에 모든 객체에
대해 항상 다른 해시코드값을 반환할 것을 보장한다. `str1과 str2과 해시코드는 같지만 서로 다른 객체라는 것을 알 수 있다.`