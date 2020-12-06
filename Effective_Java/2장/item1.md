# `아이템1: 생성자 대신 정적 팩토리 메소드를 고려하라`

클래스의 인스턴스를 얻는 전통적인 방법은 `public 생성자를 이용하는 것`입니다. 

```java
public class Test {
    private String name;

    public Test(String name) {
        this.name = name;
    }

    public static void main(String[] args) {
        Test test = new Test("Gyunny");
    }
}
```

위의 코드를 보면 `Test 클래스`의 생성자를 이용해서 객체를 생성한 것을 볼 수 있는데 이것이 `public 생성자를 이용해서 객체를 만든 예입니다.`
그렇지만 객체를 만들 때 프로그래머는 생성자의 매게변수만을 보고 어떤 객체가 반환될 것인지 예측하기가 쉽지 않습니다. 

<br>

그래서 이러한 방법 말고 모든 프로그래머가 알아둬야 할 기법이 하나 더 있습니다. 바로 `클래스는 생성자와 별도로 정적 팩토리 메소드(static factory method)를 제공할 수 있다는 것입니다.`

```java
public class Test {
    private String name;

    public Test(String name) {
        this.name = name;
    }

    public static Test withName(String name) {
        return new Test(name);
    }

    public static void main(String[] args) {
        Test test = withName("Gyunny");
    }
}
```

위의 코드와 같이 `정적 팩토리 메소드`를 사용하는 방법이 있습니다. 


<br>

이번에는 책에서 나온 예시를 들어보겠습니다. 아래의 코드는 `Boolean 클래스`에서 `valueOf()` 메소드의 코드만을 가져왔습니다.

```java
public final class Boolean implements java.io.Serializable,Comparable<Boolean> {

    public static final Boolean TRUE = new Boolean(true);

    public static final Boolean FALSE = new Boolean(false);

    public static Boolean valueOf(boolean b) {
        return (b ? TRUE : FALSE);
    }
}
```

`valueOf()메소드`는 기본 타입인 boolean 값을 받아 Boolean 객체 참조로 변환해주는 메소드입니다. 이러한 메소드를 `정적 팩토리 메소드`라고 합니다.
이렇게 클래스는 public 생성자 대신 `정적 팩토리 메소드를` 제공할 수 있습니다. 

<br>

이 방식에는 `장점`과 `단점`이 모두 존재하는데 각각에 자세히 알아보겠습니다.

<br>

## `먼저 장점이 다섯 가지가 있는데 하나씩 정리해보겠습니다.`

### `장점1 : 이름을 가질 수 있다.`

생성자에 넘기는 매게변수의 이름만으로는 반환될 객체의 특성을 제대로 설명하기가 쉽지 않습니다. 반면에 정적 팩토리 메소드는 이름만 잘 지으면 반환될 객체의 특성을 쉽게 잘 표현할 수 있습니다. 

```java
public class BigInteger extends Number implements Comparable<BigInteger> {

    // 생성자
    public BigInteger(int bitLength, int certainty, Random rnd) {
        // logic
    }

    // 정적 팩토리 메소드
    public static BigInteger probablePrime(int bitLength, Random rnd) {
        // logic
    }
}
```

위의 코드를 보았을 때, `생성자`와 `정적 팩토리 메소드`중 `값이 소수인 BigInteger`를 반환한다는 의미에 더 가까운 것을 뽑으라면 저도 `정적 팩토리 메소드`에 한표를 줄 것 같습니다. 
이렇게 이름을 가질 수 있다는 것이 장점이라고 할 수 있습니다.

<br>

그리고 생성자는 `하나의 시그니처로는 생성자를 하나만 만들 수 있습니다.` 

```java
public class Test {
    private String name;
    private String address;

    public Test(String name) {
        this.name = name;
    }
    
    // 오류
    public Test(String address) {
        this.address = address;
    }
}
```

위의 코드를 보면 매게 변수가 1개인(name) 생성자가 이미 존재하기 때문에 매게 변수가 1개인(address) 생성자를 하나 더 만들었을 때 오류가 발생하는 것을 볼 수 있습니다.
(매게변수가 1개인 생성자가 이미 존재하기 때문입니다.)

<br>

물론 입력 매게변수들의 순서를 다르게 한 생성자를 새로 추가하는 식으로 이 제한을 피해볼 수도 있지만, 이러한 API를 사용하는 개발자는 각 생성자가 어떤 역할을 하는지 정확히 알기 어려워지게 됩니다. 

<br>

`이름을 가질 수 있는 정적 팩터리 메소드에는 이러한 제약이 없습니다.` 한 클래스에 시그너처가 같은 생성자가 여러 개 필요할 것 같으면, 생성자를 정적 팩토리 메소드로 바꾸고 각각의 차이를 잘 드러내는 이름을 지어주면 됩니다.

