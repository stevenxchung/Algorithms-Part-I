import java.util.LinkedList;

public class Board {
    // Initialize 2D board
    private int[][] board;

    // Return a duplicate of the board
    private int[][] dupe(int[][] board) {
        int[][] dupe = new int[board.length][board.length];
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                dupe[i][j] = board[i][j];
            }
        }

        return dupe;
    }

    // Check if a particular spot is vacant
    private boolean vacant(int n) {
        return n == 0;
    }

    // Check if a particular spot is in the right place
    private boolean inPlace(int i, int j) {
        int spot = board[i][j];

        return vacant(spot) && spot == (i * dimension() + j + 1);
    }

    // Calculate Manhattan distance
    private int manDistance(int i, int j) {
        int spot = board[i][j];
        int iRelative = (spot - 1) / dimension();
        int jRelative = (spot - 1) % dimension();
        int distance = 0;

        if (!vacant(spot)) {
            // We only care about non-vacant spots to the finished board
            distance = Math.abs(i - iRelative) + Math.abs(j - jRelative);
        }

        return distance;
    }

    // Swap two elements on the board
    private int[][] swapSpots(int i, int j, int a, int b) {
        int[][] dupe = dupe(board);
        int tempSpot = dupe[i][j];
        dupe[i][j] = dupe[a][b];
        dupe[a][b] = tempSpot;

        return dupe;
    }

    // Return history of vacancies
    private int[] vacantPosition() {
        for (int i = 0; i < dimension(); i++) {
            for (int j = 0; j < dimension(); j++) {
                if (vacant(board[i][j])) {
                    int[] position = new int[2];
                    position[0] = i;
                    position[1] = j;

                    return position;
                }
            }
        }

        throw new java.lang.RuntimeException();
    }

    // Construct a board from an n-by-n array of blocks
    // (where blocks[i][j] = block in row i, column j)
    public Board(int[][] board) {
        this.board = dupe(board);
    }

    // Board dimension n
    public int dimension() {
        return board.length;
    }

    // Number of blocks out of place
    public int hamming() {
        int blocksOutOfPlace = 0;
        for (int i = 0; i < dimension(); i++) {
            for (int j = 0; j < dimension(); j++) {
                if (!inPlace(i, j)) {
                    blocksOutOfPlace++;
                }
            }
        }

        return blocksOutOfPlace;
    }

    // Sum of Manhattan distances between blocks and goal
    public int manhattan() {
        int sumOfDistances = 0;
        for (int i = 0; i < dimension(); i++) {
            for (int j = 0; j < dimension(); j++) {
                if (!inPlace(i, j)) {
                    sumOfDistances += manDistance(i, j);
                }
            }
        }

        return sumOfDistances;
    }

    // Is this board the goal board?
    public boolean isGoal() {
        for (int i = 0; i < dimension(); i++) {
            for (int j = 0; j < dimension(); j++) {
                // If an element is not in place then the board has not been completed
                if (!inPlace(i, j)) {
                    return false;
                }
            }
        }

        return true;
    }

    // A board that is obtained by exchanging any pair of blocks
    public Board twin() {
        for (int i = 0; i < dimension(); i++) {
            for (int j = 0; j < dimension() - 1; j++) {
                if (!vacant(board[i][j]) && !vacant(board[i][j + 1])) {
                    return new Board(swapSpots(i, j, i, j + 1));
                }
            }
        }

        throw new java.lang.RuntimeException();
    }

    // Does this board equal y?
    public boolean equals(Object y) {
        // Run through initial checks before checking each spot
        if (y == this) {
            return true;
        } else if (((Board) y).board.length != board.length || !(y instanceof Board) || y == null) {
            return false;
        }

        // Check each spot on the board
        for (int i = 0; i < dimension(); i++) {
            for (int j = 0; j < dimension(); j++) {
                if (((Board) y).board[i][j] != board[i][j]) {
                    // Return as soon as mismatch occurs
                    return false;
                }
            }
        }

        // Otherwise both objects match
        return true;
    }

    // All neighboring boards
    public Iterable<Board> neighbors() {
        LinkedList<Board> neighbors = new LinkedList<>();

        int[] position = vacantPosition();
        int iVPos = position[0];
        int jVPos = position[1];

        // All scenarios for neighboring boards
        // Rows scenario
        if (iVPos > 0) {
            neighbors.add(new Board(swapSpots(iVPos, jVPos, iVPos - 1, jVPos)));
        } else if (iVPos < dimension() - 1) {
            neighbors.add(new Board(swapSpots(iVPos, jVPos, iVPos + 1, jVPos)));
        }
        // Columns scenario
        if (jVPos > 0) {
            neighbors.add(new Board(swapSpots(iVPos, jVPos, iVPos, jVPos - 1)));
        } else if (jVPos < dimension() - 1) {
            neighbors.add(new Board(swapSpots(iVPos, jVPos, iVPos, jVPos + 1)));
        }

        return neighbors;
    }

    // String representation of this board (in the output format specified below)
    public String toString() {
        // Use StringBuilder to handle string representation
        StringBuilder gameBoard = new StringBuilder();
        gameBoard.append(dimension() + "\n");
        int element = 0;
        for (int i = 0; i < dimension(); i++) {
            for (int j = 0; j < dimension(); j++) {
                element = board[i][j];
                gameBoard.append(String.format("%2d ", element));
            }
            // Add a newline for each row
            gameBoard.append("\n");
        }

        return gameBoard.toString();
    }

    // Unit tests (not graded)
    public static void main(String[] args) {

    }
}
