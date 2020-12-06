# 아이템5 : 자원을 직접 명시하지 말고 의존 객체 주입을 사용하라

많은 클래스가 하나 이상의 자원에 의존합니다. 가령 맞춤법 검사는 사전에 의존하고 있습니다. 

> 이렇게 사용하는 자원에 따라 동작이 달라지는 클래스에는 정적 유틸리티 클래스나 싱글턴 방식이 적합하지 않습니다. 

<br>

## `정적 유틸리티를 이용한 예시`

```java
import java.util.List;

public class SpellChecker {

    private static final Lexicon dictionary = new KoreanDictionary();

    private SpellChecker() {

    }

    public static boolean isValid(String word) {
        throw new UnsupportedOperationException();
    }

    public static List<String> suggestions(String typo) {
        throw new UnsupportedOperationException();
    }
}

interface Lexicon {}

class KoreanDictionary implements Lexicon {}
``` 

그리고 이번에는 `싱글턴`으로 구현하는 예시를 보겠습니다.

## `싱글턴으로 이용한 예시`

```java
public class SpellChecker {

    private final Lexicon dictionary = new KoreanDictionary();

    private SpellChecker() {

    }
    
    public static final SpellChecker INSTANCE = new SpellChecker() {
        
    };

    public static boolean isValid(String word) {
        throw new UnsupportedOperationException();
    }

    public static List<String> suggestions(String typo) {
        throw new UnsupportedOperationException();
    }
}

interface Lexicon {}

class KoreanDictionary implements Lexicon {}
```

두 방식 모두 사전을 하나만 사용한다면 크게 문제가 되지 않을 수 있습니다. 하지만 실제 사전이라면 사전이 언어별로 따로 있고 특수 어휘용 사전을 별로도 두기도 합니다. 
심지어 테스트용 사전도 필요할 수 있습니다. 이렇게 `어떤 클래스가 사용하는 리스소에 따라 행동을 달리 해야 하는 경우에는 '정적 유틸리티 클래스'와 '싱글턴'을 사용하는 것은 부적절합니다.`

<br>

대신 클래스(SpellChecker)가 여러 자원 인스턴스를 지원해야 하며, 클라이언트가 원하는 자원(dictionary)을 사용해야 합니다. 
`이 조건을 만족하는 간단한 패턴이 있으니, 바로 인스턴스를 생성할 때 생성자에 필요한 저원을 넘겨주는 방식입니다.`

이는 `의존 객체 주입의 한 형태`로 맞춤법 검사기를 생성할 때 의존 객체인 사전을 주입해주면 됩니다. 

<br>

### `의존 객체 주입의 예시`

```java
import java.util.List;
import java.util.Objects;

public class SpellChecker {

    private final Lexicon dictionary;

    public SpellChecker(Lexicon dictionary) {
        this.dictionary = Objects.requireNonNull(dictionary);
    }

    public boolean isValid(String word) {
        throw new UnsupportedOperationException();
    }

    public List<String> suggestions(String typo) {
        throw new UnsupportedOperationException();
    }

    public static void main(String[] args) {
        Lexicon lexicon = new KoreanDictionary();
        
        SpellChecker spellChecker = new SpellChecker(lexicon);
        spellChecker.isValid("Gyunny");
    }
}

interface Lexicon {}

class KoreanDictionary implements Lexicon {}
```

`Lexicon`이라는 dictionary에 따라 SpellChecker가 달라지기 때문에 생성자를 통해 의존성을 주입을 사용해서 쉽게 바꿀 수 있도록 하는 것이 좋습니다. 
위의 예에서는 `ditionary`라는 하나의 자원을 사용하지만, 자원이 여러 개이고 의존 관계가 어떻든 문제 없이 작동합니다. 

또한 불변을 보장하여 여러 클라이언트가 의존 객체들을 문제 없이 공유할 수 있기도 합니다. 

<br>

그리고 의존 객체 주입 패턴의 변형으로, 생성자에 `자원 팩터리`를 넘겨주는 방식이 있습니다. 

> 팩터리란? 호출될 때마다 특정 타입의 인스턴스를 반복해서 만들어주는 객체를 말합니다. 

자바 8에서 소개한 `Supplier<T> 인터페이스`가 팩토리를 표현한 완벽한 예입니다. 

```java
@FunctionalInterface
public interface Supplier<T> {
    
    T get();
}
```

`Supplier<T> 인터페이스`를 입력으로 받는 메서드는 일반적으로 한정적 와일드카드 타입을 사용해 팩터리의 타입 매게변수를 제한해야 합니다.

> Q. <? extends T> - T와 그 자손들을 구현한 객체들만 매개변수로 가능

이 방식을 사용해서 클라이언트가 자신이 명시한 타입의 하위 타입이라면 무엇이든 생성할 수 있는 팩토리를 넘길 수 있습니다. 

예를들어, 아래의 코드는 클라이언트가 제공한 팩토리가 생성한 타입(Tile)들로 구성된 모자이크(Mosaic)을 만드는 메소드입니다. 

```
Mosaic create(Supplier<? extends Tile> tileFactory) { . . . }
```

의존 객체 주입이 `유연성`과 `테스트 용이성`을 개선해주긴 하지만, 의존성이 매우 많은 프로젝트에서는 코드를 복잡해집니다. 
이럴 때 `Spring 프레임워크`를 사용해서 `DI`, `IoC 컨테이너`를 사용한다면 쉽게 문제를 해결할 수 있습니다. 

<br>

## `핵심 정리`

> 클래스가 내부적으로 하나 이상의 자원에 의존하고, 그 자원이 클래스 동작에 영향을 준다면 싱글턴과 정적 유틸리티 클래스는 사용하지 않는 것이 좋다.
> 이 자원들을 클래스가 직접 만들게 해서도 안 된다. 대신 필요한 자원을 생성자에 넘겨주자. 의존 객체 주입이라 하는 이 기법은 클래스의 유연성, 재사용성, 테스트 용이성을 기막히게 개선해준다. 




