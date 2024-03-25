package ExampleCode;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

public class StringEncode {
    public static void main(String[] args) throws UnsupportedEncodingException {
        String korean = "하이";

        byte[] isoBytes = korean.getBytes(StandardCharsets.ISO_8859_1);
        byte[] utf8Bytes = korean.getBytes(StandardCharsets.UTF_8);

        String result1 = new String(isoBytes, StandardCharsets.ISO_8859_1);
        String result2 = new String(utf8Bytes, StandardCharsets.UTF_8);
        String result3 = new String(isoBytes, StandardCharsets.UTF_8);

        System.out.println("result1: " + result1);
        System.out.println("result2: " + result2);
        System.out.println("result3: " + result3);


    }
}