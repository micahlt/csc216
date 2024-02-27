import javax.sound.sampled.Line;

/**
 * Represents a linked implementation of a stack.
 *
 * @author Java Foundations
 * @version 4.0
 */

public class LinkedStack<T> implements StackADT<T>, StackADTExtended<T> {
    private int count;
    private LinearNode<T> top;

    /**
     * Creates an empty stack.
     */
    public LinkedStack() {
        count = 0;
        top = null;
    }

    /**
     * Adds the specified element to the top of this stack.
     * 
     * @param element element to be pushed on stack
     */
    public void push(T element) {
        LinearNode<T> temp = new LinearNode<T>(element);

        temp.setNext(top);
        top = temp;
        count++;
    }

    /**
     * Removes the element at the top of this stack and returns a
     * reference to it.
     * 
     * @return element from top of stack
     * @throws EmptyCollectionException if the stack is empty
     */
    public T pop() throws EmptyCollectionException {
        if (isEmpty())
            throw new EmptyCollectionException("stack");

        T result = top.getElement();
        top = top.getNext();
        count--;

        return result;
    }

    /**
     * Returns a reference to the element at the top of this stack.
     * The element is not removed from the stack.
     * 
     * @return element on top of stack
     * @throws EmptyCollectionException if the stack is empty
     */
    public T peek() throws EmptyCollectionException {
        if (isEmpty())
            throw new EmptyCollectionException("stack");

        return top.getElement();
    }

    /**
     * Returns true if this stack is empty and false otherwise.
     * 
     * @return true if stack is empty
     */
    public boolean isEmpty() {
        return count == 0;
    }

    /**
     * Returns the number of elements in this stack.
     * 
     * @return number of elements in the stack
     */
    public int size() {
        return count;
    }

    /**
     * Returns a string representation of this stack.
     * 
     * @return string representation of the stack
     */
    public String toString() {
        String ret = "<";
        LinearNode<T> scan = top;
        while (scan != null) {
            ret += " " + scan.getElement();
            scan = scan.getNext();
        }

        return ret + " ]";
    }

    /**
     * Generalized peek which returns an item anywhere on the stack. The
     * given the parameter index gives the distance down from the top
     * item, where the top is 0, the one below is at 1, etc. If the
     * index equals or exceeds the stack size (or is negative), throws
     * ElementNotFound exception.
     * 
     * @param index Distance from the stack top of peeked item.
     */
    public T peek(int index) throws ElementNotFoundException {
        LinearNode<T> currentNode = top;
        if (currentNode == null) {
            throw new ElementNotFoundException("LinkedStack");
        }
        for (int i = 0; i < index; i++) {
            if (currentNode.getNext() == null) {
                throw new ElementNotFoundException("LinkedStack");
            }
            currentNode = currentNode.getNext();
        }
        return currentNode.getElement(); // Replace this with a correct return value.
    }

    /**
     * Exchange the top two items on the stack, so the second item becomes
     * the top and the top becomes the second. If the stack is empty or
     * has only one item, the stack is not changed.
     */
    public void swaptop() {
        if ((top == null) || (top.getNext() == null))
            return;
        LinearNode<T> second = top.getNext();
        top.setNext(second.getNext());
        second.setNext(top);
        top = second;
    }

    /**
     * Push another stack onto this one. The effect is to empty the argument
     * stack, and add all its contents to this stack so that they will be
     * popped first, and in the original order. Will throw a ClassCastExcetion
     * of the argument is not a LinkedStack<T>.
     * 
     * @param st Stack to be pushed.
     */
    public void pushother(StackADTExtended<T> st) {
        LinkedStack<T> tempStack = (LinkedStack<T>) st;
        if (tempStack.size() == 0)
            return;
        LinearNode<T> paramStackTail = tempStack.top;
        while (paramStackTail.getNext() != null) {
            paramStackTail = paramStackTail.getNext();
        }
        paramStackTail.setNext(top);
        top = tempStack.top;
        count += tempStack.size();
        tempStack.top = null;
        tempStack.count = 0;
    }
}
