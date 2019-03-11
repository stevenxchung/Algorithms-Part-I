## Week 3: Mergesort

> We study the mergesort algorithm and show that it guarantees to sort any array of nn items with at most *n * log(n)* compares. We also consider a non-recursive, bottom-up version. We prove that any compare-based sorting algorithm must make at least ∼*n * log(n)* compares in the worst case. We discuss using different orderings for the objects that we are sorting and the related concept of stability.

### Mergesort
* Mergesort could be broken down into parts:
  * Divide array into halves
  * Recursively sort each half
  * Merge two halves

* The Java implementation of mergesort could be implemented as follows:
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

* To add to the above implementation:
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

* The run-time estimate of mergesort is 10^8 compares/second
* A supercomputer executes 10^12 compares/second
* The lesson learned is that a good algorithm is better than investing in a supercomputer
* Mergesort uses at most *n * log(n)* compares and about *n * log(n)* array accesses to sort any array of size *n*

* One improvement we could make is using insertion sort as part of our sort procedure:
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
* Although mergesort is easy to understand as recursive program, bottom-up version has no recursion:
  * Pass through array, merging sub-arrays of size 1
  * Repeat for sub-arrays of size 2, 4, 8, 16, ...

* A Java implementation of bottom-up mergesort:
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
* Computational complexity is a framework to study efficiency of algorithms for solving a particular problem X
* There are several thing we care about when studying complexity:
  * Model of computation - allowable operations
  * Cost model - operation counts
  * Upper bound - cost guarantee provided by some algorithm for X
  * Lower bound - proven limit on cost guarantee of all algorithms for X
  * Optimal algorithm - algorithm with best possible cost guarantee for X

* Here is an example with sorting:
  * Model of computation: decision tree
  * Cost model: # compares
  * Upper bound: ~*n * log(n)* (mergesort)
  * Lower bound: ~*n * log(n)*
  * Optimal algorithm: mergesort

* Mergesort is optimal with respect to # compares but not optimal with respect to space usage

### Comparators
* We previously saw how Comparables allow us to implement sorts with any type of data, with Comparators, we can help sort using some alternate order or many different orders on the same data

* Here is an insertion sort implementation using Comparators:
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

* In general, to implement a comparator:
  * Define a (nested) class that implements the Comparator interface
  * Implement the compare() method

### Stability
* Stability refers to how well an algorithm is able to preserve the relative order of items with equal keys
* Which one of our previously covered sorts are stable?
  * Insertion and merge sort are stable but not selection or shellsort
* Why are insertion sort and merge sort stable?
  * Equal items never move past each other
* Why are selection sort and shellsort not stable?
  * There may be long-distance exchanges which might move an item past some equal item

## Week 3: Quicksort

> We introduce and implement the randomized quicksort algorithm and analyze its performance. We also consider randomized quick-select, a quicksort variant which finds the kth smallest item in linear time. Finally, consider 3-way quicksort, a variant of quicksort that works especially well in the presence of duplicate keys.

### Quicksort
* Quicksort is a recursive method which works in several steps:
  * Shuffle the array
  * Partition so that, for some j:
    * Entry a[j] is in place
    * No larger entry to the left of j
    * No smaller entry to the right of j
  * Sort each piece recursively

* The code for partitioning is as follows:
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

* Remember that quicksort itself is a recursive program:
``` java
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

* Shuffling is needed for performance guarantee, this preserves randomness
* Best case number of compares for quicksort is ~*n * log(n)*
* Worst case number of compares for quicksort is ~*(1/2) * n^2*
* The average number of compares for quicksort is ~*2n * log(n)*
* On average, quicksort does about 40% more compares than mergesort but faster than mergesort due to less data movement
* Quicksort is an *in-place* sorting algorithm
* Quicksort is *not stable*

* There is a way to make quicksort faster by using insertion sort as a step for small arrays:
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

* Another improvement is to take median of sample (not worth for large samples):
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
* Similar to quicksort we want a method to quickly select a particular item in a set
* The idea for selection is to partition an array so that:
  * Entry a[j] is in place
  * No larger entry to the left of j
  * No smaller entry to the right of j
* Repeat in one sub-array, depending on j
* Finished when j equals k

* An implementation of quick-select is as follows:
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

* Quick-select takes *linear-time* on average

### Duplicate Keys
* Mergesort with duplicate keys always took between *(1/2) * n * log(n)* and *n * log(n)* compares
* Quicksort with duplicate keys goes quadratic unless partitioning stops on equal keys
* We want to partition array into three parts such that:
  * Entries between *a* and *b* are equal to partition item *c*
  * No larger entries to left to *a* (lower bound)
  * No smaller entries to right of *b* (upper bound)

* Here's how we would implement three-way quicksort:
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

* Randomized quicksort with three-way partitioning reduces running time from linearithmic to linear in broad class of applications

### System Sorts
* Java has a *sort()* method which:
  * Has a different method for each primitive type
  * Has a method for data types that implement Comparable
  * Has a method that uses a Comparator
  * Uses tuned quicksort for primitive types; tuned mergesort for objects

* A sort function widely used in C, C++ and Java has the following:
  * Cutoff to insertion sort for small sub-arrays
  * Partitioning scheme: three-way partitioning (Dijkstra's)
  * Partitioning item:
    * Small arrays: middle entry
    * Medium arrays: median of three
    * Large arrays: Tukey's ninther

* Tukey's ninther - median of three samples, each three entries:
  * Approximates the median of nine
  * Uses at most twelve compares
