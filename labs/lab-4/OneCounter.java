import Timers.*;

public class OneCounter extends CountableN {
    // Count all the one bits in the binary represtation of n.
    public void run(int n) {
        int totone = 0;
        while (n > 0) {
            if (n % 2 == 1) {
                ++totone;
            }
            n = n / 2;
            cnt();
            cnt();
        }
    }
}
