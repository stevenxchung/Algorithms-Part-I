import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.util.TreeSet;

public class PointSET {
    // Initialize tree
    private TreeSet<Point2D> points;

    // Throw null if argument is null
    private void isNull(Object arg) {
        if (arg == null) {
            throw new java.lang.IllegalArgumentException();
        }
    }

    // Construct an empty set of points
    public PointSET() {
        points = new TreeSet<>();
    }

    // Is the set empty?
    public boolean isEmpty() {
        return points.size() == 0;
    }

    // Number of points in the set
    public int size() {
        return points.size();
    }

    // Add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        // Check argument
        isNull(p);
        points.add(p);
    }

    // Does the set contain point p?
    public boolean contains(Point2D p) {
        // Check argument
        isNull(p);
        return points.contains(p);
    }

    // Draw all points to standard draw
    public void draw() {
        StdDraw.clear();
        StdDraw.setPenColor(StdDraw.RED);
        StdDraw.setPenRadius(0.01);

        // Draw each point in the tree set
        for (Point2D p : points) {
            p.draw();
        }
    }

    // All points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        // Check argument
        isNull(rect);
        // Initialize queue
        Queue<Point2D> queue = new Queue<>();

        for (Point2D p2D : points) {
            // Add to queue if point in rectangle matches point in tree set
            if (rect.contains(p2D)) {
                queue.enqueue(p2D);
            }
        }

        return queue;
    }

    // A nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        // Check argument
        isNull(p);
        // If there is nothing in the tree set return null
        if (isEmpty()) {
            return null;
        }

        Point2D closest = null;
        // Loop through points to find the nearest neighbor in the set to point p
        for (Point2D p2D : points) {
            if (closest == null || p.distanceSquaredTo(p) < closest.distanceSquaredTo(p)) {
                closest = p2D;
            }
        }

        return closest;
    }

    // Unit testing of the methods (optional)
    public static void main(String[] args) {
    }
}
