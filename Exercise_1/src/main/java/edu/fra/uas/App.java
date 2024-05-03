package edu.fra.uas;

import java.util.HashMap;
import java.util.Map;

public class App {
    private static final Map<Long, Integer> memory = new HashMap<>();

    public static void main(String[] args) {
        int limit = 10;
        int maxSteps = 0;
        int numberWithMaxSteps = 1;

        for (int i = 1; i < limit; i++) {
            int steps = findStepsToReachOne(i);
            System.out.println("Find step " + i + " with max steps " + steps);
            if (steps > maxSteps) {
                maxSteps = steps;
                numberWithMaxSteps = i;
            }
        }

        System.out.println("Verified the ULAM function terminates at 1 for all integers < " + limit);
        System.out.println("Number with the maximum steps: " + numberWithMaxSteps + " with " + maxSteps + " steps.");

    }

    private static int findStepsToReachOne(long n) {
        if (n == 1) return 0;

        if (!memory.isEmpty() && memory.containsKey(n)) return memory.get(n);

        long next = n % 2 == 0 ? n / 2 : 3 * n + 1;
        int steps = 1 + findStepsToReachOne(next);

        memory.put(n, steps);
        return steps;
    }
}
