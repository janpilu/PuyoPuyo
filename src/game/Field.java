package game;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Field extends StackPane {
    private Rectangle b;
    private int x;
    private int y;
    private boolean full;

    public Field(int x, int y){
        this.x = x;
        this.y = y;
        this.b = new Rectangle(20,20);
        b.setStroke(Color.BLACK);
        b.setFill(Color.WHITE);
        this.getChildren().addAll(b);
    }

    public Rectangle getB() {
        return b;
    }

    public void setB(Rectangle b) {
        this.b = b;
    }
}
