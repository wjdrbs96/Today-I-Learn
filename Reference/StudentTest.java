package Reference;

public class StudentTest {
    public static void main(String[] args){
        Student stu = new Student(2016,"Lee");
        stu.setKoreaSubject("국어",100);
        stu.setMathSubject("수학",95);

        Student stu1 = new Student(2015,"kim");
        stu1.setKoreaSubject("국어",90);
        stu1.setMathSubject("수학",100);

        stu.ShowStudent();
        stu1.ShowStudent();
    }
}
