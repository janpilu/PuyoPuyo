package game;

import org.junit.Test;

import static org.junit.Assert.*;

public class ModelTest {

    Model m;

    @org.junit.Before
    public void setUp() throws Exception {
        m = new Model();
        m.newPuyoPair();
    }

    @Test
    public void TestMovingLeftNeighbour(){
        Field[][] temp = m.getMap();
        temp[0][m.getY()-1] = new Puyo(0,m.getY()-1,1);
        m.setMap(temp);

        m.getCurPuyo().getPuyo1().setX(1);
        m.getCurPuyo().getPuyo1().setY(m.getY()-1);
        m.getCurPuyo().getPuyo2().setX(2);
        m.getCurPuyo().getPuyo2().setY(m.getY()-1);

        m.move(-1,0);

        assertEquals(1,m.getCurPuyo().getPuyo1().getX());
    }

    @Test
    public void TestMovingLeftNoNeighbour(){

    }

    @Test
    public void TestMovingLeftWall(){

    }

    @org.junit.After
    public void tearDown() throws Exception {
    }
}