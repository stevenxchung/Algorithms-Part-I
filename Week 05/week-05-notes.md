## Week 5: Balanced Search Trees

> In this lecture, our goal is to develop a symbol table with guaranteed logarithmic performance for search and insert (and many other operations). We begin with 2-3 trees, which are easy to analyze but hard to implement. Next, we consider red-black binary search trees, which we view as a novel way to implement 2-3 trees as binary search trees. Finally, we introduce B-trees, a generalization of 2-3 trees that are widely used to implement file systems.

### 2-3 Search Trees
* In a two-three search tree we allow one or two keys per node:
  * Two-node: one key, two children
  * Three-node, two keys, three children
* A two-three tree has perfect balance - every path from root to null has same length
* In-order traversal yields keys in ascending order
* Splitting a four-node is a **local** transformation: constant number of operations
* Each transformation maintains symmetric order and perfect balance
* In terms of tree height:
  * Worst case performance for all two-nodes is *log(N)*
  * Best case performance for all three-nodes is about *0.631 * log(N)*
  * A two-three tree is guaranteed logarithmic performance for search and insert

### Red-black BSTs
* In red-black BSTs, we:
  * Represent a two-three tree as a BST
  * Use "internal" left-leaning links as "glue" for three-nodes
* Another way of looking at red-black trees is that they are BSTs where:
  * No node has two red links connected to it
  * Every path from root to null link has the same number of black links
  * Red links lean left
* Search is the same for elementary BSTs as for red-black BSTs (however red-black BSTs run faster due to better balance)
* The basic operations for red-black BST are:
  * Rotate left
  * Rotate right
  * Swap colors

* A red-black tree is represented as the following:
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

* To rotate a right-leaning red link left:
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

* To rotate a left-leaning red link right:
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

* If we want to swap colors we would implement:
```java
private void flipColors(Node h) {
  h.color = RED;
  h.left.color = BLACK;
  h.right.color = BLACK;
}
```

* For left-leaning red-black trees, insertion is implemented as:
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

* Height of tree is less than or equal to *2 * log(N)* in the worst case
* Every path from root to null link has same number of black links
* Never two red links in-a-row

### B-Trees
* B-trees are a generalization of balance trees which allow for data access using a minimum number of probes
* B-trees generalize two-three trees by allowing up to *M - 1* key-link pairs per node
  * At least 2 key-link pairs at root
  * At least *M / 2* key-link pairs in other nodes
  * External nodes contain client keys
  * Internal nodes contain copies of keys to guide search
* To search in a B-tree:
  * Start at root
  * Find interval for search key and take corresponding link
  * Search terminates in external node
* To insert in a B-tree
  * Search for new key
  * Insert at bottom
  * Split nodes with *M* key-link pairs on the way up the tree
* A search or insertion in a B-tree of order *M* with *N* keys requires between *log_{M-1}(N)* and *log_{M/2}(N)* probes
* All internal nodes (besides root) have between *M / 2* and *M - 1* links
* In practice number of probes is at most four
* To optimize a B-tree, always keep root page in memory

## Week 5: Geometric Applications of BSTs

> We start with 1D and 2D range searching, where the goal is to find all points in a given 1D or 2D interval. To accomplish this, we consider kd-trees, a natural generalization of BSTs when the keys are points in the plane (or higher dimensions). We also consider intersection problems, where the goal is to find all intersections among a set of line segments or rectangles.

### 1D Range Search
* We will look at intersections among geometric objects where BSTs are the basis for solving these problems efficiently
* A 1D range search is an extension of ordered symbol table, here are some operations:
  * Insert key-value pair
  * Search for key *k*
  * Delete key *k*
  * **Range search**: find all keys between *k1* and *k2*
  * **Range count**: number of keys between *k1* and *k2*
* An application for 1D range search is using it for database queries
* A geometric interpretation of 1D range search is:
  * Keys are a point on a **line**
  * Find/count points in a given **1D interval**

### Line Segment Intersection
* Generally given *N* horizontal and vertical line segments, find all intersections
* A brute force method for counting each intersection will produce a quadratic algorithm (not practical or scalable)
* A better way to find the intersections between two pairs of lines is to implement the sweep-line algorithm:
  * Sweep a vertical line from left to right
  * x-coordinates define events
  * h-segment (left endpoint): insert y-coordinate into BST
  * h-segment (right endpoint): remove y-coordinate into BST
  * v-segment: range search for interval of y-endpoints
