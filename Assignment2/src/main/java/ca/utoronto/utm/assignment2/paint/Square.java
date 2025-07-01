package ca.utoronto.utm.assignment2.paint;

import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

import static java.lang.Math.abs;

/**
 * Captures a square drawing.
 * This includes the coordinates as well as the properties of the square,
 * (E.g. line thickness, fill, color).
 */
public class Square extends Rectangle{
    protected Point firstPoint;
    protected Point secondPoint;      // Release point from the user
    protected Point modifiedSP;       // Modified secondPoint (with width = height)
    protected boolean fill;

    /**
     * Constructor for class Square.
     * Create a square with the specified properties.
     *
     * @param firstPoint the point user pressed
     * @param secondPoint the point user released
     * @param color the color of square
     * @param fill whether the square is filled with color
     * @param lineThickness the line thickness of the square outline
     */
    public Square(Point firstPoint, Point secondPoint, Color color, Boolean fill, double lineThickness) {
        super(firstPoint, secondPoint, color, fill, lineThickness);
        this.name = "Square";
        this.firstPoint = firstPoint;
        this.secondPoint = secondPoint;
        this.fill = fill;
    }

    /**
     * Calculate the coordinates of the second point of the square (modifiedSP) based on secondPoint.
     * Modifying the second point guarantees that the width and height of the square is equal.
     * The calculated Point will be stored in this.modifiedSP.
     */
    private void calModifiedSP() {
        double width = abs(secondPoint.x - firstPoint.x);
        double height = abs(secondPoint.y - firstPoint.y);
        if (width >= height) {
            if (secondPoint.y >= firstPoint.y){
                this.modifiedSP = new Point(secondPoint.x, firstPoint.y + width);    // Draw to the right
            } else {
                this.modifiedSP = new Point(secondPoint.x, firstPoint.y - width);    // Draw to the left
            }
        } else {
            if (secondPoint.x >= firstPoint.x){
                this.modifiedSP = new Point(firstPoint.x + height, secondPoint.y);    // Draw to the right
            } else {
                this.modifiedSP = new Point(firstPoint.x - height, secondPoint.y);    // Draw to the left
            }
        }
    }

    /**
     * Calculate the coordinates of the top left and bottom right corner of the square.
     * And store the points in this.topLeft and this.bottomRight.
     */
    @Override
    public void calTopLeftBottomRight() {
        double x1 = firstPoint.x;
        double y1 = firstPoint.y;
        double x2 = modifiedSP.x;
        double y2 = modifiedSP.y;

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
     * Update the modifiedSP, topLeft, and bottomRight accordingly.
     *
     * @param sp The second point of the drawing square.
     */
    @Override
    public void setSecondPoint(Point sp) {
        this.secondPoint = sp;
        this.calModifiedSP();
        this.calTopLeftBottomRight();
    }

    /**
     *
     * @return T or F, whether the shape should be filled (T) or not (F)
     */
    public Boolean getfill() {
        return fill;
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
        System.out.println("Added Square");
    }

    /**
     * Creates and returns a copy of the square.
     *
     * @return a cloned instance of the square
     */
    @Override
    public Drawable clone() {
        Rectangle clone = new Rectangle(firstPoint.clone(), secondPoint.clone(), color, fill, lineThickness);
        clone.topLeft = this.topLeft.clone();
        clone.bottomRight = this.bottomRight.clone();
        clone.setSecondPoint(modifiedSP.clone());
        return clone;
    }

    /**
     * Move the square according to the user's actions.
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
        firstPoint.x += diffX;
        firstPoint.y += diffY;
        secondPoint.x += diffX;
        secondPoint.y += diffY;
        modifiedSP.x += diffX;
        modifiedSP.y += diffY;
        topLeft.x += diffX;
        topLeft.y += diffY;
        bottomRight.x += diffX;
        bottomRight.y += diffY;
    }
}
