package ExampleCode;

import java.util.HashSet;
import java.util.Set;

public class Test {
    public static void main(String[] args) {
        Integer a = 1;
        Integer b = 1;
        Integer c = 99999;
        Integer d = 99999;

        System.out.println(a == b);
        System.out.println(c == d);

        Set<Integer> set = new HashSet<>();
    }
}