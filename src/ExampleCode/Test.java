package ExampleCode;

import java.io.UnsupportedEncodingException;

/**
 * Test
 *
 * @author JungGyun.Choi
 * @version 1.0.0
 * @since 2024. 03. 26.
 */
public class Test {
    public static void main(String[] args) throws UnsupportedEncodingException {
        String korean = "한글";
        byte[] bytes = korean.getBytes();
        String string = new String(bytes);
        System.out.println(string);
    }
}
