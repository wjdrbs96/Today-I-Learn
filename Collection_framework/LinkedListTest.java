package Collection_framework;

import java.util.LinkedList;

public class LinkedListTest {
    public static void main(String[] args){
        LinkedList<String> mylist = new LinkedList<String>();

        mylist.add("A");
        mylist.add("B");
        mylist.add("C");

        System.out.println(mylist);
        mylist.add(1,"D");
        System.out.println(mylist);

        for(int i=0; i<mylist.size(); i++){
            String s = mylist.get(i);
            System.out.print(s + " ");
        }
        System.out.println();
        for(String s : mylist){
            System.out.println(s);
        }

    }
}
