public class Plane {
    private int timeEntered;

    /**
     * Plane constructor
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
