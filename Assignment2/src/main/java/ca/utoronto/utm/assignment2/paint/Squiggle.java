package ca.utoronto.utm.assignment2.paint;

import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

import java.util.ArrayList;

/**
 * Captures a squiggle drawing.
 * This includes the points of the squiggle as well as its properties,
 * (E.g. line thickness, color).
 */
public class Squiggle extends Line {

    /**
     * Constructor for class Squiggle
     * Create a squiggle with the specified color, line thickness, and name.
     *
     * @param color the color of the squiggle
     * @param lineThickness the thickness of the squiggle
     */
    public Squiggle(Color color, double lineThickness) {
        super(color, lineThickness, "Squiggle");
    }

    /**
     * Called when the mouse button is pressed.
     *
     * @param mouseEvent the MouseEvent associated with the press action
     */
    @Override
    public void pressed(MouseEvent mouseEvent) {;}

    /**
     * Called when the mouse is moved.
     *
     * @param mouseEvent the MouseEvent associated with the movement action
     */
    @Override
    public void moved(MouseEvent mouseEvent) {;}

    /**
     * Called when the mouse is dragged.
     *
     * @param mouseEvent the MouseEvent associated with the drag action
     */
    @Override
    public void dragged(MouseEvent mouseEvent) {
        this.addPoint(new Point(mouseEvent.getX(), mouseEvent.getY()));
    }

    /**
     * Called when the mouse button is released.
     *
     * @param mouseEvent the MouseEvent associated with the release action
     */
    @Override
    public void released(MouseEvent mouseEvent) {
        this.addPoint(new Point(mouseEvent.getX(), mouseEvent.getY()));
        System.out.println("Added Squiggle");
    }

    /**
     * Checks if a given point, represented by a MouseEvent, is on the squiggle.
     *
     * @param mouseEvent the MouseEvent containing the point to check
     * @return true if the point is on the squiggle, false otherwise
     */
    @Override
    public boolean isIn(MouseEvent mouseEvent) {
        for (Point point : this.points) {
            if (mouseEvent.getX() == point.x && mouseEvent.getY() == point.y) {
                return true;
            }
        } return false;
    }

    /**
     * Creates and returns a copy of the squiggle.
     *
     * @return a cloned instance of the squiggle
     */
    @Override
    public Drawable clone() {
        Squiggle clone = new Squiggle(this.color, this.lineThickness);
        for (Point point : this.points) {
            clone.addPoint(point.clone());
        }
        return clone;
    }

    /**
     * Determines if the squiggle is selected based on the given selector.
     *
     * @param selector the selector of the current selecting action
     * @return true if the squiggle is selected, false otherwise
     */
    @Override
    public boolean isSelect(Selector selector) {
        for (Point point : this.points) {
            if (point.x >= selector.topLeft.x && point.x <= selector.bottomRight.x && point.y >= selector.topLeft.y && point.y <= selector.bottomRight.y) {
                return true;
            }
        } return false;
    }

    /**
     *
     * @return an arrayList of all the points that form the squiggle
     */
    public ArrayList<Point> getPoints() {
        return points;
    }
}
