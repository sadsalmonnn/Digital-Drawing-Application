package ca.utoronto.utm.assignment2.paint;

import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 * Represents a Text box shape that can be drawn on a canvas.
 * This includes the coordinates as well as the properties of the text box,
 * (e.g. line thickness, fill, color).
 * The class provides methods to handle text input, calculate coordinates for
 * positioning, and clone the object.
 * 
 * This class extends the Shape class and implements basic event handling
 * methods.
 */
public class Textbox extends Shape {
    private String text = "";
    private Point bottomLeft;

    /**
     * Constructor for class Textbox.
     * Create a Textbox with the specified properties.
     *
     * @param bottomLeft    the point user pressed
     * @param color         the color of text
     * @param lineThickness the line thickness of the text
     */
    public Textbox(Point bottomLeft, Color color, double lineThickness) {
        super(color, lineThickness, "Text");
        this.bottomLeft = bottomLeft;
        updatecoordinates();
    }

    /**
     * @return the bottom left point of the text box
     */
    public Point getBottomLeft() {
        return this.bottomLeft;
    }

    /**
     * @return the current text of the text box
     */
    public String getText() {
        return text;
    }

    /**
     * Sets the text of the text box to the given text.
     * 
     * @param text the new text of the text box
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * Appends the given character to the current text of the text box. Updates
     * the coordinates based on the new text.
     * 
     * @param key the character to append to the text
     */
    public void typedKey(String key) {
        this.text += key;
        updatecoordinates();
    }

    /**
     * Sets the bottom right point of the text box to the given point.
     * 
     * @param bottomRight the new bottom right point of the text box
     */
    public void setBottomRight(Point bottomRight) {
        this.bottomRight = bottomRight;
    }

    /**
     * @return the bottom right point of the text box
     */
    public Point getBottomRight() {
        return this.bottomRight;
    }

    /**
     *
     * @param mouseEvent The mouse pressing event from user.
     */
    public void pressed(MouseEvent mouseEvent) {
        return;
    }

    /**
     * Called when the mouse is moved.
     *
     * @param mouseEvent the MouseEvent associated with the movement action
     */
    public void moved(MouseEvent mouseEvent) {
        return;
    }

    /**
     * Called when the mouse is dragged.
     *
     * @param mouseEvent the MouseEvent associated with the drag action
     */
    public void dragged(MouseEvent mouseEvent) {
        return;
    }

    /**
     * Called when the mouse button is released.
     *
     * @param mouseEvent the MouseEvent associated with the release action
     */
    public void released(MouseEvent mouseEvent) {
        return;
    }

    /**
     * Sets the line thickness for the text box and updates the coordinates
     * based on the new line thickness.
     *
     * @param lineThickness the new line thickness of the text box
     */
    @Override
    public void setLineThickness(double lineThickness) {
        this.lineThickness = lineThickness;
        updatecoordinates();
    }

    /**
     * Updates the coordinates of the text box based on the line thickness and
     * the bottom left corner. This method will be called whenever the line
     * thickness is changed.
     */
    public void updatecoordinates() {
        Text textNode = new Text(getText());
        textNode.setFont(new Font("Arial", lineThickness * 10));

        textNode.applyCss();
        double textWidth = textNode.getBoundsInParent().getWidth();
        double textHeight = textNode.getBoundsInParent().getHeight() - textNode.getBoundsInParent().getHeight() / 3;

        double bottomRightX = (bottomLeft).x + textWidth;
        double bottomRightY = bottomLeft.y;

        setBottomRight(new Point(bottomRightX, bottomRightY));

        double topLeftX = bottomLeft.x;
        double topLeftY = bottomLeft.y - textHeight;
        Point topLeft = new Point(topLeftX, topLeftY);

        this.topLeft = topLeft;
    }

    /**
     * Creates and returns a copy of the Textbox.
     *
     * @return a cloned instance of the Textbox
     */
    public Drawable clone() {
        Textbox clone = new Textbox(this.bottomLeft, this.color, this.lineThickness);
        clone.text = this.text;
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
        System.out.println("Before:" + topLeft.x + " " + topLeft.y);
        this.topLeft.x += diffX;
        this.topLeft.y += diffY;
        this.bottomRight.x += diffX;
        this.bottomRight.y += diffY;

        this.bottomLeft.x = this.topLeft.x;
        this.bottomLeft.y = this.bottomRight.y;
        System.out.println("Before:" + topLeft.x + " " + topLeft.y);
    }
}
