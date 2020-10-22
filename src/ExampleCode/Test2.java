package ExampleCode;

public class Test2 {
    static int data = 10;
    public static void main(String[] args) {
        int data = 5;
        data++;
        System.out.println(data);
        ttt();
    }

    static void ttt() {
        data++;
        System.out.println(data);
    }
}
