package inheritance.control;

public class CheckTest {
    public static void main(String []args){
        CheckAccount cAccount = new CheckAccount("이민정",1234,5667);
        cAccount.deposit(250000);
        cAccount.withdraw(50000);
        cAccount.checkBalance();

        System.out.println(cAccount.name);
        System.out.println(cAccount.minimum);
        //System.out.println(cAccount.open); //하위 클래스가 아니므로 open 도 접근이 안되는 것을 확인
        //System.out.println(cAccount.number); // default
        //System.out.println(cAccount.balance); //private
    }
}
