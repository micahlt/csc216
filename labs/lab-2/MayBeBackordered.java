abstract class MayBeBackordered implements InventoryItem {
    private int onHand = 0;
    private int backOrderCount = 0;

    public int getQty() {
        return onHand;
    }

    public int getBackordered() {
        return backOrderCount;
    }

    public void buySome(int amtBought) {
        onHand += amtBought - backOrderCount;
        if (onHand < 0)
            onHand = 0;
        if (backOrderCount > 0) {
            backOrderCount -= amtBought;
            if (backOrderCount < 0) {
                backOrderCount = 0;
            }
        }
    }

    public void sellSome(int amtSold) {
        if (amtSold > onHand) {
            backOrderCount += amtSold - onHand;
            onHand = 0;
        } else {
            onHand -= amtSold;
        }
    }

    // This returns a string including many of the parameters of the object.
    // It is used to by descendants to simply making toString() methods.
    protected String partsString() {
        return "$" + getPrice() + ", wt: " + getWeight() + " lbs, " +
                getQty() + " on hand, " + getBackordered() + " backordered";
    }
}
