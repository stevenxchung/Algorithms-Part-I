// Egg drop
// Version 0: 1 egg, <= T tosses
// Not really sure so I just tried random value for floors and T
import java.util.Random;

public class EggDrop {
  // From java.util.random
  private static int getRandomNumberInRange(int min, int max) {
		if (min >= max) {
			throw new IllegalArgumentException("max must be greater than min");
		}
		Random r = new Random();
		return r.nextInt((max - min) + 1) + min;
	}

	public boolean eggDrop(int floor, int breakPoint) {
		// Egg does break
    if (floor >= breakPoint) {
      System.out.print("Breaks! (floor=" + floor + " >= T=" + breakPoint + ")");
      return true;
    }
    // Egg does not break
    System.out.print("Does not break! (floor=" + floor + " < T=" + breakPoint + ")");
		return false;
	}

	// Test
	public static void main(String[] args) {
		EggDrop obj = new EggDrop();
		int floor = getRandomNumberInRange(1, 100);
		int breakPoint = getRandomNumberInRange(1, 100);

    obj.eggDrop(floor, breakPoint);
  }
}
