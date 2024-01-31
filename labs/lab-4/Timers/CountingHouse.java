package Timers;

import java.util.*;

/*
 * This object holds the comparision counts.  It contains factory 
 * methods for objects suitable for sorting or searching which count
 * their comparisions.
 */

class CountingHouse {
    /* Counter manipulation. */
    private int m_cmpcount = 0;

    public int get_cnt() { return m_cmpcount; }
    public void clr_cnt() { m_cmpcount = 0; }
    public void inc() { ++m_cmpcount; }

    /*
     * The type of object actually sorted or searched.  It wraps an Integer
     * and counts the number of comparisions by overloading equals and
     * compareTo.  May generate others later.
     */
    public class CountedInteger implements Comparable<CountedInteger> {
        CountedInteger(int a) { m_val = a; }

        public boolean equals(Object obj) {
            return equals((CountedInteger)obj);
        }
        public boolean equals(CountedInteger obj) {
            ++m_cmpcount;
            return m_val == obj.m_val;
        }
        public int compareTo(CountedInteger aci) {
            ++m_cmpcount;
            return m_val - aci.m_val;
        }

        public String toString() {
            return Integer.toString(m_val);
        }

        public int intValue() { return m_val; }

        private int m_val;
    }

    // Create a new counted integer object.
    public CountedInteger new_int(int v) { return new CountedInteger(v); }
    
    // Random integers.
    private Random m_rand = new Random();// For making random test data.

    public void setSeed(int s) { m_rand.setSeed(s); }

    public int get_int(int limit) {
        return m_rand.nextInt(limit);
    }

    public int get_int(int low, int hi) {
        return m_rand.nextInt(hi - low + 1) + low;
    }

    public CountedInteger new_rand_int(int low, int hi) {
        return new CountedInteger(get_int(low, hi));
    }

    // Create an array of random CountedIntegers.
    public int [] fill_rand_int(int size, int min, int max)
    {
        int rangesize = max - min + 1;
        int[] ret = new int[size];
        for(int i = 0; i < size; ++i) {
            ret[i] = get_int(min, max);
        }
        return ret;
    }

    // Create an array of random CountedIntegers.
    public CountedInteger [] fill_rand(int size, int min, int max)
    {
        int rangesize = max - min + 1;
        CountedInteger[] ret = new CountedInteger[size];
        for(int i = 0; i < size; ++i) {
            ret[i] = new_rand_int(min, max);
        }
        return ret;
    }

    // Create a sequential array of random(-ish) data which is increasing.
    // (Or decreasing, if reverse is true).
    public int [] fill_rand_ordered_int(int size, int min, int max,
                                        boolean reverse)
    {
        // Find a step which describes a ramp between the ranges.
        double step = (double)(max - min) / (double)(size-1);

        // This will smoothly go from the min to the max.
        double limit = min;

        // This is the last number we chose.
        int last = min;

        int[] ret = new int[size];
        for(int i = 0; i < size; ++i) {
            // Pick a number between the last and the ramp limit.
            int newsel = m_rand.nextInt((int)(limit - last + 1.9)) + last;
            ret[reverse ? size-i-1 : i] = newsel;

            // Proceed to next step.
            limit += step;
            last = newsel;
        }
        return ret;
    }

    // Create a sequential array of random(-ish) data which is increasing.
    // (Or decreasing, if reverse is true).
    public CountedInteger [] fill_rand_ordered(int size, int min, int max,
                                               boolean reverse)
    {
        int[] ints = fill_rand_ordered_int(size, min, max, reverse);
        CountedInteger[] ret = new CountedInteger[size];
        for(int i = 0; i < size; ++i) ret[i] = new CountedInteger(ints[i]);
        return ret;
    }
}
