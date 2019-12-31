package Java_Example;

public class Whale implements Mammal {
    public static String Type ="고래";

    public void walk(){
        System.out.println(Type + "는 물에서 수영합니다");
    }

    public void giveBirth(){
        System.out.println(Type + "는 아기를 낳습니다");
    }
}
