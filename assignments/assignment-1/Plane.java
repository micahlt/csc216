public class Plane {
    private int maxLandingQueueTime;
    private int timeEntered;

    /**
     * Plane (landing) constructor
     * 
     * @param maxLandingQueueTime - maximum amount of time that a plane can stay in
     *                            the landing queue without running out of fuel and
     *                            crashing;
     * @param currentTime         - current time on clock
     */
    public Plane(int maxLandingQueueTime, int currentTime) {
        this.maxLandingQueueTime = maxLandingQueueTime;
        this.timeEntered = currentTime;
    }

    /**
     * Plane (taking off) constructor
     * 
     * @param currentTime - current time on clock
     */
    public Plane(int currentTime) {
        this.timeEntered = currentTime;
    }

    /**
     * Gets the time at which the plane entered the landing or takeoff queue
     * 
     * @return a time
     */
    public int getTimeEntered() {
        return timeEntered;
    }
}
