import java.util.*;

/*
 * Utilities for testing a list of T.
 */

/*
 * This class contains methods for checking a list implementing the base
 * list ADT.  It has been modified to use the newer ErrCounter class.
 * Result is a bit of a combo, avoiding a full re-write of the tester.
 */
class ListTester<T> {
    // Counter.
    private Testing.ErrCounter m_counter = new Testing.ErrCounter();
    public Testing.ErrCounter counter() { return m_counter; }
    
    public int get_err_count() { return m_counter.getErrorCount(); }

    // Verify the length and emptiness.  Make sure the length is the
    // value specified, and the the isEmpty() agrees with the size being
    // zero or not.
    public <T> void check_size(DoubleLinkedList<T> list, int length)
    {
        // See if the size agrees with specification.
        if(length != list.getSize()) {
            m_counter.failMessage("size is " +
                                  list.getSize() + " instead of " + length);
        }

        // See if isEmpty() agrees with size being zero.
        if(list.getSize() == 0 && !list.isEmpty()) {
            m_counter.failMessage("size is " + 
                                  list.getSize() + " but list is not empty.");
        } else if(list.getSize() != 0 && list.isEmpty()) {
            m_counter.failMessage("size is " +
                                  list.getSize() + " but list is empty.");
        }

        // Some special checks for empty list.
        if(length == 0 && (list.getFirst() != null || list.getLast() != null)) {
            m_counter.failMessage("list is empty, but " +
                                  "getFirst() or getLast() is not null");
        }
    }

    // Verify the contents of the list compared to the array.
    public <T> void verify(DoubleLinkedList<T> list, T[] cont)
    {
        check_size(list, cont.length);

        // Make an iterator for the list, and corresponding index for the array.
        // The Cursor implements Iterator, but has additional features.
        DoubleLinkedList<T>.Cursor lscan = list.cursor();
        int i = 0;

        // Scan the list and array and see if they agree.  
        while(lscan.hasNext() && i < cont.length) {
            T litem = lscan.next();
            if(!cont[i].equals(litem)) {
                m_counter.failMessage("list postion " + i +
                                      " is " + litem + ", but should be " +
                                      cont[i]);
            }
            ++i;
        }

        // Check that one doesn't have extra items.
        if(lscan.hasNext()) {
            m_counter.failMessage("(verify), list is " +
                                  "too long.");
        }
        else if(i < cont.length) {
            m_counter.failMessage("(verify), list is " +
                                  "too short.");
        }

        // Now do the same backwards.  The list Cursor can go both
        // ways.  These are features in addition to a standard Java 
        // Iterator.  Perhaps enough to make it useful.
        lscan.reset();
        if(cont.length == 0) {
            if(lscan.back() != null) {
                m_counter.failMessage("(verify), content " +
                                      "should be empty, but there is a last " +
                                      "item.");
            }
        } else {
            i = cont.length - 1;
            lscan.back();
            while(i >= 0 && !lscan.isNull()) {
                if(!cont[i].equals(lscan.curr())) {
                    m_counter.failMessage("reverse scan, " +
                                          "list postion " + i + " is " +
                                          lscan.curr() + ", but should be " + 
                                          cont[i]);
                }

                --i;
                lscan.back();
            }

            if(i >= 0) {
                m_counter.failMessage("(verify) " +
                                      " reverse scan, list is too short.");
            } else if(!lscan.isNull()) {
                m_counter.failMessage("(verify) " +
                                      " reverse scan, list is too long.");
            }
        }
    }

    // Check the find method.  Given the list, the item, and correct
    // presence.  Returns the found item cursor
    public <T> DoubleLinkedList<T>.Cursor check_find
        (DoubleLinkedList<T> list, T item, boolean present)
    {
        DoubleLinkedList<T>.Cursor loc = list.find(item);
        if(loc.isNull()) {
            if(present) {
                m_counter.failMessage("did not find " +
                                      item + " as expected.");
            }
        } else {
            if(!present) {
                m_counter.failMessage("find found " + item +
                                      " when not expected.");
            }
            if(!Testing.EQ.test(item,loc.curr())) {
                m_counter.failMessage("find found " +
                                      loc.curr() + " instead of " + item);
            }
        }

        return loc;
    }

