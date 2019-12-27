package APIClass;

public class BoxingUnboxing {
    public static void main(String []args){
        //boxing 은 기본형에서 랩퍼 객체의 자동변환
        //unboxing은 랩퍼 객체에서 기본형의 변환
        Double radius = 2.59; //boxing

        double r = radius;  // unboxing
        System.out.print("반지름: " + r + ", 원 면적: ");
        System.out.println(radius * radius * Math.PI);

        Integer x =5, y=3;  //boxing
        System.out.printf("%d + %d = %d%n", x , y , x+y);  //unboxing
        Boolean b = true;  //boxing
        System.out.printf("%b %n", b);  //unboxing

    }
}
