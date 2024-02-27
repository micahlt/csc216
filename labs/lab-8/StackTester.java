import java.util.*;
import Testing.ErrCounter;

/* 
 * Class for testing the stack interface. 
 */
class StackTester {

    /*
     * Verify that the contents of array cont match stack s, with the
     * last item in cont being on the top of the stack. The process will
     * empty the stack, but not change the array.
     */
    public <T, S extends StackADT<T>> void verify(T[] cont, S s, ErrCounter err) {
        err.quietEnter("Verify Contents");

        // Go through the members of the array backwards, from the
        // high-subscript end down to zero. For each member, we pop the
        // stack and see if they match.
        for (int i = cont.length - 1; i >= 0; --i) {

            // Since the stack (should be) the same size as the array,
            // it should not become empty.
            if (s.isEmpty()) {
                err.failMessage("Fewer stack items than expected");
                break;
            }

            // The next thing we pop should be the current array member.
            if (!cont[i].equals(s.peek())) {
                err.failMessage("Incorrect peek while popping (" +
                        s.peek() + " should be " + cont[i] + ")");
            }

            // Pop a member.
            T itm = s.pop();

            // The (remaining) stack size should be the same as the subscript
            if (s.size() != i) {
                err.failMessage("Incorrect size while popping (" +
                        s.size() + " should be " + i + ")");
            }

            // What we popped should be equal to the array item.
            if (!itm.equals(cont[i])) {
                err.failMessage("Popped " + itm + " should be " + cont[i]);
            }
        }

        // The loop should have emptied the stack.
        if (!s.isEmpty()) {
            err.failMessage("Stack should be empty after content test.");
        }

        // This could be an issue with some implementations. Make sure
        // popping now throws the proper exception
        try {
            T top = s.pop();
        } catch (EmptyCollectionException e) {
            // This _should_ happen.
            err.leave();
            return;
        }

        // But maybe it didn't
        err.failMessage("Popping emptied stack didn't throw " +
                "EmptyCollectionException.");
        err.leave();
    }

    /*
     * Push the contents of the array cont, starting in position 0, onto s,
     * checking that each push is correct.
     */
    public <T, S extends StackADT<T>> void check_stack(T[] cont, S s, ErrCounter err) {
        err.enter("Bulk push test",
                "Fill and empty a stack, make sure everthing goes on and " +
                        "comes off in the correct order.  Data is \n   (bottom) " +
                        Arrays.toString(cont) + " (top)");

        // Stack should be empty to begin with. (This is the caller's
        // responsibility).
        if (!s.isEmpty()) {
            err.failMessage("Stack should be empty before content test.");
        }

        // Go through the array pushing the members.
        for (int i = 0; i < cont.length; ++i) {
            // The size of the stack should equal the subscript before pushing.
            if (s.size() != i) {
                err.failMessage("Incorrect size while pushing (" + s.size() +
                        " should be " + i + ")");
            }

            // Do the push.
            s.push(cont[i]);

            // We should see the item on the stack now.
            if (!cont[i].equals(s.peek())) {
                err.failMessage("Incorrect peek while pushing (" + s.peek() +
                        " should be " + cont[i] + ")");
            }
        }

        // The size of the stack should equal the array size.
        if (s.size() != cont.length) {
            err.failMessage("Incorrect size while full (" + s.size() +
                    " should be " + cont.length + ")");
        }

        // Pop the contents back of the stack verifying against the array.
        verify(cont, s, err);

        err.leave();
    }

    /*
     * Run tests based on the basic stack interface.
     */
    public void stack_test(ErrCounter err) {
        err.setTraceBase();

        try {
            // Make sure printing works ok.
            LinkedStack<Integer> ps = new LinkedStack<Integer>();
            err.enter("Print Stacks");
            System.out.println("  Empty stack: " + ps);
            ps.push(25);
            System.out.println("  Singleton stack: " + ps);
            ps.push(12);
            ps.push(-3);
            ps.push(4);
            System.out.println("  Larger stack: " + ps);
            err.leave();

            // Content tests. Each test creates an array and a stack,
            // and runs check_stack to verify the stack's behavior.
            err.enter("Integer Stack Test");
            Integer[] ivalues = { 14, 19, 3, 17, 2 };
            LinkedStack<Integer> li = new LinkedStack<Integer>();
            check_stack(ivalues, li, err);

            Integer[] ivalues2 = { 48, -34, 16, 18, 58, 248, 17, 29 };
            check_stack(ivalues, li, err);
            err.leave();

            err.enter("String Stack test");
            String[] svalues = { "Carson", "Smitty", "Joe", "Alvin", "Sally" };
            check_stack(svalues, new LinkedStack<String>(), err);
            err.leave();

            err.enter("Singleton Double stack");
            Double[] one = { 4.98 };
            check_stack(one, new LinkedStack<Double>(), err);
            err.leave();

            // Make sure the empty collection exception is thrown when trying
            // to peek or pop on an empty stack. The loop runs four tests,
            // simply pop a new empty stack, simply peek an new empty stack,
            // and repeat those after pushing and popping one stack item so
            // the stack isn't quite so new.
            err.enter("Exception test",
                    "Testing for empty stack exceptions.  Stack is empty " +
                            "or contains 10.");
            for (int i = 0; i < 4; ++i) {
                // Create a stack.
                LinkedStack<Integer> ls = new LinkedStack<Integer>();

                // For tests 2 and 3, push something then pop it.
                Integer p = 0;
                if (i == 2 || i == 3) {
                    ls.push(10);
                    p = ls.pop();
                }

                // Verify that the stack is empty (might as well).
                if (!ls.isEmpty()) {
                    err.failMessage("Stack should be empty during " +
                            "exception test.");
                }

                // Try the to pop the stack (tests 0 and 2), or peek it
                // (tests 1 and 3).
                try {
                    if (i == 0 || i == 2)
                        p = ls.pop();
                    else
                        p = ls.peek();

                    err.failMessage("Failed to throw execption on pop or " +
                            "peek on empty.");
                } catch (EmptyCollectionException e) {
                    // Good.
                }
            }
            err.leave();
        } catch (Exception e) {
            err.failMessage("Unexpected exception", e);
            err.leaveAll();
        }
    }
}
