package game;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

public class Controller implements Initializable, EventHandler<KeyEvent> {

    @FXML
    private GridPane root;
    private Model m;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        m = new Model();
        initFileds();
        m.newPuyoPair();
        m.drop();
        refresh();
        redraw();
    }

    public void refresh(){
        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                Runnable updater = new Runnable() {

                    @Override
                    public void run() {
                        redraw();
                    }
                };

                while (true) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException ex) {
                    }

                    // UI update is run on the Application thread
                    Platform.runLater(updater);
                }
            }

        });
        // don't let thread prevent JVM shutdown
        thread.setDaemon(true);
        thread.start();
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
        this.m.checkBelow();
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
                if(this.m.getCurPuyo().getPuyo1().getY()>1) {
                    m.turn(false);
                    redraw();
                }
                break;
            case R:
                if(this.m.getCurPuyo().getPuyo1().getY()>1) {
                    m.turn(true);
                    redraw();
                }
                break;
        }
        //this.m.checkBelow();
    }
}
