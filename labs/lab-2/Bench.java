public class Bench extends AsAvailable {
    private double price = 99.50;

    public String name() {
        return "Wooden bench";
    }

    // Return a description
    public String toString() {
        return name() + ": " + partsString();
    }

    // Return other parameters.
    public double getWeight() {
        return 30.0;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

}