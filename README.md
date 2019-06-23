# Algorithms, Part I - Princeton University

This course covers programming and problem solving with applications as well as data structures and algorithms:

| topic      | data structures and algorithms                     | part    |
| ---------- | -------------------------------------------------- | ------- |
| data types | stack, queue, bag, union-find, priority queue      | part I  |
| sorting    | quicksort, mergesort, heapsort                     | part I  |
| searching  | BST, red-black BST, hash table                     | part I  |
| graphs     | BFS, DFS, Prim, Kruskal, Dijkstra                  | part II |
| strings    | radix sorts, tries, KMP, regexps, data compression | part II |
| advanced   | B-tree, suffix array, maxflow                      | part II |

- Week 1:
  - Union-find
  - Analysis of Algorithms
- Week 2:
  - Stacks and Queues
  - Elementary Sorts
- Week 3:
  - Mergesort
  - Quicksort
- Week 4:
  - Priority Queues
  - Elementary Symbol Tables
- Week 5:
  - Balanced Search Trees
  - Geometric Applications of BSTs
- Week 6:
  - Hash Tables
  - Symbol Table Applications

## Week 1: Union-Find

> We illustrate our basic approach to developing and analyzing algorithms by considering the dynamic connectivity problem. We introduce the union–find data type and consider several implementations (quick find, quick union, weighted quick union, and weighted quick union with path compression). Finally, we apply the union–find data type to the percolation problem from physical chemistry.

### Steps to developing a usable algorithm

- Model the problem
- Find an algorithm to solve it
- Fast enough? Fits in memory?
- If not, figure out why
- Find a way to address the problem
- Iterate until satisfied

### Dynamic Connectivity

- Given a set of _N_ objects
  - Union command: connect two objects
  - Find/connected query: is there a path connecting the two objects?
- When programming, it is convenient to name to name objects 0 to _N - 1_
  - Use integers as array index
  - Suppress details not relevant to union-find
- Find query will check if two objects are in the same component
- Replace components containing two objects with their union

- The skeleton java program might look something like this:

```java
public class UF {
  // Initialize union-find data structure with N objects (0 to N-1)
  UF(int N)
  // Add connection between p and q
  void union(int p, int, q) {}
  // Are p and q in the same component?
  boolean connected(int p, int, q) {}
}
```

- We can check the API design before getting too far:

```java
public static void main(String[] args) {
  int N = StdIn.readInt();
  UF uf = new UF(N);
  while (!StdIn.isEmpty()) {
    int p = StdIn.readInt();
    int q = StdIn.readInt();
    if (!uf.connected(p, q)) {
      uf.union(p, q);
      StdOut.println(p + ' ' + q);
    }
  }
}
```

### Quick-find

- Also called an eager algorithm, has a data structure consisting of:
  - Integer array _id[]_ of size _N_
  - _p_ and _q_ are connected if and only if they have the same id
- Find operation becomes: check if _p_ and _q_ have the same id
- Union operation is a bit trickier: to merge components containing _p_ and _q_, change all entries whose id equals _id[p]_ to _id[q]_

- The java implementation for quick find might look something like this:

```java
public class QuickFindUF {
  private int[] id;

  public QuickFindUF(int N) {
    id = new int[N];
    for (int i = 0; i < N; i++) {
      id[i] = i;
    }
  }

  public boolean connected(int p, int q) {
    return id[p] == id[q];
  }

  public void union(int p, int q) {
    int pid = id[p];
    int qid = id[q];
    for (int i = 0; i < id.length; i++) {
      if (id[i] == pid) id[i] = qid
    }
  }
}
```

- However, quick-find is too slow, it would take _N^2_ array accesses to process sequence of _N_ union commands on _N_ objects

### Quick-union

- Also known as lazy approach, has data structure consisting of:
  - Integer array _id[]_ of size _N_
  - _id[i]_ is parent of _i_
  - Root of _i_ is _id[id[id[...id[i]...]]]_
- Find: check if _p_ and _q_ have the same root
- Union: Merge components containing _p_ and _q_, set the id of _p_'s root to the id of _q_'s root

- Implementation in java:

```java
public class QuickUnionUF {
  private int[] id;

  public QuickUnionUF(int N) {
    id = new int[N];
    for (int i = 0; i < N; i++) {
      id[i] = i;
    }
  }

  private int root(int i) {
    while (i != id[i]) {
      i = id[i];
    }
    return i;
  }

  public boolean connected(int p, int q) {
    return root(p) == root(q);
  }

  public void union(int p, int q) {
    int i = root(p);
    int j = root(q);
    id[i] = j;
  }
}
```

- Unfortunately, quick-union is also too slow:
  - Trees can get tall
  - Find too expensive (could be _N_ array accesses)

### Quick-union Improvements

- _Weighted_ quick-union improves quick-union by:
  - Modifying quick-union to avoid tall trees
  - Keeping track of size of each tree (number of objects)
  - Balance by linking root of smaller tree to root of larger tree
- Data structure for weighted quick-union is the same as quick-union but we maintain extra array _sz[i]_ to count number of objects in the tree rooted at _i_

- Find is identical to quick-union:

```java
return root(p) == root(q);
```

- Union will link root of smaller tree to root of larger tree and update the _sz[]_ array:

```java
int i = root(p);
int j = root(q);
if (i == j) {
  return;
}
if (sz[i] < sz[j]) {
  id[i] = j;
  sz[j] += sz[i];
}
else  {
  id[j] = i;
  sz[i] += sz[j];
}
```

- This weighted improvement allows the following:

  - Find takes time proportional to the depth of _p_ and _q_
  - Union takes constant time, given roots

- Another improvement is to implement _path compression_:

  - Just after computing the root of _p_, set the id of each examined node to point to that root

- The java implementation of path compression is very simple, just add one line of code:

```java
private int root (int i) {
  while (i != id[i]) {
    id[i] = id[id[i]]; // extra line of code
    i = id[i];
  }
  return i;
}
```

- Friedman and Sachs proved that there is no linear-time algorithm for the union-find problem but weighted quick-union with path compression gets us pretty close!

### Percolation

- _N-by-N_ grid of sites
- Each site is open with probability _p_ (or blocked with probability _1 - p_)
- System percolates if and only if top and bottom are connected by open sites

## Week 1: Analysis of Algorithms

> The basis of our approach for analyzing the performance of algorithms is the scientific method. We begin by performing computational experiments to measure the running times of our programs. We use these measurements to develop hypotheses about performance. Next, we create mathematical models to explain their behavior. Finally, we consider analyzing the memory usage of our Java programs.

### Reasons to analyze algorithms

- Predict performance
- Compare algorithms
- Provide guarantees
- Understand theoretical basis
- Avoid performance bugs

### Scientific method applied to analysis of algorithms

- A framework for predicting performance and comparing algorithms

  - Observe some feature of the natural world (run-time of a program on a computer)
  - Hypothesize a mode that is consistent with the observations
  - Predict events using the hypothesis (run-time of a larger problem-size)
  - Verify the predictions by making further observations
  - Validate by repeating until the hypothesis and observations agree

- Some principles of the scientific method include:
  - Experiments must be reproducible
  - Hypotheses must be falsifiable

### Experimental Algorithmics

- Observing what is happening to the run-time of our programs as we vary input size gives us a way to predict performance
- System independent effects:
  - Algorithm
  - Input data
- System dependent effects:
  - Hardware: CPU, memory, cache, ...
  - Software: compiler, interpreter, garbage collector, ...
  - System: operating system, network, other apps, ...
- Bad news:
  - It is difficult to get precise measurements
- Good news:
  - Much easier and cheaper than other sciences

### Mathematical Models

- Although we could observe the run-time of our programs, it does not help us understand what our algorithms are doing
- By looking at some basic operation as a proxy for running time we can come up with a general run-time of our operation
- Once we have an estimate we can ignore lower order terms (tilde notation)
- We can estimate a discrete sum through discrete mathematics or calculus

### Order-of-growth Classifications

