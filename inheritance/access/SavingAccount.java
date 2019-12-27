package inheritance.access;

public class SavingAccount extends Account{
    public double rates;

    public SavingAccount(String name, int number, double rates){
        super(name,number);
        this.rates = rates;
    }

    public static void main(String []args){
        SavingAccount myAccount = new SavingAccount("김태희",1234,0.34);
        myAccount.deposit(400000); //deposit 메소드 public 이므로 접근가능
        myAccount.withdraw(50000); //withdraw 메소드 public 이므로 접근가능
        myAccount.checkBalance(); //checkBalace 메소드 public 이므로 접근가능

        System.out.println(myAccount.name);  // name 은 public 접근가능
        System.out.println(myAccount.open);  // open 은 protected 접근가능
        System.out.println(myAccount.number); // number 는 default 접근가능
        //System.out.println(myAccount.balance);  //balance는 private 이므로 접근불가
    }

}
