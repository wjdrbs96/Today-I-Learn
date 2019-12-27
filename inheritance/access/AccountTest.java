package inheritance.access;

public class AccountTest {
    public static void main(String []args){
        SavingAccount myAccount = new SavingAccount("강민경",1234,0.32);
        myAccount.deposit(50000);
        myAccount.withdraw(4000);
        myAccount.checkBalance();

        System.out.println(myAccount.name);
        System.out.println(myAccount.open);
        System.out.println(myAccount.number);
        //System.out.println(myAccount.balance); //접근불가
    }
}
