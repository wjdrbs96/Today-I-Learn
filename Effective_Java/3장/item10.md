# `아이템10 : equals는 일반 규약을 지켜 재정의하라`

`equals 메소드`는 재정의하기 쉬워 보이지만 곳곳에 함정이 도사리고 있어서 자칫하면 끔찍한 결과를 초래합니다. 
문제를 회피하는 가장 쉬운 길은 아예 재정의하지 않는 것입니다. 

<br>

## `equals 메소드를 재정의 하면 안되는 경우`

- ### 각 인스턴스가 본질적으로 고유하다
    - 값을 표현하는 게 아니라 동작하는 개체를 표현하는 클래스가 여기 해당합니다. ex) Thread, Bean으로 등록하는 객체(Repository, Server, Controller)가 있을 것 같습니다.
    
- ### 인스턴스의 '논리적 동치성(logical equality)'을 검사할 일이 없다. 
    - java.util.regex.Pattern은 Object의 기본 equals만으로 해결됩니다.
    
- ### 상위 클래스에서 재정의한 equals가 하위 클래스에도 딱 들어맞는다.
    - 같은 특징을 갖는(`List-AbstractList`, `Set-AbstractSet`) 경우는 상위 클래스에서 정의한 equals를 상속받아 그대로 쓰고 있습니다.
    
- ### 클래스가 private이거나 package-private이고 equals 메소드를 호출할 일이 없다.
    - ```java
        public class Test {
            @Override
            public boolean equals(Object obj) {
                throw new AssertionError();
            }
        }
      ```
    - equals가 실수로 호출되는 걸 막고 싶다면 위와 같이 사용하면 됩니다. 
    
<br>

그러면 이번에는 equals를 재정의해야 하는 상황에 대해서 알아보겠습니다. 

<br>

## `equals를 재정의 해야하는 경우`

`객체 식별성(object identity; 두 객체가 물리적으로 같은가)`이 아니라 `논리적 동치성`을 확인 해야 하는데, 상위 클래스의 equals가 논리적 동치성을 비교하도록 재정의되지 않았을 때 입니다.

```java
public class Fruit {
    private String name;

    public Fruit(String name) {
        this.name = name;
    }

    public static void main(String[] args) {
        Fruit fruit1 = new Fruit("Orange");
        Fruit fruit2 = new Fruit("Orange");
    }
}
```

위와 같이 name 필드의 값으로 두 객체의 동치성을 확인해야 할 때 equals를 재정의해서 사용해야 합니다.
그리고 일반적으로 값 클래스(Integer, String)가 비교를 할 때는 객체가 같은지가 아니라 값이 같은지를 알고 싶어할 것입니다.

하지만 값 클래스라 해도, 값이 같은 인스턴스가 둘 이상 만들어지지 않음을 보장하는 인스턴스 통제 클래스라면 equals를 재정의하지 않아도 됩니다. 
ex) `enum 클래스 => 어짜피 논리적으로 같은 인스턴스가 2개 이상 만들어지지 않으니 논리적 동치성과 객체 식별성이 사실상 똑같은 의미가 됩니다.`
    
<br>

## `Equals 메소드를 재정의할 때는 반드시 일반 규약을 따라야 합니다.`

다음은 Object 명세에 적힌 규약입니다. 

> equals 메서드는 동치관계 (equivalence relation)를 구현하며, 다음을 만족한다. 

<br>

### `반사성(reflexivity)`

- null이 아닌 모든 참조 값 x에 대해 x.equals(y)는 true이다.
    
```java
import java.util.ArrayList;
import java.util.List;

public class Fruit {
    private String name;

    public Fruit(String name) {
        this.name = name;
    }

    public static void main(String[] args) {
        List<Fruit> list = new ArrayList<>();
        Fruit f = new Fruit("Apple");
        list.add(f);
        System.out.println(list.contains(f));  // true
    }
}
```    

`list.contains(f)`의 결과가 false라면 반사성을 만족하지 못하는 경우입니다. 

<br>

### `대칭성(symmetry)`

- null이 아닌 모든 참조 값 x, y에 대해, x.equals(y)가 true면 y.equals(x)도 true이다. 

### `대칭성을 위반한 클래스`

```java
public class CaseInsensitiveString {
    private String s;

    public CaseInsensitiveString(String s) {
        this.s = Obejcts.requireNonNull(s);
    }

    @Override
    public boolean equals(Object o){
        if (o instanceof CaseInsensitiveString)
            return s.equalsIgnoreCase(((CaseInsensitiveString) o).s);
        if (o instanceof String) // 한방향으로만 작동한다.
            return s.equalsIgnoreCase((String) o);
        return false;
    }

    public static void main(String[] args) {
        CaseInsensitiveString cis = new CaseInsensitiveString("Gyunny");
        String s = "Gyunny";

        System.out.println(cis.equals(s));  // true
        System.out.println(s.equals(cis));  // false
    }
}
```

CaseInsensitiveString 클래스는 equals 메소드에 instanceof를 통해 String 클래스로 형변환을 하기에 `cis.equals(s)`가 true가 나올 수 있습니다.
하지만 String 클래스에 재정의한 equals 메소드는 CaseInsensitiveString가 존재하지 않기 때문에 false가 나오게 됩니다.


<br>

### `대칭성을 만족하게 수정`

