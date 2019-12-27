package inheritance.typecast;

public class Instanceof {
    public static void main(String []args){
        Person she =new Person("이소라",2054632);
        if(she instanceof Staff){
            Staff st1 = (Staff)she;
        }
        else{
            System.out.println("she는 Staff 객체가 아니다");
        }

        Person p = new Staff("김상기",12345,"강서대학교",6363636);
        //업캐스팅
        if(p instanceof Staff){
            System.out.println("p는 Staff 객체이다");
            Staff st2 = (Staff)p;
        }
    }
}
