# `들어가기 전에`

이번 글에서는 `Generic Type erasure`에 대해서 알아보겠습니다. 저에게는 쉽지 않은 내용입니다 ㅠㅠ 하지만 중요한 내용이니 정리를 열심히 해보겠습니다.

[제네릭](https://github.com/wjdrbs96/Gyunny-Java-Lab/blob/master/Java_God/21%EC%9E%A5/%EC%A0%9C%EB%84%A4%EB%A6%AD.md) 은 JDK 1.5에 도입되었습니다. 제네릭을 공부해보셨으면 확실히 버그에 대한 처리와 좀 더 안정적이고 편리하게?
개발을 할 수 있다는 것을 느낄 수 있었을 것입니다.

이렇게 제네릭이 5버전 부터 나왔기에, 하위 버전과의 호환성 유지를 위한 작업이 필요했습니다. 따라서 코드의 호환성 때매 `소거(erasure)` 방식을 사용하게 됩니다.

`제네릭`과 `배열`의 차이를 2가지만 간단하게 알아보면서 `소거`방식에 대해 정리해보겠습니다.

## `첫 번째`

배열은 `공변`이고, 제네릭은 `불공변`입니다. 저번 글에서도 정리한 적이 있는데요 한번 더 정리하고 가겠습니다.

<br>

### `공변이란?`

자기 자신과 자식 객체로 타입 변환을 허용해주는 것입니다.

```
Object[] before = new Long[1];
``` 

그렇기 때문에 위와 같은 문법을 허용시켜 줍니다.

<br>

### `불공변이란?`

`List<String>`과 `List<Object>`가 있을 때 두 개의 타입은 전혀 관련이 없다는 뜻입니다.

```java
public class Test {
    public static void test(List<Object> list) {
        
    }
    public static void main(String[] args) {
        List<String> list = new ArrayList<>();
        list.add("Gyunny");
        test(list);   // 컴파일 에러
    } 
}
```

제네릭이 `불공변`이 아니라면 위의 코드가 컴파일 에러가 발생하지 않을 것입니다. 하지만 `불공변`이라는 자기와 타입이 같은 것만 같다고 인식하는 특징 때문에 컴파일 에러가 발생합니다.

이러한 특성 때문에 제네릭이 컴파일 타임에 타입 안정성을 가지는 장점을 가질 수 있습니다.

<br>

## `두 번째`

배열은 `구체화(reify)`되고, 제네릭은 `비 구체화(non-reify)`가 됩니다. 용어가 벌써 뭔가 싶습니다..

- `구체화 타입(reifiable type): 자신의 타입 정보를 런타임에도 알고 있는 것`
- `비 구체화 타입(non-reifiable type): 런타임에는 소거(erasure)가 되기 때문에 컴파일 타임보다 정보를 적게 가지는 것`

여기서 바로 `소거(erasure)`가 나옵니다. 바로 제네릭은 컴파일 타임에 타입 체크를 한 후에 런타임에는 타입을 지우는 방법을 사용하고 있습니다.

<br>

# `Generic Type erasure란?`

소거란 원소 타입을 컴파일 타입에만 검사하고 `런타임에는 해당 타입 정보를 알 수 없는 것`입니다.
한마디로, 컴파일 타임에만 타입 제약 조건을 정의하고, 런타임에는 타입을 제거한다는 뜻입니다.

<br>

### `Java 컴파일러의 타입 소거`

- `unbounded Type(<?>, <T>)는 Object로 변환합니다.`
- `bound type(<E extends Comparable>)의 경우는 Object가 아닌 Comprarable로 변환합니다.`
- `제네릭 타입을 사용할 수 있는 일반 클래스, 인터페이스, 메소드에만 소거 규칙을 적용합니다.`
- `타입 안정성 보존을 위해 필요하다면 type casting을 넣습니다.`
- `확장된 제네릭 타입에서 다형성을 보존하기 위해 bridge method를 생성합니다.`

예제를 보면서 자세히 알아보겠습니다.

```java
// 컴파일 할 때 (타입 소거 전) 
public class Test<T> {
    public void test(T test) {
        System.out.println(test.toString());
    }
}
```
```java
// 런타임 때 (타입 소거 후)
public class Test {
    public void test(Object test) {
        System.out.println(test.toString());
    }
}
```

`unbouned type`에 대해서는 위와 같이 Object로 바꾸게 됩니다. `bound type`에 대해서는 어떻게 바뀔까요?

```java
public class Test<T extends Comparable<T>> {
    private T data;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
```
```java
public class Test {
    private Comparable data;

    public Comparable getData() {
        return data;
    }

    public void setData(Comparable data) {
        this.data = data;
    }
}
```

`bound type`에 대해서는 Object가 아닌 한정시킨 타입으로 변환이 됩니다. 그리고 `세 번째 규칙`이 있습니다.

세 번째 규칙은 자바 컴파일러가 제네릭의 타입 안정성을 위해 `bridge mothod`도 만들어낼 수 있다는 것입니다.

```java
public class MyComparator implements Comparator<Integer> {
   public int compare(Integer a, Integer b) {
      //
   }
}
```

만약에 위와 같은 예제 코드가 있다고 가정하겠습니다. 그러면 위에서 말한대로 런타임에는 코드가 어떻게 변할까요?

```java
public class MyComparator implements Comparator {
   public int compare(Integer a, Integer b) {
      //
   }
}
```

위와 같이 타입이 소거된 상태로 변할 것입니다. 그리고 Comparator의 compare 메소드의 매개변수 타입은 Object로 바뀔 것입니다.

이러한 메소드 시그니처 사이에 불일치를 없애기 위해서 컴파일러는 런타임에 해당 제네릭 타입의 타임소거를 위한 `bridge method`를 만들어 줍니다.

```java
public class MyComparator implements Comparator<Integer> {
   public int compare(Integer a, Integer b) {
      //
   }

   //THIS is a "bridge method"
   public int compare(Object a, Object b) {
      return compare((Integer)a, (Integer)b);
   }
}
```

그러면 매개변수가 Integer 타입의 compare 메소드를 사용할 수 있게 됩니다. 

 