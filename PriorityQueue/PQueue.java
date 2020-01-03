package PriorityQueue;

import java.util.PriorityQueue;

public class PQueue {

    public static PriorityQueue<Student> getPriorityQueueStudents(){
        PriorityQueue<Student> pQueue = new PriorityQueue<Student>();

        pQueue.offer(new Student("김철수",20));
        pQueue.offer(new Student("김영희",130));
        pQueue.offer(new Student("이나영",7));
        pQueue.offer(new Student("장경철",43));
        pQueue.offer(new Student("김우진",100));

        return pQueue;
    }

}
