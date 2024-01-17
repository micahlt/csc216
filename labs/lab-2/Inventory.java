import java.lang.*;
import java.util.*;
class Inventory {
    private TreeMap<Integer,InventoryItem> items =
        new TreeMap<Integer,InventoryItem>();
    private int nextStockNo = 1000;

    public int newItem(InventoryItem item) {
        int stockNo = nextStockNo++;
        items.put(stockNo, item);
        return stockNo;
    }

    public InventoryItem fetchItem(Integer stockNo)  {
        InventoryItem itm = items.get(stockNo);
        if(itm == null) {
            throw new InventoryException("Unknown stock number " + stockNo);
        }
        return itm;
    }

    public int countItems() {
        return items.size();
    }

    public Set<Integer> stockNumbers() {
        return items.keySet();
    }
}
