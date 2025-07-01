package ca.utoronto.utm.assignment2.paint;

import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.Optional;

import javax.imageio.ImageIO;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.layout.StackPane;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;

/**
 * FXMLController is a controller class for handling user interactions with the
 * application interface. It manages the canvas, color panel, tools and much
 * more.
 * 
 * This class implements the observer pattern to receive updates from the model,
 * PaintModel, and updates the UI accordingly.
 * 
 * This class includes the mode, the model, the graphics context, file related
 * properties, and FXML elements.
 * 
 */
public class FXMLController implements Observer {
    private String mode = "default";
    private PaintModel model;
    private GraphicsContext g2d;

    @FXML
    private Canvas paintCanvas;
    @FXML
    private ToggleButton filloutline;
    @FXML
    private Slider linethicknessslider;
    @FXML
    private ImageView ovalimg;
    @FXML
    private ImageView rectangleimg;
    @FXML
    private ImageView circleimg;
    @FXML
    private ImageView squareimg;
    @FXML
    private ImageView triangleimg;
    @FXML
    private ImageView polylineimg;
    @FXML
    private VBox colorsection;
    @FXML
    private Button primarycolor;
    @FXML
    private GridPane colorpanel;

    private ImageView[] shapeicons;
    private String[] shapeiconpath = new String[6];
    private String[] shapeiconfilledpath = new String[6];
    private String filesave;
    private boolean changed = false;

    /**
     * Constructor for class FXMLController.
     * Instantiates the controller, with the model, and adds itself as an observer.
     */
    public FXMLController() {
        this.model = new PaintModel();
        this.model.addObserver(this);
    }

    /**
     * Initializes the FXML view with event handlers and listeners, setting up
     * specific elements in the view such as canvas, color panel, tools, and shapes
     * icons.
     */
    @FXML
    private void initialize() {
        g2d = paintCanvas.getGraphicsContext2D();
        filloutline.setSelected(true);

        // Resizes the canvas, when the window is resized
        HBox parent = (HBox) paintCanvas.getParent();
        parent.widthProperty().addListener((obs, oldWidth, newWidth) -> {
            this.model.resizeCanvasWidth(paintCanvas, newWidth.doubleValue() - 4);
        });
        parent.heightProperty().addListener((obs, oldHeight, newHeight) -> {
            this.model.resizeCanvasHeight(paintCanvas, newHeight.doubleValue() - 4);
        });

        initializeLineThickness();
        initializeShapeIcons();
        initializeColorIcons();
    }

