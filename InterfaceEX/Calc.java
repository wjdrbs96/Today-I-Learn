package InterfaceEX;

public interface Calc {
    double PI = 3.14;   //public static final 생략되어 있음

    int add(int num1, int num2);
    int sub(int num1, int num2);
    int times(int num1, int num2);
    int div(int num1, int num2);

    //인터페이스에는 일반 메소드는 없으므로 다 public abstract 생략되어 있음

}
