public class LinkedStack<T> implements StackADT<T> {
    private int count;
    private LinearNode<T> top;

    public LinkedStack() {
        count = 0;
        top = null;
    }

    public void push(T element) {
        LinearNode<T> temp = new LinearNode<T>(element);

        temp.setNext(top);
        top = temp;
        count++;
    }

    public T pop() throws EmptyCollectionException {
        if (isEmpty()) {
            throw new EmptyCollectionException("stack");
        }

        T result = top.getElement();
        top = top.getNext();
        count--;
        return result;
    }

    public T peek() throws EmptyCollectionException {
        if (isEmpty()) {
            throw new EmptyCollectionException("stack");
        }

        T result = top.getNext().getElement();
        return result;
    }

    public boolean isEmpty() {
        return count == 0;
    }

    public int size() {
        return count;
    }

    public String toString() {
        String result = "\n";
        LinearNode<T> currentNode = top;
        for (int i = 1; i < count; i++) {
            result += currentNode.getElement() + "";
            currentNode = currentNode.getNext();
        }
        return result;
    }
}
