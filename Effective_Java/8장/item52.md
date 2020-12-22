# `아이템52 : 다중정의는 신중히 사용하라`

```java
import java.math.BigInteger;
import java.util.*;

public class CollectionClassifier {
    public static String classify(Set<?> s) {
        return "집합";
    }

    public static String classify(List<?> list) {
        return "리스트";
    }

    public static String classify(Collection<?> c) {
        return "그 외";
    }

    public static void main(String[] args) {
        Collection<?> [] collections = {
                new HashSet<String>(),
                new ArrayList<BigInteger>(),
                new HashMap<String, String>().values()
        };

        for (Collection<?> c : collections) {
            System.out.println(classify(c));
        }
    }
}
```

위의 코드는 `그 외`만 세 번 반복해서 출력합니다.

이유가 무엇일까요? ?

- 다중정의(오버로딩)은 `어느 메소드를 호출할지가 컴파일타임에 정해지기 때문입니다.` => 이건 저는 처음 알았습니다..

- 직관과 어긋나는 이유는 `재정의한 메소드는 '동적'으로 선택되고 다중정의한 메소드는 '정적'으로 선택되기 때문입니다.`
    - 오버라이딩 => 동적바인딩
    
```java
import java.util.List;

class Wine {
    String name() {
        return "포도주";
    }
}

class SparklingWine extends Wine {
    @Override
    String name() {
        return "발주송 포도주";
    }
}

class Champagne extends SparklingWine {
    @Override
    String name() {
        return "샴페인";
    }
}

public class Overriding {
    public static void main(String[] args) {
        List<Wine> wineList = List.of(
                new Wine(), new SparklingWine(), new Champagne());
        
        for (Wine wine : wineList) {
            System.out.println(wine.name());
        }
    }
}
```
```
포도주
발주성 포도주
샴페인
```

결과가 이런 이유는 `동적 바인딩(가장 하위에서 정의ㅡ한 재정의 메소드가 실행되는 것)`의 속성 때문입니다.

<br>

## `위의 오버로딩 문제점 코드 수정`

```java
public class CollectionClassifier {
    public static String classify(Collection<?> c) {
        return c instanceof Set ? "집합" :
                c instanceof List ? "리스트" : "그 외";
    }
}
```

- 헷갈릴 수 있는 코드 작성하지 말기
- 안전하고 보수적으로 가려면 매게변수 수가 같은 다중정의는 만들기 말기

<br>

## `ObjectOutputStream 클래스`

이 클래스는 다중정의가 아닌 모든 메소드에 이름을 지어주는 방법을 선택했습니다. (writeBoolean(boolean), writeInt(int), writeLong(long))

<br>

## `생성자의 다중정의`

- 정적 팩터리라는 대안 사용하기
- 생성자의 매게변수 수가 같을 때
    - 매게변수 중 하나 이상이 `근본적으로 다르면(두 타입이 null이 아니고 값을 서로 어느 쪽으로든 형변환할 수 없을 때)` 헷갈릴 일이 없습니다. (ex: int, Collection)
    
<br>

## `오토박싱이 생기면서 생긴 문제점`

```java
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class SetList {
    public static void main(String[] args) {
        Set<Integer> set = new TreeSet<>();
        List<Integer> list = new ArrayList<>();

        for (int i = -3; i < 3; ++i) {
            set.add(i);
            list.add(i);
        }

        for (int i = 0; i < 3; ++i) {
            set.remove(i);
            list.remove(i);
        }

        System.out.println(set + " " + list);  // [-3, -2, -1] [-2, 0, 2]
    }
}
```

코드는 결로 생각하기로는 [-3, -2, -1] 로 둘 다 나올 것 같은데 list가 결과가 다르게 나옵니다.

왜 그럴까요?

```
boolean remove(Object o);
E remove(int index);
``` 

위와 같이 List 인터페이스가 remove 메소드를 다중정의 해놓았습니다. 그렇기에 Object를 매개변수로 받는 메소드는 원소를 지우고
int를 매개변수로 받는 메소드를 인덱스를 삭제하게 됩니다. 

```
for (int i = 0; i < 3; ++i) {
     set.remove(i);
     list.remove(Integer.valueOf(i));
}
```

위와 같이 수정을 하면 제대로 동작을 합니다. 

`오토박싱이 생긴 이후로 Object와 int는 근본적으로 다르지 않기 때문에 생긴 문제입니다.`

<br>

### P317에 어려운 예시가 나오네요..

- 컴파일러 제작자를 위한 설명이니 무슨 말인지 이해되지 않더라도 그냥 넘어가라네요.. ? 
- 핵심은 다중정의된 메소드들이 함수형 인터페이스를 인수로 받을 때, 비록 서로 다른 함수형 인터페이스라도 인수 위치가 같으면 혼란이 생긴다는 것
- `따라서 메소드를 다중정의할 때 서로 다른 함수형 인터페이스라도 같은 위치의 인수로 받아서는 안 된다.` => 서로 다른 함수형 인터페이스라도 서로 근본적으로 다르지 않다는 뜻입니다.

<br>

## 애매한 부분

```
public static String valueOf(Object obj) {
    return (obj == null) ? "null" : obj.toString();
}

public static String valueOf(char data[]) {
    return new String(data);
}
```

<br>

### `String 클래스의 다중정의`

```java
public final class String {

    public boolean contentEquals(StringBuffer sb) {
        return contentEquals((CharSequence)sb);
    }

    public boolean contentEquals(CharSequence cs) {
        // Argument is a StringBuffer, StringBuilder
        if (cs instanceof AbstractStringBuilder) {
            if (cs instanceof StringBuffer) {
                synchronized(cs) {
                   return nonSyncContentEquals((AbstractStringBuilder)cs);
                }
            } else {
                return nonSyncContentEquals((AbstractStringBuilder)cs);
            }
        }
        // Argument is a String
        if (cs instanceof String) {
            return equals(cs);
        }
        // Argument is a generic CharSequence
        char v1[] = value;
        int n = v1.length;
        if (n != cs.length()) {
            return false;
        }
        for (int i = 0; i < n; i++) {
            if (v1[i] != cs.charAt(i)) {
                return false;
            }
        }
        return true;
    }
}
```

위의 경우는 다중정의를 하고 있어도 `같은 객체를 입력하면 같은 작업을 수행해주니 문제가 없다.`
하지만 자바는 이번 아이템에 대해서 실패한 클래스 => String.valueOf() 에 `같은 객체를 넌네도 전혀 다른 일을 수행한다.`

- 질문 : 같은 객체를 건네더라도 전혀 다른 일을 수행한다. ? ? ?  => 그니까.. 메소드가 오버로딩 되어서 두 개가 다른 일을 해서 문제라는 뜻일까요? 같은 객체를 건넨다가 애매하네요..



