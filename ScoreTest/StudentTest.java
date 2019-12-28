package ScoreTest;

public class StudentTest {
    public static void main(String []args){
        Student Lee = new Student(101,"Lee");
        Lee.addSubject("국어",100);
        Lee.addSubject("수학",90);

        Student kim = new Student(102,"Kim");
        kim.addSubject("국어",100);
        kim.addSubject("수학",90);
        kim.addSubject("영어",80);

        Lee.ShowListInfo();
        System.out.println("==============");
        kim.ShowListInfo();



    }
}