- There are only a small set of functions we care about regarding performance:
  - _log(N)_ - Fast
  - _N_ - OK
  - _N _ log(N)\* - OK
  - _N^2_ - Slow
  - _N^3_ - Slower
  - _2^N_ - Slowest
- We try the best we can to make sure an algorithm is not quadratic or cubic

### Binary Search: Java implementation

- Below is a Java implementation of binary search which takes logarithmic time to complete:

```java
public static int binarySearch(int[] a, int key) {
  int lo = 0, hi = a.length - 1;
  while (lo <= hi) {
    int mid = lo + (hi - lo) / 2;
    if (key < a[mid]) {
      hi = mid - 1;
    } else if (key > a[mid]) {
      lo = mid + 1;
    } else {
      return mid;
    }
    return -1;
  }
}
```

### Types of Analysis

- There are different ways of analyzing the algorithm depending on the input, this breaks down algorithm run-time into best case, average case, and worst case:
  - Best case is determined by the "easiest" input and provides a goal for all inputs
  - Worse case is determined by the "hardest" input and provides a guarantee for all inputs
  - Best and worst case can be mapped to the lower and upper bound of run-time respectively
  - Average case is the expected cost for a random input and provides a way to predict run-time
  - _Important to note that Big-O notation provides only an upper bound on the growth rate of a function as n gets large_

### Algorithm design approach

- Develop an algorithm
- Prove a lower bound
- Lower the upper bound (discover a new algorithm)
- Raise the lower bound (more difficult)

### Memory

- Although run-time costs of operations are important, memory cost is also important
- Let's look at an example of memory cost in Java, how much memory does this union-find function use as a function of _N_? _About 8N bytes_

```java
public class WeightedQuickUnionUF { // 16 bytes (object overhead)
  private int[] id; // 8 + (4N + 24) each reference + int[] array
  private int[] sz;
  private int count; // 4 bytes (int)
  // 4 bytes (padding)
  public WeightedQuickUnionUF(int N) {
    id = new int[N];
    sz = new int[N];
    for (int i = 0; i < N; i++) {
      id[i] = i;
    }
    for (int i = 0; i < N; i++) {
      sz[i] = i;
    }
  }
}
```

## Week 2: Stacks and Queues

> We consider two fundamental data types for storing collections of objects: the stack and the queue. We implement each using either a singly-linked list or a resizing array. We introduce two advanced Java features—generics and iterators—that simplify client code. Finally, we consider various applications of stacks and queues ranging from parsing arithmetic expressions to simulating queueing systems.

### Stacks and Queues

- There are some fundamental data types:
  - Value: collection of objects
  - Operations: _insert, remove, iterate_, test if empty
- Stack - LIFO (last-in-first-out)
- Queue - FIFO (first-in-first-out)

### Stacks

- An example of a stack test client implemented in Java:

```java
public static void main(String[] args) {
  StackOfStrings stack = new StackOfStrings();
  while (!StdIn.isEmpty()) {
    String s = StdIn.readString();
    if (s.equals("-")) {
      StdOut.print(stack.pop());
    }
    else {
      stack.push(s);
    }
  }
}
```

- What if we wanted to implement a stack with a linked-list?

```java
public class LinkedStackOfStrings {
  private Node first = null;
  // Private inner class
  private class Node {
    String item;
    Node next;
  }

  public boolean isEmpty() {
    return first == null;
  }

  public void push(String item) {
    Node oldfirst = first;
    first = new Node();
    first.item = item;
    first.next = oldfirst;
  }

  public String pop() {
    String item = first.item;
    first = first.next;
    return item;
  }
}
```

- Every operation takes constant time in the worst case
- A stack with _N_ items uses ~40 _N_ bytes

- What if we wanted to implement a stack with an array?

```java
public class FixedCapacityStackOfStrings {
  private String[] s;
  private int N = 0;
  // Cheating a bit here since we are requiring the client to input a capacity
  public FixedCapacityStackOfStrings(int capacity) {
    s = new String[capacity];
  }

  public boolean isEmpty() {
    return N == 0;
  }

  public void push(String item) {
    s[N++] = item; // Use N++ to index into array then increment N
  }

  public String pop() {
    return s[--N]; // Decrement N, then use to index into array
  }
}
```

- Stack considerations include:
  - Underflow: throw an exception if pop from an empty stack
  - Overflow: use resizing array for array implementation
- Null items can be inserted into a stack
- Loitering: holding reference to an object when it is no longer needed

```java
// Here we are still holding references
public String pop() {
  return s[--N];
}

// To address loitering
public String pop() {
  String item = s[--N];
  s[N] = null
  return item;
}
```

### Resizing Arrays

- Problem: looking back at our stack array implementation, we require the client to provide capacity when we need to know how to grow and shrink the array
- If array is full, create a new array of twice the size:

```java
public ResizingArrayStackOfStrings() {
  s = new String[1];
}

public void push(String item) {
  if (N == s.length) {
    resize(2 * s.length);
  }
  s[N++] = item;
}

private void resize(int capacity) {
  String[] copy = new String[capacity];
  for (int i = 0; i < N; i++) {
    copy[i] = s[i];
  }
  s = copy;
}
```

- To shrink the array we would implement the logic:
  - `push()`: double size of array _s[]_ when array is full
  - `pop()`: halve the size of array _s[]_ when array is one-half full
- This sequence is too expensive in the worst case however since each operation takes _N_ time, instead it is more efficient to implement the following:
  - `push()`: double size of array _s[]_ when array is full
  - `pop()`: halve the size of array _s[]_ when array is _one-quarter_ full

```java
public String pop() {
  String item = s[--N];
  s[N] = null;
  if (N > 0 && N == s.length/4) {
    resize(s.length/2);
  }
  return item;
}
```

- So when would we implement a resizing array vs a linked-list?
  - Linked-list implementation:
    - Every operation takes constant time in the worst case
    - Uses extra time and space to deal with the links
  - Resizing-array implementation:
    - Every operation takes constant amortized time (constant time overall)
    - Less wasted space

### Queues

- What if we wanted to implement a queue with a linked-list?

```java
public class LinkedQueueOfStrings {
  private Node first = null;
  private Node last = null;

  private class Node {
    String item;
    Node next;
  }

  public boolean isEmpty() {
    return first == null;
  }

  public void enqueue(String item) {
    Node oldlast = last;
    last = new Node();
    last.item = item;
    last.next = null;
    // Check if empty
    if (isEmpty()) {
      first = last;
    } else {
      oldlast.next = last;
    }
  }

  public String dequeue() {
    String item = first.item;
    first = first.next;
    // Check if empty
    if (isEmpty()) {
      last = null;
    }
    return item;
  }
}
```

### Generics

- So far our implementations only covers strings not other data types, we want to implement a separate stack class for each type
- One solution is to implement casting, however casting is error-prone since run-time error could occur if types mismatch
- A better approach is to use generics:

  - Avoids casting in the client
  - Discovers type mismatch errors at compile-time instead of run-time

- A linked-list implementation for stacks using generics:

```java
// Here we can use our stack implementation for the generic type name Item
public class Stack<Item> {
  private Node first = null;

  private class Node {
    Item item;
    Node next;
  }

  public boolean isEmpty() {
    return first == null;
  }

  public void push(Item item) {
    Node oldfirst = first;
    first = new Node();
    first.item = item;
    first.next = oldfirst;
  }

  public Item pop() {
    Item item = first.item;
    first = first.next;
    return item;
  }
}
```

- However Java does not allow for array implementation using generics:

```java
// This is not allowed in Java
public class FixedCapacityStack<Item> {
  private Item[] s;
  private int N = 0;

  public FixedCapacityStack(int capacity) {
    s = new Item[capacity];
  }

  public boolean isEmpty() {
    return N == 0;
  }

  public void push(Item item) {
    s[N++] = item;
  }

  public Item pop() {
    return s[--N];
  }
}
```

- Therefore, for arrays we are stuck with casting:

