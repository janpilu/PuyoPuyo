package game;

import javafx.scene.paint.Color;

public class PuyoPair {
    private Puyo puyo1;
    private Puyo puyo2;

    public PuyoPair(){
        int r1 = (int)(Math.random() * ((5 - 1) + 1)) + 1;
        int r2 = (int)(Math.random() * ((5 - 1) + 1)) + 1;
        this.puyo1 = new Puyo(5,0, r1);
        this.puyo2 = new Puyo(6,0, r2);
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

}