    // Check that the list contains all the things we can see when we 
    // iterate through.
    public <T> void check_find_all(DoubleLinkedList<T> list)
    {
        Iterator<T> lscan = list.iterator();

        while(lscan.hasNext()) {
            check_find(list, lscan.next(), true);
        }
    }

    // Check what the list thinks its first and last are contained in the list.
    // If the list is size one, also check first and last are the same.  If
    // either first or last are non-null, they specify the correct value and 
    // are checked against what the list says.
    public <T> void check_first_last(DoubleLinkedList<T> list, T first, T last)
    {
        // If the list is empty, make sure first() and last() are null.
        if(list.isEmpty()) {
            if(first != null || last != null) {
                m_counter.failMessage("check_first_last) " +
                                      "list unexpectedly empty.");
            }

            if(list.getFirst() != null) {
                m_counter.failMessage("(check_first_last) " +
                                      "empty list has a front (" + 
                                      list.getFirst() + ")");
            }
            if(list.getLast() != null) {
                m_counter.failMessage("(check_first_last) " +
                                      "empty list has a last (" + 
                                      list.getLast() + ")");
            }
        } else {
            // Not empty.  Take it through its paces.
            T firstitem = list.getFirst();
            if(first != null && !Testing.EQ.test(first, firstitem)) {
                m_counter.failMessage("incorrect first " +
                                      "element: " + firstitem + ": " +
                                      (firstitem == null ?
                                       "null" : "not " + first));
            }
            
            // Make sure it contains it.
            check_find(list, firstitem, true);

            T lastitem = list.getLast();
            if(last != null && !Testing.EQ.test(last, lastitem)) {
                m_counter.failMessage("incorrect last " +
                                      "element: " + lastitem + ": " +
                                      (lastitem == null ?
                                       "null" : "not " + last));
            }
            
            // Make sure it contains it.
            check_find(list, lastitem, true);

            // For a singleton list, the first and last must match.
            if(list.getSize() == 1 && !Testing.EQ.test(firstitem, lastitem)) {
                m_counter.failMessage("singleton list head "
                                      + "and tail don't match (" + firstitem +
                                      " and " + lastitem + ")");
            }
        }
    }

    // The remove first tester.  Removes the first item and makes sure it
    // agrees with the value sent.  If item is sent as null, just use what
    // the list says is first.  Also verifies the size change with removal
    // of the item.
    public <T> void remove_first_chk(DoubleLinkedList<T> list, T item)
    {
        // If list is empty and an item was sent, probably didn't mean for
        // the list to be empty.
        if(list.isEmpty()) {
            if(item != null) {
                m_counter.failMessage("(remove_first_chk) " +
                                      "list unexpectedly empty");
            }
            return;
        }

        // Check that item matches first.
        if(item == null)
            item = list.getFirst();

        if(!Testing.EQ.test(item,list.getFirst())) {
            m_counter.failMessage("first item in list is " +
                                  list.getFirst() + ", but should be " + item);
        }

        // Remember the size, then remove the item.
        int oldsize = list.getSize();
        T old_first = list.removeFirst();
        if(!Testing.EQ.test(old_first,item)) {
            m_counter.failMessage("removed first item was " +
                                  old_first + ", but should be " + item);
        }
        if(old_first == null && oldsize != 0) {
            m_counter.failMessage("unable to remove first " +
                                  "item from non-empty list.");
        }

        // Check the size change.
        check_size(list, item == null ? oldsize : oldsize - 1);
    }

