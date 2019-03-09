// Dutch national flag

// Simplest case in pseudo-code
public class DutchNationalFlag {

  public void Swap(int[][] a, int bucketA, int bucketB) {
    System.out.println("Array before swap: " + a);
    String temp = "";
    temp = a[bucketA][0];
    a[bucketA][0] = a[bucketB][0];
    a[bucketB][0] = temp;
    System.out.println("Array after swap: " + a);
  }

  public void Color(int[][] a, int bucket) {
    System.out.println("Pebble color: " + a[bucket][0]);
  }

  // Test
  public static void main(String[] args) {
    DutchNationalFlag obj = new DutchNationalFlag();
    int[][] a = new int[][] {
      {"red"},
      {"white"},
      {"blue"}
      // {n} ...
    };

    obj.Swap(a, 0, 1);
    obj.Color(a, 0);
  }
}
