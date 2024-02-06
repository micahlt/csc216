package LinkedListImplementation;

public class LinearNode<T> {
    T element;
    LinearNode next;

    public LinearNode() {
        element = null;
        next = null;
    }

    public LinearNode(T newEl) {
        element = newEl;
        next = null;
    }

    public LinearNode(T newEl, LinearNode newNext) {
        element = newEl;
        next = newNext;
    }
}