```java
// This is allowed in Java
public class FixedCapacityStack<Item> {
  private Item[] s;
  private int N = 0;

  public FixedCapacityStack(int capacity) {
    s = (Item[]) new Object[capacity]; // Cast using (Item[])
  }

  public boolean isEmpty() {
    return N == 0;
  }

  public void push(Item item) {
    s[N++] = item;
  }

  public Item pop() {
    return s[--N];
  }
}
```

- What about primitive types?
  - Each primitive type has a wrapper object type
  - There is a feature known as autoboxing which acts as an automatic cast between a primitive type and its wrapper:

```java
// This is what happens behind the scenes
Stack<Integer> s = new Stack<Integer>();
s.push(17); // s.push(new Integer(17));
int a = s.pop(); // int a = s.pop().intValue();
```

### Iterators

- What is an Iterable?
  - Has a method that returns an Iterator

```java
public interface Iterable<Item> {
  Iterator<Item> iterator();
}
```

- What is an Iterator?
  - Has methods hasNext() and next()

```java
public interface Iterable<Item> {
  boolean hasNext();
  Item next();
}
```

- Why make data structures Iterable?
  - Java supports elegant client code

```java
for (String s: stack) {
  StdOut.println(s);
}

// longhand foreach statement
Iterator<String> i = stack.iterator();
while (i.hasNext()) {
  String s = i.next();
  StdOut.println(s);
}
```

- Below is an implementation of a stack iterator for linked-lists:

```java
public class Stack<Item> implements Iterable<Item> {

  // ...

  public Iterator<Item> iterator() {
    return new ListIterator();
  }

  private class ListIterator implements Iterator<Item> {
    private Node current = first;

    public boolean hasNext() {
      return current != null;
    }

    // Not supported
    public void remove() {}

    public Item next() {
      Item item = current.item;
      current = current.next;
      return item
    }
  }
}
```

- For arrays it is similar:

```java
public class Stack<Item> implements Iterable<Item> {

  // ...

  public Iterator<Item> iterator() {
    return new ReverseArrayIterator();
  }

  private class ReverseArrayIterator implements Iterator<Item> {
    private int i = N;

    public boolean hasNext() {
      return i > 0;
    }

    // Not supported
    public void remove() {}

    public Item next() {
      return s[--i];
    }
  }
}
```

## Week 2: Elementary Sorts

> We introduce the sorting problem and Java's Comparable interface. We study two elementary sorting methods (selection sort and insertion sort) and a variation of one of them (shellsort). We also consider two algorithms for uniformly shuffling an array. We conclude with an application of sorting to computing the convex hull via the Graham scan algorithm.

### Selection Sort

- Start out with an unsorted array and in the ith iteration we go through the array to try to find the smallest remaining entry
- Scans from left to right and tries to find the smallest to swap

- Below is an implementation of selection sort in Java:

```java
public class Selection {
  public class static void sort(Comparable[] a) {
    int N = a.length;
    for (int i = 0; i < N; i++) {
      int min = i;
      for (int j = i + 1; j < N; j++) {
        if (less(a[j], a[min])) {
          min = j;
        }
        exch(a, i, min);
      }
    }
  }

  private static boolean less(Comparable v, Comparable w) {
    // ...
  }

  private static void exch(Comparable[] a, int i, int j) {
    // ...
  }
}
```

- Selection sort has a quadratic run-time even if input is sorted
- Linear number of exchanges

### Insertion Sort

- Move an index _i_ from left to right as before but now move the element into position among the other elements to the left (can implement this least-to-greatest or greatest-to-least)

- The implementation for insertion sort in Java is as follows:

```java
public class Insertion {
  public static void sort(Comparable[] a) {
    int N = a.length;
    for (int i = 0; i < N; i++) {
      for (int j = i; j > 0; j--) {
        if (less(a[j], a[j - 1])) {
          exch(a, j, j - 1);
        }
        else {
          break;
        }
      }
    }
  }

  private static boolean less(Comparable v, Comparable w) {
    // ...
  }

  private static void exch(Comparable[] a, int i, int j) {
    // ...
  }
}
```

- Insertion sort does depend on the initial order of the data
- In most cases, the data is partially sorted and the run-time is linear for this case

### Shellsort

- One of the oldest sorting methods invented by Shell in 1959
- The idea of shellsort is to move entries more than one position at a time by _h-sorting_ the array
- Very similar to insertion sort except when going back left to check order we go back by _h_ and not index _i_
- A _g-sorted_ array remains _g-sorted_ after _h-sorting_ it

- The implementation for shellsort in Java is as follows:

```java
public class Shell {
  public static void sort(Comparable[] a) {
    int N = a.length;

    int h = 1;
    while (h < N/3) {
      h = 3 * h + 1
    }

    while (h >= 1) {
      for (int i = h; i < N; i++) {
        for (int j = i; j >= h && less(a[j], a[j - h]); j -= h) {
          exch(a, j, j - h)
        }
      }

      h = h/3
    }
  }

  private static boolean less(Comparable v, Comparable w) {
    // ...
  }

  private static void exch(Comparable[] a, int i, int j) {
    // ...
  }
}
```

- In general, the worst-case number of compares used by shellsort with the _3x + 1_ increments is about _O(n^3/2)_, however in practice the run-time is much less but there is no known model of that
- Why use shellsort?
  - Fast unless array size is huge
  - Tiny, fixed footprint for code (used in embedded systems)
  - Hardware sort prototype

### Shuffling

- How would we shuffle a deck of cards in linear-time such that the result is a uniformly random permutation?

  - Use Knuth's shuffling algorithm which picks a random integer _r_ between 0 and _i_ and applies that to shuffle the deck on each iteration

- The implementation of Knuth's shuffling algorithm in Java:

```java
public class StdRandom {
  // ...
  public static void shuffle(Object[] a) {
    int N = a.length;
    for (int i = 0; i < N; i++) {
      int r = StdRandom.uniform(i + 1);
      exch(a, i , r);
    }
  }
}
```

### Convex Hull

- The convex hull is a geometric object which is the smallest polygon that encloses all points
- How to write a program which gives us a convex hull?
  - Consider the geometric properties of the convex hull:
    - Can transverse convex hull only by making counterclockwise turns
    - The vertices of convex hull appear in increasing order of polar angle
- From what we know about the geometric properties of the convex hull we can apply Graham scan:

  - Choose point _p_ with smallest y-coordinate
  - Sort points by polar angle with _p_
  - Consider points in order, discard unless it can create a CCW turn

- Below is a sample implementation of Graham scan in Java:

```java
Stack<Point2D> hull = new Stack<Point>();

Arrays.sort(p, Point2D.Y_ORDER); // p[0] is now point with lowest y-coordinate
Arrays.sort(p, p[0].BY_POLAR_ORDER); // Sort by polar angle with respect to p[0]

hull.push(p[0]);
hull.push(p[1]);

for (int i = 2; i < N; i++) {
  Point2D top = hull.pop();
  // Discard points that would create CW turns
  while (Point2D.ccw(hull.peek(), top, p[i]]) <= 0) {
    top = hull.pop()
  }
  hull.push(top);
  hull.push(p[i]); // Add p[i] to putative hull
}
```

## Week 3: Mergesort

> We study the mergesort algorithm and show that it guarantees to sort any array of _N _ N* items with at most *N _ log(N)_ compares. We also consider a non-recursive, bottom-up version. We prove that any compare-based sorting algorithm must make at least ∼*N * log(N)\* compares in the worst case. We discuss using different orderings for the objects that we are sorting and the related concept of stability.

### Mergesort

- Mergesort could be broken down into parts:

  - Divide array into halves
  - Recursively sort each half
  - Merge two halves

- The Java implementation of mergesort could be implemented as follows:

```java
private static void merge(Comparable[] a, Comparable[] aux, int lo, int mid, int hi) {
  assert isSorted(a, lo, mid); // Precondition: a[lo... mid] sorted
  assert isSorted(a, mid + 1, hi); // Precondition: a[mid + 1... hi] sorted

  for (int k = lo; k <= hi; k++) {
    aux[k] = a[k];
  }

  int i = lo, j = mid + 1;
  for ( int k = lo; k <= hi; k++) {
    if (i > mid) {
      a[k] = aux[j++];
    } else if (j > hi) {
      a[k] = aux[i++];
    } else if (less(aux[j], aux[i])) {
      a[k] = aux[j++];
    } else {
      a[k] = aux[i++];
    }
  }

  assert isSorted(a, lo, hi); // Post-condition: a[lo... hi] sorted
}
```

