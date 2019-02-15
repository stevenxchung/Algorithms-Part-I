import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.StdOut;

public class PercolationStats {

    private double[] fractions; // Initialize array of fractions

    /**
     * Returns ratio of opened sites to total sites to determine when percolation occurs
     */
    private double percResults(int n) {
        Percolation perc = new Percolation(n);
        int openedSites = 0;
        while (!perc.percolates()) {
            int i = StdRandom.uniform(1, n + 1);
            int j = StdRandom.uniform(1, n + 1);
            if (!perc.isOpen(i, j)) {
                perc.open(i, j);
                openedSites++;
            }
        }
        return (double) openedSites / (n * n);
    }

    /**
     * Performs a series of computational experiments on the n x n grid
     */
    public PercolationStats(int n, int trials) {
        // Check for invalid inputs
        if (n <= 0 || trials <= 0) {
            throw new java.lang.IllegalArgumentException();
        }
        fractions = new double[trials];
        for (int i = 0; i < trials; i++) {
            fractions[i] = percResults(n);
        }
    }

    /**
     * Sample mean of percolation threshold
     */
    public double mean() {
        return StdStats.mean(fractions);
    }

    /**
     * Sample standard deviation of percolation threshold
     */
    public double stddev() {
        return StdStats.stddev(fractions);
    }

    /**
     * Returns low endpoint of the 95% confidence interval
     */
    public double confidenceLo() {
        return mean() - ((1.96 * stddev()) / Math.sqrt(fractions.length));
    }

    /**
     * Returns high endpoint of 95% confidence interval
     */
    public double confidenceHi() {
        return mean() + ((1.96 * stddev()) / Math.sqrt(fractions.length));
    }

    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int trials = Integer.parseInt(args[1]);
        PercolationStats percStats = new PercolationStats(n, trials);
        StdOut.println("mean = " + percStats.mean());
        StdOut.println("stddev = " + percStats.stddev());
        StdOut.println("95% confidence interval = " + percStats.confidenceLo() + ", " + percStats.confidenceHi());
    }
}