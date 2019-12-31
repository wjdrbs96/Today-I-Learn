package Fruit_Code;

public class Test {
    public static void main(String[] args){
        Fruit fAry[] = {new Grape(), new Apple(), new Pear()};

        for(Fruit f : fAry){
            f.print();
        }
    }
}
