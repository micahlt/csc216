import java.util.*;
import Testing.ErrCounter;

/* 
 * Class for testing the extended stack interface.  Also runs the
 * tests for the basic iterface.
 */
class ExtendedStackTester extends StackTester {
    /*
     * Test swaptop() by filling a stack with the contents of the array,
     * then popping the items in pairs, after swapping them. It checks
     * that the correct values come out.
     */
    public void double_swap_test(Integer[] vals, ErrCounter err) {
        String eo = vals.length % 2 == 0 ? "even" : "odd";

        LinkedStack<Integer> nums = new LinkedStack<Integer>();
        for (int i = 0; i < vals.length; ++i)
            nums.push(vals[i]);
        int ndx = vals.length - 1;
        while (!nums.isEmpty()) {
            nums.swaptop();
            Integer t1 = nums.pop();
            if (nums.isEmpty()) {
                if (!t1.equals(vals[ndx]))
                    err.failMessage("Swaptop on singleton list failed.  (" +
                            eo + " test)");
            } else {
                Integer t2 = nums.pop();
                if (!t1.equals(vals[ndx - 1]) || !t2.equals(vals[ndx])) {
                    err.failMessage("Swaptop failed.  Saw " + t1 + " then " +
                            t2 + " instead of " + vals[ndx - 1] +
                            " then " + vals[ndx] +
                            " (" + eo + " test)");
                }
            }
            ndx -= 2;
        }
    }

