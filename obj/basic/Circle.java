package obj.basic;

public class Circle {
    public double radius;
    public static double PI = 3.141592;

    public Circle(double radius){
        this.radius = radius;
    }

    public double getArea(){
        return radius * radius * PI;
    }

    public void print(){
        System.out.printf("반지름이 %f인 원의 면적은 %f입니다%n", radius,getArea());
    }

    public static void main(String []args){
        System.out.println("원주율: " + Circle.PI); //정적 필드는 클래스.필드 가능
        Circle c1 = new Circle(2.78);
        c1.print();
        Circle c2 = new Circle(5.25);
        c2.print();

        c1.PI=3.14;
        System.out.println("원주율: " + c2.PI);
        //정적필드는 저장공간이 하나이므로 c1을 통해서 바꿔도 c2도 바뀐다.
    }
}
