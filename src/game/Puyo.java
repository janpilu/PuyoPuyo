package game;

import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;


public class Puyo extends Field {
    private boolean floating;
    private ColorPuyo c;
    private int x;
    private int y;
    private Color color;
    private Rectangle body;

    public Puyo(int x, int y, Color c){
        super(x,y);
        this.floating = true;
        color = c;
        body = getB();
        body.setFill(color);
        setB(body);
        this.x = x;
        this.y = y;
        this.getChildren().clear();
        this.getChildren().add(getB());
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Rectangle getBody() {
        return body;
    }

    public void setBody(Rectangle body) {
        this.body = body;
    }

    public boolean isFloating() {
        return floating;
    }

    public void setFloating(boolean floating) {
        this.floating = floating;
    }
}
