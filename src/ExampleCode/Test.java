package ExampleCode;

public class Test<T> {
    private String title;
    private T first, second;

    public Test(String title) {
        this.title = title;
    }

    void add(T o) {
        if (first == null) {
            first = o;
        } else if (second == null) {
            second = null;
        }
        else {
            first = second;
            second = o;
        }
    }

    public static void main(String[] args) {
        Test<String> testString = new Test<>("String title");
        Test<Integer> testInteger = new Test<>("Integer title");

        testString.add("String");
        testInteger.add(1);
    }
}
