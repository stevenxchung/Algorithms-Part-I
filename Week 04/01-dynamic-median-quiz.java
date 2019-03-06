// Dynamic median
import java.util.*;

public class DynamicMedian {
  // Upper half
  PriorityQueue<Integer> minHeap = null;
  // Lower half (see Comparator.reverseOrder() below)
  PriorityQueue<Integer> maxHeap = null;

  public DynamicMedian() {
    minHeap = new PriorityQueue<Integer>();
    maxHeap = new PriorityQueue<Integer>(Comparator.reverseOrder());
  }

  public double findMed() {
    if (minHeap.size() > maxHeap.size()) {
      // Return first element in the heap if satisfied
      return minHeap.peek();
    } else {
      // Otherwise closest median is the average of the first element on both heaps
      return (minHeap.peek() + maxHeap.peek()) / 2.0;
    }
  }

  public void addElement(int element) {
    // Add element to minHeap
    minHeap.offer(element);
    // Minimum element in minHeap goes to maxHeap
    maxHeap.offer(minHeap.poll());

    if (minHeap.size() < maxHeap.size()) {
      // Add max element from max to min if min heap is smaller
      minHeap.offer(maxHeap.poll());
    }
  }

  public static void main(String[] args) {
    // Test
  }
}
