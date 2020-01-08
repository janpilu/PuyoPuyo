package game;

import java.util.concurrent.TimeUnit;

/**
 * Class responsible for dropping the current puyoPair
 */
public class Fall implements Runnable{

    private Model m;

    /**
     * initializes Model
     * @param m
     */
    public Fall(Model m){
        this.m=m;
    }

    /**
     * Runs as long as loose is false
     * waits for a given time and moves the puyopair by one y coordinate
     * progressively increases the dropspeed by reducing the cooldown
     */
    @Override
    public void run() {
        int i=1;
        double multiplier=0.15;
        while (!m.isLoose()){
            i++;
            if(i%5==0&&multiplier<2.5)
                multiplier = i*0.015;
            if(multiplier<0.15)
                multiplier=0.15;
            if(multiplier>2.5)
                multiplier=2.5;
            try {
                //TimeUnit.MILLISECONDS.sleep(Math.round(200/multiplier));
                TimeUnit.MILLISECONDS.sleep(200);
                synchronized (Controller.lock) {
                    this.m.move(0, 1);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
