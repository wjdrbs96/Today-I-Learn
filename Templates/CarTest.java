package Templates;

public class CarTest {
    public static void main(String[] args){
        Car aicar = new AICar();   // 업캐스팅

        aicar.run();
        //Q : 업캐스팅을 한 후에 aicar 참조변수로 run 메소드를 호출
        //Q : 그러면 어떻게 Car 클래스 안에 run 메소드 에서는 drive 메소드를 AICar 꺼를 호출하지?
        System.out.println("=================");

        Car manualcar = new ManualCar();
        manualcar.run();
    }
}
