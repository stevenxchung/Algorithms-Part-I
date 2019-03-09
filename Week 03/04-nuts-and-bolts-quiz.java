// Nuts and bolts
// Pseudo-code

public class NutsAndBolts {
  // Swap method
  private static void swap(int[] a, int i, int j) {
    int objSwap = a[i];
    a[i] = a[j];
    a[j] = objSwap;
  }

  // Returns partition index of an array
  private static int partition(int[] arr, int lo, int hi, int pivot) {
    int i = lo;
    for (int j = lo; j < hi; j++) {
      if (arr[j] < pivot) {
        swap(arr, i, j);
        i++;
      } else if (arr[j] == pivot) {
        swap(arr, j, hi);
        j--;
      }
    }

    swap(arr, i, hi);
    return i;
  }

  // Matches nuts and bolts
  private static void nutsAndBolts(int[] nuts, int[] bolts, int lo, int hi) {
    if (lo < hi) {
      // Establish base case for pivot point and partition
      int pivot = partition(nuts, lo, hi, bolts[hi]);
      partition(bolts, lo, hi, nuts[pivot]);
      // Use recursion
      nutsAndBolts(nuts, bolts, lo, pivot - 1);
      nutsAndBolts(nuts, bolts, pivot + 1, hi);
    }
  }

  public static void main(String[] args) {
    // Test
  }
}
