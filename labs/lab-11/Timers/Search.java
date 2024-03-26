package Timers;

import java.lang.*;
import java.util.*;

/*
 * The main program for timing a dictionary.
 */
class Search {
    /* Parameter settings. */
    private static Parms m_parms = new Parms();

    /* This manages the comparison counting, and provides some utilities. */
    private static CountingHouse m_counter;

    /* For measuring time durations, insert and search. */
    private static long m_totalinsdur = 0;
    private static long m_totalsrcdur = 0;

    /* Total comarisons. */
    private static int m_totalinscomps = 0;
    private static int m_totalsrccomps = 0;

    /* Error detected. */
    private static boolean m_error = false;

    /*
     * Execute one test trial.  The sorter is used to run the sort, and the
     * rest is for display.  This is trial t of ntrials, which is only used for
     * building messages.  The verbosty flag is
     *    0 for print nothing.
     *    1 for print only the result.
     *    2 for full chatter.
     */
    private static void trial(int verbosity,
                              Searcher<CountingHouse.CountedInteger> searcher,
                              int t, int ntrials)
    {
        // If there is more than one trial, number them.
        if(ntrials > 1 && verbosity == 2)
            System.out.println("\n=== Trial " + t + " ===");

        // Insert-related parameters.
        int nitem = m_parms.get("nitem").ival();
        int min = m_parms.get("min").ival();
        int max = m_parms.get("max").ival();

        // Get the data for the test.
        CountingHouse.CountedInteger[] arr = null;
        if(m_parms.get("ascend").bval() || m_parms.get("descend").bval()) {
            arr = m_counter.fill_rand_ordered
                (nitem, min, max,m_parms.get("descend").bval());
        } else {
            arr = m_counter.fill_rand(nitem, min, max);
        }

        // Maybe print the data
        if(m_parms.get("prins").bval())
            Util.pr_array("Before Inserting", arr);

        // Perform the inserts.
        m_counter.clr_cnt();
        long instart = System.nanoTime();
        for(int i = 0; i < nitem; ++i)
            searcher.insert(arr[i]);
        long insdur = System.nanoTime() - instart;
        m_totalinsdur += insdur;
        int inscount = m_counter.get_cnt();
        m_totalinscomps += inscount;

        // Number of incorrect finds.
        int nbad = 0;

        // Get data for searching.  Go ahead and stick it in an array first
        // to keep rand number generator overhead out of search timing.
        boolean search_present = m_parms.get("present").bval();
        int nsrc = m_parms.get("nsrc").ival();
        CountingHouse.CountedInteger[] tofind = null;
        if(search_present) {
            tofind = new CountingHouse.CountedInteger[nsrc];
            for(int i = 0; i < nsrc; ++i)
                tofind[i] = arr[m_counter.get_int(nitem)];
        } else
            tofind = m_counter.fill_rand(nsrc, min, max);
        CountingHouse.CountedInteger[] found =
            new CountingHouse.CountedInteger[nsrc];

        // Maybe print the sought items.
        if(m_parms.get("prsrc").bval())
            Util.pr_array("Before Searching", tofind);

        // Perform the searches.
        long srcstart = System.nanoTime();
        m_counter.clr_cnt();
        for(int i = 0; i < nsrc; ++i) {
            found[i] = searcher.find(tofind[i]);
        }
        long searchdur = System.nanoTime() - srcstart;
        m_totalsrcdur += searchdur;
        int searchcount = m_counter.get_cnt();
        m_totalsrccomps += searchcount;

        // Count misses.
        int nfound = 0;
        if(m_parms.get("check").bval() || !m_parms.get("present").bval()) {
            for(int i = 0; i < nsrc; ++i) {
                if(found[i] != null)
                    ++nfound;
            }
        }

        // Check.
        int bad = 0;
        if(m_parms.get("check").bval()) {
            for(int i = 0; i < nsrc; ++i) {
                if(found[i] != null) {
                    if(!found[i].equals(tofind[i]))
                        ++bad;
                }
            }

            if(bad > 0) {
                System.out.println("*** " + bad + " searches found the wrong "
                                   + "item ***");
                m_error = true;
            }
            if(search_present && nfound < nsrc) {
                System.out.println("*** Only " + nfound + " items found, but " +
                                   "all " + nsrc + " should have been. ***");
                m_error = true;
            }
            else if(bad == 0 && verbosity > 1)
                System.out.println("No search errors found.");
        } else if(verbosity > 1)
            System.out.println("Seach not checked.");

        if(verbosity > 0) {
            System.out.println("Insert time: " + Util.fmt_time(insdur) + " (" +
                               Util.fmt_time(insdur/nitem) + "/insert), " +
                               inscount + " comparisons (" + 
                               Util.fltav(inscount,nitem) + "/insert)");
            System.out.println("Search time: " + Util.fmt_time(searchdur) + " ("
                               + Util.fmt_time(searchdur/nsrc) + "/search), " +
                               searchcount + " comparisons (" + 
                               Util.fltav(searchcount,nsrc) + "/search)");
            if(!m_parms.get("present").bval()) {
                System.out.println(nfound + " found (" +
                                   Util.fltav(100*nfound,nsrc) + "%)");
            }
        }
    }

