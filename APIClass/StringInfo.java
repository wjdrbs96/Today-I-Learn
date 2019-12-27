package APIClass;

public class StringInfo {
    public static void main(String []args){
        String snum = "20123458";
        String name = new String("김경석");
        String dept = new String(new char[] {'컴','퓨','터','학','과'});
        String java = new String("java");

        System.out.printf("%d ,", snum.compareTo("20123458"));
        System.out.printf("%d %n",snum.compareTo("20123456"));
        //비교해서 같으면 0

        System.out.printf("%s " , name);
        System.out.printf("%s ", name.replace('김', '강'));
        System.out.printf("%s %n", name);  //String 객체라 바뀌지는 않음
        System.out.printf("%s ",dept);
        System.out.printf("%s ",dept.substring(0,3));  //0~2번 인덱스까지의 문자 반환
        System.out.printf("%d %n",dept.length());   //dept 의 길이
        System.out.printf("%s ",java.indexOf('a'));  //java 에서 a는 몇번 째 인덱스에서 가장 처음으로 나오는지?
        System.out.printf("%s ",java.concat(" language"));
        System.out.printf("%s ",java.replace('j','J'));
        System.out.printf("%s ",java.toUpperCase());
        System.out.printf("%b ", java.equals("JAVA"));   // 대소문자 구별하고 비교
        System.out.printf("%b ", java.equalsIgnoreCase("JAVA")); // 대소문자를 무시하고 비교
        System.out.printf("%s %n", java.substring(1));  // 인덱스 1번 부터 끝까지 반환
    }
}
