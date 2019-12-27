package APIClass;

public class ObjectMath {
    public static void main(String []args){
        Object obj = new Object();
        System.out.println(obj.getClass());  //Object 메소드
        System.out.println(obj.hashCode());  //Object 메소드
        System.out.println(obj.toString());  //Object 메소드

        //Math Class는 Java.lang 안에 있는 클래스이다.
        System.out.println(Math.PI);
        System.out.println(Math.round(-3.5));
        System.out.println(Math.abs(-3.4));
        System.out.println(Math.pow(3,4));
    }
}
