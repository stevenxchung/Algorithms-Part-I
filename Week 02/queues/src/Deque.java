import java.util.Iterator;

public class Deque<Item> implements Iterable<Item> {
    private Node first;
    private Node last;
    private int size;

    private class Node {
        Item item;
        Node prev;
        Node next;
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
    private void nullException(Item item) {
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
        nullException(item);
        Node oldNode = first;
        first = new Node();
        first.item = item;
        first.next = oldNode;

        // If deque is empty set last to first
        if (last == null) {
            last = first;
        } else {
            first.next.prev = first;
        }

        // Increase the size after the operation
        size++;
    }

    // Add the item to the end
    public void addLast(Item item) {
        nullException(item);
        Node oldNode = last;
        last = new Node();
        last.item = item;
        last.prev = oldNode;

        // If deque is empty set first to last
        if (first == null) {
            first = last;
        } else {
            last.prev.next = last;
        }

        // Increase the size after the operation
        size++;
    }

    // Remove and return the item from the front
    public Item removeFirst() {
        emptyException();
        Item item = first.item;
        // Decrease the size after operation
        size--;

        // If deque is still empty after assignment set both to null
        if (isEmpty()) {
            first = null;
            last = null;
        } else {
            first = first.next;
            first.prev = null;
        }

        return item;
    }

    // Remove and return the item from the end
    public Item removeLast() {
        emptyException();
        Item item = last.item;
        // Decrease the size after operation
        size--;

        // If deque is still empty after assignment set both to null
        if (isEmpty()) {
            last = null;
            first = null;
        } else {
            last = last.prev;
            last.next = null;
        }

        return item;
    }

    // Return an iterator over items in order from front to end
    public Iterator<Item> iterator() {
        return new customIterator();
    }
}
