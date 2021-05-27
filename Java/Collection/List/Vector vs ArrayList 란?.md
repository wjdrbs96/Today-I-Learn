# `Vector 클래스와 ArrayList 클래스 차이는?`

Vector 클래스는 `자바 컬렉션 프레임워크`가 나오기 전부터 있던 클래스입니다. 그리고 ArrayList 클래스는 Vector 클래스의 단점을 보완하기 위해서 나온 클래스입니다. 

이번 글에서는 두 클래스가 어떤 차이가 있는지 알아보겠습니다.

<br>

## `Vector 클래스란?`

```java
public class Vector<E>
    extends AbstractList<E>
    implements List<E>, RandomAccess, Cloneable, java.io.Serializable
{
    protected Object[] elementData;

    protected int capacityIncrement;

    public Vector(int initialCapacity, int capacityIncrement) {
    }

    public Vector(int initialCapacity) {
        this(initialCapacity, 0);
    }

    public Vector() {
        this(10);
    }

    public Vector(Collection<? extends E> c) {
    }
}
``` 

우선 Vector는 `List` 인터페이스를 구현하고 있는 것을 볼 수 있습니다. 그래서 `순서를 유지하고`, `중복을 허용`하는 특징을 가지고 있습니다.(Vector는 `배열`을 사용하는 것도 볼 수 있습니다.)

`생성자`를 보면 `initialCapacity`, `capacityIncrement` 매개변수가 있는데 어떤 의미인지 알아보겠습니다. 

- `기본 생성자: 초기용량 = 10 그리고 용량이 꽉 찬다면 자동으로 더 큰 배열에 복사하는 과정이 일어납니다. 용량은 기존 용량 x 2로 늘어납니다.`
- `생성자를 통해서 초기용량이 예측 가능하고, 배열 용량이 초과했을 때 늘어나는 용량을 지정하여 Vector 객체를 만들 수도 있습니다.`