    // The remove back tester.  Removes the last item and makes sure it
    // agrees with the value sent.  If item is sent as null, just use what
    // the list says is last.  Also verifies the size change with removal
    // of the item.
    public <T> void remove_last_chk(DoubleLinkedList<T> list, T item)
    {
        // If list is empty and an item was sent, probably didn't mean for
        // the list to be empty.
        if(list.isEmpty() && item != null) {
            m_counter.failMessage("(remove_last_chk) " +
                                  "list unexpectedly empty");
            return;
        }

        // Check that item matches first.
        if(item == null)
            item = list.getLast();

        if(!Testing.EQ.test(item, list.getLast())) {
            m_counter.failMessage("last item in list is " +
                                  list.getLast() + ", but should be " + item);
        }

        // Remember the size, then remove the item.
        int oldsize = list.getSize();
        T old_last = list.removeLast();
        if(!Testing.EQ.test(old_last,item)) {
            m_counter.failMessage("removed last item was " +
                                  old_last + ", but should be " + item);
        }
        if(old_last == null && oldsize != 0) {
            m_counter.failMessage("unable to remove last " +
                                  "item from non-empty list.");
        }

        // Check the size change.
        check_size(list, item == null ? oldsize : oldsize - 1);
    }

    // The general remove tester.  Given the value to remove, and whether it
    // is present in the list.
    public <T> void remove_mid_chk(DoubleLinkedList<T> list,
                                   T item, boolean present)
    {
        // See if find can find the item, and if that is the expected result.
        DoubleLinkedList<T>.Cursor loc = check_find(list, item, present);
        if(loc.isNull()) return;

        // Note the existing successor, then return.
        T after = loc.fwd();
        loc.back();

        // Remember the size, then remove the item.
        int oldsize = list.getSize();
        DoubleLinkedList<T>.Cursor afterloc = list.remove(loc);

        if(!Testing.EQ.test(after,afterloc.curr())) {
            m_counter.failMessage("remove returns bad " +
                                  "cursor, " + afterloc.curr() +
                                  ", but should show " + after);
        }

        // Check the size change.
        check_size(list, oldsize - 1);
    }

    // This runs several of the remove checks, as selected by the presence
    // of non-null items in the argument list.  Null means not to run 
    // the test.
    public <T> void remove_chk(DoubleLinkedList<T> list,
                               T first, T last, T present, T absent)
    {
        if(first != null) remove_first_chk(list, first);
        if(last != null) remove_last_chk(list, last);
        if(present != null) remove_mid_chk(list, present, true);
        if(absent != null) remove_mid_chk(list, absent, false);
    }

    // Run some sanity checks that should be true of any list
    public <T> void sanity_check(DoubleLinkedList<T> list)
    {
        if((list.getSize() == 0) != list.isEmpty()) {
            m_counter.failMessage(", size is " +
                                  list.getSize() + " but list is" +
                                  (list.isEmpty() ? " " : " not ") + "empty.");
        }

        check_find_all(list);
        check_first_last(list, null, null);
    }

    // See if two lists are equal.  Check twice, forwards and backwards.
    public <T> void scan_test(DoubleLinkedList<T> list1,
                              DoubleLinkedList<T> list2)
    {
        DoubleLinkedList<T>.Cursor cur1 = list1.cursor();
        DoubleLinkedList<T>.Cursor cur2 = list2.cursor();

        T itm1 = cur1.fwd();
        T itm2 = cur2.fwd();
        while(itm1 != null) {
            if(!Testing.EQ.test(itm1,itm2)) {
                m_counter.failMessage("scan_test, " +
                                      "fwd scan, " + itm1 + " not equal to " +
                                      itm2);
            }
            itm1 = cur1.fwd();
            itm2 = cur2.fwd();
        }
        if(itm2 != null) {
            m_counter.failMessage("scan_test, lists of unequal length");
        }

        // Back the other way.
        cur1.reset();
        cur2.reset();

        itm1 = cur1.back();
        itm2 = cur2.back();
        while(itm1 != null) {
            if(!Testing.EQ.test(itm1,itm2)) {
                m_counter.failMessage("scan_test, back " +
                                      "scan, " + itm1 + " not equal to " +
                                      itm2);
            }
            itm1 = cur1.back();
            itm2 = cur2.back();
        }
        if(itm2 != null) {
            m_counter.failMessage("scan_test, done_back, " +
                                  itm2 + " expected to be null");
        }
    }

