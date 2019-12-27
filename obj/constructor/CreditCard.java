package obj.constructor;

public class CreditCard {
    public String owner;
    private long number;

    public CreditCard(String owner){  // 인자가 1개인 생성자
        this.owner = owner;
    }

    public CreditCard(long number){  // 인자가 1개인 생성자
        this.number = number;
    }

    public CreditCard(String owner, long number){
        this.owner = owner;
        this.number = number;
    }

    public long getNumber(){   //number 가 private이므로 getter 메소드 만듬
        return number;
    }

    public static void main(String []args){
        CreditCard card1 = new CreditCard("권해동");
        CreditCard card2 = new CreditCard(3452_4587_2345_9845L);
        card2.owner = "권순미" ;
        CreditCard card3 = new CreditCard("권다혜", 5638_8627_8623_8249L);

        System.out.println("card1: " + card1.owner + " " + card1.getNumber());
        System.out.println("card2: " + card2.owner + " " + card2.getNumber());
        System.out.println("card3: " + card3.owner + " " + card3.getNumber());
    }


}
