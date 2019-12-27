package obj.basic;

public class Circle2 {
    public double radius;
    public static final double PI = 3.141592;
    //상수는 static 과 같이 쓰는 것이 좋다. 상수는 수정 불가이므로 하나의 저장공간만 사용하는게 좋다

    public Circle2(double radius){
        this.radius = radius;
    }

    public double getArea(){
        return radius * radius * PI;
    }

    public double getCircumference(){
        return 2 * PI * radius;
    }

    public void print(){
        System.out.printf("반지름이 %.2f인 원의 면적은 %.2f이고,",radius , getArea());
        System.out.printf(" 둘레길이는 %.2f이다%n" , getCircumference());
    }

    public static void main(String []args){
        Circle2 c1 = new Circle2(2.78);
        c1.print();

        Circle2 c2 = new Circle2(5.25);
        c2.print();
    }

}
