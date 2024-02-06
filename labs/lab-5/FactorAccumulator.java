public class FactorAccumulator implements Accumulator<Double> {
    private double tot;

    FactorAccumulator() {
        tot = 0.0;
    }

    public void accumulate(Double more) {
        tot *= more;
    }

    public Double get() {
        return tot;
    }
}
