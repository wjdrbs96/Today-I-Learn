package Collection_framework;

import java.util.Stack;

public class StackTest {
    public static void main(String []args){
        Stack<Integer> stack = new Stack<Integer>();

        stack.push(1);
        stack.push(2);
        stack.push(3);
        stack.push(4);
        stack.push(5);

        System.out.println(stack.pop());
        System.out.println(stack.pop());
        System.out.println(stack.pop());
        System.out.println(stack.size());  // 현재 스택에 있는 원소의 개수를 반환
        System.out.println(stack.peek());  // 가장 위에 있는 데이터를 꺼내옴

    }
}
