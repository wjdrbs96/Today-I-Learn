package StringPackage;

public class StringTest1 {
    public static void main(String[] args){
        String str1 = new String("Java");
        String str2 = new String("Android");
        System.out.println(System.identityHashCode(str1));

        str1 = str1.concat(str2);  //이렇게 하면 str1에 새로운 문자열이 생긴 것 같지만 아니다.
        System.out.println(System.identityHashCode(str1));
        //위에 identityHashCode와 아래꺼를 비교하면 다른 것을 확인할 수 있다.
        //따라서 str1 은 새로운 메모리를 할당함
        //자주 연결을 해서 쓸 것이라면 String 은 메모리 낭비를 고려해야 함
    }
}
