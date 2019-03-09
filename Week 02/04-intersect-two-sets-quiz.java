// Intersection of two sets

// Simplest case in pseudo-code
public class Intersection {
  public int countMatches(int[][] a, int[][] b) {
    int count = 0;
    for (int i = 0; i < a.length; i++) {
      for (int j = 0; j < a[i].length; j++) {
        if (a[i][j] == b[i][j]) {
          count++;
        }
      }
    }
    return count;
  }

  // Test
  public static void main(String[] args) {
    Intersection obj = new Intersection();
    int[][] a = new int[][] {
      {1, 2},
      {3, 4},
      {5, 6},
      {7, 8},
    };
    int[][] b = new int[][] {
      {1, 2},
      {3, 4},
      {5, 6},
      {7, 8},
    };
    System.out.println("There are " + obj.countMatches(a, b) + " matches found!");
  }
}
