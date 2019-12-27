package APIClass;

public class StringBufferInfo {
    public static void main(String []args){
        StringBuffer pg = new StringBuffer("JAVA");  //기본 용량 16
        System.out.println(pg.capacity());  // 기본 용량 16 + 4 = 20
        System.out.println(pg.append(" language"));
        System.out.println(pg.capacity()); // 20 + language 가 되어야 할거 같은데 20 이 나오네
        System.out.println(pg.insert(5,"programming "));
        System.out.println(pg.capacity()); // 이건 왜 42여 ?
        System.out.println(pg);
        System.out.println(pg.replace(0,4,"Objective-C"));
        System.out.println(pg.substring(0,9));
        System.out.println(pg.charAt(10));  // 지정된 인덱스를 가져옴
        pg.setCharAt(10,'D');  // 지정된 인덱스에 D 를 지정
        System.out.println(pg);
    }
}
