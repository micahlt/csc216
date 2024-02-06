public interface Accumulator<T> {
    /**
     * Constructor for the accumulator
     * 
     * @param more - generic data to add to the accumulation
     */
    public void accumulate(T more);

    /**
     * Get the current accumulation
     * 
     */
    public T get();
}