* The sweep-line algorithm takes time proportional to *N * log(N + R)* to fin all *R* intersections among *N* orthogonal line segments

* In general the run-time of each operation for sweep-line is as follows:
  * Put x-coordinates on a PQ (or sort): *N * log(N)*
  * Insert y-coordinates into BST: *N * log(N)*
  * Delete y-coordinates from BST: *N * log(N)*
  * Range searches in BST: *N * log(N + R)*
* In summary, using the sweep-line algorithm reduces 2D orthogonal line segment intersection search to 1D range search

### KD-trees
* There exist an extension of the 1D range search known as 2D orthogonal range search:
  * Insert a 2D key
  * Delete a 2D key
  * Search for a 2D key
  * **Range search**: find all keys that lie in a 2D range
  * **Range count**: number of keys that lie in a 2D range
* The geometric interpretation of the 2D orthogonal range search includes:
  * Keys which are a point in the **plane**
  * Operations to find/count the points in a given **h-v rectangle** (axis-aligned)
* In grids, we want to use a *sqrt(N) * sqrt(N)* size square
* However, the main challenge with the 2D orthogonal range search is the problem of **clustering**:
  * Lists are too long, even though average length is short
  * Need data structure that adapts gracefully to data

* How to address clustering? Use 2D-tree (binary search tree) to find all points in a query axis-aligned rectangle:
  * Check if point in node lies in given rectangle
  * Recursively search left/bottom (if any could fall in rectangle)
  * Recursively search right/top (if any could fall in rectangle)
* Typical run-time for 2D-tree is *R + log(N)*
* Worst case for a balanced tree is *R + sqrt(N)*

* Similarly, to find the nearest neighbor in a 2D-tree:
  * Check distance from point in node to query point
  * Recursively search left/bottom (if any could fall in rectangle)
  * Recursively search right/top (if any could fall in rectangle)
  * Organize method so that it begins by searching for query point
* Typical run-time for 2D-tree when finding nearest neighbor is *log(N)*
* Worst case for a balanced tree when finding nearest neighbor is *N*

* So what is a KD-tree? It's simply an extension of what we have already seen before:
  * We want to recursively partition k-dimensional space into two half-spaces
  * To implement a KD-tree we use a BST but cycle through dimensions with 2D trees

### Interval Search
* 1D interval search is a data structure to hold set of overlapping intervals
* Some operations include:
  * Insert an interval *(lo, hi)*
  * Search for an interval *(lo, hi)*
  * Delete an interval *(lo, hi)*
  * **Interval intersection query**: given an interval *(lo, hi)*, find all intervals (or one interval) in data structure that intersects *(lo, hi)*
* For interval search trees we want to:
  * Create BST where each node stores an interval *(lo, hi)*
  * Use the left endpoint as BST **key**
  * Store **max endpoint** in subtree rooted at node
* To get performance guarantee we want use a **red-black BST**

### Rectangle Intersection
* With KD-trees we can find all intersections among a set of *N* orthogonal rectangles
* The rectangle intersection problem became a very important problem when building computers since we needed a way to figure out how to draw on a computer, particularly microprocessor design became a **geometric** problem
* Since processing power doubles every 18 months (Moore's Law), we need to use linearithmic algorithm to sustain Moore's Law

* To solve the orthogonal rectangle search we implement the sweep-line algorithm but slightly modified for rectangles (interval search instead of range search):
  * x-coordinates of left and right endpoints define events
  * Maintain set of rectangles that intersect the sweep line in an interval search tree (using y-intervals of rectangle)
  * Left endpoint: interval search for y-interval of rectangle; insert y-interval
  * Right endpoint: remove y-interval

* In general the run-time of each operation for sweep-line for orthogonal rectangles is as follows:
  * Put x-coordinates on a PQ (or sort): *N * log(N)*
  * Insert y-coordinates into ST: *N * log(N)*
  * Delete y-coordinates from ST: *N * log(N)*
  * Interval searches for y-intervals: *N * log(N) + R * log(N)*
