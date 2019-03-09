// Decimal dominants
// Pseudo-code

public class DecimalDominants {
  // Find occurrences
  public static int findOccurrences(int[] arr) {
    int lenArr = arr.length;
    int count = 0;
    int matches = 0;
    int j = lenArr - 1;

    // Start from beginning
    for (int i = 0; i < lenArr; i++) {
      System.out.println("i: " + i);
      System.out.println("j: " + j);

      if (arr[i] == arr[j] && i != j) {
        matches++;
      }

      if (arr[i] == arr[j] && matches > lenArr/10) {
        System.out.println("Satisfied!");
        return matches;
      } else if (i == j) {
        System.out.println("End of compare!");
        return 0;
      }
      count++;
      j--;
    }

    return matches;
  }

  // Test
  public static void main(String[] args) {
    DecimalDominants obj = new DecimalDominants();
    int[] arr = new int[] {1, 2, 3, 4, 5};
    System.out.println("There are " + findOccurrences(arr) + " occurrences!");
  }
}
