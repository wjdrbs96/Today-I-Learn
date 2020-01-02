package Exception;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class ExceptionTest {
    public static void main(String[] args) {
        FileInputStream fils = null;
        try {
            fils = new FileInputStream("a.txt");
        } catch (FileNotFoundException e) {
            System.out.println(e);
        }finally{
            try {
                fils.close();
                System.out.println("fianlly");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println("end");
    }
}
