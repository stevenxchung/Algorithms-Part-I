## Week 2: Stacks and Queues

> We consider two fundamental data types for storing collections of objects: the stack and the queue. We implement each using either a singly-linked list or a resizing array. We introduce two advanced Java features—generics and iterators—that simplify client code. Finally, we consider various applications of stacks and queues ranging from parsing arithmetic expressions to simulating queueing systems.

### Stacks and Queues
* There are some fundamental data types:
  * Value: collection of objects
  * Operations: *insert, remove, iterate*, test if empty
* Stack - LIFO (last-in-first-out)
* Queue - FIFO (first-in-first-out)

### Stacks
* An example of a stack test client implemented in Java:
```java
public static void main(String[] args) {
  StackOfStrings stack = new StackOfStrings();
  while (!StdIn.isEmpty()) {
    String s = StdIn.readString();
    if (s.equals("-")) {
      StdOut.print(stack.pop());
    }
    else {
      stack.push(s);
    }
  }
}
```

* What if we wanted to implement a stack with a linked-list?
```java
public class LinkedStackOfStrings {
  private Node first = null;
  // Private inner class
  private class Node {
    String item;
    Node next;
  }

  public boolean isEmpty() {
    return first == null;
  }

  public void push(String item) {
    Node oldfirst = first;
    first = new Node();
    first.item = item;
    first.next = oldfirst;
  }

  public String pop() {
    String item = first.item;
    first = first.next;
    return item;
  }
}
```

* Every operation takes constant time in the worst case
* A stack with N items uses ~40 N bytes
