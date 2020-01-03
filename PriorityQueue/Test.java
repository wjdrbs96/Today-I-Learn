package PriorityQueue;

import java.util.PriorityQueue;

public class Test {
    public static void main(String[] args){
        PriorityQueue<Student> pQueue = PQueue.getPriorityQueueStudents();

        while(!pQueue.isEmpty()){
            System.out.println(pQueue.poll());
            //우선순위 큐 안에 Student 객체가 있다.
        }
    }
}
