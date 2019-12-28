package StringPackage;

public class StringClass {
    public static void main(String[] args){
        String str1 = new String("abc");    //new를 이용시 힙영역 메모리 할당
        String str2 = new String("abc");    //new를 이용시 힙영역 메모리 할당
        System.out.println(str1 == str2);   // false

        String str3 = "abc";  // 상수풀
        String str4 = "abc";  // 상수풀
        System.out.println(str3 == str4);    // ture
    }
}
