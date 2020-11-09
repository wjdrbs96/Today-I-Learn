package ExampleCode;

import java.io.IOException;
import java.sql.SQLException;

public class Test {
    private int grade;
    private int age;

    void setGrade(int grade) throws IOException {
        if (grade < 1 || grade > 6) {
            throw new IOException("학년 에러");
        }
        this.grade = grade;
    }

    void setAge(int age) throws SQLException {
        if (age < 0) {
            throw new SQLException("나이 에러");
        }
        this.age = age;
    }

    public static void main(String[] args) {
        Test test = new Test();

        try {
            test.setAge(-1);
            test.setGrade(-1);
        } catch (SQLException sqe) {
            sqe.printStackTrace();
        } catch (IOException io) {
            io.printStackTrace();
        } finally {
            System.out.println("무조건 실행되는 동작");
        }
    }
}
