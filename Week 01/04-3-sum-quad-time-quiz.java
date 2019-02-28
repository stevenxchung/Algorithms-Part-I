// 3-SUM in quadratic time

public class FindTriplet {

	boolean findTriple(int nums[], int size, int target) {
		// First
		for (int i = 0; i < size - 2; i++) {
			// Second element
			for (int j = i + 1; j < size - 1; j++) {
        // Third element
				for (int k = j + 1; k < size; k++) {
					if (nums[i] + nums[j] + nums[k] == target) {
						System.out.print("Triplet: " + nums[i] + ", " + nums[j] + ", " + nums[k]);
						return true;
					}
				}
			}
		}
		// No triplet was found
		return false;
	}

	// Test function
	public static void main(String[] args) {
		FindTriplet triplet = new FindTriplet();
		int nums[] = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
		int target = 20;
		int size = nums.length;

		triplet.findTriple(nums, size, target);
	}
}
