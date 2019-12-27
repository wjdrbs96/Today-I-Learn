package obj.basic;

public class Const {
    public static void main(String []args){
        final int maxSize = 5;  //finall 이 붙었으므로 상수처리
        int data[] = new int[maxSize]; //maxSize는 상수이므로 배열 크기 지정 가능
        System.out.println("배열크기: " + data.length);
        System.out.println("배열크기: " + maxSize);
    }
}
