import Timers.*;

// Generate all strings of ABC having length n.
public class ABC extends CountableN {
    public void run(int n) {
        char[] letters = new char[n];
        for(int i = 0; i < n; ++i) {
            letters[i] = 'A';
            cnt();
        }
        
        boolean done = false;
        while(!done) {
            // Print the current set of letters.
            System.out.println(letters);
            
            // Roll all the trailing C's back to A's.  May be none.
            int i = n-1;
            while(i >= 0 && letters[i] == 'C') {
                letters[i] = 'A';
                --i;
                cnt();
            }

            // Advance the left-most non-A.
            if(i < 0) {
                done = true;
            } else {
                ++letters[i];
            }
            cnt();
        }
    }
}

