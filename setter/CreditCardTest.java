package obj.setter;

public class CreditCardTest {
    public static void main(String []args){
        CreditCard parkCard = new CreditCard();
        CreditCard leeCard = new CreditCard();

        parkCard.owner = "박지성";
        parkCard.setNumber(2378_7643_7634_9825L);
        //private와 같이 외부에서 바로 참조할 수 없는 필드에 대해 getter, setter 사용
        leeCard.owner = "이민정";
        leeCard.setNumber(6556_9876_4521_6838L);

        parkCard.use(1000000);
        leeCard.use(15000);
        parkCard.payBill(50000);
        leeCard.payBill(10000);
        System.out.println(parkCard.owner +": " + parkCard.getNumber() );
        //값을 가져올 때는 getNumber 메소드를 이용하여 가져온다.
        System.out.println("카드잔액: " + parkCard.getBalance());
        System.out.println("카드포인트: " + parkCard.getPoint());
        System.out.println(leeCard.owner + ": " + leeCard.getNumber());
        System.out.println("카드대금 잔액: " + leeCard.getBalance());
        System.out.println("카드포인트: " + leeCard.getPoint());

    }
}
