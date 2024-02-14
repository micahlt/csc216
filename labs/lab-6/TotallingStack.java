import java.util.*;

/*
 * This is a stack of Coins which also keeps track of the total value
 * of the stack contents.
 */
class TotallingStack extends Stack<Mint.Coin> {
    // Update the stack value, then call the parent to perform the push.
    public Mint.Coin push(Mint.Coin c) {
        m_value += c.getValue();
        return super.push(c);
    }

    // Use the parent to pop, then remove that from the total value.
    public Mint.Coin pop() {
        Mint.Coin ret = super.pop();
        m_value -= ret.getValue();
        return ret;
    }

    // Return the total value of the coins on the stack.
    public int getValue() { return m_value; }

    // Value of the coins.
    private int m_value = 0;

    // Don't do this.
    public int search(Object o) {
        throw new UnsupportedOperationException("Stacks are not searchable");
    }

}
