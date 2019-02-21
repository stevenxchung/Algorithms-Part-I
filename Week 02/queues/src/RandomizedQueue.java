import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private Item[] itemArr;
    private int size;

    // Throw exception if deque is empty
    private void emptyException() {
        if (isEmpty()) {
            throw new java.util.NoSuchElementException();
        }
    }

    // Resizes array when called
    private void resizeArr(int maxSize) {
        Item[] temp = (Item[]) new Object[maxSize];
        for (int i = 0; i < size; i++) {
            temp[i] = itemArr[i];
        }
        itemArr = temp;
    }

    // Similar to deque, need to have custom iterator to perform repetitive queue tasks
    private class customIterator implements Iterator<Item> {
        private int i = size;
        private int[] element;

        public customIterator() {
            element = new int[i];
            for (int j = 0; j < i; j++) {
                element[j] = j;
            }
            // Shuffle element after creating list
            StdRandom.shuffle(element);
        }

        // Array only has another element if iterator is greater than zero
        public boolean hasNext() {
            return i > 0;
        }

        // Return the next element if there is another
        public Item next() {
            if (!hasNext()) {
                throw new java.util.NoSuchElementException();
            }

            return itemArr[element[--i]];
        }

        // Throws exception if remove() is executed in iterator
        public void remove() {
            throw new java.lang.UnsupportedOperationException();
        }
    }

    // Construct an empty randomized queue
    public RandomizedQueue() {
        // Cast Item[]
        itemArr = (Item[]) new Object[1];
        size = 0;
    }

    // Check if randomized queue is empty
    public boolean isEmpty() {
        return size == 0;
    }

    // Return number of items on the randomized queue
    public int size() {
        return size;
    }

    // Add the item
    public void enqueue(Item item) {
        if (item == null) {
            throw new java.lang.IllegalArgumentException();
        }
        // Resize array if array is at max
        if (size == itemArr.length) {
            resizeArr(2 * itemArr.length);
        }

        itemArr[size++] = item;
    }

    // Remove and return a random item
    public Item dequeue() {
        emptyException();
        // Resize array if array is at 1/4 the size
        if (size == (itemArr.length / 4) && size > 0) {
            resizeArr(itemArr.length / 2);
        }

        int rand = StdRandom.uniform(size);
        // Initialize item as an element at some random index
        Item item = itemArr[rand];
        itemArr[rand] = itemArr[size - 1];
        itemArr[--size] = null;

        return item;
    }

    // Return a random item (but do not remove it)
    public Item sample() {
        emptyException();
        int rand = StdRandom.uniform(size);

        return itemArr[rand];
    }

    // Return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new customIterator();
    }
}
