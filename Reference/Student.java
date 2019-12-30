package Reference;

public class Student {
    int studentId;
    String studentName;

    Subject korea ;
    Subject math;

    public Student(int id, String name){
        studentId = id;
        studentName = name;
        korea = new Subject();
        math = new Subject();
    }

    public void setKoreaSubject(String name, int score){
        korea.subjectName = name;
        korea.score = score;
    }

    public void setMathSubject(String name, int score){
        math.subjectName = name;
        math.score = score;
    }

    public void ShowStudent(){
        int total = korea.score + math.score;
        System.out.println(studentName + "의 총점은 " + total+ " 입니다");
    }
}