- To add to the above implementation:

```java
public class Merge {
  private static void merge(...) {
    /* as before */
  }

  private static void sort(Comparable[] a, Comparable[] aux, int lo, int mid, int hi) {
    if (hi <= lo) {
      return;
    }

    int mid = lo + (hi - lo) / 2;
    sort(a, aux, lo, mid);
    sort(a, aux, mid + 1, hi);
    merge(a, aux, low, mid, hi);
  }

  public static void sort(Comparable[] a) {
    aux = new Comparable[a.length];
    sort(a, aux, 0, a.length - 1);
  }
}
```

- The run-time estimate of mergesort is 10^8 compares/second
- A supercomputer executes 10^12 compares/second
- The lesson learned is that a good algorithm is better than investing in a supercomputer
- Mergesort uses at most _N _ log(N)* compares and about *N _ log(N)_ array accesses to sort any array of size _N_

- One improvement we could make is using insertion sort as part of our sort procedure:

```java
private static void sort(Comparable[] a, Comparable[] aux, int lo, int mid, int hi) {
  if (hi <= lo + CUTOFF - 1) {
    Insertion.sort(a, lo, hi);
    return;
  }

  int mid = lo + (hi - lo) / 2;
  sort(a, aux, lo, mid);
  sort(a, aux, mid + 1, hi);
  // Another improvement is to stop if sorted
  if (!less(a[mid + 1], a[mid])) {
    return;
  }
  merge(a, aux, low, mid, hi);
}
```

### Bottom-up Mergesort

- Although mergesort is easy to understand as recursive program, bottom-up version has no recursion:

  - Pass through array, merging sub-arrays of size 1
  - Repeat for sub-arrays of size 2, 4, 8, 16, ...

- A Java implementation of bottom-up mergesort:

```java
public class MergeBU {
  private static Comparable[] aux;

  private static void merge(Comparable[] a, int lo, int mid, int hi) {
    /* as before */
  }

  private static void sort(Comparable[] a) {
    int N = a.length;
    aux = new Comparable[N];
    for (int sz = 1; sz < N; sz = sz + sz) {
      for (int lo = 0; lo < N - sz; lo += sz + sz) {
        merge(a, lo, lo + sz - 1, Math.min(lo + sz + sz - 1, N - 1));
      }
    }
  }
}
```

### Sort Complexity

- Computational complexity is a framework to study efficiency of algorithms for solving a particular problem X
- There are several thing we care about when studying complexity:

  - Model of computation - allowable operations
  - Cost model - operation counts
  - Upper bound - cost guarantee provided by some algorithm for X
  - Lower bound - proven limit on cost guarantee of all algorithms for X
  - Optimal algorithm - algorithm with best possible cost guarantee for X

- Here is an example with sorting:

  - Model of computation: decision tree
  - Cost model: # compares
  - Upper bound: ~_N _ log(N)\* (mergesort)
  - Lower bound: ~_N _ log(N)\*
  - Optimal algorithm: mergesort

- Mergesort is optimal with respect to # compares but not optimal with respect to space usage

### Comparators

- We previously saw how Comparables allow us to implement sorts with any type of data, with Comparators, we can help sort using some alternate order or many different orders on the same data

- Here is an insertion sort implementation using Comparators:

```java
private static void sort(Object[] a, Comparator comparator) {
  int N = a.length;
  for (int i = 0; i < N; i++) {
    for (int j = i; j > 0 && less(comparator, a[j], a[j - 1]); j--) {
      exch(a, j, j - 1);
    }
  }
}

private static boolean less(Comparator c, Object v, Object w) {
  return c.compare(v, w) < 0;
}

private static void exch(Object[] a; int i; int j) {
  Object swap = a[i];
  a[i] = a[j];
  a[j] = swap;
}
```

- In general, to implement a comparator:
  - Define a (nested) class that implements the Comparator interface
  - Implement the compare() method

### Stability

- Stability refers to how well an algorithm is able to preserve the relative order of items with equal keys
- Which one of our previously covered sorts are stable?
  - Insertion and merge sort are stable but not selection or shellsort
- Why are insertion sort and merge sort stable?
  - Equal items never move past each other
- Why are selection sort and shellsort not stable?
  - There may be long-distance exchanges which might move an item past some equal item

## Week 3: Quicksort

> We introduce and implement the randomized quicksort algorithm and analyze its performance. We also consider randomized quick-select, a quicksort variant which finds the _kth_ smallest item in linear time. Finally, consider 3-way quicksort, a variant of quicksort that works especially well in the presence of duplicate keys.

### Quicksort

- Quicksort is a recursive method which works in several steps:

  - Shuffle the array
  - Partition so that, for some _j_:
    - Entry _a[j]_ is in place
    - No larger entry to the left of _j_
    - No smaller entry to the right of _j_
  - Sort each piece recursively

- The code for partitioning is as follows:

```java
private static int partition(Comparable[] a, int lo, int hi) {
  int i = lo, j = hi + 1;
  while (true) {
    // Find item on left to swap
    while (less(a[++i], a[lo]) {
      if (i = hi) {
        break;
      }
    })
    // Find item on right to swap
    while (less(a[lo], a[--j]) {
      if (j = lo) {
        break;
      }
    })
    // Check if pointers cross
    if (i >= j) {
      break;
      exch(a, i, j);
    }
  }
  // Swap with partitioning item
  exch(a, lo, j);
  // Return index of item now known to be in place
  return j;
}
```

- Remember that quicksort itself is a recursive program:

```java
public class Quick {
  private static int partition(Comparable[] a, int lo, int hi) {
    /* as before */
  }

  public static void sort(Comparable[] a) {
    StdRandom.shuffle(a); // Needed for performance guarantee
    sort(a, 0, a.length - 1);
  }

  public static void sort(Comparable[] a, int lo, int hi) {
    if (hi <= lo) {
      return;
    }
    int j = partition(a, lo, hi);
    sort(a, lo, j - 1);
    sort(a, j + 1, hi);
  }
}
```

- Shuffling is needed for performance guarantee, this preserves randomness
- Best case number of compares for quicksort is ~_N _ log(N)\*
- Worst case number of compares for quicksort is ~_(1/2) _ N^2\*
- The average number of compares for quicksort is ~*2*N _ log(N)_
- On average, quicksort does about 40% more compares than mergesort but faster than mergesort due to less data movement
- Quicksort is an _in-place_ sorting algorithm
- Quicksort is _not stable_

- There is a way to make quicksort faster by using insertion sort as a step for small arrays:

```java
public static void sort(Comparable[] a, int lo, int hi) {
  if (hi <= lo + CUTOFF - 1) {
    Insertion.sort(a, lo, hi);
    return;
  }
  int j = partition(a, lo, hi);
  sort(a, lo, j - 1);
  sort(a, j + 1, hi);
}
```

- Another improvement is to take median of sample (not worth for large samples):

```java
public static void sort(Comparable[] a, int lo, int hi) {
  if (hi <= lo) {
    return;
  }

  int m = medianOfThree(a, lo, lo + (hi - lo) / 2, hi);
  swap(a, lo, m);

  int j = partition(a, lo, hi);
  sort(a, lo, j - 1);
  sort(a, j + 1, hi);
}
```

### Selection

- Similar to quicksort we want a method to quickly select a particular item in a set
- The idea for selection is to partition an array so that:
  - Entry* a[j]* is in place
  - No larger entry to the left of _j_
  - No smaller entry to the right of _j_
- Repeat in one sub-array, depending on _j_
- Finished when _j_ equals _k_

- An implementation of quick-select is as follows:

```java
public static Comparable select(Comparable[] a, int k) {
  StdRandom.shuffle(a);
  int lo = 0, hi = a.length - 1;
  while (hi < lo) {
    int j = partition(a, lo, hi);
    if (j < k) {
      lo = j + 1;
    } else if (j > k) {
      hi = j - 1;
    } else {
      return a[k];
    }
    return a[k];
  }
}
```

