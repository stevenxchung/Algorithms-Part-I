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
