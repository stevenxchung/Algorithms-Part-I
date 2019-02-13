## Week 1

Week 1 covers topics in Union-find which addresses the dyanmic connectivity problem. Analysis of algorithms is also another topic included in the Week 1 lectures.

### Steps to developing a usable algorithm
* Model the problem
* Find an algorithm to solve it
* Fast enough? Fits in memory?
* If not, figure out why
* Find a way to address the problem
* Iterate until satisfied

### Dynamic connectivity
* Given a set of N objects
  * Union command: connect two objects
  * Find/connected query: is there a path connecting the two objects?
* When programming, it is convenient to name to name objects 0 to N-1
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
  * Integer array id[] of size N
  * p and q are connected if and only if they have the same id
* Find operation becomes: check if p and q have the same id
* Union operation is a bit trickier: to merge components containing p and q, change all entries whose id equals id[p] to id[q]

* The java implementation for quick find might look something like this:
```java
public class QuickFindUF {
  private int[] id;

  public QuickFindUF(int N) {
    id = new int[N];
    for (int i = 0; i < N; i++)
      id[i] = i;
  }

  public boolean connected(int p, int q) {
    return id[p] == id[q];
  }

  public void union(int p, int q) {
    int pid = id[p];
    int qid = id[q];
    for (int i = 0; i < id.length; i++)
      if (id[i] == pid) id[i] = qid
  }
}
```

* However, quick-find is too slow, it would take N^2 array accesses to process sequence of N union commands on N objects

### Quick-union
* Also known as lazy approach, has data structure consisting of:
  * Integer array id[] of size N
  * id[i] is parent of i
  * Root of i is id[id[id[...id[i]...]]]
* Find: check if p and q have the same root
* Union: Merge components containing p and q, set the id of p's root to the id of q's root

* Implementation in java:
```java
public class QuickUnionUF {
  private int[] id;

  public QuickUnionUF(int N) {
    id = new int[N];
    for (int i = 0; i < N; i++) id[i] = i;
  }

  private int root(int i) {
    while (i != id[i]) i = id[i];
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
  * Find too expensive (could be N array accesses)

### Quick-union improvements
* *Weighted* quick-union improves quick-union by:
  * Modifying quick-union to avoid tall trees
  * Keeping track of size of each tree (number of objects)
  * Balance by linking root of smaller tree to root of larger tree
* Data structure for weighted quick-union is the same as quick-union but we maintain extra array sz[i] to count number of objects in the tree rooted at i

* Find is identical to quick-union:
```java
return root(p) == root(q);
```

* Union will link root of smaller tree to root of larger tree and update the sz[] array:
```java
int i = root(p);
int j = root(q);
if (i == j) return;
if (sz[i] < sz[j]) { id[i] = j; sz[j] += sz[i]; }
else  { id[j] = i; sz[i] += sz[j]; }
```

* This weighted improvement allows the following:
  * Find takes time proportional to the depth of p and q
  * Union takes constant time, given roots

* Another improvement is to implement *path compression*:
  * Just after computing the root of p, set the id of each examined node to point to that root

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
* N-by-N grid of sites
* Each site is open with probability p (or blocked with porbability 1-p)
* System percolates if and only if top and bottom are connected by open sites
