package game;

import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;
import static game.ColorPuyo.*;


public class Puyo extends Field {
    private boolean floating;
    private ColorPuyo c;
    private int x;
    private int y;
    private Rectangle body;

    public Puyo(int x, int y, int c){
        super(x,y);
        body = getB();
        switch (c){
            case 1:
                this.c = RED;
                body.setFill(Color.RED);
                break;
            case 2:
                this.c = BLUE;
                body.setFill(Color.BLUE);
                break;
            case 3:
                this.c = GREEN;
                body.setFill(Color.GREEN);
                break;
            case 4:
                this.c = YELLOW;
                body.setFill(Color.YELLOW);
                break;
            case 5:
                this.c = PURPLE;
                body.setFill(Color.PURPLE);
                break;
        }
        this.floating = true;
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

    public ColorPuyo getColor() {
        return c;
    }

    public void setColor(ColorPuyo c) {
        this.c = c;
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
