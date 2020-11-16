# Java는 `Call by Reference` vs `Call by Value` 중 어떤 것일까?

코드의 예를 보면서 알아보자.

```java
public class Test2 {
    public static void main(String[] args) {
        int a = 1;
        int b = 2;
        swap(a, b);
        System.out.println(a);  // 1
        System.out.println(b);  // 2

    }

    static void swap(int a, int b) {
        int temp = a;
        a = b;
        b = temp;
    }
}
```

위의 코드에서 `primitive type`으로 매게변수를 넘겼기 때문에 `call by value`로 인해 원래의 a, b값이 바뀌지 않는다는 것은 쉽게 예측할 수 있다.

<br>

## 그러면 Java의 참조형은 `Call by Reference`로 작동할까?

```java
public class Test {
    public static void main(String[] args) {
        Integer a = 3;
        Integer b = 5;
        swapTest(a, b);
        System.out.println(a + " " + b);
    }

    static void swapTest(Integer a, Integer b) {
        Integer temp = a;
        a = b;
        b = temp;
    }
}
```

그러면 이번에는 `primitive type`이 아니라 `Reference type`인데 값이 바뀔까 안 바뀔까?


### `정답은 바뀌지 않는다.` 이유가 무엇일까?

일단 바뀌지 않는 것을 보면 `자바는 Call by Reference`가 아님을 알 수 있다. 그러면 어떤 방식으로 동작하는지 알아보자.

<br>

자바에서는 매게변수에 실제 주소 값을 넘기는 것이 아니라 주소 값이 가리키는 곳과 똑같이 가르키는 주소 값을 복사해서 넘겨주게 된다. 
정리하자면 `자바는 객체(참조 타입)을 메서드의 매게변수로 넘길 때 참조하는 지역변수의 실제 주소를 넘기는 것이 아니라 그 지역변수가 가리키고 있는 힙 영역의 객체를 가리키는 새로운 지역변수를 생성하여 그것을 통하여 같은 객체를 가리키도록 하는 방식임을 알 수 있다.`

<br>

또한 새로운 지역변수를 생성해 이루어지기 때문에 기본 타입을 매개변수로 받아 값을 바꾼 경우와 참조타입을 매개변수로 받아 값을 변경한 경우 모두 스택에서 변경이 이루어지기 때문에 아무런 의미가 없게 된다.

![title](https://user-images.githubusercontent.com/47904523/80357265-ae1fd000-88b5-11ea-845a-ad4744e40148.png)

<br>

그렇기 때문에 `swap` 메소드에서는 `원본 주소`의 값을 바꾼 것이 아니라 `새로 복사된 주소`를 서로 바꾸게 된 것이다. 따라서 원본 주소의 값에는 영향이 없는 것이다. 
(참조값들을 다른 스택(함수)에서 변경하는 것은 아무런 의미가 없다.)

<br>

## 필드 값으로 바꿔보자.

```java
public class Test2 {
    public static void main(String[] args) {
        MyClass myClass1 = new MyClass(1);
        MyClass myClass2 = new MyClass(2);
        swap(myClass1, myClass2);
        System.out.println(myClass1.getIndex());  // 2
        System.out.println(myClass2.getIndex());  // 1

    }

    static void swap(MyClass m1, MyClass m2) {
        int tmpIndex = m1.getIndex();
        m1.setIndex(m2.getIndex());
        m2.setIndex(tmpIndex);
    }
}

class MyClass {
    private int index;

    public MyClass(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
```

이번에는 값이 바뀐다. `이번에는 왜 바뀌는 것일까?` 예측할 수 있겠는가?

<br>

같은 객체를 가리키는 새로운 지역변수(swap 메소드의 지역변수)를 생성하여 매게변수로 넘긴 상황이다. 주소가 복사되었긴 하지만 같은 객체를 가리키기 때문에
값 자체를 바꾸면 원본의 값이 바뀌게 되기 때문에 이번에는 a, b의 값이 바뀌는 것이다. 이렇게 `참조 값을 받아서 참조 값을 이용하여 값을 바꾸는 것은 가능하다.`

<br>

## 자바 메모리 영역

![ti](https://user-images.githubusercontent.com/47904523/80357196-8f213e00-88b5-11ea-9951-e4cb87170723.png)

- 일반적으로 `Primitive type`은 스택(stack)영역에서 생성되고, `Reference type`는 힙(Heap)영역에서 생성된다.
- 기본타입의 변수는 메모리에 `실제 값`을 저장하지만, 참조타입은 힙 영역의 저장되어 있는 `주소`가 저장된다. 