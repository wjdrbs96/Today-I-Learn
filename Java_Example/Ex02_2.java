package Java_Example;

class A{}
class AB extends A{}
class ABC extends AB{}

public class Ex02_2 {
    public static void main(String[] args){
        A x = new A();
        A y = new AB();   //업 캐스팅
        A z = new ABC();  // 업 캐스팅

        System.out.println(x instanceof A);  //true
        System.out.println(x instanceof AB); //false
        System.out.println(y instanceof AB); //true
        System.out.println(y instanceof ABC); //false
        System.out.println(z instanceof ABC); //true
        System.out.println(z instanceof AB); //true
        System.out.println(z instanceof A); //true

    }
}
