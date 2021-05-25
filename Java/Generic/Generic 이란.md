# `제네릭(Generic)이란?`

자바의 `제네릭(Generic)`은 형 변환시에 발생할 수 있는 문제들을 사전에 없애기 위해서 만든 것입니다.

아직은 어떤 말인지 와닿지 않을 수 있습니다. 바로 예제 코드를 보면서 제네릭에 대해서 알아보겠습니다.

```java
public class CastingDTO {
    private Object object;

    public void setObject(Object object) {
        this.object = object;
    }

    public Object getObject() {
        return object;
    }
}
```
```java
public class GenericSample {
    public static void main(String[] args) {
        GenericSample sample = new GenericSample();
        sample.checkCastingDTO();
    }
    
    public void checkCastingDTO() {
        CastingDTO dto1 = new CastingDTO();
        dto1.setObject(new String());
        
        CastingDTO dto2 = new CastingDTO();
        dto2.setObject(new StringBuffer());
        
        CastingDTO dto3 = new CastingDTO();
        dto3.setObject(new StringBuilder());
    }
}
```

위와 같이 `CastingDTO` 클래스에 Object 타입의 인스턴스 필드가 존재합니다. 그리고 GenericSample 클래스의 checkCastingDTO 메소드에서 setter를 이용해서
`String`, `StringBuffer`, `StringBuilder`를 매개변수로 보냈습니다.

여기까지는 큰 문제는 없어보입니다. 하지만 `getter`를 이용해서 필드의 값을 가져오려 할 때는 어떻게 될까요? 아래의 코드를 보겠습니다.

```
String temp1 = (String)dto1.getObject();
StringBuffer temp2 = (StringBuffer)dto2.getObject();
StringBuilder temp3 = (StringBuilder)dto3.getObject();
```

getter 메소드의 리턴 타입도 Object 이기 때문에 위와 같이 값을 받아올 때마다 형 변환을 해야합니다.

이러면 빈번한 형변환을 하기 때문에 귀찮기도 하고 헷갈릴 수도 있고, 실수를 할 가능성도 있어보입니다.

```java
public class GenericSample {

    public void checkCastingDTO(CastingDTO dto) {
        Object tempObject = dto.getObject();
        if (tempObject instanceof StringBuilder) {
            System.out.println("StringBuilder");
        } else if (tempObject instanceof StringBuffer) {
            System.out.println("StringBuffer");
        }
    }
}
```

