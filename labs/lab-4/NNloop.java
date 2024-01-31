import Timers.*;

// The simple double loop from p. 456 in your text.
public class NNloop extends CountableN {
    public void run(int n) {
        for (int count = 0; count < n; count++) {
            for (int count2 = 0; count2 < n; count2++) {
                cnt();
            }
            for (int count2 = 0; count2 < n; count2++) {
                cnt();
            }
        }
    }
}
