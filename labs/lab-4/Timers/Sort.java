package Timers;

import java.lang.*;
import java.util.*;

/*
 * The main program for timing a sort.
 */
class Sort {
    /* Parameter settings. */
    private static Parms m_parms = new Parms();

    /* This manages the comparison counting, and provides some utilities. */
    private static CountingHouse m_counter;

    /* For measuring sort duration. */
    private static long m_totalduration = 0;

    /* Total comarisons. */
    private static int m_totalcount = 0;

    /*
     * Execute one test trial.  The sorter is used to run the sort, and the
     * rest is for display.  This is trial t of ntrials, which is only used for
     * building messages.  The verbosty flag is
     *    0 for print nothing.
     *    1 for print only the result.
     *    2 for full chatter.
     */
    private static void trial(int verbosity,
                              Sorter<CountingHouse.CountedInteger> sorter,
                              int t, int ntrials)
    {
        // If there is more than one trial, number them.
        if(ntrials > 1 && verbosity == 2)
            System.out.println("\n=== Trial " + t + " ===");

        // Construct the random array.
        CountingHouse.CountedInteger[] arr = null;
        if(m_parms.get("ascend").bval() || m_parms.get("descend").bval()) {
            arr = m_counter.fill_rand_ordered
                (m_parms.get("size").ival(),m_parms.get("min").ival(),
                 m_parms.get("max").ival(), m_parms.get("descend").bval());
        } else {
            arr = m_counter.fill_rand
                (m_parms.get("size").ival(),m_parms.get("min").ival(),
                 m_parms.get("max").ival());
        }

        // Get this content hash used to check correctness.
        int hash = 0;
        if(m_parms.get("check").bval())
            hash = Util.oi_hash(arr);

        // Maybe print the array.
        if(m_parms.get("prepr").bval())
            Util.pr_array("Before Sorting", arr);

        // Parms ok.  Run the sort.
        m_counter.clr_cnt();
        long start = System.nanoTime();
        sorter.sort(arr);
        long duration = System.nanoTime() - start;
        m_totalduration += duration;
        int count = m_counter.get_cnt();
        m_totalcount += count;

        // Maybe print the sorted array.
        if(m_parms.get("print").bval())
            Util.pr_array("Sorted array", arr);

        String msg = "Sort correctness not checked";
        int nerr = 0;
        boolean conterr = false;
        if(m_parms.get("check").bval()) {
            nerr = Util.check_order(arr);
            conterr = (hash != Util.oi_hash(arr));

            if(conterr && nerr > 0)
                msg = "Array values changed and not correctly ordered";
            else if(conterr)
                msg = "Array is in order, but some values differ";
            else if(nerr > 0)
                msg = "Array has the right values, but not in order.";
            else
                msg = "Array appears to have been correctly sorted";
        }

        if(verbosity > 0) {
            if(verbosity == 2) {
                System.out.println("Sorted array of " +
                                   m_parms.get("size").ival() + 
                                   " values.  " + msg);
            }
            System.out.println(count + " comparisons (" +
                               Util.fltav(count, m_parms.get("size").ival()) +
                               " per item); " + Util.fmt_time(duration));
        } else if(nerr > 0 || conterr) {
            System.out.println("There were errors in the sort.");
        }
    }

    public static void main(String[] args)
    {
        // Load the parameter values into the parms object.
        m_parms.reqstr("sort", "Name of sorting class");
        m_parms.reqint("size","Size of test array");
        m_parms.parm("trials", 1, "Number of test trials (def 1)");
        m_parms.parm("min", -100, "Min random array value (def -100).");
        m_parms.parm("max", 100, "Max random array value (def 100).");
        m_parms.reqint("seed", "Random seed.  If not set, randomize");
        m_parms.parm("check", true,
                     "Verify sort result (default set)");
        m_parms.parm("ascend", false,
                     "Generate ordered ascending data in the array (default unset)");
        m_parms.parm("descend", false,
                     "Generate ordered descending data in the array (default unset)");
        m_parms.parm("print", false,
                     "Print the array after sorting (default unset)");
        m_parms.parm("prepr", false,
                     "Print the array before sorting (default unset)");
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
            m_parms.req("sort");
            m_parms.req("size");

            // Get the sorter object to test.  NOTE:  Since Java shreded their
            // formerly pretty decent type system when the added generics, 
            // I cannot pass Sorter<CountingHouse.CountedInteger>.class as is 
            // correct.
            Sorter<CountingHouse.CountedInteger> sorter = null;
            sorter = (Sorter<CountingHouse.CountedInteger>)
                Util.get_tested_object(m_parms.get("sort").sval(),
                                       Sorter.class,
                                       !m_parms.get("quiet").bval());
            if(sorter == null) System.exit(1);

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
                    trial(0, sorter, t, ndisc);
            }

            // Repeat the timed trials the specified number of times.
            int ntrials = m_parms.get("trials").ival();
            m_totalduration = 0;
            m_totalcount = 0;
            int trial_verbosity =
                m_parms.get("quiet").bval() ? (ntrials > 1 ? 0 : 1) : 2;
            for(int t = 1; t <= ntrials; ++t) {
                trial(trial_verbosity, sorter, t, ntrials);
            }

            if(ntrials > 1) {
                if(!m_parms.get("quiet").bval())
                    System.out.println();
                System.out.println(Util.fltav(m_totalcount, ntrials) + 
                                   " average comparisons; (" +
                                   Util.fltav(m_totalcount,
                                       ntrials*m_parms.get("size").ival()) +
                                   " per item); " + 
                                   Util.fmt_time(Util.av(m_totalduration,
                                                         ntrials)) +
                                   " average time.");
            }
        } catch (Exception e) {
            System.out.println("Error: " + e);
            e.printStackTrace(System.out);
        }
    }
}
