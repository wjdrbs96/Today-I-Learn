package Templates;

public class AICar extends Car {

    public void drive(){  //추상 메소드 구현
        System.out.println("자율주행 합니다");
        System.out.println("스스로 방향을 바꿉니다");
    }

    public void stop(){  //추상 메소드 구현
        System.out.println("스스로 멈춥니다");
    }
}
