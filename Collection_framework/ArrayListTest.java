package Collection_framework;

import java.util.ArrayList;

public class ArrayListTest {
    public static void main(String []args){
        ArrayList<String> list = new ArrayList<String>();
        list.add("a");
        list.add("b");
        list.add("c");

        for(int i=0; i<list.size(); i++){
            String s = list.get(i);
            System.out.println(s);
        }

    }
}
