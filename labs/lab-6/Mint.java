/*
 * This represents the collection of Destablian coins.
 */
class Mint {
    // This is one coin.
    public static class Coin {
        private Coin(String name, int value) {
            m_name = name;
            m_value = value;
        }
        public String getName() { return m_name; }
        public int getValue() { return m_value; }
        private String m_name;
        private int m_value;

        public String toString() { return m_name; }
    }

    // This is the collection of all the coins.
    private static Coin[] m_coins = {
        new Coin("grop", 3),
        new Coin("wunkus", 7),
        new Coin("droogle", 13),
        new Coin("krompus", 16),
        new Coin("megakrompus", 37)
    };

    /*
     * Methods for the collection.
     */

    // The smallest and largest coins (a grop or a mmegakrompus, respectively).
    public static Coin smallest() { return m_coins[0]; }
    public static Coin largest() { return m_coins[m_coins.length-1]; }

    // See if you've got one of those.
    public static boolean isSmallest(Coin c)
    {
        return c.getValue() == m_coins[0].getValue();
    }
    public static boolean isLargest(Coin c)
    {
        return c.getValue() == m_coins[m_coins.length-1].getValue();
    }

    // Find the next smaller coin, or return null if
    // there is none.
    public static Coin smaller(Coin than)
    {
        for(int i = 1; i < m_coins.length; ++i) {
            if(m_coins[i].getValue() == than.getValue())
                return m_coins[i-1];
        }
        return null;
    }
}
