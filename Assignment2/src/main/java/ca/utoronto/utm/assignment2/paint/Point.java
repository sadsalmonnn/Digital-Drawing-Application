package ca.utoronto.utm.assignment2.paint;

/**
 * Capture a point on the drawing canvas using its x-y coordinates.
 */
public class Point {
        protected double x, y; // Available to our package

        /**
         * Constructor for class Point.
         * Create a Point with the specified coordinates.
         *
         * @param x the x-coordinate of the point
         * @param y the y-coordinate of the point
         */
        Point(double x, double y) {
                this.x = x;
                this.y = y;
        }

        /**
         * Clone the point.
         *
         * @return a new point with the same x-y coordinates as this point.
         */
        public Point clone() {
                return new Point(x, y);
        }
}
