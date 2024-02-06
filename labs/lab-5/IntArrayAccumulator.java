public class IntArrayAccumulator implements Accumulator<Integer[]> {
    private IntAccumulator[] accumArray;

    IntArrayAccumulator(int size) {
        accumArray = new IntAccumulator[size];
        for (int i = 0; i < size; i++) {
            accumArray[i] = new IntAccumulator();
        }
    }

    public void accumulate(Integer[] more) {
        for (int i = 0; i < Math.min(accumArray.length, more.length); i++) {
            accumArray[i].accumulate(more[i]);
        }
    }

    public Integer[] get() {
        Integer[] result = new Integer[accumArray.length];
        for (int i = 0; i < accumArray.length; i++) {
            result[i] = accumArray[i].get();
        }
        return result;
    }
}
