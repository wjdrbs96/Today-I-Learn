package ExampleCode;

public class Test2 {

    public static void main(String[] args) {
        int a = 5;

        for (int i = 0; i < 5; ++i) {
            a = i;
            System.out.println(System.identityHashCode(a));
        }
    }
}
