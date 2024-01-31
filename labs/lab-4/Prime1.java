import Timers.*;

// Print all primes from 1 to N.
public class Prime1 extends CountableN {
    public void run(int n) {
        // Keep track of how many are on the line.
        int linecnt = 0;

        // Try the numbers.
        for (int p = 2; p <= n; ++p) {
            // See if it's prime.
            boolean isprime = true;
            for (int d = 2; cnt(isprime && d <= p / 2); ++d) {
                if (p % d == 0) {
                    isprime = false;
                }
            }

            // Print if prime.
            if (isprime) {
                System.out.print(p + " ");

                // Make line breaks after every 10 numbers.
                ++linecnt;
                if (linecnt == 10) {
                    System.out.println();
                    linecnt = 0;

                    cnt();
                }
            }
        }
        System.out.println();
    }
}
