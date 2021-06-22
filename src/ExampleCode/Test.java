package ExampleCode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class    Test {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        String s = br.readLine();

        System.out.println(s.equals("null"));

        try {

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

