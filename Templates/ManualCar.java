package Templates;

public class ManualCar extends Car {

    public void drive(){
        System.out.println("사람이 핸들을 조작합니다");
        System.out.println("사람이 운전합니다");
    }

    public void stop(){
        System.out.println("브레이크를 밟아 멈춥니다");
    }

}
