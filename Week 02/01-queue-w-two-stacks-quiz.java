// Queue with two stacks
import java.util.Stack;

public class StackQueue<Item> {
  // One stack for inputs
  private Stack<Item> input = new Stack<Item>();
  // One stack for outputs
  private Stack<Item> output = new Stack<Item>();

  public int size() {
    return input.size() + output.size();
  }

  public boolean isEmpty() {
    return (size() == 0);
  }

  public void enqueue(Item item) {
    // Check if item is null
    if (item == null) {
      throw new java.lang.NullPointerException();
    }
    input.push(item);
  }

  public Item dequeue() {
    // Check for empty objects
    if (isEmpty()) {
      throw new java.util.NoSuchElementException();
    }
    // Check for empty outputs
    if (output.isEmpty()) {
      while (!input.isEmpty()) {
        // Push to output stack whatever was popped off the input stack
        output.push(input.pop());
      }
    }

    return output.pop();
  }

  // Testing
  public static void main(String[] args) {
    StackQueue<Integer> queueWTwoStacks = new StackQueue<Integer>();
    int start = 0;
    int incr = 0;
    int end = 10;

    System.out.println("Initial size: " + queueWTwoStacks.size());
    queueWTwoStacks.enqueue(start);
    while (incr <= end) {
      // Dequeue even elements
      if (incr % 2 == 0) {
        System.out.println("Dequeue: " + queueWTwoStacks.dequeue());
      } else {
        // Otherwise enqueue at odd elements
        queueWTwoStacks.enqueue(incr);
        System.out.println("Enqueue: " + incr);
      }
      incr++;
    }
    // Continue to dequeue until list is empty
    while (!queueWTwoStacks.isEmpty()) {
        System.out.println("Dequeue: " + queueWTwoStacks.dequeue());
    }
    System.out.println("Final Size: " + queueWTwoStacks.size());
  }
}
