package ca.utoronto.utm.assignment2.paint;

import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

/**
 * Captures a rectangle drawing.
 * This includes the coordinates as well as the properties of the rectangle,
 * (E.g. line thickness, fill, color).
 */
public class Rectangle extends Shape {
    protected Point firstPoint;
    protected Point secondPoint;
    protected boolean fill;

    /**
     * Constructor for class Rectangle.
     * Create a rectangle with the specified properties.
     *
     * @param firstPoint    the point user pressed
     * @param secondPoint   the point user released
     * @param color         the color of rectangle
     * @param fill          whether the rectangle is filled with color
     * @param lineThickness the line thickness of the rectangle outline
     */
    public Rectangle(Point firstPoint, Point secondPoint, Color color, Boolean fill, double lineThickness) {
        super(color, lineThickness, "Rectangle");
        this.firstPoint = firstPoint;
        this.secondPoint = secondPoint;
        this.fill = fill;
    }

    /**
     * Calculate the coordinates of the top left and bottom right corner of the
     * rectangle.
     * Then store the points in this.topLeft and this.bottomRight.
     */
    public void calTopLeftBottomRight() {
        double x1 = firstPoint.x;
        double y1 = firstPoint.y;
        double x2 = secondPoint.x;
        double y2 = secondPoint.y;

        if (x1 < x2) {
            this.topLeft.x = x1;
            this.bottomRight.x = x2;
        } else {
            this.topLeft.x = x2;
            this.bottomRight.x = x1;
        }
        if (y1 < y2) {
            this.topLeft.y = y1;
            this.bottomRight.y = y2;
        } else {
            this.topLeft.y = y2;
            this.bottomRight.y = y1;
        }
    }

    /**
     * Set to the new secondPoint.
     * Update topLeft and bottomRight accordingly.
     *
     * @param secondPoint The second point of the drawing rectangle.
     */
    public void setSecondPoint(Point secondPoint) {
        this.secondPoint = secondPoint;
        this.calTopLeftBottomRight();
    }

    /**
     *
     * @return a Point with x = width of rectangle, and y = height of rectangle.
     */
    public Point getWidthHeight() {
        return new Point(this.bottomRight.x - this.topLeft.x, this.bottomRight.y - this.topLeft.y);
    }

    /**
     *
     * @return True or False, whether the shape should be filled (T) or not (F)
     */
    public Boolean getfill() {
        return fill;
    }

    /**
     *
     * @param mouseEvent The mouse pressing event from user.
     */
    @Override
    public void pressed(MouseEvent mouseEvent) {
        ;
    }

    /**
     * Called when the mouse is moved.
     *
     * @param mouseEvent the MouseEvent associated with the movement action
     */
    @Override
    public void moved(MouseEvent mouseEvent) {
        ;
    }

    /**
     * Called when the mouse is dragged.
     *
     * @param mouseEvent the MouseEvent associated with the drag action
     */
    @Override
    public void dragged(MouseEvent mouseEvent) {
        this.setSecondPoint(new Point(mouseEvent.getX(), mouseEvent.getY()));
    }

    /**
     * Called when the mouse button is released.
     *
     * @param mouseEvent the MouseEvent associated with the release action
     */
    @Override
    public void released(MouseEvent mouseEvent) {
        System.out.println("Added Rectangle");
    }

    /**
     * Creates and returns a copy of the rectangle.
     *
     * @return a cloned instance of the rectangle
     */
    @Override
    public Drawable clone() {
        Rectangle clone = new Rectangle(firstPoint.clone(), secondPoint.clone(), color, fill, lineThickness);
        clone.topLeft = this.topLeft.clone();
        clone.bottomRight = this.bottomRight.clone();
        return clone;
    }

    /**
     * Move the rectangle according to the user's actions.
     *
     * @param startX the initial x-coordinates of the move
     * @param startY the initial y-coordinates of the move
     * @param endX   the ending x-coordinates of the move
     * @param endY   the ending y-coordinates of the move
     */
    @Override
    public void moveTo(double startX, double startY, double endX, double endY) {
        double diffX = endX - startX;
        double diffY = endY - startY;
        firstPoint.x += diffX;
        firstPoint.y += diffY;
        secondPoint.x += diffX;
        secondPoint.y += diffY;
        topLeft.x += diffX;
        topLeft.y += diffY;
        bottomRight.x += diffX;
        bottomRight.y += diffY;
    }
}
