// Stack with max
import java.util.Stack;
import java.util.TreeSet;

public class StackWithMax<Item> extends Stack<Item> {

  private TreeSet<Item> tree = new TreeSet<Item>();

  // TreeSet.last() returns the last item which is the max
  public Item max() {
    return tree.last();
  }

  // Insert an element
  public Item push(Item item) {
    // Use push() from the parent class Stack
    super.push(item);
    tree.add(item);
    return item;
  }

  // Pop off an element
  public Item pop() {
    Item item = super.pop();
    tree.remove(item);
    return item;
  }

  // Test
  public static void main(String[] args) {
    StackWithMax<Integer> maxStack = new StackWithMax<Integer>();
    int a = 1;
    int b = 20;
    int c = 100;

    maxStack.push(a);
    maxStack.push(b);
    maxStack.push(c);

    System.out.println("Max: " + maxStack.max());

    maxStack.pop();
    maxStack.pop();

    System.out.println("Max: " + maxStack.max());
  }
}
