package Java.ExampleCode;

import java.util.Arrays;

public class Test {
    public static void main(String[] args) {
        String[] names = {"gyun", "hyunwoo", "bobae"};

        Arrays.sort(names, String::compareToIgnoreCase);

        System.out.println(Arrays.toString(names));    // [bobae, gyun, hyunwoo]
    }
}
