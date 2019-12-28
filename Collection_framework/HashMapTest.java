package Collection_framework;

import java.util.HashMap;

public class HashMapTest {
    public static void main(String[] args){
        HashMap<String,String> hm = new HashMap<String,String>();

        hm.put("대한민국","서울");
        hm.put("일본","동경");
        hm.put("중국","북경");
        hm.put("태국","방콕");
        hm.put("중국","북경");

        System.out.print("키 : " + hm.keySet());  //키 만 모아서 반환
        System.out.println(" 값 : "  +hm.values()); //값 만 모아서 반환

    }
}