- Quick-select takes _linear-time_ on average

### Duplicate Keys

- Mergesort with duplicate keys always took between _(1/2) _ N _ log(N)_ and _N _ log(N)\* compares
- Quicksort with duplicate keys goes quadratic unless partitioning stops on equal keys
- We want to partition array into three parts such that:

  - Entries between _a_ and _b_ are equal to partition item _c_
  - No larger entries to left to _a_ (lower bound)
  - No smaller entries to right of _b_ (upper bound)

- Here's how we would implement three-way quicksort:

```java
private static void sort(Comparable[] a, int lo, int hi) {
  if (hi <= lo) {
    return;
  }

  int lt = lo, gt = hi;
  Comparable v = a[lo];
  int i = lo;

  while (i <= gt) {
    int cmp = a[i].compareTo(v);

    if (cmp < 0) {
      exch(a, lt++, i++);
    } else if (cmp > 0) {
      exch(a, i, gt--);
    } else {
      i++;
    }

    sort(a, lo, lt - 1);
    sort(a, gt + 1, hi);
  }
}
```

- Randomized quicksort with three-way partitioning reduces running time from linearithmic to linear in broad class of applications

### System Sorts

- Java has a `sort()` method which:

  - Has a different method for each primitive type
  - Has a method for data types that implement Comparable
  - Has a method that uses a Comparator
  - Uses tuned quicksort for primitive types; tuned mergesort for objects

