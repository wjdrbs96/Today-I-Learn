package ExampleCode;

public class Test {
    public static void main(String[] args) {
        Account account = new Account();

        account.deposit(10000);
        try {
            account.withdraw(30000);
        } catch (BalanceException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
}