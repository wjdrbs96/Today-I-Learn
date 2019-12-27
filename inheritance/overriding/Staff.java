package inheritance.overriding;

public class Staff extends Faculty {
    public String division;

    public Staff(String name, long number, String univ, long idNumber, String division){
        super(name,number,univ,idNumber);
        this.division = division;
    }

    public void printInfo(){  //오버라이딩 메소드
        System.out.print("이름: " + super.name + " 주민번호: " + super.getSNumber());
        System.out.print(" 대학: " + univ + " 직원번호: " +super.number);
        System.out.println(" 부서: " +division);
    }

    public void printFacultyInfo(){
        super.printInfo();
    }

    public static void main(String []args){
        Person she = new Person("이소라",2056432);
        she.printInfo();  // Person 클래스 메소드 호출

        Faculty i = new Faculty("김영태",1145782, "연한대학교",38764);
        i.printInfo();  //Faculty 클래스 메소드 호출

        Staff he = new Staff("최영기",1167429, "남도대하교",1287, "기획처");
        he.printInfo();  //Staff 클래스 메소드 호출
        he.printFacultyInfo();
    }
}
