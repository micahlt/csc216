/*
 * This accumulates integers.
 */
class IntAccumulator implements Accumulator<Integer> {
    private int tot;

    IntAccumulator() {
        tot = 0;
    }

    public void accumulate(Integer more) {
        tot += more;
    }

    public Integer get() {
        return tot;
    }

}
