// Counting inversions

public class CountingInversions {

  public int countingInversions(int[] arr, int n) {
    // Initalize count to zero
    int count = 0;
    // Loop over array for the entire length
    for (int i = 0; i < n - 1; i++) {
      // For each element check if arr[i] > arr[j]
      for (int j = i + 1; j < n; j++) {
        if (arr[i] > arr[j]) {
          count++;
        }
      }
    }

    return count;
  }

  // Test
  public static void main(String[] args) {
    CountingInversions obj = new CountingInversions();
    int[] testArr = new int[] { 1, 2, 1, 2, 3 };
    System.out.println("There are " + obj.countingInversions(testArr, testArr.length) + " inversions!");
  }
}
