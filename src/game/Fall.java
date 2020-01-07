package game;

import java.util.concurrent.TimeUnit;

public class Fall implements Runnable{

    private Model m;

    public Fall(Model m){
        this.m=m;
    }

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
                System.out.println(multiplier+"");
                TimeUnit.MILLISECONDS.sleep(Math.round(200/multiplier));
                this.m.move(0,1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
