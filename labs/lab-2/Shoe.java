public class Shoe extends MayBeBackordered {
    private double price = 25.00;

    public String name() {
        return "Cheap shoe";
    }

    // Return a description
    public String toString() {
        return name() + ": " + partsString();
    }

    // Return other parameters.
    public double getWeight() {
        return 3.0;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public void buySome(int amtBought) {
        if (amtBought % 2 == 0) {
            super.buySome(amtBought);
        } else {
            throw new PairingException("This item can only be bought in pairs");
        }
    }

    @Override
    public void sellSome(int amtSold) {
        if (amtSold % 2 == 0) {
            super.sellSome(amtSold);
        } else {
            throw new PairingException("This item can only be sold in pairs");
        }
    }
}