package ExampleCode;

public class ThreadEx {
    static long startTime = 0;
    public static void main(String[] args) {
        Thread_Ex1 th1 = new Thread_Ex1();
        th1.start();

        startTime = System.currentTimeMillis();

        for (int i = 0; i < 300; ++i) {
            System.out.printf("%s", new String("-"));
        }

        System.out.print("소요시간1: " + (System.currentTimeMillis() - ThreadEx.startTime));
    }
}

class Thread_Ex1 extends Thread {
    @Override
    public void run() {
        for (int i = 0; i < 300; ++i) {
            System.out.printf("%s", new String("|"));
        }
        System.out.print("소요시간2: " + (System.currentTimeMillis() - ThreadEx.startTime));
    }
}