    // Use the cursor to copy the list, then see if it was right.
    public <T> void rebuild_test(DoubleLinkedList<T> list)
    {
        // Copy the list backwards.
        DoubleLinkedList<T> copy = new DoubleLinkedList<T>();
        DoubleLinkedList<T>.Cursor cur = list.cursor();
        T itm = cur.back();
        while(itm != null) {
            copy.addFirst(itm);
            itm = cur.back();
        }

        // See if its equal.
        scan_test(list, copy);

        // Make a forward copy of the copy and check that.
        DoubleLinkedList<T> recopy = new DoubleLinkedList<T>();
        cur = copy.cursor();
        itm = cur.fwd();
        while(itm != null) {
            recopy.addLast(itm);
            itm = cur.fwd();
        }

        // See if its equal.
        scan_test(recopy, list);
    }

    // This tests insert by finding an olditem, and inserting
    // the newitem after it, and applying various checks to the result.
    public <T> void insert_after_test(DoubleLinkedList<T> list,
                                      T olditem, T newitem)
    {
        DoubleLinkedList<T>.Cursor loc = check_find(list, olditem, true);
        if(loc.isNull()) return;

        // Find the present successor of the olditem node.
        T succ = loc.fwd();
        loc.back();

        // Insert the newitem after it.
        list.insert(loc, newitem);
        if(loc.isNull()) {
            m_counter.failMessage("insert after test: " +
                                  "insert left null cursor.");
        } else {
            // The cursor should point to the new option after insert.
            if(!Testing.EQ.test(newitem, loc.curr())) {
                m_counter.failMessage("insert after test: " +
                                      "insert not at new item (" + loc.curr() +
                                      " instead of " + newitem + ")");
            }

            // The new node's predecessor should be whatever we inserted
            // it after.
            T item = loc.back();
            if(!Testing.EQ.test(olditem, item)) {
                m_counter.failMessage("insert after test: " +
                                      "insert not at new item not after " + 
                                      olditem + "(" + loc.curr() +
                                      " instead).");
            }

            // Then we should return to the new node.
            item = loc.fwd();
            if(!Testing.EQ.test(newitem, item)) {
                m_counter.failMessage("insert after test: " +
                                      "cursor does not return to inserted " +
                                      "item " + loc.curr() + " instead of " +
                                      newitem);
            }

            // Then we should reach the successor of the add-after node, 
            // which is now the successor of the inserted node.
            item = loc.fwd();
            if(!Testing.EQ.test(succ, item)) {
                m_counter.failMessage("insert after test: " +
                                      "inserted item has wrong successor: " +
                                      loc.curr() + " instead of " + succ);
            }
        }
    }
}


class ListTest {

    private static ListTester<String> tester = new ListTester<String>();

    // Just fill the list with the array.
    public static <T> void fill(DoubleLinkedList<T> list, T[] data)
    {
        for(T itm: data) list.addLast(itm);
    }

    // Same result as the above, but fills the list from the front,
    // going through the array backwards.
    public static <T> void fillRev(DoubleLinkedList<T> list, T[] data)
    {
        for(int i = data.length-1; i >= 0; --i)
            list.addFirst(data[i]);
    }

    public static <T> void dumpbackwards(DoubleLinkedList<T> list)
    {
        DoubleLinkedList<T>.Cursor loc = list.cursor();
        T itm = loc.back();
        while(itm != null) {
            System.out.println("===> " + itm + " <===");
            itm = loc.back();
        }
    }

