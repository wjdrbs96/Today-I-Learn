package InterfaceEX;

public class CompleteCalc extends Calculator {

    public int times(int num1, int num2){
        return num1 * num2;
    }

    public int div(int num1, int num2){
        return num1 / num2;
    }

    //이렇게 추상클래스에 있는 추상메소드들은 나눠서 구현하는게 나한테 좀 어색하다.
}
