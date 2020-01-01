package Bookshelf;

public class BookShelfTest {
    public static void main(String[] args){
        Queue bq = new Bookshelf();
        bq.enq("태백산백1");  // 책장에서 꺼낼 때 Queue 를 구현
        bq.enq("태백산백2");
        bq.enq("태백산백3");

        System.out.println(bq.deq());
        System.out.println(bq.deq());
        System.out.println(bq.deq());
    }
}
