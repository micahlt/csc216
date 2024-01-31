import Timers.*;

// Find the minium and maximum values in an array or integer.
public class Arrmax extends CountableA {
    public void run(int[] arr) {
        // set max and min to the first value of the passed array
        int max = arr[0];
        int min = arr[0];
        // loop through array to find max and min
        for(int i = 1; cnt(i < arr.length); i++) {
            if(arr[i] > max) {
                max = arr[i];
                cnt();
            }
            if(arr[i] < min) {
                min = arr[i];
                cnt();
            }
        }
        System.out.println("Min is " + min + ", Max is " + max);
    }
}
