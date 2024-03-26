
/* 
 * This class implements a selection sort based on the code from Lewis & Chase,
 * but which implements the Timers.Sorter<> interface, so it can be
 * measured by the Timers package.
 */
import Timers.*;

public class SelectionSort<T extends Comparable<T>>
        implements Timers.Sorter<T> {
    public void sort(T[] data) {
        for (int index = 0; index < data.length - 1; index++) {
            int min = index;
            for (int scan = index + 1; scan < data.length; scan++) {
                if (data[scan].compareTo(data[min]) < 0) {
                    min = scan;
                }
            }
            swap(data, min, index);
        }
    }

    private void swap(T[] data, int firstIndex, int secondIndex) {
        T temp = data[firstIndex];
        data[firstIndex] = data[secondIndex];
        data[secondIndex] = temp;
    }
}
