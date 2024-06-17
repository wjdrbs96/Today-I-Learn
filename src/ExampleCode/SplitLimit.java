package ExampleCode;

public class SplitLimit {
    public static void main(String[] args) {
        String input = "Velad:Device:GW";

        // split limit 옵션
        // 4개로 쪼개겠다는 의미
        String[] split = input.split(":", 4);
        for (String s : split) {
            System.out.println("s: " + s);
        }
    }
}
