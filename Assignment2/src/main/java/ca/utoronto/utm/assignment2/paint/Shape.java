package ca.utoronto.utm.assignment2.paint;

import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

/**
 * An abstract class representing a basic shape with indicated top-left and bottom-right corner points.
 * This class extends the Drawable class.
 */
public abstract class Shape extends Drawable {
    protected Point topLeft = new Point(0, 0);
    protected Point bottomRight = new Point(0, 0);

    /**
     * Constructor for class Shape.
     * Create a shape object with the specified color, line thickness, and name.
     *
     * @param color the color of the Shape object
     * @param lineThickness the thickness of the line used to draw the Shape object
     * @param name the name of the Shape object
     */
    public Shape(Color color, double lineThickness, String name) {
        super(color, lineThickness, name);
    }

    /**
     *
     * @return the top-left corner of the shape
     */
    public Point getTopLeft() {
        return topLeft;
    }

    /**
     * Sets the top-left corner point of the shape.
     *
     * @param topLeft the new top-left corner for the shape
     */
    public void setTopLeft(Point topLeft) {
        this.topLeft = topLeft;
    }

    /**
     * Sets the bottom-right corner point of the shape.
     *
     * @param bottomRight the new top-left corner {@code Point} for the shape
     */
    public void setBottomRight(Point bottomRight) {
        this.bottomRight = bottomRight;
    }

    /**
     * Determines if the shape is selected based on the given selector.
     *
     * @param selector the selector of the current selecting action
     * @return true if the shape is selected, false otherwise
     */
    @Override
    public boolean isSelect(Selector selector) {
        return !(this.bottomRight.x < selector.topLeft.x || // shape is to the left of selector
                this.topLeft.x > selector.bottomRight.x || // shape is to the right of selector
                this.bottomRight.y < selector.topLeft.y || // shape is above selector
                this.topLeft.y > selector.bottomRight.y); // shape is below selector
    }

    /**
     * Checks if a given point, represented by a MouseEvent, is within the bounds of the shape.
     *
     * @param mouseEvent the MouseEvent containing the point to check
     * @return true if the point is within bounds, false otherwise
     */
    @Override
    public boolean isIn(MouseEvent mouseEvent) {
        if (mouseEvent.getX() >= this.topLeft.x && mouseEvent.getX() <= this.bottomRight.x
                && mouseEvent.getY() >= this.topLeft.y && mouseEvent.getY() <= this.bottomRight.y) {
            return true;
        }
        return false;
    }
}
