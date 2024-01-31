import Timers.*;

// This raises 24 to the power of n.
public class Raise1 extends CountableN {
    public void run(int n) {
        double result = 1.0;
        for(int i = 1; cnt(i <= n); ++i) {
            result *= 24.0;
        }

        System.out.println("24.0 ^ " + n + " = " + result);
    }
}

