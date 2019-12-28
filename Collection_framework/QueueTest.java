package Collection_framework;

import java.util.LinkedList;
import java.util.Queue;

public class QueueTest {
    public static void main(String []args){
        Queue q = new LinkedList();   // Queue 인터페이스와 LinkedList 클래스의 업캐스팅
        //Queue인터페이스 안에 있는 추상메소드들은 LinkedList 안에서 다 구현이 되어 있다.
        //따라서 LinkedList에서 오버라이딩 했기 때문에 참조변수 q 로 접근 가능(동적 바인딩)
        q.add("0");
        q.add("1");
        q.add("2");
        q.add("3");
        while(!q.isEmpty()){
            System.out.println(q.poll());
        }
    }
}
