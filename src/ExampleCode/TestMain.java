package ExampleCode;

public class TestMain {
    public static void main(String[] args) throws InterruptedException {
        FooThread fooThread = new FooThread();
        WoodyThread woodyThread = new WoodyThread();
        fooThread.start();
        woodyThread.start();
        Thread.sleep(10000);   // 어떤 쓰레드가 sleep 할까요?
        System.out.println("Main Thread Finish!");
    }
}

class FooThread extends Thread {
    @Override
    public void run() {
        for (int i = 0; i < 10; ++i) {
            System.out.println("FOO 멘토님 최고");
        }
    }
}

class WoodyThread extends Thread {
    @Override
    public void run() {
        for (int i = 0; i < 10; ++i) {
            System.out.println("WOODY 멘토님 최고");
        }
    }
}