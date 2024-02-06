/*
 * Make some short tests of the IntAccumulator, StringAccumulator and
 * FactorAccumulator.
 */
class accumtest1 {
    public static <T> void runtest(Accumulator<T> acc, T[] data) {
        for (int i = 0; i < data.length; ++i) {
            System.out.print(data[i] + " ");
            acc.accumulate(data[i]);
        }
        System.out.println("=> " + acc.get());
    }

    public static void main(String[] args) {
        IntAccumulator acc1 = new IntAccumulator();
        Integer[] arr1 = { 4, 9, 8, 13, 9, -2, 15 };
        runtest(acc1, arr1);

        System.out.println();

        StringAccumulator acc2 = new StringAccumulator();
        String[] arr2 = { "Boris", "lives", "here" };
        runtest(acc2, arr2);

        System.out.println();

        FactorAccumulator acc3 = new FactorAccumulator();
        Double[] arr3 = { 0.2, 1.1, 3.8, -0.23, 0.7, 2.2, 1.287, 0.316 };
        runtest(acc3, arr3);

    }
}
