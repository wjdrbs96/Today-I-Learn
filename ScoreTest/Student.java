package ScoreTest;

import java.util.ArrayList;

public class Student {
    int studentId;
    String studentName;

    ArrayList<Subject> subjectlist;

    public Student(int studnetID, String studentName){
        this.studentId = studentId;
        this.studentName = studentName;
        subjectlist = new ArrayList();
    }

    public void addSubject(String name, int score){
        Subject subject = new Subject(name, score);
        subjectlist.add(subject);
    }

    public void ShowListInfo(){
        int total = 0;
        for(Subject subject : subjectlist){
            total += subject.getScore();
            System.out.println(studentName + "학생의 " +subject.getName() + " 교과목의 성적은 " + subject.getScore() + " 점 입니다.");
        }
        System.out.println(studentName + " 학생의 " + total + " 점 입니다");
    }





}
