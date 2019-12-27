package inheritance.typecast;

public class DownCasting {
    public static void main(String []args){
        Person she = new Person("이소라",2056442);
        System.out.println(she.name + " " + she.number);
        //Faculty f = she;   //컴파일 오류
        // 참조변수 f 에 메모리 할당이 되지 않았기 때문임
        //Faculty f1 = (Faculty)she; //실행 오류
        Person p = new Staff("김상기",1187,"강서대학교",3456);
        //업 캐스팅
        Staff s = (Staff)p;  // 다운캐스팅
        s.division = "교학처";
        System.out.print(p.name + " " + p.number +" ");
        System.out.print(s.univ + " " + s.number + " ");
        System.out.println(s.division);
    }
}
