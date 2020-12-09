package ExampleCode.Exam;

import java.util.Collections;
import java.util.TreeSet;

public class TreeMaxMin {

    static Integer getMin(TreeSet<Integer> treeSet) {
        return Collections.min(treeSet);
    }

    static Integer getMax(TreeSet<Integer> treeSet) {
        return Collections.max(treeSet);
    }

    public static void main(String[] args) {
        TreeSet<Integer> treeSet = new TreeSet<>();
        treeSet.add(5); treeSet.add(7); treeSet.add(4);
        treeSet.add(6); treeSet.add(1); treeSet.add(8);
    }
}