    /*
     * Run the tests on the new methods in the extended interface.
     */
    public void extended_test(ErrCounter err) {
        err.setTraceBase();

        // Try the indexed peek.
        LinkedStack<String> ip = new LinkedStack<String>();
        String[] words = { "red", "blue", "orange", "lumpy",
                "ridiculous", "odd", "Tuesday" };
        err.enter("Indexed Peek",
                "Testing indexed peek.  Stack is\n" +
                        "   (bottom) " + Arrays.toString(words) + " (top)");
        for (int i = 0; i < words.length; ++i) {
            ip.push(words[i]);
        }
        for (int ndx = 0; ndx <= words.length + 2; ndx++) {
            try {
                String actuallyIs = ip.peek(ndx);
                if (ndx >= words.length) {
                    err.failMessage("Indexed peek() failed to throw " +
                            "when out of bounds.  " + ndx + " > " +
                            (words.length - 1));
                    continue;
                }
                String shouldBe = words[words.length - ndx - 1];
                if (!shouldBe.equals(actuallyIs)) {
                    err.failMessage("Peek(" + ndx + ") is " + actuallyIs +
                            ", but should be " + shouldBe);
                }
            } catch (ElementNotFoundException enf) {
                if (ndx < words.length) {
                    err.failMessage("Indexed Peek() throws at " + ndx +
                            " instead of " + words.length +
                            " or more.  Exception: " + enf);
                }
            } catch (Exception e) {
                err.failMessage("Unexpected exception", e);
            }
        }

        // Make sure we can use an empty stack, and it always throws not
        // found.
        LinkedStack<Integer> is = new LinkedStack<Integer>();
        try {
            Integer i = is.peek(0);
            err.failMessage("Peek(0) on empty stack failed to throw " +
                    "ElementNotFoundException");
        } catch (ElementNotFoundException enf) {
        } catch (Exception e) {
            err.failMessage("Unexpected exception (peek(0) empty test)", e);
        }
        try {
            Integer i = is.peek(1);
            err.failMessage("Peek(1) on empty stack failed to throw " +
                    "ElementNotFoundException");
        } catch (ElementNotFoundException enf) {
        } catch (Exception e) {
            err.failMessage("Unexpected exception (peek(1) empty test)", e);
        }
        err.leaveAll();

        Integer[] a56 = { 56 };

        err.enter("Swaptop");
        try {
            // Swaptop() no-change cases.
            err.quietEnter("No-change",
                    "Make sure empty stack swaptop does nothing.");
            LinkedStack<Integer> mt1 = new LinkedStack<Integer>();
            mt1.swaptop();
            if (!mt1.isEmpty() || mt1.size() != 0)
                err.failMessage("Swap on empty stack un-emptied it.");
            err.leave();

            err.quietEnter("Singleton",
                    "Make sure singleton stack swaptop does nothing.");
            mt1.push(56);
            mt1.swaptop();
            verify(a56, mt1, err);
            err.leave();

            // Try the swap, by pairs, even and odd.
            err.quietEnter("By Pairs");
            {
                Integer[] swi = { 14, 5, 44, 12 };
                err.quietEnter("Even",
                        "Swaptop tested in pairs.  Stack is\n" +
                                "   (bottom) " + Arrays.toString(swi) + " (top)");
                double_swap_test(swi, err);
                err.leave();
            }
            {
                Integer[] swi = { 19, -4, 12, 44, 18 };
                err.quietEnter("Odd",
                        "Swaptop tested in pairs.  Stack is\n" +
                                "   (bottom) " + Arrays.toString(swi) + " (top)");
                double_swap_test(swi, err);
                err.leave();
            }
            err.leave();

            // Swap test moving one down.
            LinkedStack<String> st = new LinkedStack<String>();
            String[] sa = { "peep", "booble", "niblob", "wonk", "neep" };
            err.quietEnter("Move Down",
                    "Swaptop tested singly.  Stack is\n" +
                            "   (bottom) " + Arrays.toString(sa) + " (top)");
            for (int i = 0; i < sa.length; ++i)
                st.push(sa[i]);
            st.push("onion");
            int ndx = sa.length - 1;
            while (st.size() > 1) {
                st.swaptop();
                String top = st.pop();
                if (!top.equals(sa[ndx])) {
                    err.failMessage("Swaptop did not hide previous top.");
                }
                --ndx;
            }
            String top = st.pop();
            if (!top.equals("onion")) {
                err.failMessage("Swaptop lost old top");
            }
            if (!st.isEmpty() || st.size() != 0) {
                err.failMessage("Swaptop test did not empty stack.");
            }
            err.leave();
        } catch (Exception e) {
            err.failMessage("Unexpected exception", e);
        }
        err.leaveAll();

        Integer[] ivalues = { 18, 5, 76, -7, 13, 9, 18, 2, 141, 17,
                55, 8, 17, -30, 2, 58, 17, 3, 22 };

        // Try out extended push.
        err.enter("Pushother");
        try {
            err.quietEnter("General");
            LinkedStack<Integer> l1 = new LinkedStack<Integer>();
            LinkedStack<Integer> l2 = new LinkedStack<Integer>();
            LinkedStack<Integer> l3 = new LinkedStack<Integer>();
            for (int i = 0; i < 5; ++i) {
                l1.push(ivalues[i]);
            }
            for (int i = 5; i < ivalues.length - 6; ++i) {
                l2.push(ivalues[i]);
            }
            for (int i = ivalues.length - 6; i < ivalues.length; ++i) {
                l3.push(ivalues[i]);
            }
            l1.pushother(l2);
            l1.pushother(l3);
            if (!l2.isEmpty() || !l3.isEmpty())
                err.failMessage("pushother did not empty stacks");
            if (l2.size() != 0 || l3.size() != 0)
                err.failMessage("pushother did not zero sizes");
            verify(ivalues, l1, err);
            err.leave();

            // Simpler cases. All empty.
            err.quietEnter("All Empty");
            LinkedStack<Integer> mt1 = new LinkedStack<Integer>();
            LinkedStack<Integer> mt2 = new LinkedStack<Integer>();
            mt1.pushother(mt2);
            if (!mt1.isEmpty() || !mt2.isEmpty() ||
                    mt1.size() != 0 || mt2.size() != 0)
                err.failMessage("pushother of empties failed.");
            err.leave();

            // Make a small stack and push empty on it.
            err.quietEnter("empty onto non-empty");
            LinkedStack<Integer> nums = new LinkedStack<Integer>();
            Integer[] nvals = { 4, 18, -5, 10 };
            for (int i = 0; i < nvals.length; ++i)
                nums.push(nvals[i]);
            mt2 = new LinkedStack<Integer>();
            nums.pushother(mt2);
            verify(nvals, nums, err);
            err.leave();

            // Push it onto an empty.
            err.quietEnter("non-empty onto empty");
            nums = new LinkedStack<Integer>();
            for (int i = 0; i < nvals.length; ++i)
                nums.push(nvals[i]);
            mt2 = new LinkedStack<Integer>();
            mt2.pushother(nums);
            verify(nvals, mt2, err);
            if (!nums.isEmpty() || nums.size() != 0)
                err.failMessage("pushother() to empty stack did not empty " +
                        "argument");
            err.leave();
        } catch (Exception e) {
            err.failMessage("Unexpected exception", e);
        }
        err.leaveAll();

        /*
         * ***** Omit the Reverse tests. Not part of assignment. *****
         * // Try out extended push.
         * err.enter("Reverse");
         * 
         * try {
         * // Reverse tests. Empty stack.
         * err.quietEnter("Empty");
         * LinkedStack<Integer> mt1 = new LinkedStack<Integer>();
         * mt1.reverse();
         * if(!mt1.isEmpty() || mt1.size() != 0) {
         * err.failMessage("Reverse empty not empty.");
         * }
         * err.leave();
         * 
         * err.quietEnter("Singlton");
         * mt1.push(56);
         * mt1.reverse();
         * verify(a56, mt1, err);
         * err.leave();
         * 
         * // Reverse a larger stack.
         * err.quietEnter("General");
         * LinkedStack<Integer> nums = new LinkedStack<Integer>();
         * for(int i = ivalues.length-1; i >= 0; --i)
         * nums.push(ivalues[i]);
         * nums.reverse();
         * verify(ivalues, nums, err);
         * err.leave();
         * 
         * } catch(Exception e) {
         * err.failMessage("Unexpected exception", e);
         * }
         * err.leaveAll();
         */
    }
}
