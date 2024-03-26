package Timers;

/*
 * An interface for a class for sorting.  Just has the one method.
 */
public interface Sorter<T extends Comparable<? super T>> {
    public void sort(T[] array);
}
