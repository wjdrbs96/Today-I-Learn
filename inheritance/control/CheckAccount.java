package inheritance.control;

import inheritance.access.Account;   //외부 패키지 클래스를 import 시켜옴
import inheritance.access.SavingAccount;

public class CheckAccount extends Account {
    long minimum;

    public CheckAccount(String name, int number, long minimum){
        super(name,number);
        this.minimum = minimum;
    }

    public static void main(String []args){
        SavingAccount myAccount = new SavingAccount("김태희",1234,0.34);
        myAccount.deposit(250000);
        myAccount.withdraw(50000);
        myAccount.checkBalance();

        System.out.println(myAccount.name);
        //System.out.println(myAccount.open);    // protected라 SavingAccount는 접근 불가능
        //System.out.println(myAccount.number);  //default라 다른 패키지에서 접근 불가
        //System.out.println(myAccount.balance); //private 라 접근불가

        CheckAccount cAccount = new CheckAccount("이민전",99987,50000);
        cAccount.deposit(2500000);
        cAccount.withdraw(50000);
        cAccount.checkBalance();

        System.out.println(cAccount.name);
        System.out.println(cAccount.open);
        //System.out.println(cAccount.number);   //defult 는 다른 패키지에서 접근이 안됨
        //System.out.println(cAccount.balance);  //private는 같은 클래스가 아니면 접근 불가
    }
}
