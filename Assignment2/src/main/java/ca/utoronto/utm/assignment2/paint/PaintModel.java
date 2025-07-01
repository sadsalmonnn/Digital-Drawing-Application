package ca.utoronto.utm.assignment2.paint;

import java.io.File;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Observable;

import javafx.event.EventType;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

/**
 * The application model.
 */
public class PaintModel extends Observable {

    // the current shape that is being drawn
    private Drawable currentDrawable;

    // the history of actions (redo, undo, eraser and drawables)
    protected ArrayList<Action> ActionHistory = new ArrayList<Action>();

    // the selected actions
    private ArrayList<Action> sActionHistory = new ArrayList<Action>();

    // the copied actions and selector
    private ArrayList<Action> cActionHistory = new ArrayList<Action>();
    private Drawable cSelector;

    // the last point that the mouse clicked/drag on a selector
    private Point lastPoint = new Point(0, 0);

    private Boolean fill = true;    // the status of fill

    private Color color = Color.BLACK;  // the color

    private double lineThickness = 1;   // the thickness of lines/words

    private Image background;   // the background image

    /**
     * set fill status
     * @param fill the status of fill that will be set to
     */
    public void setFill(Boolean fill) {
        this.fill = fill;
    }

    /**
     * get the status of fill
     * @return the boolean representation of fill status
     */
    public boolean getFill() {
        return this.fill;
    }

    /**
     * set color
     * @param color the color that will be set to
     */
    public void setColor(Color color) {
        this.color = color;
    }

    /**
     * set the thickness of lines
     * @param lt the thickness of lines that will be set to
     */
    public void setLineThickness(double lt) {
        this.lineThickness = lt;
    }

    /**
     * Performs an undo operation and update the drawing board.
     */
    public void undo() {
        this.removeSelector();
        this.ActionHistory.add(new Undo());
        System.out.println("Undo Successful");
        this.setChanged();
        this.notifyObservers();
    }

    /**
     * Restores the most recent action that was undone by the user,
     * effectively reversing an "Undo" operation.
     * Nothing will be done if the last action is not "Undo",
     * e.g. Draw, Draw, Undo, Redo (effective), Redo (ineffective)
     * e.g. Draw, Undo, Draw, Redo (ineffective)
     */
    public void redo() {
        this.removeSelector();
        if (!ActionHistory.isEmpty() && ActionHistory.getLast().getName().equals("Undo")) {
            System.out.println("Redo Successful");
            ActionHistory.removeLast();
            this.setChanged();
            this.notifyObservers();
        } else {
            System.out.println("Redo Failed");
        }
    }

    /**
     * change the color of the selected shapes
     * @param color the color that the selected shapes will be set to
     */
    public void setSelectedShapeColor(Color color) {
        if (!sActionHistory.isEmpty()) {
            for (Action action : sActionHistory) {
                ((Drawable) action).setColor(color);
            }
            this.setChanged();
            this.notifyObservers();
            System.out.println("Set selected shape color to " + color);
        }
    }

    /**
     * set the line thickness of the selected shapes
     * @param thickness the line thickness that the selected shapes will be set to
     */
    public void setSelectedShapeThickness(double thickness) {
        if (!sActionHistory.isEmpty()) {
            for (Action action : sActionHistory) {
                ((Drawable) action).setLineThickness(thickness);
            }
            this.setChanged();
            this.notifyObservers();
            System.out.println("Set selected shape thickness to " + thickness);
        }
    }

    /**
     * the selection function
     * puts the qualified shape into an arraylist
     */
    public void select() {
        if (((Selector) currentDrawable).getfill()) {   // if current selector hasn't ended
            Selector selector = (Selector) currentDrawable;
            ArrayList<Action> drawingActions = this.getDrawings();  // get the actual drawings drawn on screen
            sActionHistory.clear(); // clear the previous selected objects

            for (Action action : drawingActions) {
                // everything drawn on screen but selector && is covered by selector
                if (!action.getName().equals("Selector") && ((Drawable) action).isSelect(selector)) {
                    sActionHistory.add(action); // record shape
                }
            }
        } else {
            System.out.println("Nothing selected");
        }
    }

