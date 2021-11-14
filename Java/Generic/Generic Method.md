## `제너릭 메소드`

제네릭 메소드는 메소드의 선언 부에 적은 제네릭으로 리턴 타입, 파라미터의 타입이 정해지는 메소드입니다.

![generic](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FPKxdy%2FbtqG9A6wj3r%2F45am1mQxCFFbdUUvUCrh00%2Fimg.png)

제너릭에 대한 예시를 보면서 이해해보겠습니다. 

```java
public class Student<T> {
    private static T name;
}
```

먼저 static 변수는 제너릭은 사용할 수 없습니다. 왜냐하면 Student 클래스가 인스턴스가 되기 전에 static은 메모리에 올라가는데 이 때 name의 타입인 T가 결정되지 않기 때문에 위와 같이 사용할 수 없는 것입니다.

<br>

```java
public class Student<T> {
    private static T name;
}
```

static 메소드에도 제너릭을 사용하면 에러가 발생하는 이유는 static 변수와 마찬가지로 Student 클래스가 인스턴스화 되기 전에 메모리에 올라가는데 T의 타입이 정해지지 않았기 때문입니다.

<br> <br>

## `제너릭 메소드는 static이 가능하다.`

위에서 에러가 발생했는데 어떻게 가능한 것일까요? 이럴 때 사용하는 것이 제너릭 메소드입니다. 제너릭 메소드는 호출 시에 매게 타입을 지정하기 때문에 static이 가능합니다.

```java
public class Student<T> {
    
    public static <T> T getOneStudent(T id) {
        return id;
    }
}
```

사용법은 위와 같이 return type 앞에 제너릭을 사용해주면 됩니다. 여기서 주의해야 할 점은 Student 클래스에 지정한 제너릭 타입 <T>와 제너릭 메소드에 붙은 <T>는 같은 T를 사용하더라도 전혀 별개라는 것입니다.

클래스에 표시하는 <T>는 인스턴스 변수라고 생각하겠습니다. 인스턴스가 생성될 때 마다 지정되기 때문입니다. 그리고 제너릭 메소드에 붙은 T는 지역변수를 선언한 것과 같다고 생각하겠습니다. (메소드의 붙은 모든 T는 클래스에 붙은 T와 다릅니다.)

<br> <br>

## `그러면 아래의 경우는 어떻게 되는것인가?`

```java
public interface List<E> extends Collection<E> {
    boolean add(E e);
}
```

List 인터페이스에서 add메소드만 가져온 것입니다. 이 때도 List에 있는 E와 add 메소드의 매개변수 타입 E가 다를까요? 생각해보면 이 때는 두개의 타입이 같습니다. 위에서 다르다고 했는데 어떤 차이가 있는 것일까요?

<br> <br>

## `제너릭 메소드를 사용하면 T가 지역변수로 바뀐다.`

제너릭 메소드를 사용하면 T가 지역변수로 바뀐다는 것이 무슨말일까요? 위의 Student 클래스를 보겠습니다. 클래스에 붙은 T와(Student<T>) 메소드에 붙은 T는( static <T> getOneStudent(T id) )는 다르고,  List에 있는 E와 List 인터페이스의 add 메소드의 있는 E는 같은 E입니다. 이렇게 차이가 나는 것은 제너릭 메소드를 사용했기 때문에 getOneStudent() 메소드의 T는 지역변수로 바뀌게 되는 것입니다.

```java
public static void printAll(ArrayList<? extends Test> list1, ArrayList<? extends Test> list2) {
// 로직
}

public static <T extends Test> void printAll(ArrayList<T> list1, ArrayList<T> list2) {
// 로직
}
```

제너릭 메소드를 사용하지 않는다면 첫 번째와 같이 매게변수의 타입에다가 타입제한을 해야했습니다. 이렇게 매개변수마다 제네릭을 이용해서 타입제한을 하는 것이 아니라 제너릭 메소드를 사용하면 두 번째 같이 바꿀 수 있습니다.