    public static Searcher<CountingHouse.CountedInteger>
        get_searcher(boolean quiet)
    {
            return
                Util.get_tested_object(m_parms.get("src").sval(),
                                       Searcher.class,
                                       !quiet && !m_parms.get("quiet").bval());
    }

    public static void main(String[] args)
    {
        // Load the parameter values into the parms object.
        m_parms.reqstr("src", "Name of the searching class");
        m_parms.reqint("nitem","Number of items to insert");
        m_parms.reqint("nsrc","Number of items to search for");
        m_parms.parm("trials", 1, "Number of test trials (def 1)");
        m_parms.parm("min", -100000000,
                     "Min random data value (def -100000000).");
        m_parms.parm("max", 100000000,
                     "Max random data value (def 100000000).");
        m_parms.reqint("seed", "Random seed.  If not set, randomize");
        m_parms.parm("check", true,
                     "Run some checks on search result (default set)");
        m_parms.parm("ascend", false,
                     "Insert ordered increasing data into the collection " +
                     "(default unset)");
        m_parms.parm("descend", false,
                     "Insert ordered decreasing data into the collection " +
                     "(default unset)");
        m_parms.parm("present", true, "Search only for data which has " +
                     "been inserted (default true).");
        m_parms.parm("prins", false,
                     "Print the data before inserting (default unset)");
        m_parms.parm("prsrc", false,
                     "Print the search items (default unset)");
        m_parms.reqint("disc", "Number of trials to discard before " +
                       "measuring (def 0)");
        m_parms.parm("quiet", false, "Reduce the amount of output.");
        m_parms.parm("help", false, "Print this list and exit.");

        try {
            // Get all the parm values set on the command line, and request
            // the essentials if absent.
            m_parms.load(args);
            if(m_parms.get("help").bval()) {
                m_parms.print_help();
                System.exit(0);
            }
            m_parms.req("src");
            m_parms.req("nitem");
            m_parms.req("nsrc");

            // Get the searcher object to test.
            Searcher<CountingHouse.CountedInteger> searcher =
                get_searcher(false);
            if(searcher == null) System.exit(1);

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
                    trial(0, searcher, t, ndisc);

                // Replace the object.  Need a new (empty) one for each test.
                searcher = get_searcher(true);
                if(searcher == null) System.exit(1);
            }

            // Repeat the timed trials the specified number of times.
            int ntrials = m_parms.get("trials").ival();
            m_totalinsdur = 0;
            m_totalsrcdur = 0;
            m_totalinscomps = 0;
            m_totalsrccomps = 0;
            int trial_verbosity =
                m_parms.get("quiet").bval() ? (ntrials > 1 ? 0 : 1) : 2;
            for(int t = 1; t <= ntrials; ++t) {
                trial(trial_verbosity, searcher, t, ntrials);

                // Replace the object.  Need a new (empty) one for each test.
                searcher = get_searcher(true);
                if(searcher == null) System.exit(1);
            }

            if(ntrials > 1) {
                if(!m_parms.get("quiet").bval()) {
                    System.out.println();
                }

                if(m_error) {
                    System.out.println("*** Errors in one or more trials ***");
                }

                if(!m_parms.get("quiet").bval()) {
                    System.out.println("=== Overall Totals ===");
                }

                int nitem = m_parms.get("nitem").ival();
                int nsrc = m_parms.get("nsrc").ival();
                System.out.println("Insert time: " +
                                   Util.fmt_time(m_totalinsdur) + " (" +
                                   Util.fmt_time(m_totalinsdur/(nitem*ntrials))
                                   + "/insert), " + m_totalinscomps +
                                   " comparisons (" + 
                                   Util.fltav(m_totalinscomps,nitem*ntrials) +
                                   "/insert)");
                System.out.println("Search time: " +
                                   Util.fmt_time(m_totalsrcdur) + " (" +
                                   Util.fmt_time(m_totalsrcdur/(nsrc*ntrials))
                                   + "/search), " + m_totalsrccomps +
                                   " comparisons (" + 
                                   Util.fltav(m_totalsrccomps,nsrc*ntrials) +
                                   "/search)");
            }
        } catch (Exception e) {
            System.out.println("Error: " + e);
            e.printStackTrace(System.out);
        }
    }
}
