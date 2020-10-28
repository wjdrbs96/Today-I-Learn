package ExampleCode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Test2 {
    public static void main(String[] args) {
        List<String> animals = new ArrayList<>();
        animals.add("dog");
        animals.add("horse");
        animals.add("cat");

        Collections.sort(animals, (s1, s2) -> s2.length() - s1.length());

        System.out.println(animals.get(0));
    }
}
