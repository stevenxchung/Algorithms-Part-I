import java.util.Stack;

class StackQueue<Item> {
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
}
