package ExampleCode;

import java.util.stream.IntStream;

public class Test2 {
    public static void main(String[] args) {
        IntStream intStream = IntStream.rangeClosed(1, 10); // 1 ~ 10
        intStream.filter(i -> i % 2 == 0).forEach(System.out::print);  // 246810
    }
}
