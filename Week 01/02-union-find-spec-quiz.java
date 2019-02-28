// Union-find with specific canonical element

public class UnionFindLargest {
  private int id[]; // Parents of node i at id[i]
  private int size[]; // Size of node i at size[i]
  private int large[]; // Largest component at large[i]

  private int root(int i) {
    while (i != id[i]) {
      id[i] = id[id[i]]; // From path compression lesson
      i = id[i];
    }
    return i;
  }

  // Returns the largest element in the connected component containing i
  public int find(int i) {
    return large[root(i)];
  }

  public boolean connected(int i, int j) {
    return root(i) == root(j);
  }

  public UnionFindLargest(int N) {
    id = new int[N];
    size = new int[N];
    large = new int[N];
    for (int i = 0; i < N; ++i) {
      id[i] = i;
      size[i] += 1;
      large[i] = i;
    }
  }

  public void union(int p, int q) {
    int i = root(p);
    int j = root(q);
    int largeRootP = large[rootp];
    int largeRootQ = large[rootq];

    if (i == j) {
      return;
    }

    if (size[i] < size[j]) {
      id[i] = j;
      size[j] += size[i];

      if (largeRootP > largeRootQ) {
        large[j] = largeRootP;
      }
    } else {
      id[j] = i;
      size[i] += size[j];

      if (largeRootP < largeRootQ) {
        large[i] = largeRootQ;
      }
    }
  }

  public static void main(String[] args) {
    UnionFindLargest unionFind = new UnionFindLargest(4);
    unionFind.union(0, 1);
    unionFind.union(2, 3);
    System.out.println(unionFind.find(0) == 1);
    System.out.println(unionFind.find(2) == 3);
  }
}
