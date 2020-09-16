package Java;

import java.util.Scanner;

public class Test {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        int a = input.nextInt();
        int b = input.nextInt();
        String s = input.next();

        System.out.println(printString(s, a));
        System.out.println(printString(s, a, b));
    }

    static String printString(String s, int a) {
        return s.substring(a);
    }

    static String printString(String s, int a, int b) {
        return s.substring(a, b);
    }
}




