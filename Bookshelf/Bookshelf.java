package Bookshelf;

public class Bookshelf extends Shelf implements Queue{

    public void enq(String title){
        shelf.add(title);
    }

    public String deq(){
        return shelf.remove(0);
    }

    public int getSize(){
        return getCount();
    }


}
