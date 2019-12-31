package Javalang_Class;

public class BoxingUnboxing {
    public static void main(String[] args){
        Double radius = 2.59;  //박싱(boxing)
        double r = radius;     //언박싱(unboxing)

        System.out.print("반지름: " + r + ", 원 면적: ");
        System.out.println(radius*radius*Math.PI);

        Integer x =5, y =3;
        System.out.printf("%d + %d = %d %n",x,y,x+y);
        Boolean b = true; //boxing
        System.out.printf("%b %n",b);  //unboxing

    }
}
