package generics;

import java.util.ArrayList;

public class MyContainer<E> {
    private ArrayList<E> list;

    public MyContainer(){
        list = new ArrayList();
    }

    public E get(int index){
        return list.get(index);
    }

    public void add(E element){
        list.add(element);
    }

    public static void main(String []args){
        MyContainer<String> p1 = new MyContainer<String>();
        p1.add("algol"); p1.add("C"); p1.add("Java");
        System.out.print(p1.get(0) + " ");
        System.out.print(p1.get(1) + " ");
        System.out.println(p1.get(2));
    }
}
