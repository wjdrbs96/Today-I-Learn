# `아이템35: ordinal 메소드 대신 인스턴스 필드를 사용하라`

대부분의 열거 타입 상수는 자연스럽게 하나의 정수값에 대응됩니다. 그리고 모든 열거 탕비은 해당 상수가 그 열거 타입에서 몇 번째 위치인지를 반환하는 ordinal이라는 메소드를 제공합니다. 

```java
public enum Ensemble {
    SOLE, DUET, TRIO, QUARTET, QUINTET, SEXTET;
    
    public int numberOfMusicians() {
        return ordinal() + 1;
    }
}
```

위의 코드는 동작은 하지만 유지보수 하기에 매우 좋지 않은 코드입니다. 상수 선언 순서를 바꾸거나 중간에 뭐 하나를 빼는 순간 문제가 많이 생기기 때문이다.

<br>

## 해결책

- 열거 타입 상수에 연결된 값은 ordinal 메소드로 얻지 말고 인스턴스 필드에 저장하자.

```java
public enum Ensemble {
    SOLE(1), DUET(2), TRIO(3);
    
    private final int numberOfMusicians;
    Ensemble(int size) {
        this.numberOfMusicians = size;
    }
    
    public int numberOfMusicianS() {
        return numberOfMusicians;
    }
}
```

Enum의 API 문서를 보면 ordinal에 대해 이렇게 쓰여 있습니다. `대부분의 프로그래머는 이 메소드를 쓸 일이 없다. 이 메소드는 EnumSet과 EnumMap 같이 열거 타입 기반의 범용 자료구조에 
쓸 목적으로 설계되었다` 따라서 이런 용도가 아니라면 ordinal 메소드를 사용하지 맙시다.