    /**
     * removing the current selector
     */
    public void removeSelector() {
        if (this.currentDrawable != null && this.currentDrawable.getName().equals("Selector")) {
            this.ActionHistory.removeLast(); // remove "Selector" from history
            this.currentDrawable = null;    // reset currentDrawable
            sActionHistory.clear(); // clear selected shapes
            this.setChanged();
            this.notifyObservers();
            System.out.println("Removed Selector");
        }
    }

    /**
     * Ends the current polyline.
     * @param mode the current mode
     */
    public void endPolyline(String mode) {
        if (!mode.equals("Polyline") && !ActionHistory.isEmpty()
                && this.ActionHistory.getLast().getName().equals("Polyline")) {
            ((Polyline) this.ActionHistory.getLast()).endPolyline();    // end last recorded polyline
            this.currentDrawable = null;    // reset currentDrawable
            this.setChanged();
            this.notifyObservers();
        }
    }

    /**
     * Erasing the top most drawable under mouseEvent
     * @param mouseEvent the record of user's mouse pointer when user triggers this method
     */
    public void erase(MouseEvent mouseEvent) {
        Drawable removed = null;    // initialize the record of removed drawing
        ArrayList<Action> drawingActions = this.getDrawings();  // get the list of actual drawings on screen

        for (int index = 0; index < drawingActions.size(); index++) {
            Action action = drawingActions.get(index);
                if (((Drawable) action).isIn(mouseEvent)) { // if shape is under the record of mouse pointer
                    removed = (Drawable) action;    // record the latest qualified Drawable object
                }
        }
        if (removed != null) {  // if something should be removed
            this.ActionHistory.add(new Eraser(removed));    // record eraser event
            this.setChanged();
            this.notifyObservers();
        }
    }

