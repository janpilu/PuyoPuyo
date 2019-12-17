package game;

import java.util.concurrent.TimeUnit;

public class Refresh implements Runnable {

    private Controller c;

    public Refresh(Controller c){
        this.c = c;
    }

    @Override
    public void run() {
        while (true){
                try {
                    TimeUnit.MILLISECONDS.sleep(250);
                    this.c.redraw();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
        }
    }
}
