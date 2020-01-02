package Exception;

public class ArrayException {
    public static void main(String[] args) {
        int[] arr = new int[5];

        /*for(int i=0; i<=5; i++){
            System.out.println(arr[i]);
        }
        //5번 인덱스는 없기 때문에 예외 발생

         */
        try {
            for (int i = 0; i <= 5; i++) {
                System.out.println(arr[i]);
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println(e);
            System.out.println("예외처리");
        }

    }
}
