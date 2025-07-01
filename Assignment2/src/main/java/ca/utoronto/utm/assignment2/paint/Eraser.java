package ca.utoronto.utm.assignment2.paint;

/**
 * Captures an Erasing action.
 */
public class Eraser extends Action {
    protected Drawable erasedDrawable;

    /**
     * Constructor for class Eraser.
     * Create an Erasing action that removes a drawable object.
     *
     * @param drawable the erased drawable object from this action
     */
    public Eraser(Drawable drawable) {
        super("Eraser");
        this.erasedDrawable = drawable;
    }

    /**
     *
     * @return the drawable object that has been erased.
     */
    public Drawable getErasedDrawable() {
        return erasedDrawable;
    }

}