```java
public class Test {
    private String name;
    private String address;
    
    public static Test withName(String name) {
        Test test = new Test();
        test.name = name;
        return test;
    }
    
    public static Test withAddress(String address) {
        Test test = new Test();
        test.address = address;
        return test;
    }

    public static void main(String[] args) {
        Test testName = withName("Gyunny");
        Test testAddress = withAddress("Address");
    }
}
```

코드의 예시를 들면 위와 같이 `name`, `address`에 해당하는 `정적 팩토리 메소드`를 이용해서 작성할 수 있습니다. 

<br>

### `장점2: 호출될 때마다 인스턴스를 새로 생성하지는 않아도 된다.`

이러한 장점으로 `불변 클래스`는 인스턴스를 미리 만들어 놓거나 새로 생성한 인스턴스를 캐싱하여 재활용하는 식으로 불필요한 객체 생성을 피할 수 있습니다.

<br>

대표적인 예로는 위에서 보았던 `Boolean.valueOf(boolean)` 메소드가 있는데 이 메소드는 객체를 아예 생성하지 않습니다. 
위의 `Boolean` 클래스를 다시 보면 `TRUE`, `FALSE`를 상수로 정의해놓고 메소드에서 이것을 반환하는 것을 볼 수 있습니다. 
`따라서 객체 생성 비용이 큰 객체가 자주 요청되는 상황이라면 성능을 상당히 끌어올릴 수 있습니다.`

<br>

정적 팩토리 방식의 클래스는 언제 어느 인스턴스를 살아 있게 할지를 철저히 통제할 수 있습니다. 이런 클래스를 `인스턴스 통제 클래스` 라고 합니다.

<br>

### `그렇다면 인스턴스를 통제하는 이유는 무엇일까요?`

- 인스턴스를 통제하면 클래스를 `싱글톤`으로, `인스턴스화 불가`로 만들 수도 있습니다.
- 불변 값 클래스에서 동치인 인스턴스가 단 하나뿐임을 보장할 수 있습니다. (a == b일 때만 a.equals(b) 성립)
- 인스턴스 통제는 `플라이웨이트 패턴`의 근간이 되며, 열거 타입은 인스턴스가 하나만 만들어짐을 보장합니다.  

```java
public class Test {
    private String name;
    
    private static final Test GOOD_STUDY = new Test();
    
    public static Test Test_goodStudy() {
        return GOOD_STUDY;
    }

    public static void main(String[] args) {
        Test test = Test_goodStudy();
    }
}
```

위와 같이 `싱글턴`을 이용해서 메소드를 여러 번 호출해도 객체가 한번만 만들어지게 만들 수도 있습니다. item3, 4에서 좀 더 자세히 살펴보겠습니다.

<br>

### `플라이웨이트 패턴이란?`

> 플라이웨이트 패턴 (Flyweight pattern) : 데이터를 공유하여 메모리를 절약하는 패턴, 공통으로 사용되는 객체는 한번만 사용되고 Pool에의해서 관리, 사용된다.
> (JVM의 String Pool에서 같은 String이 잇는지 먼저 찾는다. [불변객체 String])

<br>

## `장점3 : 반환 타입의 하위 타입 객체를 반환할 수 있는 능력이 있다.`

이것은 자바의 `다형성`의 특징을 이용하기 때문에 엄청나게 유연한 코딩을 할 수 있습니다. 

```java
public interface Type {
    static Type getAType() {
        return new AType();
    }

    static Type getBType() {
        return new BType();
    }
}

class AType implements Type {
}

class BType implements Type {
}
```

위와 같이 `Type` 인터페이스가 있고, 이것을 구현하는 `AType`, `BType` 클래스가 있다고 가정해봅시다. 그러면 `getAType()` 메소드와 `getBType()`의 메소드의 
반환 타입은 인터페이스인 `Type` 이지만, 반환하고 있는 것은 인터페이스의 하위 클래스인 것을 볼 수 있습니다. 
`이는 인터페이스를 정적 팩토리 메소드의 반환 타입으로 사용하는 인터페이스 기반 프레임워크를 만드는 핵심 기술이기도 합니다. `


<br>

그러면 사용자 입장에서 해당 API를 사용할 때 `Type.getAType()`으로 호출해서 사용할 것입니다. 그러면 인터페이스를 구현하고 있는 클래스를 노출시키지 않을 수 있고, 사용자 입장에서도 반환 된 클래스가 
어떤 클래스인지 굳이 찾아보지 않아도 되는 장점도 있습니다. 

<br>

