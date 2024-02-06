package ArrayStackImplementation;

import java.util.Arrays;

import ArrayStackImplementation.exceptions.*;

/**
 * An array implementation of a stack in which the bottom of the stack is fixed
 * at index 0
 * 
 * @author Micah Lindley
 * @version 1.0
 */
public class ArrayStack<T> implements StackADT<T> {
    private final static int DEFAULT_CAPACITY = 100;
    private int top = 0;
    private T[] stack;

    public ArrayStack() {
        this(DEFAULT_CAPACITY);
    }

    public ArrayStack(int initialCapacity) {
        top = 0;
        stack = (T[]) (new Object[DEFAULT_CAPACITY]);
    }

    public void push(T element) {
        if (size() == stack.length) {
            expandCapacity();
        }
        stack[top] = element;
        top++;
    }

    public T pop() throws EmptyCollectionException {
        if (isEmpty()) {
            throw new EmptyCollectionException("stack");
        }
        T result = stack[top--];
        return result;
    }

    public T peek() {
        if (isEmpty()) {
            throw new EmptyCollectionException("stack");
        }
        return stack[top - 1];
    }

    public boolean isEmpty() {
        return top == 0;
    }

    public int size() {
        return top;
    }

    public void expandCapacity() {
        stack = Arrays.copyOf(stack, stack.length * 2);
    }

    public String toString() {
        if (isEmpty()) {
            throw new EmptyCollectionException("stack");
        }
        return Arrays.toString(stack);
    }
}