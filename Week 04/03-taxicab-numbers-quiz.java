// Taxicab numbers
import java.util.*;

public class TaxicabNumbers {

  public int taxicabNumbers(int n) {
    int taxiNum = 1;
    int firstCount = 0;

    // Until firstCount == requested nth taxicab number
    while (firstCount < n) {
      // Reset to 0 each loop
      int secondCount = 0;

      // Try all pairs for a^3 + b^3 = taxNum
      for (int a = 1; a <= Math.pow(taxiNum, 1/3); a++) {
        for (int b = a + 1; b <= Math.pow(taxiNum, 1/3); b++) {
          if (Math.pow(a, 3) + Math.pow(b, 3) == taxiNum) {
            secondCount++;
          }
        }
      }

      taxiNum++;
    }

    return taxiNum;
  }

  // Test
  public static void main(String[] args) {
    int n = 5;
    TaxicabNumbers obj = new TaxicabNumbers();
    System.out.println("Iteration no. " + n + " has a taxicab number of " + obj.taxicabNumbers(5));
  }
}
