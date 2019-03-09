// Permutation
import java.util.ArrayList;

// Simplest case in pseudo-code
public class Permutation {

  public void isPermutation(int[] a, int[] b) {
    List<Integer> checkArrSize = new ArrayList<Integer>();
    for (int i = 0; i < a.length - 1; i++) {
      for (int j = 0; j < b.length - 1; j++) {
        if (a[i] == b[j]) {
          checkArrSize.add(b[j]);
        }
      }
    }

    if (a.length == checkArrSize.length) {
      System.out.println(a + " is a permuation of " + b);
    } else {
      System.out.println(a + " is not a permuation of " + b);
    }
  }

  // Test
  public static void main(String[] args) {
    Permutation obj = new Permutation();
    int[] a = new int[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
    int[] b = new int[] {9, 8, 7, 6, 5, 4, 3, 2, 1, 0};

    obj.isPermutation(a, b);
  }
}
