import java.util.Arrays;

/*
 * Recursion lab demonstration program.  This program simply runs tests
 * of several recursive methods.
 */
class RDemo {
    /*
     * Search the portion of an array of integers between start and
     * stop (inclusive) for a given value sought. If found, return
     * its subscript. If not, return -1. Note: If start > stop, the
     * represented section of the array is empty, and nothing will be
     * found (return -1).
     */
    // *** Implement this method ***
    public static int find(int[] array, int start, int stop, int tofind) {
        if (start > stop) {
            return -1;
        }
        if (array[start] == tofind) {
            return start;
        } else {
            return find(array, start + 1, stop, tofind);
        }
    }

    /*
     * Print the find array, showing subscripts
     */
    public static void pr_arr(int[] array) {
        for (int i = 0; i < array.length; ++i)
            System.out.printf("%4d ", i);
        System.out.println();

        for (int i = 0; i < array.length; ++i)
            System.out.printf("%4d ", array[i]);
        System.out.println();
    }

    /*
     * Run the find function, printing its arguments (execpt the array) and
     * the result.
     */
    public static void find_test(int[] array, int start, int stop, int tofind) {
        try {
            System.out.println("find(array, " + start + ", " + stop + ", " +
                    tofind + ") => " +
                    find(array, start, stop, tofind));
        } catch (StackOverflowError e) {
            wentBoom(e, "find(" + Arrays.toString(array) + ", " + start + ", " +
                    +stop + ", " + tofind + ")");
        } catch (Exception e) {
            wentBoom(e, "find(" + Arrays.toString(array) + ", " + start + ", " +
                    +stop + ", " + tofind + ")");
        }
    }

    /*
     * Return a string representing num in the indicated base. The base should
     * be in the range 2 to 36 (inclusive). The returned string represents
     * digit values in the range 10-36 with the uppercase letters.
     */
    public static String basecon(int num, int base) {
        if (num == 0)
            return "0";
        else if (num < 0)
            return "-" + baseconR(-num, base);
        else
            return baseconR(num, base);
    }

