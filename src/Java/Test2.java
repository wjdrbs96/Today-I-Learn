package Java;

import java.util.Collection;

public class Test2 {

    static Test makeTest(Collection<? extends Comparable<?>> c) {
        return new Test();
    }

    public static void main(String[] args) {

    }
}
