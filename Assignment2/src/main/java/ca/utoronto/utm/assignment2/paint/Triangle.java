package ca.utoronto.utm.assignment2.paint;

import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

/**
 * Captures a triangle drawing.
 * This includes the coordinates as well as the properties of the triangle,
 * (E.g. line thickness, fill, color).
 */
public class Triangle extends Shape {
    protected Point firstPoint;
    protected Point secondPoint;
    protected Point thirdPoint;
    protected boolean fill;

    /**
     * Constructor for class Triangle.
     * Create a triangle with the specified properties.
     *
     * @param firstPoint the point user pressed
     * @param secondPoint the point user released
     * @param color the color of triangle
     * @param fill whether the triangle is filled with color
     * @param lineThickness the line thickness of the triangle outline
     */
    public Triangle(Point firstPoint, Point secondPoint, Color color, Boolean fill, double lineThickness) {
        super(color, lineThickness, "Triangle");
        this.firstPoint = firstPoint;
        this.secondPoint = secondPoint;
        this.thirdPoint = new Point(0, 0);
        this.fill = fill;
    }

    /**
     * Calculate the coordinates of the third point based on the first and second point.
     * So that an isosceles triangle will be drawn, with firstPoint as the tip.
     * Then store the point in this.thirdPoint.
     */
    public void calThirdPoint() {
        this.thirdPoint.y = secondPoint.y;
        this.thirdPoint.x = 2 * firstPoint.x - secondPoint.x;
        this.calTopLeftBottomRight();
    }

    /**
     *
     * @param secondPoint The second point of the drawing triangle.
     */
    public void setSecondPoint(Point secondPoint) {
        this.secondPoint = secondPoint;
        this.calThirdPoint();
    }

    /**
     * Calculate the coordinates of the top left and bottom right corner of the
     * smallest rectangle that this triangle can fit into.
     * Then store the points in this.topLeft and this.bottomRight.
     */
    public void calTopLeftBottomRight() {
        double x1 = firstPoint.x;
        double y1 = firstPoint.y;
        double x2 = secondPoint.x;
        double y2 = secondPoint.y;

        if (x1 < x2) {
            this.topLeft.x = thirdPoint.x;
            this.bottomRight.x = x2;
        } else {
            this.topLeft.x = x2;
            this.bottomRight.x = thirdPoint.x;
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
     *
     * @return True or False, whether the shape should be filled (T) or not (F)
     */
    public Boolean getfill() {
        return fill;
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
        this.setSecondPoint(new Point(mouseEvent.getX(), mouseEvent.getY()));
    }

    /**
     * Called when the mouse button is released.
     *
     * @param mouseEvent the MouseEvent associated with the release action
     */
    @Override
    public void released(MouseEvent mouseEvent) {
        System.out.println("Added Triangle");
    }

    /**
     * Creates and returns a copy of the triangle.
     *
     * @return a cloned instance of the triangle
     */
    @Override
    public Drawable clone() {
        Triangle clone = new Triangle(firstPoint.clone(), secondPoint.clone(), color, fill, lineThickness);
        clone.thirdPoint = this.thirdPoint.clone();
        return clone;
    }

    /**
     * Move the triangle according to the user's actions.
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
        thirdPoint.x += diffX;
        thirdPoint.y += diffY;
    }
}
