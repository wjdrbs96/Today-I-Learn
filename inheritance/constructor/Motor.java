package inheritance.constructor;

public class Motor extends Vehicle {
    public String name = "자동차";
    public int displacement;

    public Motor(){
        super();
    }

    public Motor(double MaxSpeed, int seater, int displacement){
        super(MaxSpeed, seater);   // 부모클래스 인자가2개인 생성자를 호출해줌
        this.displacement = displacement;
    }

    public void printInfo(){
        System.out.printf(super.name + ": " + this.name);
        System.out.println(", 최대속도: " + maxSpeed + " km");
        System.out.printf("정원: " +seater + " 명");
        System.out.println(", 배기량: " + displacement + " cc");
    }

    public static void main(String []args){
        Motor myCar = new Motor(300,4,5000);
        myCar.printInfo();
    }
}
