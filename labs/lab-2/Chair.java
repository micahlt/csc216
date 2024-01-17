class Chair extends AsAvailable {
    private double price = 138.25;

    public String name() {
        return "Nice desk chair";
    }
    
    // Return a description
    public String toString() {
        return name() + ": " + partsString();
    }

    // Return other parameters.
    public double getWeight() {
        return 17.0;
    }
    public double getPrice() {
        return price;
    }
    public void setPrice(double price) {
        this.price = price;
    }

}
