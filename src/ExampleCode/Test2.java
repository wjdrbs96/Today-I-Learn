package ExampleCode;

public class Test2<T> {
    String title;
    T first, second;

    public Test2(String title) {
        this.title = title;
    }

    void add(T o) {
        if (first == null) {
            first = o;
        }
        else if (second == null) {
            second = o;
        }
        else {
            first = second;
            second = o;
        }
    }

    public static void main(String[] args) {
        Test2<Integer> test2 = new Test2<>("제목입니다");
    }
}