아니면 위와 같이 `instanceof`를 사용해서 타입 체크를 하는 방법도 있습니다.([instanceof란?](https://github.com/wjdrbs96/Today-I-Learn/blob/master/Java/%20Object-oriented/instanceof.md)) 하지만.. 이 방법 또한 코드가 복잡해지고 가독성에도 좋지는 않은 것 같습니다.
`이러한 단점을 보완하기 위해 나온 것이 바로 제네릭(Generic) 입니다.`

가장 처음에 말했던 말이 이제는 어떤 말인지 이해를 할 수 있을 것 같습니다. 제네릭에 대해 좀 더 자세히 알아보겠습니다.

<br>

## `제네릭(Generic) 정의`

- 일반적인 코드를 작성하고, 이 코드를 다양한 타입의 객체에 대하여 `재사용`하는 프로그래밍 기법입니다.
- 타입을 파라미터화해서 `컴파일시 구체적인 타입이 결정`되도록 하는 것입니다. (실행시에 타입에러가 나는거 보다는 컴파일시에 미리 타입을 강하게 체크해서 에러를 사전에 방지해줍니다.)

이제 위의 코드를 `제네릭(Generic)`으로 바꿔보겠습니다.

```java
public class CastingDTO<T> {
    private T object;

    public void setObject(T object) {
        this.object = object;
    }

    public T getObject() {
        return object;
    }
}
```

위와 같이 `Object` 대신에 `T`라는 알파벳으로 바꿨습니다. 이렇게 <> 를 `제네릭 타입`이라고 합니다.

<br>

### `Generic 타입이란?`

- 선언시 클래스 또는 인터페이스 이름 뒤에 <> 부호가 붙습니다. (<> 안에는 클래스와 같이 구체적인 타입을 지정해줘야 합니다. + 어떤 타입이 들어가도 상관 없습니다. )
- <> 사이에는 타입 파라미터가 위치합니다.

<br>

### `타입 파라미터란?`
    - 일반적으로 대문자 알파벳 하나의 문자를 사용합니다.


따라서 그리고 아래의 코드를 보면서 자세히 알아보겠습니다.

```java
public class GenericSample {
    public static void main(String[] args) {
        GenericSample sample = new GenericSample();
        sample.checkCastingDTO();
    }

    public void checkCastingDTO() {
        CastingDTO<String> dto1 = new CastingDTO<>();
        dto1.setObject(new String());

        CastingDTO<StringBuffer> dto2 = new CastingDTO<>();
        dto2.setObject(new StringBuffer());

        CastingDTO<StringBuilder> dto3 = new CastingDTO<>();
        dto3.setObject(new StringBuilder());
    }
}
```

먼저 `CastingDTO<String>` 코드가 보입니다. 위에서 말했던 것처럼 <> 안에는 `구체적인 타입`을 정해서 넣어주면 됩니다.
이제 getter 메소드를 사용해보면 제네릭의 강력함을 느낄 수 있습니다.

```
String test1 = dto1.getObject();
StringBuffer test2 = dto2.getObject();
StringBuilder test3 = dto3.getObject();
``` 

`CastingDTO` 객체를 만들 때 <> 안에 작성했던 타입이 컴파일 타임에 컴파일러가 T 부분을 <>에 작성했던 타입으로 바꿔주기 때문에 형 변환을 하지 않고 편리하게 사용할 수 있습니다.

<br>

### `제네릭 타입의 이름 정하기`

제네릭 타입을 선언할 때 클래스 선언시 <> 안에 어떤 단어가 들어가도 상관 없습니다. 그렇지만 자바에서 정한 규칙은 아래와 같습니다.

- `E : 요소(Element, 자바 컬렉션(Collection)에서 주로 사용됨`
- `K : 키`
- `N : 숫자`
- `T : 타입`
- `V: 값`
- `S, U, V : 두 번째, 세 번째, 내 번째에 선언된 타입`

꼭 위와 같이 사용해야 하는 것은 아니지만 가독성을 좋게 하려면 이렇게 사용하는 것이 좋습니다.

<br>

## `제네릭 제한된 타입 파라미터, 와일드카드`

제네릭을 좀 더 사용하다보면 `?`라는 것이 나오는데 어떤 의미일까요?

먼저 위의 제네릭 코드와 비슷한 코드를 먼저 보겠습니다.

```java
public class WildcardGeneric<W> {
    W wildcard;

    public W getWildcard() {
        return wildcard;
    }

    public void setWildcard(W wildcard) {
        this.wildcard = wildcard;
    }
}
``` 
```java
public class WildCardSample {   
    public void wildcardStringMethod(WildcardGeneric<String> c) {
        String wildcard = c.getWildcard();
        System.out.println(wildcard);
    }
}
```

위의 `wildcardStringMethod` 메소드를 보면 매개변수의 제네릭 타입을 `String`으로 해놨기 때문에 메소드를 호출할 때 `Generic 타입`은 String만 가능합니다.

`하지만 WildcardGeneric<Integer>`와 같이 다른 타입도 매개변수로 동시에 받고 싶다면 어떻게 해야할까요? `이럴 때 사용하는 것이 ? 입니다.`

한번 위의 메소드를 ?로 바꿔보겠습니다.

```java
public class WildCardSample {

    public void wildcardStringMethod(WildcardGeneric<?> c) {
        Object wildcard = c.getWildcard();
    }
}
```  

위와 같이 <> 안에 ?로 바꾸면 모든 타입을 다 매개변수로 받을 수 있습니다. 이렇게 ?로 명시한 타입을 영어로는 `wildcard 타입`이라고 부릅니다.

그러면 저는 여기서 조금 의문이 들었습니다. `WildcardGeneric<?>과 WildcardGeneric<Object>의 차이가 무엇일까` 라는 생각이 났는데 바로 대답을 못하겠습니다..

<br>

## `<Object> vs <?> 의 차이는?`

차이를 알고 나면 약간 허무할 수도 있습니다. 차이를 알아보기 위해 아래의 코드를 보겠습니다.

```java
public class WildCardSample {
    public static void main(String[] args) {
        WildCardSample sample = new WildCardSample();
        WildcardGeneric<Integer> card = new WildcardGeneric<>();
        sample.wildcardStringMethod(card);
    }

    public void wildcardStringMethod(WildcardGeneric<Object> c) {

    }
}
```  

위의 코드를 보면 WildcardGeneric<Integer> 타입을 메소드 호출시에 매개변수에 넣었습니다. 메소드 매개변수의 타입은 WildcardGeneric<Object> 입니다.
`그냥 보면 모든 클래스는 Object의 자식 클래스이기 때문에 이것도 가능할 것 같지만 위의 코드는 컴파일 에러가 발생합니다.`

<br> 

### `컴파일 에러가 발생하는 이유가 무엇일까요?`

제네릭은 `불공변`이기 때문입니다. 즉, 서로 다른 타입 Type1, Typ1가 있을 때, List<Type1>은 List<Type2>의 하위 타입도 상위 타입도 아니라는 뜻입니다.

이러한 제네릭 특징 때문에 위의 코드는 컴파일 에러가 발생하는 것입니다.

<br>

## `제네릭 선언에 사용하는 타입의 범위도 지정할 수 있다.`

이렇게 제네릭 선언에 타입의 범위를 지정하는 것을 `제한된 타입 파라미터` 라고 부릅니다.

예를들어 아래와 같이 숫자의 크기를 비교하는 compare 메소드가 있다고 가정해보겠습니다.

```
public int compare(T t1, T t2) {
    double v1 = t1.doubleValue();
    double v2 = t2.doubleValue();
 
    return Double.compare(v1, v2);
}
```

매개변수 T에는 어떠한 타입도 올 수 있습니다. 그렇기 때문에 T에 만약 비교가 불가능한 타입이 온다면 위의 메소드는 본래의 기능을 수행하기 어려워집니다.

따라서 이러한 상황에서 타입을 제한하는 것이 `제한된 타입 파라미터` 입니다.

```
public <T extends Number> int compare(T t1, T t2) {
    double v1 = t1.doubleValue();
    double v2 = t2.doubleValue();
 
    return Double.compare(v1, v2);
}
```

따라서 위와 같이 `<T extends Number>` 라는 것을 적어주면 T에는 반드시 Number 클래스를 상속 받고 있는 클래스만 올 수 있습니다.

<br>

### `와일드 카드<?>의 제한 종류`

- `<? extends T> 와일드 카드의 상한 제한(upper bound) : T 타입과 T를 상속 받고 있는 타입`
- `<? super T> 와일드 카드의 하한 제한(lower bound) : T 타입과 T의 상위 타입`

<br>

### `메소드를 제네릭하게 선언하는 법`

`제네릭 메소드`는 메소드의 선언부에 적은 제네릭으로 리턴 타입, 파라미터의 타입이 정해지는 메소드입니다.

![image](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FPKxdy%2FbtqG9A6wj3r%2F45am1mQxCFFbdUUvUCrh00%2Fimg.png)

제네릭 메소드의 형태는 위와 같습니다. 지금까지는 이게 무엇인가 싶을 수 있지만 예제 코드를 보면서 차근차근 알아보겠습니다.

```java
public class Student<T> {
  
    static T getName(T name) {   
        return name;
    }
}
```

먼저 메소드를 static 으로 선언하면 제네릭을 사용 할 수 없습니다. 왜냐하면 static 이기 때문에 Student가 인스턴스화 되기 전에 메모리에 올라가는데 이 때 타입 T가 결정되지 않았기 때문에 사용할 수 없는 것입니다.

`이러한 상황에서 제네릭 메소드를 사용할 수 있습니다.`

```java
public class Student<T> {
    
    static <T> T getOneStudent(T id) {
        return id;
    }
}
```

여기서 하나 주의해야 할 점이 있습니다. `Student<T>`에서 T와 메소드에 있는 T는 같은 알파벳이지만 전혀 별개라는 것입니다.

간단하게 말하자면, 클래스에 붙은 T는 `인스턴스 변수`라고 생각할 수 있고, 제네릭 메소드의 T는 `지역 변수`라고 할 수 있습니다.
다시 말하면 지역적으로 제네릭 타입을 사용하고 싶다면 제네릭 메소드를 사용할 수 있습니다.

그리고 `코드의 중복을 막을 수 있습니다.`

```
public static void printAll(ArrayList<? extends Test> list1, ArrayList<? extends Test> list2) {
        // 로직
}
    
public static <T extends Test> void printAll(ArrayList<T> list1, ArrayList<T> list2) {
        // 로직
}
```

만약 제네릭 메소드를 사용하지 않고 `제한된 파라미터`를 적용한다면 첫 번째와 같이 사용할 수 있지만, 제네릭 메소드를 사용한다면 지역변수 T가 되기 때문에 두 번째 처럼 사용해서 코드를 줄일 수 있다는 장점이 있습니다.

제네릭 메소드는 `타입캐스팅 에러의 경우를 제외시킬 수 있기 때문에 훨씬 안전하게 사용할 수`있다는 장점도 가지고 있습니다.


<br>

### `제네릭 타입을 여러 개 사용하기`

```java
public class WildCardSample {
    public <S, T extends Car> void multiGenericMethod(WildcardGeneric<T> c, T addValue, S another) {
        
    }
}
```

제네릭 타입을 여러 개 사용하는 방법은 위와 같습니다. 

이렇게 이번 글에서는 자바 제네릭에 대해서 알아보았는데요. 뭔가 보면 알 거 같지만 또 시간이 지나면 헷갈리는 제네릭.. 두고두고 계속 사용하면서 익숙해지고 응용할 수 있도록 숙달 시켜야 할 거 같습니다.