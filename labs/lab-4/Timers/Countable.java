package Timers;

public class Countable {
    // Nice to have one of these.
    private Timers.CountingHouse m_counter;
    protected void setCountingHouse(Timers.CountingHouse ch) {
        m_counter = ch;
    }

    // Here's some useful methods for counting.
    public boolean cnt(boolean b) {
        if(b) m_counter.inc();
        return b;
    }

    // And a straightforward one.
    public void cnt() { m_counter.inc(); }
};
