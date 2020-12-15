# `아이템40: @Override 애너테이션을 일관되게 사용하라`

```java
import java.util.HashSet;
import java.util.Set;

public class Bigram {
    private final char first;
    private final char second;

    public Bigram(char first, char second) {
        this.first = first;
        this.second = second;
    }

    public boolean equals(Bigram b) {
        return b.first == first && b.second == second;
    }

    public int hashCode() {
        return 31 * first + second;
    }

    public static void main(String[] args) {
        Set<Bigram> s = new HashSet<>();
        for (int i = 0; i < 10; ++i) {
            for (char ch = 'a'; ch <= 'z'; ch++) {
                s.add(new Bigram(ch, ch));
            }
        }
        System.out.println(s.size());
    }
}
```

위의 코드를 보면 26이 출력될 것 같지만 결과는 260이 출력됩니다. 

<br>

### 왜 그럴까요?

Bigram 클래스를 봤을 때 equals 메소드를 오버라이딩 한 것 같지만 리턴 타입을 보면 boolean 임을 알 수 있습니다. 따라서 다중정의를 해버리게 된 것입니다.

재정의를 하려면 Object의 equals 메소드를 해야하기 때문에 리턴타입이 Object여야 합니다. 

<br>

## 정리

- 상위 클래스의 메소드를 재정의하려는 못든 메소드에 @Override 에너테이션을 달자. 
- 예외는 구체 클래스에서 상위 클래스의 추상 메소드를 재정의할 때는 굳이 @Override를 달지 않아도 된다.
- @Override 어노테이션을 달면 우리가 실수했을 때 컴파일러가 바로 알려줄 것이기 때문에 장점이 있다.

