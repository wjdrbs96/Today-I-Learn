package ExampleCode;

public class Person {
    private String name;
    private int age;
    private String currentLocation;

    void setAge(int age) throws Exception {
        if (age < 0) {
            throw new Exception("나이가 음수입니다");
        }
        this.age = age;
    }

    public static void main(String[] args) {
        Person person = new Person();
        try {
            person.setAge(-1);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }
}
