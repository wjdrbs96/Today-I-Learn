# `아이템42 : 익명 클래스보다는 람다를 사용하라`

```java
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Word {
    public static void main(String[] args) {
        List<String> word = new ArrayList<>();
        word.add("Gyuuny");
        word.add("Const");

        Collections.sort(word, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return Integer.compare(o1.length(), o2.length());
            }
        });
    }
}
```

String을 담은 List를 정렬할 때 자바8 이전에는 위와 같이 `익명 클래스`를 이용해서 정렬하였습니다. 하지만 익명 클래스 방식은 코드가 너무 길기 때문에
자바 8에 와서 추상 메소드 하나짜리 인터페이스는 특별한 인터페이스로 인정받았습니다. 

이것은 `함수형 인터페이스` 라고 부르는데 이 인터페이스들의 인스턴스를 람다식을 사용해 만들 수 있게 된 것입니다. 

```java
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Word {
    public static void main(String[] args) {
        List<String> word = new ArrayList<>();
        word.add("Gyuuny");
        word.add("Const");
        
        Collections.sort(word, (s1, s2) -> Integer.compare(s1.length(), s2.length()));
    }
}
```

그러면 위와 같이 코드를 깔끔하게 줄일 수 있습니다. `타입을 명시해야 코드가 더 명확할 때만 제외하고는, 람다는 컴파일러가 알아서 유추하기 때문에 매게변수 타입은 생략하자.`

```java
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.util.Comparator.comparingInt;

public class Word {
    public static void main(String[] args) {
        List<String> word = new ArrayList<>();
        word.add("Gyuuny");
        word.add("Const");

        Collections.sort(word, comparingInt(String::length));
    }
}
```

더 나아가 자바 8 때 List 인터페이스에 추가된 sort 메소드를 이요하면 더욱 짧아진다. 

<br>

`하지만 람다는 이름이 없고 문서화도 못 한다. 따라서 코드 자체로 동작이 명확히 설명되지 않거나 코드 줄 수가 많아지면 람다를 쓰지 말아야 한다.`

그리고 람다는 함수형 인터페이스 에서만 쓰이기 때문에 추상 클래스의 인스턴스를 만들 때 람다를 쓸 수 없으니, 익명 클래스를 사용해야 한다. 

- 람다는 자신을 참조할 수 없다.
- 람다를 직렬화하는 일은 극히 삼가야 한다. 

<br>

## 핵심 정리

- 익명 클래스는 함수형 인터페이스가 아닌 타입의 인스턴스를 만들 때만 사용하라.
