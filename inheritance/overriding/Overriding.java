package inheritance.overriding;

public class Overriding {
    public static void main(String []args){
        Person she = new Person("이소라",2045432);
        she.printInfo();

        Person i = new Faculty("김영태",1145782,"연한대학교",38764);
        i.printInfo();
        //업캐스팅을 한 후에 참조변수 i 로 printInfo 메소드를 호출하면 동적 바인딩에 의해서 Faculty 클래스 메소드 호출

        Person he = new Staff("최영기",12345,"남도대학교",1287,"기획처");
        he.printInfo();
        //위와 같이 업스캐팅 후에 메소드 호출은 동적바인딩에 의해서 하위클래스 메소드 호출

        Faculty f = (Faculty)he; //다운 캐스팅
        f.printInfo();  // 다운캐스팅을 해도 Staff 클래스 메소드 호출
        Staff s = (Staff)he;
        s.printInfo(); // Staff 클래스 메소드 호출
    }
}
