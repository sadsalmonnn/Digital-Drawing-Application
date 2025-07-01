package ca.utoronto.utm.assignment2.paint;

/**
 * Abstract class that captures an action done by user.
 * This includes the drawing, undo/redo, selecting, copy and pasting actions.
 */
public abstract class Action {
    protected String name;

    /**
     * Constructor for class Action
     *
     * @param name Name of the action done
     */
    public Action (String name) {
        this.name = name;
    }

    /**
     *
     * @return name of the action
     */
    public String getName() {
        return name;
    }
}