    /**
     * Initializes the line thickness slider with some major and minor tick
     * properties. Adds a listener to the value property of the slider that calls
     * linethicknesshandle when the value is a whole number
     */
    private void initializeLineThickness() {
        linethicknessslider.setMin(1);
        linethicknessslider.setMax(10.00000000001);
        linethicknessslider.setValue(1);
        linethicknessslider.setShowTickLabels(true);
        linethicknessslider.setShowTickMarks(true);
        linethicknessslider.setMajorTickUnit(1);
        linethicknessslider.setMinorTickCount(0);
        linethicknessslider.setBlockIncrement(1);
        linethicknessslider.snapToTicksProperty().set(true);

        linethicknessslider.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.doubleValue() % 1 == 0) {
                linethicknesshandle(newValue.doubleValue());
            }
        });
    }

    /**
     * Initializes the shape icons with their respective icons and icon paths.
     */
    private void initializeShapeIcons() {
        String shapes[] = { "Oval", "Rectangle", "Circle", "Square", "Triangle", "Polyline" };
        this.shapeicons = new ImageView[] { ovalimg, rectangleimg, circleimg, squareimg, triangleimg, polylineimg };

        for (int i = 0; i < shapeicons.length; i++) {
            shapeiconpath[i] = "/ca/utoronto/utm/assignment2/img/icons/" + shapes[i] + ".png";
            shapeiconfilledpath[i] = "/ca/utoronto/utm/assignment2/img/icons/" + shapes[i] + "Fill.png";
        }
    }

    /**
     * Initializes the color panel by creating a 4x6 grid of small buttons with
     * different colors. The primary color button is selected by default.
     */
    private void initializeColorIcons() {
        primarycolor.getStyleClass().add("clickedbutton");

        String[][] colors = {
                { "#000000", "#8B0000", "#006400", "#00008B", "#FFA500", "#4B0082" },
                { "#7F7F7F", "#CC3333", "#338833", "#3366CC", "#FFBF00", "#550099" },
                { "#C3C3C3", "#FF6666", "#66CC66", "#66B2FF", "#FFD700", "#6A00CC" },
                { "#FFFFFF", "#FF9999", "#99FF99", "#99CCFF", "#FFFF00", "#8000FF" }
        };

        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 6; col++) {
                Button button = new Button();
                button.setPrefSize(20, 20);
                button.setMinSize(20, 20);
                button.setMaxSize(20, 20);
                button.getStyleClass().add("colorpanelbuttons");
                button.setStyle("-fx-background-color: " + colors[row][col]);
                button.setText(colors[row][col]);
                button.setOnAction(this::colorhandle);
                colorpanel.add(button, col, row);
            }
        }
    }

    /**
     * Handles mouse events on the canvas for drawing or using tools.
     * 
     * @param mouseEvent The mouse event triggered by user interaction.
     */
    @FXML
    private void canvashandle(MouseEvent mouseEvent) {
        EventType<MouseEvent> mouseEventType = (EventType<MouseEvent>) mouseEvent.getEventType();
        if (this.mode.compareTo("Eyedropper") == 0) {
            PixelReader pixelReader = paintCanvas.snapshot(null, null).getPixelReader();
            Color color = this.model.eyedropped(mouseEventType, mouseEvent, pixelReader);

            primarycolor.setStyle("-fx-background-color: " + colorToHex(color));
            primarycolor.setText(colorToHex(color));
        } else if (!this.mode.equals("default")) {
            this.model.drawShape(mouseEventType, mouseEvent, mode);
        }
    }

    /**
     * Converts a Color object to a hex string.
     * 
     * @param color The Color object to convert.
     * @return The hex string representation of the color.
     */
    private String colorToHex(Color color) {
        // Get the red, green, and blue components (values are between 0 and 1)
        int red = (int) (color.getRed() * 255);
        int green = (int) (color.getGreen() * 255);
        int blue = (int) (color.getBlue() * 255);

        // Format the RGB values into a hex string
        return String.format("#%02X%02X%02X", red, green, blue);
    }

    /**
     * Handles the event when a color button is clicked, and updates the model to
     * use the selected color.
     * 
     * @param event The ActionEvent, color button, that triggered the method call.
     */
    @FXML
    private void colorhandle(ActionEvent event) {
        Button originbutton = (Button) event.getSource();

        if (originbutton.getParent() instanceof GridPane) {

            updateButtonStyles(originbutton.getParent().getParent(), (Node) event.getSource());
        } else {
            updateButtonStyles(originbutton.getParent(), (Node) event.getSource());
        }
        Color color = Color.valueOf(originbutton.getText().toUpperCase());
        this.model.setColor(color);
        System.out.println(originbutton.getText());
        this.model.setSelectedShapeColor(color);
    }

    /**
     * Handles the event when a mode button is clicked, and updates the model to
     * use the selected mode. If the current mode is Polyline and the selected
     * mode is not Polyline, or if the current mode is Selector and the selected
     * mode is not Selector, then the model's endPolyline or removeSelector methods
     * are called, respectively. The mode is then updated to the selected mode, with
     * the usage of styling.
     * 
     * @param event The ActionEvent, tool or shape buttons, that triggered the
     *              method call.
     */
    @FXML
    private void modehandle(ActionEvent event) {
        Button originbutton = (Button) event.getSource();
        String command = originbutton.getText();
        if (this.mode.compareTo("Polyline") == 0 && command.compareTo("Polyline") != 0) {
            this.model.endPolyline(command);
        } else if (this.mode.compareTo("Selector") == 0 && command.compareTo("Selector") != 0) {
            this.model.removeSelector();
        }
        this.mode = command;
        updateButtonStyles((Node) originbutton.getScene().getRoot(), (Node) event.getSource());
        System.out.println(this.mode);
    }

    /**
     * Recursively updates the style class of buttons in the given subtree to not
     * have the "clickedbutton" style class, and adds the "clickedbutton" style
     * class to the given node. Used to update the style of buttons when a mode or
     * color is selected.
     * 
     * @param rootNode  The root node of the subtree to update.
     * @param givenNode The node to add the "clickedbutton" style class to.
     */
    private void updateButtonStyles(Node rootNode, Node givenNode) {
        if (rootNode == null) {
            return;
        }

        if (rootNode == colorsection) {
            return;
        }
        rootNode.getStyleClass().remove("clickedbutton");

        if (rootNode == givenNode) {
            rootNode.getStyleClass().add("clickedbutton");
        }

        if (rootNode instanceof Parent) {
            Parent parent = (Parent) rootNode;
            for (Node child : parent.getChildrenUnmodifiable()) {
                updateButtonStyles(child, givenNode);
            }
        }
    }

    /**
     * Handles the event when the fill style button is clicked, and updates the
     * model to use the selected fill style. Toggles the fill style on and off, and
     * updates the shape icons and button styles accordingly.
     * 
     * @param event The ActionEvent, fill style button, that triggered the method
     *              call.
     */
    @FXML
    private void fillstylehandle(ActionEvent event) {
        Node originbutton = (Node) event.getSource();
        StackPane root = (StackPane) originbutton.getParent();

        ToggleButton togglebutton = (ToggleButton) root.getChildren().get(0);
        Button button = (Button) root.getChildren().get(1);
        Label label = (Label) togglebutton.getGraphic();

        if (!this.model.getFill()) {
            this.model.setFill(true);
            for (int i = 0; i < shapeicons.length; i++) {
                shapeicons[i].setImage(new Image(getClass().getResource(shapeiconfilledpath[i]).toString()));
            }

            button.getStyleClass().remove("buttonfilloff");
            label.getStyleClass().remove("filllabeloff");
            label.setText("Fill");

        } else {
            this.model.setFill(false);
            for (int i = 0; i < shapeicons.length; i++) {
                shapeicons[i].setImage(new Image(getClass().getResource(shapeiconpath[i]).toString()));
            }
            button.getStyleClass().add("buttonfilloff");
            label.getStyleClass().add("filllabeloff");
            label.setText("");
        }
    }

    /**
     * Handles the event when the undo or redo button is clicked, and updates the
     * model accordingly.
     * 
     * @param event The ActionEvent, undo or redo button, that triggered the method
     *              call.
     */
    @FXML
    private void undoredohandle(ActionEvent event) {
        Button originbutton = (Button) event.getSource();
        if (originbutton.getText().equals("Undo")) {
            this.model.undo();
        } else {
            this.model.redo();
        }
    }

    /**
     * Handles the event when the line thickness slider is moved, and updates the
     * model accordingly.
     * 
     * @param newValue The new value of the line thickness slider.
     */
    @FXML
    private void linethicknesshandle(double newValue) {
        System.out.println("Selected Line Thickness: " + (int) newValue);
        this.model.setLineThickness(newValue);
        this.model.setSelectedShapeThickness(newValue);
    }

    /**
     * Handles the event when the file menu items are clicked, and updates the model
     * accordingly. If the "New" menu item is clicked, it prompts the user to save
     * the current canvas before opening a new one. If the "Open" menu item is
     * clicked, it opens a file dialog to select a png file to open. If the "Save"
     * or "Save As" menu items are clicked, it saves the current canvas to a file.
     * 
     * @param event The ActionEvent, file menu items, that triggered the method
     *              call.
     */
    @FXML
    private void filehandler(ActionEvent event) {
        MenuItem originbutton = (MenuItem) event.getSource();
        String command = originbutton.getText();

        if (command.equals("New")) {

            // Prompt the user to save the current canvas before opening a new one, in a new
            // window
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Save");
            alert.setHeaderText("You are about to open a new canvas!");
            alert.setContentText("Do you want to save your current changes before opening a new one?");
            ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
            ButtonType cancelButtonType = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
            ButtonType nosaveButtonType = new ButtonType("Don't Save", ButtonBar.ButtonData.NO);
            alert.getButtonTypes().setAll(saveButtonType, nosaveButtonType, cancelButtonType);

            Optional<ButtonType> result = alert.showAndWait();

            if (this.changed && !this.model.ActionHistory.isEmpty() && result.get() == saveButtonType) {
                if (saveFile()) {
                    this.model.filenew();
                }
            } else if (result.get() == cancelButtonType) {
                return;
            } else if (result.get() == nosaveButtonType) {
                this.model.filenew();
            }
        } else if (command.equals("Open")) {
            FileChooser fileChooser = new FileChooser();
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("png files (*.png)", "*.png");
            FileChooser.ExtensionFilter extFilter2 = new FileChooser.ExtensionFilter("jpg files (*.jpg)", "*.jpg");
            FileChooser.ExtensionFilter extFilter3 = new FileChooser.ExtensionFilter("jpg files (*.jpeg)", "*.jpeg");
            fileChooser.getExtensionFilters().add(extFilter);
            fileChooser.getExtensionFilters().add(extFilter2);
            fileChooser.getExtensionFilters().add(extFilter3);

            Stage stage = (Stage) this.paintCanvas.getScene().getWindow();

            File file = fileChooser.showOpenDialog(stage);

            if (file == null) {
                System.out.println("File is null");
                return;
            }

            this.model.fileopen(file);
        } else if (command.equals("Save")) {
            saveFile();
        } else if (command.equals("Save As")) {
            this.filesave = null;
            saveFile();
        }
    }

    /**
     * Saves the current state of the canvas to a file. If a file path is already
     * specified, it uses that path; otherwise, it opens a file chooser to select
     * the save location and file type (PNG, JPG, JPEG). The canvas is captured as
     * an image and written to the selected file. Returns true if the save is
     * successful, false if it is canceled or an error occurs.
     *
     * @return true if the file is successfully saved, false otherwise
     */
    private boolean saveFile() {
        File file;
        if (filesave != null && !filesave.isEmpty()) {
            file = new File(filesave);
        } else {
            FileChooser fileChooser = new FileChooser();
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("png files (*.png)", "*.png");
            FileChooser.ExtensionFilter extFilter2 = new FileChooser.ExtensionFilter("jpg files (*.jpg)", "*.jpg");
            FileChooser.ExtensionFilter extFilter3 = new FileChooser.ExtensionFilter("jpg files (*.jpeg)", "*.jpeg");
            fileChooser.getExtensionFilters().add(extFilter);
            fileChooser.getExtensionFilters().add(extFilter2);
            fileChooser.getExtensionFilters().add(extFilter3);

            Stage stage = (Stage) this.paintCanvas.getScene().getWindow();

            file = fileChooser.showSaveDialog(stage);
        }
        if (file != null) {
            this.filesave = file.getAbsolutePath();

            // Attempts to save the canvas as an image
            try {
                WritableImage writableImage = new WritableImage((int) paintCanvas.getWidth(),
                        (int) paintCanvas.getHeight());
                paintCanvas.snapshot(null, writableImage);
                RenderedImage renderedImage = SwingFXUtils.fromFXImage(writableImage, null);
                ImageIO.write(renderedImage, "png", file);
                this.changed = false;
            } catch (IOException ex) {
                System.out.println("Save Error");
                return false;
            }
        } else {
            System.out.println("Save Canceled");
            return false;
        }
        return true;
    }

    /**
     * Handles the event when the edit menu items are clicked, and updates the model
     * accordingly.
     * 
     * @param event The ActionEvent, edit menu items, that triggered the method
     *              call.
     */
    @FXML
    private void edithandler(ActionEvent event) {
        MenuItem originbutton = (MenuItem) event.getSource();
        String command = originbutton.getText();

        if (command.equals("Cut")) {
            this.model.editcut();
        } else if (command.equals("Copy")) {
            this.model.editcopy();
        } else if (command.equals("Paste")) {
            this.model.editpaste();
        }
    }

    /**
     * Handles the event when a key is pressed and updates the model accordingly.
     * It is associated with the key pressed event of the canvas.
     * 
     * @param event The KeyEvent that triggered the method call.
     */
    @FXML
    private void typehandler(KeyEvent event) {
        if (!event.getCharacter().matches("[A-Za-z0-9\\S&&[^\\n]]+")) {
            System.out.println("Enter key pressed");
            return;
        }
        this.model.typedKey(event.getCharacter());
    }

    /**
     * This method is responsible for redrawing the entire canvas based on the
     * current state of the model. It is called whenever the model changes and
     * the canvas needs to be updated to reflect the changes.
     * 
     * This method is also responsible for clearing the canvas and then drawing
     * all the shapes and lines that are stored in the model. The method is
     * called whenever the model changes and the canvas needs to be updated to
     * reflect the changes.
     *
     * This method is also associated with the observable interface, which is
     * used to notify the controller that the model has changed and the canvas
     * needs to be updated.
     * 
     * @param o   The Observable object that the controller is observing, that is
     *            the PaintModel
     * @param arg The object that contains the data that the model has changed to.
     */
    @Override
    public void update(Observable o, Object arg) {

        PaintModel model = (PaintModel) o;
        Image bg = model.getCanvasBackground();

        // GraphicsContext g2d = this.paintCanvas.getGraphicsContext2D();
        g2d.clearRect(0, 0, this.paintCanvas.getWidth(), this.paintCanvas.getHeight());

        if (bg != null) {
            g2d.drawImage(bg, 0, 0, bg.getWidth(), bg.getHeight());
        }

        ArrayList<Action> drawingActions = this.model.getDrawings();

        // Draw all the shapes in the model accordingly
        for (Action action : drawingActions) {
            switch (action.getName()) {
                case "Oval":
                    Oval oval = (Oval) action;
                    double x_Oval = oval.getTopLeft().x;
                    double y_Oval = oval.getTopLeft().y;
                    double radiusX = oval.getRadiusX();
                    double radiusY = oval.getRadiusY();
                    g2d.setLineWidth(oval.getLineThickness());
                    if (oval.getfill()) {
                        g2d.setFill(oval.getColor());
                        g2d.fillOval(x_Oval, y_Oval, radiusX * 2, radiusY * 2);
                    } else {
                        g2d.setStroke(oval.getColor());
                        g2d.strokeOval(x_Oval, y_Oval, radiusX * 2, radiusY * 2);
                    }
                    break;

                case "Circle":
                    Circle circle = (Circle) action;
                    double x = circle.getTopLeft().x;
                    double y = circle.getTopLeft().y;
                    double radius = circle.getRadius();
                    g2d.setLineWidth(circle.getLineThickness());
                    if (circle.getfill()) {
                        g2d.setFill(circle.getColor());
                        g2d.fillOval(x, y, radius * 2, radius * 2);
                    } else {
                        g2d.setStroke(circle.getColor());
                        g2d.strokeOval(x, y, radius * 2, radius * 2);
                    }
                    break;

                case "Rectangle":
                    Rectangle rectangle = (Rectangle) action;
                    Point topLeft = rectangle.getTopLeft();
                    double x1 = topLeft.x;
                    double y1 = topLeft.y;
                    Point widthHeight = rectangle.getWidthHeight();
                    double x2 = widthHeight.x;
                    double y2 = widthHeight.y;
                    g2d.setLineWidth(rectangle.getLineThickness());
                    if (rectangle.getfill()) {
                        g2d.setFill(rectangle.getColor());
                        g2d.fillRect(x1, y1, x2, y2);
                    } else {
                        g2d.setStroke(rectangle.getColor());
                        g2d.strokeRect(x1, y1, x2, y2);
                    }
                    break;

                case "Squiggle":
                    Squiggle squiggle = (Squiggle) action;
                    g2d.setStroke(squiggle.getColor());
                    g2d.setLineWidth(squiggle.getLineThickness());
                    ArrayList<Point> points = squiggle.getPoints();
                    for (int i = 0; i < points.size() - 1; i++) {
                        Point p1 = points.get(i);
                        Point p2 = points.get(i + 1);
                        g2d.strokeLine(p1.x, p1.y, p2.x, p2.y);
                    }
                    break;

                case "Polyline":
                    Polyline polyline = (Polyline) action;
                    g2d.setStroke(polyline.getColor());
                    g2d.setLineWidth(polyline.getLineThickness());
                    ArrayList<Point> polylinePoints = polyline.getPoints();
                    for (int i = 0; i < polylinePoints.size() - 1; i++) {
                        Point p1 = polylinePoints.get(i);
                        Point p2 = polylinePoints.get(i + 1);
                        g2d.strokeLine(p1.x, p1.y, p2.x, p2.y);
                    }
                    if (polyline.getCurrentPoint() != null) {
                        Point last = polylinePoints.getLast();
                        Point current = polyline.getCurrentPoint();
                        g2d.strokeLine(last.x, last.y, current.x, current.y);
                    }
                    break;

                case "Square":
                    Square square = (Square) action;
                    Point topLeftSquare = square.getTopLeft();
                    double x1Square = topLeftSquare.x;
                    double y1Square = topLeftSquare.y;
                    Point widthHeightSquare = square.getWidthHeight();
                    double x2Square = widthHeightSquare.x;
                    double y2Square = widthHeightSquare.y;
                    g2d.setLineWidth(square.getLineThickness());
                    if (square.getfill()) {
                        g2d.setFill(square.getColor());
                        g2d.fillRect(x1Square, y1Square, x2Square, y2Square);
                    } else {
                        g2d.setStroke(square.getColor());
                        g2d.strokeRect(x1Square, y1Square, x2Square, y2Square);
                    }
                    break;

                case "Triangle":
                    Triangle triangle = (Triangle) action;
                    double[] P1 = { triangle.firstPoint.x, triangle.secondPoint.x, triangle.thirdPoint.x };
                    double[] P2 = { triangle.firstPoint.y, triangle.secondPoint.y, triangle.thirdPoint.y };
                    g2d.setLineWidth(triangle.getLineThickness());
                    if (triangle.getfill()) {
                        g2d.setFill(triangle.getColor());
                        g2d.fillPolygon(P1, P2, 3);
                    } else {
                        g2d.setStroke(triangle.getColor());
                        g2d.strokePolygon(P1, P2, 3);
                    }
                    break;

                case "Selector":
                    Selector selector = (Selector) action;
                    Point topLeftSelector = selector.getTopLeft();
                    double x1Selector = topLeftSelector.x;
                    double y1Selector = topLeftSelector.y;
                    Point widthHeightSelector = selector.getWidthHeight();
                    double x2Selector = widthHeightSelector.x;
                    double y2Selector = widthHeightSelector.y;
                    g2d.setLineWidth(selector.getLineThickness());
                    if (selector.getfill()) {
                        g2d.setFill(selector.getColor());
                        g2d.fillRect(x1Selector, y1Selector, x2Selector, y2Selector);
                    }
                    g2d.setStroke(Color.DARKBLUE);
                    g2d.strokeRect(x1Selector, y1Selector, x2Selector, y2Selector);
                    break;

                case "Text":
                    Textbox text = (Textbox) action;
                    g2d.setFill(text.getColor());
                    g2d.setFont(new Font("Arial", text.lineThickness * 10));
                    g2d.fillText(text.getText(), text.getBottomLeft().x, text.getBottomLeft().y);
            }
        }
        this.changed = true;
    }

}
