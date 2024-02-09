public class LinearNode<T> {
    private LinearNode<T> next;
    private T element;

    public LinearNode() {
        element = null;
        next = null;
    }

    public LinearNode(T newEl) {
        next = null;
        element = newEl;
    }

    public LinearNode(T newEl, LinearNode newNext) {
        element = newEl;
        next = newNext;
    }

    public LinearNode<T> getNext() {
        return next;
    }

    public void setNext(LinearNode<T> node) {
        next = node;
    }

    public T getElement() {
        return element;
    }

    public void setElement(T elem) {
        element = elem;
    }
}
