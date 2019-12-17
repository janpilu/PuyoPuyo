package game;

import java.util.concurrent.TimeUnit;

public class Fall implements Runnable{

    private Model m;

    public Fall(Model m){
        this.m=m;
    }

    @Override
    public void run() {
        while (true){
            try {
                TimeUnit.MILLISECONDS.sleep(300);
                this.m.move(0,1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
