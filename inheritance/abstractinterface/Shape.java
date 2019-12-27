package inheritance.abstractinterface;

public abstract class Shape {  //추상클래스는 class 앞에 abstract 를 붙힌다
    protected double x,y;

    public Shape(double x, double y){
        this.x = x;
        this.y = y;
    }

    public void drawCenter(){
        System.out.println("(x,y) = " +x + ", " + y);
        //일반메소드
    }

    public abstract void draw(); //추상메소드
    //추상 클래스는 반드시 추상 메소드를 가져야 한다.
}
