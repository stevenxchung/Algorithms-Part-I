// Inorder traversal with constant extra space

public class InorderTraversal {
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

  // Returns value of current node
  public int InorderTraversal(Node n) {
    // Breaks if input is null
    if (n == null) {
      return -1;
    }

    Node prev;
    Node curr = n;
    while (curr != null) {
      if (curr.left == null) {
        // Point to right node if left node is null
        curr = curr.right;
        return curr.val;
      } else {
        // Initialize previous node as left node
        prev = curr.left;

        while (curr != prev.right && prev.right != null) {
          prev = prev.right;
        }

        if (prev.right == null) {
          // Assign previous right node to current node then point to left node
          prev.right = curr;
          curr = curr.left;
        } else {
          // Otherwise previous right node is null and current node points to the right node
          prev.right = null;
          curr = curr.right;
          return curr.val;
        }
      }
    }

    return -1;
  }

  // Test
  public static void main(String[] args) {
    InorderTraversal obj = new InorderTraversal();
    System.out.println(obj.InorderTraversal(null));
  }
}
