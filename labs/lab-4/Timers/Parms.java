package Timers;

import java.util.*;

class Parms {
    // Extract the name part of a parmeter.  Looks for = or leading + or -
    // to figure out what it is dealing with.
    public static String name(String pair)
    {
        int n = pair.indexOf('=');
        if(n != -1) return pair.substring(0,n);
            
        if(pair.charAt(0) == '+' || pair.charAt(0) == '-')
            return pair.substring(1);
        if(pair.length() >= 2 && pair.substring(0,2).equals("no"))
            return pair.substring(2);
        return pair;
    }

    /*
     * Objects for holding parameter values.  They are strings ints or 
     * bool, and complain about various misuse.
     */
    abstract class Parm
    {
        // Locate the = in a key=value pair and return it, or throw.
        protected int splitloc(String pair)
        {
            int ret = pair.indexOf('=');
            if(ret < 0)
                throw new IllegalArgumentException("No = in parm setting");
            return ret;
        }

        // Get the value from the parm string (with name).
        public abstract void parse(String s);

        // Set the value from a string (without name).
        public abstract void set(String s);

        // Return the value of the given type, or puke.
        public int ival() {
            throw new RuntimeException("Use of non-int parm as integer");
        }
        public String sval() {
            throw new RuntimeException("Use of non-string parm as a string");
        }
        public boolean bval() {
            throw new RuntimeException("Use of non-bool parm as a bool");
        }

        // Value is not set.
        public abstract boolean no_val();

        // Documentation string.
        private String m_doc;
        public Parm sdoc(String s) {
            m_doc = s;
            return this;
        }
        public String doc() { return m_doc; }
    }

    /*
     * Integer value parameters
     */
    class IntParm extends Parm
    {
        public IntParm() { m_value = 0; m_unset = true; }
        public IntParm(int n) { m_value = n; m_unset = false; }
        public void parse(String pair) {
            m_value = Integer.valueOf(pair.substring(splitloc(pair)+1));
            m_unset = false;
        }
        public void set(String v) {
            m_value = Integer.valueOf(v);
            m_unset = false;
        }
        public int ival() { return m_value; }
        public boolean no_val() { return m_unset; }

        private int m_value;
        private boolean m_unset;
    }

    /*
     * String value parameters.
     */
    class StrParm extends Parm
    {
        public StrParm() { m_value = null; }
        public StrParm(String s) { m_value = s; }
        public void parse(String pair) {
            m_value = pair.substring(splitloc(pair)+1);
        }
        public void set(String v) {
            m_value = v;
        }
        public String sval() { return m_value; }
        public boolean no_val() { return m_value == null; }

        private String m_value;
    }

    /*
     * Boolean value parameters (flags).
     */
    class BoolParm extends Parm
    {
        public BoolParm() { m_value = false; }
        public BoolParm(boolean b) { m_value = b; }
        public void parse(String itm) {
            if(itm.charAt(0) == '-' ||
               (itm.length() >= 2 && itm.substring(0,2).equals("no")))
                m_value = false;
            else
                m_value = true;
        }
        public void set(String v) {
            parse(v);
        }
        public boolean bval() { return m_value; }
        public boolean no_val() { return false; }
        private boolean m_value;
    }

    // List of parameters.
    private HashMap<String, Parm> m_parms = new HashMap<String, Parm>();

    /*
     * Print the documentation parameters.
     */
    public void print_help()
    {
        Object[] keys = m_parms.keySet().toArray();
        Arrays.sort(keys);

        System.out.println("Set parameters on command line by name=value.  " +
                           "For booleans (flags),");
        System.out.println("use just the name or +name to set; clear with " +
                           "noname or -name.");
        for(int i = 0; i < keys.length; ++i) {
            System.out.printf("  %-10s%s\n", (String)keys[i],
                              m_parms.get(keys[i]).doc());
        }
    }

    /*
     * Set a parameter, of various types, including default and doc.
     */
    public void parm(String key, int dflt, String doc) {
        IntParm ip = new IntParm(dflt);
        ip.sdoc(doc);
        m_parms.put(key, ip);
    }
    public void parm(String key, String dflt, String doc) {
        StrParm sp = new StrParm(dflt);
        sp.sdoc(doc);
        m_parms.put(key, sp);
    }
    public void parm(String key, boolean dflt, String doc) {
        BoolParm bp = new BoolParm(dflt);
        bp.sdoc(doc);
        m_parms.put(key, bp);
    }
    public void reqint(String key, String doc) {
        IntParm ip = new IntParm();
        ip.sdoc(doc);
        m_parms.put(key, ip);
    }
    public void reqstr(String key, String doc) {
        StrParm sp = new StrParm();
        sp.sdoc(doc);
        m_parms.put(key, sp);
    }

    /* 
     * Get a parm value, or null.
     */
    public Parm get(String parm) {
        return m_parms.get(parm);
    }

    /*
     * Load the paramters.  First set all the defaults, then set the values
     * given the parameter list.
     */
    public void load(String[] args)
    {
        boolean ok = true;

        // Process the command line parms.
        for(int i = 0; i < args.length; ++i) {
            try {
                String name = name(args[i]);
                if(!m_parms.containsKey(name))
                    throw new IllegalArgumentException
                        ("Unknow parameter " + name);
                m_parms.get(name).parse(args[i]);
            } catch(Exception e) {
                System.out.println("Unable to parse parameter " + args[i] +
                                   ": " + e);
                ok = false;
            }
        }

        if(!ok)
            throw new RuntimeException("Bad command-line parameter");
    }

    /* Request scanner. */
    private Scanner scan = null;

    /* Request a parameter value from a user, if the parameter does not
       already have one. */
    public void req(String parm, String prompt) {
        Parm p = m_parms.get(parm);
        if(p == null)
            throw new RuntimeException("Program error: req for unknown parm");
        if(!p.no_val()) return;

        if(prompt == null) prompt = "Enter " + p.doc() + ":";
        if(scan == null) scan = new Scanner(System.in);
        while(true) {
            System.out.print(prompt + " ");
            try {
                p.set(scan.next());
                return;
            } catch(IllegalArgumentException e) {
                // Note: the standard string-to-integer conversion throws
                // NumberFormatException, which refines this one.
                System.out.println(e);
                System.out.println("Please try again.");
            } // Other exceptions pass up.
        }
    }
    public void req(String parm) { req(parm, null); }
}
