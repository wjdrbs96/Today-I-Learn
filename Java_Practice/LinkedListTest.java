package Java_Practice;

import java.util.LinkedList;
import java.util.Iterator;

public class LinkedListTest {
    public static void print(Iterator it){
        while(it.hasNext()){
            System.out.print(it.next() + " ");
        }
        System.out.println();
    }
    public static void main(String[] args) {
        LinkedList list = new LinkedList();
        list.add("pascal"); list.addLast(1);
        list.add("JAVA"); list.addLast(3.4);
        list.addFirst("algol"); list.addFirst(0.87);
        list.add(3,null); list.addLast(5.8);
        list.hashCode();
        list.toString();
        Iterator it = list.iterator();
        //LinkedList 클래스에서 Collection 인터페이스에 있는 iterator을 오버라이딩 해서 사용가능함
        //it 에는 Iterator 객체가 반한되어서 참조변수인 it 가 객체를 이용가능함
        print(it);  //순방향으로 출력
        print(list.descendingIterator()); //역으로 출력


        for(Object e : list.toArray()){
            System.out.print(e + " ");
        }

        System.out.println();
        Object obj = new Object();
        System.out.println(obj);
        obj.toString();

    }

}
