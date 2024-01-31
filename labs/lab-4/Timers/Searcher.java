package Timers;

/*
 * An interface for a class for sorting.  Just has the one method.
 */
public interface Searcher<T extends Comparable<? super T>> {
    /*
     * Insert an item into the collection to be searched.  It should be
     * find-able by the find method.
     */
    public void insert(T item);

    /*
     * Search fo the indicated item.  Return null if not found, and return a
     * reference to the found item (or an equal one) if found.
     */
    public T find(T item);
}
