// Merging with smaller auxiliary array

public class MergeWithSmallAuxiliaryArray {

  public void mergeWithSmall(Comparable[] original, Comparable[] aux, int N) {
    // Initialize auxiliary array
    for (int i = 0; i < N; i++) {
      aux[i] = original[i];
    }

    // Original array index
    int a = N;
    // Auxiliary index
    int b = 0;
    // Merged array index
    int merge = 0;
    while (merge < original.length) {
      // Move merge and original index if aux index is greater or equal to N
      if (b >= N) {
        original[merge++] = original[a++];
      // Move merge and aux index if aux index is greater or equal to original array length
      } else if (a >= original.length) {
        original[merge++] = aux[b++];
      // Move merge and aux index if aux value is less than original
      } else if (aux[b].compareTo(original[a]) < 0) {
        original[merge++] = aux[b++];
      // Otherwise, move merge and original index
      } else {
        original[merge++] = original[a++];
      }
    }
  }

  public static void main(String[] args) {
    // Test
  }
}
