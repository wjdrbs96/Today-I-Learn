# `Java 8에서 변경된 것들은?`

- `Lambda(람다)`
- `stream(스트림)`
- `method reference(메소드 참조)`

이번 글에서는 위의 3개에 대해서 알아보겠습니다.

<br>

## `Lambda(람다)란?`

```java
public class Test {
    public static void main(String[] args) {
        MyFunction f = new MyFunction() {
            @Override
            public int max(int a, int b) {
                return a > b ? a : b;
            }
        }; 
        
        // int t = f.max(5, 3)  익명 객체의 메소드 호출
    }
}


interface MyFunction {
    int max(int a, int b);
}
```

위와 같이 a, b 중에 큰 값을 반환하는 메소드를 가진 인터페이스를 구현할 때는 `익명 클래스`를 이용해야 했습니다. 익명 클래스는 딱 보아도 복잡해보이고 가독성도 떨어진다는 것을 느낄 수 있습니다.

그래서 이를 보완하기 위해 나온 것이 `Lambda(람다) 표현식`입니다.  

```java
public class Test {
    public static void main(String[] args) {
        MyFunction f = (int a, int b) -> a > b ? a : b;
        int big = f.max(5, 3);
    }
}
```

위의 코드를 `람다`를 이용하면 위와 같이 간단히 줄일 수 있습니다. 처음 람다 식을 접하면 어색하고 이해가 가지 않을 수 있으니 하나씩 정리해보겠습니다. 

```
(int a, int b) -> a > b ? a : b;
```

여기서 매개변수에 int 형을 선언하고 있지만 이것은 `생략`이 가능합니다. 왜 생략이 가능할까요? `우리가 호출하고자 하는 메소드의 매개변수가 int 이기 때문에 충분히 유추할 수 있기 때문입니다.`

```
(a, b) -> a > b ? a : b;
```

위와 같이 사용이 가능하다는 뜻입니다. `그러면 메소드가 여러 개 있다면 어떻게 타입 추론을 할 수 있을까요?` 할 수가 없습니다. 그래서 람다를 사용하는 인터페이스는 메소드를 하나만 가질 수 있습니다. 
이를 `기능적 인터페이스(Functional Interface)`라고 합니다. 

하지만 어떠한 예방 장치가 없다면 실수로 인터페이스에 메소드를 추가할 수도 있고 구분이 애매해집니다. 
이럴 때 `@FunctionalInterface` 라는 어노테이션을 사용하면 됩니다. 이 어노테이션을 사용하면 메소드가 2개 이상이 되었을 때 컴파일 에러를 발생시켜서 메소드 1개를 유지할 수 있습니다. 

```java
@FunctionalInterface
public interface Test {
    int operation(int a, int b);
}
```

<br>

이번에는 쓰레드를 만들 때 썼던 `Runnable 인터페이스`를 보겠습니다. 

```java
@FunctionalInterface
public interface Runnable {
    public abstract void run();
}
```

내부 코드를 보면 위와 같이 `@FunctionalInterface`가 붙어 있습니다. `Runnable 인터페이스는 기능 인터페이스`라는 것을 알 수 있습니다. 

```java
public class Test {
    public static void main(String[] args) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                System.out.println(Thread.currentThread().getName());
            }
        };
        new Thread(runnable).start();
    }
}
```

`Runnable`이 기능적 인터페이스이기 때문에 `람다`를 이용해서 간단하게 수정할 수 있습니다. 어떻게 하면 좋을까요?

```java
public class Test {
    public static void main(String[] args) {
        new Thread(() -> System.out.println(Thread.currentThread().getName())).start();
    }
}
```

위와 같이 람다 표현식을 사용할 수 있습니다. 

<br>

## `java.util.functin 패키지`

- `Predicate`
- `Supplier`
- `Consumer`
- `Function`
- `UnaryOperator`
- `BinaryOperator`

