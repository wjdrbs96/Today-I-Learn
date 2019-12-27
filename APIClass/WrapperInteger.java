package APIClass;

public class WrapperInteger {
    public static void main(String []args){
        //랩퍼클래스 --> 자바의 기본형을 참조형으로 표현한다.
        Integer age = new Integer(20);
        Integer snum = new Integer("201211105");

        System.out.println("나이:" + age.intValue());
        System.out.println("학번:" + snum.intValue());
        System.out.println(age.equals(snum)); //문자열이든 정수든 값만 같으면 true

        System.out.println(Integer.parseInt("24567"));
        System.out.println(Integer.parseInt("ff",16)); // 문자열 ff를 16진법으로 분석해 int로 반환
        System.out.println(Integer.toBinaryString(255)); //255 를 이진수 문자열로 반환
        System.out.println(Integer.toString(255,16)); // 255를 16진수 문자열로 반환
    }
}
