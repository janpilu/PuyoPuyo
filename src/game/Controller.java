package game;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable, EventHandler<KeyEvent> {

    @FXML
    private GridPane root;
    private Model m;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        m = new Model();
        initFileds();
        m.newPuyoPair();
        redraw();
    }

    private void initFileds() {
        Field[][] map = new Field[this.m.getX()][this.m.getY()];
        Field temp;
        for (int x = 0; x < this.m.getX(); x++){
            for (int y = 0; y < this.m.getY(); y++){
                temp = new Field(x,y);
                this.root.add(temp,x,y);
                map[x][y] = temp;
                this.m.setMap(map);
            }
        }
    }

    public void redraw(){
        root.getChildren().clear();
        for (int x = 0; x < this.m.getX(); x++){
            for (int y = 0; y < this.m.getY(); y++){
                Field temp = this.m.getMap()[x][y];
                this.root.add(temp,x,y);
            }
        }
    }

    @Override
    public void handle(KeyEvent event) {
        switch (event.getCode()){
            case LEFT:
                if(m.checkLeft()) {
                    m.move(-1, 0);
                    redraw();
                }
                break;
            case RIGHT:
                if(m.checkRight()) {
                    m.move(1, 0);
                    redraw();
                }
                break;
            case DOWN:
                m.move(0,1);
                redraw();
                break;
            case E:
                m.turn(false);
                redraw();
                break;
            case R:
                m.turn(true);
                redraw();
                break;
        }
        this.m.checkBelow();
    }
}
