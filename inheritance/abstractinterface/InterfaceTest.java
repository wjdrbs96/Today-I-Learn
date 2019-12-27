package inheritance.abstractinterface;

public class InterfaceTest {
    public static void main(String []args){
        Device pdev[] = {new HPPrinter(), new USBMemory()};
        //업캐스팅

        pdev[0].print();  //동적바인딩
        pdev[1].print();  //동적바인딩

        ((Connectable)pdev[0]).connect(); // pdev[0].connect() 불가능
        ((Connectable)pdev[1]).connect(); // pdev[1].connect() 불가능


    }
}
