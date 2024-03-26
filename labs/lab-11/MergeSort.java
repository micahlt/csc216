public class MergeSort<T extends Comparable<T>>
        implements Timers.Sorter<T> {
    private Comparable[] merge_area;

    public void sort(T[] array)
    {
        // Allocate temp merge space, then start the recursive sort.
        merge_area = new Comparable[array.length];
        sort(array, 0, array.length);
    }

    /*
     * Sort the portion of the array from start (inclusive) through end
     * (exclusive).
     */
    private void sort(T[] array, int start, int end)
    {
        // See if there's anything to sort (more than one item)
        if(end - start > 1) {
            // Divide, and sort each half
            int midpoint = start + (end - start) / 2;
            sort(array, start, midpoint);
            sort(array, midpoint, end);

            // Merge the sorted halves together using space in merge_area.
            int left = start;           // Left sorted data.
            int right = midpoint;       // Right sorted data
            int dest = left;            // Merged data destination.
            while(dest < end) {
                // If we enter, at least one item still needs to be moved.
                if(left < midpoint && (right >= end ||
                                       array[left].compareTo(array[right]) < 0))
                    // The left is not exhaused, and either the right is or
                    // the left is smaller than the right, so we have to move
                    // the left item to the merge area.
                    merge_area[dest++] = array[left++];
                else
                    // We know something needs to be moved, and it wasn't the
                    // left.  Must be the right.
                    merge_area[dest++] = array[right++];
            }

            // Copy back
            System.arraycopy(merge_area, start, array, start, end - start);
        }
    }
}
