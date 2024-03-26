package Timers;

import java.lang.*;
import java.util.*;

/*
 * The main program for an arbitrary test function.
 */
class Counter {
    /* Parameter settings. */
    private static Parms m_parms = new Parms();

    /* This manages the comparison counting, and provides some utilities. */
    private static CountingHouse m_counter;

    /* For measuring sort duration. */
    private static long m_totalduration = 0;

    /* Total operations. */
    private static int m_totalcount = 0;

    /* Bits for managing the properties of the specific subclass used. */
    private static final int C_N = 1;           // Bits for each class
    private static final int C_A = 2;
    private static final int C_NA = 4;
    private static final int C_NN = 8;
    private static final int C_AA = 16;
    private static int algtype = 0;             // Bit for actual class used.
    private static final int C_ARR = C_A|C_NA|C_AA;// Class that needs an array.
    private static final int C_1ARR = C_A|C_NA; // Class that needs just 1 array
    private static final int C_2ARR = C_AA;     // Class that needs two arrays.
    private static final int C_2D = C_NA|C_AA|C_NN;// Class with two dimensions.
    private static final int C_A11 = C_A|C_AA;  // First array size is size1
    private static final int C_A12 = C_NA;      // First array size is size2
    private static boolean ttype(int s) {
        return (algtype & s) != 0;
    }
    
    /*
     * Execute one test trial.  The runner is executed, and the
     * rest is for display.  This is trial t of ntrials, which is only used for
     * building messages.  The verbosty flag is
     *    0 for print nothing.
     *    1 for print only the result.
     *    2 for full chatter.
     */
    private static void trial(int verbosity, Countable countable,
                              int t, int ntrials, int size, int size2)
    {
        // If there is more than one trial, number them.
        if(ntrials > 1 && verbosity == 2) {
            System.out.println("\n=== Trial " + t + " ===");
            if(m_parms.get("step").ival() > 0)
                System.out.println("size = " + size);
            if(m_parms.get("step2").ival() > 0)
                System.out.println("size2 = " + size2);
        }

        // Construct the random array(s).
        int[] arr = null, arr2 = null;
        if(ttype(C_ARR)) {
            int array_size = ttype(C_A11) ? size : size2;

            int min = m_parms.get("min").ival();
            int max = m_parms.get("max").ival();
            if(m_parms.get("ascend").bval() || m_parms.get("descend").bval()) {
                boolean reverse = m_parms.get("descend").bval();
                arr = m_counter.fill_rand_ordered_int
                    (array_size, min, max, reverse);
                if(ttype(C_2ARR))
                    arr2 = m_counter.fill_rand_ordered_int
                        (size2, min, max, reverse);
            } else {
                arr = m_counter.fill_rand_int(array_size, min, max);
                if(ttype(C_2ARR))
                    arr2 = m_counter.fill_rand_int
                        (m_parms.get("size2").ival(), min, max);
            }
        }

        // Maybe print the array.
        if(ttype(C_ARR) && m_parms.get("prepr").bval()) {
            Util.pr_array("Before Processing", arr);
            if(ttype(C_2ARR))
                Util.pr_array("Before Processing (array 2)", arr2);
        }

        // Run the algorithm.
        m_counter.clr_cnt();
        countable.setCountingHouse(m_counter);
        long start = System.nanoTime();
        switch(algtype) {
        case C_N:
            ((CountableN)countable).run(size);
            break;
        case C_A:
            ((CountableA)countable).run(arr);
            break;
        case C_NA:
            ((CountableNA)countable).run(size, arr);
            break;
        case C_NN:
            ((CountableNN)countable).run(size, size2);
            break;
        case C_AA:
            ((CountableAA)countable).run(arr, arr2);
            break;
        }
        long duration = System.nanoTime() - start;
        m_totalduration += duration;
        int count = m_counter.get_cnt();
        m_totalcount += count;

        // Maybe print the final array.
        if(ttype(C_ARR) && m_parms.get("print").bval()) {
            Util.pr_array("After Processing", arr);
            if(ttype(C_2ARR))
                Util.pr_array("After Processing (array 2)", arr2);
        }

        if(verbosity > 0) {
            System.out.println(count + " operations, " +
                             Util.fmt_time(duration));
            if(size == 0) return; // Just avoid zero division.
            System.out.println(Util.fltav(count, size) +
                               " operations per " +
                               (ttype(C_2D) ? "first " : "")
                               + "problem size; " +
                               Util.fmt_time(Util.av(duration,size)) +
                               " per " + (ttype(C_2D) ? "first " : "") +
                               "problem size");
            if(ttype(C_2D)) {
                if(size2 == 0) return; // Just avoid zero division.
                System.out.println
                    (Util.fltav(count, size2) +
                     " operations per second problem size; " +
                     Util.fmt_time(Util.av(duration, size2)) +
                     " per second problem size");
                System.out.println
                    (Util.fltav(count, size*size2) +
                     " operations per problem size product; " + 
                     Util.fmt_time(Util.av(duration, size*size2)) +
                     " per size product");
                System.out.println
                    (Util.fltav(count, size+size2) +
                     " operations per problem size sum; " + 
                     Util.fmt_time(Util.av(duration, size+size2)) +
                     " per size sum");
            }
        } 
    }

