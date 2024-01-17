public class Preboxed extends MayBeBackordered {
    private int itemCount = 0;
    private InventoryItem item = null;

    Preboxed(InventoryItem item, int count) {
        itemCount = count;
        this.item = item;
    }

    public String name() {
        return "Box of " + itemCount + " " + item.name().toLowerCase() + "s";
    }

    public String toString() {
        return this.name() + ": " + partsString();
    }

    public double getWeight() {
        return item.getWeight() * itemCount;
    }

    public double getPrice() {
        return item.getPrice() * itemCount;
    }

    public void setPrice(double price) {
        item.setPrice(price / itemCount);
    }

    public int getQty() {
        return item.getQty();
    }
}
