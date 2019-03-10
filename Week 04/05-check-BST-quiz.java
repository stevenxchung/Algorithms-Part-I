// Check if a binary tree is a BST

public class CheckIfBinaryTreeIsBST {
  // Initialize private class for Node
  private class Node {
    private Node left, right;
    private int key;
    private int val;
    private int N;

    public Node(int key, int val, int N) {
      this.key = key;
      this.val = val;
      this.N = N;
    }
  }

  // Is the binary tree a BST?
  // Implements recursively
  public boolean checkIfBinaryTreeIsBST(Node n, int min, int max) {
    // BST if input node is null
    if (n == null) {
      return true;
    }

    // Cannot be a BST if key is not within the min/max range
    if (n.key >= max || n.key <= min) {
      return false;
    }

    return checkIfBinaryTreeIsBST(n.left, min, n.key) && checkIfBinaryTreeIsBST(n.right, n.key, max);
  }

  // Test
  public static void main(String[] args) {
    CheckIfBinaryTreeIsBST obj = new CheckIfBinaryTreeIsBST();
    System.out.println("Is the binary tree a BST? " + obj.checkIfBinaryTreeIsBST(null, 0, 100));
  }
}
