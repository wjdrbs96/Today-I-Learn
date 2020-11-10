package ExampleCode;

public class Test {
    public static void main(String[] args) {
        int i = 10;

        Integer integer = i;                          // 컴파일 전
        // Integer integer = Integer.valueOf(i);      // 컴파일 후 해석

        Long l = 100L;                                // 컴파일 전
        // Long l = new Long(100L);                   // 컴파일 후 해석
    }
}
