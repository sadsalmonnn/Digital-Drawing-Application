package ca.utoronto.utm.assignment2.paint;

import javafx.scene.paint.Color;

import java.util.ArrayList;

/**
 * An abstract class representing a line made up of multiple points.
 * This class extends the Drawable class and provides methods for
 * managing the points that form the line, as well as moving the line.
 */
public abstract class Line extends Drawable {
    protected ArrayList<Point> points = new ArrayList<Point>();

    /**
     * Constructor for class Line.
     * Create a line with the specified color, line thickness, and name.
     *
     * @param color the color of the line
     * @param lineThickness the thickness of the line
     * @param name the name of the line
     */
    public Line(Color color, double lineThickness, String name) {
        super(color, lineThickness, name);
    }

    /**
     *
     * @return the list of points that make up the line.
     */
    public ArrayList<Point> getPoints() {
        return points;
    }

    /**
     *
     * @param point the point to add to the line
     */
    public void addPoint(Point point) {
        points.add(point);
    }

    /**
     * Move the line according to the user's actions.
     *
     * @param startX the initial x-coordinates of the move
     * @param startY the initial y-coordinates of the move
     * @param endX the ending x-coordinates of the move
     * @param endY the ending y-coordinates of the move
     */
    @Override
    public void moveTo(double startX, double startY, double endX, double endY) {
        double diffX = endX - startX;
        double diffY = endY - startY;
        for (Point p : points) {
            p.x += diffX;
            p.y += diffY;
        }
    }
}
