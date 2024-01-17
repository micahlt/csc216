class Table extends AsAvailable {
    private double price = 202.50;

    public String name() {
        return "Mediocre table";
    }
    
    // Return a description
    public String toString() {
        return name() + ": " + partsString();
    }

    // Return other parameters.
    public double getWeight() {
        return 241.22;
    }
    public double getPrice() {
        return price;
    }
    public void setPrice(double price) {
        this.price = price;
    }

}
