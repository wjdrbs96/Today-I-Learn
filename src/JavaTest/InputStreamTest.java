package ExampleCode.Exam;

import java.io.*;

public class InputStreamTest {
    public static void main(String[] args) {
        try (FileInputStream fn = new FileInputStream("test1.txt");
             InputStreamReader in = new InputStreamReader(fn, "UTF-8")) {
            
            int c;
            while ((c = in.read()) != -1) {
                System.out.print((char)c);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
