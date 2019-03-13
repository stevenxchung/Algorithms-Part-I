public class PointSET {
    // Construct an empty set of points
    public PointSET() {}

    // Is the set empty?
    public boolean isEmpty() {}

    // Add the point to the set (if it is not already in the set)
    public void insert(Point 2D p) {}

    // Does the set contain point p?
    public boolean contains(Point2D p) {}

    // Draw all points to standard draw
    public void draw() {}

    // All points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {}

    // A nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {}

    // Unit testing of the methods (optional)
    public static void main(String[] args) {}
}