    /*
     * Gives a string of the correct digit for each value from 0 to 35.
     * This is useful in writing baseconR which follows. That is, if
     * d is the value of a digit in some base 2 to 36, digitStrings[d]
     * is the digit used to represent it.
     */
    public static String[] digitStrings = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9",
            "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M",
            "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z" };

    /*
     * This does the real work for basecon. Likewise, it takes a number and
     * a base (2-36) and returns a string representing the number in that
     * base, with these differences:
     * o The parameter num is assumed to be non-negative.
     * o If num is zero, return the empty string.
     */
    // *** Implement this method ***
    public static String baseconR(int num, int base) {
        if (num != 0) {
            int q = num / base;
            int r = num % base;
            return baseconR(q, base) + digitStrings[r];
        } else {
            return "";
        }
    }

    /*
     * Run basecon with the indicated values and print the parameters and
     * result.
     */
    public static void basecon_test(int num, int base) {
        try {
            System.out.println(num + " in base " + base + " is " +
                    basecon(num, base));
        } catch (StackOverflowError e) {
            wentBoom(e, "basecon(" + num + ", " + base + ")");
        } catch (Exception e) {
            wentBoom(e, "basecon(" + num + ", " + base + ")");
        }
    }

    /*
     * Evaluate a polynomial represented by an array of coefficients. The
     * coefficients are stored from hightest order to lowest, and the degree
     * of the polynomial will be one less than the length of the array.
     * Takes a value for x and the array of coefficients and returns the value.
     */
    public static double polyval(double x, double[] coeffs) {
        return polyvalR(x, coeffs, coeffs.length);
    }

    /*
     * This does the work for polyval. Does the same thing, except that
     * it considers only the first size members of coeffs, and ignores
     * the rest of them. Size will be less than or equal to the length
     * of the array; just pretend the length is size. The size may be
     * zero, in which case the function should return zero.
     */
    // *** Implement this method ***
    public static double polyvalR(double x, double[] coeffs, int size) {
        if (size == 1) {
            return coeffs[0];
        } else {
            double re = polyvalR(x, coeffs, size - 1);
            return x * re + coeffs[size - 1];
        }
    }

    /*
     * Run polyval and print its arguments and result.
     */
    public static void polyval_test(double x, double[] coeffs) {
        String sep = "";
        for (int i = 0; i < coeffs.length; ++i) {
            System.out.print(sep + coeffs[i]);
            int pwr = coeffs.length - i - 1;
            if (pwr > 0) {
                System.out.print("x");
                if (pwr > 1)
                    System.out.print("^" + pwr);
            }
            sep = " + ";
        }

        try {
            System.out.println(" [x=" + x + "] = " + polyval(x, coeffs));
        } catch (StackOverflowError e) {
            wentBoom(e, "polyval(" + x + ", " + Arrays.toString(coeffs) + ")");
        } catch (Exception e) {
            wentBoom(e, "polyval(" + x + ", " + Arrays.toString(coeffs) + ")");
        }
    }

    /*
     * Return the base raised to the indicated power. Assume pwr >= 0.
     */
    // *** Implement this method ***
    public static double raise(double base, int pwr) {
        if (pwr > 1) {
            return base * raise(base, pwr - 1);
        } else {
            return base;
        }

    }

    /*
     * Call raise, and print the arguments and the method's result.
     */
    public static void raise_test(double base, int pwr) {
        try {
            System.out.println(base + "^" + pwr + " = " + raise(base, pwr));
        } catch (StackOverflowError e) {
            wentBoom(e, "raise(" + base + ", " + pwr + ")");
        } catch (Exception e) {
            wentBoom(e, "raise(" + base + ", " + pwr + ")");
        }
    }

    public static void main(String[] args) {
        // Test the find() function.
        System.out.println("== testing find() ==");
        int a1[] = { 4, 18, 91, 1, 3, 17, 33, 54, 11, 39 };
        pr_arr(a1);
        find_test(a1, 1, 7, 91);
        find_test(a1, 1, 7, 39);
        find_test(a1, 1, 7, 4);
        find_test(a1, 1, 7, 62);
        find_test(a1, 0, 9, 33);
        find_test(a1, 0, 9, 44);
        find_test(a1, 3, 9, 39);
        find_test(a1, 5, 5, 17);
        find_test(a1, 5, 5, 18);
        find_test(a1, 9, 9, 19);
        find_test(a1, 9, 9, 39);
        find_test(a1, 0, 0, 18);
        find_test(a1, 0, 0, 4);
        System.out.println();

        // Test the base converter.
        System.out.println("== testing basecon() ==");
        basecon_test(39873, 17);
        basecon_test(-10, 2);
        basecon_test(4987, 5);
        basecon_test(-32987, 20);
        basecon_test(987291, 36);
        basecon_test(77, 3);
        basecon_test(-87635, 16);
        basecon_test(12, 21);
        System.out.println();

        // Test the polynomial evaluator
        System.out.println("== testing polyval() ==");
        double p1[] = { 5.1, -2.0, 1.1, 3.0 };
        polyval_test(10.5, p1);
        polyval_test(-17.2, p1);
        double p2[] = { -3.1, 11.02, -1.1, 4.0, 0.0 };
        polyval_test(10.5, p2);
        polyval_test(-17.2, p2);
        System.out.println();

        // Test raise-to-power
        System.out.println("== testing raise() ==");
        raise_test(5.3, 2);
        raise_test(1.3, 6);
        raise_test(-15.3, 4);
        raise_test(15.3, 11);
        raise_test(-7, 5);
        raise_test(19.45, 22);
        raise_test(2.2, 0);
    }

    public static void wentBoom(Exception e, String msg) {
        wentBoom(e.toString(), e.getStackTrace(), msg);
    }

    public static void wentBoom(StackOverflowError e, String msg) {
        wentBoom(e.toString(), e.getStackTrace(), msg);
    }

    // Print a message and stack trace from an exception.
    public static void wentBoom(String emsg, StackTraceElement[] traces,
            String msg) {
        // Print the basic info.
        System.out.println("Exception thrown on " + msg);
        System.out.println("  " + emsg);

        // Get the stack trace.
        int rpt_cnt = 0;
        for (int i = 0; i < traces.length; ++i) {
            // See if this entry is a repeat.
            if (i > 0 && traces[i].equals(traces[i - 1])) {
                ++rpt_cnt;
            } else {
                // If we're ending a long repeat, show the count.
                if (rpt_cnt >= 3)
                    System.out.println("     ... appears " + (rpt_cnt + 1) +
                            " times ...");
                rpt_cnt = 0;
            }

            // Print the trace item, but suppress repeating runs.
            if (rpt_cnt < 3) {
                System.out.println("   " + traces[i]);
            }
        }
        if (rpt_cnt >= 3)
            System.out.println("     ... appears " + (rpt_cnt + 1) +
                    " times ...");
    }
}
