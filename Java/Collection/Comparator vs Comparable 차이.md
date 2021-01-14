# `Comparator와 Comparable 차이`

<br>

## `Comparator 인터페이스`

```java
package java.util;

@FunctionalInterface
public interface Comparator<T> {

    int compare(T o1, T o2);
}
```

`Comparator` 인터페이스에 다른 default 메소들도 존재하면 abstract method는 compare 하나만 존재하는 `FunctionalInteface`입니다.

<br>

## `Comparable 인터페이스`

```java
package java.lang;

public interface Comparable<T> {

    public int compareTo(T o);
}
```

`Comparable` 인터페이스도 `compareTo`라는 추상 메소드 하나만 가지고 있는 인터페이스 입니다. 

`compare(T o1, T o2)`, `compareTo(T o)`를 보면 이름으로도 비교하는데 사용한다는 것을 알 수 있습니다. 
(두 객체가 같으며 0, 비교하는 값보다 작으면 음수, 크면 양수를 반환하도록 구현합니다.)

그리고 매개변수가 `compare()`은 2개, `compareTo()`는 1개라는 것을 알 수 있습니다. 

<br>

<br>

## `그러면 Comparable과 Comparator 인터페이스의 차이는 무엇일까요?`

- `Comparable: 기본 정렬 기준을 구현하는데 사용`
- `Comparator: 기본 정렬 기준 외에 다른 기준으로 정렬하고자 할 때 사용(내림차순, 자기가 정한 정렬 기준 등등)`

아마 compare 메소드는 매개변수가 2개, compareTo 메소드는 매개변수가 1개이기 때문에 그러지 않을까 싶습니다. 

<br>

## `정렬 코드 예제`

```java
import java.util.Arrays;
import java.util.Comparator;

public class Test {
    public static void main(String[] args) {
        String[] list=  {"cat", "Dog", "lion", "tiger"};

        Arrays.sort(list);  // String의 Comparable 구현에 의한 정렬

        Arrays.sort(list, String.CASE_INSENSITIVE_ORDER);  // 대소문자 구문 안함
 
        Arrays.sort(list, new Descending());       // 내림차순 정렬
    }
}

class Descending implements Comparator {

    @Override
    public int compare(Object o1, Object o2) {
        if (o1 instanceof Comparable && o2 instanceof Comparable) {
            Comparable c1 = (Comparable)o1;
            Comparable c2 = (Comparable)o2;
            return c1.compareTo(c2) * -1;
        }
        return -1;
    }
}
```

- `Arrays.sort()`
    - 오름차순으로 정렬을 하는 가장 기본적인 방법입니다. 이럴 때는 String이 Comparable을 구현하여 compareTo 메소드를 사용하여 정렬합니다.
- `Arrays.sort(list, String.CASE_INSENSITIVE_ORDER)`
    - ![스크린샷 2021-01-14 오후 5 42 43](https://user-images.githubusercontent.com/45676906/104565647-e9566f00-568f-11eb-99b7-b58bf7ee7087.png)
    - 위의 정렬은 대소문자를 구분하지 않고 정렬하는 방법인데, String 클래스의 내부를 보면 위와 같이 `Comparator`를 구현해서 정렬하고 있다는 것을 볼 수 있습니다.
- `Arrays.sort(list, new Descending())`
    - Descending 이라는 클래스를 만들어서 `Comparator` 인터페이스를 구현해서 내림차순 정렬로 만든 것을 볼 수 있습니다.
    
    
<br>

## `정리하기`

- Comparable 인터페이스 메소드 compare(T o)는 매개변수가 하나이기 때문에 오름차순 정렬로 쓰이는 것 같고
- Comparator 인터페이스 메소드 compareTo(T o1, T o2)는 매개변수가 두 개이기 때문에 오름차순 외 다른 정렬 기준으로 사용할 때 쓰이는 것 같습니다.
