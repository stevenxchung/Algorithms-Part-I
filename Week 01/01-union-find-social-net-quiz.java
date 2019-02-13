// Social network connectivity
public class WeightedQuickUnionFind {
  private int id[]; // Parents of node i at id[i]
  private int size[]; // Size of node i at size[i]
  private int numConnected; // n connected components

  private int root(int i) {
    while (i != id[i]) {
      id[i] = id[id[i]]; // From path compression lesson
      i = id[i];
    }
    return i;
  }

  public int numConnected() {
    return numConnected;
  }

  public boolean connected(int p, int q) {
    return root(p) == root(q);
  }

  public WeightedQuickUnionFind(int N) {
    numConnected = N;
    id = new int[N];
    size = new int[N];
    for (int i = 0; i < N; ++i) {
      id[i] = i;
      size[i] += 1;
    }
  }

  public void union(int p, int q) {
    int i = root(p);
    int j = root(q);
    if (i == j)
      return;
    if (size[i] < size[j]) {
      id[i] = j;
      size[j] += size[i];
    } else {
      id[j] = i;
      size[i] += size[j];
    }
    numConnected--;
  }

  public static void main(String[] args) {
    WeightedQuickUnionFind unionFind = new WeightedQuickUnionFind(5);
    System.out.println(unionFind.numConnected() == 1);
    unionFind.union(0, 1);
    System.out.println(unionFind.numConnected() == 2);
    unionFind.union(2, 3);
    System.out.println(unionFind.numConnected() == 3);
    unionFind.union(4, 5);
    System.out.println(unionFind.numConnected() == 4);
    unionFind.union(6, 7);
    System.out.println(unionFind.numConnected() == 5);
  }
}
