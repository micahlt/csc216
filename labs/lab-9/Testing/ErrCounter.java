package Testing;

import java.util.*;

/*
 * This beastie keeps track of error counts and prints messages.
 */
public class ErrCounter {
    // Overall message maximum.
    final static int MSG_MAX = 5;

    // When a new subtest starts, this many messages will be printed
    // even when over the MSG_MAX
    final static int SEC_MSG_MAX = 1;

    // Note: Error messages containing an exception are printed regardless.
    // These are expected to be thrown from user code, and will generally
    // terminate at least the subtest, or perhaps the whole test plan.

    // Set the trace limit to the trace depth less the given arg.  
    private void setTraceLimit(int adj)
    {
        StackTraceElement[] traces = Thread.currentThread().getStackTrace();
        m_traceLimit = traces.length - 2 - adj;
    }

    public ErrCounter()
    {
        setTraceLimit(2);
        m_name = "";
    }

    public ErrCounter(String name)
    {
        setTraceLimit(2);
        m_name = name;
    }

    // Set the current function as top of error trace.
    public void setTraceBase()
    {
        setTraceLimit(2);
    }

    private class SubTest {
        public int count = 0;
        public String name, phase;
        public int limitCountdown = SEC_MSG_MAX;
        public boolean quiet;
        public SubTest(String n, boolean q) { name = n; quiet = q; }
        public SubTest(String n, String p, boolean q) {
            name = n; phase = p; quiet = q;
        }
    }

    // Return the current full test name.  Note: If there is no  main test
    // name and no active subtest, returns empty string.
    private String youAreHere()
    {
        // Add the test name, if any.
        String ret = m_name;
        if(!ret.equals("") && m_subtests.size() > 0) {
            ret += "->";
        }

        // Add all subtest name(s).
        ListIterator<SubTest> i = m_subtests.listIterator(0);
        while(i.hasNext()) {
            SubTest st = i.next();
            ret += st.name;
            if(i.hasNext()) {
                ret += "->";
            }
        }

        return ret;
    }
    
    // Generate blanks according to m_indent.
    private String indent()
    {
        String ret = "";
        for(int i = 0; i < m_indent; ++i)
            ret += "  ";
        return ret;
    }

    // Current phase, if set.  It will also recorded in the stack of
    // SubTest objects, so it can be cleared.  This describes the top-level
    // purpose of the test, and is printed in case of an error.  Only one
    // phase is current, and attempts to set a phase deeper in the stack
    // are ignored.
    private String m_phase = null;

    // Enter or leave a subtest.  Generates output and adjusts counts and
    // records.  enter()/leave() calls should balance.  Extra leave() calls
    // try to avoid exceptions, but will not print summary messages.
    // Java needs default parameters.
    private void enter(String name, String phase, boolean quiet)
    {
        if(phase != null && m_phase == null) {
            m_phase = phase;
            m_subtests.addLast(new SubTest(name, phase, quiet));
        } else  {
            m_subtests.addLast(new SubTest(name, quiet));
        }
        if(!quiet) {
            if(!m_name.equals("") || m_subtests.size() > 0) ++m_indent;
            System.out.println(indent() + "+ " +youAreHere() + ".");
        }
    }
    public void enter(String name, String phase)
    {
        enter(name, phase, false);
    }
    public void enter(String name)
    {
        enter(name, null, false);
    }
    public void quietEnter(String name)
    {
        enter(name, null, true);
    }
    public void quietEnter(String name, String phase)
    {
        enter(name, phase, true);
    }
    public int leave()
    {
        if(m_subtests.size() == 0) return m_errcount;
        String loc = youAreHere();
        SubTest st = m_subtests.removeLast();
        if(st.phase == m_phase) m_phase = null;
        if(!st.quiet && st.count > 0) {
            System.out.println(indent() + "!!! " + loc + ", " +
                               st.count + " errors.");
        }
        if(m_subtests.size() > 0) {
            SubTest newtop = m_subtests.peekLast();
            newtop.count += st.count;
            if(newtop.limitCountdown > st.limitCountdown) {
                newtop.limitCountdown = st.limitCountdown;
            }
        }
            
        if(m_indent > 0 && !st.quiet) --m_indent;

        return st.count;
    }

    // This is mostly for cleaning up after an exception.
    public void leaveAll()
    {
        while(m_subtests.size() > 0) leave();
    }

    // For limiting output by category.
    public class MsgCat {
        private int m_limit;
        private int m_count;
        private String m_name;
        MsgCat(int limit, String name) {
            m_limit = limit;
            m_count = 0;
            m_name = name;
        }
        boolean suppress()
        {
            return ++m_count > m_limit;
        }
    }
    public MsgCat getLimiter(int limit, String name) {
        return new MsgCat(limit,name);
    }

