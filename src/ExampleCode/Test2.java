package ExampleCode;

import java.util.stream.Stream;

public class Test2 {
    public static void main(String[] args) {
        Stream<String[]> strArrStream = Stream.of(
                new String[]{"abc", "def", "ghi"},
                new String[]{"ABC", "DEF", "GHI"}
        );
    }
}
