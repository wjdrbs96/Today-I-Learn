package ExampleCode;

import java.util.Set;
import java.util.TreeSet;

public class Test {
    public static void main(String[] args) {
        Set<String> set = new TreeSet<>();

        set.add("1");
        set.add("2");
        set.add("3");
        set.add("1");
        set.add("2");

        set.forEach(System.out::println);
    }
}

