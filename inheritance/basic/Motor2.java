package inheritance.basic;

public class Motor2 extends Vehicle {
    public String name = "자동차";
    public int displacement;

    public void setMaxSpeed(int maxSpeed){
        this.maxSpeed = maxSpeed;
    }

    public void setSeater(int seater){
        this.seater = seater;
    }

    public void setDisplacement(int displacement){
        this.displacement = displacement;
    }

    public void printInfo(){
        System.out.printf(super.name + ":" + this.name);
        //super.name 과 this.name 을 구별하기
        System.out.println(", 최대속도: " +maxSpeed + " km,");
        System.out.printf("정원: " + seater + " 명");
        System.out.println(", 배기량: " + displacement+ " cc");
    }

    public static void main(String []args){
        Motor2 myCar = new Motor2();
        myCar.setMaxSpeed(300);
        myCar.setSeater(2);
        myCar.setDisplacement(3500);
        myCar.printInfo();
    }
}
