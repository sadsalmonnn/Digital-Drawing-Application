package ca.utoronto.utm.assignment2.paint;

import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

import java.util.ArrayList;

/**
 * Captures a polyline drawing.
 * This includes the points of the polyline as well as its properties,
 * (E.g. line thickness, color).
 */
public class Polyline extends Line {
    protected Point currentPoint = null;
    protected int lastOrgInd = 0;
    protected boolean end = false;

    /**
     * Constructor for class Polyline
     * Create a polyline with the specified color, line thickness, and name.
     *
     * @param color         the color of the polyline
     * @param lineThickness the thickness of the polyline
     */
    public Polyline(Color color, double lineThickness) {
        super(color, lineThickness, "Polyline");
    }

    /**
     * Called when the mouse button is pressed.
     *
     * @param mouseEvent the MouseEvent associated with the press action
     */
    @Override
    public void pressed(MouseEvent mouseEvent) {
        this.points.add(new Point(mouseEvent.getX(), mouseEvent.getY()));
    }

    /**
     * Called when the mouse is moved.
     *
     * @param mouseEvent the MouseEvent associated with the movement action
     */
    @Override
    public void moved(MouseEvent mouseEvent) {
        this.currentPoint = new Point(mouseEvent.getX(), mouseEvent.getY());
    }

    /**
     * Called when the mouse is dragged.
     *
     * @param mouseEvent the MouseEvent associated with the drag action
     */
    @Override
    public void dragged(MouseEvent mouseEvent) {
        ;
    }

    /**
     * Called when the mouse button is released.
     *
     * @param mouseEvent the MouseEvent associated with the release action
     */
    @Override
    public void released(MouseEvent mouseEvent) {
        ;
    }

    /**
     * Checks if a given point, represented by a MouseEvent, is on the polyline.
     *
     * @param mouseEvent the MouseEvent containing the point to check
     * @return true if the point is on the polyline, false otherwise
     */
    @Override
    public boolean isIn(MouseEvent mouseEvent) {
        for (int i = 0; i < this.points.size() - 1; i++) {
            Point a = this.points.get(i);
            Point b = this.points.get(i + 1);
            Point c = new Point(mouseEvent.getX(), mouseEvent.getY());
            if ((c.y - a.y) / (b.y - a.y) - 0.001 <= (c.x - a.x) / (b.x - a.x)
                    && (c.x - a.x) / (b.x - a.x) <= (c.y - a.y) / (b.y - a.y) + 0.001) {
                return true;
            }
        }
        return false;
    }

    /**
     * Creates and returns a copy of the polyline.
     *
     * @return a cloned instance of the polyline
     */
    @Override
    public Drawable clone() {
        Polyline clone = new Polyline(this.color, this.lineThickness);
        for (Point point : this.points) {
            clone.addPoint(point.clone());
        }
        if (currentPoint != null) {
            clone.currentPoint = this.currentPoint.clone();
        }
        clone.lastOrgInd = this.lastOrgInd;
        clone.end = this.end;
        return clone;
    }

    /**
     * Determines if the polyline is selected based on the given selector.
     *
     * @param selector the selector of the current selecting action
     * @return true if the polyline is selected, false otherwise
     */
    @Override
    public boolean isSelect(Selector selector) {
        for (Point point : this.points) {
            if (point.x >= selector.topLeft.x && point.x <= selector.bottomRight.x && point.y >= selector.topLeft.y
                    && point.y <= selector.bottomRight.y) {
                return true;
            }
        }
        return false;
    }

    /**
     * Link the last point on the polyline to the first point.
     */
    public void connectFirstPoint() {
        if (!(this.points.getLast().x == this.points.getLast().x && this.points.getLast().y == this.points.getFirst().y)
                && this.points.size() - this.lastOrgInd >= 3) {
            this.points.add(this.points.getFirst().clone());
            this.lastOrgInd = this.points.size() - 1;
            System.out.println("Linked last point to origin");
            System.out.println("Point count: " + this.points.size());
        }
    }

    /**
     *
     * @return an arrayList of all the points that form the polyline
     */
    public ArrayList<Point> getPoints() {
        return points;
    }

    /**
     *
     * @return the most recent point of the polyline construction
     */
    public Point getCurrentPoint() {
        return currentPoint;
    }

    /**
     * Set the polyline construction as finished
     */
    public void endPolyline() {
        this.end = true;
        this.currentPoint = null;
    }

    /**
     *
     * @return true if the polyline has ended construction, false otherwise
     */
    public boolean hadEnd() {
        return end;
    }
}