그리고 자바 컬렉션 프레임워크는 핵심 인터페이스들에 수정 불가나 동기화 등의 기능을 덧붙인 총 45개의 유틸리티 구현체를 제공합니다. 
이 구현체를 `java.util.Collections` 라는 클래스를 굳이 만들지 않고도 인터페이스 자체에서 정적 팩토리 메소드를 통해 얻도록 구현 해놓았습니다.

<br>

이러한 방법으로 컬렉션 프레임워크는 45개의 클래스를 공개하지 않기 때문에 API를 작게 만들 수 있었습니다. 그러면 사용자의 입장에서도 알아야 하는 개념의 수도 적어지고, 난이도도 낮아지는 장점이 생기게 되었습니다.
`명시한 인터페이스대로 동작하는 객체를 얻을 것임을 알기에 굳이 별도로 문서를 찾아가며 실제 구현 클래스가 무엇인지 알아보지 않아도 됩니다.`

<br> 

### `코드로 예를 들어보겠습니다.`

```java
public interface List<E> extends Collection<E> {
    static <E> List<E> of() {
        return (List<E>) ImmutableCollections.ListN.EMPTY_LIST;
    }
}
```

자바 9의 List 인테페이스의 of() 메소드는 인터페이스를 반환하는 정적 팩토리 메소드입니다. 클라이언트 입장에서는 반환되는 클래스가 어떤 것인지 알 필요가 없이
그냥 of 메소드의 기능이 무엇인지만 알고 `List.of()`와 같이 사용하면 되는 것입니다. 

<br> 

`인터페이스에 정적 메소드를 사용할 수 있는 것은 자바8 부터 입니다.` 그래서 자바8 이전에는 인터페이스의 유사 클래스를 만들어 그 안에 정적 메소드를 정의하는 방식으로 우회했습니다. 

```java
public class Collections {
     private Collections() { }

     public static final List EMPTY_LIST = new EmptyList<>();

     @SuppressWarnings("unchecked")
     public static final <T> List<T> emptyList() {
         return (List<T>) EMPTY_LIST;
     }
}
```

위의 코드 `Collections` 클래스의 `emptyList()` 메소드가 대표적인 예시입니다. (생성자의 접근 지정자를 private로 해놓음으로서 객체 생성을 막아 놓은 것을 알 수 있습니다.)

<br>

## `장점4 : 입력 매게변수에 따라 매번 다른 클래스의 객체를 반환할 수 있다.`

반환 타입의 하위 타입이기만 하면 어떤 클래스의 객체를 반환하든 상관없습니다. 
 
```java
public class Foo {

    public static Foo getFoo(boolean flag) {
        return flag ? new TestFoo() : new BarFoo();
    }

    static class BarFoo extends Foo {
    }

    static class TestFoo extends Foo {
    }

    public static void main(String[] args) {
        Foo foo1 = Foo.getFoo(true);     // TestFoo
        Foo foo2 = Foo.getFoo(false);    // BarFoo
    }
}
```

위의 코드를 보면 매게변수 `flag`에 따라 반환 타입이 달라지는 것을 볼 수 있습니다. 이렇게 정적 팩토리 메소드를 사용하면 유연하게 구조를 갖출 수 있습니다. 

<br>

또 다른 예를들면, `EnumSet 클래스`는 public 생성자 없이 오직 정적 팩토리 메소드 `allOf()`, `of()`등을 제공합니다. 
그런데 `allOf()`와 `of()` 모두 내부 코드를 보면 `noneOf()`메소드를 호출하는 것을 볼 수 있습니다. `noneOf()` 메소드를 자세히 살펴보겠습니다. 

```java
public abstract class EnumSet<E extends Enum<E>> extends AbstractSet<E>
    implements Cloneable, java.io.Serializable
{
    public static <E extends Enum<E>> EnumSet<E> allOf(Class<E> elementType) {
        EnumSet<E> result = noneOf(elementType);
        result.addAll();
        return result;
    }

    public static <E extends Enum<E>> EnumSet<E> noneOf(Class<E> elementType) {
        Enum<?>[] universe = getUniverse(elementType);
        if (universe == null)
            throw new ClassCastException(elementType + " not an enum");

        if (universe.length <= 64)
            return new RegularEnumSet<>(elementType, universe);
        else
            return new JumboEnumSet<>(elementType, universe);
    }

    public static <E extends Enum<E>> EnumSet<E> of(E e1, E e2) {
        EnumSet<E> result = noneOf(e1.getDeclaringClass());
        result.add(e1);
        result.add(e2);
        return result;
    }
}
```

위의 코드의 `noneOf()` 메소드를 보면 원소가 64개 이하면 원소들을 long 변수 하나로 관리하는 `RegularEnumSet`의 인스턴스를, 65개 이상이면 long 배열로 관리하는 `JumboEnumSet`의 인스턴스를 반환하는 것을 볼 수 있습니다.

