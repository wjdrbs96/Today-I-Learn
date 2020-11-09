# 자원을 직접 명시하지 말고 의존 객체 주입을 사용하라

많은 클래스가 하나 이상의 자원에 의존한다. 가령 맞춤법 검사기는 사전(dic-tionary)에 의존하는데, 이런 클래스를 정적 유틸리티 클래스로 구현할 모습을 적지 않게 볼 수 있다.

<br>

```java
// 부적절한 static 유틸리티 사용 예 - 유연하지 않고 테스트 할 수 없다.
public class SpellChecker {

    private static final Lexicon dictionary = new KoreanDicationry();

    private SpellChecker() {
        // Noninstantiable
    }

    public static boolean isValid(String word) {
        throw new UnsupportedOperationException();
    }


    public static List<String> suggestions(String typo) {
        throw new UnsupportedOperationException();
    }
}

interface Lexicon {}

class KoreanDicationry implements Lexicon {}
```

비슷하게, 싱클턴으로 구현하는 경우도 흔하다.

```java
// 부적절한 싱글톤 사용 예 - 유연하지 않고 테스트 할 수 없다.
public class SpellChecker {

    private final Lexicon dictionary = new KoreanDicationry();

    private SpellChecker() {
    }

    public static final SpellChecker INSTANCE = new SpellChecker() {
    };

    public boolean isValid(String word) {
        throw new UnsupportedOperationException();
    }


    public List<String> suggestions(String typo) {
        throw new UnsupportedOperationException();
    }

}
```

두 방식 모두 사전을 단 하나만 사용한다는 가정하면 그리 훌룡해 보이지 않다. 실전에서는 사전이 언어별로 따로 있고 특수 어휘용 사전을 별도로 두기도 한다. 
심지어 테스트용 사전도 필요할 수 있다. 사전 하나로 이 모든 쓰임에 대응할 수 있기를 바라는 건 너무 단순한 생각이다.

<br>

SpellChecker가 여러 사전을 사용할 수 있도록 만들어보자. 간단히 dictionary 필드에서 final 한정자를 제거하고 다른 사전으로 교체하는 메소드를 추가할 수 있지만,
아쉽게도 이 방식은 어색하고 오류를 내기 쉬우며 `멀티스레드 환경에서는 쓸 수 없다.`  

<br>

`사용하는 자원에 따라 동작이 달라지는 클래스에는 정적 유틸리티 클래스나 싱글턴 방식이 적합하지 않다.`
이는 의존 객체 주입의 한 형태로, 맞춤법 검사기를 생성할 때 의존 객체인 사전을 주입해주면 된다. 

```java
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

}

class Lexicon {}
```

`의존 객체 주입 패턴`은 아주 단순하여 수많은 프로그래머가 이 방식에 이름이 있따는 사실도 모른 채 사용해왔다. 
예에서는 dictionary라는 딱 하나의 자원만 사용하지만, 자원이 몇 개든 의존 관계가 어떻든 상관없이 잘 작동한다. 

<br>

또한 불변을 보장하면 같은 자원을 사용하려는 여러 클라이언트가 의존 객체들을 안심하고 공유할 수 있기도 하다. 
의존 객체 주입은 생성자, 정적 팩터리, 빌더 모두에 똑같이 응용할 수 있다. 

<br>

이 패턴의 쓸만한 변형으로, 생성자에 자원 팩토리를 넘겨주는 방식이 있다. 팩터리란 호출할 때마다 특정 타입의 인스턴스를 반복해서 만들어주는 객체를 말한다.

<br>

즉, 팩토리 메소드 채턴을 구현한 것이다. 자바 8에서 소개한 Supplier<T> 인터페이스가 팩토리를 표현한 완벽한 예다. 

<br>

`의존 객체 주입`이 `유연성`과 `테스트 용이성`을 거샌해주긴 하지만, 의존성이 수천 개나 되는 큰 프로젝트에서는 코드를 어지럽게 만들기도 한다. 
스프링(Spring) 같은 의존 객체 주입 프레임워크를 사용하면 이런 어질러짐을 해소할 수 있다. 

<br>

### 핵심정리 

```
클래스가 내부적으로 하나 이상의 자원에 의존하고, 그 자원이 클래스 동작에 영향을 준다면 싱글턴과 정적 유틸리티 클래스는 사용하지 않는 것이 좋다.
이 자원들을 클래스가 직접 만들게 해서도 안 된다. 대신 필요한 자원을 혹은 그 자원을 만들어주는 팩터리를 생성자에 념겨주자.
의존 객체 주입이라 하는 이 기법은 클래스의 유연성, 재사용성, 테스트 용이성을 기막히게 개선해준다. 
```