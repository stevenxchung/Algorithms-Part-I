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
