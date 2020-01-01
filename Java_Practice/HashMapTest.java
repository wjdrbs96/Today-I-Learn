package Java_Practice;

import java.util.HashMap;
import java.util.Iterator;

public class HashMapTest {
    public static void main(String[] args){
        HashMap<String,String> hm = new HashMap<String,String>();
        hm.put("대한민국","서울");
        hm.put("일본","동경");
        hm.put("중국","북경");
        hm.put("태국","방콕");
        hm.put("중국","북경");

        System.out.println("키 : " + hm.keySet());
        System.out.println("값 : " + hm.values());
        System.out.println(hm);
        hm.toString();
        Iterator<String> keys = hm.keySet().iterator();
        while(keys.hasNext()){
            String key = keys.next();
            String value = hm.get(key);
            System.out.println(key + " : " + value);
        }

    }
}
