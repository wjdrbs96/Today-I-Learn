package Javalang_Class;

public class WrapperInteger {
    public static void main(String[] args){
        Integer age = new Integer(20);
        Integer snum = new Integer("20");

        System.out.println("나이:" + age.intValue()); //참조형의 내부 정수 값을 int형으로 반환
        System.out.println("학번:" + snum.intValue());
        System.out.println(age.equals(snum)); //참조형의 내부 정수값이 같으면 true 반환

        System.out.println(Integer.parseInt("24567"));
        System.out.println(Integer.parseInt("ff",16)); //ff = 1515 인데 16x15 + 1x15 = 255
        System.out.println(Integer.toBinaryString(255)); //255는 이진수로 변환
        System.out.println(Integer.toString(255,16)); //255를 15진법 문자열로 변환
        System.out.println(Integer.toString(255));



    }
}
