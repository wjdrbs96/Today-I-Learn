package ExampleCode;

public class Test {
    String name;
    String address;

    private static final Test test = new Test();

    public Test() {

    }

    public Test(String name) {
        this.name = name;
    }

    public static Test getTest() {
        return test;
    }

    public static void main(String[] args) {
        Test ttt = new Test("이름"); // 생성자를 통해 객체 생성 => 매번 새로운 객체

        Test ttt1 = getTest(); // 정적 팩토리 메소드를 통해 객체 생성
    }
}

