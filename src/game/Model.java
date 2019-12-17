package game;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static game.PuyoPairState.*;

public class Model {

    private Field[][] map;
    public int x;
    private int y;
    private PuyoPairState state;
    private List<Puyo> checked;
    private List<Puyo> toCheck;
    private PuyoPair curPuyo;
    private int curPosX;
    private int curPosY;

    public Model(){
        this.x =12;
        this.y =24;

        this.checked = new ArrayList<>();
        this.toCheck = new ArrayList<>();

        map = new Field[x][y];
    }

    public void drop(){
        Fall f = new Fall(this);
        new Thread(f).start();
    }

    public void newPuyoPair(){
        this.curPuyo = new PuyoPair();
        this.state = HOT;
        setPuyo(curPuyo.getPuyo1().getX(),curPuyo.getPuyo1().getY(),curPuyo.getPuyo1());
        setPuyo(curPuyo.getPuyo2().getX(),curPuyo.getPuyo2().getY(),curPuyo.getPuyo2());
    }

    public void setPuyo(int x, int y, Puyo p){
            this.map[x][y] = p;
    }

    public void remPuyo(int x, int y){
        this.map[x][y] = new Field(x,y);
    }

    public void collapse(int x, int y){
        this.map[x][y] = new Field(x,y);
        if (this.map[x][y - 1].getClass() == Puyo.class&&y>0) {
            for (int i = y; i > 0; i--) {
                if (this.map[x][i].getClass() == Puyo.class)
                    fastdrop((Puyo) this.map[x][i]);
            }
        }
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

            setPuyo(newpuyo1x, newpuyo1y, this.curPuyo.getPuyo1());
            setPuyo(newpuyo2x, newpuyo2y, this.curPuyo.getPuyo2());
        }
    }

    public void turnMove(int x, int y, int xo, int yo){
        if (checkx(x) && checky(y)) {
            remPuyo(xo, yo);
            this.curPuyo.getPuyo1().setX(x);
            this.curPuyo.getPuyo1().setY(y);
            setPuyo(x, y, this.curPuyo.getPuyo1());
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
                if(!checkFieldForPuyo(newX,newY)) {
                    if (checkx(newX) && checky(newY)) {
                        turnMove(newX, newY, oldx, oldy);
                        if (clock)
                            this.state = VOT;
                        else
                            this.state = VTO;
                    }
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
                if(!checkFieldForPuyo(newX,newY)) {
                    if (checkx(newX) && checky(newY)) {
                        turnMove(newX, newY, oldx, oldy);
                        if (clock)
                            this.state = HTO;
                        else
                            this.state = HOT;
                    }
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
                if(!checkFieldForPuyo(newX,newY)) {
                    if (checkx(newX) && checky(newY)) {
                        turnMove(newX, newY, oldx, oldy);
                        if (clock)
                            this.state = HOT;
                        else
                            this.state = HTO;
                    }
                }
                break;
        }
    }

    public void chain(List<Field> tokill){
        tokill.forEach(p->remPuyo(p.getX(),p.getY()));
    }

    public void checkBelow() {
        switch (state){
            case HOT:
            case HTO:
                if(curPuyo.getPuyo1().getY()==y-1) {
                    bothDown();
                }
                if(map[curPuyo.getPuyo1().getX()][curPuyo.getPuyo1().getY()+1].getClass()==Puyo.class)
                    fastdrop(this.curPuyo.getPuyo2());
                if(map[curPuyo.getPuyo2().getX()][curPuyo.getPuyo2().getY()+1].getClass()==Puyo.class)
                    fastdrop(this.curPuyo.getPuyo1());
                break;
            case VOT:
                if(curPuyo.getPuyo2().getY()==y-1) {
                    bothDown();
                }
                if(map[curPuyo.getPuyo2().getX()][curPuyo.getPuyo2().getY()+1].getClass()==Puyo.class) {
                    bothDown();
                }
                break;
            case VTO:
                if(curPuyo.getPuyo1().getY()==y-1) {
                    bothDown();
                }
                if(map[curPuyo.getPuyo1().getX()][curPuyo.getPuyo1().getY()+1].getClass()==Puyo.class) {
                    bothDown();
                }
                break;
        }
    }

    public void bothDown(){
        curPuyo.getPuyo1().setFloating(false);
        curPuyo.getPuyo2().setFloating(false);
        check(curPuyo.getPuyo1());
        check(curPuyo.getPuyo2());
        newPuyoPair();
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

    public boolean checkFieldForPuyo(int x,int y){
        if(map[x][y].getClass()==Puyo.class)
            return true;
        return false;
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
    /**
    private void fastdrop(int i){
        if(i == 1){
            while (map[curPuyo.getPuyo1().getX()][curPuyo.getPuyo1().getY()+1].getClass()!=Puyo.class){
                int oldpuyo1y = this.curPuyo.getPuyo1().getY();
                remPuyo(this.curPuyo.getPuyo1().getX(),oldpuyo1y);
                this.curPuyo.getPuyo1().setY(oldpuyo1y+1);
                this.setPuyo(this.curPuyo.getPuyo1().getX(),this.curPuyo.getPuyo1().getY(),1);
                if(this.curPuyo.getPuyo1().getY()+1==y) {
                    curPuyo.getPuyo1().setFloating(false);
                    check(curPuyo.getPuyo1());
                    check(curPuyo.getPuyo2());
                    break;
                }
            }
            curPuyo.getPuyo1().setFloating(false);
            check(curPuyo.getPuyo1());
            check(curPuyo.getPuyo2());
            newPuyoPair();
        }else if(i==2){
            while (map[curPuyo.getPuyo2().getX()][curPuyo.getPuyo2().getY()+1].getClass()!=Puyo.class){
                int oldpuyo2y = this.curPuyo.getPuyo2().getY();
                remPuyo(this.curPuyo.getPuyo2().getX(),oldpuyo2y);
                this.curPuyo.getPuyo2().setY(oldpuyo2y+1);
                this.setPuyo(this.curPuyo.getPuyo2().getX(),this.curPuyo.getPuyo2().getY(),2);
                if(this.curPuyo.getPuyo2().getY()+1==y) {
                    curPuyo.getPuyo2().setFloating(false);
                    check(curPuyo.getPuyo1());
                    check(curPuyo.getPuyo2());
                    break;
                }
            }
            curPuyo.getPuyo2().setFloating(false);
            check(curPuyo.getPuyo1());
            check(curPuyo.getPuyo2());
            newPuyoPair();
        }
    }
*/
    private void fastdrop(Puyo p){
        while (map[p.getX()][p.getY()+1].getClass()!=Puyo.class){
            int oldpuyo1y = p.getY();
            remPuyo(p.getX(),oldpuyo1y);
            p.setY(oldpuyo1y+1);
            this.setPuyo(p.getX(),p.getY(),p);
            if(p.getY()+1==y) {
                p.setFloating(false);
                check(p);
                if(p.equals(this.curPuyo.getPuyo1()))
                    check(this.curPuyo.getPuyo2());
                else if(p.equals(this.curPuyo.getPuyo2()))
                    check(this.curPuyo.getPuyo1());
                break;
            }
        }
        p.setFloating(false);
        check(p);
        if(p.equals(this.curPuyo.getPuyo1()))
            check(this.curPuyo.getPuyo2());
        else if(p.equals(this.curPuyo.getPuyo2()))
            check(this.curPuyo.getPuyo1());
        newPuyoPair();
    }

    private void check(Puyo puyo) {
        this.checked = new ArrayList<>();
        this.toCheck = new ArrayList<>();
        this.toCheck.add(puyo);
        int i = 0;
        chainStart(i);
        if(this.checked.size()>=4){
            this.checked.forEach(p->collapse(p.getX(),p.getY()));
        }
    }

    public void chainStart(int i){
        this.checked.add(toCheck.get(i));
        int x = toCheck.get(i).getX();
        int y = toCheck.get(i).getY();
        chainL(x,y);
        chainR(x,y);
        chainU(x,y);
        chainD(x,y);
        i++;
        AtomicBoolean temp = new AtomicBoolean();
        toCheck.forEach(p->{
            if(!checked.contains(p))
                temp.set(true);
        });
        if (temp.get()){
            chainStart(i);
        }
    }

    public void chainL(int x, int y){
        if(x>0){
            if(map[x-1][y].getClass()==Puyo.class&&((Puyo)map[x-1][y]).getColor()==((Puyo)map[x][y]).getColor()){
                this.toCheck.add((Puyo)map[x-1][y]);
                chainL(x-1,y);
            }
        }
    }
    public void chainR(int x, int y){
        if(x<this.x-1){
            if(map[x+1][y].getClass()==Puyo.class&&((Puyo)map[x+1][y]).getColor()==((Puyo)map[x][y]).getColor()){
                this.toCheck.add((Puyo)map[x+1][y]);
                chainR(x+1,y);
            }
        }
    }
    public void chainD(int x, int y){
        if(y<this.y-1){
            if(map[x][y+1].getClass()==Puyo.class&&((Puyo)map[x][y+1]).getColor()==((Puyo)map[x][y]).getColor()){
                this.toCheck.add((Puyo)map[x][y+1]);
                chainD(x,y+1);
            }
        }
    }
    public void chainU(int x, int y){
        if(y>0){
            if(map[x][y-1].getClass()==Puyo.class&&((Puyo)map[x][y-1]).getColor()==((Puyo)map[x][y]).getColor()){
                this.toCheck.add((Puyo)map[x][y-1]);
                chainU(x,y-1);
            }
        }
    }

    public List<Field> getNeighbors(Field cur){
        List<Field> neighbors = new ArrayList<>();
        //  f
        // fXf
        //  f

        int[] toAdd = {
                0, 1,
                -1, 0,
                1, 0,
                0,-1
        };

        for(int i = 0;i<toAdd.length;i++){
            int toAddX = cur.getX()+toAdd[i];
            int toAddY = cur.getY()+toAdd[++i];
            if(toAddX >=0 && toAddX<this.x&&toAddY>=0&&toAddY<this.y){
                Field temp =map[toAddX][toAddY];
                if(temp.getClass()==Puyo.class && !((Puyo) temp).isFloating() && ((Puyo) temp).getColor() == ((Puyo) cur).getColor()) {
                    neighbors.add(temp);
                }
            }
        }
        return neighbors;
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

    public PuyoPair getCurPuyo() {
        return curPuyo;
    }
}
