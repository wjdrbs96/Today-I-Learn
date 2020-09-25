package Java.ExampleCode;

import java.util.ArrayList;
import java.util.List;
import java.util.Spliterator;

public class Test {
    public static void main(String[] args) {
        List<String> list = new ArrayList<>();
        list.add("gyun");
        list.add("hyunwoo");
        list.add("bobae");
        list.add("toby");

        Spliterator<String> spliterator = list.spliterator();
        Spliterator<String> stringSpliterator = spliterator.trySplit();
        while (spliterator.tryAdvance(System.out::println));
        System.out.println("=================");
        while (stringSpliterator.tryAdvance(System.out::println));
    }
}
