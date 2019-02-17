// Search in a bitonic array
public class FindBit {

	boolean findBit(int nums[], int size, int key) {
		// First
    for (int k = 0; k < size; k++) {
      if (nums[k] == key) {
        System.out.print("Key: " + nums[k]);
        return true;
      }
    }
		// No key was found
		return false;
	}

	// Test function
	public static void main(String[] args) {
		FindBit bit = new FindBit();
		int nums[] = { 1, 2, 3, 4, 5, 4, 3, 2, 1 };
		int key = 5;
		int size = nums.length;

		bit.findBit(nums, size, key);
	}
}
