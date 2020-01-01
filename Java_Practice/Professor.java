package Java_Practice;

public class Professor {
    String dept;
    int uNumber;

    public Professor(int uNumber){
        this.uNumber = uNumber;
    }

    public Professor(String dept){
        this.dept = dept;
    }

    public Professor(int uNumber, String dpet){
        this(uNumber);
        this.dept = dept;
    }

    public static void main(String[] args){

    }
}
