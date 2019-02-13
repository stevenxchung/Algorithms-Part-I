// Successor with delete
public class SuccessorAfterDelete {
  private int N; // Number of integers N in S
  private boolean data[]; // False if data[x] is removed
  private UnionFindLargest unionFind; // From previous exercise

  public int successor(int x) {
    // Proceed only if data exists
    if (data[x]) {
      return x;
    } else {
      int numIntegers = unionFind.find(x) + 1;
      if (numIntegers >= N) {
        System.out.println("Error, no successor!");
        return -1;
      } else {
        return numIntegers;
      }
    }
  }

  public void remove(int x) {
    data[x] = false; // Data is removed
    // Union to successor depending on the node value
    if (x < N - 1 && !data[x + 1]) {
      unionFind.union(x, x + 1);
    }
    if (x > 0 && !data[x - 1]) {
      unionFind.union(x, x - 1);
    }
  }

  public SuccessorAfterDelete(int N) {
    data = new boolean[N];
    // Set flags
    for (int i = 0; i < N; i++) {
      data[i] = true;
    }
    unionFind = new UnionFindLargest(N);
  }

  public static void main(String[] args) {
    SuccessorAfterDelete afterDelete = new SuccessorAfterDelete(5);
    afterDelete.remove(1);
    System.out.println(afterDelete.successor(1) == 2);
    afterDelete.remove(2);
    System.out.println(afterDelete.successor(2) == 3);
  }
}
