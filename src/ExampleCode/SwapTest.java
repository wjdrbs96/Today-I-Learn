package ExampleCode;

public class SwapTest {
    int value;

    public SwapTest(int value) {
        this.value = value;
    }
    public static void swap(SwapTest a, SwapTest b) {
        int temp = a.value;
        a.value = b.value;
        b.value = temp;
    }

    public static void main(String[] args) {
        SwapTest a = new SwapTest(1);
        SwapTest b = new SwapTest(2);
        System.out.println(a.value + " " + b.value);
        swap(a, b);
        System.out.println(a.value + " " + b.value);
    }
}