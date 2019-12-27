package inheritance.abstractinterface;

public class USBMemory extends Device implements Connectable {
    //인터페이스들 간에 상속은 extends 를 사용하고 인터페이스를 구현하기 위한 하위 클래스는 implements
    public void print(){
        System.out.println("삼성 USB 메모리입니다");
    }

    public void connect(){
        System.out.println(name + ", USB 메모리를 연결합니다");
    }

}