    public static void main(String[] args)
    {
        // Load the parameter values into the parms object.
        m_parms.reqstr("countable", "Name of countable class");
        m_parms.reqint("size","Problem Size (First Dimension)");
        m_parms.reqint("size2","Second Dimension Size.");
        m_parms.parm("trials", 1, "Number of test trials (def 1)");
        m_parms.parm("step", 0, "Increment for size between trials.");
        m_parms.parm("step2", 0, "Increment for size2 between trials.");        
        m_parms.parm("min", -100000, "Min random array value (def -100000).");
        m_parms.parm("max", 100000, "Max random array value (def 100000).");
        m_parms.reqint("seed", "Random seed.  If not set, randomize");
        m_parms.parm("ascend", false,
                     "Generate ordered ascending data in the array (default unset)");
        m_parms.parm("descend", false,
                     "Generate ordered descending data in the array (default unset)");
        m_parms.parm("print", false,
                     "Print the array after running (default unset)");
        m_parms.parm("prepr", false,
                     "Print the array before running (default unset)");
        m_parms.reqint("disc", "Number of trials to discard before " +
                       "measuring (def 0)");
        m_parms.parm("quiet", false, "Reduce the amount of output.");
        m_parms.parm("help", false, "Print this list and exit.");

        if(m_parms.get("ascend").bval() && m_parms.get("descend").bval()) {
            System.out.println("Sorry, you can't have data both ascend and descend.");
            System.exit(1);
        }

        try {
            // Get all the parm values set on the command line, and request
            // the essentials if absent.
            m_parms.load(args);
            if(m_parms.get("help").bval()) {
                m_parms.print_help();
                System.exit(0);
            }
            m_parms.req("countable");
            m_parms.req("size");

            // Try to get the class object.
            Countable countable = (Countable)Util.get_tested_object
                (m_parms.get("countable").sval(),
                 Countable.class,!m_parms.get("quiet").bval());
            if(countable == null) System.exit(1);

            // Require and record the correct subclass.
            if(countable instanceof CountableA)
                algtype = C_A;
            else if(countable instanceof CountableN)
                algtype = C_N;
            else if(countable instanceof CountableNA)
                algtype = C_NA;
            else if(countable instanceof CountableNN)
                algtype = C_NN;
            else if(countable instanceof CountableAA)
                algtype = C_AA;
            else {
                System.out.println("Your Countable object " + m_parms.get("countable") +
                                   " must extend either CountableN, CountableA, " +
                                   "CountableNA, CountableNN or CountableAA.");
                System.exit(1);
            }
            if(ttype(C_2D))
                m_parms.req("size2");
            else
                m_parms.get("size2").set("0");

            // Set the random seed.
            m_counter = new CountingHouse();
            Parms.Parm p = m_parms.get("seed");
            if(!p.no_val()) m_counter.setSeed(p.ival());

            // Run any discard trials.
            if(!m_parms.get("disc").no_val()) {
                int ndisc = m_parms.get("disc").ival();
                if(!m_parms.get("quiet").bval())
                    System.out.println("Discarding first " + ndisc +
                                       " trials.");
                for(int t = 1; t < ndisc; ++t)
                    trial(0, countable, t, ndisc,
                          m_parms.get("size").ival(),
                          m_parms.get("size2").ival());
            }

            // Repeat the timed trials the specified number of times.
            int ntrials = m_parms.get("trials").ival();
            m_totalduration = 0;
            m_totalcount = 0;
            int trial_verbosity =
                m_parms.get("quiet").bval() ? (ntrials > 1 ? 0 : 1) : 2;
            int s1 = m_parms.get("size").ival();
            int s2 = m_parms.get("size2").ival();
            for(int t = 1; t <= ntrials; ++t) {
                trial(trial_verbosity, countable, t, ntrials, s1, s2);
                s1 += m_parms.get("step").ival();
                s2 += m_parms.get("step2").ival();
            }
            
            int size = m_parms.get("size").ival();
    
            if(ntrials > 1 && m_parms.get("step").ival() == 0 && 
               m_parms.get("step2").ival() == 0) {
                System.out.print(Util.fltav(m_totalcount, ntrials) + 
                                 " operations/trial; ");
                if(size == 0) {
                    System.exit(0);  // Avoid divisions by zero.
                }
                System.out.println(Util.fltav(m_totalcount, ntrials*size) +
                                   " interations/" +
                                   (ttype(C_2D) ? "first " : "") +
                                   "problem size");
                System.out.println(Util.fmt_time(Util.av(m_totalduration,
                                                         ntrials)) +
                                   " per trial; " +
                                   Util.fmt_time(Util.av(m_totalduration,
                                                         ntrials*size)) +
                                   " per " +  (ttype(C_2D) ? "first " : "") +
                                   "problem size");
                if(ttype(C_2D)) {
                    int size2 = m_parms.get("size2").ival();
                    if(size2 == 0) {
                        System.exit(0);  // Avoid divisions by zero.
                    }
                    System.out.println(Util.fltav(m_totalcount, ntrials*size2) +
                                       " interations per second " +
                                       "problem size");
                    System.out.println(Util.fmt_time(Util.av(m_totalduration,
                                                             ntrials*size2)) +
                                       " average time per second problem size");
                    int volume = size*size2;
                    int tot = size + size2;
                    System.out.println(Util.fltav(m_totalcount,
                                                  ntrials*volume) +
                                       " interations per size product; " + 
                                       Util.fmt_time(Util.av(m_totalduration,
                                                             ntrials*volume)) +
                                       " average time per size product");
                    System.out.println(Util.fltav(m_totalcount,
                                                  ntrials*tot) +
                                       " interations per size sum; " + 
                                       Util.fmt_time(Util.av(m_totalduration,
                                                             ntrials*tot)) +
                                       " average time per size sum");
                }
            }
        } catch (Exception e) {
            System.out.println("Error: " + e);
            e.printStackTrace(System.out);
        }
    }
}
