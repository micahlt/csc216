import Timers.*;

public class InsertionSort<T extends Comparable<T>> implements Timers.Sorter<T> {
    public void sort(T[] data) {
        for (int index = 1; index < data.length; index++) {
            T key = data[index];
            int position = index;
            while (position > 0 && data[position - 1].compareTo(key) > 0) {
                data[position] = data[position - 1];
                position--;
            }

            data[position] = key;
        }
    }
}
