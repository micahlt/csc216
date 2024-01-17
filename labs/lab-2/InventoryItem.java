interface InventoryItem {
    // Name of item.  This returns a short name without any details.
    public String name();
    
    // Return other parameters.
    public double getWeight();
    public double getPrice();
    public void setPrice(double price);
    public int getQty();

    // What to when you buy or sell some of the objects.
    public void buySome(int amtBought);
    public void sellSome(int amtSold);
}
