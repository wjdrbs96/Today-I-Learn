package inheritance.overloading;

public class MyMath {
    double x,y;

    public MyMath(double x, double y){
        this.x = x;
        this.y = y;
    }

    public double multiply(){
        return x*y;
    }

    public static double multiply(double a, double b){
        return a*b;
        //정적 메소드이므로 비정적 필드와 비정적 메소드를 참조할 수 없다.
        //this와 super 를 정적 메소드에서는 사용할 수 없다.
    }

    public static void main(String []args){
        MyMath math = new MyMath(3.4,6.7);
        System.out.println(math.multiply());
        System.out.println(math.multiply(3.4,6.7));
    }
}
