## Week 3: Mergesort

We study the mergesort algorithm and show that it guarantees to sort any array of nn items with at most *n * log(n)* compares. We also consider a nonrecursive, bottom-up version. We prove that any compare-based sorting algorithm must make at least ∼*n * log(n)* compares in the worst case. We discuss using different orderings for the objects that we are sorting and the related concept of stability.

### Mergesort
* Mergesort could be broken down into parts:
  * Divide array into halves
  * Recursively sort each half
  * Merge two halves

* The Java implementation of Mergesort could be implemented as follows:
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
