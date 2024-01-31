package Timers;

import java.lang.*;

/*
 * Varous random utilities.
 */
class Util { 
    //
    // This is for helping load the requested class.  I can't abstract it
    // properly, because of the way Java generics are broken.  But this is
    // sort-a close.
    //   clname         The name of class to load.
    //   iface          The interface we need to cast to.  Geneally sent
    //                  as something like Sorter<CountedInteger>.class
    //   announce       If a class is loaded, print its name before attempting
    //                  to cast it to the final type.
    //
    public static Object get_tested_object(String clname, Class iface,
                                           boolean announce)
    {
        // Attempt to dynamically load the class given by clname.
        ClassLoader cload = Util.class.getClassLoader();
        Class tested_class = null;
        try {
            tested_class = cload.loadClass(clname);
            if(announce)
                System.out.println("Testing class " + tested_class.getName());
        } catch (Exception e) {
            System.out.println("Unable to find class " + clname + ": " + e);
            return null;
        }

        // Try to use the dynamically-loaded class name to instantiate an
        // object to do the sorting.
        try {
            return iface.cast(tested_class.newInstance());
        } catch(ClassCastException cce) {
            System.out.println("Class " + tested_class.getName() + " does " +
                               "not seem to implement interface " +
                               iface.getName() + ".");
            return null;
        } catch(IllegalAccessException iae) {
            System.out.println("Java says " + iae);
            System.out.println("Make sure to make the class public: " +
                               "\"public class " +
                               tested_class.getName() + "<...> ... { ... }\"");
            return null;
        } catch(Exception e) {
            System.out.println("Unable to instantiate class " +
                               tested_class.getName() + ": " + e);
            return null;
        }
    }

    /*
     * Verify that the array is in sorted order.  Return the number of
     * out-of-order points.
     */
    public static int check_order(CountingHouse.CountedInteger[] arr)
    {
        int errct = 0;
        for(int i = 1; i < arr.length; ++i) {
            if(arr[i-1].intValue() > arr[i].intValue()) ++errct;
        }
        return errct;
    }

    /*
     * Compute an order-independent hash (a fancy xor, really) that can
     * be used to detect at least some content errors by the sort.
     */
    public static int oi_hash(CountingHouse.CountedInteger[] arr)
    {
        int ret = 0;
        for(int i = 0; i < arr.length; ++i) {
            int step = arr[i].intValue() ^
                (arr[i].intValue() << (arr[i].intValue() & 0x0f));
            ret ^= step;
        }

        return ret;
    }

    // Print the array.  Just belt it out.  Long ones look ugly; oh well.
    public static <T> void pr_array(String msg, T[] arr)
    {
        System.out.println(msg+":");
        System.out.print(arr[0]);
        for(int i = 1; i < arr.length; ++i)
            System.out.print(", " + arr[i]);
        System.out.println();
    }

    public static void pr_array(String msg, int[] arr)
    {
        System.out.println(msg+":");
        System.out.print(arr[0]);
        for(int i = 1; i < arr.length; ++i)
            System.out.print(", " + arr[i]);
        System.out.println();
    }
            
    // Take a rounded integer average.
    public static long av(long tot, int cnt)
    {
        return (tot + (cnt>>1)) / cnt;
    }

    // Return a formated average to one decimal (for printing).
    public static String fltav(long tot, int cnt)
    {
        double result = (double)tot/(double)cnt;
        return String.format("%3.1f", result);
    }

    // Format an inteval with reasonable units.
    public static String fmt_time(long amt)
    {
        String units = "ns";
        long divisor = 1;

        if(amt < 10000)
            return amt+" ns";
        else if(amt < 1000000) {
            units = "us";
            divisor = 1000;
        }
        else if(amt < 1000000000) {
            units = "ms";
            divisor = 1000000;
        } else {
            units = "s";
            divisor = 1000000000;
        }

        long intpart = amt / divisor;
        long rest = amt % divisor;
        divisor /= 100;
        long fracpart = rest / divisor;
        long discard = rest % divisor;
        if(discard > divisor/2) ++fracpart;
        return String.format("%d.%02d %s", intpart, fracpart, units);
    }

}
