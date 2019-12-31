package Java_Example;

class Super{
    int n = 1;
    public int getN(){
        return n;
    }
}

class Sub extends Super{
    int n = 10;
    public int getN(){
        return n;
    }

    public int getSuperN(){
        return super.getN();
    }
}



public class Ex08_1 {
    public static void main(String[] args){
        Super sup = new Sub();    //업캐스팅
        System.out.println(sup.getN());  // getN 메소드는 오버라이딩 되어 있으니 동적바인딩 생각
        System.out.println(sup.n); // sup 은 Super 클래스만 참조 가능
        System.out.println(((Sub) sup).n);  //다운 캐스팅을 한 이후에 n 을 참조하면 Sub 클래스꺼 참조
        //부모 클래스와 자식 클래스에 n이 둘다 있다면 다운캐스팅 되었으니 자식클래스 꺼를 참조함
        System.out.println(((Sub) sup).getSuperN());

    }
}
