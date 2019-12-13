package game;

import java.util.List;

import static game.PuyoPairState.*;

public class Model {

    private Field[][] map;
    public int x;
    private int y;
    private PuyoPairState state;
    private List<Puyo> puyos;

    private PuyoPair curPuyo;
    private int curPosX;
    private int curPosY;

    public Model(){
        this.x =12;
        this.y =24;

        map = new Field[x][y];
    }

    public void newPuyoPair(){
        this.curPuyo = new PuyoPair();
        this.state = HOT;
        setPuyo(curPuyo.getPuyo1().getX(),curPuyo.getPuyo1().getY(),1);
        setPuyo(curPuyo.getPuyo2().getX(),curPuyo.getPuyo2().getY(),2);
    }

    public void setPuyo(int x, int y, int p){
        if(p==1)
            this.map[x][y] = curPuyo.getPuyo1();
        else if(p==2)
            this.map[x][y] = curPuyo.getPuyo2();
    }

    public void remPuyo(int x, int y){
        this.map[x][y] = new Field(x,y);
    }

    private boolean checky(int y) {
        if(y<0||y>=this.y){
            return false;
        }
        return true;
    }

    private boolean checkx(int x) {
        if(x<0||x>=this.x){
            return false;
        }
        return true;
    }

    public void drop(){

    }

    public void move(int x, int y){
        int oldpuyo1y = this.curPuyo.getPuyo1().getY();
        int newpuyo1y = this.curPuyo.getPuyo1().getY()+y;

        int oldpuyo2y = this.curPuyo.getPuyo2().getY();
        int newpuyo2y = this.curPuyo.getPuyo2().getY()+y;

        int oldpuyo1x = this.curPuyo.getPuyo1().getX();
        int newpuyo1x = this.curPuyo.getPuyo1().getX()+x;

        int oldpuyo2x = this.curPuyo.getPuyo2().getX();
        int newpuyo2x = this.curPuyo.getPuyo2().getX()+x;

        if(checkx(newpuyo1x)&&checkx(newpuyo2x)&&checky(newpuyo1y)&&checky(newpuyo2y)) {
            this.curPuyo.getPuyo1().setX(newpuyo1x);
            this.curPuyo.getPuyo1().setY(newpuyo1y);

            this.curPuyo.getPuyo2().setY(newpuyo2y);
            this.curPuyo.getPuyo2().setX(newpuyo2x);

            remPuyo(oldpuyo1x, oldpuyo1y);
            remPuyo(oldpuyo2x, oldpuyo2y);

            setPuyo(newpuyo1x, newpuyo1y, 1);
            setPuyo(newpuyo2x, newpuyo2y, 2);
        }
    }

    public void turnMove(int x, int y, int xo, int yo){
        if(checkx(x)&&checky(y)){
            remPuyo(xo,yo);
            this.curPuyo.getPuyo1().setX(x);
            this.curPuyo.getPuyo1().setY(y);
            setPuyo(x,y,1);
        }
    }

    //+left clock -right counterclock
    public void turn(boolean clock){
        int oldx = this.curPuyo.getPuyo1().getX();
        int oldy = this.curPuyo.getPuyo1().getY();
        int newX;
        int newY;
        switch (state){
            case HOT:
                if(clock) {
                    newX = this.curPuyo.getPuyo1().getX() + 1;
                    newY = this.curPuyo.getPuyo1().getY() - 1;
                }else{
                    newX = this.curPuyo.getPuyo1().getX() + 1;
                    newY = this.curPuyo.getPuyo1().getY() + 1;
                }
                if(checkx(newX)&&checky(newY)) {
                    turnMove(newX, newY, oldx, oldy);
                    if (clock)
                        this.state = VOT;
                    else
                        this.state = VTO;
                }
                break;
            case HTO:
                if(clock) {
                    newX = this.curPuyo.getPuyo1().getX() - 1;
                    newY = this.curPuyo.getPuyo1().getY() + 1;
                }else{
                    newX = this.curPuyo.getPuyo1().getX() - 1;
                    newY = this.curPuyo.getPuyo1().getY() - 1;
                }
                if(checkx(newX)&&checky(newY)) {
                    turnMove(newX, newY, oldx, oldy);
                    if (clock)
                        this.state = VTO;
                    else
                        this.state = VOT;
                }
                break;
            case VOT:
                if(clock) {
                    newX = this.curPuyo.getPuyo1().getX() + 1;
                    newY = this.curPuyo.getPuyo1().getY() + 1;
                }else{
                    newX = this.curPuyo.getPuyo1().getX() - 1;
                    newY = this.curPuyo.getPuyo1().getY() + 1;
                }
                if(checkx(newX)&&checky(newY)) {
                    turnMove(newX, newY, oldx, oldy);
                    if (clock)
                        this.state = HTO;
                    else
                        this.state = HOT;
                }
                break;
            case VTO:
                if(clock) {
                    newX = this.curPuyo.getPuyo1().getX() - 1;
                    newY = this.curPuyo.getPuyo1().getY() - 1;
                }else{
                    newX = this.curPuyo.getPuyo1().getX() + 1;
                    newY = this.curPuyo.getPuyo1().getY() - 1;
                }
                if(checkx(newX)&&checky(newY)) {
                    turnMove(newX, newY, oldx, oldy);
                    if (clock)
                        this.state = HOT;
                    else
                        this.state = HTO;
                }
                break;
        }
    }

