import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class Solver {
    // Instantiate class Go
    private class Go implements Comparable<Go> {
        private Board board;
        private Go previous;
        private int moves = 0;

        private Go(Board board, Go previous) {
            this.board = board;
            this.previous = previous;
            this.moves = previous.moves + 1;
        }

        private Go(Board board) {
            this.board = board;
        }

        public int compareTo(Go go) {
            return (this.moves - go.moves) + (this.board.manhattan() - go.board.manhattan());
        }
    }

    private Go lastPlacement;

    // Returns null or goal depending on the case
    private Go findFinish(MinPQ<Go> go) {
        // If there is no place left to go
        if (go.isEmpty()) {
            return null;
        }

        // Smallest key in the queue
        Go goal = go.delMin();

        // If the goal board is achieved
        if (goal.board.isGoal()) {
            return goal;
        }

        // Loop through all neighbors
        for (Board neighbor : goal.board.neighbors()) {
            // Insert a move if valid
            if (goal.previous == null || !neighbor.equals(goal.previous.board)) {
                go.insert(new Go(neighbor, goal));
            }
        }

        // Otherwise, return null
        return null;
    }

    // Find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        // Create priority queues for the normal and twin board
        MinPQ<Go> go = new MinPQ<>();
        go.insert(new Go(initial));
        MinPQ<Go> twinGo = new MinPQ<>();
        twinGo.insert(new Go(initial.twin()));

        // Until either findFinish() returns null or lastPlacement is not null
        while (true) {
            lastPlacement = findFinish(go);
            if (findFinish(twinGo) != null || lastPlacement != null) {
                return;
            }
        }
    }

    // Is the initial board solvable?
    public boolean isSolvable() {
        return (lastPlacement != null);
    }

    // Min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        int minMoves;

        if (isSolvable()) {
            minMoves = lastPlacement.moves;
        } else {
            minMoves = -1;
        }

        return minMoves;
    }

    // Sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        // Create a stack
        Stack<Board> go = new Stack<>();

        if (!isSolvable()) {
            return null;
        }
        // Generate sequence
        while (lastPlacement != null) {
            go.push(lastPlacement.board);
            lastPlacement = lastPlacement.previous;
        }

        return go;
    }

    // Given in assignment
    public static void main(String[] args) {

        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}
