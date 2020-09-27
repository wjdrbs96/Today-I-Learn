package ExampleCode;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class Test {
    public static void main(String[] args) {
        String[] strArr = {"aaa", "ddd", "ccc"};
        List<String> strList = Arrays.asList(strArr);

//        Stream<String> stream = strList.stream();
//        Stream<String> stream1 = Arrays.stream(strArr);

        Stream<String> sorted = strList.stream().sorted();
        sorted.forEach(System.out::println);
        sorted.forEach(System.out::println);  // 에러
    }
}