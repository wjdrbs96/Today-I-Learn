package generics;

public class Mygenerics {
    public static <T> T get(T[] ary, int index){
        return ary[index];
    }

    public static <T> T getLast(T[] ary){
        return ary[ary.length-1];
    }

    public static void main(String []args){
        Integer n[] = {3,4,5,7};  //boxing
        System.out.println(Mygenerics.get(n,2) + " " + Mygenerics.getLast(n));
        String s[] = {"Generics", "type casting", "input", "output"};
        System.out.println(Mygenerics.get(s,1)+" " + Mygenerics.getLast(s));
    }
}
