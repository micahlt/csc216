import Timers.*;

// This raises 24 to the power of n.
public class Raise2 extends CountableN {
    public void run(int n) {
        double result = 1.0;
        double base = 24.0;
        int pwr = n;
        while(cnt(pwr >= 1)) {
            if(pwr % 2 == 0) {
                pwr = pwr/2;
                base *= base;
            } else {
                result *= base;
                --pwr;
            }
        }

        System.out.println("24.0 ^ " + n + " = " + result);
    }
}

