package ca.utoronto.utm.assignment2.paint;

import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

import static java.lang.Math.abs;

/**
 * Captures an Oval drawing.
 * This includes the coordinates as well as the properties of the oval,
 * (E.g. line thickness, fill, color).
 */
public class Oval extends Shape{
    protected Point centre;
    protected double radiusX;
    protected double radiusY;
    protected boolean fill;

    /**
     * Constructor for class Oval.
     * Create an oval with the specified properties.
     *
     * @param centre the centre of the oval ( the initial point that the user starts drawing )
     * @param radiusX the distance from the centre to the rightmost/leftmost point of the oval
     * @param radiusY the distance from the centre to the uppermost/bottommost point of the oval
     * @param color the color of oval
     * @param fill whether the oval is filled with color
     * @param lineThickness the line thickness of the oval outline
     */
    public Oval(Point centre, double radiusX, double radiusY, Color color, Boolean fill, double lineThickness){
        super(color, lineThickness, "Oval");
        this.centre = centre;
        this.radiusX = radiusX;
        this.radiusY = radiusY;
        this.fill = fill;
    }

    /**
     * @return the Point centre of the oval.
     */
    public Point getCentre() {
        return centre;
    }

    /**
     * @return True or False, whether the shape should be filled (T) or not (F)
     */
    public Boolean getfill() {
        return fill;
    }

    /**
     * @return the distance from the centre to the rightmost/leftmost point of the oval
     */
    public double getRadiusX() {
        return radiusX;
    }

    /**
     * @return the distance from the centre to the uppermost/bottommost point of the oval
     */
    public double getRadiusY() {
        return radiusY;
    }

    /**
     * @param radius the new distance from the centre to the rightmost/leftmost point of the oval
     */
    private void setRadiusX(double radius) {
        this.radiusX = radius;
    }

    /**
     * @param radius the new distance from the centre to the uppermost/bottommost point of the oval
     */
    private void setRadiusY(double radius) {
        this.radiusY = radius;
    }

    /**
     * @return the Point of the top left corner of the rectangle that the oval can perfectly fit in
     */
    public Point getTopLeft() {
        return topLeft;
    }

    /**
     * @param mouseEvent The mouse pressing event from user.
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
     * Update the radiusX, radiusY, topLeft, bottomRight of the oval according to the drag action.
     *
     * @param mouseEvent The mouse dragging event from user.
     */
    @Override
    public void dragged(MouseEvent mouseEvent) {
        // Difference between the starting and ending point of the drag
        this.setRadiusX(abs(this.getCentre().x - mouseEvent.getX()));
        this.setRadiusY(abs(this.getCentre().y - mouseEvent.getY()));
        this.setTopLeft(new Point(this.getCentre().x - this.getRadiusX(), this.getCentre().y - this.getRadiusY()));
        this.setBottomRight(new Point(this.topLeft.x + (this.getRadiusX() * 2), this.topLeft.y + (this.getRadiusY() * 2)));
    }

    /**
     * Called when the mouse button is released.
     *
     * @param mouseEvent the MouseEvent associated with the release action
     */
    @Override
    public void released(MouseEvent mouseEvent) {
        System.out.println("Added Oval");
    }

    /**
     * Creates and returns a copy of the oval.
     *
     * @return a cloned instance of the oval
     */
    @Override
    public Drawable clone() {
        Oval clone = new Oval(centre.clone(), radiusX, radiusY, color, fill, lineThickness);
        clone.topLeft = topLeft.clone();
        clone.bottomRight = bottomRight.clone();
        return clone;
    }

    /**
     * Move the oval according to the user's actions.
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
        centre.x += diffX;
        centre.y += diffY;
        topLeft.x += diffX;
        topLeft.y += diffY;
        bottomRight.x += diffX;
        bottomRight.y += diffY;
    }
}
