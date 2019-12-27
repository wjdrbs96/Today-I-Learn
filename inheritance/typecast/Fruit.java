package inheritance.typecast;

// Fruit 을 부모로 아래 자식들이 Apple, Pear, Grape 가 있음
public class Fruit {
    public static void main(String []args){
        Fruit f = new Fruit();
        System.out.println(f instanceof Fruit); //true
        System.out.println(f instanceof Apple); //False, 참조변수 f 는 Fruit 만 참조 가능
        System.out.println(f instanceof Grape); //False
        Fruit fa = new Apple();
        System.out.println(fa instanceof Fruit); //true, 참조변수 fa는 fruit 참조 가능
        System.out.println(fa instanceof Apple); //true, 참조변수 fa는 Apple 도 true (업캐스팅 상태)
        System.out.println(fa instanceof Pear); //False, Pear 랑 fa 랑은 관련이 없음
        Apple a = new Apple();
        System.out.println(a instanceof Fruit); //ture, 참조변수 a로 Fruit 참조 할 수 있음
        System.out.println(a instanceof Apple); //true
    }
}
class Apple extends Fruit{}
class Pear extends Fruit{}
class Grape extends Fruit{}