    /**
     * determine the appropriate move for the incoming mouse event
     * @param mouseEventType the type of incoming mouse event
     * @param mouseEvent the incoming mouse event
     * @param mode the current drawing mode
     */
    public void drawShape(EventType<MouseEvent> mouseEventType, MouseEvent mouseEvent, String mode) {
        if (mouseEvent.isMiddleButtonDown()) {  // if the middle button was clicked
            // if current drawing is a polyline and it hasn't end
            if (mode.equals("Polyline") && !ActionHistory.isEmpty()
                    && this.ActionHistory.getLast().getName().equals("Polyline")
                    && !((Polyline) ActionHistory.getLast()).hadEnd()) {
                ((Polyline) this.ActionHistory.getLast()).endPolyline();    // calls method to end polyline
                this.setChanged();
                this.notifyObservers();
            }
        } else if (mouseEvent.isSecondaryButtonDown()) {    // if the right button was clicked
            // if current drawing is a polyline and it hasn't end
            if (mode.equals("Polyline") && !ActionHistory.isEmpty()
                    && this.ActionHistory.getLast().getName().equals("Polyline")
                    && !((Polyline) ActionHistory.getLast()).hadEnd()) {
                ((Polyline) this.ActionHistory.getLast()).connectFirstPoint();
                this.setChanged();
                this.notifyObservers();
            }
        } else if (mouseEventType.equals(MouseEvent.MOUSE_PRESSED)) {   // if the left button was clicked
            // remove selector if current drawing is a selector
            if (this.currentDrawable != null && this.currentDrawable.getName().equals("Selector")
                    && !this.currentDrawable.isIn(mouseEvent)) {
                this.removeSelector();
            }
            // call method erase if current mode is Eraser
            if (mode.equals("Eraser") && !this.ActionHistory.isEmpty()) {
                this.erase(mouseEvent);
            }
            // initial lastPoint if current drawable is a selector and mouse event is on top of selector
            else if (currentDrawable != null && currentDrawable.getName().equals("Selector")
                    && this.currentDrawable.isIn(mouseEvent)) {
                lastPoint = new Point(mouseEvent.getX(), mouseEvent.getY());
                System.out.println("Set initial dragging point");

            }
            // create drawable object if mode is not Polyline
            else if (!Objects.equals(mode, "Polyline")) {
                // use factory to create drawable object
                this.currentDrawable = DrawableFactory.createDrawable(mode,
                        this.getArgs(mode, mouseEvent, this.color));
                // record drawable object
                this.recordDrawable(this.currentDrawable);
                System.out.println("Started " + mode);
            }
            // Enters polyline mode
            else {
                // initial new polyline if no polyline was drawn or all drawn polyline had end
                if (this.ActionHistory.isEmpty()
                        || !this.ActionHistory.getLast().getName().equals("Polyline")
                        || ((Polyline) this.ActionHistory.getLast()).hadEnd()) {
                    this.currentDrawable = DrawableFactory.createDrawable(mode,
                            this.getArgs(mode, mouseEvent, this.color));
                    this.recordDrawable(this.currentDrawable);
                    this.currentDrawable.pressed(mouseEvent);
                    System.out.println("Starting Point Added for Polyline");
                } else {    // record new point to polyline
                    this.currentDrawable.pressed(mouseEvent);
                    System.out.println("Added a Point for Polyline");
                    this.setChanged();
                    this.notifyObservers();
                }
                System.out.println("Point count: "
                        + ((Polyline) this.ActionHistory.getLast()).getPoints().size());
            }
        } else if (mouseEventType.equals(MouseEvent.MOUSE_MOVED)) { // if the mouse was moved
            // if current drawing is a polyline and it hasn't end
            if (mode.equals("Polyline") && !ActionHistory.isEmpty()
                    && this.ActionHistory.getLast().getName().equals("Polyline")
                    && !((Polyline) this.ActionHistory.getLast()).hadEnd()) {
                ((Polyline) this.ActionHistory.getLast()).moved(mouseEvent);    // polyline feedback
                this.setChanged();
                this.notifyObservers();
            }
        } else if (mouseEventType.equals(MouseEvent.MOUSE_DRAGGED)) {   // if mouse was dragged
            if (this.currentDrawable != null) { // if is drawing something
                // if mouse is dragginga selector
                if (currentDrawable.getName().equals("Selector")
                        && !((Selector) currentDrawable).getfill()
                        && this.currentDrawable.isIn(mouseEvent)) {
                    for (Action action : sActionHistory) {  // move everything in sActionHistory
                        ((Drawable) action).moveTo(lastPoint.x, lastPoint.y, mouseEvent.getX(),
                                mouseEvent.getY());
                    }
                    currentDrawable.moveTo(lastPoint.x, lastPoint.y, mouseEvent.getX(),
                            mouseEvent.getY()); // move selector as well
                    lastPoint = new Point(mouseEvent.getX(), mouseEvent.getY());    // update last point
                    this.setChanged();
                    this.notifyObservers();
                } else {    // else shape feedback
                    this.currentDrawable.dragged(mouseEvent);
                    this.setChanged();
                    this.notifyObservers();
                }
            }
        } else if (mouseEventType.equals(MouseEvent.MOUSE_RELEASED)) {  // if mouse button/s was released
            if (this.currentDrawable != null) { // if is drawing something
                this.currentDrawable.released(mouseEvent);  // call Drawable to perform release action
                if (currentDrawable.getName().equals("Selector")) { // call method select if current drawing is a selector
                    this.select();
                    ((Selector) currentDrawable).endSelector(); // end current selector
                }
                // if current drawing is a not polyline and not textbox
                else if (!currentDrawable.getName().equals("Polyline")
                        && !currentDrawable.getName().equals("Text")) {
                    this.currentDrawable = null;    // reset currentDrawable
                }
                this.setChanged();
                this.notifyObservers();
            }
        }
    }

