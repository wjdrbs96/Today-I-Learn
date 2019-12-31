package Javalang_Class;

public class WrapperCharacter {
    public static void main(String[] args){
        Character ch = '@';
        System.out.print(ch + " ");
        System.out.print(ch.charValue() + " ");
        System.out.println(ch.toString());
        System.out.println(ch.compareTo('@') + " ");
        System.out.println(ch.hashCode());
        System.out.println();

        System.out.print(Character.toUpperCase('d') + " ");
        System.out.println(Character.toLowerCase('F'));
        System.out.print(Character.getName('@') + ", ");
        System.out.println(Character.getName('l'));
        //Character 클래스의 getName (int codePoint) 메소드는 지정된
        // 문자의 유니 코드 이름을 나타내는 문자열을 리턴하거나 코드
        // 포인트가 지정되지 않은 경우 널을 리턴합니다.

        System.out.println(Character.isDigit('$') + " "); //$는 정수가 아니므로 false
        System.out.println(Character.isDigit('5'));   // 5 는 정수이므로 true
    }
}
