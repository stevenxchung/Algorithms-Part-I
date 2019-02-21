import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

// Takes an integer k, reads in a sequence of strings and prints exactly k of them
public class Permutation {
    public static void main(String[] args) {
        RandomizedQueue<String> strings = new RandomizedQueue<String>();
        // Use k as number of args
        int k = Integer.parseInt(args[0]);

        // Get input
        while (!StdIn.isEmpty()) {
            strings.enqueue(StdIn.readString());
        }

        for (int i = 0; i < k; i++) {
            StdOut.println(strings.dequeue());
        }
    }
}
