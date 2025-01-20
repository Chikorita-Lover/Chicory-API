package net.chikorita_lover.chicory;

public final class MathUtil {
    public static int lcm(int a, int b) {
        if (a == 0 || b == 0) {
            return 0;
        }
        a = Math.abs(a);
        b = Math.abs(b);
        int max = Math.max(a, b);
        int min = Math.min(a, b);
        int lcm = max;
        while (lcm % min != 0) {
            lcm += max;
        }
        return lcm;
    }

    public static int lcm(Integer[] ints) {
        if (ints.length == 0) {
            return 0;
        }
        if (ints.length == 1) {
            return Math.abs(ints[0]);
        }
        int lcm = lcm(ints[0], ints[1]);
        for (int i = 2; i < ints.length; ++i) {
            lcm = lcm(lcm, ints[i]);
        }
        return lcm;
    }
}
