package ca.utoronto.utm.assignment2.paint;

import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

/**
 * An abstract class representing a drawable object that can be drawn with specific color and line thickness.
 * This class extends the Action class and provides basic properties and methods for handling the mouse events,
 * as well as changing the color and line thickness of the drawable.
 */
public abstract class Drawable extends Action {
    protected Color color;
    protected double lineThickness;

    /**
     * Constructor for class Drawable.
     * Create a drawable object with the specified color, line thickness, and name.
     *
     * @param color the color of the drawable object
     * @param lineThickness the thickness of the line used to draw the object
     * @param name the name of the drawable object
     */
    public Drawable(Color color, double lineThickness, String name) {
        super(name);
        this.color = color;
        this.lineThickness = lineThickness;
    }

    /**
     *
     * @return the color of the drawable object
     */
    public Color getColor() {
        return color;
    }

    /**
     *
     * @return the line thickness of the drawable object
     */
    public double getLineThickness() {
        return lineThickness;
    }

    /**
     * Sets the color of the drawable object.
     *
     * @param color the new color for the drawable object
     */
    public void setColor(Color color) {
        this.color = color;
    }

    /**
     * Sets the line thickness of the drawable object.
     *
     * @param lineThickness the new line thickness for the drawable object
     */
    public void setLineThickness(double lineThickness) {
        this.lineThickness = lineThickness;
    }

    /**
     * Called when the mouse button is pressed.
     *
     * @param mouseEvent the MouseEvent associated with the press action
     */
    public abstract void pressed(MouseEvent mouseEvent);

    /**
     * Called when the mouse is moved.
     *
     * @param mouseEvent the MouseEvent associated with the movement action
     */
    public abstract void moved(MouseEvent mouseEvent);

    /**
     * Called when the mouse is dragged.
     *
     * @param mouseEvent the MouseEvent associated with the drag action
     */
    public abstract void dragged(MouseEvent mouseEvent);

    /**
     * Called when the mouse button is released.
     *
     * @param mouseEvent the MouseEvent associated with the release action
     */
    public abstract void released(MouseEvent mouseEvent);

    /**
     * Determines if an item is selected based on the given selector.
     *
     * @param selector the selector of the current selecting action
     * @return true if the item is selected, false otherwise
     */
    public abstract boolean isSelect(Selector selector);


    /**
     * Checks if a given point, represented by a MouseEvent, is within the bounds of the drawable object.
     *
     * @param mouseEvent the MouseEvent containing the point to check
     * @return true if the point is within bounds, false otherwise
     */
    public abstract boolean isIn(MouseEvent mouseEvent);

    /**
     * Creates and returns a copy of the drawable object.
     *
     * @return a cloned instance of the drawable object
     */
    public abstract Drawable clone();

    /**
     * Move the drawable object according to the user's actions.
     *
     * @param startX the initial x-coordinates of the move
     * @param startY the initial y-coordinates of the move
     * @param endX the ending x-coordinates of the move
     * @param endY the ending y-coordinates of the move
     */
    public abstract void moveTo(double startX, double startY, double endX, double endY);
}
