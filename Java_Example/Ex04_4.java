package Java_Example;

class X{
    int num = 0;
    X() {num++;}
}

class XY extends X{
    XY() {num++;}
}

class XYZ extends XY{

}

public class Ex04_4 {
    public static void main(String[] args){
        XY a = new XY();
        System.out.println(a.num);
        //하위 클래스에서 객체를 만들면 상위클래스 부터 생성이 되므로 num 은 2가 된다.
    }
}
