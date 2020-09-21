package Java;

@FunctionalInterface
public interface MyFunction {

    void run();
}

class LambdaEx1 {
    static void execute(MyFunction f) {    // 매게변수가 MyFunction
        f.run();
    }

    static MyFunction getMyFunction() {   // 반환타입이 MyFunction
        MyFunction f = () -> System.out.println("f3.run()");
        return f;
    }

    public static void main(String[] args) {
        MyFunction f1 = () -> System.out.println("f1.run()");

        MyFunction f2 = new MyFunction() {
            @Override
            public void run() {  // public 반드시 붙여야 함
                System.out.println("f2.run()");
            }
        };

        MyFunction f3 = getMyFunction();
        f1.run();
        f2.run();
        f3.run();

        execute(f1);
        execute(() -> System.out.println("run()"));
    }
}

