# Paint Application - JavaFX Drawing Tool

![image](https://github.com/user-attachments/assets/f8af4d55-d230-4e1f-a0ea-3d5bddd92424)


---

## Overview

This is a JavaFX-based Paint application developed as part of the University of Toronto course assignments. It allows users to create, edit, and save drawings using a variety of shapes and freehand tools. The app supports features such as:

- Drawing multiple shapes (Oval, Circle, Rectangle, Square, Triangle, Polyline, Squiggle)
- Editing options: Undo, Redo, Cut, Copy, Paste
- Color selection with a palette and eyedropper tool
- Line thickness adjustment
- Fill and outline toggle for shapes
- Text input on canvas
- File operations: New, Open, Save, Save As (supports PNG, JPG, JPEG)
- Shape selection and manipulation

The project follows the **Model-View-Controller (MVC)** design pattern and the **Observer** pattern to keep the UI synchronized with the drawing model.

---

## Features

### Drawing Tools
- **Basic shapes:** Oval, Circle, Rectangle, Square, Triangle
- **Freehand tools:** Squiggle and Polyline for smooth curves and connected lines
- **Text tool:** Add editable text on the canvas

### Editing
- Undo and redo actions to revert or re-apply changes
- Cut, copy, and paste functionality for shapes and drawings
- Select and manipulate shapes with a selector tool

### Colors & Styles
- Color palette with predefined colors and eyedropper tool to pick colors from the canvas
- Toggle between fill and outline mode for shapes
- Adjustable line thickness via slider

### File Operations
- Create a new blank canvas with prompt to save unsaved work
- Open existing image files (PNG, JPG, JPEG)
- Save current canvas to image files with format options

---

## Project Structure

- **`FXMLController.java`**  
  Controls the user interface, handles user inputs, updates the canvas display, and communicates with the model.

- **`PaintModel.java`** (not shown here but implied)  
  The core model managing the current drawing state, action history, undo/redo stack, and shape data.

- **`Action.java`**  
  Abstract class representing a user action (drawing, editing, etc.)

- **`Shape.java` and subclasses**  
  Abstract and concrete classes representing drawable shapes (Oval, Circle, Rectangle, etc.)

- **Utility Classes**  
  `Point.java`, `Polyline.java`, `Selector.java`, `Textbox.java`, etc. represent components or data structures needed by shapes and actions.

- **Resources**  
  Image icons for shapes, CSS styles, and FXML files for UI layout.

---

## Architecture & Design

The application uses **MVC architecture**:

- **Model:** Manages drawing data, shapes, colors, fill/outline states, and undo/redo history.
- **View:** The JavaFX canvas and UI components (buttons, sliders, color panels).
- **Controller:** Handles UI events and updates the model and view accordingly.

The controller implements **Observer pattern** to listen for changes in the model and redraw the canvas when needed.

---

## Getting Started

### Prerequisites

- Java Development Kit (JDK) 8 or higher
- JavaFX SDK compatible with your JDK version
- IDE like IntelliJ IDEA, Eclipse, or VS Code with JavaFX support
