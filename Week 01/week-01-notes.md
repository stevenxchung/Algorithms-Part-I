## Week 1: Union-Find

> We illustrate our basic approach to developing and analyzing algorithms by considering the dynamic connectivity problem. We introduce the union–find data type and consider several implementations (quick find, quick union, weighted quick union, and weighted quick union with path compression). Finally, we apply the union–find data type to the percolation problem from physical chemistry.

### Steps to developing a usable algorithm
* Model the problem
* Find an algorithm to solve it
* Fast enough? Fits in memory?
* If not, figure out why
* Find a way to address the problem
* Iterate until satisfied

### Dynamic Connectivity
* Given a set of *N* objects
  * Union command: connect two objects
  * Find/connected query: is there a path connecting the two objects?
* When programming, it is convenient to name to name objects 0 to *N - 1*
  * Use integers as array index
  * Suppress details not relevant to union-find
* Find query will check if two objects are in the same component
* Replace components containing two objects with their union

* The skeleton java program might look something like this:
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

* We can check the API design before getting too far:
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
* Also called an eager algorithm, has a data structure consisting of:
  * Integer array *id[]* of size *N*
  * *p* and *q* are connected if and only if they have the same id
* Find operation becomes: check if *p* and *q* have the same id
* Union operation is a bit trickier: to merge components containing *p* and *q*, change all entries whose id equals *id[p]* to *id[q]*

* The java implementation for quick find might look something like this:
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

* However, quick-find is too slow, it would take *N^2* array accesses to process sequence of *N* union commands on *N* objects

### Quick-union
* Also known as lazy approach, has data structure consisting of:
  * Integer array *id[]* of size *N*
  * *id[i]* is parent of *i*
  * Root of *i* is *id[id[id[...id[i]...]]]*
* Find: check if *p* and *q* have the same root
* Union: Merge components containing *p* and *q*, set the id of *p*'s root to the id of *q*'s root

* Implementation in java:
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

* Unfortunately, quick-union is also too slow:
  * Trees can get tall
  * Find too expensive (could be *N* array accesses)

### Quick-union Improvements
* *Weighted* quick-union improves quick-union by:
  * Modifying quick-union to avoid tall trees
  * Keeping track of size of each tree (number of objects)
  * Balance by linking root of smaller tree to root of larger tree
* Data structure for weighted quick-union is the same as quick-union but we maintain extra array *sz[i]* to count number of objects in the tree rooted at *i*

* Find is identical to quick-union:
```java
return root(p) == root(q);
```

* Union will link root of smaller tree to root of larger tree and update the *sz[]* array:
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

* This weighted improvement allows the following:
  * Find takes time proportional to the depth of *p* and *q*
  * Union takes constant time, given roots

* Another improvement is to implement *path compression*:
  * Just after computing the root of *p*, set the id of each examined node to point to that root

* The java implementation of path compression is very simple, just add one line of code:
```java
private int root (int i) {
  while (i != id[i]) {
    id[i] = id[id[i]]; // extra line of code
    i = id[i];
  }
  return i;
}
```

* Friedman and Sachs proved that there is no linear-time algorithm for the union-find problem but weighted quick-union with path compression gets us pretty close!

### Percolation
* *N-by-N* grid of sites
* Each site is open with probability *p* (or blocked with probability *1 - p*)
* System percolates if and only if top and bottom are connected by open sites

## Week 1: Analysis of Algorithms

> The basis of our approach for analyzing the performance of algorithms is the scientific method. We begin by performing computational experiments to measure the running times of our programs. We use these measurements to develop hypotheses about performance. Next, we create mathematical models to explain their behavior. Finally, we consider analyzing the memory usage of our Java programs.

### Reasons to analyze algorithms
* Predict performance
* Compare algorithms
* Provide guarantees
* Understand theoretical basis
* Avoid performance bugs

### Scientific method applied to analysis of algorithms
* A framework for predicting performance and comparing algorithms
  * Observe some feature of the natural world (run-time of a program on a computer)
  * Hypothesize a mode that is consistent with the observations
  * Predict events using the hypothesis (run-time of a larger problem-size)
  * Verify the predictions by making further observations
  * Validate by repeating until the hypothesis and observations agree

* Some principles of the scientific method include:
  * Experiments must be reproducible
  * Hypotheses must be falsifiable

### Experimental Algorithmics
* Observing what is happening to the run-time of our programs as we vary input size gives us a way to predict performance
* System independent effects:
  * Algorithm
  * Input data
* System dependent effects:
  * Hardware: CPU, memory, cache, ...
  * Software: compiler, interpreter, garbage collector, ...
  * System: operating system, network, other apps, ...
* Bad news:
  * It is difficult to get precise measurements
* Good news:
  * Much easier and cheaper than other sciences

### Mathematical Models
* Although we could observe the run-time of our programs, it does not help us understand what our algorithms are doing
* By looking at some basic operation as a proxy for running time we can come up with a general run-time of our operation
* Once we have an estimate we can ignore lower order terms (tilde notation)
* We can estimate a discrete sum through discrete mathematics or calculus

### Order-of-growth Classifications
* There are only a small set of functions we care about regarding performance:
  * *log(N)* - Fast
  * *N* - OK
  * *N * log(N)* - OK
  * *N^2* - Slow
  * *N^3* - Slower
  * *2^N* - Slowest
* We try the best we can to make sure an algorithm is not quadratic or cubic


### Binary Search: Java implementation
* Below is a Java implementation of binary search which takes logarithmic time to complete:
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
* There are different ways of analyzing the algorithm depending on the input, this breaks down algorithm run-time into best case, average case, and worst case:
  * Best case is determined by the "easiest" input and provides a goal for all inputs
  * Worse case is determined by the "hardest" input and provides a guarantee for all inputs
  * Best and worst case can be mapped to the lower and upper bound of run-time respectively
  * Average case is the expected cost for a random input and provides a way to predict run-time
  * *Important to note that Big-O notation provides only an upper bound on the growth rate of a function as n gets large*

### Algorithm design approach
* Develop an algorithm
* Prove a lower bound
* Lower the upper bound (discover a new algorithm)
* Raise the lower bound (more difficult)

### Memory
* Although run-time costs of operations are important, memory cost is also important
* Let's look at an example of memory cost in Java, how much memory does this union-find function use as a function of *N*? *About 8N bytes*
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
