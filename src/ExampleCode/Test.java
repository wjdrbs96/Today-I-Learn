package ExampleCode;

import java.util.Arrays;

public class Test {

    public static void main(String[] args) {
        String[] list = {"홍길동", "이발사", "유투버", "네이버"};

        // 출석부 명단 만들기
        String s = checkAttendance(list);
        System.out.println(s.substring(0, s.length() - 1));

        // 전학간 학생 제거
        String s1 = removeStudent(s, "이발사");
        System.out.println(s1.substring(0, s1.length() - 1));

        // 전체출석부 명단을 배열로 만들기
        String[] strings = makeEach(s);
        System.out.println(Arrays.toString(strings));
    }

    // 출석부 명단 만들기
    static String checkAttendance(String[] list) {
        StringBuilder sb = new StringBuilder();
        for (String a : list) {
            sb.append(a.concat("-"));
        }
        return sb.toString();
    }

    // 전학간 학생 제거
    static String removeStudent(String list, String name) {
        String[] split = list.split("-");
        StringBuilder sb = new StringBuilder();

        for (String s : split) {
            if (s.equals(name)) continue;
            sb.append(s.concat("-"));
        }
        return sb.toString();
    }

    // 전체출석부 명단을 배열로 만들기
    static String[] makeEach(String attendance) {
        String[] split = attendance.split("-");
        return split;
    }

}