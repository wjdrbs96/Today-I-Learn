package inheritance.abstractinterface;

public class Abstract {
    public static void main(String []args){
        Shape r = new Rectangle(2,3,3.67,7.89); //업캐스팅
        Shape c = new Circle(3,4,4.82);  //업 캐스팅

        r.draw();   // 동적바인딩에 의하여 Reactangle 클래스 draw 호출
        c.draw();  // 동적바인딩에 의하여 Circle 클래스 draw 호출

        Shape sa[] = {new Rectangle(2.5,3.1,1.67,3.89), new Circle(4.2,3.8,1.56)};
        // 이렇게 업캐스팅을 배열을 이용해서 할 수 있다는 것도 알아두기

        sa[0].draw();
        sa[1].draw();
        //위에와 같지만 표현을 배열을 이용해서 하냐 안하냐의 차이




    }
}
