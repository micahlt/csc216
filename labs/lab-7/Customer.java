package lab7;

/**
 * Represents a customer in the line.
 * 
 * @author Micah Lindley
 * @version 1.0
 */
public class Customer {
    private int timeEntered;

    /**
     * Creates a new customer to add to the line
     * 
     * @param time - the minute at which the customer entered the line
     */
    Customer(int time) {
        timeEntered = time;
    }

    /**
     * Get the minute that the customer entered the line
     * 
     * @return the minute that the customer entered the line
     */
    public int getTimeEntered() {
        return timeEntered;
    }
}
