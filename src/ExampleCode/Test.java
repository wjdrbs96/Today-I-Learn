package ExampleCode;

import java.util.ArrayList;
import java.util.List;

public class Test {
    public static void main(String[] args) {
        Student student1 = new Student("이름1", 100, 5);
        Student student2 = new Student("이름2", 50, 3);
        Student student3 = new Student("이름3", 60, 4);

        List<Student> students = new ArrayList<>();
        students.add(student1);
        students.add(student2);
        students.add(student3);

        students.stream()
                .map(Student::getName)
                .forEach(System.out::println);
    }

}

class Student {
    private String name;
    private int score;
    private int grade;

    public Student(String name, int score, int grade) {
        this.name = name;
        this.score = score;
        this.grade = grade;
    }

    public String getName() {
        return name;
    }

}

