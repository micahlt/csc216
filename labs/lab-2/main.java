import java.io.*;
import java.util.*;

class Main {

    // Read an inventory item from the input and fetch the item.
    // If no item number is found on input, print a message and
    // return null. If one is found, print a description and
    // return the item found.
    static InventoryItem readIno(Scanner s, Inventory inven) {
        if (!s.hasNextInt()) {
            System.out.println("Missing inventory number");
            return null;
        }
        int ino = s.nextInt();
        InventoryItem itm = inven.fetchItem(ino);
        System.out.println(ino + ": " + itm.toString());
        return itm;
    }

    // Read and return a quantity. Negative if not found.
    static int readQty(Scanner s) {
        if (!s.hasNextInt()) {
            System.out.println("Missing quantity");
            return -1;
        }
        int q = s.nextInt();
        if (q < 0) {
            System.out.println("Quantity must be non-negative");
            return -1;
        }
        return q;
    }

    public static void main(String[] args) {
        Inventory inven = new Inventory();

        inven.newItem(new Chair());
        inven.newItem(new Table());
        inven.newItem(new Bench());
        inven.newItem(new Sock());
        inven.newItem(new Shoe());
        inven.newItem(new Preboxed(new Sock(), 6));
        inven.newItem(new Preboxed(new Shoe(), 3));
        inven.newItem(new Preboxed(new Chair(), 5));
        // *** Add new inventory items here. ***

        Scanner lines = new Scanner(System.in);

        // Read the input commands.
        System.out.print("cmd> ");
        while (lines.hasNextLine()) {
            // Read the line and build a scanner for its parts.
            Scanner in = new Scanner(lines.nextLine());

            // Read the command name
            String cmd = in.next();

            // Go through the commands, and implement each
            try {
                if (cmd.equals("help")) {
                    System.out.print(
                            " check   <invno>          -- Check inventory of item.\n" +
                                    " sell    <invno> <qty>    -- Sell a quantity of item.\n" +
                                    " buy     <invno> <qty>    -- Buya quantity of item.\n" +
                                    " setpr   <invno> <price>  -- Set the price of an item.\n" +
                                    " list                     -- List inventory.\n" +
                                    " totals                   -- Inventory totals.\n" +
                                    " help                     -- Just what you see.\n" +
                                    " quit                     -- Exit program.\n");
                } else if (cmd.equals("check")) {
                    InventoryItem itm = readIno(in, inven);

                } else if (cmd.equals("sell")) {
                    InventoryItem itm = readIno(in, inven);
                    int qty = itm == null ? -1 : readQty(in);
                    if (itm != null && qty >= 0) {
                        itm.sellSome(qty);
                        System.out.println("Sell " + qty + " now " +
                                itm.getQty());
                    }

                } else if (cmd.equals("buy")) {
                    InventoryItem itm = readIno(in, inven);
                    int qty = itm == null ? -1 : readQty(in);
                    if (itm != null && qty >= 0) {
                        itm.buySome(qty);
                        System.out.println("Buy " + qty + " now " +
                                itm.getQty());
                    }
                } else if (cmd.equals("setpr")) {
                    InventoryItem itm = readIno(in, inven);
                    if (!in.hasNextDouble()) {
                        System.out.println("Missing price");
                    }
                    if (itm != null && in.hasNextDouble()) {
                        itm.setPrice(in.nextDouble());
                        System.out.println("Price now $" + itm.getPrice());
                    }
                } else if (cmd.equals("list")) {
                    Iterator<Integer> i = inven.stockNumbers().iterator();
                    while (i.hasNext()) {
                        Integer stkno = i.next();
                        InventoryItem inv = inven.fetchItem(stkno);
                        System.out.println(stkno + ": " + inv);
                    }
                } else if (cmd.equals("totals")) {
                    double totwt = 0.0;
                    int number = 0;
                    int value = 0;
                    Iterator<Integer> i = inven.stockNumbers().iterator();
                    while (i.hasNext()) {
                        InventoryItem inv = inven.fetchItem(i.next());
                        number += inv.getQty();
                        totwt += inv.getQty() * inv.getWeight();
                        value += inv.getQty() * inv.getPrice();
                    }
                    System.out.println(inven.countItems() + " items, " +
                            number + " units in stock.  Weighs " +
                            totwt + " lbs, $" + value);
                } else if (cmd.equals("quit")) {
                    return;
                } else {
                    System.out.println("Unknown command " + cmd);
                }
            } catch (InventoryException e) {
                System.out.println(e.getMessage());
            }
            System.out.print("cmd> ");
        }
    }
}
