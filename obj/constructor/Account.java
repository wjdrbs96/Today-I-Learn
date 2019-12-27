package obj.constructor;

public class Account {
    public String owner;
    public long balance;

    public Account(String owner){
        this.owner = owner;
    }

    public Account(long balance){
        this.balance = balance;
    }

    public Account(String owner, long balance){
        this(owner);    // this 인자가 1개이고 owner 인 생성자를 호출함
        //this 는 2번째 줄 이상에서 부터는 사용 불가능
        this.balance = balance;
    }

    public static void main(String []args){
        Account act1 = new Account("최여진");
        Account act2 = new Account(1000000);
        act2.owner = "홍혜빈";
        Account act3 = new Account("신세경",200000);

        System.out.printf("act1: %s %d %n", act1.owner, act1.balance);
        System.out.printf("act2: %s %d %n", act2.owner, act2.balance);
        System.out.printf("act3: %s %d %n",act3.owner, act3.balance);


    }
}
