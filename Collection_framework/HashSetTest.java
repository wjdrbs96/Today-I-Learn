package Collection_framework;

import java.util.HashSet;

public class HashSetTest {
    public static void main(String[] args){
        //인터페이스 Set은 중복을 허용하지 않는다.

        HashSet<Integer> setA = new HashSet<Integer>();
        setA.add(3); setA.add(5); setA.add(7);
        setA.add(8); setA.add(7); setA.add(9);
        System.out.print("A = ");
        System.out.println(setA);

        HashSet<Integer> setB = new HashSet<Integer>();
        setB.add(5); setB.add(3); setB.add(2);
        System.out.print("B = ");
        System.out.println(setB);

        boolean isChanged = setA.removeAll(setB); //setB 의 내용을 SetA 에서 모두 삭제하라는 메소드
        System.out.print("A-B = ");
        System.out.println(setA);
    }
}
