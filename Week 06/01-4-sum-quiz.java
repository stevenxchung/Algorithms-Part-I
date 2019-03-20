// 4-Sum
// Pseudo-code simplest case

public class FourSum {

  private void isNull(Object obj) {
    if (obj == null) {
      throw new java.lang.NullPointerException();
    }
  }

  private void checkLength(int[] a, int n) {
    int lenA = a.length;
    if (n >= lenA) {
      throw new IllegalArgumentException();
    }
  }

  // Returns true if distinct indicies i, j, k, and l yield a[i] + a[j] = a[k] + a[l]
  public boolean fourSum(int[] a, int i, int j, int k, int l) {
    // Check if any arguments are null
    isNull(a);
    isNull(i);
    isNull(j);
    isNull(l);

    // Check if index exceeds or is equal to length of array
    checkLength(a, i);
    checkLength(a, j);
    checkLength(a, k);
    checkLength(a, l);

    // Check if a[i] + a[j] = a[k] + a[l]
    if (a[i] + a[j] == a[k] + a[l]) {
      return true;
    }

    return false;
  }

  // Test
  public static void main(String[] args) {
    FourSum obj = new FourSum();
    int[] a = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
    int i = 0;
    int j = 9;
    int k = 3;
    int l = 6;
    System.out.println("a[" + i + "] + a[" + j + "] = a[" + k + "] + a[" + l + "]? " + obj.fourSum(a, i, j, k, l));
  }
}
