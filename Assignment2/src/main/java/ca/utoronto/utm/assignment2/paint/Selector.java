package ca.utoronto.utm.assignment2.paint;

import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

/**
 * The rectangular selector.
 */
public class Selector extends Rectangle {

    /**
     * Class constructor.
     * @param firstPoint the point user clicked on
     * @param secondPoint the point user released on
     */
    public Selector(Point firstPoint, Point secondPoint) {
            super(firstPoint, secondPoint, Color.rgb(173, 216, 230, 0.75), true, 1);
            this.name = "Selector";
    }

    /**
     * Ends the selector.
     * fill == true - selector hasn't ended
     * fill == false - selector has ended
     */
    public void endSelector() {
        fill = false;
    }

    /**
     *
     * @return a clone of this selector
     */
    @Override
    public Drawable clone() {
        Selector clone = new Selector(firstPoint.clone(), secondPoint.clone());
        clone.topLeft = this.topLeft.clone();
        clone.bottomRight = this.bottomRight.clone();
        clone.fill = this.fill;
        return clone;
    }

    /**
     * The actions needed when user released mouse click.
     * @param mouseEvent the MouseEvent associated with the release action
     */
    @Override
    public void released(MouseEvent mouseEvent) {
        if (this.fill) {
            System.out.println("Added Selector");
        }
    }
}
