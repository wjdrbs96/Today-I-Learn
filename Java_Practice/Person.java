package Java_Practice;

public class Person {
    String name;

    public Person(String name){
        this.name = name;
    }

    public static void main(String[] args){
        Person p = new Person("예진");
        System.out.println(p.name);
        //p = new Person(); 이런거 불가능
        p.name = "진태";
        System.out.println(p.name);
    }
}
