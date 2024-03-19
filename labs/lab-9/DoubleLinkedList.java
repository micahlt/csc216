// Double-linked list class.  

import java.util.*;

class DoubleLinkedList<T> {
    private class Node<T> {
        T elt;
        Node<T> next, prev;

        public Node(T e) {
            elt = e;
        }
    }

    // The head and tail are set to null by Java when the object is created.
    private Node<T> head, tail;
    private int size = 0;

    // Constructor with not much to do.
    public DoubleLinkedList() {
    }

    // Get the size of the list
    public int getSize() {
        return size;
    }

    // See if the list is empty
    // **** Implement this function (Step 1) ****
    public boolean isEmpty() {
        return size == 0;
    }

    // Discard all list contents.
    public void clear() {
        head = tail = null;
        size = 0;
    }

    // Get the first or last thing in the list. Return null if list is empty.
    public T getFirst() {
        if (head == null)
            return null;
        else
            return head.elt;
    }

    public T getLast() {
        if (tail == null)
            return null;
        else
            return tail.elt;
    }

    // Add the element to the front or the back of the list.
    public void addFirst(T elt) {
        Node<T> newNode = new Node<T>(elt);
        newNode.next = head;

        if (head == null)
            tail = newNode;
        else
            head.prev = newNode;

        head = newNode;

        ++size;
    }

    // **** Implement this function (Step 2) ****
    public void addLast(T elt) {
        Node<T> newNode = new Node<T>(elt);
        newNode.prev = tail;

        if (tail == null) {
            head = newNode;
        } else {
            tail.next = newNode;
        }

        tail = newNode;
        ++size;
    }

    // Find an element in the list. Locates the first member of
    // the list which is equal to elt. Compare with the equals() method.
    private Node<T> findNode(T elt) {
        // Search through the list.
        Node<T> scan = head;
        while (scan != null && !scan.elt.equals(elt)) {
            scan = scan.next;
        }

        return scan;
    }

    // Insert an element into the list immediately after the location
    // indicated by the pred parameter. If the predecessor is null,
    // insert at the front of the list. The links of newnode will be
    // overwritten.
    private void linkNodeAfter(Node<T> pred, Node<T> newNode) {
        // Pred will be the predecessor of the new node, its successor
        // will be the new node's successor.
        Node<T> succ;
        if (pred == null)
            succ = head;
        else
            succ = pred.next;

        // Set up the links of the new node.
        newNode.prev = pred;
        newNode.next = succ;

        // Set the links in the list.
        if (pred == null) {
            head = newNode;
        } else {
            pred.next = newNode;
        }

        if (succ == null) {
            tail = newNode;
        } else {
            succ.prev = newNode;
        }
    }

    // Unlink the the indicated node from the linked list. Returns
    // the argument.
    // **** Implement this function (Step 3) ****
    private Node<T> unlinkNode(Node<T> zombie) {
        if (zombie.prev == null && zombie.next == null) {
            // only in list
            head = null;
            tail = null;
        } else if (zombie.prev == null && zombie.next != null) {
            // first in list
            head = zombie.next;
            zombie.next.prev = null;
        } else if (zombie.next == null && zombie.prev != null) {
            // last in list
            tail = zombie.prev;
            zombie.prev.next = null;
        } else {
            // in the middle of list
            zombie.next.prev = zombie.prev;
            zombie.prev.next = zombie.next;
        }
        return zombie;
    }

    // Remove and return the first or last thing in the list. Return
    // null and leave the list unchanged if it is empty.
    public T removeFirst() {
        if (head == null)
            return null;
        --size;
        return unlinkNode(head).elt;
    }

    public T removeLast() {
        if (tail == null)
            return null;
        --size;
        return unlinkNode(tail).elt;
    }

    // Append another list onto this one, emptying the other list.
    // **** Implement this function (Step 4) ****
    public void append(DoubleLinkedList<T> other) {
        size += other.size;
        other.size = 0;
        if (other.head == null)
            return;
        if (head == null) {
            head = other.head;
            tail = other.tail;
        } else {
            other.head.prev = tail;
            tail.next = other.head;
        }
        tail = other.tail;
        other.head = null;
        other.tail = null;
    }

    // This is the iterator for the list, but it also provides additional
    // methods which allow it to be used as a general cursor indicating
    // some location in the list.
    public class Cursor implements Iterator<T> {
        private Node<T> location;

        private Cursor() {
        }

        private Cursor(Node<T> n) {
            location = n;
        }

        // Tell if this cursor is null.
        public boolean isNull() {
            return location == null;
        }

        // Return the element at the current location, or null, if the
        // cursor has not started, or has run off the end.
        public T curr() {
            if (location == null)
                return null;
            else
                return location.elt;
        }

        // Move the location forward or backward. Forward from the last,
        // or backward from the first makes the location null. Forward
        // from null goes to the first (if any), and back from null
        // goes to the last (if any). Return the element at the new
        // location. (In C, *++p or *--p, except not blowing up
        // when p is or becomes null.)
        public T fwd() {
            if (location == null) {
                location = head;
            } else {
                location = location.next;
            }
            return curr();
        }

        public T back() {
            if (location == null) {
                location = tail;
            } else {
                location = location.prev;
            }
            return curr();
        }

        // Reset the iterator, so the fwd() and back() will be the first and
        // last, resp, the iterator is back to its initial state.
        public void reset() {
            location = null;
        }

        // Implement the iterator methods. The remove is not technically
        // correct, since it won't throw if you call it twice without a
        // next in between. Could be fixed by adding a boolean to detect
        // that situation, but it really doesn't seem worth the trouble to
        // enforce a rule that's fairly stupid anyway.
        public boolean hasNext() {
            return (location == null && head != null) ||
                    (location != null && location.next != null);
        }

        public T next() {
            T ret = fwd();
            if (ret == null)
                throw new NoSuchElementException();
            return ret;
        }

        public void remove() {
            if (curr() == null)
                throw new IllegalStateException();
            DoubleLinkedList.this.remove(this);
            back();
        }
    }

    // Get the cursor, but this is also the iterator, so give both names.
    public Cursor cursor() {
        return new Cursor();
    }

    public Iterator<T> iterator() {
        return cursor();
    }

    // Find the first node equal to elt and return a cursor to it, or a null
    // cursor.
    public Cursor find(T elt) {
        return new Cursor(findNode(elt));
    }

    // Insert the data item following the location given by the cursor.
    // If the cursor is null, insert at the front of the list. Returns
    // the original cursor, which now denotes the inserted item.
    public Cursor insert(Cursor loc, T dat) {
        // Update the list.
        linkNodeAfter(loc.location, new Node<T>(dat));
        ++size;

        // Advance the cursor.
        loc.fwd();
        return loc;
    }

    // Remove the node (if any) denoted by loc. If loc is null, the list is
    // not changed and the iterator is returned. Othewise, the item is
    // removed and the cursor is returned pointing to the removed node's
    // successor, if any, or null.
    public Cursor remove(Cursor loc) {
        // Get the list node, return if none.
        Node<T> zombie = loc.location;
        if (zombie == null)
            return loc;

        // Move the iterator.
        loc.fwd();

        // Update the list.
        unlinkNode(zombie);
        --size;

        return loc;
    }
}
