// Selection in two sorted arrays
// Pseudo-code simplest case where array sizes are equal, values are equal

public class SelectionInTwoSortedArrays {
  // Compares two arrays and returns the largest key (largest match)
  private static int compare(int[] arrA, int[] arrB) {
    int largest = 0;
    int lenA = arrA.length;
    int lenB = arrB.length;
    // If sorted, largest value is always the last one...
    if (arrA[lenA - 1] != arrB[lenB - 1]) {
      // Arrays not valid
      return -1;
    } else {
      // arrB[lenB - 1] works fine
      return largest = arrA[lenA - 1];
    }
  }

  public static void main(String[] args) {
    // Test
  }
}
