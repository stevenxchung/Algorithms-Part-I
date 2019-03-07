## Week 4: Priority Queues

We introduce the priority queue data type and an efficient implementation using the *binary heap* data structure. This implementation also leads to an efficient sorting algorithm known as *heapsort*. We conclude with an applications of priority queues where we simulate the motion of *N* particles subject to the laws of elastic collision.

### APIs and elementary implementations
* Let's compare inserts and deletes:
  * Stack - remove the item most recently added
  * Queue - remove the item least recently added
  * Randomized queue - remove a random item
  * Priority queue - remove the largest or smallest item

* A requirement for priority queues - generic types are Comparable
* Let's look at a common scenario where a priority queue would be implemented:
  * Find the largest *M* items in a stream of *N* items (e.g. fraud detection, file maintenance)
  * Constraint: not enough memory to store *N* items

* Here's how this would be implemented in Java:
```java
// Use a min-oriented pq
// Transaction data type is Comparable (ordered by $)
MinPQ<Transaction> pq = new MinPQ<Transaction>();

while (Std.hasNextLine()) {
  String line = StdIn.readLine();
  Transaction item = new Transaction(line);
  pq.insert(item);
  // pq contains largest *M* items
  if (pq.size() > M) {
    pq.delMin();
  }
}
```

* For unordered arrays, priority queue would look something like this in Java:
```java
public class UnorderedMaxPQ<Key extends Comparable<key>> {
  private Key[] pq; // pq[i] = ith element on pq
  private int N; // Number of elements on pq

  public UnorderedMaxPQ(int capacity) {
    pq = (Key[]) new Comparable[capacity]; // No generic array creation
  }

  public boolean isEmpty() {
    return N == 0;
  }

  public void insert(Key x) {
    pq[N++] = x;
  }

  public Key delMax() {
    int max = 0;
    for (int i = 1; i < N; i++) {
      if (less(max, i)) {
        max = i;
      }
      exch(max, n - 1);
      return pq[--N]; // Null out entry to prevent loitering
    }
  }
}
```

### Binary heaps
* A binary heap is based on the binary tree (empty or node with lines to left and right binary trees)
* Here are some characteristics of trees:
  * A complete tree is perfectly balanced except for bottom level
  * Height of complete tree with *N* nodes is *log(n)*
  * Height only increases when *N* is a power of two

* Binary heaps are an array representation of a heap-ordered complete binary tree, here's what we mean by that:
  * Heap-ordered binary tree:
    * Keys in nodes
    * Parent's key no smaller than children's keys
  * Array representation:
    * Indices start at one
    * Take nodes in level order
    * No explicit links needed
* The largest key in a binary heap is a[1] which is the root of the binary tree

* Here's an implementation of binary heap:
```java
public class MaxPQ<Key extends Comparable<Key>> {
  private Key[] pq;
  private int N;

  public MaxPQ(int capacity) {
    pq = (Key[]) new Comparable[capacity + 1];
  }

  public boolean isEmpty() {
    return N == 0;
  }

  public void insert(Key key) {
    pq[++N] = x;
    swim(N);
  }

  public Key delMax() {
    Key max = pq[1];
    exch(1, N--);
    sink(1);
    pq[N + 1] = null;

    return max;
  }

  private void swim(int k) {
    while (k > 1 && less(k / 2, k)) {
      exch(k , k / 2);
      k = k / 2;
    }
  }

  private void sink(int k) {
    while (2 * k <= N) {
      int j = 2 * k;
      if (j < N && less(j, j + 1)) {
        j++;
      }
      if (!less(k, j)) {
        break;
      }

      exch(k, j);
      k = j;
    }
  }

  private boolean less(int i, int j) {
    return pq[i].compareTo(pq[j]) < 0;
  }

  private void exch(int i, int j) {
    Key t = pq[i];
    pq[i] = pq[j];
    pq[j] = t;
  }
}
```
### Heap sort
* Here's the basic idea of heap sort:
  * Create max-heap with all *N* keys
  * Repeatedly remove the maximum key
* We want to build max heap using bottom-up method (start from the bottom of the tree)
* Then we want to repeatedly delete the largest remaining item

* Here is what the implementation looks like:
```java
public class Heap {
  public static void sort(Comparable[] pq) {
    int N = pq.length;
    for (int k = N / 2; k >= 1; k--) {
      sink(pq, k, N);
    }
    while (N > 1) {
      exch(pq, 1, N);
      sink(pq, 1, --N);
    }
  }

  private static void sink(Comparable[] pq, int k, int N) {
    /* as before */
  }

  private static boolean less(Comparable[] pq, int i, int j) {
    /* as before */
  }

  private static void exch(Comparable[] pq, int i, int j) {
    /* as before */
  }
}
```

* Heap construction uses less than or equal to 2 *N* compares and exchanges
* Heap sort uses less than or equal to 2 * *(N * log(n))* compares and exchanges
* Heap sort has an in-place sorting algorithm with *N * log(n)* worse-case
* The bottom line is that heap sort is optimal for both time and space **but**:
  * Inner loop is longer than quicksort
  * Makes poor use of cache memory
  * Not stable