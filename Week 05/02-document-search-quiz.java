// Document search
// Pseudo-code simplest case

public class DocumentSearch {

  // Returns shortest interval in which m query words appear
  public int documentSearch(String[] n, String[] m) {
    // Invalid input
    if (n == null || m == null) {
      return -1;
    }

    int count = 0;

    // Try to find first occurence
    for (int i = 0; i < m.length; i++) {
      for (int j = 0; j < n.length; j++) {
        if (n[j] == m[i]) {
          count = j;
        }
      }
    }

    return count;
  }

  // Test
  public static void main(String[] args) {
    DocumentSearch obj = new DocumentSearch();
    String[] documentWords = new String[]{ "one", "two", "three"};
    String[] queryWords = new String[]{ "three" };
    System.out.println("The query " + queryWords[0] + " first appears at string index = " + obj.documentSearch(documentWords, queryWords));
  }
}
