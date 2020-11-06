package ExampleCode;

public class MyThread {
    public static void main(String[] args) {
        ThreadEx_2 t1 = new ThreadEx_2();
        t1.run();
    }
}

class ThreadEx_2 extends Thread {
    public void run() {
        throwException();
    }

    public void throwException() {
        try {
            throw new Exception();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}



