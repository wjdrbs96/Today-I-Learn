## 사용자 정의 예외 만들기

기존의 정의된 예외 클래스 외에 필요에 따라 프로그래머가 새로운 예외 클래스를 정의하여 사용할 수 있다. 

```java
public class Test extends Exception {
    public Test(String message) { // 문자열을 매게변수로 받는 생성자
        super(message);   // 조상인 Exception 클래스의 생성자를 호출
    }
}
```

<br>

### 예제

```java
public class Account {
    private long balance;

    public Account() {

    }

    public long getBalance() {
        return balance;
    }

    public void deposit(int money) {
        balance += money;
    }

    public void withdraw(int money) {
        if (balance < money) {
            throw new BalanceException("잔고부족" + (money - balance) + " 모자람");
        }
        balance -= money;
    }
}
```

```java
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
```
```
잔고부족20000 모자람
ExampleCode.BalanceException: 잔고부족20000 모자람
	at ExampleCode.Account.withdraw(Account.java:20)
	at ExampleCode.Test.main(Test.java:9)
```

