package game;

import javafx.scene.paint.Color;

public class PuyoPair {
    private Puyo puyo1;
    private Puyo puyo2;

    public PuyoPair(){
        this.puyo1 = new Puyo(5,0, Color.RED);
        this.puyo2 = new Puyo(6,0, Color.BLUE);
    }

    public void turnLeft(){

    }

    public void turnRight(){

    }

    public void left(){

    }

    public void right(){

    }

    public void down(){

    }

    public Puyo getPuyo1() {
        return puyo1;
    }

    public void setPuyo1(Puyo puyo1) {
        this.puyo1 = puyo1;
    }

    public Puyo getPuyo2() {
        return puyo2;
    }

    public void setPuyo2(Puyo puyo2) {
        this.puyo2 = puyo2;
    }

    public void setPuyo2pos(int x, int y) {
        this.puyo2.setX(x);
        this.puyo2.setY(y);
    }

    public void setPuyo1pos(int x, int y) {
        this.puyo1.setX(x);
        this.puyo1.setY(y);
    }
}
