package inheritance.overloading;

public class Sum {
    // add 메소드를 인자를 다르게 줘서 정의함
    //클래스 내부에서 인자가 다르나 이름이 같은 메소드가 여러 개 정의될 수 있는 특징을 메소드 오버로딩
    //반환값이나 지정자가 다르더라도 인자가 값으면 같은 메소드로 취급
    public int add(int a, int b){
        return a+b;
    }

    public int add(int a, int b, int c){
        return a+b+c;
    }

    public int add(int a[], int n){
        int sum=0;
        for(int i=0;i<n;i++){
            sum+=a[i];
        }
        return sum;
    }

    public static void main(String []args){
        Sum adder = new Sum();
        System.out.println(adder.add(3,6));
        System.out.println(adder.add(3,6,9));
        System.out.println(adder.add(new int[]{3,6,9,12},4));
    }
}
