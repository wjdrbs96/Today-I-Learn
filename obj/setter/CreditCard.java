package obj.setter;

//getter 와 setter 구현하기
//필드와 값을 저장하고 반환하는 메소드를 각각 setter와 getter 라 한다.
public class CreditCard {
    public String owner;
    private long number;
    private int point;
    private int balance;

    public void use(int amount){
        balance += amount;
    }

    public void payBill(int amount){
        balance -= amount;
        addPoint(amount);
    }

    private void addPoint(int amount){
        point += amount/1000;
    }

    public long getNumber(){  //getter 구현 //number는 private임
        return number;
    }

    public void setNumber(long number){
        if(number < 1000_0000_0000L){
            System.err.println("잘못된 카드번호");
            return;
        }
        this.number = number;
    }

    public int getPoint(){
        return point;
    }

    public void setPoint(int point){
        this.point = point;
    }

    public int getBalance(){
        return balance;
    }

    public void setBalance(int balance){
        this.balance = balance;
    }
}
