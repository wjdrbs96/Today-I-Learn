package inheritance.abstractinterface;

public class HPPrinter extends Device implements Connectable {
    public void print(){
        System.out.println("HP프린터이다");
    }

    public void connect(){   //추상메소드를 구현함
        System.out.println(name + ", HP프린터를 연결합니다");
    }
}
