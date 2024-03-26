import Timers.*;

public class QuickSort<T extends Comparable<T>> implements Timers.Sorter<T> {
    public void sort(T[] data) {
        quickSort(data, 0, data.length - 1);
    }

    private void quickSort(T[] data, int min, int max) {
        if (min < max) {
            int partitionIndex = partition(data, min, max);
            quickSort(data, min, partitionIndex - 1);
            quickSort(data, partitionIndex + 1, max);
        }
    }

    private int partition(T[] data, int min, int max) {
        T partionEl;
        int left, right;
        int middle = (min + max) / 2;
        partionEl = data[middle];
        swap(data, middle, min);
        left = min;
        right = max;

        while (left < right) {
            // Search for an element that is greater than the partition element
            while (left < right && data[left].compareTo(partionEl) <= 0) {
                left++;
            }
            // Search for an element that is less than the partition element
            while (data[right].compareTo(partionEl) > 0) {
                right--;
            }
            if (left < right) {
                swap(data, left, right);
            }
        }

        swap(data, min, right);
        return right;
    }

    private void swap(T[] data, int firstIndex, int secondIndex) {
        T temp = data[firstIndex];
        data[firstIndex] = data[secondIndex];
        data[secondIndex] = temp;
    }
}
