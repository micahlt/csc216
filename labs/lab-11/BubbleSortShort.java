import Timers.*;

public class BubbleSortShort<T extends Comparable<T>> implements Timers.Sorter<T> {
    public void sort(T[] data) {
        int position, scan;

        boolean didSort = false;
        for (position = data.length - 1; position >= 0; position--) {
            for (scan = 0; scan <= position - 1; scan++) {
                if (data[scan].compareTo(data[scan + 1]) > 0) {
                    swap(data, scan, scan + 1);
                    didSort = true;
                }
            }
            if (!didSort) {
                break;
            }
        }
    }

    private void swap(T[] data, int firstIndex, int secondIndex) {
        T temp = data[firstIndex];
        data[firstIndex] = data[secondIndex];
        data[secondIndex] = temp;
    }
}
