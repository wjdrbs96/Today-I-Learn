import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class FileReaderTest {
    public static void main(String[] args) throws FileNotFoundException {
        try (FileReader fr = new FileReader("test1.txt")) {
            int c = 0;
            while ((c = fr.read()) != -1) {
                System.out.print((char)c);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
