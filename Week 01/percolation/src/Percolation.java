import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private int boxSize; // Size of the grid
    private int top = 0; // Initial top node at 0
    private int bottom; // Initial bottom node
    private int nOpenSites = 0; // Number of opened sites
    private boolean[][] opened; // Holds 2D information on which site is open or closed
    private WeightedQuickUnionUF wqu; // Weight Quick Union object reference

    /**
     * Checks if indices are valid
     */
    private boolean checkIndices(int row, int col) {
        // True or false
        return (row < 1 || row > boxSize || col < 1 || col > boxSize);
    }

    /**
     * Converts 2D array into a 1D array
     */
    private int getIndex(int row, int col) {
        // Check if indices are valid
        if (checkIndices(row, col)) {
            throw new java.lang.IllegalArgumentException();
        }
        return boxSize * (row - 1) + col;
    }

    /**
     * Percolation() will create n x n grid, all sites will initially be unfilled
     */
    public Percolation(int inputSize) {
        boxSize = inputSize;
        bottom = boxSize * boxSize + 1;
        wqu = new WeightedQuickUnionUF(boxSize * boxSize + 2);
        opened = new boolean[boxSize][boxSize];
        // Check for invalid inputs
        if (inputSize <= 0) {
            throw new java.lang.IllegalArgumentException();
        }
    }

    /**
     * Opens site at a particular 2D index
     */
    public void open(int row, int col) {
        // Check if indices are valid
        if (checkIndices(row, col)) {
            throw new java.lang.IllegalArgumentException();
        }

        // If site is not open, set to open
        if (!isOpen(row, col)) {
            opened[row - 1][col - 1] = true; // Set whichever site is opened to true
            nOpenSites++;
        }

        // Checks if top row is selected first then go down the checklist
        if (row == 1) {
            wqu.union(getIndex(row, col), top);
        }
        if (row == boxSize) {
            wqu.union(getIndex(row, col), bottom);
        }

        // Must also check if particular site is open
        if (row > 1 && isOpen(row - 1, col)) {
            wqu.union(getIndex(row, col), getIndex(row - 1, col));
        }
        if (row < boxSize && isOpen(row + 1, col)) {
            wqu.union(getIndex(row, col), getIndex(row + 1, col));
        }
        if (col > 1 && isOpen(row, col - 1)) {
            wqu.union(getIndex(row, col), getIndex(row, col - 1));
        }
        if (col < boxSize && isOpen(row, col + 1)) {
            wqu.union(getIndex(row, col), getIndex(row, col + 1));
        }
    }

    /**
     * Check if site is open
     */
    public boolean isOpen(int row, int col) {
        // Check if indices are valid
        if (checkIndices(row, col)) {
            throw new java.lang.IllegalArgumentException();
        }
        return opened[row - 1][col - 1];
    }

    /**
     * Check if size is full
     */
    public boolean isFull(int row, int col) {
        // Check if indices are valid
        if (checkIndices(row, col)) {
            throw new java.lang.IllegalArgumentException();
        }
        return wqu.connected(getIndex(row, col), top);
    }

    /**
     * Return number of open sites
     */
    public int numberOfOpenSites() {
        return nOpenSites;
    }

    /**
     * Checks for percolation
     */
    public boolean percolates() {
        return wqu.connected(top, bottom);
    }
}