![스크린샷 2021-01-25 오후 11 50 38](https://user-images.githubusercontent.com/45676906/105721705-3546c500-5f68-11eb-96fd-9cfcb00306d8.png)

위의 사진은 Vector 클래스에서 배열 용량을 늘릴 때 사용하는 코드입니다. 코드를 보면 용량을 늘리고, 복사하는 과정이 일어나는 것을 볼 수 있습니다. 

<br>

### `Vector는 Thread-safe 하다.`

```java
public class Vector<E>
    extends AbstractList<E>
    implements List<E>, RandomAccess, Cloneable, java.io.Serializable
{
    public synchronized int capacity() {
        return elementData.length;
    }

    public synchronized int size() {
        return elementCount;
    }

    public synchronized boolean isEmpty() {
        return elementCount == 0;
    }
}
```

Vector 클래스의 일부 메소드들을 가져온 것입니다. 메소드에 `synchronized`가 있는 것을 보아 Vector 클래스는 `Thread-safe` 하다는 것을 알 수 있습니다. 
즉, 멀티스레드 환경이 아닐 때 Vector 클래스를 사용하게 되면 `성능이 떨어지게 됩니다.`

<br>

### `Vector는 Iterator와 Enumeration을 사용할 수 있다.`

```java
import java.util.Iterator;
import java.util.Vector;

public class Test1 {
    public static void main(String[] args) {
        Vector<String> vector = new Vector<>();
        vector.add("Gyunny");
        vector.add("Bobae");
        vector.add("Hyungil");

        Iterator<String> iterator = vector.iterator();

        while (iterator.hasNext()) {
            System.out.print(iterator.next() + " "); // Gyunny Bobae Hyungil 
        }
    }
}
```

위의 코드는 Iterator를 사용하여 코드를 출력하는 예제입니다. 

<br>

```java
import java.util.Enumeration;
import java.util.Vector;

public class Test1 {
    public static void main(String[] args) {
        Vector<String> vector = new Vector<>();
        vector.add("Gyunny");
        vector.add("Bobae");
        vector.add("Hyungil");

        Enumeration<String> elements = vector.elements();

        while (elements.hasMoreElements()) {
            System.out.print(elements.nextElement() + " "); // Gyunny Bobae Hyungil 
        }
    }
}
```

위의 코드는 Enumeration를 이용해서 출력하는 예제입니다. 이렇게 Vector 클래스는 `Iterator와 Enumeration`를 이용해서 값을 출력할 수 있습니다. 

<br>

## `ArrayList 클래스란?`

```java
public class ArrayList<E> extends AbstractList<E>
        implements List<E>, RandomAccess, Cloneable, java.io.Serializable
{
    private static final int DEFAULT_CAPACITY = 10;

    transient Object[] elementData; // non-private to simplify nested class access

    public ArrayList(int initialCapacity) {
    }

    public ArrayList() {
        this.elementData = DEFAULTCAPACITY_EMPTY_ELEMENTDATA;
    }

    public ArrayList(Collection<? extends E> c) {
    }
}
```

ArrayList 클래스는 List 인터페이스를 구현하고 있는 대표적인 클래스입니다. 그래서 역시 `순서를 유지하고`, `값의 중복을 허용`한다는 특징을 가지고 있습니다. 
그리고 ArrayList 클래스는 Vector 클래스의 단점을 보완하기 위해 나온 클래스이기 때문에 역시 `배열`을 사용하고 있는 것을 볼 수 있습니다. 

ArrayList 생성자를 보면 3개가 있습니다. 

- `기본 생성자: 초기 용량이 10이고, 용량이 초과하면 용량을 1.5배 증가한 배열에 복사하는 과정이 일어납니다.`
- `초기 용량 설정 생성자: 배열에 복사하는 비용은 많이 들기 때문에 웬만하면 적당한 초기 용량을 설정하는 것이 효율적입니다.`
- `Collection 생성자: Collection 타입의 매게변수를 받는 생성자가 존재합니다.`

![스크린샷 2021-01-26 오전 12 03 25](https://user-images.githubusercontent.com/45676906/105723317-fe71ae80-5f69-11eb-954a-fcca77676662.png)

ArrayList 내부 코드를 보면 위와 같이 `배열의 용량을 1.5배로 증가시키고`, `새로운 배열에 복사하는 과정`이 일어나는 것을 볼 수 있습니다. 

<br>

### `ArrayList는 Thread-safe 하지 않다.`

```java
public class ArrayList<E> extends AbstractList<E>
        implements List<E>, RandomAccess, Cloneable, java.io.Serializable
{
    public boolean add(E e) {
    }
    
    public void add(int index, E element) {
    }


    public E remove(int index) {
    }
}
```

ArrayList 클래스의 메소드에는 `synchronized` 키워드가 없습니다. 그렇기 때문에 `Thread-safe`하지 않고, Vector 클래스보다 성능이 좋습니다. 
만약 멀티스레드 환경에서 사용해야 한다면 `CopyOnWriteArrayList 또는 Collections.synchronizedList`를 사용할 수 있습니다.

<br>

### `ArrayList는 Iterator만 사용할 수 있다.`

```java
import java.util.ArrayList;
import java.util.Iterator;

public class Test1 {
    public static void main(String[] args) {
        ArrayList<Integer> list = new ArrayList<>();

        list.add(1);
        list.add(2);

        Iterator<Integer> iterator = list.iterator();

        while (iterator.hasNext()) {
            System.out.print(iterator.next() + " "); // 1 2
        }
    }
}
```

Iterator와 Enumeration의 관계도 ArrayList와 Vector의 관계와 같습니다. 즉, Enumeration보단 Iterator를 사용하면 됩니다. 그래서 ArrayList에서 Enumeration를 지원하지 않습니다.

<br>

## `ArrayList vs Vector 요약`

- `ArrayList는 Thread-safe 하지 않고, Vector는 Thread-safe 하다.따라서 성능은 ArrayList가 더 좋다.`
- `ArrayList 초기 용량은 10이고 초과하면 1.5배로 늘림, Vector의 초기용량은 10, 초과하면 2배로 늘림`
- `ArrayList는 Iterator만 사용할 수 있고, Vector는 Enumeration, Iterator 둘 다 사용할 수 있음`
- `Vector 클래스는 컬렉션 프레임워크 이전에 나온 클래스이고, 현재 소스 코드 호환 때문에 남아 있는 클래스 이기 때문에 ArrayList를 사용하자.`
