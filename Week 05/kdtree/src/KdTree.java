import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

public class KdTree {
    // Initialize boundaries
    private double yMin = 0.0;
    private double xMin = 0.0;
    private double yMax = 1.0;
    private double xMax = 1.0;

    // Initialize tree size
    private int sz = 0;

    // Create Node
    private Node n;

    private class Node {
        // Define properties of each node
        private Point2D p;
        private RectHV rect;
        private Node left, right;

        // Instantiate Node
        private Node(Point2D p2DVal, RectHV rectVal) {
            left = null;
            right = null;
            p = p2DVal;
            rect = rectVal;
        }
    }

    // Throw null if argument is null
    private void isNull(Object arg) {
        if (arg == null) {
            throw new java.lang.IllegalArgumentException();
        }
    }

    // Helper function which compares two points and returns the result (positive, negative, or zero real number)
    private int compare(int d, Point2D a, Point2D b) {
        // Even numbers for dimension, even and odd dimensions support
        if (d % 2 != 0) {
            // y-coordinates only
            int result = new Double(a.y()).compareTo(new Double(b.y()));

            if (result == 0) {
                return new Double(a.x()).compareTo(new Double(b.x()));
            } else {
                return result;
            }
        } else {
            // x-coordinates only
            int result = new Double(a.x()).compareTo(new Double(b.x()));

            if (result == 0) {
                return new Double(a.y()).compareTo(new Double(b.y()));
            } else {
                return result;
            }
        }
    }

    // Helper function which inserts a node into a tree
    private Node insertHelper(int d, Node n, Point2D p2DVal, double xmin, double ymin, double xmax, double ymax) {
        if (n == null) {
            // Add new node and increase the size of the tree by one
            sz++;
            return new Node(p2DVal, new RectHV(xmin, ymin, xmax, ymax));
        }

        // Result of compare
        int compareVal = compare(d, p2DVal, n.p);

        // Only perform one of the following operations
        // Use compare result to determine where to insert in the tree
        if (compareVal > 0) {
            // Insert right
            if (d % 2 != 0) {
                n.right = insertHelper(d + 1, n.right, p2DVal, xmin, n.p.y(), xmax, ymax);
            } else {
                n.right = insertHelper(d + 1, n.right, p2DVal, n.p.x(), ymin, xmax, ymax);
            }
        } else if (compareVal < 0) {
            // Insert left
            if (d % 2 != 0) {
                n.left = insertHelper(d + 1, n.left, p2DVal, xmin, ymin, xmax, n.p.y());
            } else {
                n.left = insertHelper(d + 1, n.left, p2DVal, xmin, ymin, n.p.x(), ymax);
            }
        }

        return n;
    }

    // Helper function for contains
    private Point2D containsHelper(int d, Node n, Point2D p2D) {
        while (n != null) {
            int compareVal = compare(d, p2D, n.p);

            if (compareVal > 0) {
                // Return right point
                return containsHelper(d + 1, n.right, p2D);
            } else if (compareVal < 0) {
                // Return left point
                return containsHelper(d + 1, n.left, p2D);
            } else {
                // Otherwise return current point
                return n.p;
            }
        }
        // Otherwise return null
        return null;
    }

    // Helper function to draw lines
    private void drawHelper(int d, Node n) {
        if (n != null) {
            // Keep going left
            drawHelper(d + 1, n.left);

            StdDraw.setPenRadius();
            if (d % 2 != 0) {
                // Horizontal splits
                StdDraw.setPenColor(StdDraw.BLUE);
                StdDraw.line(n.rect.xmin(), n.p.y(), n.rect.xmax(), n.p.y());
            } else {
                // Vertical splits
                StdDraw.setPenColor(StdDraw.RED);
                StdDraw.line(n.p.x(), n.rect.ymin(), n.p.x(), n.rect.ymax());
            }
            StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.setPenRadius(0.01);
            n.p.draw();

            // Now go right
            drawHelper(d + 1, n.right);
        }
    }

    // Helper function to sum ranges
    private void rangeHelper(Node n, RectHV rect, Queue<Point2D> queue) {
        if (n != null && rect.intersects(n.rect)) {
            if (rect.contains(n.p)) {
                // Add to queue if both conditions are met
                queue.enqueue(n.p);
            }
            // Recursively add to queue
            rangeHelper(n.left, rect, queue);
            rangeHelper(n.right, rect, queue);
        }
    }

    // Helper function to find nearest neighbor
    private Point2D nearestHelper(Node n, Point2D p, Point2D closest) {
        if (n != null) {
            // Closest must be the same as the point in this case
            if (closest == null) {
                closest = n.p;
            }

            // Compare closest to input and the current point to input
            if (closest.distanceSquaredTo(p) >= n.rect.distanceSquaredTo(p)) {
                // Check current point distance versus closes point distance
                if (n.p.distanceSquaredTo(p) < closest.distanceSquaredTo(p)) {
                    closest = n.p;
                }

                // Where to next?
                if (n.left != null && n.left.rect.contains(p)) {
                    closest = nearestHelper(n.left, p, closest);
                    closest = nearestHelper(n.right, p, closest);
                } else {
                    closest = nearestHelper(n.right, p, closest);
                    closest = nearestHelper(n.left, p, closest);

                }
            }
        }
        // Return the closest point
        return closest;
    }

    // Construct an empty set of points
    public KdTree() {
        sz = 0;
    }

    // Is the set empty?
    public boolean isEmpty() {
        return sz == 0;
    }

    // Number of points in the set
    public int size() {
        return sz;
    }

    // Add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        // Check argument
        isNull(p);
        n = insertHelper(0, n, p, xMin, yMin, xMax, yMax);
    }

    // Does the set contain point p?
    public boolean contains(Point2D p) {
        // Check argument
        isNull(p);
        return containsHelper(0, n, p) != null;
    }

    // Draw all points to standard draw
    public void draw() {
        // Clears the screen to white
        StdDraw.clear();
        drawHelper(0, n);
    }

    // All points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        Queue<Point2D> queue = new Queue<>();
        // Check argument
        isNull(rect);
        rangeHelper(n, rect, queue);

        return queue;
    }

    // A nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        // Check argument
        isNull(p);
        if (isEmpty()) {
            return null;
        }
        Point2D value = null;
        value = nearestHelper(n, p, value);

        return value;
    }

    // Unit testing of the methods (optional)
    public static void main(String[] args) {
    }
}
