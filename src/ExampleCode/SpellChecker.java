package ExampleCode;

public class SpellChecker {
    public static void main(String[] args) {
        int a = 3;
        System.out.println(System.identityHashCode(a));
        a = 2;
        System.out.println(System.identityHashCode(a));
        a = 3;
        System.out.println(System.identityHashCode(a));

    }
}
