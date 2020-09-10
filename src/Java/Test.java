package Java;

public class Test {
    static {
        System.out.println("statc { }");       // 클래스 초기화 블럭
    }

    {
        System.out.println("{ }");             // 인스턴스 초기화 블럭
    }

    public Test() {
        System.out.println("생성자");
    }

    public static void main(String[] args) {
        System.out.println("Test test = new Test();");
        Test test = new Test();

        System.out.println("Test test2 = new Test();");
        Test test2 = new Test();
    }
}
