import java.util.List;
import java.util.Arrays;
import java.util.LinkedList;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdDraw;

public class BruteCollinearPoints {

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

    // Finds all line segments containing four points
    public BruteCollinearPoints(Point[] points) {
        // Run through corner cases
        isNull(points);
        Point[] sorted = points.clone();
        Arrays.sort(sorted);
        isDuplicate(sorted);

        int arrLength = points.length;
        List<LineSegment> segmentList = new LinkedList<>();

        // Until i < points.length - 3
        for (int i = 0; i < arrLength - 3; i++) {
            Point a = sorted[i];

            // Until j < points.length - 2
            for (int j = i + 1; j < arrLength - 2; j++) {
                Point b = sorted[j];
                double firstSlope = a.slopeTo(b);

                // Until k < points.length - 1
                for (int k = j + 1; k < arrLength - 1; k++) {
                    Point c = sorted[k];
                    double secondSlope = a.slopeTo(c);

                    if (firstSlope == secondSlope) {
                        // First slope has to equal second
                        // Until l < points.length
                        for (int l = k + 1; l < arrLength; l++) {
                            Point d = sorted[l];
                            double thirdSlope = a.slopeTo(d);

                            if (firstSlope == thirdSlope) {
                                // First slope has to equal third
                                segmentList.add(new LineSegment(a, d));
                            }
                        }
                    }
                }
            }
        }
        // Update array of line segments
        lineSegments = segmentList.toArray(new LineSegment[0]);
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
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