    // An inserting test for linked list.
    public static void step12_test()
    {
        tester.counter().enter("Add first/last test");

        String[] foo = { "this", "is", "a", "bunch", "of", "strings", "for",
                         "testing" };

        tester.counter().quietEnter(" -- Singleton List --");
        DoubleLinkedList<String> listA = new DoubleLinkedList<String>();
        listA.addFirst("Fred");
        tester.sanity_check(listA);
        tester.check_first_last(listA, "Fred", "Fred");
        tester.check_find_all(listA);

        DoubleLinkedList<String> listB = new DoubleLinkedList<String>();
        listB.addFirst("Barney");
        tester.sanity_check(listB);
        tester.check_first_last(listB, "Barney", "Barney");
        tester.check_find_all(listB);
        tester.counter().leave();
        
        tester.counter().quietEnter(" -- Multiple List --");
        DoubleLinkedList<String> list1 = new DoubleLinkedList<String>();
        fill(list1, foo);
        tester.verify(list1, foo);
        tester.sanity_check(list1);
        tester.verify(list1, foo);
        tester.check_first_last(list1, "this", "testing");
        tester.check_find_all(list1);

        DoubleLinkedList<String> list2 = new DoubleLinkedList<String>();
        fillRev(list2, foo);
        tester.sanity_check(list2);
        tester.verify(list2, foo);
        tester.check_first_last(list2, "this", "testing");
        tester.check_find_all(list2);

        tester.check_find(list2, "borus", false);
        tester.check_find(list1, "froggie", false);
        tester.counter().leave();

        tester.counter().quietEnter(" -- Results Equal Test --");
        tester.scan_test(list1, list2);
        tester.counter().leave();

        tester.counter().quietEnter(" -- Empty find test --");
        DoubleLinkedList<String> mt  = new DoubleLinkedList<String>();
        tester.check_find(mt, "billy", false);
        tester.counter().leave();
        
        tester.counter().leave();
    }

    public static void step3_test()
    {
        tester.counter().enter("Insert/delete test");

        DoubleLinkedList<String> sl = new DoubleLinkedList<String>();

        // Insert a bunch of strings, in a careful sequence (see
        // result below).
        tester.sanity_check(sl);
        sl.addLast("bligger");
        tester.sanity_check(sl);
        sl.removeFirst();

        tester.counter().quietEnter(" -- Test A (addFirst/addLast) --");

        sl.addFirst("Albert");
        sl.addLast("goes");
        sl.addLast("south");
        tester.check_first_last(sl, "Albert", "south");
        tester.sanity_check(sl);
        tester.counter().leave();

        tester.counter().quietEnter(" -- Test B (removeLast) --");

        sl.removeLast();
        tester.check_first_last(sl, "Albert", "goes");
        tester.sanity_check(sl);
        sl.addLast("north");
        tester.counter().leave();

        tester.counter().quietEnter(" -- Test C (various mid-list tests) --");

        tester.remove_mid_chk(sl, "goes", true);
        sl.addFirst("Mr.");
        tester.check_find_all(sl);
        tester.insert_after_test(sl, "Albert", "flies");
        tester.sanity_check(sl);
        sl.removeFirst();
        tester.counter().leave();

        tester.counter().quietEnter(" -- Test D (multiple addLast) --");
        
        tester.check_first_last(sl, "Albert", "north");
        sl.addLast("on");
        sl.addLast("Tuesday");
        sl.addLast("morning.");
        tester.sanity_check(sl);
        tester.counter().leave();

        tester.counter().quietEnter(" -- Test E " +
                           "(some remove and insert_after tests) --");
        
        tester.remove_mid_chk(sl, "Tuesday", true);
        tester.sanity_check(sl);
        tester.remove_mid_chk(sl, "on", true);
        sl.removeLast();
        tester.insert_after_test(sl,"north", "west");
        tester.sanity_check(sl);
        tester.counter().leave();
        
        tester.counter().quietEnter(" -- Test F (check final result) --");
        
        // See if we got it right.
        String[] answer = { "Albert", "flies", "north", "west" };
        tester.verify(sl, answer);
        tester.check_find_all(sl);
        tester.counter().leave();
        
        tester.counter().leave();
    }
    
