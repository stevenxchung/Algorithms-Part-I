// Shuffling a linked list
// Pseudo-code simplest case

public class ShufflingLinkedList {
  // Can shuffle a deck by cutting deck in half and alternate cards during merge
  public void shufflingLinkedList(Node head) {
    // Only move on if deck has cards
    if (head == NULL) {
      return;
    }

    // Spits deck at halfway point
    Node half = splitAtHalfway(head);
    // Alternate cards when merging
    alternateMerge(head, half);
    // Head of the list will be the card at the halfway point
    head = half;
  }

  public void splitAtHalfway(Node first) {
    // Spit deck at halfway point
  }

  public void alternateMerge(Node first, Node second) {
    // Merge the to halves by alternating cards
  }

  public static void main(String[] args) {
    // Test
  }
}