대표적으로 위의 인터페이스들이 있습니다. 내용에 대해서는 [java.util.function 정리](https://github.com/wjdrbs96/Today-I-Learn/blob/master/Java/Lambda%20%26%20Stream/java.util.function.Package.md) 를 참고하면 좋을 것 같습니다.

<br>

## `stream 이란?`

자바의 스트림은 `뭔가 연속된 정보`를 처리하는데 사용합니다. 바로 스트림에 대해서 알아보겠습니다. 

```
list.stream().filter(x -> x > 10).count();
``` 

위의 코드를 보았을 때 `stream()을 스트림 생성`, `filter()를 중개 연산`, `count()를 종단 연산`이라고 합니다. 

<br>

- `스트림 생성 : 컬렉션 목록을 스트림 객체로 변환합니다.`
- `중개 연산 : 생성된 스트림 객체를 사용하여 중개 연산 부분에서 처리합니다. 하지만, 이 부분에서는 아무런 결과를 리턴하지 못합니다.`
- `종단 연산 : 마지막으로 중개 연산에서 작업된 내용을 바탕으로 결과를 리턴해 줍니다.`

말로만 들으면 긴가 민가 할 수도 있습니다. 코드를 보면 바로 알 수 있습니다. 

```java
import java.util.ArrayList;
import java.util.List;

public class Test {
    public static void main(String[] args) {
        List<String> names = new ArrayList<>();

        names.add("Gyunny");
        names.add("Spring");
        names.add("Study");

        names.stream().map((s) -> {
            System.out.println(s);
            return s.toUpperCase();
        });
    }
}
```

위의 코드의 결과는 무엇일까요? `정답은 아무 것도 출력되지 않는다` 입니다. 이유가 무엇일까요? 바로 `map()`은 `중개 연산`이기 때문입니다.

위에서 중개 연산은 아무런 결과를 리턴해주지 않는다고 했습니다. 따라서 위의 코드는 `종단 연산`이 없기 때문에 결과가 출력되지 않는 것입니다. 

```java
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Test {
    public static void main(String[] args) {
        List<String> names = new ArrayList<>();

        names.add("Gyunny");
        names.add("Spring");
        names.add("Study");

        List<String> collect = names.stream().map((s) -> {
            System.out.println(s);
            return s.toUpperCase();
        }).collect(Collectors.toList());
    }
}
```

그래서 위와 같이 `collect()`라는 `종단 연산`을 사용해야 결과가 출력됩니다. 

<br>


## `스트림 연산`

### `stream map()`

```java
import java.util.Arrays;
import java.util.List;

public class Test {
    public static void main(String[] args) {
        List<Integer> intList = Arrays.asList(1, 2, 3, 4, 5, 6);

        for (int value : intList) {
            int tempValue = value * 3;
            System.out.print(tempValue + " ");
        }
    }
}
```

위와 같이 List의 원소들을 3씩 곱해서 출력하는 코드를 짠다면 위와 같이 짤 수 있습니다. 

하지만 `stream map()`을 이용하면 더 간단하게 코드를 짤 수 있습니다. 

```java
import java.util.Arrays;
import java.util.List;

public class Test {
    public static void main(String[] args) {
        List<Integer> intList = Arrays.asList(1, 2, 3, 4, 5, 6);

        intList.stream().map(x -> x * 3).forEach(System.out::println);
    }
}
```

위와 같이 `map()`을 사용하면 `x -> x * 3` 조건에 맞는 스트림이 만들어지고 그 스트림을 `forEach()`를 통해서 출력을 하고 있습니다. 
훨씬 코드가 간결한 것을 볼 수 있습니다. 

```java
public class StudentDTO {
    String name;
    int age;
    int scoreMath;
    int scoreEnglish;

    public StudentDTO(String name, int age, int scoreMath, int scoreEnglish) {
        this.name = name;
        this.age = age;
        this.scoreMath = scoreMath;
        this.scoreEnglish = scoreEnglish;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getScoreMath() {
        return scoreMath;
    }

    public void setScoreMath(int scoreMath) {
        this.scoreMath = scoreMath;
    }

    public int getScoreEnglish() {
        return scoreEnglish;
    }

    public void setScoreEnglish(int scoreEnglish) {
        this.scoreEnglish = scoreEnglish;
    }
}
```

위의 `StudentDTO` 라는 클래스가 존재합니다. 

```java
import java.util.ArrayList;
import java.util.List;

public class StudentMapSample {
    public static void main(String[] args) {
        StudentMapSample sample = new StudentMapSample();
        List<StudentDTO> studentDTOList = new ArrayList<>();
        studentDTOList.add(new StudentDTO("규니1", 43, 99, 10));
        studentDTOList.add(new StudentDTO("규니2", 30, 71, 85));
        studentDTOList.add(new StudentDTO("규니2", 32, 81, 75));
        sample.printStudentNames(studentDTOList);
    }

    public void printStudentNames(List<StudentDTO> students) {
        students.stream().map(student -> student.getName()).forEach(System.out::println);
    }
}
```

그리고 DTO 객체의 이름만 출력하고 싶다면 위와 같이 map을 이용해서 name만 따로 출력도 간편하게 할 수 있습니다. 

`map()`은 자주 쓰이는 것이고 그리 어렵지 않으니 잘 익혀두면 좋을 것 같습니다.

<br>

## `stream filter()`

filter는 말 그대로 어떤 것을 걸러낼 때 사용하는 메소드입니다. 예제를 보면 더 쉽게 이해할 수 있습니다. 

```java
import java.util.ArrayList;
import java.util.List;

public class StudentFilterSample {
    public static void main(String[] args) {
        StudentFilterSample sample = new StudentFilterSample();
        List<StudentDTO> studentDTOList = new ArrayList<>();
        studentDTOList.add(new StudentDTO("규니1", 43, 99, 10));
        studentDTOList.add(new StudentDTO("규니2", 30, 71, 85));
        studentDTOList.add(new StudentDTO("규니2", 32, 81, 75));
        sample.printStudentNames(studentDTOList);
    }
    
    public void filterWithScoreForLoop(List<StudentDTO> studentDTOList,  int scoreCutLine) {
        studentDTOList.stream().
                filter(student -> student.getScoreMath() > scoreCutLine)
                .forEach(System.out::println);
                
    }
} 
```

위와 같이 filter를 이용해서 점수 커트라인을 정해서 출력할 수도 있습니다.

예제를 보면 금방 메소드의 사용법을 익힐 수 있을 것입니다. 

<br>

## `Stream을 정리하기`

- 스트림 `생성 - 중간 연산 - 종단 연산`으로 구분됩니다.
- 중간 연산은 데이터를 가공할 때 사용되며 연산 결과로 Stream 타입을 리턴합니다. 따라서 여러 개의 중간 연산을 연결할 수 있습니다.
- 종단 연산은 스트림 처리를 마무리하기 위해서 사용됩니다. 

<br>

### `중간 연산의 종류`

- `filter()`
- `map()`
- `flatMap()`
- `distinct()`
- `sorted()`

<br>

### `종단 연산의 종류`

- `forEach()`
- `toArray()`
- `reduce()`
- `collect()`
- `max(), min(), count()`

<br>

## `추가로 참고 하기`

- [람다(Lambda) 정리](https://github.com/wjdrbs96/Today-I-Learn/blob/master/Java/Lambda%20%26%20Stream/Lambda%EB%9E%80%3F.md)
- [Stream 이란?](https://github.com/wjdrbs96/Today-I-Learn/blob/master/Java/Lambda%20%26%20Stream/Stream.md)
- [Stream 연산](https://github.com/wjdrbs96/Today-I-Learn/blob/master/Java/Lambda%20%26%20Stream/Stream2.md)
- [메소드 참조](https://github.com/wjdrbs96/Today-I-Learn/blob/master/Java/Lambda%20%26%20Stream/methodReference.md)
- [Effective Java item45 - 스트림은 주의해서 사용하라](https://github.com/wjdrbs96/Gyunny-Java-Lab/blob/master/Effective_Java/Effective_Java/7%EC%9E%A5/item45.md)

