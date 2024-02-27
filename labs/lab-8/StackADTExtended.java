/**
 * Extenstions to the stack ADT.
 */
public interface StackADTExtended<T>
{
    /**
     * Generalized peek which returns an item anywhere on the stack.  The 
     * given the parameter index gives the distance down from the top
     * item, where the top is 0, the one below is at 1, etc.  If the 
     * index equals or exceeds the stack size, throws ElementNotFound
     * exception.
     * @param index Distance from the stack top of peeked item.
     */
    public T peek(int index);

    /**
     * Exchange the top two items on the stack, so the second item becomes
     * the top and the top becomes the second.  If the stack is empty or
     * has only one item, the stack is not changed.
     */
    public void swaptop();
    
    /**
     * Push another stack onto this one.  The effect is to empty the argument
     * stack, and add all its contents to this stack so that they will be
     * popped first, and in the original order.
     * @param st Stack to be pushed.  Should be of the same type as the
     * implementation.
     */
    public void pushother(StackADTExtended<T> st);

    /**
     * Reverse the stack.  The previous top element becomes the bottom, and the
     * bottom becomes the tail.  The items will be removed in the reverse 
     * order.
     */
    // public void reverse();
}
