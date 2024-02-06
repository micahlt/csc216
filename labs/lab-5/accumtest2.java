/*
 * Test the integer array accumulator.
 */
class accumtest2 {
    public static void main(String[] args)
    {
        IntArrayAccumulator ia = new IntArrayAccumulator(10);

        Integer[] arr1 = { 4, 9, 8, 13, 9, -2, 15 };
        Integer[] arr2 = { 7, 18, 3, 33, 4, -8, 19, 5, 22, 14, 44, 18, 2 };
        Integer[] arr3 = new Integer[10];
        for(int i = 1; i <= 10; ++i) arr3[i-1] = i;

        ia.accumulate(arr1);
        ia.accumulate(arr2);
        ia.accumulate(arr3);

        Integer[] result = ia.get();
        for(Integer i: result)
            System.out.print(i + " ");
        System.out.println();
    }
}
