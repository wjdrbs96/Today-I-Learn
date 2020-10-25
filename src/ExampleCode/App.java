package ExampleCode;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class App {
    public static void main(String[] args) {
        List<OnlineClass> springClasses = new ArrayList<>();

        springClasses.add(new OnlineClass(1, "spring boot", true));
        springClasses.add(new OnlineClass(1, "spring data jpa", true));
        springClasses.add(new OnlineClass(1, "spring mvc", false));
        springClasses.add(new OnlineClass(1, "spring core", false));
        springClasses.add(new OnlineClass(1, "rest api development", false));

        OnlineClass spring_boot = new OnlineClass(1, "spring boot", true);
        Duration studyDuration = spring_boot.getProgress().getStudyDuration();
        System.out.println(studyDuration);
    }
}
