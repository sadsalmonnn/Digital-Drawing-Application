package ca.utoronto.utm.assignment2.paint;

import javafx.scene.paint.Color;

/**
 * A factory design pattern implementation.
 * Used to organize the creation of all drawable object.
 * */
public class DrawableFactory {

    /**
     *
     * @param name the name of the object that will be created
     * @param args an array of objects that contains the information for the initialization of drawable object
     * @return the drawable object that is initialized
     */
    public static Drawable createDrawable(String name, Object[] args) {
        Drawable shape = null;
        switch (name) {
            case "Oval":
                shape = new Oval((Point) args[0], (Integer) args[1], (Integer) args[2], (Color) args[3],
                        (Boolean) args[4], (double) args[5]);
                break;
            case "Circle":
                shape = new Circle((Point) args[0], (Integer) args[1], (Color) args[2], (Boolean) args[3],
                        (double) args[4]);
                break;
            case "Rectangle":
                shape = new Rectangle((Point) args[0], (Point) args[1], (Color) args[2], (Boolean) args[3],
                        (double) args[4]);
                break;
            case "Square":
                shape = new Square((Point) args[0], (Point) args[1], (Color) args[2], (Boolean) args[3],
                        (double) args[4]);
                break;
            case "Squiggle":
                shape = new Squiggle((Color) args[0], (double) args[1]);
                break;
            case "Polyline":
                shape = new Polyline((Color) args[0], (double) args[1]);
                break;
            case "Triangle":
                shape = new Triangle((Point) args[0], (Point) args[1], (Color) args[2], (Boolean) args[3], (double) args[4]);
                break;
            case "Selector":
                shape = new Selector((Point) args[0], (Point) args[1]);
                break;
            case "Text":
                shape = new Textbox((Point) args[0], (Color) args[1], (double) args[2]);
                break;
        }
        return shape;
    }
}