<br>

이 두 객체 타입은 노출되지 않고 감춰져 있기 때문에 사용자는 이에 대해 알 필요가 없으며 추후에 새로운 타입을 만들거나 기존 타입을 없애도 문제없이 사용할 수 있습니다.
(`EnumSet의 하위타입이기만 하면 되는 것이다.`)

<br>

## `장점5 : 정적 팩토리 메소드를 작성하는 시점에는 반환할 객체의 클레스가 존재하지 않아도 된다.`

장점5의 유연함은 `서비스 제공자 프레임워크를 만드는 근반이 됩니다.` 대표적인 서비스 제공자 프레임워크로는 JDBC가 있습니다. 


### `service provider framework의 컴포넌트`
   
- 서비스 인터페이스(service interface): 구현체의 동작을 정의합니다.
    - `JDBC) Connection`
  
- 제공자 등록 API (provider registration API): provider가 구현체를 등록할 때 사용합니다.
    - `JDBC) DriverManager.registerDriver`
    
- 서비스 접근 API(service access API): 클라이언트가 서비스의 인스턴스를 얻을 때 사용 클라이언트가 이 service access API 사용시 원하는 구현체 조건을 명시할 수 있습니다. 
    - `JDBC) DriverManager.getConnection`

- 서비스 제공자 인터페이스(service provider interface): service interface의 인스턴스를 생성하는 팩토리 객체를 설명해줍니다.
    - `JDBC) Driver` 
   

<br>

지금까지는 장점 5개를 알아보았으니 이번에는 단점 2개를 알아보겠습니다. 

## `단점1 : 상속을 하려면 public이나 protected 생성자가 필요하니 정적 팩토리 메소드만 제공하면 하위 클래스를 만들 수 없다.`

위에서 보았던 `Collections` 클래스를 보면 생성자의 접근제어자가 `private`인 것을 보았습니다. 그렇기 때문에 이 클래스는 누군가의 부모 클래스가 될 수 없습니다. 

![스크린샷 2020-12-04 오후 4 31 19](https://user-images.githubusercontent.com/45676906/101134797-43eebb00-364e-11eb-83fd-8582d98b495e.png)


<br>

## `단점2 : 정적 팩토리 메소드는 프로그래머가 찾기 어렵다.`

일반적으로 자바 API Docs를 보면 생성자는 상단에 있기 때문에 찾기가 쉽습니다. 하지만 정적 팩토리 메소드는 다른 메소드와 구분 없이 함께 보여주고 
사용자가 정적 팩토리 메소드 방식 클래스를 인스턴스화할 방법을 알아내야 하는데 찾기 쉽지 않다는 단점이 있습니다.  

<br>

## `정적 팩토리 메소드에 사용하는 명명 방식들`

- ### `from`: 매게변수를 하나 받아서 해당 타입의 인스턴스를 반환하는 형변환 메소드
    - ex) `Date d = Date.from(instant);`
    
- ### `Of`: 여러 매게변수를 받아 적합한 타입의 인스턴스를 반환하는 집계 메소드
    - ex) `Set<Rank> faceCards = EnumSet.of(JACK, QUEEN, KING);`
    
- ### `valueOf`: from과 of의 더 자세한 버전
    - ex) `BigInteger prime = BigInteger.valueOf(Integer.MAX_VALUE);`;
    
- ### `instance` 혹은 `getInstance`: 매게변수를 받는다면 매게변수로 명시한 인스턴스를 반환하지만, 같은 인스턴스임을 보장하지는 않는다. 
    - ex) `StackWalker luke = StackWalker,getInstance(options);`
    
- ### `crete` 혹은 `newInstance`: instance 혹은 getInstance와 같지만, 매번 새로운 인스턴스를 생성해 반환함을 보장한다. 
    - ex) `Object newArray = Array.newInstance(classObject, arrayLen);`
    
- ### `newType`: newInstance와 같으나, 생성할 클래스가 아닌 다른 클래스에 팩토리 메소드를 정의할 때 쓴다. 'Type'은 팩토리 메소드가 반환할 객체의 타입이다.
    - ex) `BufferReader br = Files.newBufferedReader(path);`
    
- ### `type`: getType과 newType의 간결한 버전
    - ex) `List<Complaint> litany = Collections.list(legacyLitany);`
    

<br>

## `핵심 정리`

> 정적 팩토리 메소드와 public 생성자는 각자의 쓰임새가 있으니 상대적인 장단점을 이해하고 사용하는 것이 좋다. 그렇다고 하더라도 정적 팩토리를 사용하는 게 유리한 경우가 더 많으므로 무작정 public 
> 생성자를 제공하는 습관이 있다면 고치자.

