package ExampleCode;

import java.util.Optional;

public class Test2 {
    public static void main(String[] args) {
        Integer integer = Optional.of("")
                .filter(x -> x.length() > 0)
                .map(Integer::parseInt).orElse(-1);
        System.out.println(integer.intValue());
    }
}
