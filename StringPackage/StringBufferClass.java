package StringPackage;

public class StringBufferClass {
    public static void main(String []args){
        String java = new String("JAVA");
        System.out.println(System.identityHashCode(java));

        StringBuilder str1 = new StringBuilder(java);
        System.out.println(System.identityHashCode(str1));

        str1.append("Android");
        System.out.println(System.identityHashCode(str1));
        //위, 아래의 identityHashCode 값이 똑같은 것을 확인할 수 있다.




    }
}
