package game;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * Fields make up the entirety of the gameboard
 */
public class Field extends StackPane {
    private Rectangle b;
    private int x;
    private int y;
    private boolean full;

    /**
     * Initializes the passed cooradinates and colors the Field
     * @param x
     * @param y
     */
    public Field(int x, int y){
        this.x = x;
        this.y = y;
        this.b = new Rectangle(20,20);
        b.setStroke(Color.BLACK);
        b.setFill(Color.WHITE);
        this.getChildren().addAll(b);
    }

    /**
     * Returns the Fields Body
     * @return Rectangle b
     */
    public Rectangle getB() {
        return b;
    }

    /**
     * Sets the Fields Body
     * @param b
     */
    public void setB(Rectangle b) {
        this.b = b;
    }

    /**
     * Returns the Fields y Coordinate
     * @return int y
     */
    public int getY() {
        return y;
    }

    /**
     * Sets the Fields y Coordinates
     * @param y
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * Returns the Fields x Coordinate
     * @return int x
     */
    public int getX() {
        return x;
    }

    /**
     * Sets the Fields x Coordinates
     * @param x
     */
    public void setX(int x) {
        this.x = x;
    }
}
