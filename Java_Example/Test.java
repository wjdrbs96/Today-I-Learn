package Java_Example;

public class Test {
    public static void main(String[] args){
        Mammal m1 = new Person();  // 업 캐스팅
        Mammal m2 = new Whale();   // 업 캐스팅
        Test t = new Test();
        t.ShowInfo(m1);
        t.ShowInfo(m2);

    }
    public void ShowInfo(Mammal mk){
        mk.walk();
        mk.giveBirth();
    }
}
