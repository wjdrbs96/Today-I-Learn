package inheritance.overriding;

public class Person {
    public String name;
    public long number;

    public Person(String name, long number){
        super();
        this.name = name;
        this.number = number;
    }

    public void printInfo(){  //오버라이딩 메소드
        System.out.println("이름: " + name + " 주민번호: " + number);
    }
}