    public static void step4_test()
    {
        tester.counter().enter("Append test");

        DoubleLinkedList<String> l1 = new DoubleLinkedList<String>();
        DoubleLinkedList<String> l2 = new DoubleLinkedList<String>();

        tester.counter().quietEnter(" -- Empty/Empty --");

        l1.append(l2);
        tester.sanity_check(l1);
        tester.sanity_check(l2);
        tester.check_size(l1, 0);
        tester.check_size(l2, 0);
        tester.counter().leave();

        tester.counter().quietEnter(" -- Single/Empty --");

        String[] bee = { "bee" };
        l1.addLast("bee");
        l1.append(l2);
        tester.check_size(l2, 0);
        tester.verify(l1, bee);
        tester.check_first_last(l1, "bee", "bee");
        tester.check_find_all(l1);
        tester.remove_mid_chk(l1, "bee", true);
        tester.counter().leave();

        tester.counter().quietEnter(" -- Empty/Single --");

        l2.addLast("bee");
        l1.append(l2);
        tester.verify(l1, bee);
        tester.check_first_last(l1, "bee", "bee");
        tester.check_find_all(l1);
        tester.check_size(l1, 1);
        tester.check_size(l2, 0);
        l1.removeFirst();
        tester.counter().leave();

        tester.counter().quietEnter(" -- Empty/Multiple --");

        l2.addLast("thing");
        l2.addLast("otherthing");
        l2.addLast("that too");
        l1.append(l2);
        String[] em_contents = { "thing", "otherthing", "that too" };
        tester.verify(l1, em_contents);
        tester.check_first_last(l1, "thing", "that too");
        tester.check_find_all(l1);
        tester.check_size(l1, 3);
        tester.check_size(l2, 0);
        l1.clear();
        tester.counter().leave();

        tester.counter().quietEnter(" -- Multiple/Empty --");

        l1.addLast("thing");
        l1.addLast("otherthing");
        l1.addLast("that too");
        l1.append(l2);
        tester.verify(l1, em_contents);
        tester.check_first_last(l1, "thing", "that too");
        tester.check_find_all(l1);
        tester.check_size(l1, 3);
        tester.check_size(l2, 0);
        l1.clear();
        tester.counter().leave();

        tester.counter().quietEnter(" -- Multiple/Single --");

        l1.addLast("thing");
        l1.addLast("otherthing");
        l1.addLast("that too");
        l2.addLast("one more thing");
        l1.append(l2);
        String[] em2_contents =
            { "thing", "otherthing", "that too", "one more thing" };
        tester.verify(l1, em2_contents);
        tester.check_first_last(l1, "thing", "one more thing");
        tester.check_find_all(l1);
        tester.check_size(l1, 4);
        tester.check_size(l2, 0);
        l1.clear();
        tester.counter().leave();

        tester.counter().quietEnter(" -- Single/Multiple --");

        l1.addLast("thing");
        l2.addLast("otherthing");
        l2.addLast("that too");
        l2.addLast("one more thing");
        l1.append(l2);
        tester.verify(l1, em2_contents);
        tester.check_first_last(l1, "thing", "one more thing");
        tester.check_find_all(l1);
        tester.check_size(l1, 4);
        tester.check_size(l2, 0);
        l1.clear();
        tester.counter().leave();

        tester.counter().quietEnter(" -- General --");

        String[] strings1 = { "Here", "are", "some", "strings", "for", "you" };
        fill(l1, strings1);
        String[] strings2 = { "And", "some", "more" };
        fill(l2, strings2);
        l1.append(l2);
        tester.check_size(l2, 0);

        String[] strings = { "Here", "are", "some", "strings", "for", "you",
                             "And", "some", "more" };
        tester.verify(l1,strings);
        tester.check_size(l2, 0);
        tester.check_first_last(l1, "Here", "more");
        tester.check_find_all(l1);
        tester.counter().leave();

        tester.counter().leave();
    }

    public static void main(String args[])
    {
        // Prompt for the name of the file, and open it.
        System.out.print("Which step (2-4): ");
        Scanner in = new Scanner(System.in);
        int step;
        step = in.nextInt();

        try {
            step12_test();

            if(step > 2) {
                step3_test();

                if(step > 3)
                    step4_test();
            }
        }
        catch(Exception e) {
            tester.counter().failMessage("Unexpected exception " ,e);
        }

        tester.counter().finalMessage();
    }
}
