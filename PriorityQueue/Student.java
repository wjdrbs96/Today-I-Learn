package PriorityQueue;

public class Student implements Comparable<Student>{
    //Comparable 은 java.lang 에 속하는 인터페이스이다.
    String name;
    int age;


    public Student(String name, int age){
        this.name = name;
        this.age = age;
    }

    //Comparable 인터페이스의 추상메소드 오버라이딩
    public int compareTo(Student target){
        if(this.age <= target.age){  // 내림차순
            System.out.printf("%d %d %n", this.age, target.age);
            return 1;
        }
        else{
            System.out.printf("%d %d %n", this.age, target.age);
            return -1;
        }
    }

    public String toString(){
        return "이름 : " + name + ", 점수 : " + age;
    }
}
