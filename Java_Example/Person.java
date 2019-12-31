package Java_Example;

public class Person implements Mammal {
    public static String Type= "사람";

    public void walk(){
        System.out.println(Type +  "은 두발로 걷습니다");
    }

    public void giveBirth(){
        System.out.println(Type + "이 아기를 낳습니다");
    }
}
