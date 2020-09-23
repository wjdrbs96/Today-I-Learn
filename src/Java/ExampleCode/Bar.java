package Java.ExampleCode;

public interface Bar {

    default void printName() {
        System.out.println("BAR");
    }
}