    /**
     * Eyedropper tool that changes the current color of the PaintModel when the
     * mouse is pressed. The color is determined by the pixel at the mouse's
     * position on the canvas.
     * 
     * @param mouseEventType The type of mouse event that triggered this call.
     * @param mouseEvent     The MouseEvent that triggered this call.
     * @param pixelReader    The PixelReader to read the color from.
     * @return The current color of the PaintModel.
     */
    public Color eyedropped(EventType<MouseEvent> mouseEventType, MouseEvent mouseEvent, PixelReader pixelReader) {
        if (mouseEventType.equals(MouseEvent.MOUSE_PRESSED)) {
            Color color = pixelReader.getColor((int) mouseEvent.getX(), (int) mouseEvent.getY());
            this.color = color;
            System.out.println("Eyedropped: " + color);
        }
        return this.color;
    }

    /**
     * Handles the event when the user types a key while the current drawable is a
     * Textbox.
     * 
     * @param key the key that was typed
     */
    public void typedKey(String key) {
        if (this.currentDrawable != null && this.currentDrawable.getName().equals("Text")) {
            System.out.println("Typed " + key);
            ((Textbox) this.currentDrawable).typedKey(key);
            this.setChanged();
            this.notifyObservers();
        }
    }

    /**
     *
     * @param mode current drawing mode
     * @param mouseEvent current mouseEvent
     * @param color current color
     * @return an array of arguments for creations in shape factory
     */
    private Object[] getArgs(String mode, MouseEvent mouseEvent, Color color) {
        Object[] args = null;
        switch (mode) {
            case "Oval":
                args = new Object[] { new Point(mouseEvent.getX(), mouseEvent.getY()), 0, 0, color,
                        this.fill, this.lineThickness };
                break;
            case "Circle":
                args = new Object[] { new Point(mouseEvent.getX(), mouseEvent.getY()), 0, color,
                        this.fill, this.lineThickness };
                break;
            case "Rectangle", "Square", "Triangle":
                args = new Object[] { new Point(mouseEvent.getX(), mouseEvent.getY()), new Point(0, 0),
                        color, this.fill, this.lineThickness };
                break;
            case "Squiggle", "Polyline":
                args = new Object[] { color, this.lineThickness };
                break;
            case "Text":
                args = new Object[] { new Point(mouseEvent.getX(), mouseEvent.getY()), color,
                        this.lineThickness };
                break;
            case "Selector":
                args = new Object[] { new Point(mouseEvent.getX(), mouseEvent.getY()), new Point(0, 0),
                        this };
                break;
        }
        return args;
    }

    /**
     *
     * @return an arraylist of actual drawings on screen
     */
    public ArrayList<Action> getDrawings() {
        // Remove all Undoes and undone actions
        ArrayList<Action> undoneActions = new ArrayList<Action>();
        ArrayList<Integer> redundantUndoes = new ArrayList<Integer>();
        int index = 0;
        for (Action action : this.ActionHistory) {
            if (action.getName().equals("Undo") && !undoneActions.isEmpty()) {
                undoneActions.removeLast();
            } else if (!action.getName().equals("Undo")) {
                undoneActions.add(action);
            } else {
                redundantUndoes.add(index);
            }
            index++;
        }
        for (int i = redundantUndoes.size() - 1; i >= 0; i--) {
            this.ActionHistory.remove((int) redundantUndoes.get(i));
        }

        // Remove all Erasers and erased shapes, only drawables will be left
        ArrayList<Action> drawingActions = new ArrayList<Action>();
        for (Action action : undoneActions) {
            if (action.getName().equals("Eraser")) {
                Eraser eraser = (Eraser) action;
                drawingActions.remove(eraser.getErasedDrawable());
            } else {
                drawingActions.add(action);
            }
        }
        return drawingActions;
    }

    /**
     * Record drawable to history.
     * @param drawable a drawable object
     */
    private void recordDrawable(Drawable drawable) {
        this.ActionHistory.add(drawable);
    }

