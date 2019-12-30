package Generic;

public class GenericPrinterTest {
    public static void main(String[] args){
        GenericPrinter<Powder> powderPrinter = new GenericPrinter<Powder>();
        Powder powder = new Powder();
        powderPrinter.setMaterial(powder);
        System.out.println(powder);

        GenericPrinter<Plastic> plasticPrinter = new GenericPrinter<Plastic>();
        Plastic plastic = new Plastic();
        plasticPrinter.setMaterial(plastic);
        System.out.println(plastic);

        //GenericPrinter<Water> WaterPrinter = new GenericPrinter<Water>();
        //Water 로는 3D프린팅이 안되므로 이렇게 제한을 놓고 싶을 때는 상속의 개념을 사용

    }
}


