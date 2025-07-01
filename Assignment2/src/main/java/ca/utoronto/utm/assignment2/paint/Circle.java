package ca.utoronto.utm.assignment2.paint;

import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

/**
 * Capture a Circle drawing.
 * This includes the coordinates as well as the properties of the circle,
 * (E.g. line thickness, fill, color).
 */
public class Circle extends Shape {
        protected Point centre;
        protected double radius;
        protected boolean fill;

        /**
         * Constructor for class Circle.
         * Create an oval with the specified properties.
         *
         * @param centre the centre of the circle ( the initial point that the user starts drawing )
         * @param radius the radius of the circle
         * @param color the color of oval
         * @param fill whether the oval is filled with color
         * @param lineThickness the line thickness of the oval outline
         */
        public Circle(Point centre, double radius, Color color, Boolean fill, double lineThickness) {
                super(color, lineThickness, "Circle");
                this.centre = centre;
                this.radius = radius;
                this.fill = fill;
        }

        /**
         * @return the Point centre of the circle.
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
         * @return the radius of the circle
         */
        public double getRadius() {
                return radius;
        }

        /**
         * Update the radius of the circle
         *
         * @param radius the new radius
         */
        private void setRadius(double radius) {
                this.radius = radius;
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
         * Update the radius, topLeft, bottomRight of the circle according to the drag action.
         *
         * @param mouseEvent The mouse dragging event from user.
         */
        @Override
        public void dragged(MouseEvent mouseEvent) {
                // Difference between the starting and ending point of the drag
                double changeX = (this.getCentre().x - mouseEvent.getX());
                double changeY = (this.getCentre().y - mouseEvent.getY());
                // Point-distance formula to get distance dragged (radius)
                double radius = Math.sqrt(Math.pow(changeX, 2) + Math.pow(changeY, 2));
                this.setRadius(radius);
                double topLeftCornerX = this.getCentre().x - radius;
                double topLeftCornerY = this.getCentre().y - radius;
                this.setTopLeft(new Point(topLeftCornerX, topLeftCornerY));
                this.setBottomRight(new Point(this.topLeft.x + (this.getRadius() * 2), this.topLeft.y + (this.getRadius() * 2)));
        }

        /**
         * Called when the mouse button is released.
         *
         * @param mouseEvent the MouseEvent associated with the release action
         */
        @Override
        public void released(MouseEvent mouseEvent) {
                System.out.println("Added Circle");
        }

        /**
         * Creates and returns a copy of the circle.
         *
         * @return a cloned instance of the circle
         */
        @Override
        public Drawable clone() {
                Circle clone = new Circle(centre.clone(), radius, color, fill, lineThickness);
                clone.topLeft = this.topLeft.clone();
                clone.bottomRight = this.bottomRight.clone();
                return clone;
        }

        /**
         * Move the circle according to the user's actions.
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
