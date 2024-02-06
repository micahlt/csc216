import java.util.*;

/*
 * This program tests the pair class by (twice) making an arry of them and
 * sorting that array to make sure the compareTo works correctly.
 */
class sorter {
    /*
     * This is a generic function which prints out an array of anything,
     * at 6 items per line.
     */
    public static <T> void dump(T[] arr)
    {
        final int LINESIZE = 6;
        int linect = 0;
        for(T p: arr) {
            System.out.print(p + " ");
            if(++linect == LINESIZE) {
                System.out.println();
                linect = 0;
            }
        }
        if(linect > 0)
            System.out.println();
        System.out.println();
    }
    public static void main(String[] args)
    {
        // Just make sure these work.
        Pair<String, Boolean> p = new Pair<String, Boolean>("True", true);
        Pair<String, Boolean> q = new Pair<String, Boolean>("False", false);
        System.out.println(p.getFirst() + " " + p.getSecond() + " " +
                           q.getFirst() + " " + q.getSecond());
        System.out.println();

        // Test with an array of Integer, String pairs.
        @SuppressWarnings("unchecked")  // Because Java generics are broken
        Pair<Integer,String>[] arr = new Pair[25];

        String[] names = { "Fred", "Bill", "Alice", "Sally", "Kermit" };
        int sub = 0;
        for(String s: names) {
            for(int i = 10; i < 15; ++i)
                arr[sub++] = new Pair<Integer,String>(i, s);
        }

        dump(arr);
        Arrays.sort(arr);
        dump(arr);

        // Check with pairs of Character and Float.  Note that in this
        // test, the float values are random numbers, so the output
        // will vary from one run to the next.
        @SuppressWarnings("unchecked")  // Because Java generics are broken
        Pair<Character,Float>[] arr2 = new Pair[30];

        Random r = new Random();
        sub = 0;
        String name = "Rosco";
        for(int i = 0; i < 6; ++i) {
            for(int j = 0; j < name.length(); ++j) {
                arr2[sub++] = new Pair<Character,Float>
                    (name.charAt(j), r.nextFloat());
            }
        }

        dump(arr2);
        Arrays.sort(arr2);
        dump(arr2);
    }
}
