abstract class AsAvailable implements InventoryItem {
    private int onHand = 0;

    public int getQty() {
        return onHand;
    }

    public void buySome(int amtBought) {
        onHand += amtBought;
    }

    public void sellSome(int amtSold) {
        if (amtSold > onHand)
            throw new OversellException("Oversold: " + name());
        onHand -= amtSold;
    }

    // This returns a string including many of the parameters of the object.
    // It is used to by descendants to simply making toString() methods.
    protected String partsString() {
        return "$" + getPrice() + ", wt: " + getWeight() + " lbs, " +
                getQty() + " on hand";
    }
}
