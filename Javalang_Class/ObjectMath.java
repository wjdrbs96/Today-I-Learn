package Javalang_Class;

public class ObjectMath {
    public static void main(String[] args){
        Object obj = new Object();
        System.out.println(obj.getClass());  // 현재 객체의 클래스를 반환
        System.out.println(obj.hashCode());  //현재 객체의 고유 ID 값 반환
        System.out.println(obj.toString());  // 클래스이름@16진수해쉬코드

        System.out.println(Math.PI);
        System.out.println(Math.round(-3.5)); //반올림한 정수 반환
        System.out.println(Math.abs(-3.4));   //절대값 반환
        System.out.println(Math.pow(3,4));   //3의 4승을 double 형으로 반환
    }
}
