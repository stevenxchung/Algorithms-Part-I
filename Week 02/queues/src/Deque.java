import java.util.Iterator;

public class Deque<Item> implements Iterable<Item> {
    private Node first;
    private Node last;
    private int size;

    private class Node {
        Item item;
        Node next;
        Node last;
    }

    // Need to have custom iterator to perform repetitive queue tasks
    private class customIterator implements Iterator<Item> {
        private Node current = first;

        // Returns true only if the current node is not null
        public boolean hasNext() {
            return current != null;
        }

        public Item next() {
            // Throw exception if current does not have a neighbor
            if (!hasNext()) {
                throw new java.util.NoSuchElementException();
            }
            Item item = current.item;
            current = current.next;
            
            return item;
        }

        // Throws exception if remove() is executed in iterator
        public void remove() {
            throw new java.lang.UnsupportedOperationException();
        }
    }

    // Throw exception if item is null
    private void nullException() {
        if (item == null) {
            throw new java.lang.IllegalArgumentException();
        }
    }

    // Throw exception if deque is empty
    private void emptyException() {
        if (isEmpty()) {
            throw new java.util.NoSuchElementException();
        }
    }

    // Construct an empty deque
    public Deque() {
        first = null;
        last = null;
        size = 0;
    }

    // Check if deque is empty
    public boolean isEmpty() {
        return size == 0;
    }

    // Return number of items on the deque
    public int size() {
        return size;
    }

    // Add the item to the front
    public void addFirst(Item item) {
        nullException();
        Node oldNode = first;
        first = new Node();
        first.item = item;
        first.next = oldNode;
        first.last = null;

        // If deque is empty set last to first
        if (isEmpty()) {
            last = first;
        } else {
            oldNode.last = first;
        }

        // Increase the size after the operation
        size += 1;
    }

    // Add the item to the end
    public void addLast(Item item) {
        nullException();
        Node oldNode = last;
        last = new Node();
        last.item = item;
        last.next = null;
        last.last = oldNode;

        // If deque is empty set first to last
        if (isEmpty()) {
            first = last;
        } else {
            oldNode.next = last;
        }

        // Increase the size after the operation
        size += 1;
    }

    // Remove and return the item from the front
    public Item removeFirst() {
        emptyException();
        Item item = first.item;
        first = first.next;

        // If deque is still empty after assignment, set last to first
        if (isEmpty()) {
            last = first;
        } else {
            first.last = null;
        }

        // Decrease the size after operation
        size -= 1;

        return item;
    }

    // Remove and return the item from the end
    public Item removeLast() {
        emptyException();
        Item item = last.item;
        last = last.last;

        // If deque is still empty after assignment, set first to last
        if (isEmpty()) {
            first = last;
        } else {
            last.last = null;
        }

        // Decrease the size after operation
        size -= 1;

        return item;
    }

    // Return an iterator over items in order from front to end
    public Iterator<Item> iterator() {
        return new customIterator();
    }
}
