package Java;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Test {
    public static void main(String[] args) {
        List<String> list = Arrays.asList("abc", "aaa", "bbb", "ddd", "aaa");
        Collections.sort(list, (s1, s2) -> s2.compareTo(s1));
    }
}




