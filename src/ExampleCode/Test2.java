package ExampleCode;

public class Test2 {
    public static void main(String[] args) {
        MyClass myClass1 = new MyClass(1);
        MyClass myClass2 = new MyClass(2);
        swap(myClass1, myClass2);
        System.out.println(myClass1.getIndex());
        System.out.println(myClass2.getIndex());

    }

    static void swap(MyClass m1, MyClass m2) {
        int tmpIndex = m1.getIndex();
        m1.setIndex(m2.getIndex());
        m2.setIndex(tmpIndex);
    }
}

class MyClass {
    private int index;

    public MyClass(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}