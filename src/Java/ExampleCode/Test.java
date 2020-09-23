package Java.ExampleCode;

import java.util.function.Function;

public class Test {
    public static void main(String[] args) {
        Function<Integer, Integer> plus = (i) -> i + 10;
        Function<Integer, Integer> multiply = (i) -> i * 2;

        System.out.println(plus.andThen(multiply).apply(2));
    }
}