    // The non-limit.
    private MsgCat m_limitless = new MsgCat(Integer.MAX_VALUE, "");

    // Print an error message and retain counts.  If e is non-null, 
    // the error limit is ignored (message printed anyway), and the
    // exception trace is also generated.
    private void failMessage(String msg, MsgCat catLimiter,
                             int extraDiscard, Throwable e)
    {
        // Count this error, and also recover the count of remaining
        // new-secion error message limit.
        ++m_errcount;
        int oldSecLimit = 0;
        if(m_subtests.size() > 0) {
            SubTest st = m_subtests.peekLast();
            st.count++;
            oldSecLimit = st.limitCountdown--;
        }

        // Maybe print all the details.
        if(e != null || (!catLimiter.suppress() &&
                         (m_errcount <= MSG_MAX || oldSecLimit > 0))) {
            if(m_phase != null) {
                System.out.println(indent()+"|| "+m_phase.replace
                                   ("\n", "\n"+indent()+"|| "));
                m_phase = null;
            }
            System.out.println(indent() + "=== ["+m_errcount+"] " +
                               youAreHere() + " ===");
            System.out.println(indent() + "===  " + msg + " ===");
            if(e != null) {
                System.out.println(indent() + "===  Exception: " + e + " ===");
            }
            if(e != null) {
                StackTraceElement[] traces = e.getStackTrace();
                int limit = traces.length - m_traceLimit;
                int rpt_cnt = 0;
                for(int i = 0; i < limit; ++i) {
                    if(i > 0 && i < limit - 1 && traces[i].equals(traces[i-1])) {
                        // Immediate repeat.  Handles simple unbounded recursion, but
                        // something more interesting breaks.
                        ++rpt_cnt;
                    } else {
                        // If we're ending a long repeat, show the count.
                        if(rpt_cnt >= 3)
                            System.out.println(indent() + "   ... " + (rpt_cnt-2) +
                                               " more");
                        rpt_cnt = 0;
                    }
                    if(rpt_cnt < 3) {
                        System.out.println(indent() + "  " + traces[i]);
                    }
                }
                System.out.println(indent() + "Detected:");
            }
            StackTraceElement[] traces = Thread.currentThread().getStackTrace();
            int limit = traces.length - m_traceLimit;
            int start = 3 + extraDiscard;
            for(int i = start; i < limit; ++i)
                System.out.println(indent() + "  " + traces[i]);

            System.out.println();
        } else {
            m_suppressed++;
        }
    }

    // Public interfaces.
    public void failMessage(String msg, Throwable e)
    {
        failMessage(msg, m_limitless, 0, e);
    }
        
    // Default to no exception.
    public void failMessage(String msg)
    {
        failMessage(msg, m_limitless, 0, null);
    }

    // Public interfaces.
    // Default to no exception.
    public void failMessage(MsgCat catLimit, String msg)
    {
        failMessage(msg, catLimit, 0, null);
    }

    // Print a final error summary.
    public void finalMessage(String msg)
    {
        if(!m_name.equals("") && !msg.equals("")) {
            msg = msg + " ("+m_name+")";
        } else if(msg.equals("")) {
            msg = m_name;
        }
        if(!msg.equals("")) {
            msg = msg + " ";
        }
        if(m_errcount == 0) {
            System.out.println("*** " + msg + "Done: No errors ***");
        } else {
            System.out.print("=== " + msg + "Done: " + m_errcount +
                             " errors detected");
            if(m_suppressed > 0) {
                System.out.println("; " + m_suppressed + " messages were " +
                                   "suppressed to limit output. ===");
            } else {
                System.out.println(". ===");
            }
        }
    }
    public void finalMessage() {
        finalMessage("");
    }
        
    // Or roll your own.
    public int getErrorCount() { return m_errcount; }
    public int getSupressionCount() { return m_suppressed; }
    public int getSectionErrorCount() {
        if(m_subtests.size() > 0)
            return m_subtests.peekLast().count;
        else
            return m_errcount;
    }
    public String getTestName() {
        return youAreHere();
    }

    // Data.
    private String m_name;              // Main test name, if non-blank.
    private int m_traceLimit = 0;       // Amount to clip from top of traces.
    private int m_indent = 0;           // Current message printing indent.
    private int m_errcount = 0;         // Overall error count
    private int m_suppressed = 0;       // Count of suppressed error messages.

    // A stack-like list giving the path to the current subtest.
    private LinkedList<SubTest> m_subtests = new LinkedList<SubTest>();
}
