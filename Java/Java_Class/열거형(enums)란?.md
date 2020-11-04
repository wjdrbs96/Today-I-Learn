# 열거형(enums)란?

열거형은 서로 관련된 상수를 편리하게 선언하기 위한 것으로 여러 상수를 정의할 때 사용하면 유용하다. 
한마디로 `서로 연관된 상수들의 집합`을 의미한다. 

<br>

## Enum의 장점

- 코드가 단순해지며, 가독성이 좋다.
- 인스턴스 생성과 상속을 방지하여 상수값의 타입안정성이 보장된다. 
- enum class를 사용해 새로운 상수들의 타입을 정의함으로 정의한 타입이외의 타입을 가진 데이터값을 컴파일시 체크한다. 

```java
public class EnumExample {
    static final String MALE = "MALE";
    static final String FEMALE = "FEMALE";

    public static void main(String[] args) {
        String gender;
        gender = EnumExample.MALE;
        gender = EnumExample.FEMALE;

        // 컴파일 에러가 발생하지 않음 -> 문제점 발생
        gender = "boy";

        Gender gender2;
        gender2 = Gender.MALE;
        gender2 = Gender.FEMALE;
        
        // 컴파일 에러 발생 -> 의도하지 않은 상수 값을 체크할 수 있음
        gender2 = "boy";

    }
}

enum Gender {
    MALE, FEMALE
}
```


