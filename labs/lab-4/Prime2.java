import Timers.*;

// Print all primes from 1 to N using the class seive method.
public class Prime2 extends CountableN {
    public void run(int n) {
        // Allocate and initialze the seive.  The position
        // seive[p-2] represents the status of p.
        boolean[] seive = new boolean[n-1];
        for(int i = 0; i < n-1; ++i)
            seive[i] = true;
        
        // Keep track of how many are on the line.
        int linecnt = 0;
        
        // Try the numbers.
        for(int p = 2; p <= n; ++p) {
            // See p is prime.
            if(seive[p-2]) {
                // Print if it is.
                System.out.print(p + " ");

                // Make line breaks after every 10 numbers.
                ++linecnt;
                if(linecnt == 10) {
                    System.out.println();
                    linecnt = 0;
                }

                // Eliminate its multiples.
                for(int i = 2*p; i <= n; i += p) {
                    seive[i-2] = false;
                    cnt();
                }
            } else {
                cnt();
            } 
        }
        System.out.println();
    }
}