    /**
     * Resizes the canvas to the given width.
     * 
     * @param paintCanvas the canvas to be resized
     * @param newWidth    the new width of the canvas
     */
    public void resizeCanvasWidth(Canvas paintCanvas, Double newWidth) {
        paintCanvas.setWidth(newWidth.doubleValue());
        this.setChanged();
        this.notifyObservers();
    }

    /**
     * Resizes the canvas to the given height.
     * 
     * @param paintCanvas the canvas to be resized
     * @param newHeight   the new height of the canvas
     */
    public void resizeCanvasHeight(Canvas paintCanvas, Double newHeight) {
        paintCanvas.setHeight(newHeight.doubleValue());
        this.setChanged();
        this.notifyObservers();
    }

    /**
     * Clears the action history and notifies all observers that the model
     * has changed, effectively resetting the canvas to a new state.
     */
    public void filenew() {
        this.ActionHistory.clear();
        this.setChanged();
        this.notifyObservers();
    }

    /**
     * Opens a file as the canvas background. The file is read as an image and
     * the model is updated to reflect the new background.
     * 
     * @param file the file containing the image to be used as the background
     */
    public void fileopen(File file) {
        Image image = new Image(file.toURI().toString());
        this.background = image;
        this.setChanged();
        this.notifyObservers();
    }

    /**
     * Cut method.
     */
    public void editcut() {
        // if current drawing is a selector and something was selected
        if (currentDrawable != null && currentDrawable.getName().equals("Selector")
                && !sActionHistory.isEmpty()) {
            cSelector = currentDrawable;    // record current selector
            cActionHistory.clear(); // clear last copied
            for (Action action : sActionHistory) {
                cActionHistory.add(((Drawable) action).clone());    // record a clone of action
                ActionHistory.remove(action);   // remove action from history
            }
            this.removeSelector();  // remove selector
            System.out.println("Cut Successful");
            this.setChanged();
            this.notifyObservers();
        } else {
            System.out.println("Cut Unsuccessful");
        }
    }

    /**
     * Copy method.
     */
    public void editcopy() {
        // if current drawing is a selector and something was selected
        if (currentDrawable != null && currentDrawable.getName().equals("Selector")
                && !sActionHistory.isEmpty()) {
            cSelector = currentDrawable;    // record current selector
            cActionHistory.clear(); // clear previous copied objects
            for (Action action : sActionHistory) {
                cActionHistory.add(((Drawable) action).clone());    // record a clone of action
            }
            System.out.println("Copy Successful");
        } else {
            System.out.println("Copy Unsuccessful");
        }
    }

    /**
     * Paste method.
     */
    public void editpaste() {
        // if currently drawing a polyline, end the polyline and call this method again
        if (currentDrawable != null && currentDrawable.getName().equals("Polyline")
                && !((Polyline) currentDrawable).hadEnd()) {
            ((Polyline) this.ActionHistory.getLast()).endPolyline();
            this.editpaste();
        }
        // if something was copied
        else if (!cActionHistory.isEmpty() && cSelector != null) {
            this.removeSelector();  // remove current selector
            ActionHistory.addAll(cActionHistory); // add all copied to history
            currentDrawable = cSelector.clone();    // clone current selector as current drawing
            ActionHistory.add(currentDrawable); // add current selector to history
            sActionHistory = (ArrayList<Action>) cActionHistory.clone();    // selected set to whatever copied
            cActionHistory.clear(); // clear whatever was copied
            for (Action action : sActionHistory) {  // re-clone everything that was pasted
                cActionHistory.add(((Drawable) action).clone());
            }
            System.out.println(sActionHistory);
            System.out.println("Added Selector, selected pasted");
            this.setChanged();
            this.notifyObservers();
            System.out.println("Paste Successful");
        } else {
            System.out.println("Paste Unsuccessful");
        }
    }

    /**
     * @return the background image of the canvas, null if there is no background
     */
    public Image getCanvasBackground() {
        return this.background;
    }
}
