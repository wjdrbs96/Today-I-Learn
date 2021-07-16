# `stream map() 이란 무엇일까?`

stream은 Java 8에서 추가된 기능입니다. stream은 `뭔가 연속된 정보`를 처리하는 데 사용됩니다. 이번 글에서는 스트림에서 제공하는 많은 연산 중에서 `map()` 사용 법에 대해서 알아보겠습니다. 

<br>

## `map()`

- map()은 데이터를 특정 데이터로 변환하는데 사용됩니다. 

<br>

글로만 보면 무슨 말인가 싶을 수도 있지만 코드를 보면 바로 이해할 수 있습니다. 

```java
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
```

이처럼 어떤 데이터를 변환하는데 사용되는 메소드가 `map()` 입니다.