package ExampleCode;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

public class Test {
    public static void main(String[] args) throws UnsupportedEncodingException {
        String result = new String("í\u0095\u0098ì\u009D´".getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
        String result2 = new String("í\u0095\u0098ì\u009D´".getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8);

        System.out.println("result: " + result);
        System.out.println("result2: " + result2);
    }
}