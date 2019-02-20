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

* What if we wanted to implement a stack with an array?
```java
public class FixedCapacityStackOfStrings {
  private String[] s;
  private int N = 0;
  // Cheating a bit here since we are requiring the client to input a capacity
  public FixedCapacityStackOfStrings(int capacity) {
    s = new String[capacity];
  }

  public boolean isEmpty() {
    return N == 0;
  }

  public void push(String item) {
    s[N++] = item; // Use N++ to index into array then increment N
  }

  public String pop() {
    return s[--N]; // Decrement N, then use to index into array
  }
}
```

* Stack considerations include:
  * Underflow: throw an exception if pop from an empty stack
  * Overflow: use resizing array for array implementation
* Null items can be inserted into a stack
* Loitering: holding reference to an object when it is no longer needed
```java
// Here we are still holding references
public String pop() {
  return s[--N];
}

// To address loitering
public String pop() {
  String item = s[--N];
  s[N] = null
  return item;
}
```
### Resizing arrays
* Problem: looking back at our stack array implementation, we require the client to provide capacity when we need to know how to grow and shrink the array
* If array is full, create a new array of twice the size:
``` java
public ResizingArrayStackOfStrings() {
  s = new String[1];
}

public void push(String item) {
  if (N == s.length) {
    resize(2 * s.length);
  }
  s[N++] = item;
}

private void resize(int capacity) {
  String[] copy = new String[capacity];
  for (int i = 0; i < N; i++) {
    copy[i] = s[i];
  }
  s = copy;
}
```

* To shrink the array we would implement the logic:
  * push(): double size of array s[] when array is full
  * pop(): havle the size of array s[] when array is one-half full
* This sequence is too expensive in the worst case however since each operation takes N time, instead it is more efficient to implement the following:
   * push(): double size of array s[] when array is full
   * pop(): havle the size of array s[] when array is *one-quarter* full
``` java
public String pop() {
  String item = s[--N];
  s[N] = null;
  if (N > 0 && N == s.length/4) {
    resize(s.length/2);
  }
  return item;
}
```

* So when would we implement a resizing array vs a linked-list?
  * Linked-list implementation:
    * Every operation takes constant time in the worst case
    * Uses extra time and space to deal with the links
  * Resizing-array implementation:
    * Every operation takes constant amortized time (constant time overall)
    * Less wasted space

### Queues
* What if we wanted to implement a queue with a linked-list?
```java
public class LinkedQueueOfStrings {
  private Node first = null;
  private Node last = null;

  private class Node {
    String item;
    Node next;
  }

  public boolean isEmpty() {
    return first == null;
  }

  public void enqueue(String item) {
    Node oldlast = last;
    last = new Node();
    last.item = item;
    last.next = null;
    // Check if empty
    if (isEmpty()) {
      first = last;
    } else {
      oldlast.next = last;
    }
  }

  public String dequeue() {
    String item = first.item;
    first = first.next;
    // Check if empty
    if (isEmpty()) {
      last = null;
    }
    return item;
  }
}
```

### Generics
* So far our implementations only covers strings not other data types, we want to implement a separate stack class for each type
* One solution is to implement casting, however casting is error-prone since run-time error could occur if types mismatch
* A better approach is to use generics:
  * Avoids casting in the client
  * Discovers type mismatch errors at compile-time instead of run-time

* A linked-list implementation for stacks using generics:
```java
// Here we can use our stack implementation for the generic type name Item
public class Stack<Item> {
  private Node first = null;

  private class Node {
    Item item;
    Node next;
  }

  public boolean isEmpty() {
    return first == null;
  }

  public void push(Item item) {
    Node oldfirst = first;
    first = new Node();
    first.item = item;
    first.next = oldfirst;
  }

  public Item pop() {
    Item item = first.item;
    first = first.next;
    return item;
  }
}
```

* However Java does not allow for array implementation using generics:
```java
// This is not allowed in Java
public class FixedCapacityStack<Item> {
  private Item[] s;
  private int N = 0;

  public FixedCapacityStack(int capacity) {
    s = new Item[capacity];
  }

  public boolean isEmpty() {
    return N == 0;
  }

  public void push(Item item) {
    s[N++] = item;
  }

  public Item pop() {
    return s[--N];
  }
}
```

* Therefore, for arrays we are stuck with casting:
```java
// This is allowed in Java
public class FixedCapacityStack<Item> {
  private Item[] s;
  private int N = 0;

  public FixedCapacityStack(int capacity) {
    s = (Item[]) new Object[capacity]; // Cast using (Item[])
  }

  public boolean isEmpty() {
    return N == 0;
  }

  public void push(Item item) {
    s[N++] = item;
  }

  public Item pop() {
    return s[--N];
  }
}
```

* What about primitive types?
  * Each primitive type has a wrapper object type
  * There is a feature known as autoboxing which acts as an automatic cast between a primitive type and its wrapper:
```java
// This is what happens behind the scenes
Stack<Integer> s = new Stack<Integer>();
s.push(17); // s.push(new Integer(17));
int a = s.pop(); // int a = s.pop().intValue();
```

### Iterators
* What is an Iterable?
  * Has a method that returns an Iterator
```java
public interface Iterable<Item> {
  Iterator<Item> iterator();
}
```

* What is an Iterator?
  * Has methods hasNext() and next()
```java
public interface Iterable<Item> {
  boolean hasNext();
  Item next();
}
```

* Why make data structures Iterable?
  * Java supports elegant client code
```java
for (String s: stack) {
  StdOut.println(s);
}

// longhand foreach statement
Iterator<String> i = stack.iterator();
while (i.hasNext()) {
  String s = i.next();
  StdOut.println(s);
}
```

* Below is an implementation of a stack iterator for linked-lists:
```java
public class Stack<Item> implements Iterable<Item> {

  // ...

  public Iterator<Item> iterator() {
    return new ListIterator();
  }

  private class ListIterator implements Iterator<Item> {
    private Node current = first;

    public boolean hasNext() {
      return current != null;
    }

    // Not supported
    public void remove() {}

    public Item next() {
      Item item = current.item;
      current = current.next;
      return item
    }
  }
}
```

* For arrays it is similar:
```java
public class Stack<Item> implements Iterable<Item> {

  // ...

  public Iterator<Item> iterator() {
    return new ReverseArrayIterator();
  }

  private class ReverseArrayIterator implements Iterator<Item> {
    private int i = N;

    public boolean hasNext() {
      return i > 0;
    }

    // Not supported
    public void remove() {}

    public Item next() {
      return s[--i];
    }
  }
}
```

## Week 2: Elementary Sorts

We introduce the sorting problem and Java's Comparable interface. We study two elementary sorting methods (selection sort and insertion sort) and a variation of one of them (shellsort). We also consider two algorithms for uniformly shuffling an array. We conclude with an application of sorting to computing the convex hull via the Graham scan algorithm.
