package ExampleCode;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class StringEncode {
    public static void main(String[] args) throws UnsupportedEncodingException {
        String korean = "하이";

        String encode = URLEncoder.encode(korean, StandardCharsets.UTF_8);
        System.out.println("encode: " + encode);

        String decode = URLDecoder.decode(encode, StandardCharsets.ISO_8859_1);
        System.out.println("decode: " + decode);

        String name = new String(decode.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
        System.out.println("name: " + name);

//        System.out.println("result1: " + result1);
//        System.out.println("result2: " + result2);
//        System.out.println("result3: " + result3);
//        System.out.println("result4: " + result4);
    }
}