    public void checkBelow() {
        switch (state){
            case HOT:
            case HTO:
                if(curPuyo.getPuyo1().getY()==y-1)
                    newPuyoPair();
                if(map[curPuyo.getPuyo1().getX()][curPuyo.getPuyo1().getY()+1].getClass()==Puyo.class)
                    fastdrop(2);
                if(map[curPuyo.getPuyo2().getX()][curPuyo.getPuyo2().getY()+1].getClass()==Puyo.class)
                    fastdrop(1);
                break;
            case VOT:
                if(curPuyo.getPuyo2().getY()==y-1)
                    newPuyoPair();
                if(map[curPuyo.getPuyo2().getX()][curPuyo.getPuyo2().getY()+1].getClass()==Puyo.class)
                    newPuyoPair();
                break;
            case VTO:
                if(curPuyo.getPuyo1().getY()==y-1)
                    newPuyoPair();
                if(map[curPuyo.getPuyo1().getX()][curPuyo.getPuyo1().getY()+1].getClass()==Puyo.class)
                    newPuyoPair();
                break;
        }
    }

    public boolean checkLeft() {
        switch (state){
            case VTO:
            case HOT:
                if(!checkx(this.curPuyo.getPuyo1().getX()-1))
                    return false;
                if(map[this.curPuyo.getPuyo1().getX()-1][this.curPuyo.getPuyo1().getY()].getClass() == Puyo.class)
                    return false;
                break;
            case VOT:
            case HTO:
                if(!checkx(this.curPuyo.getPuyo2().getX()-1))
                    return false;
                if(map[this.curPuyo.getPuyo2().getX()-1][this.curPuyo.getPuyo2().getY()].getClass() == Puyo.class)
                    return false;
                break;
        }
        return true;
    }

    public boolean checkRight() {
        switch (state){
            case VOT:
            case HOT:
                if(!checkx(this.curPuyo.getPuyo2().getX()+1))
                    return false;
                if(map[this.curPuyo.getPuyo2().getX()+1][this.curPuyo.getPuyo2().getY()].getClass() == Puyo.class)
                    return false;
                break;
            case VTO:
            case HTO:
                if(!checkx(this.curPuyo.getPuyo1().getX()+1))
                    return false;
                if(map[this.curPuyo.getPuyo1().getX()+1][this.curPuyo.getPuyo1().getY()].getClass() == Puyo.class)
                    return false;
                break;
        }
        return true;
    }


    private void fastdrop(int i){
        if(i == 1){
            while (map[curPuyo.getPuyo1().getX()][curPuyo.getPuyo1().getY()+1].getClass()!=Puyo.class){
                int oldpuyo1y = this.curPuyo.getPuyo1().getY();
                remPuyo(this.curPuyo.getPuyo1().getX(),oldpuyo1y);
                this.curPuyo.getPuyo1().setY(oldpuyo1y+1);
                this.setPuyo(this.curPuyo.getPuyo1().getX(),this.curPuyo.getPuyo1().getY(),1);
                if(this.curPuyo.getPuyo1().getY()+1==y)
                    break;
            }
            newPuyoPair();
        }else if(i==2){
            while (map[curPuyo.getPuyo2().getX()][curPuyo.getPuyo2().getY()+1].getClass()!=Puyo.class){
                int oldpuyo2y = this.curPuyo.getPuyo2().getY();
                remPuyo(this.curPuyo.getPuyo2().getX(),oldpuyo2y);
                this.curPuyo.getPuyo2().setY(oldpuyo2y+1);
                this.setPuyo(this.curPuyo.getPuyo2().getX(),this.curPuyo.getPuyo2().getY(),2);
                if(this.curPuyo.getPuyo2().getY()+1==y)
                    break;
            }
            newPuyoPair();
        }
    }

    public Field[][] getMap() {
        return map;
    }

    public void setMap(Field[][] map) {
        this.map = map;
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
}
