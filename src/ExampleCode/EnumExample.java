package ExampleCode;

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

    }
}

enum Gender {
    MALE, FEMALE
}
