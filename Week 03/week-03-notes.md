## Week 3: Mergesort

We study the mergesort algorithm and show that it guarantees to sort any array of nn items with at most *n * log(n)* compares. We also consider a nonrecursive, bottom-up version. We prove that any compare-based sorting algorithm must make at least ∼*n * log(n)* compares in the worst case. We discuss using different orderings for the objects that we are sorting and the related concept of stability.

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

  assert isSorted(a, lo, hi); // Postcondition: a[lo... hi] sorted
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

### Bottom-up mergesort
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

### Sort complexity
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
