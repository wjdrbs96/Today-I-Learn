package Java.ExampleCode;

public class Test2 {

    public static void main(String[] args) {
        B b = new B(2);
    }
}

class A {
    public A() {
        System.out.println("생성자 A");
    }

    int a;

    public A(int a) {
        this.a = a;
        System.out.println("생성자 A2");
    }
}

class B extends A {
    public B() {
        System.out.println("생성자 B");
    }

    int b;
    public B(int b) {
        super(b);
        this.b = b;
        System.out.println("생성자 B2");
    }
}
