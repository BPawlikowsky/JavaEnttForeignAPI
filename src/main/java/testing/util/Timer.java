package testing.util;

import java.util.Stack;
import java.util.Vector;

public class Timer {
    private static final Vector<String> results = new Vector<>(1000);
    private static long start;
    private static final Stack<String> titles = new Stack<>();

    public static void start(String title) {
        start = System.nanoTime();
        titles.push(title);
    }

    public static void end() {
        var end = System.nanoTime();
        var time = end - start;

        var result = "[RESULT](" + titles.pop() + ") Time taken: " + time * 0.000001f + "ms";
        results.addElement(result);
    }

    public static Vector<String> getResults() {
        return results;
    }
}
