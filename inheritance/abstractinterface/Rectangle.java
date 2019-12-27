package inheritance.abstractinterface;

public class Rectangle extends Shape {
    double height;
    double width;

    public Rectangle(double x, double y, double width, double height){
        super(x,y);
        this.width = width;
        this.height = height;
    }

    public void draw(){
        super.drawCenter();
        System.out.printf("가로:%f, 세로:%f, ",width,height);
        System.out.printf("사각형 면적: %f\n",width*height);
    }
}
