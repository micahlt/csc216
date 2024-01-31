import Timers.*;

// The simple loop from p. 455 in your text.
public class Nloop extends CountableN {
    public void run(int n) {
        for (int count = 0; count < n; count += 3) {
            cnt();
        }
    }
}