<br>

```java
public static <T extends Comparable<? super T>> void sort(List<T> list)
```


위의 제너릭 메소드를 분석해보면 위의 있는 T는 모두 같은 타입입니다.

<br>

```java
<T extends Comparable<T>
```

그리고 위의 제너릭 메소드에 사용한 것을 보면 반드시 Comparable 인터페이스를 구현한 클래스 타입이어야 한다는 조건입니다.

<br>

```java
<? super T>
```

이거의 뜻은 T와 타입이 같거나 조상클래스만 ?에 가능하다는 뜻입니다. 예를들면 A클래스가 있고 A클래스의 조상 B가 있다고 가정했을 때, ?에는 A, B, Object가 가능합니다. 

<br> <br>

## `그러면 제네릭 메소드를 왜 사용하는 것일까?`

일단 매개변수가 길어지는 것이 가독성에 좋지 않기 때문에 리턴타입 옆으로 따로 뺀 것 같습니다. 그리고 만약에 비교를 해야하는 메소드라면 반드시 Comparable 인터페이스를 구현한 클래스여야 하고, 위의 경우라면 반드시 Test 클래스의 자손이어야 한다. 이처럼 타입캐스팅 에러의 경우를 제외시킬 수 있기 때문에 훨씬 안전하게 사용할 수 있어서 사용하는 것이다. 그리고 제너릭 메소드를 사용하면 클래스의 T와 메소드의 T는 같은 문자를 사용하더라도 다른 문자라는 것을 기억해야 합니다.

<br> <br>

## `제너릭 메소드의 사용법 2가지`

```java
public class Box<T> {
    private T t;
    
    public T getT() {
        return t;
    }
    
    public void setT(T t) {
        this.t = t;
    }
}
```
```java
public class Util {
    
    public static<T> Box<T> boxing(T t) {
        Box<T> box = new Box<T>();
        box.setT(t);
        return box;
    } 
}
```
```java
public class Main {
    public static void main(String[] args) {
        Box<Integer> box1 = Util.<Integer>boxing(100);
        
        Box<String> box2 = Util.boxing("암묵적호출");
    }
}
```

제너릭 메소드 호출하기 위해서는 2가지 방법이 있습니다. Main 클래스에서 처럼 호출할 때 타입을 지정하는 법과 지정하지 않아도 되는 방법이 있다. 타입을 지정하게 되면 컴파일러가 <Integer>를 보고 타입을 지정합니다. 그러나 암묵적호출의 경우 매게타입이 String 인 것을 보고 컴파일러가 타입을 추청합니다.

<br> <br>

## `제너릭 클래스와 독립적`

다시 정리하자면 형식과 사용이 제너릭 클래스와 똑같지만, 클래스의 <T>와 제너릭 메소드의 <T>는 다르기 때문에 잘 생각해야 합니다. 그리고 제네릭 메소드는 그 메소드를 포함하고 있는 클래스가 제네릭인지 아닌지 상관없습니다.

```java
class Student<T>{

    public T getOneStudent(T id){ return id; }  // 1
    
    public <T> T getId(T id){return id;} // 2 제네릭 클래스의 T와 다름  
    
    public <S> T toT1(S id){return id; }  // 3
    
    public static <S> T toT2(S id){return id;}  // 4 에러 
}
```

- 1번의 경우 클래스의 제너릭 타입 T를그대로 사용하는 경우다.
- 2번의 경우 클래스의 제너릭 타입 T와 제너릭 메소드 타입 T는 다르다.
- 3번의 경우 static 메소드가 아닌 일반메소드기 때문에 클래스의 타입과 제너릭 메소드의 타입을 같이 사용가능하다.
- 4번의 경우 static 메소드기 때문에 클래스의 제너릭 타입 T를 사용하기 때문에 에러가 발생한다.