- A sort function widely used in C, C++ and Java has the following:

  - Cutoff to insertion sort for small sub-arrays
  - Partitioning scheme: three-way partitioning (Dijkstra's)
  - Partitioning item:
    - Small arrays: middle entry
    - Medium arrays: median of three
    - Large arrays: Tukey's ninther

- Tukey's ninther - median of three samples, each three entries:
  - Approximates the median of nine
  - Uses at most twelve compares

## Week 4: Priority Queues

> We introduce the priority queue data type and an efficient implementation using the _binary heap_ data structure. This implementation also leads to an efficient sorting algorithm known as _heapsort_. We conclude with an applications of priority queues where we simulate the motion of _N_ particles subject to the laws of elastic collision.

### APIs and elementary implementations

- Let's compare inserts and deletes:

  - Stack - remove the item most recently added
  - Queue - remove the item least recently added
  - Randomized queue - remove a random item
  - Priority queue - remove the largest or smallest item

- A requirement for priority queues - generic types are Comparable
- Let's look at a common scenario where a priority queue would be implemented:

  - Find the largest _M_ items in a stream of _N_ items (e.g. fraud detection, file maintenance)
  - Constraint: not enough memory to store _N_ items

- Here's how this would be implemented in Java:

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

- For unordered arrays, priority queue would look something like this in Java:

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

### Binary Heaps

- A binary heap is based on the binary tree (empty or node with lines to left and right binary trees)
- Here are some characteristics of trees:

  - A complete tree is perfectly balanced except for bottom level
  - Height of complete tree with _N_ nodes is _log(n)_
  - Height only increases when _N_ is a power of two

- Binary heaps are an array representation of a heap-ordered complete binary tree, here's what we mean by that:
  - Heap-ordered binary tree:
    - Keys in nodes
    - Parent's key no smaller than children's keys
  - Array representation:
    - Indices start at one
    - Take nodes in level order
    - No explicit links needed
- The largest key in a binary heap is a[1] which is the root of the binary tree

- Here's an implementation of binary heap:

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

### Heap Sort

- Here's the basic idea of heap sort:
  - Create max-heap with all _N_ keys
  - Repeatedly remove the maximum key
- We want to build max heap using bottom-up method (start from the bottom of the tree)
- Then we want to repeatedly delete the largest remaining item

- Here is what the implementation looks like:

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

- Heap construction uses less than or equal to 2 _N_ compares and exchanges
- Heap sort uses less than or equal to 2 \* _(N _ log(n))\* compares and exchanges
- Heap sort has an in-place sorting algorithm with _N _ log(n)\* worse-case
- The bottom line is that heap sort is optimal for both time and space **but**:
  - Inner loop is longer than quicksort
  - Makes poor use of cache memory
  - Not stable

## Week 4: Elementary Symbol Tables

> We define an API for _symbol tables_ (also known as _associative arrays_) and describe two elementary implementations using a sorted array (binary search) and an unordered list (sequential search). When the keys are Comparable, we define an extended API that includes the additional methods min, max floor, ceiling, rank, and select. To develop an efficient implementation of this API, we study the _binary search tree_ data structure and analyze its performance.

### Symbol Table API

- Symbol tables are key-value pair abstractions:

  - **Insert** a value with a specified key
  - Given a key, **search** for the corresponding value

- A good way to set up a basic symbol table API is to implement an associative array abstraction (associate one value with each key)

- Some conventions of this implementation are:
  - Values are not _null_
  - Method `get()` returns _null_ if key not present
  - Method `put()` overwrites old value with new value
- For keys and values:
  - Assume keys are `Comparable`, use `compareTo()`
  - Assume keys are any generic type, use `equals()` to test equality
  - Assume keys are any generic type, use `equals()` to test equality and then `hashCode()` to scramble key
- Best practices:
  - Best to use immutable types for symbol table keys
  - Immutable types in Java include: String, Integer, Double, java.io.File, ...
  - Mutable in Java: StringBuilder, java.net.URL, arrays, ...
- Lastly, all java classes inherit a method `equals()`

### Elementary Implementations

- One elementary implementation is sequential search in a linked list:

  - Data structure - maintain an unordered linked list of key-value pairs
  - Search - scan through all keys until match is found
  - Insert - scan through all keys until match is found, and add to front if there is no match

- Another implementation is binary search with an ordered array:

  - Data structure - maintain an ordered array of key-value pairs
  - Rank helper function - how many keys are less than _k_

- Here is what the binary search implementation looks like in Java:

```java
public Value get(Key key) {
  if (isEmpty()) {
    return null;
  }
  int i = rank(key);
  if (i < N && keys[i].compareTo(key) == 0) {
    return vals[i];
  } else {
    return null;
  }
}

private int rank(Key key) {
  int lo = 0, hi = N - 1;
  while (lo <= hi) {
    int mid = lo + (hi - lo) / 2;
    int cmp = key.compareTo(key[mid]);
    if (cmp < 0) {
      hi = mid - 1;
    } else if (cmp > 0) {
      lo = mid + 1;
    } else {
      return mid;
    }
  }
  return lo;
}
```

### Ordered Operations

- When keys are comparable we can put them in order, we saw that we can use binary search to get an efficient symbol table implementation
- Below is a summary of sequential search versus binary search for the following operations:
  - Search
  - Insert/delete
  - Min/max
  - Floor/ceiling
  - Rank
  - Select
  - Ordered iteration

| operations        | sequential search | binary search |
| ----------------- | ----------------- | ------------- |
| search            | _N_               | _log(N)_      |
| insert/delete     | _N_               | _N_           |
| min/max           | _N_               | _1_           |
| floor/ceiling     | _N_               | _log(N)_      |
| rank              | _N_               | _log(N)_      |
| select            | _N_               | _log(N)_      |
| ordered iteration | _N _ log(N)\*     | _N_           |

### Binary Search Tree

- A BST (Binary Search Tree) is a **binary tree** in **symmetric order**
- A binary tree tree is either:
  - Empty
  - Two disjoint binary trees (left and right)
- BSTs have symmetric order - each node has a key and every node's key is:
  - Larger than all keys in its left subtree
  - Smaller than all keys in its right subtree
- BSTs is a reference to a root `Node`
- A Node is comprised of four fields:

  - A `Key` and a `Value`
  - A reference to the left and right subtree

- Here is what the `Node` class looks like in Java:

```java
private class Node {
  private Key key;
  private Value val;
  private Node left, right;
  public Node(Key key, Value val) {
    this.key = key;
    this.val = val;
  }
}
```

- BST search in Java is:

```java
public Value get(Key key) {
  Node x = root;
  while (x != null) {
    int cmp = key.compareTo(x.key);
    if (cmp < 0) {
      x = x.left;
    } else if (cmp > 0) {
      x = x.right;
    } else {
      return x.val;
    }
  }
  return null;
}
```

- BST insert implemented in Java is:

```java
public void put(Key key, Value val) {
  root = put(root, key, val);
}
// This implementation of put() is recursive
private Node put(Node x, Key key, Value val) {
  if (x == null) {
    return new Node(key, val);
  }
  int cmp = key.compareTo(x.key);
  if (cmp < 0) {
    x.left = put(x.left, key, val);
  } else if (cmp > 0) {
    x.right = put(x.right, key, val);
  } else {
    x.val = val;
  }
  return x;
}
```

### Ordered operations in BSTs

- There are many different ordered operations when it comes to BSTs

- How do we find the minimum or maximum in a BST?
  - Minimum - move left from the root
  - Maximum - move right from the root
- How do we determine the floor and ceiling?
  - Floor - largest key less than or equal to a given key
  - Ceiling - smallest key greater than or equal to a given key
- How do we determine rank (How many keys less than _k_)?
  - Implement recursive algorithm (3 cases)
  - Need to implement subtree count as part of the `Node` class for this to work
- How to implement in-order traversal?

  - Traverse left subtree
  - Enqueue key
  - Traverse right subtree

- For all ordered operations, BSTs have a run-time efficiency proportional to the height of the tree except for ordered iteration
- The height of the BST is also proportional to _log(N)_ if keys are inserted in random order

### Deletion in BSTs

- To remove a node with a given key (lazy approach):
  - Set its value to _null_
  - Leave key in tree to guide searches (but don't consider it equal in search)
  - The problem with this lazy approach is that memory will be overload
- How to delete the minimum (also applicable to maximum):
  - Go left until finding a node with a null left link
  - Replace that node by its right link
  - Update subtree counts
- Another way to implement min/max deletion is using Hibbard deletion:
  - To delete a node with key _k_, search for node _t_ containing key _k_
- The main problem with Hibbert deletion is that it unbalances the tree leading to _sqrt(N)_ height

## Week 5: Balanced Search Trees

> In this lecture, our goal is to develop a symbol table with guaranteed logarithmic performance for search and insert (and many other operations). We begin with 2-3 trees, which are easy to analyze but hard to implement. Next, we consider red-black binary search trees, which we view as a novel way to implement 2-3 trees as binary search trees. Finally, we introduce B-trees, a generalization of 2-3 trees that are widely used to implement file systems.

### 2-3 Search Trees

- In a two-three search tree we allow one or two keys per node:
  - Two-node: one key, two children
  - Three-node, two keys, three children
- A two-three tree has perfect balance - every path from root to null has same length
- In-order traversal yields keys in ascending order
- Splitting a four-node is a **local** transformation: constant number of operations
- Each transformation maintains symmetric order and perfect balance
- In terms of tree height:
  - Worst case performance for all two-nodes is _log(N)_
  - Best case performance for all three-nodes is about _0.631 _ log(N)\*
  - A two-three tree is guaranteed logarithmic performance for search and insert

### Red-black BSTs

- In red-black BSTs, we:
  - Represent a two-three tree as a BST
  - Use "internal" left-leaning links as "glue" for three-nodes
- Another way of looking at red-black trees is that they are BSTs where:
  - No node has two red links connected to it
  - Every path from root to null link has the same number of black links
  - Red links lean left
- Search is the same for elementary BSTs as for red-black BSTs (however red-black BSTs run faster due to better balance)
- The basic operations for red-black BST are:

  - Rotate left
  - Rotate right
  - Swap colors

- A red-black tree is represented as the following:

```java
private static final boolean RED = true;
private static final boolean BLACK = false;

private class Node {
  Key key;
  Value val;
  Node left, right;
  Boolean color; // Color of parent link
}

private boolean isRed(Node x) {
  // Null links are black
  if (x == null) {
    return false;
  }
  return x.color == RED;
}
```

- To rotate a right-leaning red link left:

```java
private Node rotateLeft(Node h) {
  Node x = h.right;
  h.right = x.left;
  x.left = h;
  x.color = h.color;
  h.color = RED;
  return x;
}
```

- To rotate a left-leaning red link right:

```java
private Node rotateRight(Node h) {
  Node x = h.left;
  h.left = x.right;
  x.right = h;
  x.color = h.color;
  h.color = RED;
  return x;
}
```

- If we want to swap colors we would implement:

```java
private void flipColors(Node h) {
  h.color = RED;
  h.left.color = BLACK;
  h.right.color = BLACK;
}
```

- For left-leaning red-black trees, insertion is implemented as:

```java
private Node put(Node h, Key key, Value val) {
  // Insert at bottom and color red
  if (h == null) {
    return new Node(key, val, RED);
  }

  int cmp = key.compareTo(h.key);

  if (cmp < 0) {
    h.left = put(h.left, key, val);
  } else if (cmp > 0) {
    h.right = put(h.right, key, val);
  } else {
    h.val = val;
  }

  // Lean left
  if (isRed(h.right) && !isRed(h.left)) {
    h = rotateLeft(h);
  }
  // Balance four-node
  if (isRed(h.left) && isRed(h.left.left)) {
    h = rotateRight(h);
  }
  // Split four-node
  if (isRed(h.left) && isRed(h.right)) {
    flipColors(h);
  }

  return h;
}
```

- Height of tree is less than or equal to _2 _ log(N)\* in the worst case
- Every path from root to null link has same number of black links
- Never two red links in-a-row

### B-Trees

- B-trees are a generalization of balance trees which allow for data access using a minimum number of probes
- B-trees generalize two-three trees by allowing up to _M - 1_ key-link pairs per node
  - At least 2 key-link pairs at root
  - At least _M / 2_ key-link pairs in other nodes
  - External nodes contain client keys
  - Internal nodes contain copies of keys to guide search
- To search in a B-tree:
  - Start at root
  - Find interval for search key and take corresponding link
  - Search terminates in external node
- To insert in a B-tree
  - Search for new key
  - Insert at bottom
  - Split nodes with _M_ key-link pairs on the way up the tree
- A search or insertion in a B-tree of order _M_ with _N_ keys requires between _log\_{M-1}(N)_ and _log\_{M/2}(N)_ probes
- All internal nodes (besides root) have between _M / 2_ and _M - 1_ links
- In practice number of probes is at most four
- To optimize a B-tree, always keep root page in memory

## Week 5: Geometric Applications of BSTs

> We start with 1D and 2D range searching, where the goal is to find all points in a given 1D or 2D interval. To accomplish this, we consider kd-trees, a natural generalization of BSTs when the keys are points in the plane (or higher dimensions). We also consider intersection problems, where the goal is to find all intersections among a set of line segments or rectangles.

### 1D Range Search

- We will look at intersections among geometric objects where BSTs are the basis for solving these problems efficiently
- A 1D range search is an extension of ordered symbol table, here are some operations:
  - Insert key-value pair
  - Search for key _k_
  - Delete key _k_
  - **Range search**: find all keys between _k1_ and _k2_
  - **Range count**: number of keys between _k1_ and _k2_
- An application for 1D range search is using it for database queries
- A geometric interpretation of 1D range search is:
  - Keys are a point on a **line**
  - Find/count points in a given **1D interval**

### Line Segment Intersection

- Generally given _N_ horizontal and vertical line segments, find all intersections
- A brute force method for counting each intersection will produce a quadratic algorithm (not practical or scalable)
- A better way to find the intersections between two pairs of lines is to implement the sweep-line algorithm:
  - Sweep a vertical line from left to right
  - x-coordinates define events
  - h-segment (left endpoint): insert y-coordinate into BST
  - h-segment (right endpoint): remove y-coordinate into BST
  - v-segment: range search for interval of y-endpoints
- The sweep-line algorithm takes time proportional to _N _ log(N + R)* to fin all *R* intersections among *N\* orthogonal line segments

- In general the run-time of each operation for sweep-line is as follows:
  - Put x-coordinates on a PQ (or sort): _N _ log(N)\*
  - Insert y-coordinates into BST: _N _ log(N)\*
  - Delete y-coordinates from BST: _N _ log(N)\*
  - Range searches in BST: _N _ log(N + R)\*
- In summary, using the sweep-line algorithm reduces 2D orthogonal line segment intersection search to 1D range search

### KD-trees

- There exist an extension of the 1D range search known as 2D orthogonal range search:
  - Insert a 2D key
  - Delete a 2D key
  - Search for a 2D key
  - **Range search**: find all keys that lie in a 2D range
  - **Range count**: number of keys that lie in a 2D range
- The geometric interpretation of the 2D orthogonal range search includes:
  - Keys which are a point in the **plane**
  - Operations to find/count the points in a given **h-v rectangle** (axis-aligned)
- In grids, we want to use a _sqrt(N) _ sqrt(N)\* size square
- However, the main challenge with the 2D orthogonal range search is the problem of **clustering**:

  - Lists are too long, even though average length is short
  - Need data structure that adapts gracefully to data

- How to address clustering? Use 2D-tree (binary search tree) to find all points in a query axis-aligned rectangle:
  - Check if point in node lies in given rectangle
  - Recursively search left/bottom (if any could fall in rectangle)
  - Recursively search right/top (if any could fall in rectangle)
- Typical run-time for 2D-tree is _R + log(N)_
- Worst case for a balanced tree is _R + sqrt(N)_

- Similarly, to find the nearest neighbor in a 2D-tree:
  - Check distance from point in node to query point
  - Recursively search left/bottom (if any could fall in rectangle)
  - Recursively search right/top (if any could fall in rectangle)
  - Organize method so that it begins by searching for query point
- Typical run-time for 2D-tree when finding nearest neighbor is _log(N)_
- Worst case for a balanced tree when finding nearest neighbor is _N_

- So what is a KD-tree? It's simply an extension of what we have already seen before:
  - We want to recursively partition k-dimensional space into two half-spaces
  - To implement a KD-tree we use a BST but cycle through dimensions with 2D trees

### Interval Search

- 1D interval search is a data structure to hold set of overlapping intervals
- Some operations include:
  - Insert an interval _(lo, hi)_
  - Search for an interval _(lo, hi)_
  - Delete an interval _(lo, hi)_
  - **Interval intersection query**: given an interval _(lo, hi)_, find all intervals (or one interval) in data structure that intersects _(lo, hi)_
- For interval search trees we want to:
  - Create BST where each node stores an interval _(lo, hi)_
  - Use the left endpoint as BST **key**
  - Store **max endpoint** in subtree rooted at node
- To get performance guarantee we want use a **red-black BST**

### Rectangle Intersection

- With KD-trees we can find all intersections among a set of _N_ orthogonal rectangles
- The rectangle intersection problem became a very important problem when building computers since we needed a way to figure out how to draw on a computer, particularly microprocessor design became a **geometric** problem
- Since processing power doubles every 18 months (Moore's Law), we need to use linearithmic algorithm to sustain Moore's Law

- To solve the orthogonal rectangle search we implement the sweep-line algorithm but slightly modified for rectangles (interval search instead of range search):

  - x-coordinates of left and right endpoints define events
  - Maintain set of rectangles that intersect the sweep line in an interval search tree (using y-intervals of rectangle)
  - Left endpoint: interval search for y-interval of rectangle; insert y-interval
  - Right endpoint: remove y-interval

- In general the run-time of each operation for sweep-line for orthogonal rectangles is as follows:
  - Put x-coordinates on a PQ (or sort): _N _ log(N)\*
  - Insert y-coordinates into ST: _N _ log(N)\*
  - Delete y-coordinates from ST: _N _ log(N)\*
  - Interval searches for y-intervals: _N _ log(N) + R _ log(N)_

## Week 6: Hash Tables

> We begin by describing the desirable properties of hash function and how to implement them in Java, including a fundamental tenet known as the _uniform hashing assumption_ that underlies the potential success of a hashing application. Then, we consider two strategies for implementing hash tables—_separate chaining_ and _linear probing_. Both strategies yield constant-time performance for search and insert under the uniform hashing assumption. We conclude with applications of symbol tables including sets, dictionary clients, indexing clients, and sparse vectors.

### Hash Tables

- Another approach to implementing symbol tables that can also be very effective in a practical application is hashing
- Hashing involves using a **hash function** - a method for computing array index from a key
- When using a hash function we want to scramble the keys uniformly to produce a table index:
  - Efficiently computable
  - Each table index equally likely for each key
- Some examples of bad hashing:
  - Using the first three digits of a phone number
  - Using the first three digits of SSN
- Some examples of good hashing:
  - Using the last digits of a phone number or SSN
- We also need a different approach for each key type
- All Java classes inherit a method `hashCode()` which returns a 32-bit `int`
- In general if `x.equals(y)`, then `(x.hashCode() == y.hashCode())`
- It is highly desirable to have `!x.equals(y)`, then `(x.hashCode() != y.hashCode())`
- The default implementation for `hashCode()` uses the memory address of x

- Java implementation is different for `Integer`, `Boolean`, `Double`, etc. below is an implementation of `hashCode()` for the `Integer` type:

```java
public final class Integer {
  private final int value {
    // ...
    public int hashCode() {
      return value;
    }
  }
}
```

- The standard recipe for hash code design is as follows:
  - Combine each significant field using the _31 _ x + y\* rule
  - If field is a primitive type, use wrapper type `hashCode()`
  - If field is null, return `0`
  - If field is a reference type, use `hashCode()`
  - If field is an array, apply to each entry

### Separate Chaining

- Separate chaining is a collision red solution strategy that makes use of elementary linked list
- During a collision, two distinct keys hashing to same index:
  - Birthday problem - can't avoid collisions unless you have a ridiculous (quadratic) amount of memory
  - Coupon collector + load balancing - collisions will be evenly distributed
- How do we deal with collisions efficiently?

  - Use an array of _M < N_ linked lists:
    - Hash: map key to integer _i_ between _0_ and _M - 1_
    - Insert: put at front of _ith_ chain (if not already there)
    - Search: need to search only _ith_ chain

- Separate chaining implementation in Java is as follows:

```java
public class SeparateChainHashST<Key, Value> {
  // NOTE: Array doubling and halving code omitted
  private int M = 97; // Number of chains
  private Node[] st = new Node[M]; // Array of chains

  private static class Node {
    private Object key; // No generic array creation
    private Object val; // Declare key and value of type Object
    private Node next;
    // ...
  }

  public Value get(Key key) {
    int i = hash(key);
    for (Node x = st[i]; x != null; x = x.next) {
      if (key.equals(x.key)) {
        return (Value) x.val;
      }
    }
    return null;
  }
}
```

- For insertion the code becomes:

```java
public class SeparateChainHashST<Key, Value> {
  // NOTE: Array doubling and halving code omitted
  private int M = 97; // Number of chains
  private Node[] st = new Node[M]; // Array of chains

  private static class Node {
    private Object key; // No generic array creation
    private Object val; // Declare key and value of type Object
    private Node next;
    // ...
  }

  public void put(Key key, Value val) {
    int i = hash(key);
    for (Node x = st[i]; x != null; x = x.next) {
      if (key.equals(x.key)) {
        x.val = val;
        return;
      }
    }
    st[i] = new Node(key, val, st[i]);
  }
}
```

- The number of probes (`equals()` and `hashCode()`) for search/insert is proportional to _N / M_:
  - _M_ too large -> too many empty chains
  - _M_ too small -> chains too long
  - Typical choice: _M ~ N / 5_ -> constant-time operation

### Linear Probing

- Another closure resolution method is known as linear probing
- Linear probing is also known as _open addressing_ - when a new key collides, find next empty slot, and put it there
- The methods for linear probing for hash tables is as follows:

  - **Hash** - map key to integer _i_ between _0_ and _M - 1_
  - **Insert** - put a table at index _i_ if free; if not try _i + 1_, _i + 2_, etc.
  - **Search** - search table index _i_; if occupied but no match, try _i + 1_, _i + 2_, etc.
  - Note: array size _M_ **must be** greater than number of key-value pairs _N_

- The Java implementation for linear probing:

```java
public class LinearProbingHashST<Key, Value> {
  // NOTE: Array doubling and halving code omitted
  private int M = 30001;
  private Value[] vals = (Value[]) new Object[M];
  private Key[] keys = (Key[]) new Object[M];

  private int hash(Key key) {
    /* as before */
  }

  private void put (Key key, Value val) {
    int i;
    for (i = hash(key); keys[i] != null; i = (i + 1) % M) {
      if (keys[i].equals(key)) {
        break;
      }
    }
    keys[i] = key;
    vals[i] = val;
  }

  private Value get(Key key) {
    for (int i = hash(key); keys[i] != null; i = (i + 1) % M) {
      if (key.equals(keys[i])) {
        return vals[i];
      }
    }
    return null;
  }
}
```

- Clustering became a problem:
  - **Cluster** - a contiguous block of items
  - **Observation** - new keys likely to hash into middle of big clusters
- Under uniform hashing assumption, the average of number of probes in a linear probing hash table of size _M_ that contains _N = a _ M\* keys is:
  - Search hit ~ _(1 / 2) _ (1 + (1 / (1 - a)))\*
  - Search miss/insert ~ _(1 / 2) _ (1 + (1 / (1 - a)^2))\*
- In summary, for linear probing:
  - _M_ too large -> too many empty array entries
  - _M_ too small -> search time blows up
  - Typical choice: _a ~ N / M ~ 1/2_ -> constant-time operation

### Hash Table Context

- For `String` `hashCode()` in Java 1.1:

  - Only examine 8-9 evenly spaced characters for long strings
  - The benefit is saving time in performing arithmetic

- An implementation of `hashCode()` in Java:

```java
public int hashCode() {
  int hash = 0;
  int skip = Math.max(1, length() / 8);
  for (int i = 0; i < length(); i += skip) {
    hash = s[i] + (37 * hash);
  }
  return hash;
}
```

- However, there was great potential for bad collision patterns

- Separate chaining versus linear probing:

  - **Separate chaining**:
    - Easier to implement delete
    - Performance degrades gracefully
    - Clustering less sensitive to poorly-designed hash function
  - **Linear probing**:
    - Less wasted space
    - Better cache performance

- Hash tables versus balanced search trees:
  - **Hash tables**:
    - Simpler to code
    - No effective alternative for unordered keys
    - Faster for simple keys (a few arithmetic operations versus _log(N)_ compares)
    - Better system support in Java for strings (e.g. cache hash code)
  - **Balanced search trees**:
    - Stronger performance guarantee
    - Support for ordered ST (symbol table) operations
    - Easier to implement `compareTo()` correctly than `equals()` and `hashCode()`

## Week 6: Symbol Table Applications

This section will cover topics (optional) on symbol table applications including _sets_, _dictionary clients_, _indexing clients_, and _sparse vectors_

### Sets

- Mathematically, sets are a collection of distinct keys
- To implement a set:

  - Read in a list of words from one file
  - Print out all words from standard input that are { in, not in } the list

- An example of such implementation in the real world is a _whitelist_:

```java
public class WhiteList {
  public static void main(String[] args) {
    // Create empty set of strings
    SET<String> set = new SET<String>();

    // Read in whitelist
    In in = new In(args[0]);
    while (!in.isEmpty()) {
      set.add(in.readString());
    }

    // Print words in list
    while (!StdIn.isEmpty()) {
      String word = StdIn.readString();
      if (set.contains(word)) {
        StdOut.println(word);
      }
    }
  }
}
```

- Conversely, there is also a _blacklist_ implementation:

```java
public class BlackList {
  public static void main(String[] args) {
    // Create empty set of strings
    SET<String> set = new SET<String>();

    // Read in blacklist
    In in = new In(args[0]);
    while (!in.isEmpty()) {
      set.add(in.readString());
    }

    // Print words not in list
    while (!StdIn.isEmpty()) {
      String word = StdIn.readString();
      if (!set.contains(word)) {
        StdOut.println(word);
      }
    }
  }
}
```

### Dictionary Clients

- Dictionary clients refer to the dictionary lookup problem:
  - A comma-separated value (CSV) file
  - Key field
  - Value field
- Example of such a problem in practice include DNS lookups, amino acids, and class lists

- Below is an example of a dictionary lookup in a CSV file:

```java
public class LookupCSV {
  public static void main(String[] args) {
    // Process input file
    In in = new In(args[0]);
    int keyField = Integer.parseInt(args[1]);
    int valField = Integer.parseInt(args[2]);

    // Build symbol table
    ST<String, String> st = new ST<String, String>();
    while (!in.isEmpty()) {
      String line = in.readLine();
      String[] tokens = line.split(",");
      String key = tokens[keyField];
      String val = tokens[valField];
      st.put(key, val);
    }

    // Process lookups with standard I/O
    while (!StdIn.isEmpty()) {
      String s = StdIn.readString();
      if (!st.contains(s)) {
        StdOut.println("Not found");
      } else {
        StdOut.println(st.get(s));
      }
    }
  }
}
```

### Indexing Clients

- Indexing clients refer to the file indexing problem:

  - Our goal is to index a PC or the web
  - Given a list of files specified, create an index so that you can efficiently find all files containing a given query string
  - Solution: key = query string, value = set of files containing that string

- Below is an example of the file indexing implementation:

```java
import java.io.file;
public class FileIndex {
  public static void main(String[] args) {
    // Symbol table
    ST<String, SET<File>> st = new ST<String, SET<File>>();

    // List of file names from command line
    for (String filename : args) {
      File file = new File(filename);
      In in = new In(file);
      // For each word in file, add file to corresponding set
      while (!in.isEmpty()) {
        String key = in.readString();
        if (!st.contains(key)) {
          st.put(key, new SET<File>());
        }
        SET<File> set = st.get(key);
        set.add(file);
      }
    }

    // Process queries
    while (!StdIn.isEmpty()) {
      String query = StdIn.readString();
      StdOut.println(st.get(query));
    }
  }
}
```

### Sparse Vectors

- For nested loops the running time is _N^2_, how do we address this issue for matrices and matrix multiplication?
  - Use symbol tables!
- The standard representation of a vector is done using a 1D array:
  - Constant time access to elements
  - Space proportional to _N_
- The symbol table representation for a vector could be as follows:

  - Key = index, value = entry
  - Efficient iterator
  - Space proportional to number of non-zeroes

- The implementation in java is as follows:

```java
public class SparseVector {
  // HashST because order not important
  private HashST<Integer, Double> v;

  // Empty ST represents all zeros vector
  public SparseVector() {
    v = new HashST<Integer, Double>();
  }

  // a[i] = value
  public void put (int i, double x) {
    v.put(i, x);
  }

  // Return a[i]
  public double get(int i) {
    if (!v.contains(i)) {
      return 0.0;
    } else {
      return v.get(i);
    }
  }

  public Iterable<Integer> indices() {
    return v.keys();
  }

  // Dot product is constant time for sparse vectors
  public double dot(double[] that) {
    double sum = 0.0;
    for (int i : indices()) {
      sum += that[i] * this.get(i);
    }
    return sum;
  }
}
```

- For 2D arrays, the standard matrix representation is:
  - Each row of matrix is an **array**
  - Constant time access to elements
  - Space proportional to _N^2_
- The sparse matrix representation is as follows:

  - Each row of matrix is a **sparse vector**
  - Efficient access to elements
  - Space proportional to number of non-zeroes plus _N_

- The code for 1D sparse vector just needs the following:

```java
// ...
SparseVector[] a = new SparseVector[N];
double[] x = new double[N];
double[] b = new double[N];
// ...
// Initialize a[] and x[]
// ...
// Linear running time for sparse matrix
for (int i = 0; i < N; i++) {
  b[i] = a[i].dot(x);
}
```
