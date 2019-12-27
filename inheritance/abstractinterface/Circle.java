package inheritance.abstractinterface;

public class Circle extends Shape {
    double radius;

    public Circle(double x, double y, double radius){
        super(x,y);
        this.radius = radius;
    }

    public void draw(){
        //추상 메소드를 반드시 하위클래스에서 구현을 해줘야 에러가 발생하지 않는다.
        super.drawCenter();
        System.out.printf("반지름: %f", radius);
        System.out.printf("원 면적: %f\n", radius*radius * Math.PI);
    }
}
