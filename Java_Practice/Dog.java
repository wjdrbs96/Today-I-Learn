package Java_Practice;

public class Dog {
    String name;

    public Dog(String name){   //생성자에는 반환형 존재 x
        this.name = name;
    }

    public static void main(String[] args){
        Dog doori = new Dog("Doori");
        System.out.println(doori.name);

    }
}