```java
public class CaseInsensitiveString {
    private String s;

    public CaseInsensitiveString(String s) {
        this.s = Obejcts.requireNonNull(s);
    }

    @Override
    public boolean equals(Object o){
        return o instanceof CaseInsensitiveString &&
                ((CaseInsensitiveString) o).s.equalsIgnoreCase(s);
        // String에 대한 instanceof 부분을 빼고 구현한다.
    }

    public static void main(String[] args) {
        CaseInsensitiveString cis = new CaseInsensitiveString("Gyunny");
        String s = "Gyunny";

        System.out.println(cis.equals(s));  // false
        System.out.println(s.equals(cis));  // false
    }
}
```

<br>

### `추이성(transitivity)`

- null이 아닌 모든 참조 값 x, y, z에 대해, x.equals(y)가 true이고, y.equals(z)도 true면, x.equals(z)도 true다.

<br>

### `일관성(consistency)`

`null`이 아닌 모든 참조 값 x, y에 대해 `x.equals(y)`를 반복해서 호출하면 항상 true를 반환하거나 항상 false를 반환한다.

- 두 객체가 같다면 수정되지 않는 한 영원히 같아야 한다는 뜻입니다. 반면 불변 객체는 한번 다르면 끝까지 달라야 합니다. 

<br>

### `null이 아니다.`

`null`이 아닌 모든 참조 값 x에 대해서 `x.equals(null)`은 false 이다.

<br>

## `양 질의 equals 메소드를 구현하는 방법`

- ### == 연산자를 사용해 입력이 자기 자신의 참조인지 확인한다. 
    - 자기 자신의 참조라면 true를 반환해야 합니다.
    
- ### instanceof 연산자로 입력이 올바른 타입인지 확인한다. 
    - 그렇지 않다면 false를 반환해야 합니다.
    
- ### 입력을 올바른 타입으로 형변환 한다.
    - 위에서 instanceof 검사를 했기 때문에 이 단계는 100% 성공합니다.
    
- ### 입력 객체와 자기 자신의 대응되는 '핵심' 필드들이 모두 일치하는지 하나씩 검사한다.
    - 모든 필드가 일치하면 true를, 하나라도 다르면 false를 반환합니다.

<br>

## 코드로 적용해보면?

위에서 살펴본 규악들을 준수하여 코드로 적용을 해보겠습니다. 

```java
public final class phoneNumber {
    private final short areaCode, prefix, lineNum;

    @Override
    public boolean equals(Object o) {
        if( o == this) {
            return true;
        }

        if( o == null) {
            return false;
        }

        if(!(o instanceof PhoneNumber)) {
            return false;
        }

        PhoneNumber pn = (PhoneNumber)o;
        return pn.lineNum == lineNum && pn.prefix == prefix
                        && pn.areaCode == areaCode;
    }
}
```

- equals를 재정의할 땐 hashCode도 반드시 재정의해야 합니다.
- 필드들의 동치성만 검사해도 equals 규약을 어렵지 않게 지킬 수 있습니다. 예를들어, File 클래스라면, 심볼릭 링크를 비교해 같은 파일을 가리키는지를 확인하려 하면 안됩니다. 
- Object 외의 타입을 매게변로 받는 equals 메소드는 선언하면 안됩니다. 

```java
public class Test {
    @Override
    public boolean equals(MyClass obj) {
        // logic
    }
}
```

<br>

### `equals가 메소드를 잘못 재정의된 사례`

`java.sql` 패키지의 Timestamp 클래스의 equals 메소드를 볼 수 있습니다. 이 클래스는 java.util 패키지의 Date 클래스를 상속하여 만들어진 클래스인데
두 클래스의 equals 메소드를 살펴보면 아래와 같습니다. 

```java
public class Timestamp extends java.util.Date {

    public boolean equals(Timestamp ts) {
        if (super.equals(ts)) {
            if  (nanos == ts.nanos) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
    public boolean equals(java.lang.Object ts) {
        if (ts instanceof Timestamp) {
            return this.equals((Timestamp)ts);
        } else {
            return false;
        }
    }
}
``` 

```java
public class Date {
    public boolean equals(Object obj) {
        return obj instanceof Date && getTime() == ((Date) obj).getTime();
    }
}
```

그리고 아래의 코드를 실행하면 어떤 결과가 나올까요?

```java
import java.sql.Timestamp;
import java.util.Date;

public class Test {
    public static void main(String[] args) {
        Timestamp timestamp = new Timestamp(0L);
        Date date = new Date(timestamp.getTime());

        System.out.println(timestamp.equals(date));  // false
        System.out.println(date.equals(timestamp));  // true
    }
}
```

Timestamp의 equals 메서드에서는 instanceof 연산자로 인해 false가 됩니다. 
물론 타입 검사없이 형변환을 한다고 하더라도 nanos 값을 검사로 인해 false가 반환될 것입니다. Date의 equals 메서드에서는 시간이 같은지만 검사하므로 true가 됩니다.

<br>

## `핵심 정리`

> 꼭 필요한 경우가 아니면 equals를 재정의하지 말자. 많은 경우에 Object의 equals가 여러분이 원하는 비교를 정확히 수행해준다.
> 재정의해야 할 때는 그 클래스의 핵심 필드 모두를 빠짐없이, 다섯 가지 규약을 확실히 지켜가며 비교해야 한다. 

