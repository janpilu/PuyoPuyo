package game;

import java.util.*;
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
    private PuyoPair next;
    private int score;
    private boolean loose;
    private Thread t;

    /**
     * Initializes the Attributes
     */
    public Model(){
        this.x =12;
        this.y =24;

        this.score = 0;
        this.loose = false;

        this.checked = new ArrayList<>();
        this.toCheck = new ArrayList<>();

        map = new Field[x][y];
        this.next = new PuyoPair();
    }

    /**
     * Called by the Controllers initialize Method
     * Starts the thread that drops the puyos
     */
    public void drop(){
        Fall f = new Fall(this);
        this.t = new Thread(f);
        t.start();
    }

    /**
     * Sets the old next PuyoPair as the current PuyoPair
     * Sets the State to HorizontalOneTwo
     * Adds the new curPuyo into the map.
     * should the puyos have reached the ceiling the loose method is called
     */
    public void newPuyoPair(){
        this.curPuyo = this.next;
        this.next = new PuyoPair();
        this.state = HOT;
        setPuyo(curPuyo.getPuyo1().getX(),curPuyo.getPuyo1().getY(),curPuyo.getPuyo1());
        setPuyo(curPuyo.getPuyo2().getX(),curPuyo.getPuyo2().getY(),curPuyo.getPuyo2());
        if(map[curPuyo.getPuyo1().getX()][curPuyo.getPuyo1().getY()+1].getClass()== Puyo.class || map[curPuyo.getPuyo2().getX()][curPuyo.getPuyo2().getY()+1].getClass()== Puyo.class)
            loose();
    }

    /**
     * Sets the loose attribute to true and joins the fall thread
     */
    private void loose() {
        this.loose = true;
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets a passed Puyo into the map using the passed coordinates
     * @param x
     * @param y
     * @param p
     */
    public void setPuyo(int x, int y, Puyo p){
            this.map[x][y] = p;
    }

    /**
     * Removes a Puyo from the field corresponding to the passed coordinates by instantiating a new empty field
     * @param x
     * @param y
     */
    public void remPuyo(int x, int y){
        this.map[x][y] = new Field(x,y);
    }

    /**
     * Called when 4 or more Puyos touch
     * removes the Puyo coresponding with the passed coordinates
     * checks if the fields above are puyos and drops them
     * @param x
     * @param y
     */
    public void collapse(int x, int y){
        this.map[x][y] = new Field(x,y);
        if (this.map[x][y - 1].getClass() == Puyo.class&&y>0) {
            for (int i = y; i > 0; i--) {
                if (this.map[x][i].getClass() == Puyo.class)
                    fastdrop((Puyo) this.map[x][i]);
            }
        }
    }

    /**
     * Checks if the passed coordinate is a viable y coordinate
     * @param y
     * @return true if viable
     */
    private boolean checky(int y) {
        if(y<0||y>=this.y){
            return false;
        }
        return true;
    }

    /**
     * Checks if the passed coordinate is a viable x coordinate
     * @param x
     * @return true if viable
     */
    private boolean checkx(int x) {
        if(x<0||x>=this.x){
            return false;
        }
        return true;
    }

    /**
     * Moves the Current puyos by the passed shift value
     * First checks if the new Coordinates are viable for both puyos
     * @param x shift
     * @param y shift
     */
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

    /**
     * Either turns the current Puyopair clock or counter clockwise
     * Calculates the new Coordinates depending on the puyos state and the rotation direction
     * if the new Coordinates are viable the turnMove method is called
     * @param clock
     */
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

    /**
     * Passes the Puyos old and new Coordinates and moves them accordingly, also removing the old ones
     * @param x
     * @param y
     * @param xo
     * @param yo
     */
    public void turnMove(int x, int y, int xo, int yo){
        if (checkx(x) && checky(y)) {
            remPuyo(xo, yo);
            this.curPuyo.getPuyo1().setX(x);
            this.curPuyo.getPuyo1().setY(y);
            setPuyo(x, y, this.curPuyo.getPuyo1());
        }
    }

    /**
     * Checks the fields below the current puyo pair depending on its state
     * if both touch the ground bothDown() is called
     * if one touches another puyo the other one is fast dropped by calling fastdrop(Puyo p)
     */
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

    /**
     * Sets both puyos floating attribute to false
     * checks for a chain and instantiates a new PuyoPair
     */
    public void bothDown(){
        curPuyo.getPuyo1().setFloating(false);
        curPuyo.getPuyo2().setFloating(false);
        check(curPuyo.getPuyo1());
        check(curPuyo.getPuyo2());
        newPuyoPair();
    }

    /**
     * Depending on the state checks the left side of the Current puyopair for viable coordinates
     * @return true if viable
     */
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

    /**
     * Checks if the filed corresponding to the passed parameters contains a puyo
     * @param x
     * @param y
     * @return true if contains puyo
     */
    public boolean checkFieldForPuyo(int x,int y){
        if(map[x][y].getClass()==Puyo.class)
            return true;
        return false;
    }

    /**
     * Depending on the state checks the right side of the Current puyopair for viable coordinates
     * @return true if viable
     */
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
     * Drops the passed puyo to the lowest possible empty field
     * @param p
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

    /**
     * recursively checks if the passed puyo is part of a chain of 4 or more
     * adds the passed puyo into toCheck and calls chainStart
     * @param puyo
     */
    private void check(Puyo puyo) {
        this.checked = new ArrayList<>();
        this.toCheck = new ArrayList<>();
        this.toCheck.add(puyo);
        int i = 0;
        chainStart(i);
        Set<Puyo> s = new HashSet(this.checked);
        if(s.size()>=4){
            s.forEach(p->{
                collapse(p.getX(),p.getY());
                this.score++;
            });
        }
    }

    /**
     * Start of the recursive checking with the puyo in the toCheck list corresponding to the passed parameter
     * calls chainL,R,U & D
     * Checks if toCheck is Equal with checked, in that case the recursion stops
     * otherwise this method is called again with an increased value
     * @param i
     */
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

    /**
     * Checks if the field on the left contains a puyo with the same color, in that case the method is called again with an altered x parameter
     * @param x
     * @param y
     */
    public void chainL(int x, int y){
        if(x>0){
            if(map[x-1][y].getClass()==Puyo.class&&map[x][y].getClass()==Puyo.class&&((Puyo)map[x-1][y]).getColor()==((Puyo)map[x][y]).getColor()&&map[x-1][y]!=map[x][y]){
                this.toCheck.add((Puyo)map[x-1][y]);
                chainL(x-1,y);
            }
        }
    }

    /**
     * Checks if the field on the right contains a puyo with the same color, in that case the method is called again with an altered x parameter
     * @param x
     * @param y
     */
    public void chainR(int x, int y){
        if(x<this.x-1){
            if(map[x+1][y].getClass()==Puyo.class&&map[x][y].getClass()==Puyo.class&&((Puyo)map[x+1][y]).getColor()==((Puyo)map[x][y]).getColor()&&map[x+1][y]!=map[x][y]){
                this.toCheck.add((Puyo)map[x+1][y]);
                chainR(x+1,y);
            }
        }
    }

    /**
     * Checks if the field below contains a puyo with the same color, in that case the method is called again with an altered y parameter
     * @param x
     * @param y
     */
    public void chainD(int x, int y){
        if(y<this.y-1){
            if(map[x][y+1].getClass()==Puyo.class&&map[x][y].getClass()==Puyo.class&&((Puyo)map[x][y+1]).getColor()==((Puyo)map[x][y]).getColor()&&map[x][y+1]!=map[x][y]){
                this.toCheck.add((Puyo)map[x][y+1]);
                chainD(x,y+1);
            }
        }
    }

    /**
     * Checks if the field above contains a puyo with the same color, in that case the method is called again with an altered y parameter
     * @param x
     * @param y
     */
    public void chainU(int x, int y){
        if(y>0){
            if(map[x][y-1].getClass()==Puyo.class&&map[x][y].getClass()==Puyo.class&&((Puyo)map[x][y-1]).getColor()==((Puyo)map[x][y]).getColor()&&map[x][y-1]!=map[x][y]){
                this.toCheck.add((Puyo)map[x][y-1]);
                chainU(x,y-1);
            }
        }
    }

    public Field[][] getMap() {
        return map;
    }

    //Setter and Getter Methods

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

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public boolean isLoose() {
        return loose;
    }

    public void setLoose(boolean loose) {
        this.loose = loose;
    }

    public PuyoPair getNext() {
        return next;
    }

    public void setNext(PuyoPair next) {
        this.next = next;
    }
}
