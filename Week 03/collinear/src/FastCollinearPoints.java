import java.util.List;
import java.util.Arrays;
import java.util.LinkedList;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdDraw;


public class FastCollinearPoints {

    private LineSegment[] lineSegments;

    // Corner cases
    private void isNull(Point[] points) {
        if (points == null) {
            // Argument to the constructor is null
            throw new java.lang.IllegalArgumentException();
        }
        for (Point p : points) {
            if (p == null) {
                // Any point in the array is null
                throw new java.lang.IllegalArgumentException();
            }
        }
    }

    // Duplicate check
    private void isDuplicate(Point[] points) {
        for (int i = 0; i < points.length - 1; i++) {
            if (points[i].compareTo(points[i + 1]) == 0) {
                // Any point in the array is repeated
                throw new java.lang.IllegalArgumentException();
            }
        }
    }

    // Finds all line segments containing four or more points
    public FastCollinearPoints(Point[] points) {
        // Run through corner cases
        isNull(points);
        Point[] sorted = points.clone();
        Arrays.sort(sorted);
        isDuplicate(sorted);

        int arrLength = points.length;
        List<LineSegment> maxSegments = new LinkedList<>();

        // Loop through array
        for (int i = 0; i < arrLength; i++) {
            Point s = sorted[i];
            Point[] pointRefs = sorted.clone();
            // Sort array of point references by slope order which was already sorted
            Arrays.sort(pointRefs, s.slopeOrder());

            int j = 1;
            // Trigger as long as j is less than length of the points array
            while (j < arrLength) {
                LinkedList<Point> ll = new LinkedList<>();
                double slopeRef = s.slopeTo(pointRefs[j]);

                do {
                    // Trigger at least once and then evaluate while condition
                    // Condition requires slope reference to be equal to value
                    // returned by slopeTo()
                    // Evaluate j < array length as a precaution
                    ll.add(pointRefs[j++]);
                } while (j < arrLength && slopeRef == s.slopeTo(pointRefs[j]));

                if (ll.size() >= 3 && s.compareTo(ll.peek()) < 0) {
                    // Once linked list is 3 or more or points are equal (see Points.java)
                    // set max to last and add to maxSegments
                    Point min = s;
                    Point max = ll.removeLast();
                    maxSegments.add(new LineSegment(min, max));
                }

            }
        }
        // Update array of line segments
        lineSegments = maxSegments.toArray(new LineSegment[0]);
    }

    // The number of line segments
    public int numberOfSegments() {
        return lineSegments.length;
    }

    // The line segments
    public LineSegment[] segments() {
        return lineSegments.clone();
    }

    // Given in assignment
    public static void main(String[] args) {

        // Read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // Draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // Print and draw the line segments
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
